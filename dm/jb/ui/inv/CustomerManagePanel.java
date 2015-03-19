package dm.jb.ui.inv;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.IDateEditor;
import dm.jb.db.objects.CustomerAdvancedRow;
import dm.jb.db.objects.CustomerRow;
import dm.jb.db.objects.CustomerTableDef;
import dm.jb.db.objects.UserInfoRow;
import dm.jb.db.objects.UserInfoTableDef;
import dm.jb.ext.RFIDReadListener;
import dm.jb.ext.RFIDReader;
import dm.jb.ui.MainWindow;
import dm.jb.ui.billing.CustomerPanelInterface;
import dm.jb.ui.common.HelpButton;
import dm.jb.ui.common.ShortKeyCommon;
import dm.tools.db.DBConnection;
import dm.tools.db.DBException;
import dm.tools.db.DBRow;
import dm.tools.db.Db;
import dm.tools.dbui.ActionObject;
import dm.tools.dbui.DBUIActionAdapter;
import dm.tools.dbui.DBUIContainerImpl;
import dm.tools.ui.AbstractMainPanel;
import dm.tools.ui.ActionPanel;
import dm.tools.ui.QuickTask;
import dm.tools.ui.QuickTaskBox;
import dm.tools.ui.QuickTaskPane;
import dm.tools.ui.QuickTaskPane.TaskGroup;
import dm.tools.ui.UICommon;
import dm.tools.ui.components.DBUIResultPanel;
import dm.tools.ui.components.JBActionButton;
import dm.tools.ui.components.JBDBUIDateField;
import dm.tools.ui.components.JBDBUIIntegerTextField;
import dm.tools.ui.components.JBPhoneField;
import dm.tools.ui.components.JBSearchButton;
import dm.tools.ui.components.JBStringTextField;
import dm.tools.ui.components.JBTextArea;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import org.jdesktop.swingx.JXButton;

