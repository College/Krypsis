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
package org.krypsis.http.request;

import org.krypsis.module.ModuleContext;

import java.util.Hashtable;

public interface Requestable {

  /**
   * @return The requested URL as string
   */
  public String getUrl();

  /**
   * @return The path without the query
   */
  public String getPath();

  /**
   * @return The query. Contains the module context and the action
   */
  public String getQuery();

  /**
   * @return The requested action name
   */
  public String getAction();

  /**
   * @return The provided parameters or an empty hashmap if none given
   */
  public Hashtable getParameters();

  /**
   * @param name The parameter name
   * @return The value of the parameter
   */
  public String getParameter(String name);

  /**
   * @return The name of the requested module
   */
  public ModuleContext getModuleContext();
}
