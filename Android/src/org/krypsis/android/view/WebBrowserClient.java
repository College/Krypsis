package org.krypsis.android.view;

import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import org.krypsis.android.Application;
import org.krypsis.http.request.Request;
import org.krypsis.http.request.RequestException;

/**
 * Date: 04.03.2009
 *
 * The web view client is used to interact between
 * the web view and this application. Some of the
 * urls provides within a html page are special
 * command urls for this framework.
 *
 * This client will intercept theese commands and
 * delegate the calls to the WebBrowser component
 */
public class WebBrowserClient extends WebViewClient {
  private final Application application;

  /**
   * Creates a new webbrowser client with a reference to
   * the actual browser.
   * @param application The web browser component.
   */
  public WebBrowserClient(Application application) {
    this.application = application;
  }

  /**
   * If the web view requests an url that is actualy
   * a framework command, then override the url loading
   * and let the framework to the work
   * @param webView The affacted web view
   * @param url The url that should be called.
   * @return True if the url should not be loaded. Otherwise false.
   */
  @Override
  public boolean shouldOverrideUrlLoading(WebView webView, String url) {
    try {
      final Request request = new Request(url);
      getApplication().dispatch(request);
      return true;
    }
    catch (RequestException e) {
      return super.shouldOverrideUrlLoading(webView, url);
    }
  }

  public Application getApplication() {
    return application;
  }
}
