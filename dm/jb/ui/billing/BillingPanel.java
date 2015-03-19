package dm.jb.ui.billing;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.db.objects.CustomerRow;
import dm.jb.db.objects.Payment;
import dm.jb.db.objects.StockAndProductRow;
import dm.jb.db.objects.StockAndProductTableDef;
import dm.jb.db.objects.StoreInfoRow;
import dm.jb.db.objects.StoreInfoTableDef;
import dm.jb.ext.RFIDReadListener;
import dm.jb.ext.RFIDReader;
import dm.jb.messages.JbMessageLoader;
import dm.jb.messages.ProductMessages_base;
import dm.jb.op.bill.Bill;
import dm.jb.op.bill.BillProduct;
import dm.jb.printing.JePrinterException;
import dm.jb.printing.Print;
import dm.jb.ui.StringValueImpl;
import dm.jb.ui.common.JBNumTextFieldWithKeyPad;
import dm.jb.ui.common.JBShadowPanel;
import dm.jb.ui.common.NumericVirtualKeyPad;
import dm.tools.db.DBConnection;
import dm.tools.db.DBException;
import dm.tools.db.Db;
import dm.tools.messages.ButtonLabels_base;
import dm.tools.types.InternalQuantity;
import dm.tools.ui.NonEditableJXTable;
import dm.tools.ui.UICommon;
import dm.tools.ui.components.AutoSearchTextField;
import dm.tools.ui.components.JBDBUIAmountTextField;
import dm.tools.utils.CommonConfig;
import dm.tools.utils.CommonConfigChangeListener;
import dm.tools.utils.MyFormatter;
import dm.tools.utils.Validation;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.table.ColumnFactory;
import org.jdesktop.swingx.table.TableColumnExt;

