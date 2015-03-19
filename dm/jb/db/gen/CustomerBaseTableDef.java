package dm.jb.db.gen;

import dm.tools.db.DBObjectDef;
import java.util.ArrayList;

public class CustomerBaseTableDef
  extends DBObjectDef
{
  protected CustomerBaseTableDef()
  {
    super("CUSTOMER");
    initAttrs();
  }
  
  private void initAttrs()
  {
    addAttribute("CUST_INDEX", (short)1, true, true, true);
    addAttribute("CUST_NAME", (short)3, false);
    addAttribute("CUST_ADDRESS", (short)3, false);
    addAttribute("CUST_PHONE", (short)3, false);
    addAttribute("LOYALTY", (short)1, false);
    addAttribute("BARCODE", (short)3, false);
    addAttribute("RFID", (short)3, false);
    addAttribute("JOIN_DATE", (short)2, false);
    setKeyAttr("CUST_INDEX");
  }
  
  public CustomerBaseRow getNewRow()
  {
    return new CustomerBaseRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.CustomerBaseTableDef
 * JD-Core Version:    0.7.0.1
 */