package dm.jb.db.objects;

import dm.jb.db.gen.CurrentStockBaseTableDef;
import dm.jb.op.report.ProductAndAllStock;
import dm.tools.db.BindObject;
import dm.tools.db.DBException;
import dm.tools.db.DBRow;
import java.util.ArrayList;
import java.util.Iterator;

public class CurrentStockTableDef
  extends CurrentStockBaseTableDef
{
  private static CurrentStockTableDef _mInstance = null;
  
  public static CurrentStockTableDef getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new CurrentStockTableDef();
    }
    return _mInstance;
  }
  
  public CurrentStockRow getNewRow()
  {
    return new CurrentStockRow(getAttrList().size(), this);
  }
  
  public ArrayList<ProductAndAllStock> getProductsAndStocksForCats(ArrayList<CategoryRow> paramArrayList)
    throws DBException
  {
    return getProductsAndStocksForCatsInternal(paramArrayList, false, false, true, false, false);
  }
  
  public ArrayList<CurrentStockRow> getStocksForProducts(ArrayList<ProductRow> paramArrayList, ArrayList<CurrentStockRow> paramArrayList1)
    throws DBException
  {
    BindObject[] arrayOfBindObject = { new BindObject(1, 1, Integer.valueOf(-1)) };
    ArrayList localArrayList1 = new ArrayList();
    Iterator localIterator1 = paramArrayList.iterator();
    while (localIterator1.hasNext())
    {
      ProductRow localProductRow = (ProductRow)localIterator1.next();
      arrayOfBindObject[0].value = Integer.valueOf(localProductRow.getProdIndex());
      ArrayList localArrayList2 = getAllValuesWithWhereClauseWithBind("PROD_ID=?", arrayOfBindObject);
      if ((localArrayList2 != null) && (localArrayList2.size() != 0))
      {
        Iterator localIterator2 = localArrayList2.iterator();
        while (localIterator2.hasNext())
        {
          DBRow localDBRow = (DBRow)localIterator2.next();
          if (paramArrayList1 == null) {
            localArrayList1.add((CurrentStockRow)localDBRow);
          } else if (!isDuplicateEnrty(paramArrayList1, (CurrentStockRow)localDBRow)) {
            localArrayList1.add((CurrentStockRow)localDBRow);
          }
        }
        localArrayList2.clear();
        localArrayList2 = null;
      }
    }
    return localArrayList1;
  }
  
  private boolean isDuplicateEnrty(ArrayList<CurrentStockRow> paramArrayList, CurrentStockRow paramCurrentStockRow)
  {
    Iterator localIterator = paramArrayList.iterator();
    while (localIterator.hasNext())
    {
      CurrentStockRow localCurrentStockRow = (CurrentStockRow)localIterator.next();
      if (localCurrentStockRow.getStockIndex() == paramCurrentStockRow.getStockIndex()) {
        return true;
      }
    }
    return false;
  }
  
  public ArrayList<ProductAndAllStock> getProductsAndStocksForCatsForLowStock(ArrayList<CategoryRow> paramArrayList, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4)
    throws DBException
  {
    return getProductsAndStocksForCatsInternal(paramArrayList, true, paramBoolean1, paramBoolean2, paramBoolean3, paramBoolean4);
  }
  
  public ArrayList<ProductAndAllStock> getProductsAndStocksForCatsInternal(ArrayList<CategoryRow> paramArrayList, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5)
    throws DBException
  {
    ArrayList localArrayList1 = new ArrayList();
    Object[] arrayOfObject = paramArrayList.toArray();
    int i = 0;
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(" ((STOCK < LOW_STOCK ) OR (STOCK = LOW_STOCK)) ");
    Object localObject2;
    Object localObject3;
    while (i < arrayOfObject.length)
    {
      localObject1 = new StringBuffer(" CAT_INDEX IN( ");
      int j = arrayOfObject.length - i > 20 ? i + 20 : arrayOfObject.length;
      for (int k = i; k < j; k++)
      {
        ((StringBuffer)localObject1).append(((CategoryRow)arrayOfObject[k]).getCatIndex());
        if (k != j - 1) {
          ((StringBuffer)localObject1).append(",");
        }
      }
      ((StringBuffer)localObject1).append(") ");
      if ((paramBoolean1) && (paramBoolean2))
      {
        ((StringBuffer)localObject1).append(" AND ");
        ((StringBuffer)localObject1).append(localStringBuffer);
      }
      localObject2 = ProductTableDef.getInstance().getAllValuesWithWhereClause(((StringBuffer)localObject1).toString());
      localObject3 = ((ArrayList)localObject2).iterator();
      while (((Iterator)localObject3).hasNext()) {
        localArrayList1.add((StockAndProductRow)((Iterator)localObject3).next());
      }
      i = j;
    }
    if (localArrayList1.size() == 0) {
      return null;
    }
    Object localObject1 = localArrayList1.iterator();
    ArrayList localArrayList2 = new ArrayList();
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = (StockAndProductRow)((Iterator)localObject1).next();
      localObject3 = new ProductAndAllStock((StockAndProductRow)localObject2);
      if ((!paramBoolean1) || (paramBoolean3))
      {
        ArrayList localArrayList3 = getAllValuesWithWhereClause(" PROD_ID=" + ((StockAndProductRow)localObject2).getProdIndex());
        Iterator localIterator = localArrayList3.iterator();
        while (localIterator.hasNext())
        {
          CurrentStockRow localCurrentStockRow = (CurrentStockRow)localIterator.next();
          ((ProductAndAllStock)localObject3).addCurrentStock(localCurrentStockRow);
        }
        if (((!paramBoolean1) || (!paramBoolean4) || (((ProductAndAllStock)localObject3).getTotalCurrentStock() <= ((StockAndProductRow)localObject2).getLowStock())) && ((!paramBoolean1) || (!paramBoolean5) || (((ProductAndAllStock)localObject3).getTotalCurrentStock() + ((StockAndProductRow)localObject2).getStock() <= ((StockAndProductRow)localObject2).getLowStock()))) {
          localArrayList2.add(localObject3);
        }
      }
    }
    return localArrayList2;
  }
  
  public ArrayList<ProductAndAllStock> getProductsAndStocksForDepts(ArrayList<DeptRow> paramArrayList)
    throws DBException
  {
    return getProductsAndStocksForDeptsInternal(paramArrayList, false, true, false, false, false);
  }
  
  public ArrayList<ProductAndAllStock> getProductsAndStocksForDeptsForLowStock(ArrayList<DeptRow> paramArrayList, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4)
    throws DBException
  {
    return getProductsAndStocksForDeptsInternal(paramArrayList, true, paramBoolean1, paramBoolean2, paramBoolean3, paramBoolean4);
  }
  
  private ArrayList<ProductAndAllStock> getProductsAndStocksForDeptsInternal(ArrayList<DeptRow> paramArrayList, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5)
    throws DBException
  {
    ArrayList localArrayList1 = new ArrayList();
    Object[] arrayOfObject = paramArrayList.toArray();
    int i = 0;
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(" ((STOCK < LOW_STOCK ) OR (STOCK = LOW_STOCK)) ");
    Object localObject2;
    Object localObject3;
    while (i < arrayOfObject.length)
    {
      localObject1 = new StringBuffer(" DEPT_INDEX IN( ");
      int j = arrayOfObject.length - i > 20 ? i + 20 : arrayOfObject.length;
      for (int k = i; k < j; k++)
      {
        ((StringBuffer)localObject1).append(((DeptRow)arrayOfObject[k]).getDeptIndex());
        if (k != j - 1) {
          ((StringBuffer)localObject1).append(",");
        }
      }
      ((StringBuffer)localObject1).append(") ");
      if ((paramBoolean1) && (paramBoolean3))
      {
        ((StringBuffer)localObject1).append(" AND ");
        ((StringBuffer)localObject1).append(localStringBuffer);
      }
      localObject2 = ProductTableDef.getInstance().getAllValuesWithWhereClause(((StringBuffer)localObject1).toString());
      localObject3 = ((ArrayList)localObject2).iterator();
      while (((Iterator)localObject3).hasNext()) {
        localArrayList1.add((StockAndProductRow)((Iterator)localObject3).next());
      }
      i = j;
    }
    if (localArrayList1.size() == 0) {
      return null;
    }
    Object localObject1 = localArrayList1.iterator();
    ArrayList localArrayList2 = new ArrayList();
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = (StockAndProductRow)((Iterator)localObject1).next();
      localObject3 = new ProductAndAllStock((StockAndProductRow)localObject2);
      if ((!paramBoolean1) || (paramBoolean2))
      {
        ArrayList localArrayList3 = getAllValuesWithWhereClause(" PROD_ID=" + ((StockAndProductRow)localObject2).getProdIndex());
        Iterator localIterator = localArrayList3.iterator();
        while (localIterator.hasNext())
        {
          CurrentStockRow localCurrentStockRow = (CurrentStockRow)localIterator.next();
          ((ProductAndAllStock)localObject3).addCurrentStock(localCurrentStockRow);
        }
        if (((!paramBoolean1) || (!paramBoolean4) || (((ProductAndAllStock)localObject3).getTotalCurrentStock() <= ((StockAndProductRow)localObject2).getLowStock())) && ((!paramBoolean1) || (!paramBoolean5) || (((ProductAndAllStock)localObject3).getTotalCurrentStock() + ((StockAndProductRow)localObject2).getStock() <= ((StockAndProductRow)localObject2).getLowStock()))) {
          localArrayList2.add(localObject3);
        }
      }
    }
    return localArrayList2;
  }
  
  public ArrayList<CurrentStockRow> getCurrentStockListForProduct(int paramInt)
    throws DBException
  {
    ArrayList localArrayList1 = getAllValuesWithWhereClause(" PROD_ID=" + paramInt);
    if ((localArrayList1 == null) || (localArrayList1.size() == 0)) {
      return null;
    }
    ArrayList localArrayList2 = new ArrayList();
    Iterator localIterator = localArrayList1.iterator();
    while (localIterator.hasNext())
    {
      DBRow localDBRow = (DBRow)localIterator.next();
      localArrayList2.add((CurrentStockRow)localDBRow);
    }
    localArrayList1.clear();
    localArrayList1 = null;
    return localArrayList2;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.CurrentStockTableDef
 * JD-Core Version:    0.7.0.1
 */