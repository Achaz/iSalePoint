package dm.jb.ui.report;

public class JBReportException
  extends Exception
{
  private String _mMessage = null;
  private String _mCause = null;
  private Exception _mBase = null;
  
  public JBReportException(String paramString1, String paramString2, Exception paramException)
  {
    super(paramException);
    this._mMessage = paramString1;
    this._mCause = paramString2;
    this._mBase = paramException;
  }
  
  public String toString()
  {
    return this._mMessage;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.report.JBReportException
 * JD-Core Version:    0.7.0.1
 */