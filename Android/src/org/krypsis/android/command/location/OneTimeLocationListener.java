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

package org.krypsis.android.command.location;

import android.location.Location;
import android.location.LocationManager;
import org.krypsis.http.request.Requestable;
import org.krypsis.http.response.ResponseDispatchable;
import org.krypsis.log.Logger;
import org.krypsis.log.LoggerFactory;
import org.krypsis.module.Module;

public class OneTimeLocationListener extends LocationChangedListener {
  private final static Logger logger = LoggerFactory.getLogger(OneTimeLocationListener.class);

  public OneTimeLocationListener(LocationManager manager, Module module, Requestable request, ResponseDispatchable dispatchable) {
    super(manager, module, request, dispatchable);
  }

  /**
   * Receives a location from the location provider. If the location is
   * valid, then dispatch the location coordinates and destroy this listener
   *
   * @param location The location
   */
  public void onLocationChanged(Location location) {
    super.onLocationChanged(location);

    if (location != null) {
      logger.info("One time location received: " + location);
      getModule().removeModuleListener(this, true);
    }
    else {
      logger.warn("Listener will not be destroyed until a valid location arrives...");
    }
  }
}