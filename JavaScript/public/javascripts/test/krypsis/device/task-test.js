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

YUI({
  base: '../../../yui3/',
  charset: 'utf-8',
  loadOptional: true,
  combine: false,
  timeout: 10000,
  filter: 'raw'
}).use('yuitest', 'console', function(Y) {
  var test = new Y.Test.Case({

    setUp: function() {
      Krypsis.Device.dispatch = function(arg1, args2, arg3, callback) {
        callback();
      };
    },

    testExecuteTask: function() {
      var task = new Krypsis.Device.Task({context: 'mock'}, 'action', {});
      Y.Assert.isFalse(task.completed);
      task.execute();
      Y.Assert.isTrue(task.completed);
    },

    testExecuteCallback: function() {
      var listener = Y.Mock();

      Y.Mock.expect(listener, {
        method: 'cal1'
      });

      var task = new Krypsis.Device.Task({context: 'mock'}, 'action', {});
      task.execute(listener.cal1);

      Y.Mock.verify(listener);
    },

    testCallbackCreation: function() {
      var module = Krypsis.Device.Mock = {context: 'mock'};
      var task = new Krypsis.Device.Task(module, 'action', {});

      Y.Assert.isNotUndefined(Krypsis.Device.Mock.Success.action);
      Y.Assert.isNotUndefined(Krypsis.Device.Mock.Error.action);

      Y.Assert.areSame(Krypsis.Device.Mock.Success.action, task.onSuccess);
      Y.Assert.areSame(Krypsis.Device.Mock.Error.action, task.onFailure);
    }
  });

  Y.Test.Runner.add(test);
  Y.Test.Runner.run();
});