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

import android.hardware.Camera;
import android.view.SurfaceHolder;

import java.io.IOException;

public class ImageCaptureListener implements SurfaceHolder.Callback {
  private final ImagePreviewer previewer;
  private Camera camera;

  public ImageCaptureListener(ImagePreviewer previewer) {
    this.previewer = previewer;
  }

  public void surfaceCreated(SurfaceHolder holder) {
    camera = Camera.open();
    try {
      camera.setPreviewDisplay(holder);
    }
    catch (IOException e) {
      camera.release();
    }
  }

  public void surfaceDestroyed(SurfaceHolder holder) {
    camera.stopPreview();
    camera.release();
    camera = null;
  }

  public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
    Camera.Parameters parameters = camera.getParameters();
    parameters.setPreviewSize(w, h);
    camera.setParameters(parameters);
    camera.startPreview();
  }

  public ImagePreviewer getPreviewer() {
    return previewer;
  }

  public void takePicture(Camera.PictureCallback callback) {
    camera.takePicture(null, null, callback);
  }
}
