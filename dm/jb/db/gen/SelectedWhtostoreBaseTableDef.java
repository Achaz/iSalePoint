package dm.jb.db.gen;

import dm.tools.db.DBException;
import dm.tools.db.DBObjectDef;
import java.util.ArrayList;

public class SelectedWhtostoreBaseTableDef
  extends DBObjectDef
{
  protected SelectedWhtostoreBaseTableDef()
  {
    super("SELECTED_WHTOSTORE");
    initAttrs();
  }
  
  private void initAttrs()
  {
    addAttribute("TXN_NO", (short)1, true, true);
    addAttribute("SL_NO", (short)1, true, true);
    addAttribute("SELECTED", (short)3, false);
    String[] arrayOfString = { "TXN_NO", "SL_NO" };
    try
    {
      setIndexAttrs(arrayOfString);
    }
    catch (DBException localDBException)
    {
      localDBException.printStackTrace();
    }
  }
  
  public SelectedWhtostoreBaseRow getNewRow()
  {
    return new SelectedWhtostoreBaseRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.SelectedWhtostoreBaseTableDef
 * JD-Core Version:    0.7.0.1
 */