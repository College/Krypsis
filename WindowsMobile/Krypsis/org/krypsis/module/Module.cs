using System;
using System.Collections.Generic;
using System.Text;
using System.Windows.Forms;
using System.Threading;

namespace WindowsMobile.org.krypsis.module
{
    public abstract class Module : ICommand
    {
        /// <summary>
        /// Ein Dictionary das als Schlüssel den Namen der Funktion und als Wert den Zeiger auf eine Funktion enthält.
        /// Die Variable enthält alle Funktionszeiger des Moduls. 
        /// </summary>
        protected Dictionary<string, ModuleFunctionPointer> functionPointerDictionary;
        /// <summary>
        /// Der Browser der Anwendung
        /// </summary>
        public static WebBrowser _browser;
        /// <summary>
        /// Event um das Ende der Abarbeitung der Funktion weiterzugeben.
        /// </summary>
        public event EventHandler ModuleFunctionIsReady;
        /// <summary>
        /// Liste mit Threads in denen die Modulfunktionen abgearbeitet werden.
        /// </summary>
        private Dictionary<string, Thread> threadList;
        /// <summary>
        /// Anzahl der aktiven Threads
        /// </summary>
        private int threadsInWork;
        /// <summary>
        /// Zwischenspeicher für die aufzurufende Funktion.
        /// </summary>
        ModuleFunctionPointer requestFunction;
        /// <summary>
        /// Zwiscenspeicher für die Parameter der aufzurufenden Funktion.
        /// </summary>
        Dictionary<string, string> requestFunctionParameter;


        public Module() {
            functionPointerDictionary = new Dictionary<string, ModuleFunctionPointer>();
            threadList = new Dictionary<string,Thread>();
            threadsInWork = 0;
            requestFunction = null; 
        }

        ~Module() { 
        
        }

        protected virtual void OnModuleFunctionIsReady(EventArgs e) {
            EventHandler moduleFunctionIsReady = ModuleFunctionIsReady;
            if (moduleFunctionIsReady != null)
            {
                moduleFunctionIsReady(this, e);
            }
        }

     //   public abstract string Execute(string function, Dictionary<string, string> parameter);
        /// <summary>
        /// Startet die Abarbeitung der Modulfunktion.
        /// </summary>
        /// <param name="function">Name der Funktion - entspricht dem Schlüssel in dem Dictionary functionPointerDictionary.</param>
        /// <param name="parameter">Die Parameter die an die Funktion übergäben werden.</param>
        /// <returns></returns>
        public string Execute(string function, Dictionary<string, string> parameter)
        {
            // Neuen Thread erstellen
            Thread t = new Thread(new ThreadStart(Run));
            // Dem Thread einen Namen zuweisen um ihn später zu identifizieren
            t.Name = ""+t.GetHashCode();
            MessageBox.Show("Thread mit Namen gestertet: "+t.Name);//TEST
            // Funktionsparameter zwischenspeichern
            requestFunctionParameter = parameter;
            // Funktionszeiger zwischenspeichern
            requestFunction = GetModulFunction(function);
            t.Start();
            threadsInWork++;
            threadList.Add(t.Name, t);
            // Wen die Funktionen der Mudule paralell abgearbeitet werden könn, muss die Funktion in einem Thread gestertet werden
            // und ert wenn der Thread zu Ende ist, darf das Event weiter gegeben werden.
            return null;
        }

        private void Run() {
            requestFunction(requestFunctionParameter);
            OnModuleFunctionIsReady(EventArgs.Empty);
            MessageBox.Show("Thread mit Namen beende: " + Thread.CurrentThread.Name);
            threadList.Remove(Thread.CurrentThread.Name);
            threadsInWork--;
        }

        /// <summary>
        /// Gibt die Anzahl der momentan aktiven Threads des Moduls zurück.
        /// </summary>
        public int ThreadCount{
            get { return threadsInWork; }
            set { threadsInWork = value; }
        }


        #region protected functions

        /// <summary>
        /// Fügt eine Funktion zu dem Modul hinzu.
        /// </summary>
        /// <param name="functionPointer">Zeiger auf die Funktion</param>
        /// <param name="functionName">Name der Funktion - entspricht dem Schlüssel in dem Dictionary functionPointerDictionary.</param>
        /// <returns>true wenn die Funktion hinzugefügt wurde.</returns>
        protected bool AddFunctionToModul(ModuleFunctionPointer functionPointer, string functionName) {
            try
            {
                functionPointerDictionary.Add(functionName, functionPointer);
                return true;
            }catch(Exception ex){
                return false;
            }
        }
        /// <summary>
        /// Sucht ob eine Funktion im Modul bereits registriert wurde und gibt diese zurück.
        /// </summary>
        /// <param name="function">Name der Funktion - entspricht dem Schlüssel in dem Dictionary functionPointerDictionary.</param>
        /// <returns>Zeiger auf die funktion wenn registriert, ánsonsten null</returns>
        protected ModuleFunctionPointer GetModulFunction(string function)
        {
            if(functionPointerDictionary.ContainsKey(function)){
                return functionPointerDictionary[function];
            }
            return null;
        }
        #endregion
    }
}
