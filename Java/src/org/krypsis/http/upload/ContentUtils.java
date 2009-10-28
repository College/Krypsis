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

import java.util.Random;

/**
 * <p>
 * Use this class to generate boundaries and acess public constants
 * </p>
 */
public class ContentUtils {
  /**
   * The boumdaries will be generates from this characters
   */
  private static final String CHARS = "abcdefghijklmonpqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_";
  private static final Random randomizer = new Random();
  
  /**
   * Generates a random string with the given lenght.
   * @param length The length of the string.
   * @return The random boundary.
   */
  public static String generateBoundary(int length) {
    final char[] buf = new char[length];
    for (int i = 0; i < buf.length; i++) {
      buf[i] = CHARS.charAt(randomizer.nextInt(CHARS.length()));
    }
    return new String(buf);
  }
}
