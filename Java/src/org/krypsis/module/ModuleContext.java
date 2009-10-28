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

import java.util.Hashtable;

/**
 * This class describes and defines the supported
 * modules by defining an unique identifier for
 * each of the modules.
 */
public class ModuleContext {
  private final String name;

  private ModuleContext(String name) {
    this.name = name;
    CONTEXTS.put(name.toLowerCase(), this);
  }

  public String getName() {
    return name;
  }

  public static ModuleContext getContextByName(String name) {
    name = name.toLowerCase();
    return CONTEXTS.containsKey(name) ? (ModuleContext) CONTEXTS.get(name) : null;
  }

  public static Hashtable getDefinedContexts() {
    return CONTEXTS;
  }

  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ModuleContext that = (ModuleContext) o;

    if (name != null ? !name.equals(that.name) : that.name != null) {
      return false;
    }

    return true;
  }

  public int hashCode() {
    return name != null ? name.hashCode() : 0;
  }

  public String toString() {
    return "ModuleContext:" + name;
  }

  private static final Hashtable CONTEXTS = new Hashtable();

  public static final ModuleContext BASE     = new ModuleContext("Base");
  public static final ModuleContext PHOTO    = new ModuleContext("Photo");
  public static final ModuleContext LOCATION = new ModuleContext("Location");
  public static final ModuleContext SCREEN   = new ModuleContext("Screen");
  public static final ModuleContext QUEUE    = new ModuleContext("Queue");
  public static final ModuleContext PHONE    = new ModuleContext("Phone");
}
