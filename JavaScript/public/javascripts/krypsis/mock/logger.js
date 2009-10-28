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
 * The default implementation of Krypsis logging interface does
 * nothing. It means the developer has no possibility to get notified
 * when the framework is telling him something. 
 * </p>
 *
 * <p>
 * To change this behaviour you could implement the Adapter interface
 * and provide a log(level, message) method in your adapter implementation.
 * </p>
 *
 * @class The mocked Krypsis logger. 
 */
Krypsis.Mock.Log = {

  /**
   * <p>
   * This adapter implements the Krypsis.Log.Adpater interface.
   * By using this you will get the output either in an element
   * you provide or the default element that will be created and
   * placed at the bottom of the page.
   * </p>
   *
   * <p>
   * The ID of the element that this adapter creates for you is
   * <code>KrypsisMockLogger</code>
   * </p>
   */
  adapter: {
    /**
     * <p>
     * If set to true, the the adapter is initialized
     * and can be used. Default is FALSE
     * </p>
     *
     * @private
     */
    initialized: false,

    /**
     * <p>
     * The element to append the log message to.
     * The default is an element with the ID 'logging'
     * You can overwrite this property to provide your own
     * container.
     * </p>
     *
     * @example
     * <pre>
     *  Krypsis.Mock.Log.adapter.output = 'your_element_id';
     * </pre>
     * 
     * @public
     */
    output: 'KrypsisMockLogger',

    /**
     * <p>
     * Write the given message to the logger. The message will
     * be prefixed with the current date and the given log level.
     * </p>
     *
     * @param level The level of the message
     * @param args The message itself.
     *
     * @public
     */
    log: function(level, args) {
      /* Do nothing if the output is not specified. */
      if (!this.initialized) {
        this.initialize(); 
      }

      /*
      If output element does not exists,
      we do not have a target to write our message to
      */
      if (!this.output) {
        return null;
      }

      try {
        var logName = Krypsis.Log.levelName(level);
        var date = new Date();

        var message = "[" + this.getHours(date) + ":" + this.getMinutes(date) + ":" + this.getSeconds(date) + "] " + logName + ": " + args + "<br />" + this.output.innerHTML;
        if (message.length > 1000) {
          message = message.substring(0, 1000) + "...";
        }
        this.output.innerHTML = message;
      }
      catch(e) {
        alert("The mock logging interface catched an exception: " + e.message);
      }
    },

    /**
     * @private
     */
    getHours: function(date) {
      var hours = date.getHours().toString();
      return this.normalize(hours);
    },

    /**
     * @private
     */
    getMinutes: function(date) {
      var minutes = date.getMinutes().toString();
      return this.normalize(minutes);
    },

    /**
     * @private
     */
    getSeconds: function(date) {
      var seconds = date.getSeconds().toString();
      return this.normalize(seconds);
    },

    /**
     * @private
     */
    normalize: function(value) {
      if (value.length == 1) {
        value = "0" + value;
      }
      return value;
    },

    /**
     * <p>
     * Initialize the adapter by requesting the DOM
     * element that is specified by the 'output' field.
     * If the element does not exists, then it will be created and placed
     * just before the closing </body> Tag.
     * </p>
     *
     * <p>
     * You do not have to call this function manually. It will be done
     * by the first log call.
     * </p>
     *
     * @public
     */
    initialize: function() {
      if (this.output) {
        try {
          this.output = Krypsis.$(this.output);
        }
        catch(e) {
          var div = document.createElement("div");
          div.id = this.output;
          document.body.appendChild(div);
          this.output = div;
        }
      }

      this.initialized = true;
    }
  } 
};

/* Replace the default adapter by this mock adapter */
Krypsis.Log.adapter = Krypsis.Mock.Log.adapter;
Krypsis.Log.level = Krypsis.Log.DEBUG;