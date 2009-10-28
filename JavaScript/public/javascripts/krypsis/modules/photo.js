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
 * The photo module gives you the abbility to retreive pictures from the
 * device. Unfortunately there is currently no way to pass the binary data
 * to the JavaScript. Therefore we developed an image service where the photos
 * will be strored temporary. 
 * </p>
 *
 *
 * @static
 * @class Photo module to take and upload pictures to a
 * temporary remote image service
 * @since 0.1
 * @depends ../krypsis.js
 */
Krypsis.Device.Photo = {

  /* Each module must provide its unique context */
  context: "photo",

  /**
   * <p>
   * Starts the camera application and lets the user
   * take a photo. This photo will be uploaded to a temporary
   * image server and the response of this server will be
   * returned as the arguments of the onSuccess callback.
   * Notice: The timeout for this command is set to 60 sec. If you
   * exepect a longer command execution then overwrite the timeout.
   * </p>
   * <p>
   * You have to provide the following parameters:
   * <ul>
   * <li><strong>uploadurl</strong>: The remote url of the temporary image server.
   * This address must be accessible by the the krypsis framework and should accept post messages.</li>
   * <li><strong>parametername</strong>: The name of the parameter that will be associated
   * with the uploaded image. You will use this parameter on the server side to access the binary
   * data</li>
   * <li><strong>filename</strong>: Optional. The name of the picture file. Will be generated when missing.</li>
   * <li><strong>*</strong>: Optional. You can pass additionaly any other parameter that will be passed to the image
   * server. For example, if you are using the Krypsis Image Server then you have to provide the Kypsis Id (kid)</li>
   * </ul>
   * </p>
   *
   * <p>
   * The onSuccess callback provides an object with the following properties:
   * <ul>
   * <li><strong>response</strong>: The full text of the server response. In most cases you will provide your
   * own temporary image service so you have to know what do do with that response string.
   * In many cases it will be either a json string or a uri like query part that have to be parsed.</li>
   * </ul>
   * </p>
   *
   * <p>
   * The possible error codes are:
   * <ul>
   * <li><strong>CANCELED_BY_USER</strong> The user canceled the operation</li>
   * </ul>
   * </p>
   *
   * @example
   * <em>An example of taking a photo and using any other temporary
   * image service.</em>
   * <pre>
   *  Krypsis.Device.Photo.takeAndUpload({
   *    parameters: {
   *      uploadurl: 'http://my.remote.server.com/upload/temporary_image',
   *      parametername: 'image'
   *      filename: 'photo.jpg'
   *    },
   *    onSuccess: function(result) {
   *      Krypsis.$('serverResponse').innerHTML = result.response;
   *    }
   *  });
   * </pre>
   *
   * @example
   * <em>
   * And example of taking a photo and using the krypsis image
   * service for saving the temporary image. The Krypsis image service
   * expects the Krypsis Id as parameter.
   * </em>
   * <pre>
   *  Krypsis.Device.Base.getMetaData({
   *    onSuccess: function(args) {
   *      takePhoto(args.kid);
   *    }
   *  });
   *
   *  function takePhoto(krypsisId) {
   *    Krypsis.Device.Photo.takeAndUpload({
   *      parameters: {
   *        uploadurl: 'http://image.krypsis.org/images',
   *        parametername: 'file'
   *        filename: 'photo.jpg',
   *        kid: krypsisId
   *      },
   *      onSuccess: function(result) {
   *        Krypsis.$('serverResponse').innerHTML = result.response;
   *      }
   *    });
   *  }
   *
   *  // or
   *
   *  var task = Krypsis.Device.Photo.takeAndUpload({...}, false);
   *  task.execute();
   * </pre>
   *
   * @param {Object} options The parameters and callbacks
   * @param {Boolean} execute Shold the task be executed after creation. Default is true
   *
   * @since 0.1
   * @public
   */
  takeAndUpload: function(options, execute) {
    return Krypsis.Device.createTask(this, "takeandupload", options, execute);
  },

  /**
   * <p>
   * Starts the camera application and let't the user take a picture.
   * This photo will be down sized to 1024x768 and converted into a Base64 String.
   * </p>
   * <p>
   * Krypsis will also create a thumbnailed version for you. You can control the thumbnail
   * creation by specifying the width or the height or both of the thumb. The Thumbnail will
   * also be a Base64 encoded string.
   * </p>
   *
   * <p>
   * This function expects and supports the following parameters:
   * <ul>
   * <li><strong>{int} width</strong> the width of the thumbnail. The default value will be 100px. <em>[optional]</em></li>
   * <li><strong>{int} height</strong> the heigth of the thumbnail. The default value will be 100px. <em>[optional]</em></li>
   * </ul>
   * </p>
   *
   * <p>
   * The onSuccess callback provides an object with the following properties:
   * <ul>
   * <li><strong>{String} data</strong> The image data as a Base64 encoded string</li>
   * <li><strong>{String} thumbnaildata</strong> The thumbnails data as Base64 encoded string. Note that this field can
   * be empty sind a thumbnail is only returned if </li>
   * <li><strong>{String} contenttype</strong> The contenttype of the image. Usualy 'image/jpeg' as we work with mobile
   * devices, but also 'image/png' is not uncommon.</li>
   * <li><strong></strong></li>
   * </ul>
   * </p>
   *
   * <p>
   * The possible error codes are:
   * <ul>
   * <li><strong>CANCELED_BY_USER</strong> The user canceled the operation</li>
   * <li><strong>CAPABILITY_NOT_SUPPORTED</strong> The device has no camera (or media library)</li>
   * </ul>
   * </p>

   *
   * @example
   * <em>
   * The following example shows how to get a photo from camera and display the thumbnail.
   * Previously the screen resolution will be determined to fit the photo to the screen
   * </em>
   *
   * <pre>
   *  Krypsis.Device.Scree.getInfo({
   *    onSuccess: function(result) {
   *      getPhoto(result.width, result.height);
   *    }
   *  });
   *
   *  function getPhoto(width, height) {
   *    Krypsis.Device.Photo.takeAndGet({
   *      parameters: {
   *        width: width,
   *        height: height
   *      },
   *      onSuccess: function(result) {
   *        if (result.thumbnaildata) {
   *          var img = Krypsis.$('image');
   *          // Display the Base64 encoded image like this
   *          // &lt;img src="data:image/gif;base64,KZGlkjgjhkuz..."&gt;
   *          img.src = "data:" + result.contenttype + ";base64," + result.thumbnaildata;
   *        }
   *      }
   *    });
   *  }
   *
   *  // or
   *
   *  var task = Krypsis.Device.Photo.takeAndGet({..}, false);
   *  task.execute();
   * </pre>
   *
   * @param {Object} options The parameters and Callbacks
   * @param {Boolean} execute Shold the task be executed after creation. Default is true
   *
   * @since 0.2
   * @public
   */
  takeAndGet: function(options, execute) {
    return Krypsis.Device.createTask(this, "takeandget", options, execute);
  }
};