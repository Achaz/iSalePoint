package dm.jb.db.objects;

import dm.jb.db.gen.StockInfoBaseTableDef;
import dm.tools.db.DBException;
import java.util.ArrayList;

public class StockInfoTableDef
  extends StockInfoBaseTableDef
{
  private static StockInfoTableDef _mInstance = null;
  
  public static StockInfoTableDef getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new StockInfoTableDef();
    }
    return _mInstance;
  }
  
  public StockInfoRow getNewRow()
  {
    return new StockInfoRow(getAttrList().size(), this);
  }
  
  public StockInfoRow findRowByIndex(int paramInt)
    throws DBException
  {
    return (StockInfoRow)super.findRowByIndex(paramInt);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.StockInfoTableDef
 * JD-Core Version:    0.7.0.1
 */