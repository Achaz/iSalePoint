package dm.jb.db.gen;

import dm.tools.db.DBObjectDef;
import java.util.ArrayList;

public class PoEntryBaseTableDef
  extends DBObjectDef
{
  protected PoEntryBaseTableDef()
  {
    super("PO_ENTRY");
    initAttrs();
  }
  
  private void initAttrs()
  {
    addAttribute("PO_ID", (short)1, true, true, false);
    addAttribute("PO_ENTRY_INDEX", (short)1, true, true, false);
    addAttribute("PRODUCT_ID", (short)1, false);
    addAttribute("QUANTITY", (short)6, false);
    addAttribute("PRICE_EXPECTED", (short)6, false);
    addAttribute("PRICE_PAID", (short)6, false);
    addAttribute("QUANTITY_RECIEVED", (short)6, false);
    setKeyAttr("PO_ENTRY_INDEX");
  }
  
  public PoEntryBaseRow getNewRow()
  {
    return new PoEntryBaseRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.PoEntryBaseTableDef
 * JD-Core Version:    0.7.0.1
 */