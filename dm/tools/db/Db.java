package dm.tools.db;

import dm.tools.utils.Config;

public class Db
{
  public static final int DBTYPE_ORACLE = 1;
  public static final int DBTYPE_MYSQL = 2;
  public static final int DBTYPE_POSTGRESQL = 3;
  public static final int DBACCESS_INSERT = 1;
  public static final int DBACCESS_DELETE = 2;
  public static final int DBACCESS_SELECT = 4;
  private static int _mDBType = 1;
  private static DBConnection _mConn = null;
  
  public static void setConnection(DBConnection paramDBConnection)
  {
    _mConn = paramDBConnection;
  }
  
  public static int getDBType()
  {
    return _mDBType;
  }
  
  public static void createConnection(String paramString1, int paramInt1, String paramString2, String paramString3, int paramInt2, String paramString4)
    throws DBException
  {
    _mConn = DBConnection.createConnection(paramString1, paramInt1, paramString2, paramString3, paramInt2, paramString4);
    _mDBType = paramInt2;
  }
  
  public static DBConnection getConnection()
  {
    return _mConn;
  }
  
  public static boolean isConnected()
  {
    return _mConn != null;
  }
  
  public static String getSearchFormattedString(String paramString)
  {
    return paramString.replace("*", "%");
  }
  
  public static boolean hasNonSearchChar(String paramString)
  {
    char[] arrayOfChar = paramString.toCharArray();
    for (int i = 0; i < arrayOfChar.length; i++) {
      if ((arrayOfChar[i] != '*') && (arrayOfChar[i] != '%')) {
        return true;
      }
    }
    return false;
  }
  
  public static void initStatic(String paramString)
  {
    String str = Config.INSTANCE.getAttrib(paramString + ".DB_CONFIG.SQL_FLAVOUR");
    if (str != null) {
      if (str.equalsIgnoreCase("MySql")) {
        _mDBType = 2;
      } else if (str.equalsIgnoreCase("PostgreSQL")) {
        _mDBType = 3;
      }
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.db.Db
 * JD-Core Version:    0.7.0.1
 */