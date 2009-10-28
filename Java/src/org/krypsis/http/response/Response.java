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

package org.krypsis.http.response;

import org.krypsis.http.request.Requestable;

import java.util.Hashtable;

public interface Response {

  /**
   * @return The request for this response
   */
  public Requestable getRequest();

  /**
   * @return The parameters for this response
   */
  public Hashtable getParameters();

  /**
   * Sets the valud of a parameter.
   * @param key The key of the parameter
   * @param value The value of the parameter
   */
  public void setParameter(String key, Object value);

  /**
   * Returns the namespace of the javascript callback call.
   * Example: If the function Krypsis.Device.Base.getmetadata is requested,
   * then the result could be either an error or a success.
   * In case of a success the namespace would be 'Success' so the callback
   * can be constructed as:
   * Krypsis.Device.Base.Success.getmetadata
   *
   * In case of an error, the result of this function would be 'Error' and
   * the callback will be constructed as :
   * Krypsis.Device.Base.Error.getmetadata
   *
   * @return The response namespace
   */
  public abstract String getResponseNamespace();

  /**
   * Returns the javascript function call, that call be directly
   * evaluated by the browser javascript engine.
   *
   * @return A string that represents the javascript function call.
   */
  public String toJavascriptFunction();
}
