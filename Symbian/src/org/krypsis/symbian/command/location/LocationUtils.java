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

import org.krypsis.http.response.Response;
import org.krypsis.command.Locations;

import javax.microedition.location.QualifiedCoordinates;
import javax.microedition.location.Location;

/**
 * Date: 22.05.2009
 */
public class LocationUtils {
  public static void copyLocationToResponse(Response response, Location location) {
    final QualifiedCoordinates coordinates = location.getQualifiedCoordinates();
    final float accuracy = (coordinates.getHorizontalAccuracy() + coordinates.getVerticalAccuracy()) / 2;

    response.setParameter(Locations.ACCURACY, new Float(accuracy));
    response.setParameter(Locations.ALTITUDE, new Double(coordinates.getAltitude()));
    response.setParameter(Locations.LATITUDE, new Double(coordinates.getLatitude()));
    response.setParameter(Locations.LONGITUDE, new Double(coordinates.getLongitude()));
    response.setParameter(Locations.TIMESTAMP, new Long(location.getTimestamp()));
  }
}