public class BillingPanel
  extends JXPanel
{
  private static BillingPanel _mInstance = null;
  private JBNumTextFieldWithKeyPad _mProductId = null;
  private JButton _mLoadButton = null;
  private JButton _mSearchButton = null;
  private JButton _mClearButton = null;
  private JTextField _mQuantity = null;
  private AutoSearchTextField _mProductName = null;
  private JLabel _mTotalAmount = null;
  private JLabel _mTotalTax = null;
  private JBDBUIAmountTextField _mFinalDiscount = null;
  private JLabel _mFinalAmount = null;
  private JLabel _mQtyUnitLbl = null;
  private BillTableModel _mModel = null;
  private NonEditableJXTable _mBillTable = null;
  private StockAndProductRow _mCurrentProduct = null;
  private boolean _mEditMode = false;
  private boolean _mBillUpdateMode = false;
  private BillProduct _mSelectedBillProduct = null;
  private JButton _mDeleteButton = null;
  private JButton _mBillDeleteButton = null;
  private Bill _mBill = new Bill();
  private int _mSavedMode = 0;
  private double _mTotalRefund = 0.0D;
  
  public static BillingPanel getBillingPanel()
  {
    if (_mInstance == null) {
      _mInstance = new BillingPanel();
    }
    return _mInstance;
  }
  
  private BillingPanel()
  {
    initUI();
    InputMap localInputMap = getInputMap(2);
    String str = "F12Action";
    localInputMap.put(KeyStroke.getKeyStroke("F12"), str);
    Object localObject = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        BillingPanel.this.okClicked();
      }
    };
    getActionMap().put(str, (Action)localObject);
    localInputMap = getInputMap(2);
    str = "F4Action";
    localInputMap.put(KeyStroke.getKeyStroke("F4"), str);
    localObject = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (!BillingPanel.this._mBillUpdateMode) {
          BillingPanel.this._mProductId.requestFocusInWindow();
        } else {
          BillingPanel.this._mQuantity.requestFocusInWindow();
        }
      }
    };
    getActionMap().put(str, (Action)localObject);
    str = "F5Action";
    localInputMap.put(KeyStroke.getKeyStroke("F5"), str);
    localObject = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (BillingPanel.this._mBillUpdateMode) {
          return;
        }
        BillingPanel.this.productNameLoadClicked();
      }
    };
    getActionMap().put(str, (Action)localObject);
    str = "ESCAction";
    localInputMap.put(KeyStroke.getKeyStroke("ESCAPE"), str);
    localObject = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        BillingPanel.this.clearProdPanel();
      }
    };
    getActionMap().put(str, (Action)localObject);
    str = "F6Action";
    localInputMap.put(KeyStroke.getKeyStroke("F6"), str);
    localObject = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        BillingPanel.this._mBillTable.requestFocusInWindow();
      }
    };
    getActionMap().put(str, (Action)localObject);
    str = "F11Action";
    localInputMap.put(KeyStroke.getKeyStroke("F11"), str);
    localObject = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (!BillingPanel.this._mBillUpdateMode) {
          return;
        }
        BillingPanel.this.cancelBill();
      }
    };
    getActionMap().put(str, (Action)localObject);
    RFIDReader localRFIDReader = RFIDReader._mInstance;
    localRFIDReader.addReadListener(new RFIDReadListener()
    {
      public void dataRead(String paramAnonymousString)
      {
        if (!BillingPanel.this.isVisible()) {
          return;
        }
        if ((StockCheckWindow.INSTANCE.isVisible()) || (ViewBillNoDialog.getInstance().isVisible())) {
          return;
        }
        if ((PaymentDialog.getInstance().isVisible()) || (CustomerInfoDialog.getInstance().isVisible())) {
          return;
        }
        BillingPanel.this.searchAndAddByRFID(paramAnonymousString);
      }
    });
    addMouseListener(new MouseListener()
    {
      public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
      {
        NumericVirtualKeyPad.getInstance().setVisible(false);
      }
      
      public void mousePressed(MouseEvent paramAnonymousMouseEvent) {}
      
      public void mouseReleased(MouseEvent paramAnonymousMouseEvent) {}
      
      public void mouseEntered(MouseEvent paramAnonymousMouseEvent) {}
      
      public void mouseExited(MouseEvent paramAnonymousMouseEvent) {}
    });
  }
  
  public Bill getCurrentBill()
  {
    return this._mBill;
  }
  
  public void setDefaultFocus()
  {
    this._mProductId.requestFocusInWindow();
    this._mDeleteButton.setEnabled(false);
  }
  
  public JPanel getPanel()
  {
    return this;
  }
  
  public void reloadWindow() {}
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("10px,pref,40px, pref:grow,30px,10px", "pref,pref:grow, pref, 30px, 10px");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    int i = 1;
    localCellConstraints.xywh(1, i, 2, 1, CellConstraints.LEFT, CellConstraints.FILL);
    add(getProductPanel(), localCellConstraints);
    localCellConstraints.xywh(4, i, 2, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(getOpButtonPanel(), localCellConstraints);
    i++;
    localCellConstraints.xywh(1, i, 5, 1, CellConstraints.FILL, CellConstraints.FILL);
    TablePanel localTablePanel = new TablePanel();
    add(localTablePanel, localCellConstraints);
    i += 1;
    localCellConstraints.xywh(1, i, 6, 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(getTotalPanel(), localCellConstraints);
    i += 1;
    localCellConstraints.xywh(1, i, 6, 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(getButtonPanel(), localCellConstraints);
    CommonConfig.getInstance().addChangeListener(new CommonConfigChangeListener()
    {
      public void setConfigValues()
      {
        BillingPanel.this.configValuesChanged();
      }
    });
    setColumnWidths();
  }
  
  private JPanel getProductPanel()
  {
    return new ProductPanel();
  }
  
  private JPanel getOpButtonPanel()
  {
    return new AddDeleteButtonPanel();
  }
  
  private JPanel getTotalPanel()
  {
    return new TotalPanel();
  }
  
  private JPanel getButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("10px, 100px, 30px,100px, pref:grow, 100px, 10px", "30px");
    localJPanel.setLayout(localFormLayout);
    localJPanel.setBackground(getBackground());
    CellConstraints localCellConstraints = new CellConstraints();
    JbMessageLoader localJbMessageLoader = (JbMessageLoader)UICommon.getMessageLoader();
    ButtonLabels_base localButtonLabels_base = localJbMessageLoader.getButtonLabelsMessages();
    JXButton localJXButton = new JXButton(localButtonLabels_base.getMessage(135175) + " [F12]");
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.setBackground(localJPanel.getBackground());
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        BillingPanel.this.okClicked();
      }
    });
    localJXButton = new JXButton("Delete  [F11]");
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.setBackground(localJPanel.getBackground());
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        BillingPanel.this.cancelBill();
      }
    });
    this._mBillDeleteButton = localJXButton;
    this._mBillDeleteButton.setEnabled(false);
    localJXButton = new JXButton(" Reset ");
    localCellConstraints.xywh(6, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.setBackground(localJPanel.getBackground());
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        BillingPanel.this.resetClicked();
      }
    });
    return localJPanel;
  }
  
  public void cancelBill()
  {
    UpdateTypeDialog.getInstance().setNoRefundMode(true);
    UpdateTypeDialog.getInstance().setVisible(true);
    if (UpdateTypeDialog.getInstance().isCancelled()) {
      return;
    }
    DBConnection localDBConnection = Db.getConnection();
    try
    {
      localDBConnection.openTrans();
      int i = UpdateTypeDialog.getInstance().getUpdateMode();
      this._mBill.deleteFromDB(UpdateTypeDialog.getInstance().getUpdateMode() == 1, i);
      localDBConnection.endTrans();
    }
    catch (DBException localDBException)
    {
      localDBConnection.rollbackNoExp();
      UICommon.showError("Internal Error deleting the bill.", "Internal Error", BillingLauncher.getInstance());
      return;
    }
    this._mBill.clear();
    prepareForNewBill();
    UICommon.showMessage("The bill deleted successfully.", "Error", BillingLauncher.getInstance());
  }
  
  private void setColumnWidths()
  {
    ArrayList localArrayList = ((BillTableModel)this._mBillTable.getModel())._mColumns;
    Iterator localIterator = localArrayList.iterator();
    int i = 0;
    TableColumnModel localTableColumnModel = this._mBillTable.getColumnModel();
    while (localIterator.hasNext())
    {
      BillingColumn localBillingColumn = (BillingColumn)localIterator.next();
      TableColumn localTableColumn = localTableColumnModel.getColumn(i);
      localTableColumn.setCellRenderer(new TestRenderer());
      localTableColumn.setPreferredWidth(localBillingColumn._mSize);
      localTableColumn.setHeaderRenderer(new TableCellRenderer()
      {
        public Component getTableCellRendererComponent(JTable paramAnonymousJTable, Object paramAnonymousObject, boolean paramAnonymousBoolean1, boolean paramAnonymousBoolean2, int paramAnonymousInt1, int paramAnonymousInt2)
        {
          JTableHeader localJTableHeader = BillingPanel.this._mBillTable.getTableHeader();
          TableCellRenderer localTableCellRenderer = localJTableHeader.getDefaultRenderer();
          Component localComponent = localTableCellRenderer.getTableCellRendererComponent(paramAnonymousJTable, paramAnonymousObject, paramAnonymousBoolean1, paramAnonymousBoolean2, paramAnonymousInt1, paramAnonymousInt2);
          return localComponent;
        }
      });
      i++;
    }
  }
  
  private void clearProdPanel()
  {
    this._mProductId.setText("");
    this._mQuantity.setText("");
    this._mCurrentProduct = null;
    this._mQtyUnitLbl.setText("");
    this._mProductName.setText("");
    this._mProductId.requestFocusInWindow();
  }
  
  private void enableProdPanel(boolean paramBoolean)
  {
    this._mProductId.setEditable(paramBoolean);
    this._mQuantity.setEditable(paramBoolean);
    this._mProductName.setEditable(paramBoolean);
    this._mLoadButton.setEnabled(paramBoolean);
    this._mSearchButton.setEnabled(paramBoolean);
  }
  
  private void okClicked()
  {
    if (this._mBill.getEntriesCount() == 0)
    {
      UICommon.showError("No product has been added to bill.", "Error", BillingLauncher.getInstance());
      setDefaultFocus();
      return;
    }
    if (this._mBillUpdateMode) {
      openCustomer();
    } else {
      openCustomer();
    }
  }
  
  private void configValuesChanged()
  {
    remove(this._mBillTable);
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(2, 3, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    this._mBillTable = new NonEditableJXTable(new BillTableModel());
    this._mBillTable.setShowGrid(false);
    this._mBillTable.setShowVerticalLines(true);
    this._mBillTable.setGridColor(Color.BLACK);
    JScrollPane localJScrollPane = new JScrollPane(this._mBillTable);
    add(localJScrollPane, localCellConstraints);
    setColumnWidths();
  }
  
  private void productNameLoadClicked()
  {
    BillingProductSearchDialog localBillingProductSearchDialog = BillingProductSearchDialog.getInstance();
    localBillingProductSearchDialog.setProdName(this._mProductName.getText().trim() + "%");
    int i = StoreInfoTableDef.getCurrentStore().getStoreId();
    StockAndProductRow localStockAndProductRow = localBillingProductSearchDialog.searchAndShow(this._mProductName.getText().trim() + "%", i);
    if (localStockAndProductRow == null) {
      return;
    }
    this._mProductId.setText("" + localStockAndProductRow.getProductCode());
    this._mCurrentProduct = localStockAndProductRow;
    setProductData(localStockAndProductRow);
  }
  
  public void productListSelectClickedOneElement(StockAndProductRow paramStockAndProductRow)
  {
    if (paramStockAndProductRow == null) {
      return;
    }
    this._mProductId.setText(paramStockAndProductRow.getProductCode());
    productIdLoadClicked(false);
    this._mQuantity.requestFocusInWindow();
  }
  
  private void searchAndAddByRFID(String paramString)
  {
    try
    {
      StockAndProductRow localStockAndProductRow = StockAndProductTableDef.getInstance().getProductByRfid(paramString);
      if (localStockAndProductRow == null)
      {
        UICommon.showError("Invalid Product Code or no stock available.", "Error", BillingLauncher.getInstance());
        this._mProductId.requestFocusInWindow();
        return;
      }
      this._mCurrentProduct = localStockAndProductRow;
      setProductData(this._mCurrentProduct);
    }
    catch (DBException localDBException)
    {
      UICommon.showError("Internal error reading product details.\nCheck the input values. If the problem persis contact administrator", "Internal error", BillingLauncher.getInstance());
      return;
    }
    this._mQuantity.setText("1");
    addBillProductClicked();
  }
  
  private void productIdLoadClicked(boolean paramBoolean)
  {
    String str = this._mProductId.getText().trim();
    if (str.length() == 0)
    {
      UICommon.showError("Product Code cannot be empty.\nIf you want are looking for search, enter the appropriate wild card(eg., '*'), and press enter.", "Error", BillingLauncher.getInstance());
      this._mProductId.requestFocusInWindow();
      return;
    }
    try
    {
      StockAndProductRow localStockAndProductRow = StockAndProductTableDef.getInstance().getProductByCode(str);
      if (localStockAndProductRow == null)
      {
        UICommon.showError("Invalid Product Code or no stock available.", "Error", BillingLauncher.getInstance());
        this._mProductId.requestFocusInWindow();
        return;
      }
      this._mCurrentProduct = localStockAndProductRow;
      setProductData(this._mCurrentProduct);
    }
    catch (DBException localDBException)
    {
      UICommon.showError("Internal error reading product details.\nCheck the input values. If the problem persis contact administrator", "Internal error", BillingLauncher.getInstance());
      return;
    }
    this._mQuantity.setText("1");
    if (paramBoolean) {
      addBillProductClicked();
    }
  }
  
  private void resetClicked()
  {
    this._mBill.clear();
    prepareForNewBill();
  }
  
  public void prepareForNewBill()
  {
    clearProdPanel();
    this._mBillUpdateMode = false;
    this._mBillTable.clearSelection();
    this._mModel.removeAllRows();
    this._mBillTable.updateUI();
    this._mProductId.requestFocusInWindow();
    this._mSelectedBillProduct = null;
    this._mEditMode = false;
    this._mBill = new Bill();
    this._mFinalDiscount.setText("0.00");
    updateTotalPanel();
    enableProdPanel(true);
    this._mDeleteButton.setEnabled(false);
    this._mProductName.setText("");
    this._mClearButton.setEnabled(true);
    this._mBillDeleteButton.setEnabled(false);
  }
  
  public void loadSavedBill(Bill paramBill)
  {
    this._mSavedMode = 0;
    this._mTotalRefund = 0.0D;
    ArrayList localArrayList = paramBill.getBillEntries();
    Iterator localIterator = localArrayList.iterator();
    this._mEditMode = false;
    while (localIterator.hasNext())
    {
      BillProduct localBillProduct = (BillProduct)localIterator.next();
      this._mModel.addBillProduct(localBillProduct);
    }
    this._mBill = paramBill;
    clearProdPanel();
    this._mProductId.requestFocusInWindow();
    updateTotalPanel();
    enableProdPanel(true);
    if (paramBill != null) {
      this._mBillDeleteButton.setEnabled(true);
    }
  }
  
  public void loadBill(Bill paramBill)
  {
    ArrayList localArrayList = paramBill.getBillEntries();
    Iterator localIterator = localArrayList.iterator();
    this._mEditMode = false;
    while (localIterator.hasNext())
    {
      BillProduct localBillProduct = (BillProduct)localIterator.next();
      this._mModel.addBillProduct(localBillProduct);
    }
    this._mBill = paramBill;
    clearProdPanel();
    this._mProductId.requestFocusInWindow();
    updateTotalPanel();
    if (this._mBill.getStoredFinalAmount() < 0.0D) {
      this._mFinalDiscount.setEditable(false);
    }
    enableProdPanel(false);
    if (paramBill != null) {
      this._mBillDeleteButton.setEnabled(true);
    }
  }
  
  private void addBillProductClicked()
  {
    if (this._mCurrentProduct == null)
    {
      UICommon.showError("Select a product to add.", "Error", BillingLauncher.getInstance());
      this._mProductId.requestFocusInWindow();
      return;
    }
    setProductData(this._mCurrentProduct);
    String str = this._mQuantity.getText();
    if (str.length() == 0)
    {
      UICommon.showError("Quantity is not specified.", "Error", BillingLauncher.getInstance());
      this._mQuantity.requestFocusInWindow();
      return;
    }
    if (!Validation.isValidFloat(str, 8, false))
    {
      UICommon.showError("Invalid quantity.", "Error", BillingLauncher.getInstance());
      this._mQuantity.requestFocusInWindow();
      return;
    }
    if ((str.contains(".")) && (!InternalQuantity.isUnitFractionAllowed(this._mCurrentProduct.getProdUnit())))
    {
      UICommon.showError("Fraction quantity is not alowed for this product.", "Error", BillingLauncher.getInstance());
      this._mQuantity.requestFocusInWindow();
      return;
    }
    double d1 = Double.parseDouble(str);
    if ((this._mBillUpdateMode) && (this._mSelectedBillProduct != null))
    {
      UpdateTypeDialog.getInstance().initialize();
      if (d1 != this._mSelectedBillProduct.getOldQuantity())
      {
        int i = 1;
        double d2 = 0.0D;
        double d3;
        if (d1 > this._mSelectedBillProduct.getOldQuantity())
        {
          UpdateTypeDialog.getInstance().setAddMode(true);
          UpdateTypeDialog.getInstance().setNoRefundMode(true);
        }
        else
        {
          d3 = this._mSelectedBillProduct.getOldQuantity() - d1;
          double d4 = this._mSelectedBillProduct.getFinalAmountForQuantity(d3);
          UpdateTypeDialog.getInstance().setNoRefundMode(false);
          d4 = MyFormatter.roundAmount(d4, CommonConfig.getInstance().roundingOption);
          UpdateTypeDialog.getInstance().setRefundAmount(d4);
          UpdateTypeDialog.getInstance().setAddMode(false);
          i = 0;
        }
        UpdateTypeDialog.getInstance().setPreviousMode(this._mSavedMode);
        UpdateTypeDialog.getInstance().setVisible(true);
        if (UpdateTypeDialog.getInstance().isCancelled()) {
          return;
        }
        if (i != 0)
        {
          d3 = d1 - this._mSelectedBillProduct.getOldQuantity();
          d2 = this._mSelectedBillProduct.getFinalAmountForQuantity(d3);
        }
        else
        {
          d2 = UpdateTypeDialog.getInstance().getRefund();
        }
        d2 = MyFormatter.roundAmount(d2, CommonConfig.getInstance().roundingOption);
        int j = UpdateTypeDialog.getInstance().getUpdateMode();
        this._mSavedMode = j;
        byte b1 = UpdateTypeDialog.getInstance().getRestockMode();
        byte b2 = 1;
        if (this._mEditMode) {
          b2 = d1 != 0.0D ? 3 : 2;
        }
        if (b2 == 3)
        {
          if (d1 < this._mSelectedBillProduct.getOldQuantity())
          {
            this._mTotalRefund += d2;
            this._mSelectedBillProduct.setRefund(1.0D * d2);
          }
        }
        else if (b2 == 2)
        {
          this._mTotalRefund += d2;
          this._mSelectedBillProduct.setRefund(1.0D * d2);
        }
        this._mSelectedBillProduct.setBillChange(j, b1, b2);
        this._mSelectedBillProduct.setRefund(i != 0 ? d2 : d2 * -1.0D);
      }
      else
      {
        this._mSelectedBillProduct.setBillChange(null);
      }
    }
    if (this._mEditMode)
    {
      this._mBill.updateBillProduct(this._mSelectedBillProduct, d1);
      this._mModel.updateProduct(this._mSelectedBillProduct.getIndex() - 1, this._mSelectedBillProduct);
      if (!this._mBillUpdateMode) {
        this._mProductId.requestFocusInWindow();
      }
      updateTotalPanel();
      this._mBillTable.clearSelection();
      this._mBillTable.updateUI();
      return;
    }
    java.util.Date localDate = new java.util.Date();
    java.sql.Date localDate1 = this._mCurrentProduct.getExpiry();
    if (localDate1 != null)
    {
      SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(CommonConfig.getInstance().dateFormat);
      localObject = localSimpleDateFormat.format(localDate1);
      if ((localDate.compareTo(localDate1) > 0) && (this._mCurrentProduct.getExpiry() == null))
      {
        UICommon.showError("The product has been expired on " + (String)localObject, "Error", BillingLauncher.getInstance());
        return;
      }
    }
    boolean bool = this._mBill.addBillProduct(this._mCurrentProduct.getProdIndex(), d1, this._mCurrentProduct, false);
    Object localObject = this._mBill.getBillProductByProductId(this._mCurrentProduct.getProdIndex());
    if (!bool) {
      this._mModel.addBillProduct((BillProduct)localObject);
    }
    clearProdPanel();
    this._mProductId.requestFocusInWindow();
    updateTotalPanel();
    this._mBillTable.updateUI();
  }
  
  private void setProductData(StockAndProductRow paramStockAndProductRow)
  {
    this._mProductName.setText(paramStockAndProductRow.getProdName());
    this._mQtyUnitLbl.setText("  " + InternalQuantity.quantityUnits[paramStockAndProductRow.getProdUnit()]);
    this._mQuantity.requestFocusInWindow();
  }
  
  private void addlDiscountClicked()
  {
    if (!isDiscountValid()) {
      return;
    }
    updateTotalPanel();
  }
  
  private boolean isDiscountValid()
  {
    return true;
  }
  
  private double getFinalTotalWithDiscount()
  {
    double d = this._mBill.getFinalAmount();
    return MyFormatter.roundAmount(d, CommonConfig.getInstance().roundingOption);
  }
  
  private void updateTotalPanel()
  {
    if (!isDiscountValid()) {
      return;
    }
    this._mTotalAmount.setText(MyFormatter.getFormattedAmount(this._mBill.getTotalAmount(), false));
    double d = getFinalTotalWithDiscount();
    this._mFinalAmount.setText(MyFormatter.getFormattedAmount(d, false));
    this._mTotalTax.setText(MyFormatter.getFormattedAmount(this._mBill.getFinalTax(), false));
  }
  
  private void billproductSelected(BillProduct paramBillProduct)
  {
    StockAndProductRow localStockAndProductRow = paramBillProduct.getStockAndProduct();
    this._mCurrentProduct = localStockAndProductRow;
    this._mProductId.setEditable(false);
    this._mProductId.setText(localStockAndProductRow.getProductCode());
    this._mProductName.setText(localStockAndProductRow.getProdName());
    this._mQtyUnitLbl.setText("  " + InternalQuantity.quantityUnits[localStockAndProductRow.getProdUnit()]);
    this._mQuantity.setText(paramBillProduct.getQuantityStringWithoutSpace().toString());
  }
  
  private void openCustomer()
  {
    if (CommonConfig.getInstance().customerOption != 2)
    {
      CustomerInfoDialog localCustomerInfoDialog = CustomerInfoDialog.getInstance();
      localCustomerInfoDialog.setLoyaltyPoints(this._mBill.getTotalLoyaltyPoints(), this._mBillUpdateMode);
      localCustomerInfoDialog.setRedeemablePoints(this._mBill.getRedeemableLoyaltyPoints());
      localCustomerInfoDialog.clearAllFields();
      localCustomerInfoDialog.setUpdate(this._mBillUpdateMode);
      CustomerRow localCustomerRow1 = this._mBill.getCustomer();
      if (this._mBillUpdateMode)
      {
        localCustomerInfoDialog.setOldLoyaltyPoints(this._mBill.getRedeemedPoints());
        localCustomerInfoDialog.setPointsAwarded(this._mBill.getLoyaltyPointsAwardedInDb());
        localCustomerInfoDialog.setCustomer(this._mBill.getCustomer(), this._mBillUpdateMode);
      }
      localCustomerInfoDialog.setVisible(true);
      if (localCustomerInfoDialog.isCancelled()) {
        return;
      }
      CustomerRow localCustomerRow2 = localCustomerInfoDialog.getSelectedRow();
      if ((localCustomerRow1 != null) && (localCustomerRow2.getCustIndex() != localCustomerRow1.getCustIndex())) {
        localCustomerRow1.setLoyalty(localCustomerRow1.getLoyalty() + this._mBill.getLoyaltyPointsInDb());
      }
      double d1 = 0.0D;
      if ((localCustomerInfoDialog.isRedeemOptionSelected()) && (CommonConfig.getInstance().redemptionOption == 0))
      {
        double d2 = CommonConfig.getInstance().redemptionAmount;
        d1 = d2 * localCustomerInfoDialog.getRedeemedPoints();
        this._mBill.setPointsRedeemed(localCustomerInfoDialog.getRedeemedPoints());
        localCustomerRow2.setLoyalty(localCustomerRow2.getLoyalty() - localCustomerInfoDialog.getRedeemedPoints());
      }
      customerPanelOKClicked(localCustomerRow2, d1);
    }
    else
    {
      customerPanelOKClicked(null, 0.0D);
    }
  }
  
  public void customerPanelOKClicked(CustomerRow paramCustomerRow, double paramDouble)
  {
    this._mBill.setCustomerInfo(paramCustomerRow);
    makePayment(paramDouble);
  }
  
  private void makePayment(double paramDouble)
  {
    PaymentDialog.getInstance();
    PaymentPanel localPaymentPanel = PaymentPanel.getInstance();
    localPaymentPanel.setBillUpdateMode(this._mBillUpdateMode);
    localPaymentPanel.setDefaultButton();
    Payment localPayment1 = this._mBill.getPayment();
    if (localPayment1 != null)
    {
      localPaymentPanel.setEditable(true);
      localPaymentPanel.setOldAmount(localPayment1.getTotalAmount());
      localPaymentPanel.setPayment(localPayment1);
    }
    else
    {
      localPaymentPanel.resetDetails();
      localPaymentPanel.clearAllFields();
    }
    localPaymentPanel.setTotalAmount(getFinalTotalWithDiscount() - paramDouble, this._mTotalRefund);
    PaymentDialog.getInstance().pack();
    PaymentDialog.getInstance().setVisible(true);
    if (localPaymentPanel.isCancelled()) {
      return;
    }
    Payment localPayment2 = localPaymentPanel.getPayment();
    this._mBill.setPayment(localPayment2);
    paymentWindowOKClicked();
  }
  
  public void clearAllFields()
  {
    resetClicked();
  }
  
  public void paymentWindowOKClicked()
  {
    this._mBill.setRoundedFinalAmount(getFinalTotalWithDiscount());
    DBConnection localDBConnection = Db.getConnection();
    try
    {
      localDBConnection.openTrans();
      if (this._mBillUpdateMode) {
        this._mBill.updateInDb();
      } else {
        this._mBill.createBillinDB();
      }
      localDBConnection.endTrans();
    }
    catch (DBException localDBException)
    {
      localDBException.printStackTrace();
      localDBConnection.rollbackNoExp();
      UICommon.showError("Internal error creating bill entries.\nTry again later. If the problem persists contact administrator.", "Error", BillingLauncher.getInstance());
      return;
    }
    this._mEditMode = false;
    try
    {
      Print.getInstance().printBill(this._mBill);
    }
    catch (JePrinterException localJePrinterException)
    {
      UICommon.showError("Error printing the bill.\nDetails:\n" + localJePrinterException.toString(), "Printing Error", BillingLauncher.getInstance());
      resetClicked();
      this._mBillTable.updateUI();
      return;
    }
    this._mBillTable.updateUI();
    UICommon.showMessage("Bill entered successfully.\nBill No. : " + this._mBill.getBillNo(), "Success", BillingLauncher.getInstance());
    this._mBill.clear();
    prepareForNewBill();
  }
  
  private void removeSelectedRow()
  {
    int[] arrayOfInt = this._mBillTable.getSelectedRows();
    if (arrayOfInt.length == 0) {
      return;
    }
    int i = this._mBillTable.convertRowIndexToModel(arrayOfInt[0]);
    BillProduct localBillProduct = this._mModel.getBillProductAtIndex(i);
    localBillProduct.recalcInternal();
    if (this._mBillUpdateMode)
    {
      UpdateTypeDialog.getInstance().initialize();
      UpdateTypeDialog.getInstance().setPreviousMode(this._mSavedMode);
      UpdateTypeDialog.getInstance().setNoRefundMode(false);
      double d1 = 0.0D;
      d1 = MyFormatter.roundAmount(localBillProduct.getTotalAmount(), CommonConfig.getInstance().roundingOption);
      UpdateTypeDialog.getInstance().setRefundAmount(d1);
      UpdateTypeDialog.getInstance().setVisible(true);
      if (UpdateTypeDialog.getInstance().isCancelled()) {
        return;
      }
      int j = UpdateTypeDialog.getInstance().getUpdateMode();
      double d2 = UpdateTypeDialog.getInstance().getRefund();
      this._mTotalRefund += d2;
      this._mSavedMode = j;
      byte b1 = UpdateTypeDialog.getInstance().getRestockMode();
      byte b2 = 2;
      localBillProduct.setBillChange(j, b1, b2);
      localBillProduct.setRefund(-1.0D * d2);
      this._mBill.addRowsForDelete(localBillProduct);
    }
    this._mModel.removeRow(arrayOfInt[0]);
    this._mBill.removeBillProduct(localBillProduct);
    updateTotalPanel();
    this._mBillTable.updateUI();
    enableProdPanel(true);
    this._mModel.updateSlNos();
    clearProdPanel();
    this._mBillTable.clearSelection();
    this._mBillTable.requestFocusInWindow();
  }
  
  public void productListSelectClicked(ArrayList<StockAndProductRow> paramArrayList)
  {
    productListSelectClickedOneElement((StockAndProductRow)paramArrayList.get(0));
  }
  
  public void setToUpdateMode(boolean paramBoolean)
  {
    this._mBillUpdateMode = paramBoolean;
    this._mBillTable.requestFocusInWindow();
  }
  
  void saveCurrentBill()
  {
    if (this._mBill.getEntriesCount() != 0) {
      Bill.saveBill(this._mBill);
    }
  }
  
  private class TotalPanel
    extends JBShadowPanel
  {
    public TotalPanel()
    {
      super();
      initUI();
      addMouseListener(new MouseListener()
      {
        public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
        {
          NumericVirtualKeyPad.getInstance().setVisible(false);
        }
        
        public void mousePressed(MouseEvent paramAnonymousMouseEvent) {}
        
        public void mouseReleased(MouseEvent paramAnonymousMouseEvent) {}
        
        public void mouseEntered(MouseEvent paramAnonymousMouseEvent) {}
        
        public void mouseExited(MouseEvent paramAnonymousMouseEvent) {}
      });
    }
    
    private void initUI()
    {
      FormLayout localFormLayout = new FormLayout("14px,10px,100px,0px, 150px,5px,0px, pref:grow,100px,10px, 190px,5px,70px", "24px,35px,10px,35px,24px");
      setLayout(localFormLayout);
      setBackground(getBackground());
      CellConstraints localCellConstraints = new CellConstraints();
      JLabel localJLabel = new JLabel("Subtotal : ");
      Font localFont1 = localJLabel.getFont();
      localFont1 = new Font(localFont1.getName(), 0, 20);
      localJLabel.setFont(localFont1);
      localCellConstraints.xywh(3, 4, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
      add(localJLabel, localCellConstraints);
      BillingPanel.this._mTotalAmount = new JLabel("0.00");
      BillingPanel.this._mTotalAmount.setForeground(new Color(13, 140, 151));
      Font localFont2 = BillingPanel.this._mTotalAmount.getFont();
      localFont2 = new Font(localFont2.getName(), 1, 24);
      BillingPanel.this._mTotalAmount.setFont(localFont2);
      localCellConstraints.xywh(5, 4, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
      add(BillingPanel.this._mTotalAmount, localCellConstraints);
      BillingPanel.this._mTotalAmount.setHorizontalAlignment(4);
      BillingPanel.this._mTotalAmount.setText("0.00");
      localJLabel = new JLabel("Tax : ");
      localFont1 = new Font(localFont1.getName(), 0, 20);
      localJLabel.setFont(localFont1);
      localCellConstraints.xywh(3, 2, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
      add(localJLabel, localCellConstraints);
      BillingPanel.this._mTotalTax = new JLabel("0.00");
      BillingPanel.this._mTotalTax.setForeground(new Color(13, 140, 151));
      localFont2 = new Font(localFont2.getName(), 1, 24);
      BillingPanel.this._mTotalTax.setFont(localFont2);
      localCellConstraints.xywh(5, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
      add(BillingPanel.this._mTotalTax, localCellConstraints);
      BillingPanel.this._mTotalTax.setHorizontalAlignment(4);
      BillingPanel.this._mTotalTax.setText("0.00");
      if (!CommonConfig.getInstance().finalTax)
      {
        BillingPanel.this._mTotalTax.setVisible(false);
        localJLabel.setVisible(false);
      }
      localJLabel = new JLabel("Discount :");
      localJLabel.setFont(localFont1);
      localCellConstraints.xywh(9, 2, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
      add(localJLabel, localCellConstraints);
      BillingPanel.this._mFinalDiscount = new JBDBUIAmountTextField("DUMMY", null);
      localCellConstraints.xywh(11, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
      add(BillingPanel.this._mFinalDiscount, localCellConstraints);
      BillingPanel.this._mFinalDiscount.setFont(localFont2);
      BillingPanel.this._mFinalDiscount.setHorizontalAlignment(4);
      BillingPanel.this._mFinalDiscount.setText("0.00");
      BillingPanel.this._mFinalDiscount.addKeyListener(new KeyAdapter()
      {
        public void keyTyped(KeyEvent paramAnonymousKeyEvent)
        {
          if (paramAnonymousKeyEvent.getKeyChar() == '\n') {
            BillingPanel.this.addlDiscountClicked();
          }
        }
      });
      BillingPanel.this._mFinalDiscount.addFocusListener(new FocusAdapter()
      {
        public void focusLost(FocusEvent paramAnonymousFocusEvent)
        {
          Double localDouble = Double.valueOf(Double.valueOf(BillingPanel.this._mFinalDiscount.getText()).doubleValue());
          BillingPanel.this._mBill.setFinalDiscount(localDouble.doubleValue());
          BillingPanel.this.updateTotalPanel();
        }
      });
      localJLabel = new JLabel("Total :");
      localCellConstraints.xywh(9, 4, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
      add(localJLabel, localCellConstraints);
      localJLabel.setFont(localFont1);
      BillingPanel.this._mFinalAmount = new JLabel();
      BillingPanel.this._mFinalAmount.setFont(localFont2);
      localCellConstraints.xywh(11, 4, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
      add(BillingPanel.this._mFinalAmount, localCellConstraints);
      BillingPanel.this._mFinalAmount.setHorizontalAlignment(4);
      BillingPanel.this._mFinalAmount.setText("0.00");
      BillingPanel.this._mFinalDiscount.addKeyListener(new KeyAdapter()
      {
        public void keyPressed(KeyEvent paramAnonymousKeyEvent)
        {
          if (paramAnonymousKeyEvent.getKeyCode() == 115) {
            BillingPanel.this.updateTotalPanel();
          }
        }
      });
    }
  }
  
  private class TablePanel
    extends JXPanel
  {
    public TablePanel()
    {
      tableInitUI();
      addMouseListener(new MouseListener()
      {
        public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
        {
          NumericVirtualKeyPad.getInstance().setVisible(false);
        }
        
        public void mousePressed(MouseEvent paramAnonymousMouseEvent) {}
        
        public void mouseReleased(MouseEvent paramAnonymousMouseEvent) {}
        
        public void mouseEntered(MouseEvent paramAnonymousMouseEvent) {}
        
        public void mouseExited(MouseEvent paramAnonymousMouseEvent) {}
      });
    }
    
    private void tableInitUI()
    {
      FormLayout localFormLayout = new FormLayout("14px,pref:grow,0px", "0px,200px:grow,14px");
      setLayout(localFormLayout);
      CellConstraints localCellConstraints = new CellConstraints();
      localCellConstraints.xywh(2, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
      BillingPanel.this._mModel = new BillTableModel();
      BillingPanel.this._mBillTable = new NonEditableJXTable();
      BillingPanel.this._mBillTable.setRowHeight(20);
      BillingPanel.this._mBillTable.setColumnFactory(new BillingPanel.MyColumnFactory(BillingPanel.this));
      BillingPanel.this._mBillTable.setModel(BillingPanel.this._mModel);
      JScrollPane localJScrollPane = new JScrollPane(BillingPanel.this._mBillTable);
      add(localJScrollPane, localCellConstraints);
      BillingPanel.this._mBillTable.getSelectionModel().setSelectionMode(0);
      BillingPanel.this._mBillTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
      {
        public void valueChanged(ListSelectionEvent paramAnonymousListSelectionEvent)
        {
          if (paramAnonymousListSelectionEvent.getValueIsAdjusting()) {
            return;
          }
          BillingPanel.this._mSelectedBillProduct = null;
          int[] arrayOfInt = BillingPanel.this._mBillTable.getSelectedRows();
          BillingPanel.this._mEditMode = false;
          if (arrayOfInt.length == 0)
          {
            if (BillingPanel.this._mBillTable.hasFocus())
            {
              BillingPanel.this._mCurrentProduct = null;
              BillingPanel.this.clearProdPanel();
              BillingPanel.this._mProductId.requestFocusInWindow();
            }
            BillingPanel.this.enableProdPanel(!BillingPanel.this._mBillUpdateMode);
            BillingPanel.this._mClearButton.setEnabled(true);
            BillingPanel.this._mDeleteButton.setEnabled(false);
            return;
          }
          if (arrayOfInt.length > 1)
          {
            BillingPanel.this._mCurrentProduct = null;
            BillingPanel.this.clearProdPanel();
            BillingPanel.this.enableProdPanel(false);
            BillingPanel.this._mClearButton.setEnabled(false);
            return;
          }
          BillingPanel.this._mEditMode = true;
          BillingPanel.this.enableProdPanel(!BillingPanel.this._mBillUpdateMode);
          int i = BillingPanel.this._mBillTable.convertRowIndexToModel(arrayOfInt[0]);
          BillProduct localBillProduct = BillingPanel.this._mModel.getBillProductAtIndex(i);
          BillingPanel.this._mSelectedBillProduct = localBillProduct;
          BillingPanel.this.billproductSelected(localBillProduct);
          BillingPanel.this._mQuantity.setEditable(true);
          BillingPanel.this._mClearButton.setEnabled(false);
          BillingPanel.this._mDeleteButton.setEnabled(true);
        }
      });
      BillingPanel.this._mBillTable.addKeyListener(new KeyAdapter()
      {
        public void keyTyped(KeyEvent paramAnonymousKeyEvent)
        {
          if (paramAnonymousKeyEvent.getKeyChar() == '') {
            BillingPanel.this.removeSelectedRow();
          }
        }
      });
    }
  }
  
  private class ProductPanel
    extends JBShadowPanel
  {
    public ProductPanel()
    {
      initUI();
      addMouseListener(new MouseListener()
      {
        public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
        {
          NumericVirtualKeyPad.getInstance().setVisible(false);
        }
        
        public void mousePressed(MouseEvent paramAnonymousMouseEvent) {}
        
        public void mouseReleased(MouseEvent paramAnonymousMouseEvent) {}
        
        public void mouseEntered(MouseEvent paramAnonymousMouseEvent) {}
        
        public void mouseExited(MouseEvent paramAnonymousMouseEvent) {}
      });
    }
    
    private void initUI()
    {
      FormLayout localFormLayout = new FormLayout("10px, 120px, 10px, 150px, 2px, 100px, 10px,100px,40px", "30px, 30px, 10px, 30px,10px,30px, 30px");
      setLayout(localFormLayout);
      setBackground(getBackground());
      CellConstraints localCellConstraints = new CellConstraints();
      int i = 2;
      JbMessageLoader localJbMessageLoader = (JbMessageLoader)UICommon.getMessageLoader();
      ProductMessages_base localProductMessages_base = localJbMessageLoader.getProductLabelsMessages();
      JLabel localJLabel = new JLabel("Code  : ");
      Font localFont1 = localJLabel.getFont();
      localFont1 = new Font(localFont1.getName(), 1, 14);
      localJLabel.setFont(localFont1);
      localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
      add(localJLabel, localCellConstraints);
      BillingPanel.this._mProductId = new JBNumTextFieldWithKeyPad();
      Font localFont2 = BillingPanel.this._mProductId.getFont();
      localFont2 = new Font(localFont2.getName(), 1, 14);
      BillingPanel.this._mProductId.setFont(localFont2);
      localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
      add(BillingPanel.this._mProductId, localCellConstraints);
      BillingPanel.this._mProductId.setBackground(UICommon.MANDATORY_COLOR);
      BillingPanel.this._mProductId.addKeyListener(new KeyAdapter()
      {
        public void keyPressed(KeyEvent paramAnonymousKeyEvent)
        {
          if (BillingPanel.this._mBillUpdateMode) {
            return;
          }
          if (paramAnonymousKeyEvent.getKeyCode() == 10) {
            BillingPanel.this.productIdLoadClicked(true);
          } else if (paramAnonymousKeyEvent.getKeyCode() == 114) {
            BillingPanel.this.productIdLoadClicked(false);
          }
        }
      });
      JXButton localJXButton = new JXButton("<HTML><B>Load  [F3]</B></HTML>");
      BillingPanel.this._mLoadButton = localJXButton;
      localCellConstraints.xywh(6, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
      add(localJXButton, localCellConstraints);
      localJXButton.setBackground(getBackground());
      localJXButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          BillingPanel.this.productIdLoadClicked(false);
        }
      });
      i += 2;
      localJLabel = new JLabel(localProductMessages_base.getMessage(131073) + " : ");
      localJLabel.setFont(localFont1);
      localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
      add(localJLabel, localCellConstraints);
      BillingPanel.this._mProductName = new AutoSearchTextField();
      BillingPanel.this._mProductName.setAutoSearchEnabled(true);
      BillingPanel.this._mProductName.setFont(localFont2);
      localCellConstraints.xywh(4, i, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
      add(BillingPanel.this._mProductName, localCellConstraints);
      BillingPanel.this._mProductName.setBackground(UICommon.MANDATORY_COLOR);
      localJXButton = new JXButton("<HTML><B>Search [F5]</B></HTML>");
      BillingPanel.this._mSearchButton = localJXButton;
      localCellConstraints.xywh(8, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
      add(localJXButton, localCellConstraints);
      localJXButton.setBackground(getBackground());
      localJXButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          BillingPanel.this.productNameLoadClicked();
        }
      });
      i += 2;
      localJLabel = new JLabel(localProductMessages_base.getMessage(131076) + " : ");
      localJLabel.setFont(localFont1);
      localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
      add(localJLabel, localCellConstraints);
      BillingPanel.this._mQuantity = new JTextField();
      localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
      add(BillingPanel.this._mQuantity, localCellConstraints);
      BillingPanel.this._mQtyUnitLbl = new JLabel("");
      BillingPanel.this._mQtyUnitLbl.setFont(localFont2);
      localCellConstraints.xywh(6, i, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
      add(BillingPanel.this._mQtyUnitLbl, localCellConstraints);
      BillingPanel.this._mQtyUnitLbl.setFont(localFont1);
      BillingPanel.this._mQuantity.setFont(localFont2);
      BillingPanel.this._mQuantity.setBackground(UICommon.MANDATORY_COLOR);
      BillingPanel.this._mQuantity.addKeyListener(new KeyAdapter()
      {
        public void keyPressed(KeyEvent paramAnonymousKeyEvent)
        {
          if (paramAnonymousKeyEvent.getKeyCode() == 10)
          {
            if (!BillingPanel.this.isDiscountValid()) {
              return;
            }
            BillingPanel.this.addBillProductClicked();
          }
        }
      });
    }
  }
  
  private class AddDeleteButtonPanel
    extends JBShadowPanel
  {
    public AddDeleteButtonPanel()
    {
      initUI();
      addMouseListener(new MouseListener()
      {
        public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
        {
          NumericVirtualKeyPad.getInstance().setVisible(false);
        }
        
        public void mousePressed(MouseEvent paramAnonymousMouseEvent) {}
        
        public void mouseReleased(MouseEvent paramAnonymousMouseEvent) {}
        
        public void mouseEntered(MouseEvent paramAnonymousMouseEvent) {}
        
        public void mouseExited(MouseEvent paramAnonymousMouseEvent) {}
      });
    }
    
    private void initUI()
    {
      FormLayout localFormLayout = new FormLayout("30px, 10px,120px,10px,30px", "30px, 28px, 10px, 28px,10px,28px,30px");
      setLayout(localFormLayout);
      setBackground(getBackground());
      CellConstraints localCellConstraints = new CellConstraints();
      JXButton localJXButton = new JXButton("Add   [ENTER] ");
      localCellConstraints.xywh(3, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
      add(localJXButton, localCellConstraints);
      localJXButton.setBackground(getBackground());
      localJXButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          if (!BillingPanel.this.isDiscountValid()) {
            return;
          }
          BillingPanel.this.addBillProductClicked();
        }
      });
      localJXButton = new JXButton("Delete   [Del]");
      BillingPanel.this._mDeleteButton = localJXButton;
      localCellConstraints.xywh(3, 4, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
      add(localJXButton, localCellConstraints);
      localJXButton.setBackground(getBackground());
      localJXButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          BillingPanel.this.removeSelectedRow();
        }
      });
      localJXButton = new JXButton("Clear   [ESC] ");
      BillingPanel.this._mClearButton = localJXButton;
      localCellConstraints.xywh(3, 6, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
      add(localJXButton, localCellConstraints);
      localJXButton.setBackground(getBackground());
      localJXButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          BillingPanel.this.clearProdPanel();
        }
      });
    }
  }
  
  class TestRenderer
    extends DefaultTableCellRenderer
  {
    TestRenderer() {}
    
    public Component getTableCellRendererComponent(JTable paramJTable, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2)
    {
      super.getTableCellRendererComponent(paramJTable, paramObject, paramBoolean1, paramBoolean2, paramInt1, paramInt2);
      Font localFont1 = getFont();
      Font localFont2 = new Font(localFont1.getName(), localFont1.getStyle(), 14);
      setFont(localFont2);
      BillingColumn localBillingColumn = (BillingColumn)BillingPanel.this._mModel._mColumns.get(paramInt2);
      setHorizontalAlignment(localBillingColumn.alignment);
      return this;
    }
  }
  
  public class MyColumnFactory
    extends ColumnFactory
  {
    public MyColumnFactory() {}
    
    public void configureTableColumn(TableModel paramTableModel, TableColumnExt paramTableColumnExt)
    {
      super.configureTableColumn(paramTableModel, paramTableColumnExt);
      int i = paramTableColumnExt.getModelIndex();
      if (i != 2) {
        paramTableColumnExt.setCellRenderer(new DefaultTableRenderer(new StringValueImpl(), 4));
      }
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.billing.BillingPanel
 * JD-Core Version:    0.7.0.1
 */