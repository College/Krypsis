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

package org.krypsis.blackberry.command.photo;

import org.krypsis.http.request.Requestable;
import org.krypsis.http.request.RequestException;
import org.krypsis.http.response.ErrorResponse;
import org.krypsis.http.response.Response;
import org.krypsis.http.response.ResponseDispatchable;
import org.krypsis.http.response.SuccessResponse;
import org.krypsis.http.upload.*;
import org.krypsis.module.Command;
import org.krypsis.module.Module;
import org.krypsis.blackberry.http.upload.HttpConnector;
import org.krypsis.command.Photos;
import org.krypsis.command.photo.PhotoListener;
import org.krypsis.log.LoggerFactory;
import org.krypsis.log.Logger;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import java.util.Date;

import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.system.JPEGEncodedImage;
import net.rim.device.api.math.Fixed32;

/**
 * Date: 13.05.2009
 *
 * Takes a picture from camera and uploads it to
 * a remote server address. The server response will
 * be passed to the callback.
 */
public class TakeAndUploadCommand implements Command, PhotoListener {
  private final Logger logger = LoggerFactory.getLogger(TakeAndUploadCommand.class);
  public static final int MAX_WIDTH = 1024;
  public static final int MAX_HEIGHT = 1024;

  /** The response dispatcher */
  private ResponseDispatchable dispatchable;

  /** The target upload url */
  private String uploadUrl;

  /** The parameter name to link with the uploaded photo */
  private String parameterName;
  /** The name of the image. */
  private String fileName;
  private Requestable request;

  public void execute(Module module, Requestable request, ResponseDispatchable dispatchable) {
    try {
      this.dispatchable = dispatchable;
      this.request = request;

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
    catch (RequestException re) {
      dispatchable.dispatch(new ErrorResponse(request, re.getReason()));
    }
    catch (Exception e) {
      logger.error("An unknown error occured: " + e);
      dispatchable.dispatch(new ErrorResponse(request, ErrorResponse.UNKNOWN));
    }
  }

  public void onPhoto(final byte[] bytes) {
    final Runnable runnable = new Runnable() {
      public void run() {
        Response response;
        try {
          EncodedImage image = JPEGEncodedImage.createEncodedImage(bytes, 0, bytes.length);
          image = resize(image, MAX_WIDTH, MAX_HEIGHT);
          Uploadable uploadable = new HttpUploader();
          final Content jpegImage = new JpegImage(image.getData(), fileName, parameterName);
          final HttpConnection connection = (HttpConnection) Connector.open(uploadUrl, Connector.READ_WRITE);
          final Connectable connector = new HttpConnector(connection);
          final String serverResponse = uploadable.upload(connector, jpegImage);
          response = new SuccessResponse(request);
          response.setParameter(Photos.RESPONSE, serverResponse);
        }
        catch (Exception e) {
          response = new ErrorResponse(request, ErrorResponse.NETWORK);
        }

        dispatchable.dispatch(response);
      }
    };

    try {
      final Thread thread = new Thread(runnable);
      thread.start();
      thread.join();
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void onCancel() {
    final Response response = new ErrorResponse(request, ErrorResponse.CANCELED_BY_USER);
    dispatchable.dispatch(response);
  }

  public void onError(int code) {
    final Response response = new ErrorResponse(request, code);
    dispatchable.dispatch(response);
  }

  protected EncodedImage resize(EncodedImage image, int maxWidth, int maxHeight) {
    if (maxWidth <= 0 || maxHeight <= 0) {
      return image;
    }

    final int width = image.getWidth();
    final int height = image.getHeight();

    if (maxWidth >= width && maxHeight >= height) {
      return image;
    }

    image = scaleImageToWidth(image, maxWidth);
    return scaleImageToHeight(image, maxHeight);
  }

  public static EncodedImage scaleImageToWidth(EncodedImage encoded, int newWidth) {
    return scaleToFactor(encoded, encoded.getWidth(), newWidth);
  }

  public static EncodedImage scaleImageToHeight(EncodedImage encoded, int newHeight) {
    return scaleToFactor(encoded, encoded.getHeight(), newHeight);
  }

  public static EncodedImage scaleToFactor(EncodedImage encoded, int curSize, int newSize) {
    int numerator = Fixed32.toFP(curSize);
    int denominator = Fixed32.toFP(newSize);
    int scale = Fixed32.div(numerator, denominator);

    return encoded.scaleImage32(scale, scale);
  }
}
