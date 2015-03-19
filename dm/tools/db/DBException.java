package dm.tools.db;

import java.io.PrintStream;
import java.sql.SQLException;

public class DBException
  extends Exception
{
  private SQLException _mSQLException = null;
  private String _mMessage = null;
  private String _mCause = null;
  private String _mActions = null;
  private DBException _mBaseException = null;
  
  public DBException(String paramString1, String paramString2, String paramString3, DBException paramDBException, SQLException paramSQLException)
  {
    this._mSQLException = paramSQLException;
    this._mBaseException = paramDBException;
    this._mActions = paramString3;
    this._mMessage = paramString1;
    this._mCause = paramString2;
  }
  
  public String getExpCause()
  {
    return this._mCause;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("DBException :" + this._mMessage + "\n");
    localStringBuffer.append("  Cause : " + this._mCause + "\n");
    localStringBuffer.append("Suggested action :\n     " + this._mActions);
    return localStringBuffer.toString();
  }
  
  public SQLException getSQLException()
  {
    return this._mSQLException;
  }
  
  public DBException getBaseException()
  {
    return this._mBaseException;
  }
  
  public void printStackTrace()
  {
    super.printStackTrace();
    if (this._mBaseException != null)
    {
      System.err.println("Caused by");
      this._mBaseException.printStackTrace();
    }
    if (this._mSQLException != null)
    {
      System.err.println("SQL Exception ");
      this._mSQLException.printStackTrace();
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.db.DBException
 * JD-Core Version:    0.7.0.1
 */