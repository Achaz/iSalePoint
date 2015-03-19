package dm.jb.ui.common;

import javax.swing.JComponent;
import javax.swing.JLabel;

public class ShortKeyCommon
{
  public static final int SHORTKEYFOR_ADD_BUTTON = 65;
  public static final int SHORTKEYFOR_UPDATE_BUTTON = 85;
  public static final int SHORTKEYFOR_DELETE_BUTTON = 68;
  public static final int SHORTKEYFOR_RESET_BUTTON = 82;
  public static final int SHORTKEYFOR_HELP_BUTTON = 72;
  public static final int SHORTKEYFOR_ID_SEARCH_BUTTON = 74;
  public static final int SHORTKEYFOR_NAME_SEARCH_BUTTON = 90;
  public static final int SHORTKEYFOR_PHONE_SEARCH_BUTTON = 71;
  public static final int SHORTKEYFOR_SHUTTLE_SELECT_TO_RIGHT = 49;
  public static final int SHORTKEYFOR_SHUTTLE_SELECT_ALL_TO_RIGHT = 50;
  public static final int SHORTKEYFOR_SHUTTLE_SELECT_TO_LEFT = 51;
  public static final int SHORTKEYFOR_SHUTTLE_SELECT_ALL_TO_LEFT = 52;
  public static final int SHORTKEYFOR_MOVE_UP = 38;
  public static final int SHORTKEYFOR_MOVE_TO_TOP = 37;
  public static final int SHORTKEYFOR_MOVE_DOWN = 40;
  public static final int SHORTKEYFOR_MOVE_TO_BOTTOM = 39;
  public static final int SHORTKEYFOR_PRODUCT_NAME = 78;
  public static final int SHORTKEYFOR_PRODUCT_CODE = 80;
  public static final int SHORTKEYFOR_QUANTITY = 81;
  
  public static void shortKeyForLabels(JLabel paramJLabel, int paramInt, JComponent paramJComponent)
  {
    paramJLabel.setDisplayedMnemonic(paramInt);
    paramJLabel.setLabelFor(paramJComponent);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.common.ShortKeyCommon
 * JD-Core Version:    0.7.0.1
 */