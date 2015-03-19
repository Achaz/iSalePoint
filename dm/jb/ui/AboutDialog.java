package dm.jb.ui;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.ui.res.ResourceUtils;
import dm.tools.ui.UICommon;
import dm.tools.utils.Config;
import dm.tools.utils.Registration;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class AboutDialog
  extends JDialog
{
  private static AboutDialog _mInstance = null;
  private JTextField regCode = null;
  
  public static AboutDialog getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new AboutDialog();
    }
    return _mInstance;
  }
  
  private AboutDialog()
  {
    super(MainWindow.instance, ResourceUtils.getString("ABOUT_ISALEPOINT"), true);
    initUI();
    pack();
    InputMap localInputMap = ((JPanel)getContentPane()).getInputMap(0);
    String str = "ESCAction";
    localInputMap.put(KeyStroke.getKeyStroke("ESCAPE"), str);
    AbstractAction local1 = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        AboutDialog.this.setVisible(false);
      }
    };
    ((JPanel)getContentPane()).getActionMap().put(str, local1);
    setLocationRelativeTo(MainWindow.instance);
    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent paramAnonymousWindowEvent)
      {
        if (MainWindow.instance == null) {
          System.exit(0);
        }
      }
    });
  }
  
  private void initUI()
  {
    JPanel localJPanel = (JPanel)getContentPane();
    FormLayout localFormLayout = new FormLayout("10px,pref,10px,200px:grow,10px", "10px,pref,10px,pref,20px,35px,10px");
    CellConstraints localCellConstraints = new CellConstraints();
    localJPanel.setLayout(localFormLayout);
    URL localURL = getClass().getResource("/dm/jb/images/jeBillingLogo.gif");
    ImageIcon localImageIcon = new ImageIcon(localURL, "Logo");
    JLabel localJLabel = new JLabel(localImageIcon);
    localCellConstraints.xywh(2, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    localJLabel = new JLabel("iSalePoint v1.0 beta");
    localCellConstraints.xywh(4, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    localCellConstraints.xywh(1, 4, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(getRegistrationCodePanel(), localCellConstraints);
    localCellConstraints.xywh(2, 6, 3, 1, CellConstraints.CENTER, CellConstraints.FILL);
    localJPanel.add(getButtonPanel(), localCellConstraints);
  }
  
  private JPanel getButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("90px", "pref:grow");
    CellConstraints localCellConstraints = new CellConstraints();
    localJPanel.setLayout(localFormLayout);
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    JButton localJButton = new JButton("Close");
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (MainWindow.instance == null) {
          System.exit(0);
        }
        AboutDialog.this.setVisible(false);
      }
    });
    localJPanel.add(localJButton, localCellConstraints);
    return localJPanel;
  }
  
  private JPanel getRegistrationCodePanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("10px,90px,10px,200px,10px,100px, 10px:grow", "25px,10px,25px");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    JLabel localJLabel = new JLabel("Serial No. : ");
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    JTextField localJTextField = new JTextField();
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJTextField, localCellConstraints);
    String str = Registration.getInstance().getComputerSerialCode();
    localJTextField.setText(str);
    localJTextField.setEditable(false);
    localJLabel = new JLabel("Reg. No. : ");
    localCellConstraints.xywh(2, 3, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this.regCode = new JTextField();
    localCellConstraints.xywh(4, 3, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this.regCode, localCellConstraints);
    JButton localJButton = new JButton("Register");
    localCellConstraints.xywh(6, 3, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJButton, localCellConstraints);
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        AboutDialog.this.registrationClicked();
      }
    });
    return localJPanel;
  }
  
  private void registrationClicked()
  {
    String str = this.regCode.getText().trim();
    if (str.length() == 0)
    {
      UICommon.showError("Registration code cannot be empty. Please obtain code.", "Registration failed.", MainWindow.instance);
      this.regCode.requestFocusInWindow();
      return;
    }
    if (!Registration.getInstance().isAppRegistered(str))
    {
      UICommon.showError("You are not registered.", "Registration Failed.", MainWindow.instance);
      this.regCode.requestFocusInWindow();
      return;
    }
    Config.INSTANCE.setAttrib("JB_CONFIG.REG_CODE.VALUE", str);
    Config.INSTANCE.printXML();
    UICommon.showMessage("Registration succesful. You can restart the application for it to take effect.", "Success", MainWindow.instance);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.AboutDialog
 * JD-Core Version:    0.7.0.1
 */