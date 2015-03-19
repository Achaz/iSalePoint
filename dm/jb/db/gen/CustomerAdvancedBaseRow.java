package dm.jb.db.gen;

import dm.tools.db.DBRow;
import java.sql.Date;

public class CustomerAdvancedBaseRow
  extends DBRow
{
  protected CustomerAdvancedBaseRow(int paramInt, CustomerAdvancedBaseTableDef paramCustomerAdvancedBaseTableDef)
  {
    super(paramInt, paramCustomerAdvancedBaseTableDef);
  }
  
  public void setValues(Date paramDate1, Date paramDate2, String paramString1, int paramInt1, String paramString2, int paramInt2, int paramInt3, int paramInt4, String paramString3)
  {
    setDob(paramDate1);
    setAnniversary(paramDate2);
    setMobile(paramString1);
    setProfession(paramInt1);
    setHobby(paramString2);
    setDomesticTravel(paramInt2);
    setIntTravel(paramInt3);
    setCustomerId(paramInt4);
    setEmail(paramString3);
  }
  
  public void setDob(Date paramDate)
  {
    setValue("DOB", paramDate);
  }
  
  public Date getDob()
  {
    Date localDate = (Date)getValue("DOB");
    return localDate;
  }
  
  public void setAnniversary(Date paramDate)
  {
    setValue("ANNIVERSARY", paramDate);
  }
  
  public Date getAnniversary()
  {
    Date localDate = (Date)getValue("ANNIVERSARY");
    return localDate;
  }
  
  public void setMobile(String paramString)
  {
    setValue("MOBILE", paramString);
  }
  
  public String getMobile()
  {
    return (String)getValue("MOBILE");
  }
  
  public void setEmail(String paramString)
  {
    setValue("EMAIL", paramString);
  }
  
  public String getEmail()
  {
    return (String)getValue("EMAIL");
  }
  
  public void setProfession(int paramInt)
  {
    setValue("PROFESSION", Integer.valueOf(paramInt));
  }
  
  public int getProfession()
  {
    return ((Integer)getValue("PROFESSION")).intValue();
  }
  
  public void setHobby(String paramString)
  {
    setValue("HOBBY", paramString);
  }
  
  public String getHobby()
  {
    return (String)getValue("HOBBY");
  }
  
  public void setDomesticTravel(int paramInt)
  {
    setValue("DOMESTIC_TRAVEL", Integer.valueOf(paramInt));
  }
  
  public int getDomesticTravel()
  {
    return ((Integer)getValue("DOMESTIC_TRAVEL")).intValue();
  }
  
  public void setIntTravel(int paramInt)
  {
    setValue("INT_TRAVEL", Integer.valueOf(paramInt));
  }
  
  public int getIntTravel()
  {
    return ((Integer)getValue("INT_TRAVEL")).intValue();
  }
  
  public void setCustomerId(int paramInt)
  {
    setValue("CUSTOMER_ID", Integer.valueOf(paramInt));
  }
  
  public int getCustomerId()
  {
    return ((Integer)getValue("CUSTOMER_ID")).intValue();
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.CustomerAdvancedBaseRow
 * JD-Core Version:    0.7.0.1
 */