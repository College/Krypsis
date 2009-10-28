using System;

using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;

namespace WindowsMobile
{

    public partial class Form1 : Form
    {

        private MainController _MinController;

        public Form1()
        {
            InitializeComponent();
            _MinController = new MainController(this);
       
        }

        public ToolBarButton TbbtnBack {
            get { return tbbtnBack; }
        }

        public ToolBarButton TbbtnForward
        {
            get { return tbbtnForward; }
        }

        public ToolBarButton TbbtnNew
        {
            get { return tbbtnNew; }
        }

        public ToolBarButton TbbtnFavorites
        {
            get { return tbbtnFavorites; }
        }

        public ToolBarButton TbbtnSearch
        {
            get { return tbbtnSearch; }
        }

        public ToolBarButton TbbtnShow
        {
            get { return tbbtnShow; }
        }

        public ToolBar ControlBar {
            get { return toolBar1; }
        }

        public WebBrowser Browser {
            get { return browser; }
        }
    }
}