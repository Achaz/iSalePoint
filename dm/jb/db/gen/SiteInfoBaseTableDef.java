package dm.jb.db.gen;

import dm.tools.db.DBObjectDef;
import java.util.ArrayList;

public class SiteInfoBaseTableDef
  extends DBObjectDef
{
  protected SiteInfoBaseTableDef()
  {
    super("SITE_INFO");
    initAttrs();
  }
  
  private void initAttrs()
  {
    addAttribute("SITE_INDEX", (short)1, true, true, true);
    addAttribute("SITE_ID", (short)3, true);
    addAttribute("STORE_ID", (short)1, true);
    setKeyAttr("SITE_INDEX");
  }
  
  public SiteInfoBaseRow getNewRow()
  {
    return new SiteInfoBaseRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.SiteInfoBaseTableDef
 * JD-Core Version:    0.7.0.1
 */