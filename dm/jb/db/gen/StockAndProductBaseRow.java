package dm.jb.db.gen;

import dm.tools.db.DBRow;
import java.sql.Date;

public class StockAndProductBaseRow
  extends DBRow
{
  protected StockAndProductBaseRow(int paramInt, StockAndProductBaseTableDef paramStockAndProductBaseTableDef)
  {
    super(paramInt, paramStockAndProductBaseTableDef);
  }
  
  public void setValues(int paramInt1, int paramInt2, int paramInt3, String paramString1, int paramInt4, double paramDouble1, double paramDouble2, double paramDouble3, int paramInt5, double paramDouble4, int paramInt6, int paramInt7, String paramString2, int paramInt8, int paramInt9, int paramInt10, double paramDouble5, double paramDouble6, Date paramDate, int paramInt11, boolean paramBoolean)
  {
    setProdIndex(paramInt1);
    setCatIndex(paramInt2);
    setDeptIndex(paramInt3);
    setProdName(paramString1);
    setProdUnit(paramInt4);
    setUnitPrice(paramDouble1);
    setDiscount(paramDouble2);
    setTax(paramDouble3);
    setTaxUnit(paramInt5);
    setLowStock(paramDouble4);
    setLoyaltyPoints(paramInt6);
    setRedeemablePoints(paramInt7);
    setProductCode(paramString2);
    setVendorId(paramInt8);
    setStoreId(paramInt9);
    setProductId(paramInt10);
    setStock(paramDouble5);
    setPurchasePrice(paramDouble6);
    setExpiry(paramDate);
    setStockIndex(paramInt11);
    setTaxIncluded(paramBoolean);
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
  
  public void setTaxIncluded(boolean paramBoolean)
  {
    setValue("TAX_INCLUDED", paramBoolean ? "Y" : "N");
  }
  
  public boolean getTaxIncluded()
  {
    return (String)getValue("TAX_INCLUDED") == "Y";
  }
  
  public void setVendorId(int paramInt)
  {
    setValue("VENDOR_ID", Integer.valueOf(paramInt));
  }
  
  public int getVendorId()
  {
    return ((Integer)getValue("VENDOR_ID")).intValue();
  }
  
  public void setStoreId(int paramInt)
  {
    setValue("STORE_ID", Integer.valueOf(paramInt));
  }
  
  public int getStoreId()
  {
    return ((Integer)getValue("STORE_ID")).intValue();
  }
  
  public void setProductId(int paramInt)
  {
    setValue("PRODUCT_ID", Integer.valueOf(paramInt));
  }
  
  public int getProductId()
  {
    return ((Integer)getValue("PRODUCT_ID")).intValue();
  }
  
  public void setStock(double paramDouble)
  {
    setValue("STOCK", Double.valueOf(paramDouble));
  }
  
  public double getStock()
  {
    return ((Double)getValue("STOCK")).doubleValue();
  }
  
  public void setPurchasePrice(double paramDouble)
  {
    setValue("PURCHASE_PRICE", Double.valueOf(paramDouble));
  }
  
  public double getPurchasePrice()
  {
    return ((Double)getValue("PURCHASE_PRICE")).doubleValue();
  }
  
  public void setExpiry(Date paramDate)
  {
    setValue("EXPIRY", paramDate);
  }
  
  public Date getExpiry()
  {
    Date localDate = (Date)getValue("EXPIRY");
    return localDate;
  }
  
  public void setStockIndex(int paramInt)
  {
    setValue("STOCK_INDEX", Integer.valueOf(paramInt));
  }
  
  public int getStockIndex()
  {
    return ((Integer)getValue("STOCK_INDEX")).intValue();
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.StockAndProductBaseRow
 * JD-Core Version:    0.7.0.1
 */