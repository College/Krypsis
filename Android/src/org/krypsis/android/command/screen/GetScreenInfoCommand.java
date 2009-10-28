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

package org.krypsis.android.command.screen;

import org.krypsis.module.Command;
import org.krypsis.module.Module;
import org.krypsis.http.request.Requestable;
import org.krypsis.http.response.ResponseDispatchable;
import org.krypsis.http.response.SuccessResponse;
import org.krypsis.http.response.Response;
import org.krypsis.android.Application;
import org.krypsis.command.Screens;
import android.util.DisplayMetrics;
import android.view.Display;

public class GetScreenInfoCommand implements Command {

  public void execute(Module module, Requestable request, ResponseDispatchable dispatchable) {
    final Response response = new SuccessResponse(request);
    final Application application = (Application) module.getRootApplication();
    final Display display = application.getWindowManager().getDefaultDisplay();
    final DisplayMetrics metrics = new DisplayMetrics();

    display.getMetrics(metrics);
    final int orientation = display.getOrientation();

    response.setParameter(Screens.WIDTH, metrics.widthPixels);
    response.setParameter(Screens.HEIGHT, metrics.heightPixels);
    response.setParameter(Screens.ORIENTATION, orientation);

    dispatchable.dispatch(response);
  }
}
