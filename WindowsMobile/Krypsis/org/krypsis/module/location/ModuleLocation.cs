using System;
using WindowsMobile.org.krypsis.module;
using System.Collections.Generic;
using System.Text;
using System.Windows.Forms;
using GPSLocater;


namespace WindowsMobile.org.krypsis.module.location
{
    public class ModuleLocation : Module
    {
        #region variables
        private GPSLocate gps;
        #endregion

        #region constructor
        public ModuleLocation()
        {
            AddFunctionToModul(new ModuleFunctionPointer(Get), "get");
       //     AddFunctionToModul(new ModuleFunctionPointer(), "");
       //     AddFunctionToModul(new ModuleFunctionPointer(), "");
        }
        #endregion

        public string Get(Dictionary<string, string> parameter)
        {
            gps = new GPSLocate("COM4");
            gps.GPSEvent += new MyGPSEventHandler(test);
            gps.GpsReadThreadStart();
            return "";
        }
        // TEST
        private void test(string a, string b, string c) {
            MessageBox.Show("a = "+a+" b = "+b+" c = "+c);
            gps.GpsReadThreadStop();
        }

    }
}
