package dm.tools.db;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class DBObjectDef
{
  String _mTableName = null;
  ArrayList<String> _mColNames = new ArrayList();
  ArrayList<DBAttribute> _mAttrList = new ArrayList();
  DBAttribute _mIndexAttribute = null;
  DBAttribute _mKeyAttr = null;
  private DBAttribute[] _mKeyAttrs = null;
  private String _mIndexName = null;
  private boolean _mAttributeAdded = false;
  private Statement _mCreateStatement = null;
  private Statement _mCreateStatementWithAuto = null;
  private PreparedStatement _mStatemetForSelectByIndex = null;
  private PreparedStatement _mStatemetForSelectByKeyForUpdate = null;
  private PreparedStatement _mStatemetForSelectByKey = null;
  private PreparedStatement _mStatemenForDelete = null;
  private PreparedStatement _mStatementForSelectAll = null;
  private PreparedStatement _mStatementForFindRowByKey = null;
  private HashMap<String, PreparedStatement> _mStmtMap = new HashMap();
  
  public DBObjectDef(String paramString)
  {
    this._mTableName = paramString;
  }
  
  public void addAttribute(String paramString, short paramShort, boolean paramBoolean)
  {
    DBAttribute localDBAttribute = new DBAttribute(paramString, paramShort, paramBoolean);
    this._mColNames.add(paramString);
    this._mAttrList.add(localDBAttribute);
    this._mAttributeAdded = true;
  }
  
  public void addAttribute(String paramString, short paramShort, boolean paramBoolean1, boolean paramBoolean2)
  {
    DBAttribute localDBAttribute = new DBAttribute(paramString, paramShort, paramBoolean1);
    this._mColNames.add(paramString);
    this._mAttrList.add(localDBAttribute);
    if (paramBoolean2) {
      this._mIndexAttribute = localDBAttribute;
    }
    this._mAttributeAdded = true;
  }
  
  public void addAttribute(String paramString, short paramShort, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    DBAttribute localDBAttribute = new DBAttribute(paramString, paramShort, paramBoolean1, paramBoolean3);
    this._mColNames.add(paramString);
    this._mAttrList.add(localDBAttribute);
    if (paramBoolean2) {
      this._mIndexAttribute = localDBAttribute;
    }
    this._mAttributeAdded = true;
  }
  
  public void addAttribute(String paramString1, short paramShort, boolean paramBoolean, String paramString2)
  {
    DBAttribute localDBAttribute = new DBAttribute(paramString1, paramShort, paramBoolean);
    this._mColNames.add(paramString1);
    this._mAttrList.add(localDBAttribute);
    this._mIndexAttribute = localDBAttribute;
    this._mIndexName = paramString2;
    this._mAttributeAdded = true;
  }
  
  public void setKeyAttr(DBAttribute paramDBAttribute)
  {
    if (paramDBAttribute == null) {
      return;
    }
    this._mKeyAttr = paramDBAttribute;
  }
  
  public void setKeyAttr(String paramString)
  {
    setKeyAttr(getAttributeDefByName(paramString));
  }
  
  public void setIndexAttrs(String[] paramArrayOfString)
    throws DBException
  {
    this._mKeyAttrs = new DBAttribute[paramArrayOfString.length];
    for (int i = 0; i < paramArrayOfString.length; i++)
    {
      DBAttribute localDBAttribute = getAttributeDefByName(paramArrayOfString[i]);
      if (localDBAttribute == null)
      {
        this._mKeyAttrs = null;
        throw new DBException("Attribute definition not ound for column " + paramArrayOfString[i], "DBException.", "Check with developer.", null, null);
      }
      this._mKeyAttrs[i] = localDBAttribute;
    }
  }
  
  public String toString()
  {
    return this._mTableName;
  }
  
  int createRow(DBRow paramDBRow, boolean paramBoolean)
    throws DBException
  {
    if (paramBoolean)
    {
      if (this._mCreateStatementWithAuto == null) {
        createSqlStatementForCreate(true);
      }
    }
    else if (this._mCreateStatement == null) {
      createSqlStatementForCreate(false);
    }
    Iterator localIterator = this._mAttrList.iterator();
    Object[] arrayOfObject = paramDBRow.getData();
    int i = 1;
    int j = 1;
    try
    {
      while (localIterator.hasNext())
      {
        localObject = (DBAttribute)localIterator.next();
        if ((((DBAttribute)localObject).isAutoIncr()) && (!paramBoolean))
        {
          i++;
        }
        else if (arrayOfObject[(i - 1)] == null)
        {
          if (((DBAttribute)localObject).isMandatory()) {
            throw new DBException("Missing value for mandatory attribute " + ((DBAttribute)localObject).getName(), null, null, null, null);
          }
          ((DBAttribute)localObject).bindValue((PreparedStatement)this._mCreateStatement, j, null);
          j++;
          i++;
        }
        else
        {
          PreparedStatement localPreparedStatement = paramBoolean ? (PreparedStatement)this._mCreateStatementWithAuto : (PreparedStatement)this._mCreateStatement;
          ((DBAttribute)localObject).bindValue(localPreparedStatement, j, arrayOfObject[(i - 1)]);
          j++;
          i++;
        }
      }
      ((PreparedStatement)this._mCreateStatement).executeUpdate();
      Object localObject = this._mCreateStatement.getGeneratedKeys();
      int k = -1;
      if (((ResultSet)localObject).next())
      {
        k = ((ResultSet)localObject).getInt(1);
        localIterator = this._mAttrList.iterator();
        for (i = 0; localIterator.hasNext(); i++)
        {
          DBAttribute localDBAttribute = (DBAttribute)localIterator.next();
          if (localDBAttribute.isAutoIncr())
          {
            if (localDBAttribute.getType() == 1)
            {
              arrayOfObject[i] = new Integer(k);
              break;
            }
            if (localDBAttribute.getType() != 7) {
              break;
            }
            arrayOfObject[i] = new Long(k);
            break;
          }
        }
      }
      paramDBRow.resaveKeyvalues();
      paramDBRow.clearAllAttributesChanged();
      return k;
    }
    catch (SQLException localSQLException)
    {
      localSQLException.printStackTrace();
      throw new DBException("SQLException creating row for table " + this._mTableName, "SQLException.", "Check with developer.", null, localSQLException);
    }
  }
  
  private void createSqlStatementForCreate(boolean paramBoolean)
    throws DBException
  {
    String str = getSqlStringForInsert(paramBoolean);
    if (!paramBoolean) {
      this._mCreateStatement = Db.getConnection().createPreparedStatement(str, true);
    } else {
      this._mCreateStatementWithAuto = Db.getConnection().createPreparedStatement(str, true);
    }
  }
  
  public DBRow getNewRow()
  {
    return new DBRow(this._mAttrList.size(), this);
  }
  
  public ArrayList<DBAttribute> getAttrList()
  {
    return this._mAttrList;
  }
  
  public DBRow findRowByKey(Object paramObject)
    throws DBException
  {
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = paramObject;
    return findRowByKey(arrayOfObject);
  }
  
  public DBRow findRowByKey(Object[] paramArrayOfObject)
    throws DBException
  {
    if (this._mStatementForFindRowByKey == null)
    {
      localObject = getSqlStringForFindByKey(this._mKeyAttrs);
      this._mStatementForFindRowByKey = Db.getConnection().createPreparedStatement((String)localObject, true);
    }
    Object localObject = null;
    try
    {
      for (int i = 0; i < paramArrayOfObject.length; i++) {
        this._mStatementForFindRowByKey.setObject(i + 1, paramArrayOfObject[i]);
      }
      ResultSet localResultSet = this._mStatementForFindRowByKey.executeQuery();
      boolean bool = localResultSet.next();
      if (!bool) {
        return null;
      }
      localObject = getNewRow();
      storeValueFromResultSet(localResultSet, (DBRow)localObject);
      ((DBRow)localObject).clearAllAttributesChanged();
    }
    catch (SQLException localSQLException)
    {
      throw new DBException("SQLException fetching the row with index for table " + this._mTableName, localSQLException.toString(), "Check the input values", null, localSQLException);
    }
    return localObject;
  }
  
  protected boolean findRowByKeyAndFillValues(String paramString, Object paramObject, DBRow paramDBRow)
    throws DBException
  {
    if (this._mStatemetForSelectByKey == null) {
      this._mStatemetForSelectByKey = Db.getConnection().createPreparedStatement(getSqlStringForFindByKey(paramString));
    }
    DBAttribute localDBAttribute = getAttributeDefByName(paramString);
    if (localDBAttribute == null) {
      throw new DBException("Attribute not found for the specified column.", "Finding attribute by key column", "Contact developer", null, null);
    }
    try
    {
      localDBAttribute.bindValue(this._mStatemetForSelectByKey, 1, paramObject);
      ResultSet localResultSet = this._mStatemetForSelectByKey.executeQuery();
      boolean bool = localResultSet.next();
      if (!bool) {
        return false;
      }
      storeValueFromResultSet(localResultSet, paramDBRow);
      paramDBRow.clearAllAttributesChanged();
    }
    catch (SQLException localSQLException)
    {
      localSQLException.printStackTrace();
      throw new DBException("SQLException fetching the row with key for table " + this._mTableName, localSQLException.toString(), "Check the input values", null, localSQLException);
    }
    return true;
  }
  
  protected boolean findRowByIndexAndFillValues(int paramInt, DBRow paramDBRow)
    throws DBException
  {
    if (this._mIndexAttribute == null) {
      throw new DBException("Index attribute is not defined.", "findRowByIndexAndFillValues  called with no index attribute", "Programming error.", null, null);
    }
    if (this._mStatemetForSelectByIndex == null) {
      this._mStatemetForSelectByIndex = Db.getConnection().createPreparedStatement(getSqlStringForFindByIndex());
    }
    try
    {
      this._mStatemetForSelectByIndex.setInt(1, paramInt);
      ResultSet localResultSet = this._mStatemetForSelectByIndex.executeQuery();
      boolean bool = localResultSet.next();
      if (!bool) {
        return false;
      }
      storeValueFromResultSet(localResultSet, paramDBRow);
      paramDBRow.clearAllAttributesChanged();
    }
    catch (SQLException localSQLException)
    {
      localSQLException.printStackTrace();
      throw new DBException("SQLException fetching the row with index for table " + this._mTableName, localSQLException.toString(), "Check the input values", null, localSQLException);
    }
    return true;
  }
  
  private void updateInternalForAttrs(DBRow paramDBRow)
    throws DBException
  {
    if ((this._mKeyAttrs == null) || (this._mKeyAttrs.length == 0)) {
      throw new DBException("Key attribute and index attribute is not available.", "Update call", "Check with developer", null, null);
    }
    PreparedStatement localPreparedStatement = null;
    ArrayList localArrayList = getUpdatedAttributeList(paramDBRow);
    if ((localArrayList == null) || (localArrayList.size() == 0)) {
      return;
    }
    String str = getSqlForUpdate(localArrayList, this._mKeyAttrs);
    localPreparedStatement = Db.getConnection().createPreparedStatement(str);
    Iterator localIterator = localArrayList.iterator();
    int i = 1;
    try
    {
      while (localIterator.hasNext())
      {
        DBAttribute localDBAttribute = (DBAttribute)localIterator.next();
        localDBAttribute.bindValue(localPreparedStatement, i, paramDBRow.getValue(localDBAttribute.getName()));
        i++;
      }
      for (int j = 0; j < this._mKeyAttrs.length; j++)
      {
        this._mKeyAttrs[j].bindValue(localPreparedStatement, i, paramDBRow.getValue(this._mKeyAttrs[j].getName()));
        i++;
      }
    }
    catch (SQLException localSQLException1)
    {
      localSQLException1.printStackTrace();
      throw new DBException("SQLException binding values", "Bind values for update.", "Check with developer", null, localSQLException1);
    }
    try
    {
      localPreparedStatement.executeUpdate();
      localPreparedStatement.close();
    }
    catch (SQLException localSQLException2)
    {
      throw new DBException("SQLException while updating row", "Update error", "Chekc the developer", null, localSQLException2);
    }
  }
  
  public void update(boolean paramBoolean, DBRow paramDBRow)
    throws DBException
  {
    if ((!paramBoolean) || (this._mKeyAttrs != null))
    {
      updateInternalForAttrs(paramDBRow);
      return;
    }
    PreparedStatement localPreparedStatement = null;
    ArrayList localArrayList = getUpdatedAttributeList(paramDBRow);
    String str = getSqlForUpdate(localArrayList, this._mKeyAttrs);
    if (str == null) {
      return;
    }
    localPreparedStatement = Db.getConnection().createPreparedStatement(str);
    Iterator localIterator = localArrayList.iterator();
    int i = 1;
    try
    {
      while (localIterator.hasNext())
      {
        DBAttribute localDBAttribute = (DBAttribute)localIterator.next();
        localDBAttribute.bindValue(localPreparedStatement, i, paramDBRow.getValue(localDBAttribute.getName()));
        i++;
      }
      if (this._mIndexAttribute != null) {
        this._mIndexAttribute.bindValue(localPreparedStatement, i, paramDBRow.getIndexValue());
      } else if (this._mKeyAttr != null) {
        this._mKeyAttr.bindValue(localPreparedStatement, i, paramDBRow.getKeyValue());
      }
    }
    catch (SQLException localSQLException1)
    {
      throw new DBException("SQLException binding values", "Bind values for update.", "Check with developer", null, localSQLException1);
    }
    try
    {
      localPreparedStatement.executeUpdate();
      localPreparedStatement.close();
    }
    catch (SQLException localSQLException2)
    {
      throw new DBException("SQLException " + localSQLException2.toString() + " while updating row", "Update error", "Chekc the developer", null, localSQLException2);
    }
    if (this._mKeyAttr != null) {
      paramDBRow.setKeyValue(paramDBRow.getValue(this._mKeyAttr.getName()));
    }
    if (this._mIndexAttribute != null) {
      paramDBRow.setIndexValue(paramDBRow.getValue(this._mIndexAttribute.getName()));
    }
  }
  
  void delete(DBRow paramDBRow)
    throws DBException
  {
    if ((this._mIndexAttribute == null) && (this._mKeyAttr == null) && ((this._mKeyAttrs == null) || (this._mKeyAttrs.length == 0))) {
      throw new DBException("Key attribute and Index attribute is not present.", "Delete without key or index attribute", "Contact developer", null, null);
    }
    if (this._mStatemenForDelete == null)
    {
      String str;
      if ((this._mIndexAttribute != null) || (this._mKeyAttr != null))
      {
        str = getDeleteSQLForAttribute(this._mIndexAttribute != null ? this._mIndexAttribute : this._mKeyAttr);
        this._mStatemenForDelete = Db.getConnection().createPreparedStatement(str);
      }
      else
      {
        str = getDeleteSQLForAttribute(this._mKeyAttrs);
        this._mStatemenForDelete = Db.getConnection().createPreparedStatement(str);
      }
    }
    try
    {
      if (this._mIndexAttribute != null)
      {
        this._mIndexAttribute.bindValue(this._mStatemenForDelete, 1, paramDBRow.getValue(this._mIndexAttribute.getName()));
      }
      else if (this._mKeyAttr != null)
      {
        this._mKeyAttr.bindValue(this._mStatemenForDelete, 1, paramDBRow.getValue(this._mKeyAttr.getName()));
      }
      else
      {
        int i = 1;
        for (DBAttribute localDBAttribute : this._mKeyAttrs)
        {
          localDBAttribute.bindValue(this._mStatemenForDelete, i, paramDBRow.getValue(localDBAttribute.getName()));
          i++;
        }
      }
    }
    catch (SQLException localSQLException1)
    {
      throw new DBException("SQLException binding values for delete.", "Binding for delete with index", "Contact developer.", null, null);
    }
    try
    {
      this._mStatemenForDelete.executeUpdate();
    }
    catch (SQLException localSQLException2)
    {
      System.err.println(localSQLException2);
      throw new DBException("SQLException executing delete.", "Execute for delete with index attribute", "Contact developer", null, localSQLException2);
    }
  }
  
  public void deleteWithWhere(String paramString, BindObject[] paramArrayOfBindObject)
    throws DBException
  {
    PreparedStatement localPreparedStatement = null;
    try
    {
      localPreparedStatement = Db.getConnection().createPreparedStatement(getDeleteSQLWithWhereClause(paramString));
      for (int i = 0; i < paramArrayOfBindObject.length; i++) {
        setBindWithType(paramArrayOfBindObject[i], localPreparedStatement);
      }
      localPreparedStatement.executeUpdate();
    }
    catch (SQLException localSQLException)
    {
      throw new DBException("SQLException executing delete.", "Execute for delete with bind values.", "Contact developer", null, localSQLException);
    }
  }
  
  public void deleteWithWhere(String paramString)
    throws DBException
  {
    String str = getDeleteSQLWithWhereClause(paramString);
    PreparedStatement localPreparedStatement = Db.getConnection().createPreparedStatement(str);
    try
    {
      localPreparedStatement.executeUpdate();
    }
    catch (SQLException localSQLException1)
    {
      throw new DBException("SQLException executing delete.", "Execute for delete with index attribute", "Contact developer", null, localSQLException1);
    }
    try
    {
      localPreparedStatement.close();
    }
    catch (SQLException localSQLException2)
    {
      localSQLException2.printStackTrace();
    }
  }
  
  private ArrayList<DBAttribute> getUpdatedAttributeList(DBRow paramDBRow)
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = this._mAttrList.iterator();
    for (int i = 0; localIterator.hasNext(); i++)
    {
      DBAttribute localDBAttribute = (DBAttribute)localIterator.next();
      if (paramDBRow.isAttributeUpdated(i)) {
        localArrayList.add(localDBAttribute);
      }
    }
    return localArrayList;
  }
  
  private String getSqlStringForFindByIndex()
  {
    StringBuffer localStringBuffer = new StringBuffer("SELECT ");
    Iterator localIterator = this._mColNames.iterator();
    String str = (String)localIterator.next();
    localStringBuffer.append(str);
    while (localIterator.hasNext())
    {
      str = (String)localIterator.next();
      localStringBuffer.append(", " + str);
    }
    localStringBuffer.append(" FROM ");
    localStringBuffer.append(this._mTableName);
    localStringBuffer.append(" WHERE ");
    if (this._mIndexAttribute != null) {
      localStringBuffer.append(this._mIndexAttribute.getName());
    } else {
      localStringBuffer.append(this._mKeyAttr.getName());
    }
    localStringBuffer.append("=?");
    return localStringBuffer.toString();
  }
  
  private String getSqlStringForFindByIndexForUpdate(DBAttribute paramDBAttribute)
  {
    StringBuffer localStringBuffer = new StringBuffer("SELECT ");
    Iterator localIterator = this._mColNames.iterator();
    String str = (String)localIterator.next();
    localStringBuffer.append(str);
    while (localIterator.hasNext())
    {
      str = (String)localIterator.next();
      localStringBuffer.append(", " + str);
    }
    localStringBuffer.append(" FROM ");
    localStringBuffer.append(this._mTableName);
    localStringBuffer.append(" WHERE ");
    localStringBuffer.append(paramDBAttribute.getName());
    localStringBuffer.append("=?");
    localStringBuffer.append(" FOR UPDATE");
    return localStringBuffer.toString();
  }
  
  private String getSqlStringForFindByKey(DBAttribute[] paramArrayOfDBAttribute)
  {
    StringBuffer localStringBuffer = new StringBuffer("SELECT ");
    Iterator localIterator = this._mColNames.iterator();
    String str = (String)localIterator.next();
    localStringBuffer.append(str);
    while (localIterator.hasNext())
    {
      str = (String)localIterator.next();
      localStringBuffer.append(", " + str);
    }
    localStringBuffer.append(" FROM ");
    localStringBuffer.append(this._mTableName);
    localStringBuffer.append(" WHERE ");
    DBAttribute localDBAttribute = paramArrayOfDBAttribute[0];
    localStringBuffer.append(localDBAttribute.getName() + "=? ");
    for (int i = 1; i < paramArrayOfDBAttribute.length; i++)
    {
      localDBAttribute = paramArrayOfDBAttribute[i];
      localStringBuffer.append("AND ");
      localStringBuffer.append(localDBAttribute.getName() + "=? ");
    }
    localStringBuffer.append(" FOR UPDATE");
    return localStringBuffer.toString();
  }
  
  private String getSqlStringForFindByIndexForUpdate(DBAttribute[] paramArrayOfDBAttribute)
  {
    StringBuffer localStringBuffer = new StringBuffer("SELECT ");
    Iterator localIterator = this._mColNames.iterator();
    String str = (String)localIterator.next();
    localStringBuffer.append(str);
    while (localIterator.hasNext())
    {
      str = (String)localIterator.next();
      localStringBuffer.append(", " + str);
    }
    localStringBuffer.append(" FROM ");
    localStringBuffer.append(this._mTableName);
    localStringBuffer.append(" WHERE ");
    DBAttribute localDBAttribute = paramArrayOfDBAttribute[0];
    localStringBuffer.append(localDBAttribute.getName() + "=? ");
    for (int i = 1; i < paramArrayOfDBAttribute.length; i++)
    {
      localDBAttribute = paramArrayOfDBAttribute[i];
      localStringBuffer.append("AND ");
      localStringBuffer.append(localDBAttribute.getName() + "=? ");
    }
    localStringBuffer.append(" FOR UPDATE");
    return localStringBuffer.toString();
  }
  
  private String getSqlStringForFindByKey(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer("SELECT ");
    Iterator localIterator = this._mColNames.iterator();
    String str = (String)localIterator.next();
    localStringBuffer.append(str);
    while (localIterator.hasNext())
    {
      str = (String)localIterator.next();
      localStringBuffer.append(", " + str);
    }
    localStringBuffer.append(" FROM ");
    localStringBuffer.append(this._mTableName);
    localStringBuffer.append(" WHERE ");
    localStringBuffer.append(paramString);
    localStringBuffer.append("=?");
    return localStringBuffer.toString();
  }
  
  private String getSqlStringForInsert(boolean paramBoolean)
  {
    StringBuffer localStringBuffer1 = new StringBuffer("INSERT INTO ");
    localStringBuffer1.append(this._mTableName);
    localStringBuffer1.append(" ");
    Iterator localIterator = this._mAttrList.iterator();
    StringBuffer localStringBuffer2 = new StringBuffer("(");
    StringBuffer localStringBuffer3 = new StringBuffer(" VALUES(");
    DBAttribute localDBAttribute = (DBAttribute)localIterator.next();
    if (!paramBoolean) {
      while (localDBAttribute.isAutoIncr()) {
        localDBAttribute = (DBAttribute)localIterator.next();
      }
    }
    localStringBuffer2.append(localDBAttribute.getName());
    localStringBuffer3.append("?");
    while (localIterator.hasNext())
    {
      localDBAttribute = (DBAttribute)localIterator.next();
      if ((paramBoolean) || (!localDBAttribute.isAutoIncr()))
      {
        localStringBuffer2.append(",");
        localStringBuffer2.append(localDBAttribute.getName());
        localStringBuffer3.append(" ,?");
      }
    }
    localStringBuffer2.append(")");
    localStringBuffer3.append(")");
    localStringBuffer1.append(localStringBuffer2);
    localStringBuffer1.append(" ");
    localStringBuffer1.append(localStringBuffer3);
    return localStringBuffer1.toString();
  }
  
  private String getSqlForUpdate(ArrayList<DBAttribute> paramArrayList, DBAttribute[] paramArrayOfDBAttribute)
  {
    StringBuffer localStringBuffer = new StringBuffer("UPDATE " + this._mTableName + " SET ");
    Iterator localIterator = paramArrayList.iterator();
    if (!localIterator.hasNext()) {
      return null;
    }
    DBAttribute localDBAttribute1 = (DBAttribute)localIterator.next();
    localStringBuffer.append(localDBAttribute1.getName() + " = ? ");
    while (localIterator.hasNext())
    {
      localDBAttribute1 = (DBAttribute)localIterator.next();
      localStringBuffer.append(", " + localDBAttribute1.getName() + " = ? ");
    }
    localStringBuffer.append("WHERE ");
    if ((paramArrayOfDBAttribute != null) && (paramArrayOfDBAttribute.length > 0))
    {
      DBAttribute localDBAttribute2 = paramArrayOfDBAttribute[0];
      localStringBuffer.append(localDBAttribute2.getName() + "=? ");
      for (int i = 1; i < paramArrayOfDBAttribute.length; i++)
      {
        localDBAttribute2 = paramArrayOfDBAttribute[i];
        localStringBuffer.append("AND ");
        localStringBuffer.append(localDBAttribute2.getName() + "=? ");
      }
    }
    else if (this._mIndexAttribute != null)
    {
      localStringBuffer.append(this._mIndexAttribute.getName() + "=? ");
    }
    else if (this._mKeyAttr != null)
    {
      localStringBuffer.append(this._mKeyAttr.getName() + "=? ");
    }
    return localStringBuffer.toString();
  }
  
  private String getSqlStringForSelectAll()
  {
    StringBuffer localStringBuffer1 = new StringBuffer("SELECT ");
    Iterator localIterator = this._mAttrList.iterator();
    StringBuffer localStringBuffer2 = new StringBuffer(" ");
    DBAttribute localDBAttribute = (DBAttribute)localIterator.next();
    localStringBuffer2.append(localDBAttribute.getName());
    while (localIterator.hasNext())
    {
      localDBAttribute = (DBAttribute)localIterator.next();
      localStringBuffer2.append(",");
      localStringBuffer2.append(localDBAttribute.getName());
    }
    localStringBuffer2.append(" ");
    localStringBuffer1.append(localStringBuffer2);
    localStringBuffer1.append(" FROM " + this._mTableName);
    return localStringBuffer1.toString();
  }
  
  private String getDeleteSQLForAttribute(DBAttribute paramDBAttribute)
  {
    StringBuffer localStringBuffer = new StringBuffer("DELETE FROM " + this._mTableName);
    localStringBuffer.append(" WHERE ");
    localStringBuffer.append(paramDBAttribute.getName() + "=? ");
    return localStringBuffer.toString();
  }
  
  private String getDeleteSQLForAttribute(DBAttribute[] paramArrayOfDBAttribute)
  {
    StringBuffer localStringBuffer = new StringBuffer("DELETE FROM " + this._mTableName);
    localStringBuffer.append(" WHERE ");
    DBAttribute localDBAttribute = paramArrayOfDBAttribute[0];
    localStringBuffer.append(localDBAttribute.getName() + "=? ");
    for (int i = 1; i < paramArrayOfDBAttribute.length; i++)
    {
      localStringBuffer.append("AND ");
      localStringBuffer.append(paramArrayOfDBAttribute[i].getName() + "=? ");
    }
    return localStringBuffer.toString();
  }
  
  private String getDeleteSQLWithWhereClause(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer("DELETE FROM " + this._mTableName);
    localStringBuffer.append(" WHERE " + paramString);
    return localStringBuffer.toString();
  }
  
  public ArrayList<DBRow> getAllValues()
    throws DBException
  {
    if (this._mStatementForSelectAll == null) {
      this._mStatementForSelectAll = Db.getConnection().createPreparedStatement(getSqlStringForSelectAll());
    }
    ArrayList localArrayList = new ArrayList();
    try
    {
      ResultSet localResultSet = this._mStatementForSelectAll.executeQuery();
      boolean bool = localResultSet.next();
      while (bool)
      {
        DBRow localDBRow = getNewRow();
        storeValueFromResultSet(localResultSet, localDBRow);
        bool = localResultSet.next();
        localArrayList.add(localDBRow);
      }
    }
    catch (SQLException localSQLException)
    {
      System.err.println(localSQLException);
      throw new DBException("SQLException querying all the rows.", "SQLException", "Check the exception", null, localSQLException);
    }
    return localArrayList;
  }
  
  public ArrayList<DBRow> getAllValuesWithWhereClause(String paramString)
    throws DBException
  {
    Statement localStatement = Db.getConnection().createStatement();
    ArrayList localArrayList = new ArrayList();
    try
    {
      ResultSet localResultSet = localStatement.executeQuery(getSqlForSelectWithWhere(paramString));
      boolean bool = localResultSet.next();
      while (bool)
      {
        DBRow localDBRow = getNewRow();
        storeValueFromResultSet(localResultSet, localDBRow);
        bool = localResultSet.next();
        localArrayList.add(localDBRow);
      }
      localStatement.close();
    }
    catch (SQLException localSQLException1)
    {
      System.err.println(localSQLException1);
      throw new DBException("SQLException querying all the rows with where clause.", "SQLException", "Check the exception", null, localSQLException1);
    }
    try
    {
      localStatement.close();
    }
    catch (SQLException localSQLException2)
    {
      localSQLException2.printStackTrace();
    }
    return localArrayList;
  }
  
  public ArrayList<DBRow> getAllValuesWithWhereClauseForUpdate(String paramString)
    throws DBException
  {
    PreparedStatement localPreparedStatement = null;
    localPreparedStatement = Db.getConnection().createPreparedStatement(getSqlForSelectWithWhere(paramString) + " FOR UPDATE");
    ArrayList localArrayList = new ArrayList();
    try
    {
      ResultSet localResultSet = localPreparedStatement.executeQuery();
      boolean bool = localResultSet.first();
      while (bool)
      {
        DBRow localDBRow = getNewRow();
        storeValueFromResultSet(localResultSet, localDBRow);
        bool = localResultSet.next();
        localArrayList.add(localDBRow);
      }
      localPreparedStatement.close();
    }
    catch (SQLException localSQLException)
    {
      System.err.println(localSQLException);
      throw new DBException("SQLException querying all the rows with where clause.", "SQLException", "Check the exception", null, localSQLException);
    }
    return localArrayList;
  }
  
  public ArrayList<DBRow> getAllValuesWithWhereClauseWithBindForUpdate(String paramString, BindObject[] paramArrayOfBindObject)
    throws DBException
  {
    return getAllValuesWithWhereClauseWithBindForUpdate(paramString, paramArrayOfBindObject, null);
  }
  
  public ArrayList<DBRow> getAllValuesWithWhereClauseWithBindForUpdate(String paramString1, BindObject[] paramArrayOfBindObject, String paramString2)
    throws DBException
  {
    PreparedStatement localPreparedStatement = null;
    if (paramString2 != null) {
      localPreparedStatement = (PreparedStatement)this._mStmtMap.get(paramString2);
    }
    if (localPreparedStatement == null)
    {
      localPreparedStatement = Db.getConnection().createPreparedStatement(getSqlForSelectWithWhere(paramString1) + " FOR UPDATE");
      if (paramString2 != null) {
        this._mStmtMap.put(paramString2, localPreparedStatement);
      }
    }
    ArrayList localArrayList = new ArrayList();
    try
    {
      for (int i = 0; i < paramArrayOfBindObject.length; i++) {
        localPreparedStatement.setObject(paramArrayOfBindObject[i].index, paramArrayOfBindObject[i].value);
      }
      ResultSet localResultSet = localPreparedStatement.executeQuery();
      boolean bool = localResultSet.first();
      while (bool)
      {
        DBRow localDBRow = getNewRow();
        storeValueFromResultSet(localResultSet, localDBRow);
        bool = localResultSet.next();
        localArrayList.add(localDBRow);
      }
    }
    catch (SQLException localSQLException1)
    {
      System.err.println(localSQLException1);
      throw new DBException("SQLException querying all the rows with where clause.", "SQLException", "Check the exception", null, localSQLException1);
    }
    try
    {
      if (paramString2 == null) {
        localPreparedStatement.close();
      }
    }
    catch (SQLException localSQLException2)
    {
      localSQLException2.printStackTrace();
    }
    return localArrayList;
  }
  
  public ArrayList<DBRow> getAllValuesWithWhereClauseWithBind(String paramString, BindObject[] paramArrayOfBindObject)
    throws DBException
  {
    return getAllValuesWithWhereClauseWithBind(paramString, paramArrayOfBindObject, null);
  }
  
  public ArrayList<DBRow> getAllValuesWithWhereClauseWithBind(String paramString1, BindObject[] paramArrayOfBindObject, String paramString2)
    throws DBException
  {
    PreparedStatement localPreparedStatement = null;
    if (paramString2 != null) {
      localPreparedStatement = (PreparedStatement)this._mStmtMap.get(paramString2);
    }
    if (localPreparedStatement == null)
    {
      localPreparedStatement = Db.getConnection().createPreparedStatement(getSqlForSelectWithWhere(paramString1));
      if (paramString2 != null) {
        this._mStmtMap.put(paramString2, localPreparedStatement);
      }
    }
    ArrayList localArrayList = new ArrayList();
    try
    {
      for (int i = 0; i < paramArrayOfBindObject.length; i++) {
        localPreparedStatement.setObject(paramArrayOfBindObject[i].index, paramArrayOfBindObject[i].value);
      }
      ResultSet localResultSet = localPreparedStatement.executeQuery();
      boolean bool = localResultSet.first();
      while (bool)
      {
        DBRow localDBRow = getNewRow();
        storeValueFromResultSet(localResultSet, localDBRow);
        bool = localResultSet.next();
        localArrayList.add(localDBRow);
      }
      localResultSet.close();
    }
    catch (SQLException localSQLException1)
    {
      System.err.println(localSQLException1);
      throw new DBException("SQLException querying all the rows with where clause.", "SQLException", "Check the exception", null, localSQLException1);
    }
    try
    {
      if (paramString2 == null) {
        localPreparedStatement.close();
      }
    }
    catch (SQLException localSQLException2)
    {
      localSQLException2.printStackTrace();
    }
    return localArrayList;
  }
  
  public String getSqlForSelectWithWhere(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer(getSqlStringForSelectAll());
    localStringBuffer.append(" WHERE ").append(paramString);
    return localStringBuffer.toString();
  }
  
  public DBRow findRowByIndex(long paramLong)
    throws DBException
  {
    if ((this._mIndexAttribute == null) && (this._mKeyAttr == null)) {
      throw new DBException("Index attribute is not defined.", "findRowByIndexAndFillValues  called with no index attribute", "Programming error.", null, null);
    }
    if (this._mStatemetForSelectByIndex == null) {
      this._mStatemetForSelectByIndex = Db.getConnection().createPreparedStatement(getSqlStringForFindByIndex());
    }
    DBRow localDBRow = null;
    try
    {
      this._mStatemetForSelectByIndex.setLong(1, paramLong);
      ResultSet localResultSet = this._mStatemetForSelectByIndex.executeQuery();
      boolean bool = localResultSet.next();
      if (!bool) {
        return null;
      }
      localDBRow = getNewRow();
      storeValueFromResultSet(localResultSet, localDBRow);
      localDBRow.clearAllAttributesChanged();
    }
    catch (SQLException localSQLException)
    {
      throw new DBException("SQLException fetching the row with index for table " + this._mTableName, localSQLException.toString(), "Check the input values", null, localSQLException);
    }
    return localDBRow;
  }
  
  public DBRow findRowByIndex(int paramInt)
    throws DBException
  {
    if ((this._mIndexAttribute == null) && (this._mKeyAttr == null)) {
      throw new DBException("Index attribute is not defined.", "findRowByIndexAndFillValues  called with no index attribute", "Programming error.", null, null);
    }
    if (this._mStatemetForSelectByIndex == null) {
      this._mStatemetForSelectByIndex = Db.getConnection().createPreparedStatement(getSqlStringForFindByIndex());
    }
    DBRow localDBRow = null;
    try
    {
      this._mStatemetForSelectByIndex.setInt(1, paramInt);
      ResultSet localResultSet = this._mStatemetForSelectByIndex.executeQuery();
      boolean bool = localResultSet.next();
      if (!bool) {
        return null;
      }
      localDBRow = getNewRow();
      storeValueFromResultSet(localResultSet, localDBRow);
      localDBRow.clearAllAttributesChanged();
    }
    catch (SQLException localSQLException)
    {
      throw new DBException("SQLException fetching the row with index for table " + this._mTableName, localSQLException.toString(), "Check the input values", null, localSQLException);
    }
    return localDBRow;
  }
  
  public DBRow findRowByIndexForUpdate(int paramInt)
    throws DBException
  {
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Integer.valueOf(paramInt);
    return findRowByIndexForUpdate(arrayOfObject);
  }
  
  public DBRow findRowByIndexForUpdate(Object[] paramArrayOfObject)
    throws DBException
  {
    if ((this._mIndexAttribute == null) && (this._mKeyAttr == null) && ((this._mKeyAttrs == null) || (this._mKeyAttrs.length == 0))) {
      throw new DBException("Index attribute is not defined.", "findRowByIndexAndFillValues  called with no index attribute", "Programming error.", null, null);
    }
    if (this._mStatemetForSelectByKeyForUpdate == null)
    {
      localObject1 = null;
      if ((this._mIndexAttribute != null) || (this._mKeyAttr != null)) {
        localObject1 = getSqlStringForFindByIndexForUpdate(this._mIndexAttribute != null ? this._mIndexAttribute : this._mKeyAttr);
      } else {
        localObject1 = getSqlStringForFindByIndexForUpdate(this._mKeyAttrs);
      }
      this._mStatemetForSelectByKeyForUpdate = Db.getConnection().createPreparedStatement((String)localObject1);
    }
    Object localObject1 = null;
    try
    {
      int i = 1;
      for (Object localObject3 : paramArrayOfObject)
      {
        this._mStatemetForSelectByKeyForUpdate.setObject(i, localObject3);
        i++;
      }
      ??? = this._mStatemetForSelectByKeyForUpdate.executeQuery();
      boolean bool = ((ResultSet)???).first();
      if (!bool) {
        return null;
      }
      localObject1 = getNewRow();
      storeValueFromResultSet((ResultSet)???, (DBRow)localObject1);
      ((DBRow)localObject1).clearAllAttributesChanged();
    }
    catch (SQLException localSQLException)
    {
      throw new DBException("SQLException fetching the row with index for table " + this._mTableName, localSQLException.toString(), "Check the input values", null, localSQLException);
    }
    return localObject1;
  }
  
  private void storeValueFromResultSet(ResultSet paramResultSet, DBRow paramDBRow)
    throws SQLException
  {
    Iterator localIterator = this._mAttrList.iterator();
    for (int i = 1; localIterator.hasNext(); i++)
    {
      DBAttribute localDBAttribute = (DBAttribute)localIterator.next();
      Object localObject = localDBAttribute.getValueFromResultSet(i, paramResultSet);
      paramDBRow.setValueFromDB(localDBAttribute.getName(), localObject);
    }
    paramDBRow.resaveKeyvalues();
  }
  
  public DBAttribute getAttributeDefByName(String paramString)
  {
    Iterator localIterator1 = this._mColNames.iterator();
    Iterator localIterator2 = this._mAttrList.iterator();
    while (localIterator1.hasNext())
    {
      String str = (String)localIterator1.next();
      DBAttribute localDBAttribute = (DBAttribute)localIterator2.next();
      if (str.equals(paramString)) {
        return localDBAttribute;
      }
    }
    return null;
  }
  
  public void removeStatementsFromMap(String paramString)
  {
    PreparedStatement localPreparedStatement = (PreparedStatement)this._mStmtMap.remove(paramString);
    if (localPreparedStatement != null) {
      try
      {
        localPreparedStatement.close();
      }
      catch (SQLException localSQLException)
      {
        localSQLException.printStackTrace();
      }
    }
  }
  
  public void deleteAllRows()
    throws DBException
  {
    Statement localStatement = null;
    try
    {
      localStatement = Db.getConnection().getJDBCConnection().createStatement();
      localStatement.executeUpdate("DELETE FROM " + this._mTableName);
      localStatement.close();
    }
    catch (SQLException localSQLException1)
    {
      try
      {
        localStatement.close();
      }
      catch (SQLException localSQLException2)
      {
        localSQLException1.printStackTrace();
      }
      throw new DBException("SQL Exception removing all rows.", "Delete all rows", "Contact administrator", null, localSQLException1);
    }
  }
  
  private void setBindWithType(BindObject paramBindObject, PreparedStatement paramPreparedStatement)
    throws SQLException
  {
    switch (paramBindObject.type)
    {
    case 7: 
      paramPreparedStatement.setLong(paramBindObject.index, ((Long)paramBindObject.value).longValue());
      break;
    case 2: 
      paramPreparedStatement.setDate(paramBindObject.index, (Date)paramBindObject.value);
      break;
    case 6: 
      paramPreparedStatement.setDouble(paramBindObject.index, ((Double)paramBindObject.value).doubleValue());
      break;
    case 4: 
      paramPreparedStatement.setFloat(paramBindObject.index, ((Float)paramBindObject.value).floatValue());
      break;
    case 1: 
      paramPreparedStatement.setInt(paramBindObject.index, ((Integer)paramBindObject.value).intValue());
      break;
    case 5: 
      paramPreparedStatement.setTime(paramBindObject.index, (Time)paramBindObject.value);
      break;
    case 3: 
      paramPreparedStatement.setString(paramBindObject.index, (String)paramBindObject.value);
      break;
    default: 
      paramPreparedStatement.setObject(paramBindObject.index, paramBindObject.value);
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.db.DBObjectDef
 * JD-Core Version:    0.7.0.1
 */