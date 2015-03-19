package dm.jb.db.gen;

import dm.tools.db.DBRow;

public class StockReturnEntryBaseRow
  extends DBRow
{
  protected StockReturnEntryBaseRow(int paramInt, StockReturnEntryBaseTableDef paramStockReturnEntryBaseTableDef)
  {
    super(paramInt, paramStockReturnEntryBaseTableDef);
  }
  
  public void setValues(long paramLong1, long paramLong2, double paramDouble, String paramString, int paramInt1, int paramInt2, int paramInt3)
  {
    setSrTxn(paramLong1);
    setProductId(paramLong2);
    setQuantity(paramDouble);
    setFromType(paramString);
    setFromId(paramInt1);
    setEntryIndex(paramInt2);
    setStockIndex(paramInt3);
  }
  
  public void setSrTxn(long paramLong)
  {
    setValue("SR_TXN", Long.valueOf(paramLong));
  }
  
  public long getSrTxn()
  {
    return ((Long)getValue("SR_TXN")).longValue();
  }
  
  public void setProductId(long paramLong)
  {
    setValue("PRODUCT_ID", Long.valueOf(paramLong));
  }
  
  public long getProductId()
  {
    return ((Long)getValue("PRODUCT_ID")).longValue();
  }
  
  public void setQuantity(double paramDouble)
  {
    setValue("QUANTITY", Double.valueOf(paramDouble));
  }
  
  public double getQuantity()
  {
    return ((Double)getValue("QUANTITY")).doubleValue();
  }
  
  public void setFromType(String paramString)
  {
    setValue("FROM_TYPE", paramString);
  }
  
  public String getFromType()
  {
    return (String)getValue("FROM_TYPE");
  }
  
  public void setFromId(int paramInt)
  {
    setValue("FROM_ID", Integer.valueOf(paramInt));
  }
  
  public int getFromId()
  {
    return ((Integer)getValue("FROM_ID")).intValue();
  }
  
  public void setStockIndex(int paramInt)
  {
    setValue("STOCK_INDEX", Integer.valueOf(paramInt));
  }
  
  public int getStockIndex()
  {
    return ((Integer)getValue("STOCK_INDEX")).intValue();
  }
  
  public void setEntryIndex(int paramInt)
  {
    setValue("ENTRY_INDEX", Integer.valueOf(paramInt));
  }
  
  public int getEntryIndex()
  {
    return ((Integer)getValue("ENTRY_INDEX")).intValue();
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.StockReturnEntryBaseRow
 * JD-Core Version:    0.7.0.1
 */