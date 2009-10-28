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

package org.krypsis.symbian.command.screen;

import org.krypsis.module.Command;
import org.krypsis.module.Module;
import org.krypsis.module.ModuleListener;
import org.krypsis.http.request.Requestable;
import org.krypsis.http.request.RequestException;
import org.krypsis.http.response.ResponseDispatchable;
import org.krypsis.http.response.ErrorResponse;
import org.krypsis.command.Screens;
import org.krypsis.log.LoggerFactory;
import org.krypsis.log.Logger;
import org.eclipse.ercp.swt.mobile.MobileDevice;
import org.eclipse.ercp.swt.mobile.Screen;

/**
 * Date: 30.05.2009
 *
 * Observes the orientation of the display. This command
 * creates a screen listener that will be notified whenever
 * the device is flipped. 
 */
public class StartObserveOrientationCommand implements Command {
  private final Logger logger = LoggerFactory.getLogger(StartObserveOrientationCommand.class);

  public void execute(Module module, Requestable request, ResponseDispatchable dispatchable) {
    try {
      ModuleListener listener = (ModuleListener) module.get(Screens.ORIENTATION_LISTENER);

      if (listener == null) {
        final MobileDevice mobileDevice = MobileDevice.getMobileDevice();
        final Screen[] screens = mobileDevice != null ? mobileDevice.getScreens() : null;

        if (screens == null || screens.length == 0) {
          logger.error("Could not get mobile device or active screen");
          throw new RequestException(ErrorResponse.CAPABILITY_NOT_SUPPORTED);
        }

        logger.info("Orientation Listener started");
        listener = new OrientationListener(screens[0], dispatchable, request);
        module.put(Screens.ORIENTATION_LISTENER, listener);
        module.addModuleListener(listener);
      }
      else {
        logger.warn("Orientation listener is already active");
        throw new RequestException(ErrorResponse.OBSERVER_STARTED);
      }
    }
    catch (RequestException re) {
      dispatchable.dispatch(new ErrorResponse(request, re.getReason()));
    }
    catch (Exception e) {
      logger.error("An unknown error occured: " + e);
      dispatchable.dispatch(new ErrorResponse(request, ErrorResponse.UNKNOWN));
    }
  }
}
