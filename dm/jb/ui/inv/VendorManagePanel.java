package dm.jb.ui.inv;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.db.objects.VendorTableDef;
import dm.jb.ui.MainWindow;
import dm.jb.ui.common.HelpButton;
import dm.jb.ui.res.MessageResourceUtils;
import dm.jb.ui.res.ResourceUtils;
import dm.tools.db.DBException;
import dm.tools.db.DBRow;
import dm.tools.db.Db;
import dm.tools.dbui.ActionObject;
import dm.tools.dbui.DBUIActionAdapter;
import dm.tools.dbui.DBUIContainerImpl;
import dm.tools.dbui.ValidationException;
import dm.tools.ui.AbstractMainPanel;
import dm.tools.ui.UICommon;
import dm.tools.ui.components.DBUIResultPanel;
import dm.tools.ui.components.JBActionButton;
import dm.tools.ui.components.JBPhoneField;
import dm.tools.ui.components.JBSearchButton;
import dm.tools.ui.components.JBStringTextField;
import dm.tools.ui.components.JBTextArea;
import dm.tools.ui.components.Validator;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import org.jdesktop.swingx.JXButton;

public class VendorManagePanel
  extends AbstractMainPanel
  implements Validator
{
  public static VendorManagePanel _mInstance = new VendorManagePanel();
  private DBUIContainerImpl _mDBUIContainer = null;
  private JBStringTextField _mVendorName = null;
  private JBTextArea _mVendorAddress = null;
  private JBPhoneField _mPhone = null;
  
  private VendorManagePanel()
  {
    initUI();
    this._mDBUIContainer.addValidator(this);
  }
  
  public void setDefaultFocus()
  {
    this._mVendorName.requestFocusInWindow();
  }
  
  public JPanel getPanel()
  {
    return this;
  }
  
  public void windowDisplayed()
  {
    this._mVendorName.requestFocusInWindow();
    this._mDBUIContainer.resetAttributes();
  }
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("10px,100px,10px,200px,3px,40px,10px:grow", "10px,25px,10px,100px,10px,25px,20px,30px,20px,30px,10px");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    int i = 2;
    JLabel localJLabel = new JLabel();
    ResourceUtils.setLabelString("VENDOR_MANAGE_NAME", localJLabel);
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mVendorName = ((JBStringTextField)this._mDBUIContainer.createComponentForAttribute("VENDOR_NAME", "Vendor Name"));
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mVendorName, localCellConstraints);
    this._mVendorName.setMandatory(true);
    this._mVendorName.setMaxLength(63);
    this._mVendorName.setName("VENDOR_MANAGE.NAME");
    JBSearchButton localJBSearchButton = new JBSearchButton(false);
    localJBSearchButton.setMnemonic(90);
    localCellConstraints.xywh(6, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJBSearchButton, localCellConstraints);
    localJBSearchButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        VendorManagePanel.this.searchVendorByName();
      }
    });
    i += 2;
    localJLabel = new JLabel();
    ResourceUtils.setLabelString("VENDOR_MANAGE_ADDRESS", localJLabel);
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.TOP);
    add(localJLabel, localCellConstraints);
    this._mVendorAddress = ((JBTextArea)this._mDBUIContainer.createComponentForAttribute("VENDOR_ADDRESS", "Vendor Address", (short)1, false));
    this._mVendorAddress.setMaxLength(512);
    this._mVendorAddress.setName("VENDOR_MANAGE.ADDRESS");
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(new JScrollPane(this._mVendorAddress), localCellConstraints);
    i += 2;
    localJLabel = new JLabel();
    ResourceUtils.setLabelString("VENDOR_MANAGE_PHONE", localJLabel);
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mPhone = ((JBPhoneField)this._mDBUIContainer.createComponentForAttribute("VENDOR_PHONE", "Phone", (short)2, false));
    this._mPhone.setName("VENDOR_MANAGE.PHONE");
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mPhone, localCellConstraints);
    localJBSearchButton = new JBSearchButton(false);
    localJBSearchButton.setMnemonic(71);
    localCellConstraints.xywh(6, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJBSearchButton, localCellConstraints);
    localJBSearchButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        VendorManagePanel.this.searchVendorByPhone();
      }
    });
    i += 2;
    localCellConstraints.xywh(1, i, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getActionPanel(), localCellConstraints);
    i++;
    localCellConstraints.xywh(1, i, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(new JSeparator(), localCellConstraints);
    i++;
    localCellConstraints.xywh(1, i, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getButtonPanel(), localCellConstraints);
  }
  
  public void validateValue()
    throws ValidationException
  {}
  
  private JPanel getActionPanel()
  {
    JPanel localJPanel = new JPanel();
    localJPanel.setLayout(new FormLayout("10px,100px,20px,100px,20px,100px,20px,100px,10px:grow", "30px"));
    CellConstraints localCellConstraints = new CellConstraints();
    JBActionButton localJBActionButton = (JBActionButton)this._mDBUIContainer.createActionObject(ResourceUtils.getString("ADD_BTN"), "CREATE", null);
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJBActionButton, localCellConstraints);
    localJBActionButton.setMnemonic(65);
    localJBActionButton.addDBUIActionListener(new DBUIActionAdapter()
    {
      public void actionFailed(Object paramAnonymousObject)
      {
        UICommon.showResErrorMessage("VENDOR_CREATE_ERROR", MainWindow.instance);
        VendorManagePanel.this._mVendorName.requestFocusInWindow();
      }
      
      public void afterAction(ActionObject paramAnonymousActionObject)
      {
        VendorManagePanel.this._mDBUIContainer.resetAttributes();
        VendorManagePanel.this._mVendorName.requestFocusInWindow();
        UICommon.showResSuccessMessage("VENDOR_CREATE_SUCCESS", MainWindow.instance);
      }
    });
    localJBActionButton.setName("VENDOR_MANAGE.CREATE");
    localJBActionButton = (JBActionButton)this._mDBUIContainer.createActionObject(ResourceUtils.getString("UPDATE_BTN"), "UPDATE", null);
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJBActionButton, localCellConstraints);
    localJBActionButton.setMnemonic(85);
    localJBActionButton.addDBUIActionListener(new DBUIActionAdapter()
    {
      public void actionFailed(Object paramAnonymousObject)
      {
        UICommon.showResErrorMessage("VENDOR_UPDATE_ERROR", MainWindow.instance);
        VendorManagePanel.this._mVendorName.requestFocusInWindow();
      }
      
      public void afterAction(ActionObject paramAnonymousActionObject)
      {
        UICommon.showResSuccessMessage("VENDOR_UPDATE_SUCCESS", MainWindow.instance);
      }
    });
    localJBActionButton = (JBActionButton)this._mDBUIContainer.createActionObject(ResourceUtils.getString("DELETE_BTN"), "DELETE", null);
    localCellConstraints.xywh(6, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJBActionButton, localCellConstraints);
    localJBActionButton.setMnemonic(68);
    localJBActionButton.addDBUIActionListener(new DBUIActionAdapter()
    {
      public boolean beforeAction(ActionObject paramAnonymousActionObject)
      {
        int i = UICommon.showQuestion(MessageResourceUtils.getString("VENDOR_DELETE_CONFIRM"), MessageResourceUtils.getString("VENDOR_DELETE_TITLE"), MainWindow.instance);
        return i == 1;
      }
      
      public void actionFailed(Object paramAnonymousObject)
      {
        UICommon.showResErrorMessage("VENDOR_DELETE_ERROR", MainWindow.instance);
        VendorManagePanel.this._mVendorName.requestFocusInWindow();
      }
      
      public void afterAction(ActionObject paramAnonymousActionObject)
      {
        VendorManagePanel.this._mDBUIContainer.resetAttributes();
        UICommon.showResSuccessMessage("VENDOR_DELETE_SUCCESS", MainWindow.instance);
      }
    });
    JXButton localJXButton = new JXButton(ResourceUtils.getString("RESET_BTN"));
    localCellConstraints.xywh(8, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.setMnemonic(82);
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        VendorManagePanel.this._mVendorName.requestFocusInWindow();
        VendorManagePanel.this._mDBUIContainer.resetAttributes();
      }
    });
    return localJPanel;
  }
  
  private JPanel getButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    localJPanel.setLayout(new FormLayout("10px:grow,100px,20px,100px,10px", "30px"));
    CellConstraints localCellConstraints = new CellConstraints();
    Object localObject = new JXButton(ResourceUtils.getString("CLOSE_BTN"));
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JXButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        VendorManagePanel.this.closeWindow();
      }
    });
    localObject = new HelpButton("ISP_MANAGE_VENDORS");
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JXButton)localObject).setMnemonic(72);
    ((JXButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent) {}
    });
    return localJPanel;
  }
  
  private void searchVendorByName()
  {
    String str = this._mVendorName.getText().trim();
    str = Db.getSearchFormattedString(str);
    str = str + "%";
    try
    {
      ArrayList localArrayList = VendorTableDef.getInstance().getAllValuesWithWhereClause("VENDOR_NAME LIKE '" + str + "'");
      if ((localArrayList == null) || (localArrayList.size() == 0))
      {
        this._mVendorName.requestFocusInWindow();
        UICommon.showResErrorMessage("VENDOR_NO_RECORD_FOUND", MainWindow.instance);
        return;
      }
      String[] arrayOfString = { "VENDOR_NAME", ResourceUtils.getString("VENDOR_NAME_LBL"), "VENDOR_PHONE", ResourceUtils.getString("VENDOR_PHONE_LBL") };
      DBUIResultPanel localDBUIResultPanel = new DBUIResultPanel(arrayOfString, MainWindow.instance);
      localDBUIResultPanel.setData(localArrayList);
      localDBUIResultPanel.setLocationRelativeTo(MainWindow.instance);
      localDBUIResultPanel.setVisible(true);
      this._mVendorName.requestFocusInWindow();
      if (localDBUIResultPanel.isCancelled()) {
        return;
      }
      DBRow localDBRow = (DBRow)localDBUIResultPanel.getSelectedRow();
      this._mDBUIContainer.setCurrentInstance(localDBRow);
    }
    catch (DBException localDBException)
    {
      UICommon.showInternalError(MessageResourceUtils.getString("VENDOR_VENDOR_SEARCH_INTERNAL_ERROR"));
      return;
    }
  }
  
  private void searchVendorByPhone()
  {
    String str = this._mPhone.getText().trim();
    str = Db.getSearchFormattedString(str);
    str = str + "%";
    try
    {
      ArrayList localArrayList = VendorTableDef.getInstance().getAllValuesWithWhereClause("VENDOR_PHONE LIKE '" + str + "'");
      if ((localArrayList == null) || (localArrayList.size() == 0))
      {
        this._mPhone.requestFocusInWindow();
        UICommon.showResErrorMessage("VENDOR_NO_RECORD_FOUND", MainWindow.instance);
        return;
      }
      String[] arrayOfString = { "VENDOR_NAME", ResourceUtils.getString("VENDOR_NAME_LBL"), "VENDOR_PHONE", ResourceUtils.getString("VENDOR_PHONE_LBL") };
      DBUIResultPanel localDBUIResultPanel = new DBUIResultPanel(arrayOfString, MainWindow.instance);
      localDBUIResultPanel.setData(localArrayList);
      localDBUIResultPanel.setLocationRelativeTo(MainWindow.instance);
      localDBUIResultPanel.setVisible(true);
      this._mPhone.requestFocusInWindow();
      if (localDBUIResultPanel.isCancelled()) {
        return;
      }
      DBRow localDBRow = (DBRow)localDBUIResultPanel.getSelectedRow();
      this._mDBUIContainer.setCurrentInstance(localDBRow);
    }
    catch (DBException localDBException)
    {
      UICommon.showInternalError(MessageResourceUtils.getString("VENDOR_VENDOR_SEARCH_INTERNAL_ERROR"));
      return;
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.inv.VendorManagePanel
 * JD-Core Version:    0.7.0.1
 */