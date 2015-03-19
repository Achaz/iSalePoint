package dm.jb.db.objects;

import dm.jb.db.gen.BillTableBaseTableDef;
import dm.tools.db.BindObject;
import dm.tools.db.DBException;
import java.util.ArrayList;
import java.util.Iterator;

public class BillTableTableDef
  extends BillTableBaseTableDef
{
  private static BillTableTableDef _mInstance = null;
  
  public static BillTableTableDef getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new BillTableTableDef();
    }
    return _mInstance;
  }
  
  public BillTableRow getNewRow()
  {
    return new BillTableRow(getAttrList().size(), this);
  }
  
  public BillTableRow getBillTableForBillNo(int paramInt)
    throws DBException
  {
    return (BillTableRow)findRowByIndex(paramInt);
  }
  
  public ArrayList<BillTableRow> getBillTableForDateDuration(java.util.Date paramDate1, java.util.Date paramDate2)
    throws DBException
  {
    BindObject[] arrayOfBindObject = new BindObject[4];
    java.sql.Date localDate1 = new java.sql.Date(paramDate1.getTime());
    java.sql.Date localDate2 = new java.sql.Date(paramDate2.getTime());
    arrayOfBindObject[0] = new BindObject(1, 5, localDate1);
    arrayOfBindObject[1] = new BindObject(2, 5, localDate1);
    arrayOfBindObject[2] = new BindObject(3, 5, localDate2);
    arrayOfBindObject[3] = new BindObject(4, 5, localDate2);
    ArrayList localArrayList1 = getAllValuesWithWhereClauseWithBind("((BILL_DATE > ?) || (BILL_DATE = ?)) && ((BILL_DATE < ?) || (BILL_DATE = ?)) ", arrayOfBindObject);
    Iterator localIterator = localArrayList1.iterator();
    ArrayList localArrayList2 = new ArrayList();
    while (localIterator.hasNext())
    {
      BillTableRow localBillTableRow = (BillTableRow)localIterator.next();
      localBillTableRow.getBillEntries();
      localArrayList2.add(localBillTableRow);
    }
    return localArrayList2;
  }
  
  public BillEntryRow[] getBillEntriesForDateDurationForDepts(java.util.Date paramDate1, java.util.Date paramDate2, ArrayList<DeptRow> paramArrayList)
    throws DBException
  {
    StringBuffer localStringBuffer1 = new StringBuffer();
    Iterator localIterator = paramArrayList.iterator();
    StringBuffer localStringBuffer2 = new StringBuffer("SELECT BILL_NO FROM BILL_TABLE WHERE ((BILL_DATE > ?) || (BILL_DATE = ?)) && ((BILL_DATE < ?) || (BILL_DATE = ?)) ");
    StringBuffer localStringBuffer3 = new StringBuffer("SELECT PRODUCT_INDEX FROM PRODUCT WHERE DEPT_INDEX IN ( ");
    while (localIterator.hasNext())
    {
      localObject = (DeptRow)localIterator.next();
      if (localIterator.hasNext()) {
        localStringBuffer3.append(((DeptRow)localObject).getDeptIndex() + ", ");
      } else {
        localStringBuffer3.append(((DeptRow)localObject).getDeptIndex());
      }
    }
    localStringBuffer3.append(" )");
    localStringBuffer1.append("( BILL_NO IN (");
    localStringBuffer1.append(localStringBuffer2);
    localStringBuffer1.append(") AND ( PRODUCT_INDEX IN (");
    localStringBuffer1.append(localStringBuffer3);
    localStringBuffer1.append(") ) ) ");
    Object localObject = new BindObject[4];
    java.sql.Date localDate1 = new java.sql.Date(paramDate1.getTime());
    java.sql.Date localDate2 = new java.sql.Date(paramDate2.getTime());
    localObject[0] = new BindObject(1, 5, localDate1);
    localObject[1] = new BindObject(2, 5, localDate1);
    localObject[2] = new BindObject(3, 5, localDate2);
    localObject[3] = new BindObject(4, 5, localDate2);
    ArrayList localArrayList = BillEntryTableDef.getInstance().getAllValuesWithWhereClauseWithBind(localStringBuffer1.toString(), (BindObject[])localObject);
    BillEntryRow[] arrayOfBillEntryRow = (BillEntryRow[])localArrayList.toArray(new BillEntryRow[1]);
    if (localArrayList.size() == 0) {
      arrayOfBillEntryRow = new BillEntryRow[0];
    }
    for (int i = 0; i < arrayOfBillEntryRow.length - 1; i++) {
      for (int j = 0; j < arrayOfBillEntryRow.length - 1 - i; j++) {
        if (arrayOfBillEntryRow[(j + 1)].getProduct().getDeptIndex() > arrayOfBillEntryRow[j].getProduct().getDeptIndex())
        {
          BillEntryRow localBillEntryRow = arrayOfBillEntryRow[j];
          arrayOfBillEntryRow[j] = arrayOfBillEntryRow[(j + 1)];
          arrayOfBillEntryRow[(j + 1)] = localBillEntryRow;
        }
      }
    }
    return arrayOfBillEntryRow;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.BillTableTableDef
 * JD-Core Version:    0.7.0.1
 */