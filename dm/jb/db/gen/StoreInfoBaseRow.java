package dm.jb.db.gen;

import dm.tools.db.DBRow;

public class StoreInfoBaseRow
  extends DBRow
{
  protected StoreInfoBaseRow(int paramInt, StoreInfoBaseTableDef paramStoreInfoBaseTableDef)
  {
    super(paramInt, paramStoreInfoBaseTableDef);
  }
  
  public void setValues(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    setStoreCode(paramString1);
    setAddress(paramString2);
    setPhone(paramString3);
    setName(paramString4);
  }
  
  public void setStoreId(int paramInt)
  {
    setValue("STORE_ID", Integer.valueOf(paramInt));
  }
  
  public int getStoreId()
  {
    return ((Integer)getValue("STORE_ID")).intValue();
  }
  
  public void setStoreCode(String paramString)
  {
    setValue("STORE_CODE", paramString);
  }
  
  public String getStoreCode()
  {
    return (String)getValue("STORE_CODE");
  }
  
  public void setAddress(String paramString)
  {
    setValue("ADDRESS", paramString);
  }
  
  public String getAddress()
  {
    return (String)getValue("ADDRESS");
  }
  
  public void setPhone(String paramString)
  {
    setValue("PHONE", paramString);
  }
  
  public String getPhone()
  {
    return (String)getValue("PHONE");
  }
  
  public void setName(String paramString)
  {
    setValue("NAME", paramString);
  }
  
  public String getName()
  {
    return (String)getValue("NAME");
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.StoreInfoBaseRow
 * JD-Core Version:    0.7.0.1
 */