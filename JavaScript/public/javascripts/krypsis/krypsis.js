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

if ("undefined" !== typeof Krypsis || Krypsis) {
  alert("Krypsis is already defined.");
}

/**
 * Krypsis is the project name and at the same
 * time the root object for all krypsis functions
 * and objects
 * 
 * @static
 * @class The krypsis namespace
 */
var Krypsis = {

  /**
   * <p>
   * Load the Krypsis framework. This function must be added to the body.unload event.
   * </p>
   *
   * <p>
   * Due to some mobile-browser limitations Krypsis is not able to register
   * itself for a "DOM is ready" event. Therefore you, as a web developer, have
   * to do it by yourself. Best practice is to wire the onload and onunload events
   * of the body element with the Krypsis.load and Krypsis.unload functions.
   * </p>
   *
   * <p>
   * If you need to get notified about those events you can add an onLoad and
   * onUnload listener.
   * @link Krypsis#addLoadEvent
   * @link Krypsis#addUnloadEvent
   * </p>
   *
   * @example
   * <pre>
   *  &lt;body onload="Krypsis.load();"/&gt;
   * </pre>
   * 
   * @public
   */
  load: function() {
    this.loaded = true;
    this.fireEvent(this, 'onLoad');

    if (!Krypsis.isSet(Krypsis.Device.connector)) {
      Krypsis.Device.connector = Krypsis.Device.guessConnector();
      Krypsis.Log.info("Set device connector to " + Krypsis.Device.connector);
    }

    Krypsis.Log.info("Krypsis is successfully loaded");
  },


  /**
   * <p>
   * Unload Krypsis. This function must be added to the body.onunload event.
   * </p>
   *
   * <p>
   * The expected parameter tells Krypsis whether the underlying device should
   * be notified or not. If set to true, then the Krypsis.Device.Base.reset();
   * function will be executed and the underlying device will stop and
   * collect all previously created listeners. Also the device internal state
   * will be reseted. This is very recommended, since the user can cancel using
   * the web application and all created listeners will be active in backgroud,
   * which can result in unmeant behaviuor.
   * </p>
   *
   * <p>
   * So don't forget to provide the unload function in the body-tag callback
   * </p>
   *
   * @example
   * <pre>
   *  &lt;body onunload="Krypsis.unload(true);"/&gt;
   * </pre>
   *
   * @param {Function} notifyDevice Should the device be notified? Default is set to TRUE.
   * @public
   */
  unload: function(notifyDevice) {
    this.loaded = false;

    if (Krypsis.isSet(notifyDevice) ? notifyDevice : true) {
      Krypsis.Device.Base.reset({});
    }

    this.fireEvent(this, 'unUnload');

    Krypsis.Log.info("Krypsis is successfully unloaded");
  },

  /**
   * <p>
   * Get notified when Krypsis is loaded and ready to be used.
   * </p>
   *
   * @example
   * <pre>
   *  Krypsis.addLoadEvent(function() {
   *    alert("Krypsis is " + Krypsis.loaded ? 'loaded' : 'not loaded');
   *  });
   * </pre>
   *
   * @param {Function} callback The callback to execute when Krypsis is ready
   * @public
   */
  addLoadEvent: function(callback) {
    Krypsis.addListener(callback, this, 'onLoad');
  },

  /**
   * <p>
   * Get notified when Krypsis is unloaded.
   * </p>
   *
   * @example
   * <pre>
   *  Krypsis.addUnloadEvent(function() {
   *    alert("Krypsis is " + Krypsis.loaded ? 'loaded' : 'unloaded');
   *  });
   * </pre>
   *
   * @param {Function} callback The callback the execute when Krypsis is unloaded
   * @public
   */
  addUnloadEvent: function(callback) {
    Krypsis.addListener(callback, this, 'onUnload');
  },

  /**
   * <p>
   * Adds a listener to an object to observe the execution of a particular
   * function.
   * </p>
   *
   * @param {Function} callback The function to call when the observed method is executed.
   * @param {Object} obj The object that provides the observed method
   * @param {String} name The name of the method to observe
   *
   * @public
   */
  addListener: function(callback, obj, name) {
    if (typeof callback != 'function') {
      throw new Error("Function is expected as parameter");
    }

    var oldListener = obj[name] || function() {};
    obj[name] = function() {
      var args = Array.prototype.slice.call(arguments);

      oldListener.apply(obj, args);
      callback.apply(obj, args);
    };
  },

  /**
   * <p>
   * Executes a function of the given object. This function is used
   * whenever a listener was previosly added and now the source event
   * should be fired.
   * </p>
   *
   * <p>
   * For example: The load event if Krypsis is defined as follows
   * <code>Krypsis.addListener(FUNCTION, this, 'onLoad');</code>
   * If now all listener should be notified about this 'load' event
   * then we can achieve this by execution this function:
   * <code>Krypsis.fireEvent(Krypsis, 'onLoad');</code>
   * </p>
   *
   * <p>
   * Alternatively we could write:
   * <pre>
   * if (Krypsis.isSet(Krypsis.onLoad)) {
   *  Krypsis.onLoad();
   * }
   * </pre>
   * </p>
   *
   * @param {Object} source The source object
   * @param {String} event The function to execute as string.
   *
   * @private
   */
  fireEvent: function(source, event) {
    if (!source || ! event) {
      return false;
    }

    var eventFunction = source[event];
    if (typeof eventFunction == 'function') {
      var args = Array.prototype.slice.call(arguments);
      if (args.length >= 2) {
        args = args.slice(2, args.length);
      }
      eventFunction.apply(source, args);
    }
  },


  /**
   * <p>
   * Returns the element with the given id.
   * </p>
   * 
   * @param {String|Object} id The id of the element
   * @param {Boolean} raise If it set to true and the element
   * was not found then an exception will be thrown
   *
   * @public
   */
  $: function(id, raise) {

    if ("object" == typeof id) { return id; }

    raise = Krypsis.isSet(raise) ? raise : true;

    var element = null;

    if (Krypsis.isDefined(typeof document.getElementById)) {
      element = document.getElementById(id);
    }
    else if (Krypsis.isDefined(typeof document.all)) {
      element = document.all[id];
    }
    else {
      var errorMessage = "Krypsis could not access DOM elements. The getElementById() function is not defined.";
      Krypsis.Log.error(errorMessage);
      throw new Error(errorMessage);
    }

    if (!Krypsis.isSet(element) && raise) {
      throw "Element with id " + id + " does not exist.";
    }
    return element;
  },

  /**
   * <p>
   * Sets the text of a html element in a browser
   * independent way.
   * Notice: The old text will be overwritten
   * </p>
   *
   * @param {Element} element The element to add the text to
   * @param {String} text The text to display
   *
   * @public 
   */
  setText: function(element, text) {
    if (Krypsis.isDefined(typeof element.innerText)) {
      element.innerText = text;
    }
    else {
      element.innerHTML = text;
    }
  },

  /**
   * <p>
   * Append the given text to the existing inner text of
   * the element.
   * Notice: The old text will not be removed. The new Text will be appended
   * </p>
   *
   * @param {Element} element The element to add the text to
   * @param {String} text The text to display
   *
   * @public
   */
  appendText: function(element, text) {
    if (Krypsis.isDefined(typeof element.innerText)) {
      element.innerText += text;
    }
    else {
      element.innerHTML += text;
    }
  },

  /**
   * Returns true if the given value is defined
   * and is not null
   *
   * @param {Object} obj The object to check.
   *
   * @public
   */
  isSet: function(obj) {
    return Krypsis.isDefined(typeof obj) && obj != null;
  },

  /**
   * Returns true if the given type is defined.
   * It means if the given type is not "undefined"
   *
   * @example
   * <pre>
   *  Krypsis.isDefined(typeof THE_OBJECT_TO_CHECK);
   * </pre>
   * 
   * @param {Object} type The type to check.
   *
   * @public
   */
  isDefined: function(type) {
    return type != undefined && type != 'undefined' && type != 'unknown';
  },

  /**
   * Extends a object by copying the attributes from
   * the source into the destination
   *
   * @param {Object} destination The target
   * @param {Object} source The source object
   *
   * @public
   */  
  extend: function(destination, source) {
    for (var property in source) {
      destination[property] = source[property];
    }
    return destination;
  },

  /**
   * The krypsis Log interface is as simple as stupid.
   * You can defined your error level and krypsis will
   * use a log interface to display the messages with the defined
   * level.
   *
   * @class The Krypsis logger
   * @static
   */
  Log: {
    /**
     * <p>Debug level</p>
     * @static
     * @field
     * @public
     */
    DEBUG:  0,

    /**
     * <p>Info level</p>
     * @static
     * @field
     * @public
     */
    INFO:   1,

    /**
     * <p>Warn level</p>
     * @static
     * @field
     * @public
     */
    WARN:   2,

    /**
     * <p>Error level</p>
     * @static
     * @field
     * @public
     */
    ERROR:  3,

    /**
     * <p>Fatal level</p>
     * @static
     * @field
     * @public
     */
    FATAL:  10,

    /**
     * <p>
     * The level of the logger decides which type of massages
     * are passed to the adapter and there to you. The default value
     * is ERROR, which means no DEBUG, INFO or WARN messages will
     * be delivered. Only ERROR and FATAL messages will be passed
     * to the adapter.
     * </p>
     *
     * <p>
     * To change this you can pass another level to this field:
     * </p>
     *
     * @example
     * <pre>
     * Krypsis.Log.level = Krypsis.Log.DEBUG;
     * </pre>
     *
     * @public
     * @field
     */
    level: 3,

    /**
     * <p>
     * The default logging adapter is an empty stub. It means
     * Krypsis by default does not printing any information.
     * </p>
     *
     * <p>
     * If you need to see the logging output, then either include the mock
     * or implement your own adapter. The only one function you need is log(level, message);
     * </p>
     *
     * @static
     * @public
     */
    adapter: {
      log: function(level, message) {}
    },

    /**
     * <p>
     * Print message with level DEBUG
     * </p>
     *
     * @param {String} msg The message
     * @public
     */
    debug: function(msg) {
      this.log(this.DEBUG, msg);
    },

    /**
     * <p>
     * Print message with level INFO
     * </p>
     *
     * @param {String} msg The message
     * @public
     */
    info: function(msg) {
      this.log(this.INFO, msg);
    },

    /**
     * <p>
     * Print message with level WARN
     * </p>
     *
     * @param {String} msg The message
     * @public
     */
    warn: function(msg) {
      this.log(this.WARN, msg);
    },

    /**
     * <p>
     * Print message with level ERROR
     * </p>
     *
     * @param {String} msg The message
     * @public
     */
    error: function(msg) {
      this.log(this.ERROR, msg);
    },

    /**
     * <p>
     * Print message with level FATAL
     * </p>
     *
     * @param {String} msg The message
     * @public
     */
    fatal: function(msg) {
      this.log(this.FATAL, msg);
    },

    /**
     * <p>
     * Print message with specified level
     * </p>
     *
     * @param {Number} level the log level
     * @param {String} msg The message
     * @private
     */
    log: function(level, msg) {
      level = level || this.DEBUG;
      
      if (this.level <= level) {
        this.adapter.log(level, msg);
      }
    },

    /**
     * <p>
     * Returns the human readable name of the level value.
     * </p>
     *
     * @param {Number} level The level as int value
     * @private
     */
    levelName: function(level) {
      switch(level) {
        case this.DEBUG:  return "DEBUG";
        case this.INFO:   return "INFO ";
        case this.WARN:   return "WARN ";
        case this.ERROR:  return "ERROR";
        case this.FATAL:  return "FATAL";
        default: return "INFO ";
      }
    }
  },

  /**
   * This is a helper module to build uris
   * parameters from hashes
   * 
   * @static
   * @class The
   */
  Uri: {
    /**
     * Expects a hash of parameters that should be converted
     * in URI compatible string and joined with a '&'.
     * 
     * @param {Object} parameters The parameters hash {}
     *
     * @public
     */
    toQueryString: function(parameters) {
      if (!Krypsis.isSet(parameters) || parameters.length <= 0) {
        return null;
      }

      var result = this.toQueryPairs(parameters);
      return result && result.length > 0 ? result.join('&') : null;
    },

    /**
     * Iterates over the given hash and creates an
     * array of key=value pairs. The array can be joined
     * after this procedure.
     * Note: If the hash is undefined, null or empty
     * the result is null.
     *
     * @param {Object} hash The hash to convert into key=value pairs.
     *
     * @private
     */
    toQueryPairs: function(hash) {
      if (!hash) { return null; }
      
      var result = [];

      for (var key in hash) {
        if (typeof hash[key] == 'function') {
          continue;
        }

        var pair = this.toQueryPair(key, hash[key]);
        if (pair) { result.push(pair); }
      }

      return result.length == 0 ? null : result;
    },

    /**
     * Encodes the given key and value to a URI compatible
     * string and concatinates them with a '='
     *
     * @param {String} key The key
     * @param {String} value The value
     * @return {String} encoded 'key=value'
     *
     * @private
     */
    toQueryPair: function(key, value) {
      if (!key || key.length == 0) { return null; }
      value = Krypsis.isSet(value) ? value : '';
      return encodeURIComponent(key) + '=' + encodeURIComponent(value);
    }
  },

  /**
   * This is a small AJAX library that enables
   * the framework to call remote services in background
   * without reloading the full page content
   * 
   * @class Krypsis AJAX library
   * @static
   */
  Xhr: {

    /**
     * Executes a request to the specified url.
     * You can control the request process with the
     * given options.
     * 
     * @param {String} url The url to call.
     * @param {Object} options The options hash.
     *
     * <p>
     * The following parameters are supported and used to control the
     * behaviour of the AJAX transmission.
     * <ul>
     * <li><strong>method</strong> Either <code>POST</code> or <code>GET</code>. Default is <code>GET</code></li>
     * <li><strong>parameters</strong> The parameters hash with key - value pairs to send to the server.</li>
     * <li><strong>evalScripts</strong> Should the javascript be evalueted if any found. Default is false.</li>
     * <li><strong>asynchronous</strong> The type of the connection to the server. If <code>false</code>, then the operation
     * block until the server responds to the request. Default is <code>true</code></li>
     * </ul>
     * </p>
     *
     * <p>
     * A main part of the options hash are the definable callbacks
     * (very similar to the prototype's callbacks)
     * <ul>
     * <li><strong>onCreate</strong> The XHR instance is created. (1 Param => the XHR wrapper)</li>
     * <li><strong>onInitialized</strong> Connection is opened. (1 Param => the XHR wrapper)</li>
     * <li><strong>onSent</strong> Data is sent. (1 Param => the XHR wrapper)</li>
     * <li><strong>onLoading</strong> Read the Response. (1 Param => the XHR wrapper)</li>
     * <li><strong>onSuccess</strong> Response status is ok. Something between 200 and 299. (1 Param => the XHR wrapper)</li>
     * <li><strong>onFailure</strong> Response statis is NOT ok. A status code that is not between 200 and 299. (1 Param => the XHR wrapper)</li>
     * <li><strong>onException</strong> An exception occurs during the request. (1 param => the XHR wrapper, 2 param => Exception itself)</li>
     * </ul>
     * </p>
     *
     * @return The XHR wrapper. To access the XMLHttpRequest instance, call <code>xhr.transport</code>
     *
     * @public
     */
    request: function(url, options) {

      if (!Krypsis.isSet(url)) {
        throw "Url is missing";
      }

      /*
       * Attention: this is a wrapper around the xmlhttprequest instance.
       * Access the instance by the transport property
       */
      var xhr = this.getXmlHttpRequest();

      if (!Krypsis.isSet(xhr)) {
        throw "XmlHttpRequest is not supported by this browser";
      }

      /*
       * Make sure the given options are valid and
       * the default values are set.
       */
      options.applyDefaults({
        method: 'GET',
        parameters: null,
        username: null,
        password: null,
        asynchronous: true,
        evalScripts: false
      });

      /*
       * Apply the user callbacks by copying each function
       * that begins with 'on' to the callbacks hash
       */
      this.applyCallbacks(xhr, options);

      /*
       * Dispatches the event with the given name.
       * If callback is not defined, nothing happens.
       */
      this.applyDispatchEventFunction(xhr);

      /*
       * Dispatches the fist event => onCreate.
       * The usecase on this callback could be to
       * show a spinner or deactivate some elements.
       */
      xhr.dispatchStateEvent("Create");

      /* Prepare options */
      var parameters = options.parameters;
      var method = options.method;
      xhr.evalScripts = options.evalScripts;

      this.applyOnReadyStateChangedFunction(xhr);


      try {
        /* Open connection */
        xhr.transport.open(method, url, options.asynchronous);

        /*
         * Compile the url and the request headers.
         * If parameters are given then the url build depends on
         * the method.
         *
         * In case of an get, we have to append the parameters to the
         * url and send the request.
         *
         * In case of a post the content type has to be set to
         * 'application/x-www-form-urlencoded' and the parameters will
         * be sent by the underlying XMLHttpRequest instance as body content.
         */
        if (parameters) {
          /* Convert the params to an url encoded string. */
          parameters = Krypsis.Uri.toQueryString(parameters);
          if (method == "GET") {
            url = url = "?" + parameters;
            parameters = null;
          }
          else {
            xhr.transport.setRequestHeader("Connection", "close");
            xhr.transport.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
            xhr.transport.setRequestHeader("Content-length", parameters.length);
          }
        }

        xhr.transport.send(parameters);
        return xhr;
      }
      catch (e) {
        /**
         * An exception we can handle in two different
         * ways. If an onException callback is provided
         * then the event will be dispatched.
         *
         * Otherwise we will rethrow the exception.
         */
        if (!xhr.dispatchStateEvent("Exception", e)) {
          throw e;
        }
      }
    },

    /**
     * <p>
     * Calls an ajax updater and replaces the given
     * container with the result in successfull
     * </p>
     *
     * @param {String|Object} container The target container where to place the received content into.
     * The container can be a string or a hash. If the param is a string, then it will be
     * converted into a hash where the string bacames the value of the the success key.
     * The hash contains the folowing keys:
     * <ul>
     * <li><strong>success</strong> The container id in case of a success response</li>
     * <li><strong>failure</strong> The container id in case of a failed response</li>
     * </ul>
     *
     * @param {String} url The url to execute.
     * @param {Object} options The options hash. See the {@link Krypsis.Xhr.request()} function for more
     * infromation about the options hash
     *
     * @return {Object} the XHR wrapper instance
     *
     * @public
     */
    update: function(container, url, options) {
      if (!container) {
        throw "container is missing. Could not update a null element";
      }

      if ("string" == typeof container) {
        container = { success: container };
      }

      container.success = Krypsis.$(container.success);
      if (container.failure) {
        container.failure = Krypsis.$(container.failure);
      }

      options = options || {};

      var oldOnSuccess = options.onSuccess;
      options.onSuccess = function(xhr) {
        container.success.innerHTML = xhr.transport.responseText;
        if ("function" == typeof oldOnFailure) { oldOnSuccess(xhr); }
      };

      if (container.failure) {
        var oldOnFailure = options.onFailure;
        options.onFailure = function(xhr) {
          container.failure.innerHTML = xhr.transport.responseText;
          if ("function" == typeof oldOnFailure) { oldOnFailure(xhr); }
        };
      }

      return this.request(url, options);
    },    

    /**
     * Implement the onreadystatechange callback to respond
     * on each internal state changement.
     *
     * @param {Object} xhr The XMLHttpRequest instance to extend
     *
     * @private
     */
    applyOnReadyStateChangedFunction: function(xhr) {
      /* Set the state changed callback */
      xhr.transport.onreadystatechange = function() {
        var states = {
          0: 'Create',
          1: 'Initialized',
          2: 'Sent',
          3: 'Loading',
          4: 'Complete'
        };

        if (xhr.transport.readyState == 4) {
          var text = xhr.transport.responseText;
          if (xhr.evalScripts && text) {
            var regexp = '<script[^>]*>([\\S\\s]*?)<\/script>';
            var scripts  = text.match(new RegExp(regexp, 'img')) || [];
            for (var i = 0; i < scripts.length; i++) {
              var script = scripts[i].match(new RegExp(regexp, 'im')) || ['', ''];
              try {
                script = script[1].replace(/\s/g, ' ');
                eval(script);
              }
              catch (e) {
                Krypsis.Log.error("Error in evaluating script: " + script);
              }
            }
          }

          xhr.dispatchStateEvent(xhr.transport.status >= 200 && xhr.transport.status < 300 ? "Success" : "Failure");
        }
        xhr.dispatchStateEvent(states[xhr.transport.readyState]);
      };
    },

    /**
     * Extends the given xhr instance by adding a dispatchStateEvent
     * function to the object. The appended function is used to
     * notify the listener whenever the internal state of the xhr has
     * changed.
     * 
     * @param {Object} xhr The XMLHttpRequest instance that should be extended.
     *
     * @private
     */
    applyDispatchEventFunction: function(xhr) {
      xhr.dispatchStateEvent = function(name, args) {
        var callback = this.xhrCallbacks['on' + name];
        if (callback) {
          if (args) {
            callback(this, args);
          }
          else {
            callback(this);
          }
          /* Delete callback to avoid double execution */
          this.xhrCallbacks['on' + name] = null;

          /* Indicate the callback was executed. */
          return true;
        }
        else {
          /* No callback found. Return false. */
          return false;
        }
      };
    },

    /**
     * Copies all function of the options object
     * that starts with 'on' to the xhr callbacks hash.
     *
     * @param {Object} xhr the XMLHttpRequest instance
     * @param {Object} options The options hash that was passed to the request function.
     *
     * @private
     */
    applyCallbacks: function(xhr, options) {
      xhr.xhrCallbacks = {};
      for (var key in options) {
        var value = options[key];
        if (key.indexOf('on') == 0 && typeof value == 'function') {
          xhr.xhrCallbacks[key] = value;
        }
      }
    },

    /**
     * Creates and returns a new instance of
     * the XmlHttpRequest object. In case of ie
     * earlier tha than version an instance of
     * ActiveXObject will be returned.
     *
     * @private
     */
    getXmlHttpRequest: function() {
      var transport = null;

      if (window.XMLHttpRequest) {
        transport = new XMLHttpRequest();
      }
      else if (window.ActiveXObject) {
        try {
          transport = new ActiveXObject('Microsoft.XMLHTTP');
        }
        catch (e) {
          transport = new ActiveXObject("Msxml2.XMLHTTP");
        }
      }
      
      return transport != null ? Krypsis.extend({}, { transport: transport }) : null;
    }
  },

  /**
   * The device object is the interface to the undelying
   * framework. It provides function to access the geo
   * location, photo functionality and additional sensor
   * data.
   * @static
   * @class The device abstraction layer to control and query
   * the smartphone
   */
  Device: {

    /**
     * The connector is the the command executer
     * and can als be replaced by a mockup.
     * Usefull for development sites where the
     * natibve framework is not avalable.
     * 
     * @private
     */
    connector: null,

    /**
     * Contains the the meta information about the
     * underlying mobile device, like the operating system, version
     * and so on. At the startup time of Krypsis this field is set
     * to null. You have to call the {@link Krypsis.Device.initialize()}
     * function to initialize and set this field with an object that
     * provides the meta information.
     * 
     * @field
     * @public
     */
    metaData: null,

    /**
     * <p>
     * Set the timeout for the command execution.
     * Default is set to 10000 (10 seconds)
     * </p>
     */
    timeout: 10000,

    /**
     * Returns the connector that fits the best to
     * this device.
     *
     * @private
     */
    guessConnector: function() {
      var connector = null;
      if (Krypsis.isDefined(typeof window.blackberry)) {
        connector = Krypsis.Device.BlackBerryConnector;
      }
      else if (Krypsis.isDefined(typeof window.symbian)) {
        connector = Krypsis.Device.SymbianConnector;
      }
      else {
        connector = Krypsis.Device.DefaultConnector;
      }
      return connector;
    },

    /**
     * <p>
     * Dispatches the given command to the connector
     * and let the Krypsis do the rest.
     * Warning: This function is private an not intended to be called directly.
     * It is only documented for the sake of completeness
     * </p>
     *
     * @param {Krypsis.Device.Task} task The task to dispatch
     * @private
     */
    dispatch: function(task) {
      if (!task) {
        Krypsis.Log.error("Can't dispatch task. It's null!");
        throw new Error("Task expected, got " + task);
      }

      /* Guess the connector if not already done. */
      if (!this.connector) {
        this.connector = this.guessConnector();
      }

      try {
        Krypsis.Log.debug(task.toString() + " dispatch task...");
        this.connector.execute(task.context, task.action, task.parameters || {});
      }
      catch(e) {
        Krypsis.Log.error("Device#dispatch: " + e);
        Krypsis.fireEvent(Krypsis.Device, 'onError', e);
        task.onFailure({code: Krypsis.Device.Error.NETWORK});
      }
    },

    /**
     * <p>
     * Use this function to get notified whenever a task raises
     * an exception. Your callback will be called and the exception
     * be passed to.
     * </p>
     *
     * @example
     * <pre>
     *  Krypsis.Device.addErrorListener(function(e) {
     *    alert("Error: " + e.message);
     *  });
     * </pre>
     *
     * @param {Function} callback the function to call when an error ocured.
     *
     * @public
     */
    addErrorListener: function(callback) {
      Krypsis.addListener(callback, this, 'onError');
    },

    /**
     * <p>
     * A task describes what kind of work the device has to do
     * and which results are expected. The tasks will be passed
     * to the execute method of the connector.
     * </p>
     *
     * @param {Object} module The targeted module
     * @param {String} action The action to execute
     * @param {Object} options Contains the parameters and callbacks
     * 
     * @class Represents a action request to the device.
     */
    Task: function(module, action, options) {
      var self = this;

      self.completed  = false;
      self.module     = module;
      self.context    = module.context;
      self.action     = action;
      self.options    = options || {};
      self.parameters = options.parameters || {};

      /* Wire the callback mechanism */
      self.module['Success'] = self.module['Success'] || {};
      self.module['Error']   = self.module['Error']   || {};

      /* If the on complete event should be wired, then add the listener */
      if (options.onComplete) {
        Krypsis.addListener(options.onComplete, self, 'onTaskCompleteEvent');
      }

      /**
       * When the device executes this command and everything
       * went well, then this onSuccess callback is executed.
       *
       * @param {Object} args The parameters from the device.
       */
      self.module['Success'][self.action] = function(args) {
        if (options.onSuccess) {
          Krypsis.Log.debug("[" + self.toString() + "] Execute the onSuccess callback.");
          options.onSuccess(args);
        }
        else {
          Krypsis.Log.warn("[" + self.toString() + "] The onSuccess callback is not given. If you need the execution result, then have to provide the onSuccess callback");
        }

        self._complete();
      };

      /**
       * In case the device throw an error, then this onFailure callback
       * is executed. The arguments provides a code that indicates the error cause.
       *
       * @param {Object} args The error params
       */
      self.module['Error'][self.action] = function(args) {
        if (options.onFailure) {
          Krypsis.Log.debug("[" + self.toString() + "] Execute the onFailure callback.");
          options.onFailure(args);
        }
        else {
          Krypsis.Log.warn("[" + self.toString() + "] The onFailure callback is not given. If you need the execution result, then have to provide the onFailure callback");
        }

        self._complete();
      };

      /**
       * Independent of the result of this command execution this function
       * is called after the onSuccess or onError callback to indicate
       * the completion of this.
       *
       * @private
       */
      this._complete = function() {
        self.completed = true;
        Krypsis.fireEvent(self, 'onTaskCompleteEvent');
      };

      /**
       * <p>
       * Executes this task by delegating the call to the dispatch action
       * of the device.
       * </p>
       *
       * @param {Function} callback The callback to call when this task is finished.
       * @public
       */
      this.execute = function (callback) {
        if (callback) {
          Krypsis.addListener(callback, self, 'onTaskCompleteEvent');
        }
        
        Krypsis.Device.dispatch(self);
      };

      /**
       * Print the module and action name of this task.
       */
      this.toString = function() {
        return self.module.context + "." + this.action;
      };
    },

    /**
     * Creates and returns a new device task with the module and action.
     * If the last parameter (execute) is set to true the task will also be
     * executed before returnment.
     *
     * @param {Object} module The module
     * @param {String} action The action
     * @param {Object} options The options
     * @param {Boolean} execute Should the task be executed or just returned. Default is true.
     *
     * @private
     */
    createTask: function(module, action, options, execute) {
      var task = new Krypsis.Device.Task(module, action, options || {});
      Krypsis.Log.debug("New task created: " + task.toString());
      if (execute != false) {
        Krypsis.Log.debug("Execute new task: " + task.toString());
        task.execute();
      }
      return task;
    },

    /**
     * <p>
     * The purpose of the base module is to provide
     * basic information about the device. Additionally it provides
     * a set of functions to control the device behaviour.
     * </p>
     *
     * <p>
     * For instance you can force a page reload or navigate to an external url.
     * </p>
     *
     * <p>
     * In the future it will be possible to store information on the device and
     * retreive it in another session.
     * </p>
     *
     * <strong>Since 0.1</strong>
     *
     * @static
     * @class Krypsis.Device.Base
     * @depends ../krypsis.js
     */
    Base: {

      /**
       * @private
       */
      context: "base",

      /**
       * <p>
       * Gets the meta data of the underlying mobile device.
       * The result contains information about the operating system,
       * the browser engine and also the world wide unique Krypsis device ID.
       * </p>
       *
       * <p>
       * A Krypsis ID will be generated as soon the installed Krypsis
       * application starts at the very first time. This ID will be saved
       * permanently and loaded at the next time. So the ID stays the same
       * until the Krypsis application is removed from the device.
       * </p>
       *
       * <p>
       * The intention of this ID the identification of each device.
       * For example, it is useful whenever settings should be saved
       * on server side without forcing the user to do a registration
       * and login, because this setting could be linked with the ID
       * instead of the user name.
       * </p>
       *
       * <p>
       * The Krypsis ID should not be used as an authentication
       * criteria but as a hint for the device identity.
       * </p>
       *
       * <p>
       * You do not have to provide any parameters, but the callbacks.
       * </p>
       *
       * <p>
       * The onSuccess callback will provide an object with the following properties:
       *
       * <ul>
       * <li><strong>{String} kid</strong> The generated, unique Krypsis ID.</li>
       * <li><strong>{String} os</strong> The device operating system. Possible values are: android,
       *    iphone, symbian, blackberry, windowsmobile. The values are complete lower case.</li>
       * <li><strong>{String} browser</strong> The used browser agent. e.g. webkit, ie, gecko, blackberry</li>
       * <li><strong>{String} version</strong> The current krypsis version. Usually a string containing the major and the
       *    minor version. e.g. 1.2</li>
       * <li><strong>{String} language</strong> The device language like de, en, it</li>
       * <li><strong>{String} country</strong> The device country like US, DE, AT</li>
       * </ul>
       *
       * @example
       * <pre>
       *  Krypsis.Device.Base.getMetaData({
       *    onSuccess: function(result) {
       *      // Do something with result
       *    }
       *  });
       *
       *  // or
       *
       *  var task = Krypsis.Device.Base.getMetaData({...}, false);
       *  task.execute();
       * </pre>
       *
       *
       * @param {Object} options parameters and callbacks
       * @param {Boolean} execute Shold the task be executed after creation. Default is true
       *
       * @since 0.1
       * @public
       */
      getMetaData: function(options, execute) {
        return Krypsis.Device.createTask(this, "getmetadata", options, execute);
      },

      /**
       * <p>
       * Resets the device completely. It means if there were any listeners before, then
       * the listeners will be stopped and removed. The complete state of the device will
       * be reseted.
       * </p>
       *
       * <p>
       * Of course, persistent information like the Krypsis Id or any other session
       * independent information will remain in the storage.
       * </p>
       *
       * <p>
       * This function does not expect any parameters. The default callbacks
       * <code>onSuccess</code> and <code>onError</code> are supported and
       * will be executed in the appropriate case.
       * </p>
       *
       * @example
       * <pre>
       * Krypsis.Device.Base.reset({});
       *
       * // or
       *
       * var task = Krypsis.Device.Base.reset({}, false);
       * task.execute();
       * </pre>
       *
       * @param {Object} options The callbacks
       * @param {Boolean} execute Should the task be executed. Default is true
       *
       * @since 0.1
       * @public
       */
      reset: function(options, execute) {
        return Krypsis.Device.createTask(this, 'reset', options, execute);
      }
    },

    /**
     * <p>
     * Represents the reason for a failure of a function
     * call.
     * </p>
     *
     * @static
     * @class The error after a command execution
     *
     * @public
     */
    Error: {

      /**
       * <p>
       * The error could not be described nearly. It is not known
       * what a problem we have here
       * </p>
       * 
       * @default 1
       * @type int
       * @constant 1
       * @public
       */
      UNKNOWN: 1,

      /**
       * <p>
       * A timeout occurs. The krypsis framework didn't aswer.
       * </p>
       *
       * @default 2
       * @type int
       * @constant
       * @public
       */
      TIMEOUT: 2,

      /**
       * <p>
       * The targeted module is not supported / implemented
       * </p>
       *
       * @default 3
       * @type int
       * @constant
       * @public
       */
      MODULE_NOT_SUPPORTED: 3,

      /**
       * <p>
       * There is something wrong with the network connection.
       * </p>
       *
       * @default 4
       * @type int
       * @constant
       * @public
       */
      NETWORK: 4,

      /**
       * <p>
       * One or many of the given parameters are
       * invalid and could not be proceeded.
       * </p>
       *
       * @default 5
       * @type int
       * @constant
       * @public
       */
      INVALID_PARAMETER: 5,

      /**
       * <p>
       * The executed command is not supported
       * by the framework
       * </p>
       *
       * @default 6
       * @type int
       * @constant
       * @public
       */
      COMMAND_NOT_SUPPORTED: 6,

      /**
       * <p>
       * On the requested features is not supported.
       * It could be the whole module or a part of it.
       * </p>
       *
       * @default 7
       * @type int
       * @constant
       * @public 
       */
      CAPABILITY_NOT_SUPPORTED: 7,

      /**
       * <p>
       * The requested observer is already running.
       * Only one instance of observation is possible at
       * the same time
       * </p>
       *
       * @default 8
       * @type int
       * @constant
       * @public
       */
      OBSERVER_STARTED: 8,

      /**
       * <p>
       * The user canceled the operation.
       * </p>
       *
       * @default 9
       * @type int
       * @constant
       * @public
       */
      CANCELED_BY_USER: 9
    },

    /**
     * The default connector is the implementation
     * of the connector interface that will meet in
     * most cases our requirements. In some cases we
     * have to provide another connector implementation.
     * For example a mock up
     *
     * @static
     * @class The default implementation of the connector interface
     *
     * @private
     */
    DefaultConnector: {

      /**
       * This namespace will be prefixed before each command url.
       *
       * @private
       */
      namespace: "http://localhost/org/krypsis/device",

      /**
       * Executes the given command the the given context.
       *
       * @param {String} context The module context (e.g. photo, location...)
       * @param {String} command The command itself (e.g. get, request, load....)
       * @param {Object} args The arguments to pass to the framework
       *
       * @private
       */
      execute: function(context, command, args) {
        args = Krypsis.isSet(args) ? args : null;

        var url = this.namespace + "/" + context + "/" + command;
        var params = Krypsis.Uri.toQueryString(args);

        try {
          url = params ? url + "?" + params : url;
          location.href = url;
        }
        catch(e) {
          Krypsis.Log.error("DefaultConnector exception: " + e);
        }
      },

      toString: function() {
        return "Default Connector";
      }
    },

    /**
     * The symbian connector sends the requests to a local
     * server that is listening on a well defined port.
     * Which port it is
     */
    SymbianConnector: {

      /* Use this address to connect to the local server */
      host: "http://localhost",
      namespace: "/org/krypsis/device/",

      /**
       * The default port is 50000. But this can change
       * by the underlying krypsis implementation.
       */
      port: 50000,

      execute: function(context, command, args) {
        args = Krypsis.isSet(args) ? args : null;

        var url = this.host + ":" + this.port + this.namespace + context + "/" + command;
        var params = Krypsis.Uri.toQueryString(args);

        try {
          url = params ? url + "?" + params : url;
          Krypsis.Log.debug("Call Symbian Implementation: " + url);
          new Image().src = url;
        }
        catch(e) {
          Krypsis.Log.error(this.toString() + " exception: " + e);
        }
      },

      toString: function() {
        return "Symbian Connector";
      }
    },

    /**
     * The blackberry connector is used to comunicate with a blackberry
     * device. The main difference to the default connector is the communication
     * over xhr. The connector implements an event driven ajax pattern which
     * means a connection will be opened and will wait for an event.
     * If an event occurs it will be delivered to the javascript world and
     * the connection will be reopened again.
     */
    BlackBerryConnector: {

      /**
       * This namespace will be prefixed before each command url.
       *
       * @private
       */
      namespace: "/org/krypsis/device/",

      /**
       * Executes the command by opening an xhr connection
       * to the underlying implementation.
       * 
       * @param {String} context The module context
       * @param {String} command The command for the module
       * @param {Object} args The arguments to pass to the command
       */
      execute: function(context, command, args) {
        args = Krypsis.isSet(args) ? args : null;


        var host = this.getHostName();
        var url = host + this.namespace + context + "/" + command;
        var params = Krypsis.Uri.toQueryString(args);
        url = params ? url + "?" + params : url;

        try {
          var self = this;

          if (!self.queue) { self.queue = Krypsis.Device.BlackBerryConnector.Queue; }

          /* Stop and wait for the queue. */
          Krypsis.Xhr.request(url, {

            onCreate: function() {
              self.queue.start();
            }
          });
        }
        catch(e) {
          Krypsis.Log.error("BlackBerryConnector exception: " + e);
        }
      },

      /**
       * Returns the hostname of the current loaded window instance.
       * The host name will be prefixed with the protocol and also
       * suffixed with the port. The host name does not contains a trailing
       * slash.
       *
       * @example
       * http://localhost:3000
       *
       * @private
       */
      getHostName: function() {
        if (!this.host) {
          /* Contains also the port */
          var host = window.location.host;
          var protocol = window.location.protocol || 'http:';
          this.host = protocol + "//" + host;
        }
        return this.host;
      },

      toString: function() {
        return "BlackBerry Connector";
      },

      /**
       * The queue is an implementation of the event driven AJAX
       * pattern. It opens up a connection to the krypsis implementation
       * and the java program holds this connection until a timeout
       * occurs or until a response is ready to be dispatched to the
       * browser.
       *
       * Each time the queue is returning back it will reconnect itself.
       */
      Queue: {
        /** Indicates if the queue is connectet o not*/
        running: false,
        aborted: false,
        queue: null,

        /**
         * Starts the queue.
         */
        start: function() {
          var self = this;
          if (self.running) { return true; }
          Krypsis.Log.debug("Start blackberry queue");

          var url = Krypsis.Device.BlackBerryConnector.namespace + "queue/start";
          self.queue = Krypsis.Xhr.request(url, {
            onSuccess: function(xhr) {
              try {
                var text = xhr && xhr.transport ? xhr.transport.responseText : null;
                if (text && text.length > 0) {
                  Krypsis.Log.debug("Evaluate response: " + text);
                  eval(text);
                }
              }
              catch(e) {
                Krypsis.Log.error("Could not evaluate response: " + e);
              }
            },

            onCreate:   function() {
              self.running = true;
              self.aborted = false; 
            },
            
            onComplete: function() {
              self.running = false;
              if (!self.aborted) {
                self.start();
              }
            }
          });
        },

        /**
         * Stops the queue by aborting the XMLHttpRequest
         * instance (if exists) and setting the running flag to
         * false.
         */
        stop: function() {
          if (this.queue) {
            Krypsis.Log.debug("Stopping BlackBerry response queue...");
            this.aborted = true;
            this.queue.transport.abort();
            this.running = false;
            this.queue = null;
            Krypsis.Log.debug("Stopped");
          }
        }
      }
    }
  }
};

