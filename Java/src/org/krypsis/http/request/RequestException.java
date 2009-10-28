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

import org.krypsis.http.response.ErrorResponse;

/**
 * This a general exception on a krypsis request.
 * Each time somethgin is wrong with the given url
 * an aception of this type will be thrown
 */
public class RequestException extends Exception {
  private final int reason;

  public RequestException() {
    this("", ErrorResponse.UNKNOWN);
  }

  public RequestException(int reason) {
    this("", reason);
  }

  public RequestException(String message) {
    this(message, ErrorResponse.UNKNOWN);
  }

  public RequestException(String message, int reason) {
    super(message);
    this.reason = reason;
  }

  public int getReason() {
    return reason >= 0 ? reason : ErrorResponse.UNKNOWN;
  }
}
