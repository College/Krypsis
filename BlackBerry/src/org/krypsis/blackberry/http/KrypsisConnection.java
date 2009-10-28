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

package org.krypsis.blackberry.http;

import net.rim.device.api.io.http.HttpDateParser;
import net.rim.device.api.io.http.HttpHeaders;
import net.rim.device.api.io.http.HttpProtocolConstants;

import javax.microedition.io.HttpConnection;
import java.io.*;

import org.krypsis.http.response.Response;

/**
 * Date: 04.04.2009
 */
public class KrypsisConnection implements HttpConnection {
  public static final String OK = "OK";
  public static final String MEHTOD_GET = "GET";

  private String method;
  private HttpHeaders requestHeaders = new HttpHeaders();
  private HttpHeaders responseHeaders = new HttpHeaders();
  private final Response response;
  private final String host;
  private int port;

  public KrypsisConnection(Response response, String host) {
    this(response, host, 80);
  }

  public KrypsisConnection(Response response, String host, int port) {
    this.response = response;
    this.host = host;
    this.port = port;
    getResponseHeaders().setProperty(HttpProtocolConstants.HEADER_CONTENT_TYPE, "text/html");
  }

  public String getURL() {
    return getProtocol() + ":" + getResponse().getRequest().getUrl();
  }

  /**
   * @see javax.microedition.io.HttpConnection#getProtocol()
   */
  public String getProtocol() {
    return "http";
  }

  /**
   * @see javax.microedition.io.HttpConnection#getHost()
   */
  public String getHost() {
    return host;
  }

  /**
   * @see javax.microedition.io.HttpConnection#getFile()
   */
  public String getFile() {
    return "";
  }

  /**
   * @see javax.microedition.io.HttpConnection#getRef()
   */
  public String getRef() {
    return "";
  }

  /**
   * @see javax.microedition.io.HttpConnection#getQuery()
   */
  public String getQuery() {
    return "";
  }

  /**
   * @see javax.microedition.io.HttpConnection#getPort()
   */
  public int getPort() {
    return port;
  }

  /**
   * @see javax.microedition.io.HttpConnection#getRequestMethod()
   */
  public String getRequestMethod() {
    return (method == null) ? MEHTOD_GET : method;
  }

  /**
   * @see javax.microedition.io.HttpConnection#setRequestMethod(String)
   */
  public void setRequestMethod(String method) throws IOException {
    this.method = method;
  }

  /**
   * @see javax.microedition.io.HttpConnection#getRequestProperty(String)
   */
  public String getRequestProperty(String key) {
    return getRequestHeaders().getPropertyValue(key);
  }

  /**
   * @see javax.microedition.io.HttpConnection#setRequestProperty(String, String)
   */
  public void setRequestProperty(String key, String value) throws IOException {
    getRequestHeaders().setProperty(key, value);
  }

  /**
   * @see javax.microedition.io.HttpConnection#getResponseCode()
   */
  public int getResponseCode() throws IOException {
    return 200;
  }

  /**
   * @see javax.microedition.io.HttpConnection#getResponseMessage()
   */
  public String getResponseMessage() throws IOException {
    return OK;
  }

  /**
   * @see javax.microedition.io.HttpConnection#getExpiration()
   */
  public long getExpiration() throws IOException {
    String value = getHeaderField(HttpProtocolConstants.HEADER_EXPIRES);
    if (value != null) {
      try {
        return HttpDateParser.parse(value);
      }
      catch (Exception e) {
      }
    }

    return 0;
  }

  /**
   * @see javax.microedition.io.HttpConnection#getDate()
   */
  public long getDate() throws IOException {
    String value = getHeaderField(HttpProtocolConstants.HEADER_DATE);
    if (value != null) {
      try {
        return HttpDateParser.parse(value);
      }
      catch (Exception e) {
      }
    }

    return 0;
  }

  /**
   * @see javax.microedition.io.HttpConnection#getLastModified()
   */
  public long getLastModified() throws IOException {
    // Return current time.
    return System.currentTimeMillis();
  }

  /**
   * @see javax.microedition.io.HttpConnection#getHeaderField(String)
   */
  public String getHeaderField(String name) throws IOException {
    return getResponseHeaders().getPropertyValue(name);
  }

  /**
   * @see javax.microedition.io.HttpConnection#getHeaderFieldInt(String, int)
   */
  public int getHeaderFieldInt(String name, int def) throws IOException {
    String value = getResponseHeaders().getPropertyValue(name);
    try {
      if (value != null) {
        return Integer.parseInt(value);
      }
    }
    catch (NumberFormatException e) {
    }

    return def;
  }

  /**
   * @see javax.microedition.io.HttpConnection#getHeaderFieldDate(String, long)
   */
  public long getHeaderFieldDate(String name, long def) throws IOException {
    String value = getResponseHeaders().getPropertyValue(name);

    try {
      if (value != null) {
        return HttpDateParser.parse(value);
      }
    }
    catch (Exception e) {
    }

    return def;
  }

  /**
   * @see javax.microedition.io.HttpConnection#getHeaderField(int)
   */
  public String getHeaderField(int n) throws IOException {
    return getResponseHeaders().getPropertyValue(n);
  }

  /**
   * @see javax.microedition.io.HttpConnection#getHeaderFieldKey(int)
   */
  public String getHeaderFieldKey(int n) throws IOException {
    return getResponseHeaders().getPropertyKey(n);
  }

  /**
   * @see javax.microedition.io.HttpConnection#getType()
   */
  public String getType() {
    try {
      return getHeaderField(HttpProtocolConstants.HEADER_CONTENT_TYPE);
    }
    catch (IOException e) {
      return null;
    }
  }

  /**
   * @see javax.microedition.io.HttpConnection#getEncoding()
   */
  public String getEncoding() {
    try {
      return getHeaderField(HttpProtocolConstants.HEADER_CONTENT_ENCODING);
    }
    catch (IOException e) {
      return null;
    }
  }

  /**
   * @see javax.microedition.io.HttpConnection#getLength()
   */
  public long getLength() {
    try {
      return getHeaderFieldInt(HttpProtocolConstants.HEADER_CONTENT_LENGTH, -1);
    }
    catch (IOException e) {
    }

    return -1;
  }

  /**
   * @see javax.microedition.io.HttpConnection#openInputStream()
   */
  public InputStream openInputStream() throws IOException {
    return new ByteArrayInputStream(getResponse().toJavascriptFunction().getBytes());
  }

  /**
   * @see javax.microedition.io.HttpConnection#openDataInputStream()
   */
  public DataInputStream openDataInputStream() throws IOException {
    return new DataInputStream(openInputStream());
  }

  /**
   * @see javax.microedition.io.HttpConnection#openOutputStream()
   */
  public OutputStream openOutputStream() throws IOException {
    return new ByteArrayOutputStream();
  }

  /**
   * @see javax.microedition.io.HttpConnection#openDataOutputStream()
   */
  public DataOutputStream openDataOutputStream() throws IOException {
    return new DataOutputStream(openOutputStream());
  }

  /**
   * @see javax.microedition.io.HttpConnection#close()
   */
  public void close() throws IOException {
  }

  public Response getResponse() {
    return response;
  }

  public HttpHeaders getResponseHeaders() {
    return responseHeaders;
  }

  public HttpHeaders getRequestHeaders() {
    return requestHeaders;
  }

  public String getMethod() {
    return method;
  }
}
