using System;
using System.Net;
using System.Drawing.Imaging;
using System.Collections.Generic;
using Microsoft.WindowsMobile.Forms;
using System.Text;
using System.IO;
using System.Reflection;
using System.Windows.Forms;
using System.Text.RegularExpressions;
using System.Xml;
using GPSLocater;


namespace WindowsMobile.org.krypsis
{
    public class DeviceAgent
    {

        public string MakePhoto(string photoname)
        {
            try
            {
                CameraCaptureDialog camera = new CameraCaptureDialog();
                camera.Title = "Photo";
                camera.Mode = CameraCaptureMode.Still;
                camera.Resolution = new System.Drawing.Size(1024, 768);
                camera.DefaultFileName = photoname;
                //camera.DefaultFileName = "Bild";
                if (camera.ShowDialog() == System.Windows.Forms.DialogResult.OK /*&& camera.FileName.Length > 0*/)
                {
                    return camera.FileName;
                }
                return null;
            }catch(Exception ex){
                return null;
            }
            
        }

    }

}
