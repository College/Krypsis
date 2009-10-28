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
 * @deprecated
 */
public class JpegImage extends Content {
  private String fileName;
  private String parameterName;
  private String contentType;
  private byte[] raw;

  /**
   * Creates a new jpeg image with the filename image.jpg
   * and parameter name "file"
   *
   * @param raw The image data.
   */
  public JpegImage(byte[] raw) {
    this(raw, "image.jpg", "file");
  }

  /**
   * Creates a new jpeg image data
   *
   * @param raw The image data
   * @param fileName The file name
   * @param parameterName The parameter name
   */
  public JpegImage(byte[] raw, String fileName, String parameterName) {
    this.raw = raw;
    this.fileName = fileName;
    this.parameterName = parameterName;
  }

  public String getFileName() {
    return fileName;
  }

  public String getName() {
    return parameterName;
  }

  public String getContentType() {
    if (contentType == null) {
      this.contentType = "image/jpeg";
    }
    return contentType;
  }

  public byte[] getData() {
    return raw;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public void setParameterName(String parameterName) {
    this.parameterName = parameterName;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }
}
