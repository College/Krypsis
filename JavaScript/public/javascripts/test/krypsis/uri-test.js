YUI({
  base: '../../yui3/',
  charset: 'utf-8',
  loadOptional: true,
  combine: false,
  timeout: 10000,
  filter: 'raw'
}).use('yuitest', 'console', function(Y) {
  var awfUriTest = new Y.Test.Case({

    testToQueryPair: function() {
      Y.Assert.areSame(null, Krypsis.Uri.toQueryPair(null, null));
      Y.Assert.areSame(null, Krypsis.Uri.toQueryPair(null, ""));
      Y.Assert.areSame(null, Krypsis.Uri.toQueryPair("", null));
      Y.Assert.areSame(null, Krypsis.Uri.toQueryPair("", ""));
      Y.Assert.areSame(null, Krypsis.Uri.toQueryPair("", "value"));

      Y.Assert.areSame("key=", Krypsis.Uri.toQueryPair("key", null));
      Y.Assert.areSame("key=", Krypsis.Uri.toQueryPair("key", ''));
      Y.Assert.areSame("key=value", Krypsis.Uri.toQueryPair("key", "value"));
    },

    testToQueryPairs: function() {
      Y.Assert.areSame(null, Krypsis.Uri.toQueryPairs({}));
      Y.Assert.isTrue(Krypsis.Uri.toQueryPairs({a: null}).include('a='));
      Y.Assert.isTrue(Krypsis.Uri.toQueryPairs({a: "b"}).include("a=b"));

      var result = Krypsis.Uri.toQueryPairs({a: "b", c: null});
      Y.Assert.isTrue(result.include("a=b"));
      Y.Assert.isTrue(result.include("c="));

      result = Krypsis.Uri.toQueryPairs({a: "b", c: 'd'});
      Y.Assert.isTrue(result.include("a=b"));
      Y.Assert.isTrue(result.include("c=d"));
    },

    testToQueryString: function() {
      Y.Assert.areSame(null, Krypsis.Uri.toQueryString({}));
      Y.Assert.areSame("a=", Krypsis.Uri.toQueryString({a: null}));
      Y.Assert.areSame("a=b", Krypsis.Uri.toQueryString({a: 'b'}));
      Y.Assert.areSame("a=b&c=", Krypsis.Uri.toQueryString({a: 'b', c: null}));
      Y.Assert.areSame("a=b&c=d", Krypsis.Uri.toQueryString({a: 'b', c: 'd'}));
    }
  });

  Y.Test.Runner.add(awfUriTest);
  Y.Test.Runner.run();
});