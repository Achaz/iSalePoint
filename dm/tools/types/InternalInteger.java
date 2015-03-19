package dm.tools.types;

public class InternalInteger
{
  private Integer _mInt = Integer.valueOf(0);
  private String _mPrefix = "";
  private String _mSufix = "";
  
  public InternalInteger(int paramInt, String paramString1, String paramString2)
  {
    this._mInt = new Integer(paramInt);
    this._mPrefix = paramString1;
    this._mSufix = paramString2;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer(this._mPrefix);
    localStringBuffer.append(this._mInt.toString());
    localStringBuffer.append(this._mSufix);
    return localStringBuffer.toString();
  }
  
  public void setVal(Integer paramInteger)
  {
    this._mInt = paramInteger;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.types.InternalInteger
 * JD-Core Version:    0.7.0.1
 */