/**
 * Iterates over the elements of this array and
 * pass each element into the given callback.
 * @param {Function} callback The callbck to call.
 */
Array.prototype.each = function(callback) {
  for (var i = 0; i < this.length; i++) {
    callback(this[i]);
  }
};

/**
 * Returns true if the given object is within the array.
 * Otherwise false.
 * @param {Object} value The value to test.
 */
Array.prototype.include = function (value) {
  for (var i = 0; i < this.length; i++) {
    if (this[i] === value) {
      return true;
    }
  }
  return false;
};

/**
 * <p>
 * To handle the krypsis tasks in an easy way, we have extended the Array
 * class and added this execute() function. It expects the elements to be
 * instances of the Krypsis.Device.Task class and calls the execute() function
 * on each task. When all tasks completed their work, then the given callback
 * will be called.
 * </p>
 *
 * <p>
 * As you sometimes need to combine some Krypsis results,
 * you can use this extension as a notification container.
 * </p>
 *
 * @example
 * <pre>
 * var metaData = null;
 * var response = null;
 *
 * var tasks = [
 *    Krypsis.Device.Base.getMetaData({
 *      onSuccess: function(args) { metaData = args; }
 *    }),
 *    Krypsis.Device.Photo.takeAndUpload({
 *      parameters = {...},
 *      onSuccess: function(args) { response = args.response; }
 *    });
 * ];
 *
 * tasks.execute(function() {
 *    if (!metaData || !response) {
 *      // This never happen.
 *      throw new Error("Not all tasks executed");
 *    }
 * });
 * </pre>
 *
 * @param {Function} callback The callback to execute when all tasks are finished.
 */
