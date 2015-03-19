package dm.jb.db.gen;

import dm.tools.db.DBObjectDef;
import java.util.ArrayList;

public class WearehouseInfoBaseTableDef
  extends DBObjectDef
{
  protected WearehouseInfoBaseTableDef()
  {
    super("WEAREHOUSE_INFO");
    initAttrs();
  }
  
  private void initAttrs()
  {
    addAttribute("WEAREHOUSE_ID", (short)1, true, true, true);
    addAttribute("WEAREHOUSE_NAME", (short)3, false);
    addAttribute("WEAREHOUSE_ADDRESS", (short)3, false);
    setKeyAttr("WEAREHOUSE_ID");
  }
  
  public WearehouseInfoBaseRow getNewRow()
  {
    return new WearehouseInfoBaseRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.WearehouseInfoBaseTableDef
 * JD-Core Version:    0.7.0.1
 */