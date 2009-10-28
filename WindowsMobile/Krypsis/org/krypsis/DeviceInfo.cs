using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.Runtime.InteropServices;
using Microsoft.WindowsMobile.Status;
using Microsoft.WindowsMobile;
using Microsoft.WindowsCE.Forms;
using System.Globalization;



using System.Xml;
using System.Net;
using WindowsMobile.org.krypsis;
using Microsoft.Win32;


namespace WindowsMobile.org.krypsis
{

    public class DeviceInfo
    {
        [DllImport("coredll.dll")]
        private static extern bool SystemParametersInfo(uint uiAction, uint uiParam, StringBuilder pvParam, uint fWinIni);
        private static uint SPI_GETPLATFORMTYPE = 257;

        public static Platform GetPlatform() {
            Platform plat = Platform.Unknown;
            switch(System.Environment.OSVersion.Platform){
                case PlatformID.Win32NT:
                    plat = Platform.Win32NT;
                    break;
                case PlatformID.Win32S:
                    plat = Platform.Win32S;
                    break;
                case PlatformID.Win32Windows:
                    plat = Platform.Win32Windows;
                    break;
                case PlatformID.WinCE:
                    plat = CheckWinCEPlatform();
                    break;
                default:
                    plat = Platform.Unknown;
                    break;
            }
            return plat;
        }

        public static int GetDisplayOrientation(){
            switch(SystemSettings.ScreenOrientation){
                case(ScreenOrientation.Angle180):
                    return 180;
                case (ScreenOrientation.Angle270):
                    return 270;
                case(ScreenOrientation.Angle90):
                    return 90;
                default:
                    return 0;
            }
        }

        public static string GetDisplaySize() {
            int width = System.Windows.Forms.Screen.PrimaryScreen.Bounds.Width;
            int height = System.Windows.Forms.Screen.PrimaryScreen.Bounds.Height;
            string bounds = "" + width + " " + height;
            return bounds;
        }

        public static string[] GetPlatformVersion() {
            System.OperatingSystem os = System.Environment.OSVersion;
            string[] version  = new string[3] ;
            version[0] = os.Platform.ToString();
            version[1] = os.Version.Major.ToString();
            version[2] = os.Version.Minor.ToString();
            return version;
        }

        private static Platform CheckWinCEPlatform() {
            Platform plat = Platform.WindowsCE;
            StringBuilder strbuild = new StringBuilder(200);
            SystemParametersInfo(SPI_GETPLATFORMTYPE, 200, strbuild, 0);
            string str = strbuild.ToString();
            switch(str){
                case "PocketPC":
                    plat = Platform.PocketPC;
                    break;
                case "SmartPhone":
                    plat = Platform.Smartphone;
                    break;
            }
            return plat;
   
        }

        public void ChangeDisplayOrientation()
        {
            if (SystemSettings.ScreenOrientation == ScreenOrientation.Angle0)
            {
                SystemSettings.ScreenOrientation = ScreenOrientation.Angle270;
                Registry.SetValue(@"HKEY_LOCAL_MACHINE\System\GDI\ROTATION", "Angle", 270, RegistryValueKind.DWord);
            }
            else
            {
                SystemSettings.ScreenOrientation = ScreenOrientation.Angle0;
                Registry.SetValue(@"HKEY_LOCAL_MACHINE\System\GDI\ROTATION", "Angle", 0, RegistryValueKind.DWord);
            }
        }

        public static string GetSysstemLanguage() {
            string language = CultureInfo.CurrentCulture.Name;
            language = language.Substring(0,language.IndexOf("-"));
            return language;
        }

        public static string GetSystemCountry() {
            string language = CultureInfo.CurrentCulture.Name;
            language = language.Substring(language.IndexOf("-")+1);
            return language;
        }
    }
    public enum Platform { 
        PocketPC, WindowsCE, Smartphone, Win32NT, Win32S, Win32Windows, Unknown
    }
}
