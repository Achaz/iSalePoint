package dm.jb.db.gen;

import dm.tools.db.DBException;
import dm.tools.db.DBObjectDef;
import java.util.ArrayList;

public class BillTableBaseTableDef
  extends DBObjectDef
{
  protected BillTableBaseTableDef()
  {
    super("BILL_TABLE");
    initAttrs();
  }
  
  private void initAttrs()
  {
    addAttribute("BILL_NO", (short)1, true, true, true);
    addAttribute("BILL_CLEANED_UP", (short)3, false);
    addAttribute("BILL_DATE", (short)2, false);
    addAttribute("BILL_TIME", (short)5, false);
    addAttribute("AMOUNT", (short)6, false);
    addAttribute("ADDL_DISCOUNT", (short)6, false);
    addAttribute("TAX", (short)6, false);
    addAttribute("TOTAL_ENTRIES", (short)1, false);
    addAttribute("CUST_INDEX", (short)1, false);
    addAttribute("STORE_ID", (short)1, true, true, false);
    addAttribute("SITE_ID", (short)1, false);
    addAttribute("UID", (short)1, false);
    addAttribute("POINTS_REDEEMED", (short)1, false);
    addAttribute("POINTS_AWARDED", (short)1, false);
    String[] arrayOfString = { "BILL_NO", "STORE_ID" };
    try
    {
      setIndexAttrs(arrayOfString);
    }
    catch (DBException localDBException)
    {
      localDBException.printStackTrace();
    }
  }
  
  public BillTableBaseRow getNewRow()
  {
    return new BillTableBaseRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.BillTableBaseTableDef
 * JD-Core Version:    0.7.0.1
 */