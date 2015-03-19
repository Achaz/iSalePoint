package dm.jb;

public class JeException
  extends Exception
{
  private String _mMessage = null;
  private String _mCause = null;
  private String _mAction = null;
  private Exception _mBaseExp = null;
  
  public JeException(String paramString1, String paramString2, String paramString3, Exception paramException)
  {
    this._mMessage = paramString1;
    this._mCause = paramString2;
    this._mAction = paramString3;
    this._mBaseExp = paramException;
  }
  
  public String toString()
  {
    return this._mMessage;
  }
  
  public String getComplete()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("Message :" + this._mMessage);
    localStringBuffer.append("Cauase :" + this._mCause);
    localStringBuffer.append("Action :" + this._mAction);
    localStringBuffer.append("Base :" + this._mBaseExp.toString());
    return localStringBuffer.toString();
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.JeException
 * JD-Core Version:    0.7.0.1
 */