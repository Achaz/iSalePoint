package dm.jb.printing;

public class PrintColumn
{
  public static final byte ALIGNMENT_LEFT = 2;
  public static final byte ALIGNMENT_RIGHT = 4;
  public static final byte ALIGNMENT_CENTER = 0;
  public String name = null;
  public String code = null;
  public int width = 30;
  public int alignment = 2;
  
  public PrintColumn(String paramString1, String paramString2, int paramInt1, int paramInt2)
  {
    this.name = paramString1;
    this.width = paramInt1;
    this.code = paramString2;
    this.alignment = paramInt2;
  }
  
  public String toString()
  {
    return this.name;
  }
  
  public String getCode()
  {
    return this.code;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.printing.PrintColumn
 * JD-Core Version:    0.7.0.1
 */