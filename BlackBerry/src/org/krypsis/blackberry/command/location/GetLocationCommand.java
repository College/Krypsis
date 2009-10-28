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

package org.krypsis.blackberry.command.location;

import org.krypsis.http.request.Requestable;
import org.krypsis.http.response.ErrorResponse;
import org.krypsis.http.response.Response;
import org.krypsis.http.response.ResponseDispatchable;
import org.krypsis.http.response.SuccessResponse;
import org.krypsis.log.Logger;
import org.krypsis.log.LoggerFactory;
import org.krypsis.module.Command;
import org.krypsis.module.Module;
import org.krypsis.blackberry.command.location.LocationUtils;

import javax.microedition.location.Criteria;
import javax.microedition.location.Location;
import javax.microedition.location.LocationProvider;

/**
 * Date: 12.05.2009
 */
public class GetLocationCommand implements Command {
  

  private Logger logger = LoggerFactory.getLogger(GetLocationCommand.class);

  public void execute(Module module, Requestable request, ResponseDispatchable dispatchable) {
    Response response;

    try {
      Criteria criteria = new Criteria();
      criteria.setCostAllowed(false);

      final LocationProvider locationProvider = LocationProvider.getInstance(criteria);

      if (locationProvider == null) {
        throw new Exception("Could not get location provider");
      }

      final Location location = locationProvider.getLocation(-1);

      if (location == null) {
        logger.error("Could not get single location");
        response = new ErrorResponse(request, ErrorResponse.TIMEOUT);
      }
      else {
        response = new SuccessResponse(request);
        LocationUtils.copyLocationToResponse(response, location);
      }

      locationProvider.reset();
    }
    catch (Exception e) {
      logger.error("Exception in getting current location: " + e);
      response = new ErrorResponse(request, ErrorResponse.UNKNOWN, e.getMessage());
    }

    dispatchable.dispatch(response);
  }
}
