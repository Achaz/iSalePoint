package dm.jb.db.objects;

import dm.jb.db.gen.StockReturnBaseTableDef;
import java.util.ArrayList;

public class StockReturnTableDef
  extends StockReturnBaseTableDef
{
  private static StockReturnTableDef _mInstance = null;
  
  public static StockReturnTableDef getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new StockReturnTableDef();
    }
    return _mInstance;
  }
  
  public StockReturnRow getNewRow()
  {
    return new StockReturnRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.StockReturnTableDef
 * JD-Core Version:    0.7.0.1
 */