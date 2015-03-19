package dm.jb.db.objects;

import dm.jb.db.gen.CurrentStockBaseRow;
import dm.jb.op.sync.FileOpUtils;
import dm.jb.op.sync.SyncWriter;
import dm.tools.db.DBException;
import java.io.FileInputStream;
import java.io.IOException;

public class CurrentStockRow
  extends CurrentStockBaseRow
{
  private WearehouseInfoRow _mWh = null;
  
  CurrentStockRow(int paramInt, CurrentStockTableDef paramCurrentStockTableDef)
  {
    super(paramInt, paramCurrentStockTableDef);
  }
  
  public void setWeareHouseInfo(WearehouseInfoRow paramWearehouseInfoRow)
  {
    this._mWh = paramWearehouseInfoRow;
  }
  
  public WearehouseInfoRow getWeareHouseInfo()
    throws DBException
  {
    if (this._mWh == null) {
      this._mWh = WearehouseInfoTableDef.getInstance().getWarehouseForIndex(getWearHouseIndex());
    }
    return this._mWh;
  }
  
  public void sync(SyncWriter paramSyncWriter)
    throws IOException
  {
    paramSyncWriter.writeInt(getCurStockIndex());
    paramSyncWriter.writeInt(getStockIndex());
    paramSyncWriter.writeInt(getProdId());
    paramSyncWriter.writeDouble(getQuantity());
    paramSyncWriter.writeInt(getQuantityUnit());
    paramSyncWriter.writeInt(getWearHouseIndex());
  }
  
  public int syncFromStream(FileInputStream paramFileInputStream)
    throws IOException
  {
    int i = FileOpUtils.readInt(paramFileInputStream);
    int j = FileOpUtils.readInt(paramFileInputStream);
    int k = FileOpUtils.readInt(paramFileInputStream);
    double d = FileOpUtils.readDouble(paramFileInputStream);
    int m = FileOpUtils.readInt(paramFileInputStream);
    int n = FileOpUtils.readInt(paramFileInputStream);
    setValues(j, k, null, "Y", d, m, n);
    setCurStockIndex(i);
    return 28;
  }
  
  public String toString()
  {
    return getStockIndex() + "";
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.CurrentStockRow
 * JD-Core Version:    0.7.0.1
 */