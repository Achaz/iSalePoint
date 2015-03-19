package dm.tools.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class DBInsertStatement
  extends DBStatement
{
  private String _mTableName = null;
  private ArrayList<String> _mParams = null;
  private String _mSqlString = null;
  private HashMap<String, Object> _mValueList = null;
  private boolean _mParamsChanged = false;
  
  public DBInsertStatement(String paramString)
  {
    this._mTableName = paramString;
    if (paramString == null) {
      throw new NullPointerException("Table Name cannot be null");
    }
    this._mParams = new ArrayList();
    this._mValueList = new HashMap();
  }
  
  public void addInsertParam(String paramString)
  {
    this._mParams.add(paramString);
    this._mParamsChanged = true;
  }
  
  public void setInsertValue(String paramString, Object paramObject)
  {
    this._mValueList.put(paramString, paramObject);
  }
  
  private void prepareSQLString()
  {
    StringBuffer localStringBuffer1 = new StringBuffer("INSERT INTO " + this._mTableName + " ");
    StringBuffer localStringBuffer2 = new StringBuffer(" VALUES(");
    localStringBuffer1.append("( ");
    Iterator localIterator = this._mParams.iterator();
    boolean bool = localIterator.hasNext();
    while (bool)
    {
      String str = (String)localIterator.next();
      localStringBuffer1.append(" " + str);
      localStringBuffer2.append(" ?");
      bool = localIterator.hasNext();
      if (bool)
      {
        localStringBuffer1.append(",");
        localStringBuffer2.append(",");
      }
    }
    localStringBuffer1.append(" )");
    localStringBuffer2.append(" )");
    localStringBuffer1.append(localStringBuffer2);
    this._mSqlString = localStringBuffer1.toString();
    this._mParamsChanged = false;
  }
  
  public void validateSql()
    throws DBException
  {
    if (this._mParams.size() == 0) {
      throw new DBException("Insert parameters are not specfied", "Insert paremeters are not specified", " Add some parameters for insertion", null, null);
    }
  }
  
  public String getSqlString()
    throws DBException
  {
    if (this._mSqlString == null) {
      prepareSQLString();
    }
    return this._mSqlString;
  }
  
  public void execute(DBConnection paramDBConnection)
    throws DBException
  {
    executeUpdate(paramDBConnection);
  }
  
  public void executeUpdate(DBConnection paramDBConnection)
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
    validateValues();
    PreparedStatement localPreparedStatement = (PreparedStatement)this._mStatement;
    Iterator localIterator = this._mParams.iterator();
    boolean bool = localIterator.hasNext();
    int i = 1;
    try
    {
      while (bool)
      {
        String str = (String)localIterator.next();
        Object localObject = this._mValueList.get(str);
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
        } else if ((localObject instanceof java.util.Date)) {
          localPreparedStatement.setDate(i, new java.sql.Date(((java.util.Date)localObject).getTime()));
        }
        i++;
        bool = localIterator.hasNext();
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
  
  private void validateValues()
    throws DBException
  {
    if (this._mValueList.size() == 0) {
      throw new DBException("Values are not specified", "Insert values are not specified", "Enter the required values", null, null);
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.db.DBInsertStatement
 * JD-Core Version:    0.7.0.1
 */