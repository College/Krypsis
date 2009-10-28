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
package org.krypsis.http.request;

import org.krypsis.module.ModuleContext;
import org.krypsis.http.HttpUtils;

import java.util.Hashtable;

import me.regexp.RE;

/**
 * The krypsis url is not a typical URL or
 * it is a string that is conform to an URL
 * but it is expected to contains some parts that are needed
 * to determine the action, the module context and the parameters.
 * A valid krypsis url starts with a typical protocol and
 * host name part and is followed by the org/krypsis/device
 * path. Everything after this path will be proceeded as an valid
 * krypsis command. Is this path not present and exception will be
 * thrown. 
 */
public class Request implements Requestable {
  private static final String AMPERSAND = "&";
  private static final String SEMICOLON = ";";
  private static final String EQUALS    = "=";

  /**
   * A valid krypsis request url must contain at least
   * this path.
   */
  public static final String NAMESPACE = "/org/krypsis/device/";
  /**
   * Contains the url after the namespace
   */
  private final String url;
  private final String action;
  private final ModuleContext moduleContext;
  private String path;
  private String query;
  private Hashtable arguments = null;

  /**
   * Creates a new krypsis url instance from the given string.
   * The string must contain the krypsis namespace, a module
   * name and an action name.
   *
   * @param string The real URL to create the crypsis url from.
   * @throws RequestException If the
   * url is malformed or not a krypsis url
   */
  public Request(String string) throws RequestException {
    if (string == null || "".equals(string)) {
      throw new RequestException("The given url must not be null or empty");
    }
    final int namespaceIndex = string.indexOf(NAMESPACE);

    if (namespaceIndex < 0) {
      throw new RequestException("The url '" + string + "' is not a valid krypsis request url.");
    }

    url = string.substring(namespaceIndex + NAMESPACE.length());

    if (url.length() == 0) {
      throw new RequestException("The krypses request url part is zero. Can't do anything with it.");
    }

    /* Extract path and query parts */
    int queryIndex = url.indexOf("?");

    /* BB appends its params to the url */
    if (queryIndex < 0) {
      queryIndex = url.indexOf(";");
    }
    
    if (queryIndex >= 0) {
      path = url.substring(0, queryIndex);
      query = url.substring(queryIndex + 1);

      if (path.length() == 0) {
        throw new RequestException("The given url contains a query without a path.");
      }
    }
    else {
      path = url;
    }

    /* Per specification a krypsis call is lowecase. */
    path = path.toLowerCase();

    /* Extract moduleName and action */
    final int moduleIndex = path.indexOf("/");
    if (moduleIndex < 0) {
      throw new RequestException("Maybe the moduleName is specified, but the action is missing");
    }
    
    final String moduleName = path.substring(0, moduleIndex);
    action = path.substring(moduleIndex + 1);

    if (moduleName.length() == 0 || action.length() == 0) {
      throw new RequestException("Either moduleName or action is not given");
    }


    /* Module and action must be alphanumeric with a starting letter */
    final RE re = new RE("[a-zA-Z][a-zA-Z0-9]*");
    if (!re.match(action)) {
      throw new RequestException("The action name does not match the requirements of the expected expression: " + re);
    }

    this.moduleContext = ModuleContext.getContextByName(moduleName);
    if (moduleContext == null) {
      throw new RequestException("The module context " + moduleName + " is undefined");
    }
  }

  public String getUrl() {
    return url;
  }

  public String getPath() {
    return path;
  }

  public String getQuery() {
    return query;
  }

  public String getAction() {
    return action;
  }

  public Hashtable getParameters() {
    if (arguments == null) {
      arguments = parseArguments(getQuery());
    }
    return arguments;
  }

  public String getParameter(String key) {
    return (String) getParameters().get(key);
  }

  public ModuleContext getModuleContext() {
    return moduleContext;
  }

  /**
   * Will split the given query into small parts of keys and values
   * They will be stored in the returned hashtable.
   * A valid query is looking like this:
   * key1=value1&key2=value2;key3=value3&key4=;key5
   *
   * The & and ; are delimiters and will be removed. The = is the cohesion
   * of the key and value pair.
   *
   * @param query The query to parse
   * @return A hashtable with the parsed arguments
   */
  public static Hashtable parseArguments(String query) {
    Hashtable result = new Hashtable();

    if (query == null || query.length() == 0) {
      return result;
    }

    final RE ampersand = new RE(AMPERSAND);
    final RE semicolon = new RE(SEMICOLON);

    final String[] ampersandPairs = ampersand.split(query);
    for (int i = 0; i < ampersandPairs.length; i++) {

      final String[] semicolonPairs = semicolon.split(ampersandPairs[i]);
      for (int j = 0; j < semicolonPairs.length; j++) {
        String keyValue = semicolonPairs[j];
        if (keyValue.length() >= 0) {
          final int splitIndex = keyValue.indexOf(EQUALS);
          String key = splitIndex < 0 ? keyValue : keyValue.substring(0, splitIndex);
          String value = splitIndex > 0 ? keyValue.substring(splitIndex + 1) : null;
          if (value != null && value.length() > 0) {
            key = HttpUtils.unescape(key);
            value = HttpUtils.unescape(value);
            result.put(key, value);
          }
        }
      }
    }
    return result;
  }

  public int getTimeout() {
    final String timeoutString = getParameter("timeout");
    int timeout = 10000;

    try {
      if (timeoutString != null) {
        timeout = Integer.parseInt(timeoutString);
      }
    }
    catch (NumberFormatException e) {}
    return timeout;
  }
}