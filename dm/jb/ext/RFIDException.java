package dm.jb.ext;

import java.io.PrintStream;

public class RFIDException
  extends Exception
{
  private String _mMessage;
  private String _mCause;
  private Exception _mExp;
  
  public RFIDException(String paramString1, String paramString2, Exception paramException)
  {
    this._mMessage = paramString1;
    this._mCause = paramString2;
    this._mExp = paramException;
  }
  
  public String toString()
  {
    return this._mMessage;
  }
  
  public void printStackTrace()
  {
    super.printStackTrace();
    if (this._mExp != null)
    {
      System.err.println("Caused by ");
      this._mExp.printStackTrace();
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ext.RFIDException
 * JD-Core Version:    0.7.0.1
 */