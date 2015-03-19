package dm.jb.ui.billing;

public class BillUpdateMode
{
  public static final int BILL_UPDATE_MODE_DEFECTIVE = 1;
  public static final int BILL_UPDATE_MODE_ERROR = 2;
  public static final int BILL_UPDATE_MODE_EXCHANGE = 3;
  public static final int BILL_UPDATE_MODE_ADD = 4;
  public static BillUpdateMode[] billUpdateModes = { new BillUpdateMode("Defective", 1), new BillUpdateMode("Billing Error", 2), new BillUpdateMode("Exchange", 3), new BillUpdateMode("Addition", 4) };
  private String _mName = null;
  int code = -1;
  
  public BillUpdateMode(String paramString, int paramInt)
  {
    this._mName = paramString;
    this.code = paramInt;
  }
  
  public String toString()
  {
    return this._mName;
  }
  
  public static BillUpdateMode getModeForCode(int paramInt)
  {
    for (int i = 0; i < billUpdateModes.length; i++) {
      if (billUpdateModes[i].code == paramInt) {
        return billUpdateModes[i];
      }
    }
    return null;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.billing.BillUpdateMode
 * JD-Core Version:    0.7.0.1
 */