package dm.tools.db;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

public class DBAttribute
{
  private String _mName = null;
  public static final short ATTR_TYPE_INT = 1;
  public static final short ATTR_TYPE_DATE = 2;
  public static final short ATTR_TYPE_VARCHAR = 3;
  public static final short ATTR_TYPE_FLOAT = 4;
  public static final short ATTR_TYPE_TIME = 5;
  public static final short ATTR_TYPE_DOUBLE = 6;
  public static final short ATTR_TYPE_BIGINT = 7;
  public static final short ATTR_TYPE_TIMESTAMP = 8;
  private boolean _mMandatory = false;
  private short _mType = 3;
  private boolean _mAutoIncr = false;
  
  public DBAttribute(String paramString, short paramShort, boolean paramBoolean)
  {
    this._mName = paramString;
    this._mType = paramShort;
    this._mMandatory = paramBoolean;
  }
  
  public DBAttribute(String paramString, short paramShort, boolean paramBoolean1, boolean paramBoolean2)
  {
    this._mName = paramString;
    this._mType = paramShort;
    this._mMandatory = paramBoolean1;
    this._mAutoIncr = paramBoolean2;
  }
  
  public boolean isAutoIncr()
  {
    return this._mAutoIncr;
  }
  
  public String getName()
  {
    return this._mName;
  }
  
  public String toString()
  {
    return this._mName;
  }
  
  public void bindValue(PreparedStatement paramPreparedStatement, int paramInt, Object paramObject)
    throws SQLException
  {
    if (paramObject == null)
    {
      paramPreparedStatement.setObject(paramInt, null);
      return;
    }
    switch (this._mType)
    {
    case 1: 
      paramPreparedStatement.setInt(paramInt, ((Integer)paramObject).intValue());
      break;
    case 2: 
      paramPreparedStatement.setDate(paramInt, (Date)paramObject);
      break;
    case 5: 
      paramPreparedStatement.setTime(paramInt, (Time)paramObject);
      break;
    case 4: 
      paramPreparedStatement.setFloat(paramInt, ((Double)paramObject).floatValue());
      break;
    case 6: 
      paramPreparedStatement.setDouble(paramInt, ((Double)paramObject).doubleValue());
      break;
    case 3: 
      paramPreparedStatement.setString(paramInt, (String)paramObject);
      break;
    case 7: 
      paramPreparedStatement.setLong(paramInt, ((Long)paramObject).longValue());
      break;
    case 8: 
      paramPreparedStatement.setTimestamp(paramInt, new Timestamp(((Long)paramObject).longValue()));
    }
  }
  
  public Object getValueFromResultSet(int paramInt, ResultSet paramResultSet)
    throws SQLException
  {
    switch (this._mType)
    {
    case 1: 
      Integer localInteger = new Integer(paramResultSet.getInt(paramInt));
      return localInteger;
    case 2: 
      return paramResultSet.getDate(paramInt);
    case 5: 
      return paramResultSet.getTime(paramInt);
    case 4: 
      Float localFloat = new Float(paramResultSet.getFloat(paramInt));
      return localFloat;
    case 6: 
      Double localDouble = new Double(paramResultSet.getDouble(paramInt));
      return localDouble;
    case 3: 
      return paramResultSet.getString(paramInt);
    case 7: 
      Long localLong = new Long(paramResultSet.getLong(paramInt));
      return localLong;
    case 8: 
      Timestamp localTimestamp = new Timestamp(paramResultSet.getLong(paramInt));
      return localTimestamp;
    }
    return null;
  }
  
  public boolean isMandatory()
  {
    return this._mMandatory;
  }
  
  public short getType()
  {
    return this._mType;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.db.DBAttribute
 * JD-Core Version:    0.7.0.1
 */