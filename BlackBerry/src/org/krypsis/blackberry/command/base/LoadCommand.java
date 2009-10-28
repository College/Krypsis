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
import org.krypsis.http.request.Requestable;
import org.krypsis.http.response.ErrorResponse;
import org.krypsis.http.response.ResponseDispatchable;
import org.krypsis.http.response.SuccessResponse;
import org.krypsis.module.Module;
import org.krypsis.module.Command;
import org.krypsis.command.Bases;

/**
 * Date: 12.05.2009
 */
public class LoadCommand implements Command {
  

  public void execute(Module module, Requestable request, ResponseDispatchable dispatchable) {
    final String url = request.getParameter(Bases.URL);
    if (url == null) {
      dispatchable.dispatch(new ErrorResponse(request, ErrorResponse.INVALID_PARAMETER));
    }
    else {
      final BlackBerryApplication app = (BlackBerryApplication) module.getRootApplication();
      final BrowserSession session = app.getBrowserSession(); 
      session.displayPage(url);
      dispatchable.dispatch(new SuccessResponse(request));
    }
  }
}
