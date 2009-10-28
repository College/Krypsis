using System;
using System.Collections.Generic;
using System.Drawing;
using System.Windows.Forms;
using System.IO;
using System.Text.RegularExpressions;
using System.Text;
using System.Net;

namespace WindowsMobile.org.krypsis 
{
    public static class NetCom
    {

      /* public HPContent ServerGETCommunication(AppConfiguration config, string url)
        {
            try
            {
                if (config.Cookie == null)
                {
                    GetToken(config);

                }
                HttpWebRequest request = (HttpWebRequest)WebRequest.Create(url);
                request.Method = "GET";
                //request.Headers["Cookie"] = config.Cookie;
                request.Credentials = CredentialCache.DefaultCredentials;
                HttpWebResponse response = (HttpWebResponse)request.GetResponse();
                HPContent hp = new HPContent();
                if (response.StatusCode == HttpStatusCode.OK)
                {
                    Stream dataStream = response.GetResponseStream();
                    StreamReader reader = new StreamReader(dataStream, Encoding.ASCII);
                    string htmltext = reader.ReadToEnd();
                    reader.Close();
                    dataStream.Close();
                    response.Close();
                    hp.HTMLText = htmltext;
                    hp.ContentEncoding = response.ContentEncoding;
                    hp.ContentType = response.ContentType;
                    hp.URL = response.ResponseUri.ToString();
                    return hp;
                }
                return null;
            }
            catch (Exception ex)
            {
                MessageBox.Show("NetCom.ServerGETCommunication(): " + ex.Message);
                return null;
            }
        }
      public HPContent ServerPOSTCommunication(AppConfiguration config, string postData, string url)
        {
            try
            {

                if (config.Cookie == null)
                {
                    GetToken(config);
                }
                //string postdata = "?" + config.TokenKey + @"=" + config.TokenValue + @"&latitude=" + latitude + @"&longitude=" + longitude;
                HttpWebRequest request = (HttpWebRequest)WebRequest.Create(url);
                request.Headers["Cookie"] = config.Cookie;
                //request.Credentials = CredentialCache.DefaultCredentials;
                request.Method = "POST";
                request.Timeout = System.Threading.Timeout.Infinite;
                byte[] byte1 = Encoding.UTF8.GetBytes(postData);
                request.ContentType = "application/x-www-form-urlencoded";
                request.ContentLength = byte1.Length;
                Stream stream = request.GetRequestStream();
                stream.Write(byte1, 0, byte1.Length);
                stream.Close();
                HttpWebResponse response = (HttpWebResponse)request.GetResponse();
                HPContent hp = new HPContent();
                if (response.StatusCode == HttpStatusCode.OK)
                {
                    Stream dataStream = response.GetResponseStream();
                    StreamReader reader = new StreamReader(dataStream, Encoding.ASCII);
                    string htmltext = reader.ReadToEnd();
                    reader.Close();
                    dataStream.Close();
                    response.Close();
                    hp.HTMLText = htmltext;
                    hp.ContentEncoding = response.ContentEncoding;
                    hp.ContentType = response.ContentType;
                    hp.URL = response.ResponseUri.ToString();
                    return hp;
                }
                return null;
            }
            catch (Exception ex)
            {
                MessageBox.Show("NetCom.ServerPOSTCommunication(): " + ex.Message);
                return null;
            }
            
        }
*/
        public static string UploadFileToRemoteUrl(string file, string url, string parametername)
        {
            try
            {

                FileStream fileStream = new FileStream(file, FileMode.Open, FileAccess.Read);
                BinaryReader br = new BinaryReader(fileStream);
                byte[] tb = br.ReadBytes((int)fileStream.Length);
                string boundary = System.Guid.NewGuid().ToString();
                HttpWebRequest request = (HttpWebRequest)WebRequest.Create(url);
                request.ContentType = "multipart/form-data; boundary=" + boundary;
                request.Method = "POST";
                string header = string.Format("--{0}", boundary);
                string footer = header + "--";
                StringBuilder contents = new StringBuilder();

                // Image
                contents.Append(header + "\r\n");
                contents.Append(string.Format("Content-Disposition: form-data; name=\""+parametername+"\"; filename=\"{0}\"\r\n", file.Substring(file.LastIndexOf('\\') + 1)));
                contents.Append("Content-Type: image/jpeg\r\n");
                contents.Append("\r\n");
                byte[] b1 = Encoding.UTF8.GetBytes(contents.ToString()); //TEST
                byte[] b2 = tb;
                byte[] b3 = Encoding.UTF8.GetBytes(footer + "\r\n");
                request.ContentLength = b1.Length+b2.Length+b3.Length;
                using (Stream requestStream = request.GetRequestStream())
                {
                    requestStream.Write(b1, 0, b1.Length);
                    requestStream.Write(b2, 0, b2.Length);
                    requestStream.Write(b3, 0, b3.Length);
                    requestStream.Flush();
                    requestStream.Close();
                    using (HttpWebResponse response = (HttpWebResponse)request.GetResponse())
                    {
                        using (StreamReader reader = new StreamReader(response.GetResponseStream()))
                        {
                            if (response.StatusCode == HttpStatusCode.OK)
                            {
                                MessageBox.Show("NetCom.UploadFileToRemoteUrl > [response erhalten]");
                                string str = reader.ReadToEnd();
                                reader.Close();
                                response.Close();
                                MessageBox.Show("NetCom.UploadFileToRemoteUrl > [Response beinhaltet "+str+"]");
                                
                                return str;
                            }
                            return null;

                        }
                    }
                }

            }catch(Exception ex){
                MessageBox.Show("NetCom.UploadFileToRemoteUrl(): " + ex.Message);
                return null;
            }            
        }