Array.prototype.execute = function(callback) {
  var self = this;
  self.tasksCompleted = false;

  if (callback) {
    Krypsis.addListener(callback, self, 'onTasksCompletedEvent');
  }

  /**
   * Will be called, when a task is finished.
   * Internaly a counter will be decreased and when it
   * reaches zero, then this execution is completed.
   *
   * @private
   */
  self.taskCompleted = function () {

    if (self.tasksCompleted) {
      return false;
    }

    var allTasksCompleted = true;

    self.each(function(task) {
      if (!task.completed) {
        allTasksCompleted = false;
      }
    });

    if (allTasksCompleted) {
      self.tasksCompleted = true;
      Krypsis.fireEvent(self, 'onTasksCompletedEvent');
    }
  };

  this.each(function(task) {
    if (task instanceof Krypsis.Device.Task) {
      Krypsis.Log.debug("Array: execute task: " + task.toString());
      task.execute(self.taskCompleted);
    }
    else {
      Krypsis.Log.debug("Array: Object is not an instance of Krypsis.Device.Task: " + task.toString());
      throw new Error(task + " is not an instance of Krypsis.Device.Task");
    }
  });
};

/**
 * <p>
 * This function is usefull whenever you need some default values
 * in this object but on the other hand don't want to overwrite existing
 * values. 
 * </p>
 *
 * @example
 * <pre>
 * var myObj = { key: 'value' };
 * myObj.applyDefaults({ key: 'another value', anotherKey: 'third value'});
 *
 * // myObj => {key: 'value', anotherKey: 'third value'} 
 * </pre>
 *
 * @param {Object} defaultValues An object with default values to apply to this object
 * @public
 */
Object.prototype.applyDefaults = function(defaultValues) {
  defaultValues = defaultValues || {};
  for (var key in defaultValues) {
    var value = this[key];

    if (typeof value == 'undefined') {
      this[key] = defaultValues[key];
    }
  }
  return this;
};