/*
 * HTMLCapabilities.java
 *
 * Copyright (C) 2009-11 by RStudio, Inc.
 *
 * This program is licensed to you under the terms of version 3 of the
 * GNU Affero General Public License. This program is distributed WITHOUT
 * ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT,
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the
 * AGPL (http://www.gnu.org/licenses/agpl-3.0.txt) for more details.
 *
 */
package org.rstudio.studio.client.workbench.model;

import com.google.gwt.core.client.JavaScriptObject;

public class HTMLCapabilities extends JavaScriptObject
{
   protected HTMLCapabilities() {}
 
   public native final boolean isRHtmlSupported() /*-{
      return this.r_html_supported;
   }-*/;
   
   public native final String getRHtmlRequiredVersion() /*-{
      return this.r_html_version;
   }-*/;
   
   public native final boolean isRMarkdownSupported() /*-{
      return this.r_markdown_supported;
   }-*/;
   
   public native final String getRMarkdownRequiredVersion() /*-{
      return this.r_markdown_version;
   }-*/;
}
