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
package org.krypsis.android;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.webkit.WebView;
import org.krypsis.android.activity.ActivityResultListener;
import org.krypsis.android.command.base.GetMetaDataCommand;
import org.krypsis.android.command.base.LoadCommand;
import org.krypsis.android.command.base.ReloadCommand;
import org.krypsis.android.command.base.ResetCommand;
import org.krypsis.android.command.location.GetLocationCommand;
import org.krypsis.android.command.location.StartLocationObserverCommand;
import org.krypsis.android.command.location.StopLocationObserverCommand;
import org.krypsis.android.command.photo.TakeAndUploadCommand;
import org.krypsis.android.command.screen.GetScreenInfoCommand;
import org.krypsis.android.command.screen.StartOrientationListenerCommand;
import org.krypsis.android.command.screen.StopOrientationListenerCommand;
import org.krypsis.android.log.AndroidLoggerCreator;
import org.krypsis.android.view.WebBrowserChromeClient;
import org.krypsis.android.view.WebBrowserClient;
import org.krypsis.command.Bases;
import org.krypsis.command.Locations;
import org.krypsis.command.Photos;
import org.krypsis.command.Screens;
import org.krypsis.http.request.Requestable;
import org.krypsis.http.response.ErrorResponse;
import org.krypsis.http.response.Response;
import org.krypsis.http.response.ResponseDispatchable;
import org.krypsis.log.Logger;
import org.krypsis.log.LoggerFactory;
import org.krypsis.module.Module;
import org.krypsis.module.ModuleContext;
import org.krypsis.module.ModuleRegistry;
import org.krypsis.module.RootApplication;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is the intreface between the
 * web view component, the web view client and the
 * main application.
 */
public class Application extends Activity implements RootApplication, ResponseDispatchable {
  public static final HashMap<String, String> MIME_TYPES = new HashMap<String, String>();
  private final HashMap<String, Object> scope = new HashMap<String, Object>();
  private final ArrayList<ActivityResultListener> activityResultListeners = new ArrayList<ActivityResultListener>(); 

  private final Logger logger;

  /** Supported mime types */
  static {
    MIME_TYPES.put("image/jpeg", "jpg");
    MIME_TYPES.put("image/jpg",  "jpg");
    MIME_TYPES.put("image/png",  "png");
    MIME_TYPES.put("image/gif",  "gif");
  }

  private WebView webView;
  private WebBrowserClient webBrowserClient;

  public Application() {
    LoggerFactory.setLoggerCreator(new AndroidLoggerCreator());
    logger = LoggerFactory.getLogger(Application.class);
  }

  public void put(String key, Object value) throws IllegalArgumentException {
    scope.put(key, value);
  }

  public Object get(String key) {
    return scope.get(key);
  }

  public Object remove(String key) {
    return scope.remove(key);
  }

  public void reset() {
    ModuleRegistry.getInstance().reset();
  }

  /**
   * Dispatches the given request by detecting the responsible
   * module and and letting it doing the rest. 
   *
   * @param request The request to dispatch
   */
  public void dispatch(Requestable request) {
    try {
      final Module module = ModuleRegistry.getInstance().getModule(request.getModuleContext());
      if (module == null) {
        logger.error("Requested module '" + request.getModuleContext() + "' is not implemented or registered");
        dispatch(new ErrorResponse(request, ErrorResponse.MODULE_NOT_SUPPORTED));
      }
      else {
        logger.debug("Module '" + request.getModuleContext() + "' created. Execute action '" + request.getAction() + "' ...");
        module.handle(request, this);
      }
    }
    catch (Exception e) {
      logger.error("Unknown exception in dispatch(request): " + e);
      dispatch(new ErrorResponse(request, ErrorResponse.UNKNOWN));
    }
  }

  /**
   * Dispatches the given response by executing the javascript
   * function that is created by the response.
   *
   * @param response The response to dispatch
   */
  public void dispatch(Response response) {
    try {
      getWebView().loadUrl("javascript:" + response.toJavascriptFunction());
    }
    catch (Exception e) {
      logger.error("Unknown exception in dispatch(response): " + e);
    }
  }

