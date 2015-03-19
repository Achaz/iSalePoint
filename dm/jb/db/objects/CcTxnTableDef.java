package dm.jb.db.objects;

import dm.jb.db.gen.CcTxnBaseTableDef;
import java.util.ArrayList;

public class CcTxnTableDef
  extends CcTxnBaseTableDef
{
  private static CcTxnTableDef _mInstance = null;
  
  public static CcTxnTableDef getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new CcTxnTableDef();
    }
    return _mInstance;
  }
  
  public CcTxnRow getNewRow()
  {
    return new CcTxnRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.CcTxnTableDef
 * JD-Core Version:    0.7.0.1
 */