package dm.jb.db.objects;

import dm.jb.db.gen.DdTxnBaseTableDef;
import java.util.ArrayList;

public class DdTxnTableDef
  extends DdTxnBaseTableDef
{
  private static DdTxnTableDef _mInstance = null;
  
  public static DdTxnTableDef getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new DdTxnTableDef();
    }
    return _mInstance;
  }
  
  public DdTxnRow getNewRow()
  {
    return new DdTxnRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.DdTxnTableDef
 * JD-Core Version:    0.7.0.1
 */