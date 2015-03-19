package dm.jb.db.gen;

import dm.tools.db.DBRow;
import java.sql.Date;
import java.sql.Time;

public class PoInfoBaseRow
  extends DBRow
{
  protected PoInfoBaseRow(int paramInt, PoInfoBaseTableDef paramPoInfoBaseTableDef)
  {
    super(paramInt, paramPoInfoBaseTableDef);
  }
  
  public void setValues(Date paramDate1, Time paramTime, int paramInt1, String paramString1, int paramInt2, int paramInt3, Date paramDate2, String paramString2, String paramString3)
  {
    setPoDate(paramDate1);
    setPoTime(paramTime);
    setPoEntryCount(paramInt1);
    setPoDelievred(paramString1);
    setVendorId(paramInt2);
    setCreatedBy(paramInt3);
    setExpectedDate(paramDate2);
    setBillTo(paramString2);
    setShipTo(paramString3);
  }
  
  public void setPoIndex(int paramInt)
  {
    setValue("PO_INDEX", Integer.valueOf(paramInt));
  }
  
  public int getPoIndex()
  {
    return ((Integer)getValue("PO_INDEX")).intValue();
  }
  
  public void setPoDate(Date paramDate)
  {
    setValue("PO_DATE", paramDate);
  }
  
  public Date getPoDate()
  {
    Date localDate = (Date)getValue("PO_DATE");
    return localDate;
  }
  
  public void setPoTime(Time paramTime)
  {
    setValue("PO_TIME", paramTime);
  }
  
  public Time getPoTime()
  {
    Time localTime = (Time)getValue("PO_TIME");
    return localTime;
  }
  
  public void setPoEntryCount(int paramInt)
  {
    setValue("PO_ENTRY_COUNT", Integer.valueOf(paramInt));
  }
  
  public int getPoEntryCount()
  {
    return ((Integer)getValue("PO_ENTRY_COUNT")).intValue();
  }
  
  public void setPoDelievred(String paramString)
  {
    setValue("PO_DELIEVRED", paramString);
  }
  
  public String getPoDelievred()
  {
    return (String)getValue("PO_DELIEVRED");
  }
  
  public void setVendorId(int paramInt)
  {
    setValue("VENDOR_ID", Integer.valueOf(paramInt));
  }
  
  public int getVendorId()
  {
    return ((Integer)getValue("VENDOR_ID")).intValue();
  }
  
  public void setCreatedBy(int paramInt)
  {
    setValue("CREATED_BY", Integer.valueOf(paramInt));
  }
  
  public int getCreatedBy()
  {
    return ((Integer)getValue("CREATED_BY")).intValue();
  }
  
  public void setExpectedDate(Date paramDate)
  {
    setValue("EXPECTED_DATE", paramDate);
  }
  
  public Date getExpectedDate()
  {
    Date localDate = (Date)getValue("EXPECTED_DATE");
    return localDate;
  }
  
  public void setBillTo(String paramString)
  {
    setValue("BILL_TO", paramString);
  }
  
  public String getBillTo()
  {
    return (String)getValue("BILL_TO");
  }
  
  public void setShipTo(String paramString)
  {
    setValue("SHIP_TO", paramString);
  }
  
  public String getShipTo()
  {
    return (String)getValue("SHIP_TO");
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.PoInfoBaseRow
 * JD-Core Version:    0.7.0.1
 */