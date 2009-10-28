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

package org.krypsis.android.command.photo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import org.krypsis.android.Application;
import org.krypsis.android.activity.ActivityResultListener;
import org.krypsis.android.http.HttpConnector;
import org.krypsis.command.Photos;
import org.krypsis.http.request.RequestException;
import org.krypsis.http.request.Requestable;
import org.krypsis.http.response.ErrorResponse;
import org.krypsis.http.response.Response;
import org.krypsis.http.response.ResponseDispatchable;
import org.krypsis.http.response.SuccessResponse;
import org.krypsis.http.upload.*;
import org.krypsis.log.Logger;
import org.krypsis.log.LoggerFactory;
import org.krypsis.module.Command;
import org.krypsis.module.Module;

import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class TakeAndUploadCommand implements Command, ActivityResultListener {
  public static final int MAX_WIDTH = 1024;
  public static final int MAX_HEIGHT = 768;
  private final Logger logger = LoggerFactory.getLogger(TakeAndUploadCommand.class);
  private Requestable request;
  private ResponseDispatchable dispatcher;
  private String uploadUrl;
  private String parameterName;
  private String fileName;
  private Application application;

  public void execute(Module module, Requestable request, ResponseDispatchable dispatchable) {
    try {
      this.request = request;
      this.dispatcher = dispatchable;

      prepareParameters(request);

      this.application = (Application) module.getRootApplication();
      application.addActivityResultListener(this);
      application.startActivityForResult(new Intent(application, ImagePreviewer.class), ImagePreviewer.ID);
    }
    catch (RequestException e) {
      dispatchable.dispatch(new ErrorResponse(request, e.getReason()));
    }
    catch (Exception e) {
      dispatchable.dispatch(new ErrorResponse(request, ErrorResponse.UNKNOWN));
    }
  }

  /**
   * Will be called if the application returned after the
   * user took a camera picture or canceled the operation
   * 
   * @param requestCode The code that is associated with the activity.
   * @param resultCode The result code of the activity execution
   * @param intent The intent that contains data
   */
  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    if (requestCode == ImagePreviewer.ID) {
      this.application.removeActivityResultListener(this);
      
      if (Activity.RESULT_OK == resultCode) {
        if (intent != null) {
          onPhoto(intent.getByteArrayExtra(Intent.EXTRA_STREAM));
        }
        else {
          onError(ErrorResponse.UNKNOWN);
        }
      }
      else {
        onCancel();
      }
    }
  }

  /**
   * Will be called, when the user took a picture and accepted it.
   * The received parameter contains the picture bytes.
   * 
   * @param bytes The taken picture from camera
   */
  public void onPhoto(byte[] bytes) {
    Response response;
    try {
      final Bitmap bitmap = toBitmap(bytes);
      final Bitmap resized = resize(bitmap, MAX_WIDTH, MAX_HEIGHT);
      bytes = toByteArray(resized);

      final Content jpegImage = new JpegImage(bytes, getFileName(), getParameterName());
      final URL url = new URL(getUploadUrl());

      final Uploadable uploader = new HttpUploader();
      final Connectable connector = new HttpConnector((HttpURLConnection) url.openConnection());
      final String serverResponse = uploader.upload(connector, jpegImage);
      
      response = new SuccessResponse(getRequest());
      response.setParameter(Photos.RESPONSE, serverResponse);
    }
    catch (Exception e) {
      response = new ErrorResponse(getRequest(), ErrorResponse.NETWORK);
    }

    dispatcher.dispatch(response);
  }

  public void onCancel() {
    onError(ErrorResponse.CANCELED_BY_USER);
  }

  public void onError(int code) {
    final Response errorResponse = new ErrorResponse(getRequest(), code);
    getDispatcher().dispatch(errorResponse);
  }

  public Requestable getRequest() {
    return request;
  }

  public ResponseDispatchable getDispatcher() {
    return dispatcher;
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

  /**
   * Extracts the parameters from the request and calculates
   * the missing parameters like file name and parameter name
   *
   * @param request The request to extract the parameters from
   * @throws RequestException If one of the required parameters is missing
   */
  private void prepareParameters(Requestable request) throws RequestException {
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
  }

  /**
   * Converts the given byte array to a bitmap.
   *
   * @param data The byte array to convert
   * @return The bitmap created from the byte array
   */
  private Bitmap toBitmap(byte[] data) {
    return BitmapFactory.decodeByteArray(data, 0, data.length);
  }

  /**
   * Converts the given bitmap to an array of bytes.
   *
   * @param bitmap The bitmap to convert.
   * @return The bitmap as byte array
   */
  private byte[] toByteArray(Bitmap bitmap) {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 95, stream);
    return stream.toByteArray();
  }

  /**
   * Resize the given bitmap to the maximum size.
   *
   * Example: If the given bitmap has a size if 1600x1600
   * and should be resized to the maximum of 1024x768 then
   * the result will have a size of 768x768.
   * That is because the maximum height is 768 and the scale
   * factor is 0.48
   *
   * @param bitmap The bitmap to resize
   * @param maxWidth The maximum width
   * @param maxHeight The maximum height
   * @return The resized bitmap, or, if the given bitmap
   * size is smaller, the original reference.
   */
  private Bitmap resize(Bitmap bitmap, int maxWidth, int maxHeight) {
    final int width = bitmap.getWidth();
    final int height = bitmap.getHeight();

    if (maxWidth >= width && maxHeight >= height) {
      return bitmap;
    }

    final float scale = Math.min(((float) maxWidth) / width, ((float) maxHeight) / height);
    Matrix matrix = new Matrix();
    matrix.postScale(scale, scale);
    logger.debug("Use scale factor: " + scale);

    Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    logger.debug("Bitmap resized from " + width + "x" + height + " to " + resizedBitmap.getWidth() + "x" + resizedBitmap.getHeight());

    return resizedBitmap;
  }
}
