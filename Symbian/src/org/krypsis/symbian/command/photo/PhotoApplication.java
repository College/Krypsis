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

import org.eclipse.ercp.swt.mobile.Command;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.krypsis.command.photo.PhotoListener;
import org.krypsis.http.response.ErrorResponse;
import org.krypsis.log.Logger;
import org.krypsis.log.LoggerFactory;

import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;

/**
 * Date: 30.05.2009
 */
public class PhotoApplication implements SelectionListener {
  private final Logger logger = LoggerFactory.getLogger(PhotoApplication.class);
  private final PhotoListener photoListener;
  private Command takePictureCommand;
  private Shell shell;
  private VideoControl videoControl = null;

  public PhotoApplication(PhotoListener photoListener) {
    this.photoListener = photoListener;
    
    if (isCameraSupported()) {
      init();
    }
    else {
      photoListener.onError(ErrorResponse.CAPABILITY_NOT_SUPPORTED);
    }
  }

  /**
   * Initializes the camera application.
   */
  private void init() {
    final Display display = Display.getDefault();

    this.shell = new Shell(display.getActiveShell());
    shell.open();

    Command exitCommand = new Command(shell, Command.EXIT, 0);
    exitCommand.setText("Exit");
    exitCommand.addSelectionListener(this);

    this.takePictureCommand = new Command(shell, Command.GENERAL, 10);
    takePictureCommand.setText("Snap!");
    takePictureCommand.addSelectionListener(this);
    takePictureCommand.setDefaultCommand();

    Player player = null;
    try {
      player = Manager.createPlayer("capture://video");
      player.realize();
      this.videoControl = (VideoControl) player.getControl("VideoControl");
      Control vControl = (Control) videoControl.initDisplayMode(VideoControl.USE_GUI_PRIMITIVE, Control.class.getName());
      vControl.setParent(shell);
      Rectangle r = vControl.getBounds();
      videoControl.setDisplaySize(r.width, r.height);
      videoControl.setVisible(true);
      player.start();
    }
    catch (Exception e) {
      logger.error("Could not start capturing picture: " + e);
      getPhotoListener().onError(ErrorResponse.CAPABILITY_NOT_SUPPORTED);
      shell.dispose();
    }

    // Execute the eSWT event loop.
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }

    if (player != null) {
      player.deallocate();
      player.close();
    }
  }

  /**
   * @return Returns true if the camery is supported
   * on this device. Otherwise false
   */
  private boolean isCameraSupported() {
    final String capability = System.getProperty("supports.video.capture");
    return capability != null && capability.equals("true"); 
  }

  public void widgetSelected(SelectionEvent selectionEvent) {
    shell.dispose();
    
    if (selectionEvent.widget == takePictureCommand) {
      final byte[] bytes = takePicture();

      if (bytes == null || bytes.length == 0) {
        logger.error("Video control is null");
        getPhotoListener().onError(ErrorResponse.CAPABILITY_NOT_SUPPORTED);
      }
      else {
        logger.debug("Photo is taken. " + bytes.length + " bytes");
        getPhotoListener().onPhoto(bytes);
      }
    }
    else {
      getPhotoListener().onCancel();
    }
  }

  public void widgetDefaultSelected(SelectionEvent selectionEvent) {
    logger.debug("");
  }

  public PhotoListener getPhotoListener() {
    return photoListener;
  }

  public VideoControl getVideoControl() {
    return videoControl;
  }

  private byte[] takePicture() {
    try {
      return getVideoControl() != null ? videoControl.getSnapshot(null) : null;
    }
    catch (MediaException e) {
      logger.error("Could not take picture: " + e);
    }
    return null;
  }
}
