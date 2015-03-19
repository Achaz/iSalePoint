package dm.jb.db.gen;

import dm.tools.db.DBRow;
import java.sql.Date;
import java.sql.Time;

public class WhToStoreBaseRow
  extends DBRow
{
  protected WhToStoreBaseRow(int paramInt, WhToStoreBaseTableDef paramWhToStoreBaseTableDef)
  {
    super(paramInt, paramWhToStoreBaseTableDef);
  }
  
  public void setValues(Date paramDate, Time paramTime, int paramInt1, int paramInt2)
  {
    setDate(paramDate);
    setTime(paramTime);
    setCreatedBy(paramInt1);
    setStoreId(paramInt2);
  }
  
  public int getTxnNo()
  {
    return ((Integer)getValue("TXN_NO")).intValue();
  }
  
  public void setDate(Date paramDate)
  {
    setValue("DATE", paramDate);
  }
  
  public Date getDate()
  {
    Date localDate = (Date)getValue("DATE");
    return localDate;
  }
  
  public void setTime(Time paramTime)
  {
    setValue("TIME", paramTime);
  }
  
  public Time getTime()
  {
    Time localTime = (Time)getValue("TIME");
    return localTime;
  }
  
  public void setCreatedBy(int paramInt)
  {
    setValue("CREATED_BY", Integer.valueOf(paramInt));
  }
  
  public int getCreatedBy()
  {
    return ((Integer)getValue("CREATED_BY")).intValue();
  }
  
  public void setStoreId(int paramInt)
  {
    setValue("STORE_ID", Integer.valueOf(paramInt));
  }
  
  public int getStoreId()
  {
    return ((Integer)getValue("STORE_ID")).intValue();
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.WhToStoreBaseRow
 * JD-Core Version:    0.7.0.1
 */