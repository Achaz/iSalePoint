package dm.jb.db.gen;

import dm.tools.db.DBRow;
import java.sql.Date;
import java.sql.Time;

public class StockReturnBaseRow
  extends DBRow
{
  protected StockReturnBaseRow(int paramInt, StockReturnBaseTableDef paramStockReturnBaseTableDef)
  {
    super(paramInt, paramStockReturnBaseTableDef);
  }
  
  public void setValues(long paramLong, Date paramDate, Time paramTime, int paramInt)
  {
    setVendor(paramLong);
    setCreateDate(paramDate);
    setCreateTime(paramTime);
    setUser(paramInt);
  }
  
  public void setTxnNo(long paramLong)
  {
    setValue("TXN_NO", Long.valueOf(paramLong));
  }
  
  public long getTxnNo()
  {
    return ((Long)getValue("TXN_NO")).longValue();
  }
  
  public void setVendor(long paramLong)
  {
    setValue("VENDOR", Long.valueOf(paramLong));
  }
  
  public long getVendor()
  {
    return ((Long)getValue("VENDOR")).longValue();
  }
  
  public void setCreateDate(Date paramDate)
  {
    setValue("CREATE_DATE", paramDate);
  }
  
  public Date getCreateDate()
  {
    Date localDate = (Date)getValue("CREATE_DATE");
    return localDate;
  }
  
  public void setCreateTime(Time paramTime)
  {
    setValue("CREATE_TIME", paramTime);
  }
  
  public Time getCreateTime()
  {
    Time localTime = (Time)getValue("CREATE_TIME");
    return localTime;
  }
  
  public void setUser(int paramInt)
  {
    setValue("USER", Integer.valueOf(paramInt));
  }
  
  public int getUser()
  {
    return ((Integer)getValue("USER")).intValue();
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.StockReturnBaseRow
 * JD-Core Version:    0.7.0.1
 */