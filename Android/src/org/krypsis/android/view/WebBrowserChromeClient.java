package org.krypsis.android.view;

import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import org.krypsis.android.Application;

/**
 * Date: 04.03.2009
 */
public class WebBrowserChromeClient extends WebChromeClient {
  private final Application application;

  public WebBrowserChromeClient(Application application) {
    this.application = application;
  }

  public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getApplication());
    builder.setTitle("Alert");
    builder.setMessage(message);
    
    builder.setPositiveButton(android.R.string.ok,
      new AlertDialog.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
          result.confirm();
        }
    });
    builder.setCancelable(false);
    builder.create();
    builder.show();

    return true;
  }

  public Application getApplication() {
    return application;
  }
}
