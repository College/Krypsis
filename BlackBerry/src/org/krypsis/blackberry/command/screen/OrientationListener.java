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

import net.rim.device.api.system.Display;
import org.krypsis.http.request.Requestable;
import org.krypsis.http.response.Response;
import org.krypsis.http.response.ResponseDispatchable;
import org.krypsis.http.response.SuccessResponse;
import org.krypsis.log.Logger;
import org.krypsis.log.LoggerFactory;
import org.krypsis.module.Module;
import org.krypsis.module.ModuleListener;
import org.krypsis.command.Screens;

/**
 * Date: 14.04.2009
 */
public class OrientationListener extends Thread implements ModuleListener {
  private Logger logger = LoggerFactory.getLogger(OrientationListener.class);
  private final Requestable request;
  private final ResponseDispatchable responseDispatcher;
  private long delay;
  private int orientation;
  private boolean running = true;

  public OrientationListener(Requestable request, ResponseDispatchable dispatcher) {
    this(request, dispatcher, 1000);
  }

  public OrientationListener(Requestable request, ResponseDispatchable responseDispatcher, long delay) {
    this.request = request;
    this.responseDispatcher = responseDispatcher;
    this.delay = delay;
  }

  public void run() {
    try {
      while (running) {
        final int newOrientation = getOrientation();
        if (orientation != newOrientation) {
          final Response response = new SuccessResponse(getRequest());
          final Integer degrees = new Integer(newOrientation == Display.ORIENTATION_LANDSCAPE ? 90 : 0);
          response.setParameter(Screens.ORIENTATION, degrees);
          getDispatcher().dispatch(response);
          orientation = newOrientation;
        }
        Thread.sleep(delay);
      }
    }
    catch (InterruptedException e) {
      logger.warn("OrientationListener interrupted");
    }
  }

  public void onDestroy(Module module) {
    running = false;
  }

  public Requestable getRequest() {
    return request;
  }

  public ResponseDispatchable getDispatcher() {
    return responseDispatcher;
  }

  public int getOrientation() {
    return Display.getOrientation();
  }
}
