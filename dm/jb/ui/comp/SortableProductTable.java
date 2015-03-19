package dm.jb.ui.comp;

import dm.jb.db.objects.ProductRow;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.jdesktop.swing.JXTable;
import org.jdesktop.swing.decorator.AlternateRowHighlighter;
import org.jdesktop.swing.decorator.Highlighter;
import org.jdesktop.swing.decorator.HighlighterPipeline;

public class SortableProductTable
  extends JXTable
{
  public static final int SORT_ORDER_ASC = 1;
  public static final int SORT_ORDER_DESC = 2;
  public static final int SORT_ORDER_DEFAULT = 0;
  private ProductTableModel _mModel = new ProductTableModel();
  
  public SortableProductTable()
  {
    setModel(this._mModel);
    initUI();
  }
  
  private void initUI()
  {
    setAutoCreateColumnsFromModel(false);
    setPreferredScrollableViewportSize(new Dimension(500, 70));
    TableColumnModel localTableColumnModel = getColumnModel();
    TableColumn localTableColumn = localTableColumnModel.getColumn(0);
    localTableColumn.setPreferredWidth(80);
    localTableColumn = localTableColumnModel.getColumn(1);
    localTableColumn.setPreferredWidth(200);
    setHighlighters(new HighlighterPipeline(new Highlighter[] { AlternateRowHighlighter.classicLinePrinter }));
  }
  
  public ProductRow getProductAt(int paramInt)
  {
    return this._mModel.getProductAt(paramInt);
  }
  
  public int indexOf(ProductRow paramProductRow)
  {
    return this._mModel.indexOf(paramProductRow);
  }
  
  public void removeAllRows()
  {
    this._mModel.removeAllRows();
    updateUI();
  }
  
  public void addProductToTable(ProductRow paramProductRow)
  {
    this._mModel.addProductToTable(paramProductRow);
  }
  
  public void updateList(ArrayList<ProductRow> paramArrayList)
  {
    this._mModel.updateList(paramArrayList);
  }
  
  public void addAndInitialSort(ArrayList<ProductRow> paramArrayList)
  {
    Iterator localIterator = paramArrayList.iterator();
    while (localIterator.hasNext()) {
      addProductToTable((ProductRow)localIterator.next());
    }
  }
  
  public void removeRow(int paramInt)
  {
    this._mModel.removeRow(paramInt);
  }
  
  private class ProductTableModel
    extends AbstractTableModel
  {
    private int rowCount = 0;
    ArrayList<ProductRow> prodList = new ArrayList();
    
    public ProductTableModel() {}
    
    public int getColumnCount()
    {
      return 2;
    }
    
    public int getRowCount()
    {
      return this.rowCount;
    }
    
    public void setProductName(int paramInt, String paramString)
    {
      setValueAt(paramString, paramInt, 1);
    }
    
    public void setValueAt(Object paramObject, int paramInt1, int paramInt2)
    {
      super.setValueAt(paramObject, paramInt1, paramInt2);
    }
    
    public ArrayList<ProductRow> getProductList()
    {
      return this.prodList;
    }
    
    public Object getValueAt(int paramInt1, int paramInt2)
    {
      ProductRow localProductRow = (ProductRow)this.prodList.get(paramInt1);
      if (paramInt2 == 0) {
        return new Integer(localProductRow.getProdIndex()).toString();
      }
      return new String(localProductRow.getProdName()).toString();
    }
    
    public int indexOf(ProductRow paramProductRow)
    {
      return this.prodList.indexOf(paramProductRow);
    }
    
    public Class<String> getColumnClass(int paramInt)
    {
      return String.class;
    }
    
    public String getColumnName(int paramInt)
    {
      if (paramInt == 0) {
        return new String("Code");
      }
      return new String("Name");
    }
    
    public void addProductToTable(ProductRow paramProductRow)
    {
      Object[] arrayOfObject = { new Integer(paramProductRow.getProdIndex()).toString(), paramProductRow.getProdName() };
      this.rowCount += 1;
      setValueAt(arrayOfObject[0], this.rowCount - 1, 0);
      setValueAt(arrayOfObject[1], this.rowCount - 1, 1);
      this.prodList.add(paramProductRow);
    }
    
    public void removeAllRows()
    {
      this.rowCount = 0;
      this.prodList.clear();
    }
    
    public void removeRow(int paramInt)
    {
      this.rowCount -= 1;
      this.prodList.remove(paramInt);
    }
    
    public boolean isCellEditable(int paramInt1, int paramInt2)
    {
      return false;
    }
    
    public ProductRow getProductAt(int paramInt)
    {
      return (ProductRow)this.prodList.get(paramInt);
    }
    
    public void updateList(ArrayList<ProductRow> paramArrayList)
    {
      Iterator localIterator = paramArrayList.iterator();
      while (localIterator.hasNext())
      {
        ProductRow localProductRow = (ProductRow)localIterator.next();
        addProductToTable(localProductRow);
      }
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.comp.SortableProductTable
 * JD-Core Version:    0.7.0.1
 */