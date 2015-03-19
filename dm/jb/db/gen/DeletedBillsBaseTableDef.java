package dm.jb.db.gen;

import dm.tools.db.DBObjectDef;
import java.util.ArrayList;

public class DeletedBillsBaseTableDef
  extends DBObjectDef
{
  protected DeletedBillsBaseTableDef()
  {
    super("DELETED_BILLS");
    initAttrs();
  }
  
  private void initAttrs()
  {
    addAttribute("BILL_NO", (short)1, true, true, false);
    addAttribute("DELETE_MODE", (short)1, false);
    addAttribute("UPDATE_DATE", (short)2, false);
    addAttribute("UPDATE_TIME", (short)5, false);
    addAttribute("UPDATE_UID", (short)3, false);
    addAttribute("AMOUNT", (short)6, false);
    addAttribute("TOTAL_ENTRIES", (short)1, false);
    addAttribute("STORE_ID", (short)1, false);
    setKeyAttr("BILL_NO");
  }
  
  public DeletedBillsBaseRow getNewRow()
  {
    return new DeletedBillsBaseRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.DeletedBillsBaseTableDef
 * JD-Core Version:    0.7.0.1
 */