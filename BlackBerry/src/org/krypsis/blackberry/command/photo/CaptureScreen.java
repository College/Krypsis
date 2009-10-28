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

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import org.krypsis.log.Logger;
import org.krypsis.log.LoggerFactory;

import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;

/**
 * Date: 16.04.2009
 */
public class CaptureScreen extends MainScreen {
  private Logger logger = LoggerFactory.getLogger(CaptureScreen.class);
  private SnapshotListener snapshotListener;
  private VideoControl videoControl;
  private Field videoField;

  public CaptureScreen(SnapshotListener snapshotListener) {
    this.snapshotListener = snapshotListener;

    initializeCamera();
    createUI();

    String encodingString = System.getProperty("video.snapshot.encodings");
    // ﻿encoding=rgb565 encoding=jpeg&width=2048&height=1536&quality=superfine encoding=jpeg&width=2048&height=1536&quality=normal encoding=jpeg&width=2048&height=1536&quality=fine encoding=jpeg&width=1024&height=768&quality=normal encoding=jpeg&width=1024&height=768&quality=fine encoding=jpeg&width=1024&height=768&quality=superfine encoding=jpeg&width=640&height=480&quality=normal encoding=jpeg&width=640&height=480&quality=fine encoding=jpeg&width=640&height=480&quality=superfine
    logger.debug("Encodings: " + encodingString);
  }

  protected void initializeCamera() {
    try {
      Player player = Manager.createPlayer("capture://video");

      //Set the player to the REALIZED state (see Player docs.)
      player.realize();

      //Grab the video control and set it to the current display.
      this.videoControl = (VideoControl) player.getControl("VideoControl");

      if (videoControl != null) {
        //Create the video field as a GUI primitive (as opposed to a
        //direct video, which can only be used on platforms with
        //LCDUI support.)
        this.videoField = (Field) videoControl.initDisplayMode(VideoControl.USE_GUI_PRIMITIVE, "net.rim.device.api.ui.Field");
        //Display the video control
        //videoControl.setDisplayFullScreen(true);
        videoControl.setVisible(true);
      }

      //Set the player to the STARTED state (see Player docs.)
      player.start();
    }
    catch (Exception e) {
      logger.error("An unknown error occured: " + e);
    }
  }

  /**
   * Adds the VideoField and the "Take Photo" button to the screen.
   */
  private void createUI() {
    //Add the video field to the screen.
    add(videoField);

    //Initialize the button used to take photos.
    ButtonField photoButton = new ButtonField("Take Photo");
    photoButton.setChangeListener(new FieldChangeListener() {
      /**
       * When the "Take Photo" button is pressed, extract the image
       * from the VideoControl using getSnapshot() and push a new
       * screen to display the image to the user.
       */
      public void fieldChanged(Field field, int context) {
        try {
          final byte[] snapshot = videoControl.getSnapshot(null);
          snapshotListener.onTakeSnapshot(snapshot);
        }
        catch (Exception e) {
          logger.error("An unknown error occured during taking a photo");
        }
      }
    });

    //The HorizontalFieldManager keeps the button in the center of
    //the screen.
    HorizontalFieldManager hfm = new HorizontalFieldManager(Field.FIELD_HCENTER);
    hfm.add(photoButton);

    //Add the FieldManager containing the button to the screen.
    add(hfm);
  }
}