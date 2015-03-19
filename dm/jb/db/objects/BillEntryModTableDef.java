package dm.jb.db.objects;

import dm.jb.db.gen.BillEntryModBaseTableDef;
import java.util.ArrayList;

public class BillEntryModTableDef
  extends BillEntryModBaseTableDef
{
  private static BillEntryModTableDef _mInstance = null;
  
  public static BillEntryModTableDef getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new BillEntryModTableDef();
    }
    return _mInstance;
  }
  
  public BillEntryModRow getNewRow()
  {
    return new BillEntryModRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.BillEntryModTableDef
 * JD-Core Version:    0.7.0.1
 */