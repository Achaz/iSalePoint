package dm.jb.db.objects;

import dm.jb.db.gen.PoInfoBaseTableDef;
import dm.tools.db.DBException;
import dm.tools.db.DBRow;
import java.util.ArrayList;
import java.util.Iterator;

public class PoInfoTableDef
  extends PoInfoBaseTableDef
{
  private static PoInfoTableDef _mInstance = null;
  
  public static PoInfoTableDef getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new PoInfoTableDef();
    }
    return _mInstance;
  }
  
  public PoInfoRow getNewRow()
  {
    return new PoInfoRow(getAttrList().size(), this);
  }
  
  public PoInfoRow findRowByIndex(int paramInt)
    throws DBException
  {
    PoInfoRow localPoInfoRow = (PoInfoRow)super.findRowByIndex(paramInt);
    if (localPoInfoRow == null) {
      return null;
    }
    ArrayList localArrayList1 = null;
    ArrayList localArrayList2 = PoEntryTableDef.getInstance().getAllValuesWithWhereClause("PO_ID=" + paramInt);
    if ((localArrayList2 == null) || (localArrayList2.size() == 0)) {
      return localPoInfoRow;
    }
    localArrayList1 = new ArrayList();
    Iterator localIterator = localArrayList2.iterator();
    while (localIterator.hasNext())
    {
      DBRow localDBRow = (DBRow)localIterator.next();
      PoEntryRow localPoEntryRow = (PoEntryRow)localDBRow;
      localArrayList1.add(localPoEntryRow);
    }
    localArrayList2.clear();
    localArrayList2 = null;
    localPoInfoRow.setEntries(localArrayList1);
    return localPoInfoRow;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.PoInfoTableDef
 * JD-Core Version:    0.7.0.1
 */