package dm.tools.ui;

import com.jgoodies.looks.Options;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import dm.jb.ui.MainWindow;
import dm.jb.ui.res.MessageResourceUtils;
import dm.tools.messages.MessageLoader;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractButton;
import javax.swing.InputMap;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

public class UICommon
{
  UISettings mSettings = null;
  public static final Color MANDATORY_COLOR = new Color(195, 217, 255);
  private static JFrame _mAppRootPane = null;
  private static MessageLoader _mMessageLoaderInst = null;
  public static final int YES = 1;
  public static final int NO = 0;
  public static final int CANCEL = 2;
  private static String _mCurrentErrorString = null;
  private static String _mDateFormat = "dd-MM-yyyy";
  public static final String INTERNAL_ERROR = "\n\nTry again later. If the problem persists contact administrator.";
  
  public UICommon(UISettings paramUISettings)
  {
    this.mSettings = paramUISettings;
  }
  
  public void setLookAndFeel(Class paramClass)
  {
    this.mSettings.setSelectedLookAndFeel(paramClass);
  }
  
  public void setLookAndFeel(String paramString)
  {
    this.mSettings.setSelectedLookAndFeel(paramString);
  }
  
  public static void showInternalError(String paramString)
  {
    showError(paramString + "\n\nTry again later. If the problem persists contact administrator.", "Internal Error", MainWindow.instance);
  }
  
