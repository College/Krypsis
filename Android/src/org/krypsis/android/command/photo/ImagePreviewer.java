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
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import org.krypsis.android.R;
import org.krypsis.log.Logger;
import org.krypsis.log.LoggerFactory;

public class ImagePreviewer extends Activity implements Camera.PictureCallback {
  public static final int ID = 1000;
  private final Logger logger = LoggerFactory.getLogger(ImagePreviewer.class);
  private byte[] photoData = null;
  private ImageCaptureListener captureListener;

  @Override
  protected void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    startCameraCapture();
  }

  private void startCameraCapture() {
    logger.debug("startCameraCapture");
    
    setContentView(R.layout.capture);
    SurfaceView captureView = (SurfaceView) findViewById(R.id.captureSurface);

    SurfaceHolder holder = captureView.getHolder();
    this.captureListener = new ImageCaptureListener(this);
    holder.addCallback(this.captureListener);
    holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    setCaptureButtonListener();
  }

  public void onPictureTaken(byte[] bytes, Camera camera) {
    logger.debug("onPictureTaken: " + bytes.length + " bytes");
    
    this.photoData = bytes;
    final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    setContentView(R.layout.preview);
    final ImageView view = (ImageView) findViewById(R.id.previewImageView);
    view.setImageBitmap(bitmap);

    setPreviewButtonListener();
  }

  public ImageCaptureListener getCaptureListener() {
    return captureListener;
  }

  /* Capture actions */

  private void setCaptureButtonListener() {
    final ImageButton cancel = (ImageButton) findViewById(R.id.cancelCapture);
    cancel.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) { onCancel(); }
    });

    final ImageButton ok = (ImageButton) findViewById(R.id.okCapture);
    ok.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) { onSnapshot(); }
    });
  }

  private void setPreviewButtonListener() {
    final ImageButton cancel = (ImageButton) findViewById(R.id.cancelPreview);
    cancel.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) { onRetake(); }
    });

    final ImageButton ok = (ImageButton) findViewById(R.id.okPreview);
    ok.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) { onUsePhoto(); }
    });
  }

  /**
   * Will be called if the user wants to cancel
   * the operation. No photo is taken and no picture
   * will be used.
   */
  private void onCancel() {
    logger.info("User canceled taking picture");
    setResult(RESULT_CANCELED);
    finish();
  }

  /**
   * Will be called if the user made a photo and
   * wants to retake the process.
   */
  private void onRetake() {
    startCameraCapture();
  }

  /**
   * Will be called if the wants to take a snapshot
   * from the current camera view.
   */
  private void onSnapshot() {
    getCaptureListener().takePicture(this);
  }

  /**
   * Will be called if the user took a picture and wants to use
   * it. In this case the activity could be finished.
   */
  private void onUsePhoto() {
    ImageButton imageButton = (ImageButton) findViewById(R.id.okPreview);
    if (imageButton != null) {
      imageButton.setImageResource(R.drawable.network);
      imageButton.setEnabled(false);
    }

    logger.info("User took photo. Finish activity and pass picture data to the application");
    getIntent().putExtra(Intent.EXTRA_STREAM, this.photoData);
    setResult(RESULT_OK, getIntent());
    finish();
  }
}
