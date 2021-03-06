/*
 * SessionProjectsInternal.hpp
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

#ifndef SESSION_PROJECTS_PROJECTS_INTERNAL_HPP
#define SESSION_PROJECTS_PROJECTS_INTERNAL_HPP

#include <string>

namespace core {
   class Error;
   class FilePath;
}
 
namespace session {
namespace projects {

void startup();

core::Error initialize();

core::Error computeScratchPath(const core::FilePath& projectFile,
                               core::FilePath* pScratchPath);

} // namespace projects
} // namesapce session

#endif // SESSION_PROJECTS_PROJECTS_INTERNAL_HPP
