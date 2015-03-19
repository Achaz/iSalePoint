package dm.jb.db.gen;

import dm.tools.db.DBRow;
import java.sql.Date;

public class CustomerBaseRow
  extends DBRow
{
  protected CustomerBaseRow(int paramInt, CustomerBaseTableDef paramCustomerBaseTableDef)
  {
    super(paramInt, paramCustomerBaseTableDef);
  }
  
  public void setValues(String paramString1, String paramString2, String paramString3, int paramInt, String paramString4, String paramString5, Date paramDate)
  {
    setCustName(paramString1);
    setCustAddress(paramString2);
    setCustPhone(paramString3);
    setLoyalty(paramInt);
    setBarcode(paramString4);
    setRfid(paramString5);
    setJoinDate(paramDate);
  }
  
  public void setCustIndex(int paramInt)
  {
    setValue("CUST_INDEX", Integer.valueOf(paramInt));
  }
  
  public int getCustIndex()
  {
    return ((Integer)getValue("CUST_INDEX")).intValue();
  }
  
  public void setCustName(String paramString)
  {
    setValue("CUST_NAME", paramString);
  }
  
  public String getCustName()
  {
    return (String)getValue("CUST_NAME");
  }
  
  public void setCustAddress(String paramString)
  {
    setValue("CUST_ADDRESS", paramString);
  }
  
  public String getCustAddress()
  {
    return (String)getValue("CUST_ADDRESS");
  }
  
  public void setCustPhone(String paramString)
  {
    setValue("CUST_PHONE", paramString);
  }
  
  public String getCustPhone()
  {
    return (String)getValue("CUST_PHONE");
  }
  
  public void setLoyalty(int paramInt)
  {
    setValue("LOYALTY", Integer.valueOf(paramInt));
  }
  
  public int getLoyalty()
  {
    return ((Integer)getValue("LOYALTY")).intValue();
  }
  
  public void setBarcode(String paramString)
  {
    setValue("BARCODE", paramString);
  }
  
  public String getBarcode()
  {
    return (String)getValue("BARCODE");
  }
  
  public void setRfid(String paramString)
  {
    setValue("RFID", paramString);
  }
  
  public String getRfid()
  {
    return (String)getValue("RFID");
  }
  
  public void setJoinDate(Date paramDate)
  {
    setValue("JOIN_DATE", paramDate);
  }
  
  public Date getJoinDate()
  {
    Date localDate = (Date)getValue("JOIN_DATE");
    return localDate;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.CustomerBaseRow
 * JD-Core Version:    0.7.0.1
 */