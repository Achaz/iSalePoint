package dm.jb.db.gen;

import dm.tools.db.DBException;
import dm.tools.db.DBObjectDef;
import java.util.ArrayList;

public class BillEntryBaseTableDef
  extends DBObjectDef
{
  protected BillEntryBaseTableDef()
  {
    super("BILL_ENTRY");
    initAttrs();
  }
  
  private void initAttrs()
  {
    addAttribute("BILL_NO", (short)1, false);
    addAttribute("PRODUCT_INDEX", (short)1, false);
    addAttribute("ENTRY_INDEX", (short)1, false);
    addAttribute("QUANTITY", (short)6, false);
    addAttribute("AMOUNT", (short)6, false);
    addAttribute("DISCOUNT", (short)6, false);
    addAttribute("PURCHASE_PRICE", (short)6, false);
    addAttribute("STORE_ID", (short)1, false);
    addAttribute("TAX", (short)6, false);
    String[] arrayOfString = { "BILL_NO", "ENTRY_INDEX", "STORE_ID" };
    try
    {
      setIndexAttrs(arrayOfString);
    }
    catch (DBException localDBException)
    {
      localDBException.printStackTrace();
    }
  }
  
  public BillEntryBaseRow getNewRow()
  {
    return new BillEntryBaseRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.BillEntryBaseTableDef
 * JD-Core Version:    0.7.0.1
 */