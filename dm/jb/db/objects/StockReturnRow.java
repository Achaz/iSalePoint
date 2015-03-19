package dm.jb.db.objects;

import dm.jb.db.gen.StockReturnBaseRow;
import dm.jb.ui.inv.ReturnStock;
import dm.tools.db.DBException;
import dm.tools.db.DBRow;
import java.util.ArrayList;
import java.util.Iterator;

public class StockReturnRow
  extends StockReturnBaseRow
{
  public StockReturnRow(int paramInt, StockReturnTableDef paramStockReturnTableDef)
  {
    super(paramInt, paramStockReturnTableDef);
  }
  
  public ArrayList<ReturnStock> getReturnStockEntries()
    throws DBException
  {
    ArrayList localArrayList1 = StockReturnEntryTableDef.getInstance().getAllValuesWithWhereClause("SR_TXN=" + getTxnNo());
    if ((localArrayList1 == null) || (localArrayList1.size() == 0)) {
      return null;
    }
    ArrayList localArrayList2 = new ArrayList();
    try
    {
      Iterator localIterator = localArrayList1.iterator();
      while (localIterator.hasNext())
      {
        DBRow localDBRow = (DBRow)localIterator.next();
        StockReturnEntryRow localStockReturnEntryRow = (StockReturnEntryRow)localDBRow;
        String str = localStockReturnEntryRow.getFromType();
        ProductRow localProductRow = (ProductRow)ProductTableDef.getInstance().findRowByIndex(localStockReturnEntryRow.getProductId());
        Object localObject1;
        Object localObject2;
        ReturnStock localReturnStock;
        if (str.equals("W"))
        {
          localObject1 = (CurrentStockRow)CurrentStockTableDef.getInstance().findRowByIndex(localStockReturnEntryRow.getStockIndex());
          localObject2 = null;
          try
          {
            localObject2 = WearehouseInfoTableDef.getInstance().getWarehouseForIndex(((CurrentStockRow)localObject1).getWearHouseIndex());
          }
          catch (DBException localDBException2)
          {
            localDBException2.printStackTrace();
          }
          localReturnStock = new ReturnStock(localProductRow, localStockReturnEntryRow.getQuantity(), localObject1, localObject2);
          localArrayList2.add(localReturnStock);
        }
        else if (str.equals("S"))
        {
          localObject1 = StockAndProductTableDef.getInstance().getRowForProductId((int)localStockReturnEntryRow.getProductId(), localStockReturnEntryRow.getFromId());
          localObject2 = StoreInfoTableDef.getInstance().getStoreForIndex(localStockReturnEntryRow.getFromId());
          localReturnStock = new ReturnStock(localProductRow, localStockReturnEntryRow.getQuantity(), localObject2, localObject1);
          localArrayList2.add(localReturnStock);
        }
        else if (str.equals("D"))
        {
          localObject1 = (DefectiveProductRow)DefectiveProductTableDef.getInstance().findRowByIndex(localStockReturnEntryRow.getFromId());
          localObject2 = StoreInfoTableDef.getInstance().getStoreForIndex(((DefectiveProductRow)localObject1).getStoreId());
          localReturnStock = new ReturnStock(localProductRow, localStockReturnEntryRow.getQuantity(), localObject1, localObject2);
          localArrayList2.add(localReturnStock);
        }
      }
      return localArrayList2;
    }
    catch (DBException localDBException1)
    {
      localArrayList2.clear();
      localArrayList2 = null;
      localArrayList1.clear();
      localArrayList1 = null;
      throw localDBException1;
    }
  }
  
  public void delete()
    throws DBException
  {
    super.delete();
    StockReturnEntryTableDef.getInstance().deleteWithWhere("SR_TXN=" + getTxnNo());
  }
  
  public String toString()
  {
    return "" + getTxnNo();
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.StockReturnRow
 * JD-Core Version:    0.7.0.1
 */