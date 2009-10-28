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
 * This module defines some functions to retrieve the current location.
 * Of cource this is only possible if the device provides GPS data. 
 * </p>
 *
 * @static
 * @class The location module to get the users gps location.
 * @since 0.1
 * @depends ../krypsis.js
 */
Krypsis.Device.Location = {

  /**
   * @private
   */
  context: 'location',

  /**
   * <p>
   * Retrieves the last known gps location.
   * Notice: The timeout is set to 60 seconds here. If you need
   * more or less time then provide the "timeout" parameter.
   * </p>
   *
   * <p>
   * The following parameters are supported:
   * <ul>
   * <li><strong>accuracy</strong>: The minimal accuracy in meters before location shall be determined</li>
   * </ul>
   * </p>
   *
   * <p>
   * The onSuccess callback will provide an object with the following properties:
   *
   * <ul>
   * <li><strong>latitude</strong></li>
   * <li><strong>longitude</strong></li>
   * <li><strong>accuracy</strong></li>
   * <li><strong>altitude</strong></li>
   * <li><strong>timestamp</strong></li>
   * </ul>
   * </p>
   *
   * @example
   * <pre>
   *  Krypsis.Device.Location.getLocation({
   *    timeout: 60000,
   *
   *    onSuccess: function(result) {
   *      Krypsis.$('longitude').innerHTML = result.longitude;
   *      Krypsis.$('latitude').innerHTML  = result.latitude;
   *    }
   *  });
   *
   *  // or
   *
   *  var task = Krypsis.Device.Location.getLocation({...}, false);
   *  task.execute();
   * </pre>
   *
   * @param {Object} options The parameters and callbacks
   * @param {Boolean} execute Shold the task be executed after creation. Default is true
   *
   * @since 0.1
   * @public
   */
  getLocation: function(options, execute) {
    return Krypsis.Device.createTask(this, "getlocation", options, execute);
  },

  /**
   * <p>
   * Start observing the current location. You can specify
   * the observing time in ms and a callback to get informed
   * about the the current location.
   * </p>
   *
   * <p>
   * You can stop this listener by calling the {@link Krypsis.Device.Location.stopObserveLocation()}
   * function. Note that this function can only be used as a switch.
   * Which means that only one observer is supported at the same time.
   * After executing startObserveLocation() you have to call stopObserveLocation() to
   * reuse this function.
   * </p>
   *
   * <p>
   * The following parameters are supported:
   * <ul>
   * <li><strong>interval</strong>: The update period in ms. The default value is 10000 => 10 seconds.</li>
   * <li><strong>distance</strong>: The minimal distance in meters between two different points</li>
   * <li><strong>accuracy</strong>: The minimal accuracy in meters before location shall be determined</li>
   * </ul>
   * </p>
   *
   * <p>
   * The onSuccess callback will provide an object with the following properties:
   *
   * <ul>
   * <li><strong>latitude</strong></li>
   * <li><strong>longitude</strong></li>
   * <li><strong>accuracy</strong></li>
   * <li><strong>altitude</strong></li>
   * <li><strong>timestamp</strong></li>
   * </ul>
   * </p>
   *
   * @example
   * <pre>
   *  Krypsis.Device.Location.startObserveLocation({
   *    parameters: {
   *      // each 0.5 secs
   *      interval: 500,
   *      // 100 meters difference between the two points
   *      distance: 100
   *    },
   *    onSuccess: function(result) {
   *      Krypsis.$('longitude').innerHTML = result.longitude;
   *      Krypsis.$('latitude').innerHTML  = result.latitude;
   *    }
   *  });
   *
   *  // or
   *
   *  var task = Krypsis.Device.Location.startObserveLocation({...}, false);
   *  task.execute();
   * </pre>

   *
   * @param {Object} options properties and callbacks
   * @param {Boolean} execute Shold the task be executed after creation. Default is true
   *
   * @since 0.1
   * @public
   */
  startObserveLocation: function(options, execute) {
    return Krypsis.Device.createTask(this, "startobservelocation", options, execute);
  },

  /**
   * <p>
   * Stop the location obervation.
   * </p>
   *
   * <p>
   * The onSuccess callback is called, if the observation is
   * stopped successfully. No parameters and arguments expected
   * </p>
   *
   * @example
   * <pre>
   *  Krypsis.Device.Location.stopObserveLocation({});
   *
   *  // or
   *
   *  var task = Krypsis.Device.Location.stopObserveLocation({...}, false);
   *  task.execute();
   * </pre>
   *
   * @param {Object} options The arguments. Currently not supported and ignored
   * @param {Boolean} execute Shold the task be executed after creation. Default is true
   *
   * @since 0.1
   * @public
   */
  stopObserveLocation: function(options, execute) {
    return Krypsis.Device.createTask(this, "stopobservelocation", options, execute);
  }
};