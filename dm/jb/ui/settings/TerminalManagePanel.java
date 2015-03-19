package dm.jb.ui.settings;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.db.objects.SiteInfoRow;
import dm.jb.db.objects.SiteInfoTableDef;
import dm.jb.db.objects.StoreInfoRow;
import dm.jb.db.objects.StoreInfoTableDef;
import dm.jb.ui.MainWindow;
import dm.jb.ui.common.HelpButton;
import dm.jb.ui.common.ShortKeyCommon;
import dm.tools.db.DBException;
import dm.tools.db.DBRow;
import dm.tools.db.Db;
import dm.tools.dbui.ActionObject;
import dm.tools.dbui.DBUIActionAdapter;
import dm.tools.dbui.DBUIContainerImpl;
import dm.tools.dbui.DBUIObject;
import dm.tools.dbui.ValidationException;
import dm.tools.ui.AbstractMainPanel;
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
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

public class TerminalManagePanel
  extends AbstractMainPanel
  implements Validator
{
  private static TerminalManagePanel _mInstance = null;
  private DBUIContainerImpl _mDBUIContainer = null;
  private JBStringTextField _mIdComp = null;
  private JComboBox _mStores = null;
  private StoreInfoRow _mPrevStoreInfo = null;
  private JLabel label = null;
  
  private TerminalManagePanel()
  {
    initUI();
    this._mDBUIContainer.addValidator(this);
  }
  
  public static TerminalManagePanel getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new TerminalManagePanel();
    }
    return _mInstance;
  }
  
  public void setDefaultFocus()
  {
    this._mIdComp.requestFocusInWindow();
  }
  
  public JPanel getPanel()
  {
    return this;
  }
  
  public void windowDisplayed()
  {
    DBUIObject localDBUIObject = (DBUIObject)this._mStores;
    try
    {
      localDBUIObject.initSelf();
    }
    catch (DBException localDBException)
    {
      localDBException.printStackTrace();
    }
    this._mDBUIContainer.setCurrentInstance(null);
  }
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("10px,90px,10px,60px,3px,40px, 150px,10px:grow", "10px,25px,10px,25px,20px,30px,20px,30px,10px");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    int i = 2;
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.CENTER);
    this.label = new JLabel("Terminal Id : ");
    add(this.label, localCellConstraints);
    JComponent localJComponent1 = (JComponent)this._mDBUIContainer.createComponentForAttribute("SITE_ID", "Terminal Id");
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJComponent1, localCellConstraints);
    localJComponent1.setName("TERMINAL_MANAGE.TERMINAL_ID");
    localJComponent1.setToolTipText("Terminal Id");
    ShortKeyCommon.shortKeyForLabels(this.label, 73, localJComponent1);
    if ((localJComponent1 instanceof JBStringTextField))
    {
      localObject = (JBStringTextField)localJComponent1;
      ((JBStringTextField)localObject).setMandatory(true);
      this._mIdComp = ((JBStringTextField)localObject);
      ((JBStringTextField)localObject).setMaxLength(4);
      ((JBStringTextField)localObject).setMandatory(true);
      ((JBStringTextField)localObject).addKeyListener(new KeyAdapter()
      {
        public void keyPressed(KeyEvent paramAnonymousKeyEvent)
        {
          if (paramAnonymousKeyEvent.getKeyCode() == 114) {
            TerminalManagePanel.this.siteIdSearchClicked();
          }
        }
      });
    }
    Object localObject = new JBSearchButton(false);
    ((JButton)localObject).setMnemonic(74);
    localCellConstraints.xywh(6, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add((Component)localObject, localCellConstraints);
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        TerminalManagePanel.this.siteIdSearchClicked();
      }
    });
    ((JButton)localObject).setToolTipText("Search terminals by id");
    i += 2;
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.CENTER);
    this.label = new JLabel("Store : ");
    add(this.label, localCellConstraints);
    JComponent localJComponent2 = (JComponent)this._mDBUIContainer.createComponentForTable(StoreInfoTableDef.getInstance(), "Store field", (short)3, "STORE_ID", "STORE_ID", false);
    localCellConstraints.xywh(4, i, 4, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJComponent2, localCellConstraints);
    localJComponent2.setName("TERMINAL_MANAGE.STORES");
    localJComponent2.setBackground(UICommon.MANDATORY_COLOR);
    localJComponent2.setToolTipText("Store for the terminal");
    ShortKeyCommon.shortKeyForLabels(this.label, 79, localJComponent2);
    DBUIObject localDBUIObject = (DBUIObject)localJComponent2;
    this._mStores = ((JComboBox)localJComponent2);
    try
    {
      localDBUIObject.initSelf();
    }
    catch (DBException localDBException)
    {
      localDBException.printStackTrace();
    }
    i += 2;
    localCellConstraints.xywh(1, i, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getActionButtonPanel(), localCellConstraints);
    i++;
    localCellConstraints.xywh(1, i, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(new JSeparator(), localCellConstraints);
    i++;
    localCellConstraints.xywh(1, i, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getButtonPanel(), localCellConstraints);
    this._mDBUIContainer.addValidator(this);
  }
  
  public JPanel getActionButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("10px,100px,20px,100px,20px,100px,20px,100px,10px:grow", "30px");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    JComponent localJComponent = (JComponent)this._mDBUIContainer.createActionObject("Add", "Create", null);
    JButton localJButton1 = (JButton)localJComponent;
    localJButton1.setToolTipText("Create a new teminal");
    localJButton1.setName("TERMINAL_MANAGE.CREATE");
    localJButton1.setMnemonic(65);
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJComponent, localCellConstraints);
    ((ActionObject)localJComponent).addDBUIActionListener(new DBUIActionAdapter()
    {
      public void actionFailed(Object paramAnonymousObject)
      {
        UICommon.showError("Error creating terminal information.", "Error", MainWindow.instance);
        TerminalManagePanel.this._mIdComp.requestFocusInWindow();
      }
      
      public void afterAction(ActionObject paramAnonymousActionObject)
      {
        StoreInfoRow localStoreInfoRow = (StoreInfoRow)TerminalManagePanel.this._mStores.getSelectedItem();
        localStoreInfoRow.getSites().add((SiteInfoRow)TerminalManagePanel.this._mDBUIContainer.getCurrentInstance());
        UICommon.showMessage("Terminal created successfully.", "Success", MainWindow.instance);
      }
    });
    localJComponent = (JComponent)this._mDBUIContainer.createActionObject("Update", "Update", null);
    localJButton1 = (JButton)localJComponent;
    localJButton1.setToolTipText("Update terminal information");
    localJButton1.setMnemonic(85);
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJComponent, localCellConstraints);
    ((ActionObject)localJComponent).addDBUIActionListener(new DBUIActionAdapter()
    {
      public boolean beforeAction(ActionObject paramAnonymousActionObject)
      {
        int i = TerminalManagePanel.this._mStores.getItemCount();
        SiteInfoRow localSiteInfoRow = (SiteInfoRow)TerminalManagePanel.this._mDBUIContainer.getCurrentInstance();
        StoreInfoRow localStoreInfoRow = (StoreInfoRow)TerminalManagePanel.this._mStores.getSelectedItem();
        if (localStoreInfoRow.getStoreId() == localSiteInfoRow.getStoreId()) {
          TerminalManagePanel.this._mPrevStoreInfo = null;
        }
        for (int j = 0; j < i; j++)
        {
          localStoreInfoRow = (StoreInfoRow)TerminalManagePanel.this._mStores.getItemAt(j);
          if (localSiteInfoRow.getStoreId() == localStoreInfoRow.getStoreId())
          {
            TerminalManagePanel.this._mPrevStoreInfo = localStoreInfoRow;
            return true;
          }
        }
        return true;
      }
      
      public void actionFailed(Object paramAnonymousObject)
      {
        UICommon.showError("Error updating terminal information.", "Error", MainWindow.instance);
        TerminalManagePanel.this._mIdComp.requestFocusInWindow();
      }
      
      public void afterAction(ActionObject paramAnonymousActionObject)
      {
        if (TerminalManagePanel.this._mPrevStoreInfo != null)
        {
          TerminalManagePanel.this._mPrevStoreInfo.getSites().remove(TerminalManagePanel.this._mDBUIContainer.getCurrentInstance());
          StoreInfoRow localStoreInfoRow = (StoreInfoRow)TerminalManagePanel.this._mStores.getSelectedItem();
          localStoreInfoRow.getSites().add((SiteInfoRow)TerminalManagePanel.this._mDBUIContainer.getCurrentInstance());
        }
        UICommon.showMessage("Terminal Information updated successfully.", "Success", MainWindow.instance);
        TerminalManagePanel.this._mDBUIContainer.resetAttributes();
      }
    });
    localJComponent = (JComponent)this._mDBUIContainer.createActionObject("Delete", "Delete", null);
    localJButton1 = (JButton)localJComponent;
    localJButton1.setToolTipText("Remove terminal information");
    localJButton1.setMnemonic(68);
    localCellConstraints.xywh(6, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJComponent, localCellConstraints);
    ((ActionObject)localJComponent).addDBUIActionListener(new DBUIActionAdapter()
    {
      public boolean beforeAction(ActionObject paramAnonymousActionObject)
      {
        int i = TerminalManagePanel.this._mStores.getItemCount();
        SiteInfoRow localSiteInfoRow = (SiteInfoRow)TerminalManagePanel.this._mDBUIContainer.getCurrentInstance();
        for (int j = 0; j < i; j++)
        {
          StoreInfoRow localStoreInfoRow = (StoreInfoRow)TerminalManagePanel.this._mStores.getItemAt(j);
          if (localSiteInfoRow.getStoreId() == localStoreInfoRow.getStoreId())
          {
            TerminalManagePanel.this._mPrevStoreInfo = localStoreInfoRow;
            return true;
          }
        }
        return true;
      }
      
      public void actionFailed(Object paramAnonymousObject)
      {
        UICommon.showError("Error deleting terminal information.", "Error", MainWindow.instance);
        TerminalManagePanel.this._mIdComp.requestFocusInWindow();
      }
      
      public void afterAction(ActionObject paramAnonymousActionObject)
      {
        if (TerminalManagePanel.this._mPrevStoreInfo != null) {
          TerminalManagePanel.this._mPrevStoreInfo.getSites().remove(TerminalManagePanel.this._mDBUIContainer.getCurrentInstance());
        }
        TerminalManagePanel.this._mDBUIContainer.resetAttributes();
        UICommon.showMessage("Terminal Information deleted successfully.", "Success", MainWindow.instance);
      }
    });
    JButton localJButton2 = new JButton("Reset");
    localJButton2.setToolTipText("Reset the fields");
    localJButton2.setMnemonic(82);
    localCellConstraints.xywh(8, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJButton2, localCellConstraints);
    localJButton2.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        TerminalManagePanel.this._mDBUIContainer.resetAttributes();
        TerminalManagePanel.this.setDefaultFocus();
      }
    });
    return localJPanel;
  }
  
  private JPanel getButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("10px:grow,100px,20px,100px,10px", "pref:grow");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    JButton localJButton = new JButton("Close");
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJButton, localCellConstraints);
    localJButton.setToolTipText("Close the window");
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        TerminalManagePanel.this.closeWindow();
      }
    });
    HelpButton localHelpButton1 = new HelpButton("ISP_MANAGE_TERMINALS");
    localHelpButton1.setMnemonic(72);
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localHelpButton1, localCellConstraints);
    String str = "F1Action";
    final HelpButton localHelpButton2 = (HelpButton)localHelpButton1;
    InputMap localInputMap = getInputMap(2);
    localInputMap.put(KeyStroke.getKeyStroke("F1"), str);
    AbstractAction local8 = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        localHelpButton2.actionPerformed(paramAnonymousActionEvent);
      }
    };
    getActionMap().put(str, local8);
    localHelpButton1.setToolTipText("Help");
    return localJPanel;
  }
  
  public void validateValue()
    throws ValidationException
  {
    Object localObject = this._mStores.getSelectedItem();
    if (localObject == null) {
      throw new ValidationException("Store is not selected.", "Error", null);
    }
    StoreInfoRow localStoreInfoRow = (StoreInfoRow)localObject;
    String str = this._mIdComp.getText().trim();
    try
    {
      if (localStoreInfoRow.checkForDuplicatesite(str)) {
        throw new ValidationException("Duplicate terminal Id.", "Error", null);
      }
    }
    catch (DBException localDBException)
    {
      UICommon.showInternalError("Internal Error verifying duplicates.");
      return;
    }
  }
  
  private void siteIdSearchClicked()
  {
    String str1 = this._mIdComp.getText().trim();
    str1 = Db.getSearchFormattedString(str1);
    str1 = str1 + "%";
    StoreInfoRow localStoreInfoRow = (StoreInfoRow)this._mStores.getSelectedItem();
    try
    {
      String str2 = "SITE_ID LIKE '" + str1 + "'";
      if (localStoreInfoRow != null) {
        str2 = str2 + " AND STORE_ID=" + localStoreInfoRow.getStoreId();
      }
      ArrayList localArrayList = SiteInfoTableDef.getInstance().getAllValuesWithWhereClause(str2);
      if ((localArrayList == null) || (localArrayList.size() == 0))
      {
        this._mIdComp.requestFocusInWindow();
        UICommon.showError("No record found.", "No data", MainWindow.instance);
        return;
      }
      String[] arrayOfString = { "SITE_ID", "Site ID", "STORE_NAME", "Store Name" };
      DBUIResultPanel localDBUIResultPanel = new DBUIResultPanel(arrayOfString, MainWindow.instance);
      localDBUIResultPanel.setData(localArrayList);
      localDBUIResultPanel.setLocationRelativeTo(MainWindow.instance);
      localDBUIResultPanel.setTitle("Sites");
      localDBUIResultPanel.setVisible(true);
      this._mIdComp.requestFocusInWindow();
      if (localDBUIResultPanel.isCancelled()) {
        return;
      }
      DBRow localDBRow = (DBRow)localDBUIResultPanel.getSelectedRow();
      this._mDBUIContainer.setCurrentInstance(localDBRow);
    }
    catch (DBException localDBException)
    {
      UICommon.showError("Internal error searching store name.", "Internal Error", MainWindow.instance);
      return;
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.settings.TerminalManagePanel
 * JD-Core Version:    0.7.0.1
 */