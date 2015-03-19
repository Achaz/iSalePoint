package dm.jb.db.gen;

import dm.tools.db.DBObjectDef;
import java.util.ArrayList;

public class DeptBaseTableDef
  extends DBObjectDef
{
  protected DeptBaseTableDef()
  {
    super("DEPT");
    initAttrs();
  }
  
  private void initAttrs()
  {
    addAttribute("DEPT_INDEX", (short)1, true, true, true);
    addAttribute("DEPT_NAME", (short)3, true);
    addAttribute("DEPT_DETAILS", (short)3, false);
    setKeyAttr("DEPT_INDEX");
  }
  
  public DeptBaseRow getNewRow()
  {
    return new DeptBaseRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.DeptBaseTableDef
 * JD-Core Version:    0.7.0.1
 */