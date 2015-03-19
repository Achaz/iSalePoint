package dm.tools.db;

import java.io.PrintStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NextIndex
{
  private static PreparedStatement _mSelectStatement = null;
  private static PreparedStatement _mSelectForUpdateStatement = null;
  private static PreparedStatement _mUpdateStatement = null;
  
  public static int getNextIndex(String paramString)
    throws DBException
  {
    int i = -1;
    if (_mSelectStatement == null) {
      _mSelectStatement = Db.getConnection().createPreparedStatement("SELECT VALUE FROM INDEX_TABLE WHERE NAME=?");
    }
    try
    {
      _mSelectStatement.setString(1, paramString);
      _mSelectStatement.execute();
      ResultSet localResultSet = _mSelectStatement.getResultSet();
      if (!localResultSet.next()) {
        return -1;
      }
      i = localResultSet.getInt(1);
    }
    catch (SQLException localSQLException)
    {
      System.err.println(localSQLException);
      throw new DBException("SQLException executing select.", "Executing select for nextindex", "Contact developer", null, localSQLException);
    }
    return i;
  }
  
  public static void updateNextIndex(String paramString)
    throws DBException
  {
    int i = -1;
    if (_mSelectForUpdateStatement == null) {
      _mSelectForUpdateStatement = Db.getConnection().createPreparedStatement("SELECT VALUE FROM INDEX_TABLE WHERE NAME=? FOR UPDATE");
    }
    try
    {
      _mSelectForUpdateStatement.setString(1, paramString);
      _mSelectForUpdateStatement.execute();
      ResultSet localResultSet = _mSelectForUpdateStatement.getResultSet();
      if (!localResultSet.first()) {
        return;
      }
      i = localResultSet.getInt(1);
      i++;
      if (_mUpdateStatement == null) {
        _mUpdateStatement = Db.getConnection().createPreparedStatement("UPDATE INDEX_TABLE SET VALUE=? WHERE NAME=?");
      }
      _mUpdateStatement.setInt(1, i);
      _mUpdateStatement.setString(2, paramString);
      _mUpdateStatement.execute();
    }
    catch (SQLException localSQLException)
    {
      System.err.println(localSQLException);
      throw new DBException("SQLException executing select and update for nextindex", "Updating index", "Contact developer", null, localSQLException);
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.db.NextIndex
 * JD-Core Version:    0.7.0.1
 */