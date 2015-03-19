package dm.tools.dbui;

public class ValidationException
  extends Exception
{
  private String _mMessage = null;
  private String _mCause = null;
  private String _mSolution = null;
  
  public ValidationException(String paramString1, String paramString2, String paramString3)
  {
    this._mMessage = paramString1;
    this._mCause = paramString2;
    this._mSolution = paramString3;
  }
  
  public String toString()
  {
    return this._mMessage;
  }
  
  public String getMessage()
  {
    return this._mMessage;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.dbui.ValidationException
 * JD-Core Version:    0.7.0.1
 */