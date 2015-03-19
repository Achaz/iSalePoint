package dm.jb.db.gen;

import dm.tools.db.DBRow;

public class PoEntryBaseRow
  extends DBRow
{
  protected PoEntryBaseRow(int paramInt, PoEntryBaseTableDef paramPoEntryBaseTableDef)
  {
    super(paramInt, paramPoEntryBaseTableDef);
  }
  
  public void setValues(int paramInt1, int paramInt2, int paramInt3, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
  {
    setPoId(paramInt1);
    setPoEntryIndex(paramInt2);
    setProductId(paramInt3);
    setQuantity(paramDouble1);
    setPriceExpected(paramDouble2);
    setPricePaid(paramDouble3);
    setQuantityRecieved(paramDouble4);
  }
  
  public void setPoId(int paramInt)
  {
    setValue("PO_ID", Integer.valueOf(paramInt));
  }
  
  public int getPoId()
  {
    return ((Integer)getValue("PO_ID")).intValue();
  }
  
  public void setPoEntryIndex(int paramInt)
  {
    setValue("PO_ENTRY_INDEX", Integer.valueOf(paramInt));
  }
  
  public int getPoEntryIndex()
  {
    return ((Integer)getValue("PO_ENTRY_INDEX")).intValue();
  }
  
  public void setProductId(int paramInt)
  {
    setValue("PRODUCT_ID", Integer.valueOf(paramInt));
  }
  
  public int getProductId()
  {
    return ((Integer)getValue("PRODUCT_ID")).intValue();
  }
  
  public void setQuantity(double paramDouble)
  {
    setValue("QUANTITY", Double.valueOf(paramDouble));
  }
  
  public double getQuantity()
  {
    return ((Double)getValue("QUANTITY")).doubleValue();
  }
  
  public void setPriceExpected(double paramDouble)
  {
    setValue("PRICE_EXPECTED", Double.valueOf(paramDouble));
  }
  
  public double getPriceExpected()
  {
    return ((Double)getValue("PRICE_EXPECTED")).doubleValue();
  }
  
  public void setPricePaid(double paramDouble)
  {
    setValue("PRICE_PAID", Double.valueOf(paramDouble));
  }
  
  public double getPricePaid()
  {
    return ((Double)getValue("PRICE_PAID")).doubleValue();
  }
  
  public void setQuantityRecieved(double paramDouble)
  {
    setValue("QUANTITY_RECIEVED", Double.valueOf(paramDouble));
  }
  
  public double getQuantityRecieved()
  {
    return ((Double)getValue("QUANTITY_RECIEVED")).doubleValue();
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.PoEntryBaseRow
 * JD-Core Version:    0.7.0.1
 */