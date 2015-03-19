package dm.jb.db.gen;

import dm.tools.db.DBObjectDef;
import java.util.ArrayList;

public class CashTxnBaseTableDef
  extends DBObjectDef
{
  protected CashTxnBaseTableDef()
  {
    super("CASH_TXN");
    initAttrs();
  }
  
  private void initAttrs()
  {
    addAttribute("BILL_NO", (short)7, false);
    addAttribute("PAY_INDEX", (short)7, true, true, true);
    addAttribute("TXN_DATE", (short)2, false);
    addAttribute("TXN_TIME", (short)5, false);
    addAttribute("AMOUNT", (short)6, false);
    addAttribute("TXN_TYPE", (short)3, false);
    addAttribute("STORE_ID", (short)1, false);
    setKeyAttr("PAY_INDEX");
  }
  
  public CashTxnBaseRow getNewRow()
  {
    return new CashTxnBaseRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.CashTxnBaseTableDef
 * JD-Core Version:    0.7.0.1
 */