if (!Krypsis.isDefined(Krypsis.Demo)) {
  Krypsis.Demo = {};
}

/* Create alias for the screen module. */
var Kds = Krypsis.Device.Screen;

Krypsis.Demo.Screen = {

  getResolution: function(container) {
    
    Kds.getResolution({
      onSuccess: function(args) {
        var element = Krypsis.$(container);
        if (!element) {
          Krypsis.Log.error("Container '" + element +"' does not exist");
          return;
        }

        element.innerHTML = '';
        ['width', 'height', 'orientation'].each(function(attr) {
          Krypsis.appendText(element, attr + " = " + args[attr]);
          Krypsis.appendText(element, "\n");
        });
      }
    });
  },

  starObserveOrientation: function(container) {
    var element = Krypsis.$(container);
    if (!element) {
      Krypsis.Log.error("Element with id '" + container + "' not found");
      return;
    }

    Kds.startObserveOrientation({
      onSuccess: function(args) {
        Krypsis.setText(element, "orientation: " + args ? args.orientation : '?');
      }
    });
  },

  stopObserveOrientation: function(container) {
    var element = Krypsis.$(container);
    if (!element) {
      Krypsis.Log.error("Element with id '" + container + "' not found");
      return;
    }

    Kds.stopObserveOrientation({
      onSuccess: function(args) {
        Krypsis.setText(element, 'stopped');
      }
    });
  }
};