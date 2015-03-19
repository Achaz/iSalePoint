package dm.jb.db.gen;

import dm.tools.db.DBRow;
import java.sql.Date;

public class CurrentStockBaseRow
  extends DBRow
{
  protected CurrentStockBaseRow(int paramInt, CurrentStockBaseTableDef paramCurrentStockBaseTableDef)
  {
    super(paramInt, paramCurrentStockBaseTableDef);
  }
  
  public void setValues(int paramInt1, int paramInt2, Date paramDate, String paramString, double paramDouble, int paramInt3, int paramInt4)
  {
    setStockIndex(paramInt1);
    setProdId(paramInt2);
    setExpiry(paramDate);
    setExpiryNa(paramString);
    setQuantity(paramDouble);
    setQuantityUnit(paramInt3);
    setWearHouseIndex(paramInt4);
  }
  
  public void setCurStockIndex(int paramInt)
  {
    setValue("CUR_STOCK_INDEX", Integer.valueOf(paramInt));
  }
  
  public int getCurStockIndex()
  {
    return ((Integer)getValue("CUR_STOCK_INDEX")).intValue();
  }
  
  public void setStockIndex(int paramInt)
  {
    setValue("STOCK_INDEX", Integer.valueOf(paramInt));
  }
  
  public int getStockIndex()
  {
    return ((Integer)getValue("STOCK_INDEX")).intValue();
  }
  
  public void setProdId(int paramInt)
  {
    setValue("PROD_ID", Integer.valueOf(paramInt));
  }
  
  public int getProdId()
  {
    return ((Integer)getValue("PROD_ID")).intValue();
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
  
  public void setExpiryNa(String paramString)
  {
    setValue("EXPIRY_NA", paramString);
  }
  
  public String getExpiryNa()
  {
    return (String)getValue("EXPIRY_NA");
  }
  
  public void setQuantity(double paramDouble)
  {
    setValue("QUANTITY", Double.valueOf(paramDouble));
  }
  
  public double getQuantity()
  {
    return ((Double)getValue("QUANTITY")).doubleValue();
  }
  
  public void setQuantityUnit(int paramInt)
  {
    setValue("QUANTITY_UNIT", Integer.valueOf(paramInt));
  }
  
  public int getQuantityUnit()
  {
    return ((Integer)getValue("QUANTITY_UNIT")).intValue();
  }
  
  public void setWearHouseIndex(int paramInt)
  {
    setValue("WEAR_HOUSE_INDEX", Integer.valueOf(paramInt));
  }
  
  public int getWearHouseIndex()
  {
    return ((Integer)getValue("WEAR_HOUSE_INDEX")).intValue();
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.CurrentStockBaseRow
 * JD-Core Version:    0.7.0.1
 */