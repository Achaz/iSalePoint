package dm.jb.db.gen;

import dm.tools.db.DBRow;

public class UserInfoBaseRow
  extends DBRow
{
  protected UserInfoBaseRow(int paramInt, UserInfoBaseTableDef paramUserInfoBaseTableDef)
  {
    super(paramInt, paramUserInfoBaseTableDef);
  }
  
  public void setValues(String paramString1, String paramString2, long paramLong, int paramInt, String paramString3, String paramString4, String paramString5)
  {
    setUserId(paramString1);
    setUserName(paramString2);
    setAccessParam(paramLong);
    setStatus(paramInt);
    setPassword(paramString3);
    setRfid(paramString4);
    setLocked(paramString5);
  }
  
  public void setUserId(String paramString)
  {
    setValue("USER_ID", paramString);
  }
  
  public String getUserId()
  {
    return (String)getValue("USER_ID");
  }
  
  public void setUserName(String paramString)
  {
    setValue("USER_NAME", paramString);
  }
  
  public String getUserName()
  {
    return (String)getValue("USER_NAME");
  }
  
  public void setAccessParam(long paramLong)
  {
    setValue("ACCESS_PARAM", Long.valueOf(paramLong));
  }
  
  public long getAccessParam()
  {
    return ((Long)getValue("ACCESS_PARAM")).longValue();
  }
  
  public void setStatus(int paramInt)
  {
    setValue("STATUS", Integer.valueOf(paramInt));
  }
  
  public int getStatus()
  {
    return ((Integer)getValue("STATUS")).intValue();
  }
  
  public void setPassword(String paramString)
  {
    setValue("PASSWORD", paramString);
  }
  
  public String getPassword()
  {
    return (String)getValue("PASSWORD");
  }
  
  public void setUserIndex(int paramInt)
  {
    setValue("USER_INDEX", Integer.valueOf(paramInt));
  }
  
  public int getUserIndex()
  {
    return ((Integer)getValue("USER_INDEX")).intValue();
  }
  
  public void setRfid(String paramString)
  {
    setValue("RFID", paramString);
  }
  
  public String getRfid()
  {
    return (String)getValue("RFID");
  }
  
  public void setLocked(String paramString)
  {
    setValue("LOCKED", paramString);
  }
  
  public String getLocked()
  {
    return (String)getValue("LOCKED");
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.UserInfoBaseRow
 * JD-Core Version:    0.7.0.1
 */