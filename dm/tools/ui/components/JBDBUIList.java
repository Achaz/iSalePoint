package dm.tools.ui.components;

import dm.tools.db.DBException;
import dm.tools.db.DBObjectDef;
import dm.tools.db.DBRow;
import dm.tools.dbui.DBUIObject;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JList;

public class JBDBUIList
  extends JList
  implements DBUIObject
{
  private DBObjectDef _mTableDef = null;
  private boolean _mResetAllowed = true;
  
  public JBDBUIList(DBObjectDef paramDBObjectDef, String paramString1, String paramString2, String paramString3)
  {
    this(new JBDBUIListModel(), paramDBObjectDef, paramString1, paramString2, paramString3);
  }
  
  public JBDBUIList(DefaultListModel paramDefaultListModel, DBObjectDef paramDBObjectDef, String paramString1, String paramString2, String paramString3)
  {
    super(paramDefaultListModel);
    this._mTableDef = paramDBObjectDef;
  }
  
  public void setInstance(DBRow paramDBRow) {}
  
  public void setValueToInstance(DBRow paramDBRow) {}
  
  public void initSelf()
  {
    DefaultListModel localDefaultListModel = (DefaultListModel)getModel();
    localDefaultListModel.clear();
    if (this._mTableDef == null) {
      return;
    }
    try
    {
      ArrayList localArrayList = this._mTableDef.getAllValues();
      if ((localArrayList == null) || (localArrayList.size() == 0)) {
        return;
      }
      int i = localArrayList.size();
      for (int j = 0; j < i; j++)
      {
        DBRow localDBRow = (DBRow)localArrayList.get(j);
        localDefaultListModel.addElement(localDBRow);
      }
    }
    catch (DBException localDBException)
    {
      localDBException.printStackTrace();
    }
  }
  
  public void validateValue() {}
  
  public void setMandatory(boolean paramBoolean) {}
  
  public void resetValue()
  {
    clearSelection();
  }
  
  public void setWhereClause(String paramString)
    throws DBException
  {
    ArrayList localArrayList = this._mTableDef.getAllValuesWithWhereClause(paramString);
    clearSelection();
    if ((localArrayList == null) || (localArrayList.size() == 0))
    {
      ((DefaultListModel)getModel()).removeAllElements();
      return;
    }
    DefaultListModel localDefaultListModel = (DefaultListModel)getModel();
    localDefaultListModel.removeAllElements();
    for (int i = 0; i < localArrayList.size(); i++) {
      localDefaultListModel.addElement(localArrayList.get(i));
    }
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
 * Qualified Name:     dm.tools.ui.components.JBDBUIList
 * JD-Core Version:    0.7.0.1
 */