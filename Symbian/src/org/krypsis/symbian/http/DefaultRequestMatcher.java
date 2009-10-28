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

import org.krypsis.http.request.Request;
import me.regexp.RE;

/**
 * Date: 06.05.2009
 *
 * This mather returns the query part of the http
 * request if it
 */
public class DefaultRequestMatcher implements RequestMatchable {
  public static final String PATTERN  = "^(\\w+) (" + Request.NAMESPACE +"[a-z]+/[a-z]+(\\?[^ /#\\?]+)?) ([^ ]+)$";

  public String match(String request) throws InvalidKrypsisRequestException {
    final RE matcher = new RE(PATTERN);
    if (!matcher.match(request)) {
      throw new InvalidKrypsisRequestException("Request " + request + " does not match the required pattern: " + PATTERN);
    }
    return matcher.getParen(2);
  }
}