public class CustomerManagePanel
  extends AbstractMainPanel
{
  private static CustomerManagePanel _mInstance = null;
  private ArrayList<CustomerPanelInterface> _mListeners = new ArrayList();
  private JBStringTextField _mCustomerName = null;
  private JBStringTextField _mBarcode = null;
  private JBStringTextField _mRFID = null;
  private JBPhoneField _mCustomerPhone = null;
  private JBDBUIIntegerTextField _mLoyalty = null;
  private JTextArea _mCustomerAddress = null;
  private JButton _mOKBtn = null;
  private JBDBUIDateField _mJoinDate = null;
  private DBUIContainerImpl _mDBUIContainer = null;
  private JBDBUIIntegerTextField _mCustomerId = null;
  private static QuickTaskPane _mTaskPane = null;
  
  private CustomerManagePanel()
  {
    initUI();
    RFIDReader localRFIDReader = RFIDReader._mInstance;
    localRFIDReader.addReadListener(new RFIDReadListener()
    {
      public void dataRead(String paramAnonymousString)
      {
        if (!CustomerManagePanel.this.isVisible()) {
          return;
        }
        CustomerManagePanel.this._mRFID.setText(paramAnonymousString);
      }
    });
  }
  
  public static CustomerManagePanel getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new CustomerManagePanel();
    }
    return _mInstance;
  }
  
  private void initUI()
  {
    CustomerManagePanel localCustomerManagePanel = this;
    int i = 2;
    FormLayout localFormLayout = new FormLayout("10px,90px, 10px, 100px,3px,40px,160px, 3px,40px,pref:grow, 10px", "10px,25px, 10px,25px,10px,25px,10px,25px,10px, 25px,10px, 150px,10px,25px,10px,25px,20px,30px,20px,30px,20px,30px,10px");
    localCustomerManagePanel.setLayout(localFormLayout);
    localCustomerManagePanel.setBackground(getBackground());
    CellConstraints localCellConstraints = new CellConstraints();
    JLabel localJLabel = new JLabel("Customer Id :");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localCustomerManagePanel.add(localJLabel, localCellConstraints);
    this._mCustomerId = ((JBDBUIIntegerTextField)this._mDBUIContainer.createComponentForAttribute("CUST_INDEX", "Customer Id", true));
    ShortKeyCommon.shortKeyForLabels(localJLabel, 73, this._mCustomerId);
    this._mCustomerId.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 114) {
          CustomerManagePanel.this.custIdSearchClicked();
        }
      }
    });
    this._mCustomerId.setToolTipText("Customer Number");
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mCustomerId, localCellConstraints);
    Object localObject = new JBSearchButton(false);
    localCellConstraints.xywh(6, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add((Component)localObject, localCellConstraints);
    ((JButton)localObject).setToolTipText("Search by customer number");
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        CustomerManagePanel.this.custIdSearchClicked();
      }
    });
    i += 2;
    localJLabel = new JLabel("Name :");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localCustomerManagePanel.add(localJLabel, localCellConstraints);
    this._mCustomerName = ((JBStringTextField)this._mDBUIContainer.createComponentForAttribute("CUST_NAME", "Customer Name"));
    this._mCustomerName.setToolTipText("Customer Name");
    ShortKeyCommon.shortKeyForLabels(localJLabel, 78, this._mCustomerName);
    this._mCustomerName.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 114) {
          CustomerManagePanel.this.custNameSearchClicked();
        }
      }
    });
    localCellConstraints.xywh(4, i, 4, 1, CellConstraints.FILL, CellConstraints.FILL);
    this._mCustomerName.setMaxLength(128);
    this._mCustomerName.setRunTimeValidation(false);
    this._mCustomerName.setMandatory(true);
    localCustomerManagePanel.add(this._mCustomerName, localCellConstraints);
    localObject = new JBSearchButton(false);
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        CustomerManagePanel.this.custNameSearchClicked();
      }
    });
    localCellConstraints.xywh(9, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add((Component)localObject, localCellConstraints);
    ((JButton)localObject).setToolTipText("Search by customer name");
    i += 2;
    localJLabel = new JLabel("Barcode :");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localCustomerManagePanel.add(localJLabel, localCellConstraints);
    this._mBarcode = ((JBStringTextField)this._mDBUIContainer.createComponentForAttribute("BARCODE", "Barcode"));
    this._mBarcode.setToolTipText("Barcode tag number");
    ShortKeyCommon.shortKeyForLabels(localJLabel, 66, this._mCustomerName);
    this._mBarcode.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if ((paramAnonymousKeyEvent.getKeyCode() == 114) || (paramAnonymousKeyEvent.getKeyCode() == 10))
        {
          CustomerManagePanel.this.barcodeSearchClicked();
          paramAnonymousKeyEvent.consume();
        }
      }
    });
    localCellConstraints.xywh(4, i, 4, 1, CellConstraints.FILL, CellConstraints.FILL);
    this._mBarcode.setMaxLength(20);
    this._mBarcode.setRunTimeValidation(false);
    localCustomerManagePanel.add(this._mBarcode, localCellConstraints);
    localObject = new JBSearchButton(false);
    ((JButton)localObject).setToolTipText("Search by barcode tag number");
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        CustomerManagePanel.this.barcodeSearchClicked();
      }
    });
    localCellConstraints.xywh(9, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add((Component)localObject, localCellConstraints);
    i += 2;
    localJLabel = new JLabel("RFID :");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localCustomerManagePanel.add(localJLabel, localCellConstraints);
    this._mRFID = ((JBStringTextField)this._mDBUIContainer.createComponentForAttribute("RFID", "RFID"));
    localCellConstraints.xywh(4, i, 4, 1, CellConstraints.FILL, CellConstraints.FILL);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 70, this._mCustomerName);
    this._mRFID.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if ((paramAnonymousKeyEvent.getKeyCode() == 10) || (paramAnonymousKeyEvent.getKeyCode() == 114)) {
          CustomerManagePanel.this.rfidSearchClicked();
        }
      }
    });
    this._mRFID.setMaxLength(20);
    this._mRFID.setRunTimeValidation(false);
    localCustomerManagePanel.add(this._mRFID, localCellConstraints);
    this._mRFID.setToolTipText("RFID tag number");
    this._mRFID.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 114) {
          CustomerManagePanel.this.rfidSearchClicked();
        }
      }
    });
    localObject = new JBSearchButton(false);
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        CustomerManagePanel.this.rfidSearchClicked();
      }
    });
    localCellConstraints.xywh(9, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add((Component)localObject, localCellConstraints);
    this._mRFID.setToolTipText("Search by RFID tag number");
    i += 2;
    localJLabel = new JLabel("Join Date :");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localCustomerManagePanel.add(localJLabel, localCellConstraints);
    this._mJoinDate = ((JBDBUIDateField)this._mDBUIContainer.createComponentForAttribute("JOIN_DATE", "Join Date"));
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localCustomerManagePanel.add(this._mJoinDate, localCellConstraints);
    this._mJoinDate.getDateEditor().getUiComponent().setToolTipText("Customer join date as member");
    i += 2;
    localJLabel = new JLabel("Address :");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.TOP);
    localCustomerManagePanel.add(localJLabel, localCellConstraints);
    JBTextArea localJBTextArea = (JBTextArea)this._mDBUIContainer.createComponentForAttribute("CUST_ADDRESS", "Customer Address", (short)1, false);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 83, localJBTextArea);
    localCellConstraints.xywh(4, i, 4, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJBTextArea.setMandatory(true);
    add(new JScrollPane(localJBTextArea), localCellConstraints);
    localJBTextArea.setMaxLength(512);
    localJBTextArea.setToolTipText("Customer address");
    i += 2;
    localJLabel = new JLabel("Phone :");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.TOP);
    localCustomerManagePanel.add(localJLabel, localCellConstraints);
    this._mCustomerPhone = ((JBPhoneField)this._mDBUIContainer.createComponentForAttribute("CUST_PHONE", "Customer Phone", (short)2, false));
    this._mCustomerPhone.setToolTipText("Customer primary phone number");
    ShortKeyCommon.shortKeyForLabels(localJLabel, 80, this._mCustomerPhone);
    this._mCustomerPhone.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 114) {
          CustomerManagePanel.this.custPhoneSearchClicked();
        }
      }
    });
    localCellConstraints.xywh(4, i, 4, 1, CellConstraints.FILL, CellConstraints.FILL);
    localCustomerManagePanel.add(this._mCustomerPhone, localCellConstraints);
    this._mCustomerPhone.setMandatory(true);
    localObject = new JBSearchButton(false);
    localCellConstraints.xywh(9, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add((Component)localObject, localCellConstraints);
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        CustomerManagePanel.this.custPhoneSearchClicked();
      }
    });
    ((JButton)localObject).setToolTipText("Swarch by customer primary phone number");
    i += 2;
    localJLabel = new JLabel("Loyalty Points :");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localCustomerManagePanel.add(localJLabel, localCellConstraints);
    this._mLoyalty = ((JBDBUIIntegerTextField)this._mDBUIContainer.createComponentForAttribute("LOYALTY", "Loyalty Points", false));
    this._mLoyalty.setToolTipText("Initial loyalty points");
    ShortKeyCommon.shortKeyForLabels(localJLabel, 89, this._mLoyalty);
    this._mLoyalty.setHorizontalAlignment(4);
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mLoyalty, localCellConstraints);
    this._mLoyalty.setMandatory(true);
    i += 2;
    localObject = new JButton("Advanced");
    localCellConstraints.xywh(2, i, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    add((Component)localObject, localCellConstraints);
    ((JButton)localObject).setMnemonic(86);
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        CustomerManagePanel.this.advancedClicked();
      }
    });
    ((JButton)localObject).setToolTipText("Customer advanced information");
    i++;
    localCellConstraints.xywh(1, i, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.CENTER);
    localCustomerManagePanel.add(new JSeparator(), localCellConstraints);
    i++;
    localCellConstraints.xywh(1, i, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.FILL);
    localCustomerManagePanel.add(getActionPanel(), localCellConstraints);
    i++;
    localCellConstraints.xywh(1, i, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(new JSeparator(), localCellConstraints);
    i++;
    localCellConstraints.xywh(1, i, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getButtonPanel(), localCellConstraints);
  }
  
  private void advancedClicked()
  {
    CustomerRow localCustomerRow = (CustomerRow)this._mDBUIContainer.getCurrentInstance();
    ActionPanel localActionPanel = MainWindow.getActionPanel();
    CustomerAdvancedPanel localCustomerAdvancedPanel = CustomerAdvancedPanel.INSTANCE;
    try
    {
      if (localCustomerRow == null)
      {
        CustomerAdvancedPanel.INSTANCE.setCustomerAdvancedRow(localCustomerRow, null);
      }
      else
      {
        CustomerAdvancedRow localCustomerAdvancedRow = localCustomerRow.getAdvancedRow();
        CustomerAdvancedPanel.INSTANCE.setCustomerAdvancedRow(localCustomerRow, localCustomerAdvancedRow);
      }
    }
    catch (DBException localDBException)
    {
      UICommon.showInternalError("Internal error readind advanced details.");
      return;
    }
    localActionPanel.pushObject(localCustomerAdvancedPanel);
    localCustomerAdvancedPanel.setActionPanel(localActionPanel);
    localActionPanel.setTitle("Customer - advanced details");
    MainWindow.instance.repaint();
    localCustomerAdvancedPanel.setDefaultFocus();
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
        CustomerManagePanel.this.closeWindow();
      }
    });
    localObject = new HelpButton("ISP_MANAGE_CUSTOMER");
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JXButton)localObject).setMnemonic(72);
    return localJPanel;
  }
  
  private JPanel getActionPanel()
  {
    JPanel localJPanel = new JPanel();
    localJPanel.setLayout(new FormLayout("10px,100px,20px,100px,20px,100px,20px,100px,10px:grow", "30px"));
    CellConstraints localCellConstraints = new CellConstraints();
    JBActionButton localJBActionButton = (JBActionButton)this._mDBUIContainer.createActionObject("Add", "CREATE", null);
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJBActionButton, localCellConstraints);
    localJBActionButton.setMnemonic(65);
    localJBActionButton.addDBUIActionListener(new DBUIActionAdapter()
    {
      public void actionFailed(Object paramAnonymousObject)
      {
        UICommon.showMessage("Error creating customer information.", "Error", MainWindow.instance);
        CustomerManagePanel.this._mCustomerName.requestFocusInWindow();
      }
      
      public void afterAction(ActionObject paramAnonymousActionObject)
      {
        CustomerRow localCustomerRow = (CustomerRow)CustomerManagePanel.this._mDBUIContainer.getCurrentInstance();
        CustomerAdvancedRow localCustomerAdvancedRow = null;
        try
        {
          localCustomerAdvancedRow = localCustomerRow.getAdvancedRow();
        }
        catch (DBException localDBException1)
        {
          UICommon.showInternalError("The customer record created succesfully. Internal error updating additional details of customer.");
          return;
        }
        if (localCustomerAdvancedRow != null)
        {
          DBConnection localDBConnection = Db.getConnection();
          try
          {
            localDBConnection.openTrans();
            localCustomerAdvancedRow.setCustomerId(localCustomerRow.getCustIndex());
            localCustomerAdvancedRow.create();
            localDBConnection.endTrans();
          }
          catch (DBException localDBException2)
          {
            localDBConnection.rollbackNoExp();
            UICommon.showInternalError("The customer record created succesfully. Internal error updating additional details of customer.");
            return;
          }
        }
        CustomerManagePanel.this._mDBUIContainer.resetAttributes();
        CustomerManagePanel.this._mCustomerName.requestFocusInWindow();
        UICommon.showMessage("Customer created successfully.", "Success", MainWindow.instance);
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
        UICommon.showMessage("Error updating customer information.", "Error", MainWindow.instance);
        CustomerManagePanel.this._mCustomerName.requestFocusInWindow();
      }
      
      public void afterAction(ActionObject paramAnonymousActionObject)
      {
        CustomerRow localCustomerRow = (CustomerRow)CustomerManagePanel.this._mDBUIContainer.getCurrentInstance();
        CustomerAdvancedRow localCustomerAdvancedRow = null;
        try
        {
          localCustomerAdvancedRow = localCustomerRow.getAdvancedRow();
        }
        catch (DBException localDBException1)
        {
          UICommon.showInternalError("The customer record created succesfully. Internal error updating additional details of customer.");
          return;
        }
        if (localCustomerAdvancedRow != null)
        {
          DBConnection localDBConnection = Db.getConnection();
          try
          {
            localDBConnection.openTrans();
            if (localCustomerAdvancedRow.isCreated())
            {
              localCustomerAdvancedRow.setCustomerId(localCustomerRow.getCustIndex());
              localCustomerAdvancedRow.create();
            }
            else
            {
              localCustomerAdvancedRow.update(true);
            }
            localDBConnection.endTrans();
          }
          catch (DBException localDBException2)
          {
            localDBConnection.rollbackNoExp();
            UICommon.showInternalError("The customer record updated succesfully. Internal error updating additional details of customer.");
            return;
          }
        }
        UICommon.showMessage("Customer information updated successfully.", "Success", MainWindow.instance);
      }
    });
    localJBActionButton = (JBActionButton)this._mDBUIContainer.createActionObject("Delete", "DELETE", null);
    localCellConstraints.xywh(6, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJBActionButton.setMnemonic(68);
    localJPanel.add(localJBActionButton, localCellConstraints);
    localJBActionButton.addDBUIActionListener(new DBUIActionAdapter()
    {
      public boolean beforeAction(ActionObject paramAnonymousActionObject)
      {
        int i = UICommon.showQuestion("Are you sure you want to delete the customer ?", "Confirm Deletion", MainWindow.instance);
        return i == 1;
      }
      
      public void actionFailed(Object paramAnonymousObject)
      {
        UICommon.showMessage("Error deleting customer information.", "Error", MainWindow.instance);
        CustomerManagePanel.this._mCustomerName.requestFocusInWindow();
      }
      
      public void afterAction(ActionObject paramAnonymousActionObject)
      {
        CustomerRow localCustomerRow = (CustomerRow)CustomerManagePanel.this._mDBUIContainer.getCurrentInstance();
        CustomerAdvancedRow localCustomerAdvancedRow = null;
        try
        {
          localCustomerAdvancedRow = localCustomerRow.getAdvancedRow();
        }
        catch (DBException localDBException1)
        {
          UICommon.showInternalError("The customer record created succesfully. Internal error updating additional details of customer.");
          return;
        }
        if ((localCustomerAdvancedRow != null) && (!localCustomerAdvancedRow.isCreated()))
        {
          DBConnection localDBConnection = Db.getConnection();
          try
          {
            localDBConnection.openTrans();
            localCustomerAdvancedRow.delete();
            localDBConnection.endTrans();
          }
          catch (DBException localDBException2)
          {
            localDBConnection.rollbackNoExp();
            UICommon.showInternalError("The customer record deleted succesfully. Internal error updating additional details of customer.");
            return;
          }
        }
        CustomerManagePanel.this._mDBUIContainer.resetAttributes();
        UICommon.showMessage("Customer deleted successfully.", "Success", MainWindow.instance);
      }
    });
    JXButton localJXButton = new JXButton("Reset");
    localCellConstraints.xywh(8, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.setMnemonic(82);
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        CustomerManagePanel.this._mCustomerName.requestFocusInWindow();
        CustomerManagePanel.this._mDBUIContainer.resetAttributes();
      }
    });
    return localJPanel;
  }
  
  public void setDefaultFocus()
  {
    this._mCustomerName.requestFocusInWindow();
  }
  
  public JPanel getPanel()
  {
    return this;
  }
  
  public void clearAllFields()
  {
    if (this._mDBUIContainer != null) {
      this._mDBUIContainer.resetAttributes();
    }
  }
  
  public void windowDisplayed()
  {
    getRootPane().setDefaultButton(this._mOKBtn);
    setDefaultFocus();
  }
  
  public void addOKListener(CustomerPanelInterface paramCustomerPanelInterface)
  {
    this._mListeners.add(paramCustomerPanelInterface);
  }
  
  public void removeOkListener(CustomerPanelInterface paramCustomerPanelInterface)
  {
    this._mListeners.remove(paramCustomerPanelInterface);
  }
  
  public void setEditable(boolean paramBoolean)
  {
    this._mCustomerName.setEditable(paramBoolean);
    this._mCustomerAddress.setEditable(paramBoolean);
  }
  
  private void custIdSearchClicked()
  {
    String str = this._mCustomerId.getText().trim();
    if (str.length() == 0) {
      try
      {
        ArrayList localArrayList = null;
        localArrayList = CustomerTableDef.getInstance().getAllValues();
        if ((localArrayList == null) || (localArrayList.size() == 0))
        {
          UICommon.showError("No matching customer information found.", "No Data", MainWindow.instance);
          this._mCustomerId.requestFocusInWindow();
        }
        String[] arrayOfString = { "CUST_INDEX", "Customer Id", "CUST_NAME", "Customer Name", "CUST_PHONE", "Customer Phone", "BARCODE", "Barcode", "RFID", "RFID", "JOIN_DATE", "Join Date" };
        DBUIResultPanel localDBUIResultPanel = new DBUIResultPanel(arrayOfString, MainWindow.instance);
        localDBUIResultPanel.setData(localArrayList);
        localDBUIResultPanel.setSize(600, 400);
        localDBUIResultPanel.setLocationRelativeTo(MainWindow.instance);
        localDBUIResultPanel.setTitle("Customers");
        localDBUIResultPanel.setVisible(true);
        this._mCustomerName.requestFocusInWindow();
        if (localDBUIResultPanel.isCancelled())
        {
          this._mCustomerId.requestFocusInWindow();
          return;
        }
        DBRow localDBRow = (DBRow)localDBUIResultPanel.getSelectedRow();
        this._mDBUIContainer.setCurrentInstance(localDBRow);
        return;
      }
      catch (DBException localDBException1)
      {
        UICommon.showInternalError("Internal error searching for customer details");
        this._mCustomerId.requestFocusInWindow();
        return;
      }
    }
    CustomerRow localCustomerRow = null;
    try
    {
      int i = Integer.valueOf(str).intValue();
      localCustomerRow = CustomerTableDef.getInstance().findRowByIndex(i);
    }
    catch (NumberFormatException localNumberFormatException)
    {
      UICommon.showError("Invalid customer Id specified.", "Error", MainWindow.instance);
      this._mCustomerId.requestFocusInWindow();
      return;
    }
    catch (DBException localDBException2)
    {
      UICommon.showError("Internal error searching for the customer.\n\nContact administrator.", "Internal Error", MainWindow.instance);
      this._mCustomerId.requestFocusInWindow();
      return;
    }
    if (localCustomerRow == null)
    {
      UICommon.showError("No matching customer information found.", "No Data", MainWindow.instance);
      this._mCustomerId.requestFocusInWindow();
      return;
    }
    this._mDBUIContainer.setCurrentInstance(localCustomerRow);
  }
  
  private void custNameSearchClicked()
  {
    String[] arrayOfString = { "CUST_INDEX", "Customer Id", "CUST_NAME", "Customer Name", "CUST_PHONE", "Customer Phone", "BARCODE", "Barcode", "RFID", "RFID", "JOIN_DATE", "Join Date" };
    ArrayList localArrayList = null;
    try
    {
      String str = this._mCustomerName.getText().trim();
      localArrayList = CustomerTableDef.getInstance().getAllValuesWithWhereClause("CUST_NAME LIKE '" + str + "%'");
      if (localArrayList == null)
      {
        UICommon.showError("No matching customer information found.", "No Data", MainWindow.instance);
        return;
      }
    }
    catch (DBException localDBException)
    {
      UICommon.showError("Internal error searching customer details.", "Internal Error", MainWindow.instance);
      return;
    }
    DBUIResultPanel localDBUIResultPanel = new DBUIResultPanel(arrayOfString, MainWindow.instance);
    localDBUIResultPanel.setData(localArrayList);
    localDBUIResultPanel.setSize(600, 400);
    localDBUIResultPanel.setLocationRelativeTo(MainWindow.instance);
    localDBUIResultPanel.setTitle("Customers");
    localDBUIResultPanel.setVisible(true);
    this._mCustomerName.requestFocusInWindow();
    if (localDBUIResultPanel.isCancelled())
    {
      this._mCustomerName.requestFocusInWindow();
      return;
    }
    DBRow localDBRow = (DBRow)localDBUIResultPanel.getSelectedRow();
    this._mDBUIContainer.setCurrentInstance(localDBRow);
  }
  
  private void custPhoneSearchClicked()
  {
    String[] arrayOfString = { "CUST_INDEX", "Customer Id", "CUST_NAME", "Customer Name", "CUST_PHONE", "Customer Phone", "BARCODE", "Barcode", "RFID", "RFID", "JOIN_DATE", "Join Date" };
    ArrayList localArrayList = null;
    try
    {
      String str = this._mCustomerPhone.getText().trim();
      localArrayList = CustomerTableDef.getInstance().getAllValuesWithWhereClause("CUST_PHONE LIKE '" + str + "%'");
      if (localArrayList == null)
      {
        UICommon.showError("No matching customer information found.", "No Data", MainWindow.instance);
        return;
      }
    }
    catch (DBException localDBException)
    {
      UICommon.showError("Internal error searching customer details.", "Internal Error", MainWindow.instance);
      return;
    }
    DBUIResultPanel localDBUIResultPanel = new DBUIResultPanel(arrayOfString, MainWindow.instance);
    localDBUIResultPanel.setData(localArrayList);
    localDBUIResultPanel.setLocationRelativeTo(MainWindow.instance);
    localDBUIResultPanel.setSize(new Dimension(600, 400));
    localDBUIResultPanel.setTitle("Customers");
    localDBUIResultPanel.setVisible(true);
    this._mCustomerPhone.requestFocusInWindow();
    if (localDBUIResultPanel.isCancelled())
    {
      this._mCustomerPhone.requestFocusInWindow();
      return;
    }
    DBRow localDBRow = (DBRow)localDBUIResultPanel.getSelectedRow();
    this._mDBUIContainer.setCurrentInstance(localDBRow);
  }
  
  private void barcodeSearchClicked()
  {
    String[] arrayOfString = { "CUST_INDEX", "Customer Id", "CUST_NAME", "Customer Name", "CUST_PHONE", "Customer Phone", "BARCODE", "Barcode", "RFID", "RFID", "JOIN_DATE", "Join Date" };
    ArrayList localArrayList = null;
    try
    {
      String str = this._mBarcode.getText().trim();
      localObject = "BARCODE LIKE '" + str + "%'";
      if (str.length() == 0) {
        localObject = (String)localObject + " OR BARCODE IS NULL";
      }
      localArrayList = CustomerTableDef.getInstance().getAllValuesWithWhereClause((String)localObject);
      if (localArrayList == null)
      {
        UICommon.showError("No matching customer information found.", "No Data", MainWindow.instance);
        return;
      }
    }
    catch (DBException localDBException)
    {
      UICommon.showError("Internal error searching customer details.", "Internal Error", MainWindow.instance);
      return;
    }
    DBUIResultPanel localDBUIResultPanel = new DBUIResultPanel(arrayOfString, MainWindow.instance);
    localDBUIResultPanel.setData(localArrayList);
    localDBUIResultPanel.setSize(600, 400);
    localDBUIResultPanel.setLocationRelativeTo(MainWindow.instance);
    localDBUIResultPanel.setTitle("Customers");
    localDBUIResultPanel.setVisible(true);
    this._mCustomerName.requestFocusInWindow();
    if (localDBUIResultPanel.isCancelled())
    {
      this._mBarcode.requestFocusInWindow();
      return;
    }
    Object localObject = (DBRow)localDBUIResultPanel.getSelectedRow();
    this._mDBUIContainer.setCurrentInstance((DBRow)localObject);
  }
  
  private void rfidSearchClicked()
  {
    String[] arrayOfString = { "CUST_INDEX", "Customer Id", "CUST_NAME", "Customer Name", "CUST_PHONE", "Customer Phone", "BARCODE", "Barcode", "RFID", "RFID", "JOIN_DATE", "Join Date" };
    ArrayList localArrayList = null;
    try
    {
      String str = this._mRFID.getText().trim();
      localObject = "RFID LIKE '" + str + "%'";
      if (str.length() == 0) {
        localObject = (String)localObject + " OR RFID IS NULL";
      }
      localArrayList = CustomerTableDef.getInstance().getAllValuesWithWhereClause((String)localObject);
      if (localArrayList == null)
      {
        UICommon.showError("No matching customer information found.", "No Data", MainWindow.instance);
        return;
      }
    }
    catch (DBException localDBException)
    {
      UICommon.showError("Internal error searching customer details.", "Internal Error", MainWindow.instance);
      return;
    }
    DBUIResultPanel localDBUIResultPanel = new DBUIResultPanel(arrayOfString, MainWindow.instance);
    localDBUIResultPanel.setSize(600, 400);
    localDBUIResultPanel.setData(localArrayList);
    localDBUIResultPanel.setLocationRelativeTo(MainWindow.instance);
    localDBUIResultPanel.setTitle("Customers");
    localDBUIResultPanel.setVisible(true);
    this._mCustomerName.requestFocusInWindow();
    if (localDBUIResultPanel.isCancelled())
    {
      this._mRFID.requestFocusInWindow();
      return;
    }
    Object localObject = (DBRow)localDBUIResultPanel.getSelectedRow();
    this._mDBUIContainer.setCurrentInstance((DBRow)localObject);
  }
  
  public static void manageCustomers()
  {
    QuickTaskBox localQuickTaskBox = QuickTaskBox.getInstance();
    if (!localQuickTaskBox.getName().equals("Customers"))
    {
      setupActionList();
      localQuickTaskBox.setTaskPane(_mTaskPane);
      localQuickTaskBox.setName("Customers");
    }
    ActionPanel localActionPanel = MainWindow.getActionPanel();
    CustomerManagePanel localCustomerManagePanel = getInstance();
    localCustomerManagePanel.clearAllFields();
    localActionPanel.cleanPush(localCustomerManagePanel);
    localCustomerManagePanel.setActionPanel(localActionPanel);
    localActionPanel.setTitle("Manage Customer");
    MainWindow.instance.repaint();
    localCustomerManagePanel.setDefaultFocus();
  }
  
  public static void manageCustomerHistory()
  {
    QuickTaskBox localQuickTaskBox = QuickTaskBox.getInstance();
    if (!localQuickTaskBox.getName().equals("Customers"))
    {
      setupActionList();
      localQuickTaskBox.setTaskPane(_mTaskPane);
      localQuickTaskBox.setName("Customers");
    }
    ActionPanel localActionPanel = MainWindow.getActionPanel();
    CustomerPurchaseHistoryUI localCustomerPurchaseHistoryUI = CustomerPurchaseHistoryUI.INSTANCE;
    localCustomerPurchaseHistoryUI.clearAllFields();
    localActionPanel.cleanPush(localCustomerPurchaseHistoryUI);
    localCustomerPurchaseHistoryUI.setActionPanel(localActionPanel);
    localActionPanel.setTitle("Customer Purchase History");
    MainWindow.instance.repaint();
    localCustomerPurchaseHistoryUI.setDefaultFocus();
  }
  
  private static void setupActionList()
  {
    if ((UserInfoTableDef.getCurrentUser().hasAccess(1L)) && (_mTaskPane == null))
    {
      _mTaskPane = new QuickTaskPane();
      QuickTaskPane.TaskGroup localTaskGroup = _mTaskPane.createTaskGroup("Customers", "/dm/jb/images/product.gif");
      localTaskGroup.addAction("Manage Customer", "Manage customer information", "/dm/jb/images/customer.gif", new QuickTask()
      {
        public void actionPerformed() {}
      });
      localTaskGroup.addAction("Customer Purchase History", "Customer purchase history", "/dm/jb/images/customer_sales.png", new QuickTask()
      {
        public void actionPerformed() {}
      });
      _mTaskPane.addTaskGroup(localTaskGroup);
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.inv.CustomerManagePanel
 * JD-Core Version:    0.7.0.1
 */