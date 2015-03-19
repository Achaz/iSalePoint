package dm.jb.db.gen;

import dm.tools.db.DBObjectDef;
import java.util.ArrayList;

public class BillEntryModBaseTableDef
  extends DBObjectDef
{
  protected BillEntryModBaseTableDef()
  {
    super("BILL_ENTRY_MOD");
    initAttrs();
  }
  
  private void initAttrs()
  {
    addAttribute("BILL_NO", (short)1, true, true);
    addAttribute("PRODUCT_INDEX", (short)1, false);
    addAttribute("ENTRY_INDEX", (short)1, true, true);
    addAttribute("QUANTITY", (short)6, false);
    addAttribute("AMOUNT", (short)6, false);
    addAttribute("DISCOUNT", (short)6, false);
    addAttribute("MOD_REASON", (short)1, false);
    addAttribute("UPDATE_DATE", (short)2, false);
    addAttribute("UPDATE_TIME", (short)5, false);
    addAttribute("UPDATE_UID", (short)3, false);
    addAttribute("STORE_ID", (short)1, false);
    addAttribute("RETURNED_TAX", (short)6, false);
  }
  
  public BillEntryModBaseRow getNewRow()
  {
    return new BillEntryModBaseRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.BillEntryModBaseTableDef
 * JD-Core Version:    0.7.0.1
 */