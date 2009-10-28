YUI({
  base: '../../../yui3/',
  charset: 'utf-8',
  loadOptional: true,
  combine: false,
  timeout: 10000,
  filter: 'raw'
}).use('yuitest', 'console', function(Y) {
  var krypsisDeviceTest = new Y.Test.Case({

    setUp: function() {
      Krypsis.Device.Base.getMetaData = function(args) {
        args.onSuccess();
      };

      Krypsis.Device.connector = {
        execute: function() {}
      };
    },

    testDispatchWithIllegalArguments: function() {
      try {
        Krypsis.Device.dispatch();
        Y.Assert.fail("Should fail here. task not given");
      }
      catch(e) {
        if (e instanceof Y.Assert.Error) { throw e; }
      }

      try {
        Krypsis.Device.dispatch();
        Y.Assert.fail("Should fail here. Connector is missing");
      }
      catch(e) {
        if (e instanceof Y.Assert.Error) { throw e; }
      }

      var connector = Y.Mock();
      Y.Mock.expect(connector, {
        method: 'execute',
        args: ['context', 'command', Y.Mock.Value.Object]
      });

      Krypsis.Device.connector = connector;

      try {
        Krypsis.Device.dispatch();
        Y.Assert.fail("Should fail here, module is missing");
      }
      catch(e) {
        if (e instanceof Y.Assert.Error) { throw e; }
      }

      var task = Y.Mock();

      try {
        Krypsis.Device.dispatch(task);
        Y.Assert.fail("Should fail here. Module context is missing");
      }
      catch(e) {
        if (e instanceof Y.Assert.Error) { throw e; }
      }

      Y.Mock.verify(task);
      Y.Mock.verify(connector);
    },

    testAddErrorListener: function() {
      var error = new Error();
      var errorHandler = Y.Mock();
      Y.Mock.expect(errorHandler, {
        method: 'onError',
        args: [error]
      });

      Krypsis.Device.addErrorListener(errorHandler.onError);
      Krypsis.fireEvent(Krypsis.Device, 'onError', error);

      Y.Mock.verify(errorHandler);
    }
  });

  Y.Test.Runner.add(krypsisDeviceTest);
  Y.Test.Runner.run();
});