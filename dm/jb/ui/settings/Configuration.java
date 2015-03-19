package dm.jb.ui.settings;

import dm.jb.db.objects.CompanyInfoRow;
import dm.jb.db.objects.CompanyInfoTableDef;
import dm.jb.db.objects.UserInfoRow;
import dm.jb.db.objects.UserInfoTableDef;
import dm.jb.ui.MainWindow;
import dm.tools.db.DBException;
import dm.tools.ui.AbstractMainPanel;
import dm.tools.ui.ActionPanel;
import dm.tools.ui.QuickTask;
import dm.tools.ui.QuickTaskBox;
import dm.tools.ui.QuickTaskPane;
import dm.tools.ui.QuickTaskPane.TaskGroup;
import dm.tools.ui.UICommon;

public class Configuration
{
  private static QuickTaskPane _mTaskPane = null;
  private static QuickTaskPane.TaskGroup _mConfigGroup = null;
  
  public static void configPanel()
  {
    QuickTaskBox localQuickTaskBox = QuickTaskBox.getInstance();
    if (!localQuickTaskBox.getName().equals("Configuration"))
    {
      setupActionList();
      localQuickTaskBox.setTaskPane(_mTaskPane);
      localQuickTaskBox.setName("Configuration");
    }
    ActionPanel localActionPanel = MainWindow.getActionPanel();
    _mConfigGroup.expand();
    AbstractMainPanel localAbstractMainPanel = (AbstractMainPanel)ConfigurationPanel.getConfigPanel();
    localActionPanel.cleanPush(localAbstractMainPanel);
    localAbstractMainPanel.setActionPanel(localActionPanel);
    localActionPanel.setTitle("Configuration");
    MainWindow.instance.repaint();
    localAbstractMainPanel.setDefaultFocus();
  }
  
  public static void cleanupPanel()
  {
    QuickTaskBox localQuickTaskBox = QuickTaskBox.getInstance();
    if (!localQuickTaskBox.getName().equals("Configuration"))
    {
      setupActionList();
      localQuickTaskBox.setTaskPane(_mTaskPane);
      localQuickTaskBox.setName("Configuration");
    }
    ActionPanel localActionPanel = MainWindow.getActionPanel();
    _mConfigGroup.expand();
    CleanupUI localCleanupUI = CleanupUI.INSTANCE;
    localCleanupUI.clearAllFields();
    localActionPanel.cleanPush(localCleanupUI);
    localCleanupUI.setActionPanel(localActionPanel);
    localActionPanel.setTitle("Cleanup");
    MainWindow.instance.repaint();
    localCleanupUI.setDefaultFocus();
  }
  
  public static void storeManagePanel()
  {
    QuickTaskBox localQuickTaskBox = QuickTaskBox.getInstance();
    if (!localQuickTaskBox.getName().equals("Configuration"))
    {
      setupActionList();
      localQuickTaskBox.setTaskPane(_mTaskPane);
      localQuickTaskBox.setName("Configuration");
    }
    ActionPanel localActionPanel = MainWindow.getActionPanel();
    _mConfigGroup.expand();
    StoreManagePanel localStoreManagePanel = StoreManagePanel.getInstance();
    localActionPanel.cleanPush(localStoreManagePanel);
    localStoreManagePanel.setActionPanel(localActionPanel);
    localActionPanel.setTitle("Manage Stores");
    MainWindow.instance.repaint();
    localStoreManagePanel.setDefaultFocus();
  }
  
  public static void userManagePanel()
  {
    QuickTaskBox localQuickTaskBox = QuickTaskBox.getInstance();
    if (!localQuickTaskBox.getName().equals("Configuration"))
    {
      setupActionList();
      localQuickTaskBox.setTaskPane(_mTaskPane);
      localQuickTaskBox.setName("Configuration");
    }
    ActionPanel localActionPanel = MainWindow.getActionPanel();
    _mConfigGroup.expand();
    UserManagePanel localUserManagePanel = UserManagePanel.getInstance();
    localActionPanel.cleanPush(localUserManagePanel);
    localUserManagePanel.setActionPanel(localActionPanel);
    localActionPanel.setTitle("Manage Employee");
    MainWindow.instance.repaint();
    localUserManagePanel.setDefaultFocus();
  }
  
  public static void wearehouseManagePanel()
  {
    QuickTaskBox localQuickTaskBox = QuickTaskBox.getInstance();
    if (!localQuickTaskBox.getName().equals("Configuration"))
    {
      setupActionList();
      localQuickTaskBox.setTaskPane(_mTaskPane);
      localQuickTaskBox.setName("Configuration");
    }
    _mConfigGroup.expand();
    ActionPanel localActionPanel = MainWindow.getActionPanel();
    WarehouseManagePanel localWarehouseManagePanel = WarehouseManagePanel.getInstance();
    localActionPanel.cleanPush(localWarehouseManagePanel);
    localWarehouseManagePanel.setActionPanel(localActionPanel);
    localActionPanel.setTitle("Manage WareHouse");
    MainWindow.instance.repaint();
    localWarehouseManagePanel.setDefaultFocus();
  }
  
