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

package org.krypsis.symbian.http;

/**
 * Date: 06.05.2009
 *
 * The http server expects an instance of this
 * interface to deside if the whether the incoming
 * request is a krypsis requets or not.
 */
public interface RequestMatchable {

  /**
   * Expects an incoming HTTP request and returns the
   * interested url as result. If the http request is not a valid
   * krypsis request, the function throws an exception.
   *
   * @param request The incomming http request
   * @return The krypsis url without the http method and protocol version.
   * @throws InvalidKrypsisRequestException If the http request is not a valid
   * krypsis request
   */
  public String match(String request) throws InvalidKrypsisRequestException ;
}
