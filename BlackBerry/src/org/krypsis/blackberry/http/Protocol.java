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

import net.rim.device.api.io.FilterBaseInterface;
import org.krypsis.blackberry.BlackBerryApplication;
import org.krypsis.blackberry.Main;
import org.krypsis.blackberry.http.response.ResponseQueue;
import org.krypsis.http.request.Request;
import org.krypsis.http.request.RequestException;
import org.krypsis.http.response.ErrorResponse;
import org.krypsis.http.response.Response;
import org.krypsis.http.response.ResponseDispatchable;
import org.krypsis.http.response.EmptyResponse;
import org.krypsis.log.Logger;
import org.krypsis.log.LoggerFactory;
import org.krypsis.module.Module;
import org.krypsis.module.ModuleContext;
import org.krypsis.module.ModuleRegistry;

import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import java.io.IOException;

/**
 * Per convention it is possible to define a connection filter
 * that will be instantiated whenever the requested URL is matching
 * the defined pattern. We use this feature to intercept krypsis requests
 *
 * If a request instance can be successfully created from the given url
 * then the request will be dispatched to the responsibsle module 
 */
public class Protocol implements FilterBaseInterface, ResponseDispatchable {
  public static final int QUEUE_TIMEOUT = 60000;

  private final Logger log = LoggerFactory.getLogger(Protocol.class);
  private final BlackBerryApplication blackBerryApplication;
  private static final String QUEUE = "responsequeue";

  public Protocol() {
    blackBerryApplication = BlackBerryApplication.getInstance();
  }

  /**
   * This method will open a filtered Http Connection.
   *
   * @see net.rim.device.api.io.FilterBaseInterface#openFilter(String, int, boolean)
   */
  public Connection openFilter(String name, int mode, boolean timeouts) throws IOException {
    try {
      final Request request = new Request(name);
      log.debug("Intercept krypsis request: " + name);
      final Response response = dispatchRequest(request);
      return response == null ? null : new KrypsisConnection(response, Main.HOST);
    }
    catch (RequestException e) {
      log.debug("Not a crypsis request. Pass it to the network");
      return Connector.open("http:" + name + ";usefilter=false", mode, timeouts);
    }
  }

  /**
   * Dispatches the given request. If the responsible module
   * is not available then an error response will be returned.
   *
   * @param request The request to dispatch.
   * @return A response to the given request or null.
   */
  public Response dispatchRequest(Request request) {

    /*
      If this is a queue request, then return the next request
      from the queue.
     */
    if (ModuleContext.QUEUE.equals(request.getModuleContext())) {
      final long begin = System.currentTimeMillis();
      log.debug("Queue: Wait for response...");
      final Response response = getResponseQueue().get(QUEUE_TIMEOUT);
      final long end = System.currentTimeMillis();
      log.debug("Queue: Returned after " + (end - begin) + " ms " + (response == null ? "without" : "with") + " a response: =>" + response);
      /* The queue response must not be null but an empty response */
      return response == null ? new EmptyResponse(request) : response;
    }

    final Module module = getRegistry().getModule(request.getModuleContext());
    if (module == null) {
      log.error("Requested module '" + request.getModuleContext() + "' is not implemented or registered");
      dispatch(new ErrorResponse(request, ErrorResponse.MODULE_NOT_SUPPORTED));
    }
    else {
      log.debug("Module '" + request.getModuleContext() + "' created. Execute action '" + request.getAction() + "' ...");
      module.handle(request, this);
    }

    return null;
  }

  public void dispatch(Response response) {
    getResponseQueue().add(response);
  }

  public ResponseQueue getResponseQueue() {
    ResponseQueue queue = (ResponseQueue) getGlobalScope().get(QUEUE);
    if (queue == null) {
      log.debug("Create response queue with a size of 32");
      queue = new ResponseQueue(32);
      getGlobalScope().put(QUEUE, queue);
    }
    return queue;
  }

  public ModuleRegistry getRegistry() {
    return ModuleRegistry.getInstance();
  }

  public BlackBerryApplication getGlobalScope() {
    return blackBerryApplication;
  }
}
