package dm.jb.ui.settings;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
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
import dm.tools.dbui.ValidationException;
import dm.tools.ui.AbstractMainPanel;
import dm.tools.ui.UICommon;
import dm.tools.ui.components.DBUIResultPanel;
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
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

public class StoreManagePanel
  extends AbstractMainPanel
  implements Validator
{
  private static StoreManagePanel _mInstance = null;
  private DBUIContainerImpl _mDBUIContainer = null;
  private JBStringTextField _mNameComp = null;
  private JBStringTextField _mCodeComp = null;
  private JLabel label = null;
  
  public static StoreManagePanel getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new StoreManagePanel();
    }
    return _mInstance;
  }
  
  private StoreManagePanel()
  {
    initUI();
  }
  
  public void setDefaultFocus()
  {
    this._mNameComp.requestFocusInWindow();
  }
  
  public JPanel getPanel()
  {
    return this;
  }
  
  public void windowDisplayed()
  {
    this._mDBUIContainer.setCurrentInstance(null);
  }
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("10px,90px,10px,80px,3px, 40px, 160px,3px,40px, 10px:grow", "10px,25px,10px,25px,10px,100px,10px,25px,20px,30px, 20px,30px,10px");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    int i = 2;
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.CENTER);
    this.label = new JLabel("Store Code : ");
    add(this.label, localCellConstraints);
    JComponent localJComponent1 = (JComponent)this._mDBUIContainer.createComponentForAttribute("STORE_CODE", "Store Code");
    ShortKeyCommon.shortKeyForLabels(this.label, 67, localJComponent1);
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJComponent1, localCellConstraints);
    localJComponent1.setName("STORE_MANAGE.CODE");
    if ((localJComponent1 instanceof JBStringTextField))
    {
      localObject1 = (JBStringTextField)localJComponent1;
      ((JBStringTextField)localObject1).setMaxLength(3);
      ((JBStringTextField)localObject1).setMinLength(3);
      ((JBStringTextField)localObject1).setRunTimeValidation(false);
      ((JBStringTextField)localObject1).setMandatory(true);
      this._mCodeComp = ((JBStringTextField)localObject1);
      this._mCodeComp.setToolTipText("Store code");
      this.label.setLabelFor(this._mCodeComp);
      ((JBStringTextField)localObject1).addKeyListener(new KeyAdapter()
      {
        public void keyPressed(KeyEvent paramAnonymousKeyEvent)
        {
          if (paramAnonymousKeyEvent.getKeyCode() == 114) {
            StoreManagePanel.this.searchStoreCodeClicked();
          }
        }
      });
    }
    Object localObject1 = new JBSearchButton(false);
    ((JButton)localObject1).setToolTipText("Search by store code");
    ((JButton)localObject1).setMnemonic(74);
    localCellConstraints.xywh(6, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add((Component)localObject1, localCellConstraints);
    ((JButton)localObject1).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        StoreManagePanel.this.searchStoreCodeClicked();
      }
    });
    i += 2;
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.CENTER);
    this.label = new JLabel("Store Name : ");
    add(this.label, localCellConstraints);
    JComponent localJComponent2 = (JComponent)this._mDBUIContainer.createComponentForAttribute("NAME", "Store Name");
    localJComponent2.setToolTipText("Store name");
    ShortKeyCommon.shortKeyForLabels(this.label, 78, localJComponent2);
    localCellConstraints.xywh(4, i, 4, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJComponent2, localCellConstraints);
    localJComponent2.setName("STORE_MANAGE.NAME");
    if ((localJComponent2 instanceof JBStringTextField))
    {
      localObject2 = (JBStringTextField)localJComponent2;
      ((JBStringTextField)localObject2).setMaxLength(128);
      ((JBStringTextField)localObject2).setRunTimeValidation(false);
      ((JBStringTextField)localObject2).setMandatory(true);
      this._mNameComp = ((JBStringTextField)localObject2);
      ((JBStringTextField)localObject2).addKeyListener(new KeyAdapter()
      {
        public void keyPressed(KeyEvent paramAnonymousKeyEvent)
        {
          if (paramAnonymousKeyEvent.getKeyCode() == 114) {
            StoreManagePanel.this.searchStoreNameClicked();
          }
        }
      });
    }
    localObject1 = new JBSearchButton(false);
    ((JButton)localObject1).setToolTipText("Search by store name");
    ((JButton)localObject1).setMnemonic(90);
    localCellConstraints.xywh(9, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add((Component)localObject1, localCellConstraints);
    ((JButton)localObject1).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        StoreManagePanel.this.searchStoreNameClicked();
      }
    });
    i += 2;
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.TOP);
    this.label = new JLabel("Address : ");
    add(this.label, localCellConstraints);
    Object localObject2 = (JComponent)this._mDBUIContainer.createComponentForAttribute("ADDRESS", "Store Adress", (short)1, false);
    ((JComponent)localObject2).setToolTipText("Store address");
    ShortKeyCommon.shortKeyForLabels(this.label, 69, (JComponent)localObject2);
    localCellConstraints.xywh(4, i, 4, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(new JScrollPane((Component)localObject2), localCellConstraints);
    if ((localObject2 instanceof JBTextArea))
    {
      localObject3 = (JBTextArea)localObject2;
      ((JBTextArea)localObject3).setMaxLength(512);
      ((JBTextArea)localObject3).setMinLength(0);
      ((JBTextArea)localObject3).setRunTimeValidation(false);
      ((JBTextArea)localObject3).setMandatory(true);
    }
    ((JComponent)localObject2).setName("STORE_MANAGE.ADDRESS");
    i += 2;
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.CENTER);
    this.label = new JLabel("Phone : ");
    add(this.label, localCellConstraints);
    Object localObject3 = (JComponent)this._mDBUIContainer.createComponentForAttribute("PHONE", "Store Phone", (short)2, false);
    ((JComponent)localObject3).setToolTipText("Store phone");
    ShortKeyCommon.shortKeyForLabels(this.label, 80, (JComponent)localObject3);
    localCellConstraints.xywh(4, i, 4, 1, CellConstraints.FILL, CellConstraints.FILL);
    add((Component)localObject3, localCellConstraints);
    ((JComponent)localObject3).setName("STORE_MANAGE.PHONE");
    i += 2;
    localCellConstraints.xywh(1, i, localFormLayout.getColumnCount(), 1, CellConstraints.LEFT, CellConstraints.FILL);
    add(getActionButtonPanel(), localCellConstraints);
    i++;
    localCellConstraints.xywh(1, i, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(new JSeparator(), localCellConstraints);
    i++;
    localCellConstraints.xywh(1, i, localFormLayout.getColumnCount(), 1, CellConstraints.RIGHT, CellConstraints.FILL);
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
    JButton localJButton = (JButton)localJComponent;
    localJButton.setName("STORE_MANAGE.CREATE");
    localJButton.setToolTipText("Create new store");
    localJButton.setMnemonic(65);
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJComponent, localCellConstraints);
    ((ActionObject)localJComponent).addDBUIActionListener(new DBUIActionAdapter()
    {
      public void actionFailed(Object paramAnonymousObject)
      {
        UICommon.showMessage("Error creating store information.", "Error", MainWindow.instance);
        StoreManagePanel.this._mNameComp.requestFocusInWindow();
      }
      
      public void afterAction(ActionObject paramAnonymousActionObject)
      {
        UICommon.showMessage("Store information created successfully.", "Success", MainWindow.instance);
      }
    });
    localJComponent = (JComponent)this._mDBUIContainer.createActionObject("Update", "Update", null);
    localJButton = (JButton)localJComponent;
    localJButton.setToolTipText("Update store information");
    localJButton.setMnemonic(85);
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJComponent, localCellConstraints);
    ((ActionObject)localJComponent).addDBUIActionListener(new DBUIActionAdapter()
    {
      public void actionFailed(Object paramAnonymousObject)
      {
        UICommon.showMessage("Error updating store information.", "Error", MainWindow.instance);
        StoreManagePanel.this._mNameComp.requestFocusInWindow();
      }
      
      public void afterAction(ActionObject paramAnonymousActionObject)
      {
        UICommon.showMessage("Store information updated successfully.", "Success", MainWindow.instance);
        StoreManagePanel.this._mDBUIContainer.resetAttributes();
      }
    });
    localJComponent = (JComponent)this._mDBUIContainer.createActionObject("Delete", "Delete", null);
    localJButton = (JButton)localJComponent;
    localJButton.setMnemonic(68);
    localCellConstraints.xywh(6, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJComponent, localCellConstraints);
    ((ActionObject)localJComponent).addDBUIActionListener(new DBUIActionAdapter()
    {
      public void actionFailed(Object paramAnonymousObject)
      {
        UICommon.showMessage("Error deleting store information.", "Error", MainWindow.instance);
        StoreManagePanel.this._mNameComp.requestFocusInWindow();
      }
      
      public void afterAction(ActionObject paramAnonymousActionObject)
      {
        StoreManagePanel.this._mDBUIContainer.resetAttributes();
        UICommon.showMessage("Store information deleted successfully.", "Success", MainWindow.instance);
      }
    });
    localJButton.setToolTipText("Remove the store");
    localCellConstraints.xywh(8, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJButton = new JButton("Reset");
    localJButton.setMnemonic(82);
    localJPanel.add(localJButton, localCellConstraints);
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        StoreManagePanel.this._mDBUIContainer.resetAttributes();
        StoreManagePanel.this.setDefaultFocus();
      }
    });
    localJButton.setToolTipText("Reset the fields and prepare for new store record.");
    return localJPanel;
  }
  
  private JPanel getButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("10px:grow,100px,20px,100px,10px", "30px");
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
        StoreManagePanel.this.closeWindow();
      }
    });
    HelpButton localHelpButton1 = new HelpButton("ISP_MANAGE_STORES");
    localHelpButton1.setMnemonic(72);
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localHelpButton1, localCellConstraints);
    String str = "F1Action";
    final HelpButton localHelpButton2 = (HelpButton)localHelpButton1;
    localHelpButton2.setToolTipText("Help");
    InputMap localInputMap = getInputMap(2);
    localInputMap.put(KeyStroke.getKeyStroke("F1"), str);
    AbstractAction local10 = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        localHelpButton2.actionPerformed(paramAnonymousActionEvent);
      }
    };
    getActionMap().put(str, local10);
    return localJPanel;
  }
  
  public void validateValue()
    throws ValidationException
  {
    String str1 = this._mNameComp.getText().trim();
    String str2 = this._mCodeComp.getText().trim();
    DBRow localDBRow = this._mDBUIContainer.getCurrentInstance();
    if (localDBRow != null)
    {
      if ((localDBRow.getValue("NAME") != null) && (localDBRow.getValue("NAME").equals(str1))) {
        str1 = null;
      }
      if ((localDBRow.getValue("STORE_CODE") != null) && (localDBRow.getValue("STORE_CODE").equals(str2))) {
        str2 = null;
      }
    }
    try
    {
      if (StoreInfoTableDef.getInstance().isDuplicate(str2))
      {
        this._mCodeComp.requestFocusInWindow();
        throw new ValidationException("Store information with the same store code already exist.", "Error", null);
      }
    }
    catch (DBException localDBException)
    {
      setDefaultFocus();
      throw new ValidationException("Internal error validating input values.", "Internal Error", null);
    }
  }
  
  private void searchStoreNameClicked()
  {
    String str = this._mNameComp.getText().trim();
    str = Db.getSearchFormattedString(str);
    str = str + "%";
    try
    {
      ArrayList localArrayList = StoreInfoTableDef.getInstance().getAllValuesWithWhereClause("NAME LIKE '" + str + "'");
      if ((localArrayList == null) || (localArrayList.size() == 0))
      {
        this._mNameComp.requestFocusInWindow();
        UICommon.showError("No record found.", "No data", MainWindow.instance);
        return;
      }
      String[] arrayOfString = { "NAME", "Store Name", "STORE_CODE", "Store Code" };
      DBUIResultPanel localDBUIResultPanel = new DBUIResultPanel(arrayOfString, MainWindow.instance);
      localDBUIResultPanel.setData(localArrayList);
      localDBUIResultPanel.setLocationRelativeTo(MainWindow.instance);
      localDBUIResultPanel.setTitle("Stores");
      localDBUIResultPanel.setVisible(true);
      this._mNameComp.requestFocusInWindow();
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
  
  private void searchStoreCodeClicked()
  {
    String str = this._mCodeComp.getText().trim();
    str = Db.getSearchFormattedString(str);
    str = str + "%";
    try
    {
      ArrayList localArrayList = StoreInfoTableDef.getInstance().getAllValuesWithWhereClause("STORE_CODE LIKE '" + str + "'");
      if ((localArrayList == null) || (localArrayList.size() == 0))
      {
        this._mNameComp.requestFocusInWindow();
        UICommon.showError("No record found.", "No data", MainWindow.instance);
        return;
      }
      String[] arrayOfString = { "NAME", "Store Name", "STORE_CODE", "Store Code" };
      DBUIResultPanel localDBUIResultPanel = new DBUIResultPanel(arrayOfString, MainWindow.instance);
      localDBUIResultPanel.setData(localArrayList);
      localDBUIResultPanel.setLocationRelativeTo(MainWindow.instance);
      localDBUIResultPanel.setTitle("Stores");
      localDBUIResultPanel.setVisible(true);
      this._mCodeComp.requestFocusInWindow();
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
 * Qualified Name:     dm.jb.ui.settings.StoreManagePanel
 * JD-Core Version:    0.7.0.1
 */