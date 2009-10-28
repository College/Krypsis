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
 * Some smartphones (iPhone, some Android devices, ...) provides
 * hardware sensors like accelerometers or compasses. Use this module
 * to get notified about this sensor data.
 * </p>
 *
 * @static
 * @class The sensor module to access smartphone sensors
 * @since 0.2
 * @depends ../krypsis.js
 */
Krypsis.Device.Sensor = {

  /**
   * @private
   */
  context: 'sensor',


  /**
   * <p>
   * Get notified when the device get moved physically.
   * </p>
   *
   * <div>
   * <em>Parameters</em>
   * <ul>
   * <li><strong>{Integer} frequency</strong> How often should the sensor be read?
   * The value is required and expected in ms.</li>
   * </ul>
   * </div>
   *
   * <div>
   * <em>Callbacks</em>
   * <ul>
   *  <li><strong>onSuccess</strong> Contains the sensor information.</li>
   *  <li><strong>onFailure</strong> If the device does not support this sensor.</li>
   * </li>
   * </div>
   *
   * <p>
   * The onSuccess Callback provides the following properties:
   * <ul>
   * <li><strong>{Float} x</strong> The x value of the sensor</li>
   * <li><strong>{Float} y</strong> The y value of the sensor</li>
   * <li><strong>{Float} z</strong> The z value of the sensor</li>
   * </li>
   * </p>
   *
   * @example
   * <pre>
   *  Krypsis.Device.Sensor.startObserveAccelerometer({
   *    parameters: { frequency: 100 } // 10 times per second
   *
   *    onSuccess: function(args) {
   *      // Do something with args.x, args.y and args.z
   *    },
   *    onFailure: function(args) {
   *      if (args && args.code == Krypsis.Device.Error.CAPABILITY_NOT_SUPPORTED) {
   *        alert("Accelerometer is not supported by your phone!");
   *      }
   *    }
   *  });
   *
   *  // or
   *
   *  var task = Krypsis.Device.Sensor.startObserveAccelerometer({...}, false);
   *  task.execute();
   * </pre>
   *
   * @param {Object} options The parameters and callbacks
   * @param {Boolean} execute Shold the task be executed after creation. Default is true
   *
   * @since 0.2
   * @public
   */
  startObserveAccelerometer: function(options, execute) {
    return new Krypsis.Device.createTask(this, "startobserveaccelerometer", options, execute);
  },

  /**
   * <p>
   * Stops the previously started accelerometer observer.
   * </p>
   *
   * <p>No parameters required or supported.</p>
   * <p>
   * <em>Callbacks</em>
   * <ul>
   * <li><strong>onSuccess</strong> If the observer was running and the observer is stopped now</li>
   * <li><strong>onFailure</strong> If the observer could not be stopped.</li>
   * </ul>
   * </p>
   *
   * @example
   * <pre>
   *  Krypsis.Device.Sensor.stopObserveAccelerometer({});
   *
   *  // or
   * 
   *  var task = Krypsis.Device.Sensor.stopObserveAccelerometer({...}, false);
   *  task.execute();
   * </pre>
   *
   * @param {Object} options The callbacks
   * @param {Boolean} execute Shold the task be executed after creation. Default is true
   *
   * @since 0.2
   * @public
   */
  stopObserveAccelerometer: function(options, execute) {
    return Krypsis.Device.createTask(this, "stopobserveaccelerometer", options, execute);
  }
};