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
Krypsis.Demo.Uploadphoto = {
  IMAGE_SERVICE: 'http://image.krypsis.org/images',

  doit: function(imageId, widthId, heightId) {
    this.image  = Krypsis.$(imageId);
    this.width  = Krypsis.$(widthId);
    this.height = Krypsis.$(heightId);

    Krypsis.Device.Base.getMetaData({
      onSuccess: function(args) {
        Krypsis.Demo.Uploadphoto.takePhoto(args.kid);
      }
    });
  },

  takePhoto: function(kid) {
    Krypsis.Device.Photo.takeAndUpload({

      parameters: {
        uploadurl:  Krypsis.Demo.Uploadphoto.IMAGE_SERVICE,
        parameter:  'file',
        kid:        kid
      },

      onSuccess: function(args) {
        Krypsis.Demo.Uploadphoto.showThumbnail(kid, args.response);
      }
    });
  },

  showThumbnail: function(kid, response) {
    var options = {
      width:  this.width.value,
      height: this.height.value,
      kid:    kid
    };
    this.image.src = this.IMAGE_SERVICE + "/" + response + "?" + Krypsis.Uri.toQueryString(options);
  }
};