package dm.jb.db.gen;

import dm.tools.db.DBObjectDef;
import java.util.ArrayList;

public class StockAndProductBaseTableDef
  extends DBObjectDef
{
  protected StockAndProductBaseTableDef()
  {
    super("STOCK_AND_PRODUCT");
    initAttrs();
  }
  
  private void initAttrs()
  {
    addAttribute("PROD_INDEX", (short)1, true);
    addAttribute("CAT_INDEX", (short)1, true);
    addAttribute("DEPT_INDEX", (short)1, true);
    addAttribute("PROD_NAME", (short)3, true);
    addAttribute("PROD_UNIT", (short)1, true);
    addAttribute("UNIT_PRICE", (short)6, true);
    addAttribute("DISCOUNT", (short)6, false);
    addAttribute("TAX", (short)6, true);
    addAttribute("TAX_UNIT", (short)1, true);
    addAttribute("LOW_STOCK", (short)6, false);
    addAttribute("LOYALTY_POINTS", (short)1, false);
    addAttribute("REDEEMABLE_POINTS", (short)1, false);
    addAttribute("PRODUCT_CODE", (short)3, false);
    addAttribute("VENDOR_ID", (short)1, false);
    addAttribute("STORE_ID", (short)1, false);
    addAttribute("PRODUCT_ID", (short)1, false);
    addAttribute("STOCK", (short)6, false);
    addAttribute("PURCHASE_PRICE", (short)6, false);
    addAttribute("EXPIRY", (short)2, false);
    addAttribute("STOCK_INDEX", (short)1, false);
    setKeyAttr("PROD_INDEX");
  }
  
  public StockAndProductBaseRow getNewRow()
  {
    return new StockAndProductBaseRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.StockAndProductBaseTableDef
 * JD-Core Version:    0.7.0.1
 */