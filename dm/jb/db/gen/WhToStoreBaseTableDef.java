package dm.jb.db.gen;

import dm.tools.db.DBObjectDef;
import java.util.ArrayList;

public class WhToStoreBaseTableDef
  extends DBObjectDef
{
  protected WhToStoreBaseTableDef()
  {
    super("WH_TO_STORE");
    initAttrs();
  }
  
  private void initAttrs()
  {
    addAttribute("TXN_NO", (short)1, true, true, true);
    addAttribute("DATE", (short)2, false);
    addAttribute("TIME", (short)5, false);
    addAttribute("CREATED_BY", (short)1, false);
    addAttribute("STORE_ID", (short)1, false);
  }
  
  public WhToStoreBaseRow getNewRow()
  {
    return new WhToStoreBaseRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.WhToStoreBaseTableDef
 * JD-Core Version:    0.7.0.1
 */