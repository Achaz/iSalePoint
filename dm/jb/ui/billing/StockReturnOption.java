package dm.jb.ui.billing;

public class StockReturnOption
{
  public static final byte STOCK_RETURN_TO_STORE = 1;
  public static final byte STOCK_RETURN_TO_VENDOR = 2;
  public static final byte STOCK_RETURN_TO_NONE = 3;
  private String _mName = null;
  byte code = -1;
  
  public StockReturnOption(String paramString, byte paramByte)
  {
    this._mName = paramString;
    this.code = paramByte;
  }
  
  public String toString()
  {
    return this._mName;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.billing.StockReturnOption
 * JD-Core Version:    0.7.0.1
 */