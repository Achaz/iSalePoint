package dm.jb.db.objects;

import dm.jb.db.gen.CashTxnBaseRow;

public class CashTxnRow
  extends CashTxnBaseRow
{
  CashTxnRow(int paramInt, CashTxnTableDef paramCashTxnTableDef)
  {
    super(paramInt, paramCashTxnTableDef);
  }
  
  public long getBillNo()
  {
    Long localLong = (Long)getValue("BILL_NO");
    if (localLong == null) {
      return -1L;
    }
    return localLong.longValue();
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.CashTxnRow
 * JD-Core Version:    0.7.0.1
 */