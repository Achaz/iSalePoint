package dm.jb.db.gen;

import dm.tools.db.DBRow;
import java.sql.Date;
import java.sql.Time;

public class CcTxnBaseRow
  extends DBRow
{
  protected CcTxnBaseRow(int paramInt, CcTxnBaseTableDef paramCcTxnBaseTableDef)
  {
    super(paramInt, paramCcTxnBaseTableDef);
  }
  
  public void setValues(long paramLong, String paramString1, String paramString2, String paramString3, double paramDouble, String paramString4, Date paramDate, Time paramTime, int paramInt)
  {
    setBillNo(paramLong);
    setCcNo(paramString1);
    setCardType(paramString2);
    setRefNo(paramString3);
    setAmount(paramDouble);
    setTxnType(paramString4);
    setTxnDate(paramDate);
    setTxnTime(paramTime);
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
  
  public void setCcIndex(long paramLong)
  {
    setValue("CC_INDEX", Long.valueOf(paramLong));
  }
  
  public long getCcIndex()
  {
    return ((Long)getValue("CC_INDEX")).longValue();
  }
  
  public void setCcNo(String paramString)
  {
    setValue("CC_NO", paramString);
  }
  
  public String getCcNo()
  {
    return (String)getValue("CC_NO");
  }
  
  public void setCardType(String paramString)
  {
    setValue("CARD_TYPE", paramString);
  }
  
  public String getCardType()
  {
    return (String)getValue("CARD_TYPE");
  }
  
  public void setRefNo(String paramString)
  {
    setValue("REF_NO", paramString);
  }
  
  public String getRefNo()
  {
    return (String)getValue("REF_NO");
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
 * Qualified Name:     dm.jb.db.gen.CcTxnBaseRow
 * JD-Core Version:    0.7.0.1
 */