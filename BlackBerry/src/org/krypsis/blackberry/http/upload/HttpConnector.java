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

package org.krypsis.blackberry.http.upload;

import org.krypsis.http.upload.Connectable;

import javax.microedition.io.HttpConnection;
import java.io.IOException;
import java.io.InputStream;

/**
 * Date: 21.05.2009
 */
public class HttpConnector implements Connectable {
  private final HttpConnection httpConnection;
  private StringBuffer data;

  public HttpConnector(HttpConnection httpConnection) {
    this.httpConnection = httpConnection;
  }

  public void setRequestProperty(String key, String value) throws IOException {
    httpConnection.setRequestProperty(key, value);
  }

  public String read() {
    return null;
  }

  public void write(String data) {
    write(new StringBuffer(data));
  }

  public void write(StringBuffer buffer) {
    if (data == null) {
      this.data = buffer;
    }
    else {
      this.data.append(buffer);
    }
  }

  public String send(String data) throws IOException {
    httpConnection.setRequestMethod(HttpConnection.POST);

    if (data == null) {
      throw new IOException("Data is empty. Nothing to send");
    }

    httpConnection.openOutputStream().write(data.getBytes());
    if (HttpConnection.HTTP_OK == httpConnection.getResponseCode()) {
      int len;
      byte[] buffer = new byte[256];
      final InputStream inputStream = httpConnection.openInputStream();
      StringBuffer raw = new StringBuffer();

      while (-1 != (len = inputStream.read(buffer))) {
        raw.append(new String(buffer, 0, len));
      }

      return raw.toString();
    }
    else {
      throw new IOException("The http response was not as expected: " + httpConnection.getResponseCode());
    }
  }

  public String send() throws IOException {
    return send(data != null ? data.toString() : null);
  }
}
