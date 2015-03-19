package dm.jb.db.gen;

import dm.tools.db.DBObjectDef;
import java.util.ArrayList;

public class PoInfoBaseTableDef
  extends DBObjectDef
{
  protected PoInfoBaseTableDef()
  {
    super("PO_INFO");
    initAttrs();
  }
  
  private void initAttrs()
  {
    addAttribute("PO_INDEX", (short)1, true, true, true);
    addAttribute("PO_DATE", (short)2, false);
    addAttribute("PO_TIME", (short)5, false);
    addAttribute("PO_ENTRY_COUNT", (short)1, false);
    addAttribute("PO_DELIEVRED", (short)3, false);
    addAttribute("VENDOR_ID", (short)1, false);
    addAttribute("CREATED_BY", (short)1, false);
    addAttribute("EXPECTED_DATE", (short)2, false);
    addAttribute("BILL_TO", (short)3, false);
    addAttribute("SHIP_TO", (short)3, false);
    setKeyAttr("PO_INDEX");
  }
  
  public PoInfoBaseRow getNewRow()
  {
    return new PoInfoBaseRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.PoInfoBaseTableDef
 * JD-Core Version:    0.7.0.1
 */