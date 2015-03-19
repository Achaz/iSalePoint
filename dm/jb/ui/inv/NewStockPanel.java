package dm.jb.ui.inv;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.IDateEditor;
import com.toedter.calendar.JDateChooser;
import dm.jb.db.objects.CurrentStockRow;
import dm.jb.db.objects.CurrentStockTableDef;
import dm.jb.db.objects.ProductRow;
import dm.jb.db.objects.ProductTableDef;
import dm.jb.db.objects.StockInfoRow;
import dm.jb.db.objects.StockInfoTableDef;
import dm.jb.db.objects.StoreInfoRow;
import dm.jb.db.objects.StoreInfoTableDef;
import dm.jb.db.objects.StoreStockRow;
import dm.jb.db.objects.StoreStockTableDef;
import dm.jb.db.objects.VendorRow;
import dm.jb.db.objects.VendorTableDef;
import dm.jb.db.objects.WearehouseInfoRow;
import dm.jb.db.objects.WearehouseInfoTableDef;
import dm.jb.ui.MainWindow;
import dm.jb.ui.common.HelpButton;
import dm.jb.ui.common.ShortKeyCommon;
import dm.jb.ui.settings.CountryInfo;
import dm.tools.db.DBConnection;
import dm.tools.db.DBException;
import dm.tools.db.DBRow;
import dm.tools.db.Db;
import dm.tools.ui.AbstractMainPanel;
import dm.tools.ui.CustomDateChooser;
import dm.tools.ui.UICommon;
import dm.tools.ui.components.JBSearchButton;
import dm.tools.utils.CommonConfig;
import dm.tools.utils.CommonConfigChangeListener;
import dm.tools.utils.Validation;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ButtonGroup;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import org.jdesktop.swingx.JXButton;

