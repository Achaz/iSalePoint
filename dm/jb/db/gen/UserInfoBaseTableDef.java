package dm.jb.db.gen;

import dm.tools.db.DBObjectDef;
import java.util.ArrayList;

public class UserInfoBaseTableDef
  extends DBObjectDef
{
  protected UserInfoBaseTableDef()
  {
    super("USER_INFO");
    initAttrs();
  }
  
  private void initAttrs()
  {
    addAttribute("USER_ID", (short)3, true, true);
    addAttribute("USER_NAME", (short)3, true);
    addAttribute("ACCESS_PARAM", (short)7, false);
    addAttribute("STATUS", (short)1, false);
    addAttribute("PASSWORD", (short)3, false);
    addAttribute("USER_INDEX", (short)1, true, true, true);
    addAttribute("RFID", (short)3, false);
    addAttribute("LOCKED", (short)3, false);
    setKeyAttr("USER_INDEX");
  }
  
  public UserInfoBaseRow getNewRow()
  {
    return new UserInfoBaseRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.UserInfoBaseTableDef
 * JD-Core Version:    0.7.0.1
 */