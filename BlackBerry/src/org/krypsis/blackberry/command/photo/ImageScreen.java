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

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;

public final class ImageScreen extends MainScreen {
  private final PhotoPreviewListener photoPreviewListener;

  public ImageScreen(final PhotoPreviewListener photoPreviewListener, final byte[] raw) {
    this.photoPreviewListener = photoPreviewListener;
    //Convert the byte array to a Bitmap image.
    Bitmap image = Bitmap.createBitmapFromBytes(raw, 0, -1, 7);

    //Create two field managers to center the screen's contents.
    HorizontalFieldManager hfm1 = new HorizontalFieldManager(Field.FIELD_HCENTER);
    HorizontalFieldManager hfm2 = new HorizontalFieldManager(Field.FIELD_HCENTER);

    //Create the field that contains the image.
    BitmapField imageField = new BitmapField(image);
    hfm1.add(imageField);

    //Create the SAVE button which returns the user to the main camera.
    //screen and saves the picture as a file
    ButtonField photoButton = new ButtonField("Save");
    photoButton.setChangeListener(new FieldChangeListener() {
      public void fieldChanged(Field field, int i) {
        getPhotoPreviewListener().onSavePhoto(raw);
      }
    });
    hfm2.add(photoButton);

    //Create the CANCEL button which returns the user to the main camera
    //screen without saving the picture.
    ButtonField cancelButton = new ButtonField("Cancel");
    cancelButton.setChangeListener(new FieldChangeListener() {
      public void fieldChanged(Field field, int i) {
        getPhotoPreviewListener().onDiscardPhoto();
      }
    });
    hfm2.add(cancelButton);

    //Add the field managers to the screen.
    add(hfm1);
    add(hfm2);
  }

  public PhotoPreviewListener getPhotoPreviewListener() {
    return photoPreviewListener;
  }

  /**
   * Handle trackball click events.
   *
   * @see net.rim.device.api.ui.Screen#invokeAction(int)
   */
  protected boolean invokeAction(int action) {
    boolean handled = super.invokeAction(action);

    if (!handled) {
      switch (action) {
        case ACTION_INVOKE: // Trackball click.
        {
          return true;
        }
      }
    }
    return handled;
  }
}