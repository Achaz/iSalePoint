package dm.tools.ui.components;

import dm.tools.db.DBAttribute;
import dm.tools.dbui.ValidationException;
import java.awt.event.KeyAdapter;

public class JBStringTextField
  extends JBRuntimeValidatedTextField
{
  private int _mMinLen = -1;
  private int _mMaxLen = -1;
  private KeyAdapter _mKeyListner = null;
  
  public JBStringTextField(String paramString, DBAttribute paramDBAttribute)
  {
    super(paramString, paramDBAttribute);
  }
  
  public JBStringTextField()
  {
    super(null, null);
  }
  
  public void setRunTimeValidation(boolean paramBoolean) {}
  
  public JBStringTextField(String paramString, int paramInt1, int paramInt2, boolean paramBoolean, DBAttribute paramDBAttribute)
  {
    super(paramString, paramDBAttribute);
    this._mMinLen = paramInt1;
    this._mMaxLen = paramInt2;
    setRunTimeValidation(paramBoolean);
  }
  
  public boolean isValidMax(String paramString)
  {
    return (this._mMaxLen == -1) || (paramString.length() <= this._mMaxLen);
  }
  
  public boolean isValidMin(String paramString)
  {
    return (this._mMinLen == -1) || (paramString.length() >= this._mMinLen);
  }
  
  public void setText(String paramString)
  {
    if (!isValidMax(paramString)) {
      return;
    }
    String str = hasValidDataInternal(paramString);
    if (str != null) {
      return;
    }
    super.setText(paramString);
  }
  
  public void validateValue()
    throws ValidationException
  {
    String str1 = getText().trim();
    if ((isMandatory()) && (str1.length() == 0))
    {
      requestFocusInWindow();
      throw new ValidationException(this._mCompName + " cannot be empty.", "Error", null);
    }
    if (str1.length() == 0) {
      return;
    }
    if (!isValidMax(str1))
    {
      requestFocusInWindow();
      throw new ValidationException(this._mCompName + " cannot exceed " + this._mMaxLen + " characters.", "Error", null);
    }
    if (!isValidMin(str1))
    {
      requestFocusInWindow();
      throw new ValidationException(this._mCompName + " should contain more than " + this._mMinLen + " characters.", "Error", null);
    }
    String str2 = hasValidDataInternal(str1);
    if (str2 != null)
    {
      requestFocusInWindow();
      throw new ValidationException(str2, "Error", null);
    }
  }
  
  public final String hasValidDataInternal(String paramString)
  {
    return null;
  }
  
  public void setMaxLength(int paramInt)
  {
    this._mMaxLen = paramInt;
  }
  
  public void setMinLength(int paramInt)
  {
    this._mMinLen = paramInt;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.ui.components.JBStringTextField
 * JD-Core Version:    0.7.0.1
 */