package dm.jb.db.gen;

import dm.tools.db.DBRow;
import java.sql.Date;

public class StoreStockBaseRow
  extends DBRow
{
  protected StoreStockBaseRow(int paramInt, StoreStockBaseTableDef paramStoreStockBaseTableDef)
  {
    super(paramInt, paramStoreStockBaseTableDef);
  }
  
  public void setValues(int paramInt1, int paramInt2, double paramDouble1, double paramDouble2, Date paramDate, int paramInt3)
  {
    setStoreId(paramInt1);
    setProductId(paramInt2);
    setStock(paramDouble1);
    setPurchasePrice(paramDouble2);
    setExpiry(paramDate);
    setStockIndex(paramInt3);
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
 * Qualified Name:     dm.jb.db.gen.StoreStockBaseRow
 * JD-Core Version:    0.7.0.1
 */