package dm.jb.db.gen;

import dm.tools.db.DBObjectDef;
import java.util.ArrayList;

public class VendorBaseTableDef
  extends DBObjectDef
{
  protected VendorBaseTableDef()
  {
    super("VENDOR");
    initAttrs();
  }
  
  private void initAttrs()
  {
    addAttribute("VENDOR_ID", (short)1, true, true, true);
    addAttribute("VENDOR_NAME", (short)3, false);
    addAttribute("VENDOR_ADDRESS", (short)3, false);
    addAttribute("VENDOR_PHONE", (short)3, false);
    setKeyAttr("VENDOR_ID");
  }
  
  public VendorBaseRow getNewRow()
  {
    return new VendorBaseRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.VendorBaseTableDef
 * JD-Core Version:    0.7.0.1
 */