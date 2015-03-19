package dm.jb.ui.inv;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.db.objects.CategoryRow;
import dm.jb.db.objects.CategoryTableDef;
import dm.jb.db.objects.DeptRow;
import dm.jb.db.objects.DeptTableDef;
import dm.jb.db.objects.TaxType;
import dm.jb.ui.MainWindow;
import dm.jb.ui.common.HelpButton;
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
import dm.tools.ui.components.DBUIResultPanel;
import dm.tools.ui.components.JBActionButton;
import dm.tools.ui.components.JBDBUIFloatTextField;
import dm.tools.ui.components.JBSearchButton;
import dm.tools.ui.components.JBStringTextField;
import dm.tools.ui.components.JBTextArea;
import dm.tools.ui.components.Validator;
import dm.tools.utils.CommonConfig;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import org.jdesktop.swingx.JXButton;

public class CategoryManagePanel
  extends AbstractMainPanel
  implements Validator
{
  private static CategoryManagePanel _mInstance = null;
  private DBUIContainerImpl _mDBUIContainer = null;
  private JBStringTextField _mCatName = null;
  private JComboBox _mDepartment = null;
  private JBDBUIFloatTextField _mDiscount = null;
  private JComboBox _mDiscountUnit = null;
  private JBDBUIFloatTextField _mTax = null;
  private JBTaxTypeCombo _mTaxTypes = null;
  private JBTextArea _mDetails = null;
  private boolean _mTaxChanged = false;
  private boolean _mTaxUnitChanged = false;
  private boolean _mDiscountChanged = false;
  
  private CategoryManagePanel()
  {
    initUI();
    this._mDBUIContainer.addValidator(this);
  }
  
  public static CategoryManagePanel getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new CategoryManagePanel();
    }
    return _mInstance;
  }
  
  public void setDefaultFocus()
  {
    this._mCatName.requestFocusInWindow();
  }
  
  public JPanel getPanel()
  {
    return this;
  }
  
  public void windowDisplayed()
  {
    DBUIObject localDBUIObject = (DBUIObject)this._mDepartment;
    try
    {
      localDBUIObject.initSelf();
    }
    catch (DBException localDBException)
    {
      localDBException.printStackTrace();
    }
    this._mDBUIContainer.resetAttributes();
    resetFields();
    this._mCatName.requestFocusInWindow();
  }
  
  void resetFields()
  {
    double d = CommonConfig.getInstance().finalTaxAmount;
    this._mTax.setText(Double.valueOf(d).toString());
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
  }
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("10px,90px,10px, 100px,3px,20px,100px,3px,40px,10px:grow", "10px,25px,10px,25px,10px,25px,10px,25px,10px,100px,20px,30px,20px,30px,10px");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    int i = 2;
    JLabel localJLabel = new JLabel("Name : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mCatName = ((JBStringTextField)this._mDBUIContainer.createComponentForAttribute("CAT_NAME", "Category Name"));
    this._mCatName.setName("CAT_MANAGE.CAT_NAME");
    ShortKeyCommon.shortKeyForLabels(localJLabel, 78, this._mCatName);
    this._mCatName.setToolTipText("Category Name");
    this._mCatName.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 114) {
          CategoryManagePanel.this.searchClicked();
        }
      }
    });
    localCellConstraints.xywh(4, i, 4, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mCatName, localCellConstraints);
    this._mCatName.setMinLength(3);
    this._mCatName.setMaxLength(33);
    JBSearchButton localJBSearchButton = new JBSearchButton(false);
    localCellConstraints.xywh(9, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJBSearchButton, localCellConstraints);
    localJBSearchButton.setToolTipText("Search category");
    localJBSearchButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        CategoryManagePanel.this.searchClicked();
      }
    });
    i += 2;
    localJLabel = new JLabel("Department : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mDepartment = ((JComboBox)this._mDBUIContainer.createComponentForTable(DeptTableDef.getInstance(), "Department", (short)3, "DEPT_INDEX", "DEPT_INDEX", false));
    this._mDepartment.setBackground(UICommon.MANDATORY_COLOR);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 84, this._mDepartment);
    this._mDepartment.setToolTipText("Department for the category");
    localCellConstraints.xywh(4, i, 4, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mDepartment, localCellConstraints);
    this._mDepartment.setName("CAT_MANAGE.DEPT");
    DBUIObject localDBUIObject = (DBUIObject)this._mDepartment;
    try
    {
      localDBUIObject.initSelf();
    }
    catch (DBException localDBException)
    {
      localDBException.printStackTrace();
    }
    i += 2;
    localJLabel = new JLabel("Discount : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mDiscount = ((JBDBUIFloatTextField)this._mDBUIContainer.createComponentForAttribute("DISCOUNT", "Discount"));
    this._mDiscount.setToolTipText("Discount");
    this._mDiscount.setName("CAT_MANAGE.DISCOUNT");
    this._mDiscount.setHorizontalAlignment(4);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 79, this._mDiscount);
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mDiscount, localCellConstraints);
    this._mDiscount.initSelf();
    this._mDiscountUnit = new JComboBox();
    localCellConstraints.xywh(7, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mDiscountUnit, localCellConstraints);
    this._mDiscountUnit.addItem("  % per unit   ");
    this._mDiscountUnit.addItem("  " + CommonConfig.getInstance().country.currency + " per unit  ");
    this._mDiscountUnit.setToolTipText("Discount type");
    this._mDiscount.setValueSetter(new DBUIComponentValueSetter()
    {
      public void setValueToInstance(DBRow paramAnonymousDBRow)
      {
        double d = 0.0D;
        if (CategoryManagePanel.this._mDiscount.getText().length() > 0) {
          d = new Double(CategoryManagePanel.this._mDiscount.getText()).doubleValue();
        }
        if ((CategoryManagePanel.this._mDiscountUnit.getSelectedIndex() != 0) && (d != 0.0D)) {
          d *= -1.0D;
        }
        paramAnonymousDBRow.setValue("DISCOUNT", Double.valueOf(d));
      }
      
      public void setInstance(DBRow paramAnonymousDBRow)
      {
        if (paramAnonymousDBRow == null)
        {
          CategoryManagePanel.this._mDiscount.initSelf();
          return;
        }
        double d = Double.valueOf(paramAnonymousDBRow.getValue("DISCOUNT").toString()).doubleValue();
        if (d >= 0.0D)
        {
          CategoryManagePanel.this._mDiscountUnit.setSelectedIndex(0);
          CategoryManagePanel.this._mDiscount.setText(Double.valueOf(d).toString());
        }
        else
        {
          CategoryManagePanel.this._mDiscountUnit.setSelectedIndex(1);
          d *= -1.0D;
          CategoryManagePanel.this._mDiscount.setText(Double.valueOf(d).toString());
        }
      }
    });
    i += 2;
    localJLabel = new JLabel("Tax :");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mTax = ((JBDBUIFloatTextField)this._mDBUIContainer.createComponentForAttribute("TAX", "Tax"));
    this._mTax.setName("CAT_MANAGE.TAX");
    this._mTax.setToolTipText("Tax");
    ShortKeyCommon.shortKeyForLabels(localJLabel, 88, this._mTax);
    this._mTax.setHorizontalAlignment(4);
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mTax, localCellConstraints);
    this._mTax.initSelf();
    localJLabel = new JLabel(" %");
    localCellConstraints.xywh(6, i, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mTax.setRunTimeValidation(true);
    this._mTax.setMaxValue(100.0F);
    this._mTax.setMinValue(0.0F);
    this._mTaxTypes = new JBTaxTypeCombo();
    localCellConstraints.xywh(7, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mTaxTypes, localCellConstraints);
    this._mDBUIContainer.addStaticListComponent(this._mTaxTypes, "TAX_UNIT");
    this._mTaxTypes.setToolTipText("Tax Type");
    i += 2;
    localJLabel = new JLabel("Details : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.TOP);
    add(localJLabel, localCellConstraints);
    this._mDetails = ((JBTextArea)this._mDBUIContainer.createComponentForAttribute("CAT_DETAILS", "Category Details", (short)1, false));
    this._mDetails.setToolTipText("Description of category");
    ShortKeyCommon.shortKeyForLabels(localJLabel, 73, this._mDetails);
    localCellConstraints.xywh(4, i, 5, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(new JScrollPane(this._mDetails), localCellConstraints);
    this._mDetails.setMaxLength(255);
    i += 2;
    localCellConstraints.xywh(1, i, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getActionPanel(), localCellConstraints);
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
    localJPanel.setLayout(new FormLayout("10px:grow,100px,20px,100px,10px", "30px"));
    CellConstraints localCellConstraints = new CellConstraints();
    Object localObject = new JXButton("Close");
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JXButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        CategoryManagePanel.this.closeWindow();
      }
    });
    ((JXButton)localObject).setToolTipText("Close the window");
    localObject = new HelpButton("ISP_CAT_MANAGE");
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JXButton)localObject).setToolTipText("Help");
    ((JXButton)localObject).setMnemonic(72);
    return localJPanel;
  }
  
  private JPanel getActionPanel()
  {
    JPanel localJPanel = new JPanel();
    localJPanel.setLayout(new FormLayout("10px,100px,20px,100px,20px,100px,20px,100px,10px:grow", "30px"));
    CellConstraints localCellConstraints = new CellConstraints();
    JBActionButton localJBActionButton = (JBActionButton)this._mDBUIContainer.createActionObject("Add", "CREATE", null);
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJBActionButton.setToolTipText("Create and add category");
    localJPanel.add(localJBActionButton, localCellConstraints);
    localJBActionButton.setMnemonic(65);
    localJBActionButton.addDBUIActionListener(new DBUIActionAdapter()
    {
      public void actionFailed(Object paramAnonymousObject)
      {
        UICommon.showMessage("Error creating category.", "Error", MainWindow.instance);
        CategoryManagePanel.this._mCatName.requestFocusInWindow();
      }
      
      public void afterAction(ActionObject paramAnonymousActionObject)
      {
        CategoryManagePanel.this._mDBUIContainer.resetAttributes();
        CategoryManagePanel.this.resetFields();
        CategoryManagePanel.this._mCatName.requestFocusInWindow();
        UICommon.showMessage("Category created successfully.", "Success", MainWindow.instance);
      }
    });
    localJBActionButton.setName("CAT_MANAGE.CREATE");
    localJBActionButton = (JBActionButton)this._mDBUIContainer.createActionObject("Update", "UPDATE", null);
    localJBActionButton.setToolTipText("Update the category");
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJBActionButton, localCellConstraints);
    localJBActionButton.setMnemonic(85);
    localJBActionButton.addDBUIActionListener(new DBUIActionAdapter()
    {
      public boolean beforeAction(ActionObject paramAnonymousActionObject)
      {
        CategoryManagePanel.this._mTaxChanged = CategoryManagePanel.this._mDBUIContainer.getCurrentInstance().isAttributeUpdated("TAX");
        CategoryManagePanel.this._mDiscountChanged = CategoryManagePanel.this._mDBUIContainer.getCurrentInstance().isAttributeUpdated("DISCOUNT");
        CategoryManagePanel.this._mTaxUnitChanged = CategoryManagePanel.this._mDBUIContainer.getCurrentInstance().isAttributeUpdated("TAX_UNIT");
        return true;
      }
      
      public void actionFailed(Object paramAnonymousObject)
      {
        UICommon.showMessage("Error updating category.", "Error", MainWindow.instance);
        CategoryManagePanel.this._mCatName.requestFocusInWindow();
      }
      
      public void afterAction(ActionObject paramAnonymousActionObject)
      {
        UICommon.showMessage("Category updated successfully.", "Success", MainWindow.instance);
        CategoryRow localCategoryRow = (CategoryRow)CategoryManagePanel.this._mDBUIContainer.getCurrentInstance();
        int i = 0;
        int j;
        String str;
        DBConnection localDBConnection2;
        Statement localStatement;
        if (CategoryManagePanel.this._mTaxChanged)
        {
          j = UICommon.showQuestion("Tax for the category is changed. Do you want to update all the products under this category with new tax value.", "Confirm Product update.", MainWindow.instance);
          if (j == 1)
          {
            i = 1;
            str = "UPDATE PRODUCT SET TAX=" + localCategoryRow.getTax() + " WHERE CAT_INDEX=" + localCategoryRow.getCatIndex();
            localDBConnection2 = Db.getConnection();
            try
            {
              Connection localConnection1 = localDBConnection2.getJDBCConnection();
              localStatement = localConnection1.createStatement();
              localStatement.executeUpdate(str);
            }
            catch (SQLException localSQLException1)
            {
              localDBConnection2.rollbackNoExp();
              UICommon.showInternalError("Internal error updating product details.");
              return;
            }
          }
        }
        if (CategoryManagePanel.this._mTaxUnitChanged)
        {
          j = UICommon.showQuestion("Tax type for the category is changed. Do you want to update all the products under this category with new tax type.", "Confirm Product update.", MainWindow.instance);
          if (j == 1)
          {
            i = 1;
            str = "UPDATE PRODUCT SET TAX_UNIT=" + localCategoryRow.getTaxUnit() + " WHERE CAT_INDEX=" + localCategoryRow.getCatIndex();
            localDBConnection2 = Db.getConnection();
            try
            {
              Connection localConnection2 = localDBConnection2.getJDBCConnection();
              localStatement = localConnection2.createStatement();
              localStatement.executeUpdate(str);
            }
            catch (SQLException localSQLException2)
            {
              localDBConnection2.rollbackNoExp();
              UICommon.showInternalError("Internal error updating product details.");
              return;
            }
          }
        }
        if (CategoryManagePanel.this._mDiscountChanged)
        {
          j = UICommon.showQuestion("Discount for the category is changed. Do you want to update all the products under this category with new discount value.", "Confirm Product update.", MainWindow.instance);
          if (j == 1)
          {
            i = 1;
            str = "UPDATE PRODUCT SET DISCOUNT=" + localCategoryRow.getDiscount() + " WHERE CAT_INDEX=" + localCategoryRow.getCatIndex();
            localDBConnection2 = Db.getConnection();
            try
            {
              Connection localConnection3 = localDBConnection2.getJDBCConnection();
              localStatement = localConnection3.createStatement();
              localStatement.executeUpdate(str);
            }
            catch (SQLException localSQLException3)
            {
              localDBConnection2.rollbackNoExp();
              UICommon.showInternalError("Internal error updating product details.");
              return;
            }
          }
        }
        if (i != 0)
        {
          DBConnection localDBConnection1 = Db.getConnection();
          try
          {
            localDBConnection1.endTrans();
          }
          catch (DBException localDBException)
          {
            localDBConnection1.rollbackNoExp();
            UICommon.showInternalError("Internal error updating product details.");
            return;
          }
          UICommon.showMessage("Product details updated successfully.", "Success", MainWindow.instance);
        }
      }
    });
    localJBActionButton = (JBActionButton)this._mDBUIContainer.createActionObject("Delete", "DELETE", null);
    localCellConstraints.xywh(6, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJBActionButton, localCellConstraints);
    localJBActionButton.setToolTipText("Remove the category");
    localJBActionButton.setMnemonic(68);
    localJBActionButton.addDBUIActionListener(new DBUIActionAdapter()
    {
      public boolean beforeAction(ActionObject paramAnonymousActionObject)
      {
        int i = UICommon.showQuestion("Are you sure you want to delete the category ?. \n\nAll the products under the category will also be removed to maintain the consistency.", "Confirm Deletion", MainWindow.instance);
        return i == 1;
      }
      
      public void actionFailed(Object paramAnonymousObject)
      {
        UICommon.showMessage("Error deleting category.", "Error", MainWindow.instance);
        CategoryManagePanel.this._mCatName.requestFocusInWindow();
      }
      
      public void afterAction(ActionObject paramAnonymousActionObject)
      {
        CategoryManagePanel.this._mDBUIContainer.resetAttributes();
        CategoryManagePanel.this.resetFields();
        UICommon.showMessage("Category deleted successfully.\n\nPlease cleanup all the stocks and other records that might be still referring to removed products, by clicking Manager > Cleanup menu item.", "Success", MainWindow.instance);
      }
    });
    JXButton localJXButton = new JXButton("Reset");
    localJXButton.setToolTipText("Clear the UI");
    localCellConstraints.xywh(8, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.setMnemonic(82);
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        CategoryManagePanel.this._mCatName.requestFocusInWindow();
        CategoryManagePanel.this._mDBUIContainer.resetAttributes();
        CategoryManagePanel.this.resetFields();
        CategoryManagePanel.this._mTaxChanged = false;
        CategoryManagePanel.this._mDiscountChanged = false;
        CategoryManagePanel.this._mTaxUnitChanged = false;
      }
    });
    return localJPanel;
  }
  
  private void searchClicked()
  {
    String str1 = this._mCatName.getText().trim();
    str1 = str1 + "%";
    String str2 = "%";
    DeptRow localDeptRow1 = (DeptRow)this._mDepartment.getSelectedItem();
    if (localDeptRow1 != null)
    {
      str2 = localDeptRow1.getDeptName();
      str2 = str2 + "%";
    }
    ArrayList localArrayList1 = null;
    try
    {
      if (!str2.equals("%"))
      {
        ArrayList localArrayList2 = DeptTableDef.getInstance().getAllValuesWithWhereClause("DEPT_NAME LIKE '" + str2 + "'");
        if ((localArrayList2 == null) || (localArrayList2.size() == 0))
        {
          UICommon.showError("No category found for the selected department.", "No data found", MainWindow.instance);
          return;
        }
        localObject = new ArrayList();
        for (int i = 0; i < localArrayList2.size(); i++)
        {
          DeptRow localDeptRow2 = (DeptRow)localArrayList2.get(i);
          ArrayList localArrayList3 = localDeptRow2.getCategoryList();
          ((ArrayList)localObject).addAll(localArrayList3);
          localArrayList3.clear();
          localArrayList3 = null;
        }
        localArrayList1 = new ArrayList();
        for (i = 0; i < ((ArrayList)localObject).size(); i++) {
          localArrayList1.add(((ArrayList)localObject).get(i));
        }
      }
      else
      {
        localArrayList1 = CategoryTableDef.getInstance().getAllValuesWithWhereClause("CAT_NAME LIKE '" + str1 + "'");
        if ((localArrayList1 == null) || (localArrayList1.size() == 0))
        {
          UICommon.showError("No category foudn macthign the criteria", "No data found.", MainWindow.instance);
          return;
        }
      }
    }
    catch (DBException localDBException)
    {
      UICommon.showError("Internal Error searching product details.\nContact administrator.", "Internal Error", MainWindow.instance);
      return;
    }
    String[] arrayOfString = { "CAT_NAME", "Category Name" };
    Object localObject = new DBUIResultPanel(arrayOfString, MainWindow.instance);
    ((DBUIResultPanel)localObject).setData(localArrayList1);
    ((DBUIResultPanel)localObject).setLocationRelativeTo(MainWindow.instance);
    ((DBUIResultPanel)localObject).setTitle("Categories");
    ((DBUIResultPanel)localObject).setVisible(true);
    this._mCatName.requestFocusInWindow();
    if (((DBUIResultPanel)localObject).isCancelled()) {
      return;
    }
    DBRow localDBRow = (DBRow)((DBUIResultPanel)localObject).getSelectedRow();
    this._mDBUIContainer.setCurrentInstance(localDBRow);
  }
  
  public void validateValue()
    throws ValidationException
  {
    int i = this._mDiscountUnit.getSelectedIndex();
    if ((i == 0) && (this._mDiscount.getText().trim().length() > 0))
    {
      double d = Double.valueOf(this._mDiscount.getText().trim()).doubleValue();
      if (d > 100.0D)
      {
        this._mDiscount.requestFocusInWindow();
        throw new ValidationException("Discount cannot be more than 100.0%", "Error", null);
      }
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
 * Qualified Name:     dm.jb.ui.inv.CategoryManagePanel
 * JD-Core Version:    0.7.0.1
 */