package dm.jb.ui.billing;

import dm.tools.db.DBException;
import java.util.Date;

public abstract interface PaymentModeObject
{
  public abstract String getRefNo();
  
  public abstract double getAmount();
  
  public abstract double getAmountForModel();
  
  public abstract String getComments();
  
  public abstract String getOptionString();
  
  public abstract PaymentOption getPaymentOption();
  
  public abstract Date getDate();
  
  public abstract String getTxnType();
  
  public abstract void createInDB(long paramLong, int paramInt)
    throws DBException;
  
  public abstract void deleteFromDB()
    throws DBException;
  
  public abstract void updateInDB(long paramLong)
    throws DBException;
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.billing.PaymentModeObject
 * JD-Core Version:    0.7.0.1
 */