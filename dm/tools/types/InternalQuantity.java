package dm.tools.types;

public class InternalQuantity
{
  public static String[] quantityUnits = { "Kg.", "gms", "No(s)", "Ltr", "ml" };
  private double _mQuantity = 0.0D;
  private int _mQuantityUnit = 0;
  private String _mPrefix = "";
  private String _mSufix = "";
  private boolean _mPrintQty = false;
  
  public InternalQuantity(double paramDouble, int paramInt, boolean paramBoolean)
  {
    this._mQuantity = paramDouble;
    this._mQuantityUnit = paramInt;
    this._mPrintQty = paramBoolean;
  }
  
  public InternalQuantity(double paramDouble, String paramString1, String paramString2, int paramInt, boolean paramBoolean)
  {
    this(paramDouble, paramInt, paramBoolean);
    this._mPrefix = paramString1;
    this._mSufix = paramString2;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer(this._mPrefix);
    if (this._mQuantityUnit == 2) {
      localStringBuffer.append((int)this._mQuantity);
    } else {
      localStringBuffer.append(this._mQuantity);
    }
    if (this._mPrintQty) {
      if (this._mQuantityUnit == -1) {
        localStringBuffer.append(" unit");
      } else {
        localStringBuffer.append(" " + quantityUnits[this._mQuantityUnit]);
      }
    }
    localStringBuffer.append(this._mSufix);
    return localStringBuffer.toString();
  }
  
  public int getCode()
  {
    return this._mQuantityUnit;
  }
  
  public static boolean isUnitFractionAllowed(int paramInt)
  {
    return paramInt != 2;
  }
  
  public static String toString(double paramDouble, short paramShort)
  {
    InternalQuantity localInternalQuantity = new InternalQuantity(paramDouble, paramShort, false);
    return localInternalQuantity.toString();
  }
  
  public static String toString(double paramDouble, short paramShort, boolean paramBoolean)
  {
    InternalQuantity localInternalQuantity = new InternalQuantity(paramDouble, paramShort, paramBoolean);
    return localInternalQuantity.toString();
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.types.InternalQuantity
 * JD-Core Version:    0.7.0.1
 */