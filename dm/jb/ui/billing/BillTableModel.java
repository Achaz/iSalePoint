package dm.jb.ui.billing;

import dm.jb.JeException;
import dm.jb.op.bill.BillProduct;
import dm.jb.ui.MainWindow;
import dm.tools.types.InternalInteger;
import dm.tools.ui.UICommon;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.table.DefaultTableModel;

public class BillTableModel
  extends DefaultTableModel
{
  ArrayList<BillingColumn> _mColumns = null;
  private int _mTotalRows = 0;
  private ArrayList<BillProduct> _mBps = new ArrayList();
  
  public BillTableModel()
  {
    initModel();
  }
  
  public void initModel()
  {
    try
    {
      this._mColumns = BillingUI.getViewColumns();
    }
    catch (JeException localJeException)
    {
      UICommon.showError("Error loading the selected column list.\nContact administrator.", "Error", MainWindow.instance);
      return;
    }
    this._mBps.clear();
  }
  
  public int getRowCount()
  {
    return this._mTotalRows;
  }
  
  public int getColumnCount()
  {
    return this._mColumns.size();
  }
  
  public void setValueAt(Object paramObject, int paramInt1, int paramInt2)
  {
    super.setValueAt(paramObject, paramInt1, paramInt2);
  }
  
  public void updateProduct(int paramInt, BillProduct paramBillProduct)
  {
    Iterator localIterator = this._mColumns.iterator();
    for (int i = 0; localIterator.hasNext(); i++)
    {
      BillingColumn localBillingColumn = (BillingColumn)localIterator.next();
      Object localObject = paramBillProduct.getDataForColCode(localBillingColumn._mCode);
      setValueAt(localObject, paramInt, i);
    }
  }
  
  public void addBillProduct(BillProduct paramBillProduct)
  {
    Iterator localIterator = this._mColumns.iterator();
    int i = 0;
    Object[] arrayOfObject = new Object[getColumnCount()];
    while (localIterator.hasNext())
    {
      BillingColumn localBillingColumn = (BillingColumn)localIterator.next();
      Object localObject = paramBillProduct.getDataForColCode(localBillingColumn._mCode);
      arrayOfObject[i] = localObject;
      i++;
    }
    this._mTotalRows += 1;
    insertRow(this._mTotalRows - 1, arrayOfObject);
    this._mBps.add(paramBillProduct);
  }
  
  public Class<BillProduct> getColumnClass(int paramInt)
  {
    return BillProduct.class;
  }
  
  public String getColumnName(int paramInt)
  {
    return ((BillingColumn)this._mColumns.get(paramInt)).toString();
  }
  
  public void removeAllRows()
  {
    this._mTotalRows = 0;
    this._mBps.clear();
  }
  
  public void removeRow(int paramInt)
  {
    super.removeRow(paramInt);
    this._mBps.remove(paramInt);
    this._mTotalRows -= 1;
  }
  
  public boolean isCellEditable(int paramInt1, int paramInt2)
  {
    return false;
  }
  
  public String getCodeForColumn(int paramInt)
  {
    BillingColumn localBillingColumn = (BillingColumn)this._mColumns.get(paramInt);
    return localBillingColumn._mCode;
  }
  
  public void updateSlNos()
  {
    for (int i = 0; i < this._mTotalRows; i++)
    {
      Object localObject = getValueAt(i, 0);
      if ((localObject instanceof InternalInteger))
      {
        InternalInteger localInternalInteger = (InternalInteger)localObject;
        localInternalInteger.setVal(new Integer(i + 1));
        setValueAt(localInternalInteger, i, 0);
      }
    }
  }
  
  public BillProduct getBillProductAtIndex(int paramInt)
  {
    return (BillProduct)this._mBps.get(paramInt);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.billing.BillTableModel
 * JD-Core Version:    0.7.0.1
 */