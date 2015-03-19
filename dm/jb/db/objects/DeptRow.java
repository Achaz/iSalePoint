package dm.jb.db.objects;

import dm.jb.db.gen.DeptBaseRow;
import dm.jb.op.sync.FileOpUtils;
import dm.jb.op.sync.SyncWriter;
import dm.tools.db.DBException;
import dm.tools.db.DBRow;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class DeptRow
  extends DeptBaseRow
  implements Comparable
{
  private ArrayList<CategoryRow> _mCatList = null;
  
  DeptRow(int paramInt, DeptTableDef paramDeptTableDef)
  {
    super(paramInt, paramDeptTableDef);
  }
  
  public String toString()
  {
    return getDeptName();
  }
  
  private ArrayList<CategoryRow> getCategoryListForThis()
    throws DBException
  {
    ArrayList localArrayList1 = CategoryTableDef.getInstance().getAllValuesWithWhereClause(" DEPT_INDEX=" + getDeptIndex());
    Iterator localIterator = localArrayList1.iterator();
    ArrayList localArrayList2 = new ArrayList();
    while (localIterator.hasNext())
    {
      CategoryRow localCategoryRow = (CategoryRow)localIterator.next();
      localArrayList2.add(localCategoryRow);
    }
    return localArrayList2;
  }
  
  public ArrayList<CategoryRow> getCategoryList()
    throws DBException
  {
    if (this._mCatList == null) {
      this._mCatList = getCategoryListForThis();
    }
    return this._mCatList;
  }
  
  public ArrayList<ProductRow> getAllProducts()
    throws DBException
  {
    ArrayList localArrayList1 = ProductTableDef.getInstance().getAllValuesWithWhereClause("DEPT_INDEX=" + getDeptIndex());
    if ((localArrayList1 == null) || (localArrayList1.size() == 0)) {
      return null;
    }
    ArrayList localArrayList2 = new ArrayList();
    Iterator localIterator = localArrayList1.iterator();
    while (localIterator.hasNext())
    {
      DBRow localDBRow = (DBRow)localIterator.next();
      localArrayList2.add((ProductRow)localDBRow);
    }
    localArrayList1.clear();
    localArrayList1 = null;
    return localArrayList2;
  }
  
  public void addCategoryRow(CategoryRow paramCategoryRow)
    throws DBException
  {
    if (this._mCatList == null) {
      this._mCatList = getCategoryListForThis();
    } else {
      this._mCatList.add(paramCategoryRow);
    }
  }
  
  public boolean isDuplicateCategory(String paramString)
    throws DBException
  {
    if (this._mCatList == null) {
      this._mCatList = getCategoryListForThis();
    }
    Iterator localIterator = this._mCatList.iterator();
    while (localIterator.hasNext())
    {
      CategoryRow localCategoryRow = (CategoryRow)localIterator.next();
      if (localCategoryRow.getCatName().equals(paramString)) {
        return true;
      }
    }
    return false;
  }
  
  public void removeFromCatList(CategoryRow paramCategoryRow)
    throws DBException
  {
    if (this._mCatList == null) {
      this._mCatList = getCategoryListForThis();
    }
    this._mCatList.remove(paramCategoryRow);
  }
  
  public CategoryRow findCategoryByIndex(int paramInt)
    throws DBException
  {
    if (this._mCatList == null) {
      this._mCatList = getCategoryListForThis();
    }
    Object localObject = null;
    Iterator localIterator = this._mCatList.iterator();
    while (localIterator.hasNext())
    {
      CategoryRow localCategoryRow = (CategoryRow)localIterator.next();
      if (localCategoryRow.getCatIndex() == paramInt)
      {
        localObject = localCategoryRow;
        break;
      }
    }
    return localObject;
  }
  
  public void delete()
    throws DBException
  {
    CategoryTableDef.getInstance().deleteAllCategoriesForDeptIndex(getDeptIndex());
    super.delete();
    DeptTableDef.getInstance().deleteDeptFromList(this);
    if (this._mCatList != null)
    {
      Iterator localIterator = this._mCatList.iterator();
      while (localIterator.hasNext())
      {
        CategoryRow localCategoryRow = (CategoryRow)localIterator.next();
        localCategoryRow.delete();
        localCategoryRow.cleanProductList();
      }
      this._mCatList = null;
    }
  }
  
  public int compareTo(Object paramObject)
  {
    if ((paramObject instanceof DeptRow))
    {
      DeptRow localDeptRow = (DeptRow)paramObject;
      if (localDeptRow.getDeptIndex() == getDeptIndex()) {
        return 0;
      }
    }
    return 1;
  }
  
  public void sync(SyncWriter paramSyncWriter)
    throws IOException
  {
    paramSyncWriter.writeInt(getDeptIndex());
    paramSyncWriter.writeString(getDeptName());
    paramSyncWriter.writeString(getDeptDetails());
  }
  
  public int syncFromStream(FileInputStream paramFileInputStream)
    throws IOException
  {
    int i = FileOpUtils.readInt(paramFileInputStream);
    String str1 = FileOpUtils.readString(paramFileInputStream);
    String str2 = FileOpUtils.readString(paramFileInputStream);
    setDeptIndex(i);
    setDeptName(str1);
    setDeptDetails(str2);
    return 4 + (str1.getBytes().length + 2) + (str2.getBytes().length + 2);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.DeptRow
 * JD-Core Version:    0.7.0.1
 */