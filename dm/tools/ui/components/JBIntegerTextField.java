package dm.tools.ui.components;

import dm.tools.utils.Validation;
import javax.swing.JTextField;

public class JBIntegerTextField
  extends JTextField
{
  private boolean _mAutoValidate = false;
  private int _mMaxValue = 2147483647;
  private int _mMinValue = -2147483648;
  private boolean _mMandatory = false;
  
  public JBIntegerTextField() {}
  
  public JBIntegerTextField(boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2)
  {
    this._mAutoValidate = paramBoolean2;
    this._mMaxValue = paramInt1;
    this._mMinValue = paramInt2;
    this._mMandatory = paramBoolean1;
  }
  
  public boolean isValidValue()
  {
    String str = getText().trim();
    if ((this._mMandatory) && (str.length() == 0)) {
      return false;
    }
    boolean bool = Validation.isValidInt(str, this._mMinValue < 0);
    if (!bool) {
      return false;
    }
    int i = Integer.valueOf(str).intValue();
    return (i >= this._mMinValue) && (i <= this._mMaxValue);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.ui.components.JBIntegerTextField
 * JD-Core Version:    0.7.0.1
 */