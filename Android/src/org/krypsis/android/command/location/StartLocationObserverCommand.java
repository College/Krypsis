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

import org.krypsis.module.Command;
import org.krypsis.module.Module;
import org.krypsis.http.request.Requestable;
import org.krypsis.http.request.RequestException;
import org.krypsis.http.response.ResponseDispatchable;
import org.krypsis.http.response.ErrorResponse;
import org.krypsis.log.LoggerFactory;
import org.krypsis.log.Logger;
import org.krypsis.android.Application;
import org.krypsis.command.Locations;
import android.location.Criteria;
import android.location.LocationManager;
import android.content.Context;

public class StartLocationObserverCommand implements Command {
  public static final String LOCATION_LISTENER = "locationlistener";
  private final Logger logger = LoggerFactory.getLogger(StartLocationObserverCommand.class);

  public void execute(Module module, Requestable request, ResponseDispatchable dispatchable) {
    try {
      if (module.get(LOCATION_LISTENER) != null) {
        throw new RequestException(ErrorResponse.OBSERVER_STARTED);
      }

      final Application application = (Application) module.getRootApplication();
      final LocationManager manager = (LocationManager) application.getSystemService(Context.LOCATION_SERVICE);

      if (manager == null) {
        throw new RequestException(ErrorResponse.CAPABILITY_NOT_SUPPORTED);
      }

      final Criteria criteria = new Criteria();
      criteria.setCostAllowed(false);

      final String providerName = manager.getBestProvider(criteria, true);

      String intervalString = request.getParameter(Locations.INTERVAL);
      String accuracyString = request.getParameter(Locations.ACCURACY);

      final Long interval = (intervalString == null ? 10000 : Long.parseLong(intervalString)) / 1000;
      final Float accuracy = accuracyString == null ? 200 : Float.parseFloat(accuracyString);

      logger.debug("Start observe location with interval: " + interval + " and accuracy: " + accuracy);

      final LocationChangedListener listener = new LocationChangedListener(manager, module, request, dispatchable);
      module.addModuleListener(listener);
      module.put(LOCATION_LISTENER, listener);

      manager.requestLocationUpdates(providerName, interval, accuracy, listener);
    }
    catch (RequestException e) {
      logger.error(e.getMessage());
      dispatchable.dispatch(new ErrorResponse(request, e.getReason()));
    }
    catch (Exception e) {
      logger.error("Exception in StartLocationObserverCommand: " + e.getMessage());
      dispatchable.dispatch(new ErrorResponse(request, ErrorResponse.UNKNOWN));
    }
  }
}
