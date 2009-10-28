/*
 * Krypsis - Write web applications based on proved technologies
 * like HTML, JavaScript, CSS... and access functionality of your
 * smartphone in a platform independent way.
 *
 * Copyright (C) 2008 - 2009 krypsis.org (have.a.go@krypsis.org)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

package org.krypsis.blackberry.command.screen;

import org.krypsis.http.request.Requestable;
import org.krypsis.http.response.ResponseDispatchable;
import org.krypsis.log.Logger;
import org.krypsis.log.LoggerFactory;
import org.krypsis.module.Command;
import org.krypsis.module.Module;
import org.krypsis.command.Screens;

/**
 * Date: 13.05.2009
 */
public class StartOrientationListenerCommand implements Command {
  private Logger logger = LoggerFactory.getLogger(StartOrientationListenerCommand.class);

  public void execute(Module module, Requestable request, ResponseDispatchable dispatchable) {
    OrientationListener listener = (OrientationListener) module.get(Screens.ORIENTATION_LISTENER);

    if (listener != null) {
      logger.error("Could not start orientation listener. Already running. Stop it first");
    }
    else {
      listener = new OrientationListener(request, dispatchable);
      listener.start();

      module.addModuleListener(listener);
      module.put(Screens.ORIENTATION_LISTENER, listener);
      
      logger.info("Orientation listener started...");
    }
  }
}
