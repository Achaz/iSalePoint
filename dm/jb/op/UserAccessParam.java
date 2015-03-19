package dm.jb.op;

import java.util.ArrayList;

public class UserAccessParam
{
  public static final long USER_ACCESS_MANAGE_USER = 1L;
  public static final long USER_ACCESS_MANAGE_PRODUCT = 2L;
  public static final long USER_ACCESS_INV_MANAGE = 4L;
  public static final long USER_ACCESS_CONFIG = 8L;
  public static final long USER_ACCESS_PRINT_CONFIG = 16L;
  public static final long USER_ACCESS_CUSTOMER_MANAGE = 32L;
  public static final long USER_ACCESS_COMPANY_CONFIG = 64L;
  public static final long USER_ACCESS_WAREHOUSE_CONFIG = 128L;
  public static final long USER_ACCESS_STORE_CONFIG = 256L;
  public static final long USER_ACCESS_EDIT_STOCK = 512L;
  public static final long USER_ACCESS_WH_TO_STORE_SYNC = 1024L;
  public static final long USER_ACCESS_CLEANUP = 2048L;
  public static final long USER_ACCESS_PO_CREATE = 4096L;
  public static final long USER_ACCESS_REPORT = 8192L;
  public static final long USER_ACCESS_ADMIN = -1L;
  public static final UserAccessParam[] accessList = { new UserAccessParam(-1L, "Administrator"), new UserAccessParam(1L, "Manage User"), new UserAccessParam(2L, "Product Management"), new UserAccessParam(4L, "Inventory Management"), new UserAccessParam(8L, "General Configuration"), new UserAccessParam(16L, "Printing Configuration"), new UserAccessParam(32L, "Manage Customers"), new UserAccessParam(64L, "Configure Company"), new UserAccessParam(128L, "Manage Warehouse"), new UserAccessParam(256L, "Manage Stores"), new UserAccessParam(512L, "Edit Stock"), new UserAccessParam(1024L, "Recieve goods ate store"), new UserAccessParam(2048L, "Cleanup"), new UserAccessParam(4096L, "Purchase Order create"), new UserAccessParam(8192L, "Reporting") };
  private static ArrayList<UserAccessParam> _mAccessArrayList = null;
  private long _mCode = 0L;
  private String _mName = null;
  
  public UserAccessParam(long paramLong, String paramString)
  {
    this._mCode = paramLong;
    this._mName = paramString;
  }
  
  public String toString()
  {
    return this._mName;
  }
  
  public static ArrayList<UserAccessParam> getAccessListAsArrayList()
  {
    if (_mAccessArrayList == null)
    {
      _mAccessArrayList = new ArrayList();
      for (int i = 0; i < accessList.length; i++) {
        _mAccessArrayList.add(accessList[i]);
      }
    }
    return _mAccessArrayList;
  }
  
  public long getCode()
  {
    return this._mCode;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.op.UserAccessParam
 * JD-Core Version:    0.7.0.1
 */