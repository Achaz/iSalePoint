package dm.jb.ui.inv;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.db.objects.CurrentStockRow;
import dm.jb.db.objects.CurrentStockTableDef;
import dm.jb.db.objects.ProductRow;
import dm.jb.db.objects.StockAndProductRow;
import dm.jb.db.objects.StockAndProductTableDef;
import dm.jb.db.objects.StoreInfoRow;
import dm.jb.db.objects.StoreInfoTableDef;
import dm.jb.db.objects.StoreStockRow;
import dm.jb.db.objects.StoreStockTableDef;
import dm.jb.db.objects.WearehouseInfoRow;
import dm.jb.db.objects.WearehouseInfoTableDef;
import dm.jb.ui.MainWindow;
import dm.jb.ui.common.HelpButton;
import dm.jb.ui.res.MessageResourceUtils;
import dm.jb.ui.res.ResourceUtils;
import dm.jb.ui.settings.CountryInfo;
import dm.tools.db.DBConnection;
import dm.tools.db.DBException;
import dm.tools.db.Db;
import dm.tools.types.InternalAmount;
import dm.tools.types.InternalQuantity;
import dm.tools.ui.AbstractMainPanel;
import dm.tools.ui.UICommon;
import dm.tools.ui.components.JBSearchButton;
import dm.tools.ui.components.JTextSeparator;
import dm.tools.utils.CommonConfig;
import dm.tools.utils.Validation;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class EditStockPanel
  extends AbstractMainPanel
{
  public static EditStockPanel INSTANCE = new EditStockPanel();
  private JTextField _mProductId = null;
  private JTextField _mProductName = null;
  private JTextField _mPurchasePrice = null;
  private JComboBox _mStores = null;
  private JTextField _mStockInStore = null;
  private JLabel _mStoreUnit = null;
  private JButton _mPriceSaveButton = null;
  private JButton _mQtySaveButton = null;
  private JTable _mWhStock = null;
  private WarehouseStockModel _mModel = null;
  private JComboBox _mwhs = null;
  private MyComboBoxRenderer _mRenderer = null;
  private ArrayList<StockAndProductRow> _mStocks = new ArrayList();
  private ArrayList<CurrentStockRow> _mCurrentStocks = new ArrayList();
  private StockAndProductRow _mCurrentStoreStock = null;
  
  private EditStockPanel()
  {
    initUI();
  }
  
  public void setDefaultFocus()
  {
    this._mProductId.requestFocusInWindow();
  }
  
  public JPanel getPanel()
  {
    return this;
  }
  
  public void windowDisplayed()
  {
    this._mwhs.removeAllItems();
    this._mRenderer.removeAllItems();
    Iterator localIterator;
    Object localObject;
    try
    {
      ArrayList localArrayList1 = WearehouseInfoTableDef.getInstance().getWarehouseList();
      if ((localArrayList1 != null) && (localArrayList1.size() > 0))
      {
        localIterator = localArrayList1.iterator();
        while (localIterator.hasNext())
        {
          localObject = (WearehouseInfoRow)localIterator.next();
          this._mwhs.addItem(localObject);
          this._mRenderer.addItem(localObject);
        }
      }
    }
    catch (DBException localDBException1)
    {
      localDBException1.printStackTrace();
      UICommon.showInternalError(MessageResourceUtils.getString("EDIT_STOCK_WH_INTERNAL_ERROR"));
      return;
    }
    this._mStores.removeAllItems();
    try
    {
      ArrayList localArrayList2 = StoreInfoTableDef.getInstance().getStoreList();
      if ((localArrayList2 != null) && (localArrayList2.size() > 0))
      {
        localIterator = localArrayList2.iterator();
        while (localIterator.hasNext())
        {
          localObject = (StoreInfoRow)localIterator.next();
          this._mStores.addItem(localObject);
        }
      }
    }
    catch (DBException localDBException2)
    {
      localDBException2.printStackTrace();
      UICommon.showInternalError(MessageResourceUtils.getString("EDIT_STOCK_STORE_INTERNAL_ERROR"));
      return;
    }
    resetAllFields();
  }
  
  public void resetAllFields()
  {
    this._mProductName.setText("");
    this._mProductId.setText("");
    this._mPurchasePrice.setText("");
    this._mStockInStore.setText("");
    this._mStoreUnit.setText("");
    this._mModel.removeAllItems();
    this._mPriceSaveButton.setEnabled(false);
    this._mQtySaveButton.setEnabled(false);
    this._mStocks.clear();
    this._mCurrentStocks.clear();
    this._mCurrentStoreStock = null;
  }
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("10px,90px,10px,100px,3px,40px,3px,80px, 20px,3px,40px,pref:grow,10px", "10px,25px,10px,25px,20px,25px,10px,25px,10px,25px,10px,25px,20px,25px,10px,200px,10px,30px,20px,30px,10px");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    JLabel localJLabel = new JLabel();
    ResourceUtils.setLabelString("EDIT_STOCK_PRODUCT_CODE", localJLabel);
    int i = 2;
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mProductId = new JTextField();
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mProductId, localCellConstraints);
    this._mProductId.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 114) {
          EditStockPanel.this.searchByProductId();
        }
      }
    });
    JBSearchButton localJBSearchButton = new JBSearchButton(false);
    localCellConstraints.xywh(6, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJBSearchButton, localCellConstraints);
    localJBSearchButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        EditStockPanel.this.searchByProductId();
      }
    });
    i += 2;
    localJLabel = new JLabel();
    ResourceUtils.setLabelString("EDIT_STOCK_PRODUCT_NAME", localJLabel);
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mProductName = new JTextField();
    localCellConstraints.xywh(4, i, 6, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mProductName, localCellConstraints);
    this._mProductName.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 114) {
          EditStockPanel.this.searchByProductName();
        }
      }
    });
    localJBSearchButton = new JBSearchButton(false);
    localCellConstraints.xywh(11, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJBSearchButton, localCellConstraints);
    localJBSearchButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        EditStockPanel.this.searchByProductName();
      }
    });
    i += 2;
    localCellConstraints.xywh(2, i, 11, 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(new JTextSeparator(ResourceUtils.getString("EDIT_STOCK_PRODUCT_ST_ST")), localCellConstraints);
    i += 2;
    localJLabel = new JLabel();
    ResourceUtils.setLabelString("EDIT_STOCK_STORE", localJLabel);
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mStores = new JComboBox();
    localCellConstraints.xywh(4, i, 6, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mStores, localCellConstraints);
    this._mStores.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        StoreInfoRow localStoreInfoRow = (StoreInfoRow)EditStockPanel.this._mStores.getSelectedItem();
        if (localStoreInfoRow == null) {
          return;
        }
        int i = localStoreInfoRow.getStoreId();
        if (EditStockPanel.this._mStocks == null)
        {
          EditStockPanel.this.setStockproduct(null);
          return;
        }
        Object localObject = null;
        Iterator localIterator = EditStockPanel.this._mStocks.iterator();
        while (localIterator.hasNext())
        {
          StockAndProductRow localStockAndProductRow = (StockAndProductRow)localIterator.next();
          if (localStockAndProductRow.getStoreId() == i) {
            localObject = localStockAndProductRow;
          }
        }
        EditStockPanel.this.setStockproduct(localObject);
      }
    });
    i += 2;
    localJLabel = new JLabel();
    ResourceUtils.setLabelString("EDIT_STOCK_PP", localJLabel);
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mPurchasePrice = new JTextField();
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mPurchasePrice, localCellConstraints);
    this._mPurchasePrice.setHorizontalAlignment(4);
    localJLabel = new JLabel(CommonConfig.getInstance().country.currency);
    localCellConstraints.xywh(6, i, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    JButton localJButton = new JButton();
    ResourceUtils.setButtonString("SAVE_BTN", localJButton);
    localCellConstraints.xywh(8, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJButton, localCellConstraints);
    localJButton.setToolTipText(ResourceUtils.getString("EDIT_STOCK_SAVE_TOOLTIP"));
    this._mPriceSaveButton = localJButton;
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (EditStockPanel.this._mCurrentStoreStock == null) {
          return;
        }
        String str = EditStockPanel.this._mPurchasePrice.getText().trim();
        if (str.length() == 0)
        {
          UICommon.showError(MessageResourceUtils.getString("EDIT_STOCK_PP_EMPTY"), MessageResourceUtils.getString("ERROR_TITLE"), MainWindow.instance);
          EditStockPanel.this._mPurchasePrice.requestFocusInWindow();
          return;
        }
        if (!Validation.isValidAmount(str))
        {
          UICommon.showError(MessageResourceUtils.getString("EDIT_STOCK_PP_INVALID"), MessageResourceUtils.getString("ERROR_TITLE"), MainWindow.instance);
          EditStockPanel.this._mPurchasePrice.requestFocusInWindow();
          return;
        }
        Double localDouble = Double.valueOf(Double.valueOf(str).doubleValue());
        DBConnection localDBConnection = Db.getConnection();
        try
        {
          Object[] arrayOfObject = { Integer.valueOf(EditStockPanel.this._mCurrentStoreStock.getStoreId()), Integer.valueOf(EditStockPanel.this._mCurrentStoreStock.getProdIndex()) };
          StoreStockRow localStoreStockRow = (StoreStockRow)StoreStockTableDef.getInstance().findRowByIndexForUpdate(arrayOfObject);
          localStoreStockRow.setPurchasePrice(localDouble.doubleValue());
          localDBConnection.openTrans();
          localStoreStockRow.update(true);
          localDBConnection.endTrans();
          UICommon.showMessage(MessageResourceUtils.getString("EDIT_STOCK_PP_SUCCESS"), MessageResourceUtils.getString("SUCCESS_TITLE"), MainWindow.instance);
        }
        catch (DBException localDBException)
        {
          localDBException.printStackTrace();
          localDBConnection.rollbackNoExp();
          UICommon.showInternalError(MessageResourceUtils.getString("EDIT_STOCK_ST_INTERNAL_ERROR"));
          return;
        }
      }
    });
    i += 2;
    localJLabel = new JLabel();
    ResourceUtils.setLabelString("EDIT_STOCK_PRODUCT_ST_ST_LBL", localJLabel);
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mStockInStore = new JTextField();
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mStockInStore, localCellConstraints);
    this._mStockInStore.setHorizontalAlignment(4);
    localJButton = new JButton();
    ResourceUtils.setButtonString("SAVE_BTN", localJButton);
    localCellConstraints.xywh(8, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJButton, localCellConstraints);
    localJButton.setToolTipText(ResourceUtils.getString("EDIT_STOCK_ST_ST_QTY"));
    this._mQtySaveButton = localJButton;
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (EditStockPanel.this._mCurrentStoreStock == null) {
          return;
        }
        String str = EditStockPanel.this._mStockInStore.getText().trim();
        if (str.length() == 0)
        {
          UICommon.showError(MessageResourceUtils.getString("EDIT_STOCK_QTY_EMPTY"), MessageResourceUtils.getString("ERROR_TITLE"), MainWindow.instance);
          EditStockPanel.this._mStockInStore.requestFocusInWindow();
          return;
        }
        if ((str.contains(".")) && (!InternalQuantity.isUnitFractionAllowed(EditStockPanel.this._mCurrentStoreStock.getProdUnit())))
        {
          UICommon.showError(MessageResourceUtils.getString("EDIT_STOCK_DECIMAL_ERROR"), MessageResourceUtils.getString("ERROR_TITLE"), MainWindow.instance);
          EditStockPanel.this._mStockInStore.requestFocusInWindow();
          return;
        }
        if (!Validation.isValidFloat(str, 4, false))
        {
          UICommon.showError(MessageResourceUtils.getString("EDIT_STOCK_INVALID_STV"), MessageResourceUtils.getString("ERROR_TITLE"), MainWindow.instance);
          EditStockPanel.this._mStockInStore.requestFocusInWindow();
          return;
        }
        Double localDouble = Double.valueOf(Double.valueOf(str).doubleValue());
        DBConnection localDBConnection = Db.getConnection();
        try
        {
          Object[] arrayOfObject = { Integer.valueOf(EditStockPanel.this._mCurrentStoreStock.getStoreId()), Integer.valueOf(EditStockPanel.this._mCurrentStoreStock.getProdIndex()) };
          StoreStockRow localStoreStockRow = (StoreStockRow)StoreStockTableDef.getInstance().findRowByIndexForUpdate(arrayOfObject);
          localStoreStockRow.setStock(localDouble.doubleValue());
          localDBConnection.openTrans();
          localStoreStockRow.update(true);
          localDBConnection.endTrans();
          UICommon.showResSuccessMessage("EDIT_STOCK_ST_SUCCESS.", MainWindow.instance);
        }
        catch (DBException localDBException)
        {
          localDBException.printStackTrace();
          localDBConnection.rollbackNoExp();
          UICommon.showInternalError(MessageResourceUtils.getString("EDIT_STOCK_ST_ST_INTERNAL_ERROR"));
          return;
        }
      }
    });
    this._mStoreUnit = new JLabel();
    ResourceUtils.setLabelString("EDIT_STOCK_UNITS", this._mStoreUnit);
    localCellConstraints.xywh(6, i, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    add(this._mStoreUnit, localCellConstraints);
    i += 2;
    localCellConstraints.xywh(2, i, 11, 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(new JTextSeparator(ResourceUtils.getString("EDIT_STOCK_WH_ST")), localCellConstraints);
    i += 2;
    this._mModel = new WarehouseStockModel(null);
    this._mWhStock = new JTable(this._mModel);
    localCellConstraints.xywh(2, i, 11, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(new JScrollPane(this._mWhStock), localCellConstraints);
    this._mWhStock.setRowHeight(22);
    i += 2;
    localCellConstraints.xywh(2, i, 11, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(getWhSavebuttonPanel(), localCellConstraints);
    i++;
    localCellConstraints.xywh(1, i, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(new JSeparator(), localCellConstraints);
    i++;
    localCellConstraints.xywh(1, i, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getButtonPanel(), localCellConstraints);
    this._mwhs = new JComboBox();
    this._mWhStock.getColumnModel().getColumn(1);
    this._mWhStock.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(this._mwhs));
    this._mRenderer = new MyComboBoxRenderer();
    this._mWhStock.getColumnModel().getColumn(1).setCellRenderer(this._mRenderer);
  }
  
  private JPanel getWhSavebuttonPanel()
  {
    JPanel localJPanel = new JPanel();
    localJPanel.setLayout(new FormLayout("80px,10px,80px", "25px"));
    CellConstraints localCellConstraints = new CellConstraints();
    JButton localJButton = new JButton();
    ResourceUtils.setButtonString("SAVE_BTN", localJButton);
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJButton, localCellConstraints);
    localJButton.setToolTipText(ResourceUtils.getString("EDIT_STOCK_WH_ST"));
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        EditStockPanel.this.tableSaveClicked();
      }
    });
    localJButton = new JButton();
    ResourceUtils.setButtonString("RESET_BTN", localJButton);
    localCellConstraints.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJButton, localCellConstraints);
    localJButton.setToolTipText(ResourceUtils.getString("EDIT_STOCK_WH_RESET_ST_TOOLTIP"));
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        EditStockPanel.this.resetClicked();
      }
    });
    return localJPanel;
  }
  
  private void tableSaveClicked()
  {
    int i = this._mModel.getRowCount();
    ArrayList localArrayList = new ArrayList();
    Object localObject1;
    Object localObject2;
    for (int j = 0; j < i; j++)
    {
      localObject1 = (CurrentStockRow)this._mModel.getValueAt(j, 0);
      localObject2 = new BigDecimal(((CurrentStockRow)localObject1).getQuantity());
      BigDecimal localBigDecimal = new BigDecimal(((Double)this._mModel.getValueAt(1, 2)).doubleValue());
      int k = 0;
      if (((BigDecimal)localObject2).compareTo(localBigDecimal) != 0)
      {
        k = 1;
        ((CurrentStockRow)localObject1).setQuantity(localBigDecimal.doubleValue());
      }
      WearehouseInfoRow localWearehouseInfoRow = (WearehouseInfoRow)this._mModel.getValueAt(j, 1);
      if (localWearehouseInfoRow.getWearehouseId() != ((CurrentStockRow)localObject1).getWearHouseIndex())
      {
        k = 1;
        ((CurrentStockRow)localObject1).setWearHouseIndex(localWearehouseInfoRow.getWearehouseId());
        ((CurrentStockRow)localObject1).setWeareHouseInfo(localWearehouseInfoRow);
      }
      if (k != 0) {
        localArrayList.add(localObject1);
      }
    }
    if (localArrayList.size() > 0)
    {
      DBConnection localDBConnection = Db.getConnection();
      try
      {
        localDBConnection.openTrans();
        localObject1 = localArrayList.iterator();
        while (((Iterator)localObject1).hasNext())
        {
          localObject2 = (CurrentStockRow)((Iterator)localObject1).next();
          ((CurrentStockRow)localObject2).update(true);
        }
        localDBConnection.endTrans();
      }
      catch (DBException localDBException)
      {
        localDBConnection.rollbackNoExp();
        UICommon.showInternalError(MessageResourceUtils.getString("EDIT_STOCK_WH_ST_ST_INTERNAL_ERROR"));
        return;
      }
      UICommon.showResSuccessMessage(MessageResourceUtils.getString("EDIT_STOCK_WH_ST_ST_SUCCESS"), MainWindow.instance);
    }
    else
    {
      UICommon.showError(MessageResourceUtils.getString("EDIT_STOCK_WH_ST_ST_NO_CHANGE"), MessageResourceUtils.getString("EDIT_STOCK_WH_ST_ST_NO_CHANGE_TITLE"), MainWindow.instance);
      return;
    }
  }
  
  private JPanel getButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    localJPanel.setLayout(new FormLayout("10px,100px,pref:grow,100px,10px", "30px"));
    CellConstraints localCellConstraints = new CellConstraints();
    Object localObject = new JButton();
    ResourceUtils.setButtonString("CLOSE_BTN", (JButton)localObject);
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        EditStockPanel.this.closeWindow();
      }
    });
    localObject = new HelpButton("ISP_EDIT_STOCK");
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    return localJPanel;
  }
  
  private void searchByProductId()
  {
    String str = this._mProductId.getText().trim();
    Object localObject;
    if (str.length() == 0)
    {
      localObject = new ProductSearchPanel(MainWindow.instance);
      ((ProductSearchPanel)localObject).setDefaultFocus();
      ((ProductSearchPanel)localObject).setSingleSelectionMode(true);
      ((ProductSearchPanel)localObject).setVisible(true);
      if (((ProductSearchPanel)localObject).isCancelled())
      {
        this._mProductId.requestFocusInWindow();
        return;
      }
      ProductRow[] arrayOfProductRow = ((ProductSearchPanel)localObject).getSelectedProducts();
      if ((arrayOfProductRow == null) || (arrayOfProductRow.length == 0))
      {
        this._mProductId.requestFocusInWindow();
        return;
      }
      this._mProductName.setText(arrayOfProductRow[0].getProdName());
      this._mProductId.setText(arrayOfProductRow[0].getProductCode());
      try
      {
        ArrayList localArrayList = StockAndProductTableDef.getInstance().getAllStocksForProduct(arrayOfProductRow[0].getProdIndex());
        if (localArrayList == null)
        {
          UICommon.showError(MessageResourceUtils.getString("EDIT_STOCK_WH_NO_STOCK"), MessageResourceUtils.getString("EDIT_STOCK_WH_NO_STOCK_TITLE"), MainWindow.instance);
          this._mProductId.requestFocusInWindow();
          return;
        }
        setStocks(localArrayList);
      }
      catch (DBException localDBException2)
      {
        localDBException2.printStackTrace();
        UICommon.showInternalError(MessageResourceUtils.getString("EDIT_STOCK_INTERNAL_ERROR_CURRENT_STOCK"));
        this._mProductId.requestFocusInWindow();
        return;
      }
      return;
    }
    try
    {
      localObject = StockAndProductTableDef.getInstance().getAllStocksForProduct(str);
      if (localObject == null)
      {
        UICommon.showError(MessageResourceUtils.getString("EDIT_STOCK_NO_PRODUCT"), MessageResourceUtils.getString("EDIT_STOCK_NO_PRODUCT_TITLE"), MainWindow.instance);
        this._mProductId.requestFocusInWindow();
        return;
      }
      this._mProductName.setText(((StockAndProductRow)((ArrayList)localObject).get(0)).getProdName());
      this._mProductId.setText(((StockAndProductRow)((ArrayList)localObject).get(0)).getProductCode());
      setStocks((ArrayList)localObject);
    }
    catch (DBException localDBException1)
    {
      localDBException1.printStackTrace();
      UICommon.showInternalError(MessageResourceUtils.getString("EDIT_STOCK_INTERNAL_ERROR_CURRENT_STOCK"));
      this._mProductId.requestFocusInWindow();
      return;
    }
  }
  
  private void searchByProductName()
  {
    String str = this._mProductName.getText().trim();
    ProductRow localProductRow = null;
    Object localObject;
    if (str.length() == 0)
    {
      localObject = new ProductSearchPanel(MainWindow.instance);
      ((ProductSearchPanel)localObject).setDefaultFocus();
      ((ProductSearchPanel)localObject).setSingleSelectionMode(true);
      ((ProductSearchPanel)localObject).setVisible(true);
      if (((ProductSearchPanel)localObject).isCancelled())
      {
        this._mProductName.requestFocusInWindow();
        return;
      }
      ProductRow[] arrayOfProductRow = ((ProductSearchPanel)localObject).getSelectedProducts();
      if ((arrayOfProductRow == null) || (arrayOfProductRow.length == 0))
      {
        this._mProductName.requestFocusInWindow();
        return;
      }
      localProductRow = arrayOfProductRow[0];
    }
    else
    {
      if (!str.endsWith("%")) {
        str = str + "%";
      }
      localObject = new ProductSearchPanel(MainWindow.instance);
      ((ProductSearchPanel)localObject).setSingleSelectionMode(true);
      localProductRow = ((ProductSearchPanel)localObject).searchProduct(str, "", "", MainWindow.instance);
      if (localProductRow == null)
      {
        this._mProductName.requestFocusInWindow();
        return;
      }
    }
    this._mProductName.setText(localProductRow.getProdName());
    this._mProductId.setText(localProductRow.getProductCode());
    try
    {
      localObject = StockAndProductTableDef.getInstance().getAllStocksForProduct(localProductRow.getProdIndex());
      if (localObject == null)
      {
        this._mProductName.requestFocusInWindow();
        UICommon.showInternalError(MessageResourceUtils.getString("EDIT_STOCK_INTERNAL_ERROR_CURRENT_STOCK"));
        return;
      }
      setStocks((ArrayList)localObject);
    }
    catch (DBException localDBException)
    {
      localDBException.printStackTrace();
      this._mProductName.requestFocusInWindow();
      UICommon.showInternalError(MessageResourceUtils.getString("EDIT_STOCK_INTERNAL_ERROR_CURRENT_STOCK"));
      return;
    }
  }
  
  public void setStocks(ArrayList<StockAndProductRow> paramArrayList)
    throws DBException
  {
    this._mStocks = paramArrayList;
    this._mModel.removeAllItems();
    if (paramArrayList == null)
    {
      this._mProductName.setText("");
      this._mProductId.setText("");
      this._mPurchasePrice.setText("");
      this._mStockInStore.setText("");
      this._mStoreUnit.setText("");
      this._mModel.removeAllItems();
      this._mPriceSaveButton.setEnabled(false);
      this._mQtySaveButton.setEnabled(false);
      return;
    }
    StoreInfoRow localStoreInfoRow = (StoreInfoRow)this._mStores.getSelectedItem();
    int i = localStoreInfoRow.getStoreId();
    Object localObject1 = null;
    Object localObject2 = this._mStocks.iterator();
    while (((Iterator)localObject2).hasNext())
    {
      localObject3 = (StockAndProductRow)((Iterator)localObject2).next();
      if (((StockAndProductRow)localObject3).getStoreId() == i) {
        localObject1 = localObject3;
      }
    }
    setStockproduct(localObject1);
    localObject2 = (StockAndProductRow)this._mStocks.get(0);
    Object localObject3 = CurrentStockTableDef.getInstance().getCurrentStockListForProduct(((StockAndProductRow)localObject2).getProdIndex());
    this._mCurrentStocks = ((ArrayList)localObject3);
    if (localObject3 != null)
    {
      Iterator localIterator = ((ArrayList)localObject3).iterator();
      while (localIterator.hasNext())
      {
        CurrentStockRow localCurrentStockRow = (CurrentStockRow)localIterator.next();
        this._mModel.addCurrentStockRow(localCurrentStockRow);
      }
    }
  }
  
  private void setStockproduct(StockAndProductRow paramStockAndProductRow)
  {
    this._mCurrentStoreStock = paramStockAndProductRow;
    if (paramStockAndProductRow == null)
    {
      this._mPurchasePrice.setText("");
      this._mStockInStore.setText("");
      this._mStoreUnit.setText("");
      this._mPriceSaveButton.setEnabled(false);
      this._mQtySaveButton.setEnabled(false);
      return;
    }
    this._mPriceSaveButton.setEnabled(true);
    this._mQtySaveButton.setEnabled(true);
    this._mPurchasePrice.setText(InternalAmount.toString(paramStockAndProductRow.getPurchasePrice()));
    this._mStockInStore.setText(InternalQuantity.toString(paramStockAndProductRow.getStock(), (short)paramStockAndProductRow.getProdUnit()));
    this._mStoreUnit.setText(InternalQuantity.quantityUnits[paramStockAndProductRow.getProdUnit()]);
  }
  
  private void resetClicked()
  {
    try
    {
      if (this._mCurrentStocks != null)
      {
        this._mModel.removeAllItems();
        Iterator localIterator = this._mCurrentStocks.iterator();
        while (localIterator.hasNext())
        {
          CurrentStockRow localCurrentStockRow = (CurrentStockRow)localIterator.next();
          this._mModel.addCurrentStockRow(localCurrentStockRow);
        }
      }
    }
    catch (DBException localDBException)
    {
      UICommon.showInternalError(MessageResourceUtils.getString("EDIT_STOCK_WH_INTERNAL_ERROR_UPD"));
      return;
    }
  }
  
  public class MyComboBoxRenderer
    extends JComboBox
    implements TableCellRenderer
  {
    public MyComboBoxRenderer() {}
    
    public Component getTableCellRendererComponent(JTable paramJTable, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2)
    {
      if (paramBoolean1)
      {
        setForeground(paramJTable.getSelectionForeground());
        super.setBackground(paramJTable.getSelectionBackground());
      }
      else
      {
        setForeground(paramJTable.getForeground());
        setBackground(paramJTable.getBackground());
      }
      setSelectedItem(paramObject);
      return this;
    }
  }
  
  private class WarehouseStockModel
    extends DefaultTableModel
  {
    String[] colNames = { ResourceUtils.getString("EDIT_STOCK_COL_ST_ID"), ResourceUtils.getString("EDIT_STOCK_COL_WH"), ResourceUtils.getString("EDIT_STOCK_COL_QTY") };
    Class dataClass = Object.class;
    
    private WarehouseStockModel() {}
    
    public int getColumnCount()
    {
      return this.colNames.length;
    }
    
    public String getColumnName(int paramInt)
    {
      return this.colNames[paramInt];
    }
    
    public boolean isCellEditable(int paramInt1, int paramInt2)
    {
      return paramInt2 != 0;
    }
    
    public Class getColumnClass(int paramInt)
    {
      if (paramInt == 2) {
        return this.dataClass;
      }
      if (paramInt == 0) {
        return Integer.class;
      }
      return Object.class;
    }
    
    public void addCurrentStockRow(CurrentStockRow paramCurrentStockRow)
      throws DBException
    {
      Object localObject = null;
      if (paramCurrentStockRow.getQuantityUnit() == 2)
      {
        localObject = new Integer(new Double(paramCurrentStockRow.getQuantity()).intValue());
        this.dataClass = Integer.class;
      }
      else
      {
        localObject = new Double(paramCurrentStockRow.getQuantity());
        this.dataClass = Double.class;
      }
      Object[] arrayOfObject = { paramCurrentStockRow, paramCurrentStockRow.getWeareHouseInfo(), localObject };
      addRow(arrayOfObject);
    }
    
    public void removeAllItems()
    {
      int i = getRowCount();
      for (int j = 0; j < i; j++) {
        removeRow(0);
      }
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.inv.EditStockPanel
 * JD-Core Version:    0.7.0.1
 */