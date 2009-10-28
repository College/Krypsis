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
import org.krypsis.command.Locations;

import javax.microedition.location.Criteria;
import javax.microedition.location.LocationListener;
import javax.microedition.location.LocationProvider;

/**
 * Date: 12.05.2009
 */
public class StartLocationObserverCommand implements Command {
  public static final String LOCATION_LISTENER = "locationlistener";
  
  private Logger logger = LoggerFactory.getLogger(StartLocationObserverCommand.class);

  public void execute(Module module, Requestable request, ResponseDispatchable dispatchable) {
    Response response;

    final LocationListener oldListener = (LocationListener) module.get(LOCATION_LISTENER);

    /**
     * If an old listener exists in the module scope, then
     * stop the execution and raise an error.
     */
    if (oldListener != null) {
      response = new ErrorResponse(request, ErrorResponse.OBSERVER_STARTED);
    }
    else {
      try {

        String intervalString = request.getParameter(Locations.INTERVAL);
        String accuracyString = request.getParameter(Locations.ACCURACY);


        final int interval = (intervalString == null ? 10000 : Integer.parseInt(intervalString)) / 1000;
        final int accuracy = accuracyString == null ? 200 : Integer.parseInt(accuracyString);

        logger.debug("Start observe location with interval: " + interval + " and accuracy: " + accuracy);

        final Criteria criteria = new Criteria();
        criteria.setCostAllowed(false);
        criteria.setVerticalAccuracy(accuracy);
        criteria.setHorizontalAccuracy(accuracy);

        final LocationProvider locationProvider = LocationProvider.getInstance(criteria);

        if (locationProvider == null) {
          throw new Exception("Could not get location provider");
        }

        final GpsLocationListener locationListener = new GpsLocationListener(dispatchable, request);
        module.addModuleListener(locationListener);
        locationProvider.setLocationListener(locationListener, interval, -1, -1);
        logger.info("Location observer started...");
        
        module.put(LOCATION_LISTENER, locationListener);

        response = new SuccessResponse(request);
      }
      catch (Exception e) {
        logger.error("Exception in starting location observer: " + e);
        response = new ErrorResponse(request, ErrorResponse.UNKNOWN, e.getMessage());
      }
    }

    dispatchable.dispatch(response);
  }
}
