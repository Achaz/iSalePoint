package dm.jb.db.gen;

import dm.tools.db.DBRow;
import java.sql.Date;
import java.sql.Time;

public class DdTxnBaseRow
  extends DBRow
{
  protected DdTxnBaseRow(int paramInt, DdTxnBaseTableDef paramDdTxnBaseTableDef)
  {
    super(paramInt, paramDdTxnBaseTableDef);
  }
  
  public void setValues(long paramLong, String paramString1, Date paramDate1, Date paramDate2, Time paramTime, String paramString2, String paramString3, double paramDouble, int paramInt)
  {
    setBillNo(paramLong);
    setDdNo(paramString1);
    setPayDate(paramDate1);
    setTxnDate(paramDate2);
    setTxnTime(paramTime);
    setBank(paramString2);
    setTxnType(paramString3);
    setAmount(paramDouble);
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
  
  public void setDdPayIndex(long paramLong)
  {
    setValue("DD_PAY_INDEX", Long.valueOf(paramLong));
  }
  
  public long getDdPayIndex()
  {
    return ((Long)getValue("DD_PAY_INDEX")).longValue();
  }
  
  public void setDdNo(String paramString)
  {
    setValue("DD_NO", paramString);
  }
  
  public String getDdNo()
  {
    return (String)getValue("DD_NO");
  }
  
  public void setPayDate(Date paramDate)
  {
    setValue("PAY_DATE", paramDate);
  }
  
  public Date getPayDate()
  {
    Date localDate = (Date)getValue("PAY_DATE");
    return localDate;
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
  
  public void setBank(String paramString)
  {
    setValue("BANK", paramString);
  }
  
  public String getBank()
  {
    return (String)getValue("BANK");
  }
  
  public void setTxnType(String paramString)
  {
    setValue("TXN_TYPE", paramString);
  }
  
  public String getTxnType()
  {
    return (String)getValue("TXN_TYPE");
  }
  
  public void setAmount(double paramDouble)
  {
    setValue("AMOUNT", Double.valueOf(paramDouble));
  }
  
  public double getAmount()
  {
    return ((Double)getValue("AMOUNT")).doubleValue();
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
 * Qualified Name:     dm.jb.db.gen.DdTxnBaseRow
 * JD-Core Version:    0.7.0.1
 */