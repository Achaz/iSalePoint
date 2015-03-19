package dm.jb.db.objects;

import dm.jb.db.gen.SiteInfoBaseRow;
import dm.tools.db.DBException;

public class SiteInfoRow
  extends SiteInfoBaseRow
{
  SiteInfoRow(int paramInt, SiteInfoTableDef paramSiteInfoTableDef)
  {
    super(paramInt, paramSiteInfoTableDef);
  }
  
  public String toString()
  {
    return "" + getSiteId();
  }
  
  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof SiteInfoRow))
    {
      SiteInfoRow localSiteInfoRow = (SiteInfoRow)paramObject;
      return (localSiteInfoRow.getSiteId().equals(getSiteId())) && (localSiteInfoRow.getStoreId() == getStoreId());
    }
    return super.equals(paramObject);
  }
  
  public Object getValue(String paramString)
  {
    if (paramString.equals("STORE_NAME"))
    {
      int i = getStoreId();
      StoreInfoRow localStoreInfoRow = null;
      try
      {
        localStoreInfoRow = StoreInfoTableDef.getInstance().getStoreForIndex(i);
      }
      catch (DBException localDBException)
      {
        localDBException.printStackTrace();
      }
      if (localStoreInfoRow == null) {
        return "NA";
      }
      return localStoreInfoRow.getName();
    }
    return super.getValue(paramString);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.SiteInfoRow
 * JD-Core Version:    0.7.0.1
 */