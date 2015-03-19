package dm.jb.db.gen;

import dm.tools.db.DBRow;

public class BillEntryBaseRow
  extends DBRow
{
  protected BillEntryBaseRow(int paramInt, BillEntryBaseTableDef paramBillEntryBaseTableDef)
  {
    super(paramInt, paramBillEntryBaseTableDef);
  }
  
  public void setValues(int paramInt1, int paramInt2, int paramInt3, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, int paramInt4, double paramDouble5)
  {
    setBillNo(paramInt1);
    setProductIndex(paramInt2);
    setEntryIndex(paramInt3);
    setQuantity(paramDouble1);
    setAmount(paramDouble2);
    setDiscount(paramDouble3);
    setPurchasePrice(paramDouble4);
    setStoreId(paramInt4);
    setTax(paramDouble5);
  }
  
  public void setBillNo(int paramInt)
  {
    setValue("BILL_NO", Integer.valueOf(paramInt));
  }
  
  public int getBillNo()
  {
    return ((Integer)getValue("BILL_NO")).intValue();
  }
  
  public void setProductIndex(int paramInt)
  {
    setValue("PRODUCT_INDEX", Integer.valueOf(paramInt));
  }
  
  public int getProductIndex()
  {
    return ((Integer)getValue("PRODUCT_INDEX")).intValue();
  }
  
  public void setEntryIndex(int paramInt)
  {
    setValue("ENTRY_INDEX", Integer.valueOf(paramInt));
  }
  
  public int getEntryIndex()
  {
    return ((Integer)getValue("ENTRY_INDEX")).intValue();
  }
  
  public void setQuantity(double paramDouble)
  {
    setValue("QUANTITY", Double.valueOf(paramDouble));
  }
  
  public double getQuantity()
  {
    return ((Double)getValue("QUANTITY")).doubleValue();
  }
  
  public void setAmount(double paramDouble)
  {
    setValue("AMOUNT", Double.valueOf(paramDouble));
  }
  
  public double getAmount()
  {
    return ((Double)getValue("AMOUNT")).doubleValue();
  }
  
  public void setDiscount(double paramDouble)
  {
    setValue("DISCOUNT", Double.valueOf(paramDouble));
  }
  
  public double getDiscount()
  {
    return ((Double)getValue("DISCOUNT")).doubleValue();
  }
  
  public void setPurchasePrice(double paramDouble)
  {
    setValue("PURCHASE_PRICE", Double.valueOf(paramDouble));
  }
  
  public double getPurchasePrice()
  {
    return ((Double)getValue("PURCHASE_PRICE")).doubleValue();
  }
  
  public void setStoreId(int paramInt)
  {
    setValue("STORE_ID", Integer.valueOf(paramInt));
  }
  
  public int getStoreId()
  {
    return ((Integer)getValue("STORE_ID")).intValue();
  }
  
  public void setTax(double paramDouble)
  {
    setValue("TAX", Double.valueOf(paramDouble));
  }
  
  public double getTax()
  {
    return ((Double)getValue("TAX")).doubleValue();
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.BillEntryBaseRow
 * JD-Core Version:    0.7.0.1
 */