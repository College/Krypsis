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

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 * The connectable interface is a wrapper around the http connection
 * of a system sepcific implementation.
 */
public interface Connectable {

  /**
   * Sets a http request property like connection, content-type etc.
   *
   * @param key The request key
   * @param value The value to the key
   * @throws java.io.IOException If the request property is not allowed, or supported
   */
  public void setRequestProperty(String key, String value) throws IOException;

  /**
   * Writes the given string to the output stream.
   *
   * @param data The string to write into the output stream
   */
  public void write(String data);

  /**
   * Writes the data to the out put stream
   *
   * @param buffer The buffer to write data into
   *
   * @deprecated
   */
  public void write(StringBuffer buffer);

  /**
   * Sends the data that was written into the output
   * stream.
   *
   * @return The server response
   * @throws java.io.IOException If the data could not be send
   */
  public String send() throws IOException;

  /**
   * Sends the string data to the remote server.
   *
   * @param data The data to send
   * @return The server response
   * @throws java.io.IOException If the data could not be send
   */
  public String send(String data) throws IOException;
}
