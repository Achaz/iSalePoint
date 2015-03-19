package dm.jb.db.gen;

import dm.tools.db.DBObjectDef;
import java.util.ArrayList;

public class CcTxnBaseTableDef
  extends DBObjectDef
{
  protected CcTxnBaseTableDef()
  {
    super("CC_TXN");
    initAttrs();
  }
  
  private void initAttrs()
  {
    addAttribute("BILL_NO", (short)7, false);
    addAttribute("CC_INDEX", (short)7, true, true, true);
    addAttribute("CC_NO", (short)3, false);
    addAttribute("CARD_TYPE", (short)3, false);
    addAttribute("REF_NO", (short)3, false);
    addAttribute("AMOUNT", (short)6, false);
    addAttribute("TXN_TYPE", (short)3, false);
    addAttribute("TXN_DATE", (short)2, false);
    addAttribute("TXN_TIME", (short)5, false);
    addAttribute("STORE_ID", (short)1, false);
    setKeyAttr("CC_INDEX");
  }
  
  public CcTxnBaseRow getNewRow()
  {
    return new CcTxnBaseRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.CcTxnBaseTableDef
 * JD-Core Version:    0.7.0.1
 */