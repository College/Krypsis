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

import android.location.LocationListener;
import android.location.LocationManager;
import org.krypsis.http.request.Requestable;
import org.krypsis.http.response.ResponseDispatchable;
import org.krypsis.log.Logger;
import org.krypsis.log.LoggerFactory;
import org.krypsis.module.Module;
import org.krypsis.module.ModuleListener;

public abstract class AbstractLocationListener implements LocationListener, ModuleListener {
  protected Logger logger = LoggerFactory.getLogger(LocationChangedListener.class);
  private final ResponseDispatchable dispatcher;
  private final Requestable request;
  private final Module module;
  private final LocationManager manager;

  public AbstractLocationListener(LocationManager manager, Module module, Requestable request, ResponseDispatchable dispatcher) {
    this.manager = manager;
    this.dispatcher = dispatcher;
    this.request = request;
    this.module = module;
  }

  /**
   * If module is about to be destroyed, then unregister
   * this listener and free associated resources
   * 
   * @param module The module that will be destroyed
   */
  public void onDestroy(Module module) {
    logger.info("unregister location listener");
    getManager().removeUpdates(this);
  }

  public ResponseDispatchable getDispatcher() {
    return dispatcher;
  }

  public Requestable getRequest() {
    return request;
  }

  public Module getModule() {
    return module;
  }

  public LocationManager getManager() {
    return manager;
  }
}
