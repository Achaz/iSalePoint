package dm.jb.db.objects;

import dm.jb.db.gen.UserInfoBaseRow;

public class UserInfoRow
  extends UserInfoBaseRow
{
  public static final int ACCOUNT_STATUS_LOCKED = 1;
  public static final int ACCOUNT_STATUS_NEW_PASSWORD = 2;
  
  UserInfoRow(int paramInt, UserInfoTableDef paramUserInfoTableDef)
  {
    super(paramInt, paramUserInfoTableDef);
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer(getUserId());
    localStringBuffer.append("(");
    localStringBuffer.append(getUserName());
    localStringBuffer.append(")");
    return localStringBuffer.toString();
  }
  
  public String getPassword()
  {
    String str = super.getPassword();
    return str;
  }
  
  public void setPassword(String paramString)
  {
    String str = paramString;
    super.setPassword(str);
  }
  
  public boolean hasAccess(long paramLong)
  {
    return (getAccessParam() & paramLong) > 0L;
  }
  
  public void setLocked(boolean paramBoolean)
  {
    super.setLocked(paramBoolean ? "Y" : "N");
  }
  
  public boolean isLocked()
  {
    return super.getLocked().equals("Y");
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.UserInfoRow
 * JD-Core Version:    0.7.0.1
 */