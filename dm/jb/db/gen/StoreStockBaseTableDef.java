package dm.jb.db.gen;

import dm.tools.db.DBException;
import dm.tools.db.DBObjectDef;
import java.util.ArrayList;

public class StoreStockBaseTableDef
  extends DBObjectDef
{
  protected StoreStockBaseTableDef()
  {
    super("STORE_STOCK");
    initAttrs();
  }
  
  private void initAttrs()
  {
    addAttribute("STORE_ID", (short)1, true, false);
    addAttribute("PRODUCT_ID", (short)1, true, false);
    addAttribute("STOCK", (short)6, true);
    addAttribute("PURCHASE_PRICE", (short)6, true);
    addAttribute("EXPIRY", (short)2, false);
    addAttribute("STOCK_INDEX", (short)1, true);
    String[] arrayOfString = { "STORE_ID", "PRODUCT_ID" };
    try
    {
      setIndexAttrs(arrayOfString);
    }
    catch (DBException localDBException)
    {
      localDBException.printStackTrace();
    }
  }
  
  public StoreStockBaseRow getNewRow()
  {
    return new StoreStockBaseRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.StoreStockBaseTableDef
 * JD-Core Version:    0.7.0.1
 */