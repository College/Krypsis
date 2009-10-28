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

if (!Krypsis.isDefined(Krypsis.Demo)) {
  Krypsis.Demo = {};
}

/**
 * This example shows how rettreive the screen resolution
 * and the GPS position of the device.
 *
 * With this information a the google static maps service
 * will return a map with a marker on the current position.
 *
 * The image size will fit the entire mobile screen.
 */
Krypsis.Demo.Staticmaps = {
  HOST: 'http://maps.google.com/staticmap',

  /**
   * Builds the static maps url from the given params and returns the result.
   */
  buildUrl: function(options) {
    options = (options || {}).applyDefaults({
      maptype:  "mobile",
      zoom:     '12',
      sensor:   'true',
      key:      'ABQIAAAAQsy1m0NpT7Z_yI_m5IExvBRngN1a2Hlc2HCph_9A1fdZ0WiTwRT7f6lE9LXkTwS8f8xst5stntdIwA'
    });
    var query = Krypsis.Uri.toQueryString(options);
    return Krypsis.Demo.Staticmaps.HOST + "?" + query;
  },

  /**
   * Start example and loads the map into the given image.
   *
   * @param imageId The image DOM id
   */
  loadStaticMap: function(imageId) {
    Krypsis.Log.debug("Load static map into :" + imageId);
    this.imageId = imageId;

    try {
      this.getScreenSize();
    }
    catch(e) {
      Krypsis.Log.error(e);
    }
  },

  getScreenSize: function() {
    var self = this;
    /* Request the screen resolution */
    Krypsis.Device.Screen.getResolution({
      onSuccess: function(args) {
        self.getLocation(args.width + "x" + args.height);
      }
    });
  },

  getLocation: function(size) {
    var self = this;
    var image = Krypsis.$(self.imageId);

    Krypsis.Device.Location.getLocation({
      onSuccess: function(args) {
        var url = self.buildUrl({
          size: size,
          markers: args.latitude + "," + args.longitude
        });

        Krypsis.Log.debug("Tasks completed. Load image: " + url);
        image.src = url;
      }
    });
  },

  /**
   * By calling this function
   * @param imageId
   */
  enableScreenOrientation: function(imageId) {
    var self = this;
    var image = Krypsis.$(imageId);

    if (!self.orientationListenerEnabled) {
      Krypsis.Device.Screen.startObserveOrientation({
        onSuccess: function(args) {
          self.size = args.width + "x" + args.height;

          image.src = self.buildUrl({
            size: self.size,
            markers: self.markers
          });
        }
      });

      self.orientationListenerEnabled = true;
    }
    else {
      self.orientationListenerEnabled = false;
      Krypsis.Device.Screen.stopObserveOrientation({
        onSuccess: function() {
          Krypsis.Log.debug("Screen orientation listener stopped");
        }
      });
    }
  }
};