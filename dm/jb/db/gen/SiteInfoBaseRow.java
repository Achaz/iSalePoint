package dm.jb.db.gen;

import dm.tools.db.DBRow;

public class SiteInfoBaseRow
  extends DBRow
{
  protected SiteInfoBaseRow(int paramInt, SiteInfoBaseTableDef paramSiteInfoBaseTableDef)
  {
    super(paramInt, paramSiteInfoBaseTableDef);
  }
  
  public void setValues(String paramString, int paramInt)
  {
    setSiteId(paramString);
    setStoreId(paramInt);
  }
  
  public void setSiteIndex(int paramInt)
  {
    setValue("SITE_INDEX", Integer.valueOf(paramInt));
  }
  
  public int getSiteIndex()
  {
    return ((Integer)getValue("SITE_INDEX")).intValue();
  }
  
  public void setSiteId(String paramString)
  {
    setValue("SITE_ID", paramString);
  }
  
  public String getSiteId()
  {
    return (String)getValue("SITE_ID");
  }
  
  public void setStoreId(int paramInt)
  {
    setValue("STORE_ID", Integer.valueOf(paramInt));
  }
  
  public int getStoreId()
  {
    return ((Integer)getValue("STORE_ID")).intValue();
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.SiteInfoBaseRow
 * JD-Core Version:    0.7.0.1
 */