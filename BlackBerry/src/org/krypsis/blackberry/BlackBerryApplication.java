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

package org.krypsis.blackberry;

import net.rim.blackberry.api.browser.Browser;
import net.rim.blackberry.api.browser.BrowserSession;
import org.krypsis.blackberry.command.base.GetMetaDataCommand;
import org.krypsis.blackberry.command.base.LoadCommand;
import org.krypsis.blackberry.command.base.ReloadCommand;
import org.krypsis.blackberry.command.base.ResetCommand;
import org.krypsis.blackberry.command.location.GetLocationCommand;
import org.krypsis.blackberry.command.location.StartLocationObserverCommand;
import org.krypsis.blackberry.command.location.StopLocationObserverCommand;
import org.krypsis.blackberry.command.photo.TakeAndUploadCommand;
import org.krypsis.blackberry.command.screen.GetScreenInfoCommand;
import org.krypsis.blackberry.command.screen.StartOrientationListenerCommand;
import org.krypsis.blackberry.command.screen.StopOrientationListenerCommand;
import org.krypsis.blackberry.db.Setting;
import org.krypsis.blackberry.db.SettingsStore;
import org.krypsis.command.Bases;
import org.krypsis.command.Locations;
import org.krypsis.command.Photos;
import org.krypsis.command.Screens;
import org.krypsis.log.Logger;
import org.krypsis.log.LoggerFactory;
import org.krypsis.module.Module;
import org.krypsis.module.ModuleContext;
import org.krypsis.module.ModuleRegistry;
import org.krypsis.module.RootApplication;
import org.krypsis.util.Identity;

import java.util.Hashtable;

/**
 * Date: 13.04.2009
 *
 * The allocator class is a singleton that provides
 * access to listeners, modules and is used to exchange
 * information between modules. 
 */
public class BlackBerryApplication implements RootApplication {
  private final Logger logger = LoggerFactory.getLogger(BlackBerryApplication.class);
  private final static Hashtable scope = new Hashtable();
  private BrowserSession browserSession;
  private static BlackBerryApplication blackBerryApplication = new BlackBerryApplication();

  private BlackBerryApplication() {
    final ModuleRegistry moduleRegistry = ModuleRegistry.getInstance();
    moduleRegistry.clear();

    final Module baseModule     = new Module(this, ModuleContext.BASE);
    baseModule.registerCommand(Bases.GET_META_DATA, GetMetaDataCommand.class);
    baseModule.registerCommand(Bases.LOAD, LoadCommand.class);
    baseModule.registerCommand(Bases.RELOAD, ReloadCommand.class);
    baseModule.registerCommand(Bases.RESET, ResetCommand.class);

    final Module locationModule = new Module(this, ModuleContext.LOCATION);
    locationModule.registerCommand(Locations.GET_LOCATION, GetLocationCommand.class);
    locationModule.registerCommand(Locations.START_OBSERVE_LOCATION, StartLocationObserverCommand.class);
    locationModule.registerCommand(Locations.STOP_OBSERVE_LOCATION, StopLocationObserverCommand.class);

    final Module screenModule   = new Module(this, ModuleContext.SCREEN);
    screenModule.registerCommand(Screens.GET_INFO, GetScreenInfoCommand.class);
    screenModule.registerCommand(Screens.START_OBSERVE_ORIENTATION, StartOrientationListenerCommand.class);
    screenModule.registerCommand(Screens.STOP_OBSERVE_ORIENTATION, StopOrientationListenerCommand.class);

    final Module photoModule    = new Module(this, ModuleContext.PHOTO);
    photoModule.registerCommand(Photos.TAKE_AND_UPLOAD, TakeAndUploadCommand.class);

    moduleRegistry.register(baseModule);
    moduleRegistry.register(locationModule);
    moduleRegistry.register(screenModule);
    moduleRegistry.register(photoModule);
  }

  public static BlackBerryApplication getInstance() {
    return blackBerryApplication;
  }

  public void put(String key, Object value) throws IllegalArgumentException {
    if (scope.containsKey(key)) {
      throw new IllegalArgumentException("The key " + key + " is already present in the global scope.");
    }
    logger.debug("Global Scope: " + key + " => " + value);
    scope.put(key, value);
  }

  public Object get(String key) {
    return scope.containsKey(key) ? scope.get(key) : null;
  }

  public Object remove(String key) {
    logger.debug("Global Scope: remove key: " + key);
    return scope.remove(key);
  }

  public void reset() {
    ModuleRegistry.getInstance().reset();
  }

  public BrowserSession getBrowserSession() {
    if (browserSession == null) {
      browserSession = Browser.getDefaultSession();
    }
    return browserSession;
  }

  /**
   * @return Returns the unique Krypsis ID for this device.
   */
  public String getKrypsisId() {
    String krypsisId = (String) SettingsStore.getValueByKey(Bases.KRYPSIS_ID);
    if (krypsisId == null) {
      krypsisId = Identity.getInstance().generate();
      final Setting setting = new Setting(Bases.KRYPSIS_ID, krypsisId);
      SettingsStore.add(setting);
    }
    return krypsisId;
  }
}
