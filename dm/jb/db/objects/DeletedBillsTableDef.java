package dm.jb.db.objects;

import dm.jb.db.gen.DeletedBillsBaseTableDef;
import java.util.ArrayList;

public class DeletedBillsTableDef
  extends DeletedBillsBaseTableDef
{
  private static DeletedBillsTableDef _mInstance = null;
  
  public static DeletedBillsTableDef getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new DeletedBillsTableDef();
    }
    return _mInstance;
  }
  
  public DeletedBillsRow getNewRow()
  {
    return new DeletedBillsRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.DeletedBillsTableDef
 * JD-Core Version:    0.7.0.1
 */