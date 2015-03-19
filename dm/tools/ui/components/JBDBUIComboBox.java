package dm.tools.ui.components;

import dm.tools.db.DBAttribute;
import dm.tools.db.DBException;
import dm.tools.db.DBObjectDef;
import dm.tools.db.DBRow;
import dm.tools.dbui.DBUIObject;
import dm.tools.dbui.ValidationException;
import dm.tools.ui.UICommon;
import java.util.ArrayList;
import javax.swing.JComboBox;

public class JBDBUIComboBox
  extends JComboBox
  implements DBUIObject
{
  private DBObjectDef _mTableDef = null;
  private String _mCompName = null;
  private String _mReturnAttrib = null;
  private String _mAttribName = null;
  private boolean isMandatory = false;
  private boolean _mResetAllowed = true;
  
  public JBDBUIComboBox(DBObjectDef paramDBObjectDef, String paramString1, String paramString2, String paramString3)
  {
    this._mTableDef = paramDBObjectDef;
    this._mCompName = paramString1;
    this._mReturnAttrib = paramString2;
    this._mAttribName = paramString3;
    this.isMandatory = this._mTableDef.getAttributeDefByName(paramString2).isMandatory();
  }
  
  public void setInstance(DBRow paramDBRow)
  {
    if (paramDBRow == null)
    {
      setSelectedItem(null);
      return;
    }
    Object localObject = paramDBRow.getValue(this._mAttribName);
    int i = getItemCount();
    for (int j = 0; j < i; j++)
    {
      DBRow localDBRow = (DBRow)getItemAt(j);
      if (localDBRow.getValue(this._mAttribName).equals(localObject))
      {
        setSelectedItem(localDBRow);
        break;
      }
    }
  }
  
  public void setValueToInstance(DBRow paramDBRow)
  {
    DBRow localDBRow = (DBRow)getSelectedItem();
    if (localDBRow == null) {
      paramDBRow.setValue(this._mAttribName, null);
    } else {
      paramDBRow.setValue(this._mAttribName, localDBRow.getValue(this._mReturnAttrib));
    }
  }
  
  public void validateValue()
    throws ValidationException
  {
    if (getSelectedItem() == null)
    {
      requestFocusInWindow();
      throw new ValidationException(this._mCompName + " cannot be be empty.", "Error", "Error");
    }
  }
  
  private void populate()
    throws DBException
  {
    removeAllItems();
    ArrayList localArrayList = this._mTableDef.getAllValues();
    if (localArrayList != null)
    {
      int i = localArrayList.size();
      for (int j = 0; j < i; j++) {
        addItem(localArrayList.get(j));
      }
    }
  }
  
  public void initSelf()
    throws DBException
  {
    populate();
  }
  
  public void setMandatory(boolean paramBoolean)
  {
    setBackground(UICommon.MANDATORY_COLOR);
  }
  
  public void resetValue()
  {
    setSelectedItem(null);
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
 * Qualified Name:     dm.tools.ui.components.JBDBUIComboBox
 * JD-Core Version:    0.7.0.1
 */