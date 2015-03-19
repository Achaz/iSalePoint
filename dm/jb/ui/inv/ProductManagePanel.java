package dm.jb.ui.inv;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.db.objects.CategoryRow;
import dm.jb.db.objects.CategoryTableDef;
import dm.jb.db.objects.DeptRow;
import dm.jb.db.objects.DeptTableDef;
import dm.jb.db.objects.ProductRow;
import dm.jb.db.objects.ProductTableDef;
import dm.jb.db.objects.TaxType;
import dm.jb.db.objects.VendorTableDef;
import dm.jb.ext.RFIDReadListener;
import dm.jb.ext.RFIDReader;
import dm.jb.ui.MainWindow;
import dm.jb.ui.common.HelpButton;
import dm.jb.ui.common.JBQuantityUnitCombo;
import dm.jb.ui.common.ShortKeyCommon;
import dm.jb.ui.settings.CountryInfo;
import dm.tools.db.DBConnection;
import dm.tools.db.DBException;
import dm.tools.db.DBRow;
import dm.tools.db.Db;
import dm.tools.dbui.ActionObject;
import dm.tools.dbui.DBUIActionAdapter;
import dm.tools.dbui.DBUIContainerImpl;
import dm.tools.dbui.DBUIObject;
import dm.tools.dbui.StaticListComponent;
import dm.tools.dbui.ValidationException;
import dm.tools.ui.AbstractMainPanel;
import dm.tools.ui.UICommon;
import dm.tools.ui.components.DBUIComponentValueSetter;
import dm.tools.ui.components.JBDBUIAmountTextField;
import dm.tools.ui.components.JBDBUIComboBox;
import dm.tools.ui.components.JBDBUIFloatTextField;
import dm.tools.ui.components.JBDBUIIntegerTextField;
import dm.tools.ui.components.JBSearchButton;
import dm.tools.ui.components.JBStringTextField;
import dm.tools.ui.components.Validator;
import dm.tools.utils.CommonConfig;
import dm.tools.utils.CommonConfigChangeListener;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JSeparator;
import org.jdesktop.swingx.JXButton;

