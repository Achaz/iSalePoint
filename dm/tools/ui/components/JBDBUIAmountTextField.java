package dm.tools.ui.components;

import dm.tools.db.DBAttribute;
import dm.tools.db.DBRow;
import dm.tools.dbui.DBUIObject;
import dm.tools.dbui.ValidationException;
import dm.tools.types.InternalAmount;
import dm.tools.ui.UICommon;
import dm.tools.utils.Validation;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTextField;

public class JBDBUIAmountTextField
  extends JTextField
  implements KeyListener, FocusListener, DBUIObject
{
  private String _mOldText = "";
  private DBAttribute _mAttrib = null;
  private String _mCompName = null;
  private boolean _mResetAllowed = true;
  
  public JBDBUIAmountTextField(String paramString, DBAttribute paramDBAttribute)
  {
    this._mAttrib = paramDBAttribute;
    this._mCompName = paramString;
  }
  
  public void setText(String paramString)
  {
    super.setText(paramString);
    this._mOldText = paramString;
  }
  
  public void keyPressed(KeyEvent paramKeyEvent) {}
  
  public void keyReleased(KeyEvent paramKeyEvent)
  {
    int i = getCaretPosition();
    String str = getText();
    if (!Validation.isValidAmount(str))
    {
      setText(this._mOldText);
      setCaretPosition(i - 1);
      paramKeyEvent.consume();
    }
    else
    {
      this._mOldText = str;
    }
  }
  
  public void keyTyped(KeyEvent paramKeyEvent) {}
  
  public void focusGained(FocusEvent paramFocusEvent) {}
  
  public void focusLost(FocusEvent paramFocusEvent)
  {
    String str = getText();
    if (!Validation.isValidAmount(str))
    {
      requestFocusInWindow();
      return;
    }
    double d = Double.valueOf(str).doubleValue();
    InternalAmount localInternalAmount = new InternalAmount(d);
    setText(localInternalAmount.toString());
  }
  
  public void setInstance(DBRow paramDBRow)
  {
    if (paramDBRow == null)
    {
      initSelf();
      return;
    }
    double d = Double.valueOf(paramDBRow.getValue(this._mAttrib.getName()).toString()).doubleValue();
    InternalAmount localInternalAmount = new InternalAmount(d);
    setText(localInternalAmount.toString());
  }
  
  public void setValueToInstance(DBRow paramDBRow)
  {
    String str = getText().trim();
    paramDBRow.setValue(this._mAttrib.getName(), Double.valueOf(str));
  }
  
  public void initSelf()
  {
    setText("0.00");
  }
  
  public void setMandatory(boolean paramBoolean)
  {
    setBackground(UICommon.MANDATORY_COLOR);
  }
  
  public void resetValue()
  {
    initSelf();
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
    if (!isMandatory()) {
      return;
    }
    if (str.length() == 0)
    {
      requestFocusInWindow();
      throw new ValidationException(this._mCompName + " cannot be empty", "Error", "Error");
    }
    if (!Validation.isValidAmount(str))
    {
      requestFocusInWindow();
      throw new ValidationException(this._mCompName + " is invlaid", "Error", "Error");
    }
  }
  
  public boolean isMandatory()
  {
    if (this._mAttrib != null) {
      return this._mAttrib.isMandatory();
    }
    return false;
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
 * Qualified Name:     dm.tools.ui.components.JBDBUIAmountTextField
 * JD-Core Version:    0.7.0.1
 */