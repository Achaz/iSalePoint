package dm.jb.ui.billing;

import dm.jb.db.objects.CustomerRow;

public abstract interface CustomerListAction
{
  public abstract void selected(CustomerRow paramCustomerRow);
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.billing.CustomerListAction
 * JD-Core Version:    0.7.0.1
 */