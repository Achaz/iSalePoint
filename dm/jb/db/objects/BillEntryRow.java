package dm.jb.db.objects;

import dm.jb.db.gen.BillEntryBaseRow;
import dm.tools.db.DBException;

public class BillEntryRow
  extends BillEntryBaseRow
{
  private ProductRow _mProduct = null;
  
  BillEntryRow(int paramInt, BillEntryTableDef paramBillEntryTableDef)
  {
    super(paramInt, paramBillEntryTableDef);
  }
  
  public ProductRow getProduct()
  {
    if (this._mProduct == null) {
      try
      {
        this._mProduct = ProductTableDef.getInstance().findRowByIndex(getProductIndex());
      }
      catch (DBException localDBException)
      {
        localDBException.printStackTrace();
      }
    }
    return this._mProduct;
  }
  
  public void setProduct(ProductRow paramProductRow)
  {
    this._mProduct = paramProductRow;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.BillEntryRow
 * JD-Core Version:    0.7.0.1
 */