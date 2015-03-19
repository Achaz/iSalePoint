package dm.jb.db.gen;

import dm.tools.db.DBRow;

public class ConfigInfoBaseRow
  extends DBRow
{
  protected ConfigInfoBaseRow(int paramInt, ConfigInfoBaseTableDef paramConfigInfoBaseTableDef)
  {
    super(paramInt, paramConfigInfoBaseTableDef);
  }
  
  public void setValues(String paramString1, String paramString2)
  {
    setName(paramString1);
    setValue(paramString2);
  }
  
  public void setName(String paramString)
  {
    setValue("NAME", paramString);
  }
  
  public String getName()
  {
    return (String)getValue("NAME");
  }
  
  public void setValue(String paramString)
  {
    setValue("VALUE", paramString);
  }
  
  public String getValue()
  {
    return (String)getValue("VALUE");
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.ConfigInfoBaseRow
 * JD-Core Version:    0.7.0.1
 */