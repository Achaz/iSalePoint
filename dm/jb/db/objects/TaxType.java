package dm.jb.db.objects;

public class TaxType
{
  public static final short TAX_TYPE_VAT = 1;
  public static final short TAX_TYPE_SERVICE_TAX = 2;
  public static TaxType[] taxTypes = { new TaxType(1, "VAT"), new TaxType(2, "Service Tax") };
  private short _mCode = 0;
  private String _mName = null;
  
  public TaxType(short paramShort, String paramString)
  {
    this._mCode = paramShort;
    this._mName = paramString;
  }
  
  public short getCode()
  {
    return this._mCode;
  }
  
  public String toString()
  {
    return this._mName;
  }
  
  public String getName()
  {
    return this._mName;
  }
  
  public static TaxType getTaxTypeForCode(int paramInt)
  {
    for (int i = 0; i < taxTypes.length; i++) {
      if (taxTypes[i]._mCode == paramInt) {
        return taxTypes[i];
      }
    }
    return null;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.TaxType
 * JD-Core Version:    0.7.0.1
 */