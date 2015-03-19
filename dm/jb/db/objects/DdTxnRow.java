package dm.jb.db.objects;

import dm.jb.db.gen.DdTxnBaseRow;

public class DdTxnRow
  extends DdTxnBaseRow
{
  DdTxnRow(int paramInt, DdTxnTableDef paramDdTxnTableDef)
  {
    super(paramInt, paramDdTxnTableDef);
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
 * Qualified Name:     dm.jb.db.objects.DdTxnRow
 * JD-Core Version:    0.7.0.1
 */