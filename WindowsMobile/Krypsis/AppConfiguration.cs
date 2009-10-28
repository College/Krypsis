using System;

using System.Collections.Generic;
using System.Text;
using System.Windows.Forms;
using WindowsMobile.org.krypsis;

namespace WindowsMobile
{
    public class AppConfiguration
    {
        #region Private Variables
        private CfgFile cfgFile;           // Representiert die ini bzw conf Datei
        private string cookie;
        private string tokenURI;
        private string tokenKey;
        private string tokenValue;
        private string uriNEW, uriSEARCH, uriPHOTO, uriFAVORITES;
        #endregion
        #region CONSTRUCTOR
        public AppConfiguration(string confinipath) {
            string path;
            path = System.IO.Path.GetDirectoryName(System.Reflection.Assembly.GetExecutingAssembly().GetName().CodeBase);
            cfgFile = new CfgFile(path + confinipath /*"\\confini.ci"*/);
            UriNEW = cfgFile.getValue("URL", "NEW", true);
            UriSEARCH = cfgFile.getValue("URL", "SEARCH", true);
            UriFAVORITES = cfgFile.getValue("URL", "FAVORITES", true);
            UriPHOTO = cfgFile.getValue("URL", "PHOTO", true);
            UriTOKEN = cfgFile.getValue("URL", "TOKEN", true);
            TokenKey = cfgFile.getValue("DATA", "TOKENKEY", true);
            TokenValue = cfgFile.getValue("DATA", "TOKENVALUE", true);
            Cookie = cfgFile.getValue("DATA", "COOKIE", true);

        }
        #endregion
        #region GETSET
        public string UriSEARCH
        {
            get { return uriSEARCH; }
            set
            {
                if (value.Length == 0 || value == "")
                    uriSEARCH = null;
                else
                {
                    uriSEARCH = value;
                    cfgFile.setValue("URL", "SEARCH", value, true);
                    cfgFile.Save();
                }
            }
        }

        public string UriPHOTO
        {
            get { return uriPHOTO; }
            set
            {
                if (value.Length == 0 || value == "")
                    uriPHOTO = null;
                else
                {
                    uriPHOTO = value;
                    cfgFile.setValue("URL", "PHOTO", value, true);
                    cfgFile.Save();
                }
            }
        }

        public string UriFAVORITES
        {
            get { return uriFAVORITES; }
            set
            {
                if (value.Length == 0 || value == "")
                    uriFAVORITES = null;
                else
                {
                    uriFAVORITES = value;
                    cfgFile.setValue("URL", "FAVORITES", value, true);
                    cfgFile.Save();
                }
            }
        }

        public string UriNEW {
            get { return uriNEW; }
            set
            {
                if (value.Length == 0 || value == "")
                    uriNEW = null;
                else
                {
                    uriNEW = value;
                    cfgFile.setValue("URL", "NEW", value, true);
                    cfgFile.Save();
                }
            }
        }

        public string TokenKey {
            get { return tokenKey; }
            set
            {
                if (value.Length == 0 || value == "")
                    tokenKey = null;
                else
                {
                    tokenKey = value;
                    cfgFile.setValue("DATA", "TOKENKEY", value, true);
                    cfgFile.Save();
                   // MessageBox.Show(TokenKey);
                }
            }
        }

        public string TokenValue {
            get { return tokenValue; }
            set
            {
                if (value.Length == 0 || value == "")
                    tokenValue = null;
                else
                {
                    tokenValue = value;
                    cfgFile.setValue("DATA", "TOKENVALUE", value, true);
                    cfgFile.Save();
                  //  MessageBox.Show(tokenValue);
                }
            }
        }

        public string UriTOKEN {
            get { return tokenURI; }
            set
            {
                if (value.Length == 0 || value == "")
                    tokenURI = null;
                else
                {
                    tokenURI = value;
                    cfgFile.setValue("URL", "TOKEN", value, true);
                    cfgFile.Save();
                 }
            }
        }

        public string Cookie
        {
            get { return cookie; }
            set
            {
                if (value.Length == 0 || value == "")
                    cookie = null;
                else
                {
                    cookie = value;
                    cfgFile.setValue("DATA", "COOKIE", value, true);
                    cfgFile.Save();
                  //  MessageBox.Show(cookie);
                }
            }
        }
        #endregion
    }
}
