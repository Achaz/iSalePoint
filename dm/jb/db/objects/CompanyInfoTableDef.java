package dm.jb.db.objects;

import dm.jb.db.gen.CompanyInfoBaseTableDef;
import dm.tools.db.DBException;
import java.util.ArrayList;

public class CompanyInfoTableDef
  extends CompanyInfoBaseTableDef
{
  private static CompanyInfoTableDef _mInstance = null;
  private CompanyInfoRow _mCompnayInfo = null;
  
  public static CompanyInfoTableDef getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new CompanyInfoTableDef();
    }
    return _mInstance;
  }
  
  public CompanyInfoRow getNewRow()
  {
    return new CompanyInfoRow(getAttrList().size(), this);
  }
  
  public CompanyInfoRow getCompany()
    throws DBException
  {
    if (this._mCompnayInfo == null)
    {
      ArrayList localArrayList = getAllValues();
      if ((localArrayList == null) || (localArrayList.size() == 0)) {
        return null;
      }
      this._mCompnayInfo = ((CompanyInfoRow)localArrayList.get(0));
    }
    return this._mCompnayInfo;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.CompanyInfoTableDef
 * JD-Core Version:    0.7.0.1
 */