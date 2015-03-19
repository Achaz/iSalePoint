package dm.jb.db.objects;

import dm.jb.db.gen.WhToStoreBaseTableDef;
import java.util.ArrayList;

public class WhToStoreTableDef
  extends WhToStoreBaseTableDef
{
  private static WhToStoreTableDef _mInstance = null;
  
  public static WhToStoreTableDef getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new WhToStoreTableDef();
    }
    return _mInstance;
  }
  
  public WhToStoreRow getNewRow()
  {
    return new WhToStoreRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.WhToStoreTableDef
 * JD-Core Version:    0.7.0.1
 */