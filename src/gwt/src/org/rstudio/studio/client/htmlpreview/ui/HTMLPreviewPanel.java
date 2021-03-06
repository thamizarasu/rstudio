package org.rstudio.studio.client.htmlpreview.ui;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.inject.Inject;

import org.rstudio.core.client.command.AppCommand;
import org.rstudio.core.client.dom.IFrameElementEx;
import org.rstudio.core.client.dom.WindowEx;
import org.rstudio.core.client.widget.CanFocus;
import org.rstudio.core.client.widget.FindTextBox;
import org.rstudio.core.client.widget.MessageDialog;
import org.rstudio.core.client.widget.Toolbar;
import org.rstudio.core.client.widget.ToolbarFileLabel;
import org.rstudio.studio.client.RStudioGinjector;
import org.rstudio.studio.client.htmlpreview.HTMLPreviewPresenter;
import org.rstudio.studio.client.workbench.commands.Commands;

public class HTMLPreviewPanel extends ResizeComposite
                              implements HTMLPreviewPresenter.Display
{
   @Inject
   public HTMLPreviewPanel(Commands commands)
   {
      LayoutPanel panel = new LayoutPanel();
      
      Toolbar toolbar = createToolbar(commands);
      int tbHeight = toolbar.getHeight();
      panel.add(toolbar);
      panel.setWidgetLeftRight(toolbar, 0, Unit.PX, 0, Unit.PX);
      panel.setWidgetTopHeight(toolbar, 0, Unit.PX, tbHeight, Unit.PX);
      
      previewFrame_ = new PreviewFrame();
      previewFrame_.setSize("100%", "100%");
      panel.add(previewFrame_);
      panel.setWidgetLeftRight(previewFrame_,  0, Unit.PX, 0, Unit.PX);
      panel.setWidgetTopBottom(previewFrame_, tbHeight+1, Unit.PX, 0, Unit.PX);
      
      initWidget(panel);
   }
   
   private Toolbar createToolbar(Commands commands)
   {
      Toolbar toolbar = new Toolbar();
      
      fileLabel_ = new ToolbarFileLabel(toolbar, 200);
      
      toolbar.addLeftSeparator();
      
      printHtmlPreview_ = commands.printHtmlPreview();
      toolbar.addLeftWidget(printHtmlPreview_.createToolbarButton());
      
      findTextBox_ = new FindTextBox("");
      findTextBox_.setIconVisible(true);
      findTextBox_.setOverrideWidth(120);
      toolbar.addRightWidget(findTextBox_);
      
      findTextBox_.addKeyDownHandler(new KeyDownHandler() {
         @Override
         public void onKeyDown(KeyDownEvent event)
         {
            // enter key triggers a find
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
            {
               event.preventDefault();
               event.stopPropagation();
               findInTopic(findTextBox_.getValue().trim(), findTextBox_);
               findTextBox_.focus();
            }
            else if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE)
            {
               findTextBox_.setValue("");
            }       
         }
         
         private void findInTopic(String term, CanFocus findInputSource)
         {
            // get content window
            WindowEx contentWindow = previewFrame_.getWindow();
            if (contentWindow == null)
               return;
                
            if (!contentWindow.find(term, false, false, true, false))
            {
               RStudioGinjector.INSTANCE.getGlobalDisplay().showMessage(
                     MessageDialog.INFO,
                     "Find in Page", 
                     "No occurences found",
                     findInputSource);
            }     
         }
         
      });
      
      return toolbar;
   }
   
   @Override
   public void showProgress(String caption)
   {
      closeProgress();
      activeProgressDialog_ = new HTMLPreviewProgressDialog(caption);
   }
   
   @Override
   public void setProgressCaption(String caption)
   {
      activeProgressDialog_.setCaption(caption);
   }
   
   @Override
   public HandlerRegistration addProgressClickHandler(ClickHandler handler)
   {
      return activeProgressDialog_.addClickHandler(handler);
   }
   
   @Override
   public void showProgressOutput(String output)
   {
      activeProgressDialog_.showOutput(output);
   }

   @Override
   public void stopProgress()
   {
      activeProgressDialog_.stopProgress();
   }
   
   @Override
   public void closeProgress()
   {
      if (activeProgressDialog_ != null)
      {
         activeProgressDialog_.dismiss();
         activeProgressDialog_ = null;
      }
   }
   
   @Override
   public void showPreview(String sourceFile,
                           String url, 
                           boolean enableScripts)
   {
      fileLabel_.setFileName(sourceFile);
      printHtmlPreview_.setVisible(!enableScripts);
      findTextBox_.setVisible(!enableScripts);
      previewFrame_.setScriptsEnabled(enableScripts);
      previewFrame_.navigate(url);
   }
   
   @Override
   public void print()
   {
      WindowEx window = previewFrame_.getWindow();
      window.focus();
      window.print();
   }
   
   private class PreviewFrame extends Frame
   {
      public PreviewFrame()
      {
         setStylePrimaryName("rstudio-HelpFrame");
         setScriptsEnabled(false);
      }
      
      public void setScriptsEnabled(boolean scriptsEnabled)
      {
         // enable scripts for the iframe sandbox if requested. note that
         // if we do allow scripts we need to make sure that same-origin
         // is not allowed (so the scripts are confined to this frame). 
         // however if scripts are not allowed we explicitly allow same-origin
         // so that find & print will work and so reloading will preserve 
         // scroll position. in both cases we allow popups for embedded links.
         // net tradeoff: if scripts are enabled then print, find, and 
         // preservation of scroll position after reload do not work
         if (scriptsEnabled)
         {
            getElement().setAttribute("sandbox", "allow-scripts " +
                                                 "allow-popups");
         }
         else
         {
            getElement().setAttribute("sandbox", "allow-same-origin " +
                                                 "allow-popups");
         }
      }
      
      public void navigate(final String url)
      {
         RepeatingCommand navigateCommand = new RepeatingCommand() {
            @Override
            public boolean execute()
            {
               if (getIFrame() != null && getWindow() != null)
               {
                  if (url.equals(getWindow().getLocationHref()))
                  {
                     getWindow().reload();
                  }
                  else
                  {
                     getWindow().replaceLocationHref(url);
                  }
                  return false;
               }
               else
               {
                  return true;
               }
            }
         };

         if (navigateCommand.execute())
            Scheduler.get().scheduleFixedDelay(navigateCommand, 50);      
      }
      
      private IFrameElementEx getIFrame()
      {
         return getElement().cast();
      }
      
      protected WindowEx getWindow()
      {
         return getIFrame().getContentWindow();
      }

   }
 
   private final PreviewFrame previewFrame_;
   private ToolbarFileLabel fileLabel_;
   private FindTextBox findTextBox_;
   private AppCommand printHtmlPreview_;
   private HTMLPreviewProgressDialog activeProgressDialog_;
}
