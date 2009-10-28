using System;
using WindowsMobile.org.krypsis.module;
using WindowsMobile.org.krypsis.module.screen;
using WindowsMobile.org.krypsis.module.photo;
using WindowsMobile.org.krypsis.module.base_;
using WindowsMobile.org.krypsis.module.location;
using System.Collections.Generic;
using System.Windows.Forms;
using System.Text;

namespace WindowsMobile.org.krypsis
{
    public class ModuleController
    {
        #region variables
        private Dictionary<string, Type> modulDictionary;
        private WebBrowser _browser;
        private string urlModule;
        private string urlFunction;
        private Dictionary<string, Module> modulesInWork;
        private Dictionary<string, string> urlParameter;
        #endregion

        public ModuleController(WebBrowser _browser) {
            this._browser = _browser;
            Module._browser = _browser;
           // System.Reflection.Assembly a = System.Reflection.Assembly.Load("WindowsMobile");
           // Module m = (Module)a.CreateInstance("ModuleScreen");
            object o = System.Reflection.Assembly.GetExecutingAssembly().CreateInstance("ModuleScreen");
            modulesInWork = new Dictionary<string, Module>();
            modulDictionary = new Dictionary<string, Type>();
            modulDictionary.Add("screen", typeof(ModuleScreen));
            modulDictionary.Add("photo", typeof(ModulePhoto));
            modulDictionary.Add("base", typeof(ModuleBase));
            modulDictionary.Add("location", typeof(ModuleLocation));

            // weitere Modulle hinzufügen
        }

        public void StartStandAloneWork(string url) {
            // Ermiteln des Mouls, der Funktion und der Funktionsparameter.
            string subUrl = url.Substring("http://localhost/org/krypsis/device/".Length);
            urlParameter = GetFunctionParameter(subUrl);
            string[] tmp = GetMuduleAndFunction(subUrl);
            urlModule = tmp[0];
            urlFunction = tmp[1];

            // Mdul identifizieren
            if(modulDictionary.ContainsKey(urlModule)){
                // Wenn das betreffende Modul bereits aktiv ist führe nur die Funktion aus.
                if (modulesInWork.ContainsKey(urlModule))
                {
                    modulesInWork[urlModule].ModuleFunctionIsReady += new EventHandler(m_ModuleFunctionIsReady);
                    modulesInWork[urlModule].Execute(urlFunction, urlParameter);
                    modulesInWork[urlModule].ThreadCount++;
                }
                else 
                {
                    Module m = (Module)Activator.CreateInstance(modulDictionary[urlModule]);
                    m.ModuleFunctionIsReady += new EventHandler(m_ModuleFunctionIsReady);
                    m.Execute(urlFunction, urlParameter);
                    modulesInWork[urlModule].ThreadCount++;
                    modulesInWork.Add(urlModule, m);       // Das Objekt soll nach dem der Arbeitsthread abgearbeitet wurde zerstöhrt werden.
                }

                // Fehlerbehandlung einbauen
                
            }


        }
        /// <summary>
        /// Löscht das Modul aus der Liste
        /// </summary>
        /// <param name="sender">Das Modul das gelöscht werden soll.</param>
        /// <param name="e">noch leer</param>
        void m_ModuleFunctionIsReady(object sender, EventArgs e)
        {
            MessageBox.Show("Funktion ind fertig, Modul wird entfernt!");
            Module m = (Module)sender;
            if (m.ThreadCount == 0)
                ;// Das Argument muss den Namen des moduls beinhalten um das Modul hier zu entfernen
        }


        private Dictionary<string, string> GetFunctionParameter(string subUrl)
        {
            return NetCom.GetURIParameter(subUrl);     // Parameter speichern und die Anzahl ermitteln.
        }

        private string[] GetMuduleAndFunction(string subUrl) {
            string module;
            if (subUrl.Contains("?"))
            {
                module = subUrl.Substring(0, subUrl.IndexOf('?'));
            }
            else
                module = subUrl;

            return module.Split('/');
        }
    }
}
