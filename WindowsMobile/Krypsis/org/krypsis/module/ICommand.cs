using System;
using System.Collections.Generic;
using System.Text;

namespace WindowsMobile.org.krypsis.module
{
    public delegate string ModuleFunctionPointer(Dictionary<string, string> parameter);
    interface ICommand
    {
        string Execute(string function, Dictionary<string, string> parameter);
    }
}
