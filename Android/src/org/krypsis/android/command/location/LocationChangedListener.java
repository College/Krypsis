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
import android.os.Bundle;
import org.krypsis.http.request.Requestable;
import org.krypsis.http.response.Response;
import org.krypsis.http.response.ResponseDispatchable;
import org.krypsis.http.response.SuccessResponse;
import org.krypsis.module.Module;

public class LocationChangedListener extends AbstractLocationListener {

  public LocationChangedListener(LocationManager manager, Module module, Requestable request, ResponseDispatchable dispatchable) {
    super(manager, module, request, dispatchable);
  }

  public void onStatusChanged(String provider, int status, Bundle bundle) {
    logger.debug("status changed to " + status);
  }

  public void onProviderEnabled(String provider) {
    logger.debug("Provider " + provider + " enabled");
  }

  public void onProviderDisabled(String provider) {
    logger.debug("Provider " + provider + " disabled");
  }

  public void onLocationChanged(Location location) {
    if (location != null) {
      logger.debug("Received location : " + location);
      final Response response = new SuccessResponse(getRequest());
      LocationUtils.copyLocationToResponse(response, location);
      getDispatcher().dispatch(response);
    }
    else {
      logger.warn("onLocationChanged called, but location is null");
    }
  }
}
