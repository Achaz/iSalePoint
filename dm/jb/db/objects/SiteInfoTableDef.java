package dm.jb.db.objects;

import dm.jb.db.gen.SiteInfoBaseTableDef;
import java.util.ArrayList;

public class SiteInfoTableDef
  extends SiteInfoBaseTableDef
{
  private static SiteInfoTableDef _mInstance = null;
  private static SiteInfoRow _mCurrentSite = null;
  
  public static SiteInfoTableDef getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new SiteInfoTableDef();
    }
    return _mInstance;
  }
  
  public SiteInfoRow getNewRow()
  {
    return new SiteInfoRow(getAttrList().size(), this);
  }
  
  public static void setCurrentSite(SiteInfoRow paramSiteInfoRow)
  {
    _mCurrentSite = paramSiteInfoRow;
  }
  
  public static SiteInfoRow getCurrentSite()
  {
    return _mCurrentSite;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.SiteInfoTableDef
 * JD-Core Version:    0.7.0.1
 */