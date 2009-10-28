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

import me.regexp.RE;

/**
 * This class is a small locale helper for the j2me environment.
 * You can get the locale of your device by requesting the
 * 'microedition.locale' system property.
 *
 * System.getProperty("microedition.locale");
 *
 * You can pass the value as constructor argument and then use the
 * langauge and country getter for the particular information. 
 */
public class LocaleMatcher {
  private static final String PATTERN = "([a-z]{2})\\-([A-Z]{2})";
  private final String language;
  private final String country;

  /**
   * Creates a new Locale instance and expects a String that
   * matches the "([a-z]{2})\-([A-Z]{2})" pattern. e.g. en-US
   *
   * @param locale The locale String
   */
  public LocaleMatcher(String locale) {
    final RE matcher = new RE(PATTERN);
    if (locale != null && !locale.equals("") && matcher.match(locale)) {
      this.language = matcher.getParen(1);
      this.country = matcher.getParen(2);
    }
    else {
      throw new IllegalArgumentException("Locale '" + locale + "' does not match the pattern '" + PATTERN + "'");
    }
  }

  /**
   * @return The language as a lower-case two-letter code
   * @link http://www.ics.uci.edu/pub/ietf/http/related/iso639.txt
   */
  public String getLanguage() {
    return language != null ? language.toLowerCase() : language;
  }

  /**
   * @return The country as a upper-case two-letter code
   * @link http://www.chemie.fu-berlin.de/diverse/doc/ISO_3166.html
   */
  public String getCountry() {
    return country != null ? country.toUpperCase() : country;
  }
}
