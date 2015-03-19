package dm.jb.ui.res;

import java.util.ResourceBundle;

public class MessageResourceUtils
{
  public static MessageResourceUtils INSTANCE = new MessageResourceUtils();
  private ResourceBundle _mBundle = null;
  
  public String getStringInternal(String paramString)
  {
    return this._mBundle.getString(paramString);
  }
  
  public static String getString(String paramString)
  {
    return INSTANCE.getStringInternal(paramString);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.res.MessageResourceUtils
 * JD-Core Version:    0.7.0.1
 */