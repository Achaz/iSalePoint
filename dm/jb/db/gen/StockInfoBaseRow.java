package dm.jb.db.gen;

import dm.tools.db.DBRow;
import java.sql.Date;

public class StockInfoBaseRow
  extends DBRow
{
  protected StockInfoBaseRow(int paramInt, StockInfoBaseTableDef paramStockInfoBaseTableDef)
  {
    super(paramInt, paramStockInfoBaseTableDef);
  }
  
  public void setValues(int paramInt1, int paramInt2, Date paramDate, double paramDouble1, double paramDouble2, String paramString, int paramInt3, int paramInt4)
  {
    setVendor(paramInt1);
    setProdId(paramInt2);
    setStockDate(paramDate);
    setPurchasePrice(paramDouble1);
    setQuantity(paramDouble2);
    setDestinationType(paramString);
    setDestinationIndex(paramInt3);
    setPoIndex(paramInt4);
  }
  
  public void setStockIndex(int paramInt)
  {
    setValue("STOCK_INDEX", Integer.valueOf(paramInt));
  }
  
  public int getStockIndex()
  {
    return ((Integer)getValue("STOCK_INDEX")).intValue();
  }
  
  public void setVendor(int paramInt)
  {
    setValue("VENDOR", Integer.valueOf(paramInt));
  }
  
  public int getVendor()
  {
    return ((Integer)getValue("VENDOR")).intValue();
  }
  
  public void setProdId(int paramInt)
  {
    setValue("PROD_ID", Integer.valueOf(paramInt));
  }
  
  public int getProdId()
  {
    return ((Integer)getValue("PROD_ID")).intValue();
  }
  
  public void setStockDate(Date paramDate)
  {
    setValue("STOCK_DATE", paramDate);
  }
  
  public Date getStockDate()
  {
    Date localDate = (Date)getValue("STOCK_DATE");
    return localDate;
  }
  
  public void setPurchasePrice(double paramDouble)
  {
    setValue("PURCHASE_PRICE", Double.valueOf(paramDouble));
  }
  
  public double getPurchasePrice()
  {
    return ((Double)getValue("PURCHASE_PRICE")).doubleValue();
  }
  
  public void setQuantity(double paramDouble)
  {
    setValue("QUANTITY", Double.valueOf(paramDouble));
  }
  
  public double getQuantity()
  {
    return ((Double)getValue("QUANTITY")).doubleValue();
  }
  
  public void setDestinationType(String paramString)
  {
    setValue("DESTINATION_TYPE", paramString);
  }
  
  public String getDestinationType()
  {
    return (String)getValue("DESTINATION_TYPE");
  }
  
  public void setDestinationIndex(int paramInt)
  {
    setValue("DESTINATION_INDEX", Integer.valueOf(paramInt));
  }
  
  public int getDestinationIndex()
  {
    return ((Integer)getValue("DESTINATION_INDEX")).intValue();
  }
  
  public void setPoIndex(int paramInt)
  {
    setValue("PO_INDEX", Integer.valueOf(paramInt));
  }
  
  public int getPoIndex()
  {
    return ((Integer)getValue("PO_INDEX")).intValue();
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.StockInfoBaseRow
 * JD-Core Version:    0.7.0.1
 */