package dm.tools.utils;

import java.math.BigDecimal;

public class SafeMath
{
  public static double safeSubtract(double paramDouble1, double paramDouble2)
  {
    return BigDecimal.valueOf(paramDouble1).subtract(BigDecimal.valueOf(paramDouble2)).doubleValue();
  }
  
  public static double safeAdd(double paramDouble1, double paramDouble2)
  {
    return BigDecimal.valueOf(paramDouble1).add(BigDecimal.valueOf(paramDouble2)).doubleValue();
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.utils.SafeMath
 * JD-Core Version:    0.7.0.1
 */