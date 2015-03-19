package dm.jb.op.report;

import dm.jb.db.objects.CurrentStockRow;
import dm.jb.db.objects.StockAndProductRow;
import java.util.ArrayList;
import java.util.Iterator;

public class ProductAndAllStock
{
  private ArrayList<CurrentStockRow> _mCurrentStocks = null;
  private StockAndProductRow _mProduct = null;
  
  public ProductAndAllStock(StockAndProductRow paramStockAndProductRow)
  {
    this._mProduct = paramStockAndProductRow;
  }
  
  public void addCurrentStock(CurrentStockRow paramCurrentStockRow)
  {
    if (this._mCurrentStocks == null) {
      this._mCurrentStocks = new ArrayList();
    }
    this._mCurrentStocks.add(paramCurrentStockRow);
  }
  
  public double getTotalCurrentStock()
  {
    if (this._mCurrentStocks == null) {
      return 0.0D;
    }
    Iterator localIterator = this._mCurrentStocks.iterator();
    for (double d = 0.0D; localIterator.hasNext(); d += ((CurrentStockRow)localIterator.next()).getQuantity()) {}
    return d;
  }
  
  public StockAndProductRow getProduct()
  {
    return this._mProduct;
  }
  
  public ArrayList<CurrentStockRow> getCurrentStocks()
  {
    return this._mCurrentStocks;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.op.report.ProductAndAllStock
 * JD-Core Version:    0.7.0.1
 */