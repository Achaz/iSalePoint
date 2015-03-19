package dm.jb.ui;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.db.objects.SiteInfoRow;
import dm.jb.db.objects.SiteInfoTableDef;
import dm.jb.db.objects.StoreInfoRow;
import dm.jb.db.objects.StoreInfoTableDef;
import dm.jb.db.objects.UserInfoRow;
import dm.jb.db.objects.UserInfoTableDef;
import dm.jb.ext.RFIDReadListener;
import dm.jb.ext.RFIDReader;
import dm.jb.ui.res.MessageResourceUtils;
import dm.jb.ui.res.ResourceUtils;
import dm.tools.db.DBConnection;
import dm.tools.db.DBException;
import dm.tools.db.DBRow;
import dm.tools.db.Db;
import dm.tools.ui.UICommon;
import dm.tools.utils.CommonConfig;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRootPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;

public class LoginDialog
  extends JDialog
{
  private JTextField _mUserId = null;
  private JPasswordField _mPassword = null;
  private JComboBox _mStore = null;
  private JComboBox _mSite = null;
  private boolean _mLoginForNoSite = false;
  
  public LoginDialog(JFrame paramJFrame, String paramString, boolean paramBoolean)
  {
    super(paramJFrame, paramString, paramBoolean);
    setTitle(ResourceUtils.getString("LOGIN_DIALOG_TITLE"));
    initUI();
    setResizable(false);
    addWindowListener(new WindowAdapter()
    {
      public void windowOpened(WindowEvent paramAnonymousWindowEvent)
      {
        try
        {
          ArrayList localArrayList = StoreInfoTableDef.getInstance().getAllValues();
          if ((localArrayList == null) || (localArrayList.size() == 0)) {
            return;
          }
          Iterator localIterator = localArrayList.iterator();
          while (localIterator.hasNext())
          {
            DBRow localDBRow = (DBRow)localIterator.next();
            LoginDialog.this._mStore.addItem(localDBRow);
          }
        }
        catch (DBException localDBException)
        {
          UICommon.showError(MessageResourceUtils.getString("LOGIN_INTERNA_STORE_SITE_ERROR"), MessageResourceUtils.getString("INTERNAL_ERROR_TITLE"), (JFrame)null);
        }
      }
    });
    pack();
    setLocationRelativeTo(null);
    if (CommonConfig.getInstance().enableRFID)
    {
      RFIDReader localRFIDReader = RFIDReader._mInstance;
      final LoginDialog localLoginDialog = this;
      localRFIDReader.addReadListener(new RFIDReadListener()
      {
        public void dataRead(String paramAnonymousString)
        {
          if (!LoginDialog.this.isVisible()) {
            return;
          }
          try
          {
            UserInfoRow localUserInfoRow = UserInfoTableDef.getInstance().findByRFID(paramAnonymousString);
            if (localUserInfoRow == null)
            {
              UICommon.showError(MessageResourceUtils.getString("LOGIN_INVALID_AUTH_ERROR"), MessageResourceUtils.getString("LOGIN_TITLE"), localLoginDialog);
              return;
            }
            LoginDialog.this.loginWithUser(localUserInfoRow);
          }
          catch (DBException localDBException)
          {
            UICommon.showError(MessageResourceUtils.getString("LOGIN_INTERNAL_ACCESS_ERROR"), MessageResourceUtils.getString("INTERNAL_ERROR_TITLE"), localLoginDialog);
          }
        }
      });
    }
  }
  
  private void initUI()
  {
    JPanel localJPanel = (JPanel)getContentPane();
    FormLayout localFormLayout = new FormLayout("10px, 90px, 10px, 100px, pref:grow, 10px, 50px, 50px, 10px", "100px, 10px, 25px, 10px, 25px, 10px,25px,10px,25px, 30px, 30px, 10px");
    localJPanel.setLayout(localFormLayout);
    JLabel localJLabel1 = new JLabel(new ImageIcon(getClass().getResource("/dm/jb/images/login.gif")));
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(1, 1, 9, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJLabel1, localCellConstraints);
    this._mUserId = new JTextField();
    JLabel localJLabel2 = new JLabel();
    ResourceUtils.setLabelString("LOGIN_USER_ID_LBL", localJLabel2);
    localJLabel2.setLabelFor(this._mUserId);
    localCellConstraints.xywh(2, 3, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel2, localCellConstraints);
    localCellConstraints.xywh(4, 3, 4, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mUserId, localCellConstraints);
    this._mPassword = new JPasswordField();
    localJLabel2 = new JLabel();
    ResourceUtils.setLabelString("LOGIN_PASSWORD_LBL", localJLabel2);
    localJLabel2.setLabelFor(this._mPassword);
    localCellConstraints.xywh(2, 5, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel2, localCellConstraints);
    localCellConstraints.xywh(4, 5, 4, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mPassword, localCellConstraints);
    localJLabel2 = new JLabel();
    ResourceUtils.setLabelString("LOGIN_STORE_LBL", localJLabel2);
    localCellConstraints.xywh(2, 7, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel2, localCellConstraints);
    this._mStore = new JComboBox();
    localCellConstraints.xywh(4, 7, 4, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mStore, localCellConstraints);
    this._mStore.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        StoreInfoRow localStoreInfoRow = (StoreInfoRow)LoginDialog.this._mStore.getSelectedItem();
        try
        {
          LoginDialog.this._mSite.removeAllItems();
          ArrayList localArrayList = SiteInfoTableDef.getInstance().getAllValuesWithWhereClause(" STORE_ID=" + localStoreInfoRow.getStoreId());
          Iterator localIterator = localArrayList.iterator();
          while (localIterator.hasNext())
          {
            DBRow localDBRow = (DBRow)localIterator.next();
            LoginDialog.this._mSite.addItem(localDBRow);
          }
        }
        catch (DBException localDBException)
        {
          UICommon.showError("Internal error reading site information.", "Internal Error", (JFrame)null);
          return;
        }
      }
    });
    localJLabel2 = new JLabel();
    ResourceUtils.setLabelString("LOGIN_SITE_LBL", localJLabel2);
    localCellConstraints.xywh(2, 9, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel2, localCellConstraints);
    this._mSite = new JComboBox();
    localCellConstraints.xywh(4, 9, 4, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mSite, localCellConstraints);
    JSeparator localJSeparator = new JSeparator();
    localCellConstraints.xywh(1, 10, 9, 1, CellConstraints.FILL, CellConstraints.CENTER);
    localJPanel.add(localJSeparator, localCellConstraints);
    JButton localJButton = new JButton();
    ResourceUtils.setButtonString("OK_BTN", localJButton);
    localCellConstraints.xywh(2, 11, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJButton, localCellConstraints);
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        LoginDialog.this.okClicked();
      }
    });
    localJButton.getRootPane().setDefaultButton(localJButton);
    localJButton = new JButton();
    ResourceUtils.setButtonString("CLOSE_BTN", localJButton);
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        System.exit(0);
      }
    });
    localCellConstraints.xywh(4, 11, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJButton, localCellConstraints);
    localJButton = new JButton();
    ResourceUtils.setButtonString("HELP_BTN", localJButton);
    localCellConstraints.xywh(7, 11, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJButton, localCellConstraints);
  }
  
  public boolean showAndValidate(boolean paramBoolean)
  {
    this._mLoginForNoSite = paramBoolean;
    setVisible(true);
    return UserInfoTableDef.getCurrentUser() != null;
  }
  
  private void okClicked()
  {
    String str1 = this._mUserId.getText();
    int i = str1.length();
    if ((i == 0) || (i > 32))
    {
      UICommon.showError(MessageResourceUtils.getString("LOGIN_INVALID_USER_ID_ERROR"), MessageResourceUtils.getString("ERROR_TITLE"), this);
      this._mUserId.requestFocusInWindow();
      return;
    }
    String str2 = new String(this._mPassword.getPassword());
    i = str2.length();
    if ((i == 0) || (i > 32))
    {
      UICommon.showError(MessageResourceUtils.getString("LOGIN_INVALID_PASSWORD_ERROR"), MessageResourceUtils.getString("ERROR_TITLE"), this);
      this._mPassword.requestFocusInWindow();
      return;
    }
    try
    {
      UserInfoRow localUserInfoRow = UserInfoTableDef.getInstance().getUserInfoForUid(str1);
      if (localUserInfoRow == null)
      {
        UICommon.showError(MessageResourceUtils.getString("LOGIN_INVALID_AUTH_ERROR"), MessageResourceUtils.getString("LOGIN_TITLE"), this);
        return;
      }
      String str3 = localUserInfoRow.getPassword();
      if (!str2.equals(str3))
      {
        UICommon.showError(MessageResourceUtils.getString("LOGIN_INVALID_AUTH_ERROR"), MessageResourceUtils.getString("LOGIN_TITLE"), this);
        return;
      }
      if ((localUserInfoRow.isLocked()) && (localUserInfoRow.getAccessParam() != -1L))
      {
        UICommon.showError(MessageResourceUtils.getString("LOGIN_LOCK_ERROR"), MessageResourceUtils.getString("SESSION_LOCK_TITLE"), this);
        return;
      }
      if (!loginWithUser(localUserInfoRow)) {
        return;
      }
    }
    catch (DBException localDBException)
    {
      UICommon.showError(MessageResourceUtils.getString("LOGIN_DBD_ERROR"), MessageResourceUtils.getString("ERROR_TITLE"), this);
      return;
    }
  }
  
  public boolean createConnectionForTesting(String paramString)
  {
    return createConnectionForTesting(paramString, false);
  }
  
  public boolean createConnectionForTesting(String paramString, boolean paramBoolean)
  {
    DBConnection localDBConnection = Db.getConnection();
    try
    {
      UserInfoRow localUserInfoRow = UserInfoTableDef.getInstance().getUserInfoForUid(paramString);
      if (localUserInfoRow == null)
      {
        UICommon.showError(MessageResourceUtils.getString("LOGIN_INVALID_AUTH_ERROR"), MessageResourceUtils.getString("LOGIN_TITLE"), this);
        dispose();
        return false;
      }
      if ((localUserInfoRow.isLocked()) && (localUserInfoRow.getAccessParam() != -1L))
      {
        UICommon.showError(MessageResourceUtils.getString("LOGIN_LOCK_ERROR"), MessageResourceUtils.getString("SESSION_LOCK_TITLE"), this);
        return false;
      }
      UserInfoTableDef.setCurrentUser(localUserInfoRow);
      localDBConnection.openTrans();
      localUserInfoRow.setLocked(true);
      localUserInfoRow.update(true);
      localDBConnection.commit();
      ArrayList localArrayList1 = StoreInfoTableDef.getInstance().getAllValues();
      int i = -1;
      StoreInfoRow localStoreInfoRow = null;
      SiteInfoRow localSiteInfoRow = null;
      if (!paramBoolean)
      {
        if ((localArrayList1 != null) && (localArrayList1.size() > 0))
        {
          localStoreInfoRow = (StoreInfoRow)localArrayList1.get(0);
          i = localStoreInfoRow.getStoreId();
        }
        ArrayList localArrayList2 = SiteInfoTableDef.getInstance().getAllValuesWithWhereClause("STORE_ID=" + i);
        if ((localArrayList2 != null) && (localArrayList2.size() > 0)) {
          localSiteInfoRow = (SiteInfoRow)localArrayList2.get(0);
        }
      }
      loginWithUserAndStore(localStoreInfoRow, localSiteInfoRow);
    }
    catch (Exception localException)
    {
      localDBConnection.rollbackNoExp();
      System.err.println(localException);
      localException.printStackTrace();
      return false;
    }
    return true;
  }
  
  private boolean loginWithUser(UserInfoRow paramUserInfoRow)
  {
    StoreInfoRow localStoreInfoRow = (StoreInfoRow)this._mStore.getSelectedItem();
    SiteInfoRow localSiteInfoRow = (SiteInfoRow)this._mSite.getSelectedItem();
    if ((localSiteInfoRow == null) || (localStoreInfoRow == null))
    {
      if (!this._mLoginForNoSite)
      {
        UICommon.showError(MessageResourceUtils.getString("LOGIN_STORE_SITE_ERROR"), MessageResourceUtils.getString("ERROR_TITLE"), this);
        if (this._mStore == null) {
          this._mStore.requestFocusInWindow();
        } else {
          this._mSite.requestFocusInWindow();
        }
        return false;
      }
      if (paramUserInfoRow.getAccessParam() != 9223372036854775807L)
      {
        UICommon.showError(MessageResourceUtils.getString("LOGIN_STORE_SITE_SELECT_ERROR"), MessageResourceUtils.getString("ERROR_TITLE"), this);
        return false;
      }
      UICommon.showWarning(MessageResourceUtils.getString("LOGIN_ONLY_SERVER_CONFIG_WARNING"), MessageResourceUtils.getString("WARNING_TITLE"), this);
    }
    DBConnection localDBConnection = Db.getConnection();
    try
    {
      UserInfoTableDef.setCurrentUser(paramUserInfoRow);
      localDBConnection.openTrans();
      if (paramUserInfoRow.getAccessParam() != 9223372036854775807L) {
        paramUserInfoRow.setLocked(true);
      }
      paramUserInfoRow.update(true);
      localDBConnection.commit();
    }
    catch (DBException localDBException)
    {
      localDBConnection.rollbackNoExp();
      UICommon.showWarning(MessageResourceUtils.getString("LOGIN_USER_LOCK_ERROR"), MessageResourceUtils.getString("ERROR_TITLE"), this);
      return false;
    }
    loginWithUserAndStore(localStoreInfoRow, localSiteInfoRow);
    return true;
  }
  
  private void loginWithUserAndStore(StoreInfoRow paramStoreInfoRow, SiteInfoRow paramSiteInfoRow)
  {
    StoreInfoTableDef.setCurrentStore(paramStoreInfoRow);
    SiteInfoTableDef.setCurrentSite(paramSiteInfoRow);
    dispose();
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.LoginDialog
 * JD-Core Version:    0.7.0.1
 */