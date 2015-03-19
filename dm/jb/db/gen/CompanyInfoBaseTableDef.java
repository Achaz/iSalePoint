package dm.jb.db.gen;

import dm.tools.db.DBObjectDef;
import java.util.ArrayList;

public class CompanyInfoBaseTableDef
  extends DBObjectDef
{
  protected CompanyInfoBaseTableDef()
  {
    super("COMPANY_INFO");
    initAttrs();
  }
  
  private void initAttrs()
  {
    addAttribute("NAME", (short)3, true, true, false);
    addAttribute("ADDRESS", (short)3, false);
    addAttribute("PHONE1", (short)3, false);
    setKeyAttr("NAME");
  }
  
  public CompanyInfoBaseRow getNewRow()
  {
    return new CompanyInfoBaseRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.CompanyInfoBaseTableDef
 * JD-Core Version:    0.7.0.1
 */