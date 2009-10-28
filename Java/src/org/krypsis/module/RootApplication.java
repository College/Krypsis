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

package org.krypsis.module;

/**
 *
 * The root application is the instance that
 * holds a reference on the modules. In most cases it will be
 * the entry point of the mobile application and usually
 * it is a singleton.
 */
public interface RootApplication {

  /**
   * Puts a value into the global scope of an application.
   *
   * @param key The key to associate with the object
   * @param value The object to save.
   *
   *
   * @throws IllegalArgumentException If the key is already used and a
   * ssociated with another object.
   */
  public void put(String key, Object value) throws IllegalArgumentException;

  /**
   * Returns the object that is associated with the given key.
   * If key not found null is returned.
   *
   * @param key The key that is associated with the object.
   * @return The object or null
   */
  public Object get(String key);

  /**
   * Removes the object that is associated with the given
   * key from the global scope. The associated Object is returned.
   * If key is not known, then null is returened
   *
   * @param key The key that is associated with the object.
   * @return The removed object or null
   */
  public Object remove(String key);

  /**
   * Resets the entire application. Stop all listeners and
   * reinitialize the commands.
   */
  public void reset();
}
