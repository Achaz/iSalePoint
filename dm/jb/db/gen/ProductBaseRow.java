package dm.jb.db.gen;

import dm.tools.db.DBRow;

public class ProductBaseRow
  extends DBRow
{
  protected ProductBaseRow(int paramInt, ProductBaseTableDef paramProductBaseTableDef)
  {
    super(paramInt, paramProductBaseTableDef);
  }
  
  public void setValues(int paramInt1, int paramInt2, String paramString1, int paramInt3, double paramDouble1, double paramDouble2, double paramDouble3, int paramInt4, double paramDouble4, int paramInt5, int paramInt6, String paramString2, int paramInt7, String paramString3)
  {
    setCatIndex(paramInt1);
    setDeptIndex(paramInt2);
    setProdName(paramString1);
    setProdUnit(paramInt3);
    setUnitPrice(paramDouble1);
    setDiscount(paramDouble2);
    setTax(paramDouble3);
    setTaxUnit(paramInt4);
    setLowStock(paramDouble4);
    setLoyaltyPoints(paramInt5);
    setRedeemablePoints(paramInt6);
    setProductCode(paramString2);
    setVendorId(paramInt7);
    setRfid(paramString3);
  }
  
  public void setProdIndex(int paramInt)
  {
    setValue("PROD_INDEX", Integer.valueOf(paramInt));
  }
  
  public int getProdIndex()
  {
    return ((Integer)getValue("PROD_INDEX")).intValue();
  }
  
  public void setCatIndex(int paramInt)
  {
    setValue("CAT_INDEX", Integer.valueOf(paramInt));
  }
  
  public int getCatIndex()
  {
    return ((Integer)getValue("CAT_INDEX")).intValue();
  }
  
  public void setDeptIndex(int paramInt)
  {
    setValue("DEPT_INDEX", Integer.valueOf(paramInt));
  }
  
  public int getDeptIndex()
  {
    return ((Integer)getValue("DEPT_INDEX")).intValue();
  }
  
  public void setProdName(String paramString)
  {
    setValue("PROD_NAME", paramString);
  }
  
  public String getProdName()
  {
    return (String)getValue("PROD_NAME");
  }
  
  public void setProdUnit(int paramInt)
  {
    setValue("PROD_UNIT", Integer.valueOf(paramInt));
  }
  
  public int getProdUnit()
  {
    return ((Integer)getValue("PROD_UNIT")).intValue();
  }
  
  public void setUnitPrice(double paramDouble)
  {
    setValue("UNIT_PRICE", Double.valueOf(paramDouble));
  }
  
  public double getUnitPrice()
  {
    return ((Double)getValue("UNIT_PRICE")).doubleValue();
  }
  
  public void setDiscount(double paramDouble)
  {
    setValue("DISCOUNT", Double.valueOf(paramDouble));
  }
  
  public double getDiscount()
  {
    return ((Double)getValue("DISCOUNT")).doubleValue();
  }
  
  public void setTax(double paramDouble)
  {
    setValue("TAX", Double.valueOf(paramDouble));
  }
  
  public double getTax()
  {
    return ((Double)getValue("TAX")).doubleValue();
  }
  
  public void setTaxUnit(int paramInt)
  {
    setValue("TAX_UNIT", Integer.valueOf(paramInt));
  }
  
  public int getTaxUnit()
  {
    return ((Integer)getValue("TAX_UNIT")).intValue();
  }
  
  public void setLowStock(double paramDouble)
  {
    setValue("LOW_STOCK", Double.valueOf(paramDouble));
  }
  
  public double getLowStock()
  {
    return ((Double)getValue("LOW_STOCK")).doubleValue();
  }
  
  public void setLoyaltyPoints(int paramInt)
  {
    setValue("LOYALTY_POINTS", Integer.valueOf(paramInt));
  }
  
  public int getLoyaltyPoints()
  {
    return ((Integer)getValue("LOYALTY_POINTS")).intValue();
  }
  
  public void setRedeemablePoints(int paramInt)
  {
    setValue("REDEEMABLE_POINTS", Integer.valueOf(paramInt));
  }
  
  public int getRedeemablePoints()
  {
    return ((Integer)getValue("REDEEMABLE_POINTS")).intValue();
  }
  
  public void setProductCode(String paramString)
  {
    setValue("PRODUCT_CODE", paramString);
  }
  
  public String getProductCode()
  {
    return (String)getValue("PRODUCT_CODE");
  }
  
  public void setRfid(String paramString)
  {
    setValue("RFID", paramString);
  }
  
  public String getRfid()
  {
    return (String)getValue("RFID");
  }
  
  public void setVendorId(int paramInt)
  {
    setValue("VENDOR_ID", Integer.valueOf(paramInt));
  }
  
  public int getVendorId()
  {
    return ((Integer)getValue("VENDOR_ID")).intValue();
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.ProductBaseRow
 * JD-Core Version:    0.7.0.1
 */