package dm.jb.op.sync;

import java.io.IOException;
import javax.swing.JPanel;

public abstract interface SyncMode
{
  public static final short SYNC_MODE_PRODUCT_DETAILS = 1;
  public static final short SYNC_OBJECT_DEPT_ROW = 1;
  public static final short SYNC_OBJECT_CAT_ROW = 2;
  public static final short SYNC_OBJECT_PROD_ROW = 3;
  public static final short SYNC_OBJECT_STOCK_ROW = 4;
  public static final short SYNC_OBJECT_WH_ROW = 5;
  public static final byte DB_SYNC_TYPE_CLEAN_AND_CREATE = 1;
  public static final byte DB_SYNC_TYPE_UPDATE_AND_CREATE = 2;
  public static final byte DB_SYNC_TYPE_UPDATE_ONLY = 3;
  public static final byte DB_SYNC_TYPE_CREATE_ONLY = 4;
  
  public abstract JPanel getOptionPanel();
  
  public abstract boolean isPanelValid();
  
  public abstract SyncWriter getWriterInstance();
  
  public abstract void writeModeSpecificDataToWrite(SyncWriter paramSyncWriter)
    throws IOException;
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.op.sync.SyncMode
 * JD-Core Version:    0.7.0.1
 */