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

package org.krypsis.symbian.command.photo;

import org.krypsis.command.photo.PhotoListener;
import org.krypsis.command.Photos;
import org.krypsis.http.request.Requestable;
import org.krypsis.http.request.RequestException;
import org.krypsis.http.response.ErrorResponse;
import org.krypsis.http.response.ResponseDispatchable;
import org.krypsis.http.response.Response;
import org.krypsis.http.response.SuccessResponse;
import org.krypsis.http.upload.*;
import org.krypsis.log.Logger;
import org.krypsis.log.LoggerFactory;
import org.krypsis.module.Command;
import org.krypsis.module.Module;
import org.krypsis.symbian.http.upload.HttpConnector;

import javax.microedition.io.HttpConnection;
import javax.microedition.io.Connector;
import java.util.Date;

/**
 * Date: 30.05.2009
 *
 * Takes a photo and uploads it to a remote server
 */
public class TakeAndUploadCommand implements Command, PhotoListener {
  private final Logger logger = LoggerFactory.getLogger(TakeAndUploadCommand.class);
  private Module module;
  private Requestable request;
  private ResponseDispatchable dispatable;
  private String uploadUrl;
  private String parameterName;
  private String fileName;

  public void execute(Module module, Requestable request, ResponseDispatchable dispatchable) {
    try {
      this.module = module;
      this.request = request;
      this.dispatable = dispatchable;

      this.uploadUrl = request.getParameter(Photos.UPLOAD_URL);
      this.parameterName = request.getParameter(Photos.PARAMETER_NAME);
      this.fileName = request.getParameter(Photos.FILENAME);

      if (uploadUrl == null || "".equals(uploadUrl)) {
        logger.warn("The upload url is missing.");
        throw new RequestException(ErrorResponse.INVALID_PARAMETER);
      }

      if (parameterName == null) {
        parameterName = "file";
      }

      if (fileName == null) {
        fileName = "filename" + (new Date()).getTime();
      }

      new PhotoApplication(this);
    }
    catch (Exception e) {
      logger.error("An unknown error occured: " + e);
      dispatchable.dispatch(new ErrorResponse(request, ErrorResponse.UNKNOWN));
    }
  }

  public void onPhoto(byte[] bytes) {
    Response response;
    try {
      Uploadable uploadable = new HttpUploader();
      final Content jpegImage = new JpegImage(bytes, fileName, parameterName);
      final HttpConnection connection = (HttpConnection) Connector.open(uploadUrl, Connector.READ_WRITE);
      final Connectable connector = new HttpConnector(connection);
      final String serverResponse = uploadable.upload(connector, jpegImage);
      response = new SuccessResponse(request);
      response.setParameter(Photos.RESPONSE, serverResponse);
    }
    catch (Exception e) {
      response = new ErrorResponse(request, ErrorResponse.NETWORK);
    }

    getDispatable().dispatch(response);
  }

  public void onCancel() {
    getDispatable().dispatch(new ErrorResponse(getRequest(), ErrorResponse.CANCELED_BY_USER));
  }

  public void onError(int code) {
    getDispatable().dispatch(new ErrorResponse(getRequest(), code));
  }

  public Module getModule() {
    return module;
  }

  public Requestable getRequest() {
    return request;
  }

  public ResponseDispatchable getDispatable() {
    return dispatable;
  }

  public String getUploadUrl() {
    return uploadUrl;
  }

  public String getParameterName() {
    return parameterName;
  }

  public String getFileName() {
    return fileName;
  }
}
