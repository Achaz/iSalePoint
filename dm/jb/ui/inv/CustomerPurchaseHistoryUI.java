package dm.jb.ui.inv;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.IDateEditor;
import com.toedter.calendar.JDateChooser;
import dm.jb.db.objects.BillTableRow;
import dm.jb.db.objects.BillTableTableDef;
import dm.jb.db.objects.CompanyInfoRow;
import dm.jb.db.objects.CompanyInfoTableDef;
import dm.jb.db.objects.CustomerRow;
import dm.jb.db.objects.CustomerTableDef;
import dm.jb.op.bill.Bill;
import dm.jb.printing.JePrinterException;
import dm.jb.printing.Print;
import dm.jb.ui.MainWindow;
import dm.jb.ui.common.HelpButton;
import dm.jb.ui.settings.CountryInfo;
import dm.tools.db.BindObject;
import dm.tools.db.DBException;
import dm.tools.db.DBRow;
import dm.tools.types.InternalAmount;
import dm.tools.types.InternalDate;
import dm.tools.ui.AbstractMainPanel;
import dm.tools.ui.ActionPanel;
import dm.tools.ui.UICommon;
import dm.tools.ui.components.DBUIResultPanel;
import dm.tools.ui.components.JBSearchButton;
import dm.tools.utils.CommonConfig;
import dm.tools.utils.TextPrinter;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

