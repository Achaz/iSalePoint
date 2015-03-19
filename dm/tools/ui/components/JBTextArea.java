package dm.tools.ui.components;

import dm.tools.db.DBAttribute;
import dm.tools.db.DBRow;
import dm.tools.dbui.DBUIObject;
import dm.tools.dbui.ValidationException;
import dm.tools.ui.UICommon;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextArea;

public class JBTextArea
  extends JTextArea
  implements Validator, DBUIObject
{
  public String _mCompName = null;
  private DBAttribute _mAttribDef = null;
  private int _mMinLen = -1;
  private int _mMaxLen = -1;
  private KeyAdapter _mKeyListner = null;
  private boolean _mMandatory = false;
  private boolean _mResetAllowed = true;
  
  public JBTextArea(String paramString, DBAttribute paramDBAttribute)
  {
    this._mCompName = paramString;
    this._mAttribDef = paramDBAttribute;
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
    String str1 = getText();
    if (!isValidMax(str1)) {
      return;
    }
    String str2 = hasValidDataInternal(str1);
    if (str2 != null) {
      return;
    }
    super.setText(paramString);
  }
  
  public void validateValue()
    throws ValidationException
  {
    String str1 = getText().trim();
    if (((this._mAttribDef != null) && (this._mAttribDef.isMandatory())) || ((this._mMandatory) && (str1.length() == 0)))
    {
      requestFocusInWindow();
      throw new ValidationException(this._mCompName + " cannot be empty", "Error", null);
    }
    if (str1.length() == 0) {
      return;
    }
    if (!isValidMax(str1))
    {
      requestFocusInWindow();
      throw new ValidationException(this._mCompName + " cannot exceed more than " + this._mMaxLen + " characeters.", "Error", null);
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
  
  public void setInstance(DBRow paramDBRow)
  {
    if (paramDBRow == null)
    {
      setText("");
      return;
    }
    String str = this._mAttribDef.getName();
    Object localObject = paramDBRow.getValue(str);
    setText(localObject != null ? paramDBRow.getValue(str).toString() : "");
  }
  
  public void setValueToInstance(DBRow paramDBRow)
  {
    String str1 = getText();
    String str2 = this._mAttribDef.getName();
    paramDBRow.setValue(str2, str1);
  }
  
  public void setRunTimeValidation(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      if (this._mKeyListner != null) {
        removeKeyListener(this._mKeyListner);
      }
      this._mKeyListner = new KeyAdapter()
      {
        public void keyTyped(KeyEvent paramAnonymousKeyEvent)
        {
          String str1 = JBTextArea.this.getText() + paramAnonymousKeyEvent.getKeyChar();
          if (!JBTextArea.this.isValidMax(str1))
          {
            paramAnonymousKeyEvent.consume();
            return;
          }
          String str2 = JBTextArea.this.hasValidDataInternal(str1);
          if (str2 != null) {
            paramAnonymousKeyEvent.consume();
          }
        }
      };
      addKeyListener(this._mKeyListner);
    }
    else
    {
      if (this._mKeyListner != null) {
        removeKeyListener(this._mKeyListner);
      }
      this._mKeyListner = null;
    }
  }
  
  public void initSelf() {}
  
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
    setText("");
  }
  
  public boolean isResetAllowed()
  {
    return this._mResetAllowed;
  }
  
  public void setResetAllowed(boolean paramBoolean)
  {
    this._mResetAllowed = true;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.ui.components.JBTextArea
 * JD-Core Version:    0.7.0.1
 */