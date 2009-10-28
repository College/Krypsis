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

package org.krypsis.android.command.base;

import org.krypsis.module.Command;
import org.krypsis.module.Module;
import org.krypsis.http.request.Requestable;
import org.krypsis.http.response.ResponseDispatchable;
import org.krypsis.http.response.SuccessResponse;
import org.krypsis.android.R;
import org.krypsis.android.Application;
import org.krypsis.android.db.DatabaseManager;
import org.krypsis.command.Bases;
import org.krypsis.util.Identity;
import org.krypsis.util.LocaleMatcher;
import org.krypsis.log.LoggerFactory;
import org.krypsis.log.Logger;

import java.util.Locale;

public class GetMetaDataCommand implements Command {
  private final Logger logger = LoggerFactory.getLogger(GetMetaDataCommand.class);

  public void execute(Module module, Requestable request, ResponseDispatchable dispatchable) {
    final Application application = (Application) module.getRootApplication();
    final SuccessResponse response = new SuccessResponse(request);

    String krypsisId = DatabaseManager.getInstance().readApplicationInfo(Bases.KRYPSIS_ID);
    if(krypsisId == null) {
      //generate krypsisId
      krypsisId = Identity.getInstance().generate();
      DatabaseManager.getInstance().writeApplicationInfo(Bases.KRYPSIS_ID, krypsisId);
    }

    response.addParameter(Bases.KID, krypsisId);
    response.addParameter(Bases.OS, application.getText(R.string.metadata_os));
    response.addParameter(Bases.BROWSER, application.getText(R.string.metadata_browser));

    /* Get the locale of this device and provide the language code as param */
    final Locale locale = Locale.getDefault();
    if (locale != null) {
      try {
        /* Ensure the language is two-letter lower-case and country two-letter upper-case */
        final LocaleMatcher matcher = new LocaleMatcher(locale.getLanguage() + "-" + locale.getCountry());
        response.addParameter(Bases.LANGUAGE, matcher.getLanguage());
        response.addParameter(Bases.COUNTRY, matcher.getCountry());
      }
      catch (IllegalArgumentException e) {
        logger.error(e.toString());
      }
    }

    dispatchable.dispatch(response);
  }
}
