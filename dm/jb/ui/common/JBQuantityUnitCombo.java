package dm.jb.ui.common;

import dm.tools.db.DBAttribute;
import dm.tools.db.DBRow;
import dm.tools.dbui.StaticListComponent;
import dm.tools.dbui.ValidationException;
import dm.tools.types.InternalQuantity;
import javax.swing.JComboBox;

public class JBQuantityUnitCombo
  extends JComboBox
  implements StaticListComponent
{
  private DBAttribute _mAttrib = null;
  private String _mCompName = null;
  
  public JBQuantityUnitCombo(String paramString, DBAttribute paramDBAttribute)
  {
    super(InternalQuantity.quantityUnits);
    this._mAttrib = paramDBAttribute;
    this._mCompName = paramString;
  }
  
  public void setValueToRow(DBRow paramDBRow)
  {
    int i = getSelectedIndex();
    paramDBRow.setValue(this._mAttrib.getName(), Integer.valueOf(i));
  }
  
  public void setValueFromRow(DBRow paramDBRow)
  {
    if (paramDBRow == null)
    {
      setSelectedItem(null);
      return;
    }
    Object localObject = paramDBRow.getValue(this._mAttrib.getName());
    int i = 2;
    if (localObject != null) {
      i = Integer.valueOf(localObject.toString()).intValue();
    }
    String str = InternalQuantity.quantityUnits[i];
    setSelectedItem(str);
  }
  
  public void validateValue()
    throws ValidationException
  {
    if ((this._mAttrib.isMandatory()) && (getSelectedItem() == null))
    {
      requestFocusInWindow();
      throw new ValidationException(this._mCompName + " cannot be empty.", "Error", "Error");
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.common.JBQuantityUnitCombo
 * JD-Core Version:    0.7.0.1
 */