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

/**
 * <p>
 * An instance of this class contains file content and information
 * about this file.
 * </p>
 */
public class FileContent extends Content {

  /**
   * Creates a regular form content with the given name and data
   *
   * @param contentType The content type of the file
   * @param name The parameter name
   * @param filename The name of the file
   * @param data The data byte array of the file contents
   * @param binary Is the file content binary encoded?
   */
  public FileContent(String contentType, String name, String filename, byte[] data, boolean binary) {
    super(data);

    /* Introduce the file and parameter name */
    final StringBuffer buffer = new StringBuffer();
    buffer.append("form-data; ");
    buffer.append("name=\"");
    buffer.append(name);
    buffer.append("\"; ");
    buffer.append("filename=\"");
    buffer.append(filename);
    buffer.append("\"");
    setContentDisposition(buffer.toString());

    /* Sets the content type of this file */
    setContentType(contentType);

    /* Sets the content lenght */
    //addHeader(CONTENT_LENGTH, String.valueOf(data.length));
    //addHeader("Content-transfer-encoding", "binary");
  }
}