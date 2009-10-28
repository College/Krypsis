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

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.Screen;

import org.krypsis.command.photo.PhotoListener;

/**
 * Date: 16.04.2009
 *
 * The photo application will be created when a new camera
 * picture should be shot. The application creates at first a capture
 * screen where the user is seeing the moving camera picture. When the
 * user is ready to shoot a photo, he press the "take photo" button
 * and will viewing the result. If the result is not good enought,
 * he will be able to retake the photo. After he is finishished the application
 * returns to the previous screen and pass the picture bytearray to the caller.
 */
public class PhotoApplication implements SnapshotListener, PhotoPreviewListener {
  private final CaptureScreen captureScreen;
  private final UiApplication uiApplication;
  private final PhotoListener photoListener;
  private ImageScreen imageScreen;

  public PhotoApplication(PhotoListener photoListener) {
    this.photoListener = photoListener;
    this.captureScreen = new CaptureScreen(this);
    this.uiApplication = UiApplication.getUiApplication();

    this.uiApplication.invokeAndWait(new Runnable() {
      public void run() {
        uiApplication.pushScreen(captureScreen);
      }
    });
  }

  public void onTakeSnapshot(byte[] bytes) {
    this.imageScreen = new ImageScreen(this, bytes);

    //Push this screen to display it to the user.
    pushScreen(imageScreen);
  }

  public void onCancelSnapshot() {
    popScreen(captureScreen);
    getPhotoListener().onCancel();
  }

  public void onSavePhoto(byte[] raw) {
    this.uiApplication.invokeAndWait(new Runnable() {
      public void run() {
        uiApplication.popScreen(imageScreen);
        uiApplication.popScreen(captureScreen);
      }
    });
    getPhotoListener().onPhoto(raw);
  }

  public void onDiscardPhoto() {
    popScreen(imageScreen);
  }

  public CaptureScreen getCaptureScreen() {
    return captureScreen;
  }

  public UiApplication getUiApplication() {
    return uiApplication;
  }

  public PhotoListener getPhotoListener() {
    return photoListener;
  }

  private void popScreen(Screen screen) {
    UiApplication.getUiApplication().popScreen(screen);
  }

  private void pushScreen(Screen screen) {
    UiApplication.getUiApplication().pushScreen(screen);
  }
}
