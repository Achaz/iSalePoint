package dm.jb.ui.inv;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.db.objects.CurrentStockRow;
import dm.jb.db.objects.CurrentStockTableDef;
import dm.jb.db.objects.ProductRow;
import dm.jb.db.objects.ProductTableDef;
import dm.jb.db.objects.StoreInfoRow;
import dm.jb.db.objects.StoreInfoTableDef;
import dm.jb.db.objects.StoreStockRow;
import dm.jb.db.objects.StoreStockTableDef;
import dm.jb.db.objects.WearehouseInfoRow;
import dm.jb.db.objects.WearehouseInfoTableDef;
import dm.jb.ui.MainWindow;
import dm.jb.ui.common.HelpButton;
import dm.jb.ui.common.ShortKeyCommon;
import dm.tools.db.DBConnection;
import dm.tools.db.DBException;
import dm.tools.db.Db;
import dm.tools.types.InternalQuantity;
import dm.tools.ui.AbstractMainPanel;
import dm.tools.ui.UICommon;
import dm.tools.ui.components.JBSearchButton;
import dm.tools.utils.Validation;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class RestockPanel
  extends AbstractMainPanel
{
  private static RestockPanel _mRestockPanel = null;
  private JTextField _mRestockQuantity = null;
  private JTextField _mProductId = null;
  private JTextField _mProductName = null;
  private JTextField _mAvailableQuantity = null;
  private JLabel _mRestockQtyUnitLbl = null;
  private JLabel _mQtyUnitLbl = null;
  private JComboBox _mWareHouse = null;
  private JComboBox _mStore = null;
  private CurrentStockTable _mStockTable = null;
  private ProductRow _mSelectedProduct = null;
  private JButton _mOKButton = null;
  ArrayList<CurrentStockRow> _mStockList = null;
  ArrayList<StoreStockRow> _mStoreStock = null;
  
  public JPanel getPanel()
  {
    return this;
  }
  
  public void windowDisplayed()
  {
    getRootPane().setDefaultButton(this._mOKButton);
    this._mOKButton.setEnabled(false);
    try
    {
      ArrayList localArrayList = WearehouseInfoTableDef.getInstance().getWarehouseList();
      this._mWareHouse.removeAllItems();
      Object localObject1 = localArrayList.iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject2 = (WearehouseInfoRow)((Iterator)localObject1).next();
        this._mWareHouse.addItem(localObject2);
      }
      localObject1 = StoreInfoTableDef.getInstance().getStoreList();
      Object localObject2 = StoreInfoTableDef.getCurrentStore();
      Object localObject3 = null;
      Iterator localIterator = ((ArrayList)localObject1).iterator();
      while (localIterator.hasNext())
      {
        StoreInfoRow localStoreInfoRow = (StoreInfoRow)localIterator.next();
        this._mStore.addItem(localStoreInfoRow);
        if ((localObject3 == null) && (localObject2 != null) && (((StoreInfoRow)localObject2).getStoreId() == localStoreInfoRow.getStoreId())) {
          localObject3 = localStoreInfoRow;
        }
      }
      if (localObject3 != null) {
        this._mStore.setSelectedItem(localObject3);
      }
    }
    catch (DBException localDBException)
    {
      UICommon.showError("Internal error populating warehouse list.\n\nTry again later. If the problem persists contact administrator.", "Internal Error", MainWindow.instance);
      return;
    }
  }
  
  public void setDefaultFocus()
  {
    this._mProductId.requestFocusInWindow();
  }
  
  public static RestockPanel getRestockPanel()
  {
    if (_mRestockPanel == null) {
      _mRestockPanel = new RestockPanel();
    }
    _mRestockPanel.clearAllFields();
    return _mRestockPanel;
  }
  
  public RestockPanel()
  {
    initUI();
  }
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("650px:grow", "10px, pref, 20px, 300px:grow, 20px, 30px, 20px");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(1, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getProductPanel(), localCellConstraints);
    localCellConstraints.xywh(1, 4, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getTablePanel(), localCellConstraints);
    localCellConstraints.xywh(1, 5, 1, 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(new JSeparator(), localCellConstraints);
    localCellConstraints.xywh(1, 6, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getButtonPanel(), localCellConstraints);
  }
  
  public JPanel getProductPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("10px, 90px, 10px, 140px,3px, 40px, 3px,40px, 10px", "25px,10px,25px, 10px, 25px, 10px, 25px, 10px, 25px,10px,25px");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    JLabel localJLabel = new JLabel("Product Code : ");
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mProductId = new JTextField();
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mProductId, localCellConstraints);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 80, this._mProductId);
    this._mProductId.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 114) {
          RestockPanel.this.productIdLoadClicked();
        }
      }
    });
    this._mProductId.setBackground(UICommon.MANDATORY_COLOR);
    JBSearchButton localJBSearchButton = new JBSearchButton(false);
    localCellConstraints.xywh(6, 1, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    localJPanel.add(localJBSearchButton, localCellConstraints);
    localJBSearchButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        RestockPanel.this.productIdLoadClicked();
      }
    });
    localJLabel = new JLabel("Product Name : ");
    localCellConstraints.xywh(2, 3, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mProductName = new JTextField();
    localCellConstraints.xywh(4, 3, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mProductName, localCellConstraints);
    this._mProductName.setBackground(UICommon.MANDATORY_COLOR);
    this._mProductName.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 114) {
          RestockPanel.this.searchByProductNameClicked();
        }
      }
    });
    ShortKeyCommon.shortKeyForLabels(localJLabel, 78, this._mProductName);
    localJBSearchButton = new JBSearchButton(false);
    localCellConstraints.xywh(8, 3, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    localJPanel.add(localJBSearchButton, localCellConstraints);
    localJLabel = new JLabel("Available : ");
    localCellConstraints.xywh(2, 5, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mAvailableQuantity = new JTextField();
    localCellConstraints.xywh(4, 5, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mAvailableQuantity, localCellConstraints);
    this._mAvailableQuantity.setEditable(false);
    localJBSearchButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        RestockPanel.this.searchByProductNameClicked();
      }
    });
    ShortKeyCommon.shortKeyForLabels(localJLabel, 86, this._mAvailableQuantity);
    this._mQtyUnitLbl = new JLabel("");
    localCellConstraints.xywh(6, 5, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mQtyUnitLbl, localCellConstraints);
    localJLabel = new JLabel("Restock : ");
    localCellConstraints.xywh(2, 7, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mRestockQuantity = new JTextField();
    localCellConstraints.xywh(4, 7, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mRestockQuantity, localCellConstraints);
    this._mRestockQuantity.setBackground(UICommon.MANDATORY_COLOR);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 79, this._mRestockQuantity);
    this._mRestockQtyUnitLbl = new JLabel("");
    localCellConstraints.xywh(6, 7, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mRestockQtyUnitLbl, localCellConstraints);
    localJLabel = new JLabel("Store : ");
    this._mStore = new JComboBox();
    localCellConstraints.xywh(2, 9, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    localCellConstraints.xywh(4, 9, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mStore, localCellConstraints);
    this._mStore.setBackground(UICommon.MANDATORY_COLOR);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 87, this._mStore);
    localJLabel = new JLabel("Warehouse : ");
    this._mWareHouse = new JComboBox();
    localCellConstraints.xywh(2, 11, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    localCellConstraints.xywh(4, 11, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mWareHouse, localCellConstraints);
    this._mWareHouse.setBackground(UICommon.MANDATORY_COLOR);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 87, this._mWareHouse);
    localJPanel.setBackground(getBackground());
    return localJPanel;
  }
  
  private JPanel getTablePanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("10px, pref:grow,10px", "pref:grow");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    this._mStockTable = new CurrentStockTable();
    this._mStockTable.setSelectionMode(0);
    JScrollPane localJScrollPane = new JScrollPane(this._mStockTable);
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJScrollPane, localCellConstraints);
    localJPanel.setBackground(getBackground());
    setColumnWidths();
    return localJPanel;
  }
  
  public void clearAllFields()
  {
    this._mRestockQuantity.setText("");
    this._mProductId.setText("");
    this._mProductName.setText("");
    this._mAvailableQuantity.setText("");
    this._mRestockQtyUnitLbl.setText("");
    this._mQtyUnitLbl.setText("");
    this._mStockTable.removeAllRows();
    this._mOKButton.setEnabled(false);
    this._mStockTable.updateUI();
  }
  
  private void setColumnWidths()
  {
    TableColumnModel localTableColumnModel = this._mStockTable.getColumnModel();
    TableColumn localTableColumn = localTableColumnModel.getColumn(0);
    localTableColumn.setPreferredWidth(40);
  }
  
  private JPanel getButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("10px, 100px, 20px, 100px, pref:grow, 100px, pref:grow, 100px, 10px", "30px");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    Object localObject = new JButton(" O K ");
    ((JButton)localObject).setMnemonic(79);
    this._mOKButton = ((JButton)localObject);
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        RestockPanel.this.restockClicked();
      }
    });
    localObject = new JButton(" Reset ");
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JButton)localObject).setMnemonic(82);
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        RestockPanel.this.clearAllFields();
        RestockPanel.this.setDefaultFocus();
      }
    });
    localObject = new JButton(" Close ");
    localCellConstraints.xywh(6, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        RestockPanel.this.closeWindow();
      }
    });
    localObject = new HelpButton("ISP_RESTOCK");
    localCellConstraints.xywh(8, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JButton)localObject).setMnemonic(72);
    return localJPanel;
  }
  
  private void setProductData(ProductRow paramProductRow, StoreStockRow paramStoreStockRow)
  {
    this._mProductName.setText(paramProductRow.getProdName());
    this._mProductId.setText(paramProductRow.getProductCode());
    if (paramProductRow.getProdUnit() == 2)
    {
      int i = (int)paramStoreStockRow.getStock();
      this._mAvailableQuantity.setText(Integer.toString(i));
    }
    else
    {
      this._mAvailableQuantity.setText(Double.toString(paramStoreStockRow.getStock()));
    }
    this._mQtyUnitLbl.setText("  " + InternalQuantity.quantityUnits[paramProductRow.getProdUnit()]);
    this._mRestockQtyUnitLbl.setText("  " + InternalQuantity.quantityUnits[paramProductRow.getProdUnit()]);
    this._mOKButton.setEnabled(true);
  }
  
  private void searchByProductNameClicked()
  {
    String str = this._mProductName.getText().trim();
    if ((str == null) || (str.length() == 0) || (str.equals("*")) || (str.equals("%")))
    {
      int i = UICommon.showQuestion("You are going to make an extensive search. This might need more system resource.\n\nDo you want to continue.", "Confirm Search", MainWindow.instance);
      if (i != 1) {
        return;
      }
    }
    ProductSearchPanel localProductSearchPanel = new ProductSearchPanel(MainWindow.instance);
    localProductSearchPanel.setLocationRelativeTo(MainWindow.instance);
    str = Db.getSearchFormattedString(str);
    if (str.length() == 0) {
      str = "%";
    }
    ProductRow localProductRow = localProductSearchPanel.searchProduct(str, "", "", MainWindow.instance);
    if (localProductRow != null) {
      loadStocks(localProductRow);
    }
  }
  
  private void productIdLoadClicked()
  {
    String str = this._mProductId.getText();
    clearAllFields();
    this._mProductId.setText(str);
    if (str.length() == 0)
    {
      UICommon.showError("Product Code cannot be empty.\nIf you want are looking for search, enter the appropriate wild card(eg., '*'), and press enter.", "Error", MainWindow.instance);
      this._mProductId.requestFocusInWindow();
      return;
    }
    if (!Validation.isValidInt(str, false))
    {
      UICommon.showError("Invalid product Code specified", "Error", MainWindow.instance);
      this._mProductId.requestFocusInWindow();
      return;
    }
    int i = new Integer(str).intValue();
    ProductRow localProductRow = null;
    try
    {
      localProductRow = ProductTableDef.getInstance().findRowByIndex(i);
    }
    catch (DBException localDBException)
    {
      UICommon.showError("Internal error reading product details.\nCheck i\the input values. If the problem persis contact administrator", "Internal error", MainWindow.instance);
      return;
    }
    if (localProductRow == null)
    {
      UICommon.showError("No product found for the selected Product Code", "Error", MainWindow.instance);
      this._mProductId.requestFocusInWindow();
      return;
    }
    loadStocks(localProductRow);
  }
  
  private void loadStocks(ProductRow paramProductRow)
  {
    StoreStockRow localStoreStockRow = null;
    try
    {
      localStoreStockRow = StoreStockTableDef.getInstance().getStockForProductInCurrentStore(paramProductRow.getProdIndex());
    }
    catch (DBException localDBException1)
    {
      localDBException1.printStackTrace();
      UICommon.showError("Internal error.\nContact administrator.", "Internal Error", MainWindow.instance);
      return;
    }
    if (localStoreStockRow == null)
    {
      UICommon.showError("No stock item found. This could be an internal program error.\nContact administrator.", "Error", MainWindow.instance);
      return;
    }
    setProductData(paramProductRow, localStoreStockRow);
    this._mSelectedProduct = paramProductRow;
    this._mStockTable.removeAllRows();
    this._mStockTable.updateUI();
    try
    {
      this._mStockList = CurrentStockTableDef.getInstance().getCurrentStockListForProduct(paramProductRow.getProdIndex());
      ArrayList localArrayList = StockInfoAndCurrentStock.getStockInfoAndCurrentStocksForCurrentStocks(this._mStockList);
      Iterator localIterator = localArrayList.iterator();
      while (localIterator.hasNext())
      {
        StockInfoAndCurrentStock localStockInfoAndCurrentStock = (StockInfoAndCurrentStock)localIterator.next();
        this._mStockTable.addStock(localStockInfoAndCurrentStock);
      }
      this._mStockTable.updateUI();
    }
    catch (DBException localDBException2)
    {
      UICommon.showError("Internal error reading stock information.\n\nTry again later. If the problem persists contact administartor", "Internal Error", MainWindow.instance);
      return;
    }
    this._mRestockQuantity.requestFocusInWindow();
  }
  
  private void restockClicked()
  {
    if (this._mSelectedProduct == null)
    {
      UICommon.showError("Please select a product to restock.", "Error", MainWindow.instance);
      this._mProductId.requestFocusInWindow();
      return;
    }
    String str = this._mRestockQuantity.getText().trim();
    if (str.length() == 0)
    {
      UICommon.showError("Restock quantity cannot be empty.", "Error", MainWindow.instance);
      this._mRestockQuantity.requestFocusInWindow();
      return;
    }
    if (!Validation.isValidFloat(str, 5, false))
    {
      UICommon.showError("Invalid Restock quantity.", "Error", MainWindow.instance);
      this._mRestockQuantity.requestFocusInWindow();
      return;
    }
    double d = Double.valueOf(str).floatValue();
    if (d <= 0.0D)
    {
      UICommon.showError("Restock quantity cannot be zero.", "Error", MainWindow.instance);
      return;
    }
    WearehouseInfoRow localWearehouseInfoRow = (WearehouseInfoRow)this._mWareHouse.getSelectedItem();
    CurrentStockRow localCurrentStockRow = CurrentStockTableDef.getInstance().getNewRow();
    localCurrentStockRow.setValues(-1, this._mSelectedProduct.getProdIndex(), null, "Y", d, this._mSelectedProduct.getProdUnit(), localWearehouseInfoRow.getWearehouseId());
    DBConnection localDBConnection = Db.getConnection();
    try
    {
      localDBConnection.openTrans();
      localCurrentStockRow.create();
      localDBConnection.commit();
    }
    catch (DBException localDBException)
    {
      localDBConnection.rollbackNoExp();
      UICommon.showError("Internal Error updating stock details.\n\nTry again later. If the problem persists contact administrator.", "Internal Error", MainWindow.instance);
      return;
    }
    UICommon.showMessage("Stock updated successfully.", "Success", MainWindow.instance);
  }
  
  private boolean isValidData()
  {
    String str = this._mRestockQuantity.getText();
    if (str.length() == 0)
    {
      UICommon.showError("Re-stock quantity not specified.", "Error", MainWindow.instance);
      this._mRestockQuantity.requestFocusInWindow();
      return false;
    }
    if ((str.indexOf(".") != -1) && (!InternalQuantity.isUnitFractionAllowed(this._mSelectedProduct.getProdUnit())))
    {
      UICommon.showError("Fraction quantity is not alowed for this product.", "Error", MainWindow.instance);
      this._mRestockQuantity.requestFocusInWindow();
      return false;
    }
    return true;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.inv.RestockPanel
 * JD-Core Version:    0.7.0.1
 */