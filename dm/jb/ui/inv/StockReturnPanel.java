package dm.jb.ui.inv;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.db.objects.CurrentStockRow;
import dm.jb.db.objects.CurrentStockTableDef;
import dm.jb.db.objects.DefectiveProductRow;
import dm.jb.db.objects.DefectiveProductTableDef;
import dm.jb.db.objects.ProductRow;
import dm.jb.db.objects.ProductTableDef;
import dm.jb.db.objects.StockAndProductRow;
import dm.jb.db.objects.StockAndProductTableDef;
import dm.jb.db.objects.StockInfoRow;
import dm.jb.db.objects.StockInfoTableDef;
import dm.jb.db.objects.StockReturnEntryRow;
import dm.jb.db.objects.StockReturnEntryTableDef;
import dm.jb.db.objects.StockReturnRow;
import dm.jb.db.objects.StockReturnTableDef;
import dm.jb.db.objects.StoreInfoRow;
import dm.jb.db.objects.StoreInfoTableDef;
import dm.jb.db.objects.StoreStockRow;
import dm.jb.db.objects.StoreStockTableDef;
import dm.jb.db.objects.UserInfoRow;
import dm.jb.db.objects.UserInfoTableDef;
import dm.jb.db.objects.VendorRow;
import dm.jb.db.objects.VendorTableDef;
import dm.jb.db.objects.WearehouseInfoRow;
import dm.jb.db.objects.WearehouseInfoTableDef;
import dm.jb.printing.JePrinterException;
import dm.jb.printing.Print;
import dm.jb.ui.MainWindow;
import dm.jb.ui.common.HelpButton;
import dm.jb.ui.common.ShortKeyCommon;
import dm.tools.db.DBConnection;
import dm.tools.db.DBException;
import dm.tools.db.DBRow;
import dm.tools.db.Db;
import dm.tools.types.InternalQuantity;
import dm.tools.ui.AbstractMainPanel;
import dm.tools.ui.UICommon;
import dm.tools.ui.components.JBSearchButton;
import dm.tools.utils.CommonConfig;
import dm.tools.utils.Validation;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class StockReturnPanel
  extends AbstractMainPanel
{
  public static StockReturnPanel INSTANCE = new StockReturnPanel();
  private JComboBox _mReturnToVendor = null;
  private JTextField _mProductCode = null;
  private JTextField _mProductName = null;
  private JComboBox _mStore = null;
  private JTextField _mStoreCurrentQty = null;
  private JLabel _mStoreCurrentQtyUnit = null;
  private JTextField _mStoreReturnQty = null;
  private JLabel _mStoreReturnQtyUnit = null;
  private JButton _mProductNameSearchButton = null;
  private JButton _mProductCodeSearchButton = null;
  private JTextField _mWarehouseReturn = null;
  private JTextField _mDefectiveReturn = null;
  private JLabel _mWarehouseReturnUnit = null;
  private JLabel _mDefectiveReturnUnit = null;
  private ReturnProductTableModel _mReturnModel = null;
  private JTable _mReturnTable = null;
  private WarehouseProductTableModel _mWarehouseModel = null;
  private JTable _mWarehouseTable = null;
  private DefectiveProductTableModel _mDefectivesModel = null;
  private JTable _mDefectivesTable = null;
  private HashMap<Integer, StockAndProductRow> _mStocks = new HashMap();
  private ProductRow _mSelectedProduct = null;
  private JTabbedPane _mTabPane = null;
  
  private StockReturnPanel()
  {
    initUI();
  }
  
  public void setDefaultFocus()
  {
    this._mProductCode.requestFocusInWindow();
  }
  
  public JPanel getPanel()
  {
    return this;
  }
  
  public void windowDisplayed()
  {
    try
    {
      ArrayList localArrayList = StoreInfoTableDef.getInstance().getStoreList();
      Object localObject1 = localArrayList.iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject2 = (StoreInfoRow)((Iterator)localObject1).next();
        this._mStore.addItem(localObject2);
      }
      localObject1 = VendorTableDef.getInstance().getVendorRows();
      Object localObject2 = ((ArrayList)localObject1).iterator();
      while (((Iterator)localObject2).hasNext())
      {
        VendorRow localVendorRow = (VendorRow)((Iterator)localObject2).next();
        this._mReturnToVendor.addItem(localVendorRow);
      }
    }
    catch (DBException localDBException)
    {
      localDBException.printStackTrace();
      UICommon.showError("Internal error in reading store details.\n\nTry again later. If the problem persists contact administrator.", "Internal Error", MainWindow.instance);
      return;
    }
  }
  
  public void prepareForView()
  {
    enableComponents(false);
  }
  
  public void prepareForNew()
  {
    enableComponents(true);
  }
  
  private void enableComponents(boolean paramBoolean)
  {
    this._mProductCode.setEditable(paramBoolean);
    this._mProductName.setEditable(paramBoolean);
    this._mProductNameSearchButton.setEnabled(paramBoolean);
    this._mProductCodeSearchButton.setEnabled(paramBoolean);
    this._mStoreCurrentQtyUnit.setEnabled(paramBoolean);
    this._mStoreCurrentQty.setEnabled(paramBoolean);
    this._mStoreReturnQty.setEnabled(paramBoolean);
    this._mWarehouseReturn.setEnabled(paramBoolean);
    this._mDefectiveReturn.setEnabled(paramBoolean);
    this._mWarehouseReturnUnit.setText("");
    this._mDefectiveReturnUnit.setText("");
  }
  
  public void clearAllFields()
  {
    this._mSelectedProduct = null;
    this._mProductCode.setText("");
    this._mProductName.setText("");
    this._mStoreCurrentQty.setText("");
    this._mStoreCurrentQtyUnit.setText("");
    this._mStoreReturnQty.setText("");
    this._mStoreReturnQtyUnit.setText("");
    this._mStocks.clear();
    this._mDefectivesModel.removeAllDefectiveRows();
    this._mWarehouseModel.removeAllStockRows();
    this._mReturnModel.removeAllReturnRows();
    this._mWarehouseReturn.setText("");
    this._mDefectiveReturn.setText("");
    this._mWarehouseReturnUnit.setText("");
    this._mDefectiveReturnUnit.setText("");
  }
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("10px,90px,10px,200px, 370px:grow", "10px,25px,20px,pref, 10px, 300px:grow, 20px, 30px, 10px");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    this._mReturnToVendor = new JComboBox();
    localCellConstraints.xywh(2, 2, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    JLabel localJLabel = new JLabel("Vendor : ");
    add(localJLabel, localCellConstraints);
    localCellConstraints.xywh(4, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mReturnToVendor, localCellConstraints);
    localCellConstraints.xywh(1, 4, 5, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getProductPanel(), localCellConstraints);
    localCellConstraints.xywh(1, 6, 5, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getTablePanel(), localCellConstraints);
    localCellConstraints.xywh(1, 7, 5, 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(new JSeparator(), localCellConstraints);
    localCellConstraints.xywh(1, 8, 5, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getButtonPanel(), localCellConstraints);
  }
  
  public JPanel getProductPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("10px, 90px, 10px, 100px,3px, 40px, 40px,3px,40px, 40px,10px", "25px,10px,25px, 10px, 25px, 10px, 25px,10px,25px");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    int i = 1;
    JLabel localJLabel = new JLabel("Product Code : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mProductCode = new JTextField();
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mProductCode, localCellConstraints);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 80, this._mProductCode);
    this._mProductCode.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 114) {
          StockReturnPanel.this.productIdLoadClicked();
        }
      }
    });
    this._mProductCode.setBackground(UICommon.MANDATORY_COLOR);
    Object localObject = new JBSearchButton(false);
    this._mProductCodeSearchButton = ((JButton)localObject);
    localCellConstraints.xywh(6, i, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        StockReturnPanel.this.productIdLoadClicked();
      }
    });
    i += 2;
    localJLabel = new JLabel("Product Name : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mProductName = new JTextField();
    localCellConstraints.xywh(4, i, 4, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mProductName, localCellConstraints);
    this._mProductName.setBackground(UICommon.MANDATORY_COLOR);
    this._mProductName.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 114) {
          StockReturnPanel.this.searchByProductNameClicked();
        }
      }
    });
    ShortKeyCommon.shortKeyForLabels(localJLabel, 78, this._mProductName);
    localObject = new JBSearchButton(false);
    this._mProductNameSearchButton = ((JButton)localObject);
    localCellConstraints.xywh(9, i, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        StockReturnPanel.this.searchByProductNameClicked();
      }
    });
    i += 2;
    localJLabel = new JLabel("Store : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mStore = new JComboBox();
    localCellConstraints.xywh(4, i, 4, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mStore, localCellConstraints);
    this._mStore.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        StockReturnPanel.this.storeChanged();
      }
    });
    i += 2;
    localJLabel = new JLabel("Store stock : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mStoreCurrentQty = new JTextField();
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mStoreCurrentQty, localCellConstraints);
    this._mStoreCurrentQty.setEditable(false);
    this._mStoreCurrentQty.setHorizontalAlignment(4);
    this._mStoreCurrentQtyUnit = new JLabel("");
    localCellConstraints.xywh(6, i, 2, 1, CellConstraints.LEFT, CellConstraints.FILL);
    localJPanel.add(this._mStoreCurrentQtyUnit, localCellConstraints);
    i += 2;
    localJLabel = new JLabel("Return stock : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mStoreReturnQty = new JTextField();
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mStoreReturnQty, localCellConstraints);
    this._mStoreReturnQty.setHorizontalAlignment(4);
    this._mStoreReturnQtyUnit = new JLabel("");
    localCellConstraints.xywh(6, i, 2, 1, CellConstraints.LEFT, CellConstraints.FILL);
    localJPanel.add(this._mStoreReturnQtyUnit, localCellConstraints);
    localObject = new JButton("Add");
    localCellConstraints.xywh(9, i, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        StockReturnPanel.this.addFromProductPanelClicked();
      }
    });
    localJPanel.setBackground(getBackground());
    return localJPanel;
  }
  
  private JPanel getTablePanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("10px,300px,10px,80px,10px,250px,10px", "pref:grow,30px,10px,30px,pref:grow");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(2, 1, 1, 5, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(getFromTabPane(), localCellConstraints);
    JButton localJButton = new JButton("Add");
    localCellConstraints.xywh(4, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJButton, localCellConstraints);
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        StockReturnPanel.this.addFromTabClicked();
      }
    });
    localJButton = new JButton("Remove");
    localCellConstraints.xywh(4, 4, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJButton, localCellConstraints);
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        StockReturnPanel.this.deleteClicked();
      }
    });
    this._mReturnModel = new ReturnProductTableModel(null);
    this._mReturnTable = new JTable(this._mReturnModel);
    this._mReturnTable.setSelectionMode(0);
    this._mReturnTable.setAutoResizeMode(0);
    this._mReturnTable.setSize(new Dimension(400, 200));
    this._mReturnTable.getColumnModel().getColumn(2).setWidth(400);
    localCellConstraints.xywh(6, 1, 1, 5, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(new JScrollPane(this._mReturnTable, 20, 30), localCellConstraints);
    return localJPanel;
  }
  
  private JTabbedPane getFromTabPane()
  {
    this._mTabPane = new JTabbedPane();
    CellConstraints localCellConstraints = new CellConstraints();
    JPanel localJPanel = new JPanel();
    localJPanel.setLayout(new FormLayout("10px,90px,10px,100px,3px,60px,10px", "10px,200px,10px,25px,10px"));
    this._mWarehouseModel = new WarehouseProductTableModel(null);
    this._mWarehouseTable = new JTable(this._mWarehouseModel);
    this._mWarehouseTable.setSelectionMode(0);
    localCellConstraints.xywh(2, 2, 5, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(new JScrollPane(this._mWarehouseTable, 20, 32), localCellConstraints);
    this._mTabPane.add("Warehouse", localJPanel);
    JLabel localJLabel = new JLabel("Return Quantity : ");
    localCellConstraints.xywh(2, 4, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mWarehouseReturn = new JTextField();
    localCellConstraints.xywh(4, 4, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mWarehouseReturn, localCellConstraints);
    this._mWarehouseReturn.setHorizontalAlignment(4);
    this._mWarehouseReturnUnit = new JLabel();
    localCellConstraints.xywh(6, 4, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    localJPanel.add(this._mWarehouseReturnUnit, localCellConstraints);
    localJPanel = new JPanel();
    localJPanel.setLayout(new FormLayout("10px,90px,10px,100px,3px,60px,10px", "10px,200px,10px,25px,10px"));
    this._mDefectivesModel = new DefectiveProductTableModel(null);
    this._mDefectivesTable = new JTable(this._mDefectivesModel);
    this._mDefectivesTable.setSelectionMode(0);
    localCellConstraints.xywh(2, 2, 5, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(new JScrollPane(this._mDefectivesTable, 20, 32), localCellConstraints);
    localJLabel = new JLabel("Return Quantity : ");
    localCellConstraints.xywh(2, 4, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mDefectiveReturn = new JTextField();
    localCellConstraints.xywh(4, 4, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mDefectiveReturn, localCellConstraints);
    this._mDefectiveReturn.setHorizontalAlignment(4);
    this._mDefectiveReturnUnit = new JLabel();
    localCellConstraints.xywh(6, 4, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    localJPanel.add(this._mDefectiveReturnUnit, localCellConstraints);
    this._mTabPane.add("Defectives", localJPanel);
    return this._mTabPane;
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
        StockReturnPanel.this.okClicked();
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
        StockReturnPanel.this.clearAllFields();
        StockReturnPanel.this.setDefaultFocus();
      }
    });
    localObject = new JButton(" Close ");
    localCellConstraints.xywh(6, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        StockReturnPanel.this.closeWindow();
      }
    });
    localObject = new HelpButton("ISP_STOCK_RETURN");
    localCellConstraints.xywh(8, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JButton)localObject).setMnemonic(72);
    return localJPanel;
  }
  
  private void setProductData(ProductRow paramProductRow)
  {
    this._mProductCode.setText(paramProductRow.getProductCode());
    this._mProductName.setText(paramProductRow.getProdName());
    this._mStoreCurrentQtyUnit.setText(InternalQuantity.quantityUnits[paramProductRow.getProdUnit()]);
    this._mStoreReturnQtyUnit.setText(InternalQuantity.quantityUnits[paramProductRow.getProdUnit()]);
    this._mWarehouseReturnUnit.setText(InternalQuantity.quantityUnits[paramProductRow.getProdUnit()]);
    this._mDefectiveReturnUnit.setText(InternalQuantity.quantityUnits[paramProductRow.getProdUnit()]);
    this._mSelectedProduct = paramProductRow;
  }
  
  private void deleteClicked()
  {
    int i = this._mReturnTable.getSelectedRow();
    if (i < 0)
    {
      UICommon.showError("No item is selected for deletion. Please select an entry from the table right side for deletion.", "Error", MainWindow.instance);
      return;
    }
    i = this._mReturnTable.convertRowIndexToModel(i);
    ReturnStock localReturnStock = (ReturnStock)this._mReturnModel._mReturns.get(i);
    Object localObject1;
    Object localObject2;
    if ((localReturnStock.fromObject instanceof StoreInfoRow))
    {
      if (localReturnStock.productRow.getProdIndex() == this._mSelectedProduct.getProdIndex())
      {
        localObject1 = (StoreInfoRow)this._mStore.getSelectedItem();
        if ((localObject1 != null) && (this._mStocks.size() > 0))
        {
          localObject2 = (StockAndProductRow)this._mStocks.get(new Integer(((StoreInfoRow)localObject1).getStoreId()));
          if (localObject2 == null)
          {
            this._mStoreCurrentQty.setText(InternalQuantity.toString(localReturnStock.quantity, (short)localReturnStock.productRow.getProdUnit(), false));
          }
          else
          {
            ((StockAndProductRow)localObject2).setStock(((StockAndProductRow)localObject2).getStock() + localReturnStock.quantity);
            this._mStoreCurrentQty.setText(InternalQuantity.toString(((StockAndProductRow)localObject2).getStock(), (short)((StockAndProductRow)localObject2).getProdUnit(), false));
          }
        }
      }
    }
    else
    {
      int j;
      Iterator localIterator;
      Object localObject3;
      String str;
      if ((localReturnStock.fromObject instanceof CurrentStockRow))
      {
        localObject1 = (CurrentStockRow)localReturnStock.fromObject;
        if (localReturnStock.productRow.getProdIndex() == ((CurrentStockRow)localObject1).getProdId())
        {
          localObject2 = this._mWarehouseModel.stocks;
          j = 0;
          localIterator = ((ArrayList)localObject2).iterator();
          while (localIterator.hasNext())
          {
            localObject3 = (CurrentStockRow)localIterator.next();
            if (((CurrentStockRow)localObject3).getStockIndex() == ((CurrentStockRow)localObject1).getStockIndex())
            {
              ((CurrentStockRow)localObject3).setQuantity(((CurrentStockRow)localObject3).getQuantity() + localReturnStock.quantity);
              str = InternalQuantity.toString(((CurrentStockRow)localObject3).getQuantity(), (short)((CurrentStockRow)localObject3).getQuantityUnit(), true) + "   ";
              this._mWarehouseModel.setValueAt(str, j, 2);
              break;
            }
            j++;
          }
        }
      }
      else
      {
        localObject1 = (DefectiveProductWrapper)localReturnStock.fromObject;
        if (localReturnStock.productRow.getProdIndex() == ((DefectiveProductWrapper)localObject1)._mProduct.getProductId())
        {
          localObject2 = this._mDefectivesModel.defectives;
          j = 0;
          localIterator = ((ArrayList)localObject2).iterator();
          while (localIterator.hasNext())
          {
            localObject3 = (DefectiveProductWrapper)localIterator.next();
            if (((DefectiveProductWrapper)localObject3)._mProduct.getObjectIndex() == ((DefectiveProductWrapper)localObject1)._mProduct.getObjectIndex())
            {
              ((DefectiveProductWrapper)localObject3)._mProduct.setQuantity(((DefectiveProductWrapper)localObject3)._mProduct.getQuantity() + localReturnStock.quantity);
              str = InternalQuantity.toString(((DefectiveProductWrapper)localObject3)._mProduct.getQuantity(), (short)this._mSelectedProduct.getProdUnit(), true) + "   ";
              this._mDefectivesModel.setValueAt(str, j, 1);
              break;
            }
            j++;
          }
        }
      }
    }
    this._mReturnModel.removeReturnRow(i);
  }
  
  private void productIdLoadClicked()
  {
    String str = this._mProductCode.getText().trim();
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
        this._mProductCode.requestFocusInWindow();
        return;
      }
      localObject2 = ((ProductSearchPanel)localObject1).getSelectedProducts();
      if ((localObject2 == null) || (localObject2.length == 0))
      {
        this._mProductCode.requestFocusInWindow();
        return;
      }
      setProductData(localObject2[0]);
      try
      {
        loadStockDetailsForProduct(localObject2[0]);
      }
      catch (DBException localDBException2)
      {
        UICommon.showError("Internal error searching product details. Try again later.\n\nIf the problem persists contact administrator.", "Internal Error", MainWindow.instance);
        this._mProductCode.requestFocusInWindow();
        return;
      }
      return;
    }
    try
    {
      localObject1 = "PRODUCT_CODE='" + str + "' ";
      localObject2 = ProductTableDef.getInstance().getAllValuesWithWhereClause((String)localObject1);
      if ((localObject2 == null) || (((ArrayList)localObject2).size() == 0))
      {
        UICommon.showError("No product found.", "Error", MainWindow.instance);
        this._mProductCode.requestFocusInWindow();
        return;
      }
      ProductRow localProductRow = (ProductRow)((ArrayList)localObject2).get(0);
      setProductData(localProductRow);
      loadStockDetailsForProduct(localProductRow);
    }
    catch (DBException localDBException1)
    {
      UICommon.showError("Internal error searching product details. Try again later.\n\nIf the problem persists contact administrator.", "Internal Error", MainWindow.instance);
      return;
    }
  }
  
  private void searchByProductNameClicked()
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
        UICommon.showError("Internal error searching product details. Try again later.\n\nIf the problem persists contact administrator.", "Internal Error", MainWindow.instance);
        this._mProductCode.requestFocusInWindow();
        return;
      }
      return;
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
    }
    catch (DBException localDBException1)
    {
      localDBException1.printStackTrace();
      UICommon.showError("Internal Error searching for product.", "Internal Error", MainWindow.instance);
      this._mProductName.setText(str);
      return;
    }
  }
  
  private void loadStockDetailsForProduct(ProductRow paramProductRow)
    throws DBException
  {
    ArrayList localArrayList1 = StockAndProductTableDef.getInstance().getAllValuesWithWhereClause("PRODUCT_ID=" + paramProductRow.getProdIndex());
    this._mStocks.clear();
    Object localObject2;
    Object localObject3;
    Object localObject4;
    if ((localArrayList1 != null) && (localArrayList1.size() > 0))
    {
      localArrayList2 = this._mReturnModel._mReturns;
      localObject1 = localArrayList1.iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject2 = (DBRow)((Iterator)localObject1).next();
        localObject3 = (StockAndProductRow)localObject2;
        localObject4 = localArrayList2.iterator();
        while (((Iterator)localObject4).hasNext())
        {
          ReturnStock localReturnStock = (ReturnStock)((Iterator)localObject4).next();
          if ((localReturnStock.fromObject instanceof StoreInfoRow))
          {
            StoreInfoRow localStoreInfoRow = (StoreInfoRow)localReturnStock.fromObject;
            int i = localStoreInfoRow.getStoreId();
            if ((((StockAndProductRow)localObject3).getStoreId() == i) && (((StockAndProductRow)localObject3).getProdIndex() == localReturnStock.productRow.getProdIndex())) {
              ((StockAndProductRow)localObject3).setStock(((StockAndProductRow)localObject3).getStock() - localReturnStock.quantity);
            }
          }
        }
        this._mStocks.put(new Integer(((StockAndProductRow)localObject3).getStoreId()), localObject3);
      }
      storeChanged();
      localArrayList1.clear();
    }
    ArrayList localArrayList2 = DefectiveProductTableDef.getInstance().getAllValuesWithWhereClause("PRODUCT_ID=" + paramProductRow.getProdIndex());
    this._mDefectivesModel.removeAllDefectiveRows();
    Object localObject5;
    if ((localArrayList2 != null) && (localArrayList2.size() > 0))
    {
      localObject1 = localArrayList2.iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject2 = (DBRow)((Iterator)localObject1).next();
        localObject3 = (DefectiveProductRow)localObject2;
        localObject4 = null;
        try
        {
          localObject4 = StoreInfoTableDef.getInstance().getStoreForIndex(((DefectiveProductRow)localObject3).getStoreId());
        }
        catch (DBException localDBException)
        {
          localDBException.printStackTrace();
        }
        localObject5 = new DefectiveProductWrapper((DefectiveProductRow)localObject3, (StoreInfoRow)localObject4);
        ((DefectiveProductRow)localObject3).setProductRow(this._mSelectedProduct);
        this._mDefectivesModel.addDefectiveRow((DefectiveProductWrapper)localObject5);
      }
      localArrayList2.clear();
    }
    Object localObject1 = CurrentStockTableDef.getInstance().getAllValuesWithWhereClause("PROD_ID=" + paramProductRow.getProdIndex());
    this._mWarehouseModel.removeAllStockRows();
    if ((localObject1 != null) && (((ArrayList)localObject1).size() > 0))
    {
      localObject2 = ((ArrayList)localObject1).iterator();
      while (((Iterator)localObject2).hasNext())
      {
        localObject3 = (DBRow)((Iterator)localObject2).next();
        localObject4 = (CurrentStockRow)localObject3;
        localObject5 = StockInfoTableDef.getInstance().findRowByIndex(((CurrentStockRow)localObject4).getStockIndex());
        if (localObject5 != null) {
          this._mWarehouseModel.addStockRow((CurrentStockRow)localObject4, ((StockInfoRow)localObject5).getStockDate());
        }
      }
    }
  }
  
  private void storeChanged()
  {
    StoreInfoRow localStoreInfoRow = (StoreInfoRow)this._mStore.getSelectedItem();
    this._mStoreCurrentQty.setText("");
    if ((localStoreInfoRow != null) && (this._mStocks.size() > 0))
    {
      StockAndProductRow localStockAndProductRow = (StockAndProductRow)this._mStocks.get(new Integer(localStoreInfoRow.getStoreId()));
      if (localStockAndProductRow == null) {
        this._mStoreCurrentQty.setText(InternalQuantity.toString(0.0D, (short)this._mSelectedProduct.getProdUnit(), false));
      } else {
        this._mStoreCurrentQty.setText(InternalQuantity.toString(localStockAndProductRow.getStock(), (short)localStockAndProductRow.getProdUnit(), false));
      }
    }
  }
  
  private void addFromProductPanelClicked()
  {
    if (this._mSelectedProduct == null)
    {
      UICommon.showError("No product selected.", "Error", MainWindow.instance);
      this._mProductCode.requestFocusInWindow();
      return;
    }
    String str = this._mStoreReturnQty.getText().trim();
    if (str.length() == 0)
    {
      UICommon.showError("The return quantity cannot be empty.", "Error", MainWindow.instance);
      this._mStoreReturnQty.requestFocusInWindow();
      return;
    }
    int i = InternalQuantity.isUnitFractionAllowed(this._mSelectedProduct.getProdUnit()) ? 5 : 0;
    if (!Validation.isValidFloat(str, i, false))
    {
      UICommon.showError("Invalid quantity.", "Error", MainWindow.instance);
      this._mStoreReturnQty.requestFocusInWindow();
      return;
    }
    double d1 = Double.valueOf(str).doubleValue();
    if (d1 == 0.0D)
    {
      UICommon.showError("Please enter a value greater than zero for the return quantity.", "Error", MainWindow.instance);
      this._mStoreReturnQty.requestFocusInWindow();
      return;
    }
    double d2 = Double.valueOf(this._mStoreCurrentQty.getText()).doubleValue();
    if (d1 > d2)
    {
      UICommon.showError("Return stock cannot be more than the stock available at store.", "Error", MainWindow.instance);
      this._mStoreReturnQty.requestFocusInWindow();
      return;
    }
    StoreInfoRow localStoreInfoRow = (StoreInfoRow)this._mStore.getSelectedItem();
    if ((localStoreInfoRow != null) && (this._mStocks.size() > 0))
    {
      StockAndProductRow localStockAndProductRow = (StockAndProductRow)this._mStocks.get(new Integer(localStoreInfoRow.getStoreId()));
      if (localStockAndProductRow == null)
      {
        UICommon.showError("No stock for the product is available in the selected store.", "Error", MainWindow.instance);
        this._mStoreReturnQty.requestFocusInWindow();
        return;
      }
      if (d1 > localStockAndProductRow.getStock())
      {
        UICommon.showError("Not enough stock available in the store.\nEnter a return stock value less than the stock available in the store.", "Error", MainWindow.instance);
        this._mStoreReturnQty.requestFocusInWindow();
        return;
      }
      ReturnStock localReturnStock = new ReturnStock(this._mSelectedProduct, d1, localStoreInfoRow, localStockAndProductRow);
      this._mReturnModel.addReturnRow(localReturnStock);
      localStockAndProductRow.setStock(localStockAndProductRow.getStock() - d1);
      this._mStoreCurrentQty.setText(InternalQuantity.toString(localStockAndProductRow.getStock(), (short)localStockAndProductRow.getProdUnit(), false));
    }
  }
  
  private void addFromTabClicked()
  {
    int i = this._mTabPane.getSelectedIndex();
    String str;
    int j;
    double d;
    int k;
    Object localObject1;
    Object localObject2;
    if (i == 0)
    {
      str = this._mWarehouseReturn.getText().trim();
      if (str.length() == 0)
      {
        UICommon.showError("Return Quantity cannot be empty.", "Error", MainWindow.instance);
        this._mWarehouseReturn.requestFocusInWindow();
        return;
      }
      j = InternalQuantity.isUnitFractionAllowed(this._mSelectedProduct.getProdUnit()) ? 5 : 0;
      if (!Validation.isValidFloat(str, j, false))
      {
        UICommon.showError("Invalid quantity.", "Error", MainWindow.instance);
        this._mWarehouseReturn.requestFocusInWindow();
        return;
      }
      d = Double.valueOf(str).doubleValue();
      if (d == 0.0D)
      {
        UICommon.showError("Please enter a value greater than zero for return quantity.", "Error", MainWindow.instance);
        this._mWarehouseReturn.requestFocusInWindow();
        return;
      }
      k = this._mWarehouseTable.getSelectedRow();
      if (k < 0)
      {
        UICommon.showError("No stock is selected from warehouse.", "Error", MainWindow.instance);
        this._mWarehouseTable.requestFocusInWindow();
        return;
      }
      k = this._mWarehouseTable.convertRowIndexToModel(k);
      localObject1 = (CurrentStockRow)this._mWarehouseModel.stocks.get(k);
      if (((CurrentStockRow)localObject1).getQuantity() < d)
      {
        UICommon.showError("Return quantity cannot be more than the available stock selected.", "Error", MainWindow.instance);
        this._mWarehouseReturn.requestFocusInWindow();
        return;
      }
      this._mWarehouseModel.reduceStockFromIndex(k, d);
      localObject2 = null;
      try
      {
        localObject2 = WearehouseInfoTableDef.getInstance().getWarehouseForIndex(((CurrentStockRow)localObject1).getWearHouseIndex());
      }
      catch (DBException localDBException)
      {
        localDBException.printStackTrace();
      }
      ReturnStock localReturnStock = new ReturnStock(this._mSelectedProduct, d, localObject1, localObject2);
      this._mReturnModel.addReturnRow(localReturnStock);
    }
    else
    {
      str = this._mDefectiveReturn.getText().trim();
      if (str.length() == 0)
      {
        UICommon.showError("Return Quantity cannot be empty.", "Error", MainWindow.instance);
        this._mDefectiveReturn.requestFocusInWindow();
        return;
      }
      j = InternalQuantity.isUnitFractionAllowed(this._mSelectedProduct.getProdUnit()) ? 5 : 0;
      if (!Validation.isValidFloat(str, j, false))
      {
        UICommon.showError("Invalid quantity.", "Error", MainWindow.instance);
        this._mDefectiveReturn.requestFocusInWindow();
        return;
      }
      d = Double.valueOf(str).doubleValue();
      if (d == 0.0D)
      {
        UICommon.showError("Please enter a value greater than zero for return quantity.", "Error", MainWindow.instance);
        this._mDefectiveReturn.requestFocusInWindow();
        return;
      }
      k = this._mDefectivesTable.getSelectedRow();
      if (k < 0)
      {
        UICommon.showError("No stock is selected from warehouse.", "Error", MainWindow.instance);
        this._mDefectivesTable.requestFocusInWindow();
        return;
      }
      k = this._mDefectivesTable.convertRowIndexToModel(k);
      localObject1 = (DefectiveProductWrapper)this._mDefectivesModel.defectives.get(k);
      if (((DefectiveProductWrapper)localObject1)._mProduct.getQuantity() < d)
      {
        UICommon.showError("Return quantity cannot be more than the total defectives selected.", "Error", MainWindow.instance);
        this._mDefectiveReturn.requestFocusInWindow();
        return;
      }
      localObject2 = new ReturnStock(this._mSelectedProduct, d, localObject1);
      this._mReturnModel.addReturnRow((ReturnStock)localObject2);
      this._mDefectivesModel.reduceQuantityForIndex(k, d);
    }
  }
  
  private void okClicked()
  {
    if (this._mReturnModel.getRowCount() == 0)
    {
      UICommon.showError("No item is selected for returning.", "Error", MainWindow.instance);
      this._mProductCode.requestFocusInWindow();
      return;
    }
    VendorRow localVendorRow = (VendorRow)this._mReturnToVendor.getSelectedItem();
    if (localVendorRow == null)
    {
      UICommon.showError("Please select a vendor to which the item has be returned.", "Error", MainWindow.instance);
      this._mReturnToVendor.requestFocusInWindow();
      return;
    }
    StockReturnRow localStockReturnRow = StockReturnTableDef.getInstance().getNewRow();
    java.sql.Date localDate = new java.sql.Date(new java.util.Date().getTime());
    Time localTime = new Time(new java.util.Date().getTime());
    localStockReturnRow.setValues(localVendorRow.getVendorId(), localDate, localTime, UserInfoTableDef.getCurrentUser().getUserIndex());
    DBConnection localDBConnection = Db.getConnection();
    try
    {
      localDBConnection.openTrans();
      localStockReturnRow.create();
    }
    catch (DBException localDBException1)
    {
      localDBConnection.rollbackNoExp();
      localDBException1.printStackTrace();
      UICommon.showError("Internal error updating database.\n\nTry again later. If the problem persists contact administrator.", "Internal Error", MainWindow.instance);
      return;
    }
    ArrayList localArrayList = this._mReturnModel._mReturns;
    StockReturnEntryTableDef localStockReturnEntryTableDef = StockReturnEntryTableDef.getInstance();
    int i = 1;
    long l = localStockReturnRow.getTxnNo();
    try
    {
      Iterator localIterator = localArrayList.iterator();
      ReturnStock localReturnStock;
      Object localObject1;
      Object localObject2;
      while (localIterator.hasNext())
      {
        localReturnStock = (ReturnStock)localIterator.next();
        localObject1 = localStockReturnEntryTableDef.getNewRow();
        localObject2 = "D";
        int k = -1;
        int m = -1;
        if ((localReturnStock.fromObject instanceof StoreInfoRow))
        {
          localObject2 = "S";
          k = ((StoreInfoRow)localReturnStock.fromObject).getStoreId();
        }
        else if ((localReturnStock.fromObject instanceof CurrentStockRow))
        {
          localObject2 = "W";
          k = ((CurrentStockRow)localReturnStock.fromObject).getWearHouseIndex();
          m = ((CurrentStockRow)localReturnStock.fromObject).getCurStockIndex();
        }
        else
        {
          DefectiveProductWrapper localDefectiveProductWrapper = (DefectiveProductWrapper)localReturnStock.fromObject;
          k = localDefectiveProductWrapper._mStore.getStoreId();
        }
        ((StockReturnEntryRow)localObject1).setValues(l, localReturnStock.productRow.getProdIndex(), localReturnStock.quantity, (String)localObject2, k, i, m);
        ((StockReturnEntryRow)localObject1).create();
        i++;
      }
      localIterator = localArrayList.iterator();
      while (localIterator.hasNext())
      {
        localReturnStock = (ReturnStock)localIterator.next();
        if ((localReturnStock.fromObject instanceof StoreInfoRow))
        {
          localObject1 = (StockAndProductRow)localReturnStock.internalObject;
          ((StockAndProductRow)localObject1).setStock(((StockAndProductRow)localObject1).getStock() - localReturnStock.quantity);
          localObject2 = StoreStockTableDef.getInstance().getAllValuesWithWhereClause("STORE_ID=" + ((StockAndProductRow)localObject1).getStoreId() + " AND PRODUCT_ID=" + ((StockAndProductRow)localObject1).getProdIndex());
          if ((localObject2 == null) || (((ArrayList)localObject2).size() == 0)) {
            throw new DBException("No stock rows found.", "Get STORE_STOCK based on StockAndProductRow.", "Check SQL", null, null);
          }
          StoreStockRow localStoreStockRow = (StoreStockRow)((ArrayList)localObject2).get(0);
          localStoreStockRow.setStock(((StockAndProductRow)localObject1).getStock());
          localStoreStockRow.update(true);
        }
        else if ((localReturnStock.fromObject instanceof CurrentStockRow))
        {
          localObject1 = (CurrentStockRow)localReturnStock.fromObject;
          ((CurrentStockRow)localObject1).update(true);
        }
        else
        {
          localObject1 = (DefectiveProductWrapper)localReturnStock.fromObject;
          ((DefectiveProductWrapper)localObject1)._mProduct.setQuantitySent(localReturnStock.quantity);
          ((DefectiveProductWrapper)localObject1)._mProduct.update(true);
        }
      }
      localDBConnection.commit();
      int j = UICommon.showQuestion("Records updated successfully. Transaction number :" + l + "\n\nDo you want to print it ?", "Success", MainWindow.instance);
      if (j != 1) {
        return;
      }
      try
      {
        Print.getInstance().printStockReturn(localStockReturnRow, localArrayList, localVendorRow);
      }
      catch (JePrinterException localJePrinterException)
      {
        localJePrinterException.printStackTrace();
        UICommon.showError("Error printing the bill.\nDetails:\n" + localJePrinterException.toString(), "Printing Error", MainWindow.instance);
        return;
      }
    }
    catch (DBException localDBException2)
    {
      localDBConnection.rollbackNoExp();
      localDBException2.printStackTrace();
      UICommon.showError("Internal error updating database.\n\nTry again later. If the problem persists contact administrator.", "Internal Error", MainWindow.instance);
      return;
    }
  }
  
  private class DefectiveProductWrapper
  {
    private DefectiveProductRow _mProduct = null;
    private StoreInfoRow _mStore = null;
    
    public DefectiveProductWrapper(DefectiveProductRow paramDefectiveProductRow, StoreInfoRow paramStoreInfoRow)
    {
      this._mStore = paramStoreInfoRow;
      this._mProduct = paramDefectiveProductRow;
    }
  }
  
  private class ReturnProductTableModel
    extends DefaultTableModel
  {
    String[] columns = { "Code", "Name", "Quantity", "From" };
    private ArrayList<ReturnStock> _mReturns = new ArrayList();
    
    private ReturnProductTableModel() {}
    
    public int getColumnCount()
    {
      return this.columns.length;
    }
    
    public Class getColumnClass(int paramInt)
    {
      if (paramInt == 2) {
        return Integer.class;
      }
      return String.class;
    }
    
    public String getColumnName(int paramInt)
    {
      return this.columns[paramInt];
    }
    
    public void removeReturnRow(int paramInt)
    {
      super.removeRow(paramInt);
      this._mReturns.remove(paramInt);
    }
    
    public void addReturnRow(ReturnStock paramReturnStock)
    {
      String str1 = " Error ";
      int j;
      Object localObject2;
      Object localObject3;
      Object localObject4;
      if ((paramReturnStock.fromObject instanceof StoreInfoRow))
      {
        int i = ((StoreInfoRow)paramReturnStock.fromObject).getStoreId();
        j = -1;
        Iterator localIterator = this._mReturns.iterator();
        while (localIterator.hasNext())
        {
          localObject2 = (ReturnStock)localIterator.next();
          j++;
          if ((((ReturnStock)localObject2).fromObject instanceof StoreInfoRow))
          {
            localObject3 = (StoreInfoRow)((ReturnStock)localObject2).fromObject;
            if ((((StoreInfoRow)localObject3).getStoreId() == i) && (((ReturnStock)localObject2).productRow.getProdIndex() == paramReturnStock.productRow.getProdIndex()))
            {
              localObject2.quantity += paramReturnStock.quantity;
              localObject4 = InternalQuantity.toString(((ReturnStock)localObject2).quantity, (short)((ReturnStock)localObject2).productRow.getProdUnit(), true);
              setValueAt(localObject4, j, 2);
              return;
            }
          }
        }
        str1 = "Store : " + paramReturnStock.fromObject.toString();
      }
      else
      {
        Object localObject5;
        if ((paramReturnStock.fromObject instanceof CurrentStockRow))
        {
          localObject1 = (CurrentStockRow)paramReturnStock.fromObject;
          j = ((CurrentStockRow)localObject1).getStockIndex();
          int k = -1;
          localObject2 = this._mReturns.iterator();
          while (((Iterator)localObject2).hasNext())
          {
            localObject3 = (ReturnStock)((Iterator)localObject2).next();
            k++;
            if ((((ReturnStock)localObject3).fromObject instanceof CurrentStockRow))
            {
              localObject4 = (CurrentStockRow)((ReturnStock)localObject3).fromObject;
              if (((CurrentStockRow)localObject4).getStockIndex() == j)
              {
                localObject3.quantity += paramReturnStock.quantity;
                localObject5 = InternalQuantity.toString(((ReturnStock)localObject3).quantity, (short)((ReturnStock)localObject3).productRow.getProdUnit(), true);
                setValueAt(localObject5, k, 2);
                return;
              }
            }
          }
          localObject2 = (WearehouseInfoRow)paramReturnStock.internalObject;
          str1 = "Warehouse : " + (localObject2 == null ? "-NA-" : ((WearehouseInfoRow)localObject2).toString());
        }
        else
        {
          localObject1 = (StockReturnPanel.DefectiveProductWrapper)paramReturnStock.fromObject;
          long l = ((StockReturnPanel.DefectiveProductWrapper)localObject1)._mProduct.getObjectIndex();
          int m = -1;
          localObject3 = this._mReturns.iterator();
          while (((Iterator)localObject3).hasNext())
          {
            localObject4 = (ReturnStock)((Iterator)localObject3).next();
            m++;
            if ((((ReturnStock)localObject4).fromObject instanceof StockReturnPanel.DefectiveProductWrapper))
            {
              localObject5 = (StockReturnPanel.DefectiveProductWrapper)((ReturnStock)localObject4).fromObject;
              if (((StockReturnPanel.DefectiveProductWrapper)localObject5)._mProduct.getObjectIndex() == l)
              {
                localObject4.quantity += paramReturnStock.quantity;
                String str2 = InternalQuantity.toString(((ReturnStock)localObject4).quantity, (short)((ReturnStock)localObject4).productRow.getProdUnit(), true);
                setValueAt(str2, m, 2);
                return;
              }
            }
          }
          str1 = "Defective in " + ((StockReturnPanel.DefectiveProductWrapper)localObject1)._mStore;
        }
      }
      Object localObject1 = InternalQuantity.toString(paramReturnStock.quantity, (short)paramReturnStock.productRow.getProdUnit(), true);
      String[] arrayOfString = { paramReturnStock.productRow.getProductCode(), paramReturnStock.productRow.getProdName(), localObject1, str1 };
      addRow(arrayOfString);
      this._mReturns.add(paramReturnStock);
    }
    
    private void removeAllReturnRows()
    {
      this._mReturns.clear();
      int i = getRowCount();
      for (int j = 0; j < i; j++) {
        removeRow(0);
      }
    }
  }
  
  private class WarehouseProductTableModel
    extends DefaultTableModel
  {
    String[] columns = { "Warehouse", "Date", "Quantity" };
    ArrayList<CurrentStockRow> stocks = new ArrayList();
    
    private WarehouseProductTableModel() {}
    
    public int getColumnCount()
    {
      return this.columns.length;
    }
    
    public Class getColumnClass(int paramInt)
    {
      if (paramInt == 2) {
        return Integer.class;
      }
      return String.class;
    }
    
    public String getColumnName(int paramInt)
    {
      return this.columns[paramInt];
    }
    
    public void reduceStockFromIndex(int paramInt, double paramDouble)
    {
      CurrentStockRow localCurrentStockRow = (CurrentStockRow)this.stocks.get(paramInt);
      localCurrentStockRow.setQuantity(localCurrentStockRow.getQuantity() - paramDouble);
      String str = InternalQuantity.toString(localCurrentStockRow.getQuantity(), (short)localCurrentStockRow.getQuantityUnit(), true) + "   ";
      setValueAt(str, paramInt, 2);
    }
    
    public void addStockRow(CurrentStockRow paramCurrentStockRow, java.util.Date paramDate)
    {
      Object localObject1 = StockReturnPanel.access$1500(StockReturnPanel.this)._mReturns.iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject2 = (ReturnStock)((Iterator)localObject1).next();
        if ((((ReturnStock)localObject2).fromObject instanceof CurrentStockRow))
        {
          CurrentStockRow localCurrentStockRow = (CurrentStockRow)((ReturnStock)localObject2).fromObject;
          if (localCurrentStockRow.getStockIndex() == paramCurrentStockRow.getStockIndex())
          {
            paramCurrentStockRow.setQuantity(paramCurrentStockRow.getQuantity() - ((ReturnStock)localObject2).quantity);
            break;
          }
        }
      }
      localObject1 = new SimpleDateFormat(CommonConfig.getInstance().dateFormat);
      Object localObject2 = null;
      try
      {
        localObject2 = WearehouseInfoTableDef.getInstance().getWarehouseForIndex(paramCurrentStockRow.getWearHouseIndex());
      }
      catch (DBException localDBException)
      {
        localDBException.printStackTrace();
      }
      String str = "NA";
      if (localObject2 != null) {
        str = ((WearehouseInfoRow)localObject2).getWearehouseName();
      }
      String[] arrayOfString = { str, ((SimpleDateFormat)localObject1).format(paramDate), InternalQuantity.toString(paramCurrentStockRow.getQuantity(), (short)paramCurrentStockRow.getQuantityUnit(), true) + "   " };
      addRow(arrayOfString);
      this.stocks.add(paramCurrentStockRow);
    }
    
    public void removeAllStockRows()
    {
      this.stocks.clear();
      int i = getRowCount();
      for (int j = 0; j < i; j++) {
        removeRow(0);
      }
    }
  }
  
  private class DefectiveProductTableModel
    extends DefaultTableModel
  {
    String[] columns = { "Store", "Quantity", "Recieved" };
    ArrayList<StockReturnPanel.DefectiveProductWrapper> defectives = new ArrayList();
    
    private DefectiveProductTableModel() {}
    
    public int getColumnCount()
    {
      return this.columns.length;
    }
    
    public String getColumnName(int paramInt)
    {
      return this.columns[paramInt];
    }
    
    public Class getColumnClass(int paramInt)
    {
      if (paramInt == 1) {
        return Integer.class;
      }
      return String.class;
    }
    
    public void reduceQuantityForIndex(int paramInt, double paramDouble)
    {
      StockReturnPanel.DefectiveProductWrapper localDefectiveProductWrapper = (StockReturnPanel.DefectiveProductWrapper)this.defectives.get(paramInt);
      localDefectiveProductWrapper._mProduct.setQuantity(localDefectiveProductWrapper._mProduct.getQuantity() - paramDouble);
      String str = InternalQuantity.toString(localDefectiveProductWrapper._mProduct.getQuantity(), (short)StockReturnPanel.this._mSelectedProduct.getProdUnit(), true) + "   ";
      setValueAt(str, paramInt, 1);
    }
    
    public void removeAllDefectiveRows()
    {
      this.defectives.clear();
      int i = getRowCount();
      for (int j = 0; j < i; j++) {
        removeRow(0);
      }
    }
    
    public void addDefectiveRow(StockReturnPanel.DefectiveProductWrapper paramDefectiveProductWrapper)
    {
      Object localObject1 = StockReturnPanel.access$1500(StockReturnPanel.this)._mReturns.iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject2 = (ReturnStock)((Iterator)localObject1).next();
        if ((((ReturnStock)localObject2).fromObject instanceof StockReturnPanel.DefectiveProductWrapper))
        {
          StockReturnPanel.DefectiveProductWrapper localDefectiveProductWrapper = (StockReturnPanel.DefectiveProductWrapper)((ReturnStock)localObject2).fromObject;
          if (localDefectiveProductWrapper._mProduct.getObjectIndex() == paramDefectiveProductWrapper._mProduct.getObjectIndex())
          {
            paramDefectiveProductWrapper._mProduct.setQuantity(paramDefectiveProductWrapper._mProduct.getQuantity() - ((ReturnStock)localObject2).quantity);
            break;
          }
        }
      }
      localObject1 = new SimpleDateFormat(CommonConfig.getInstance().dateFormat);
      Object localObject2 = { paramDefectiveProductWrapper._mStore.getName(), InternalQuantity.toString(paramDefectiveProductWrapper._mProduct.getQuantity(), (short)StockReturnPanel.this._mSelectedProduct.getProdUnit(), true) + "   ", ((SimpleDateFormat)localObject1).format(paramDefectiveProductWrapper._mProduct.getRecieveDate()) };
      this.defectives.add(paramDefectiveProductWrapper);
      addRow((Object[])localObject2);
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.inv.StockReturnPanel
 * JD-Core Version:    0.7.0.1
 */