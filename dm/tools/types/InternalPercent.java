package dm.tools.types;

import java.text.DecimalFormat;

public class InternalPercent
{
  private double _mPerc = 0.0D;
  
  public InternalPercent(double paramDouble)
  {
    this._mPerc = paramDouble;
  }
  
  public String toString()
  {
    DecimalFormat localDecimalFormat = new DecimalFormat(".00 %");
    return localDecimalFormat.format(this._mPerc);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.types.InternalPercent
 * JD-Core Version:    0.7.0.1
 */