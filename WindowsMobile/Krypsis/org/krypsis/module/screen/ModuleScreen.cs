using System;
using WindowsMobile.org.krypsis.module;
using System.Collections.Generic;
using System.Text;
using System.Windows.Forms;
using Microsoft.WindowsMobile.Status;

namespace WindowsMobile.org.krypsis.module.screen
{
    public class ModuleScreen : Module
    {
        #region variables
        private SystemState displayOrientation;         // Für die Überwachung der Displayorientierung                   // Browser für die Kommunikation
        #endregion

        #region constructor
        public ModuleScreen() {
            displayOrientation = null;
            AddFunctionToModul(new ModuleFunctionPointer(GetInfo), "getinfo");
            AddFunctionToModul(new ModuleFunctionPointer(StartObserveOrientation), "startobserveorientation");
            AddFunctionToModul(new ModuleFunctionPointer(StopObserveOrientation), "stopobserveorientation");
        }
        #endregion



      /*  public override string Execute(string function, Dictionary<string, string> parameter)
        {
            ModuleFunctionPointer f = GetModulFunction(function);
            f(parameter);
            return null;
        }*/


        #region Screen-functions
        /// <summary>
        /// GetInfo ermittelt die Displaygröße und die Displayorientierung des Gerätes und sendet dies an den server.
        /// </summary>
        private string GetInfo(Dictionary<string, string> parameter)
        {
            string[] display = ((string)(DeviceInfo.GetDisplaySize())).Split(' ');
            int orientation = DeviceInfo.GetDisplayOrientation();
            _browser.Navigate(new Uri("javascript:Krypsis.Device.Screen.Success.getinfo({width : " + display[0] + ", height : " + display[1] + ", orientation : " + orientation + "})"));
            //MessageBox.Show("Device.GetInfo [ Größe und orientierung gesendet "+ orientation+"]");
            //deviceThread = null;
            return "";
        }

        /// <summary>
        /// Startet einen Listener der eine Änderung der Displayausrichtund erfasst.
        /// </summary>
        private string StartObserveOrientation(Dictionary<string, string> parameter)
        {
            // MessageBox.Show("Device.GetInfo [ Starte observer ]");
            if (displayOrientation != null)
            {
                //deviceThread = null;
                return "";
            }

            displayOrientation = new SystemState(SystemProperty.DisplayRotation);
            displayOrientation.Changed += new ChangeEventHandler(displayOrientation_Changed);
            //deviceThread = null;

            return "";
        }
        /// <summary>
        /// Meldet die Änderung der Displayausrichtung dem Server
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="args"></param>
        void displayOrientation_Changed(object sender, ChangeEventArgs args)
        {
            _browser.Navigate(new Uri("javascript:Krypsis.Device.Screen.Success.startobserveorientation({orientation : " + DeviceInfo.GetDisplayOrientation() + "})"));
        }

        /// <summary>
        /// Stopt den Listener zur erfassung der Displayausrichtung.
        /// </summary>

        private string StopObserveOrientation(Dictionary<string, string> parameter)
        {
            // MessageBox.Show("Device.GetInfo [ Stope server ]");
            if (displayOrientation == null)
            {
                //deviceThread = null;
                return "";
            }
            displayOrientation.Changed -= new ChangeEventHandler(displayOrientation_Changed);
            displayOrientation = null;
            //deviceThread = null;
            return "";
        }

        #endregion

    }
}
