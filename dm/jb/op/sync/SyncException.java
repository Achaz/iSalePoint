package dm.jb.op.sync;

public class SyncException
  extends Exception
{
  private String _mMessage = null;
  private Exception _mException = null;
  
  public SyncException(String paramString)
  {
    this(paramString, null);
  }
  
  public SyncException(String paramString, Exception paramException)
  {
    this._mMessage = paramString;
    this._mException = paramException;
  }
  
  public void printStackTrace()
  {
    super.printStackTrace();
    this._mException.printStackTrace();
  }
  
  public String toString()
  {
    return this._mMessage + "\n" + super.toString();
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.op.sync.SyncException
 * JD-Core Version:    0.7.0.1
 */