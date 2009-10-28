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
package org.krypsis.symbian;

import org.krypsis.http.response.Response;
import org.krypsis.http.response.ErrorResponse;
import org.krypsis.http.request.Request;
import org.krypsis.module.ModuleRegistry;
import org.krypsis.module.Module;
import org.krypsis.module.Command;
import org.krypsis.util.Queue;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Display;

/**
 * Date: 29.05.2009
 * <p/>
 * This thread is an observer on the request and response queue.
 * Each time a request arrives this thread will create a command
 * instance for the request and start the work.
 * <p/>
 * On the other hand it also will process the responses by forcing
 * the broser to execute the callback function of the response.
 */
public class RequestResponseThread extends Thread {
  private static final int DELAY = 100;
  private boolean running = false;
  private final Browser browser;
  private final Queue requestQueue;
  private final Queue responseQueue;
  private final KrypsisApplication application;

  public RequestResponseThread(KrypsisApplication application, Browser browser, Queue requestQueue, Queue responseQueue) {
    this.application = application;
    this.browser = browser;
    this.requestQueue = requestQueue;
    this.responseQueue = responseQueue;
  }

  public void start() {
    this.running = true;
    super.start();
  }

  /**
   * Do the work
   */
  public void run() {
    while (isRunning()) {
      final Display display = Display.getDefault();

      if (display == null) {
        throw new NullPointerException("Could not get the current SWT display!");
      }

      display.syncExec(new Runnable() {
        public void run() {
          try {
            processRequests();
            processResponses();
            sleep(DELAY);
            getBrowser().redraw();
          }
          catch (InterruptedException e) {
            setRunning(false);
          }
        }
      });
    }

  }

  private void processResponses() {
    final Response response = (Response) getResponseQueue().get();
    if (response != null) {
      getBrowser().setUrl("javascript:" + response.toJavascriptFunction());
    }
  }

  private void processRequests() {
    final Request request = (Request) getRequestQueue().get();
    if (request != null) {
      final ModuleRegistry moduleRegistry = ModuleRegistry.getInstance();
      final Module module = moduleRegistry.getModule(request.getModuleContext());
      if (module != null) {
        final Command command = module.getCommand(request.getAction());
        if (command == null) {
          final Response response = new ErrorResponse(request, ErrorResponse.COMMAND_NOT_SUPPORTED);
          getResponseQueue().add(response);
        }
        else {
          command.execute(module, request, getApplication());
        }
      }
    }
  }

  public Browser getBrowser() {
    return browser;
  }

  public Queue getRequestQueue() {
    return requestQueue;
  }

  public Queue getResponseQueue() {
    return responseQueue;
  }

  public boolean isRunning() {
    return running;
  }

  public void setRunning(boolean running) {
    this.running = running;
  }

  public KrypsisApplication getApplication() {
    return application;
  }
}
