package dm.jb.db.objects;

import dm.jb.db.gen.StoreInfoBaseRow;
import dm.tools.db.DBException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;

public class StoreInfoRow
  extends StoreInfoBaseRow
{
  ArrayList<SiteInfoRow> _mSites = null;
  
  public StoreInfoRow(int paramInt, StoreInfoTableDef paramStoreInfoTableDef)
  {
    super(paramInt, paramStoreInfoTableDef);
  }
  
  public String toString()
  {
    return getName();
  }
  
  public ArrayList<SiteInfoRow> getSites()
  {
    if (this._mSites == null) {
      try
      {
        storeSites();
      }
      catch (DBException localDBException)
      {
        System.err.println(localDBException);
      }
    }
    return this._mSites;
  }
  
  public SiteInfoRow getSiteForId(int paramInt)
    throws DBException
  {
    if (this._mSites == null) {
      storeSites();
    }
    Iterator localIterator = this._mSites.iterator();
    while (localIterator.hasNext())
    {
      SiteInfoRow localSiteInfoRow = (SiteInfoRow)localIterator.next();
      if (localSiteInfoRow.getSiteIndex() == paramInt) {
        return localSiteInfoRow;
      }
    }
    return null;
  }
  
  void storeSites()
    throws DBException
  {
    ArrayList localArrayList1 = SiteInfoTableDef.getInstance().getAllValuesWithWhereClause("STORE_ID=" + getStoreId());
    if ((localArrayList1 == null) || (localArrayList1.size() == 0))
    {
      this._mSites = null;
      return;
    }
    ArrayList localArrayList2 = new ArrayList();
    for (int i = 0; i < localArrayList1.size(); i++) {
      localArrayList2.add((SiteInfoRow)localArrayList1.get(i));
    }
    localArrayList1.clear();
    localArrayList1 = null;
    this._mSites = localArrayList2;
  }
  
  public boolean checkForDuplicatesite(String paramString)
    throws DBException
  {
    if ((this._mSites == null) || (this._mSites.size() == 0))
    {
      storeSites();
      return false;
    }
    for (int i = 0; i < this._mSites.size(); i++)
    {
      SiteInfoRow localSiteInfoRow = (SiteInfoRow)this._mSites.get(i);
      if (localSiteInfoRow.getSiteId().equals(paramString)) {
        return true;
      }
    }
    return false;
  }
  
  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof StoreInfoRow))
    {
      StoreInfoRow localStoreInfoRow = (StoreInfoRow)paramObject;
      return localStoreInfoRow.getStoreId() == getStoreId();
    }
    return super.equals(paramObject);
  }
  
  public void create()
    throws DBException
  {
    super.create();
    StoreInfoTableDef.getInstance().addStore(this);
  }
  
  public void delete()
    throws DBException
  {
    super.delete();
    StoreInfoTableDef.getInstance().deleteStore(this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.StoreInfoRow
 * JD-Core Version:    0.7.0.1
 */