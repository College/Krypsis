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

package org.krypsis.symbian.command.base;

import org.krypsis.command.Bases;
import org.krypsis.http.request.Requestable;
import org.krypsis.http.response.ErrorResponse;
import org.krypsis.http.response.Response;
import org.krypsis.http.response.ResponseDispatchable;
import org.krypsis.http.response.SuccessResponse;
import org.krypsis.module.Command;
import org.krypsis.module.Module;
import org.krypsis.symbian.KrypsisApplication;

/**
 * Date: 28.05.2009
 *
 * Loads the given url into the browser.
 */
public class LoadCommand implements Command {

  public void execute(Module module, Requestable request, ResponseDispatchable dispatchable) {
    final KrypsisApplication root = (KrypsisApplication) module.getRootApplication();
    final String url = request.getParameter(Bases.URL);

    Response response;
    if (url == null || "".equals(url)) {
      response = new ErrorResponse(request, ErrorResponse.INVALID_PARAMETER);
    }
    else if (root == null) {
      response = new ErrorResponse(request, ErrorResponse.CAPABILITY_NOT_SUPPORTED);
    }
    else {
      root.getBrowser().setUrl(url);
      response = new SuccessResponse(request);
    }

    dispatchable.dispatch(response);
  }
}
