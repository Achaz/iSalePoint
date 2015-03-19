package dm.jb.db.gen;

import dm.tools.db.DBRow;
import java.sql.Date;

public class DefectiveProductBaseRow
  extends DBRow
{
  protected DefectiveProductBaseRow(int paramInt, DefectiveProductBaseTableDef paramDefectiveProductBaseTableDef)
  {
    super(paramInt, paramDefectiveProductBaseTableDef);
  }
  
  public void setValues(long paramLong, int paramInt, double paramDouble1, Date paramDate, double paramDouble2)
  {
    setStoreId(paramInt);
    setQuantity(paramDouble1);
    setRecieveDate(paramDate);
    setQuantitySent(paramDouble2);
  }
  
  public void setProductId(long paramLong)
  {
    setValue("PRODUCT_ID", Long.valueOf(paramLong));
  }
  
  public long getProductId()
  {
    return ((Long)getValue("PRODUCT_ID")).longValue();
  }
  
  public void setStoreId(int paramInt)
  {
    setValue("STORE_ID", Integer.valueOf(paramInt));
  }
  
  public int getStoreId()
  {
    return ((Integer)getValue("STORE_ID")).intValue();
  }
  
  public void setQuantity(double paramDouble)
  {
    setValue("QUANTITY", Double.valueOf(paramDouble));
  }
  
  public double getQuantity()
  {
    return ((Double)getValue("QUANTITY")).doubleValue();
  }
  
  public void setRecieveDate(Date paramDate)
  {
    setValue("RECIEVE_DATE", paramDate);
  }
  
  public Date getRecieveDate()
  {
    Date localDate = (Date)getValue("RECIEVE_DATE");
    return localDate;
  }
  
  public void setQuantitySent(double paramDouble)
  {
    setValue("QUANTITY_SENT", Double.valueOf(paramDouble));
  }
  
  public double getQuantitySent()
  {
    return ((Double)getValue("QUANTITY_SENT")).doubleValue();
  }
  
  public void setObjectIndex(long paramLong)
  {
    setValue("OBJECT_INDEX", Long.valueOf(paramLong));
  }
  
  public long getObjectIndex()
  {
    return ((Long)getValue("OBJECT_INDEX")).longValue();
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.DefectiveProductBaseRow
 * JD-Core Version:    0.7.0.1
 */