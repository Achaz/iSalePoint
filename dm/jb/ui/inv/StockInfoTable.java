package dm.jb.ui.inv;

import dm.jb.db.objects.StockInfoRow;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.JXTable;

public class StockInfoTable
  extends JXTable
{
  StockProductModel model = new StockProductModel();
  
  public StockInfoTable()
  {
    setModel(this.model);
    initUI();
  }
  
  private void initUI()
  {
    StockAllotTableCellRenderer localStockAllotTableCellRenderer = new StockAllotTableCellRenderer();
    setDefaultRenderer(Object.class, localStockAllotTableCellRenderer);
  }
  
  public boolean isCellEditable(int paramInt1, int paramInt2)
  {
    return false;
  }
  
  public void removeAllRows()
  {
    this.model.removeAllRows();
  }
  
  public void addStock(StockInfoRow paramStockInfoRow)
  {
    this.model.addStock(paramStockInfoRow);
  }
  
  class StockAllotTableCellRenderer
    extends DefaultTableCellRenderer
  {
    StockAllotTableCellRenderer() {}
    
    public Component getTableCellRendererComponent(JTable paramJTable, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2)
    {
      super.getTableCellRendererComponent(paramJTable, paramObject, paramBoolean1, paramBoolean2, paramInt1, paramInt2);
      if ((paramInt2 == 0) || (paramInt2 == 1) || (paramInt2 == 4)) {
        setHorizontalAlignment(4);
      } else {
        setHorizontalAlignment(2);
      }
      return this;
    }
  }
  
  private class StockProductModel
    extends DefaultTableModel
  {
    private int _mTotalRows = 0;
    String[] columNames = { "     Sl no.    ", "     Stock quantity      ", "      Stock date      ", "     Expiry date      ", "     Purchase Price  " };
    
    public StockProductModel() {}
    
    public int getRowCount()
    {
      return this._mTotalRows;
    }
    
    public int getColumnCount()
    {
      return this.columNames.length;
    }
    
    public void addStock(StockInfoRow paramStockInfoRow) {}
    
    public void removeRow(int paramInt)
    {
      super.removeRow(paramInt);
      this._mTotalRows -= 1;
    }
    
    public void removeAllRows()
    {
      this._mTotalRows = 0;
    }
    
    public void setValueAt(Object paramObject, int paramInt1, int paramInt2)
    {
      super.setValueAt(paramObject, paramInt1, paramInt2);
    }
    
    public String getColumnName(int paramInt)
    {
      return this.columNames[paramInt];
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.inv.StockInfoTable
 * JD-Core Version:    0.7.0.1
 */