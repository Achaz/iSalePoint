package dm.tools.db;

public class AccessRight
{
  public int accessCode = 0;
  private String _mName = null;
  
  public AccessRight(String paramString, int paramInt)
  {
    this._mName = paramString;
    this.accessCode = paramInt;
  }
  
  public String toString()
  {
    return this._mName;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.db.AccessRight
 * JD-Core Version:    0.7.0.1
 */