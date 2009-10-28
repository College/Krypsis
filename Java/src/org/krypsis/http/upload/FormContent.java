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
 * The form content is a simple text content and expects a name and a value
 * Example:
 * Content-Disposition: form-data; name="submit-name"
 *
 * Larry
 * </p>
 */
public class FormContent extends Content {

  /**
   * Creates a regular form content with the given name and data
   * 
   * @param name The parameter name
   * @param value The value of this content
   */
  public FormContent(String name, String value) {
    super(value.getBytes());
    
    final StringBuffer buffer = new StringBuffer();
    buffer.append("form-data; ");
    buffer.append("name=\"");
    buffer.append(name);
    buffer.append("\"");

    setContentDisposition(buffer.toString());
  }
}
