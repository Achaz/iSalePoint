package dm.jb.db;

import dm.tools.utils.Config;

public class DBAccess
{
  public static String getDBUserName()
  {
    Object localObject = "billing";
    String str = Config.INSTANCE.getAttrib("JB_CONFIG.DB_USER_NAME.VALUE");
    if ((str != null) && (str.length() > 0)) {
      localObject = str;
    }
    return localObject;
  }
  
  public static String getPassword()
  {
    Object localObject = "gnillib";
    String str = Config.INSTANCE.getAttrib("JB_CONFIG.DB_PASSWORD.VALUE");
    if ((str != null) && (str.length() > 0)) {
      localObject = str;
    }
    localObject = decodePassword((String)localObject);
    return localObject;
  }
  
  private static String decodePassword(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer(paramString);
    localStringBuffer = localStringBuffer.reverse();
    return localStringBuffer.toString();
  }
  
  private static String encodePassword(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer(paramString);
    localStringBuffer = localStringBuffer.reverse();
    return localStringBuffer.toString();
  }
  
  public static void setPassword(String paramString)
  {
    paramString = encodePassword(paramString);
    Config.INSTANCE.setAttrib("JB_CONFIG.DB_PASSWORD.VALUE", paramString);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.DBAccess
 * JD-Core Version:    0.7.0.1
 */