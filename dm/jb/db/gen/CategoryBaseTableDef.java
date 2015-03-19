package dm.jb.db.gen;

import dm.tools.db.DBObjectDef;
import java.util.ArrayList;

public class CategoryBaseTableDef
  extends DBObjectDef
{
  protected CategoryBaseTableDef()
  {
    super("CATEGORY");
    initAttrs();
  }
  
  private void initAttrs()
  {
    addAttribute("CAT_INDEX", (short)1, true, true, true);
    addAttribute("DEPT_INDEX", (short)1, true);
    addAttribute("CAT_NAME", (short)3, true);
    addAttribute("CAT_DETAILS", (short)3, false);
    addAttribute("DISCOUNT", (short)6, false);
    addAttribute("TAX", (short)6, false);
    addAttribute("TAX_UNIT", (short)1, false);
    setKeyAttr("CAT_INDEX");
  }
  
  public CategoryBaseRow getNewRow()
  {
    return new CategoryBaseRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.CategoryBaseTableDef
 * JD-Core Version:    0.7.0.1
 */