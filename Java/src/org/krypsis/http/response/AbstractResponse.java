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

import java.util.Enumeration;
import java.util.Hashtable;

public abstract class AbstractResponse implements Response {
  public static final String NAMESPACE = "Krypsis.Device";
  private final Requestable request;
  private final Hashtable parameters = new Hashtable();

  public AbstractResponse(Requestable request) {
    this.request = request;
  }

  public Requestable getRequest() {
    return request;
  }

  public void addParameter(String key, Object value) {
    getParameters().put(key, value);
  }

  public Hashtable getParameters() {
    return parameters;
  }

  public Object getParameter(String key) {
    return parameters.get(key);
  }

  public void setParameter(String key, Object value) {
    getParameters().put(key, value);
  }

  public String toJavascriptFunction() {
    final StringBuffer buffer = new StringBuffer();
    buffer.append(NAMESPACE);
    buffer.append(".");
    buffer.append(getRequest().getModuleContext().getName());
    buffer.append(".");
    buffer.append(getResponseNamespace());
    buffer.append(".");
    buffer.append(getRequest().getAction());
    buffer.append("(");
    if (getParameters().size() > 0) {
      buffer.append(parametersToJavascript());
    }
    buffer.append(")");
    return buffer.toString();
  }

  /**
   * @return Returns the parameters as an javascript object representation
   */
  protected String parametersToJavascript() {
    StringBuffer buffer = new StringBuffer();
    final Enumeration keys = getParameters().keys();

    buffer.append("{");
    while (keys.hasMoreElements()) {
      String key = (String) keys.nextElement();
      Object value = getParameter(key);
      if (value != null) {
        buffer.append(key);
        buffer.append(": ");
        buffer.append(value instanceof String ? "'" + value + "'" : value);
        if (keys.hasMoreElements()) {
          buffer.append(", ");
        }
      }
    }
    buffer.append("}");
    
    return buffer.toString();
  }

  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("{");
    buffer.append(request.getModuleContext());
    buffer.append(".");
    buffer.append(getResponseNamespace());
    buffer.append(".");
    buffer.append(request.getAction());
    buffer.append(": ");

    final Enumeration keys = getParameters().keys();
    while (keys.hasMoreElements()) {
      String key = (String) keys.nextElement();
      Object value = getParameter(key);
      buffer.append(key);
      buffer.append(" = ");
      buffer.append(value);
      if (keys.hasMoreElements()) {
        buffer.append(", ");
      }
    }
    buffer.append("}");
    return buffer.toString();
  }
}
