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

public class ErrorResponse extends AbstractResponse {
  public static final int UNKNOWN                 = 1;
  public static final int TIMEOUT                 = 2;
  public static final int MODULE_NOT_SUPPORTED    = 3;
  public static final int NETWORK                 = 4;
  public static final int INVALID_PARAMETER       = 5;
  public static final int COMMAND_NOT_SUPPORTED   = 6;
  public static final int CAPABILITY_NOT_SUPPORTED= 7;
  public static final int OBSERVER_STARTED        = 8;
  public static final int CANCELED_BY_USER        = 9;

  public static final String MESSAGE = "message";
  public static final String CODE = "code";

  public ErrorResponse(Requestable request) {
    this(request, UNKNOWN);
  }

  public ErrorResponse(Requestable request, int code) {
    this(request, code, null);
  }

  public ErrorResponse(Requestable request, int code, String message) {
    super(request);
    addParameter(CODE, new Integer(code));
    if (message != null && !"".equals(message)) {
      addParameter(MESSAGE, message);
    }
  }

  public String getResponseNamespace() {
    return "Error";
  }

  public int getErrorCode() {
    return ((Integer) getParameter(CODE)).intValue();
  }

  public String getMessage() {
    return (String) getParameter(MESSAGE);
  }
}
