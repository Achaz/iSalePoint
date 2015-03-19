package dm.jb.db.gen;

import dm.tools.db.DBObjectDef;
import java.util.ArrayList;

public class PoPaymentBaseTableDef
  extends DBObjectDef
{
  protected PoPaymentBaseTableDef()
  {
    super("PO_PAYMENT");
    initAttrs();
  }
  
  private void initAttrs()
  {
    addAttribute("PURCHASE_ORDER_NO", (short)1, false);
    addAttribute("AMOUNT_PAID", (short)6, false);
    addAttribute("DATE", (short)2, false);
  }
  
  public PoPaymentBaseRow getNewRow()
  {
    return new PoPaymentBaseRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.PoPaymentBaseTableDef
 * JD-Core Version:    0.7.0.1
 */