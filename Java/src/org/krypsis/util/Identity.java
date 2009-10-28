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
package org.krypsis.util;

import java.util.Random;
import java.util.Date;

/**
 * Generates a new unique krypsis device id in form of
 * [a-zA-Z0-9]{32}@CURRENT_TIME_IN_MILLI_SECONDS
 *
 * Example ID:
 *  0qn9T6spLdwgMeeMlMsgpaFyN2VmhajH@1245405600123
 */
public class Identity {
  private final static Identity instance = new Identity();

  /**
   * Hide the constructor.
   */
  private Identity() {}

  /**
   * @return The instance of this generator
   */
  public static Identity getInstance() {
    return instance;
  }

  /**
   * @return Generates a new ID and returns it as result.
   */
  public String generate() {
    return generate(new Date());
  }

  /**
   * Generates a new random ID with the given date.
   *
   * @param date The date to append to the id.
   * @return The generated ID
   */
  public String generate(Date date) {
    final StringBuffer buffer = new StringBuffer();
    buffer.append(getRandomString(32));
    buffer.append("@");
    buffer.append(String.valueOf(date.getTime()));
    return buffer.toString();
  }

  /**
   * Returns an alhanumeric string with the specified size.
   * 
   * @param length The length of the string to generate
   * @return The random string
   */
  public String getRandomString(int length) {
    final Random random = new Random();

    char[] chars = merge(getCharacterRange(48, 57), getCharacterRange(65, 90));
    chars        = merge(chars, getCharacterRange(97, 122));

    final StringBuffer result = new StringBuffer();
    for (int i = 0; i < length; i++) {
      result.append(chars[random.nextInt(chars.length)]);
    }
    
    return result.toString();
  }

  /**
   * Merges both array to one single and returns the result.
   *
   * @param first The first char array
   * @param second The second char array to merge
   * @return The merged arrays
   */
  public char[] merge(char[] first, char[] second) {
    final char[] chars = new char[first.length + second.length];

    for (int i = 0; i < chars.length; i++) {
      chars[i] = i < first.length ? first[i] : second[i - first.length];
    }
    
    return chars;
  }


  /**
   * Returns a char array with all character between the given
   * ASCII values range.
   *
   * @param from The ASCII code to start
   * @param to The ASCII code of the end
   * @return All characters in the ASCII range
   */
  public char[] getCharacterRange(int from, int to) {
    final char[] range = new char[(to - from) + 1];

    for (int i = from; i <= to; i++) {
      range[i - from] = (char) i;
    }
    
    return range;
  }
}
