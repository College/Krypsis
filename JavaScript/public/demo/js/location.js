if (!Krypsis.isDefined(Krypsis.Demo)) {
  Krypsis.Demo = {};
}

Krypsis.Demo.Location = {

  getLocation: function(container) {
    var element = Krypsis.$(container);
    if (!element) {
      Krypsis.Log.error("Element with id '"  + container + "' not found.");
      return false;
    }
    
    Krypsis.Device.Location.getLocation({
      onSuccess: function(args) {
        Krypsis.setText(element, "");
        for (attr in args) {
          var value = args[attr];

          if (typeof value == 'function') {
            continue;
          }

          Krypsis.appendText(element, attr + " = " + value);
          Krypsis.appendText(element, "\n");
        }
      }
    });
  },

  startObserveLocation: function(container) {
    var element = Krypsis.$(container);
    if (!element) {
      Krypsis.Log.error("Element with id '"  + container + "' not found.");
      return false;
    }

    Krypsis.setText(element, "will start soon...");
    
    Krypsis.Device.Location.startObserveLocation({
      onSuccess: function(args) {
        Krypsis.setText(element, "");
        for (attr in args) {
          var value = args[attr];

          if (typeof value == 'function') {
            continue;
          }

          Krypsis.appendText(element, attr + " = " + value);
          Krypsis.appendText(element, "\n");
        }
      }
    });
  },

  stopObserveLocation: function(container) {
    var element = Krypsis.$(container);
    if (!element) {
      Krypsis.Log.error("Element with id '"  + container + "' not found.");
      return false;
    }

    Krypsis.Device.Location.stopObserveLocation({
      onSuccess: function() {
        Krypsis.setText(element, "stopped");
      }
    });
  }
};