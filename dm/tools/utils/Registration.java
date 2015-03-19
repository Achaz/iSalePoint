package dm.tools.utils;

public class Registration
{
  public static final int REG_LOYALTY = 1;
  public static final int REG_BARCODE_PRINT = 2;
  public static final int REG_SALES_RETURN = 3;
  public static final int REG_SALES_REPORT = 4;
  public static final int REG_MULTI_STORE = 5;
  public static final int REG_MULTI_SITE = 6;
  public static final int REG_WAREHOUSE = 7;
  public static final int REG_PURCHASE_ORDER = 8;
  private static Registration _mInstance = new Registration();
  
  public static Registration getInstance()
  {
    return _mInstance;
  }
  
  public String getComputerSerialCode()
  {
    return getGenCode();
  }
  
  public boolean isAppRegistered(String paramString)
  {
    return isRegistered(paramString);
  }
  
  public boolean isFeatureAllowed(int paramInt)
  {
    return false;
  }
  
  public native String getGenCode();
  
  public native boolean isRegistered(String paramString);
  
  public native boolean isFeatureAllowedJNI(int paramInt);
  
  static
  {
    System.loadLibrary("billinglib");
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.utils.Registration
 * JD-Core Version:    0.7.0.1
 */