namespace WindowsMobile
{
    partial class Form1
    {
        /// <summary>
        /// Erforderliche Designervariable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Verwendete Ressourcen bereinigen.
        /// </summary>
        /// <param name="disposing">True, wenn verwaltete Ressourcen gelöscht werden sollen; andernfalls False.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Vom Windows Form-Designer generierter Code

        /// <summary>
        /// Erforderliche Methode für die Designerunterstützung.
        /// Der Inhalt der Methode darf nicht mit dem Code-Editor geändert werden.
        /// </summary>
        private void InitializeComponent()
        {
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(Form1));
            this.mainPanel = new System.Windows.Forms.Panel();
            this.browser = new System.Windows.Forms.WebBrowser();
            this.toolBar1 = new System.Windows.Forms.ToolBar();
            this.tbbtnBack = new System.Windows.Forms.ToolBarButton();
            this.tbbtnForward = new System.Windows.Forms.ToolBarButton();
            this.tbbtnFavorites = new System.Windows.Forms.ToolBarButton();
            this.tbbtnSearch = new System.Windows.Forms.ToolBarButton();
            this.tbbtnNew = new System.Windows.Forms.ToolBarButton();
            this.tbbtnShow = new System.Windows.Forms.ToolBarButton();
            this.buttonImages = new System.Windows.Forms.ImageList();
            this.mainPanel.SuspendLayout();
            this.SuspendLayout();
            // 
            // mainPanel
            // 
            this.mainPanel.Controls.Add(this.browser);
            this.mainPanel.Dock = System.Windows.Forms.DockStyle.Fill;
            this.mainPanel.Location = new System.Drawing.Point(0, 0);
            this.mainPanel.Name = "mainPanel";
            this.mainPanel.Size = new System.Drawing.Size(240, 268);
            // 
            // browser
            // 
            this.browser.Dock = System.Windows.Forms.DockStyle.Fill;
            this.browser.Location = new System.Drawing.Point(0, 0);
            this.browser.Name = "browser";
            this.browser.Size = new System.Drawing.Size(240, 268);
            // 
            // toolBar1
            // 
            this.toolBar1.Buttons.Add(this.tbbtnBack);
            this.toolBar1.Buttons.Add(this.tbbtnForward);
            this.toolBar1.Buttons.Add(this.tbbtnFavorites);
            this.toolBar1.Buttons.Add(this.tbbtnSearch);
            this.toolBar1.Buttons.Add(this.tbbtnNew);
            this.toolBar1.Buttons.Add(this.tbbtnShow);
            this.toolBar1.ImageList = this.buttonImages;
            this.toolBar1.Name = "toolBar1";
            // 
            // tbbtnBack
            // 
            this.tbbtnBack.ImageIndex = 0;
            // 
            // tbbtnForward
            // 
            this.tbbtnForward.ImageIndex = 1;
            // 
            // tbbtnFavorites
            // 
            this.tbbtnFavorites.ImageIndex = 2;
            // 
            // tbbtnSearch
            // 
            this.tbbtnSearch.ImageIndex = 4;
            // 
            // tbbtnNew
            // 
            this.tbbtnNew.ImageIndex = 5;
            // 
            // tbbtnShow
            // 
            this.tbbtnShow.ImageIndex = 3;
            this.buttonImages.Images.Clear();
            this.buttonImages.Images.Add(((System.Drawing.Image)(resources.GetObject("resource"))));
            this.buttonImages.Images.Add(((System.Drawing.Image)(resources.GetObject("resource1"))));
            this.buttonImages.Images.Add(((System.Drawing.Image)(resources.GetObject("resource2"))));
            this.buttonImages.Images.Add(((System.Drawing.Image)(resources.GetObject("resource3"))));
            this.buttonImages.Images.Add(((System.Drawing.Image)(resources.GetObject("resource4"))));
            this.buttonImages.Images.Add(((System.Drawing.Image)(resources.GetObject("resource5"))));
            this.buttonImages.Images.Add(((System.Drawing.Image)(resources.GetObject("resource6"))));
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(96F, 96F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Dpi;
            this.AutoScroll = true;
            this.ClientSize = new System.Drawing.Size(240, 268);
            this.Controls.Add(this.toolBar1);
            this.Controls.Add(this.mainPanel);
            this.Name = "Form1";
            this.Text = "Form1";
            this.mainPanel.ResumeLayout(false);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Panel mainPanel;
        private System.Windows.Forms.WebBrowser browser;
        private System.Windows.Forms.ToolBar toolBar1;
        private System.Windows.Forms.ToolBarButton tbbtnBack;
        private System.Windows.Forms.ToolBarButton tbbtnForward;
        private System.Windows.Forms.ToolBarButton tbbtnFavorites;
        private System.Windows.Forms.ToolBarButton tbbtnSearch;
        private System.Windows.Forms.ToolBarButton tbbtnNew;
        private System.Windows.Forms.ToolBarButton tbbtnShow;
        private System.Windows.Forms.ImageList buttonImages;
    }
}

