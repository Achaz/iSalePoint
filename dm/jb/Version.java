package dm.jb;

public class Version
{
  private static final int VERSION = 1000001;
  private static final String RELEASE_TYPE = "Beta";
  
  public static int getVerisonAsInt()
  {
    return 1000001;
  }
  
  public static String getReleaseType()
  {
    return "Beta";
  }
  
  public static boolean isDataVersionCompatible(int paramInt)
  {
    return true;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.Version
 * JD-Core Version:    0.7.0.1
 */