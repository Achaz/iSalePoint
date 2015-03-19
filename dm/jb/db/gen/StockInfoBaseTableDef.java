package dm.jb.db.gen;

import dm.tools.db.DBObjectDef;
import java.util.ArrayList;

public class StockInfoBaseTableDef
  extends DBObjectDef
{
  protected StockInfoBaseTableDef()
  {
    super("STOCK_INFO");
    initAttrs();
  }
  
  private void initAttrs()
  {
    addAttribute("STOCK_INDEX", (short)1, true, true, true);
    addAttribute("VENDOR", (short)1, false);
    addAttribute("PROD_ID", (short)1, false);
    addAttribute("STOCK_DATE", (short)2, false);
    addAttribute("PURCHASE_PRICE", (short)6, false);
    addAttribute("QUANTITY", (short)6, false);
    addAttribute("DESTINATION_TYPE", (short)3, false);
    addAttribute("DESTINATION_INDEX", (short)1, false);
    addAttribute("PO_INDEX", (short)1, false);
    setKeyAttr("STOCK_INDEX");
  }
  
  public StockInfoBaseRow getNewRow()
  {
    return new StockInfoBaseRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.StockInfoBaseTableDef
 * JD-Core Version:    0.7.0.1
 */