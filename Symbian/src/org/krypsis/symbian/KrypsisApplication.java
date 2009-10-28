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

import org.eclipse.ercp.swt.mobile.MobileShell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.krypsis.command.Bases;
import org.krypsis.command.Screens;
import org.krypsis.command.Locations;
import org.krypsis.command.Photos;
import org.krypsis.http.request.Request;
import org.krypsis.http.request.RequestException;
import org.krypsis.http.response.Response;
import org.krypsis.http.response.ResponseDispatchable;
import org.krypsis.log.Logger;
import org.krypsis.log.LoggerFactory;
import org.krypsis.module.*;
import org.krypsis.symbian.command.base.GetMetaDataCommand;
import org.krypsis.symbian.command.base.ReloadCommand;
import org.krypsis.symbian.command.base.LoadCommand;
import org.krypsis.symbian.command.base.ResetCommand;
import org.krypsis.symbian.command.screen.GetInfoCommand;
import org.krypsis.symbian.command.screen.StartObserveOrientationCommand;
import org.krypsis.symbian.command.screen.StopObserveOrientationCommand;
import org.krypsis.symbian.command.location.GetLocationCommand;
import org.krypsis.symbian.command.location.StartLocationObserverCommand;
import org.krypsis.symbian.command.location.StopLocationObserverCommand;
import org.krypsis.symbian.command.photo.TakeAndUploadCommand;
import org.krypsis.symbian.http.DefaultRequestMatcher;
import org.krypsis.symbian.http.HttpServer;
import org.krypsis.symbian.http.ServerRequestListener;
import org.krypsis.util.Queue;

import javax.microedition.midlet.MIDlet;
import java.util.Hashtable;


public class KrypsisApplication extends MIDlet implements RootApplication, Runnable, ServerRequestListener, ResponseDispatchable {
  public static final String ENTRY_URL = "http://demo.krypsis.org/demo/index.html";
  private final Logger logger = LoggerFactory.getLogger(KrypsisApplication.class);
  private Thread uiThread;
  private Browser browser = null;
  private boolean exiting = false;
  private int style = 2;  //MobileShell.SMALL_STATUS_PANE
  private HttpServer server;
  private final Hashtable scope = new Hashtable();
  private final Queue requestQueue = new Queue(32);
  private final Queue responseQueue = new Queue(32);

  public KrypsisApplication() {
    final ModuleRegistry moduleRegistry = ModuleRegistry.getInstance();

    final Module base = new Module(this, ModuleContext.BASE);
    base.registerCommand(Bases.GET_META_DATA, GetMetaDataCommand.class);
    base.registerCommand(Bases.RELOAD, ReloadCommand.class);
    base.registerCommand(Bases.LOAD, LoadCommand.class);
    base.registerCommand(Bases.RESET, ResetCommand.class);

    final Module screen = new Module(this, ModuleContext.SCREEN);
    screen.registerCommand(Screens.GET_INFO, GetInfoCommand.class);
    screen.registerCommand(Screens.START_OBSERVE_ORIENTATION, StartObserveOrientationCommand.class);
    screen.registerCommand(Screens.STOP_OBSERVE_ORIENTATION, StopObserveOrientationCommand.class);

    final Module location = new Module(this, ModuleContext.LOCATION);
    location.registerCommand(Locations.GET_LOCATION, GetLocationCommand.class);
    location.registerCommand(Locations.START_OBSERVE_LOCATION, StartLocationObserverCommand.class);
    location.registerCommand(Locations.STOP_OBSERVE_LOCATION, StopLocationObserverCommand.class);

    final Module photo = new Module(this, ModuleContext.PHOTO);
    photo.registerCommand(Photos.TAKE_AND_UPLOAD, TakeAndUploadCommand.class);

    moduleRegistry.register(base);
    moduleRegistry.register(screen);
    moduleRegistry.register(location);
    moduleRegistry.register(photo);
  }

  public void startApp() {
    if (uiThread == null) {
      uiThread = new Thread(this);
      uiThread.start();
    }
  }

  public void pauseApp() {}

  public void destroyApp(boolean unconditional) {
    exitEventLoop();
    try {
      uiThread.join();
    }
    catch (InterruptedException e) {
    }
  }

  void exitEventLoop() {
    exiting = true;
    Display.getDefault().wake();
  }

  public void run() {
    Display display = new Display();
    MobileShell shell = new MobileShell(display, SWT.NONE, style);
    shell.setLayout(new FillLayout());
    try {
      browser = new Browser(shell, SWT.NONE);
    }
    catch (SWTError e) {
      MessageBox message = new MessageBox(shell, SWT.NONE);
      message.setText("SWTError: " + e.getMessage());
      message.open();
    }

    startLocalServer();

    if (browser != null) {
      browser.setUrl(ENTRY_URL);
      browser.addLocationListener(new LocationListener() {
        public void changing(LocationEvent locationEvent) {
          /* Log it in case this callback is ever fixed and working */
          logger.warn("changing was called: " + locationEvent.location);
        }

        public void changed(LocationEvent locationEvent) {
          if (locationEvent.location.equals(ENTRY_URL)) {
            browser.setUrl("javascript:Krypsis.Device.connector = Krypsis.Device.SymbianConnector");
            browser.setUrl("javascript:Krypsis.Device.SymbianConnector.port = " + server.getPort());
          }
        }
      });
    }

    shell.open();

    RequestResponseThread requestResponseThread = new RequestResponseThread(this, browser, requestQueue, responseQueue);
    requestResponseThread.start();

    while (!exiting) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
    requestResponseThread.setRunning(false);
    try {
      requestResponseThread.join();
    }
    catch (InterruptedException e) {}
    
    browser.dispose();  // dispose objects
    shell.dispose();
    display.dispose();
    notifyDestroyed();  // exit MIDlet
  }

  

  public void dispatch(Response response) {
    responseQueue.add(response);
  }

  public void onServerRequest(String url) {
    try {
      final Request request = new Request(url);
      requestQueue.add(request);
    }
    catch (RequestException e) {
      logger.warn("The requested ENTRY_URL " + url + " is not a krypsis request");
    }
  }

  private void startLocalServer() {
    this.server = new HttpServer(new DefaultRequestMatcher(), 50000);
    server.setRequestListener(this);
    new Thread(server).start();
  }

  public void put(String key, Object value) throws IllegalArgumentException {
    if (scope.containsKey(key)) {
      throw new IllegalArgumentException("Key " + key + " is already set in the global scope");
    }

    //noinspection unchecked
    this.scope.put(key, value);
  }

  public Object get(String key) {
    return scope.get(key);
  }

  public Object remove(String key) {
    return scope.remove(key);
  }

  public void reset() {
    ModuleRegistry.getInstance().reset();
  }

  public Browser getBrowser() {
    return browser;
  }
}