public class NewStockPanel
  extends AbstractMainPanel
{
  private static NewStockPanel _mNewStockPanel = null;
  private JDateChooser _mStockDate = null;
  private JTextField _mQuantity = null;
  private JLabel _mQuantityUnit = null;
  private JTextField _mPurchasePrice = null;
  private JComboBox _mVendor = null;
  private JButton _mApplyBtn = null;
  private ProductRow _mSelectedProduct = null;
  private JDateChooser _mExpiry = null;
  private JCheckBox _mExpiryNA = null;
  private JLabel _mPriceLabel = null;
  private JLabel _mStockToLabel = null;
  private JRadioButton _mStockToWearhouse = null;
  private JRadioButton _mStockToStore = null;
  private JComboBox _mWeareHouseCombo = null;
  private JComboBox _mStoreCombo = null;
  private JTextField _mProductNum = null;
  private JTextField _mProductName = null;
  
  public static NewStockPanel getNewStockPanel()
  {
    if (_mNewStockPanel == null)
    {
      _mNewStockPanel = new NewStockPanel();
      _mNewStockPanel.clearAllFields();
    }
    else
    {
      _mNewStockPanel.clearAllFields();
    }
    return _mNewStockPanel;
  }
  
  public NewStockPanel()
  {
    initUI();
    CommonConfig.getInstance().addChangeListener(new CommonConfigChangeListener()
    {
      public void setConfigValues()
      {
        NewStockPanel.this.configValuesChange();
      }
    });
  }
  
  public void clearAllFields()
  {
    this._mStockDate.setDate(new java.util.Date());
    this._mQuantity.setText("");
    this._mProductName.setText("");
    this._mProductNum.setText("");
    this._mApplyBtn.setEnabled(false);
    this._mPurchasePrice.setText("0.00");
    this._mVendor.setSelectedItem(null);
  }
  
  public void windowDisplayed()
  {
    getRootPane().setDefaultButton(this._mApplyBtn);
    this._mWeareHouseCombo.setEnabled(false);
    this._mStoreCombo.setEnabled(true);
    this._mStockToStore.setSelected(true);
    fillStoreAndWearhouseList();
    fillVendorDetails();
    this._mStoreCombo.setSelectedItem(StoreInfoTableDef.getCurrentStore());
  }
  
  public void initUI()
  {
    FormLayout localFormLayout1 = new FormLayout("10px, 90px, 10px, 110px, 80px, 3px, 40px, 80px, 10px, pref:grow, 10px", "10px,25px,10px,25px,10px, 25px, 10px, 0px, 0px, 25px, 10px, pref, 10px, 25px, 10px, 25px, 20px, 30px, 20px");
    setLayout(localFormLayout1);
    CellConstraints localCellConstraints = new CellConstraints();
    int i = 2;
    JLabel localJLabel = new JLabel("Product Code : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mProductNum = new JTextField();
    this._mProductNum.setToolTipText("Product Code");
    this._mProductNum.setBackground(UICommon.MANDATORY_COLOR);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 80, this._mProductNum);
    localCellConstraints.xywh(4, i, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mProductNum, localCellConstraints);
    Object localObject = new JBSearchButton(false);
    ((JXButton)localObject).setMnemonic(74);
    localCellConstraints.xywh(7, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add((Component)localObject, localCellConstraints);
    ((JXButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        NewStockPanel.this.searchByProductNumClicked();
      }
    });
    ((JXButton)localObject).setToolTipText("Search products by product code");
    this._mProductNum.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 114) {
          NewStockPanel.this.searchByProductNumClicked();
        }
      }
    });
    i += 2;
    localJLabel = new JLabel("Product Name : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mProductName = new JTextField();
    this._mProductName.setToolTipText("Product name");
    this._mProductName.setBackground(UICommon.MANDATORY_COLOR);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 78, this._mProductName);
    localCellConstraints.xywh(4, i, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mProductName, localCellConstraints);
    this._mProductName.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 114) {
          NewStockPanel.this.searchByProductNameClicked();
        }
      }
    });
    localObject = new JBSearchButton(false);
    ((JXButton)localObject).setToolTipText("Search products by product name");
    ((JXButton)localObject).setMnemonic(90);
    localCellConstraints.xywh(7, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add((Component)localObject, localCellConstraints);
    ((JXButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        NewStockPanel.this.searchByProductNameClicked();
      }
    });
    i += 2;
    localJLabel = new JLabel("Stock Date :");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mStockDate = new CustomDateChooser();
    this._mStockDate.getDateEditor().getUiComponent().setToolTipText("Stock recieve date");
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mStockDate, localCellConstraints);
    this._mStockDate.setBackground(UICommon.MANDATORY_COLOR);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 68, this._mStockDate);
    i += 2;
    this._mExpiry = new CustomDateChooser();
    ShortKeyCommon.shortKeyForLabels(localJLabel, 88, this._mExpiry);
    this._mExpiryNA = new JCheckBox("Not applicable");
    this._mExpiryNA.setBackground(getBackground());
    this._mExpiryNA.setMnemonic(66);
    this._mExpiryNA.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        int i;
        int j;
        if (NewStockPanel.this._mExpiryNA.isSelected())
        {
          i = NewStockPanel.this._mExpiry.getComponentCount();
          for (j = 0; j < i; j++) {
            NewStockPanel.this._mExpiry.getComponent(j).setEnabled(false);
          }
        }
        else
        {
          i = NewStockPanel.this._mExpiry.getComponentCount();
          for (j = 0; j < i; j++) {
            NewStockPanel.this._mExpiry.getComponent(j).setEnabled(true);
          }
        }
      }
    });
    i += 2;
    localJLabel = new JLabel("Quantity :");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mQuantity = new JTextField();
    this._mQuantity.setToolTipText("Quantity recieved");
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mQuantity, localCellConstraints);
    this._mQuantity.setBackground(UICommon.MANDATORY_COLOR);
    this._mQuantity.setHorizontalAlignment(4);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 81, this._mQuantity);
    this._mQuantityUnit = new JLabel();
    localCellConstraints.xywh(5, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mQuantityUnit, localCellConstraints);
    this._mQuantityUnit.setText("");
    i += 2;
    this._mStockToLabel = new JLabel("Stock to :");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(this._mStockToLabel, localCellConstraints);
    localCellConstraints.xywh(4, i, 6, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getStockToPanel(), localCellConstraints);
    i += 2;
    localJLabel = new JLabel("Purchase price :");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mPurchasePrice = new JTextField();
    this._mPurchasePrice.setToolTipText("Total purchase price");
    this._mPurchasePrice.setHorizontalAlignment(4);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 85, this._mPurchasePrice);
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mPurchasePrice, localCellConstraints);
    this._mPurchasePrice.setText("0.00");
    this._mPurchasePrice.setBackground(UICommon.MANDATORY_COLOR);
    localJLabel = new JLabel(CommonConfig.getInstance().country.currency);
    this._mPriceLabel = localJLabel;
    localCellConstraints.xywh(5, i, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mPriceLabel.setText("  " + CommonConfig.getInstance().country.currency);
    i += 2;
    localJLabel = new JLabel("Vendor :");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.CENTER);
    add(localJLabel, localCellConstraints);
    this._mVendor = new JComboBox();
    localCellConstraints.xywh(4, i, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mVendor, localCellConstraints);
    this._mVendor.setToolTipText("Select vendor");
    ShortKeyCommon.shortKeyForLabels(localJLabel, 86, this._mVendor);
    i++;
    localCellConstraints.xywh(1, i, localFormLayout1.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(new JSeparator(), localCellConstraints);
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout2 = new FormLayout("10px, 100px, 20px,100px,60px:grow, 100px, 60px:grow, 100px, 10px", "30px");
    i++;
    localJPanel.setLayout(localFormLayout2);
    localCellConstraints.xywh(1, i, localFormLayout1.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(localJPanel, localCellConstraints);
    localObject = new JXButton(" Update ");
    ((JXButton)localObject).setMnemonic(85);
    this._mApplyBtn = ((JButton)localObject);
    this._mApplyBtn.setToolTipText("Update the stocks");
    this._mApplyBtn.setEnabled(false);
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JXButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        NewStockPanel.this.applyClicked();
      }
    });
    localObject = new JXButton(" Reset ");
    ((JXButton)localObject).setToolTipText("Reset the fields");
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JXButton)localObject).setMnemonic(82);
    ((JXButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        NewStockPanel.this.clearAllFields();
      }
    });
    localObject = new JXButton(" Close ");
    localCellConstraints.xywh(6, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JXButton)localObject).setToolTipText("Close the window");
    ((JXButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        NewStockPanel.this.closeWindow();
      }
    });
    localObject = new HelpButton("ISP_GOODS_INWARD");
    localCellConstraints.xywh(8, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JXButton)localObject).setToolTipText("Help");
    ((JXButton)localObject).setMnemonic(72);
    String str = "F1Action";
    final HelpButton localHelpButton = (HelpButton)localObject;
    InputMap localInputMap = getInputMap(2);
    localInputMap.put(KeyStroke.getKeyStroke("F1"), str);
    AbstractAction local10 = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        localHelpButton.actionPerformed(paramAnonymousActionEvent);
      }
    };
    getActionMap().put(str, local10);
    this._mExpiryNA.setSelected(true);
    int j = this._mExpiry.getComponentCount();
    for (int k = 0; k < j; k++) {
      this._mExpiry.getComponent(k).setEnabled(false);
    }
  }
  
  private JPanel getStockToPanel()
  {
    JPanel localJPanel = new JPanel();
    localJPanel.setLayout(new FormLayout("80px,10px,pref:grow", "25px,10px,25px"));
    CellConstraints localCellConstraints = new CellConstraints();
    this._mStockToWearhouse = new JRadioButton("Warehouse");
    this._mStockToWearhouse.setToolTipText("Send stock to warehouse");
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    localJPanel.add(this._mStockToWearhouse, localCellConstraints);
    this._mStockToWearhouse.setMnemonic(87);
    this._mWeareHouseCombo = new JComboBox();
    localCellConstraints.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mWeareHouseCombo, localCellConstraints);
    this._mWeareHouseCombo.setToolTipText("Select warehouse");
    this._mStockToStore = new JRadioButton("Store");
    this._mStockToStore.setToolTipText("Send stock to store");
    localCellConstraints.xywh(1, 3, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    localJPanel.add(this._mStockToStore, localCellConstraints);
    this._mStockToStore.setMnemonic(83);
    this._mStoreCombo = new JComboBox();
    localCellConstraints.xywh(3, 3, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mStoreCombo, localCellConstraints);
    this._mStoreCombo.setToolTipText("Select store");
    ButtonGroup localButtonGroup = new ButtonGroup();
    localButtonGroup.add(this._mStockToWearhouse);
    localButtonGroup.add(this._mStockToStore);
    this._mStockToStore.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (NewStockPanel.this._mStockToStore.isSelected())
        {
          NewStockPanel.this._mWeareHouseCombo.setEnabled(false);
          NewStockPanel.this._mStoreCombo.setEnabled(true);
        }
        else
        {
          NewStockPanel.this._mWeareHouseCombo.setEnabled(true);
          NewStockPanel.this._mStoreCombo.setEnabled(false);
        }
      }
    });
    this._mStockToWearhouse.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (NewStockPanel.this._mStockToStore.isSelected())
        {
          NewStockPanel.this._mWeareHouseCombo.setEnabled(false);
          NewStockPanel.this._mStoreCombo.setEnabled(true);
        }
        else
        {
          NewStockPanel.this._mWeareHouseCombo.setEnabled(true);
          NewStockPanel.this._mStoreCombo.setEnabled(false);
        }
      }
    });
    return localJPanel;
  }
  
  public void setDefaultFocus()
  {
    this._mProductNum.requestFocusInWindow();
  }
  
  public JPanel getPanel()
  {
    return this;
  }
  
  public void fillVendorDetails()
  {
    try
    {
      ArrayList localArrayList = VendorTableDef.getInstance().getAllValues();
      this._mVendor.removeAllItems();
      if ((localArrayList == null) || (localArrayList.size() == 0)) {
        return;
      }
      Iterator localIterator = localArrayList.iterator();
      while (localIterator.hasNext())
      {
        DBRow localDBRow = (DBRow)localIterator.next();
        this._mVendor.addItem(localDBRow);
      }
    }
    catch (DBException localDBException)
    {
      UICommon.showError("Internal Error Vendor list. Try again later.\n\nIf the problem persists contact administrator.", "Internal Error", MainWindow.instance);
      return;
    }
  }
  
  public void fillStoreAndWearhouseList()
  {
    try
    {
      ArrayList localArrayList1 = StoreInfoTableDef.getInstance().getAllValues();
      if ((localArrayList1 != null) && (localArrayList1.size() > 0))
      {
        this._mStoreCombo.removeAllItems();
        for (int i = 0; i < localArrayList1.size(); i++) {
          this._mStoreCombo.addItem(localArrayList1.get(i));
        }
      }
      ArrayList localArrayList2 = WearehouseInfoTableDef.getInstance().getAllValues();
      if ((localArrayList2 == null) || (localArrayList2.size() == 0)) {
        return;
      }
      this._mWeareHouseCombo.removeAllItems();
      for (int j = 0; j < localArrayList2.size(); j++) {
        this._mWeareHouseCombo.addItem(localArrayList2.get(j));
      }
    }
    catch (DBException localDBException)
    {
      UICommon.showError("Internal Error populating store list. Try again later.\\n\\nIf the problem persists contact administrator.\", ", "Internal Error", MainWindow.instance);
      return;
    }
  }
  
  private void resetAllFields()
  {
    this._mQuantityUnit.setText("");
    this._mQuantity.setText("");
    this._mPurchasePrice.setText("0.00");
  }
  
  private void applyClicked()
  {
    String str = this._mProductNum.getText().trim();
    if (str.length() == 0)
    {
      UICommon.showError("Invalid product code.", "Error", MainWindow.instance);
      this._mProductNum.requestFocusInWindow();
      return;
    }
    DBConnection localDBConnection = Db.getConnection();
    str = this._mQuantity.getText();
    if (str.length() == 0)
    {
      UICommon.showError("Quantity cannot be empty.", "Error", MainWindow.instance);
      this._mQuantity.requestFocusInWindow();
      localDBConnection.rollbackNoExp();
      return;
    }
    if (str.indexOf('.') != -1)
    {
      if (!Validation.isValidFloat(str, 3, false))
      {
        UICommon.showError("Invalid quantity specified.", "Error", MainWindow.instance);
        this._mQuantity.requestFocusInWindow();
        localDBConnection.rollbackNoExp();
      }
    }
    else if (!Validation.isValidInt(str, false))
    {
      UICommon.showError("Invalid quantity specified.", "Error", MainWindow.instance);
      this._mQuantity.requestFocusInWindow();
      localDBConnection.rollbackNoExp();
      return;
    }
    double d1 = new Double(str).doubleValue();
    if (d1 == 0.0D)
    {
      UICommon.showError("New stock quantity cannot be zero", "Error", MainWindow.instance);
      localDBConnection.rollbackNoExp();
      return;
    }
    str = this._mPurchasePrice.getText();
    if (str.length() == 0)
    {
      UICommon.showError("Purchase priice cannot be empty.", "Error", MainWindow.instance);
      this._mPurchasePrice.requestFocusInWindow();
      localDBConnection.rollbackNoExp();
      return;
    }
    if (!Validation.isValidAmount(str))
    {
      UICommon.showError("Invalid purchase price specified.", "Error", MainWindow.instance);
      this._mPurchasePrice.requestFocusInWindow();
      localDBConnection.rollbackNoExp();
      return;
    }
    if (this._mVendor.getSelectedItem() == null)
    {
      UICommon.showError("Vendor cannot be empty..", "Error", MainWindow.instance);
      this._mVendor.requestFocusInWindow();
      localDBConnection.rollbackNoExp();
      return;
    }
    double d2 = new Double(str).doubleValue();
    java.util.Date localDate1 = this._mStockDate.getDate();
    java.util.Date localDate2 = this._mExpiry.getDate();
    if (localDate2 == null) {
      localDate2 = new java.util.Date();
    }
    BigDecimal localBigDecimal1 = new BigDecimal(d2);
    BigDecimal localBigDecimal2 = new BigDecimal(d1);
    d2 = localBigDecimal1.divide(localBigDecimal2, 2, RoundingMode.HALF_UP).doubleValue();
    int i = 1;
    CurrentStockRow localCurrentStockRow1 = -1;
    if (!this._mStockToWearhouse.isSelected())
    {
      localObject = (StoreInfoRow)this._mStoreCombo.getSelectedItem();
      if (localObject == null)
      {
        UICommon.showError("Please select a store where the stock has to be moved.", "Error", MainWindow.instance);
        localDBConnection.rollbackNoExp();
        return;
      }
      localCurrentStockRow1 = ((StoreInfoRow)localObject).getStoreId();
      i = 0;
    }
    else
    {
      localObject = (WearehouseInfoRow)this._mWeareHouseCombo.getSelectedItem();
      if (localObject == null)
      {
        UICommon.showError("Please select a warehouse where the stock has to be moved.", "Error", MainWindow.instance);
        localDBConnection.rollbackNoExp();
        return;
      }
      localCurrentStockRow1 = ((WearehouseInfoRow)localObject).getWearehouseId();
    }
    Object localObject = StockInfoTableDef.getInstance().getNewRow();
    VendorRow localVendorRow = (VendorRow)this._mVendor.getSelectedItem();
    ((StockInfoRow)localObject).setValues(localVendorRow.getVendorId(), this._mSelectedProduct.getProdIndex(), new java.sql.Date(localDate1.getTime()), d2, d1, i != 0 ? "G" : "S", localCurrentStockRow1, -1);
    try
    {
      localDBConnection.openTrans();
      ((StockInfoRow)localObject).create();
      CurrentStockRow localCurrentStockRow2;
      if (i != 0)
      {
        localCurrentStockRow2 = CurrentStockTableDef.getInstance().getNewRow();
        localCurrentStockRow2.setValues(((StockInfoRow)localObject).getStockIndex(), this._mSelectedProduct.getProdIndex(), new java.sql.Date(localDate2.getTime()), this._mExpiryNA.isSelected() ? "Y" : "N", d1, this._mSelectedProduct.getProdUnit(), localCurrentStockRow1);
        localCurrentStockRow2.create();
      }
      else
      {
        localCurrentStockRow2 = localCurrentStockRow1;
        int j = this._mSelectedProduct.getProdIndex();
        int k = ((StockInfoRow)localObject).getStockIndex();
        ArrayList localArrayList = StoreStockTableDef.getInstance().getAllValuesWithWhereClauseForUpdate("STORE_ID = " + localCurrentStockRow2 + " AND PRODUCT_ID = " + j);
        StoreStockRow localStoreStockRow;
        if ((localArrayList != null) && (localArrayList.size() > 0))
        {
          localStoreStockRow = (StoreStockRow)localArrayList.get(0);
          double d3 = localStoreStockRow.getPurchasePrice();
          localStoreStockRow.setPurchasePrice((d2 + d3) / 2.0D);
          double d4 = localStoreStockRow.getStock();
          d4 += d1;
          localStoreStockRow.setStock(d4);
          localStoreStockRow.update(true);
        }
        else
        {
          localStoreStockRow = StoreStockTableDef.getInstance().getNewRow();
          localStoreStockRow.setValues(localCurrentStockRow2, j, d1, d2, this._mExpiryNA.isSelected() ? null : new java.sql.Date(localDate2.getTime()), k);
          localStoreStockRow.create();
        }
      }
      localDBConnection.endTrans();
    }
    catch (DBException localDBException)
    {
      localDBConnection.rollbackNoExp();
      localDBException.printStackTrace();
      UICommon.showError("Internal error updating stock.\n\nCheck the input values. If the problem persists contact administrator.", "Internal Error", MainWindow.instance);
      return;
    }
    UICommon.showMessage("Stock updated successfully.", "Success", MainWindow.instance);
  }
  
  public void configValuesChange()
  {
    this._mPriceLabel.setText("  " + CommonConfig.getInstance().country.currency);
  }
  
  private void setValuesFromProduct(ProductRow paramProductRow)
  {
    if (paramProductRow == null)
    {
      clearAllFields();
      return;
    }
    this._mProductName.setText(paramProductRow.getProdName());
    this._mProductNum.setText(paramProductRow.getProductCode());
    this._mApplyBtn.setEnabled(true);
    this._mSelectedProduct = paramProductRow;
  }
  
  private void searchByProductNumClicked()
  {
    String str = this._mProductNum.getText().trim();
    Object localObject1;
    Object localObject2;
    if (str.length() == 0)
    {
      localObject1 = new ProductSearchPanel(MainWindow.instance);
      ((ProductSearchPanel)localObject1).setDefaultFocus();
      ((ProductSearchPanel)localObject1).setSingleSelectionMode(true);
      ((ProductSearchPanel)localObject1).setVisible(true);
      if (((ProductSearchPanel)localObject1).isCancelled())
      {
        this._mProductNum.requestFocusInWindow();
        return;
      }
      localObject2 = ((ProductSearchPanel)localObject1).getSelectedProducts();
      if ((localObject2 == null) || (localObject2.length == 0))
      {
        this._mProductNum.requestFocusInWindow();
        return;
      }
      this._mProductNum.setText(localObject2[0].getProductCode());
      this._mProductName.setText(localObject2[0].getProdName());
      this._mApplyBtn.setEnabled(true);
      this._mQuantity.requestFocusInWindow();
      this._mSelectedProduct = localObject2[0];
      clearAllFields();
      setValuesFromProduct(localObject2[0]);
      this._mQuantity.requestFocusInWindow();
      return;
    }
    try
    {
      localObject1 = "PRODUCT_CODE='" + str + "' ";
      localObject2 = ProductTableDef.getInstance().getAllValuesWithWhereClause((String)localObject1);
      if ((localObject2 == null) || (((ArrayList)localObject2).size() == 0))
      {
        UICommon.showError("Invalid product code specified", "Error", MainWindow.instance);
        this._mProductNum.requestFocusInWindow();
        return;
      }
      ProductRow localProductRow = (ProductRow)((ArrayList)localObject2).get(0);
      ((ArrayList)localObject2).clear();
      localObject2 = null;
      clearAllFields();
      setValuesFromProduct(localProductRow);
      this._mQuantity.requestFocusInWindow();
    }
    catch (DBException localDBException)
    {
      localDBException.printStackTrace();
      UICommon.showError("Internal Error searching for product.", "Internal Error", MainWindow.instance);
      this._mProductNum.setText(str);
      return;
    }
  }
  
  private void searchByProductNameClicked()
  {
    String str = this._mProductName.getText().trim();
    if (str.length() == 0)
    {
      localProductSearchPanel = new ProductSearchPanel(MainWindow.instance);
      localProductSearchPanel.setDefaultFocus();
      localProductSearchPanel.setSingleSelectionMode(true);
      localProductSearchPanel.setVisible(true);
      if (localProductSearchPanel.isCancelled())
      {
        this._mProductName.requestFocusInWindow();
        return;
      }
      localObject = localProductSearchPanel.getSelectedProducts();
      if ((localObject == null) || (localObject.length == 0))
      {
        this._mProductName.requestFocusInWindow();
        return;
      }
      this._mProductNum.setText(localObject[0].getProductCode());
      this._mProductName.setText(localObject[0].getProdName());
      this._mApplyBtn.setEnabled(true);
      this._mQuantity.requestFocusInWindow();
      this._mSelectedProduct = localObject[0];
      clearAllFields();
      setValuesFromProduct(localObject[0]);
      this._mQuantity.requestFocusInWindow();
      return;
    }
    ProductSearchPanel localProductSearchPanel = new ProductSearchPanel(MainWindow.instance);
    localProductSearchPanel.setLocationRelativeTo(MainWindow.instance);
    str = Db.getSearchFormattedString(str);
    if (str.length() == 0) {
      str = "%";
    }
    Object localObject = localProductSearchPanel.searchProduct(str, "", "", MainWindow.instance);
    if (localObject != null)
    {
      this._mProductNum.setText(((ProductRow)localObject).getProductCode());
      this._mProductName.setText(((ProductRow)localObject).getProdName());
      this._mApplyBtn.setEnabled(true);
      this._mQuantity.requestFocusInWindow();
      this._mSelectedProduct = ((ProductRow)localObject);
      clearAllFields();
      setValuesFromProduct((ProductRow)localObject);
      this._mQuantity.requestFocusInWindow();
    }
    else
    {
      this._mProductName.requestFocusInWindow();
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.inv.NewStockPanel
 * JD-Core Version:    0.7.0.1
 */