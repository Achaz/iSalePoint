package dm.jb.ui.inv;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.db.objects.DeptRow;
import dm.jb.db.objects.DeptTableDef;
import dm.jb.ui.MainWindow;
import dm.jb.ui.common.HelpButton;
import dm.jb.ui.common.ShortKeyCommon;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import org.jdesktop.swingx.JXButton;

public class DeptManagePanel
  extends AbstractMainPanel
  implements Validator
{
  private static DeptManagePanel _mInstance = null;
  private DBUIContainerImpl _mDBUIContainer = null;
  private JBStringTextField _mDeptName = null;
  private JBTextArea _mDescription = null;
  
  public static DeptManagePanel getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new DeptManagePanel();
    }
    return _mInstance;
  }
  
  public DeptManagePanel()
  {
    initUI();
    this._mDBUIContainer.addValidator(this);
  }
  
  public void setDefaultFocus() {}
  
  public JPanel getPanel()
  {
    return this;
  }
  
  public void windowDisplayed()
  {
    this._mDeptName.requestFocusInWindow();
    this._mDBUIContainer.resetAttributes();
  }
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("10px,90px,10px,200px,3px,40px,10px:grow", "10px,25px,10px,100px,20px,30px,20px,30px,10px");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    JLabel localJLabel = new JLabel("Name : ");
    localCellConstraints.xywh(2, 2, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mDeptName = ((JBStringTextField)this._mDBUIContainer.createComponentForAttribute("DEPT_NAME", "Department Name"));
    this._mDeptName.setName("DEPT_MANAGE.DEPT_NAME");
    ShortKeyCommon.shortKeyForLabels(localJLabel, 78, this._mDeptName);
    this._mDeptName.setToolTipText("Department Name");
    localCellConstraints.xywh(4, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mDeptName, localCellConstraints);
    this._mDeptName.setMinLength(3);
    this._mDeptName.setMaxLength(33);
    this._mDeptName.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 114) {
          DeptManagePanel.this.deptSearchClicked();
        }
      }
    });
    JBSearchButton localJBSearchButton = new JBSearchButton(false);
    localJBSearchButton.setName("DEPT_MANAGE.SEARCH");
    localCellConstraints.xywh(6, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJBSearchButton, localCellConstraints);
    localJBSearchButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        DeptManagePanel.this.deptSearchClicked();
      }
    });
    localJBSearchButton.setToolTipText("Search departments");
    localJLabel = new JLabel("Description : ");
    localCellConstraints.xywh(2, 4, 1, 1, CellConstraints.RIGHT, CellConstraints.TOP);
    add(localJLabel, localCellConstraints);
    this._mDescription = ((JBTextArea)this._mDBUIContainer.createComponentForAttribute("DEPT_DETAILS", "Department Details", (short)1, false));
    this._mDescription.setName("DEPT_MANAGE.DESCRIPTION");
    this._mDescription.setToolTipText("Department description");
    ShortKeyCommon.shortKeyForLabels(localJLabel, 84, this._mDescription);
    localCellConstraints.xywh(4, 4, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(new JScrollPane(this._mDescription), localCellConstraints);
    this._mDescription.setMaxLength(255);
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
    JBActionButton localJBActionButton = (JBActionButton)this._mDBUIContainer.createActionObject("Add", "CREATE", null);
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJBActionButton, localCellConstraints);
    localJBActionButton.setToolTipText("Create new department");
    localJBActionButton.setMnemonic(65);
    localJBActionButton.setName("DEPT_MANAGE.CREATE");
    localJBActionButton.addDBUIActionListener(new DBUIActionAdapter()
    {
      public void actionFailed(Object paramAnonymousObject)
      {
        if ((paramAnonymousObject instanceof Exception)) {
          ((Exception)paramAnonymousObject).printStackTrace();
        }
        UICommon.showError("Error creating department.", "Error", MainWindow.instance);
        DeptManagePanel.this._mDeptName.requestFocusInWindow();
      }
      
      public void afterAction(ActionObject paramAnonymousActionObject)
      {
        DeptManagePanel.this._mDBUIContainer.resetAttributes();
        DeptManagePanel.this._mDeptName.requestFocusInWindow();
        UICommon.showMessage("Department created successfully.", "Success", MainWindow.instance);
      }
    });
    localJBActionButton = (JBActionButton)this._mDBUIContainer.createActionObject("Update", "UPDATE", null);
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJBActionButton, localCellConstraints);
    localJBActionButton.setMnemonic(85);
    localJBActionButton.addDBUIActionListener(new DBUIActionAdapter()
    {
      public void actionFailed(Object paramAnonymousObject)
      {
        UICommon.showMessage("Error updating department.", "Error", MainWindow.instance);
        DeptManagePanel.this._mDeptName.requestFocusInWindow();
      }
      
      public void afterAction(ActionObject paramAnonymousActionObject)
      {
        UICommon.showMessage("Department updated successfully.", "Success", MainWindow.instance);
      }
    });
    localJBActionButton.setToolTipText("Update department");
    localJBActionButton = (JBActionButton)this._mDBUIContainer.createActionObject("Delete", "DELETE", null);
    localCellConstraints.xywh(6, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJBActionButton, localCellConstraints);
    localJBActionButton.setMnemonic(68);
    localJBActionButton.addDBUIActionListener(new DBUIActionAdapter()
    {
      public boolean beforeAction(ActionObject paramAnonymousActionObject)
      {
        int i = UICommon.showQuestion("Are you sure you want to delete the department?\n\nAll categories and products under the department will be removed to maintain the consistency.", "Confirm Deletion", MainWindow.instance);
        return i == 1;
      }
      
      public void actionFailed(Object paramAnonymousObject)
      {
        UICommon.showMessage("Error deleting department.", "Error", MainWindow.instance);
        DeptManagePanel.this._mDeptName.requestFocusInWindow();
      }
      
      public void afterAction(ActionObject paramAnonymousActionObject)
      {
        DeptManagePanel.this._mDBUIContainer.resetAttributes();
        UICommon.showMessage("Department deleted successfully.\n\nPlease cleanup all the stocks and other records that might be still referring to removed products, by clicking Manager > Cleanup menu item.", "Success", MainWindow.instance);
      }
    });
    localJBActionButton.setToolTipText("Remove department");
    JXButton localJXButton = new JXButton("Reset");
    localCellConstraints.xywh(8, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.setMnemonic(82);
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        DeptManagePanel.this._mDeptName.requestFocusInWindow();
        DeptManagePanel.this._mDBUIContainer.resetAttributes();
      }
    });
    localJXButton.setName("DEPT_MANAGE.RESET");
    localJXButton.setToolTipText("Clear the UI");
    return localJPanel;
  }
  
  private JPanel getButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    localJPanel.setLayout(new FormLayout("10px:grow,100px,20px,100px,10px", "30px"));
    CellConstraints localCellConstraints = new CellConstraints();
    Object localObject = new JXButton("Close");
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JXButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        DeptManagePanel.this.closeWindow();
      }
    });
    ((JXButton)localObject).setToolTipText("Close the window");
    localObject = new HelpButton("ISP_DEPT_MANAGE");
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JXButton)localObject).setMnemonic(72);
    ((JXButton)localObject).setToolTipText("Help");
    return localJPanel;
  }
  
  private void deptSearchClicked()
  {
    String[] arrayOfString = { "DEPT_NAME", "Department Name" };
    ArrayList localArrayList = null;
    try
    {
      String str = this._mDeptName.getText().trim();
      localArrayList = DeptTableDef.getInstance().getAllValuesWithWhereClause("DEPT_NAME LIKE '" + str + "%'");
      if (localArrayList == null)
      {
        UICommon.showError("No Data found.", "No Data", MainWindow.instance);
        return;
      }
    }
    catch (DBException localDBException)
    {
      UICommon.showError("Internal error searching department details.", "Internal Error", MainWindow.instance);
      return;
    }
    if (localArrayList.size() == 1)
    {
      this._mDBUIContainer.setCurrentInstance((DBRow)localArrayList.get(0));
      return;
    }
    DBUIResultPanel localDBUIResultPanel = new DBUIResultPanel(arrayOfString, MainWindow.instance);
    localDBUIResultPanel.setData(localArrayList);
    localDBUIResultPanel.setLocationRelativeTo(MainWindow.instance);
    localDBUIResultPanel.setTitle("Departments");
    localDBUIResultPanel.setVisible(true);
    this._mDeptName.requestFocusInWindow();
    if (localDBUIResultPanel.isCancelled()) {
      return;
    }
    DBRow localDBRow = (DBRow)localDBUIResultPanel.getSelectedRow();
    this._mDBUIContainer.setCurrentInstance(localDBRow);
  }
  
  public void validateValue()
    throws ValidationException
  {
    String str = this._mDeptName.getText().trim();
    DeptRow localDeptRow = (DeptRow)this._mDBUIContainer.getCurrentInstance();
    if ((localDeptRow != null) && (localDeptRow.getDeptName() != null) && (localDeptRow.getDeptName().equals(str))) {
      return;
    }
    try
    {
      if (DeptTableDef.getInstance().isDuplicateDept(str)) {
        throw new ValidationException("Duplicate department name.", "Error", "Error");
      }
    }
    catch (DBException localDBException)
    {
      throw new ValidationException("Internal Error verifying duplicate department names.", "Internal Error", "Internal Error");
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.inv.DeptManagePanel
 * JD-Core Version:    0.7.0.1
 */