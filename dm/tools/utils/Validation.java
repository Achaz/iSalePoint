package dm.tools.utils;

public class Validation
{
  public static boolean isValidFloat(String paramString, int paramInt, boolean paramBoolean)
  {
    int i = paramString.indexOf('.');
    if (i == -1)
    {
      try
      {
        int j = Integer.valueOf(paramString).intValue();
        if ((j < 0) && (!paramBoolean)) {
          return false;
        }
      }
      catch (NumberFormatException localNumberFormatException1)
      {
        return false;
      }
    }
    else
    {
      String str1 = paramString.substring(0, i);
      String str2 = paramString.substring(i + 1, paramString.length());
      if (paramInt == 0) {
        return false;
      }
      if (str2.length() > paramInt) {
        return false;
      }
      if (str2.indexOf('-') != -1) {
        return false;
      }
      if (str1.length() > 0) {
        try
        {
          int k = Integer.valueOf(str1).intValue();
          if ((k < 0) && (!paramBoolean)) {
            return false;
          }
        }
        catch (NumberFormatException localNumberFormatException2)
        {
          return false;
        }
      }
      if (str2.indexOf('.') != -1) {
        return false;
      }
      if (str2.length() != 0) {
        try
        {
          int m = Integer.valueOf(str2).intValue();
          if (m < 0) {
            return false;
          }
        }
        catch (NumberFormatException localNumberFormatException3)
        {
          return false;
        }
      }
    }
    return true;
  }
  
  public static boolean isValidInt(String paramString, boolean paramBoolean)
  {
    return isValidFloat(paramString, 0, paramBoolean);
  }
  
  public static boolean isValidPIN(String paramString)
  {
    if (paramString.length() != 6) {
      return false;
    }
    return isValidInt(paramString, false);
  }
  
  public static boolean isValidPhone(String paramString)
  {
    paramString.trim();
    if (paramString.length() > 15) {
      return false;
    }
    if (paramString.length() < 5) {
      return false;
    }
    if (paramString.charAt(0) == '+')
    {
      String str = paramString.substring(1, paramString.length());
      return isValidNumber(str);
    }
    return isValidNumber(paramString);
  }
  
  private static boolean isValidNumber(String paramString)
  {
    int i = paramString.length();
    for (int j = 0; j < i; j++)
    {
      int k = paramString.charAt(j);
      if ((k > 57) || (k < 48)) {
        return false;
      }
    }
    return true;
  }
  
  public static boolean isValidAmount(String paramString)
  {
    if (paramString.indexOf('.') == paramString.length() - 1) {
      return false;
    }
    return isValidFloat(paramString, 2, false);
  }
  
  public static boolean isValidEMail(String paramString)
  {
    if (paramString.length() > 128) {
      return false;
    }
    int i = paramString.indexOf('@');
    if (i <= 0) {
      return false;
    }
    String str = paramString.substring(i + 1, paramString.length());
    i = str.indexOf('@');
    if (i != -1) {
      return false;
    }
    int j = str.indexOf('.');
    return j > 0;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.utils.Validation
 * JD-Core Version:    0.7.0.1
 */