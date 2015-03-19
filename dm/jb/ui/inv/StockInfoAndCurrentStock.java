package dm.jb.ui.inv;

import dm.jb.db.objects.CurrentStockRow;
import dm.jb.db.objects.StockInfoRow;
import dm.jb.db.objects.StockInfoTableDef;
import dm.jb.db.objects.WearehouseInfoRow;
import dm.jb.db.objects.WearehouseInfoTableDef;
import dm.tools.db.DBException;
import java.util.ArrayList;
import java.util.Iterator;

public class StockInfoAndCurrentStock
{
  public StockInfoRow stockInfoRow = null;
  public CurrentStockRow currentStockRow = null;
  public WearehouseInfoRow wareHouse = null;
  
  public StockInfoAndCurrentStock(StockInfoRow paramStockInfoRow, CurrentStockRow paramCurrentStockRow, WearehouseInfoRow paramWearehouseInfoRow)
  {
    this.stockInfoRow = paramStockInfoRow;
    this.currentStockRow = paramCurrentStockRow;
    this.wareHouse = paramWearehouseInfoRow;
  }
  
  public static ArrayList<StockInfoAndCurrentStock> getStockInfoAndCurrentStocksForCurrentStocks(ArrayList<CurrentStockRow> paramArrayList)
    throws DBException
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = paramArrayList.iterator();
    while (localIterator.hasNext())
    {
      CurrentStockRow localCurrentStockRow = (CurrentStockRow)localIterator.next();
      StockInfoRow localStockInfoRow = StockInfoTableDef.getInstance().findRowByIndex(localCurrentStockRow.getStockIndex());
      WearehouseInfoRow localWearehouseInfoRow = WearehouseInfoTableDef.getInstance().getWarehouseForIndex(localCurrentStockRow.getWearHouseIndex());
      StockInfoAndCurrentStock localStockInfoAndCurrentStock = new StockInfoAndCurrentStock(localStockInfoRow, localCurrentStockRow, localWearehouseInfoRow);
      localArrayList.add(localStockInfoAndCurrentStock);
    }
    return localArrayList;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.inv.StockInfoAndCurrentStock
 * JD-Core Version:    0.7.0.1
 */