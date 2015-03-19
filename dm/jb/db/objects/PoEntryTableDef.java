package dm.jb.db.objects;

import dm.jb.db.gen.PoEntryBaseTableDef;
import java.util.ArrayList;

public class PoEntryTableDef
  extends PoEntryBaseTableDef
{
  private static PoEntryTableDef _mInstane = null;
  
  public static PoEntryTableDef getInstance()
  {
    if (_mInstane == null) {
      _mInstane = new PoEntryTableDef();
    }
    return _mInstane;
  }
  
  public PoEntryRow getNewRow()
  {
    return new PoEntryRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.PoEntryTableDef
 * JD-Core Version:    0.7.0.1
 */