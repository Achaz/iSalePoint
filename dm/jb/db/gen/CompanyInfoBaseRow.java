package dm.jb.db.gen;

import dm.tools.db.DBRow;

public class CompanyInfoBaseRow
  extends DBRow
{
  protected CompanyInfoBaseRow(int paramInt, CompanyInfoBaseTableDef paramCompanyInfoBaseTableDef)
  {
    super(paramInt, paramCompanyInfoBaseTableDef);
  }
  
  public void setValues(String paramString1, String paramString2, String paramString3)
  {
    setName(paramString1);
    setAddress(paramString2);
    setPhone1(paramString3);
  }
  
  public void setName(String paramString)
  {
    setValue("NAME", paramString);
  }
  
  public String getName()
  {
    return (String)getValue("NAME");
  }
  
  public void setAddress(String paramString)
  {
    setValue("ADDRESS", paramString);
  }
  
  public String getAddress()
  {
    return (String)getValue("ADDRESS");
  }
  
  public void setPhone1(String paramString)
  {
    setValue("PHONE1", paramString);
  }
  
  public String getPhone1()
  {
    return (String)getValue("PHONE1");
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.CompanyInfoBaseRow
 * JD-Core Version:    0.7.0.1
 */