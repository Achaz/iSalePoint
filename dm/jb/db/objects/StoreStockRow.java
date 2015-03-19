package dm.jb.db.objects;

import dm.jb.db.gen.StoreStockBaseRow;
import dm.jb.op.sync.FileOpUtils;
import dm.jb.op.sync.SyncWriter;
import java.io.FileInputStream;
import java.io.IOException;

public class StoreStockRow
  extends StoreStockBaseRow
{
  public StoreStockRow(int paramInt, StoreStockTableDef paramStoreStockTableDef)
  {
    super(paramInt, paramStoreStockTableDef);
  }
  
  public void sync(SyncWriter paramSyncWriter)
    throws IOException
  {
    paramSyncWriter.writeInt(getStoreId());
    paramSyncWriter.writeInt(getProductId());
    paramSyncWriter.writeDouble(getStock());
    paramSyncWriter.writeDouble(getPurchasePrice());
    paramSyncWriter.writeInt(getStockIndex());
  }
  
  public int syncFromStream(FileInputStream paramFileInputStream)
    throws IOException
  {
    int i = FileOpUtils.readInt(paramFileInputStream);
    int j = FileOpUtils.readInt(paramFileInputStream);
    double d1 = FileOpUtils.readDouble(paramFileInputStream);
    double d2 = FileOpUtils.readDouble(paramFileInputStream);
    int k = FileOpUtils.readInt(paramFileInputStream);
    setValues(i, j, d1, d2, null, k);
    return 28;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.StoreStockRow
 * JD-Core Version:    0.7.0.1
 */