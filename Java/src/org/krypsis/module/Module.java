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

import org.krypsis.http.request.Requestable;
import org.krypsis.http.response.ErrorResponse;
import org.krypsis.http.response.ResponseDispatchable;
import org.krypsis.log.Logger;
import org.krypsis.log.LoggerFactory;

import java.util.Hashtable;
import java.util.Vector;

public class Module {
  private Logger logger = LoggerFactory.getLogger(Module.class);
  private final ModuleContext moduleContext;
  private final RootApplication rootApplication;
  private Hashtable commands;
  private Hashtable scope;
  private Vector moduleListeners;

  /**
   * Creates a new module for the given module context.
   *
   * @param rootApplication The instance that has instantiated this module
   * @param moduleContext The module context this module is responsible for.
   */
  public Module(RootApplication rootApplication, ModuleContext moduleContext) {
    this.rootApplication = rootApplication;
    this.moduleContext = moduleContext;

    this.moduleListeners = new Vector();
    this.commands = new Hashtable();
    this.scope = new Hashtable();
  }

  /**
   * @return The module context for this module.
   */
  public ModuleContext getContext() {
    return moduleContext;
  }

  /**
   * @return The instance to the parent application.
   */
  public RootApplication getRootApplication() {
    return rootApplication;
  }

  /**
   * Put an object into scope. Can be a listener or something similar.
   * 
   * @param key The key to link to object to
   * @param value The value to put into scope
   */
  public void put(String key, Object value) {
    if (scope.containsKey(key)) {
      throw new IllegalArgumentException("The key '" + key + "' is already used within the scope!");
    }

    logger.debug("Put " + key + " = " + value);
    scope.put(key, value);
  }

  /**
   * Removes an object from scope
   *
   * @param key The key that is associated with the object that should be
   * removed.
   *
   * @return The removed object or null
   */
  public Object remove(String key) {
    logger.debug("remove " + key);
    return scope.remove(key);
  }

  /**
   * @param key The key
   * @return The object that is assiciated with the key
   */
  public Object get(String key) {
    return scope.get(key);
  }

  /**
   * Register a command class that will be linked
   * to the given action name. Every time a request
   * should be dispatched with the name, the class will
   * be instantiated and will handle the request.
   *
   * @param action The action name to register the command class with
   * @param commandClass The class of the command.
   */
  public void registerCommand(String action, Class commandClass) {
    commands.put(action, commandClass);
  }
  
  /**
   * Handle the given request and dispatch the response by
   * using the given response dispatcher.
   *
   * @param request The request to process
   * @param responseDispatcher The dispatcher that is connected to the
   * caller and will deliver the response
   */
  public void handle(Requestable request, ResponseDispatchable responseDispatcher) {
    
    if (request == null || responseDispatcher == null) {
      throw new NullPointerException("Either the request or the response dispatcher are null. Provide only valid instances!");
    }

    final Command command = getCommand(request.getAction());
    if (command != null) {
      logger.info("Dispatch command: " + request.getAction());
      command.execute(this, request, responseDispatcher);
    }
    else {
      logger.error("Could not find command for action: " + request.getAction());
      responseDispatcher.dispatch(new ErrorResponse(request, ErrorResponse.COMMAND_NOT_SUPPORTED));
    }
  }

  /**
   * Returns an instance of the command for the given
   * action name or null if the action name is not known.
   *
   * @param actionName The action name of the command
   * @return A command instance or null 
   */
  public Command getCommand(String actionName) {
    if (getCommands().containsKey(actionName)) {
      final Class commandClass = (Class) getCommands().get(actionName);
      try {
        return (Command) commandClass.newInstance();
      }
      catch (Exception e) {
        logger.error("Count not instantiate command for action: " + actionName);
        return null;
      }
    }
    return null;
  }

  /**
   * The create function is be called after
   * the module registry registered this instance.
   */
  public void create() {}

  /**
   * Register a new module listener
   *
   * @param moduleListener The module listener to register
   */
  public void addModuleListener(ModuleListener moduleListener) {
    if (moduleListener != null) {
      logger.debug("Add module listener: " + moduleListener);
      this.moduleListeners.addElement(moduleListener);
    }
  }

  /**
   * Removes the given module listener and calls the onDestroy callback
   * on the listener.
   *
   * @param moduleListener The listener to remove
   */
  public void removeModuleListener(ModuleListener moduleListener) {
    removeModuleListener(moduleListener, true);
  }

  /**
   * Removes the given module listener
   *
   * @param moduleListener The module listener to remove
   * @param callOnDestroy If true, then the onDestroy callback will be called. Otherwise not.
   */
  public void removeModuleListener(ModuleListener moduleListener, boolean callOnDestroy) {
    if (moduleListener != null) {
      logger.debug("Remove module listener: " + moduleListener);
      
      if (callOnDestroy) {
        logger.debug("Call destroy() on module listener: " + moduleListener);
        moduleListener.onDestroy(this);
      }
      this.moduleListeners.removeElement(moduleListener);
    }
  }

  /**
   * Resets the module. All listeners will be removed.
   * Also the scope map will be cleared. It means all saved
   * variables will be removed.
   */
  public void reset() {
    /* Notify the destroy  */
    if (moduleListeners != null && moduleListeners.size() > 0) {
      for (int i = 0; i < moduleListeners.size(); i++) {
        removeModuleListener((ModuleListener) moduleListeners.elementAt(i), true);
      }
      moduleListeners.removeAllElements();
    }

    if (scope != null && scope.size() > 0) {
      scope.clear();
    }
  }
  
  /**
   * This function will be called, if the module
   * registry removes this module.
   */
  public void destroy() {
    logger.info("Destroy module: " + moduleContext);
    reset();

    if (commands != null && commands.size() > 0) {
      commands.clear();
    }
  }

  public Hashtable getCommands() {
    return commands;
  }

  public Hashtable getScope() {
    return scope;
  }

  public Vector getModuleListeners() {
    return moduleListeners;
  }
}
