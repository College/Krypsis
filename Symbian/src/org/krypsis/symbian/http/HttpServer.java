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


package org.krypsis.symbian.http;

import org.krypsis.log.Logger;
import org.krypsis.log.LoggerFactory;

import javax.microedition.io.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.DataOutputStream;

public class HttpServer implements Runnable {
  private final Logger logger = LoggerFactory.getLogger(HttpServer.class);
  private int port = 50000;
  private boolean running = true;
  private final RequestMatchable matchable;
  private ServerRequestListener serverRequestListener = null;

  /**
   * Creates a new http server that creates a server
   * socker and listening to it. Each time a request is incomming,
   * the http server will acceppt the connection try to match the
   * request url with the given matcher. If the url matches,
   * the server extracts the url and dispatchs it to the
   * request listener - if any given.
   *
   * @param matchable The mather that tells the server if the
   *                  request should be dispatched or not.
   * @param port      The server port to listening to.
   */
  public HttpServer(RequestMatchable matchable, int port) {
    this.port = port;
    this.matchable = matchable;
  }

  /**
   * Start server socket listener and acceppt client
   * connection on the localhost port.
   * Each client connection is a possible krypsis
   * request. Validate the requested url and notify the
   * listener in case of a valid request.
   */
  public void run() {
    //ServerSocketConnection server = null;

    StreamConnectionNotifier server = null;
    try {

      /*
       * Open server connection at startup and wait
       * for incomming connections
       */
      server = (StreamConnectionNotifier) Connector.open("serversocket://:" + getPort());
      //server = (ServerSocketConnection) Connector.open("socket://:" + getPort(), Connector.READ_WRITE);
      if (server == null) {
        return;
      }

      while (isRunning()) {
        /* Wait for client... */
        //client = (SocketConnection) server.acceptAndOpen();
        final StreamConnection client = server.acceptAndOpen();

        logger.debug("Accepted client connection. Read content...");

        /*
        * Open input outputStream and read available bytes
        * into a string buffer. Treat the data as string at all.
        */
        final InputStream inputStream = client.openDataInputStream();
        final DataOutputStream outputStream = client.openDataOutputStream();
        final StringBuffer sb = new StringBuffer();
        int c;
        /* Read just the first line */
        while ((c = inputStream.read()) != -1 && c != '\n' && c != '\r') {
          sb.append((char) c);
        }
        byte[] trash = new byte[inputStream.available()];
        inputStream.read(trash);
        logger.debug("Threw " + trash.length + " bytes from request away.");

        /* Close connection and dispatch the incoming request */
        inputStream.close();
        StringBuffer output = new StringBuffer("HTTP/1.1 200 OK");
        output.append('\r');
        output.append('\n');
        output.append('\r');
        output.append('\n');
        outputStream.write(output.toString().getBytes());
        outputStream.flush();
        outputStream.close();
        client.close();

        String result = sb.toString();

        logger.debug("Connection closed. Content: " + result);

        try {
          final String request = getMatchable().match(result);
          if (request != null && getRequestListener() != null) {
            logger.debug("Dispatch krypsis request: " + request);
            getRequestListener().onServerRequest(request);
          }
        }
        catch (InvalidKrypsisRequestException e) {
          logger.warn("Got a non krypsis request on localhost: " + result);
        }

        try {
          Thread.sleep(100);
        }
        catch (InterruptedException e) {
          logger.warn("Http Server thread interrupted. Stop server");
          setRunning(false);
        }
      }
    }
    catch (IOException ex) {
      ex.printStackTrace();
    }
    finally {
      try {
        if (server != null) {
          server.close();
        }
      }
      catch (IOException e) {
        logger.error("Exception on freeing resources: " + e);
        // We have tried it :(
        throw new RuntimeException(e.toString());
      }
    }
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public boolean isRunning() {
    return running;
  }

  public synchronized void setRunning(boolean value) {
    this.running = value;
  }

  public RequestMatchable getMatchable() {
    return matchable;
  }

  public ServerRequestListener getRequestListener() {
    return serverRequestListener;
  }

  public void setRequestListener(ServerRequestListener serverRequestListener) {
    this.serverRequestListener = serverRequestListener;
  }
}
