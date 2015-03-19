package dm.jb.ui.res;

import java.util.ResourceBundle;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class ResourceUtils
{
  public static ResourceUtils INSTANCE = new ResourceUtils();
  private ResourceBundle _mBundle = null;
  
  public String getStringInternal(String paramString)
  {
    return this._mBundle.getString(paramString);
  }
  
  public static String getString(String paramString)
  {
    return INSTANCE.getStringInternal(paramString);
  }
  
  public static void setLabelString(String paramString, JLabel paramJLabel)
  {
    String str = getString(paramString);
    setMnemonicChar(str, paramJLabel);
    str = str.replaceFirst("&", "");
    paramJLabel.setText(str);
  }
  
  public static void setButtonString(String paramString, JButton paramJButton)
  {
    paramJButton.setText(getString(paramString));
  }
  
  public static void setCheckBoxString(String paramString, JCheckBox paramJCheckBox)
  {
    paramJCheckBox.setText(getString(paramString));
  }
  
  public static void setMenuString(String paramString, JMenu paramJMenu)
  {
    String str = getString(paramString);
    setMnemonicChar(str, paramJMenu);
    str = str.replaceFirst("&", "");
    paramJMenu.setText(str);
  }
  
  public static void setMenuItemString(String paramString, JMenuItem paramJMenuItem)
  {
    String str = getString(paramString);
    setMnemonicChar(str, paramJMenuItem);
    str = str.replaceFirst("&", "");
    paramJMenuItem.setText(str);
  }
  
  private static void setMnemonicChar(String paramString, JComponent paramJComponent)
  {
    int i = paramString.indexOf("&");
    if (i == -1) {
      return;
    }
    if ((paramJComponent instanceof AbstractButton)) {
      ((AbstractButton)paramJComponent).setMnemonic(paramString.charAt(i + 1));
    } else if ((paramJComponent instanceof JLabel)) {
      ((JLabel)paramJComponent).setDisplayedMnemonic(paramString.charAt(i + 1));
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.res.ResourceUtils
 * JD-Core Version:    0.7.0.1
 */