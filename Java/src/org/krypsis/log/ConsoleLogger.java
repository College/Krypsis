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

package org.krypsis.log;

public class ConsoleLogger extends Logger {

  public ConsoleLogger(Class clazz) {
    super(clazz);
  }

  public void debug(String message) {
    print("DEBUG", message);
  }

  public void info(String message) {
    print(" INFO", message);
  }

  public void warn(String message) {
    print(" WARN", message);
  }

  public void error(String message) {
    print("ERROR", message);
  }

  private void print(String level, String message) {
    System.out.println(level + ": [" + getClassName() + "] => " + message);
  }
}
