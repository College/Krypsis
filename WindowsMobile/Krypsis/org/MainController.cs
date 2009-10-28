using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.Xml;
using System.Net;
using WindowsMobile.org.krypsis.module.screen;
using WindowsMobile.org.krypsis;
using Microsoft.WindowsMobile.Status;
using Microsoft.WindowsCE.Forms;
using Microsoft.Win32;



namespace WindowsMobile
{
    class MainController
    {
        private ToolBar _controlBbar;
        private WebBrowser _browser;
        Form1 _mainForm;
        ModuleController moduleController;
        

        public MainController(Form1 mainForm) {
          
            this._mainForm = mainForm;
            this._controlBbar = mainForm.ControlBar;
            this._browser = mainForm.Browser;
            _browser.Navigating += new WebBrowserNavigatingEventHandler(_browser_Navigating);
            _controlBbar.ButtonClick += new ToolBarButtonClickEventHandler(controlBbar_ButtonClick);
            

            StringBuilder sb = new StringBuilder();
            sb.Append("<html><body>Test</body></html>");
            _browser.DocumentText = " ";
            _browser.DocumentText = " ";
            _browser.DocumentText = sb.ToString();


            moduleController = new ModuleController(_browser);

        }


        void _browser_Navigating(object sender, WebBrowserNavigatingEventArgs e)
        {
            try {
                if (e.Url.ToString().StartsWith("http://localhost/org/krypsis/device/"))
                {
                    
                    moduleController.StartStandAloneWork(e.Url.ToString());

                    e.Cancel = true;
                }
            }catch(Exception ex){
                MessageBox.Show("MainController._browser_Navigating  "+ex.Message);
            }
        }

        void controlBbar_ButtonClick(object sender, ToolBarButtonClickEventArgs e)
        {
            
            if (e.Button == _mainForm.TbbtnBack)
            {
                //_browser.Navigate(new Uri("http://georize.de:9494/demo/index.html"));
                _browser.Navigate(new Uri("http://demo.krypsis.org/demo/index.html"));
            }
            else if (e.Button == _mainForm.TbbtnForward)
            {
                string path = System.IO.Path.GetDirectoryName(System.Reflection.Assembly.GetExecutingAssembly().GetName().CodeBase);
                CfgFile cfg = new CfgFile(path + "\\confini.ci");
                string id = cfg.getValue("DATA", "uniqueid", true);
                if (id == "") {
                    MessageBox.Show("---");
                    SecurityProvider sp = new SecurityProvider();
                    id = sp.GetUniqueIDWithTimestamp(32);
                    cfg.setValue("DATA", "uniqueid", id ,true);
                    cfg.Save();
                }
                MessageBox.Show(id);
            }
            else if (e.Button == _mainForm.TbbtnNew)
            {

            }
            else if (e.Button == _mainForm.TbbtnSearch)
            {
              
            }
            else if (e.Button == _mainForm.TbbtnShow)
            {

            }
            else if (e.Button == _mainForm.TbbtnFavorites)
            {
                MessageBox.Show(DeviceInfo.GetSystemCountry()+"   "+DeviceInfo.GetSysstemLanguage());
            }
        }


        




    }
}



/*  NUTZLICHE ABRISSE
 -------- 
 string path;
 path = System.IO.Path.GetDirectoryName(System.Reflection.Assembly.GetExecutingAssembly().GetName().CodeBase);
 HPContent hp = _NetCom.UploadFileToRemoteUrl(_AppConfig, path+"\\Waterfall.jpg", "http://georize.de:9393/mobile/user/upload_temporary_image/");
 _browser.DocumentText = hp.HTMLText;
 --------------
  HPContent hp = _NetCom.ServerPOSTCommunication(_AppConfig, "?latitude=1.1&longitude=2.2", "http://georize.de:9393/mobile/locations/new/");
  _browser.DocumentText = hp.HTMLText;
  -------------
 
 
  
 */