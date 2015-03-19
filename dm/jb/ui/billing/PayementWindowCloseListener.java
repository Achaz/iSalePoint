package dm.jb.ui.billing;

import dm.jb.db.objects.Payment;

public abstract interface PayementWindowCloseListener
{
  public abstract void paymentWindowOKClicked(Payment paramPayment);
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.billing.PayementWindowCloseListener
 * JD-Core Version:    0.7.0.1
 */