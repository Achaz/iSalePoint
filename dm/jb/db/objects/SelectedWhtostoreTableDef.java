package dm.jb.db.objects;

import dm.jb.db.gen.SelectedWhtostoreBaseTableDef;
import java.util.ArrayList;

public class SelectedWhtostoreTableDef
  extends SelectedWhtostoreBaseTableDef
{
  private static SelectedWhtostoreTableDef _mInstance = null;
  
  public static SelectedWhtostoreTableDef getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new SelectedWhtostoreTableDef();
    }
    return _mInstance;
  }
  
  public SelectedWhtostoreRow getNewRow()
  {
    return new SelectedWhtostoreRow(getAttrList().size(), this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.SelectedWhtostoreTableDef
 * JD-Core Version:    0.7.0.1
 */