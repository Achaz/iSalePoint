package dm.jb.db.gen;

import dm.tools.db.DBObjectDef;
import java.util.ArrayList;

public class DefectiveProductBaseTableDef
  extends DBObjectDef
{
  protected DefectiveProductBaseTableDef()
  {
    super("DEFECTIVE_PRODUCT");
    initAttrs();
  }
  
  private void initAttrs()
  {
    addAttribute("PRODUCT_ID", (short)7, false);
    addAttribute("STORE_ID", (short)1, false);
    addAttribute("QUANTITY", (short)6, false);
    addAttribute("RECIEVE_DATE", (short)2, false);
    addAttribute("QUANTITY_SENT", (short)6, false);
    addAttribute("OBJECT_INDEX", (short)7, true, true, true);
    setKeyAttr("OBJECT_INDEX");
  }
  
  public DefectiveProductBaseRow getNewRow()
  {
    return new DefectiveProductBaseRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.DefectiveProductBaseTableDef
 * JD-Core Version:    0.7.0.1
 */