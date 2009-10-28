YUI({
  base: '../../yui3/',
  charset: 'utf-8',
  loadOptional: true,
  combine: false,
  timeout: 10000,
  filter: 'raw'
}).use('yuitest', 'console', function(Y) {
  var krypsisXhrTest = new Y.Test.Case({

    setUp: function() {
      
    },

    testGetXmlHttpRequest: function() {
      var result = Krypsis.Xhr.getXmlHttpRequest();
      Y.Assert.isNotUndefined(result);
      Y.Assert.isNotNull(result.transport);
      Y.Assert.isTrue(result.transport instanceof XMLHttpRequest);
    },

    testApplyCallbacks: function() {
      var xhr = Y.Mock();
      
      var options = {
        onComplete: function() {},
        onUnknown: 'should not be in the callbacks hash',
        onTimeout: function() {},
        om: 'just om ;)'
      };

      Krypsis.Xhr.applyCallbacks(xhr, options);

      Y.Assert.isNotUndefined(xhr.xhrCallbacks);
      Y.Assert.isFunction(xhr.xhrCallbacks.onComplete);
      Y.Assert.isFunction(xhr.xhrCallbacks.onTimeout);
      Y.Assert.isUndefined(xhr.xhrCallbacks.onUnknown);
      Y.Assert.isUndefined(xhr.xhrCallbacks.om);
    },

    testApplyDispatchEventFunction: function() {
      var xhr = Y.Mock();

      var callbacks = Y.Mock();
      Y.Mock.expect(callbacks, {
        method: 'onCreate',
        args: [xhr]
      });
      Y.Mock.expect(callbacks, {
        method: 'onComplete',
        args: [xhr]
      });

      Krypsis.Xhr.applyDispatchEventFunction(xhr);

      xhr.xhrCallbacks = callbacks;
      Y.Assert.isTrue(xhr.dispatchStateEvent('Create'));
      Y.Assert.isFalse(xhr.dispatchStateEvent('Create'));
      Y.Assert.isTrue(xhr.dispatchStateEvent('Complete'));
      Y.Assert.isFalse(xhr.dispatchStateEvent('Complete'));
      
      Y.Assert.isFalse(xhr.dispatchStateEvent('UNKNOWN_EVENT'));

      Y.Mock.verify(callbacks);
    },

    testApplyOnReadyStateChangedCreateFunction: function() {
      var xhr = Y.Mock();
      xhr.transport = Y.Mock();
      var callbacks = Y.Mock();

      Y.Mock.expect(callbacks, {
        method: 'onCreate',
        args: [xhr]
      });

      Krypsis.Xhr.applyCallbacks(xhr, callbacks);
      Krypsis.Xhr.applyOnReadyStateChangedFunction(xhr);
      Krypsis.Xhr.applyDispatchEventFunction(xhr);

      xhr.transport.readyState = 0;
      xhr.transport.onreadystatechange();

      Y.Mock.verify(callbacks);
    },

    testApplyOnReadyStateChangedCompleteSuccessFunction : function() {
      var xhr = Y.Mock();
      xhr.transport = Y.Mock();
      var callbacks = Y.Mock();

      Y.Mock.expect(callbacks, {
        method: 'onComplete',
        args: [xhr]
      });
      Y.Mock.expect(callbacks, {
        method: 'onSuccess',
        args: [xhr]
      });

      Krypsis.Xhr.applyCallbacks(xhr, callbacks);
      Krypsis.Xhr.applyOnReadyStateChangedFunction(xhr);
      Krypsis.Xhr.applyDispatchEventFunction(xhr);

      xhr.transport.status = 200;
      xhr.transport.readyState = 4;
      xhr.transport.onreadystatechange();

      Y.Mock.verify(callbacks);
    },

    testApplyOnReadyStateChangedCompleteFailureFunction : function() {
      var xhr = Y.Mock();
      xhr.transport = Y.Mock();
      var callbacks = Y.Mock();

      Y.Mock.expect(callbacks, {
        method: 'onComplete',
        args: [xhr]
      });
      Y.Mock.expect(callbacks, {
        method: 'onFailure',
        args: [xhr]
      });

      Krypsis.Xhr.applyCallbacks(xhr, callbacks);
      Krypsis.Xhr.applyOnReadyStateChangedFunction(xhr);
      Krypsis.Xhr.applyDispatchEventFunction(xhr);

      
      xhr.transport.status = 300;
      xhr.transport.readyState = 4;
      xhr.transport.onreadystatechange();

      Y.Mock.verify(callbacks);
    },

    /**
     * This is an integration test. Make sure the server is running.
     */
    testFunctionRequestTest: function() {
      var url = 'http://localhost:3000/javascripts/test/krypsis/xhr-test.html';
      var callback = Y.Mock();
      var backup = Y.Mock();

      Y.Mock.expect(callback, {
        method: 'onSuccess',
        args: [Y.Mock.Value.Object]
      });

      Y.Mock.expect(callback, {
        method: 'onComplete',
        args: [Y.Mock.Value.Object]
      });

      Y.Mock.expect(callback, {
        method: 'onCreate',
        args: [Y.Mock.Value.Object]
      });

      Y.Mock.expect(callback, {
        method: 'onInitialized',
        args: [Y.Mock.Value.Object]
      });

      Y.Mock.expect(callback, {
        method: 'onSent',
        args: [Y.Mock.Value.Object]
      });

      Y.Mock.expect(callback, {
        method: 'onLoading',
        args: [Y.Mock.Value.Object]
      });

      Krypsis.Xhr.request(url, callback);

      setTimeout(function() {
        Y.Mock.verify(callback);
      }, 500);
    }
  });

  Y.Test.Runner.add(krypsisXhrTest);
  Y.Test.Runner.run();
});