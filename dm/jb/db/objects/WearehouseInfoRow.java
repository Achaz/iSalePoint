package dm.jb.db.objects;

import dm.jb.db.gen.WearehouseInfoBaseRow;
import dm.tools.db.DBException;

public class WearehouseInfoRow
  extends WearehouseInfoBaseRow
{
  public WearehouseInfoRow(int paramInt, WearehouseInfoTableDef paramWearehouseInfoTableDef)
  {
    super(paramInt, paramWearehouseInfoTableDef);
  }
  
  public String toString()
  {
    return getWearehouseName();
  }
  
  public void create()
    throws DBException
  {
    super.create();
    WearehouseInfoTableDef.getInstance().addWareHouse(this);
  }
  
  public void delete()
    throws DBException
  {
    super.delete();
    WearehouseInfoTableDef.getInstance().deleteWareHouse(this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.WearehouseInfoRow
 * JD-Core Version:    0.7.0.1
 */