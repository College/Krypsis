
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

package org.krypsis.http.upload;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * The http uploader is using the http protocol to upload multipart
 * data with attachments. It is almost the same code as in the android
 * module.
 */
public class HttpUploader implements Uploadable {
  private static final String CONNECTION = "Connection";

  public String upload(Connectable connectable, HttpMessage message) throws UploadException {
    try {
      final Hashtable requestProperties = message.getRequestProperties();

      if (requestProperties.size() > 0) {
        final Enumeration keys = requestProperties.keys();
        while (keys.hasMoreElements()) {
          final String key = (String) keys.nextElement();
          connectable.setRequestProperty(key, (String) requestProperties.get(key));
        }
      }
      connectable.setRequestProperty(CONNECTION, "Keep-Alive");
      connectable.write(message.toString());
      return connectable.send();
    }
    catch (IOException e) {
      throw new UploadException("The data was not successfully send", e);
    }
  }

  public String upload(Connectable connectable, Content content) throws UploadException {
    try {
      connectable.setRequestProperty(CONNECTION, "Keep-Alive");
      connectable.write(content.toString());
      return connectable.send();
    }
    catch (IOException e) {
      throw new UploadException("The data was not successfully send");
    }
  }  
}