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

package org.krypsis.blackberry;

import net.rim.blackberry.api.browser.Browser;
import net.rim.blackberry.api.browser.BrowserSession;
import net.rim.device.api.io.http.HttpFilterRegistry;
import net.rim.device.api.ui.UiApplication;

/**
 * Date: 03.04.2009
 *
 * This small ui application is responsible for
 * registering the krypsis url filter. On startup it will request
 * the default session of the browser and point it to
 * the Mantoida url. On incoming http request this application will
 * be deactivated by the OS and will close itself.
 */
public class Main extends UiApplication {
  public static final String HOST = "georize.de:9494";
  public static final String URL = "http://" + HOST + "/demo/index.html;usefilter=false";

  private final BrowserSession browserSession;

  public Main(BrowserSession browserSession) {
    this.browserSession = browserSession;
  }

  public static void main(String[] args) {
    HttpFilterRegistry.registerFilter(HOST, "org.krypsis.blackberry.http");

    final BrowserSession session = Browser.getDefaultSession();
    session.displayPage(URL);

    final Main main = new Main(session);
    main.enterEventDispatcher();
  }

  public void activate() {
    super.activate();
    getBrowserSession().showBrowser();
  }

  public void deactivate() {
    super.deactivate();
    System.exit(0);
  }

  public BrowserSession getBrowserSession() {
    return browserSession;
  }
}
