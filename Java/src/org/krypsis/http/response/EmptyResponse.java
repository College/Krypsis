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

/**
 * The empty respose will be returned after the event queue
 * has timed out. The empty result of the body will force
 * the javascript blackberry connector to do nothing
 * but start the queue again.
 */
public class EmptyResponse extends AbstractResponse {

  public EmptyResponse(Requestable request) {
    super(request);
  }

  public String toJavascriptFunction() {
    return "";
  }

  public String getResponseNamespace() {
    return "";
  }
}