  public void initUI()
  {
    if (this.mSettings != null)
    {
      Options.setDefaultIconSize(new Dimension(18, 18));
      UIManager.put("Application.useSystemFontSettings", this.mSettings.isUseSystemFonts());
      Options.setGlobalFontSizeHints(this.mSettings.getFontSizeHints());
      Options.setUseNarrowButtons(this.mSettings.isUseNarrowButtons());
      Options.setTabIconsEnabled(this.mSettings.isTabIconsEnabled());
      UIManager.put("jgoodies.popupDropShadowEnabled", this.mSettings.isPopupDropShadowEnabled());
      LookAndFeel localLookAndFeel = this.mSettings.getSelectedLookAndFeel();
      if ((localLookAndFeel instanceof PlasticLookAndFeel))
      {
        PlasticLookAndFeel.setMyCurrentTheme(this.mSettings.getSelectedTheme());
        PlasticLookAndFeel.setTabStyle(this.mSettings.getPlasticTabStyle());
        PlasticLookAndFeel.setHighContrastFocusColorsEnabled(this.mSettings.isPlasticHighContrastFocusEnabled());
      }
      else if ((localLookAndFeel instanceof MetalLookAndFeel))
      {
        MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
      }
      JRadioButton localJRadioButton = new JRadioButton();
      localJRadioButton.getUI().uninstallUI(localJRadioButton);
      JCheckBox localJCheckBox = new JCheckBox();
      localJCheckBox.getUI().uninstallUI(localJCheckBox);
      try
      {
        UIManager.setLookAndFeel(localLookAndFeel);
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
    }
  }
  
  public static void showDelayedErrorMessage(String paramString1, final String paramString2, final JFrame paramJFrame)
  {
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        UICommon.showError(this.val$msg, paramString2, paramJFrame);
      }
    });
  }
  
  public static void showDelayedErrorMessage(String paramString1, final String paramString2, final JDialog paramJDialog)
  {
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        UICommon.showError(this.val$msg, paramString2, paramJDialog);
      }
    });
  }
  
  public static void showDelayedMessage(String paramString1, final String paramString2, final JFrame paramJFrame)
  {
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        UICommon.showMessage(this.val$msg, paramString2, paramJFrame);
      }
    });
  }
  
  public static void showDelayedMessage(String paramString1, final String paramString2, final JDialog paramJDialog)
  {
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        UICommon.showMessage(this.val$msg, paramString2, paramJDialog);
      }
    });
  }
  
  public static void showError(String paramString1, String paramString2, JFrame paramJFrame)
  {
    _mCurrentErrorString = paramString1;
    JOptionPane.showMessageDialog(paramJFrame, paramString1, paramString2, 0);
  }
  
  public static void showError(String paramString1, String paramString2, JDialog paramJDialog)
  {
    _mCurrentErrorString = paramString1;
    JOptionPane.showMessageDialog(paramJDialog, paramString1, paramString2, 0);
  }
  
  public static void showWarning(String paramString1, String paramString2, JFrame paramJFrame)
  {
    _mCurrentErrorString = paramString1;
    JOptionPane.showMessageDialog(paramJFrame, paramString1, paramString2, 2);
  }
  
  public static void showWarning(String paramString1, String paramString2, JDialog paramJDialog)
  {
    _mCurrentErrorString = paramString1;
    JOptionPane.showMessageDialog(paramJDialog, paramString1, paramString2, 2);
  }
  
  public static void showMessage(String paramString1, String paramString2, JFrame paramJFrame)
  {
    _mCurrentErrorString = paramString1;
    JOptionPane.showMessageDialog(paramJFrame, paramString1, paramString2, 1);
  }
  
  public static void showResSuccessMessage(String paramString, JFrame paramJFrame)
  {
    _mCurrentErrorString = paramString;
    JOptionPane.showMessageDialog(paramJFrame, MessageResourceUtils.getString(paramString), MessageResourceUtils.getString("SUCCESS_TITLE"), 1);
  }
  
  public static void showResErrorMessage(String paramString, JFrame paramJFrame)
  {
    _mCurrentErrorString = paramString;
    JOptionPane.showMessageDialog(paramJFrame, MessageResourceUtils.getString(paramString), MessageResourceUtils.getString("ERROR_TITLE"), 0);
  }
  
  public static void showMessage(String paramString1, String paramString2, JDialog paramJDialog)
  {
    _mCurrentErrorString = paramString1;
    JOptionPane.showMessageDialog(paramJDialog, paramString1, paramString2, 1);
  }
  
  public static int showQuestion(String paramString1, String paramString2, JFrame paramJFrame)
  {
    int i = JOptionPane.showConfirmDialog(paramJFrame, paramString1, paramString2, 0);
    if (i == 0) {
      return 1;
    }
    return 0;
  }
  
  public static int showQuestion(String paramString1, String paramString2, JDialog paramJDialog)
  {
    int i = JOptionPane.showConfirmDialog(paramJDialog, paramString1, paramString2, 0);
    if (i == 0) {
      return 1;
    }
    return 0;
  }
  
  public static int showQuestionWithCancel(String paramString1, String paramString2, JFrame paramJFrame)
  {
    int i = JOptionPane.showConfirmDialog(paramJFrame, paramString1, paramString2, 1);
    if (i == 0) {
      return 1;
    }
    if (i == 2) {
      return 2;
    }
    return 0;
  }
  
  public static int showQuestionWithCancel(String paramString1, String paramString2, JDialog paramJDialog)
  {
    int i = JOptionPane.showConfirmDialog(paramJDialog, paramString1, paramString2, 1);
    if (i == 0) {
      return 1;
    }
    if (i == 2) {
      return 2;
    }
    return 0;
  }
  
  public static void setDialogPosition(JDialog paramJDialog)
  {
    Point localPoint = paramJDialog.getLocation();
    Dimension localDimension = paramJDialog.getSize();
    localPoint.x -= localDimension.width / 2;
    localPoint.y -= localDimension.height / 2;
    paramJDialog.setLocation(localPoint);
  }
  
  public static void setAppRootPane(JFrame paramJFrame)
  {
    _mAppRootPane = paramJFrame;
  }
  
  public static JFrame getAppRootPane()
  {
    return _mAppRootPane;
  }
  
  public static String getLastErrorString()
  {
    return _mCurrentErrorString;
  }
  
  public static void registerMessageLoaderClass(String paramString)
  {
    try
    {
      Class localClass = Class.forName(paramString);
      _mMessageLoaderInst = (MessageLoader)localClass.newInstance();
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      localClassNotFoundException.printStackTrace();
    }
    catch (InstantiationException localInstantiationException)
    {
      localInstantiationException.printStackTrace();
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      localIllegalAccessException.printStackTrace();
    }
  }
  
  public static MessageLoader getMessageLoader()
  {
    return _mMessageLoaderInst;
  }
  
  public static void setDateFormat(String paramString)
  {
    _mDateFormat = paramString;
  }
  
  public static String getDateFormat()
  {
    return _mDateFormat;
  }
  
  public static void addMnemonicTrigger(AbstractButton paramAbstractButton, final int paramInt)
  {
    paramAbstractButton.addPropertyChangeListener("mnemonic", new PropertyChangeListener()
    {
      public void propertyChange(PropertyChangeEvent paramAnonymousPropertyChangeEvent)
      {
        InputMap localInputMap = SwingUtilities.getUIInputMap(this.val$button, 2);
        int i = this.val$button.getMnemonic();
        localInputMap.clear();
        localInputMap.put(KeyStroke.getKeyStroke(i, paramInt, false), "pressed");
        localInputMap.put(KeyStroke.getKeyStroke(i, paramInt, true), "released");
        localInputMap.put(KeyStroke.getKeyStroke(i, 0, true), "released");
      }
    });
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.ui.UICommon
 * JD-Core Version:    0.7.0.1
 */