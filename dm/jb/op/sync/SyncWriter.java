package dm.jb.op.sync;

import java.io.IOException;
import java.util.Date;

public abstract interface SyncWriter
{
  public abstract void open(short paramShort)
    throws SyncException;
  
  public abstract void init()
    throws SyncException;
  
  public abstract void close()
    throws SyncException;
  
  public abstract void destroy()
    throws SyncException;
  
  public abstract void writeShort(short paramShort)
    throws IOException;
  
  public abstract void writeInt(int paramInt)
    throws IOException;
  
  public abstract void writeLong(long paramLong)
    throws IOException;
  
  public abstract void writeString(String paramString)
    throws IOException;
  
  public abstract void writeDouble(double paramDouble)
    throws IOException;
  
  public abstract void writeDate(Date paramDate)
    throws IOException;
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.op.sync.SyncWriter
 * JD-Core Version:    0.7.0.1
 */