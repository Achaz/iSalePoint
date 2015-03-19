package dm.jb.db.objects;

import dm.jb.db.gen.CashTxnBaseTableDef;
import java.util.ArrayList;

public class CashTxnTableDef
  extends CashTxnBaseTableDef
{
  private static CashTxnTableDef _mInstance = null;
  
  public static CashTxnTableDef getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new CashTxnTableDef();
    }
    return _mInstance;
  }
  
  public CashTxnRow getNewRow()
  {
    return new CashTxnRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.CashTxnTableDef
 * JD-Core Version:    0.7.0.1
 */