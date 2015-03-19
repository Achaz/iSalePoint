package dm.jb.db.objects;

import dm.jb.db.gen.StockReturnEntryBaseTableDef;
import java.util.ArrayList;

public class StockReturnEntryTableDef
  extends StockReturnEntryBaseTableDef
{
  private static StockReturnEntryTableDef _mInstance = null;
  
  public static StockReturnEntryTableDef getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new StockReturnEntryTableDef();
    }
    return _mInstance;
  }
  
  public StockReturnEntryRow getNewRow()
  {
    return new StockReturnEntryRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.StockReturnEntryTableDef
 * JD-Core Version:    0.7.0.1
 */