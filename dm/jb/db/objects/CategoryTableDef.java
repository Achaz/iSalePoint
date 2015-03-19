package dm.jb.db.objects;

import dm.jb.db.gen.CategoryBaseTableDef;
import dm.tools.db.DBException;
import dm.tools.db.DBRow;
import java.util.ArrayList;

public class CategoryTableDef
  extends CategoryBaseTableDef
{
  private static CategoryTableDef _mInstance = null;
  private ArrayList<DBRow> _mCatList = null;
  
  public static CategoryTableDef getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new CategoryTableDef();
    }
    return _mInstance;
  }
  
  public CategoryRow getNewRow()
  {
    return new CategoryRow(getAttrList().size(), this);
  }
  
  public void deleteAllCategoriesForDeptIndex(int paramInt)
    throws DBException
  {
    ProductTableDef.getInstance().deleteAllForDept(paramInt);
    deleteWithWhere(" DEPT_INDEX=" + paramInt);
  }
  
  public CategoryRow findCatgeoryByIndex(int paramInt)
    throws DBException
  {
    if (this._mCatList == null)
    {
      this._mCatList = getAllValues();
      if (this._mCatList == null) {
        return null;
      }
    }
    for (int i = 0; i < this._mCatList.size(); i++)
    {
      CategoryRow localCategoryRow = (CategoryRow)this._mCatList.get(i);
      if (localCategoryRow.getCatIndex() == paramInt) {
        return localCategoryRow;
      }
    }
    return null;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.CategoryTableDef
 * JD-Core Version:    0.7.0.1
 */