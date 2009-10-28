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
 * @depends ../krypsis.js
 */
if (!Krypsis.isDefined(Krypsis.Mock)) {
  /**
   * @class The mocking library of Krypsis.
   * @static
   */
  Krypsis.Mock = {};
}
if (!Krypsis.isDefined(Krypsis.Mock.Device)) {
  Krypsis.Mock.Device = {};
}

/**
 * <p>
 * The mock namespace defines objects to mock the entire Krypsis
 * JavaScript library. The main purpose is to let web developers
 * create theier web application without the need of a real device.
 * </p>
 *
 * <p>
 * You are free to extend this library to fit you need. At the present
 * the library mocks the successful case of an execution process. To mock
 * a module you just need to include the JavaScript file in the "mock/success"
 * folder. 
 * </p>
 *
 * @public
 * @static
 * @class The Krypsis JavaScript mocking library
 */
Krypsis.Mock.Device = {

  /**
   * <p>
   * The connector mock is an implementation of the connector
   * interface and mocks up the execute function to provide
   * dummy data. Include this mock into your page when you
   * developing your krypsis application and play with that
   * fake data. It's much easier than to provide a real krypsis.
   * </p>
   *
   * @class Mock implementation of the connector interface
   * @public
   */
  Connector: {
    /**
     * <p>
     * The delegate is the original connector. It will be used to
     * pass the requested commands to if the module was not mocked.
     * </p>
     *
     * @private
     */
    delegate: null,

    /**
     * <p>
     * Execute the function and pass the call to the mocked function if
     * exists. If the module is not mocked the call will be delegated to the
     * original connector.
     * </p>
     *
     * @param context The calling module
     * @param command The command to execute
     * @param args The arguments to pass to the device.
     *
     * @public
     */
    execute: function(context, command, args) {
      /* Get the modulename and upcase the first letter */
      var modulename = context.substring(0, 1).toUpperCase() + context.substring(1);

      /*
       If this module is not mocked, then delegate the command to the
       original connector. If the connector does not exist, then throw an exception
       */
      var mock = Krypsis.Mock.Device[modulename];
      if (!mock) {
        if (this.delegate) {
          this.delegate.execute(context, command, args);
        }
        else {
          throw new Error("Context '" + context + "' is not mocked and there is no connector to delegate to.");
        }
      }


      if (mock[command]) {
        mock[command](args);
      }
      else {
        throw new Error("Mock does exist but does not provide the method: " + command);
      }
    }
  }
};

/* Save the original connector */
Krypsis.Mock.Device.Connector.delegate = Krypsis.Device.guessConnector() || null;

/* Mock the device connector */
Krypsis.Device.connector = Krypsis.Mock.Device.Connector;