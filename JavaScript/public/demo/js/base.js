if (!Krypsis.isDefined(Krypsis.Demo)) {
  Krypsis.Demo = {};
}

Krypsis.Demo.Base = {

  getMetaData: function(container) {
    Krypsis.Device.Base.getMetaData({
      onSuccess: function(args) {
        container = Krypsis.$(container);
        
        container.innerHTML = "";
        ['os', 'browser', 'version', 'kid', 'language', 'country'].each(function(key) {
          container.innerHTML += key + " = " + args[key];
          container.innerHTML += "<br />";
        });
      }
    });
  }
};