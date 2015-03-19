package dm.jb.db.objects;

import dm.jb.db.gen.StoreStockBaseTableDef;
import dm.tools.db.DBConnection;
import dm.tools.db.DBException;
import dm.tools.db.DBRow;
import dm.tools.db.Db;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class StoreStockTableDef
  extends StoreStockBaseTableDef
{
  private static StoreStockTableDef _mInstance = null;
  
  public static StoreStockTableDef getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new StoreStockTableDef();
    }
    return _mInstance;
  }
  
  public StoreStockRow getNewRow()
  {
    return new StoreStockRow(getAttrList().size(), this);
  }
  
  public StoreStockRow getStockForProductInCurrentStore(int paramInt)
    throws DBException
  {
    return getStockForProductInStore(paramInt, StoreInfoTableDef.getCurrentStore().getStoreId());
  }
  
  public StoreStockRow getStockForProductInStore(int paramInt1, int paramInt2)
    throws DBException
  {
    String str = "PRODUCT_ID = " + paramInt1 + " AND STORE_ID = " + paramInt2;
    ArrayList localArrayList = getAllValuesWithWhereClause(str);
    if ((localArrayList == null) || (localArrayList.size() == 0)) {
      return null;
    }
    return (StoreStockRow)localArrayList.get(0);
  }
  
  public StoreStockRow getStockForProductInStoreForUpdate(int paramInt1, int paramInt2)
    throws DBException
  {
    String str = "PRODUCT_ID = " + paramInt1 + " AND STORE_ID = " + paramInt2;
    ArrayList localArrayList = getAllValuesWithWhereClause(str);
    if ((localArrayList == null) || (localArrayList.size() == 0)) {
      return null;
    }
    return (StoreStockRow)localArrayList.get(0);
  }
  
  public ArrayList<StockAndProductRow> getAllItemsWithLowStockForCats(ArrayList<CategoryRow> paramArrayList, int paramInt, boolean paramBoolean)
    throws DBException
  {
    StringBuffer localStringBuffer = new StringBuffer("");
    localStringBuffer.append("SELECT PROD_INDEX, STORE_ID  ");
    if (paramInt != -1)
    {
      if (paramBoolean) {
        localStringBuffer.append("FROM STOCK_AND_PRODUCT WHERE CAT_INDEX = ? AND ((STORE_ID =? AND LOW_STOCK >= STOCK ) OR (STORE_ID <> ? OR STORE_ID IS NULL))");
      } else {
        localStringBuffer.append("FROM STOCK_AND_PRODUCT WHERE CAT_INDEX = ? AND (STORE_ID =? AND LOW_STOCK >= STOCK ) ");
      }
    }
    else {
      localStringBuffer.append("FROM STOCK_AND_PRODUCT WHERE CAT_INDEX = ? AND  STORE_ID IS NULL");
    }
    Connection localConnection = Db.getConnection().getJDBCConnection();
    ArrayList localArrayList = new ArrayList();
    try
    {
      PreparedStatement localPreparedStatement = localConnection.prepareStatement(localStringBuffer.toString());
      Iterator localIterator = paramArrayList.iterator();
      while (localIterator.hasNext())
      {
        CategoryRow localCategoryRow = (CategoryRow)localIterator.next();
        if (paramBoolean)
        {
          localPreparedStatement.setInt(1, localCategoryRow.getCatIndex());
          localPreparedStatement.setInt(2, paramInt);
          localPreparedStatement.setInt(3, paramInt);
        }
        else
        {
          localPreparedStatement.setInt(1, localCategoryRow.getCatIndex());
          if (paramInt != -1) {
            localPreparedStatement.setInt(2, paramInt);
          }
        }
        ResultSet localResultSet = localPreparedStatement.executeQuery();
        while (localResultSet.next())
        {
          StockAndProductRow localStockAndProductRow = getNextProductFromrResultSet(localResultSet);
          localStockAndProductRow.readDeptAndCategory();
          localArrayList.add(localStockAndProductRow);
        }
      }
      localPreparedStatement.close();
      return localArrayList;
    }
    catch (SQLException localSQLException)
    {
      throw new DBException("SQLException during PrepareStatement.", "PrepareStatement", "Contact Administrator", null, localSQLException);
    }
    catch (DBException localDBException)
    {
      throw localDBException;
    }
  }
  
  public ArrayList<StockAndProductRow> getAllItemsWithLowStockForDepts(ArrayList<DeptRow> paramArrayList, int paramInt, boolean paramBoolean)
    throws DBException
  {
    StringBuffer localStringBuffer = new StringBuffer("");
    localStringBuffer.append("SELECT PROD_INDEX, STORE_ID  ");
    if (paramInt != -1)
    {
      if (paramBoolean) {
        localStringBuffer.append("FROM STOCK_AND_PRODUCT WHERE DEPT_INDEX = ? AND ((STORE_ID =? AND LOW_STOCK >= STOCK ) OR (STORE_ID <> ? OR STORE_ID IS NULL))");
      } else {
        localStringBuffer.append("FROM STOCK_AND_PRODUCT WHERE DEPT_INDEX = ? AND (STORE_ID =? AND LOW_STOCK >= STOCK ) ");
      }
    }
    else {
      localStringBuffer.append("FROM STOCK_AND_PRODUCT WHERE DEPT_INDEX = ? AND STORE_ID IS NULL");
    }
    Connection localConnection = Db.getConnection().getJDBCConnection();
    ArrayList localArrayList = new ArrayList();
    try
    {
      PreparedStatement localPreparedStatement = localConnection.prepareStatement(localStringBuffer.toString());
      Iterator localIterator = paramArrayList.iterator();
      while (localIterator.hasNext())
      {
        DeptRow localDeptRow = (DeptRow)localIterator.next();
        if (paramBoolean)
        {
          localPreparedStatement.setInt(1, localDeptRow.getDeptIndex());
          localPreparedStatement.setInt(2, paramInt);
          localPreparedStatement.setInt(3, paramInt);
        }
        else
        {
          localPreparedStatement.setInt(1, localDeptRow.getDeptIndex());
          if (paramInt != -1) {
            localPreparedStatement.setInt(2, paramInt);
          }
        }
        ResultSet localResultSet = localPreparedStatement.executeQuery();
        while (localResultSet.next())
        {
          StockAndProductRow localStockAndProductRow = getNextProductFromrResultSet(localResultSet);
          localStockAndProductRow.readDeptAndCategory();
          localArrayList.add(localStockAndProductRow);
        }
      }
      localPreparedStatement.close();
      return localArrayList;
    }
    catch (SQLException localSQLException)
    {
      localSQLException.printStackTrace();
      throw new DBException("SQLException during PrepareStatement.", "PrepareStatement", "Contact Administrator", null, localSQLException);
    }
    catch (DBException localDBException)
    {
      throw localDBException;
    }
  }
  
  public ArrayList<StockAndProductRow> getAllItemsWithLowStockForProds(ArrayList<ProductRow> paramArrayList, int paramInt, boolean paramBoolean)
    throws DBException
  {
    StringBuffer localStringBuffer = new StringBuffer("");
    localStringBuffer.append("SELECT PROD_INDEX,STORE_ID  ");
    if (paramBoolean) {
      localStringBuffer.append("FROM STOCK_AND_PRODUCT WHERE PROD_INDEX = ? AND ((STORE_ID =? AND LOW_STOCK >= STOCK ) AND (STORE_ID <> ? OR STORE_ID IS NULL))");
    } else {
      localStringBuffer.append("FROM STOCK_AND_PRODUCT WHERE PROD_INDEX = ? AND (STORE_ID =? AND LOW_STOCK >= STOCK ) ");
    }
    Connection localConnection = Db.getConnection().getJDBCConnection();
    ArrayList localArrayList = new ArrayList();
    try
    {
      PreparedStatement localPreparedStatement = localConnection.prepareStatement(localStringBuffer.toString());
      Iterator localIterator = paramArrayList.iterator();
      while (localIterator.hasNext())
      {
        ProductRow localProductRow = (ProductRow)localIterator.next();
        if (paramBoolean)
        {
          localPreparedStatement.setInt(1, localProductRow.getProdIndex());
          localPreparedStatement.setInt(2, paramInt);
          localPreparedStatement.setInt(3, paramInt);
        }
        else
        {
          localPreparedStatement.setInt(1, localProductRow.getProdIndex());
          localPreparedStatement.setInt(2, paramInt);
        }
        ResultSet localResultSet = localPreparedStatement.executeQuery();
        while (localResultSet.next())
        {
          StockAndProductRow localStockAndProductRow = getNextProductFromrResultSet(localResultSet);
          localStockAndProductRow.readDeptAndCategory();
          localArrayList.add(localStockAndProductRow);
        }
      }
      localPreparedStatement.close();
      return localArrayList;
    }
    catch (SQLException localSQLException)
    {
      throw new DBException("SQLException during PrepareStatement.", "PrepareStatement", "Contact Administrator", null, localSQLException);
    }
    catch (DBException localDBException)
    {
      throw localDBException;
    }
  }
  
  public ArrayList<StoreStockRow> getStocksForProductInAllStores(int paramInt)
    throws DBException
  {
    String str = "PRODUCT_ID = " + paramInt;
    ArrayList localArrayList1 = getAllValuesWithWhereClause(str);
    if ((localArrayList1 == null) || (localArrayList1.size() == 0)) {
      return null;
    }
    ArrayList localArrayList2 = new ArrayList();
    Iterator localIterator = localArrayList1.iterator();
    while (localIterator.hasNext())
    {
      DBRow localDBRow = (DBRow)localIterator.next();
      localArrayList2.add((StoreStockRow)localDBRow);
    }
    localArrayList1.clear();
    localArrayList1 = null;
    return localArrayList2;
  }
  
  private StockAndProductRow getNextProductFromrResultSet(ResultSet paramResultSet)
    throws SQLException, DBException
  {
    int i = paramResultSet.getInt(1);
    int j = paramResultSet.getInt(2);
    return StockAndProductTableDef.getInstance().getRowForProductId(i, j);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.StoreStockTableDef
 * JD-Core Version:    0.7.0.1
 */