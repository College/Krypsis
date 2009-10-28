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

import org.krypsis.module.Command;
import org.krypsis.module.Module;
import org.krypsis.http.request.Requestable;
import org.krypsis.http.request.RequestException;
import org.krypsis.http.response.ResponseDispatchable;
import org.krypsis.http.response.ErrorResponse;
import org.krypsis.http.response.SuccessResponse;
import org.krypsis.http.response.Response;
import org.krypsis.log.LoggerFactory;
import org.krypsis.log.Logger;

import javax.microedition.location.LocationProvider;
import javax.microedition.location.Criteria;
import javax.microedition.location.LocationException;
import javax.microedition.location.Location;

/**
 * Date: 30.05.2009
 *
 * Gets the current location if available
 */
public class GetLocationCommand implements Command {
  private final Logger logger = LoggerFactory.getLogger(GetLocationCommand.class);
  
  public void execute(Module module, Requestable request, ResponseDispatchable dispatchable) {
    try {
      final Criteria criteria = new Criteria();
      criteria.setCostAllowed(false);
      final LocationProvider provider = LocationProvider.getInstance(criteria);

      if (provider == null) {
        logger.warn("Could not get gps position. LocationProvider not found");
        throw new RequestException(ErrorResponse.CAPABILITY_NOT_SUPPORTED);
      }

      final Location location = provider.getLocation(-1);
      if (location == null) {
        logger.warn("Could not get the current location.");
        throw new RequestException(ErrorResponse.CAPABILITY_NOT_SUPPORTED);
      }

      final Response response = new SuccessResponse(request);
      LocationUtils.copyLocationToResponse(response, location);

      dispatchable.dispatch(response);
    }
    catch (RequestException re) {
      dispatchable.dispatch(new ErrorResponse(request, re.getReason()));
    }
    catch (LocationException le) {
      logger.error("Could not get location: " + le);
      dispatchable.dispatch(new ErrorResponse(request, ErrorResponse.CAPABILITY_NOT_SUPPORTED));
    }
    catch (Exception e) {
      logger.error("Unknown exception occured: " + e);
      dispatchable.dispatch(new ErrorResponse(request, ErrorResponse.UNKNOWN));
    }
  }
}
