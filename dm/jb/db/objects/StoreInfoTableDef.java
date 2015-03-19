package dm.jb.db.objects;

import dm.jb.db.gen.StoreInfoBaseTableDef;
import dm.tools.db.DBException;
import dm.tools.db.DBRow;
import java.util.ArrayList;
import java.util.Iterator;

public class StoreInfoTableDef
  extends StoreInfoBaseTableDef
{
  private static StoreInfoTableDef _mInstance = null;
  private ArrayList<StoreInfoRow> _mStoreList = null;
  private static StoreInfoRow _mCurrentStore = null;
  
  public static StoreInfoTableDef getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new StoreInfoTableDef();
    }
    return _mInstance;
  }
  
  public StoreInfoRow getNewRow()
  {
    return new StoreInfoRow(getAttrList().size(), this);
  }
  
  public ArrayList<StoreInfoRow> getStoreList()
    throws DBException
  {
    if (this._mStoreList == null)
    {
      ArrayList localArrayList = super.getAllValues();
      if ((localArrayList == null) || (localArrayList.size() == 0)) {
        return null;
      }
      this._mStoreList = new ArrayList();
      Iterator localIterator = localArrayList.iterator();
      while (localIterator.hasNext())
      {
        DBRow localDBRow = (DBRow)localIterator.next();
        StoreInfoRow localStoreInfoRow = (StoreInfoRow)localDBRow;
        localStoreInfoRow.storeSites();
        this._mStoreList.add(localStoreInfoRow);
      }
    }
    return this._mStoreList;
  }
  
  public boolean isDuplicate(String paramString)
    throws DBException
  {
    getStoreList();
    if (this._mStoreList == null) {
      return false;
    }
    for (int i = 0; i < this._mStoreList.size(); i++)
    {
      StoreInfoRow localStoreInfoRow = (StoreInfoRow)this._mStoreList.get(i);
      if (localStoreInfoRow.getStoreCode().equalsIgnoreCase(paramString)) {
        return true;
      }
    }
    return false;
  }
  
  public static void setCurrentStore(StoreInfoRow paramStoreInfoRow)
  {
    _mCurrentStore = paramStoreInfoRow;
  }
  
  public static StoreInfoRow getCurrentStore()
  {
    return _mCurrentStore;
  }
  
  void addStore(StoreInfoRow paramStoreInfoRow)
    throws DBException
  {
    if (this._mStoreList == null)
    {
      getStoreList();
      if (this._mStoreList == null) {
        return;
      }
    }
    this._mStoreList.add(paramStoreInfoRow);
  }
  
  void deleteStore(StoreInfoRow paramStoreInfoRow)
    throws DBException
  {
    if (this._mStoreList == null)
    {
      getStoreList();
      if (this._mStoreList == null) {
        return;
      }
    }
    Object localObject = null;
    Iterator localIterator = this._mStoreList.iterator();
    while (localIterator.hasNext())
    {
      StoreInfoRow localStoreInfoRow = (StoreInfoRow)localIterator.next();
      if (localStoreInfoRow.getStoreId() == paramStoreInfoRow.getStoreId())
      {
        localObject = localStoreInfoRow;
        break;
      }
    }
    if (localObject != null) {
      this._mStoreList.remove(localObject);
    }
  }
  
  public StoreInfoRow getStoreForIndex(int paramInt)
    throws DBException
  {
    if (this._mStoreList == null)
    {
      getStoreList();
      if (this._mStoreList == null) {
        return null;
      }
    }
    Iterator localIterator = this._mStoreList.iterator();
    while (localIterator.hasNext())
    {
      StoreInfoRow localStoreInfoRow = (StoreInfoRow)localIterator.next();
      if (localStoreInfoRow.getStoreId() == paramInt) {
        return localStoreInfoRow;
      }
    }
    return null;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.StoreInfoTableDef
 * JD-Core Version:    0.7.0.1
 */