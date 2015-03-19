package dm.jb.ui.settings;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.db.objects.UserInfoRow;
import dm.tools.db.DBConnection;
import dm.tools.ui.UICommon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.PrintStream;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRootPane;
import javax.swing.JSeparator;

public class PasswordChangeDialog
  extends JDialog
  implements WindowListener
{
  private JPasswordField _mCurrentPassword = null;
  private JPasswordField _mNewPassword = null;
  private JPasswordField _mNewPasswordRetype = null;
  private String _mOldPassword = null;
  private boolean _mValueOK = false;
  private UserInfoRow _mUser = null;
  private DBConnection _mConnection = null;
  
  public PasswordChangeDialog(JFrame paramJFrame, String paramString1, boolean paramBoolean1, boolean paramBoolean2, String paramString2, UserInfoRow paramUserInfoRow, DBConnection paramDBConnection)
  {
    super(paramJFrame, paramString1, paramBoolean1);
    setTitle("Change password");
    initUI();
    pack();
    setResizable(false);
    addWindowListener(this);
    this._mOldPassword = paramString2;
    setLocationRelativeTo(paramJFrame);
    this._mUser = paramUserInfoRow;
    this._mConnection = paramDBConnection;
  }
  
  private void initUI()
  {
    JPanel localJPanel1 = (JPanel)getContentPane();
    FormLayout localFormLayout = new FormLayout("10px, 110px, 10px, 150px:grow, 10px", "10px, 25px, 10px, 25px, 10px, 25px, 10px, 25px, 20px, 30px, 10px");
    localJPanel1.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    JLabel localJLabel = new JLabel("You need to change the password now.");
    localCellConstraints.xywh(2, 2, 3, 1, CellConstraints.CENTER, CellConstraints.FILL);
    localJPanel1.add(localJLabel, localCellConstraints);
    localJLabel = new JLabel("Current password :");
    localCellConstraints.xywh(2, 4, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel1.add(localJLabel, localCellConstraints);
    this._mCurrentPassword = new JPasswordField();
    localCellConstraints.xywh(4, 4, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel1.add(this._mCurrentPassword, localCellConstraints);
    localJLabel = new JLabel("New password :");
    localCellConstraints.xywh(2, 6, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel1.add(localJLabel, localCellConstraints);
    this._mNewPassword = new JPasswordField();
    localCellConstraints.xywh(4, 6, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel1.add(this._mNewPassword, localCellConstraints);
    localJLabel = new JLabel("Retype password :");
    localCellConstraints.xywh(2, 8, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel1.add(localJLabel, localCellConstraints);
    this._mNewPasswordRetype = new JPasswordField();
    localCellConstraints.xywh(4, 8, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel1.add(this._mNewPasswordRetype, localCellConstraints);
    localCellConstraints.xywh(1, 9, 4, 1, CellConstraints.FILL, CellConstraints.CENTER);
    localJPanel1.add(new JSeparator(), localCellConstraints);
    JPanel localJPanel2 = new JPanel();
    localCellConstraints.xywh(1, 10, 4, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel1.add(localJPanel2, localCellConstraints);
    localFormLayout = new FormLayout("pref:grow, 100px, pref:grow, 100px, pref:grow", "30px:grow");
    localJPanel2.setLayout(localFormLayout);
    JButton localJButton = new JButton("Apply");
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel2.add(localJButton, localCellConstraints);
    localJButton.getRootPane().setDefaultButton(localJButton);
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PasswordChangeDialog.this.applyClicked();
      }
    });
    localJButton = new JButton("Close");
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel2.add(localJButton, localCellConstraints);
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PasswordChangeDialog.this.dispose();
      }
    });
  }
  
  public void windowClosed(WindowEvent paramWindowEvent) {}
  
  public void windowClosing(WindowEvent paramWindowEvent) {}
  
  public void windowDeactivated(WindowEvent paramWindowEvent) {}
  
  public void windowDeiconified(WindowEvent paramWindowEvent) {}
  
  public void windowIconified(WindowEvent paramWindowEvent) {}
  
  public void windowOpened(WindowEvent paramWindowEvent) {}
  
  public void windowActivated(WindowEvent paramWindowEvent)
  {
    removeWindowListener(this);
    this._mCurrentPassword.requestFocusInWindow();
  }
  
  public boolean validateAndSetPassword()
  {
    setVisible(true);
    return this._mValueOK;
  }
  
  private void applyClicked()
  {
    String str1 = new String(this._mCurrentPassword.getPassword());
    String str2 = new String(this._mNewPassword.getPassword());
    String str3 = new String(this._mNewPasswordRetype.getPassword());
    if (str1.length() == 0)
    {
      UICommon.showError("Current password cannot be empty", "Error", this);
      this._mCurrentPassword.requestFocusInWindow();
      return;
    }
    if (str2.length() == 0)
    {
      UICommon.showError("New password cannot be empty", "Error", this);
      this._mNewPassword.requestFocusInWindow();
      return;
    }
    if (str3.length() == 0)
    {
      UICommon.showError("New password cannot be empty", "Error", this);
      this._mNewPasswordRetype.requestFocusInWindow();
      return;
    }
    if (!str3.equals(str2))
    {
      UICommon.showError("The passwords do not match", "Error", this);
      this._mNewPasswordRetype.requestFocusInWindow();
      return;
    }
    if (!str1.equals(this._mOldPassword))
    {
      UICommon.showError("You typed old password wrong.", "Error", this);
      this._mCurrentPassword.requestFocusInWindow();
      return;
    }
    System.err.println("Change the password");
    dispose();
    this._mValueOK = true;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.settings.PasswordChangeDialog
 * JD-Core Version:    0.7.0.1
 */