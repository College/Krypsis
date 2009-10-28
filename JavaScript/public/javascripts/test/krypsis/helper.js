/**
 Contains a logger interface implementation to
 provide an output for the messages
 */

/**
 * Overwrite the log adapter
 */
Krypsis.Log.adapter = {
  log: function(level, args) {
    var logName = Krypsis.Log.levelName(level);
    var element = Krypsis.$('logging');

    if (!element) { return null; }

    var date = new Date();
    var hours = date.getHours().toString();
    if (hours.length == 1) {
      hours = "0" + hours;
    }

    var minutes = date.getMinutes().toString();
    if (minutes.length == 1) {
      minutes = "0" + minutes;
    }

    var seconds = date.getSeconds().toString();
    if (seconds.length == 1) {
      seconds = "0" + seconds;
    }

    element.innerHTML = "[" + hours + ":" + minutes + ":" + seconds + "] " + logName + ": " + args + "<br />" + element.innerHTML;
    if (element.innerHTML.length > 1000) {
      element.innerHTML = element.innerHTML.substring(0, 1000) + "...";
    }
  }
};