public class ProductManagePanel
  extends AbstractMainPanel
  implements CommonConfigChangeListener, Validator
{
  private JBStringTextField _mProductId = null;
  private JBStringTextField _mProductName = null;
  private JBStringTextField _mRFID = null;
  private JBDBUIAmountTextField _mAmount = null;
  private JBDBUIFloatTextField _mDiscount = null;
  private JBDBUIFloatTextField _mLowStock = null;
  private JLabel _mLowStockLabel = null;
  private JBDBUIComboBox _mDepartment = null;
  private JBDBUIComboBox _mCategory = null;
  private JBDBUIComboBox _mVendor = null;
  private JComboBox _mDiscountUnit = null;
  private JComboBox _mQuantityUnit = null;
  private static ProductManagePanel _mProdPanel = null;
  private JBDBUIFloatTextField _mTax = null;
  private JBTaxTypeCombo _mTaxTypes = null;
  private JLabel _mCurrencyLabel = null;
  private JButton _mOKButton = null;
  private DBUIContainerImpl _mDBUIContainer = null;
  private JBDBUIIntegerTextField _mLoyaltyPoints = null;
  private JCheckBox _mAutoGenerateProductId = null;
  private JLabel _mProductIdLbl = null;
  private JBDBUIIntegerTextField _mRedeemablePoints = null;
  private JLabel _mRedeemPointsLbl = null;
  
  public static ProductManagePanel getNewProductPanel()
  {
    if (_mProdPanel == null)
    {
      _mProdPanel = new ProductManagePanel();
      _mProdPanel.clearAllFields();
    }
    else
    {
      _mProdPanel.clearAllFields();
    }
    return _mProdPanel;
  }
  
  public ProductManagePanel()
  {
    initUI();
    CommonConfig.getInstance().addChangeListener(this);
    RFIDReader localRFIDReader = RFIDReader._mInstance;
    localRFIDReader.addReadListener(new RFIDReadListener()
    {
      public void dataRead(String paramAnonymousString)
      {
        if (!ProductManagePanel.this.isVisible()) {
          return;
        }
        ProductManagePanel.this._mRFID.setText(paramAnonymousString);
        ProductManagePanel.this._mRFID.requestFocusInWindow();
      }
    });
  }
  
  public void windowDisplayed()
  {
    JBDBUIComboBox localJBDBUIComboBox = this._mVendor;
    try
    {
      localJBDBUIComboBox.initSelf();
    }
    catch (DBException localDBException1)
    {
      localDBException1.printStackTrace();
    }
    getRootPane().setDefaultButton(this._mOKButton);
    localJBDBUIComboBox = this._mDepartment;
    try
    {
      localJBDBUIComboBox.initSelf();
    }
    catch (DBException localDBException2)
    {
      localDBException2.printStackTrace();
    }
    this._mDBUIContainer.resetAttributes(true);
    this._mProductId.setText("");
    this._mAutoGenerateProductId.setSelected(true);
    if ((CommonConfig.getInstance().redemptionOption == 2) || (CommonConfig.getInstance().redemptionOption == 0))
    {
      this._mRedeemablePoints.setEnabled(false);
      this._mRedeemPointsLbl.setEnabled(false);
    }
    else
    {
      this._mRedeemablePoints.setEnabled(true);
      this._mRedeemPointsLbl.setEnabled(true);
    }
    if (CommonConfig.getInstance().finalTax)
    {
      this._mTax.setEditable(false);
      this._mTaxTypes.setEnabled(false);
    }
    else
    {
      this._mTax.setEditable(true);
      this._mTaxTypes.setEnabled(true);
    }
    this._mTax.setText(Double.valueOf(CommonConfig.getInstance().finalTaxAmount).toString());
  }
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("10px, 90px, 10px, 100px, 20px, 80px, 3px,40px,10px,80px,3px,40px,pref:grow,10px", "10px,25px, 10px,25px, 10px,25px,10px, 25px, 10px, 25px, 10px, 25px, 10px, 25px, 10px, 25px , 10px, 25px, 10px, 25px, 10px, 25px,  20px, 30px, 20px,30px,10px");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    int i = 2;
    JLabel localJLabel = new JLabel("Product Code :");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mProductId = new JBStringTextField();
    this._mProductId.setToolTipText("Product Code");
    localCellConstraints.xywh(4, i, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mProductId, localCellConstraints);
    this._mProductId.setMaxLength(15);
    this._mProductId.setRunTimeValidation(true);
    this._mProductId.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 114) {
          ProductManagePanel.this.searchByProductCode();
        }
        if (paramAnonymousKeyEvent.getKeyCode() == 10) {
          paramAnonymousKeyEvent.consume();
        }
      }
    });
    ShortKeyCommon.shortKeyForLabels(localJLabel, 80, this._mProductId);
    JBSearchButton localJBSearchButton = new JBSearchButton(false);
    localJBSearchButton.setToolTipText("Search by product code");
    localJBSearchButton.setMnemonic(74);
    localCellConstraints.xywh(8, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJBSearchButton, localCellConstraints);
    localJBSearchButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ProductManagePanel.this.searchByProductCode();
      }
    });
    this._mProductIdLbl = localJLabel;
    this._mAutoGenerateProductId = new JCheckBox("Auto-Generate");
    localCellConstraints.xywh(10, i, 3, 1, CellConstraints.LEFT, CellConstraints.FILL);
    add(this._mAutoGenerateProductId, localCellConstraints);
    this._mAutoGenerateProductId.setToolTipText("Select to auto-generate product code.");
    this._mAutoGenerateProductId.setMnemonic(45);
    i += 2;
    localJLabel = new JLabel("Name :");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mProductName = ((JBStringTextField)this._mDBUIContainer.createComponentForAttribute("PROD_NAME", "Product Name"));
    this._mProductName.setMaxLength(64);
    this._mProductName.setName("PROD_MANAGE.NAME");
    this._mProductName.setToolTipText("Product Name");
    ShortKeyCommon.shortKeyForLabels(localJLabel, 78, this._mProductName);
    localCellConstraints.xywh(4, i, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mProductName, localCellConstraints);
    this._mProductName.setBackground(UICommon.MANDATORY_COLOR);
    localJBSearchButton = new JBSearchButton(false);
    localJBSearchButton.setMnemonic(90);
    localCellConstraints.xywh(8, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJBSearchButton, localCellConstraints);
    this._mProductName.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 114) {
          ProductManagePanel.this.searchByProductName();
        }
      }
    });
    localJBSearchButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ProductManagePanel.this.searchByProductName();
      }
    });
    localJBSearchButton.setToolTipText("Search by product name");
    i += 2;
    localJLabel = new JLabel("RFID :");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mRFID = ((JBStringTextField)this._mDBUIContainer.createComponentForAttribute("RFID", "RFID"));
    this._mRFID.setToolTipText("RFID Code");
    localCellConstraints.xywh(4, i, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mRFID, localCellConstraints);
    this._mRFID.setMaxLength(15);
    this._mRFID.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 114) {
          ProductManagePanel.this.searchByRfid();
        }
        if (paramAnonymousKeyEvent.getKeyCode() == 10) {
          paramAnonymousKeyEvent.consume();
        }
      }
    });
    ShortKeyCommon.shortKeyForLabels(localJLabel, 70, this._mProductId);
    localJBSearchButton = new JBSearchButton(false);
    localJBSearchButton.setToolTipText("Search by RFID code");
    localJBSearchButton.setMnemonic(74);
    localCellConstraints.xywh(8, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJBSearchButton, localCellConstraints);
    localJBSearchButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ProductManagePanel.this.searchByRfid();
      }
    });
    i += 2;
    localJLabel = new JLabel("Department :");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mDepartment = ((JBDBUIComboBox)this._mDBUIContainer.createComponentForTable(DeptTableDef.getInstance(), "Department", (short)3, "DEPT_INDEX", "DEPT_INDEX", false));
    this._mDepartment.setToolTipText("Department");
    ShortKeyCommon.shortKeyForLabels(localJLabel, 84, this._mDepartment);
    this._mDepartment.setName("PROD_MANAGE.DEPT");
    localCellConstraints.xywh(4, i, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
    this._mDepartment.setBackground(UICommon.MANDATORY_COLOR);
    add(this._mDepartment, localCellConstraints);
    this._mDepartment.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ProductManagePanel.this.deptChanged();
      }
    });
    this._mDepartment.setResetAllowed(false);
    i += 2;
    localJLabel = new JLabel("Category :");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mCategory = ((JBDBUIComboBox)this._mDBUIContainer.createComponentForTable(CategoryTableDef.getInstance(), "Category", (short)3, "CAT_INDEX", "CAT_INDEX", false));
    this._mCategory.setResetAllowed(false);
    this._mCategory.setToolTipText("Category");
    ShortKeyCommon.shortKeyForLabels(localJLabel, 71, this._mCategory);
    this._mCategory.setName("PROD_MANAGE.CAT");
    localCellConstraints.xywh(4, i, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mCategory, localCellConstraints);
    this._mCategory.setBackground(UICommon.MANDATORY_COLOR);
    this._mCategory.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ProductManagePanel.this.catChanged();
      }
    });
    i += 2;
    localJLabel = new JLabel("Quantity Unit :");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mQuantityUnit = new JBQuantityUnitCombo("Product Quantity Unit", ProductTableDef.getInstance().getAttributeDefByName("PROD_UNIT"));
    this._mQuantityUnit.setToolTipText("Unit of quantity");
    this._mQuantityUnit.setName("PROD_MANAGE.UNIT");
    ShortKeyCommon.shortKeyForLabels(localJLabel, 81, this._mQuantityUnit);
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    this._mQuantityUnit.setBackground(UICommon.MANDATORY_COLOR);
    add(this._mQuantityUnit, localCellConstraints);
    this._mQuantityUnit.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ProductManagePanel.this.qtyUnitChanged();
      }
    });
    this._mDBUIContainer.addStaticListComponent((StaticListComponent)this._mQuantityUnit, "PROD_UNIT");
    this._mQuantityUnit.setSelectedIndex(2);
    localJLabel = new JLabel("Unit price : ");
    localCellConstraints.xywh(6, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mAmount = ((JBDBUIAmountTextField)this._mDBUIContainer.createComponentForAttribute("UNIT_PRICE", "Unit Price", (short)5, false));
    this._mAmount.setToolTipText("Unit price");
    ShortKeyCommon.shortKeyForLabels(localJLabel, 73, this._mAmount);
    this._mAmount.setHorizontalAlignment(4);
    localCellConstraints.xywh(8, i, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mAmount, localCellConstraints);
    this._mAmount.setBackground(UICommon.MANDATORY_COLOR);
    this._mAmount.initSelf();
    localJLabel = new JLabel("  " + CommonConfig.getInstance().country.currency);
    this._mCurrencyLabel = localJLabel;
    localCellConstraints.xywh(12, i, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    i += 2;
    localJLabel = new JLabel("Discount :");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mDiscount = ((JBDBUIFloatTextField)this._mDBUIContainer.createComponentForAttribute("DISCOUNT", "Discount"));
    this._mDiscount.setRunTimeValidation(true);
    this._mDiscount.setHorizontalAlignment(4);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 79, this._mDiscount);
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mDiscount, localCellConstraints);
    this._mDiscount.initSelf();
    this._mDiscount.setToolTipText("Discount");
    this._mDiscountUnit = new JComboBox();
    localCellConstraints.xywh(5, i, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mDiscountUnit, localCellConstraints);
    this._mDiscountUnit.setToolTipText("Discount type");
    this._mDiscountUnit.addItem("  % per unit   ");
    this._mDiscountUnit.addItem("  " + CommonConfig.getInstance().country.currency + " per unit  ");
    this._mDiscount.setValueSetter(new DBUIComponentValueSetter()
    {
      public void setValueToInstance(DBRow paramAnonymousDBRow)
      {
        double d = 0.0D;
        if (ProductManagePanel.this._mDiscount.getText().length() > 0) {
          d = new Double(ProductManagePanel.this._mDiscount.getText()).doubleValue();
        }
        if ((ProductManagePanel.this._mDiscountUnit.getSelectedIndex() != 0) && (d != 0.0D)) {
          d *= -1.0D;
        }
        paramAnonymousDBRow.setValue("DISCOUNT", Double.valueOf(d));
      }
      
      public void setInstance(DBRow paramAnonymousDBRow)
      {
        if (paramAnonymousDBRow == null)
        {
          ProductManagePanel.this._mDiscount.initSelf();
          return;
        }
        double d = Double.valueOf(paramAnonymousDBRow.getValue("DISCOUNT").toString()).doubleValue();
        if (d > 0.0D)
        {
          ProductManagePanel.this._mDiscountUnit.setSelectedIndex(0);
          ProductManagePanel.this._mDiscount.setText(Double.valueOf(d).toString());
        }
        else
        {
          ProductManagePanel.this._mDiscountUnit.setSelectedIndex(1);
          if (d != 0.0D) {
            d *= -1.0D;
          }
          ProductManagePanel.this._mDiscount.setText(Double.valueOf(d).toString());
        }
      }
    });
    i += 2;
    localJLabel = new JLabel("Tax :");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mTax = ((JBDBUIFloatTextField)this._mDBUIContainer.createComponentForAttribute("TAX", "Tax"));
    ShortKeyCommon.shortKeyForLabels(localJLabel, 88, this._mTax);
    this._mTax.setToolTipText("Tax per unit");
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mTax, localCellConstraints);
    this._mTax.initSelf();
    this._mTax.setHorizontalAlignment(4);
    localJLabel = new JLabel(" %");
    localCellConstraints.xywh(5, i, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mTax.setRunTimeValidation(true);
    this._mTaxTypes = new JBTaxTypeCombo();
    this._mTaxTypes.setToolTipText("Type of tax");
    localCellConstraints.xywh(6, i, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    add(this._mTaxTypes, localCellConstraints);
    this._mDBUIContainer.addStaticListComponent(this._mTaxTypes, "TAX_UNIT");
    i += 2;
    localJLabel = new JLabel("Low Stock :");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mLowStock = ((JBDBUIFloatTextField)this._mDBUIContainer.createComponentForAttribute("LOW_STOCK", "Low Stock"));
    ShortKeyCommon.shortKeyForLabels(localJLabel, 87, this._mLowStock);
    this._mLowStock.setHorizontalAlignment(4);
    this._mLowStock.setToolTipText("Low stock quantity/Re-order level");
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mLowStock, localCellConstraints);
    this._mLowStockLabel = new JLabel(" Units");
    localCellConstraints.xywh(5, i, 2, 1, CellConstraints.LEFT, CellConstraints.FILL);
    add(this._mLowStockLabel, localCellConstraints);
    this._mLowStock.setRunTimeValidation(true);
    i += 2;
    localJLabel = new JLabel("Loyalty Points :");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mLoyaltyPoints = ((JBDBUIIntegerTextField)this._mDBUIContainer.createComponentForAttribute("LOYALTY_POINTS", "Loyalty Points"));
    ShortKeyCommon.shortKeyForLabels(localJLabel, 89, this._mLoyaltyPoints);
    this._mLoyaltyPoints.setHorizontalAlignment(4);
    this._mLoyaltyPoints.setToolTipText("Loyalty points per unit");
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mLoyaltyPoints, localCellConstraints);
    this._mLoyaltyPoints.setText("0");
    localJLabel = new JLabel("Redeem : ");
    this._mRedeemPointsLbl = localJLabel;
    localCellConstraints.xywh(6, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mRedeemablePoints = ((JBDBUIIntegerTextField)this._mDBUIContainer.createComponentForAttribute("REDEEMABLE_POINTS", "Redeemable Points"));
    ShortKeyCommon.shortKeyForLabels(localJLabel, 69, this._mRedeemablePoints);
    this._mRedeemablePoints.setHorizontalAlignment(4);
    this._mRedeemablePoints.setToolTipText("Loaylty points required to get the product with redeem");
    localCellConstraints.xywh(8, i, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mRedeemablePoints, localCellConstraints);
    this._mRedeemablePoints.setText("0");
    i += 2;
    localJLabel = new JLabel("Vendor :");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mVendor = ((JBDBUIComboBox)this._mDBUIContainer.createComponentForTable(VendorTableDef.getInstance(), "Vendor", (short)3, "VENDOR_ID", "VENDOR_ID", false));
    this._mVendor.setResetAllowed(false);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 86, this._mVendor);
    this._mVendor.setToolTipText("Preferred vendor");
    localCellConstraints.xywh(4, i, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
    this._mVendor.setBackground(UICommon.MANDATORY_COLOR);
    add(this._mVendor, localCellConstraints);
    this._mVendor.setName("PROD_MANAGE.VENDOR");
    i += 2;
    localCellConstraints.xywh(1, i, localFormLayout.getColumnCount(), 1, CellConstraints.LEFT, CellConstraints.FILL);
    add(getActionButtonPanel(), localCellConstraints);
    i++;
    JSeparator localJSeparator = new JSeparator();
    localCellConstraints.xywh(1, i, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(localJSeparator, localCellConstraints);
    i++;
    localCellConstraints.xywh(1, i, localFormLayout.getColumnCount(), 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(getButtonPanel(), localCellConstraints);
    this._mDBUIContainer.addValidator(this);
  }
  
  private JPanel getActionButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("10px, 100px, 20px, 100px, 20px, 100px,20px,100px,10px", "30px");
    CellConstraints localCellConstraints = new CellConstraints();
    localJPanel.setLayout(localFormLayout);
    localCellConstraints.xywh(1, 20, 8, 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(localJPanel, localCellConstraints);
    JButton localJButton = (JButton)this._mDBUIContainer.createActionObject("Add", "CREATE", null);
    localJButton.setMnemonic(65);
    localJButton.setToolTipText("Create a new product");
    this._mOKButton = localJButton;
    localJButton.setName("PROD_MANAGE.CREATE");
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJButton, localCellConstraints);
    ((ActionObject)localJButton).addDBUIActionListener(new DBUIActionAdapter()
    {
      public boolean beforeAction(ActionObject paramAnonymousActionObject)
      {
        if (!ProductManagePanel.this._mAutoGenerateProductId.isSelected())
        {
          String str = ProductManagePanel.this._mProductId.getText();
          ((ProductRow)ProductManagePanel.this._mDBUIContainer.getCurrentInstance()).setProductCode(str);
        }
        return true;
      }
      
      public void actionFailed(Object paramAnonymousObject)
      {
        UICommon.showMessage("Error creating product.", "Error", MainWindow.instance);
        ProductManagePanel.this._mProductName.requestFocusInWindow();
      }
      
      public void afterAction(ActionObject paramAnonymousActionObject)
      {
        ProductRow localProductRow = (ProductRow)ProductManagePanel.this._mDBUIContainer.getCurrentInstance();
        localProductRow.setKeyValue(Integer.valueOf(localProductRow.getProdIndex()));
        if ((localProductRow.getProductCode() == null) || (localProductRow.getProductCode().length() == 0)) {
          localProductRow.setProductCode("" + localProductRow.getProdIndex());
        }
        DBConnection localDBConnection = Db.getConnection();
        try
        {
          localDBConnection.openTrans();
          localProductRow.update(true);
          localDBConnection.endTrans();
        }
        catch (DBException localDBException)
        {
          localDBConnection.rollbackNoExp();
          UICommon.showError("Internal error, while updateing the product code.\n\nThe product is created successfully.", "Internal Error", MainWindow.instance);
          return;
        }
        if (ProductManagePanel.this._mAutoGenerateProductId.isSelected())
        {
          ProductManagePanel.this._mDBUIContainer.resetAttributes();
          ProductManagePanel.this._mAutoGenerateProductId.setSelected(true);
          UICommon.showMessage("Product created successfully.", "Success", MainWindow.instance);
          return;
        }
        ProductManagePanel.this._mDBUIContainer.resetAttributes();
        ProductManagePanel.this._mAutoGenerateProductId.setSelected(true);
        ProductManagePanel.this._mProductId.setText("");
        ProductManagePanel.this._mAutoGenerateProductId.setSelected(true);
        if (CommonConfig.getInstance().finalTax) {
          ProductManagePanel.this._mTax.setText(Double.valueOf(CommonConfig.getInstance().finalTaxAmount).toString());
        }
        UICommon.showMessage("Product created successfully.\nProduct Code : " + localProductRow.getProductCode(), "Success", MainWindow.instance);
      }
    });
    localJButton = (JButton)this._mDBUIContainer.createActionObject("Update", "UPDATE", null);
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJButton, localCellConstraints);
    localJButton.setToolTipText("Update product details");
    localJButton.setMnemonic(85);
    ((ActionObject)localJButton).addDBUIActionListener(new DBUIActionAdapter()
    {
      public boolean beforeAction(ActionObject paramAnonymousActionObject)
      {
        String str = ProductManagePanel.this._mProductId.getText();
        ((ProductRow)ProductManagePanel.this._mDBUIContainer.getCurrentInstance()).setProductCode(str);
        return true;
      }
      
      public void actionFailed(Object paramAnonymousObject)
      {
        UICommon.showMessage("Error updating product.", "Error", MainWindow.instance);
        ProductManagePanel.this._mProductName.requestFocusInWindow();
      }
      
      public void afterAction(ActionObject paramAnonymousActionObject)
      {
        UICommon.showMessage("Product updated successfully.", "Success", MainWindow.instance);
      }
    });
    localJButton = (JButton)this._mDBUIContainer.createActionObject("Delete", "DELETE", null);
    localCellConstraints.xywh(6, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJButton, localCellConstraints);
    localJButton.setToolTipText("Delete the selected product");
    localJButton.setMnemonic(68);
    ((ActionObject)localJButton).addDBUIActionListener(new DBUIActionAdapter()
    {
      public boolean beforeAction(ActionObject paramAnonymousActionObject)
      {
        int i = UICommon.showQuestion("Are yuu sure you want to delete this product?.", "Confirm deletion", MainWindow.instance);
        return i == 1;
      }
      
      public void actionFailed(Object paramAnonymousObject)
      {
        UICommon.showMessage("Error deleting product.", "Error", MainWindow.instance);
        ProductManagePanel.this._mProductName.requestFocusInWindow();
      }
      
      public void afterAction(ActionObject paramAnonymousActionObject)
      {
        ProductManagePanel.this._mDBUIContainer.resetAttributes();
        ProductManagePanel.this._mProductId.setText("");
        ProductManagePanel.this._mAutoGenerateProductId.setSelected(true);
        UICommon.showMessage("Product deleted successfully.\n\nPlease cleanup all the stocks and other records that might be still referring to removed product, by clicking Manager > Cleanup menu item.", "Success", MainWindow.instance);
        if (CommonConfig.getInstance().finalTax) {
          ProductManagePanel.this._mTax.setText(Double.valueOf(CommonConfig.getInstance().finalTaxAmount).toString());
        }
      }
    });
    localJButton = new JButton("Reset");
    localJButton.setToolTipText("Reset the field to default values.");
    localCellConstraints.xywh(8, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJButton, localCellConstraints);
    localJButton.setMnemonic(82);
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ProductManagePanel.this._mDBUIContainer.resetAttributes(true);
        ProductManagePanel.this._mAutoGenerateProductId.setSelected(true);
        ProductManagePanel.this._mProductId.setText("");
        if (CommonConfig.getInstance().finalTax) {
          ProductManagePanel.this._mTax.setText(Double.valueOf(CommonConfig.getInstance().finalTaxAmount).toString());
        }
      }
    });
    return localJPanel;
  }
  
  private JPanel getButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("10px, 100px,20px,100px,10px", "30px");
    CellConstraints localCellConstraints = new CellConstraints();
    localJPanel.setLayout(localFormLayout);
    Object localObject = new JButton(" Close ");
    ((JButton)localObject).setToolTipText("Close the window");
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ProductManagePanel.this.closeWindow();
      }
    });
    localObject = new HelpButton("ISP_PROD_MANAGE");
    ((JButton)localObject).setToolTipText("Display help");
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JButton)localObject).setMnemonic(72);
    return localJPanel;
  }
  
  public void setDefaultFocus()
  {
    this._mProductName.requestFocusInWindow();
  }
  
  public JPanel getPanel()
  {
    return this;
  }
  
  private void qtyUnitChanged()
  {
    if (this._mLowStockLabel != null)
    {
      String str = (String)this._mQuantityUnit.getSelectedItem();
      if (str == null)
      {
        this._mLowStockLabel.setText("");
        return;
      }
      this._mLowStockLabel.setText(" " + str);
    }
  }
  
  private void searchByRfid()
  {
    String str = this._mRFID.getText();
    if (str.length() == 0)
    {
      UICommon.showError("RFID code cannot be empty", "Error", MainWindow.instance);
      this._mRFID.requestFocusInWindow();
      return;
    }
    try
    {
      ProductRow localProductRow = ProductTableDef.getInstance().getProductByRfid(str);
      this._mDBUIContainer.setCurrentInstance(localProductRow);
      if (localProductRow == null)
      {
        UICommon.showError("No product found.", "No record", MainWindow.instance);
        return;
      }
      this._mDBUIContainer.setCurrentInstance(localProductRow);
      this._mAutoGenerateProductId.setSelected(false);
    }
    catch (DBException localDBException)
    {
      UICommon.showError("Internal error searching for product details.\n\nTry again later. If the problem persists contact administrator.", "Internal Error", MainWindow.instance);
      return;
    }
  }
  
  private void searchByProductCode()
  {
    String str = this._mProductId.getText();
    Object localObject;
    if (str.length() == 0)
    {
      localObject = new ProductSearchPanel(MainWindow.instance);
      ProductRow localProductRow = ((ProductSearchPanel)localObject).searchProduct("%", "", "", MainWindow.instance);
      this._mDBUIContainer.setCurrentInstance(localProductRow);
      this._mProductId.requestFocusInWindow();
      return;
    }
    try
    {
      localObject = ProductTableDef.getInstance().getProductByCode(str);
      if (localObject == null)
      {
        UICommon.showError("No product found.", "No record", MainWindow.instance);
        return;
      }
      this._mDBUIContainer.setCurrentInstance((DBRow)localObject);
      this._mAutoGenerateProductId.setSelected(false);
    }
    catch (DBException localDBException)
    {
      UICommon.showError("Internal error searching for product details.\n\nTry again later. If the problem persists contact administrator.", "Internal Error", MainWindow.instance);
      return;
    }
  }
  
  private void catChanged()
  {
    CategoryRow localCategoryRow = (CategoryRow)this._mCategory.getSelectedItem();
    if (localCategoryRow != null)
    {
      double d = localCategoryRow.getDiscount();
      if (d < 0.0D)
      {
        d *= -1.0D;
        this._mDiscount.setText(Double.toString(d));
        this._mDiscountUnit.setSelectedIndex(1);
      }
      else if (d == 0.0D)
      {
        this._mDiscount.initSelf();
      }
      else
      {
        this._mDiscountUnit.setSelectedIndex(0);
        this._mDiscount.setText(Double.toString(d));
      }
      d = localCategoryRow.getTax();
      String str = Double.toString(d);
      if (!CommonConfig.getInstance().finalTax) {
        this._mTax.setText(str);
      }
      TaxType localTaxType = TaxType.getTaxTypeForCode(localCategoryRow.getTaxUnit());
      this._mTaxTypes.setSelectedItem(localTaxType);
    }
  }
  
  private void deptChanged()
  {
    DeptRow localDeptRow = (DeptRow)this._mDepartment.getSelectedItem();
    if (localDeptRow == null)
    {
      this._mCategory.removeAllItems();
      return;
    }
    ArrayList localArrayList = null;
    try
    {
      localArrayList = localDeptRow.getCategoryList();
    }
    catch (DBException localDBException)
    {
      UICommon.showError("Internal error reading list of categories for the selected department.\nTry again later. If the problem persists contact the administrator", "Error", MainWindow.instance);
      return;
    }
    Iterator localIterator = localArrayList.iterator();
    this._mCategory.removeAllItems();
    while (localIterator.hasNext())
    {
      CategoryRow localCategoryRow = (CategoryRow)localIterator.next();
      this._mCategory.addItem(localCategoryRow);
    }
  }
  
  private boolean fillDeptList()
  {
    ArrayList localArrayList = null;
    try
    {
      localArrayList = DeptTableDef.getInstance().getDeptList();
    }
    catch (DBException localDBException)
    {
      UICommon.showError("Internal error reading the list of departments.\nTry again later. If the problem persists contact administrator.", "Error", MainWindow.instance);
      return false;
    }
    Iterator localIterator = localArrayList.iterator();
    this._mDepartment.removeAllItems();
    while (localIterator.hasNext())
    {
      DeptRow localDeptRow = (DeptRow)localIterator.next();
      this._mDepartment.addItem(localDeptRow);
    }
    return true;
  }
  
  public void setConfigValues()
  {
    this._mCurrencyLabel.setText("  " + CommonConfig.getInstance().country.currency);
    this._mDiscountUnit.removeAllItems();
    this._mDiscountUnit.addItem("  % per unit   ");
    this._mDiscountUnit.addItem("  " + CommonConfig.getInstance().country.currency + " per unit  ");
  }
  
  public void validateValue()
    throws ValidationException
  {
    if (!this._mAutoGenerateProductId.isSelected())
    {
      str1 = this._mProductId.getText().trim();
      if (str1.length() == 0)
      {
        this._mProductId.requestFocusInWindow();
        throw new ValidationException("Product code cannot be empty", "Error", "Error");
      }
      if (this._mDBUIContainer.getCurrentInstance().isNew())
      {
        try
        {
          ProductRow localProductRow1 = ProductTableDef.getInstance().getProductByCode(str1);
          if (localProductRow1 != null)
          {
            this._mProductId.requestFocusInWindow();
            throw new ValidationException("Duplicate product code.", "Error", "Error");
          }
        }
        catch (DBException localDBException1)
        {
          this._mProductId.requestFocusInWindow();
          throw new ValidationException("Internal Error", "Error", "Error");
        }
      }
      else
      {
        ProductRow localProductRow2 = (ProductRow)this._mDBUIContainer.getCurrentInstance();
        if (!localProductRow2.getProductCode().equals(str1)) {
          try
          {
            ProductRow localProductRow3 = ProductTableDef.getInstance().getProductByCode(str1);
            if (localProductRow3 != null)
            {
              this._mProductId.requestFocusInWindow();
              throw new ValidationException("Duplicate product code.", "Error", "Error");
            }
          }
          catch (DBException localDBException2)
          {
            this._mProductId.requestFocusInWindow();
            throw new ValidationException("Internal Error", "Error", "Error");
          }
        }
      }
    }
    String str1 = this._mDiscount.getText();
    double d1 = 0.0D;
    double d2 = new Double(this._mAmount.getText()).doubleValue();
    if (str1.length() > 0)
    {
      d1 = new Double(str1).doubleValue();
      if (this._mDiscountUnit.getSelectedIndex() == 0)
      {
        if (d1 > 100.0D) {
          throw new ValidationException("Discount cannot be more than 100%.", "Error", "Error");
        }
      }
      else if (d2 < d1) {
        throw new ValidationException("Discount cannot be more than unit price.", "Error", "Error");
      }
    }
    String str2 = this._mTax.getText();
    if (str2.length() > 0)
    {
      double d3 = new Double(str2).doubleValue();
      if (d3 > 100.0D)
      {
        this._mTax.requestFocusInWindow();
        throw new ValidationException("Tax cannot be more than 100%.", "Error", "Error");
      }
    }
    DeptRow localDeptRow = (DeptRow)this._mDepartment.getSelectedItem();
    CategoryRow localCategoryRow = (CategoryRow)this._mCategory.getSelectedItem();
    String str3 = this._mProductName.getText().trim();
    try
    {
      if ((this._mDBUIContainer.getCurrentInstance() == null) && (!((ProductRow)this._mDBUIContainer.getCurrentInstance()).getProdName().equals(str3)) && (ProductTableDef.getInstance().isDuplicateProductName(str3, localCategoryRow.getCatIndex(), localDeptRow.getDeptIndex())))
      {
        this._mProductName.requestFocusInWindow();
        throw new ValidationException("Product with the same name already exist.", "Error", "Error");
      }
    }
    catch (DBException localDBException3)
    {
      throw new ValidationException("Internal Error verifying the duplicate product name.", "Internal Error", "Internal Error");
    }
  }
  
  private void searchByProductName()
  {
    ProductSearchPanel localProductSearchPanel = new ProductSearchPanel(MainWindow.instance);
    DeptRow localDeptRow = (DeptRow)this._mDepartment.getSelectedItem();
    String str1 = this._mProductName.getText().trim();
    if (str1.length() == 0) {
      str1 = "%";
    }
    String str2 = "";
    if (localDeptRow != null) {
      str2 = localDeptRow.getDeptName();
    }
    CategoryRow localCategoryRow = (CategoryRow)this._mCategory.getSelectedItem();
    String str3 = "";
    if (localCategoryRow != null) {
      str3 = localCategoryRow.getCatName();
    }
    ProductRow localProductRow = localProductSearchPanel.searchProduct(str1, str3, str2, MainWindow.instance);
    if (localProductRow != null)
    {
      this._mDBUIContainer.setCurrentInstance(localProductRow);
      this._mAutoGenerateProductId.setSelected(false);
      if ((localProductRow.getProductCode() == null) || (localProductRow.getProductCode().length() == 0))
      {
        this._mProductId.setText("" + localProductRow.getProductCode());
      }
      else
      {
        this._mProductId.setText(localProductRow.getProductCode());
        this._mAutoGenerateProductId.setSelected(false);
      }
    }
    else
    {
      this._mProductName.requestFocusInWindow();
    }
  }
  
  public class JBTaxTypeCombo
    extends JComboBox
    implements StaticListComponent
  {
    public JBTaxTypeCombo()
    {
      super();
    }
    
    public void setValueToRow(DBRow paramDBRow)
    {
      TaxType localTaxType = (TaxType)getSelectedItem();
      paramDBRow.setValue("TAX_UNIT", Integer.valueOf(localTaxType.getCode()));
    }
    
    public void setValueFromRow(DBRow paramDBRow)
    {
      if (paramDBRow == null)
      {
        setSelectedIndex(0);
        return;
      }
      Object localObject = paramDBRow.getValue("TAX_UNIT");
      int i = 0;
      if (localObject != null) {
        i = Integer.valueOf(localObject.toString()).intValue();
      }
      setSelectedItem(TaxType.getTaxTypeForCode(i));
    }
    
    public void validateValue()
      throws ValidationException
    {
      if (getSelectedItem() == null) {
        throw new ValidationException("Tax type cannot be empty.", "Error", "Error");
      }
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.inv.ProductManagePanel
 * JD-Core Version:    0.7.0.1
 */