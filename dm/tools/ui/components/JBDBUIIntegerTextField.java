package dm.tools.ui.components;

import dm.tools.db.DBAttribute;
import dm.tools.db.DBRow;
import dm.tools.dbui.ValidationException;
import dm.tools.utils.Validation;
import java.awt.event.KeyAdapter;

public class JBDBUIIntegerTextField
  extends JBRuntimeValidatedTextField
{
  private short _mMinLength = 0;
  private short _mMaxLength = 0;
  private boolean _mAutoValidate = false;
  private KeyAdapter _mKeyListner = null;
  
  public JBDBUIIntegerTextField(String paramString, DBAttribute paramDBAttribute)
  {
    super(paramString, paramDBAttribute);
  }
  
  public JBDBUIIntegerTextField(String paramString, short paramShort1, short paramShort2, boolean paramBoolean, DBAttribute paramDBAttribute)
  {
    super(paramString, paramDBAttribute);
    this._mAutoValidate = paramBoolean;
    this._mMinLength = paramShort1;
    this._mMaxLength = paramShort2;
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
    if (str.length() == 0) {
      return;
    }
    if (!Validation.isValidInt(str, false))
    {
      requestFocusInWindow();
      throw new ValidationException(this._mCompName + " is invalid.", "Error", null);
    }
  }
  
  public void setRunTimeValidation(boolean paramBoolean) {}
  
  public void setValueToInstance(DBRow paramDBRow)
  {
    if ((getText().trim().length() == 0) && (!this._mAttrib.isMandatory()))
    {
      paramDBRow.setValue(this._mAttrib.getName(), null);
      return;
    }
    paramDBRow.setValue(this._mAttrib.getName(), Integer.valueOf(getText().trim()));
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.ui.components.JBDBUIIntegerTextField
 * JD-Core Version:    0.7.0.1
 */