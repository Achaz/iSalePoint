package dm.jb.ui.billing;

public abstract interface PaymentOptionPanel
{
  public abstract void windowDisplayed();
  
  public abstract boolean isPageValid();
  
  public abstract void setAmount(double paramDouble);
  
  public abstract void setObject(PaymentModeObject paramPaymentModeObject);
  
  public abstract void clearFields();
  
  public abstract void getValuesInObject(PaymentModeObject paramPaymentModeObject);
  
  public abstract void setEditable(boolean paramBoolean);
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.billing.PaymentOptionPanel
 * JD-Core Version:    0.7.0.1
 */