package dm.jb.printing;

public class JePrinterException
  extends Exception
{
  private String _mCause = null;
  private String _mMessage = null;
  private Object _mPrinter = null;
  
  public JePrinterException(Object paramObject, String paramString1, String paramString2)
  {
    this._mPrinter = paramObject;
    this._mMessage = paramString1;
    this._mCause = paramString2;
  }
  
  public String toString()
  {
    return this._mMessage;
  }
  
  public String getCauseString()
  {
    return this._mCause;
  }
  
  public Object getPrinter()
  {
    return this._mPrinter;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.printing.JePrinterException
 * JD-Core Version:    0.7.0.1
 */