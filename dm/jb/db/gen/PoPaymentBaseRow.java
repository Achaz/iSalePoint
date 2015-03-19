package dm.jb.db.gen;

import dm.tools.db.DBRow;
import java.sql.Date;

public class PoPaymentBaseRow
  extends DBRow
{
  protected PoPaymentBaseRow(int paramInt, PoPaymentBaseTableDef paramPoPaymentBaseTableDef)
  {
    super(paramInt, paramPoPaymentBaseTableDef);
  }
  
  public void setValues(int paramInt, double paramDouble, Date paramDate)
  {
    setPurchaseOrderNo(paramInt);
    setAmountPaid(paramDouble);
    setDate(paramDate);
  }
  
  public void setPurchaseOrderNo(int paramInt)
  {
    setValue("PURCHASE_ORDER_NO", Integer.valueOf(paramInt));
  }
  
  public int getPurchaseOrderNo()
  {
    return ((Integer)getValue("PURCHASE_ORDER_NO")).intValue();
  }
  
  public void setAmountPaid(double paramDouble)
  {
    setValue("AMOUNT_PAID", Double.valueOf(paramDouble));
  }
  
  public double getAmountPaid()
  {
    return ((Double)getValue("AMOUNT_PAID")).doubleValue();
  }
  
  public void setDate(Date paramDate)
  {
    setValue("DATE", paramDate);
  }
  
  public Date getDate()
  {
    Date localDate = (Date)getValue("DATE");
    return localDate;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.PoPaymentBaseRow
 * JD-Core Version:    0.7.0.1
 */