 /*       
        public AppConfiguration AppConfig
        {
            get
            {
                return this.applicationConfiguration;
            }
            set
            {
                this.applicationConfiguration = value;
            }
        }

*/
        private static string ByteArrayToString(byte[] arr)
        {
            System.Text.ASCIIEncoding enc = new System.Text.ASCIIEncoding();
            return enc.GetString(arr,0,arr.Length);
        }

        private static byte[] StringToByteArray(string str)
        {
            System.Text.ASCIIEncoding enc = new System.Text.ASCIIEncoding();
            return enc.GetBytes(str);
        }


/*       
        public bool GetToken(AppConfiguration config)
        {
            try
            {
                HttpWebRequest request = (HttpWebRequest)WebRequest.Create(config.UriTOKEN);
                request.Credentials = CredentialCache.DefaultCredentials;
                HttpWebResponse response = (HttpWebResponse)request.GetResponse();
                string tmpcookie = response.Headers["Set-Cookie"];
                tmpcookie = tmpcookie.Substring(tmpcookie.IndexOf('=') + 1, tmpcookie.IndexOf(';'));
                config.Cookie = tmpcookie.Substring(0, tmpcookie.IndexOf(';'));
                Stream dataStream = response.GetResponseStream();
                StreamReader reader = new StreamReader(dataStream, Encoding.ASCII);
                string str = reader.ReadToEnd();
                string[] s = str.Split('=');
                reader.Close();
                dataStream.Close();
                response.Close();
                config.TokenKey = s[0];
                config.TokenValue = s[1];
                return true;
            }
            catch (Exception ex)
            {

                return false;
            }

        }
 */ 
        /// <summary>
        /// Separiert die Parameter eines URL
        /// </summary>
        /// <param name="uri">Die URL mit Parameter</param>
        /// <param name="parameters">Ein Dictionary in das die Parameter geschrieben weren.</param>
        /// <returns>Anzahl der Parameter. Im Fehlerfall -1.</returns>
        public static Dictionary<string, string> GetURIParameter(string uri)
        {
            try {
                Dictionary<string, string> parameters = new Dictionary<string, string>();

                if (uri == null || uri.Equals(""))
                    return null;
                if (!uri.Contains("?"))
                    return null;
                if (parameters == null)
                    parameters = new Dictionary<string, string>();

                string resp = (uri.Substring(uri.IndexOf('?')+1)).ToLower().Trim();
                string[] keyvalue = resp.Split('&') ;
                string[] str;
                foreach (string s in keyvalue) {
                    str = s.Split('=');
                    parameters.Add(str[0], str[1]);
                }
                return parameters;
            }catch(Exception ex){
                return null;
            }
        }

    }
}
