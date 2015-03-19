package dm.tools.ui.components;

import com.toedter.calendar.JDateChooser;
import dm.tools.db.DBAttribute;
import dm.tools.db.DBRow;
import dm.tools.dbui.DBUIObject;
import dm.tools.dbui.ValidationException;
import dm.tools.types.InternalDate;

public class JBDBUIDateField
  extends JDateChooser
  implements DBUIObject
{
  private String _mCompName = null;
  private DBAttribute _mAttrib = null;
  private DBUIComponentValueSetter _mValueSetter = null;
  private boolean _mMandatory = false;
  private boolean _mResetAllowed = true;
  
  public JBDBUIDateField(String paramString, DBAttribute paramDBAttribute)
  {
    super(new java.util.Date());
    this._mCompName = paramString;
    this._mAttrib = paramDBAttribute;
  }
  
  public void setInstance(DBRow paramDBRow)
  {
    if (this._mValueSetter != null)
    {
      this._mValueSetter.setInstance(paramDBRow);
      return;
    }
    if (paramDBRow == null)
    {
      setDate(null);
      return;
    }
    Object localObject = paramDBRow.getValue(this._mAttrib.getName());
    if (localObject == null) {
      setDate(null);
    } else {
      setDate((java.util.Date)localObject);
    }
  }
  
  public void setValueToInstance(DBRow paramDBRow)
  {
    if (this._mValueSetter != null)
    {
      this._mValueSetter.setValueToInstance(paramDBRow);
      return;
    }
    java.sql.Date localDate = InternalDate.getSqlDate(getDate());
    paramDBRow.setValue(this._mAttrib.getName(), localDate);
  }
  
  public void initSelf()
  {
    if (((this._mAttrib != null) && (this._mAttrib.isMandatory())) || (this._mMandatory)) {
      setDate(new java.util.Date());
    } else {
      setDate(null);
    }
  }
  
  public void setMandatory(boolean paramBoolean)
  {
    this._mMandatory = paramBoolean;
  }
  
  public void resetValue()
  {
    initSelf();
  }
  
  public void validateValue()
    throws ValidationException
  {
    if (((this._mMandatory) || ((this._mAttrib != null) && (this._mAttrib.isMandatory()))) && (getDate() == null)) {
      throw new ValidationException(this._mCompName + " is mandatory.", "Mandatory attribute is missing.", "Set date");
    }
  }
  
  public void setValueSetter(DBUIComponentValueSetter paramDBUIComponentValueSetter)
  {
    this._mValueSetter = paramDBUIComponentValueSetter;
  }
  
  public boolean isResetAllowed()
  {
    return this._mResetAllowed;
  }
  
  public void setResetAllowed(boolean paramBoolean)
  {
    this._mResetAllowed = paramBoolean;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.ui.components.JBDBUIDateField
 * JD-Core Version:    0.7.0.1
 */