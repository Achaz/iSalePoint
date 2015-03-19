package dm.tools.ui.components;

import dm.tools.db.DBRow;
import dm.tools.dbui.DBUIObject;
import javax.swing.JCheckBox;

public class JBDBUICheckBox
  extends JCheckBox
  implements DBUIObject
{
  private Object _mYesValue = null;
  private Object _mNoValue = null;
  private String _mAttribName = null;
  private boolean _mResetAllowed = true;
  
  public JBDBUICheckBox(Object paramObject1, Object paramObject2, String paramString)
  {
    this._mYesValue = paramObject1;
    this._mNoValue = paramObject2;
    this._mAttribName = paramString;
  }
  
  public void setInstance(DBRow paramDBRow)
  {
    Object localObject = paramDBRow.getValue(this._mAttribName);
    if ((localObject == null) || (localObject.equals(this._mNoValue))) {
      setSelected(false);
    } else {
      setSelected(true);
    }
  }
  
  public void setValueToInstance(DBRow paramDBRow)
  {
    if (isSelected()) {
      paramDBRow.setValue(this._mAttribName, this._mYesValue);
    } else {
      paramDBRow.setValue(this._mAttribName, this._mNoValue);
    }
  }
  
  public void initSelf()
  {
    setSelected(false);
  }
  
  public void setMandatory(boolean paramBoolean) {}
  
  public void resetValue()
  {
    initSelf();
  }
  
  public void validateValue() {}
  
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
 * Qualified Name:     dm.tools.ui.components.JBDBUICheckBox
 * JD-Core Version:    0.7.0.1
 */