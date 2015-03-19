package dm.tools.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class DBDeleteStatement
  extends DBStatement
{
  private String _mSqlString = null;
  private String _mTableName = null;
  private String _mWhereClauseString = null;
  private ArrayList<String> _mWhereClauseParams = null;
  private HashMap<String, Object> _mWhereClauseValues = null;
  private boolean _mParamsChanged = false;
  
  public DBDeleteStatement(String paramString)
  {
    this._mTableName = paramString;
    if (paramString == null) {
      throw new NullPointerException("Table Name cannot be null");
    }
    this._mWhereClauseParams = new ArrayList();
    this._mWhereClauseValues = new HashMap();
  }
  
  public void setWhereClause(String paramString)
  {
    this._mWhereClauseString = paramString;
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
  
  private void prepareSqlString()
    throws DBException
  {
    StringBuffer localStringBuffer = new StringBuffer("DELETE FROM " + this._mTableName + " ");
    localStringBuffer.append(" ");
    this._mWhereClauseString = getDeleteWhereClauseSqlStringBuffer().toString();
    localStringBuffer.append(this._mWhereClauseString);
    this._mSqlString = localStringBuffer.toString();
  }
  
  private StringBuffer getDeleteWhereClauseSqlStringBuffer()
    throws DBException
  {
    StringBuffer localStringBuffer = new StringBuffer("WHERE");
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
  
  public String getSqlString()
    throws DBException
  {
    if (this._mSqlString == null) {
      prepareSqlString();
    }
    return this._mSqlString;
  }
  
  public void execute(DBConnection paramDBConnection)
    throws DBException
  {
    executeDelete(paramDBConnection);
  }
  
  public void executeDelete(DBConnection paramDBConnection)
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
    try
    {
      int i = 1;
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
    try
    {
      localPreparedStatement.execute();
    }
    catch (SQLException localSQLException3)
    {
      throw new DBException("Error executing the query.", "SQLException(" + localSQLException3.toString() + ")", "Check the validty of values. If the problem persists contact administrator", null, localSQLException3);
    }
  }
  
  public void validateSql()
    throws DBException
  {}
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.db.DBDeleteStatement
 * JD-Core Version:    0.7.0.1
 */