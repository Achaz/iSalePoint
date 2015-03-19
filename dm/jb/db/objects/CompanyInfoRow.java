package dm.jb.db.objects;

import dm.jb.db.gen.CompanyInfoBaseRow;
import dm.tools.db.DBConnection;
import dm.tools.db.DBException;
import dm.tools.db.Db;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CompanyInfoRow
  extends CompanyInfoBaseRow
{
  CompanyInfoRow(int paramInt, CompanyInfoTableDef paramCompanyInfoTableDef)
  {
    super(paramInt, paramCompanyInfoTableDef);
  }
  
  public String toString()
  {
    return getName();
  }
  
  public InputStream getReportImage()
    throws DBException
  {
    try
    {
      ResultSet localResultSet = Db.getConnection().getJDBCConnection().prepareStatement("SELECT REPORT_IMAGE FROM COMPANY_INFO").executeQuery();
      if (localResultSet.next()) {
        return localResultSet.getBinaryStream(1);
      }
      return null;
    }
    catch (SQLException localSQLException)
    {
      throw new DBException("Error reading the report image", "SQLException", "Try again later.", null, localSQLException);
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.CompanyInfoRow
 * JD-Core Version:    0.7.0.1
 */