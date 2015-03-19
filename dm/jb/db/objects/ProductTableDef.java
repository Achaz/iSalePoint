package dm.jb.db.objects;

import dm.jb.db.gen.ProductBaseTableDef;
import dm.tools.db.BindObject;
import dm.tools.db.DBException;
import dm.tools.db.Db;
import java.util.ArrayList;
import java.util.Iterator;

public class ProductTableDef
  extends ProductBaseTableDef
{
  public static final int SORT_ORDER_ASC = 1;
  public static final int SORT_ORDER_DESC = 2;
  public static final int SORT_ORDER_DEFAULT = 0;
  private static ProductTableDef _mInstance = null;
  private BindObject[] _mFndByProductCodeBind = { new BindObject(1, 3, "") };
  private BindObject[] _mFndByRfidBind = { new BindObject(1, 3, "") };
  
  public static ProductTableDef getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new ProductTableDef();
    }
    return _mInstance;
  }
  
  public ProductRow getNewRow()
  {
    return new ProductRow(getAttrList().size(), this);
  }
  
  public ProductRow findRowByIndex(int paramInt)
    throws DBException
  {
    return (ProductRow)super.findRowByIndex(paramInt);
  }
  
  public ProductRow findRowByIndexForUpdate(int paramInt)
    throws DBException
  {
    return (ProductRow)super.findRowByIndex(paramInt);
  }
  
  public ArrayList<ProductRow> getProductsForCategory(CategoryRow paramCategoryRow)
    throws DBException
  {
    ArrayList localArrayList1 = getAllValuesWithWhereClause(" CAT_INDEX=" + paramCategoryRow.getCatIndex() + " ");
    Iterator localIterator = localArrayList1.iterator();
    ArrayList localArrayList2 = new ArrayList();
    while (localIterator.hasNext()) {
      localArrayList2.add((ProductRow)localIterator.next());
    }
    return localArrayList2;
  }
  
  public static ArrayList<ProductRow> sortTableOnCode(int paramInt, ArrayList<ProductRow> paramArrayList)
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = paramArrayList.iterator();
    while (localIterator.hasNext())
    {
      ProductRow localProductRow = (ProductRow)localIterator.next();
      addToCodeSortedList(localArrayList, localProductRow, paramInt);
    }
    return localArrayList;
  }
  
  public static void addToCodeSortedList(ArrayList<ProductRow> paramArrayList, ProductRow paramProductRow, int paramInt)
  {
    int i;
    Iterator localIterator;
    ProductRow localProductRow;
    if (paramInt == 1)
    {
      i = -1;
      localIterator = paramArrayList.iterator();
      if (!localIterator.hasNext())
      {
        paramArrayList.add(paramProductRow);
        return;
      }
      while (localIterator.hasNext())
      {
        i++;
        localProductRow = (ProductRow)localIterator.next();
        if (localProductRow.getProdIndex() > paramProductRow.getProdIndex()) {
          break;
        }
        if (!localIterator.hasNext()) {
          i = -1;
        }
      }
      if (i != -1) {
        paramArrayList.add(i, paramProductRow);
      } else {
        paramArrayList.add(paramProductRow);
      }
    }
    else if (paramInt == 2)
    {
      i = -1;
      localIterator = paramArrayList.iterator();
      if (!localIterator.hasNext())
      {
        paramArrayList.add(paramProductRow);
        return;
      }
      while (localIterator.hasNext())
      {
        i++;
        localProductRow = (ProductRow)localIterator.next();
        if (localProductRow.getProdIndex() < paramProductRow.getProdIndex()) {
          break;
        }
        if (!localIterator.hasNext()) {
          i = -1;
        }
      }
      if (i != -1) {
        paramArrayList.add(i, paramProductRow);
      } else {
        paramArrayList.add(paramProductRow);
      }
    }
  }
  
  public void deleteAllForDept(int paramInt)
    throws DBException
  {
    deleteWithWhere(" DEPT_INDEX=" + paramInt);
  }
  
  public void deleteAllForCat(int paramInt)
    throws DBException
  {
    deleteWithWhere(" CAT_INDEX=" + paramInt);
  }
  
  public ArrayList<ProductRow> getProductBySearchParams(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3)
    throws DBException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    int i = 0;
    if (paramInt1 != -1)
    {
      if (paramInt2 != -1)
      {
        localStringBuffer.append(" ((PROD_INDEX > " + paramInt1 + ") OR ( PROD_INDEX = " + paramInt1 + "))");
        localStringBuffer.append(" AND ((PROD_INDEX < " + paramInt2 + ") OR (PROD_INDEX = " + paramInt2 + ")) ");
      }
      else
      {
        localStringBuffer.append(" PROD_INDEX = " + paramInt1 + " ");
      }
      i = 1;
    }
    if ((paramString3 != null) && (paramString3.length() > 0))
    {
      if (i != 0) {
        localStringBuffer.append(" AND");
      }
      localObject1 = "*" + paramString3 + "*";
      localObject2 = Db.getSearchFormattedString((String)localObject1);
      localStringBuffer.append(" ( PROD_NAME LIKE '" + (String)localObject2 + "' ) ");
      i = 1;
    }
    if ((paramString1 != null) && (paramString1.length() > 0))
    {
      if (i != 0) {
        localStringBuffer.append(" AND");
      }
      localObject1 = "*" + paramString1 + "*";
      localObject2 = Db.getSearchFormattedString((String)localObject1);
      localStringBuffer.append(" ( DEPT_INDEX IN (");
      localStringBuffer.append(" SELECT DEPT_INDEX FROM DEPT WHERE DEPT_NAME LIKE '" + (String)localObject2 + "' )");
      localStringBuffer.append(" )");
      i = 1;
    }
    if ((paramString2 != null) && (paramString2.length() > 0))
    {
      if (i != 0) {
        localStringBuffer.append(" AND");
      }
      localObject1 = "*" + paramString2 + "*";
      localObject2 = Db.getSearchFormattedString((String)localObject1);
      localStringBuffer.append(" ( CAT_INDEX IN (");
      localStringBuffer.append(" SELECT CAT_INDEX FROM CATEGORY WHERE CAT_NAME LIKE '" + (String)localObject2 + "' )");
      localStringBuffer.append(" )");
      i = 1;
    }
    Object localObject1 = null;
    if (localStringBuffer.length() == 0) {
      localObject1 = getAllValues();
    } else {
      localObject1 = getAllValuesWithWhereClause(localStringBuffer.toString());
    }
    Object localObject2 = ((ArrayList)localObject1).iterator();
    ArrayList localArrayList = new ArrayList();
    while (((Iterator)localObject2).hasNext())
    {
      ProductRow localProductRow = (ProductRow)((Iterator)localObject2).next();
      localArrayList.add(localProductRow);
      localProductRow.getCatForThis();
    }
    return localArrayList;
  }
  
  public boolean isDuplicateProductName(String paramString, int paramInt1, int paramInt2)
    throws DBException
  {
    ArrayList localArrayList = getAllValuesWithWhereClause(" PROD_NAME like '" + paramString + "' AND CAT_INDEX=" + paramInt1 + " AND DEPT_INDEX=" + paramInt2);
    return (localArrayList != null) && (localArrayList.size() != 0);
  }
  
  public void groupOnDeptOnly(Object[] paramArrayOfObject)
  {
    for (int i = 0; i < paramArrayOfObject.length; i++)
    {
      ProductRow localProductRow1 = (ProductRow)paramArrayOfObject[i];
      for (int j = i + 1; j < paramArrayOfObject.length; j++)
      {
        ProductRow localProductRow2 = (ProductRow)paramArrayOfObject[j];
        if (localProductRow1.getDept() == localProductRow2.getDept())
        {
          ProductRow localProductRow3 = (ProductRow)paramArrayOfObject[(i + 1)];
          paramArrayOfObject[(i + 1)] = localProductRow2;
          paramArrayOfObject[j] = localProductRow3;
          i++;
        }
      }
    }
  }
  
  public void groupOnCatOnly(Object[] paramArrayOfObject)
  {
    for (int i = 0; i < paramArrayOfObject.length; i++)
    {
      ProductRow localProductRow1 = (ProductRow)paramArrayOfObject[i];
      for (int j = i + 1; j < paramArrayOfObject.length; j++)
      {
        ProductRow localProductRow2 = (ProductRow)paramArrayOfObject[j];
        if (localProductRow1.getCategory() == localProductRow2.getCategory())
        {
          ProductRow localProductRow3 = (ProductRow)paramArrayOfObject[(i + 1)];
          paramArrayOfObject[(i + 1)] = localProductRow2;
          paramArrayOfObject[j] = localProductRow3;
          i++;
        }
      }
    }
  }
  
  public ProductRow getProductByCode(String paramString)
    throws DBException
  {
    this._mFndByProductCodeBind[0].value = paramString;
    ArrayList localArrayList = getAllValuesWithWhereClauseWithBind(" PRODUCT_CODE like ? ", this._mFndByProductCodeBind, "PRODUCTDEF_FIND_BY_CODE");
    if ((localArrayList == null) || (localArrayList.size() == 0)) {
      return null;
    }
    return (ProductRow)localArrayList.get(0);
  }
  
  public ProductRow getProductByRfid(String paramString)
    throws DBException
  {
    this._mFndByRfidBind[0].value = paramString;
    ArrayList localArrayList = getAllValuesWithWhereClauseWithBind(" RFID like ? ", this._mFndByRfidBind, "PRODUCTDEF_FIND_BY_RFID");
    if ((localArrayList == null) || (localArrayList.size() == 0)) {
      return null;
    }
    return (ProductRow)localArrayList.get(0);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.ProductTableDef
 * JD-Core Version:    0.7.0.1
 */