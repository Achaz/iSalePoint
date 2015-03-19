package dm.tools.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection
{
  private Connection _mConn = null;
  private boolean _mReturnAtoSupported = false;
  
  public DBConnection(Connection paramConnection)
  {
    this._mConn = paramConnection;
  }
  
  public void close()
  {
    try
    {
      this._mConn.close();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  public Connection getJDBCConnection()
  {
    return this._mConn;
  }
  
  void setAutoCommit(boolean paramBoolean)
    throws DBException
  {
    try
    {
      this._mConn.setAutoCommit(paramBoolean);
    }
    catch (SQLException localSQLException)
    {
      DBException localDBException = new DBException("Error creating the connection", "SQLException(" + localSQLException.toString() + ")", "Check driver", null, null);
      throw localDBException;
    }
  }
  
  static DBConnection createConnection(String paramString1, int paramInt1, String paramString2, String paramString3, int paramInt2, String paramString4)
    throws DBException
  {
    DBConnection localDBConnection = null;
    switch (paramInt2)
    {
    case 1: 
      localDBConnection = getOracleConnection(paramString2, paramString3);
      break;
    case 2: 
      localDBConnection = getMySQLConnection(paramString1, paramInt1, paramString2, paramString3, paramString4);
      localDBConnection._mReturnAtoSupported = true;
      break;
    case 3: 
      localDBConnection = getPSConnection(paramString1, paramInt1, paramString2, paramString3, paramString4);
    }
    localDBConnection.setAutoCommit(false);
    return localDBConnection;
  }
  
  private static DBConnection getMySQLConnection(String paramString1, int paramInt, String paramString2, String paramString3, String paramString4)
    throws DBException
  {
    DBConnection localDBConnection = null;
    String str1 = paramString4;
    try
    {
      Class.forName("com.mysql.jdbc.Driver");
      String str2 = new String("jdbc:mysql://" + paramString1 + ":" + paramInt + "/" + str1);
      localObject = DriverManager.getConnection(str2, paramString2, paramString3);
      ((Connection)localObject).setAutoCommit(false);
      ((Connection)localObject).setTransactionIsolation(2);
      ((Connection)localObject).commit();
      localDBConnection = new DBConnection((Connection)localObject);
    }
    catch (SQLException localSQLException)
    {
      localObject = new DBException("Error creating the connection", "SQLException(" + localSQLException.toString() + ")", "Check the DB access/username/password", null, localSQLException);
      throw ((Throwable)localObject);
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      Object localObject = new DBException("Error creating the connection", "ClassNotFoundException(" + localClassNotFoundException.toString() + ")", "Check driver", null, null);
      throw ((Throwable)localObject);
    }
    return localDBConnection;
  }
  
  private static DBConnection getPSConnection(String paramString1, int paramInt, String paramString2, String paramString3, String paramString4)
    throws DBException
  {
    DBConnection localDBConnection = null;
    String str1 = paramString4;
    try
    {
      Class.forName("org.postgresql.Driver");
      String str2 = new String("jdbc:postgresql://" + paramString1 + ":" + paramInt + "/" + str1);
      localObject = DriverManager.getConnection(str2, paramString2, paramString3);
      ((Connection)localObject).setAutoCommit(false);
      localDBConnection = new DBConnection((Connection)localObject);
    }
    catch (SQLException localSQLException)
    {
      localObject = new DBException("Error creating the connection", "SQLException(" + localSQLException.toString() + ")", "Check the DB access/username/password", null, localSQLException);
      throw ((Throwable)localObject);
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      Object localObject = new DBException("Error creating the connection", "ClassNotFoundException(" + localClassNotFoundException.toString() + ")", "Check driver", null, null);
      throw ((Throwable)localObject);
    }
    return localDBConnection;
  }
  
  private static DBConnection getOracleConnection(String paramString1, String paramString2)
    throws DBException
  {
    DBConnection localDBConnection = null;
    String str1 = "127.0.0.1";
    String str2 = "XE";
    String str3 = "1521";
    String str4 = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=tcp)(HOST=" + str1 + ")(PORT=" + str3 + "))(CONNECT_DATA=(SID=" + str2 + ")))";
    try
    {
      Class.forName("oracle.jdbc.driver.OracleDriver");
      Connection localConnection = DriverManager.getConnection(str4, paramString1, paramString2);
      localDBConnection = new DBConnection(localConnection);
    }
    catch (SQLException localSQLException)
    {
      localDBException = new DBException("Error creating the connection", "SQLException(" + localSQLException.toString() + ")", "Check the DB access/username/password", null, localSQLException);
      localSQLException.printStackTrace();
      throw localDBException;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      DBException localDBException = new DBException("Error creating the connection", "ClassNotFoundException(" + localClassNotFoundException.toString() + ")", "Check driver", null, null);
      throw localDBException;
    }
    return localDBConnection;
  }
  
  public Statement createStatement()
    throws DBException
  {
    try
    {
      Statement localStatement = this._mConn.createStatement();
      return localStatement;
    }
    catch (SQLException localSQLException)
    {
      throw new DBException("Internal error(Creating SQL statement) ", "SQLException (" + localSQLException.toString() + ")", "Contact the administrator", null, localSQLException);
    }
  }
  
  public void createStatement(DBStatement paramDBStatement)
    throws DBException
  {
    paramDBStatement.validateSql();
    try
    {
      Statement localStatement = this._mConn.createStatement();
      paramDBStatement.setStatement(localStatement);
    }
    catch (SQLException localSQLException)
    {
      throw new DBException("Internal error(Creating SQL statement) ", "SQLException (" + localSQLException.toString() + ")", "Contact the administrator", null, localSQLException);
    }
  }
  
  public void createPreparedStatement(DBStatement paramDBStatement)
    throws DBException
  {
    paramDBStatement.validateSql();
    try
    {
      PreparedStatement localPreparedStatement = this._mConn.prepareStatement(paramDBStatement.getSqlString());
      paramDBStatement.setStatement(localPreparedStatement);
    }
    catch (SQLException localSQLException)
    {
      throw new DBException("Internal error(Creating SQL statement) ", "SQLException (" + localSQLException.toString() + ")", "Contact the administrator", null, localSQLException);
    }
  }
  
  public void commit()
    throws DBException
  {
    try
    {
      this._mConn.commit();
    }
    catch (SQLException localSQLException)
    {
      throw new DBException("Internal error(Commiting) ", "SQLException (" + localSQLException.toString() + ")", "Contact the administrator", null, localSQLException);
    }
  }
  
  public void rollback()
    throws DBException
  {
    try
    {
      this._mConn.rollback();
    }
    catch (SQLException localSQLException)
    {
      throw new DBException("Internal error(Rollback) ", "SQLException (" + localSQLException.toString() + ")", "Contact the administrator", null, localSQLException);
    }
  }
  
  public void rollbackNoExp()
  {
    try
    {
      this._mConn.rollback();
    }
    catch (SQLException localSQLException)
    {
      localSQLException.printStackTrace();
    }
  }
  
  public void openTrans()
    throws DBException
  {}
  
  public void endTrans()
    throws DBException
  {
    try
    {
      commit();
    }
    catch (DBException localDBException)
    {
      throw localDBException;
    }
  }
  
  public PreparedStatement createPreparedStatement(String paramString)
    throws DBException
  {
    return createPreparedStatement(paramString, false);
  }
  
  public PreparedStatement createPreparedStatement(String paramString, boolean paramBoolean)
    throws DBException
  {
    PreparedStatement localPreparedStatement = null;
    try
    {
      if ((paramBoolean) && (this._mReturnAtoSupported)) {
        localPreparedStatement = this._mConn.prepareStatement(paramString, 1);
      } else {
        localPreparedStatement = this._mConn.prepareStatement(paramString);
      }
    }
    catch (SQLException localSQLException)
    {
      throw new DBException("SQL error preparing the statement.", "SQL ecception", "Contact developer", null, localSQLException);
    }
    return localPreparedStatement;
  }
  
  boolean isReturnAutoSupported()
  {
    return this._mReturnAtoSupported;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.db.DBConnection
 * JD-Core Version:    0.7.0.1
 */