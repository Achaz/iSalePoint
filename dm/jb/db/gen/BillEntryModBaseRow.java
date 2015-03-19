package dm.jb.db.gen;

import dm.tools.db.DBRow;
import java.sql.Date;
import java.sql.Time;

public class BillEntryModBaseRow
  extends DBRow
{
  protected BillEntryModBaseRow(int paramInt, BillEntryModBaseTableDef paramBillEntryModBaseTableDef)
  {
    super(paramInt, paramBillEntryModBaseTableDef);
  }
  
  public void setValues(int paramInt1, int paramInt2, int paramInt3, double paramDouble1, double paramDouble2, double paramDouble3, int paramInt4, Date paramDate, Time paramTime, String paramString, int paramInt5, double paramDouble4)
  {
    setBillNo(paramInt1);
    setProductIndex(paramInt2);
    setEntryIndex(paramInt3);
    setQuantity(paramDouble1);
    setAmount(paramDouble2);
    setDiscount(paramDouble3);
    setModReason(paramInt4);
    setUpdateDate(paramDate);
    setUpdateTime(paramTime);
    setUpdateUid(paramString);
    setStoreId(paramInt5);
    setReturnedTax(paramDouble4);
  }
  
  public void setBillNo(int paramInt)
  {
    setValue("BILL_NO", Integer.valueOf(paramInt));
  }
  
  public int getBillNo()
  {
    return ((Integer)getValue("BILL_NO")).intValue();
  }
  
  public void setProductIndex(int paramInt)
  {
    setValue("PRODUCT_INDEX", Integer.valueOf(paramInt));
  }
  
  public int getProductIndex()
  {
    return ((Integer)getValue("PRODUCT_INDEX")).intValue();
  }
  
  public void setEntryIndex(int paramInt)
  {
    setValue("ENTRY_INDEX", Integer.valueOf(paramInt));
  }
  
  public int getEntryIndex()
  {
    return ((Integer)getValue("ENTRY_INDEX")).intValue();
  }
  
  public void setQuantity(double paramDouble)
  {
    setValue("QUANTITY", Double.valueOf(paramDouble));
  }
  
  public double getQuantity()
  {
    return ((Double)getValue("QUANTITY")).doubleValue();
  }
  
  public void setAmount(double paramDouble)
  {
    setValue("AMOUNT", Double.valueOf(paramDouble));
  }
  
  public double getAmount()
  {
    return ((Double)getValue("AMOUNT")).doubleValue();
  }
  
  public void setDiscount(double paramDouble)
  {
    setValue("DISCOUNT", Double.valueOf(paramDouble));
  }
  
  public double getDiscount()
  {
    return ((Double)getValue("DISCOUNT")).doubleValue();
  }
  
  public void setModReason(int paramInt)
  {
    setValue("MOD_REASON", Integer.valueOf(paramInt));
  }
  
  public int getModReason()
  {
    return ((Integer)getValue("MOD_REASON")).intValue();
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
  
  public void setStoreId(int paramInt)
  {
    setValue("STORE_ID", Integer.valueOf(paramInt));
  }
  
  public int getStoreId()
  {
    return ((Integer)getValue("STORE_ID")).intValue();
  }
  
  public void setReturnedTax(double paramDouble)
  {
    setValue("RETURNED_TAX", Double.valueOf(paramDouble));
  }
  
  public double getReturnedTax()
  {
    return ((Double)getValue("RETURNED_TAX")).doubleValue();
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.BillEntryModBaseRow
 * JD-Core Version:    0.7.0.1
 */