package dm.jb.ui.settings;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.db.objects.WearehouseInfoRow;
import dm.jb.db.objects.WearehouseInfoTableDef;
import dm.jb.ui.MainWindow;
import dm.jb.ui.common.HelpButton;
import dm.jb.ui.common.ShortKeyCommon;
import dm.jb.ui.res.MessageResourceUtils;
import dm.jb.ui.res.ResourceUtils;
import dm.tools.db.DBException;
import dm.tools.db.DBRow;
import dm.tools.dbui.ActionObject;
import dm.tools.dbui.DBUIActionAdapter;
import dm.tools.dbui.DBUIContainerImpl;
import dm.tools.dbui.ValidationException;
import dm.tools.ui.AbstractMainPanel;
import dm.tools.ui.UICommon;
import dm.tools.ui.components.DBUIResultPanel;
import dm.tools.ui.components.JBActionButton;
import dm.tools.ui.components.JBSearchButton;
import dm.tools.ui.components.JBStringTextField;
import dm.tools.ui.components.JBTextArea;
import dm.tools.ui.components.Validator;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import org.jdesktop.swingx.JXButton;

public class WarehouseManagePanel
  extends AbstractMainPanel
  implements Validator
{
  private static WarehouseManagePanel _mInstance = null;
  private DBUIContainerImpl _mDBUIContainer = null;
  private JBStringTextField _mWareHouseName = null;
  private JBTextArea _mWareHouseAddress = null;
  
  private WarehouseManagePanel()
  {
    initUI();
    this._mDBUIContainer.addValidator(this);
  }
  
  public static WarehouseManagePanel getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new WarehouseManagePanel();
    }
    return _mInstance;
  }
  
  public void validateValue()
    throws ValidationException
  {
    String str1 = this._mWareHouseName.getText().trim();
    WearehouseInfoRow localWearehouseInfoRow = (WearehouseInfoRow)this._mDBUIContainer.getCurrentInstance();
    String str2 = localWearehouseInfoRow.getWearehouseName();
    if ((str2 == null) || (!str2.equals(str1))) {
      try
      {
        if (WearehouseInfoTableDef.getInstance().isDuplicateWearehouse(str1))
        {
          this._mWareHouseName.requestFocusInWindow();
          throw new ValidationException(MessageResourceUtils.getString("WH_DUPLICATE_NAME"), MessageResourceUtils.getString("ERROR_TITLE"), MessageResourceUtils.getString("ERROR_TITLE"));
        }
      }
      catch (DBException localDBException)
      {
        throw new ValidationException(MessageResourceUtils.getString("WH_DUPLICATE_NAME_INTERNAL_ERROR"), MessageResourceUtils.getString("INTERNAL_ERROR_TITLE"), MessageResourceUtils.getString("INTERNAL_ERROR_TITLE"));
      }
    }
  }
  
  public void setDefaultFocus()
  {
    this._mWareHouseName.requestFocusInWindow();
  }
  
  public JPanel getPanel()
  {
    return this;
  }
  
  public void windowDisplayed()
  {
    this._mWareHouseName.requestFocusInWindow();
    this._mDBUIContainer.resetAttributes();
    setDefaultFocus();
  }
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("10px,90px,10px,200px,3px,40px,10px:grow", "10px,25px,10px,100px,20px,30px,20px,30px,10px");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    JLabel localJLabel = new JLabel();
    ResourceUtils.setLabelString("WH_NAME", localJLabel);
    localCellConstraints.xywh(2, 2, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mWareHouseName = ((JBStringTextField)this._mDBUIContainer.createComponentForAttribute("WEAREHOUSE_NAME", ResourceUtils.getString("WH_NAME_TT")));
    localCellConstraints.xywh(4, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mWareHouseName, localCellConstraints);
    this._mWareHouseName.setToolTipText(ResourceUtils.getString("WH_NAME_TT"));
    ShortKeyCommon.shortKeyForLabels(localJLabel, 78, this._mWareHouseName);
    this._mWareHouseName.setMinLength(3);
    this._mWareHouseName.setMaxLength(32);
    this._mWareHouseName.setMandatory(true);
    this._mWareHouseName.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 114) {
          WarehouseManagePanel.this.warehouseSearchClicked();
        }
      }
    });
    JBSearchButton localJBSearchButton = new JBSearchButton(false);
    localJBSearchButton.setToolTipText(ResourceUtils.getString("WH_SEARCH_BTN_TT"));
    localJBSearchButton.setMnemonic(90);
    localCellConstraints.xywh(6, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJBSearchButton, localCellConstraints);
    localJBSearchButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        WarehouseManagePanel.this.warehouseSearchClicked();
      }
    });
    localJLabel = new JLabel();
    ResourceUtils.setLabelString("WH_ADDRESS", localJLabel);
    localCellConstraints.xywh(2, 4, 1, 1, CellConstraints.RIGHT, CellConstraints.TOP);
    add(localJLabel, localCellConstraints);
    this._mWareHouseAddress = ((JBTextArea)this._mDBUIContainer.createComponentForAttribute("WEAREHOUSE_ADDRESS", ResourceUtils.getString("WH_ADDRESS_TT"), (short)1, false));
    localCellConstraints.xywh(4, 4, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(new JScrollPane(this._mWareHouseAddress), localCellConstraints);
    this._mWareHouseAddress.setMaxLength(512);
    this._mWareHouseAddress.setToolTipText(ResourceUtils.getString("WH_ADDRESS_TT"));
    ShortKeyCommon.shortKeyForLabels(localJLabel, 69, this._mWareHouseAddress);
    localCellConstraints.xywh(1, 6, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getActionPanel(), localCellConstraints);
    localCellConstraints.xywh(1, 7, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(new JSeparator(), localCellConstraints);
    localCellConstraints.xywh(1, 8, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getButtonPanel(), localCellConstraints);
  }
  
  private JPanel getActionPanel()
  {
    JPanel localJPanel = new JPanel();
    localJPanel.setLayout(new FormLayout("10px,100px,20px,100px,20px,100px,20px,100px,10px:grow", "30px"));
    CellConstraints localCellConstraints = new CellConstraints();
    JBActionButton localJBActionButton = (JBActionButton)this._mDBUIContainer.createActionObject(ResourceUtils.getString("ADD_BTN"), "CREATE", null);
    localJBActionButton.setToolTipText(ResourceUtils.getString("WH_ADD_BTN_TT"));
    localJBActionButton.setMnemonic(65);
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJBActionButton, localCellConstraints);
    localJBActionButton.addDBUIActionListener(new DBUIActionAdapter()
    {
      public void actionFailed(Object paramAnonymousObject)
      {
        UICommon.showMessage(MessageResourceUtils.getString("WH_CREATE_ERROR"), MessageResourceUtils.getString("ERROR_TITLE"), MainWindow.instance);
        WarehouseManagePanel.this._mWareHouseName.requestFocusInWindow();
      }
      
      public void afterAction(ActionObject paramAnonymousActionObject)
      {
        WarehouseManagePanel.this._mDBUIContainer.resetAttributes();
        WarehouseManagePanel.this._mWareHouseName.requestFocusInWindow();
        UICommon.showMessage(MessageResourceUtils.getString("WH_CREATE_SUCCESS"), MessageResourceUtils.getString("SUCCESS_TITLE"), MainWindow.instance);
      }
    });
    localJBActionButton = (JBActionButton)this._mDBUIContainer.createActionObject(ResourceUtils.getString("UPDATE_BTN"), "UPDATE", null);
    localJBActionButton.setMnemonic(85);
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJBActionButton, localCellConstraints);
    localJBActionButton.addDBUIActionListener(new DBUIActionAdapter()
    {
      public void actionFailed(Object paramAnonymousObject)
      {
        UICommon.showMessage(MessageResourceUtils.getString("WH_UPDATE_ERROR"), MessageResourceUtils.getString("ERROR_TITLE"), MainWindow.instance);
        WarehouseManagePanel.this._mWareHouseName.requestFocusInWindow();
      }
      
      public void afterAction(ActionObject paramAnonymousActionObject)
      {
        UICommon.showMessage(MessageResourceUtils.getString("WH_UPDATE_SUCCESS"), MessageResourceUtils.getString("SUCCESS_TITLE"), MainWindow.instance);
      }
    });
    localJBActionButton.setToolTipText(ResourceUtils.getString("WH_UPDATE_BTN_TT"));
    localJBActionButton = (JBActionButton)this._mDBUIContainer.createActionObject(ResourceUtils.getString("DELETE_BTN"), "DELETE", null);
    localJBActionButton.setMnemonic(68);
    localCellConstraints.xywh(6, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJBActionButton, localCellConstraints);
    localJBActionButton.addDBUIActionListener(new DBUIActionAdapter()
    {
      public boolean beforeAction(ActionObject paramAnonymousActionObject)
      {
        int i = UICommon.showQuestion(MessageResourceUtils.getString("WH_DELETE_CONFIRM"), MessageResourceUtils.getString("WH_DELETE_CONFIRM_TITLE"), MainWindow.instance);
        return i == 1;
      }
      
      public void actionFailed(Object paramAnonymousObject)
      {
        UICommon.showMessage(MessageResourceUtils.getString("WH_DELETE_ERROR"), MessageResourceUtils.getString("ERROR_TITLE"), MainWindow.instance);
        WarehouseManagePanel.this._mWareHouseName.requestFocusInWindow();
      }
      
      public void afterAction(ActionObject paramAnonymousActionObject)
      {
        WarehouseManagePanel.this._mDBUIContainer.resetAttributes();
        UICommon.showMessage(MessageResourceUtils.getString("WH_DELETE_SUCCESS"), MessageResourceUtils.getString("SUCCESS_TITLE"), MainWindow.instance);
      }
    });
    localJBActionButton.setToolTipText(ResourceUtils.getString("WH_DELETE_BTN_TT"));
    JXButton localJXButton = new JXButton(ResourceUtils.getString("RESET_BTN"));
    localJXButton.setMnemonic(82);
    localCellConstraints.xywh(8, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        WarehouseManagePanel.this._mWareHouseName.requestFocusInWindow();
        WarehouseManagePanel.this._mDBUIContainer.resetAttributes();
      }
    });
    localJXButton.setToolTipText(ResourceUtils.getString("WH_RESET_BTN_TT"));
    return localJPanel;
  }
  
  private JPanel getButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    localJPanel.setLayout(new FormLayout("10px:grow,100px,20px,100px,10px", "30px"));
    CellConstraints localCellConstraints = new CellConstraints();
    Object localObject = new JXButton(ResourceUtils.getString("CLOSE_BTN"));
    ((JXButton)localObject).setToolTipText(ResourceUtils.getString("WH_CLOSE_BTN_TT"));
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JXButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        WarehouseManagePanel.this.closeWindow();
      }
    });
    localObject = new HelpButton("ISP_MANAGE_WAREHOUSE");
    ((JXButton)localObject).setMnemonic(72);
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    String str = "F1Action";
    final HelpButton localHelpButton = (HelpButton)localObject;
    InputMap localInputMap = getInputMap(2);
    localInputMap.put(KeyStroke.getKeyStroke("F1"), str);
    AbstractAction local8 = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        localHelpButton.actionPerformed(paramAnonymousActionEvent);
      }
    };
    getActionMap().put(str, local8);
    ((JXButton)localObject).setToolTipText(ResourceUtils.getString("HELP_BTN"));
    return localJPanel;
  }
  
  private void warehouseSearchClicked()
  {
    String[] arrayOfString = { "WEAREHOUSE_NAME", ResourceUtils.getString("WH_SEARCH_FIELD_WH_NAME") };
    ArrayList localArrayList = null;
    try
    {
      String str = this._mWareHouseName.getText().trim();
      localArrayList = WearehouseInfoTableDef.getInstance().getAllValuesWithWhereClause("WEAREHOUSE_NAME LIKE '" + str + "%'");
      if (localArrayList == null)
      {
        UICommon.showError(MessageResourceUtils.getString("WH_SEARCH_NO_DATA"), MessageResourceUtils.getString("NO_DATA_TITLE"), MainWindow.instance);
        return;
      }
    }
    catch (DBException localDBException)
    {
      UICommon.showError(MessageResourceUtils.getString("WH_SEARCH_INTERNAL_ERROR"), MessageResourceUtils.getString("INTERNAL_ERROR_TITLE"), MainWindow.instance);
      return;
    }
    DBUIResultPanel localDBUIResultPanel = new DBUIResultPanel(arrayOfString, MainWindow.instance);
    localDBUIResultPanel.setData(localArrayList);
    localDBUIResultPanel.setLocationRelativeTo(MainWindow.instance);
    localDBUIResultPanel.setTitle(ResourceUtils.getString("WH_SEARCH_WINDOW_TITLE"));
    localDBUIResultPanel.setVisible(true);
    this._mWareHouseName.requestFocusInWindow();
    if (localDBUIResultPanel.isCancelled()) {
      return;
    }
    DBRow localDBRow = (DBRow)localDBUIResultPanel.getSelectedRow();
    this._mDBUIContainer.setCurrentInstance(localDBRow);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.settings.WarehouseManagePanel
 * JD-Core Version:    0.7.0.1
 */