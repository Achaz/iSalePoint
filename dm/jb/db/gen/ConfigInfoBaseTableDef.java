package dm.jb.db.gen;

import dm.tools.db.DBObjectDef;
import java.util.ArrayList;

public class ConfigInfoBaseTableDef
  extends DBObjectDef
{
  protected ConfigInfoBaseTableDef()
  {
    super("CONFIG_INFO");
    initAttrs();
  }
  
  private void initAttrs()
  {
    addAttribute("NAME", (short)3, true, true, false);
    addAttribute("VALUE", (short)3, false);
    setKeyAttr("NAME");
  }
  
  public ConfigInfoBaseRow getNewRow()
  {
    return new ConfigInfoBaseRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.ConfigInfoBaseTableDef
 * JD-Core Version:    0.7.0.1
 */