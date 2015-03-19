package dm.jb.db.objects;

import dm.jb.db.gen.BillEntryBaseTableDef;
import dm.tools.db.DBException;
import java.util.ArrayList;

public class BillEntryTableDef
  extends BillEntryBaseTableDef
{
  private static BillEntryTableDef _mInstance = null;
  
  public static BillEntryTableDef getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new BillEntryTableDef();
    }
    return _mInstance;
  }
  
  public BillEntryRow getNewRow()
  {
    return new BillEntryRow(getAttrList().size(), this);
  }
  
  public ArrayList<BillEntryRow> getBillEntriesForBillNo(int paramInt)
    throws DBException
  {
    ArrayList localArrayList1 = getAllValuesWithWhereClause(" BILL_NO=" + paramInt);
    Object[] arrayOfObject = localArrayList1.toArray();
    for (int i = 1; i < arrayOfObject.length; i++)
    {
      BillEntryRow localBillEntryRow1 = (BillEntryRow)arrayOfObject[i];
      int k = 0;
      for (k = i - 1; k >= 0; k--)
      {
        BillEntryRow localBillEntryRow2 = (BillEntryRow)arrayOfObject[k];
        if (localBillEntryRow2.getEntryIndex() >= localBillEntryRow1.getEntryIndex()) {
          break;
        }
        arrayOfObject[(k + 1)] = arrayOfObject[k];
      }
      arrayOfObject[(k + 1)] = localBillEntryRow1;
    }
    ArrayList localArrayList2 = new ArrayList();
    for (int j = 0; j < arrayOfObject.length; j++) {
      localArrayList2.add((BillEntryRow)arrayOfObject[j]);
    }
    return localArrayList2;
  }
  
  public void deleteEntriesForBillNo(int paramInt)
    throws DBException
  {
    String str = " BILL_NO=" + paramInt;
    super.deleteWithWhere(str);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.BillEntryTableDef
 * JD-Core Version:    0.7.0.1
 */