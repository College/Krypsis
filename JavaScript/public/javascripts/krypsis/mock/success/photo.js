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
Krypsis.Mock.Device.Photo = {
  
  takeandupload: function() {
    Krypsis.Device.Photo.Success.takeandupload({response: "0qn9T6spLdwgMeeMlMsgpaFyN2VmhajH"});
  },

  takeandget: function() {
    Krypsis.Device.Photo.Success.takeandget({
      data: "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",
      thumbnaildata: "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAABGdBTUEAALGPC/xhBQAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9YGARc5KB0XV+IAAAAddEVYdENvbW1lbnQAQ3JlYXRlZCB3aXRoIFRoZSBHSU1Q72QlbgAAAF1JREFUGNO9zL0NglAAxPEfdLTs4BZM4DIO4C7OwQg2JoQ9LE1exdlYvBBeZ7jqch9//q1uH4TLzw4d6+ErXMMcXuHWxId3KOETnnXXV6MJpcq2MLaI97CER3N0vr4MkhoXe0rZigAAAABJRU5ErkJggg==",
      contenttype: "image/png"
    });
  }
};