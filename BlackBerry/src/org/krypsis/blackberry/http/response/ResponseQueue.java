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

package org.krypsis.blackberry.http.response;

import org.krypsis.util.Queue;
import org.krypsis.http.response.Response;

/**
 * Date: 16.05.2009
 *
 * The response queue extends the queue data structure
 * and adds a new get method that expects a timeout
 */
public class ResponseQueue extends Queue {
  private static final long FREQUENCY = 333;

  public ResponseQueue(int size) {
    super(size);
  }

  /**
   * Wait for at least the given time in milliseconds for
   * a response. If no response is arrived, the return null
   *
   * @param millis Time to wait in milli seconds
   * @return The response or null
   */
  public Response get(int millis) {
    Response response = null;
    final long start = System.currentTimeMillis();
    long end = start;
    try {
      while (end - start < millis) {
        response = (Response) get();
        if (response != null) {
          break;
        }

        Thread.sleep(FREQUENCY);
        end = System.currentTimeMillis();
      }
    }
    catch(InterruptedException e){}
    return response;
  }
}
