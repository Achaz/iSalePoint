package dm.jb.ui.inv;

import dm.jb.db.objects.UserInfoRow;
import dm.jb.db.objects.UserInfoTableDef;
import dm.jb.ui.MainWindow;
import dm.tools.ui.AbstractMainPanel;
import dm.tools.ui.ActionPanel;
import dm.tools.ui.QuickTask;
import dm.tools.ui.QuickTaskBox;
import dm.tools.ui.QuickTaskPane;
import dm.tools.ui.QuickTaskPane.TaskGroup;
import javax.swing.SwingUtilities;

public class ProductSetup
{
  private static QuickTaskPane _mTaskPane = null;
  private static QuickTaskPane.TaskGroup _mTGProduct = null;
  private static QuickTaskPane.TaskGroup _mTGStock = null;
  
  public static void newStock()
  {
    QuickTaskBox localQuickTaskBox = QuickTaskBox.getInstance();
    if (!localQuickTaskBox.getName().equals("Inventory"))
    {
      setupActionList();
      localQuickTaskBox.setTaskPane(_mTaskPane);
      localQuickTaskBox.setName("Inventory");
    }
    ActionPanel localActionPanel = MainWindow.getActionPanel();
    NewStockPanel localNewStockPanel = NewStockPanel.getNewStockPanel();
    localActionPanel.cleanPush(localNewStockPanel);
    localNewStockPanel.setActionPanel(localActionPanel);
    localActionPanel.setTitle("Goods Inward");
    MainWindow.instance.repaint();
    localNewStockPanel.setDefaultFocus();
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        ProductSetup.access$000();
        ProductSetup._mTGStock.expand();
      }
    });
  }
  
  public static void newStockFromPO()
  {
    QuickTaskBox localQuickTaskBox = QuickTaskBox.getInstance();
    if (!localQuickTaskBox.getName().equals("Inventory"))
    {
      setupActionList();
      localQuickTaskBox.setTaskPane(_mTaskPane);
      localQuickTaskBox.setName("Inventory");
    }
    ActionPanel localActionPanel = MainWindow.getActionPanel();
    PONewStockUI localPONewStockUI = PONewStockUI.INSTANCE;
    localActionPanel.cleanPush(localPONewStockUI);
    localPONewStockUI.setActionPanel(localActionPanel);
    localActionPanel.setTitle("New Stock with Purchase Order");
    MainWindow.instance.repaint();
    localPONewStockUI.setDefaultFocus();
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        ProductSetup.access$000();
        ProductSetup._mTGStock.expand();
      }
    });
  }
  
  public static void editStock()
  {
    QuickTaskBox localQuickTaskBox = QuickTaskBox.getInstance();
    if (!localQuickTaskBox.getName().equals("Inventory"))
    {
      setupActionList();
      localQuickTaskBox.setTaskPane(_mTaskPane);
      localQuickTaskBox.setName("Inventory");
    }
    ActionPanel localActionPanel = MainWindow.getActionPanel();
    EditStockPanel localEditStockPanel = EditStockPanel.INSTANCE;
    localActionPanel.cleanPush(localEditStockPanel);
    localEditStockPanel.setActionPanel(localActionPanel);
    localActionPanel.setTitle("Edit stock details");
    MainWindow.instance.repaint();
    localEditStockPanel.setDefaultFocus();
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        ProductSetup.access$000();
        ProductSetup._mTGStock.expand();
      }
    });
  }
  
  public static void purchaseOrder()
  {
    QuickTaskBox localQuickTaskBox = QuickTaskBox.getInstance();
    if (!localQuickTaskBox.getName().equals("Inventory"))
    {
      setupActionList();
      localQuickTaskBox.setTaskPane(_mTaskPane);
      localQuickTaskBox.setName("Inventory");
    }
    ActionPanel localActionPanel = MainWindow.getActionPanel();
    PurchaseOrderUIPanel localPurchaseOrderUIPanel = PurchaseOrderUIPanel.INSTANCE;
    localActionPanel.cleanPush(localPurchaseOrderUIPanel);
    localPurchaseOrderUIPanel.setActionPanel(localActionPanel);
    localActionPanel.setTitle("Purchase Order Create/Update");
    MainWindow.instance.repaint();
    localPurchaseOrderUIPanel.setDefaultFocus();
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        ProductSetup.access$000();
        ProductSetup._mTGStock.expand();
      }
    });
  }
  
  public static void reStock()
  {
    QuickTaskBox localQuickTaskBox = QuickTaskBox.getInstance();
    if (!localQuickTaskBox.getName().equals("Re-stock"))
    {
      setupActionList();
      localQuickTaskBox.setTaskPane(_mTaskPane);
      localQuickTaskBox.setName("Re-stock");
    }
    ActionPanel localActionPanel = MainWindow.getActionPanel();
    RestockPanel localRestockPanel = RestockPanel.getRestockPanel();
    localActionPanel.cleanPush(localRestockPanel);
    localRestockPanel.setActionPanel(localActionPanel);
    localActionPanel.setTitle("Store to Warehouse");
    MainWindow.instance.repaint();
    localRestockPanel.setDefaultFocus();
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        ProductSetup.access$000();
        ProductSetup._mTGStock.expand();
      }
    });
  }
  
  public static void allotStock()
  {
    QuickTaskBox localQuickTaskBox = QuickTaskBox.getInstance();
    if (!localQuickTaskBox.getName().equals("Allot Stock"))
    {
      setupActionList();
      localQuickTaskBox.setTaskPane(_mTaskPane);
      localQuickTaskBox.setName("Allot Stock");
    }
    ActionPanel localActionPanel = MainWindow.getActionPanel();
    AllotStockPanel localAllotStockPanel = AllotStockPanel.getAssignStockPanel();
    localActionPanel.cleanPush(localAllotStockPanel);
    localAllotStockPanel.setActionPanel(localActionPanel);
    localActionPanel.setTitle("Warehouse to Store");
    MainWindow.instance.repaint();
    localAllotStockPanel.setDefaultFocus();
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        ProductSetup.access$000();
        ProductSetup._mTGStock.expand();
      }
    });
  }
  
  public static void stockReturn()
  {
    QuickTaskBox localQuickTaskBox = QuickTaskBox.getInstance();
    if (!localQuickTaskBox.getName().equals("Goods return"))
    {
      setupActionList();
      localQuickTaskBox.setTaskPane(_mTaskPane);
      localQuickTaskBox.setName("Goods return");
    }
    ActionPanel localActionPanel = MainWindow.getActionPanel();
    StockReturnPanel localStockReturnPanel = StockReturnPanel.INSTANCE;
    StockReturnPanel.INSTANCE.prepareForNew();
    localStockReturnPanel.clearAllFields();
    localActionPanel.cleanPush(localStockReturnPanel);
    localStockReturnPanel.setActionPanel(localActionPanel);
    localActionPanel.setTitle("Goods return");
    MainWindow.instance.repaint();
    localStockReturnPanel.setDefaultFocus();
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        ProductSetup.access$000();
        ProductSetup._mTGStock.expand();
      }
    });
  }
  
  public static void viewStockReturn()
  {
    QuickTaskBox localQuickTaskBox = QuickTaskBox.getInstance();
    if (!localQuickTaskBox.getName().equals("View Goods return"))
    {
      setupActionList();
      localQuickTaskBox.setTaskPane(_mTaskPane);
      localQuickTaskBox.setName("View Goods return");
    }
    ActionPanel localActionPanel = MainWindow.getActionPanel();
    StockReturnViewPanel localStockReturnViewPanel = StockReturnViewPanel.INSTANCE;
    localStockReturnViewPanel.clearAllFields();
    localActionPanel.cleanPush(localStockReturnViewPanel);
    localStockReturnViewPanel.setActionPanel(localActionPanel);
    localActionPanel.setTitle("View Goods return");
    MainWindow.instance.repaint();
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        ProductSetup.access$000();
        ProductSetup._mTGStock.expand();
        StockReturnViewPanel.INSTANCE.setDefaultFocus();
      }
    });
  }
  
  public static void manageProduct()
  {
    QuickTaskBox localQuickTaskBox = QuickTaskBox.getInstance();
    if (!localQuickTaskBox.getName().equals("Inventory"))
    {
      setupActionList();
      localQuickTaskBox.setTaskPane(_mTaskPane);
      localQuickTaskBox.setName("Inventory");
    }
    ActionPanel localActionPanel = MainWindow.getActionPanel();
    ProductManagePanel localProductManagePanel = ProductManagePanel.getNewProductPanel();
    localActionPanel.cleanPush(localProductManagePanel);
    localProductManagePanel.setActionPanel(localActionPanel);
    localActionPanel.setTitle("Manage Product");
    MainWindow.instance.repaint();
    localProductManagePanel.setDefaultFocus();
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        ProductSetup.access$000();
        ProductSetup._mTGProduct.expand();
      }
    });
  }
  
  public static void manageVendor()
  {
    QuickTaskBox localQuickTaskBox = QuickTaskBox.getInstance();
    if (!localQuickTaskBox.getName().equals("Inventory"))
    {
      setupActionList();
      localQuickTaskBox.setTaskPane(_mTaskPane);
      localQuickTaskBox.setName("Inventory");
    }
    ActionPanel localActionPanel = MainWindow.getActionPanel();
    VendorManagePanel localVendorManagePanel = VendorManagePanel._mInstance;
    localActionPanel.cleanPush(localVendorManagePanel);
    localVendorManagePanel.setActionPanel(localActionPanel);
    localActionPanel.setTitle("Manage Vendor");
    MainWindow.instance.repaint();
    localVendorManagePanel.setDefaultFocus();
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        ProductSetup.access$000();
        ProductSetup._mTGProduct.expand();
      }
    });
  }
  
  public static void printBarCodes()
  {
    QuickTaskBox localQuickTaskBox = QuickTaskBox.getInstance();
    if (!localQuickTaskBox.getName().equals("Inventory"))
    {
      setupActionList();
      localQuickTaskBox.setTaskPane(_mTaskPane);
      localQuickTaskBox.setName("Inventory");
    }
    ActionPanel localActionPanel = MainWindow.getActionPanel();
    BarcodePrintingUI localBarcodePrintingUI = BarcodePrintingUI._mInstance;
    localActionPanel.cleanPush(localBarcodePrintingUI);
    localBarcodePrintingUI.setActionPanel(localActionPanel);
    localActionPanel.setTitle("Print Bar Codes");
    MainWindow.instance.repaint();
    localBarcodePrintingUI.setDefaultFocus();
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        ProductSetup.access$000();
        ProductSetup._mTGProduct.expand();
      }
    });
  }
  
  public static void manageDepartment()
  {
    QuickTaskBox localQuickTaskBox = QuickTaskBox.getInstance();
    if (!localQuickTaskBox.getName().equals("Inventory"))
    {
      setupActionList();
      localQuickTaskBox.setTaskPane(_mTaskPane);
      localQuickTaskBox.setName("Inventory");
    }
    ActionPanel localActionPanel = MainWindow.getActionPanel();
    DeptManagePanel localDeptManagePanel = DeptManagePanel.getInstance();
    localDeptManagePanel.setActionPanel(localActionPanel);
    localActionPanel.cleanPush(localDeptManagePanel.getPanel());
    localActionPanel.setTitle("Manage Department");
    MainWindow.instance.repaint();
    localDeptManagePanel.setDefaultFocus();
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        ProductSetup.access$000();
        ProductSetup._mTGProduct.expand();
      }
    });
  }
  
  public static void manageCategory()
  {
    QuickTaskBox localQuickTaskBox = QuickTaskBox.getInstance();
    if (!localQuickTaskBox.getName().equals("Inventory"))
    {
      setupActionList();
      localQuickTaskBox.setTaskPane(_mTaskPane);
      localQuickTaskBox.setName("Inventory");
    }
    ActionPanel localActionPanel = MainWindow.getActionPanel();
    CategoryManagePanel localCategoryManagePanel = CategoryManagePanel.getInstance();
    localCategoryManagePanel.setActionPanel(localActionPanel);
    localActionPanel.cleanPush(localCategoryManagePanel.getPanel());
    localActionPanel.setTitle("Manage Category");
    MainWindow.instance.repaint();
    localCategoryManagePanel.setDefaultFocus();
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        ProductSetup.access$000();
        ProductSetup._mTGProduct.expand();
      }
    });
  }
  
  private static void setupActionList()
  {
    UserInfoRow localUserInfoRow = UserInfoTableDef.getCurrentUser();
    if (_mTaskPane == null)
    {
      _mTaskPane = new QuickTaskPane();
      QuickTaskPane.TaskGroup localTaskGroup = null;
      if (localUserInfoRow.hasAccess(2L))
      {
        localTaskGroup = _mTaskPane.createTaskGroup("Product Management", "/dm/jb/images/product.gif");
        localTaskGroup.addAction("Manage Products", "Manage the products", "/dm/jb/images/product.gif", new QuickTask()
        {
          public void actionPerformed() {}
        });
        localTaskGroup.addAction("Manage Categories", "Manage the categories", "/dm/jb/images/category.gif", new QuickTask()
        {
          public void actionPerformed() {}
        });
        localTaskGroup.addAction("Manage Departments", "Manage the departments", "/dm/jb/images/department.gif", new QuickTask()
        {
          public void actionPerformed() {}
        });
        localTaskGroup.addAction("Manage Vendor", "Manage Vendors", "/dm/jb/images/vendor.gif", new QuickTask()
        {
          public void actionPerformed() {}
        });
        localTaskGroup.addAction("Print Barcodes", "Print barcodes for products.", "/dm/jb/images/barcode.gif", new QuickTask()
        {
          public void actionPerformed() {}
        });
        _mTaskPane.addTaskGroup(localTaskGroup);
        _mTGProduct = localTaskGroup;
      }
      if (localUserInfoRow.hasAccess(4L))
      {
        localTaskGroup = _mTaskPane.createTaskGroup("Stock Management", "/dm/jb/images/stock.gif");
        localTaskGroup.addAction("Purchase Order Create/Update", "Create/Update/Delete the Purchase Order", "/dm/jb/images/purchase_order.gif", new QuickTask()
        {
          public void actionPerformed() {}
        });
        localTaskGroup.addAction("New Stock with Purchase Order", "Add stocks with a purchase order associated with it.", "/dm/jb/images/stock_add.gif", new QuickTask()
        {
          public void actionPerformed() {}
        });
        localTaskGroup.addAction("Goods Inward", "Add and Edit goods", "/dm/jb/images/stock_add.gif", new QuickTask()
        {
          public void actionPerformed() {}
        });
        localTaskGroup.addAction("Warehouse to Store", "Allot stock to showroom/shop", "/dm/jb/images/stock_allot.gif", new QuickTask()
        {
          public void actionPerformed() {}
        });
        localTaskGroup.addAction("Edit stock", "Fine edit the stock details of store and warehouse", "/dm/jb/images/stock_allot.gif", new QuickTask()
        {
          public void actionPerformed() {}
        });
        localTaskGroup.addAction("Goods return", "Return stock to the vendor", "/dm/jb/images/stock_return.gif", new QuickTask()
        {
          public void actionPerformed() {}
        });
        _mTaskPane.addTaskGroup(localTaskGroup);
        localTaskGroup.addAction("View Goods return", "View and Delete good return transactions", "/dm/jb/images/stock_return.gif", new QuickTask()
        {
          public void actionPerformed() {}
        });
        _mTaskPane.addTaskGroup(localTaskGroup);
        _mTGStock = localTaskGroup;
      }
    }
  }
  
  private static void collapseAll()
  {
    if (_mTGProduct != null) {
      _mTGProduct.collapse();
    }
    if (_mTGStock != null) {
      _mTGStock.collapse();
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.inv.ProductSetup
 * JD-Core Version:    0.7.0.1
 */