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
  base: '../../yui3/',
  charset: 'utf-8',
  loadOptional: true,
  combine: false,
  timeout: 10000,
  filter: 'raw'
}).use('yuitest', 'console', function(Y) {
  var test = new Y.Test.Case({

    setUp: function() {
      Krypsis.Device.dispatch = function(task) {
        task.module['Success'][task.action]({});
      };
    },

    testExecutetasks: function() {
      var tasks = [];
      for (var i = 0; i < 10; i++) {
        tasks.push(new Krypsis.Device.Task({context: 'mock'}, 'action' + i, {}));
      }
      tasks.execute();

      for (i = 0; i < tasks.length; i++) {
        var task = tasks[i];
        Y.Assert.isTrue(task.completed);
      }
    },

    testExecuteCallback: function() {
      var tasks = [];
      for (var i = 0; i < 10; i++) {
        tasks.push(new Krypsis.Device.Task({context: 'mock'}, 'action' + i, {}));
      }
      var listener = Y.Mock();

      Y.Mock.expect(listener, {
        method: 'cal1'
      });

      tasks.execute(listener.cal1);

      Y.Mock.verify(listener);
    },

    testSingleTask: function() {
      var task = new Krypsis.Device.Task({context: 'mock'}, 'action', {});
      var tasks = [task];
      tasks.execute();

      Y.Assert.isTrue(task.completed);
      Y.Assert.isTrue(tasks.tasksCompleted);
    }
  });

  Y.Test.Runner.add(test);
  Y.Test.Runner.run();
});