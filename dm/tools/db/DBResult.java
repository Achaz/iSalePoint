package dm.tools.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class DBResult
{
  private ResultSet _mResultSet = null;
  private ArrayList<String> _mSelectParams = null;
  private static long _mOffset = -1L;
  
  public DBResult(ResultSet paramResultSet, ArrayList<String> paramArrayList)
  {
    this._mResultSet = paramResultSet;
    this._mSelectParams = paramArrayList;
    if (_mOffset == -1L)
    {
      GregorianCalendar localGregorianCalendar = new GregorianCalendar();
      _mOffset = localGregorianCalendar.get(15) + localGregorianCalendar.get(16);
    }
  }
  
  public String getString(String paramString)
    throws DBException
  {
    String str = null;
    int i = this._mSelectParams.indexOf(paramString);
    if (i == -1) {
      throw new DBException("Parameter " + paramString + " is not in SELECT clause", "Invalid param", "Check the input parameter", null, null);
    }
    try
    {
      str = this._mResultSet.getString(i + 1);
    }
    catch (SQLException localSQLException)
    {
      throw new DBException("SQLException", localSQLException.toString(), "Check SQL statement.", null, localSQLException);
    }
    return str;
  }
  
  public int getInt(String paramString)
    throws DBException
  {
    int i = -1;
    int j = this._mSelectParams.indexOf(paramString);
    if (j == -1) {
      throw new DBException("Parameter " + paramString + " is not in SELECT clause", "Invalid param", "Check the input parameter", null, null);
    }
    try
    {
      i = this._mResultSet.getInt(j + 1);
    }
    catch (SQLException localSQLException)
    {
      throw new DBException("SQLException", localSQLException.toString(), "Check SQL statement.", null, localSQLException);
    }
    return i;
  }
  
  public java.util.Date getDate(String paramString)
    throws DBException
  {
    java.util.Date localDate = null;
    int i = this._mSelectParams.indexOf(paramString);
    if (i == -1) {
      throw new DBException("Parameter " + paramString + " is not in SELECT clause", "Invalid param", "Check the input parameter", null, null);
    }
    try
    {
      localDate = new java.util.Date(this._mResultSet.getDate(i + 1).getTime());
    }
    catch (SQLException localSQLException)
    {
      throw new DBException("SQLException", localSQLException.toString(), "Check SQL statement.", null, localSQLException);
    }
    return localDate;
  }
  
  public float getFloat(String paramString)
    throws DBException
  {
    float f = -1.0F;
    int i = this._mSelectParams.indexOf(paramString);
    if (i == -1) {
      throw new DBException("Parameter " + paramString + " is not in SELECT clause", "Invalid param", "Check the input parameter", null, null);
    }
    try
    {
      f = this._mResultSet.getFloat(i + 1);
    }
    catch (SQLException localSQLException)
    {
      throw new DBException("SQLException", localSQLException.toString(), "Check SQL statement.", null, localSQLException);
    }
    return f;
  }
  
  public double getDouble(String paramString)
    throws DBException
  {
    double d = -1.0D;
    int i = this._mSelectParams.indexOf(paramString);
    if (i == -1) {
      throw new DBException("Parameter " + paramString + " is not in SELECT clause", "Invalid param", "Check the input parameter", null, null);
    }
    try
    {
      d = this._mResultSet.getDouble(i + 1);
    }
    catch (SQLException localSQLException)
    {
      throw new DBException("SQLException", localSQLException.toString(), "Check SQL statement.", null, localSQLException);
    }
    return d;
  }
  
  public boolean first()
    throws DBException
  {
    boolean bool = true;
    try
    {
      bool = this._mResultSet.next();
    }
    catch (SQLException localSQLException)
    {
      throw new DBException("SQLException", localSQLException.toString(), "Check SQL statement.", null, localSQLException);
    }
    return bool;
  }
  
  public boolean next()
    throws DBException
  {
    boolean bool = true;
    try
    {
      bool = this._mResultSet.next();
    }
    catch (SQLException localSQLException)
    {
      throw new DBException("SQLException", localSQLException.toString(), "Check SQL statement.", null, localSQLException);
    }
    return bool;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.db.DBResult
 * JD-Core Version:    0.7.0.1
 */