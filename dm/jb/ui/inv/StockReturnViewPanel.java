package dm.jb.ui.inv;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.db.objects.CurrentStockRow;
import dm.jb.db.objects.ProductRow;
import dm.jb.db.objects.StockReturnRow;
import dm.jb.db.objects.StockReturnTableDef;
import dm.jb.db.objects.StoreInfoRow;
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
import dm.tools.db.DBConnection;
import dm.tools.db.DBException;
import dm.tools.db.DBRow;
import dm.tools.db.Db;
import dm.tools.types.InternalQuantity;
import dm.tools.ui.AbstractMainPanel;
import dm.tools.ui.ProgressWindow;
import dm.tools.ui.ProgressWindowAction;
import dm.tools.ui.UICommon;
import dm.tools.ui.components.DBUIResultPanel;
import dm.tools.ui.components.JBSearchButton;
import dm.tools.utils.CommonConfig;
import dm.tools.utils.Validation;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class StockReturnViewPanel
  extends AbstractMainPanel
{
  public static StockReturnViewPanel INSTANCE = new StockReturnViewPanel();
  private JTextField _mTxnNo = null;
  private JTextField _mCreatedBy = null;
  private JTextField _mCreatedDate = null;
  private JTextField _mVendor = null;
  private JTable _mTable = null;
  private ReturnProductTableModel _mModel = null;
  private ArrayList<Object[]> _mReturnListForSearch = null;
  private StockReturnRow _mSelectedStockReturnRow = null;
  private ArrayList<ReturnStock> _mEntries = null;
  private VendorRow _mVendorRow = null;
  
  private StockReturnViewPanel()
  {
    initUI();
  }
  
  public void setDefaultFocus()
  {
    this._mTxnNo.requestFocusInWindow();
  }
  
  public JPanel getPanel()
  {
    return this;
  }
  
  public void windowDisplayed()
  {
    this._mTable.requestFocusInWindow();
  }
  
  public void clearAllFields()
  {
    this._mModel.removeAllReturnRows();
    this._mTxnNo.setText("");
    this._mCreatedBy.setText("");
    this._mCreatedDate.setText("");
    this._mVendor.setText("");
    this._mVendorRow = null;
    if (this._mEntries != null) {
      this._mEntries.clear();
    }
    this._mEntries = null;
    this._mSelectedStockReturnRow = null;
  }
  
  private void initUI()
  {
    setLayout(new FormLayout("10px,pref:grow,10px", "10px,pref,20px,400px:grow,20px,30px,10px"));
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(2, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getMainPanel(), localCellConstraints);
    this._mModel = new ReturnProductTableModel(null);
    this._mTable = new JTable(this._mModel);
    localCellConstraints.xywh(2, 4, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(new JScrollPane(this._mTable), localCellConstraints);
    this._mTable.setRowHeight(25);
    localCellConstraints.xywh(1, 5, 3, 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(new JSeparator(), localCellConstraints);
    localCellConstraints.xywh(2, 6, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getButtonPanel(), localCellConstraints);
  }
  
  private JPanel getMainPanel()
  {
    JPanel localJPanel = new JPanel();
    localJPanel.setLayout(new FormLayout("90px,10px, 150px,3px,30px,pref:grow", "25px,10px,25px,10px,25px,10px,25px"));
    CellConstraints localCellConstraints = new CellConstraints();
    JLabel localJLabel = new JLabel("Transaction : ");
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mTxnNo = new JTextField();
    localCellConstraints.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mTxnNo, localCellConstraints);
    this._mTxnNo.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 114) {
          StockReturnViewPanel.this.txnSearchClicked();
        }
      }
    });
    JBSearchButton localJBSearchButton = new JBSearchButton(false);
    localCellConstraints.xywh(5, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJBSearchButton, localCellConstraints);
    localJBSearchButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        StockReturnViewPanel.this.txnSearchClicked();
      }
    });
    localJLabel = new JLabel("Created By : ");
    localCellConstraints.xywh(1, 3, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mCreatedBy = new JTextField();
    localCellConstraints.xywh(3, 3, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mCreatedBy, localCellConstraints);
    this._mCreatedBy.setEditable(false);
    localJLabel = new JLabel("Create Date : ");
    localCellConstraints.xywh(1, 5, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mCreatedDate = new JTextField();
    localCellConstraints.xywh(3, 5, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mCreatedDate, localCellConstraints);
    this._mCreatedDate.setEditable(false);
    localJLabel = new JLabel("Vendor : ");
    localCellConstraints.xywh(1, 7, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mVendor = new JTextField();
    localCellConstraints.xywh(3, 7, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mVendor, localCellConstraints);
    this._mVendor.setEditable(false);
    return localJPanel;
  }
  
  private JPanel getButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    localJPanel.setLayout(new FormLayout("100px,30px,100px,30px,100px,30px:grow,100px", "30px"));
    CellConstraints localCellConstraints = new CellConstraints();
    Object localObject = new JButton("Print");
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        StockReturnViewPanel.this.printClicked();
      }
    });
    localObject = new JButton("Delete");
    localCellConstraints.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        StockReturnViewPanel.this.deleteClicked();
      }
    });
    localObject = new JButton("Close");
    localCellConstraints.xywh(5, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        StockReturnViewPanel.this.closeWindow();
      }
    });
    localObject = new HelpButton("ISP_VIEW_GOODS_RETURN");
    localCellConstraints.xywh(7, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    return localJPanel;
  }
  
  private void txnSearchClicked()
  {
    String str = this._mTxnNo.getText().trim();
    if (str.length() > 0)
    {
      if (!Validation.isValidInt(str, false))
      {
        UICommon.showError("Invalid transaction number.", "Error", MainWindow.instance);
        this._mTxnNo.requestFocusInWindow();
        return;
      }
      long l = Long.valueOf(str).longValue();
      try
      {
        DBRow localDBRow = StockReturnTableDef.getInstance().findRowByIndex(l);
        if (localDBRow == null)
        {
          UICommon.showError("Goods return records not found for the transaction number.", "Error", MainWindow.instance);
          this._mTxnNo.requestFocusInWindow();
          return;
        }
        setReturnStock((StockReturnRow)localDBRow);
      }
      catch (DBException localDBException)
      {
        localDBException.printStackTrace();
        UICommon.showError("Internal error searching for stock return details.\n\nTry again later. If the problem persists contact administrator.", "Internal Error", MainWindow.instance);
        return;
      }
    }
    else
    {
      showAllStcokReturnDetails();
    }
  }
  
  private void setReturnStock(StockReturnRow paramStockReturnRow)
  {
    clearAllFields();
    this._mTxnNo.setText(paramStockReturnRow.getTxnNo() + "");
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(CommonConfig.getInstance().dateFormat);
    this._mCreatedDate.setText(localSimpleDateFormat.format(paramStockReturnRow.getCreateDate()));
    this._mSelectedStockReturnRow = paramStockReturnRow;
    try
    {
      UserInfoRow localUserInfoRow = (UserInfoRow)UserInfoTableDef.getInstance().findRowByIndex(paramStockReturnRow.getUser());
      if (localUserInfoRow != null) {
        this._mCreatedBy.setText(localUserInfoRow.getUserName());
      }
      VendorRow localVendorRow = VendorTableDef.getInstance().getVendorById((int)paramStockReturnRow.getVendor());
      if (localVendorRow != null) {
        this._mVendor.setText(localVendorRow.getVendorName());
      }
      this._mVendorRow = localVendorRow;
      ArrayList localArrayList = paramStockReturnRow.getReturnStockEntries();
      this._mEntries = localArrayList;
      if ((localArrayList == null) || (localArrayList.size() == 0)) {
        return;
      }
      Iterator localIterator = localArrayList.iterator();
      while (localIterator.hasNext())
      {
        ReturnStock localReturnStock = (ReturnStock)localIterator.next();
        this._mModel.addReturnRow(localReturnStock);
      }
    }
    catch (DBException localDBException)
    {
      localDBException.printStackTrace();
      UICommon.showError("Internal error displaying the goods return details.\n\nTry again later. If the problem persists contact administrator.", "Internal Error", MainWindow.instance);
      return;
    }
  }
  
  private void showAllStcokReturnDetails()
  {
    ProgressWindowAction local6 = new ProgressWindowAction()
    {
      SimpleDateFormat fmt = new SimpleDateFormat(CommonConfig.getInstance().dateFormat);
      
      public void startAction()
      {
        ArrayList localArrayList1 = new ArrayList();
        StockReturnViewPanel.this._mReturnListForSearch = null;
        try
        {
          ArrayList localArrayList2 = StockReturnTableDef.getInstance().getAllValues();
          if ((localArrayList2 == null) || (localArrayList2.size() == 0)) {
            return;
          }
          Iterator localIterator = localArrayList2.iterator();
          while (localIterator.hasNext())
          {
            DBRow localDBRow = (DBRow)localIterator.next();
            StockReturnRow localStockReturnRow = (StockReturnRow)localDBRow;
            Object[] arrayOfObject = new Object[5];
            arrayOfObject[0] = (localStockReturnRow.getTxnNo() + "   ");
            arrayOfObject[1] = this.fmt.format(localStockReturnRow.getCreateDate());
            VendorRow localVendorRow = VendorTableDef.getInstance().getVendorById((int)localStockReturnRow.getVendor());
            if (localVendorRow != null) {
              arrayOfObject[2] = localVendorRow.getVendorName();
            } else {
              arrayOfObject[2] = "-NA-";
            }
            UserInfoRow localUserInfoRow = (UserInfoRow)UserInfoTableDef.getInstance().findRowByIndex(localStockReturnRow.getUser());
            if (localUserInfoRow != null) {
              arrayOfObject[3] = localUserInfoRow.getUserName();
            } else {
              arrayOfObject[3] = "-NA-";
            }
            arrayOfObject[4] = localStockReturnRow;
            localArrayList1.add(arrayOfObject);
          }
          StockReturnViewPanel.this._mReturnListForSearch = localArrayList1;
          SwingUtilities.invokeLater(new Runnable()
          {
            public void run()
            {
              StockReturnViewPanel.this.showRowDataInSearchWindow();
            }
          });
        }
        catch (DBException localDBException)
        {
          localDBException.printStackTrace();
          UICommon.showDelayedErrorMessage("Internal error searching stock return details.\n\nTry again later. If the problem persists contact administrator.", "Internal Error", MainWindow.instance);
          return;
        }
      }
      
      public String getActionName()
      {
        return "Searching for stock return records";
      }
    };
    ProgressWindow localProgressWindow = new ProgressWindow(MainWindow.instance, local6);
    localProgressWindow.startProgress();
  }
  
  private void showRowDataInSearchWindow()
  {
    String[] arrayOfString = { "Txn No", "Create Date", "Vendor", "User" };
    DBUIResultPanel localDBUIResultPanel = new DBUIResultPanel(arrayOfString, MainWindow.instance, true, true);
    localDBUIResultPanel.setArrayData(this._mReturnListForSearch);
    localDBUIResultPanel.setTitle("Goods return transactions");
    localDBUIResultPanel.setLocationRelativeTo(MainWindow.instance);
    localDBUIResultPanel.setVisible(true);
    if (localDBUIResultPanel.isCancelled()) {
      return;
    }
    Object[] arrayOfObject = (Object[])localDBUIResultPanel.getSelectedRow();
    setReturnStock((StockReturnRow)arrayOfObject[4]);
  }
  
  private void printClicked()
  {
    try
    {
      Print.getInstance().printStockReturn(this._mSelectedStockReturnRow, this._mEntries, this._mVendorRow);
    }
    catch (JePrinterException localJePrinterException)
    {
      localJePrinterException.printStackTrace();
      UICommon.showError("Error printing the bill.\nDetails:\n" + localJePrinterException.toString(), "Printing Error", MainWindow.instance);
      return;
    }
  }
  
  private void deleteClicked()
  {
    if (this._mSelectedStockReturnRow == null)
    {
      UICommon.showError("No good return transaction is loaded, for deletion.", "Error", MainWindow.instance);
      this._mTxnNo.requestFocusInWindow();
      return;
    }
    int i = UICommon.showQuestion("Are you sure you want to delete the selected returnb stock transaction ? ", "Confirm Deleteion", MainWindow.instance);
    if (i != 1) {
      return;
    }
    DBConnection localDBConnection = Db.getConnection();
    try
    {
      localDBConnection.openTrans();
      this._mSelectedStockReturnRow.delete();
      localDBConnection.commit();
    }
    catch (DBException localDBException)
    {
      localDBConnection.rollbackNoExp();
      localDBException.printStackTrace();
      UICommon.showError("Internal error removing the goods return transaction records.\n\nTry again later. If the problem persists contact administrator.", "Internal Error", MainWindow.instance);
      return;
    }
    clearAllFields();
    UICommon.showMessage("The transaction removed successfully.", "Success", MainWindow.instance);
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
      String str = " Error ";
      if ((paramReturnStock.fromObject instanceof StoreInfoRow))
      {
        str = "Store : " + paramReturnStock.fromObject.toString();
      }
      else if ((paramReturnStock.fromObject instanceof CurrentStockRow))
      {
        localObject = (CurrentStockRow)paramReturnStock.fromObject;
        try
        {
          WearehouseInfoRow localWearehouseInfoRow = WearehouseInfoTableDef.getInstance().getWarehouseForIndex(((CurrentStockRow)localObject).getWearHouseIndex());
          str = "Warehouse : " + localWearehouseInfoRow.toString();
        }
        catch (DBException localDBException)
        {
          localDBException.printStackTrace();
        }
      }
      else
      {
        str = "Defective in " + (StoreInfoRow)paramReturnStock.internalObject;
      }
      Object localObject = InternalQuantity.toString(paramReturnStock.quantity, paramReturnStock.productRow == null ? -1 : (short)paramReturnStock.productRow.getProdUnit(), true);
      String[] arrayOfString = { paramReturnStock.productRow == null ? "NA" : paramReturnStock.productRow.getProductCode(), paramReturnStock.productRow == null ? "NA" : paramReturnStock.productRow.getProdName(), (String)localObject + "  ", "  " + str };
      addRow(arrayOfString);
      this._mReturns.add(paramReturnStock);
    }
    
    public boolean isCellEditable(int paramInt1, int paramInt2)
    {
      return false;
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
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.inv.StockReturnViewPanel
 * JD-Core Version:    0.7.0.1
 */