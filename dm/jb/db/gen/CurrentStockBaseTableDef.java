package dm.jb.db.gen;

import dm.tools.db.DBObjectDef;
import java.util.ArrayList;

public class CurrentStockBaseTableDef
  extends DBObjectDef
{
  protected CurrentStockBaseTableDef()
  {
    super("CURRENT_STOCK");
    initAttrs();
  }
  
  private void initAttrs()
  {
    addAttribute("CUR_STOCK_INDEX", (short)1, true, true, true);
    addAttribute("STOCK_INDEX", (short)1, false);
    addAttribute("PROD_ID", (short)1, false);
    addAttribute("EXPIRY", (short)2, false);
    addAttribute("EXPIRY_NA", (short)3, false);
    addAttribute("QUANTITY", (short)6, false);
    addAttribute("QUANTITY_UNIT", (short)1, false);
    addAttribute("WEAR_HOUSE_INDEX", (short)1, false);
    setKeyAttr("CUR_STOCK_INDEX");
  }
  
  public CurrentStockBaseRow getNewRow()
  {
    return new CurrentStockBaseRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.CurrentStockBaseTableDef
 * JD-Core Version:    0.7.0.1
 */