YUI({
  base: '../../yui3/',
  charset: 'utf-8',
  loadOptional: true,
  combine: false,
  timeout: 10000,
  filter: 'raw'
}).use('yuitest', 'console', function(Y) {
  var awfTest = new Y.Test.Case({

    testAddOnLoadEvent: function() {
      var mock = Y.Mock();

      Y.Mock.expect(mock, {
        method: 'callback1'
      });

      Y.Mock.expect(mock, {
        method: 'callback2'
      });

      Y.Mock.expect(mock, {
        method: 'callback3'
      });

      Krypsis.addLoadEvent(mock.callback1);
      Krypsis.addLoadEvent(mock.callback2);
      Krypsis.addLoadEvent(mock.callback3);

      Krypsis.onLoad();

      Y.Mock.verify(mock);
    },

    testArrayInclude: function() {
      var obj = {property: 1};
      var array = [1, 2, 3, "1", "2", obj];
      Y.Assert.isTrue(array.include(1));
      Y.Assert.isTrue(array.include(2));
      Y.Assert.isTrue(array.include(3));
      Y.Assert.isTrue(array.include("1"));
      Y.Assert.isTrue(array.include("2"));
      Y.Assert.isTrue(array.include(obj));

      Y.Assert.isFalse(array.include(function() {}));
      Y.Assert.isFalse(array.include(null));
      Y.Assert.isFalse(array.include(undefined));
      Y.Assert.isFalse(array.include("3"));
      Y.Assert.isFalse(array.include(4));
    },

    testDollarOnValidElement: function() {
      var div = document.createElement("div");
      div.id = "testElement";
      document.body.appendChild(div);

      var element = Krypsis.$('testElement');
      Y.Assert.areSame(div, element, "Element equalness");
    },

    testDollarOnInvalidElementNoException: function() {
      var element = Krypsis.$('UnknownElementId', false);
      Y.Assert.areSame(null, element);
    },

    testDollarOnInvalidElementWithException: function() {
      try {
        Krypsis.$('UnknownElementId', true);
        Y.Assert.fail("Must fail here");
      }
      catch(e) {
        if (e instanceof Y.Assert.Error) {
          throw e;
        }
      }
    },

    testIsSet: function() {
      Y.Assert.isTrue(Krypsis.isSet({}));
      Y.Assert.isTrue(Krypsis.isSet([]));
      Y.Assert.isTrue(Krypsis.isSet(''));

      Y.Assert.isFalse(Krypsis.isSet(null));
      Y.Assert.isFalse(Krypsis.isSet(undefined));
      Y.Assert.isFalse(Krypsis.isSet());
    },

    testIsDefined: function() {
      Y.Assert.isTrue(Krypsis.isDefined(typeof {}));
      Y.Assert.isTrue(Krypsis.isDefined(typeof null));
      Y.Assert.isTrue(Krypsis.isDefined(typeof document));
      Y.Assert.isTrue(Krypsis.isDefined(typeof document.getElementById));

      Y.Assert.isFalse(Krypsis.isDefined(undefined));
      Y.Assert.isFalse(Krypsis.isDefined(typeof undefined));
      Y.Assert.isFalse(Krypsis.isDefined(typeof UNKNOWN_OBJECT));
      Y.Assert.isFalse(Krypsis.isDefined(typeof document.UNKNOWN_OBJECT));
    },

    testExtend: function() {
      var obj1 = {
        property1: '1',
        method: function() {}
      };

      var obj2 = {
        property2: 2
      };

      Krypsis.extend(obj2, obj1);

      Y.Assert.areSame(obj1.property1, obj2.property1);
      Y.Assert.areSame(2, obj2.property2);
      Y.Assert.areSame(obj1.method, obj2.method);
    },

    testApplyDefaultValues: function() {
      var testObject = {
        method: 'POST',
        booleanValue: false,
        nullValue: null,
        number: 15
      };
      var defaultValues = {
        additional: 'additional',
        method: 'GET',
        booleanValue: true,
        nullValue: 'Not null',
        number: 200
      };


      testObject.applyDefaults(defaultValues);

      Y.Assert.areSame(testObject.method,         "POST");
      Y.Assert.areSame(testObject.booleanValue,   false);
      Y.Assert.areSame(testObject.nullValue,      null);
      Y.Assert.areSame(testObject.number,         15);
      Y.Assert.areSame(defaultValues.additional,  'additional');
    },

    testAddListener: function() {
      var executed = false;
      listenerFunction = function() {
        executed = true;
      };
    
      var obj = {};

      Krypsis.addListener(listenerFunction, obj, 'doitBaby');
      obj.doitBaby();
      
      Y.Assert.isNotNull(obj.doitBaby);
      Y.Assert.isTrue(executed);
    },

    testThrowExeptionWhenStartupListenerIsInvalid: function() {
      this.ensureThrow(function() {
        Krypsis.addListener(null);
      });
      this.ensureThrow(function() {
        Krypsis.addListener("");
      });
    },

    testAddEventEvent: function() {
      var listener = Y.Mock();

      Y.Mock.expect(listener, {
        method: 'cal1'
      });
      Y.Mock.expect(listener, {
        method: 'cal2'
      });
      Y.Mock.expect(listener, {
        method: 'cal3'
      });

      Krypsis.addLoadEvent(listener.cal1);
      Krypsis.addLoadEvent(listener.cal2);
      Krypsis.addLoadEvent(listener.cal3);

      Krypsis.load();

      Y.Mock.verify(listener);
    },

    testAddLoadListener: function() {
      Krypsis.load();
      Y.Assert.isTrue(Krypsis.loaded);
    },

    testAddEventWithParams: function() {
      var called = false;
      var a1, a2, a3 = null;
      var callback = function(arg1, arg2, arg3) {
        called = true;
        a1 = arg1;
        a2 = arg2;
        a3 = arg3;
      };

      var obj = {};
      Krypsis.addListener(callback, obj, 'execute');
      Krypsis.fireEvent(obj, 'execute', "1", 2, true);
      
      Y.Assert.isTrue(called, "not called");
      Y.Assert.areSame("1", a1);
      Y.Assert.areSame(2, a2);
      Y.Assert.areSame(true, a3);

      Krypsis.fireEvent(obj, 'execute', "5", 6, "7", "8", "9");

      Y.Assert.isTrue(called, "not called");
      Y.Assert.areSame("5", a1);
      Y.Assert.areSame(6, a2);
      Y.Assert.areSame("7", a3);
    },

    ensureThrow: function(fn) {
      try {
        fn();
        Y.Assert.fail("Must fail here");
      }
      catch(e) {
        if (e instanceof Y.Assert.Error) {
          throw e;
        }
      }
    }
  });

  Y.Test.Runner.add(awfTest);
  Y.Test.Runner.run();
});