package dm.jb.db.gen;

import dm.tools.db.DBRow;

public class WearehouseInfoBaseRow
  extends DBRow
{
  protected WearehouseInfoBaseRow(int paramInt, WearehouseInfoBaseTableDef paramWearehouseInfoBaseTableDef)
  {
    super(paramInt, paramWearehouseInfoBaseTableDef);
  }
  
  public void setValues(String paramString1, String paramString2)
  {
    setWearehouseName(paramString1);
    setWearehouseAddress(paramString2);
  }
  
  public void setWearehouseId(int paramInt)
  {
    setValue("WEAREHOUSE_ID", Integer.valueOf(paramInt));
  }
  
  public int getWearehouseId()
  {
    return ((Integer)getValue("WEAREHOUSE_ID")).intValue();
  }
  
  public void setWearehouseName(String paramString)
  {
    setValue("WEAREHOUSE_NAME", paramString);
  }
  
  public String getWearehouseName()
  {
    return (String)getValue("WEAREHOUSE_NAME");
  }
  
  public void setWearehouseAddress(String paramString)
  {
    setValue("WEAREHOUSE_ADDRESS", paramString);
  }
  
  public String getWearehouseAddress()
  {
    return (String)getValue("WEAREHOUSE_ADDRESS");
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.WearehouseInfoBaseRow
 * JD-Core Version:    0.7.0.1
 */