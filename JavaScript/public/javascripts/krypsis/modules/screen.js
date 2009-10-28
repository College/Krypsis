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

/**
 * <p>
 * The screen module provides usefull information to access the screen
 * resolution and (if the device supports it) the orientation of the screen
 * in degrees.
 * </p>
 *
 * @static
 * @class Privides functions to acces the screen data
 * and observe screen events.
 * @since 0.1
 * @depends ../krypsis.js
 */
Krypsis.Device.Screen = {
  /**
   * @private
   */
  context: 'screen',

  /**
   * <p>
   * Get the information about the physical screen.
   * The result is the resolution and, if available, the
   * orientation of the screen in degrees
   * </p>
   *
   * <p>
   * The onSuccess callback will provide you an object with the following
   * properties:
   * <ul>
   * <li><strong>width</strong>: The visible width in px</li>
   * <li><strong>height</strong>: The visible height in px</li>
   * <li><strong>orientation</strong>: A value between 0 and 360 that indicates the degrees of rotation.</li>
   * </ul>
   * </p>
   *
   * @example
   * <pre>
   *  Krypsis.Device.Screen.getResolution({
   *    onSuccess: function(result) {
   *      result.width
   *      result.height
   *      result.orientation
   *    }
   *  });
   *
   *  // or
   *
   *  var task = Krypsis.Device.Screen.getResolution({...}, false);
   *  task.execute();
   * </pre>
   *
   * @param {Object} options paramerters and callbacks
   * @param {Boolean} execute Shold the task be executed after creation. Default is true
   *
   * @since 0.1
   * @public
   */
  getResolution: function(options, execute) {
    return Krypsis.Device.createTask(this, 'getresolution', options, execute);
  },

  /**
   * <p>
   * Get notified when the screen orientation is changed
   * </p>
   *
   * <p>
   * The onSuccess callback will be called each time, when this event occurs.
   * The passed object has the following properties:
   * <ul>
   * <li><strong>orientation</strong>: A value between 0 and 360 that indicates the degrees of rotation.</li>
   * </ul>
   * </p>
   *
   * @example
   * <pre>
   *  Krypsis.Device.Screen.startObserveOrientation({
   *    onSuccess: function(result) {
   *      alert("device flipped to: " + result.orientation);
   *    }
   *  });
   *
   *  // or
   *
   *  var task = Krypsis.Device.Screen.startObserveOrientation({...}, false);
   *  task.execute();
   * </pre>
   *
   * @param {Object} options The properties and callbacks
   * @param {Boolean} execute Shold the task be executed after creation. Default is true
   *
   * @since 0.1
   * @public
   */
  startObserveOrientation: function(options, execute) {
    return Krypsis.Device.createTask(this, "startobserveorientation", options, execute);
  },

  /**
   * <p>
   * Before you can use the {@link Krypsis.Device.Base.startObserveOrientation()}
   * function a second time, you have to stop the observation of the orientation
   * by calling this function.
   * </p>
   *
   * <p>
   * The onSuccess callback indicates that the event notifier is stopped
   * successfully.
   * </p>
   *
   * <p>
   * This function does not expect any parameters
   * </p>
   *
   * @example
   * <pre>
   *  Krypsis.Device.Screen.stopObserveOrientation({});
   *
   *  // or
   *
   *  var task = Krypsis.Device.Screen.stopObserveOrientation({}, false);
   *  task.execute();
   * </pre>
   *
   * @param {Object} options The parameters and callbacks
   * @param {Boolean} execute Shold the task be executed after creation. Default is true
   *
   * @since 0.1
   * @public
   */
  stopObserveOrientation: function(options, execute) {
    return Krypsis.Device.createTask(this, "stopobserveorientation", options, execute);
  }
};