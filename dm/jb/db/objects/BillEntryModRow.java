package dm.jb.db.objects;

import dm.jb.db.gen.BillEntryModBaseRow;
import dm.tools.db.DBException;
import java.sql.Date;
import java.sql.Time;

public class BillEntryModRow
  extends BillEntryModBaseRow
{
  private ProductRow _mProduct = null;
  
  BillEntryModRow(int paramInt, BillEntryModTableDef paramBillEntryModTableDef)
  {
    super(paramInt, paramBillEntryModTableDef);
  }
  
  public void setValuesFromRow(BillEntryRow paramBillEntryRow, int paramInt1, Date paramDate, Time paramTime, String paramString, double paramDouble1, double paramDouble2, double paramDouble3, int paramInt2)
  {
    double d = 0.0D;
    if (paramBillEntryRow.getAmount() != 0.0D) {
      d = paramBillEntryRow.getTax() / paramBillEntryRow.getAmount() * paramDouble2;
    }
    setValues(paramBillEntryRow.getBillNo(), paramBillEntryRow.getProductIndex(), paramBillEntryRow.getEntryIndex(), paramDouble1, paramDouble2, paramDouble3, paramInt1, paramDate, paramTime, paramString, paramInt2, d);
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
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.BillEntryModRow
 * JD-Core Version:    0.7.0.1
 */