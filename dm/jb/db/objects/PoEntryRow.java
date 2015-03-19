package dm.jb.db.objects;

import dm.jb.db.gen.PoEntryBaseRow;

public class PoEntryRow
  extends PoEntryBaseRow
{
  boolean markedForDelete = false;
  private StockAndProductRow _mProduct = null;
  
  public PoEntryRow(int paramInt, PoEntryTableDef paramPoEntryTableDef)
  {
    super(paramInt, paramPoEntryTableDef);
  }
  
  public StockAndProductRow getProduct()
  {
    return this._mProduct;
  }
  
  public void setProduct(StockAndProductRow paramStockAndProductRow)
  {
    this._mProduct = paramStockAndProductRow;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.PoEntryRow
 * JD-Core Version:    0.7.0.1
 */