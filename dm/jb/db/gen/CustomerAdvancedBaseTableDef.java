package dm.jb.db.gen;

import dm.tools.db.DBObjectDef;
import java.util.ArrayList;

public class CustomerAdvancedBaseTableDef
  extends DBObjectDef
{
  protected CustomerAdvancedBaseTableDef()
  {
    super("CUSTOMER_ADVANCED");
    initAttrs();
  }
  
  private void initAttrs()
  {
    addAttribute("CUSTOMER_ID", (short)1, true, true, false);
    addAttribute("DOB", (short)2, false);
    addAttribute("ANNIVERSARY", (short)2, false);
    addAttribute("MOBILE", (short)3, false);
    addAttribute("PROFESSION", (short)1, false);
    addAttribute("HOBBY", (short)3, false);
    addAttribute("EMAIL", (short)3, false);
    addAttribute("DOMESTIC_TRAVEL", (short)1, false);
    addAttribute("INT_TRAVEL", (short)1, false);
    setKeyAttr("CUSTOMER_ID");
  }
  
  public CustomerAdvancedBaseRow getNewRow()
  {
    return new CustomerAdvancedBaseRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.CustomerAdvancedBaseTableDef
 * JD-Core Version:    0.7.0.1
 */