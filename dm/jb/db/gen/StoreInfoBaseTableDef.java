package dm.jb.db.gen;

import dm.tools.db.DBObjectDef;
import java.util.ArrayList;

public class StoreInfoBaseTableDef
  extends DBObjectDef
{
  protected StoreInfoBaseTableDef()
  {
    super("STORE_INFO");
    initAttrs();
  }
  
  private void initAttrs()
  {
    addAttribute("STORE_ID", (short)1, true, true, true);
    addAttribute("STORE_CODE", (short)3, true);
    addAttribute("ADDRESS", (short)3, true);
    addAttribute("PHONE", (short)3, true);
    addAttribute("NAME", (short)3, false);
    setKeyAttr("STORE_ID");
  }
  
  public StoreInfoBaseRow getNewRow()
  {
    return new StoreInfoBaseRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.StoreInfoBaseTableDef
 * JD-Core Version:    0.7.0.1
 */