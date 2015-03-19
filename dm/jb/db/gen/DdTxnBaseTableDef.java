package dm.jb.db.gen;

import dm.tools.db.DBObjectDef;
import java.util.ArrayList;

public class DdTxnBaseTableDef
  extends DBObjectDef
{
  protected DdTxnBaseTableDef()
  {
    super("DD_TXN");
    initAttrs();
  }
  
  private void initAttrs()
  {
    addAttribute("BILL_NO", (short)7, false);
    addAttribute("DD_PAY_INDEX", (short)7, true, true, true);
    addAttribute("DD_NO", (short)3, false);
    addAttribute("PAY_DATE", (short)2, false);
    addAttribute("TXN_DATE", (short)2, false);
    addAttribute("TXN_TIME", (short)5, false);
    addAttribute("BANK", (short)3, false);
    addAttribute("TXN_TYPE", (short)3, false);
    addAttribute("AMOUNT", (short)6, false);
    addAttribute("STORE_ID", (short)1, false);
    setKeyAttr("DD_PAY_INDEX");
  }
  
  public DdTxnBaseRow getNewRow()
  {
    return new DdTxnBaseRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.DdTxnBaseTableDef
 * JD-Core Version:    0.7.0.1
 */