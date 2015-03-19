package dm.tools.dbui;

import dm.tools.db.DBAttribute;
import dm.tools.db.DBObjectDef;
import dm.tools.db.DBRow;
import dm.tools.ui.components.JBActionButton;
import dm.tools.ui.components.JBDBUIAmountTextField;
import dm.tools.ui.components.JBDBUIComboBox;
import dm.tools.ui.components.JBDBUIDateField;
import dm.tools.ui.components.JBDBUIFloatTextField;
import dm.tools.ui.components.JBDBUIIntegerTextField;
import dm.tools.ui.components.JBDBUIList;
import dm.tools.ui.components.JBPhoneField;
import dm.tools.ui.components.JBStringTextField;
import dm.tools.ui.components.JBTextArea;

public class ComponentFactory
{
  private static ComponentFactory _mInstance = null;
  public static final short COMP_TYPE_JTEXTAREA = 1;
  public static final short COMP_TYPE_PHONE = 2;
  public static final short COMP_TYPE_COMBO_BOX = 3;
  public static final short COMP_TYPE_LIST = 4;
  public static final short COMP_TYPE_AMOUNT = 5;
  public static final short COMP_TYPE_CHECK_BOX = 6;
  
  static ComponentFactory getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new ComponentFactory();
    }
    return _mInstance;
  }
  
  public DBUIObject getComponent(DBObjectDef paramDBObjectDef, String paramString1, short paramShort, String paramString2, String paramString3)
  {
    return getComponent(paramDBObjectDef, paramString1, null, paramShort, false, paramString2, paramString3);
  }
  
  public DBUIObject getComponent(DBObjectDef paramDBObjectDef, String paramString1, String paramString2, short paramShort, boolean paramBoolean, String paramString3, String paramString4)
  {
    switch (paramShort)
    {
    case 3: 
      return new JBDBUIComboBox(paramDBObjectDef, paramString1, paramString3, paramString4);
    case 4: 
      return new JBDBUIList(paramDBObjectDef, paramString1, paramString2, paramString3);
    }
    return null;
  }
  
  public DBUIObject getComponent(DBAttribute paramDBAttribute, String paramString, int paramInt)
  {
    if (paramInt != -1) {
      switch (paramInt)
      {
      case 1: 
        return new JBTextArea(paramString, paramDBAttribute);
      case 2: 
        return new JBPhoneField(paramString, paramDBAttribute);
      case 5: 
        return new JBDBUIAmountTextField(paramString, paramDBAttribute);
      }
    }
    switch (paramDBAttribute.getType())
    {
    case 1: 
      return new JBDBUIIntegerTextField(paramString, paramDBAttribute);
    case 3: 
      return new JBStringTextField(paramString, paramDBAttribute);
    case 4: 
    case 6: 
      return new JBDBUIFloatTextField(paramString, paramDBAttribute);
    case 2: 
      return new JBDBUIDateField(paramString, paramDBAttribute);
    }
    return null;
  }
  
  public ActionObject getActionComponent(DBRow paramDBRow, String paramString1, String paramString2)
  {
    byte b = -1;
    if (paramString1.equalsIgnoreCase("UPDATE")) {
      b = 1;
    } else if (paramString1.equalsIgnoreCase("CREATE")) {
      b = 2;
    } else if (paramString1.equalsIgnoreCase("DELETE")) {
      b = 3;
    } else if (paramString1.equalsIgnoreCase("UPDATE_CREATE")) {
      b = 4;
    }
    return new JBActionButton(paramString2, b, paramDBRow);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.dbui.ComponentFactory
 * JD-Core Version:    0.7.0.1
 */