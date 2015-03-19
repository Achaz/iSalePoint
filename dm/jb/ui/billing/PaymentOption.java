package dm.jb.ui.billing;

import dm.tools.db.DBException;
import java.sql.Date;
import java.util.ArrayList;
import javax.swing.JPanel;

public abstract interface PaymentOption
{
  public abstract JPanel getOptionPanel();
  
  public abstract String getCode();
  
  public abstract PaymentModeObject getNewPaymentObjectForBillPay(int paramInt);
  
  public abstract PaymentModeObject getNewPaymentObjectForBillRefund(int paramInt);
  
  public abstract PaymentModeObject getNewPaymentObjectForBillAddl(int paramInt);
  
  public abstract ArrayList<PaymentModeObject> getPayments(long paramLong, int paramInt)
    throws DBException;
  
  public abstract ArrayList<PaymentModeObject> getPaymentsOnDate(Date paramDate)
    throws DBException;
  
  public abstract ArrayList<PaymentModeObject> getPaymentsOnDateRange(Date paramDate1, Date paramDate2)
    throws DBException;
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.billing.PaymentOption
 * JD-Core Version:    0.7.0.1
 */