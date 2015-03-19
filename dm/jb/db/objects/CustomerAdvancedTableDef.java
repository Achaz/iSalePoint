package dm.jb.db.objects;

import dm.jb.db.gen.CustomerAdvancedBaseTableDef;
import java.util.ArrayList;

public class CustomerAdvancedTableDef
  extends CustomerAdvancedBaseTableDef
{
  private static CustomerAdvancedTableDef _mInstance = new CustomerAdvancedTableDef();
  
  public static CustomerAdvancedTableDef getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new CustomerAdvancedTableDef();
    }
    return _mInstance;
  }
  
  public CustomerAdvancedRow getNewRow()
  {
    return new CustomerAdvancedRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.CustomerAdvancedTableDef
 * JD-Core Version:    0.7.0.1
 */