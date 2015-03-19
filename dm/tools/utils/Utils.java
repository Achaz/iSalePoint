package dm.tools.utils;

import java.util.GregorianCalendar;

public class Utils
{
  private static GregorianCalendar calForAll = new GregorianCalendar();
  
  public static java.sql.Date getSqlDateForJavaDate(java.util.Date paramDate)
  {
    long l = paramDate.getTime() + calForAll.get(15) + calForAll.get(16);
    return new java.sql.Date(l);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.utils.Utils
 * JD-Core Version:    0.7.0.1
 */