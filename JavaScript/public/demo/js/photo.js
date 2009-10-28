if (!Krypsis.isDefined(Krypsis.Demo)) {
  Krypsis.Demo = {};
}

var Kdb = Krypsis.Device.Base;
var Kdp = Krypsis.Device.Photo;

Krypsis.Demo.Photo = {

  takeAndUpload: function(container) {
    var element = Krypsis.$(container);
    if (!element) {
      Krypsis.Log.error("Element with id '" + container + "' not found");
      return false;
    }

    this.getKrypsisId(function(kid) {
      Kdp.takeAndUpload({
        parameters: {
          uploadurl: "http://image.krypsis.org/images",
          parametername: 'file',
          kid: kid
        },

        onSuccess: function(args) {
          Krypsis.Log.debug("Image server returned: " + args.response);
          var image = Krypsis.$('image');
          var width = Krypsis.$('width').value || 200;
          var height = Krypsis.$('height').value || 200;

          image.src = "http://image.krypsis.org/images" +
                      "/" + args.response + "?kid=" + kid + "&width=" + width + "&height=" + height;
        }
      });
    });
  },

  getKrypsisId: function(callback) {
    Kdb.getMetaData({
      onSuccess: function(args) {
        Krypsis.Log.info("Krypsis ID: " + args.kid);
        callback(args.kid);
      }
    });
  }
};