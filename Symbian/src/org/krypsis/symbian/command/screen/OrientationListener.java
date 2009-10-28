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

import org.eclipse.ercp.swt.mobile.Screen;
import org.eclipse.ercp.swt.mobile.ScreenEvent;
import org.eclipse.ercp.swt.mobile.ScreenListener;
import org.krypsis.command.Screens;
import org.krypsis.http.request.Requestable;
import org.krypsis.http.response.Response;
import org.krypsis.http.response.ResponseDispatchable;
import org.krypsis.http.response.SuccessResponse;
import org.krypsis.log.Logger;
import org.krypsis.log.LoggerFactory;
import org.krypsis.module.Module;
import org.krypsis.module.ModuleListener;

/**
 * Date: 30.05.2009
 */
public class OrientationListener implements ModuleListener, ScreenListener {
  private final Logger logger = LoggerFactory.getLogger(OrientationListener.class);
  private final Screen screen;
  private final Requestable request;
  private final ResponseDispatchable dispatchable;
  private int orientation = -1;

  public OrientationListener(Screen screen, ResponseDispatchable dispatchable, Requestable request) {
    this.dispatchable = dispatchable;
    this.request = request;
    this.screen = screen;
    screen.addEventListener(this);
  }

  public void onDestroy(Module module) {
    getScreen().removeEventListener(this);
  }

  public void screenActivated(ScreenEvent screenEvent) {
    logger.debug("Screen is activated: " + screenEvent);
  }

  public void screenDeactivated(ScreenEvent screenEvent) {
    logger.debug("Screen is deactivated: " + screenEvent);
  }

  public void screenOrientationChanged(ScreenEvent screenEvent) {
    if (getOrientation() != screenEvent.orientation) {
      setOrientation(screenEvent.orientation);
      final Response response = new SuccessResponse(getRequest());
      response.setParameter(Screens.ORIENTATION, new Integer(getOrientation() == Screen.PORTRAIT ? 0 : 90));
      getDispatchable().dispatch(response);
      logger.debug("Orientation changed to " + screenEvent.orientation);
    }
  }

  public Screen getScreen() {
    return screen;
  }

  public int getOrientation() {
    return orientation;
  }

  public void setOrientation(int orientation) {
    this.orientation = orientation;
  }

  public Requestable getRequest() {
    return request;
  }

  public ResponseDispatchable getDispatchable() {
    return dispatchable;
  }
}
