package dm.jb.db.gen;

import dm.tools.db.DBRow;

public class VendorBaseRow
  extends DBRow
{
  protected VendorBaseRow(int paramInt, VendorBaseTableDef paramVendorBaseTableDef)
  {
    super(paramInt, paramVendorBaseTableDef);
  }
  
  public void setValues(String paramString1, String paramString2, String paramString3)
  {
    setVendorName(paramString1);
    setVendorAddress(paramString2);
    setVendorPhone(paramString3);
  }
  
  public void setVendorId(int paramInt)
  {
    setValue("VENDOR_ID", Integer.valueOf(paramInt));
  }
  
  public int getVendorId()
  {
    return ((Integer)getValue("VENDOR_ID")).intValue();
  }
  
  public void setVendorName(String paramString)
  {
    setValue("VENDOR_NAME", paramString);
  }
  
  public String getVendorName()
  {
    return (String)getValue("VENDOR_NAME");
  }
  
  public void setVendorAddress(String paramString)
  {
    setValue("VENDOR_ADDRESS", paramString);
  }
  
  public String getVendorAddress()
  {
    return (String)getValue("VENDOR_ADDRESS");
  }
  
  public void setVendorPhone(String paramString)
  {
    setValue("VENDOR_PHONE", paramString);
  }
  
  public String getVendorPhone()
  {
    return (String)getValue("VENDOR_PHONE");
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.VendorBaseRow
 * JD-Core Version:    0.7.0.1
 */