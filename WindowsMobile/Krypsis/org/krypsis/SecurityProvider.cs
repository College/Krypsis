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
    public class SecurityProvider
    {
        
        private Random rnd;
        private static int[] nums = { 58, 59, 60, 61, 62, 63, 64, 91, 92, 93, 94, 95, 96 };
        private static List<int> forbiddenNumbers = new List<int>(nums);
        public SecurityProvider() {
            rnd = null;
        }

        /// <summary>
        /// Erstellt eine zufällige, eindeutige Zeichenfolge (ID) aus Zahlen Sonderzeichen und aus Groß- und- Kleinbuchstaben.
        /// </summary>
        /// <param name="number">Wie viel Zeichen soll die eindeutige Zeichenfolge beinhalten. </param>
        /// <returns>Die eindeutige Zeichenfolge.</returns>
        public string GetUniqueID(int number)
        {
            try
            {
                rnd = new Random();
                string tocken = "";
                for (int i = 0; i < number; i++)
                {
                    tocken = tocken + GetrandomChar();
                }
                return tocken;
            }catch(Exception ex){
                return null;
            }
        }

        public string GetUniqueIDWithTimestamp(int number) {
            try
            {
                rnd = new Random();
                string tocken = "";
                for (int i = 0; i < number; i++)
                {
                    tocken = tocken + GetrandomChar();
                }


                tocken += "@" + Timestamp();
                return tocken;
            }
            catch (Exception ex)
            {
                return null;
            }
        }

        /// <summary>
        /// Generiert ein zufälliges Zeichen aus Buchstaben, Zahlen und einigen Sonderzeichen. 
        /// </summary>
        /// <returns>Ein Zeichen</returns>
        private char GetrandomChar()
        {
            int rc = rnd.Next(48, 122);
            if (forbiddenNumbers.Contains(rc))
                rc = GetrandomChar();    
            return Convert.ToChar(rc);
        }

        /// <summary>
        /// Erstellt einen Zeitstempel und gibt diesen als String zurück.
        /// </summary>
        /// <returns>Zeitstempel</returns>
        public string Timestamp() {
            DateTime timeBase = new DateTime(1970, 1, 1);
            TimeSpan timeDiff = DateTime.Now.ToUniversalTime() - timeBase;
            return ((double)timeDiff.TotalMilliseconds).ToString();
        }
    }
}
