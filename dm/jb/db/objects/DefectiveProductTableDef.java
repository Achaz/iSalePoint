package dm.jb.db.objects;

import dm.jb.db.gen.DefectiveProductBaseTableDef;
import java.util.ArrayList;

public class DefectiveProductTableDef
  extends DefectiveProductBaseTableDef
{
  private static DefectiveProductTableDef _mInstance = null;
  
  public static DefectiveProductTableDef getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new DefectiveProductTableDef();
    }
    return _mInstance;
  }
  
  public DefectiveProductRow getNewRow()
  {
    return new DefectiveProductRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.DefectiveProductTableDef
 * JD-Core Version:    0.7.0.1
 */