  public static void siteManagePanel()
  {
    QuickTaskBox localQuickTaskBox = QuickTaskBox.getInstance();
    if (!localQuickTaskBox.getName().equals("Configuration"))
    {
      setupActionList();
      localQuickTaskBox.setTaskPane(_mTaskPane);
      localQuickTaskBox.setName("Configuration");
    }
    _mConfigGroup.expand();
    ActionPanel localActionPanel = MainWindow.getActionPanel();
    TerminalManagePanel localTerminalManagePanel = TerminalManagePanel.getInstance();
    localActionPanel.cleanPush(localTerminalManagePanel);
    localTerminalManagePanel.setActionPanel(localActionPanel);
    localActionPanel.setTitle("Manage Terminal");
    MainWindow.instance.repaint();
    localTerminalManagePanel.setDefaultFocus();
  }
  
  public static void companyInfoPanel()
  {
    CompanyInfoRow localCompanyInfoRow = null;
    try
    {
      localCompanyInfoRow = CompanyInfoTableDef.getInstance().getCompany();
    }
    catch (DBException localDBException)
    {
      UICommon.showError("Internal error retrieving company information.", "Internal Error", MainWindow.instance);
      return;
    }
    QuickTaskBox localQuickTaskBox = QuickTaskBox.getInstance();
    if (!localQuickTaskBox.getName().equals("Configuration"))
    {
      setupActionList();
      localQuickTaskBox.setTaskPane(_mTaskPane);
      localQuickTaskBox.setName("Configuration");
    }
    ActionPanel localActionPanel = MainWindow.getActionPanel();
    _mConfigGroup.expand();
    CompanyInfoPanel localCompanyInfoPanel = CompanyInfoPanel.getInstance();
    localCompanyInfoPanel.setCompanyInfo(localCompanyInfoRow);
    localActionPanel.cleanPush(localCompanyInfoPanel);
    localCompanyInfoPanel.setActionPanel(localActionPanel);
    localActionPanel.setTitle("Company Information");
    MainWindow.instance.repaint();
    localCompanyInfoPanel.setDefaultFocus();
  }
  
  public static void syncProductPanel()
  {
    QuickTaskBox localQuickTaskBox = QuickTaskBox.getInstance();
    if (!localQuickTaskBox.getName().equals("Synchronization"))
    {
      setupActionList();
      localQuickTaskBox.setTaskPane(_mTaskPane);
      localQuickTaskBox.setName("Synchronization");
    }
    _mConfigGroup.collapse();
    ActionPanel localActionPanel = MainWindow.getActionPanel();
    ProductDetailsSyncPanel localProductDetailsSyncPanel = ProductDetailsSyncPanel.INSTANCE;
    localActionPanel.cleanPush(localProductDetailsSyncPanel);
    localActionPanel.setTitle(ProductDetailsSyncPanel.INSTANCE.getTitle());
    localProductDetailsSyncPanel.setActionPanel(localActionPanel);
    localProductDetailsSyncPanel.setDefaultFocus();
  }
  
  private static void setupActionList()
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
    if (_mTaskPane == null)
    {
      _mTaskPane = new QuickTaskPane();
      QuickTaskPane.TaskGroup localTaskGroup = _mTaskPane.createTaskGroup("Manage", "/dm/jb/images/settings.png");
      _mConfigGroup = localTaskGroup;
      if (UserInfoTableDef.getCurrentUser().hasAccess(64L)) {
        localTaskGroup.addAction("Company Info", "Edit Company Information", "/dm/jb/images/company.gif", new QuickTask()
        {
          public void actionPerformed() {}
        });
      }
      if (localCompanyInfoRow == null)
      {
        _mTaskPane.addTaskGroup(localTaskGroup);
        return;
      }
      if (UserInfoTableDef.getCurrentUser().hasAccess(128L)) {
        localTaskGroup.addAction("Warehouse", "Manage warehouse information", "/dm/jb/images/warehouse.gif", new QuickTask()
        {
          public void actionPerformed() {}
        });
      }
      if (UserInfoTableDef.getCurrentUser().hasAccess(256L))
      {
        localTaskGroup.addAction("Stores", "Manage store information", "/dm/jb/images/Store.gif", new QuickTask()
        {
          public void actionPerformed() {}
        });
        localTaskGroup.addAction("Terminal", "Manage terminal information", "/dm/jb/images/site.gif", new QuickTask()
        {
          public void actionPerformed() {}
        });
      }
      if ((UserInfoTableDef.getCurrentUser().getAccessParam() & 1L) != 0L) {
        localTaskGroup.addAction("User", "Manage user information", "/dm/jb/images/user.gif", new QuickTask()
        {
          public void actionPerformed() {}
        });
      }
      if (UserInfoTableDef.getCurrentUser().hasAccess(8L))
      {
        localTaskGroup.addAction("Configuration", "Configure Application", "/dm/jb/images/configuration.gif", new QuickTask()
        {
          public void actionPerformed() {}
        });
        _mTaskPane.addTaskGroup(localTaskGroup);
      }
      if (UserInfoTableDef.getCurrentUser().hasAccess(2048L))
      {
        localTaskGroup.addAction("Cleanup", "Cleanup the unused records", "/dm/jb/images/clean.png", new QuickTask()
        {
          public void actionPerformed() {}
        });
        _mTaskPane.addTaskGroup(localTaskGroup);
      }
      _mConfigGroup = localTaskGroup;
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.settings.Configuration
 * JD-Core Version:    0.7.0.1
 */