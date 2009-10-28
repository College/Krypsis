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

import org.krypsis.log.Logger;
import org.krypsis.log.LoggerFactory;

import java.util.Enumeration;
import java.util.Hashtable;


/**
 * The module registry is a class with only static
 * members that provides an interface to register,
 * request and unregister modules
 */
public class ModuleRegistry {
  private final Logger logger = LoggerFactory.getLogger(ModuleRegistry.class);
  private final Hashtable modules;
  private static final ModuleRegistry instance = new ModuleRegistry();

  private ModuleRegistry() {
    modules = new Hashtable();
  }

  /**
   * @return Returns the instance of the module registry.
   */
  public static ModuleRegistry getInstance() {
    return instance;
  }

  /**
   * Register a new module that will be returned when a handler
   * for the given module context is requested, Make sure the given
   * class implements the Module interface. This will not be checked
   * in this method, but when the module should be instantiated.
   *
   * @param module The module to register
   */
  public synchronized void register(Module module) {
    if (module != null) {
      modules.put(module.getContext().getName(), module);
      module.create();
      logger.info("Module registered: " + module.getContext());
    }
  }

  /**
   * Returns a new instance of the module that is accociated with
   * the given module context. Makes sure the context is valid and
   * the module was previously registered with the register method.
   *
   * @param moduleContext The context of the requested module
   * @return A new module instance or null, if module context is unknown.
   */
  public synchronized Module getModule(ModuleContext moduleContext) {
    if (moduleContext == null) { return null; }
    
    final Module module = (Module) modules.get(moduleContext.getName());
    if (module == null) {
      logger.warn("The requested module with context " + moduleContext + " was not found.");
    }
    return module;
  }

  /**
   * Unregister the module class of the given module context.
   * @param module The module to unregister
   */
  public synchronized void unregister(Module module) {
    if (module != null) {
      final Module moduleToRemove = (Module) modules.remove(module.getContext().getName());
      if (moduleToRemove != null) {
        moduleToRemove.destroy();
        logger.info("Unregistered module for context: " + moduleToRemove.getContext());
      }
    }
  }

  /**
   * Calls on each module the reset function.
   */
  public void reset() {
    final Enumeration keys = modules.keys();
    while (keys.hasMoreElements()) {
      final Object key = keys.nextElement();
      final Module module = (Module) modules.get(key);
      module.reset();
    }
  }

  /**
   * Removes all registered modules
   */
  public synchronized void clear() {
    final Enumeration keys = modules.keys();
    while (keys.hasMoreElements()) {
      final Object key = keys.nextElement();
      final Module module = (Module) modules.get(key); 
      unregister(module);
    }
    modules.clear();
  }

  public Hashtable getModules() {
    return modules;
  }
}
