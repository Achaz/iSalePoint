package dm.tools.ui.components;

import dm.tools.db.DBAttribute;
import dm.tools.db.DBRow;
import dm.tools.dbui.ValidationException;
import dm.tools.utils.Validation;
import java.awt.event.KeyAdapter;

public class JBDBUIFloatTextField
  extends JBRuntimeValidatedTextField
{
  private short _mMinLength = 0;
  private short _mMaxLength = 0;
  private boolean _mAutoValidate = false;
  private KeyAdapter _mKeyListner = null;
  private boolean _mMaxValSet = false;
  private double _mMaxVal = 0.0D;
  private boolean _mMinValSet = false;
  private double _mMinVal = 0.0D;
  
  public JBDBUIFloatTextField(String paramString, DBAttribute paramDBAttribute)
  {
    super(paramString, paramDBAttribute);
  }
  
  public JBDBUIFloatTextField(String paramString, short paramShort1, short paramShort2, boolean paramBoolean, DBAttribute paramDBAttribute)
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
    if (!Validation.isValidFloat(str, 25, false))
    {
      requestFocusInWindow();
      throw new ValidationException(this._mCompName + " is invalid.", "Error", null);
    }
    double d = Double.valueOf(str).doubleValue();
    if ((this._mMaxValSet) && (d > this._mMaxVal))
    {
      requestFocusInWindow();
      throw new ValidationException(this._mCompName + " cannot be more than " + this._mMaxVal + ".", "Error", null);
    }
    if ((this._mMinValSet) && (d < this._mMinVal))
    {
      requestFocusInWindow();
      throw new ValidationException(this._mCompName + " cannot be less than " + this._mMinVal + ".", "Error", null);
    }
  }
  
  public void setRunTimeValidation(boolean paramBoolean) {}
  
  public void setMaxValue(float paramFloat)
  {
    this._mMaxVal = paramFloat;
    this._mMaxValSet = true;
  }
  
  public void setMinValue(float paramFloat)
  {
    this._mMinVal = paramFloat;
    this._mMinValSet = true;
  }
  
  public void setValueToInstance(DBRow paramDBRow)
  {
    if ((getText().trim().length() == 0) && (!this._mAttrib.isMandatory()))
    {
      paramDBRow.setValue(this._mAttrib.getName(), null);
      return;
    }
    paramDBRow.setValue(this._mAttrib.getName(), Double.valueOf(getText().trim()));
  }
  
  public void setCompDefaultValue()
  {
    if (!this._mAttrib.isMandatory())
    {
      setText("");
      return;
    }
    setText("0.0");
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.ui.components.JBDBUIFloatTextField
 * JD-Core Version:    0.7.0.1
 */