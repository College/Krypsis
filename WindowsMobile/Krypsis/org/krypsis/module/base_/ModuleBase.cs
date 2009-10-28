using System;
using System.Collections.Generic;
using System.Text;
using System.Windows.Forms;
using WindowsMobile.org.krypsis.module;

namespace WindowsMobile.org.krypsis.module.base_
{
    public class ModuleBase : Module
    {

        #region variables
        #endregion

        #region constructor
        public ModuleBase()
        {
            AddFunctionToModul(new ModuleFunctionPointer(Load), "load");
            AddFunctionToModul(new ModuleFunctionPointer(GetMetaData), "getmetadata");
            AddFunctionToModul(new ModuleFunctionPointer(Reload), "reload");
        }
        #endregion

        #region Base-fonctions

        private string GetMetaData(Dictionary<string, string> parameter)
        {
            //Platform platform = DeviceInfo.GetPlatform();
           
            string[] platform = DeviceInfo.GetPlatformVersion();
            _browser.Navigate(new Uri("javascript:Krypsis.Device.Base.Success.getmetadata({kid :'" + GetID() + "', os : 'windowsmobile', browser : 'ie', browserVersion : '6.1', language : '"+ DeviceInfo.GetSysstemLanguage() +"', country : '"+DeviceInfo.GetSystemCountry()+"'})"));
            return "";
        }

        private string Load(Dictionary<string, string> parameter)
        {
            if (!parameter.ContainsKey("url"))
            {
                _browser.Navigate(new Uri("javascript:Krypsis.Device.Base.Error.load({ code : ?})"));
                return "";
            }
            if (!parameter["url"].StartsWith("http://"))
            {
                _browser.Navigate(new Uri("javascript:Krypsis.Device.Base.Error.load({ code : ?})"));
                return "";
            }
            _browser.Navigate(new Uri(parameter["url"]));
            return "";

        }

        private string Reload(Dictionary<string, string> parameter)
        {
            _browser.Refresh();
            return "";
        }

        #endregion

        #region helpfunctions
        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        private string GetID() {
            string path = System.IO.Path.GetDirectoryName(System.Reflection.Assembly.GetExecutingAssembly().GetName().CodeBase);
            CfgFile cfg = new CfgFile(path + "\\confini.ci");
            string id = cfg.getValue("DATA", "uniqueid", true);
            if (id == "")
            {
                SecurityProvider sp = new SecurityProvider();
                id = sp.GetUniqueIDWithTimestamp(32);
                cfg.setValue("DATA", "uniqueid", id, true);
                cfg.Save();
            }
            return id;
        }

        #endregion

    }
}
