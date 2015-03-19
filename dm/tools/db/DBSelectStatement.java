package dm.tools.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class DBSelectStatement
  extends DBStatement
{
  private String _mSqlString;
  private String _mTableName = null;
  private String _mSelectString = null;
  private String _mWhereString = null;
  public ArrayList<String> _mSelectParams = null;
  private boolean _mParamsChanged = false;
  private DBResult _mResult = null;
  private ArrayList<String> _mWhereClauseParams = null;
  private HashMap<String, Object> _mWhereClauseValues = null;
  private boolean _mLock = false;
  
  public DBSelectStatement(String paramString, boolean paramBoolean)
  {
    if (paramString == null) {
      throw new NullPointerException("Table Name cannot be null");
    }
    this._mTableName = paramString;
    this._mSelectParams = new ArrayList();
    this._mWhereClauseParams = new ArrayList();
    this._mWhereClauseValues = new HashMap();
    this._mLock = paramBoolean;
  }
  
  public DBSelectStatement(String paramString)
  {
    if (paramString == null) {
      throw new NullPointerException("Table Name cannot be null");
    }
    this._mTableName = paramString;
    this._mSelectParams = new ArrayList();
    this._mWhereClauseParams = new ArrayList();
    this._mWhereClauseValues = new HashMap();
  }
  
  public void addSelectParam(String paramString)
  {
    this._mParamsChanged = true;
    this._mSelectParams.add(paramString);
  }
  
  public void addWhereClauseParam(String paramString)
  {
    this._mWhereClauseParams.add(paramString);
    this._mParamsChanged = true;
  }
  
  public void setWhereClauseValue(String paramString, Object paramObject)
  {
    this._mWhereClauseValues.put(paramString, paramObject);
  }
  
  public void setWhereClause(String paramString)
  {
    this._mWhereString = paramString;
  }
  
  public String getSqlString()
    throws DBException
  {
    if (this._mSqlString == null) {
      prepareSqlString();
    }
    return this._mSqlString;
  }
  
  public void validateSql()
    throws DBException
  {}
  
  private void prepareSqlString()
    throws DBException
  {
    StringBuffer localStringBuffer1 = getSelectClauseStringBuffer();
    localStringBuffer1.append(" FROM " + this._mTableName);
    this._mSelectString = localStringBuffer1.toString();
    this._mSqlString = this._mSelectString;
    if ((this._mWhereClauseParams.size() == 0) && ((this._mWhereString == null) || (this._mWhereString.length() == 0)))
    {
      this._mSqlString = this._mSelectString;
      return;
    }
    StringBuffer localStringBuffer2 = getUpdateWhereClauseSqlStringBuffer();
    String str = localStringBuffer2.toString();
    this._mSqlString = (this._mSelectString + str);
    if (this._mLock) {
      this._mSqlString += " FOR UPDATE";
    }
  }
  
  private StringBuffer getSelectClauseStringBuffer()
  {
    StringBuffer localStringBuffer = new StringBuffer("SELECT");
    Iterator localIterator = this._mSelectParams.iterator();
    boolean bool = localIterator.hasNext();
    while (bool)
    {
      String str = (String)localIterator.next();
      localStringBuffer.append(" " + str);
      bool = localIterator.hasNext();
      if (bool) {
        localStringBuffer.append(",");
      }
    }
    return localStringBuffer;
  }
  
  private StringBuffer getUpdateWhereClauseSqlStringBuffer()
    throws DBException
  {
    StringBuffer localStringBuffer = new StringBuffer(" WHERE");
    if ((this._mWhereString != null) && (this._mWhereString.length() != 0))
    {
      localStringBuffer.append(this._mWhereString);
      return localStringBuffer;
    }
    Iterator localIterator = this._mWhereClauseParams.iterator();
    boolean bool = localIterator.hasNext();
    while (bool)
    {
      String str = (String)localIterator.next();
      localStringBuffer.append(" " + str + "=?");
      bool = localIterator.hasNext();
      if (bool) {
        localStringBuffer.append(" AND ");
      }
    }
    this._mParamsChanged = false;
    return localStringBuffer;
  }
  
  public void execute(DBConnection paramDBConnection)
    throws DBException
  {
    if (this._mStatement == null)
    {
      paramDBConnection.createPreparedStatement(this);
    }
    else if (this._mParamsChanged)
    {
      try
      {
        this._mStatement.close();
      }
      catch (SQLException localSQLException1)
      {
        throw new DBException("Internal error(Closing the statement)", "SQLException (" + localSQLException1.toString() + ")", "Contact administrator", null, localSQLException1);
      }
      paramDBConnection.createPreparedStatement(this);
    }
    PreparedStatement localPreparedStatement = (PreparedStatement)this._mStatement;
    int i = 1;
    if (this._mWhereClauseParams.size() != 0) {
      try
      {
        Iterator localIterator = this._mWhereClauseParams.iterator();
        for (boolean bool = localIterator.hasNext(); bool; bool = localIterator.hasNext())
        {
          String str = (String)localIterator.next();
          Object localObject = this._mWhereClauseValues.get(str);
          if (localObject == null) {
            throw new DBException("Internal error (Set prepared values)", "Value not set for " + str, "Contact administrator", null, null);
          }
          if ((localObject instanceof Integer)) {
            localPreparedStatement.setInt(i, ((Integer)localObject).intValue());
          } else if ((localObject instanceof String)) {
            localPreparedStatement.setString(i, (String)localObject);
          } else if ((localObject instanceof Double)) {
            localPreparedStatement.setDouble(i, ((Double)localObject).doubleValue());
          } else if ((localObject instanceof Float)) {
            localPreparedStatement.setFloat(i, ((Float)localObject).floatValue());
          }
          i++;
        }
      }
      catch (SQLException localSQLException2)
      {
        throw new DBException("Internal error (Set prepared values)", "SQLException(" + localSQLException2.toString() + ")", "Contact administrator", null, localSQLException2);
      }
    }
    try
    {
      ResultSet localResultSet = localPreparedStatement.executeQuery();
      this._mResult = new DBResult(localResultSet, this._mSelectParams);
    }
    catch (SQLException localSQLException3)
    {
      throw new DBException("Error executing the query.", "SQLException(" + localSQLException3.toString() + ")", "Check the validty of values. If the problem persists contact administrator", null, localSQLException3);
    }
  }
  
  public DBResult getResult()
  {
    return this._mResult;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.db.DBSelectStatement
 * JD-Core Version:    0.7.0.1
 */