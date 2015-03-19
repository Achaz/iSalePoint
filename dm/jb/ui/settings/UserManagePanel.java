package dm.jb.ui.settings;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.db.objects.UserInfoRow;
import dm.jb.db.objects.UserInfoTableDef;
import dm.jb.ext.RFIDReadListener;
import dm.jb.ext.RFIDReader;
import dm.jb.op.UserAccessParam;
import dm.jb.ui.MainWindow;
import dm.jb.ui.common.HelpButton;
import dm.jb.ui.common.ShortKeyCommon;
import dm.tools.db.DBException;
import dm.tools.db.DBRow;
import dm.tools.db.Db;
import dm.tools.dbui.ActionObject;
import dm.tools.dbui.DBUIActionAdapter;
import dm.tools.dbui.DBUIContainerImpl;
import dm.tools.dbui.ValidationException;
import dm.tools.ui.AbstractMainPanel;
import dm.tools.ui.ShuttlePane;
import dm.tools.ui.UICommon;
import dm.tools.ui.components.DBUIResultPanel;
import dm.tools.ui.components.JBSearchButton;
import dm.tools.ui.components.JBStringTextField;
import dm.tools.ui.components.Validator;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import org.jdesktop.swingx.JXPanel;

public class UserManagePanel
  extends AbstractMainPanel
  implements Validator
{
  private static UserManagePanel _mInstance = null;
  private DBUIContainerImpl _mDBUIContainer = null;
  private JBStringTextField _mUserId = null;
  private JBStringTextField _mUserName = null;
  private ShuttlePane<UserAccessParam> _mAccessList = null;
  private JPasswordField _mPassword = null;
  private JPasswordField _mRetypePassword = null;
  private JBStringTextField _mRFID = null;
  private JCheckBox _mLocked = null;
  
  private UserManagePanel()
  {
    initUI();
    this._mDBUIContainer.addValidator(this);
    RFIDReader localRFIDReader = RFIDReader._mInstance;
    localRFIDReader.addReadListener(new RFIDReadListener()
    {
      public void dataRead(String paramAnonymousString)
      {
        if (!UserManagePanel.this.isVisible()) {
          return;
        }
        UserManagePanel.this._mRFID.setText(paramAnonymousString);
      }
    });
  }
  
  public static UserManagePanel getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new UserManagePanel();
    }
    return _mInstance;
  }
  
  public void setDefaultFocus()
  {
    this._mUserId.requestFocusInWindow();
  }
  
  public JPanel getPanel()
  {
    return this;
  }
  
  public void windowDisplayed()
  {
    this._mAccessList.setToList(null);
    this._mAccessList.setFromList(UserAccessParam.getAccessListAsArrayList());
    this._mDBUIContainer.resetAttributes();
    this._mUserId.setEnabled(true);
    this._mPassword.setText("");
    this._mRetypePassword.setText("");
  }
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("pref:grow", "10px,pref,20px,30px,20px,30px,10px");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(1, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getUserDetailsPanel(), localCellConstraints);
    localCellConstraints.xywh(1, 4, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    add(getActionButtonPanel(), localCellConstraints);
    localCellConstraints.xywh(1, 5, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(new JSeparator(), localCellConstraints);
    localCellConstraints.xywh(1, 6, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(getButtonPanel(), localCellConstraints);
  }
  
  private JPanel getUserDetailsPanel()
  {
    JXPanel localJXPanel = new JXPanel();
    FormLayout localFormLayout = new FormLayout("10px,90px,10px,100px,3px,40px,60px,3px,40px,10px,150px,10px", "25px,10px,25px,10px,25px,10px,25px,10px,25px,20px,200px");
    localJXPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    int i = 1;
    JLabel localJLabel = new JLabel("Employee Id : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJXPanel.add(localJLabel, localCellConstraints);
    this._mUserId = ((JBStringTextField)this._mDBUIContainer.createComponentForAttribute("USER_ID", "Employee Id"));
    ShortKeyCommon.shortKeyForLabels(localJLabel, 73, this._mUserId);
    this._mUserId.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 114) {
          UserManagePanel.this.userIdSearchClicked();
        }
      }
    });
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJXPanel.add(this._mUserId, localCellConstraints);
    this._mUserId.setMinLength(6);
    this._mUserId.setMaxLength(9);
    JBSearchButton localJBSearchButton = new JBSearchButton(false);
    localCellConstraints.xywh(6, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJXPanel.add(localJBSearchButton, localCellConstraints);
    localJBSearchButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        UserManagePanel.this.userIdSearchClicked();
      }
    });
    this._mLocked = new JCheckBox("Locked");
    localCellConstraints.xywh(11, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJXPanel.add(this._mLocked, localCellConstraints);
    i += 2;
    localJLabel = new JLabel("Full Name : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJXPanel.add(localJLabel, localCellConstraints);
    this._mUserName = ((JBStringTextField)this._mDBUIContainer.createComponentForAttribute("USER_NAME", "Full Name"));
    ShortKeyCommon.shortKeyForLabels(localJLabel, 78, this._mUserName);
    localCellConstraints.xywh(4, i, 4, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJXPanel.add(this._mUserName, localCellConstraints);
    localJBSearchButton = new JBSearchButton(false);
    localCellConstraints.xywh(9, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJXPanel.add(localJBSearchButton, localCellConstraints);
    localJBSearchButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        UserManagePanel.this.userNameSearchClicked();
      }
    });
    this._mUserName.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 114) {
          UserManagePanel.this.userNameSearchClicked();
        }
      }
    });
    i += 2;
    localJLabel = new JLabel("Password : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJXPanel.add(localJLabel, localCellConstraints);
    this._mPassword = new JPasswordField();
    localCellConstraints.xywh(4, i, 4, 1, CellConstraints.FILL, CellConstraints.FILL);
    this._mPassword.setBackground(UICommon.MANDATORY_COLOR);
    localJXPanel.add(this._mPassword, localCellConstraints);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 80, this._mPassword);
    i += 2;
    localJLabel = new JLabel("Retype : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJXPanel.add(localJLabel, localCellConstraints);
    this._mRetypePassword = new JPasswordField();
    this._mRetypePassword.setBackground(UICommon.MANDATORY_COLOR);
    localCellConstraints.xywh(4, i, 4, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJXPanel.add(this._mRetypePassword, localCellConstraints);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 84, this._mRetypePassword);
    i += 2;
    localJLabel = new JLabel("RFID : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJXPanel.add(localJLabel, localCellConstraints);
    this._mRFID = ((JBStringTextField)this._mDBUIContainer.createComponentForAttribute("RFID", "RFID"));
    localCellConstraints.xywh(4, i, 4, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJXPanel.add(this._mRFID, localCellConstraints);
    localJBSearchButton = new JBSearchButton(false);
    localCellConstraints.xywh(9, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJXPanel.add(localJBSearchButton, localCellConstraints);
    localJBSearchButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        UserManagePanel.this.rfidSearchClicked();
      }
    });
    this._mRFID.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 114) {
          UserManagePanel.this.rfidSearchClicked();
        }
      }
    });
    ShortKeyCommon.shortKeyForLabels(localJLabel, 70, this._mRFID);
    i++;
    localJLabel = new JLabel("Access Allowed ");
    localCellConstraints.xywh(11, i, 1, 1, CellConstraints.CENTER, CellConstraints.CENTER);
    localJXPanel.add(localJLabel, localCellConstraints);
    i++;
    localJLabel = new JLabel("Access : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.TOP);
    localJXPanel.add(localJLabel, localCellConstraints);
    this._mAccessList = new ShuttlePane(false);
    localCellConstraints.xywh(4, i, 8, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJXPanel.add(this._mAccessList, localCellConstraints);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 83, this._mAccessList);
    return localJXPanel;
  }
  
  public JPanel getActionButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("10px,100px,20px,100px,20px,100px,20px,100px,10px:grow", "30px");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    JButton localJButton = (JButton)this._mDBUIContainer.createActionObject("Add", "Create", null);
    localJButton.setMnemonic(65);
    localJPanel.add(localJButton, localCellConstraints);
    ((ActionObject)localJButton).addDBUIActionListener(new DBUIActionAdapter()
    {
      public boolean beforeAction(ActionObject paramAnonymousActionObject)
      {
        long l = UserManagePanel.this.getSelectedAccess();
        UserInfoRow localUserInfoRow = (UserInfoRow)UserManagePanel.this._mDBUIContainer.getCurrentInstance();
        localUserInfoRow.setLocked(UserManagePanel.this._mLocked.isSelected());
        localUserInfoRow.setAccessParam(l);
        String str = new String(UserManagePanel.this._mPassword.getPassword());
        localUserInfoRow.setPassword(str);
        return true;
      }
      
      public void actionFailed(Object paramAnonymousObject)
      {
        UICommon.showError("Error creating user information.", "Error", MainWindow.instance);
        UserManagePanel.this._mUserId.requestFocusInWindow();
      }
      
      public void afterAction(ActionObject paramAnonymousActionObject)
      {
        UserManagePanel.this._mDBUIContainer.resetAttributes();
        UserManagePanel.this._mDBUIContainer.resetAttributes();
        UserManagePanel.this._mUserId.setEnabled(true);
        UserManagePanel.this._mUserId.requestFocusInWindow();
        UserManagePanel.this._mLocked.setSelected(false);
        UserManagePanel.this.setSelectedAccess(0L);
        UserManagePanel.this._mPassword.setText("");
        UserManagePanel.this._mRetypePassword.setText("");
        UICommon.showMessage("User created successfully.", "Success", MainWindow.instance);
      }
    });
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJButton = (JButton)this._mDBUIContainer.createActionObject("Update", "Update", null);
    localJPanel.add(localJButton, localCellConstraints);
    localJButton.setMnemonic(85);
    ((ActionObject)localJButton).addDBUIActionListener(new DBUIActionAdapter()
    {
      public boolean beforeAction(ActionObject paramAnonymousActionObject)
      {
        long l = UserManagePanel.this.getSelectedAccess();
        UserInfoRow localUserInfoRow = (UserInfoRow)UserManagePanel.this._mDBUIContainer.getCurrentInstance();
        localUserInfoRow.setLocked(UserManagePanel.this._mLocked.isSelected());
        localUserInfoRow.setAccessParam(l);
        String str = new String(UserManagePanel.this._mPassword.getPassword());
        localUserInfoRow.setPassword(str);
        return true;
      }
      
      public void actionFailed(Object paramAnonymousObject)
      {
        UICommon.showMessage("Error updating user information.", "Error", MainWindow.instance);
        UserManagePanel.this._mUserId.requestFocusInWindow();
      }
      
      public void afterAction(ActionObject paramAnonymousActionObject)
      {
        UICommon.showMessage("User updated successfully.", "Success", MainWindow.instance);
        UserManagePanel.this._mDBUIContainer.resetAttributes();
        UserManagePanel.this._mUserId.setEnabled(true);
        UserManagePanel.this._mDBUIContainer.resetAttributes();
        UserManagePanel.this._mUserId.setEnabled(true);
        UserManagePanel.this._mUserId.requestFocusInWindow();
        UserManagePanel.this._mLocked.setSelected(false);
        UserManagePanel.this.setSelectedAccess(0L);
        UserManagePanel.this._mPassword.setText("");
        UserManagePanel.this._mRetypePassword.setText("");
      }
    });
    localCellConstraints.xywh(6, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJButton = (JButton)this._mDBUIContainer.createActionObject("Delete", "Delete", null);
    localJPanel.add(localJButton, localCellConstraints);
    localJButton.setMnemonic(68);
    ((ActionObject)localJButton).addDBUIActionListener(new DBUIActionAdapter()
    {
      public void actionFailed(Object paramAnonymousObject)
      {
        UICommon.showMessage("Error deleting user information.", "Error", MainWindow.instance);
        UserManagePanel.this._mUserId.requestFocusInWindow();
      }
      
      public void afterAction(ActionObject paramAnonymousActionObject)
      {
        UserManagePanel.this._mDBUIContainer.resetAttributes();
        UserManagePanel.this._mUserId.setEnabled(true);
        UserManagePanel.this._mUserId.requestFocusInWindow();
        UserManagePanel.this._mLocked.setSelected(false);
        UserManagePanel.this.setSelectedAccess(0L);
        UserManagePanel.this._mPassword.setText("");
        UserManagePanel.this._mRetypePassword.setText("");
        UICommon.showMessage("User deleted successfully.", "Success", MainWindow.instance);
      }
    });
    localJButton = new JButton("Reset");
    localCellConstraints.xywh(8, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJButton, localCellConstraints);
    localJButton.setMnemonic(82);
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        UserManagePanel.this._mDBUIContainer.resetAttributes();
        UserManagePanel.this._mUserId.requestFocusInWindow();
        UserManagePanel.this._mLocked.setSelected(false);
        UserManagePanel.this.setSelectedAccess(0L);
        UserManagePanel.this._mUserId.setEnabled(true);
        UserManagePanel.this._mPassword.setText("");
        UserManagePanel.this._mRetypePassword.setText("");
      }
    });
    return localJPanel;
  }
  
  private JPanel getButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("10px, 100px,20px,100px,10px", "30px");
    CellConstraints localCellConstraints = new CellConstraints();
    localJPanel.setLayout(localFormLayout);
    Object localObject = new JButton(" Close ");
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        UserManagePanel.this.closeWindow();
      }
    });
    localObject = new HelpButton("ISP_EMP_MANAGE");
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JButton)localObject).setMnemonic(72);
    return localJPanel;
  }
  
  public void validateValue()
    throws ValidationException
  {
    String str1 = this._mUserId.getText().trim();
    try
    {
      ArrayList localArrayList = UserInfoTableDef.getInstance().getAllValuesWithWhereClause("USER_ID LIKE '" + str1 + "'");
      if ((this._mUserId.isEnabled()) && (localArrayList != null) && (localArrayList.size() > 0))
      {
        this._mUserId.requestFocusInWindow();
        throw new ValidationException("Duplicate Employee Id.", "Error", null);
      }
    }
    catch (DBException localDBException)
    {
      this._mUserId.requestFocusInWindow();
      throw new ValidationException("Internal error searching for duplicate user.", "Error", null);
    }
    String str2 = new String(this._mPassword.getPassword());
    str2 = str2.trim();
    if ((str2.length() < 6) || (str2.length() > 32))
    {
      this._mPassword.requestFocusInWindow();
      throw new ValidationException("Password should have a minimum of 6 characters and maximum of 32 characters.", "Error", null);
    }
    String str3 = new String(this._mRetypePassword.getPassword());
    if (!str3.equals(str2))
    {
      this._mRetypePassword.requestFocusInWindow();
      throw new ValidationException("Passwords does not match.", "Error", null);
    }
  }
  
  public long getSelectedAccess()
  {
    ArrayList localArrayList = this._mAccessList.getSelectedObjects();
    long l = 0L;
    Iterator localIterator = localArrayList.iterator();
    while (localIterator.hasNext())
    {
      UserAccessParam localUserAccessParam = (UserAccessParam)localIterator.next();
      l |= localUserAccessParam.getCode();
    }
    return l;
  }
  
  private void setSelectedAccess(long paramLong)
  {
    ArrayList localArrayList1 = new ArrayList();
    ArrayList localArrayList2 = new ArrayList();
    for (int i = 0; i < UserAccessParam.accessList.length; i++)
    {
      UserAccessParam localUserAccessParam = UserAccessParam.accessList[i];
      if ((localUserAccessParam.getCode() & paramLong) == localUserAccessParam.getCode()) {
        localArrayList2.add(localUserAccessParam);
      } else {
        localArrayList1.add(localUserAccessParam);
      }
    }
    this._mAccessList.setToList(localArrayList2);
    this._mAccessList.setFromList(localArrayList1);
  }
  
  private void userIdSearchClicked()
  {
    String str = this._mUserId.getText().trim();
    str = Db.getSearchFormattedString(str);
    str = str + "%";
    try
    {
      ArrayList localArrayList = UserInfoTableDef.getInstance().getAllValuesWithWhereClause("USER_ID LIKE '" + str + "'");
      if ((localArrayList == null) || (localArrayList.size() == 0))
      {
        this._mUserId.requestFocusInWindow();
        UICommon.showError("Not mathing user found.", "No data", MainWindow.instance);
        return;
      }
      String[] arrayOfString = { "USER_ID", "Employee Id", "USER_NAME", "Full Name", "RFID", "RFID" };
      DBUIResultPanel localDBUIResultPanel = new DBUIResultPanel(arrayOfString, MainWindow.instance);
      localDBUIResultPanel.setData(localArrayList);
      localDBUIResultPanel.setLocationRelativeTo(MainWindow.instance);
      localDBUIResultPanel.setTitle("Employees");
      localDBUIResultPanel.setVisible(true);
      this._mUserId.requestFocusInWindow();
      if (localDBUIResultPanel.isCancelled()) {
        return;
      }
      DBRow localDBRow = (DBRow)localDBUIResultPanel.getSelectedRow();
      this._mDBUIContainer.setCurrentInstance(localDBRow);
      UserInfoRow localUserInfoRow = (UserInfoRow)localDBRow;
      this._mLocked.setSelected(localUserInfoRow.isLocked());
      setSelectedAccess(localUserInfoRow.getAccessParam());
      this._mUserId.setEnabled(false);
      this._mPassword.setText(localUserInfoRow.getPassword());
      this._mRetypePassword.setText(localUserInfoRow.getPassword());
    }
    catch (DBException localDBException)
    {
      UICommon.showError("Internal error searching employee id.", "Internal Error", MainWindow.instance);
      return;
    }
  }
  
  private void rfidSearchClicked()
  {
    String str = this._mRFID.getText().trim();
    try
    {
      ArrayList localArrayList = UserInfoTableDef.getInstance().getAllValuesWithWhereClause("RFID LIKE '" + str + "%'");
      if ((localArrayList == null) || (localArrayList.size() == 0))
      {
        this._mUserId.requestFocusInWindow();
        UICommon.showError("No matching user found.", "No data", MainWindow.instance);
        return;
      }
      String[] arrayOfString = { "USER_ID", "Employee Id", "USER_NAME", "Full Name", "RFID", "RFID" };
      DBUIResultPanel localDBUIResultPanel = new DBUIResultPanel(arrayOfString, MainWindow.instance);
      localDBUIResultPanel.setData(localArrayList);
      localDBUIResultPanel.setLocationRelativeTo(MainWindow.instance);
      localDBUIResultPanel.setTitle("Employees");
      localDBUIResultPanel.setVisible(true);
      this._mRFID.requestFocusInWindow();
      if (localDBUIResultPanel.isCancelled()) {
        return;
      }
      DBRow localDBRow = (DBRow)localDBUIResultPanel.getSelectedRow();
      this._mDBUIContainer.setCurrentInstance(localDBRow);
      UserInfoRow localUserInfoRow = (UserInfoRow)localDBRow;
      this._mLocked.setSelected(localUserInfoRow.isLocked());
      setSelectedAccess(localUserInfoRow.getAccessParam());
      this._mPassword.setText(localUserInfoRow.getPassword());
      this._mRetypePassword.setText(localUserInfoRow.getPassword());
      this._mUserId.setEnabled(false);
    }
    catch (DBException localDBException)
    {
      UICommon.showError("Internal error searching employee id.", "Internal Error", MainWindow.instance);
      return;
    }
  }
  
  private void userNameSearchClicked()
  {
    String str = this._mUserName.getText().trim();
    str = Db.getSearchFormattedString(str);
    str = str + "%";
    try
    {
      ArrayList localArrayList = UserInfoTableDef.getInstance().getAllValuesWithWhereClause("USER_NAME LIKE '" + str + "'");
      if ((localArrayList == null) || (localArrayList.size() == 0))
      {
        this._mUserId.requestFocusInWindow();
        UICommon.showError("No matching user found.", "No data", MainWindow.instance);
        return;
      }
      String[] arrayOfString = { "USER_ID", "Employee Id", "USER_NAME", "Full Name", "RFID", "RFID" };
      DBUIResultPanel localDBUIResultPanel = new DBUIResultPanel(arrayOfString, MainWindow.instance);
      localDBUIResultPanel.setData(localArrayList);
      localDBUIResultPanel.setLocationRelativeTo(MainWindow.instance);
      localDBUIResultPanel.setTitle("Employees");
      localDBUIResultPanel.setVisible(true);
      this._mUserName.requestFocusInWindow();
      if (localDBUIResultPanel.isCancelled()) {
        return;
      }
      DBRow localDBRow = (DBRow)localDBUIResultPanel.getSelectedRow();
      this._mDBUIContainer.setCurrentInstance(localDBRow);
      UserInfoRow localUserInfoRow = (UserInfoRow)localDBRow;
      this._mLocked.setSelected(localUserInfoRow.isLocked());
      setSelectedAccess(localUserInfoRow.getAccessParam());
      this._mPassword.setText(localUserInfoRow.getPassword());
      this._mRetypePassword.setText(localUserInfoRow.getPassword());
      this._mUserId.setEnabled(false);
    }
    catch (DBException localDBException)
    {
      UICommon.showError("Internal error searching employee id.", "Internal Error", MainWindow.instance);
      return;
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.settings.UserManagePanel
 * JD-Core Version:    0.7.0.1
 */