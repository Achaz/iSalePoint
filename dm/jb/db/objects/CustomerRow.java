package dm.jb.db.objects;

import dm.jb.db.gen.CustomerBaseRow;
import dm.tools.db.DBException;

public class CustomerRow
  extends CustomerBaseRow
{
  private CustomerAdvancedRow _mAdvancedRow = null;
  
  CustomerRow(int paramInt, CustomerTableDef paramCustomerTableDef)
  {
    super(paramInt, paramCustomerTableDef);
  }
  
  public void setAdvancedRow(CustomerAdvancedRow paramCustomerAdvancedRow)
  {
    this._mAdvancedRow = paramCustomerAdvancedRow;
  }
  
  public CustomerAdvancedRow getAdvancedRow()
    throws DBException
  {
    if (this._mAdvancedRow == null) {
      this._mAdvancedRow = ((CustomerAdvancedRow)CustomerAdvancedTableDef.getInstance().findRowByIndex(getCustIndex()));
    }
    return this._mAdvancedRow;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.CustomerRow
 * JD-Core Version:    0.7.0.1
 */