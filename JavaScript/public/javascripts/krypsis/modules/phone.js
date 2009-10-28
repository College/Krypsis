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
 * The phone module gives you access to the phone core
 * functionality like sending sms or dialing a phone number.
 * </p>
 *
 * @static
 * @class The phone module to access the phone core functions
 * @since 0.2
 * @depends ../krypsis.js
 */
Krypsis.Device.Phone = {
  /**
   * @private
   */
  context: 'phone',

  /**
   * <p>
   * Sends a short message to a receiver.
   * </p>
   *
   * <p>
   * The expected parameters are the phone number to send the message
   * to and the text to send.
   *
   * <ul>
   * <li><strong>{String} number</strong> The phone number of the receiver. Required!</li>
   * <li><strong>{String} text</strong> The text to send. Required!</li>
   * </ul>
   * </p>
   *
   * @example
   * <pre>
   *  Krypsis.Device.Phone.sendSms({
   *    parameters: {
   *      number: '+4912234466887',
   *      text:   'Hi, I will be there soon ;)'
   *    },
   *    onSuccess: function() {
   *      alert("SMS sent successfully");
   *    }
   *  });
   *
   *  // or
   *
   *  var task = Krypsis.Device.Phone.sendSms({...}, false);
   *  task.execute();
   * </pre>
   *
   * @param {Object} options The options map with the parameters
   * and the callbacks.
   * @param {Boolean} execute Shold the task be executed after creation. Default is true
   *
   * @since 0.2
   * @public
   */
  sendSms: function(options, execute) {
    return Krypsis.Device.createTask(this, "sendsms", options, execute);
  },

  /**
   * <p>
   * Make a phone call to a particular number.
   * Note that the user can cancel this operation.
   * </p>
   *
   * <p>
   * <em>Parameters</em>
   * <ul>
   * <li><strong>{String} number</strong> The phone number. There are no limitations on the
   * number format, but make sure the number is valid. Otherwise an error will be thrown</li>
   * </ul>
   *
   * <em>Callbacks</em>
   * <ul>
   * <li><strong>onSuccess</strong> Will be called when
   * the user accepts the number and before the dialing begins.</li>
   * <li><strong>onFailure</strong> Is executed when the phone number is malformed or
   * the user canceled the operation.</li>
   * </ul>
   * </p>
   *
   * @example
   * <pre>
   *  Krypsis.Device.Phone.dialNumber({
   *    number: '00491234123456'
   *  });
   *
   *  // or
   *
   *  var task = Krypsis.Device.Phone.dialNumber({...}, false);
   *  task.execute();
   * </pre>
   *
   *
   * @param {Object} options The parameters and callbacks
   * @param {Boolean} execute Shold the task be executed after creation. Default is true
   *
   * @since 0.2
   * @public
   */
  dialNumber: function(options, execute) {
    return Krypsis.Device.createTask(this, "dialnumber", options, execute);
  }
};