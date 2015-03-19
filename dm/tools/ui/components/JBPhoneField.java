package dm.tools.ui.components;

import dm.tools.db.DBAttribute;
import dm.tools.dbui.ValidationException;
import dm.tools.utils.Validation;

public class JBPhoneField
  extends JBRuntimeValidatedTextField
{
  public JBPhoneField(String paramString, DBAttribute paramDBAttribute)
  {
    super(paramString, paramDBAttribute);
  }
  
  public void validateValue()
    throws ValidationException
  {
    String str = getText().trim();
    if ((str.length() == 0) && (isMandatory()))
    {
      requestFocusInWindow();
      throw new ValidationException(this._mCompName + " cannot be empty.", "Error", null);
    }
    if (str.length() == 0) {
      return;
    }
    if (!isValidPhoneInternal(str))
    {
      requestFocusInWindow();
      throw new ValidationException(this._mCompName + " is invalid.", "Error", null);
    }
  }
  
  private boolean isValidPhoneInternal(String paramString)
  {
    return Validation.isValidPhone(paramString);
  }
  
  public void setRunTimeValidation(boolean paramBoolean) {}
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.ui.components.JBPhoneField
 * JD-Core Version:    0.7.0.1
 */