package dm.jb.ui.inv;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.db.objects.CurrentStockRow;
import dm.jb.db.objects.CurrentStockTableDef;
import dm.jb.db.objects.ProductRow;
import dm.jb.db.objects.ProductTableDef;
import dm.jb.db.objects.StockAndProductRow;
import dm.jb.db.objects.StockAndProductTableDef;
import dm.jb.db.objects.StockInfoRow;
import dm.jb.db.objects.StockInfoTableDef;
import dm.jb.db.objects.StoreInfoRow;
import dm.jb.db.objects.StoreInfoTableDef;
import dm.jb.db.objects.StoreStockRow;
import dm.jb.db.objects.StoreStockTableDef;
import dm.jb.db.objects.UserInfoRow;
import dm.jb.db.objects.UserInfoTableDef;
import dm.jb.db.objects.WhToStoreEntryRow;
import dm.jb.db.objects.WhToStoreEntryTableDef;
import dm.jb.db.objects.WhToStoreRow;
import dm.jb.db.objects.WhToStoreTableDef;
import dm.jb.op.sync.WhToStoreFile;
import dm.jb.printing.JePrinterException;
import dm.jb.printing.Print;
import dm.jb.ui.MainWindow;
import dm.jb.ui.common.HelpButton;
import dm.jb.ui.common.ShortKeyCommon;
import dm.tools.db.BindObject;
import dm.tools.db.DBConnection;
import dm.tools.db.DBException;
import dm.tools.db.DBRow;
import dm.tools.db.Db;
import dm.tools.types.InternalAmount;
import dm.tools.types.InternalQuantity;
import dm.tools.ui.AbstractMainPanel;
import dm.tools.ui.UICommon;
import dm.tools.ui.components.JBSearchButton;
import dm.tools.ui.components.JTextSeparator;
import dm.tools.utils.SafeMath;
import dm.tools.utils.Validation;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class AllotStockPanel
  extends AbstractMainPanel
{
  private static AllotStockPanel _mAssignStock = null;
  private JTextField _mProductId = null;
  private JTextField _mProductName = null;
  private JTextField _mAvailableQuantity = null;
  private JLabel _mQtyUnitLbl = null;
  private JLabel _mAllotedQtyUnitLbl = null;
  private JTextField _mAllotedQuantity = null;
  private JComboBox _mStore = null;
  private CurrentStockTable _mStockTable = null;
  private JTable _mSelectedTable = null;
  private SelectedStockModel _mSelectedModel = null;
  private JButton _mOKButton = null;
  private JButton _mDeleteButton = null;
  ArrayList<StockInfoAndCurrentStock> _mStockList = null;
  private ProductRow _mSelectedProduct = null;
  private ArrayList<StockAndProductRow> _mStoreStockList = new ArrayList();
  
  public static AllotStockPanel getAssignStockPanel()
  {
    if (_mAssignStock == null) {
      _mAssignStock = new AllotStockPanel();
    } else {
      _mAssignStock.clearAllFields();
    }
    return _mAssignStock;
  }
  
  public AllotStockPanel()
  {
    initUI();
    InputMap localInputMap = getInputMap(2);
    String str = "F4Action";
    localInputMap.put(KeyStroke.getKeyStroke("F4"), str);
    Object localObject = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        AllotStockPanel.this.allotClicked();
      }
    };
    getActionMap().put(str, (Action)localObject);
    str = "F5Action";
    localInputMap.put(KeyStroke.getKeyStroke("F5"), str);
    localObject = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        AllotStockPanel.this.rowRemoveClicked();
      }
    };
    getActionMap().put(str, (Action)localObject);
    str = "F6Action";
    localInputMap.put(KeyStroke.getKeyStroke("F6"), str);
    localObject = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        AllotStockPanel.this.clearProductPanel();
      }
    };
    getActionMap().put(str, (Action)localObject);
  }
  
  private void clearProductPanel()
  {
    this._mSelectedProduct = null;
    this._mProductId.setText("");
    this._mProductName.setText("");
    this._mAvailableQuantity.setText("");
    this._mQtyUnitLbl.setText("");
    this._mAllotedQtyUnitLbl.setText("");
    this._mAllotedQuantity.setText("");
    this._mProductId.requestFocusInWindow();
  }
  
  public void clearAllFields()
  {
    this._mStoreStockList.clear();
    this._mStockTable.removeAllRows();
    clearProductPanel();
    this._mStockTable.clearSelection();
    this._mSelectedTable.clearSelection();
    this._mStockTable.removeAllRows();
    this._mSelectedModel.removeAllRows();
    if (this._mStore.getItemCount() == 0) {
      try
      {
        ArrayList localArrayList = StoreInfoTableDef.getInstance().getAllValues();
        if ((localArrayList == null) || (localArrayList.size() == 0)) {
          return;
        }
        this._mStore.removeAllItems();
        for (int i = 0; i < localArrayList.size(); i++) {
          this._mStore.addItem(localArrayList.get(i));
        }
      }
      catch (DBException localDBException)
      {
        UICommon.showError("Internal error reading store information.", "Internal Error", MainWindow.instance);
      }
    }
    this._mStore.setSelectedItem(StoreInfoTableDef.getCurrentStore());
    this._mDeleteButton.setEnabled(false);
    this._mStockTable.updateUI();
    this._mSelectedTable.updateUI();
    setDefaultFocus();
  }
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("680px:grow", "10px, pref, 20px, 300px, 20px, 30px, 20px");
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
    FormLayout localFormLayout = new FormLayout("10px, 90px, 10px, 140px,3px, 40px, 10px, 80px, 20px,3px, 40px, 30px,100px,10px:grow", "25px, 30px, 25px, 10px, ,25px,10px,25px,10px,25px");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    int i = 1;
    JLabel localJLabel = new JLabel("Store : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mStore = new JComboBox();
    localCellConstraints.xywh(4, i, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mStore, localCellConstraints);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 69, this._mStore);
    this._mStore.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        StoreInfoRow localStoreInfoRow = (StoreInfoRow)AllotStockPanel.this._mStore.getSelectedItem();
        Iterator localIterator = AllotStockPanel.this._mStoreStockList.iterator();
        while (localIterator.hasNext())
        {
          StockAndProductRow localStockAndProductRow = (StockAndProductRow)localIterator.next();
          if (localStockAndProductRow.getStoreId() == localStoreInfoRow.getStoreId())
          {
            AllotStockPanel.this.setStockData(localStockAndProductRow);
            return;
          }
        }
        AllotStockPanel.this.setStockData(null);
      }
    });
    i++;
    localCellConstraints.xywh(2, i, 13, 1, CellConstraints.FILL, CellConstraints.CENTER);
    localJPanel.add(new JTextSeparator("Product Details"), localCellConstraints);
    i++;
    localJLabel = new JLabel("Product Code : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mProductId = new JTextField();
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mProductId, localCellConstraints);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 67, this._mProductId);
    this._mProductId.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 114) {
          AllotStockPanel.this.productIdLoadClicked();
        }
      }
    });
    this._mProductId.setBackground(UICommon.MANDATORY_COLOR);
    JBSearchButton localJBSearchButton = new JBSearchButton(false);
    localJBSearchButton.setMnemonic(74);
    localCellConstraints.xywh(6, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJBSearchButton, localCellConstraints);
    localJBSearchButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        AllotStockPanel.this.productIdLoadClicked();
      }
    });
    i += 2;
    localJLabel = new JLabel("Product Name : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mProductName = new JTextField();
    localCellConstraints.xywh(4, i, 6, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mProductName, localCellConstraints);
    this._mProductName.setBackground(UICommon.MANDATORY_COLOR);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 78, this._mProductName);
    localJBSearchButton = new JBSearchButton(false);
    localCellConstraints.xywh(11, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJBSearchButton, localCellConstraints);
    this._mProductName.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 114) {
          AllotStockPanel.this.productNameSearchClicked();
        }
      }
    });
    localJBSearchButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        AllotStockPanel.this.productNameSearchClicked();
      }
    });
    i += 2;
    localJLabel = new JLabel("Current : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mAvailableQuantity = new JTextField();
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mAvailableQuantity, localCellConstraints);
    this._mAvailableQuantity.setEditable(false);
    this._mAvailableQuantity.setHorizontalAlignment(4);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 85, this._mAvailableQuantity);
    this._mQtyUnitLbl = new JLabel("");
    localCellConstraints.xywh(6, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mQtyUnitLbl, localCellConstraints);
    i += 2;
    localJLabel = new JLabel("Alloted : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mAllotedQuantity = new JTextField();
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mAllotedQuantity, localCellConstraints);
    this._mAllotedQuantity.setHorizontalAlignment(4);
    this._mAllotedQuantity.setBackground(UICommon.MANDATORY_COLOR);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 65, this._mAllotedQuantity);
    this._mAllotedQtyUnitLbl = new JLabel("");
    localCellConstraints.xywh(6, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mAllotedQtyUnitLbl, localCellConstraints);
    localCellConstraints.xywh(13, 3, 1, 7, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(getAddDeleteResetPanel(), localCellConstraints);
    return localJPanel;
  }
  
  private JPanel getAddDeleteResetPanel()
  {
    JPanel localJPanel = new JPanel();
    localJPanel.setLayout(new FormLayout("100px", "30px,10px,30px,10px,30px"));
    CellConstraints localCellConstraints = new CellConstraints();
    JButton localJButton = new JButton("Add   [F4]");
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJButton, localCellConstraints);
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        AllotStockPanel.this.allotClicked();
      }
    });
    localJButton = new JButton("Delete [F5]");
    this._mDeleteButton = localJButton;
    localCellConstraints.xywh(1, 3, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJButton, localCellConstraints);
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        AllotStockPanel.this.rowRemoveClicked();
      }
    });
    localJButton = new JButton("Reset [F6]");
    localCellConstraints.xywh(1, 5, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJButton, localCellConstraints);
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        AllotStockPanel.this.clearProductPanel();
        AllotStockPanel.this._mStockTable.removeAllRows();
        AllotStockPanel.this._mStockTable.clearSelection();
        AllotStockPanel.this.setDefaultFocus();
        AllotStockPanel.this._mStockTable.updateUI();
      }
    });
    return localJPanel;
  }
  
  private JPanel getTablePanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("10px, 300px:grow,10px,350px:grow,10px", "25px,1px, 200px:grow");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    JLabel localJLabel = new JLabel("Available stock ");
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    localJLabel = new JLabel("Selected stock ");
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mStockTable = new CurrentStockTable();
    this._mStockTable.setSelectionMode(0);
    JScrollPane localJScrollPane = new JScrollPane(this._mStockTable);
    localCellConstraints.xywh(2, 3, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJScrollPane, localCellConstraints);
    this._mStockTable.setAutoResizeMode(0);
    this._mStockTable.getColumnModel().getColumn(0).setPreferredWidth(50);
    this._mStockTable.getColumnModel().getColumn(1).setPreferredWidth(100);
    this._mStockTable.getColumnModel().getColumn(2).setPreferredWidth(60);
    this._mStockTable.getColumnModel().getColumn(3).setPreferredWidth(100);
    this._mStockTable.getColumnModel().getColumn(4).setPreferredWidth(80);
    this._mStockTable.getColumnModel().getColumn(5).setPreferredWidth(100);
    this._mSelectedModel = new SelectedStockModel(null);
    this._mSelectedTable = new JTable(this._mSelectedModel);
    this._mSelectedTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent paramAnonymousListSelectionEvent)
      {
        if (paramAnonymousListSelectionEvent.getValueIsAdjusting()) {
          return;
        }
        if (AllotStockPanel.this._mSelectedTable.getSelectedRowCount() == 0) {
          AllotStockPanel.this._mDeleteButton.setEnabled(false);
        } else {
          AllotStockPanel.this._mDeleteButton.setEnabled(true);
        }
      }
    });
    localJScrollPane = new JScrollPane(this._mSelectedTable);
    this._mSelectedTable.setSelectionMode(0);
    this._mSelectedTable.setAutoResizeMode(0);
    this._mSelectedTable.getColumnModel().getColumn(0).setPreferredWidth(50);
    this._mSelectedTable.getColumnModel().getColumn(1).setPreferredWidth(100);
    this._mSelectedTable.getColumnModel().getColumn(2).setPreferredWidth(120);
    this._mSelectedTable.getColumnModel().getColumn(3).setPreferredWidth(80);
    this._mSelectedTable.getColumnModel().getColumn(4).setPreferredWidth(80);
    this._mSelectedTable.getColumnModel().getColumn(5).setPreferredWidth(100);
    this._mSelectedTable.getColumnModel().getColumn(6).setPreferredWidth(100);
    localCellConstraints.xywh(4, 3, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJScrollPane, localCellConstraints);
    return localJPanel;
  }
  
  private JPanel getButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("10px, 100px, 20px, 100px, pref:grow, 100px, pref:grow, 100px, 10px", "30px");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    Object localObject = new JButton(" O K ");
    ((JButton)localObject).setMnemonic(79);
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        AllotStockPanel.this.okClicked();
      }
    });
    this._mOKButton = ((JButton)localObject);
    localObject = new JButton(" Reset ");
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JButton)localObject).setMnemonic(82);
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        AllotStockPanel.this.clearAllFields();
        AllotStockPanel.this.setDefaultFocus();
      }
    });
    localObject = new JButton(" Close ");
    localCellConstraints.xywh(6, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        AllotStockPanel.this.closeWindow();
      }
    });
    localObject = new HelpButton("ISP_ALLOT_STOCK");
    localCellConstraints.xywh(8, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    return localJPanel;
  }
  
  private void productNameSearchClicked()
  {
    String str = this._mProductName.getText().trim();
    Object localObject;
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
      setProductData(localObject[0]);
      try
      {
        loadStockDetailsForProduct(localObject[0]);
      }
      catch (DBException localDBException2)
      {
        localDBException2.printStackTrace();
        UICommon.showError("Internal error searching product details. Try again later.\n\nIf the problem persists contact administrator.", "Internal Error", MainWindow.instance);
        this._mProductName.requestFocusInWindow();
        return;
      }
    }
    if (!str.endsWith("%")) {
      str = str + "%";
    }
    str = Db.getSearchFormattedString(str);
    ProductSearchPanel localProductSearchPanel = new ProductSearchPanel(MainWindow.instance);
    localProductSearchPanel.setLocationRelativeTo(MainWindow.instance);
    try
    {
      localObject = localProductSearchPanel.searchProduct(str, "", "", MainWindow.instance);
      if (localObject != null)
      {
        setProductData((ProductRow)localObject);
        loadStockDetailsForProduct((ProductRow)localObject);
      }
      else
      {
        this._mProductName.requestFocusInWindow();
      }
    }
    catch (DBException localDBException1)
    {
      localDBException1.printStackTrace();
      UICommon.showError("Internal Error searching for product.", "Internal Error", MainWindow.instance);
      this._mProductName.setText(str);
      return;
    }
  }
  
  private void productIdLoadClicked()
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
      setProductData(arrayOfProductRow[0]);
      try
      {
        loadStockDetailsForProduct(arrayOfProductRow[0]);
      }
      catch (DBException localDBException2)
      {
        localDBException2.printStackTrace();
        UICommon.showError("Internal error searching product details. Try again later.\n\nIf the problem persists contact administrator.", "Internal Error", MainWindow.instance);
        this._mProductId.requestFocusInWindow();
        return;
      }
      return;
    }
    try
    {
      localObject = ProductTableDef.getInstance().getProductByCode(str);
      if (localObject == null)
      {
        UICommon.showError("No product found.", "Error", MainWindow.instance);
        this._mProductId.requestFocusInWindow();
        return;
      }
      setProductData((ProductRow)localObject);
      loadStockDetailsForProduct((ProductRow)localObject);
    }
    catch (DBException localDBException1)
    {
      localDBException1.printStackTrace();
      UICommon.showError("Internal error searching product details. Try again later.\n\nIf the problem persists contact administrator.", "Internal Error", MainWindow.instance);
      this._mProductId.requestFocusInWindow();
      return;
    }
  }
  
  private void loadStockDetailsForProduct(ProductRow paramProductRow)
    throws DBException
  {
    StoreInfoRow localStoreInfoRow = (StoreInfoRow)this._mStore.getSelectedItem();
    String str = "PROD_INDEX=" + paramProductRow.getProdIndex();
    ArrayList localArrayList = StockAndProductTableDef.getInstance().getAllValuesWithWhereClause(str);
    this._mStoreStockList.clear();
    if ((localArrayList == null) || (localArrayList.size() == 0))
    {
      this._mStockTable.removeAllRows();
      this._mSelectedProduct = null;
      this._mStockTable.clearSelection();
      this._mStockTable.updateUI();
      this._mAllotedQuantity.requestFocusInWindow();
      return;
    }
    this._mSelectedProduct = paramProductRow;
    Object localObject = localArrayList.iterator();
    while (((Iterator)localObject).hasNext())
    {
      DBRow localDBRow = (DBRow)((Iterator)localObject).next();
      StockAndProductRow localStockAndProductRow = (StockAndProductRow)localDBRow;
      this._mStoreStockList.add(localStockAndProductRow);
      if (localStockAndProductRow.getStoreId() == localStoreInfoRow.getStoreId()) {
        setStockData(localStockAndProductRow);
      }
    }
    this._mStockTable.clearSelection();
    this._mStockTable.updateUI();
    localObject = CurrentStockTableDef.getInstance().getCurrentStockListForProduct(paramProductRow.getProdIndex());
    if ((localObject == null) || (((ArrayList)localObject).size() == 0))
    {
      this._mStockTable.removeAllRows();
      this._mStockTable.updateUI();
      return;
    }
    this._mStockList = StockInfoAndCurrentStock.getStockInfoAndCurrentStocksForCurrentStocks((ArrayList)localObject);
    setStocks(this._mStockList);
    this._mAllotedQuantity.requestFocusInWindow();
  }
  
  public void setStocks(ArrayList<StockInfoAndCurrentStock> paramArrayList)
  {
    this._mStockTable.removeAllRows();
    this._mStockList = paramArrayList;
    if (paramArrayList == null) {
      return;
    }
    ArrayList localArrayList = this._mSelectedModel._mSelectedEntries;
    int i = 0;
    Iterator localIterator1 = this._mStockList.iterator();
    while (localIterator1.hasNext())
    {
      StockInfoAndCurrentStock localStockInfoAndCurrentStock = (StockInfoAndCurrentStock)localIterator1.next();
      int j = localStockInfoAndCurrentStock.currentStockRow.getStockIndex();
      Iterator localIterator2 = localArrayList.iterator();
      while (localIterator2.hasNext())
      {
        SelectedEntry localSelectedEntry = (SelectedEntry)localIterator2.next();
        if (localSelectedEntry._mStockRow.currentStockRow.getStockIndex() == j)
        {
          i = 1;
          localStockInfoAndCurrentStock.currentStockRow.setQuantity(SafeMath.safeSubtract(localStockInfoAndCurrentStock.currentStockRow.getQuantity(), localSelectedEntry._mQuantity.doubleValue()));
          break;
        }
      }
      if ((i != 0) || (localStockInfoAndCurrentStock.currentStockRow.getQuantity() > 0.0D)) {
        this._mStockTable.addStock(localStockInfoAndCurrentStock);
      }
    }
    this._mStockTable.updateUI();
  }
  
  public void setProductData(ProductRow paramProductRow)
  {
    this._mProductName.setText(paramProductRow.getProdName());
    this._mProductId.setText(Integer.toString(paramProductRow.getProdIndex()));
    this._mQtyUnitLbl.setText("  " + InternalQuantity.quantityUnits[paramProductRow.getProdUnit()]);
    this._mAllotedQtyUnitLbl.setText("  " + InternalQuantity.quantityUnits[paramProductRow.getProdUnit()]);
    this._mAvailableQuantity.setText("0.0");
  }
  
  public void setStockData(StockAndProductRow paramStockAndProductRow)
  {
    if (paramStockAndProductRow == null)
    {
      if (this._mSelectedProduct != null) {
        this._mAvailableQuantity.setText(InternalQuantity.toString(0.0D, (short)this._mSelectedProduct.getProdUnit()));
      }
    }
    else {
      this._mAvailableQuantity.setText(InternalQuantity.toString(paramStockAndProductRow.getStock(), (short)paramStockAndProductRow.getProdUnit()));
    }
  }
  
  private void allotClicked()
  {
    if (this._mSelectedProduct == null)
    {
      UICommon.showError("No product is selected.", "Error", MainWindow.instance);
      this._mProductId.requestFocusInWindow();
      return;
    }
    if (this._mStockTable.getRowCount() == 0)
    {
      UICommon.showError("None of the warehouse has the stock for the selected product.", "Error", MainWindow.instance);
      return;
    }
    int i = this._mStockTable.getSelectedRow();
    if (i < 0)
    {
      UICommon.showError("No stock selected.", "Error", MainWindow.instance);
      return;
    }
    String str = this._mAllotedQuantity.getText();
    if (str.length() == 0)
    {
      UICommon.showError("Allocation quantity cannot be empty.", "Error", MainWindow.instance);
      this._mAllotedQuantity.requestFocusInWindow();
      return;
    }
    if ((str.indexOf(".") != -1) && (!InternalQuantity.isUnitFractionAllowed(this._mSelectedProduct.getProdUnit())))
    {
      UICommon.showError("Fraction quantity is not alowed for this product.", "Error", MainWindow.instance);
      this._mAllotedQuantity.requestFocusInWindow();
      return;
    }
    if (!InternalQuantity.isUnitFractionAllowed(this._mSelectedProduct.getProdUnit()))
    {
      if (!Validation.isValidInt(str, false))
      {
        UICommon.showError("Invalid quantity specified for allocation.", "Error", MainWindow.instance);
        this._mAllotedQuantity.requestFocusInWindow();
      }
    }
    else if (!Validation.isValidFloat(str, 3, false))
    {
      UICommon.showError("Invalid quantity specified for allocation.", "Error", MainWindow.instance);
      this._mAllotedQuantity.requestFocusInWindow();
      return;
    }
    StockInfoAndCurrentStock localStockInfoAndCurrentStock = this._mStockTable.getStockAtTableIndex(i);
    double d = Double.valueOf(str).doubleValue();
    if (d == 0.0D)
    {
      UICommon.showError("Allocated quantity cannot be zero.", "Error", MainWindow.instance);
      this._mAllotedQuantity.requestFocusInWindow();
      return;
    }
    if (d > localStockInfoAndCurrentStock.currentStockRow.getQuantity())
    {
      UICommon.showError("Not enough stock available.", "Error", MainWindow.instance);
      this._mAllotedQuantity.requestFocusInWindow();
      return;
    }
    SelectedEntry localSelectedEntry = new SelectedEntry(this._mSelectedProduct, d, localStockInfoAndCurrentStock);
    int j = this._mSelectedModel.addSelectedEntryToTable(localSelectedEntry);
    j = this._mStockTable.convertRowIndexToView(j);
    Rectangle localRectangle = this._mStockTable.getCellRect(j, 0, true);
    this._mStockTable.scrollRectToVisible(localRectangle);
    localStockInfoAndCurrentStock.currentStockRow.setQuantity(SafeMath.safeSubtract(localStockInfoAndCurrentStock.currentStockRow.getQuantity(), d));
    this._mStockTable.setStockQuantityAtRow(i, localStockInfoAndCurrentStock.currentStockRow.getQuantity(), (short)this._mSelectedProduct.getProdUnit());
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
    getRootPane().setDefaultButton(this._mOKButton);
    clearAllFields();
  }
  
  private StockInfoRow[] getStocksInfos(int[] paramArrayOfInt)
    throws DBException
  {
    CurrentStockRow localCurrentStockRow = null;
    StockInfoRow localStockInfoRow = null;
    StockInfoRow[] arrayOfStockInfoRow = new StockInfoRow[paramArrayOfInt.length];
    for (int i = 0; i < paramArrayOfInt.length; i++)
    {
      localCurrentStockRow = ((StockInfoAndCurrentStock)this._mStockList.get(paramArrayOfInt[i])).currentStockRow;
      localStockInfoRow = StockInfoTableDef.getInstance().findRowByIndex(localCurrentStockRow.getStockIndex());
      arrayOfStockInfoRow[i] = localStockInfoRow;
    }
    return arrayOfStockInfoRow;
  }
  
  private void okClicked()
  {
    if (this._mSelectedModel.getRowCount() == 0)
    {
      UICommon.showError("No stock is selected for moving.", "Error", MainWindow.instance);
      setDefaultFocus();
      return;
    }
    DBConnection localDBConnection = Db.getConnection();
    int i = ((StoreInfoRow)this._mStore.getSelectedItem()).getStoreId();
    try
    {
      localDBConnection.openTrans();
      WhToStoreRow localWhToStoreRow = WhToStoreTableDef.getInstance().getNewRow();
      java.sql.Date localDate = new java.sql.Date(new java.util.Date().getTime());
      Time localTime = new Time(new java.util.Date().getTime());
      localWhToStoreRow.setValues(localDate, localTime, UserInfoTableDef.getCurrentUser().getUserIndex(), i);
      localWhToStoreRow.create();
      int j = localWhToStoreRow.getTxnNo();
      Iterator localIterator1 = this._mSelectedModel._mSelectedEntries.iterator();
      Object localObject1;
      Object localObject2;
      while (localIterator1.hasNext())
      {
        localObject1 = (SelectedEntry)localIterator1.next();
        localObject2 = WhToStoreEntryTableDef.getInstance().getNewRow();
        ((WhToStoreEntryRow)localObject2).setValues(((SelectedEntry)localObject1)._mStockRow.currentStockRow.getWearHouseIndex(), ((SelectedEntry)localObject1)._mProduct.getProdIndex(), ((SelectedEntry)localObject1)._mStockRow.currentStockRow.getStockIndex(), ((SelectedEntry)localObject1)._mQuantity.doubleValue(), j);
        ((WhToStoreEntryRow)localObject2).create();
        ((SelectedEntry)localObject1)._mStockRow.currentStockRow.update(true);
      }
      localDBConnection.endTrans();
      int k = UICommon.showQuestion("Records updated successfully. Traction No : " + j + "\n\nDo you want to update the store stock information right now ? ", "Confirm update", MainWindow.instance);
      if (k == 1)
      {
        localDBConnection.openTrans();
        localObject1 = new BindObject(1, (short)1, Integer.valueOf(i));
        localObject2 = new BindObject(2, (short)1, Integer.valueOf(-1));
        BindObject[] arrayOfBindObject = { localObject1, localObject2 };
        Iterator localIterator2 = this._mSelectedModel._mSelectedEntries.iterator();
        while (localIterator2.hasNext())
        {
          SelectedEntry localSelectedEntry = (SelectedEntry)localIterator2.next();
          arrayOfBindObject[1].value = Integer.valueOf(localSelectedEntry._mProduct.getProdIndex());
          ArrayList localArrayList = StoreStockTableDef.getInstance().getAllValuesWithWhereClauseWithBind(" STORE_ID=? AND PRODUCT_ID=? ", arrayOfBindObject, "ALLOT_STOCK_UPDATE_STORE_STOCK");
          StoreStockRow localStoreStockRow;
          if ((localArrayList == null) || (localArrayList.size() == 0))
          {
            localStoreStockRow = StoreStockTableDef.getInstance().getNewRow();
            localStoreStockRow.setValues(i, localSelectedEntry._mProduct.getProdIndex(), localSelectedEntry._mQuantity.doubleValue(), localSelectedEntry._mStockRow.stockInfoRow.getPurchasePrice(), null, localSelectedEntry._mStockRow.stockInfoRow.getStockIndex());
            localStoreStockRow.create();
          }
          else
          {
            localStoreStockRow = (StoreStockRow)localArrayList.get(0);
            double d = SafeMath.safeAdd(localStoreStockRow.getStock(), localSelectedEntry._mQuantity.doubleValue());
            localStoreStockRow.setStock(d);
            localStoreStockRow.update(true);
          }
        }
        localDBConnection.openTrans();
        StoreStockTableDef.getInstance().removeStatementsFromMap("ALLOT_STOCK_UPDATE_STORE_STOCK");
        k = UICommon.showQuestion("Store records are updated successfully.\n\nDo you want to print it.", "Success", MainWindow.instance);
        if (k != 1)
        {
          clearAllFields();
          return;
        }
        printData(localWhToStoreRow, (StoreInfoRow)this._mStore.getSelectedItem());
        clearAllFields();
      }
      else
      {
        try
        {
          localObject1 = WhToStoreFile.INSTANCE.writeData(localWhToStoreRow, this._mSelectedModel._mSelectedEntries);
          UICommon.showMessage("The data file is created at\n\t" + (String)localObject1 + ".\n\nThis file can be used to update the store information ate store.", "Success", MainWindow.instance);
          int m = UICommon.showQuestion("Do you want to print ? ", "Print", MainWindow.instance);
          if (m != 1)
          {
            clearAllFields();
            return;
          }
          printData(localWhToStoreRow, (StoreInfoRow)this._mStore.getSelectedItem());
          clearAllFields();
        }
        catch (IOException localIOException)
        {
          clearAllFields();
          localIOException.printStackTrace();
          UICommon.showError("Internal error creating data file.\n\n" + localIOException.getMessage(), "Error", MainWindow.instance);
          return;
        }
      }
    }
    catch (DBException localDBException)
    {
      localDBException.printStackTrace();
      localDBConnection.rollbackNoExp();
      StoreStockTableDef.getInstance().removeStatementsFromMap("ALLOT_STOCK_UPDATE_STORE_STOCK");
      UICommon.showError("Internal error updating records.\n\nTry again later. If the problem persists contact administrator.", "Internal Error", MainWindow.instance);
      return;
    }
  }
  
  private void rowRemoveClicked()
  {
    int i = this._mSelectedTable.getSelectedRow();
    i = this._mSelectedTable.convertRowIndexToModel(i);
    this._mStockTable.addSockForStockIndex(((SelectedEntry)this._mSelectedModel._mSelectedEntries.get(i))._mStockRow.currentStockRow.getStockIndex(), ((SelectedEntry)this._mSelectedModel._mSelectedEntries.get(i))._mQuantity.doubleValue());
    this._mSelectedModel.removeSelectedRow(i);
  }
  
  private void printData(WhToStoreRow paramWhToStoreRow, StoreInfoRow paramStoreInfoRow)
  {
    try
    {
      Print.getInstance().printWhToStore(paramWhToStoreRow, this._mSelectedModel._mSelectedEntries, paramStoreInfoRow);
    }
    catch (JePrinterException localJePrinterException)
    {
      localJePrinterException.printStackTrace();
      UICommon.showError("Error printing.\nDetails:\n" + localJePrinterException.toString(), "Printing Error", MainWindow.instance);
      return;
    }
  }
  
  public class SelectedEntry
  {
    public ProductRow _mProduct = null;
    public Double _mQuantity = Double.valueOf(0.0D);
    public StockInfoAndCurrentStock _mStockRow = null;
    
    public SelectedEntry(ProductRow paramProductRow, double paramDouble, StockInfoAndCurrentStock paramStockInfoAndCurrentStock)
    {
      this._mProduct = paramProductRow;
      this._mQuantity = Double.valueOf(paramDouble);
      this._mStockRow = paramStockInfoAndCurrentStock;
    }
  }
  
  private class SelectedStockModel
    extends DefaultTableModel
  {
    private ArrayList<AllotStockPanel.SelectedEntry> _mSelectedEntries = new ArrayList();
    private String[] columnNames = { "Sl. No", "Product Code", "Product Name    ", "  Quantity   ", "Stock No", "Warehouse    ", "Purchase Price" };
    
    private SelectedStockModel() {}
    
    public int getColumnCount()
    {
      return this.columnNames.length;
    }
    
    public String getColumnName(int paramInt)
    {
      return this.columnNames[paramInt];
    }
    
    public Class getColumnClass(int paramInt)
    {
      if ((paramInt == 0) || (paramInt == 3) || (paramInt == 4) || (paramInt == 6)) {
        return Integer.class;
      }
      return Object.class;
    }
    
    public void removeSelectedRow(int paramInt)
    {
      this._mSelectedEntries.remove(paramInt);
      removeRow(paramInt);
      int i = getRowCount();
      for (int j = paramInt; j < i; j++) {
        setValueAt(j + 1 + "  ", j, 0);
      }
    }
    
    public int addSelectedEntryToTable(AllotStockPanel.SelectedEntry paramSelectedEntry)
    {
      int i = paramSelectedEntry._mStockRow.currentStockRow.getStockIndex();
      int j = 0;
      Object localObject1 = this._mSelectedEntries.iterator();
      while (((Iterator)localObject1).hasNext())
      {
        AllotStockPanel.SelectedEntry localSelectedEntry = (AllotStockPanel.SelectedEntry)((Iterator)localObject1).next();
        if (localSelectedEntry._mStockRow.currentStockRow.getStockIndex() == i)
        {
          Object localObject2 = localSelectedEntry;
          (((AllotStockPanel.SelectedEntry)localObject2)._mQuantity = Double.valueOf(((AllotStockPanel.SelectedEntry)localObject2)._mQuantity.doubleValue() + paramSelectedEntry._mQuantity.doubleValue()));
          localObject2 = InternalQuantity.toString(localSelectedEntry._mQuantity.doubleValue(), (short)paramSelectedEntry._mProduct.getProdUnit(), true);
          setValueAt(localObject2, j, 3);
          return j;
        }
        j++;
      }
      this._mSelectedEntries.add(paramSelectedEntry);
      localObject1 = new Object[] { this._mSelectedEntries.size() + "  ", paramSelectedEntry._mProduct.getProductCode(), paramSelectedEntry._mProduct.getProdName(), InternalQuantity.toString(paramSelectedEntry._mQuantity.doubleValue(), (short)paramSelectedEntry._mProduct.getProdUnit(), true), paramSelectedEntry._mStockRow.currentStockRow.getStockIndex() + "  ", paramSelectedEntry._mStockRow.wareHouse, InternalAmount.toString(paramSelectedEntry._mStockRow.stockInfoRow.getPurchasePrice()) };
      addRow((Object[])localObject1);
      return this._mSelectedEntries.size() - 1;
    }
    
    public boolean isCellEditable(int paramInt1, int paramInt2)
    {
      return false;
    }
    
    public void removeAllRows()
    {
      this._mSelectedEntries.clear();
      int i = getRowCount();
      for (int j = 0; j < i; j++) {
        removeRow(0);
      }
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.inv.AllotStockPanel
 * JD-Core Version:    0.7.0.1
 */