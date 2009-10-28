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

package org.krypsis.http.upload;

import java.util.Hashtable;
import java.util.ArrayList;
import java.util.List;
import java.util.Enumeration;
import java.io.UnsupportedEncodingException;

public class HttpPostMessage extends HttpMessage {
  public static final String BOUNDARY = "boundary";
  public static final String BOUDARY_PREFIX = "--";
  public static final String BOUDARY_POSTFIX = BOUDARY_PREFIX;

  private final String boundary;
  private final Hashtable parameters = new Hashtable();

  public HttpPostMessage() {
    this.boundary = ContentUtils.generateBoundary(32);
    addRequestProperty(Content.CONTENT_TYPE, getContentTypeString() + "; charset=\"UTF-8\"");
  }

  public void set(String name, String value) {
    set(name, new FormContent(name, value));
  }

  public void set(String name, byte[] data, String filename, String mimeType) {
    set(name, new FileContent(mimeType, name, filename, data, true));
  }

  public void set(String name, Content content) {
    parameters.put(name, content);
  }

  public String getContentTypeString() {
    final StringBuffer type = new StringBuffer();
    type.append("multipart/form-data; ");
    type.append(BOUNDARY);
    type.append("=");
    type.append(getBoundary());
    return type.toString();
  }

  public String toString() {
    final StringBuffer buffer = new StringBuffer();
    final Enumeration elements = getParameters().elements();

    while (elements.hasMoreElements()) {
      final Content content = (Content) elements.nextElement();
      buffer.append(getBoundaryString());
      buffer.append(Content.EOL);
      buffer.append(content.toString());
      buffer.append(Content.EOL);
    }

    buffer.append(getBoundaryCloseString());

    return buffer.toString();
  }

  public String getBoundary() {
    return boundary;
  }

  public String getBoundaryString() {
    return BOUDARY_PREFIX + getBoundary();
  }

  public String getBoundaryCloseString() {
    return BOUDARY_PREFIX + getBoundary() + BOUDARY_POSTFIX;
  }

  public Hashtable getParameters() {
    return parameters;
  }
}
