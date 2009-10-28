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

import java.util.*;
import java.io.UnsupportedEncodingException;

/**
 * This class represents a content with an optional content type
 * and an optional content disposition. You can access the data
 * of the content by calling the toString() method.
 *
 * Content-Disposition: form-data; name="files"
 * Content-Type: multipart/mixed; boundary=BbC04y
 */
public class Content {
  public static final String EOL = "\r\n";
  public static final String CONTENT_TYPE = "Content-Type";
  public static final String CONTENT_DISPOSITION = "Content-Disposition";
  public static final String CONTENT_LENGTH = "Content-Length";

  /**
   * The content headers like content type, length, disposition..
   */
  private final Hashtable headers = new Hashtable();

  /**
   * The content data. Like value of a parameter or contents
   * of a file.
   */
  private final byte[] data;

  /**
   * Creates a new content without any data.
   */
  public Content() {
    this(null);
  }

  /**
   * Creates a new content with the given data. As the content could be
   * a content container, the data is allowed to be empty.
   *
   * @param data The data of this content
   */
  public Content(byte[] data) {
    this.data = data;
  }

  /**
   * Sets the content type for this content.
   *
   * @param type The content type
   */
  public void setContentType(String type) {
    addHeader(CONTENT_TYPE, type);
  }

  /**
   * Sets the content disposition for this content
   *
   * @param value The disposition.
   */
  public void setContentDisposition(String value) {
    addHeader(CONTENT_DISPOSITION, value);
  }

  /**
   * Adds a new content header or overwrite an existing one.
   * A content header could be the content type or the disposition
   * of this content.
   *
   * Examples:
   *
   * Content-Disposition: form-data; name="files"; filename="file1.txt"
   * Content-Type: multipart/form-data; boundary=AaB03x
   *
   * @param key The header key
   * @param value The header value
   */
  public void addHeader(String key, String value) {
    headers.put(key, value);
  }

  /**
   * @return The headers for this content.
   */
  public Hashtable getHeaders() {
    return headers;
  }

  /**
   * @return The data of this content. Could also be null.
   */
  public byte[] getData() {
    return data;
  }

  /**
   * Appends the data of this content to the given buffer
   * 
   * @param buffer the buffer to append the content data to.
   */
  public void toString(StringBuffer buffer) {
    final Enumeration enumeration = getHeaders().keys();
    while (enumeration.hasMoreElements()) {
      final String key = (String) enumeration.nextElement();
      final String value = (String) getHeaders().get(key);

      if (value != null) {
        buffer.append(key);
        buffer.append(": ");
        buffer.append(value);
        buffer.append(EOL);
      }
    }

    if (buffer.length() > 0) {
      buffer.append(EOL);
    }
    
    if (getData() != null && getData().length > 0) {
      try {
        buffer.append(new String(getData(), "ISO-8859-1"));
      }
      catch (UnsupportedEncodingException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public String toString() {
    final StringBuffer buffer = new StringBuffer();
    toString(buffer);
    return buffer.toString();
  }
}
