package dm.jb.db.objects;

import dm.jb.Common;
import dm.jb.db.gen.CategoryBaseRow;
import dm.jb.op.sync.FileOpUtils;
import dm.jb.op.sync.SyncWriter;
import dm.tools.db.DBException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class CategoryRow
  extends CategoryBaseRow
{
  private ArrayList<ProductRow> _mProducts = null;
  
  CategoryRow(int paramInt, CategoryTableDef paramCategoryTableDef)
  {
    super(paramInt, paramCategoryTableDef);
  }
  
  public ArrayList<ProductRow> getProductsForThis()
    throws DBException
  {
    ArrayList localArrayList1 = ProductTableDef.getInstance().getAllValuesWithWhereClause(" CAT_INDEX=" + getCatIndex());
    Iterator localIterator = localArrayList1.iterator();
    ArrayList localArrayList2 = new ArrayList();
    while (localIterator.hasNext()) {
      localArrayList2.add((ProductRow)localIterator.next());
    }
    return localArrayList2;
  }
  
  public ArrayList<ProductRow> getProducts()
    throws DBException
  {
    if (this._mProducts != null) {
      return this._mProducts;
    }
    if (Common.ProductLoadsMode == 3)
    {
      this._mProducts = getProductsForThis();
      return this._mProducts;
    }
    return getProductsForThis();
  }
  
  public void addProduct(ProductRow paramProductRow)
    throws DBException
  {
    if (this._mProducts != null)
    {
      this._mProducts.add(paramProductRow);
      return;
    }
    if (Common.ProductLoadsMode == 3) {
      this._mProducts = getProductsForThis();
    }
  }
  
  public void removeProductFromList(ProductRow paramProductRow)
    throws DBException
  {
    if (this._mProducts != null)
    {
      this._mProducts.remove(paramProductRow);
      return;
    }
    if (Common.ProductLoadsMode == 3)
    {
      this._mProducts = getProductsForThis();
      this._mProducts.remove(paramProductRow);
    }
  }
  
  public boolean isDuplicateProduct(String paramString)
    throws DBException
  {
    if (this._mProducts != null)
    {
      localObject = this._mProducts.iterator();
      while (((Iterator)localObject).hasNext())
      {
        ProductRow localProductRow = (ProductRow)((Iterator)localObject).next();
        if (localProductRow.getProdName().equals(paramString)) {
          return true;
        }
      }
      return false;
    }
    Object localObject = ProductTableDef.getInstance().getAllValuesWithWhereClause(" CAT_INDEX=" + getCatIndex() + " AND PROD_NAME LIKE '" + paramString + "'");
    return ((ArrayList)localObject).size() > 0;
  }
  
  void cleanProductList()
  {
    this._mProducts = null;
  }
  
  public void delete()
    throws DBException
  {
    ProductTableDef.getInstance().deleteAllForCat(getCatIndex());
    this._mProducts = null;
    super.delete();
  }
  
  public String toString()
  {
    return getCatName();
  }
  
  public void sync(SyncWriter paramSyncWriter)
    throws IOException
  {
    paramSyncWriter.writeInt(getCatIndex());
    paramSyncWriter.writeInt(getDeptIndex());
    paramSyncWriter.writeString(getCatName());
    paramSyncWriter.writeString(getCatDetails());
    paramSyncWriter.writeDouble(getDiscount());
    paramSyncWriter.writeDouble(getTax());
    paramSyncWriter.writeInt(getTaxUnit());
  }
  
  public int syncFromStream(FileInputStream paramFileInputStream)
    throws IOException
  {
    int i = FileOpUtils.readInt(paramFileInputStream);
    int j = FileOpUtils.readInt(paramFileInputStream);
    String str1 = FileOpUtils.readString(paramFileInputStream);
    String str2 = FileOpUtils.readString(paramFileInputStream);
    double d1 = FileOpUtils.readDouble(paramFileInputStream);
    double d2 = FileOpUtils.readDouble(paramFileInputStream);
    int k = FileOpUtils.readInt(paramFileInputStream);
    setValues(j, str1, str2, d1, d2, k);
    setCatIndex(i);
    return 8 + (str1.getBytes().length + 2) + (str2.getBytes().length + 2) + 8 + 8 + 4;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.CategoryRow
 * JD-Core Version:    0.7.0.1
 */