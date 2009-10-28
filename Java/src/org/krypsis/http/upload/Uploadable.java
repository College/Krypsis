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

public interface Uploadable {

  /**
   * Uploads the given container to the remote server.
   *
   * @param connectable The connection to the server
   * @param message The message to send to the server
   * @return The server response
   * @throws UploadException If the data could not be uploaded
   *
   */
  public String upload(Connectable connectable, HttpMessage message) throws UploadException;

  /**
   * Uploads the given file to the specified url.
   *
   * @param connectable The wire to send the data over
   * @param content The object that contains the content to transfer
   * @return The server response.
   *
   * @throws UploadException If the file could not be uploaded
   *
   * @deprecated
   */
  public String upload(Connectable connectable, Content content) throws UploadException;
}
