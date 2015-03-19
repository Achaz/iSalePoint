package dm.jb.db.objects;

import dm.jb.db.gen.UserInfoBaseTableDef;
import dm.tools.db.DBException;
import java.util.ArrayList;
import java.util.Iterator;

public class UserInfoTableDef
  extends UserInfoBaseTableDef
{
  private static UserInfoTableDef _mInstance = null;
  private static UserInfoRow _mCurrentUser = null;
  
  public static UserInfoTableDef getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new UserInfoTableDef();
    }
    return _mInstance;
  }
  
  public UserInfoRow getNewRow()
  {
    return new UserInfoRow(getAttrList().size(), this);
  }
  
  public UserInfoRow getUserInfoForUid(String paramString)
    throws DBException
  {
    return findRowByKey(paramString);
  }
  
  public UserInfoRow findRowByKey(String paramString)
    throws DBException
  {
    ArrayList localArrayList = getAllValuesWithWhereClause("USER_ID like '" + paramString + "'");
    if ((localArrayList == null) || (localArrayList.size() == 0)) {
      return null;
    }
    return (UserInfoRow)localArrayList.get(0);
  }
  
  public UserInfoRow findByRFID(String paramString)
    throws DBException
  {
    ArrayList localArrayList = getAllValuesWithWhereClause("RFID=" + paramString);
    if ((localArrayList == null) || (localArrayList.size() == 0)) {
      return null;
    }
    return (UserInfoRow)localArrayList.get(0);
  }
  
  public ArrayList<UserInfoRow> getUserList()
    throws DBException
  {
    ArrayList localArrayList1 = super.getAllValues();
    ArrayList localArrayList2 = new ArrayList();
    Iterator localIterator = localArrayList1.iterator();
    while (localIterator.hasNext())
    {
      UserInfoRow localUserInfoRow = (UserInfoRow)localIterator.next();
      localArrayList2.add(localUserInfoRow);
    }
    return localArrayList2;
  }
  
  public static UserInfoRow getCurrentUser()
  {
    return _mCurrentUser;
  }
  
  public static void setCurrentUser(UserInfoRow paramUserInfoRow)
  {
    _mCurrentUser = paramUserInfoRow;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.UserInfoTableDef
 * JD-Core Version:    0.7.0.1
 */