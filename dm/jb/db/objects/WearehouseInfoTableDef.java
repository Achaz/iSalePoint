package dm.jb.db.objects;

import dm.jb.db.gen.WearehouseInfoBaseTableDef;
import dm.tools.db.DBException;
import dm.tools.db.DBRow;
import java.util.ArrayList;
import java.util.Iterator;

public class WearehouseInfoTableDef
  extends WearehouseInfoBaseTableDef
{
  private static WearehouseInfoTableDef _mInstance = null;
  private ArrayList<WearehouseInfoRow> _mWareHouse = null;
  
  public static WearehouseInfoTableDef getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new WearehouseInfoTableDef();
    }
    return _mInstance;
  }
  
  public WearehouseInfoRow getNewRow()
  {
    return new WearehouseInfoRow(getAttrList().size(), this);
  }
  
  public ArrayList<WearehouseInfoRow> getWarehouseList()
    throws DBException
  {
    if (this._mWareHouse == null)
    {
      ArrayList localArrayList = getAllValues();
      this._mWareHouse = new ArrayList();
      Iterator localIterator = localArrayList.iterator();
      while (localIterator.hasNext())
      {
        DBRow localDBRow = (DBRow)localIterator.next();
        this._mWareHouse.add((WearehouseInfoRow)localDBRow);
      }
    }
    return this._mWareHouse;
  }
  
  public WearehouseInfoRow getWarehouseForIndex(int paramInt)
    throws DBException
  {
    ArrayList localArrayList = getWarehouseList();
    if ((localArrayList == null) || (localArrayList.size() == 0)) {
      return null;
    }
    Iterator localIterator = localArrayList.iterator();
    while (localIterator.hasNext())
    {
      WearehouseInfoRow localWearehouseInfoRow = (WearehouseInfoRow)localIterator.next();
      if (localWearehouseInfoRow.getWearehouseId() == paramInt) {
        return localWearehouseInfoRow;
      }
    }
    return null;
  }
  
  public boolean isDuplicateWearehouse(String paramString)
    throws DBException
  {
    ArrayList localArrayList = getWarehouseList();
    if ((localArrayList == null) || (localArrayList.size() == 0)) {
      return false;
    }
    Iterator localIterator = localArrayList.iterator();
    while (localIterator.hasNext())
    {
      WearehouseInfoRow localWearehouseInfoRow = (WearehouseInfoRow)localIterator.next();
      if (paramString.equals(localWearehouseInfoRow.getWearehouseName())) {
        return true;
      }
    }
    return false;
  }
  
  void addWareHouse(WearehouseInfoRow paramWearehouseInfoRow)
    throws DBException
  {
    if (this._mWareHouse == null)
    {
      getWarehouseList();
      if (this._mWareHouse == null) {
        return;
      }
    }
    this._mWareHouse.add(paramWearehouseInfoRow);
  }
  
  void deleteWareHouse(WearehouseInfoRow paramWearehouseInfoRow)
    throws DBException
  {
    if (this._mWareHouse == null)
    {
      getWarehouseList();
      if (this._mWareHouse == null) {
        return;
      }
    }
    Object localObject = null;
    Iterator localIterator = this._mWareHouse.iterator();
    while (localIterator.hasNext())
    {
      WearehouseInfoRow localWearehouseInfoRow = (WearehouseInfoRow)localIterator.next();
      if (localWearehouseInfoRow.getWearehouseId() == paramWearehouseInfoRow.getWearehouseId()) {
        localObject = localWearehouseInfoRow;
      }
    }
    if (localObject != null) {
      this._mWareHouse.remove(localObject);
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.WearehouseInfoTableDef
 * JD-Core Version:    0.7.0.1
 */