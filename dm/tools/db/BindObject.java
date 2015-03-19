package dm.tools.db;

public class BindObject
{
  public Object value = null;
  public int index = -1;
  public short type = -1;
  
  public BindObject(int paramInt, short paramShort, Object paramObject)
  {
    this.value = paramObject;
    this.type = paramShort;
    this.index = paramInt;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.db.BindObject
 * JD-Core Version:    0.7.0.1
 */