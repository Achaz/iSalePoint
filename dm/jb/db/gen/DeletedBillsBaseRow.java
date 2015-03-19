package dm.jb.db.gen;

import dm.tools.db.DBRow;
import java.sql.Date;
import java.sql.Time;

public class DeletedBillsBaseRow
  extends DBRow
{
  protected DeletedBillsBaseRow(int paramInt, DeletedBillsBaseTableDef paramDeletedBillsBaseTableDef)
  {
    super(paramInt, paramDeletedBillsBaseTableDef);
  }
  
  public void setValues(int paramInt1, int paramInt2, Date paramDate, Time paramTime, String paramString, double paramDouble, int paramInt3, int paramInt4)
  {
    setBillNo(paramInt1);
    setDeleteMode(paramInt2);
    setUpdateDate(paramDate);
    setUpdateTime(paramTime);
    setUpdateUid(paramString);
    setAmount(paramDouble);
    setTotalEntries(paramInt3);
    setStoreId(paramInt4);
  }
  
  public void setBillNo(int paramInt)
  {
    setValue("BILL_NO", Integer.valueOf(paramInt));
  }
  
  public int getBillNo()
  {
    return ((Integer)getValue("BILL_NO")).intValue();
  }
  
  public void setDeleteMode(int paramInt)
  {
    setValue("DELETE_MODE", Integer.valueOf(paramInt));
  }
  
  public int getDeleteMode()
  {
    return ((Integer)getValue("DELETE_MODE")).intValue();
  }
  
  public void setUpdateDate(Date paramDate)
  {
    setValue("UPDATE_DATE", paramDate);
  }
  
  public Date getUpdateDate()
  {
    Date localDate = (Date)getValue("UPDATE_DATE");
    return localDate;
  }
  
  public void setUpdateTime(Time paramTime)
  {
    setValue("UPDATE_TIME", paramTime);
  }
  
  public Time getUpdateTime()
  {
    Time localTime = (Time)getValue("UPDATE_TIME");
    return localTime;
  }
  
  public void setUpdateUid(String paramString)
  {
    setValue("UPDATE_UID", paramString);
  }
  
  public String getUpdateUid()
  {
    return (String)getValue("UPDATE_UID");
  }
  
  public void setAmount(double paramDouble)
  {
    setValue("AMOUNT", Double.valueOf(paramDouble));
  }
  
  public double getAmount()
  {
    return ((Double)getValue("AMOUNT")).doubleValue();
  }
  
  public void setTotalEntries(int paramInt)
  {
    setValue("TOTAL_ENTRIES", Integer.valueOf(paramInt));
  }
  
  public int getTotalEntries()
  {
    return ((Integer)getValue("TOTAL_ENTRIES")).intValue();
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
 * Qualified Name:     dm.jb.db.gen.DeletedBillsBaseRow
 * JD-Core Version:    0.7.0.1
 */