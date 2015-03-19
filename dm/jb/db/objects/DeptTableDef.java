package dm.jb.db.objects;

import dm.jb.db.gen.DeptBaseTableDef;
import dm.tools.db.DBException;
import java.util.ArrayList;
import java.util.Iterator;

public class DeptTableDef
  extends DeptBaseTableDef
{
  private static DeptTableDef _mInstance = null;
  ArrayList<DeptRow> _mDeptList = null;
  
  public static DeptTableDef getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new DeptTableDef();
    }
    return _mInstance;
  }
  
  public DeptRow getNewRow()
  {
    return new DeptRow(getAttrList().size(), this);
  }
  
  private ArrayList<DeptRow> getAllDeptList()
    throws DBException
  {
    ArrayList localArrayList1 = getAllValues();
    ArrayList localArrayList2 = new ArrayList();
    Iterator localIterator = localArrayList1.iterator();
    while (localIterator.hasNext()) {
      localArrayList2.add((DeptRow)localIterator.next());
    }
    return localArrayList2;
  }
  
  public DeptRow findRowByIndex(int paramInt)
    throws DBException
  {
    DeptRow localDeptRow = getNewRow();
    findRowByIndexAndFillValues(paramInt, localDeptRow);
    return localDeptRow;
  }
  
  public ArrayList<DeptRow> getDeptList()
    throws DBException
  {
    if (this._mDeptList == null) {
      this._mDeptList = getAllDeptList();
    }
    return this._mDeptList;
  }
  
  public boolean isDuplicateDept(String paramString)
    throws DBException
  {
    Iterator localIterator = getAllDeptList().iterator();
    while (localIterator.hasNext())
    {
      DeptRow localDeptRow = (DeptRow)localIterator.next();
      if (localDeptRow.getDeptName().equals(paramString)) {
        return true;
      }
    }
    return false;
  }
  
  public void addDeptToList(DeptRow paramDeptRow)
    throws DBException
  {
    if (this._mDeptList == null) {
      this._mDeptList = getAllDeptList();
    } else {
      this._mDeptList.add(paramDeptRow);
    }
  }
  
  public void deleteDeptFromList(DeptRow paramDeptRow)
    throws DBException
  {
    if (this._mDeptList == null) {
      this._mDeptList = getAllDeptList();
    }
    this._mDeptList.remove(paramDeptRow);
  }
  
  public DeptRow findDeptByIndex(int paramInt)
    throws DBException
  {
    if (this._mDeptList == null) {
      this._mDeptList = getAllDeptList();
    }
    Iterator localIterator = this._mDeptList.iterator();
    while (localIterator.hasNext())
    {
      DeptRow localDeptRow = (DeptRow)localIterator.next();
      if (localDeptRow.getDeptIndex() == paramInt) {
        return localDeptRow;
      }
    }
    return null;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.DeptTableDef
 * JD-Core Version:    0.7.0.1
 */