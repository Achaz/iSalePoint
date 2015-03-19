package dm.jb.ui.report.utils;

import dm.jb.db.objects.CategoryRow;
import dm.jb.db.objects.DeptRow;
import dm.jb.db.objects.ProductRow;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.jdesktop.swingx.JXTable;

public class ReportSelectedProductTable
  extends JXTable
{
  private ReportSelectedProductTableModel _mModel = null;
  
  public ReportSelectedProductTable()
  {
    setModel(this._mModel);
    setColumnControlVisible(true);
    initUI();
    setHorizontalScrollEnabled(true);
  }
  
  public void addReportProduct(ProductRow paramProductRow)
  {
    this._mModel.addProductRow(paramProductRow);
  }
  
  public boolean isCellEditable(int paramInt1, int paramInt2)
  {
    return false;
  }
  
  private void initUI()
  {
    ProductSearchTableCellRenderer localProductSearchTableCellRenderer = new ProductSearchTableCellRenderer();
    setDefaultRenderer(Object.class, localProductSearchTableCellRenderer);
    getColumnModel().getColumn(1).setPreferredWidth(90);
  }
  
  public void removeAllProducts()
  {
    this._mModel.removeAllRows();
  }
  
  public ProductRow getSelectedProduct()
  {
    return this._mModel.getProduct(getSelectedRow());
  }
  
  public ProductRow getProductAt(int paramInt)
  {
    return this._mModel.getProduct(paramInt);
  }
  
  public int getItemsCount()
  {
    return this._mModel.getRowCount();
  }
  
  public ProductRow[] getAllProductsAsArray()
  {
    return this._mModel.getAllProductsAsArray();
  }
  
  private class ReportSelectedProductTableModel
    extends DefaultTableModel
  {
    private String[] columnNames = { "Product Code ", "Product Name    ", "Category    ", "Department   " };
    private int _mTotalRows = 0;
    private ArrayList<ProductRow> items = new ArrayList();
    
    public ReportSelectedProductTableModel() {}
    
    public ProductRow getProduct(int paramInt)
    {
      return (ProductRow)this.items.get(paramInt);
    }
    
    public int getRowCount()
    {
      return this._mTotalRows;
    }
    
    public int getColumnCount()
    {
      return this.columnNames.length;
    }
    
    public String getColumnName(int paramInt)
    {
      return this.columnNames[paramInt];
    }
    
    public void removeAllRows()
    {
      this.items.clear();
      this._mTotalRows = 0;
    }
    
    public ProductRow[] getAllProductsAsArray()
    {
      ProductRow[] arrayOfProductRow = new ProductRow[this._mTotalRows];
      Iterator localIterator = this.items.iterator();
      for (int i = 0; localIterator.hasNext(); i++) {
        arrayOfProductRow[i] = ((ProductRow)localIterator.next());
      }
      return arrayOfProductRow;
    }
    
    public boolean addProductRow(ProductRow paramProductRow)
    {
      Iterator localIterator = this.items.iterator();
      while (localIterator.hasNext())
      {
        localObject = (ProductRow)localIterator.next();
        if (((ProductRow)localObject).getProdIndex() == paramProductRow.getProdIndex()) {
          return false;
        }
      }
      this._mTotalRows += 1;
      this.items.add(paramProductRow);
      Object localObject = { paramProductRow.getProductCode(), "  " + paramProductRow.getProdName(), paramProductRow.getCategory().getCatName(), paramProductRow.getDept().getDeptName() };
      insertRow(this._mTotalRows - 1, (Object[])localObject);
      return true;
    }
  }
  
  class ProductSearchTableCellRenderer
    extends DefaultTableCellRenderer
  {
    ProductSearchTableCellRenderer() {}
    
    public Component getTableCellRendererComponent(JTable paramJTable, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2)
    {
      super.getTableCellRendererComponent(paramJTable, paramObject, paramBoolean1, paramBoolean2, paramInt1, paramInt2);
      if (paramInt2 == 0) {
        setHorizontalAlignment(4);
      } else {
        setHorizontalAlignment(2);
      }
      return this;
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.report.utils.ReportSelectedProductTable
 * JD-Core Version:    0.7.0.1
 */