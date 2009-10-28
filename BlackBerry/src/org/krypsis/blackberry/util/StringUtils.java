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

package org.krypsis.blackberry.util;

/**
 * Date: 07.04.2009
 * ToDo JavaSodc
 */
public class StringUtils {

  public static String[] split(String strString, String strDelimiter) {
    String[] strArray;
    int iOccurrences = 0;
    int iIndexOfInnerString = 0;
    int iIndexOfDelimiter = 0;
    int iCounter = 0;

    //Check for null input strings.
    if (strString == null) {
      throw new IllegalArgumentException("Input string cannot be null.");
    }
    //Check for null or empty delimiter strings.
    if (strDelimiter == null || strDelimiter.length() <= 0) {
      throw new IllegalArgumentException("Delimeter cannot be null or empty.");
    }

    //strString must be in this format: (without {} )
    //"{str[0]}{delimiter}str[1]}{delimiter} ...
    // {str[n-1]}{delimiter}{str[n]}{delimiter}"

    //If strString begins with delimiter then remove it in order
    //to comply with the desired format.

    if (strString.startsWith(strDelimiter)) {
      strString = strString.substring(strDelimiter.length());
    }

    //If strString does not end with the delimiter then add it
    //to the string in order to comply with the desired format.
    if (!strString.endsWith(strDelimiter)) {
      strString += strDelimiter;
    }

    //Count occurrences of the delimiter in the string.
    //Occurrences should be the same amount of inner strings.
    while ((iIndexOfDelimiter = strString.indexOf(strDelimiter,
            iIndexOfInnerString)) != -1) {
      iOccurrences += 1;
      iIndexOfInnerString = iIndexOfDelimiter +
              strDelimiter.length();
    }

    //Declare the array with the correct size.
    strArray = new String[iOccurrences];

    //Reset the indices.
    iIndexOfInnerString = 0;
    iIndexOfDelimiter = 0;

    //Walk across the string again and this time add the
    //strings to the array.
    while ((iIndexOfDelimiter = strString.indexOf(strDelimiter,
            iIndexOfInnerString)) != -1) {

      //Add string to array.
      strArray[iCounter] = strString.substring(iIndexOfInnerString, iIndexOfDelimiter);

      //Increment the index to the next character after
      //the next delimiter.
      iIndexOfInnerString = iIndexOfDelimiter +
              strDelimiter.length();

      //Inc the counter.
      iCounter += 1;
    }

    return strArray;
  }
}
