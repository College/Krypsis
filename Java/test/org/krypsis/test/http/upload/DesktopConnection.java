package org.krypsis.test.http.upload;

import org.krypsis.http.upload.Connectable;

import javax.microedition.io.HttpConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.*;

/**
 * Date: 21.05.2009
 * ToDo JavaSodc
 */
public class DesktopConnection implements Connectable {
  private final int BUFFER_SIZE = 1024;
  private final URL url;
  private final URLConnection connection;
  private StringBuffer data;

  public DesktopConnection(String remoteUrl) throws IOException {
    this.url = new URL(remoteUrl);
    this.connection = url.openConnection();
    connection.setDoInput(true);
    connection.setDoOutput(true);
    connection.setUseCaches(false);
    ((HttpURLConnection) connection).setRequestMethod("POST");
  }

  public void setRequestProperty(String key, String value) throws IOException {
    connection.addRequestProperty(key, value);
  }

  public void write(StringBuffer buffer) {
    if (this.data == null) {
      this.data = buffer;
    }
    else {
      this.data.append(buffer);
    }
  }

  public void write(String data) {
    write(new StringBuffer(data));
  }

  public String send() throws IOException {
    return send(this.data != null ? this.data.toString() : null);
  }

  public String send(String data) throws IOException {
    final OutputStream outputStream = connection.getOutputStream();
    final byte[] bytes = data.getBytes("ISO-8859-1");
    outputStream.write(bytes);
    outputStream.flush();
    outputStream.close();

    final InputStream inputStream = connection.getInputStream();

    int len;
    byte[] buffer = new byte[256];
    StringBuffer raw = new StringBuffer();

    while (-1 != (len = inputStream.read(buffer))) {
      raw.append(new String(buffer, 0, len));
    }


    return raw.toString();
  }
}
