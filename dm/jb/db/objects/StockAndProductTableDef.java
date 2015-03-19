package dm.jb.db.objects;

import dm.jb.db.gen.StockAndProductBaseTableDef;
import dm.tools.db.BindObject;
import dm.tools.db.DBConnection;
import dm.tools.db.DBException;
import dm.tools.db.DBRow;
import dm.tools.db.Db;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

public class StockAndProductTableDef
  extends StockAndProductBaseTableDef
{
  private static StockAndProductTableDef _mInstance = null;
  private BindObject[] _mFndByProductCodeBind = { new BindObject(1, 3, "") };
  private BindObject[] _mFndByRfidBind = { new BindObject(1, 3, "") };
  
  public static StockAndProductTableDef getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new StockAndProductTableDef();
    }
    return _mInstance;
  }
  
  public StockAndProductRow getNewRow()
  {
    return new StockAndProductRow(getAttrList().size(), this);
  }
  
  public StockAndProductRow getRowForProductId(int paramInt)
    throws DBException
  {
    return getRowForProductId(paramInt, StoreInfoTableDef.getCurrentStore().getStoreId());
  }
  
  public StockAndProductRow getRowForProductId(int paramInt1, int paramInt2)
    throws DBException
  {
    ArrayList localArrayList = null;
    if (paramInt2 != 0) {
      localArrayList = getAllValuesWithWhereClause("PROD_INDEX=" + paramInt1 + " AND STORE_ID=" + paramInt2);
    } else {
      localArrayList = getAllValuesWithWhereClause("PROD_INDEX=" + paramInt1 + " AND STORE_ID IS NULL");
    }
    if ((localArrayList == null) || (localArrayList.size() == 0)) {
      return null;
    }
    return (StockAndProductRow)localArrayList.get(0);
  }
  
  public StockAndProductRow getRowForProductIdForUpdate(int paramInt)
    throws DBException
  {
    ArrayList localArrayList = getAllValuesWithWhereClauseForUpdate("PRODUCT_ID=" + paramInt + " AND STORE_ID=" + StoreInfoTableDef.getCurrentStore().getStoreId());
    if ((localArrayList == null) || (localArrayList.size() == 0)) {
      return null;
    }
    return (StockAndProductRow)localArrayList.get(0);
  }
  
  public ArrayList<StockAndProductRow> searchStockAndProductByParams(String paramString1, String paramString2, String paramString3, String paramString4, int paramInt)
    throws DBException
  {
    Connection localConnection = Db.getConnection().getJDBCConnection();
    String str1 = prepareSearchStatementForProductSearchWithStoreIndex(paramString1, paramString2, paramString3, paramString4, paramInt);
    try
    {
      Statement localStatement = localConnection.createStatement();
      ResultSet localResultSet = localStatement.executeQuery(str1);
      ArrayList localArrayList = new ArrayList();
      while (localResultSet.next())
      {
        int i = localResultSet.getInt(1);
        int j = localResultSet.getInt(2);
        int k = localResultSet.getInt(3);
        String str2 = localResultSet.getString(4);
        int m = localResultSet.getShort(5);
        double d1 = localResultSet.getDouble(6);
        double d2 = localResultSet.getDouble(7);
        double d3 = localResultSet.getDouble(8);
        int n = localResultSet.getShort(9);
        double d4 = localResultSet.getDouble(10);
        int i1 = localResultSet.getInt(11);
        int i2 = localResultSet.getInt(12);
        int i3 = localResultSet.getInt(13);
        double d5 = localResultSet.getDouble(15);
        double d6 = localResultSet.getDouble(16);
        Date localDate = localResultSet.getDate(17);
        int i4 = localResultSet.getInt(18);
        String str3 = localResultSet.getString(19);
        StockAndProductRow localStockAndProductRow = getNewRow();
        localStockAndProductRow.setProdIndex(i);
        localStockAndProductRow.setCatIndex(j);
        localStockAndProductRow.setDeptIndex(k);
        localStockAndProductRow.setProdName(str2);
        localStockAndProductRow.setProdUnit(m);
        localStockAndProductRow.setUnitPrice(d1);
        localStockAndProductRow.setDiscount(d2);
        localStockAndProductRow.setTax(d3);
        localStockAndProductRow.setTaxUnit(n);
        localStockAndProductRow.setLowStock(d4);
        localStockAndProductRow.setLoyaltyPoints(i1);
        localStockAndProductRow.setRedeemablePoints(i2);
        localStockAndProductRow.setStoreId(i3);
        localStockAndProductRow.setStock(d5);
        localStockAndProductRow.setProductId(i);
        localStockAndProductRow.setPurchasePrice(d6);
        localStockAndProductRow.setExpiry(localDate);
        localStockAndProductRow.setStockIndex(i4);
        localStockAndProductRow.setProductCode(str3);
        localArrayList.add(localStockAndProductRow);
        localStockAndProductRow.readDeptAndCategory();
      }
      return localArrayList;
    }
    catch (SQLException localSQLException)
    {
      localSQLException.printStackTrace();
      throw new DBException("Error creating statement.", "Create statment.", "Contact administrator", null, localSQLException);
    }
  }
  
  private String prepareSearchStatementForProductSearchWithStoreIndex(String paramString1, String paramString2, String paramString3, String paramString4, int paramInt)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    paramString1 = Db.getSearchFormattedString(paramString1);
    if (((paramString2 == null) || (paramString2.length() == 0)) && ((paramString3 == null) || (paramString3.length() == 0)))
    {
      localStringBuffer.append("SELECT PROD_INDEX, CAT_INDEX , DEPT_INDEX, PROD_NAME, PROD_UNIT, UNIT_PRICE, DISCOUNT, TAX, TAX_UNIT, LOW_STOCK, LOYALTY_POINTS, REDEEMABLE_POINTS, STORE_ID, PRODUCT_ID, STOCK, PURCHASE_PRICE, EXPIRY, STOCK_INDEX, PRODUCT_CODE ");
      localStringBuffer.append("FROM STOCK_AND_PRODUCT WHERE PROD_INDEX like '" + paramString1 + "'");
      if ((paramString4 != null) && (paramString4.length() > 0))
      {
        paramString4 = Db.getSearchFormattedString(paramString4);
        localStringBuffer.append(" AND PROD_NAME LIKE '" + paramString4 + "'");
      }
      if (paramInt > 0) {
        localStringBuffer.append(" AND STORE_ID=" + paramInt);
      }
      System.out.println(localStringBuffer.toString());
    }
    else if ((paramString2 == null) || (paramString2.length() == 0))
    {
      localStringBuffer.append("SELECT STP.PROD_INDEX, STP.CAT_INDEX , STP.DEPT_INDEX, STP.PROD_NAME, STP.PROD_UNIT, STP.UNIT_PRICE, STP.DISCOUNT, STP.TAX, STP.TAX_UNIT, STP.LOW_STOCK, STP.LOYALTY_POINTS, STP.REDEEMABLE_POINTS, STP.STORE_ID, STP.PRODUCT_ID, STP.STOCK, STP.PURCHASE_PRICE, STP.EXPIRY, STP.STOCK_INDEX ");
      localStringBuffer.append("FROM STOCK_AND_PRODUCT STP, CATEGORY CAT WHERE STP.PROD_INDEX like '" + paramString1 + "'");
      if ((paramString4 != null) && (paramString4.length() > 0))
      {
        paramString4 = Db.getSearchFormattedString(paramString4);
        localStringBuffer.append(" AND PROD_NAME LIKE '" + paramString4 + "'");
      }
      if (paramInt <= 0) {
        localStringBuffer.append(" AND STORE_ID=" + paramInt);
      }
      paramString3 = Db.getSearchFormattedString(paramString3);
      localStringBuffer.append(" AND CAT.CAT_INDEX = STP.CAT_INDEX AND CAT.CAT_NAME LIKE '" + paramString3 + "'");
    }
    else if ((paramString3 == null) || (paramString3.length() == 0))
    {
      localStringBuffer.append("SELECT STP.PROD_INDEX, STP.CAT_INDEX , STP.DEPT_INDEX, STP.PROD_NAME, STP.PROD_UNIT, STP.UNIT_PRICE, STP.DISCOUNT, STP.TAX, STP.TAX_UNIT, STP.LOW_STOCK, STP.LOYALTY_POINTS, STP.REDEEMABLE_POINTS, STP.STORE_ID, STP.PRODUCT_ID, STP.STOCK, STP.PURCHASE_PRICE, STP.EXPIRY, STP.STOCK_INDEX ");
      localStringBuffer.append("FROM STOCK_AND_PRODUCT STP, DEPT DEPT WHERE PROD_INDEX like '" + paramString1 + "'");
      if ((paramString4 != null) && (paramString4.length() > 0))
      {
        paramString4 = Db.getSearchFormattedString(paramString4);
        localStringBuffer.append(" AND PROD_NAME LIKE '" + paramString4 + "'");
      }
      if (paramInt <= 0) {
        localStringBuffer.append(" AND STORE_ID=" + paramInt);
      }
      paramString2 = Db.getSearchFormattedString(paramString2);
      localStringBuffer.append(" AND DEPT.DEPT_INDEX = STP.DEPT_INDEX AND DEPT.DEPT_NAME LIKE '" + paramString2 + "'");
    }
    else
    {
      localStringBuffer.append("SELECT STP.PROD_INDEX, STP.CAT_INDEX , STP.DEPT_INDEX, STP.PROD_NAME, STP.PROD_UNIT, STP.UNIT_PRICE, STP.DISCOUNT, STP.TAX, STP.TAX_UNIT, STP.LOW_STOCK, STP.LOYALTY_POINTS, STP.REDEEMABLE_POINTS, STP.STORE_ID, STP.PRODUCT_ID, STP.STOCK, STP.PURCHASE_PRICE, STP.EXPIRY, STP.STOCK_INDEX ");
      localStringBuffer.append("FROM STOCK_AND_PRODUCT STP, DEPT DEPT, CATEGORY CAT WHERE PROD_INDEX like '" + paramString1 + "'");
      if ((paramString4 != null) && (paramString4.length() > 0))
      {
        paramString4 = Db.getSearchFormattedString(paramString4);
        localStringBuffer.append(" AND PROD_NAME LIKE '" + paramString4 + "'");
      }
      if (paramInt <= 0) {
        localStringBuffer.append(" AND STORE_ID=" + paramInt);
      }
      paramString2 = Db.getSearchFormattedString(paramString2);
      localStringBuffer.append(" AND DEPT.DEPT_INDEX = STP.DEPT_INDEX AND DEPT.DEPT_NAME LIKE '" + paramString2 + "'");
      paramString3 = Db.getSearchFormattedString(paramString3);
      localStringBuffer.append(" AND CAT.CAT_INDEX = STP.CAT_INDEX AND CAT.CAT_NAME LIKE '" + paramString3 + "'");
    }
    return localStringBuffer.toString();
  }
  
  public ArrayList<StockAndProductRow> getAllValuesWithWhereClauseReturnDeptRow(String paramString)
    throws DBException
  {
    ArrayList localArrayList1 = getAllValuesWithWhereClause(paramString);
    ArrayList localArrayList2 = new ArrayList();
    Iterator localIterator = localArrayList1.iterator();
    while (localIterator.hasNext())
    {
      DBRow localDBRow = (DBRow)localIterator.next();
      StockAndProductRow localStockAndProductRow = (StockAndProductRow)localDBRow;
      localArrayList2.add((StockAndProductRow)localDBRow);
      localStockAndProductRow.readDeptAndCategory();
    }
    localArrayList1.clear();
    localArrayList1 = null;
    return localArrayList2;
  }
  
  public ArrayList<StockAndProductRow> getAllStocksForProduct(int paramInt)
    throws DBException
  {
    ArrayList localArrayList1 = getAllValuesWithWhereClause("PRODUCT_ID=" + paramInt);
    if ((localArrayList1 == null) || (localArrayList1.size() == 0)) {
      return null;
    }
    ArrayList localArrayList2 = new ArrayList();
    Iterator localIterator = localArrayList1.iterator();
    while (localIterator.hasNext())
    {
      DBRow localDBRow = (DBRow)localIterator.next();
      localArrayList2.add((StockAndProductRow)localDBRow);
    }
    localArrayList1.clear();
    localArrayList1 = null;
    return localArrayList2;
  }
  
  public ArrayList<StockAndProductRow> getAllStocksForProduct(String paramString)
    throws DBException
  {
    ArrayList localArrayList1 = getAllValuesWithWhereClause("PRODUCT_Code=" + paramString);
    if ((localArrayList1 == null) || (localArrayList1.size() == 0)) {
      return null;
    }
    ArrayList localArrayList2 = new ArrayList();
    Iterator localIterator = localArrayList1.iterator();
    while (localIterator.hasNext())
    {
      DBRow localDBRow = (DBRow)localIterator.next();
      localArrayList2.add((StockAndProductRow)localDBRow);
    }
    localArrayList1.clear();
    localArrayList1 = null;
    return localArrayList2;
  }
  
  public StockAndProductRow getProductByRfid(String paramString)
    throws DBException
  {
    this._mFndByRfidBind[0].value = paramString;
    ArrayList localArrayList = getAllValuesWithWhereClauseWithBind(" (STORE_ID =" + StoreInfoTableDef.getCurrentStore().getStoreId() + ") AND  RFID like ? ", this._mFndByRfidBind, "STOCKDEF_FIND_BY_RFID");
    if ((localArrayList == null) || (localArrayList.size() == 0)) {
      return null;
    }
    return (StockAndProductRow)localArrayList.get(0);
  }
  
  public StockAndProductRow getProductByCode(String paramString)
    throws DBException
  {
    this._mFndByProductCodeBind[0].value = paramString;
    ArrayList localArrayList = getAllValuesWithWhereClauseWithBind(" (STORE_ID =" + StoreInfoTableDef.getCurrentStore().getStoreId() + ") AND PRODUCT_CODE like ? ", this._mFndByProductCodeBind, "STOCKDEF_FIND_BY_CODE");
    if ((localArrayList == null) || (localArrayList.size() == 0)) {
      return null;
    }
    return (StockAndProductRow)localArrayList.get(0);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.StockAndProductTableDef
 * JD-Core Version:    0.7.0.1
 */