package dm.tools.db;

import java.sql.SQLException;
import java.sql.Statement;

public class DBSimpleStatement
  extends DBStatement
{
  private String _mSql = null;
  
  public DBSimpleStatement(String paramString)
  {
    this._mSql = paramString;
  }
  
  public String getSqlString()
  {
    return this._mSql;
  }
  
  public void validateSql()
    throws DBException
  {}
  
  public void execute(DBConnection paramDBConnection)
    throws DBException
  {
    if ((this._mSql == null) || (this._mSql.length() == 0)) {
      throw new DBException("SQL cannot be empty", "Internal error", "Conact administrator", null, null);
    }
    if (this._mStatement == null) {
      paramDBConnection.createPreparedStatement(this);
    }
    try
    {
      this._mStatement.execute(this._mSql);
    }
    catch (SQLException localSQLException)
    {
      throw new DBException("Error executing the query.", "SQLException(" + localSQLException.toString() + ")", "Check the validty of values. If the problem persists contact administrator", null, localSQLException);
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.db.DBSimpleStatement
 * JD-Core Version:    0.7.0.1
 */