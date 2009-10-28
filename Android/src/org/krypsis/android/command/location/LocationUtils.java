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

import org.krypsis.http.response.Response;
import org.krypsis.command.Locations;
import android.location.Location;

public class LocationUtils {

  /**
   * Copy the location information into the response.
   *
   * @param response The response to copy the location data to
   * @param location The location to copy the data from
   */
  public static void copyLocationToResponse(Response response, Location location) {
    response.setParameter(Locations.ACCURACY,  location.getAccuracy());
    response.setParameter(Locations.ALTITUDE,  location.getAltitude());
    response.setParameter(Locations.LATITUDE,  location.getLatitude());
    response.setParameter(Locations.LONGITUDE, location.getLongitude());
    response.setParameter(Locations.TIMESTAMP, location.getTime());
  }
}
