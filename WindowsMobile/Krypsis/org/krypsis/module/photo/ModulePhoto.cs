using System;
using System.Threading;
using System.Collections.Generic;
using System.Text;
using System.Windows.Forms;
using Microsoft.WindowsMobile.Status;
using Microsoft.WindowsMobile;
using Microsoft.WindowsCE.Forms;
using WindowsMobile.org.krypsis.module;


namespace WindowsMobile.org.krypsis.module.photo
{
    public class ModulePhoto : Module
    {
        #region variables

        #endregion

        #region constructor
        public ModulePhoto()
        {
            AddFunctionToModul(new ModuleFunctionPointer(TakeAndUpload), "takeandupload");
        }
        #endregion

      /* public override string Execute(string function, Dictionary<string, string> parameter)
        {
            ModuleFunctionPointer f = GetModulFunction(function);
            f(parameter);
            return null;
        }*/ 



        #region Photo-functions


        /// <summary>
        /// Erstellt ein Photo und läd es auf den webserver hoch.
        /// </summary>
        private string TakeAndUpload(Dictionary<string, string> parameter)
        {
            string response;                // Beinhaltet später den response des Servers.

            // Überprüft ob der Parameter uploadurl vom Server mitgeteilt wurde.
            if (!parameter.ContainsKey("uploadurl"))
            {
                _browser.Navigate(new Uri("javascript:Krypsis.Device.Photo.Error.takeandupload({code : 5})"));
                // MessageBox.Show("Device.TakeAndUpload > [Parameter uploadurl nicht vorhanden]");
                return "";
            }
            // Überprüft ob der Parameter parametername vom Server mitgeteilt wurde.
            if (!parameter.ContainsKey("parametername"))
            {
                _browser.Navigate(new Uri("javascript:Krypsis.Device.Photo.Error.takeandupload({code : 5})"));
                // MessageBox.Show("Device.TakeAndUpload > [Parameter parametername nicht vorhanden]");
                return "";
            }
            // Überprüft ob der Parameter filename vom Server mitgeteilt wurde und wenn ja, dann wid dieser
            // als Name für das Bild verwendet.
            if (parameter.ContainsKey("filename"))
            {
                // Wenn der "filename" Parameter übergeben wurde wird dieser alls Photoname benutzt
                DeviceAgent dataAgent = new DeviceAgent();
                string path = dataAgent.MakePhoto(parameter["filename"]);
                response = NetCom.UploadFileToRemoteUrl(path, parameter["uploadurl"], parameter["parametername"]);
                //   MessageBox.Show("Device.TakeAndUpload > Bilderpfad: ["+path+"]");
                return "";

            }
            // Wurde der Parameter filename nicht mitgeteilt wird ein Name aus Pic_ und dem Datum für das Bild erstelt.
            else
            {
                // Wenn kein "filename" Paramter übergeben wurde wird ein Name aus "Pic" und einem Zeitstempel ergeugt.
                SecurityProvider securityProvider = new SecurityProvider();
                
                DeviceAgent dataAgent = new DeviceAgent();
                string path = dataAgent.MakePhoto("Pic_" + securityProvider.Timestamp());
                response = NetCom.UploadFileToRemoteUrl(path, parameter["uploadurl"], parameter["parametername"]);
                //  MessageBox.Show("Device.TakeAndUpload > Bilderpfad: [" + path + "]");
                return "";
            }

            if (response != null && response != "")
            {
                _browser.Navigate(new Uri("javascript:Krypsis.Device.Photo.Success.takeandupload({response : '" + response + "'})"));
                //  MessageBox.Show("Device.TakeAndUpload > [Bild wurde hoch geladen!]");
                return "";
            }
            else
            {
                _browser.Navigate(new Uri("javascript:Krypsis.Device.Photo.Error.takeandupload({code : 5})"));
                return "";
            }

        }

        #endregion

    }
}
