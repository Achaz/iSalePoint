package dm.jb.ui;

import dm.jb.db.objects.CompanyInfoRow;
import dm.jb.db.objects.CompanyInfoTableDef;
import dm.jb.db.objects.UserInfoRow;
import dm.jb.db.objects.UserInfoTableDef;
import dm.jb.op.sync.FileOpUtils;
import dm.jb.op.sync.InvalidFileException;
import dm.jb.ui.billing.BillingUI;
import dm.jb.ui.inv.CustomerManagePanel;
import dm.jb.ui.inv.ProductSetup;
import dm.jb.ui.report.ReportUIPanel;
import dm.jb.ui.res.ResourceUtils;
import dm.jb.ui.settings.Configuration;
import dm.jb.ui.settings.PrintingSetupPanel;
import dm.tools.db.DBException;
import dm.tools.ui.UICommon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MainMenu
  extends JMenuBar
{
  private JMenuItem _mPrinterSettingsMenu = null;
  private JMenuItem _mCompanySetupMenu = null;
  private JMenuItem _mWareHouseMenu = null;
  private JMenuItem _mStoreMenu = null;
  private JMenuItem _mTerminalMenu = null;
  private JMenuItem _mOpenMenuItem = null;
  private JMenuItem _mUserMenuItem = null;
  private JMenuItem _mVendorMenuItem = null;
  private JMenuItem _mCustomerMenuItem = null;
  private JMenuItem _mCustomerHistoryMenuItem = null;
  private JMenu _mInventoryMenu = null;
  private JMenuItem _mReportMenu = null;
  private JMenuItem _mConfigMenuItem = null;
  private JMenuItem _mCleanupMenuItem = null;
  private JMenuItem _mPOMenuItem = null;
  private JMenuItem _mGIMenuItem = null;
  private JMenuItem _mGRMenuItem = null;
  private JMenuItem _mWHSMenuItem = null;
  private JMenuItem _mPOStockMenuItem = null;
  private JMenuItem _mEditStockMenuItem = null;
  private JMenuItem _mBarcodeMenuItem = null;
  
  public MainMenu()
  {
    addMenus();
  }
  
  private void addMenus()
  {
    CompanyInfoRow localCompanyInfoRow = null;
    try
    {
      localCompanyInfoRow = CompanyInfoTableDef.getInstance().getCompany();
    }
    catch (DBException localDBException)
    {
      localDBException.printStackTrace();
    }
    addFileMenu();
    addSettingsMenu();
    if (localCompanyInfoRow != null) {
      addReportMenu();
    }
    addHelpMenu();
  }
  
  private void addFileMenu()
  {
    CompanyInfoRow localCompanyInfoRow = null;
    try
    {
      localCompanyInfoRow = CompanyInfoTableDef.getInstance().getCompany();
    }
    catch (DBException localDBException)
    {
      localDBException.printStackTrace();
    }
    JMenu localJMenu = new JMenu();
    ResourceUtils.setMenuString("MENU_FILE", localJMenu);
    add(localJMenu);
    this._mOpenMenuItem = new JMenuItem();
    ResourceUtils.setMenuItemString("MENU_OPEN", this._mOpenMenuItem);
    localJMenu.add(this._mOpenMenuItem);
    this._mOpenMenuItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        MainMenu.this.fileOpenClicked();
      }
    });
    if (localCompanyInfoRow != null)
    {
      this._mPrinterSettingsMenu = new JMenuItem();
      ResourceUtils.setMenuItemString("MENU_PRINTER_SETTINGS", this._mPrinterSettingsMenu);
      localJMenu.add(this._mPrinterSettingsMenu);
      this._mPrinterSettingsMenu.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          MainMenu.this.settingsPritingClicked();
        }
      });
    }
    localJMenu.addSeparator();
    JMenuItem localJMenuItem = new JMenuItem();
    ResourceUtils.setMenuItemString("MENU_FILE_EXIT", localJMenuItem);
    localJMenu.add(localJMenuItem);
    localJMenuItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        System.exit(0);
      }
    });
  }
  
  private void addSettingsMenu()
  {
    JMenu localJMenu = new JMenu();
    ResourceUtils.setMenuString("MENU_MANAGE", localJMenu);
    add(localJMenu);
    this._mCompanySetupMenu = new JMenuItem();
    ResourceUtils.setMenuItemString("MENU_MANAGE_COMPANY", this._mCompanySetupMenu);
    localJMenu.add(this._mCompanySetupMenu);
    this._mCompanySetupMenu.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        MainMenu.this.companyConfigClicked();
      }
    });
    CompanyInfoRow localCompanyInfoRow = null;
    try
    {
      localCompanyInfoRow = CompanyInfoTableDef.getInstance().getCompany();
    }
    catch (DBException localDBException)
    {
      localDBException.printStackTrace();
    }
    if (localCompanyInfoRow == null) {
      return;
    }
    this._mWareHouseMenu = new JMenuItem();
    ResourceUtils.setMenuItemString("MENU_MANAGE_WAREHOUSE", this._mWareHouseMenu);
    localJMenu.add(this._mWareHouseMenu);
    this._mWareHouseMenu.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        MainMenu.this.wearehouseConfigClicked();
      }
    });
    this._mStoreMenu = new JMenuItem();
    ResourceUtils.setMenuItemString("MENU_MANAGE_STORES", this._mStoreMenu);
    localJMenu.add(this._mStoreMenu);
    this._mStoreMenu.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        MainMenu.this.storesConfigClicked();
      }
    });
    this._mTerminalMenu = new JMenuItem();
    ResourceUtils.setMenuItemString("MENU_MANAGE_TERMINALS", this._mTerminalMenu);
    localJMenu.add(this._mTerminalMenu);
    this._mTerminalMenu.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        MainMenu.this.sitesConfigClicked();
      }
    });
    this._mUserMenuItem = new JMenuItem();
    ResourceUtils.setMenuItemString("MENU_MANAGE_USERS", this._mUserMenuItem);
    localJMenu.add(this._mUserMenuItem);
    this._mUserMenuItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        MainMenu.this.employeeManageClicked();
      }
    });
    localJMenu.addSeparator();
    this._mCustomerMenuItem = new JMenuItem();
    ResourceUtils.setMenuItemString("MENU_MANAGE_CUSTOMERS", this._mCustomerMenuItem);
    localJMenu.add(this._mCustomerMenuItem);
    this._mCustomerMenuItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent) {}
    });
    this._mCustomerHistoryMenuItem = new JMenuItem();
    ResourceUtils.setMenuItemString("MENU_MANAGE_CUSTOMER_P_H", this._mCustomerHistoryMenuItem);
    localJMenu.add(this._mCustomerHistoryMenuItem);
    this._mCustomerHistoryMenuItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent) {}
    });
    this._mVendorMenuItem = new JMenuItem();
    ResourceUtils.setMenuItemString("MENU_MANAGE_VENDORS", this._mVendorMenuItem);
    localJMenu.add(this._mVendorMenuItem);
    this._mVendorMenuItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        MainMenu.this.settingsManageVendor();
      }
    });
    this._mBarcodeMenuItem = new JMenu();
    ResourceUtils.setMenuItemString("MENU_MANAGE_BARCODE", this._mBarcodeMenuItem);
    localJMenu.add(this._mBarcodeMenuItem);
    JMenuItem localJMenuItem1 = new JMenuItem();
    ResourceUtils.setMenuItemString("MENU_MANAGE_PRODUCT_BARCODE", localJMenuItem1);
    this._mBarcodeMenuItem.add(localJMenuItem1);
    localJMenuItem1.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        MainMenu.this.settingsPrintBarCodesClicked();
      }
    });
    this._mInventoryMenu = new JMenu();
    ResourceUtils.setMenuString("MENU_MANAGE_PRODUCTS", this._mInventoryMenu);
    localJMenu.add(this._mInventoryMenu);
    localJMenu.addSeparator();
    JMenuItem localJMenuItem2 = new JMenuItem();
    ResourceUtils.setMenuItemString("MENU_MANAGE_PRODUCTS_PRODUCTS", localJMenuItem2);
    this._mInventoryMenu.add(localJMenuItem2);
    localJMenuItem2.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        MainMenu.this.settingsProductsManageProductClicked();
      }
    });
    JMenuItem localJMenuItem3 = new JMenuItem();
    ResourceUtils.setMenuItemString("MENU_MANAGE_PRODUCTS_CATEGORIES", localJMenuItem3);
    this._mInventoryMenu.add(localJMenuItem3);
    localJMenuItem3.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        MainMenu.this.settingsCategoryManageClicked();
      }
    });
    JMenuItem localJMenuItem4 = new JMenuItem();
    ResourceUtils.setMenuItemString("MENU_MANAGE_PRODUCTS_DEPARTMENTS", localJMenuItem4);
    this._mInventoryMenu.add(localJMenuItem4);
    localJMenuItem4.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        MainMenu.this.settingsDepartmentNewDepartmentClicked();
      }
    });
    this._mConfigMenuItem = new JMenuItem();
    ResourceUtils.setMenuItemString("MENU_MANAGE_CONFIGURATION", this._mConfigMenuItem);
    localJMenu.add(this._mConfigMenuItem);
    this._mConfigMenuItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        MainMenu.this.settingsConfigClicked();
      }
    });
    this._mCleanupMenuItem = new JMenuItem();
    ResourceUtils.setMenuItemString("MENU_MANAGE_CLEANUP", this._mCleanupMenuItem);
    localJMenu.add(this._mCleanupMenuItem);
    this._mCleanupMenuItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent) {}
    });
    localJMenu = new JMenu();
    ResourceUtils.setMenuString("MENU_MANAGE_STOCK", localJMenu);
    add(localJMenu);
    this._mPOMenuItem = new JMenuItem();
    ResourceUtils.setMenuItemString("MENU_MANAGE_PO", this._mPOMenuItem);
    localJMenu.add(this._mPOMenuItem);
    this._mPOMenuItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        MainMenu.this.productPOClicked();
      }
    });
    this._mPOStockMenuItem = new JMenuItem();
    ResourceUtils.setMenuItemString("MENU_MANAGE_NEW_WITH_PO", this._mPOStockMenuItem);
    localJMenu.add(this._mPOStockMenuItem);
    this._mPOStockMenuItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        MainMenu.this.productPONewStockClicked();
      }
    });
    this._mGIMenuItem = new JMenuItem();
    ResourceUtils.setMenuItemString("MENU_MANAGE_GOODS_INWARD", this._mGIMenuItem);
    this._mGIMenuItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        MainMenu.this.productNewStockClicked();
      }
    });
    localJMenu.add(this._mGIMenuItem);
    this._mGRMenuItem = new JMenu("Goods Return");
    localJMenu.add(this._mGRMenuItem);
    ResourceUtils.setMenuItemString("MENU_MANAGE_GOODS_RETURN", this._mGRMenuItem);
    JMenuItem localJMenuItem5 = new JMenuItem();
    ResourceUtils.setMenuItemString("MENU_MANAGE_GOODS_RETURN_NEW", localJMenuItem5);
    this._mGRMenuItem.add(localJMenuItem5);
    localJMenuItem5.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        MainMenu.this.productGoodsReturnNewClicked();
      }
    });
    JMenuItem localJMenuItem6 = new JMenuItem("View");
    ResourceUtils.setMenuItemString("MENU_MANAGE_GOODS_RETURN_VIEW", localJMenuItem6);
    this._mGRMenuItem.add(localJMenuItem6);
    localJMenuItem6.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        MainMenu.this.productGoodsReturnViewClicked();
      }
    });
    this._mWHSMenuItem = new JMenuItem();
    ResourceUtils.setMenuItemString("MENU_MANAGE_GOODS_WH_TO_STORE", this._mWHSMenuItem);
    this._mWHSMenuItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        MainMenu.this.productAllotStockClicked();
      }
    });
    localJMenu.add(this._mWHSMenuItem);
    this._mEditStockMenuItem = new JMenuItem("Edit Stock");
    ResourceUtils.setMenuItemString("MENU_MANAGE_GOODS_EDIT_STOCK", this._mEditStockMenuItem);
    this._mEditStockMenuItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent) {}
    });
    localJMenu.add(this._mEditStockMenuItem);
  }
  
  private void addReportMenu()
  {
    JMenu localJMenu = new JMenu();
    ResourceUtils.setMenuString("MENU_REPORT", localJMenu);
    add(localJMenu);
    JMenuItem localJMenuItem = new JMenuItem();
    this._mReportMenu = localJMenuItem;
    ResourceUtils.setMenuItemString("MENU_REPORT_TEMPLATES", localJMenuItem);
    localJMenu.add(localJMenuItem);
    localJMenuItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        MainMenu.this.reportFromTemplates();
      }
    });
  }
  
  private void addHelpMenu()
  {
    JMenu localJMenu = new JMenu();
    ResourceUtils.setMenuString("MENU_HELP", localJMenu);
    add(localJMenu);
    JMenuItem localJMenuItem1 = new JMenuItem();
    ResourceUtils.setMenuItemString("MENU_HELP_CONTENT", localJMenuItem1);
    localJMenu.add(localJMenuItem1);
    localJMenuItem1.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ISPHelpLauncher.INSTANCE.showHelp("ISP_INTRO", MainWindow.instance);
      }
    });
    JMenuItem localJMenuItem2 = new JMenuItem();
    ResourceUtils.setMenuItemString("MENU_HELP_ABOUT", localJMenuItem2);
    localJMenu.add(localJMenuItem2);
    localJMenuItem2.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        AboutDialog.getInstance().setVisible(true);
      }
    });
  }
  
  public void enableMenus()
  {
    UserInfoRow localUserInfoRow = UserInfoTableDef.getCurrentUser();
    if (this._mOpenMenuItem != null) {
      if (localUserInfoRow.hasAccess(1024L)) {
        this._mOpenMenuItem.setEnabled(true);
      } else {
        this._mOpenMenuItem.setEnabled(false);
      }
    }
    if (this._mPrinterSettingsMenu != null) {
      if (localUserInfoRow.hasAccess(16L)) {
        this._mPrinterSettingsMenu.setEnabled(true);
      } else {
        this._mPrinterSettingsMenu.setEnabled(false);
      }
    }
    if (this._mCompanySetupMenu != null) {
      if (localUserInfoRow.hasAccess(64L)) {
        this._mCompanySetupMenu.setEnabled(true);
      } else {
        this._mCompanySetupMenu.setEnabled(false);
      }
    }
    if (this._mWareHouseMenu != null) {
      if (localUserInfoRow.hasAccess(128L)) {
        this._mWareHouseMenu.setEnabled(true);
      } else {
        this._mWareHouseMenu.setEnabled(false);
      }
    }
    if (this._mStoreMenu != null) {
      if (localUserInfoRow.hasAccess(256L))
      {
        this._mStoreMenu.setEnabled(true);
        this._mTerminalMenu.setEnabled(true);
      }
      else
      {
        this._mStoreMenu.setEnabled(false);
        this._mTerminalMenu.setEnabled(false);
      }
    }
    if (this._mUserMenuItem != null) {
      if (localUserInfoRow.hasAccess(1L)) {
        this._mUserMenuItem.setEnabled(true);
      } else {
        this._mUserMenuItem.setEnabled(false);
      }
    }
    if (this._mCustomerMenuItem != null) {
      if (localUserInfoRow.hasAccess(32L))
      {
        this._mCustomerMenuItem.setEnabled(true);
        this._mCustomerHistoryMenuItem.setEnabled(true);
      }
      else
      {
        this._mCustomerMenuItem.setEnabled(false);
        this._mCustomerHistoryMenuItem.setEnabled(false);
      }
    }
    if (this._mInventoryMenu != null) {
      if (localUserInfoRow.hasAccess(2L))
      {
        this._mInventoryMenu.setEnabled(true);
        this._mVendorMenuItem.setEnabled(true);
        this._mBarcodeMenuItem.setEnabled(true);
      }
      else
      {
        this._mInventoryMenu.setEnabled(false);
        this._mVendorMenuItem.setEnabled(false);
        this._mBarcodeMenuItem.setEnabled(false);
      }
    }
    if (this._mConfigMenuItem != null) {
      if (localUserInfoRow.hasAccess(8L)) {
        this._mConfigMenuItem.setEnabled(true);
      } else {
        this._mConfigMenuItem.setEnabled(false);
      }
    }
    if (this._mCleanupMenuItem != null) {
      if (localUserInfoRow.hasAccess(2048L)) {
        this._mCleanupMenuItem.setEnabled(true);
      } else {
        this._mCleanupMenuItem.setEnabled(false);
      }
    }
    if (this._mPOMenuItem != null) {
      if (localUserInfoRow.hasAccess(4096L)) {
        this._mPOMenuItem.setEnabled(true);
      } else {
        this._mPOMenuItem.setEnabled(false);
      }
    }
    if (this._mPOStockMenuItem != null) {
      if (localUserInfoRow.hasAccess(4L))
      {
        this._mPOStockMenuItem.setEnabled(true);
        this._mGIMenuItem.setEnabled(true);
        this._mGRMenuItem.setEnabled(true);
        this._mWHSMenuItem.setEnabled(true);
      }
      else
      {
        this._mPOStockMenuItem.setEnabled(false);
        this._mGIMenuItem.setEnabled(false);
        this._mGRMenuItem.setEnabled(false);
        this._mWHSMenuItem.setEnabled(false);
      }
    }
    if (this._mEditStockMenuItem != null) {
      if (localUserInfoRow.hasAccess(512L)) {
        this._mEditStockMenuItem.setEnabled(true);
      } else {
        this._mEditStockMenuItem.setEnabled(false);
      }
    }
    if (this._mReportMenu != null) {
      if (localUserInfoRow.hasAccess(8192L)) {
        this._mReportMenu.setEnabled(true);
      } else {
        this._mReportMenu.setEnabled(false);
      }
    }
  }
  
  private void settingsProductsManageProductClicked() {}
  
  private void settingsPrintBarCodesClicked() {}
  
  private void settingsDepartmentNewDepartmentClicked() {}
  
  private void settingsCategoryManageClicked() {}
  
  private void viewBillingNewClicked() {}
  
  private void viewBillingViewClicked() {}
  
  private void settingsConfigClicked() {}
  
  private void companyConfigClicked() {}
  
  private void employeeManageClicked() {}
  
  private void storesConfigClicked() {}
  
  private void sitesConfigClicked() {}
  
  private void wearehouseConfigClicked() {}
  
  private void settingsPritingClicked() {}
  
  private void settingsManageVendor() {}
  
  private void productNewStockClicked() {}
  
  private void productPOClicked() {}
  
  private void productPONewStockClicked() {}
  
  private void productAllotStockClicked() {}
  
  private void productGoodsReturnNewClicked() {}
  
  private void productGoodsReturnViewClicked() {}
  
  private void productViewStockClicked() {}
  
  private void reportFromTemplates() {}
  
  private void syncProdClicked() {}
  
  private void fileOpenClicked()
  {
    JFileChooser localJFileChooser = new JFileChooser();
    int i = localJFileChooser.showOpenDialog(MainWindow.instance);
    if (i == 0)
    {
      File localFile = localJFileChooser.getSelectedFile();
      try
      {
        boolean bool = FileOpUtils.INSTANCE.processDataFile(localFile.getAbsolutePath());
        if (!bool)
        {
          UICommon.showError("Error opening file.", "Error", MainWindow.instance);
          return;
        }
      }
      catch (DBException localDBException)
      {
        localDBException.printStackTrace();
        UICommon.showError("Database error processing file.\n\nPlease contact administrator.", "Error", MainWindow.instance);
        return;
      }
      catch (IOException localIOException)
      {
        localIOException.printStackTrace();
        UICommon.showError("Error reading the file.", "Error", MainWindow.instance);
        return;
      }
      catch (InvalidFileException localInvalidFileException)
      {
        localInvalidFileException.printStackTrace();
        UICommon.showError("Error reading the file.", "Error", MainWindow.instance);
        return;
      }
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.MainMenu
 * JD-Core Version:    0.7.0.1
 */