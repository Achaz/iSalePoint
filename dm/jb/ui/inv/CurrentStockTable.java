package dm.jb.ui.inv;

import dm.jb.db.objects.CurrentStockRow;
import dm.jb.db.objects.StockInfoRow;
import dm.tools.types.InternalAmount;
import dm.tools.types.InternalInteger;
import dm.tools.types.InternalQuantity;
import dm.tools.utils.CommonConfig;
import dm.tools.utils.SafeMath;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class CurrentStockTable
  extends JTable
{
  StockProductModel model = new StockProductModel();
  
  public CurrentStockTable()
  {
    setModel(this.model);
  }
  
  public boolean isCellEditable(int paramInt1, int paramInt2)
  {
    return false;
  }
  
  public void removeAllRows()
  {
    this.model.removeAllRows();
  }
  
  public void setStockQuantityAtRow(int paramInt, double paramDouble, short paramShort)
  {
    paramInt = convertRowIndexToModel(paramInt);
    this.model.setStockQuantityAtRow(paramInt, paramDouble, paramShort);
  }
  
  public void addStock(StockInfoAndCurrentStock paramStockInfoAndCurrentStock)
  {
    this.model.addStock(paramStockInfoAndCurrentStock);
  }
  
  public StockInfoAndCurrentStock getStockAtTableIndex(int paramInt)
  {
    paramInt = convertRowIndexToModel(paramInt);
    return this.model.getStock(paramInt);
  }
  
  void addSockForStockIndex(int paramInt, double paramDouble)
  {
    this.model.addSockForStockIndex(paramInt, paramDouble);
  }
  
  private class StockProductModel
    extends DefaultTableModel
  {
    private int _mTotalRows = 0;
    private ArrayList<StockInfoAndCurrentStock> _mData = new ArrayList();
    String[] columNames = { "Sl no.", "Warehouse", "Stock No", "Stock Quantity", "Stock Date", "Purchase Price" };
    
    public StockProductModel() {}
    
    public Class getColumnClass(int paramInt)
    {
      if ((paramInt == 0) || (paramInt == 2) || (paramInt == 3) || (paramInt == 5)) {
        return Integer.class;
      }
      return Object.class;
    }
    
    public int getRowCount()
    {
      return this._mTotalRows;
    }
    
    public int getColumnCount()
    {
      return this.columNames.length;
    }
    
    public void setStockQuantityAtRow(int paramInt, double paramDouble, short paramShort)
    {
      InternalQuantity localInternalQuantity = new InternalQuantity(paramDouble, "  ", "  ", paramShort, false);
      setValueAt(localInternalQuantity, paramInt, 3);
    }
    
    public void addStock(StockInfoAndCurrentStock paramStockInfoAndCurrentStock)
    {
      this._mData.add(paramStockInfoAndCurrentStock);
      InternalInteger localInternalInteger = new InternalInteger(this._mTotalRows + 1, "  ", "  ");
      InternalQuantity localInternalQuantity = new InternalQuantity(paramStockInfoAndCurrentStock.currentStockRow.getQuantity(), "  ", "  ", paramStockInfoAndCurrentStock.currentStockRow.getQuantityUnit(), false);
      String str = "NA";
      if (paramStockInfoAndCurrentStock.stockInfoRow != null)
      {
        localObject = new SimpleDateFormat(CommonConfig.getInstance().dateFormat);
        str = ((SimpleDateFormat)localObject).format(paramStockInfoAndCurrentStock.stockInfoRow.getStockDate());
      }
      Object localObject = paramStockInfoAndCurrentStock.stockInfoRow == null ? "NA" : InternalAmount.toString(paramStockInfoAndCurrentStock.stockInfoRow.getPurchasePrice());
      Object[] arrayOfObject = { localInternalInteger, paramStockInfoAndCurrentStock.wareHouse, paramStockInfoAndCurrentStock.currentStockRow.getStockIndex() + " ", localInternalQuantity, str, localObject };
      this._mTotalRows += 1;
      insertRow(this._mTotalRows - 1, arrayOfObject);
    }
    
    public void removeRow(int paramInt)
    {
      super.removeRow(paramInt);
      this._mData.remove(paramInt);
      this._mTotalRows -= 1;
    }
    
    public void removeAllRows()
    {
      this._mTotalRows = 0;
      this._mData.clear();
    }
    
    public void setValueAt(Object paramObject, int paramInt1, int paramInt2)
    {
      super.setValueAt(paramObject, paramInt1, paramInt2);
    }
    
    public String getColumnName(int paramInt)
    {
      return this.columNames[paramInt];
    }
    
    public boolean isCellEditable(int paramInt1, int paramInt2)
    {
      return false;
    }
    
    public StockInfoAndCurrentStock getStock(int paramInt)
    {
      return (StockInfoAndCurrentStock)this._mData.get(paramInt);
    }
    
    void addSockForStockIndex(int paramInt, double paramDouble)
    {
      int i = 0;
      Iterator localIterator = this._mData.iterator();
      while (localIterator.hasNext())
      {
        StockInfoAndCurrentStock localStockInfoAndCurrentStock = (StockInfoAndCurrentStock)localIterator.next();
        if (localStockInfoAndCurrentStock.currentStockRow.getStockIndex() == paramInt)
        {
          localStockInfoAndCurrentStock.currentStockRow.setQuantity(SafeMath.safeAdd(localStockInfoAndCurrentStock.currentStockRow.getQuantity(), paramDouble));
          InternalQuantity localInternalQuantity = new InternalQuantity(localStockInfoAndCurrentStock.currentStockRow.getQuantity(), "  ", "  ", localStockInfoAndCurrentStock.currentStockRow.getQuantityUnit(), false);
          setValueAt(localInternalQuantity, i, 3);
          return;
        }
        i++;
      }
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.inv.CurrentStockTable
 * JD-Core Version:    0.7.0.1
 */