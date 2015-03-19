package dm.jb.db.gen;

import dm.tools.db.DBException;
import dm.tools.db.DBObjectDef;
import java.util.ArrayList;

public class StockReturnEntryBaseTableDef
  extends DBObjectDef
{
  protected StockReturnEntryBaseTableDef()
  {
    super("STOCK_RETURN_ENTRY");
    initAttrs();
  }
  
  private void initAttrs()
  {
    addAttribute("SR_TXN", (short)7, true, true);
    addAttribute("PRODUCT_ID", (short)7, false);
    addAttribute("QUANTITY", (short)6, false);
    addAttribute("FROM_TYPE", (short)3, false);
    addAttribute("FROM_ID", (short)1, false);
    addAttribute("ENTRY_INDEX", (short)1, true, true);
    addAttribute("STOCK_INDEX", (short)1, false);
    String[] arrayOfString = { "SR_TXN", "ENTRY_INDEX" };
    try
    {
      setIndexAttrs(arrayOfString);
    }
    catch (DBException localDBException)
    {
      localDBException.printStackTrace();
    }
  }
  
  public StockReturnEntryBaseRow getNewRow()
  {
    return new StockReturnEntryBaseRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.StockReturnEntryBaseTableDef
 * JD-Core Version:    0.7.0.1
 */