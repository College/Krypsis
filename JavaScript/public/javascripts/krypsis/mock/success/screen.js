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
 * @depends ../mock.js
 */
Krypsis.Mock.Device.Screen = {

  getresolution: function() {
    Krypsis.Device.Screen.Success.getresolution({
      width: 320,
      height: 200,
      orientation: this.randomOrientation()
    });
  },

  startobserveorientation: function(args) {
    var self = this;
    var orientation = function() {
      var orient = self.randomOrientation();

      Krypsis.Device.Screen.Success.startobserveorientation({
        orientation:  orient,
        width:        orient == 0 ? 320 : 200,
        height:       orient == 0 ? 200 : 320
      });
    };

    Krypsis.Device.Screen.observer = setInterval(orientation, args.interval || 5000);
    orientation();
  },

  stopobserveorientation: function() {
    clearInterval(Krypsis.Device.Screen.observer);
    Krypsis.Device.Screen.Success.stopobserveorientation();
  },

  randomOrientation: function() {
    var number = parseInt(Math.random() * 10);
    return number % 2 == 0 ? 0 : 90;
  }
};