  @Override
  protected void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    setContentView(R.layout.main);
    this.webView = (WebView) findViewById(R.id.browser);

    setWebBrowserClient(new WebBrowserClient(this));
    getWebView().setWebViewClient(getWebBrowserClient());
    getWebView().setWebChromeClient(new WebBrowserChromeClient(this));

    getWebView().getSettings().setJavaScriptEnabled(true);
    getWebView().getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

    logger.debug("Navigating to: " + getEnterUrl());
    getWebView().loadUrl(getEnterUrl());

    createModules();
  }

  @Override
  protected void onRestoreInstanceState(Bundle bundle) {
    super.onRestoreInstanceState(bundle);
  }

  @Override
  protected void onSaveInstanceState(Bundle bundle) {
    super.onSaveInstanceState(bundle);
  }

  @Override
  protected void onPause() {
    super.onPause();
    ModuleRegistry.getInstance().clear();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    super.onActivityResult(requestCode, resultCode, intent);
    logger.debug("onActivityResult returned with result: " + requestCode);

    for (ActivityResultListener listener : activityResultListeners) {
      listener.onActivityResult(requestCode,  resultCode, intent);
    }

    logger.debug("onActivityResult: Listener notified");
  }

  public void addActivityResultListener(ActivityResultListener listener) {
    activityResultListeners.add(listener);
  }

  public boolean removeActivityResultListener(ActivityResultListener listener) {
    return activityResultListeners.remove(listener);
  }

  public WebView getWebView() {
    return webView;
  }

  public void setWebView(WebView webView) {
    this.webView = webView;
  }

  public WebBrowserClient getWebBrowserClient() {
    return webBrowserClient;
  }

  public void setWebBrowserClient(WebBrowserClient webBrowserClient) {
    this.webBrowserClient = webBrowserClient;
  }

  private String getEnterUrl() {
    return getString(R.string.app_enter_url);
  }

  private void createModules() {
    final ModuleRegistry moduleRegistry = ModuleRegistry.getInstance();
    moduleRegistry.clear();

    final Module baseModule = new Module(this, ModuleContext.BASE);
    baseModule.registerCommand(Bases.GET_META_DATA, GetMetaDataCommand.class);
    baseModule.registerCommand(Bases.LOAD, LoadCommand.class);
    baseModule.registerCommand(Bases.RELOAD, ReloadCommand.class);
    baseModule.registerCommand(Bases.RESET, ResetCommand.class);

    final Module locationModule = new Module(this, ModuleContext.LOCATION);
    locationModule.registerCommand(Locations.GET_LOCATION, GetLocationCommand.class);
    locationModule.registerCommand(Locations.START_OBSERVE_LOCATION, StartLocationObserverCommand.class);
    locationModule.registerCommand(Locations.STOP_OBSERVE_LOCATION, StopLocationObserverCommand.class);

    final Module screenModule = new Module(this, ModuleContext.SCREEN);
    screenModule.registerCommand(Screens.GET_INFO, GetScreenInfoCommand.class);
    screenModule.registerCommand(Screens.START_OBSERVE_ORIENTATION, StartOrientationListenerCommand.class);
    screenModule.registerCommand(Screens.STOP_OBSERVE_ORIENTATION, StopOrientationListenerCommand.class);

    final Module photoModule = new Module(this, ModuleContext.PHOTO);
    photoModule.registerCommand(Photos.TAKE_AND_UPLOAD, TakeAndUploadCommand.class);

    moduleRegistry.register(baseModule);
    moduleRegistry.register(locationModule);
    moduleRegistry.register(screenModule);
    moduleRegistry.register(photoModule);
  }

  @Override
  public void onConfigurationChanged(Configuration configuration) {
    super.onConfigurationChanged(configuration);
  }
}
