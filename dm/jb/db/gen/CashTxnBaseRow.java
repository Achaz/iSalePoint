package dm.jb.db.gen;

import dm.tools.db.DBRow;
import java.sql.Date;
import java.sql.Time;

public class CashTxnBaseRow
  extends DBRow
{
  protected CashTxnBaseRow(int paramInt, CashTxnBaseTableDef paramCashTxnBaseTableDef)
  {
    super(paramInt, paramCashTxnBaseTableDef);
  }
  
  public void setValues(long paramLong, Date paramDate, Time paramTime, double paramDouble, String paramString, int paramInt)
  {
    setBillNo(paramLong);
    setTxnDate(paramDate);
    setTxnTime(paramTime);
    setAmount(paramDouble);
    setTxnType(paramString);
    setStoreId(paramInt);
  }
  
  public void setBillNo(long paramLong)
  {
    setValue("BILL_NO", Long.valueOf(paramLong));
  }
  
  public long getBillNo()
  {
    return ((Long)getValue("BILL_NO")).longValue();
  }
  
  public void setPayIndex(long paramLong)
  {
    setValue("PAY_INDEX", Long.valueOf(paramLong));
  }
  
  public long getPayIndex()
  {
    return ((Long)getValue("PAY_INDEX")).longValue();
  }
  
  public void setTxnDate(Date paramDate)
  {
    setValue("TXN_DATE", paramDate);
  }
  
  public Date getTxnDate()
  {
    Date localDate = (Date)getValue("TXN_DATE");
    return localDate;
  }
  
  public void setTxnTime(Time paramTime)
  {
    setValue("TXN_TIME", paramTime);
  }
  
  public Time getTxnTime()
  {
    Time localTime = (Time)getValue("TXN_TIME");
    return localTime;
  }
  
  public void setAmount(double paramDouble)
  {
    setValue("AMOUNT", Double.valueOf(paramDouble));
  }
  
  public double getAmount()
  {
    return ((Double)getValue("AMOUNT")).doubleValue();
  }
  
  public void setTxnType(String paramString)
  {
    setValue("TXN_TYPE", paramString);
  }
  
  public String getTxnType()
  {
    return (String)getValue("TXN_TYPE");
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
 * Qualified Name:     dm.jb.db.gen.CashTxnBaseRow
 * JD-Core Version:    0.7.0.1
 */