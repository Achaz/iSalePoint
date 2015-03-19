package dm.tools.types;

import dm.tools.utils.MyFormatter;

public class InternalAmount
{
  private double _mAmount = 0.0D;
  private String _mPrefix = "";
  private String _mSufix = "";
  private boolean _mAppendCurrency = false;
  
  public InternalAmount(double paramDouble)
  {
    this._mAmount = paramDouble;
  }
  
  public InternalAmount(double paramDouble, String paramString1, String paramString2, boolean paramBoolean)
  {
    this._mAmount = paramDouble;
    this._mPrefix = paramString1;
    this._mSufix = paramString2;
    this._mAppendCurrency = paramBoolean;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer(this._mPrefix);
    if (this._mAmount == 0.0D) {
      this._mAmount = 0.0D;
    }
    localStringBuffer.append(MyFormatter.getFormattedAmount(this._mAmount, this._mAppendCurrency));
    localStringBuffer.append(this._mSufix);
    return localStringBuffer.toString();
  }
  
  public static InternalAmount valueOf(double paramDouble)
  {
    return new InternalAmount(paramDouble);
  }
  
  public static String toString(double paramDouble)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(MyFormatter.getFormattedAmount(paramDouble, false));
    return localStringBuffer.toString();
  }
  
  public double getAmount()
  {
    return this._mAmount;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.types.InternalAmount
 * JD-Core Version:    0.7.0.1
 */