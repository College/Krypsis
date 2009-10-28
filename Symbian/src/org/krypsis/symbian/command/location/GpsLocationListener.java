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

package org.krypsis.symbian.command.location;

import org.krypsis.http.request.Requestable;
import org.krypsis.http.response.Response;
import org.krypsis.http.response.ResponseDispatchable;
import org.krypsis.http.response.SuccessResponse;
import org.krypsis.module.Module;
import org.krypsis.module.ModuleListener;

import javax.microedition.location.Location;
import javax.microedition.location.LocationListener;
import javax.microedition.location.LocationProvider;

/**
 * Date: 13.04.2009
 */
public class GpsLocationListener implements LocationListener, ModuleListener {
  private final ResponseDispatchable responseDispatcher;
  private final Requestable request;
  private LocationProvider locationProvider;

  public GpsLocationListener(ResponseDispatchable responseDispatcher, Requestable request) {
    this.responseDispatcher = responseDispatcher;
    this.request = request;
  }

  public void locationUpdated(LocationProvider locationProvider, Location location) {
    if (location.isValid()) {
      this.locationProvider = locationProvider;
      final Response response = new SuccessResponse(getRequest());
      LocationUtils.copyLocationToResponse(response, location);
      getResponseDispatcher().dispatch(response);
    }
  }

  public void providerStateChanged(LocationProvider locationProvider, int i) {
    this.locationProvider = locationProvider;
  }

  public ResponseDispatchable getResponseDispatcher() {
    return responseDispatcher;
  }

  public Requestable getRequest() {
    return request;
  }

  /**
   * If the module is about to be destroyed, then stop the location
   * listener and reset the provider.
   *
   * @param module The module that will be destroyed
   */
  public void onDestroy(Module module) {
    if (locationProvider != null) {
      locationProvider.setLocationListener(null, 0, 0, 0);
      locationProvider.reset();
      locationProvider = null;
    }
  }
}