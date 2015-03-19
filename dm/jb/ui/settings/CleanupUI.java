package dm.jb.ui.settings;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.IDateEditor;
import com.toedter.calendar.JDateChooser;
import dm.jb.db.objects.CurrentStockTableDef;
import dm.jb.db.objects.ProductTableDef;
import dm.jb.db.objects.StockInfoTableDef;
import dm.jb.db.objects.StoreInfoRow;
import dm.jb.db.objects.StoreInfoTableDef;
import dm.jb.db.objects.StoreStockTableDef;
import dm.jb.db.objects.WearehouseInfoRow;
import dm.jb.db.objects.WearehouseInfoTableDef;
import dm.jb.ui.MainWindow;
import dm.jb.ui.common.HelpButton;
import dm.tools.db.BindObject;
import dm.tools.db.DBConnection;
import dm.tools.db.DBException;
import dm.tools.db.Db;
import dm.tools.ui.AbstractMainPanel;
import dm.tools.ui.ProgressWindow;
import dm.tools.ui.ProgressWindowAction;
import dm.tools.ui.UICommon;
import dm.tools.ui.components.JTextSeparator;
import dm.tools.utils.CommonConfig;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

public class CleanupUI
  extends AbstractMainPanel
{
  public static CleanupUI INSTANCE = new CleanupUI();
  private JCheckBox _mProductNoStockEver = null;
  private JCheckBox _mProdNoStockAtStore = null;
  private JComboBox _mProdNoStockAtStoreList = null;
  private JCheckBox _mProdNoStockAtHistory = null;
  private JCheckBox _mProdNoStockAtWh = null;
  private JComboBox _mProdNoStockAtWhList = null;
  private JCheckBox _mProductZeroStock = null;
  private JCheckBox _mProductZeroStockStore = null;
  private JCheckBox _mProductZeroStockWh = null;
  private JComboBox _mProductZeroStockStoreList = null;
  private JComboBox _mProductZeroStockWhList = null;
  private JDateChooser _mStockCleanFrom = null;
  private JDateChooser _mStockCleanTo = null;
  private JCheckBox _mZeroStockAtWh = null;
  private JComboBox _mZeroStockAtWhSelect = null;
  private JCheckBox _mStockNoProduct = null;
  private JCheckBox _mRemoveFromStoreStock = null;
  private JCheckBox _mRemoveFromWhStock = null;
  private JCheckBox _mRemoveFromStockInfo = null;
  private JComboBox _mStoreForStockNoProduct = null;
  private JComboBox _mWhForStockNoProduct = null;
  
  private CleanupUI()
  {
    initUI();
  }
  
  public void setDefaultFocus() {}
  
  public JPanel getPanel()
  {
    return this;
  }
  
  public void clearAllFields()
  {
    try
    {
      this._mStoreForStockNoProduct.removeAllItems();
      ArrayList localArrayList = StoreInfoTableDef.getInstance().getStoreList();
      this._mStoreForStockNoProduct.addItem("All");
      this._mProdNoStockAtStoreList.addItem("All");
      this._mProductZeroStockStoreList.addItem("All");
      Object localObject2;
      if ((localArrayList != null) && (localArrayList.size() > 0))
      {
        localObject1 = localArrayList.iterator();
        while (((Iterator)localObject1).hasNext())
        {
          localObject2 = (StoreInfoRow)((Iterator)localObject1).next();
          this._mStoreForStockNoProduct.addItem(localObject2);
          this._mProdNoStockAtStoreList.addItem(localObject2);
          this._mProductZeroStockStoreList.addItem(localObject2);
        }
      }
      Object localObject1 = WearehouseInfoTableDef.getInstance().getWarehouseList();
      this._mWhForStockNoProduct.addItem("All");
      this._mZeroStockAtWhSelect.addItem("All");
      this._mProdNoStockAtWhList.addItem("All");
      this._mProductZeroStockWhList.addItem("All");
      if ((localObject1 != null) && (((ArrayList)localObject1).size() > 0))
      {
        localObject2 = ((ArrayList)localObject1).iterator();
        while (((Iterator)localObject2).hasNext())
        {
          WearehouseInfoRow localWearehouseInfoRow = (WearehouseInfoRow)((Iterator)localObject2).next();
          this._mWhForStockNoProduct.addItem(localWearehouseInfoRow);
          this._mZeroStockAtWhSelect.addItem(localWearehouseInfoRow);
          this._mProdNoStockAtWhList.addItem(localWearehouseInfoRow);
          this._mProductZeroStockWhList.addItem(localWearehouseInfoRow);
        }
      }
    }
    catch (DBException localDBException)
    {
      localDBException.printStackTrace();
      UICommon.showInternalError("Internal error searching for store and warehouse information.");
      return;
    }
    this._mZeroStockAtWh.setSelected(true);
    this._mZeroStockAtWhSelect.setSelectedIndex(0);
    this._mStockNoProduct.setSelected(true);
    this._mRemoveFromStoreStock.setSelected(true);
    this._mRemoveFromWhStock.setSelected(true);
    this._mRemoveFromStockInfo.setSelected(true);
    this._mStoreForStockNoProduct.setSelectedIndex(0);
    this._mWhForStockNoProduct.setSelectedIndex(0);
    GregorianCalendar localGregorianCalendar = new GregorianCalendar();
    int i = localGregorianCalendar.get(2);
    if (i == 0) {
      i = 11;
    } else {
      i--;
    }
    localGregorianCalendar.set(2, i);
    localGregorianCalendar.set(5, 1);
    this._mStockCleanFrom.setDate(localGregorianCalendar.getTime());
    localGregorianCalendar.set(5, localGregorianCalendar.getActualMaximum(5));
    this._mStockCleanTo.setDate(localGregorianCalendar.getTime());
    this._mProductNoStockEver.setSelected(true);
    this._mProductZeroStock.setSelected(false);
    this._mProductZeroStockStore.setEnabled(false);
    this._mProductZeroStockStoreList.setEnabled(false);
    this._mProductZeroStockWh.setEnabled(false);
    this._mProductZeroStockWhList.setEnabled(false);
    this._mProdNoStockAtStore.setSelected(true);
    this._mProdNoStockAtHistory.setSelected(true);
    this._mProdNoStockAtWh.setSelected(true);
  }
  
  public void windowDisplayed() {}
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("10px,pref:grow,10px", "10px,25px,pref:grow,10px,25px,pref:grow,20px,30px,10px");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    int i = 2;
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(new JTextSeparator("Product Cleanup"), localCellConstraints);
    i++;
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getProductCleanPanel(), localCellConstraints);
    i += 2;
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(new JTextSeparator("Stock Cleanup"), localCellConstraints);
    i++;
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getStockCleanPanel(), localCellConstraints);
    i++;
    localCellConstraints.xywh(1, i, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(new JSeparator(), localCellConstraints);
    i++;
    localCellConstraints.xywh(1, i, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getButtonPanel(), localCellConstraints);
  }
  
  private JPanel getButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("10px,100px,pref:grow,100px,10px", "30px");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    Object localObject = new JButton("Close");
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        CleanupUI.this.closeWindow();
      }
    });
    localJPanel.add((Component)localObject, localCellConstraints);
    localObject = new HelpButton("ISP_CLEAN_HELP");
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    return localJPanel;
  }
  
  private JPanel getProductCleanPanel()
  {
    JPanel localJPanel1 = new JPanel();
    FormLayout localFormLayout = new FormLayout("10px,pref,10px,100px", "10px,pref,10px,pref,10px");
    localJPanel1.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(2, 2, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    JPanel localJPanel2 = new JPanel();
    localJPanel2.setLayout(new FormLayout("40px,100px,10px,150px,10px", "25px,10px,25px,10px,25px,10px,25px,10px"));
    localCellConstraints.xywh(2, 2, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    localJPanel1.add(localJPanel2, localCellConstraints);
    localCellConstraints.xywh(1, 1, 3, 1, CellConstraints.LEFT, CellConstraints.FILL);
    this._mProductNoStockEver = new JCheckBox("Products with no stock ever");
    localJPanel2.add(this._mProductNoStockEver, localCellConstraints);
    this._mProductNoStockEver.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (CleanupUI.this._mProductNoStockEver.isSelected())
        {
          CleanupUI.this._mProdNoStockAtStore.setEnabled(true);
          CleanupUI.this._mProdNoStockAtStoreList.setEnabled(true);
          CleanupUI.this._mProdNoStockAtWh.setEnabled(true);
          CleanupUI.this._mProdNoStockAtWhList.setEnabled(true);
          CleanupUI.this._mProdNoStockAtHistory.setEnabled(true);
        }
        else
        {
          CleanupUI.this._mProdNoStockAtStore.setEnabled(false);
          CleanupUI.this._mProdNoStockAtStoreList.setEnabled(false);
          CleanupUI.this._mProdNoStockAtWh.setEnabled(false);
          CleanupUI.this._mProdNoStockAtWhList.setEnabled(false);
          CleanupUI.this._mProdNoStockAtHistory.setEnabled(false);
        }
      }
    });
    this._mProdNoStockAtStore = new JCheckBox("At Store");
    localCellConstraints.xywh(2, 3, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    localJPanel2.add(this._mProdNoStockAtStore, localCellConstraints);
    this._mProdNoStockAtStore.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (CleanupUI.this._mProdNoStockAtStore.isSelected()) {
          CleanupUI.this._mProdNoStockAtStoreList.setEnabled(true);
        } else {
          CleanupUI.this._mProdNoStockAtStoreList.setEnabled(false);
        }
      }
    });
    this._mProdNoStockAtStoreList = new JComboBox();
    localCellConstraints.xywh(4, 3, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel2.add(this._mProdNoStockAtStoreList, localCellConstraints);
    this._mProdNoStockAtWh = new JCheckBox("At warehouse");
    localCellConstraints.xywh(2, 5, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    localJPanel2.add(this._mProdNoStockAtWh, localCellConstraints);
    this._mProdNoStockAtWh.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (CleanupUI.this._mProdNoStockAtWh.isSelected()) {
          CleanupUI.this._mProdNoStockAtWhList.setEnabled(true);
        } else {
          CleanupUI.this._mProdNoStockAtWhList.setEnabled(false);
        }
      }
    });
    this._mProdNoStockAtWhList = new JComboBox();
    localCellConstraints.xywh(4, 5, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel2.add(this._mProdNoStockAtWhList, localCellConstraints);
    this._mProdNoStockAtHistory = new JCheckBox("Stock History");
    localCellConstraints.xywh(2, 7, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    localJPanel2.add(this._mProdNoStockAtHistory, localCellConstraints);
    localJPanel2.setBorder(BorderFactory.createEtchedBorder());
    localJPanel2 = new JPanel();
    localJPanel2.setLayout(new FormLayout("40px,100px,10px,150px,10px", "25px,10px,25px,10px,25px,10px"));
    localCellConstraints.xywh(2, 4, 1, 1, CellConstraints.FILL, CellConstraints.BOTTOM);
    localJPanel1.add(localJPanel2, localCellConstraints);
    localJPanel2.setBorder(BorderFactory.createEtchedBorder());
    this._mProductZeroStock = new JCheckBox("Products with zero stock");
    localCellConstraints.xywh(1, 1, 3, 1, CellConstraints.LEFT, CellConstraints.FILL);
    localJPanel2.add(this._mProductZeroStock, localCellConstraints);
    this._mProductZeroStock.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (CleanupUI.this._mProductZeroStock.isSelected())
        {
          CleanupUI.this._mProductZeroStockStore.setEnabled(true);
          CleanupUI.this._mProductZeroStockStoreList.setEnabled(true);
          CleanupUI.this._mProductZeroStockWh.setEnabled(true);
          CleanupUI.this._mProductZeroStockWhList.setEnabled(true);
        }
        else
        {
          CleanupUI.this._mProductZeroStockStore.setEnabled(false);
          CleanupUI.this._mProductZeroStockStoreList.setEnabled(false);
          CleanupUI.this._mProductZeroStockWh.setEnabled(false);
          CleanupUI.this._mProductZeroStockWhList.setEnabled(false);
        }
      }
    });
    this._mProductZeroStockStore = new JCheckBox("At Store");
    localCellConstraints.xywh(2, 3, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    localJPanel2.add(this._mProductZeroStockStore, localCellConstraints);
    this._mProductZeroStockStore.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (CleanupUI.this._mProductZeroStockStore.isSelected()) {
          CleanupUI.this._mProductZeroStockStoreList.setEnabled(true);
        } else {
          CleanupUI.this._mProductZeroStockStoreList.setEnabled(false);
        }
      }
    });
    this._mProductZeroStockStoreList = new JComboBox();
    localCellConstraints.xywh(4, 3, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel2.add(this._mProductZeroStockStoreList, localCellConstraints);
    this._mProductZeroStockWh = new JCheckBox("At warehouse");
    localCellConstraints.xywh(2, 5, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    localJPanel2.add(this._mProductZeroStockWh, localCellConstraints);
    this._mProductZeroStockWh.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (CleanupUI.this._mProductZeroStockWh.isSelected()) {
          CleanupUI.this._mProductZeroStockWhList.setEnabled(true);
        } else {
          CleanupUI.this._mProductZeroStockWhList.setEnabled(false);
        }
      }
    });
    this._mProductZeroStockWhList = new JComboBox();
    localCellConstraints.xywh(4, 5, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel2.add(this._mProductZeroStockWhList, localCellConstraints);
    JButton localJButton = new JButton("Clean");
    localCellConstraints.xywh(4, 2, 1, 3, CellConstraints.LEFT, CellConstraints.BOTTOM);
    localJPanel1.add(localJButton, localCellConstraints);
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        CleanupUI.this.cleanupProductClicked();
      }
    });
    localJButton.setMinimumSize(new Dimension(100, 30));
    localJButton.setPreferredSize(new Dimension(100, 30));
    return localJPanel1;
  }
  
  private JPanel getStockCleanPanel()
  {
    JPanel localJPanel1 = new JPanel();
    FormLayout localFormLayout = new FormLayout("10px,160px,10px,130px,10px,40px,90px,50px:grow,10px,100px,10px", "10px,25px,10px,25px,10px,pref,10px");
    localJPanel1.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    JLabel localJLabel = new JLabel("Stock history for period : ");
    localCellConstraints.xywh(2, 2, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel1.add(localJLabel, localCellConstraints);
    this._mStockCleanFrom = new JDateChooser();
    localCellConstraints.xywh(4, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel1.add(this._mStockCleanFrom, localCellConstraints);
    this._mStockCleanFrom.setDateFormatString(CommonConfig.getInstance().dateFormat);
    this._mStockCleanTo = new JDateChooser();
    this._mStockCleanTo.setDateFormatString(CommonConfig.getInstance().dateFormat);
    localCellConstraints.xywh(6, 2, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel1.add(this._mStockCleanTo, localCellConstraints);
    this._mStockCleanTo.setDateFormatString(CommonConfig.getInstance().dateFormat);
    localCellConstraints.xywh(2, 4, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    this._mZeroStockAtWh = new JCheckBox("Zero stock at warehouse");
    localJPanel1.add(this._mZeroStockAtWh, localCellConstraints);
    this._mZeroStockAtWh.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (CleanupUI.this._mZeroStockAtWh.isSelected()) {
          CleanupUI.this._mZeroStockAtWhSelect.setEnabled(true);
        } else {
          CleanupUI.this._mZeroStockAtWhSelect.setEnabled(false);
        }
      }
    });
    this._mZeroStockAtWhSelect = new JComboBox();
    localCellConstraints.xywh(4, 4, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel1.add(this._mZeroStockAtWhSelect, localCellConstraints);
    JPanel localJPanel2 = new JPanel();
    localCellConstraints.xywh(2, 6, 7, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel1.add(localJPanel2, localCellConstraints);
    localJPanel2.setBorder(BorderFactory.createEtchedBorder());
    localJPanel2.setLayout(new FormLayout("40px,140px,10px,150px", "25px,5px,25px,5px,25px,5px,25px"));
    localCellConstraints.xywh(1, 1, 2, 1, CellConstraints.LEFT, CellConstraints.FILL);
    this._mStockNoProduct = new JCheckBox("Stocks with missing products");
    localJPanel2.add(this._mStockNoProduct, localCellConstraints);
    this._mStockNoProduct.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (CleanupUI.this._mStockNoProduct.isSelected())
        {
          CleanupUI.this._mRemoveFromWhStock.setEnabled(true);
          CleanupUI.this._mRemoveFromStoreStock.setEnabled(true);
          CleanupUI.this._mRemoveFromStockInfo.setEnabled(true);
          CleanupUI.this._mWhForStockNoProduct.setEnabled(true);
          CleanupUI.this._mStoreForStockNoProduct.setEnabled(true);
        }
        else
        {
          CleanupUI.this._mRemoveFromWhStock.setEnabled(false);
          CleanupUI.this._mRemoveFromStoreStock.setEnabled(false);
          CleanupUI.this._mRemoveFromStockInfo.setEnabled(false);
          CleanupUI.this._mWhForStockNoProduct.setEnabled(false);
          CleanupUI.this._mStoreForStockNoProduct.setEnabled(false);
        }
      }
    });
    localCellConstraints.xywh(2, 5, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    this._mRemoveFromWhStock = new JCheckBox("From warehouse");
    localJPanel2.add(this._mRemoveFromWhStock, localCellConstraints);
    this._mWhForStockNoProduct = new JComboBox();
    localCellConstraints.xywh(4, 5, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel2.add(this._mWhForStockNoProduct, localCellConstraints);
    this._mRemoveFromWhStock.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (CleanupUI.this._mRemoveFromWhStock.isSelected()) {
          CleanupUI.this._mWhForStockNoProduct.setEnabled(true);
        } else {
          CleanupUI.this._mWhForStockNoProduct.setEnabled(false);
        }
      }
    });
    localCellConstraints.xywh(2, 3, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    this._mRemoveFromStoreStock = new JCheckBox("From Store");
    localJPanel2.add(this._mRemoveFromStoreStock, localCellConstraints);
    this._mStoreForStockNoProduct = new JComboBox();
    localCellConstraints.xywh(4, 3, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel2.add(this._mStoreForStockNoProduct, localCellConstraints);
    this._mRemoveFromStoreStock.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (CleanupUI.this._mRemoveFromStoreStock.isSelected()) {
          CleanupUI.this._mStoreForStockNoProduct.setEnabled(true);
        } else {
          CleanupUI.this._mStoreForStockNoProduct.setEnabled(false);
        }
      }
    });
    localCellConstraints.xywh(2, 7, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    this._mRemoveFromStockInfo = new JCheckBox("From stock history");
    localJPanel2.add(this._mRemoveFromStockInfo, localCellConstraints);
    JButton localJButton = new JButton("Clean");
    localCellConstraints.xywh(10, 2, 1, localFormLayout.getRowCount() - 2, CellConstraints.LEFT, CellConstraints.BOTTOM);
    localJPanel1.add(localJButton, localCellConstraints);
    localJButton.setMinimumSize(new Dimension(100, 30));
    localJButton.setPreferredSize(new Dimension(100, 30));
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        CleanupUI.this.cleanupStockClicked();
      }
    });
    return localJPanel1;
  }
  
  private void cleanupProductClicked()
  {
    int i = 0;
    i = (i != 0) || ((this._mProductNoStockEver.isSelected()) && ((this._mProdNoStockAtStore.isSelected()) || (this._mProdNoStockAtWh.isSelected()) || (this._mProdNoStockAtHistory.isSelected()))) ? 1 : 0;
    i = (i != 0) || ((this._mProductZeroStock.isSelected()) && ((this._mProductZeroStockStore.isSelected()) || (this._mProductZeroStockWh.isSelected()))) ? 1 : 0;
    if (i == 0)
    {
      UICommon.showError("No option selected for cleanup", "Error", MainWindow.instance);
      return;
    }
    int j = UICommon.showQuestion("Are you sure you want to delete the product information as per the options selected ?", "Confirm", MainWindow.instance);
    if (j != 1) {
      return;
    }
    ProgressWindowAction local14 = new ProgressWindowAction()
    {
      public void startAction()
      {
        DBConnection localDBConnection = Db.getConnection();
        try
        {
          localDBConnection.openTrans();
          StringBuffer localStringBuffer;
          Object localObject;
          if (CleanupUI.this._mProductNoStockEver.isSelected())
          {
            localStringBuffer = new StringBuffer("");
            if (CleanupUI.this._mProdNoStockAtStore.isSelected())
            {
              localStringBuffer.append("(PROD_INDEX NOT IN (SELECT DISTINCT(PRODUCT_ID) FROM STORE_STOCK");
              if (CleanupUI.this._mProdNoStockAtStoreList.getSelectedIndex() != 0)
              {
                localObject = (StoreInfoRow)CleanupUI.this._mProdNoStockAtStoreList.getSelectedItem();
                localStringBuffer.append(" WHERE STORE_ID=" + ((StoreInfoRow)localObject).getStoreId() + ")");
              }
              else
              {
                localStringBuffer.append(")");
              }
              localStringBuffer.append(")");
            }
            if (CleanupUI.this._mProdNoStockAtWh.isSelected())
            {
              if (localStringBuffer.length() > 0) {
                localStringBuffer.append(" AND ");
              }
              localStringBuffer.append("(PROD_INDEX NOT IN (SELECT DISTINCT(PROD_ID) FROM CURRENT_STOCK ");
              if (CleanupUI.this._mProdNoStockAtWhList.getSelectedIndex() != 0)
              {
                localObject = (WearehouseInfoRow)CleanupUI.this._mProdNoStockAtWhList.getSelectedItem();
                localStringBuffer.append(" WHERE WEAR_HOUSE_INDEX=" + ((WearehouseInfoRow)localObject).getWearehouseId() + ")");
              }
              else
              {
                localStringBuffer.append(")");
              }
              localStringBuffer.append(")");
            }
            if (CleanupUI.this._mProdNoStockAtHistory.isSelected())
            {
              if (localStringBuffer.length() > 0) {
                localStringBuffer.append(" AND ");
              }
              localStringBuffer.append("(PROD_INDEX NOT IN (SELECT DISTINCT(PROD_ID) FROM STOCK_INFO))");
            }
            if ((CleanupUI.this._mProdNoStockAtStore.isSelected()) || (CleanupUI.this._mProdNoStockAtWh.isSelected()) || (CleanupUI.this._mProdNoStockAtHistory.isSelected())) {
              ProductTableDef.getInstance().deleteWithWhere(localStringBuffer.toString());
            }
          }
          if (CleanupUI.this._mProductZeroStock.isSelected())
          {
            localStringBuffer = new StringBuffer();
            if (CleanupUI.this._mProductZeroStockStore.isSelected())
            {
              localStringBuffer.append("(PROD_INDEX IN ( SELECT DISTINCT(PRODUCT_ID) FROM STORE_STOCK WHERE (STOCK= 0.0 OR STOCK < 0.0)");
              if (CleanupUI.this._mProductZeroStockStoreList.getSelectedIndex() != 0)
              {
                localObject = (StoreInfoRow)CleanupUI.this._mProductZeroStockStoreList.getSelectedItem();
                localStringBuffer.append(" AND STORE_ID=" + ((StoreInfoRow)localObject).getStoreId() + ")");
              }
              else
              {
                localStringBuffer.append(")");
              }
              localStringBuffer.append(")");
            }
            if (CleanupUI.this._mProductZeroStockWh.isSelected())
            {
              if (localStringBuffer.length() > 0) {
                localStringBuffer.append(" AND ");
              }
              localStringBuffer.append("(PROD_INDEX IN ( SELECT DISTINCT(PROD_ID) FROM CURRENT_STOCK WHERE (QUANTITY= 0.0 OR QUANTITY < 0.0)");
              if (CleanupUI.this._mProductZeroStockWhList.getSelectedIndex() != 0)
              {
                localObject = (WearehouseInfoRow)CleanupUI.this._mProductZeroStockWhList.getSelectedItem();
                localStringBuffer.append(" AND WEAR_HOUSE_INDEX=" + ((WearehouseInfoRow)localObject).getWearehouseId() + ")");
              }
              else
              {
                localStringBuffer.append(")");
              }
              localStringBuffer.append(")");
            }
            if ((CleanupUI.this._mProductZeroStockStore.isSelected()) || (CleanupUI.this._mProductZeroStockWh.isSelected())) {
              ProductTableDef.getInstance().deleteWithWhere(localStringBuffer.toString());
            }
          }
          localDBConnection.endTrans();
        }
        catch (DBException localDBException)
        {
          localDBConnection.rollbackNoExp();
          localDBException.printStackTrace();
          UICommon.showDelayedErrorMessage("Internal error cleaning up product details.\n\nTry again later. If the problem persists contact administrator.", "Internal Error", MainWindow.instance);
          return;
        }
        UICommon.showDelayedMessage("Stock information were cleaned up successfully.", "Success", MainWindow.instance);
      }
      
      public String getActionName()
      {
        return "Cleaning Prouct Information";
      }
    };
    ProgressWindow localProgressWindow = new ProgressWindow(MainWindow.instance, local14);
    localProgressWindow.startProgress();
  }
  
  private void cleanupStockClicked()
  {
    java.util.Date localDate1 = this._mStockCleanFrom.getDate();
    java.util.Date localDate2 = this._mStockCleanFrom.getDate();
    if ((localDate1 == null) && (localDate2 != null))
    {
      UICommon.showError("For cleaning stock arrival records, specify a valid date range.", "Error", MainWindow.instance);
      this._mStockCleanFrom.getDateEditor().getUiComponent().requestFocusInWindow();
      return;
    }
    if ((localDate1 != null) && (localDate2 == null))
    {
      UICommon.showError("For cleaning stock arrival records, specify a valid date range.", "Error", MainWindow.instance);
      this._mStockCleanTo.getDateEditor().getUiComponent().requestFocusInWindow();
      return;
    }
    if ((localDate1 != null) && (localDate1.getTime() > localDate2.getTime()))
    {
      UICommon.showError("The from-date is higher than the to-date.", "Error", MainWindow.instance);
      this._mStockCleanTo.getDateEditor().getUiComponent().requestFocusInWindow();
      return;
    }
    int i = 0;
    i = (i != 0) || (localDate1 != null) || (this._mZeroStockAtWh.isSelected()) ? 1 : 0;
    i = (i != 0) || ((this._mStockNoProduct.isSelected()) && ((this._mRemoveFromStoreStock.isSelected()) || (this._mRemoveFromWhStock.isSelected()) || (this._mRemoveFromStockInfo.isSelected()))) ? 1 : 0;
    if (i == 0)
    {
      UICommon.showError("No option selected for cleanup", "Error", MainWindow.instance);
      return;
    }
    int j = UICommon.showQuestion("Are you sure you want to delete the stock information as per the options selected ?", "Confirm", MainWindow.instance);
    if (j != 1) {
      return;
    }
    ProgressWindowAction local15 = new ProgressWindowAction()
    {
      public void startAction()
      {
        java.util.Date localDate1 = CleanupUI.this._mStockCleanFrom.getDate();
        java.util.Date localDate2 = CleanupUI.this._mStockCleanTo.getDate();
        DBConnection localDBConnection = Db.getConnection();
        try
        {
          localDBConnection.openTrans();
          Object localObject1;
          Object localObject2;
          if (localDate1 != null)
          {
            localObject1 = new java.sql.Date(localDate1.getTime());
            localObject2 = new java.sql.Date(localDate2.getTime());
            BindObject[] arrayOfBindObject = { new BindObject(1, 2, localObject1), new BindObject(2, 2, localObject1), new BindObject(3, 2, localObject2), new BindObject(4, 2, localObject2) };
            StockInfoTableDef.getInstance().deleteWithWhere("((STOCK_DATE >?) OR (STOCK_DATE=?)) AND ((STOCK_DATE < ?) OR (STOCK_DATE=?))", arrayOfBindObject);
          }
          if (CleanupUI.this._mZeroStockAtWh.isSelected()) {
            if (CleanupUI.this._mZeroStockAtWhSelect.getSelectedIndex() == 0)
            {
              CurrentStockTableDef.getInstance().deleteWithWhere("QUANTITY=0.0 OR QUANTITY<0.0");
            }
            else
            {
              localObject1 = (WearehouseInfoRow)CleanupUI.this._mZeroStockAtWhSelect.getSelectedItem();
              CurrentStockTableDef.getInstance().deleteWithWhere("(QUANTITY=0.0 OR QUANTITY<0.0) AND WEARE_HOUSE_INDEX= " + ((WearehouseInfoRow)localObject1).getWearehouseId());
            }
          }
          if (CleanupUI.this._mStockNoProduct.isSelected())
          {
            if (CleanupUI.this._mRemoveFromStoreStock.isSelected())
            {
              localObject1 = new StringBuffer(" (PRODUCT_ID NOT IN (SELECT PROD_INDEX FROM PRODUCT) )");
              if (CleanupUI.this._mStoreForStockNoProduct.getSelectedIndex() != 0)
              {
                localObject2 = (StoreInfoRow)CleanupUI.this._mStoreForStockNoProduct.getSelectedItem();
                ((StringBuffer)localObject1).append(" AND (STORE_ID=" + ((StoreInfoRow)localObject2).getStoreId() + ")");
              }
              StoreStockTableDef.getInstance().deleteWithWhere(((StringBuffer)localObject1).toString());
            }
            if (CleanupUI.this._mRemoveFromWhStock.isSelected())
            {
              localObject1 = new StringBuffer(" (PROD_ID NOT IN (SELECT PROD_INDEX FROM PRODUCT) )");
              if (CleanupUI.this._mWhForStockNoProduct.getSelectedIndex() != 0)
              {
                localObject2 = (WearehouseInfoRow)CleanupUI.this._mWhForStockNoProduct.getSelectedItem();
                ((StringBuffer)localObject1).append(" AND (WEAR_HOUSE_INDEX=" + ((WearehouseInfoRow)localObject2).getWearehouseId() + ")");
              }
              CurrentStockTableDef.getInstance().deleteWithWhere(((StringBuffer)localObject1).toString());
            }
          }
          localDBConnection.endTrans();
        }
        catch (DBException localDBException)
        {
          localDBConnection.rollbackNoExp();
          localDBException.printStackTrace();
          UICommon.showDelayedErrorMessage("Internal error cleaning up stock details.\n\nTry again later. If the problem persists contact administrator.", "Internal Error", MainWindow.instance);
          return;
        }
        UICommon.showDelayedMessage("Stock information were cleaned up successfully.", "Success", MainWindow.instance);
      }
      
      public String getActionName()
      {
        return "Cleaning stock information";
      }
    };
    ProgressWindow localProgressWindow = new ProgressWindow(MainWindow.instance, local15);
    localProgressWindow.startProgress();
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.settings.CleanupUI
 * JD-Core Version:    0.7.0.1
 */