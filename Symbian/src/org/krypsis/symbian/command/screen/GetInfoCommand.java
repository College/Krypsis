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

import org.eclipse.ercp.swt.mobile.MobileDevice;
import org.eclipse.ercp.swt.mobile.Screen;
import org.eclipse.swt.graphics.Rectangle;
import org.krypsis.http.request.RequestException;
import org.krypsis.http.request.Requestable;
import org.krypsis.http.response.ErrorResponse;
import org.krypsis.http.response.ResponseDispatchable;
import org.krypsis.http.response.SuccessResponse;
import org.krypsis.http.response.Response;
import org.krypsis.log.Logger;
import org.krypsis.log.LoggerFactory;
import org.krypsis.module.Command;
import org.krypsis.module.Module;
import org.krypsis.command.Screens;

/**
 * Date: 29.05.2009
 * <p/>
 * Gets the display resolution
 */
public class GetInfoCommand implements Command {
  private Logger logger = LoggerFactory.getLogger(GetInfoCommand.class);

  public void execute(Module module, Requestable request, ResponseDispatchable dispatchable) {
    try {
      final MobileDevice mobileDevice = MobileDevice.getMobileDevice();
      final Screen[] screens = mobileDevice != null ? mobileDevice.getScreens() : null;

      if (screens == null || screens.length == 0) {
        logger.warn("No mobile device or screens available.");
        throw new RequestException(ErrorResponse.CAPABILITY_NOT_SUPPORTED);
      }
      else if (screens.length > 1) {
        logger.warn("Multiple screens are available for this mobile device: " + screens.length);
      }

      final Screen screen = screens[0];
      final Rectangle bounds = screen.getBounds();

      final Response response = new SuccessResponse(request);
      response.setParameter(Screens.WIDTH, new Integer(bounds.width));
      response.setParameter(Screens.HEIGHT, new Integer(bounds.height));
      final int orientation = screen.getOrientation() == Screen.PORTRAIT ? 0 : 90;
      response.setParameter(Screens.ORIENTATION, new Integer(orientation));
      dispatchable.dispatch(response);
    }
    catch (RequestException e) {
      dispatchable.dispatch(new ErrorResponse(request, e.getReason()));
    }
    catch (Exception e) {
      logger.error("Unknown error occurd: " + e.toString());
    }
  }
}
