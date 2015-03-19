package dm.jb.db.objects;

import dm.jb.db.gen.WhToStoreEntryBaseTableDef;
import java.util.ArrayList;

public class WhToStoreEntryTableDef
  extends WhToStoreEntryBaseTableDef
{
  private static WhToStoreEntryTableDef _mInstance = null;
  
  public static WhToStoreEntryTableDef getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new WhToStoreEntryTableDef();
    }
    return _mInstance;
  }
  
  public WhToStoreEntryRow getNewRow()
  {
    return new WhToStoreEntryRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.WhToStoreEntryTableDef
 * JD-Core Version:    0.7.0.1
 */