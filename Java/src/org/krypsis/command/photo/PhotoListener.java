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

package org.krypsis.command.photo;

/**
 * The photo listener is used to notify the application
 * in case a photo was taken or the operation was canceled. 
 */
public interface PhotoListener {

  /**
   * A photo was taken and can be proceeded
   *
   * @param bytes The taken picture from camera
   */
  public void onPhoto(byte[] bytes);

  /**
   * The user cancled the operation. No photo is taken.
   */
  public void onCancel();

  /**
   * Will be called, if an error occured.
   * The reason for the error is passed as a code.
   * The constants of the codes are described in the ErrorResponse
   * class of the j2me package.
   *
   * @param code The error reason.
   */
  public void onError(int code);
}
