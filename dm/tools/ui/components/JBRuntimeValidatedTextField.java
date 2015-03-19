package dm.tools.ui.components;

import dm.tools.db.DBAttribute;
import dm.tools.db.DBRow;
import dm.tools.dbui.DBUIObject;
import dm.tools.dbui.ValidationException;
import dm.tools.ui.UICommon;
import java.awt.Color;
import javax.swing.JTextField;

public class JBRuntimeValidatedTextField
  extends JTextField
  implements DBUIObject
{
  String _mCompName = null;
  DBAttribute _mAttrib = null;
  private boolean _mMandatory = false;
  private boolean _mResetAllowed = true;
  private DBUIComponentValueSetter _mValueSetter = null;
  
  public JBRuntimeValidatedTextField(String paramString, DBAttribute paramDBAttribute)
  {
    this._mAttrib = paramDBAttribute;
    this._mCompName = paramString;
    if (this._mAttrib != null) {
      setMandatory(this._mAttrib.isMandatory());
    }
  }
  
  public String getComponentName()
  {
    return this._mCompName;
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
      resetValue();
      return;
    }
    Object localObject = paramDBRow.getValue(this._mAttrib.getName());
    String str = "";
    if (localObject != null) {
      str = localObject.toString();
    }
    setText(str);
  }
  
  public void setValueToInstance(DBRow paramDBRow)
  {
    if (this._mValueSetter != null)
    {
      this._mValueSetter.setValueToInstance(paramDBRow);
      return;
    }
    if ((getText().trim().length() == 0) && (!this._mAttrib.isMandatory()))
    {
      paramDBRow.setValue(this._mAttrib.getName(), null);
      return;
    }
    paramDBRow.setValue(this._mAttrib.getName(), getText());
  }
  
  public void initSelf()
  {
    setCompDefaultValue();
  }
  
  public void setMandatory(boolean paramBoolean)
  {
    if (paramBoolean) {
      setBackground(UICommon.MANDATORY_COLOR);
    } else {
      setBackground(Color.WHITE);
    }
    this._mMandatory = paramBoolean;
  }
  
  public void resetValue()
  {
    setCompDefaultValue();
  }
  
  public void validateValue()
    throws ValidationException
  {
    String str = getText().trim();
    if ((isMandatory()) && (str.length() == 0))
    {
      requestFocusInWindow();
      throw new ValidationException(this._mCompName + " cannot be empty.", "Error", null);
    }
    if (!isMandatory()) {}
  }
  
  public void setValueSetter(DBUIComponentValueSetter paramDBUIComponentValueSetter)
  {
    this._mValueSetter = paramDBUIComponentValueSetter;
  }
  
  public void setCompDefaultValue()
  {
    setText("");
  }
  
  public boolean isMandatory()
  {
    return (this._mMandatory) || ((this._mAttrib != null) && (this._mAttrib.isMandatory()));
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
 * Qualified Name:     dm.tools.ui.components.JBRuntimeValidatedTextField
 * JD-Core Version:    0.7.0.1
 */