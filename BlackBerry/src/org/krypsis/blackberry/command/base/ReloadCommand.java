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

package org.krypsis.blackberry.command.base;

import net.rim.blackberry.api.browser.BrowserSession;
import org.krypsis.blackberry.BlackBerryApplication;
import org.krypsis.blackberry.Main;
import org.krypsis.http.request.Requestable;
import org.krypsis.http.response.ResponseDispatchable;
import org.krypsis.http.response.SuccessResponse;
import org.krypsis.module.Module;
import org.krypsis.module.Command;

/**
 * Date: 12.05.2009
 *
 * Forces the browser to go to the entry url
 */
public class ReloadCommand implements Command {
  

  public void execute(Module module, Requestable request, ResponseDispatchable dispatchable) {
    final BlackBerryApplication blackBerryApplication = (BlackBerryApplication) module.getRootApplication();
    final BrowserSession session = blackBerryApplication.getBrowserSession();
    session.displayPage(Main.URL);
    dispatchable.dispatch(new SuccessResponse(request));
  }
}
