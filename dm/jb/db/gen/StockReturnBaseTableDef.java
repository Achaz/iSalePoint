package dm.jb.db.gen;

import dm.tools.db.DBObjectDef;
import java.util.ArrayList;

public class StockReturnBaseTableDef
  extends DBObjectDef
{
  protected StockReturnBaseTableDef()
  {
    super("STOCK_RETURN");
    initAttrs();
  }
  
  private void initAttrs()
  {
    addAttribute("TXN_NO", (short)7, true, true, true);
    addAttribute("VENDOR", (short)7, false);
    addAttribute("CREATE_DATE", (short)2, false);
    addAttribute("CREATE_TIME", (short)5, false);
    addAttribute("USER", (short)1, false);
    setKeyAttr("TXN_NO");
  }
  
  public StockReturnBaseRow getNewRow()
  {
    return new StockReturnBaseRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.StockReturnBaseTableDef
 * JD-Core Version:    0.7.0.1
 */