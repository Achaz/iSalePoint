package dm.jb.db.gen;

import dm.tools.db.DBRow;

public class WhToStoreEntryBaseRow
  extends DBRow
{
  protected WhToStoreEntryBaseRow(int paramInt, WhToStoreEntryBaseTableDef paramWhToStoreEntryBaseTableDef)
  {
    super(paramInt, paramWhToStoreEntryBaseTableDef);
  }
  
  public void setValues(int paramInt1, long paramLong, int paramInt2, double paramDouble, int paramInt3)
  {
    setWarehouseIndex(paramInt1);
    setProductId(paramLong);
    setStockId(paramInt2);
    setQuantity(paramDouble);
    setStTxnNo(paramInt3);
  }
  
  public void setWarehouseIndex(int paramInt)
  {
    setValue("WAREHOUSE_INDEX", Integer.valueOf(paramInt));
  }
  
  public int getWarehouseIndex()
  {
    return ((Integer)getValue("WAREHOUSE_INDEX")).intValue();
  }
  
  public void setProductId(long paramLong)
  {
    setValue("PRODUCT_ID", Long.valueOf(paramLong));
  }
  
  public long getProductId()
  {
    return ((Long)getValue("PRODUCT_ID")).longValue();
  }
  
  public void setStockId(int paramInt)
  {
    setValue("STOCK_ID", Integer.valueOf(paramInt));
  }
  
  public int getStockId()
  {
    return ((Integer)getValue("STOCK_ID")).intValue();
  }
  
  public void setQuantity(double paramDouble)
  {
    setValue("QUANTITY", Double.valueOf(paramDouble));
  }
  
  public double getQuantity()
  {
    return ((Double)getValue("QUANTITY")).doubleValue();
  }
  
  public void setStTxnNo(int paramInt)
  {
    setValue("ST_TXN_NO", Integer.valueOf(paramInt));
  }
  
  public int getStTxnNo()
  {
    return ((Integer)getValue("ST_TXN_NO")).intValue();
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.WhToStoreEntryBaseRow
 * JD-Core Version:    0.7.0.1
 */