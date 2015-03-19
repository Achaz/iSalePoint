package dm.jb.db.gen;

import dm.tools.db.DBObjectDef;
import java.util.ArrayList;

public class WhToStoreEntryBaseTableDef
  extends DBObjectDef
{
  protected WhToStoreEntryBaseTableDef()
  {
    super("WH_TO_STORE_ENTRY");
    initAttrs();
  }
  
  private void initAttrs()
  {
    addAttribute("WAREHOUSE_INDEX", (short)1, false);
    addAttribute("PRODUCT_ID", (short)7, false);
    addAttribute("STOCK_ID", (short)1, false);
    addAttribute("QUANTITY", (short)6, false);
    addAttribute("ST_TXN_NO", (short)1, false);
  }
  
  public WhToStoreEntryBaseRow getNewRow()
  {
    return new WhToStoreEntryBaseRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.WhToStoreEntryBaseTableDef
 * JD-Core Version:    0.7.0.1
 */