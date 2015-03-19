package dm.jb.db.objects;

import dm.jb.db.gen.CustomerBaseTableDef;
import dm.tools.db.DBException;
import dm.tools.db.Db;
import java.util.ArrayList;
import java.util.Iterator;

public class CustomerTableDef
  extends CustomerBaseTableDef
{
  private static CustomerTableDef _mInstance = null;
  
  public static CustomerTableDef getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new CustomerTableDef();
    }
    return _mInstance;
  }
  
  public CustomerRow getNewRow()
  {
    return new CustomerRow(getAttrList().size(), this);
  }
  
  public CustomerRow findRowByIndex(int paramInt)
    throws DBException
  {
    CustomerRow localCustomerRow = getNewRow();
    if (!findRowByIndexAndFillValues(paramInt, localCustomerRow)) {
      return null;
    }
    return localCustomerRow;
  }
  
  public ArrayList<CustomerRow> findRowsByName(String paramString)
    throws DBException
  {
    ArrayList localArrayList1 = null;
    String str = Db.getSearchFormattedString(paramString);
    localArrayList1 = getAllValuesWithWhereClause("CUST_NAME LIKE '" + str + "'");
    if ((localArrayList1 == null) || (localArrayList1.size() == 0)) {
      return null;
    }
    Iterator localIterator = localArrayList1.iterator();
    ArrayList localArrayList2 = new ArrayList();
    while (localIterator.hasNext())
    {
      CustomerRow localCustomerRow = (CustomerRow)localIterator.next();
      localArrayList2.add(localCustomerRow);
    }
    return localArrayList2;
  }
  
  public ArrayList<CustomerRow> findRowsByPhone(String paramString)
    throws DBException
  {
    ArrayList localArrayList1 = null;
    String str = Db.getSearchFormattedString(paramString);
    localArrayList1 = getAllValuesWithWhereClause("CUST_PHONE LIKE '" + str + "'");
    if ((localArrayList1 == null) || (localArrayList1.size() == 0)) {
      return null;
    }
    Iterator localIterator = localArrayList1.iterator();
    ArrayList localArrayList2 = new ArrayList();
    while (localIterator.hasNext())
    {
      CustomerRow localCustomerRow = (CustomerRow)localIterator.next();
      localArrayList2.add(localCustomerRow);
    }
    return localArrayList2;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.CustomerTableDef
 * JD-Core Version:    0.7.0.1
 */