public class CustomerPurchaseHistoryUI
  extends AbstractMainPanel
{
  public static CustomerPurchaseHistoryUI INSTANCE = new CustomerPurchaseHistoryUI();
  private JTextField _mCustomerId = null;
  private JTextField _mCustomerName = null;
  private JDateChooser _mFromDate = null;
  private JDateChooser _mToDate = null;
  private PurchaseHistoryModel _mModel = null;
  private JTable _mHistoryTable = null;
  private CustomerRow _mCustomer = null;
  private JButton _mDetailsButton = null;
  private JComboBox _mSaveAs = null;
  private JTextField _mFileName = null;
  private JButton _mBrowseButton = null;
  private JButton _mSendButton = null;
  private double _mTotalPurchase = 0.0D;
  private JTextField _mTotalPurchaseField = null;
  private JTextField _mTotalPoints = null;
  
  private CustomerPurchaseHistoryUI()
  {
    initUI();
  }
  
  public void setDefaultFocus()
  {
    this._mCustomerId.requestFocusInWindow();
  }
  
  public JPanel getPanel()
  {
    return this;
  }
  
  public void windowDisplayed() {}
  
  public void clearAllFields()
  {
    setCustomerData(null);
    this._mSaveAs.setSelectedIndex(0);
  }
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("10px,90px,10px,120px,3px,7px,33px,30px, 67px,3px,30px, 10px,100px,10px,100px,10px", "10px,25px,10px,25px,10px,25px,10px,200px,10px,60px,20px,pref:grow,20px,30px,10px");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    int i = 2;
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.CENTER);
    add(new JLabel("Customer Id : "), localCellConstraints);
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    this._mCustomerId = new JTextField();
    this._mCustomerId.setToolTipText("Customer id");
    add(this._mCustomerId, localCellConstraints);
    JBSearchButton localJBSearchButton = new JBSearchButton(false);
    localJBSearchButton.setToolTipText("Search customer by id");
    localCellConstraints.xywh(6, i, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJBSearchButton, localCellConstraints);
    this._mCustomerId.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 114) {
          CustomerPurchaseHistoryUI.this.custIdSearchClicked();
        }
      }
    });
    localJBSearchButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        CustomerPurchaseHistoryUI.this.custIdSearchClicked();
      }
    });
    i += 2;
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.CENTER);
    add(new JLabel("Customer Name : "), localCellConstraints);
    localCellConstraints.xywh(4, i, 6, 1, CellConstraints.FILL, CellConstraints.FILL);
    this._mCustomerName = new JTextField();
    this._mCustomerName.setToolTipText("Customer name");
    add(this._mCustomerName, localCellConstraints);
    this._mCustomerName.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 114) {
          CustomerPurchaseHistoryUI.this.custNameSearchClicked();
        }
      }
    });
    localJBSearchButton = new JBSearchButton(false);
    localJBSearchButton.setToolTipText("Search customer by name");
    localCellConstraints.xywh(11, i, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJBSearchButton, localCellConstraints);
    localJBSearchButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        CustomerPurchaseHistoryUI.this.custNameSearchClicked();
      }
    });
    i += 2;
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.CENTER);
    add(new JLabel("Duration : "), localCellConstraints);
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    this._mFromDate = new JDateChooser();
    add(this._mFromDate, localCellConstraints);
    this._mFromDate.setDateFormatString(CommonConfig.getInstance().dateFormat);
    this._mFromDate.getDateEditor().getUiComponent().setToolTipText("Duration from");
    localCellConstraints.xywh(7, i, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
    this._mToDate = new JDateChooser();
    add(this._mToDate, localCellConstraints);
    this._mToDate.setDateFormatString(CommonConfig.getInstance().dateFormat);
    this._mToDate.getDateEditor().getUiComponent().setToolTipText("Duration to");
    i += 2;
    this._mModel = new PurchaseHistoryModel(null);
    this._mHistoryTable = new JTable(this._mModel);
    this._mHistoryTable.setToolTipText("Transaction details");
    this._mHistoryTable.setSelectionMode(0);
    localCellConstraints.xywh(2, i, localFormLayout.getColumnCount() - 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(new JScrollPane(this._mHistoryTable), localCellConstraints);
    this._mHistoryTable.setAutoResizeMode(0);
    this._mHistoryTable.getColumnModel().getColumn(0).setPreferredWidth(60);
    this._mHistoryTable.getColumnModel().getColumn(1).setPreferredWidth(120);
    this._mHistoryTable.getColumnModel().getColumn(2).setPreferredWidth(80);
    this._mHistoryTable.getColumnModel().getColumn(3).setPreferredWidth(130);
    this._mHistoryTable.getColumnModel().getColumn(4).setPreferredWidth(100);
    this._mHistoryTable.getColumnModel().getColumn(5).setPreferredWidth(120);
    this._mHistoryTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent paramAnonymousListSelectionEvent)
      {
        if (paramAnonymousListSelectionEvent.getValueIsAdjusting()) {
          return;
        }
        if (CustomerPurchaseHistoryUI.this._mHistoryTable.getSelectedRow() != -1) {
          CustomerPurchaseHistoryUI.this._mDetailsButton.setEnabled(true);
        } else {
          CustomerPurchaseHistoryUI.this._mDetailsButton.setEnabled(false);
        }
      }
    });
    i += 2;
    localCellConstraints.xywh(11, i, 5, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getTotalPanel(), localCellConstraints);
    localCellConstraints.xywh(2, i, 6, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getDetailsButtonPanel(), localCellConstraints);
    i += 2;
    localCellConstraints.xywh(2, i, 8, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getSaveAsPanel(), localCellConstraints);
    i++;
    localCellConstraints.xywh(1, i, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(new JSeparator(), localCellConstraints);
    i++;
    localCellConstraints.xywh(2, i, localFormLayout.getColumnCount() - 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getButtonPanel(), localCellConstraints);
  }
  
  private JPanel getDetailsButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    localJPanel.setLayout(new FormLayout("100px,20px,100px", "30px"));
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    JButton localJButton = new JButton("Details");
    localJButton.setToolTipText("Click to see individual transaction details");
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        CustomerPurchaseHistoryUI.this.detailsClicked();
      }
    });
    localJPanel.add(localJButton, localCellConstraints);
    this._mDetailsButton = localJButton;
    localJButton.setEnabled(false);
    localCellConstraints.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJButton = new JButton("Reset");
    localJButton.setToolTipText("Reset the fields");
    localJPanel.add(localJButton, localCellConstraints);
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        CustomerPurchaseHistoryUI.this.setCustomerData(null);
        CustomerPurchaseHistoryUI.this.setDefaultFocus();
      }
    });
    return localJPanel;
  }
  
  private void enableSaveAsPanel(boolean paramBoolean)
  {
    this._mSaveAs.setEnabled(paramBoolean);
    this._mSendButton.setEnabled(paramBoolean);
    String str = (String)this._mSaveAs.getSelectedItem();
    if (str.equals("Printer"))
    {
      if (paramBoolean) {}
      this._mBrowseButton.setEnabled(false);
      if (paramBoolean) {}
      this._mFileName.setEnabled(false);
    }
    else
    {
      this._mBrowseButton.setEnabled(paramBoolean);
      this._mFileName.setEnabled(paramBoolean);
    }
  }
  
  private JPanel getSaveAsPanel()
  {
    JPanel localJPanel = new JPanel();
    localJPanel.setLayout(new FormLayout("10px,90px,10px,80px,13px,57px,3px,40px,10px", "10px,25px,10px,25px,10px,30px,10px"));
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(2, 2, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(new JLabel("Send to : "), localCellConstraints);
    this._mSaveAs = new JComboBox();
    localCellConstraints.xywh(4, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mSaveAs, localCellConstraints);
    this._mSaveAs.addItem("Printer");
    this._mSaveAs.addItem("PDF");
    this._mSaveAs.addItem("Text File");
    this._mSaveAs.setToolTipText("Output device");
    this._mSaveAs.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        String str = (String)CustomerPurchaseHistoryUI.this._mSaveAs.getSelectedItem();
        if (str.equals("Printer"))
        {
          CustomerPurchaseHistoryUI.this._mBrowseButton.setEnabled(false);
          CustomerPurchaseHistoryUI.this._mFileName.setEnabled(false);
        }
        else
        {
          CustomerPurchaseHistoryUI.this._mBrowseButton.setEnabled(true);
          CustomerPurchaseHistoryUI.this._mFileName.setEnabled(true);
        }
      }
    });
    localCellConstraints.xywh(2, 4, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(new JLabel("Save as : "), localCellConstraints);
    this._mFileName = new JTextField();
    this._mFileName.setToolTipText("File to be saved");
    localCellConstraints.xywh(4, 4, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mFileName, localCellConstraints);
    ImageIcon localImageIcon = new ImageIcon(getClass().getResource("/dm/jb/images/file_folder.gif"));
    JButton localJButton = new JButton(localImageIcon);
    localCellConstraints.xywh(8, 4, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJButton, localCellConstraints);
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JFileChooser localJFileChooser = new JFileChooser();
        int i = localJFileChooser.showOpenDialog(MainWindow.instance);
        if (i == 0)
        {
          String str = localJFileChooser.getSelectedFile().getAbsolutePath();
          CustomerPurchaseHistoryUI.this._mFileName.setText(str);
        }
      }
    });
    this._mBrowseButton = localJButton;
    localJButton.setToolTipText("browse for the file to be saved");
    localJButton = new JButton("Send");
    localCellConstraints.xywh(6, 6, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJButton, localCellConstraints);
    this._mSendButton = localJButton;
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        String str = (String)CustomerPurchaseHistoryUI.this._mSaveAs.getSelectedItem();
        Date localDate1 = CustomerPurchaseHistoryUI.this._mFromDate.getDate();
        Date localDate2 = CustomerPurchaseHistoryUI.this._mToDate.getDate();
        StringBuffer localStringBuffer = new StringBuffer();
        localStringBuffer.append(CustomerPurchaseHistoryUI.this._mCustomer.getCustName());
        localStringBuffer.append("\n");
        localStringBuffer.append(CustomerPurchaseHistoryUI.this._mCustomer.getCustAddress() != null ? CustomerPurchaseHistoryUI.this._mCustomer.getCustAddress() : "");
        if (str.equals("Printer")) {
          CustomerPurchaseHistoryUI.this.sendToPrinter(CustomerPurchaseHistoryUI.this._mTotalPurchase, CustomerPurchaseHistoryUI.this._mCustomer.getLoyalty(), localStringBuffer.toString(), localDate1, localDate2);
        } else if (str.equals("PDF")) {
          CustomerPurchaseHistoryUI.this.sendToPDF(CustomerPurchaseHistoryUI.this._mTotalPurchase, CustomerPurchaseHistoryUI.this._mCustomer.getLoyalty(), localStringBuffer.toString(), localDate1, localDate2);
        } else if (str.equals("Text File")) {
          CustomerPurchaseHistoryUI.this.sendToTextFile(CustomerPurchaseHistoryUI.this._mTotalPurchase, CustomerPurchaseHistoryUI.this._mCustomer.getLoyalty(), localStringBuffer.toString(), localDate1, localDate2);
        }
      }
    });
    localJPanel.setBorder(BorderFactory.createEtchedBorder());
    localJButton.setToolTipText("Write to teh device/print");
    return localJPanel;
  }
  
  private JPanel getTotalPanel()
  {
    JPanel localJPanel = new JPanel();
    localJPanel.setLayout(new FormLayout("90px,10px,120px,3px,60px", "25px,10px,25px"));
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    localJPanel.add(new JLabel("Total Purchase : "), localCellConstraints);
    localCellConstraints.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    this._mTotalPurchaseField = new JTextField();
    this._mTotalPurchaseField.setHorizontalAlignment(4);
    localJPanel.add(this._mTotalPurchaseField, localCellConstraints);
    localCellConstraints.xywh(5, 1, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    localJPanel.add(new JLabel(CommonConfig.getInstance().country.currency), localCellConstraints);
    this._mTotalPurchaseField.setEditable(false);
    this._mTotalPurchaseField.setToolTipText("Total amopunt spent");
    localCellConstraints.xywh(1, 3, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    localJPanel.add(new JLabel("Total Points : "), localCellConstraints);
    localCellConstraints.xywh(3, 3, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    this._mTotalPoints = new JTextField();
    this._mTotalPoints.setToolTipText("Total loyalty points available");
    localJPanel.add(this._mTotalPoints, localCellConstraints);
    this._mTotalPoints.setEditable(false);
    this._mTotalPoints.setHorizontalAlignment(4);
    return localJPanel;
  }
  
  private JPanel getButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    localJPanel.setLayout(new FormLayout("100px,pref:grow,100px", "30px"));
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    Object localObject = new JButton("Close");
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        CustomerPurchaseHistoryUI.this.closeWindow();
      }
    });
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JButton)localObject).setToolTipText("Close the window");
    localObject = new HelpButton("CUSTOMER_HISTORY");
    localCellConstraints.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JButton)localObject).setToolTipText("Help");
    return localJPanel;
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
        localDBUIResultPanel.setVisible(true);
        this._mCustomerName.requestFocusInWindow();
        if (localDBUIResultPanel.isCancelled())
        {
          this._mCustomerId.requestFocusInWindow();
          return;
        }
        DBRow localDBRow = (DBRow)localDBUIResultPanel.getSelectedRow();
        setCustomerData((CustomerRow)localDBRow);
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
    setCustomerData(localCustomerRow);
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
    localDBUIResultPanel.setVisible(true);
    this._mCustomerName.requestFocusInWindow();
    if (localDBUIResultPanel.isCancelled())
    {
      this._mCustomerName.requestFocusInWindow();
      return;
    }
    DBRow localDBRow = (DBRow)localDBUIResultPanel.getSelectedRow();
    setCustomerData((CustomerRow)localDBRow);
  }
  
  private void setCustomerData(CustomerRow paramCustomerRow)
  {
    this._mCustomer = paramCustomerRow;
    this._mHistoryTable.clearSelection();
    this._mDetailsButton.setEnabled(false);
    this._mModel.removeAllHistoryRows();
    if (paramCustomerRow == null)
    {
      this._mCustomerId.setText("");
      this._mCustomerName.setText("");
      this._mFromDate.setDate(null);
      this._mToDate.setDate(null);
      enableSaveAsPanel(false);
      this._mSaveAs.setSelectedIndex(0);
      this._mFileName.setText("");
      this._mTotalPurchaseField.setText("");
      this._mTotalPoints.setText("");
      return;
    }
    enableSaveAsPanel(true);
    this._mCustomerId.setText(paramCustomerRow.getCustIndex() + "");
    this._mCustomerName.setText(paramCustomerRow.getCustName());
    try
    {
      ArrayList localArrayList = null;
      Date localDate1 = this._mFromDate.getDate();
      Date localDate2 = this._mToDate.getDate();
      if ((localDate1 == null) && (localDate2 == null))
      {
        localArrayList = BillTableTableDef.getInstance().getAllValuesWithWhereClause("CUST_INDEX=" + paramCustomerRow.getCustIndex());
      }
      else if ((localDate1 != null) && (localDate2 == null))
      {
        localObject = new BindObject[] { new BindObject(1, 1, Integer.valueOf(paramCustomerRow.getCustIndex())), new BindObject(2, 2, InternalDate.getSqlDate(localDate1)), new BindObject(3, 2, InternalDate.getSqlDate(localDate1)) };
        localArrayList = BillTableTableDef.getInstance().getAllValuesWithWhereClauseWithBind("CUST_INDEX= ? AND (BILL_DATE > ? OR BILL_DATE = ?)", (BindObject[])localObject);
      }
      else if ((localDate1 == null) && (localDate2 != null))
      {
        localObject = new BindObject[] { new BindObject(1, 1, Integer.valueOf(paramCustomerRow.getCustIndex())), new BindObject(2, 2, InternalDate.getSqlDate(localDate2)), new BindObject(3, 2, InternalDate.getSqlDate(localDate2)) };
        localArrayList = BillTableTableDef.getInstance().getAllValuesWithWhereClauseWithBind("CUST_INDEX= ? AND (BILL_DATE < ? OR BILL_DATE = ?)", (BindObject[])localObject);
      }
      else if ((localDate1 != null) && (localDate2 != null))
      {
        localObject = new BindObject[] { new BindObject(1, 1, Integer.valueOf(paramCustomerRow.getCustIndex())), new BindObject(2, 2, InternalDate.getSqlDate(localDate1)), new BindObject(3, 2, InternalDate.getSqlDate(localDate1)), new BindObject(4, 2, InternalDate.getSqlDate(localDate2)), new BindObject(5, 2, InternalDate.getSqlDate(localDate2)) };
        localArrayList = BillTableTableDef.getInstance().getAllValuesWithWhereClauseWithBind("CUST_INDEX= ? AND ((BILL_DATE > ? OR BILL_DATE = ?) AND (BILL_DATE < ? OR BILL_DATE = ?))", (BindObject[])localObject);
      }
      if ((localArrayList == null) || (localArrayList.size() == 0))
      {
        UICommon.showError("No purchase was made by the customer.", "No records", MainWindow.instance);
        return;
      }
      this._mTotalPurchase = 0.0D;
      Object localObject = localArrayList.iterator();
      while (((Iterator)localObject).hasNext())
      {
        DBRow localDBRow = (DBRow)((Iterator)localObject).next();
        this._mModel.addHistoryRow((BillTableRow)localDBRow);
        this._mTotalPurchase += ((BillTableRow)localDBRow).getAmount();
      }
      this._mTotalPurchaseField.setText(InternalAmount.toString(this._mTotalPurchase));
      this._mTotalPoints.setText(this._mCustomer.getLoyalty() + "");
    }
    catch (DBException localDBException)
    {
      UICommon.showInternalError("Internal error searching for sale details.");
      return;
    }
  }
  
  private void detailsClicked()
  {
    int i = this._mHistoryTable.getSelectedRow();
    i = this._mHistoryTable.convertRowIndexToModel(i);
    BillTableRow localBillTableRow = this._mModel.getRowAt(i);
    try
    {
      Bill localBill = Bill.getBillForBillNo(localBillTableRow.getBillNo(), localBillTableRow.getStoreId());
      SimpleBillDetailsUI.INSTANCE.setBill(localBill);
      ActionPanel localActionPanel = MainWindow.getActionPanel();
      SimpleBillDetailsUI localSimpleBillDetailsUI = SimpleBillDetailsUI.INSTANCE;
      localActionPanel.pushObject(localSimpleBillDetailsUI);
      localSimpleBillDetailsUI.setActionPanel(localActionPanel);
      localActionPanel.setTitle("Sale Details");
      MainWindow.instance.repaint();
      localSimpleBillDetailsUI.setDefaultFocus();
    }
    catch (DBException localDBException)
    {
      UICommon.showInternalError("Internal error reading transaction details.");
      return;
    }
  }
  
  private void sendToPrinter(double paramDouble, int paramInt, String paramString, Date paramDate1, Date paramDate2)
  {
    try
    {
      Print.getInstance().printPurchaseHistory(paramDouble, paramInt, paramString, paramDate1, paramDate2, this._mModel._mBillRows);
    }
    catch (JePrinterException localJePrinterException)
    {
      UICommon.showInternalError("Internal error printing the purchase history.");
      return;
    }
    UICommon.showMessage("Details send to printer for printing.", "Success", MainWindow.instance);
  }
  
  private void sendToTextFile(double paramDouble, int paramInt, String paramString, Date paramDate1, Date paramDate2)
  {
    String str1 = this._mFileName.getText().trim();
    if (str1.length() == 0)
    {
      UICommon.showError("File name cannot be empty.", "Error", MainWindow.instance);
      this._mFileName.requestFocusInWindow();
      return;
    }
    File localFile = new File(str1);
    String str2 = null;
    if (localFile.isDirectory())
    {
      str2 = localFile + File.separator + "CustomerSalesHistory.txt";
    }
    else
    {
      if (localFile.exists())
      {
        int i = UICommon.showQuestion(str1 + " already exists.\nDo you want to replace it ?", "Confirm", MainWindow.instance);
        if (i != 1) {
          return;
        }
      }
      str2 = str1;
    }
    PrintStream localPrintStream = null;
    try
    {
      FileOutputStream localFileOutputStream = new FileOutputStream(new File(str2));
      localPrintStream = new PrintStream(localFileOutputStream);
      String str3 = TextPrinter.getCenterAlignedText(CompanyInfoTableDef.getInstance().getCompany().getName(), TextPrinter.TextPrinterColumnWidth, (byte)3);
      localPrintStream.println(str3);
      StringTokenizer localStringTokenizer = new StringTokenizer(paramString, "\n");
      while (localStringTokenizer.hasMoreTokens())
      {
        localObject1 = localStringTokenizer.nextToken();
        localPrintStream.println("    " + (String)localObject1);
      }
      localPrintStream.println();
      localPrintStream.print("Duration : ");
      Object localObject1 = new SimpleDateFormat(CommonConfig.getInstance().dateFormat);
      String str4 = "";
      if (paramDate1 != null) {
        str4 = ((SimpleDateFormat)localObject1).format(paramDate1);
      }
      String str5 = "Current";
      if (paramDate2 != null) {
        str5 = ((SimpleDateFormat)localObject1).format(paramDate2);
      }
      localPrintStream.println(str4 + " To " + str5);
      localPrintStream.println();
      TextPrinter.printDashedLine(localPrintStream, 101, 2);
      localPrintStream.println();
      String[] arrayOfString1 = { "Sl. No", "Rcpt. No", "Date", "Amount (" + CommonConfig.getInstance().country.currency + ")", "Pts. Earned", "Pts. Redeemed" };
      int[] arrayOfInt = { 6, 20, 20, 20, 15, 15 };
      TextPrinter.printTableHeader(localPrintStream, arrayOfString1, true, (byte)3, (byte)3, 40, 0, arrayOfInt);
      localPrintStream.println();
      TextPrinter.printDashedLine(localPrintStream, 101, 2);
      localPrintStream.println();
      PurchaseHistoryDS localPurchaseHistoryDS = new PurchaseHistoryDS(this._mModel._mBillRows);
      byte[] arrayOfByte = { 2, 2, 2, 2, 2, 2 };
      while (localPurchaseHistoryDS.next())
      {
        str6 = localPurchaseHistoryDS.getFieldValueInternal("SL_NO").toString();
        String str7 = localPurchaseHistoryDS.getFieldValueInternal("RCPT_NO").toString();
        String str8 = localPurchaseHistoryDS.getFieldValueInternal("RCPT_DATE").toString();
        String str9 = localPurchaseHistoryDS.getFieldValueInternal("RCPT_AMOUNT").toString();
        String str10 = localPurchaseHistoryDS.getFieldValueInternal("POINTS_EARNED").toString();
        String str11 = localPurchaseHistoryDS.getFieldValueInternal("POINTS_USED").toString();
        String[] arrayOfString2 = { str6, str7, str8, str9, str10, str11 };
        TextPrinter.printTableRow(localPrintStream, arrayOfString2, true, false, (byte)3, arrayOfByte, arrayOfInt, 0);
        localPrintStream.println();
      }
      TextPrinter.printDashedLine(localPrintStream, 101, 2);
      localPrintStream.println();
      String str6 = "Total Purchase : " + InternalAmount.toString(paramDouble) + " " + CommonConfig.getInstance().country.currency;
      str6 = TextPrinter.getRightAlignedText(str6, 101, (byte)3);
      localPrintStream.println(str6);
      localPrintStream.println();
      str6 = "Points Earned: " + paramInt;
      str6 = TextPrinter.getRightAlignedText(str6, 101, (byte)3);
      localPrintStream.println(str6);
      localPrintStream.println();
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      UICommon.showDelayedErrorMessage("Not able to open the output file for printing.", "Error", MainWindow.instance);
      this._mFileName.requestFocusInWindow();
      return;
    }
    catch (DBException localDBException)
    {
      localDBException.printStackTrace();
      UICommon.showDelayedErrorMessage("Internal Error generating the report.\n\nTry again later. If the problem persists contact administrator..", "Internal Error", MainWindow.instance);
      return;
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
      UICommon.showDelayedErrorMessage("Internal Error generating the report.\n\nTry again later. If the problem persists contact administrator..", "Internal Error", MainWindow.instance);
      return;
    }
    finally
    {
      if (localPrintStream != null) {
        localPrintStream.close();
      }
    }
    UICommon.showDelayedMessage("File created successfully at\n" + str2, "Success", MainWindow.instance);
  }
  
  private void sendToPDF(double paramDouble, int paramInt, String paramString, Date paramDate1, Date paramDate2)
  {
    String str1 = this._mFileName.getText().trim();
    if (str1.length() == 0)
    {
      UICommon.showError("File name cannot be empty.", "Error", MainWindow.instance);
      this._mFileName.requestFocusInWindow();
      return;
    }
    File localFile = new File(str1);
    String str2 = null;
    String str3 = "dm/jb/ui/inv/CustomerSalesHistory.jasper";
    if (localFile.isDirectory())
    {
      str2 = localFile + File.separator + "CustomerSalesHistory.pdf";
    }
    else
    {
      if (localFile.exists())
      {
        int i = UICommon.showQuestion(str1 + " already exists.\nDo you want to replace it ?", "Confirm", MainWindow.instance);
        if (i != 1) {
          return;
        }
      }
      str2 = str1;
    }
    try
    {
      HashMap localHashMap = new HashMap();
      localHashMap.put("REPORT_TITLE", "Customer Purchase History");
      localHashMap.put("TOTAL_POINTS", Integer.valueOf(paramInt));
      localHashMap.put("TOTAL_PURCHASE", InternalAmount.toString(paramDouble) + " " + CommonConfig.getInstance().country.currency);
      localHashMap.put("CUSTOMER_DETAILS", paramString);
      SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(CommonConfig.getInstance().dateFormat);
      String str4 = "";
      if (paramDate1 != null) {
        str4 = localSimpleDateFormat.format(paramDate1);
      }
      String str5 = "Current";
      if (paramDate2 != null) {
        str5 = localSimpleDateFormat.format(paramDate2);
      }
      localHashMap.put("REPORT_DURATION", str4 + " To " + str5);
      localHashMap.put("REPORT_CURRENCY", CommonConfig.getInstance().country.currency);
      try
      {
        InputStream localInputStream1 = CompanyInfoTableDef.getInstance().getCompany().getReportImage();
        localHashMap.put("COMPANY_LOGO", localInputStream1);
      }
      catch (DBException localDBException)
      {
        localDBException.printStackTrace();
      }
      InputStream localInputStream2 = getClass().getClassLoader().getResourceAsStream(str3);
      if (localInputStream2 == null)
      {
        UICommon.showError("Not able to find the report template.\n\nContact administrator.", "Internal Error", MainWindow.instance);
        return;
      }
      JasperPrint localJasperPrint = null;
      localJasperPrint = JasperFillManager.fillReport(localInputStream2, localHashMap, new PurchaseHistoryDS(this._mModel._mBillRows));
      JasperExportManager.exportReportToPdfFile(localJasperPrint, str2);
    }
    catch (JRException localJRException)
    {
      localJRException.printStackTrace();
      UICommon.showDelayedErrorMessage("Internal error generating report.\n\nContact administrator.", "Internal Error", MainWindow.instance);
      return;
    }
    UICommon.showDelayedMessage("File created successfully at\n" + str2, "Success", MainWindow.instance);
  }
  
  private class PurchaseHistoryModel
    extends DefaultTableModel
  {
    String[] columnNames = { "Sl. No", "Reciept No.", "Date", "Receipt Amount (" + CommonConfig.getInstance().country.currency + ")", "Loyalty Claimed", "Loyalty Redeemed" };
    private ArrayList<BillTableRow> _mBillRows = new ArrayList();
    
    private PurchaseHistoryModel() {}
    
    public int getColumnCount()
    {
      return this.columnNames.length;
    }
    
    public String getColumnName(int paramInt)
    {
      return this.columnNames[paramInt];
    }
    
    public Class getColumnClass(int paramInt)
    {
      return Integer.class;
    }
    
    public boolean isCellEditable(int paramInt1, int paramInt2)
    {
      return false;
    }
    
    void addHistoryRow(BillTableRow paramBillTableRow)
    {
      int i = getRowCount();
      i++;
      SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(CommonConfig.getInstance().dateFormat);
      Object[] arrayOfObject = { Integer.valueOf(i), Integer.valueOf(paramBillTableRow.getBillNo()), localSimpleDateFormat.format(paramBillTableRow.getBillDate()), InternalAmount.toString(paramBillTableRow.getAmount()), Integer.valueOf(paramBillTableRow.getPointsAwarded()), Integer.valueOf(paramBillTableRow.getPointsRedeemed()) };
      addRow(arrayOfObject);
      this._mBillRows.add(paramBillTableRow);
    }
    
    public BillTableRow getRowAt(int paramInt)
    {
      return (BillTableRow)this._mBillRows.get(paramInt);
    }
    
    public void removeAllHistoryRows()
    {
      this._mBillRows.clear();
      int i = getRowCount();
      for (int j = 0; j < i; j++) {
        removeRow(0);
      }
    }
  }
  
  private class PurchaseHistoryDS
    implements JRDataSource
  {
    private ArrayList<BillTableRow> _mBillRows = null;
    int currentIndex = 0;
    SimpleDateFormat fmt = new SimpleDateFormat(CommonConfig.getInstance().dateFormat);
    
    public PurchaseHistoryDS()
    {
      Object localObject;
      this._mBillRows = localObject;
      this.currentIndex = 0;
    }
    
    public boolean next()
    {
      if (this._mBillRows.size() == this.currentIndex) {
        return false;
      }
      this.currentIndex += 1;
      return this._mBillRows.size() >= this.currentIndex;
    }
    
    public Object getFieldValue(JRField paramJRField)
    {
      return getFieldValueInternal(paramJRField.getName());
    }
    
    private Object getFieldValueInternal(String paramString)
    {
      if (paramString.equals("REPORT_CURRENCY")) {
        return "( " + CommonConfig.getInstance().country.currency + " )";
      }
      if (paramString.equals("SL_NO")) {
        return Integer.valueOf(this.currentIndex);
      }
      if (paramString.equals("RCPT_NO")) {
        return Integer.valueOf(((BillTableRow)this._mBillRows.get(this.currentIndex - 1)).getBillNo());
      }
      if (paramString.equals("RCPT_DATE")) {
        return this.fmt.format(((BillTableRow)this._mBillRows.get(this.currentIndex - 1)).getBillDate());
      }
      if (paramString.equals("RCPT_AMOUNT")) {
        return InternalAmount.toString(((BillTableRow)this._mBillRows.get(this.currentIndex - 1)).getAmount());
      }
      if (paramString.equals("POINTS_EARNED")) {
        return Integer.valueOf(((BillTableRow)this._mBillRows.get(this.currentIndex - 1)).getPointsAwarded());
      }
      if (paramString.equals("POINTS_USED")) {
        return Integer.valueOf(((BillTableRow)this._mBillRows.get(this.currentIndex - 1)).getPointsRedeemed());
      }
      return "NA";
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.inv.CustomerPurchaseHistoryUI
 * JD-Core Version:    0.7.0.1
 */