package dm.jb.db.gen;

import dm.tools.db.DBRow;
import java.sql.Date;
import java.sql.Time;

public class BillTableBaseRow
  extends DBRow
{
  protected BillTableBaseRow(int paramInt, BillTableBaseTableDef paramBillTableBaseTableDef)
  {
    super(paramInt, paramBillTableBaseTableDef);
  }
  
  public void setValues(String paramString, Date paramDate, Time paramTime, double paramDouble1, double paramDouble2, double paramDouble3, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7)
  {
    setBillCleanedUp(paramString);
    setBillDate(paramDate);
    setBillTime(paramTime);
    setAmount(paramDouble1);
    setAddlDiscount(paramDouble2);
    setTax(paramDouble3);
    setTotalEntries(paramInt1);
    setCustIndex(paramInt2);
    setStoreId(paramInt3);
    setSiteId(paramInt4);
    setUid(paramInt5);
    setPointsRedeemed(paramInt6);
    setPointsAwarded(paramInt7);
  }
  
  public void setBillNo(int paramInt)
  {
    setValue("BILL_NO", Integer.valueOf(paramInt));
  }
  
  public int getBillNo()
  {
    return ((Integer)getValue("BILL_NO")).intValue();
  }
  
  public void setBillCleanedUp(String paramString)
  {
    setValue("BILL_CLEANED_UP", paramString);
  }
  
  public String getBillCleanedUp()
  {
    return (String)getValue("BILL_CLEANED_UP");
  }
  
  public void setBillDate(Date paramDate)
  {
    setValue("BILL_DATE", paramDate);
  }
  
  public Date getBillDate()
  {
    Date localDate = (Date)getValue("BILL_DATE");
    return localDate;
  }
  
  public void setBillTime(Time paramTime)
  {
    setValue("BILL_TIME", paramTime);
  }
  
  public Time getBillTime()
  {
    Time localTime = (Time)getValue("BILL_TIME");
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
  
  public void setAddlDiscount(double paramDouble)
  {
    setValue("ADDL_DISCOUNT", Double.valueOf(paramDouble));
  }
  
  public double getAddlDiscount()
  {
    return ((Double)getValue("ADDL_DISCOUNT")).doubleValue();
  }
  
  public void setTax(double paramDouble)
  {
    setValue("TAX", Double.valueOf(paramDouble));
  }
  
  public double getTax()
  {
    return ((Double)getValue("TAX")).doubleValue();
  }
  
  public void setTotalEntries(int paramInt)
  {
    setValue("TOTAL_ENTRIES", Integer.valueOf(paramInt));
  }
  
  public int getTotalEntries()
  {
    return ((Integer)getValue("TOTAL_ENTRIES")).intValue();
  }
  
  public void setCustIndex(int paramInt)
  {
    setValue("CUST_INDEX", Integer.valueOf(paramInt));
  }
  
  public int getCustIndex()
  {
    return ((Integer)getValue("CUST_INDEX")).intValue();
  }
  
  public void setStoreId(int paramInt)
  {
    setValue("STORE_ID", Integer.valueOf(paramInt));
  }
  
  public int getStoreId()
  {
    return ((Integer)getValue("STORE_ID")).intValue();
  }
  
  public void setSiteId(int paramInt)
  {
    setValue("SITE_ID", Integer.valueOf(paramInt));
  }
  
  public int getSiteId()
  {
    return ((Integer)getValue("SITE_ID")).intValue();
  }
  
  public void setUid(int paramInt)
  {
    setValue("UID", Integer.valueOf(paramInt));
  }
  
  public int getUid()
  {
    return ((Integer)getValue("UID")).intValue();
  }
  
  public void setPointsRedeemed(int paramInt)
  {
    setValue("POINTS_REDEEMED", Integer.valueOf(paramInt));
  }
  
  public int getPointsRedeemed()
  {
    return ((Integer)getValue("POINTS_REDEEMED")).intValue();
  }
  
  public void setPointsAwarded(int paramInt)
  {
    setValue("POINTS_AWARDED", Integer.valueOf(paramInt));
  }
  
  public int getPointsAwarded()
  {
    return ((Integer)getValue("POINTS_AWARDED")).intValue();
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.BillTableBaseRow
 * JD-Core Version:    0.7.0.1
 */