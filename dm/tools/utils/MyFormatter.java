package dm.tools.utils;

import dm.jb.ui.settings.CountryInfo;
import java.util.Formatter;

public class MyFormatter
{
  public static CountryInfo country = new CountryInfo();
  public static String[] roundingOptions = { "Near to 0.05", "Near to 1.00", "Near to 10.00", "Near to 100.00" };
  
  public static String getFormattedAmount(double paramDouble, boolean paramBoolean)
  {
    Formatter localFormatter = new Formatter(country.locale);
    if (paramBoolean) {
      return localFormatter.format("%.2f " + CommonConfig.getInstance().country.currency, new Object[] { Double.valueOf(paramDouble) }).toString();
    }
    return localFormatter.format("%.2f", new Object[] { Double.valueOf(paramDouble) }).toString();
  }
  
  public static void init(CountryInfo paramCountryInfo)
  {
    country = paramCountryInfo;
  }
  
  public static double roundAmount(double paramDouble, int paramInt)
  {
    double d1 = 0.0D;
    double d2;
    if (paramInt == 0)
    {
      d2 = paramDouble % 1.0D;
      d1 = paramDouble - d2;
      d2 = d2 * 100.0D / 1.0D;
      double d3 = d2 % 5.0D / 1.0D;
      if (d3 >= 2.5D) {
        d2 += 5.0D - d3;
      } else {
        d2 -= d3;
      }
      d2 /= 100.0D;
      d1 += d2;
    }
    else if (paramInt == 1)
    {
      d2 = paramDouble % 1.0D;
      if (d2 > 0.49D)
      {
        paramDouble -= d2;
        paramDouble += 1.0D;
      }
      else
      {
        paramDouble -= d2;
      }
      d1 = paramDouble;
    }
    else if (paramInt == 2)
    {
      d2 = paramDouble % 10.0D;
      if (d2 > 4.989999771118164D)
      {
        paramDouble -= d2;
        paramDouble += 10.0D;
      }
      else
      {
        paramDouble -= d2;
      }
      d1 = paramDouble;
    }
    else
    {
      d2 = paramDouble % 100.0D;
      if (d2 > 49.990001678466797D)
      {
        paramDouble -= d2;
        paramDouble += 100.0D;
      }
      else
      {
        paramDouble -= d2;
      }
      d1 = paramDouble;
    }
    return d1;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.utils.MyFormatter
 * JD-Core Version:    0.7.0.1
 */