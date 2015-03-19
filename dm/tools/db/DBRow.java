package dm.tools.db;

import java.util.ArrayList;

public class DBRow
{
  private Object[] _mData = null;
  private byte[] _mChangeFlag = null;
  private DBObjectDef _mDef = null;
  private Object _mKeyVal = null;
  private Object _mIndexVal = null;
  boolean _mIsNew = true;
  
  public DBRow(int paramInt, DBObjectDef paramDBObjectDef)
  {
    this._mData = new Object[paramInt];
    this._mChangeFlag = new byte[paramInt / 8 + 1];
    this._mDef = paramDBObjectDef;
  }
  
  public void setKeyValue(Object paramObject)
  {
    this._mKeyVal = paramObject;
  }
  
  public void setIndexValue(Object paramObject)
  {
    this._mIndexVal = paramObject;
  }
  
  public Object getKeyValue()
  {
    return this._mKeyVal;
  }
  
  public Object getIndexValue()
  {
    return this._mIndexVal;
  }
  
  public void clearAllAttributesChanged()
  {
    for (int i = 0; i < this._mChangeFlag.length; i++) {
      this._mChangeFlag[i] = 0;
    }
  }
  
  public void create()
    throws DBException
  {
    create(false);
  }
  
  public void create(boolean paramBoolean)
    throws DBException
  {
    this._mDef.createRow(this, paramBoolean);
    clearAllAttributesChanged();
    this._mIsNew = false;
  }
  
  Object[] getData()
  {
    return this._mData;
  }
  
  public Object getValue(String paramString)
  {
    int i = this._mDef._mColNames.indexOf(paramString);
    return this._mData[i];
  }
  
  void setValue(int paramInt, Object paramObject)
  {
    if (paramObject == null)
    {
      this._mData[paramInt] = paramObject;
    }
    else if (!paramObject.equals(this._mData[paramInt]))
    {
      int tmp35_34 = (paramInt / 8);
      byte[] tmp35_28 = this._mChangeFlag;
      tmp35_28[tmp35_34] = ((byte)(tmp35_28[tmp35_34] | 1 << paramInt % 8));
    }
    this._mData[paramInt] = paramObject;
  }
  
  public void setValue(String paramString, Object paramObject)
  {
    setValueInternal(paramString, paramObject, false);
  }
  
  private void setValueInternal(String paramString, Object paramObject, boolean paramBoolean)
  {
    int i = this._mDef._mColNames.indexOf(paramString);
    if (i == -1) {
      throw new NullPointerException("Attribute is null for column " + paramString);
    }
    if (!paramBoolean) {
      if (this._mData[i] == null)
      {
        int tmp69_68 = (i / 8);
        byte[] tmp69_61 = this._mChangeFlag;
        tmp69_61[tmp69_68] = ((byte)(tmp69_61[tmp69_68] | 1 << i % 8));
      }
      else if (!this._mData[i].equals(paramObject))
      {
        int tmp107_106 = (i / 8);
        byte[] tmp107_99 = this._mChangeFlag;
        tmp107_99[tmp107_106] = ((byte)(tmp107_99[tmp107_106] | 1 << i % 8));
      }
    }
    if ((this._mDef._mKeyAttr != null) && (this._mDef._mKeyAttr.getName().equals(paramString))) {
      this._mKeyVal = this._mData[i];
    }
    if ((this._mDef._mIndexAttribute != null) && (this._mDef._mIndexAttribute.getName().equals(paramString))) {
      this._mIndexVal = this._mData[i];
    }
    this._mData[i] = paramObject;
  }
  
  public void setValueFromDB(String paramString, Object paramObject)
  {
    setValueInternal(paramString, paramObject, true);
  }
  
  void resaveKeyvalues()
  {
    int i;
    if (this._mDef._mKeyAttr != null)
    {
      i = this._mDef._mColNames.indexOf(this._mDef._mKeyAttr.getName());
      this._mKeyVal = this._mData[i];
    }
    if (this._mDef._mIndexAttribute != null)
    {
      i = this._mDef._mColNames.indexOf(this._mDef._mIndexAttribute.getName());
      this._mIndexVal = this._mData[i];
    }
  }
  
  public boolean isAttributeUpdated(String paramString)
  {
    int i = this._mDef._mColNames.indexOf(paramString);
    if (i < 0) {
      return false;
    }
    return isAttributeUpdated(i);
  }
  
  public boolean isAttributeUpdated(int paramInt)
  {
    int i = this._mChangeFlag[(paramInt / 8)];
    i = (byte)(i & 1 << paramInt % 8);
    return i != 0;
  }
  
  public void update(boolean paramBoolean)
    throws DBException
  {
    if ((!isChanged()) && (!paramBoolean)) {
      return;
    }
    if (paramBoolean) {
      this._mDef.update(true, this);
    } else {
      this._mDef.update(false, this);
    }
  }
  
  public void delete()
    throws DBException
  {
    this._mDef.delete(this);
  }
  
  public boolean isChanged()
  {
    for (int i = 0; i < this._mChangeFlag.length; i++) {
      if (this._mChangeFlag[i] != 0) {
        return true;
      }
    }
    return false;
  }
  
  public boolean isNew()
  {
    for (Object localObject : this._mData) {
      if (localObject != null) {
        return false;
      }
    }
    return true;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.db.DBRow
 * JD-Core Version:    0.7.0.1
 */