package dm.tools.db;

import java.sql.SQLException;
import java.sql.Statement;

public abstract class DBStatement
{
  protected Statement _mStatement = null;
  
  public abstract String getSqlString()
    throws DBException;
  
  public abstract void validateSql()
    throws DBException;
  
  public void setStatement(Statement paramStatement)
  {
    this._mStatement = paramStatement;
  }
  
  public void execute(DBConnection paramDBConnection)
    throws DBException
  {}
  
  public void executeUpdate(DBConnection paramDBConnection)
    throws DBException
  {}
  
  public void close()
    throws DBException
  {
    try
    {
      this._mStatement.close();
    }
    catch (SQLException localSQLException)
    {
      DBException localDBException = new DBException("Error closing statement", "SQLException(" + localSQLException.toString() + ")", "Contact administrator", null, localSQLException);
      throw localDBException;
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.db.DBStatement
 * JD-Core Version:    0.7.0.1
 */