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
package org.krypsis.command;

public class Photos {
  /** Takes a camera picture and uploads the taken photo to a remote server */
  public static final String TAKE_AND_UPLOAD = "takeandupload";

  /** The url of the remote server where to upload to photo to */
  public static final String UPLOAD_URL = "uploadurl";

  /** The http parameter name that should be associated with the picture */
  public static final String PARAMETER_NAME = "parametername";

  /** The filename of the picture */
  public static final String FILENAME = "filename";

  /** The server response parameter */
  public static final String RESPONSE = "response";
}
