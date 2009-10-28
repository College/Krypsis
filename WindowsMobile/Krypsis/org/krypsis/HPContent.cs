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
    public class HPContent
    {
        private string html;
        private string contentEncoding;
        private string contentType;
        private List<string> allowHeader;
        private string url;

        public HPContent() {

        }

        /// <summary>
        /// Ruft den Quelltext der Webeite ab, oder setzt diesen.
        /// </summary>
        public string HTMLText {
            get { return html; }
            set { html = value; }
        }
        /// <summary>
        /// Ruft die Encoding der Websete ab, oder setzt diese.
        /// </summary>
        public string ContentEncoding {
            get { return contentEncoding; }
            set { contentEncoding = value; }
        }
        /// <summary>
        /// Ruft den Contenttype der Websete ab, oder setzt diesen.
        /// </summary>
        public string ContentType {
            get { return contentType; }
            set { contentType = value; }
        }
        /// <summary>
        /// Ruft die erlaubten Header dIeser Vearbeitung ab, oder setzt diese.
        /// </summary>
        public List<string> AllowHeader {
            get { return allowHeader; }
            set { allowHeader = value; }
        }
        /// <summary>
        /// Ruft die URL der Webseite ab oder setzt diese.
        /// </summary>
        public string URL {
            get { return url; }
            set { url = value; }
        }
    }
}
