/*
 * Win32FileMonitor.cpp
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

#include <core/system/FileMonitor.hpp>

#include <core/FilePath.hpp>

#include <core/system/FileScanner.hpp>
#include <core/system/System.hpp>

#include "FileMonitorImpl.hpp"

namespace core {
namespace system {
namespace file_monitor {

namespace {



} // anonymous namespace

namespace detail {

// register a new file monitor
Handle registerMonitor(const core::FilePath& filePath,
                       const Callbacks& callbacks)
{
   return NULL;
}

// unregister a file monitor
void unregisterMonitor(Handle handle)
{

}

void run(const boost::function<void()>& checkForInput)
{

}

} // namespace detail
} // namespace file_monitor
} // namespace system
} // namespace core 

   


