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

import org.krypsis.http.request.Requestable;
import org.krypsis.http.response.Response;
import org.krypsis.http.response.ResponseDispatchable;
import org.krypsis.http.response.SuccessResponse;
import org.krypsis.module.Module;
import org.krypsis.module.Command;
import org.krypsis.command.Bases;
import org.krypsis.blackberry.BlackBerryApplication;
import org.krypsis.util.LocaleMatcher;
import org.krypsis.log.Logger;
import org.krypsis.log.LoggerFactory;

public class GetMetaDataCommand implements Command {
  private static final String MICROEDITION_LOCALE = "microedition.locale";
  private final Logger logger = LoggerFactory.getLogger(GetMetaDataCommand.class);

  public void execute(Module module, Requestable request, ResponseDispatchable dispatchable) {
    final BlackBerryApplication blackBerryApplication = (BlackBerryApplication) module.getRootApplication();

    Response response = new SuccessResponse(request);
    response.setParameter(Bases.KID, blackBerryApplication.getKrypsisId());
    response.setParameter(Bases.OS, "blackberry");
    response.setParameter(Bases.BROWSER, "blackberry-browser");
    response.setParameter(Bases.VERSION, "0.1");

    /* Add the language and locale if available */
    try {
      final String localeString = System.getProperty(MICROEDITION_LOCALE);
      final LocaleMatcher matcher = new LocaleMatcher(localeString);
      response.setParameter(Bases.LANGUAGE, matcher.getLanguage());
      response.setParameter(Bases.COUNTRY, matcher.getCountry());
    }
    catch (IllegalArgumentException e) {
      logger.error("Could not parse the j2me locale system property: " + System.getProperty(MICROEDITION_LOCALE));
    }

    dispatchable.dispatch(response);
  }
}
