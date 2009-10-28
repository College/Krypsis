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

package org.krypsis.blackberry.db;

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;

import java.util.Enumeration;
import java.util.Vector;

public class SettingsStore {
  static Vector settings;
  static PersistentObject persist;

  static {
    long KEY = 220593665L;
    persist = PersistentStore.getPersistentObject(KEY);
    settings = (Vector) persist.getContents();
    if (settings == null) {
      settings = new Vector();
      persist.setContents(settings);
      persist.commit();
    }
  }

  /**
   * Adds a new setting instance to the storage
   *
   * @param setting The setting instance to add
   */
  public static void add(Setting setting) {
    settings.addElement(setting);
    persist.commit();
  }

  /**
   * Removes the given settings instance from storage.
   *
   * @param setting The setting to move
   * @return True if the setting was remove successfully
   */
  public static boolean remove(Setting setting) {
    final boolean result = settings.removeElement(setting);
    if (result) {
      persist.commit();
    }
    return result;
  }

  /**
   * Returns the value that is associated with the given key.
   *
   * @param key The key to retrieve the value for.
   * @return The value as object reference or null if no setting
   * was associated with the given key.
   */
  public static Object getValueByKey(String key) {
    final Setting setting = getByKey(key);
    return setting != null ? setting.getValue() : null;
  }

  /**
   * Returns the settings instance that is associated with
   * the given key.
   *
   * @param key The key to retrieve the setting for.
   * @return The setting instance or null
   */
  public static Setting getByKey(String key) {
      Enumeration elements = settings.elements();
    while (elements.hasMoreElements()) {
      Setting setting = (Setting) elements.nextElement();
      if (key.equals(setting.getKey())) {
        return setting;
      }
    }
    return null;
  }
}
