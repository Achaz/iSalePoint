package dm.jb.ui.billing;

public class BillingColumn
{
  public static final int ALIGNMENT_LEFT = 2;
  public static final int ALIGNMENT_RIGHT = 4;
  public static final int ALIGNMENT_CENTER = 0;
  public String _mName = null;
  public String _mCode = null;
  public int _mSize = 32;
  public int alignment = 2;
  
  public BillingColumn(String paramString1, String paramString2, int paramInt1, int paramInt2)
  {
    this._mName = paramString1;
    this._mCode = paramString2;
    this._mSize = 32;
    this.alignment = paramInt2;
  }
  
  public String toString()
  {
    return this._mName;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.billing.BillingColumn
 * JD-Core Version:    0.7.0.1
 */