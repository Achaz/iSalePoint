package dm.jb.ui.inv;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.IDateEditor;
import com.toedter.calendar.JDateChooser;
import dm.jb.db.objects.CompanyInfoRow;
import dm.jb.db.objects.CompanyInfoTableDef;
import dm.jb.db.objects.PoEntryRow;
import dm.jb.db.objects.PoEntryTableDef;
import dm.jb.db.objects.PoInfoRow;
import dm.jb.db.objects.PoInfoTableDef;
import dm.jb.db.objects.ProductRow;
import dm.jb.db.objects.StockAndProductRow;
import dm.jb.db.objects.StockAndProductTableDef;
import dm.jb.db.objects.StoreInfoRow;
import dm.jb.db.objects.StoreInfoTableDef;
import dm.jb.db.objects.UserInfoRow;
import dm.jb.db.objects.UserInfoTableDef;
import dm.jb.db.objects.VendorRow;
import dm.jb.db.objects.VendorTableDef;
import dm.jb.db.objects.WearehouseInfoRow;
import dm.jb.db.objects.WearehouseInfoTableDef;
import dm.jb.printing.JePrinterException;
import dm.jb.printing.Print;
import dm.jb.ui.MainWindow;
import dm.jb.ui.common.HelpButton;
import dm.jb.ui.common.ShortKeyCommon;
import dm.jb.ui.settings.CountryInfo;
import dm.tools.db.DBConnection;
import dm.tools.db.DBException;
import dm.tools.db.DBRow;
import dm.tools.db.Db;
import dm.tools.types.InternalAmount;
import dm.tools.types.InternalQuantity;
import dm.tools.ui.AbstractMainPanel;
import dm.tools.ui.NonEditableJXTable;
import dm.tools.ui.UICommon;
import dm.tools.ui.components.JBIntegerTextField;
import dm.tools.ui.components.JBSearchButton;
import dm.tools.ui.components.JTextSeparator;
import dm.tools.utils.CommonConfig;
import dm.tools.utils.Validation;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXLabel;

public class PurchaseOrderUIPanel
  extends AbstractMainPanel
{
  public static PurchaseOrderUIPanel INSTANCE = new PurchaseOrderUIPanel();
  private JComboBox _mVendor = null;
  private JBIntegerTextField _mProductId = null;
  private JTextField _mProductName = null;
  private JTextField _mQuantity = null;
  private NonEditableJXTable _mTable = null;
  private POTableModel _mModel = new POTableModel(null);
  private ArrayList<DBRow> _mVendorList = null;
  private StockAndProductRow _mSelectedProduct = null;
  private JLabel _mQtyUnitLbl = null;
  private JXButton _mDeleteButton = null;
  private JTextField _mOfferPrice = null;
  private JLabel _mPriceLabel = null;
  private JTextField _mPONumber = null;
  private JDateChooser _mExpectedDate = null;
  private boolean _mInEditMode = false;
  private JXButton _mUpdateButton = null;
  private JButton _mCreateButton = null;
  private JXButton _mDeletePOButton = null;
  private JXButton _mPrintButton = null;
  private PoInfoRow _mSelectedPO = null;
  private POAddressWindow _mAddressWindow = new POAddressWindow();
  private String _mBillAddress = null;
  private String _mShipAddress = null;
  
  private PurchaseOrderUIPanel()
  {
    initUI();
    InputMap localInputMap = getInputMap(2);
    String str = "F4Action";
    localInputMap.put(KeyStroke.getKeyStroke("F4"), str);
    Object localObject = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PurchaseOrderUIPanel.this.addClicked();
      }
    };
    getActionMap().put(str, (Action)localObject);
    str = "F8Action";
    localInputMap.put(KeyStroke.getKeyStroke("F8"), str);
    localObject = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PurchaseOrderUIPanel.this.detailsButtonClicked();
      }
    };
    getActionMap().put(str, (Action)localObject);
    str = "F6Action";
    localInputMap.put(KeyStroke.getKeyStroke("F6"), str);
    localObject = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PurchaseOrderUIPanel.this.setProduct(null);
        PurchaseOrderUIPanel.this._mTable.clearSelection();
      }
    };
    getActionMap().put(str, (Action)localObject);
    str = "F5";
    localInputMap.put(KeyStroke.getKeyStroke("F5"), str);
    localObject = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PurchaseOrderUIPanel.this.deleteClicked();
      }
    };
    getActionMap().put(str, (Action)localObject);
    str = "F7Action";
    localInputMap.put(KeyStroke.getKeyStroke("F7"), str);
    localObject = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PurchaseOrderUIPanel.this.detailsButtonClicked();
      }
    };
    getActionMap().put(str, (Action)localObject);
  }
  
  public void setDefaultFocus()
  {
    this._mProductId.requestFocusInWindow();
    getRootPane().setDefaultButton(this._mCreateButton);
  }
  
  public JPanel getPanel()
  {
    return this;
  }
  
  public void windowDisplayed()
  {
    populateVendorDetails();
    setProduct(null);
    this._mDeleteButton.setEnabled(false);
    this._mUpdateButton.setEnabled(false);
    this._mDeletePOButton.setEnabled(false);
    this._mPrintButton.setEnabled(false);
    resetPoClicked();
  }
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("10px,100px,10px,100px,3px,40px,100px,3px,40px,10px,pref:grow,10px:grow", "10px,25px,10px,25px, 10px,25px,30px,25px,10px,25px,10px,25px, 10px,25px, 10px:grow,250px,10px,30px, 20px,30px,10px");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    int i = 2;
    JXLabel localJXLabel = new JXLabel("PO Number : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJXLabel, localCellConstraints);
    this._mPONumber = new JTextField();
    ShortKeyCommon.shortKeyForLabels(localJXLabel, 79, this._mPONumber);
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mPONumber, localCellConstraints);
    this._mPONumber.setToolTipText("Purchase Order number");
    JBSearchButton localJBSearchButton = new JBSearchButton(false);
    localCellConstraints.xywh(6, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJBSearchButton, localCellConstraints);
    localJBSearchButton.setToolTipText("Search Purchase Order by  Purchase Order Number.");
    localJBSearchButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PurchaseOrderUIPanel.this.poLoadClicked();
      }
    });
    this._mPONumber.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 114) {
          PurchaseOrderUIPanel.this.poLoadClicked();
        }
      }
    });
    i += 2;
    localJXLabel = new JXLabel("Vendor : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJXLabel, localCellConstraints);
    this._mVendor = new JComboBox();
    ShortKeyCommon.shortKeyForLabels(localJXLabel, 86, this._mVendor);
    localCellConstraints.xywh(4, i, 4, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mVendor, localCellConstraints);
    this._mVendor.setToolTipText("Vendor to purchase the product from");
    i += 2;
    localJXLabel = new JXLabel("Expected Date : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJXLabel, localCellConstraints);
    this._mExpectedDate = new JDateChooser();
    this._mExpectedDate.setDateFormatString(CommonConfig.getInstance().dateFormat);
    ShortKeyCommon.shortKeyForLabels(localJXLabel, 88, this._mExpectedDate.getDateEditor().getUiComponent());
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mExpectedDate, localCellConstraints);
    this._mExpectedDate.setToolTipText("Expected arrival date.");
    i++;
    localCellConstraints.xywh(2, i, 8, 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(new JTextSeparator("Product"), localCellConstraints);
    i++;
    localJXLabel = new JXLabel("Product Code : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJXLabel, localCellConstraints);
    this._mProductId = new JBIntegerTextField();
    ShortKeyCommon.shortKeyForLabels(localJXLabel, 80, this._mProductId);
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mProductId, localCellConstraints);
    this._mProductId.setToolTipText("Product code to select the product.");
    this._mProductId.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 114) {
          PurchaseOrderUIPanel.this.searchByProductId();
        }
      }
    });
    this._mProductId.setBackground(UICommon.MANDATORY_COLOR);
    localJBSearchButton = new JBSearchButton(false);
    localCellConstraints.xywh(6, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJBSearchButton, localCellConstraints);
    localJBSearchButton.setToolTipText("Search the product based on Product Code.");
    localJBSearchButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PurchaseOrderUIPanel.this.searchByProductId();
      }
    });
    i += 2;
    localJXLabel = new JXLabel("Product Name : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJXLabel, localCellConstraints);
    this._mProductName = new JTextField();
    localCellConstraints.xywh(4, i, 4, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mProductName, localCellConstraints);
    this._mProductName.setToolTipText("Product name to select the product.");
    this._mProductName.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 114) {
          PurchaseOrderUIPanel.this.searchByProductName();
        }
      }
    });
    this._mProductName.setBackground(UICommon.MANDATORY_COLOR);
    ShortKeyCommon.shortKeyForLabels(localJXLabel, 78, this._mProductName);
    localJBSearchButton = new JBSearchButton(false);
    localCellConstraints.xywh(9, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJBSearchButton, localCellConstraints);
    localJBSearchButton.setToolTipText("Search the product based on Product Name.");
    localJBSearchButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PurchaseOrderUIPanel.this.searchByProductName();
      }
    });
    i += 2;
    localJXLabel = new JXLabel("Quantity : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJXLabel, localCellConstraints);
    this._mQuantity = new JTextField();
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mQuantity, localCellConstraints);
    this._mQuantity.setHorizontalAlignment(4);
    this._mQuantity.setToolTipText("Product quantity to reorder.");
    this._mQuantity.setBackground(UICommon.MANDATORY_COLOR);
    ShortKeyCommon.shortKeyForLabels(localJXLabel, 81, this._mQuantity);
    this._mQtyUnitLbl = new JLabel("");
    localCellConstraints.xywh(6, i, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mQtyUnitLbl, localCellConstraints);
    localCellConstraints.xywh(11, 2, 1, 13, CellConstraints.FILL, CellConstraints.BOTTOM);
    add(getActionButtonPanel(), localCellConstraints);
    i += 2;
    localJXLabel = new JXLabel("Offer Price : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJXLabel, localCellConstraints);
    this._mOfferPrice = new JTextField();
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mOfferPrice, localCellConstraints);
    this._mOfferPrice.setToolTipText("Price being offered for the product.");
    this._mOfferPrice.setBackground(UICommon.MANDATORY_COLOR);
    ShortKeyCommon.shortKeyForLabels(localJXLabel, 69, this._mProductName);
    this._mOfferPrice.setHorizontalAlignment(4);
    this._mPriceLabel = new JXLabel(" " + CommonConfig.getInstance().country.currency);
    localCellConstraints.xywh(6, i, 2, 1, CellConstraints.LEFT, CellConstraints.FILL);
    add(this._mPriceLabel, localCellConstraints);
    i += 2;
    this._mTable = new NonEditableJXTable(this._mModel);
    JScrollPane localJScrollPane = new JScrollPane(this._mTable);
    localCellConstraints.xywh(2, i, 10, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJScrollPane, localCellConstraints);
    this._mTable.setDefaultRenderer(Object.class, new TestRenderer());
    this._mTable.setSelectionMode(0);
    this._mTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent paramAnonymousListSelectionEvent)
      {
        if (paramAnonymousListSelectionEvent.getValueIsAdjusting()) {
          return;
        }
        int i = PurchaseOrderUIPanel.this._mTable.getSelectedRow();
        if (i != -1)
        {
          PurchaseOrderUIPanel.this._mDeleteButton.setEnabled(true);
          i = PurchaseOrderUIPanel.this._mTable.convertRowIndexToModel(i);
          PurchaseOrderUIPanel.POTableRow localPOTableRow = (PurchaseOrderUIPanel.POTableRow)PurchaseOrderUIPanel.this._mModel.getValueAt(i, 2);
          PurchaseOrderUIPanel.this.poRowSelected(localPOTableRow);
        }
        if (PurchaseOrderUIPanel.this._mTable.getSelectedRowCount() == 0)
        {
          PurchaseOrderUIPanel.this._mDeleteButton.setEnabled(false);
          PurchaseOrderUIPanel.this.setProduct(null);
          PurchaseOrderUIPanel.this.poRowSelected(null);
        }
      }
    });
    i += 2;
    localCellConstraints.xywh(1, i, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getOperationButtonPanel(), localCellConstraints);
    i++;
    localCellConstraints.xywh(1, i, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(new JSeparator(), localCellConstraints);
    i++;
    localCellConstraints.xywh(1, i, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getButtonPanel(), localCellConstraints);
    this._mTable.getColumn(0).setPreferredWidth(40);
  }
  
  private JPanel getActionButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    localJPanel.setBorder(BorderFactory.createEtchedBorder());
    FormLayout localFormLayout = new FormLayout("5px,120px,5px", "5px,30px,10px,30px,10px,30px,10px,30px,5px");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    JXButton localJXButton = new JXButton("Add/Apply [F4]");
    localCellConstraints.xywh(2, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.setToolTipText("Add the product to the Purchase Order");
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PurchaseOrderUIPanel.this.addClicked();
      }
    });
    localJXButton = new JXButton("Delete [F5]");
    localCellConstraints.xywh(2, 4, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.setToolTipText("Delete the selected product from the Purchase Order");
    this._mDeleteButton = localJXButton;
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PurchaseOrderUIPanel.this.deleteClicked();
      }
    });
    localJXButton = new JXButton("Reset [F6]");
    localCellConstraints.xywh(2, 6, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.setToolTipText("Clear product details");
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PurchaseOrderUIPanel.this.setProduct(null);
        PurchaseOrderUIPanel.this._mTable.clearSelection();
      }
    });
    localJXButton = new JXButton("Details [F7]");
    localCellConstraints.xywh(2, 8, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.setToolTipText("Details of the product");
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PurchaseOrderUIPanel.this.detailsButtonClicked();
      }
    });
    return localJPanel;
  }
  
  private JPanel getOperationButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("10px:grow,100px,10px,100px,10px,100px,10px,100px,10px,100px,10px", "30px");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    JXButton localJXButton = new JXButton("Create");
    this._mCreateButton = localJXButton;
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.setToolTipText("Create the Purchase Order");
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PurchaseOrderUIPanel.this.saveClicked();
      }
    });
    localJXButton = new JXButton("Update");
    this._mUpdateButton = localJXButton;
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.setToolTipText("Update the Purchase Order");
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PurchaseOrderUIPanel.this.updateClicked();
      }
    });
    localJXButton.setMnemonic(85);
    localJXButton = new JXButton("Delete");
    this._mDeletePOButton = localJXButton;
    localCellConstraints.xywh(6, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.setToolTipText("Delete the selected PO");
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PurchaseOrderUIPanel.this.deletePoClicked();
      }
    });
    localJXButton.setMnemonic(68);
    localJXButton = new JXButton("Reset");
    localCellConstraints.xywh(8, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.setToolTipText("Clear all the fields");
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PurchaseOrderUIPanel.this.resetPoClicked();
      }
    });
    localJXButton.setMnemonic(82);
    localJXButton = new JXButton("Print");
    localCellConstraints.xywh(10, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.setToolTipText("Print the Purchase Order");
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PurchaseOrderUIPanel.this.printClicked();
      }
    });
    this._mPrintButton = localJXButton;
    UICommon.addMnemonicTrigger(localJXButton, 2);
    localJXButton.setMnemonic(80);
    return localJPanel;
  }
  
  private JPanel getButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("10px,100px,pref:grow,100px,10px", "30px");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    Object localObject = new JXButton("Close");
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JXButton)localObject).setToolTipText("Close the window");
    ((JXButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PurchaseOrderUIPanel.this.closeWindow();
      }
    });
    localObject = new HelpButton("ISP_PO_MANAGE");
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JXButton)localObject).setToolTipText("Help");
    String str = "F1Action";
    final HelpButton localHelpButton = (HelpButton)localObject;
    InputMap localInputMap = getInputMap(2);
    localInputMap.put(KeyStroke.getKeyStroke("F1"), str);
    AbstractAction local23 = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        localHelpButton.actionPerformed(paramAnonymousActionEvent);
      }
    };
    getActionMap().put(str, local23);
    return localJPanel;
  }
  
  private void populateVendorDetails()
  {
    ArrayList localArrayList = null;
    this._mVendor.removeAllItems();
    try
    {
      localArrayList = VendorTableDef.getInstance().getAllValues();
    }
    catch (DBException localDBException)
    {
      localDBException.printStackTrace();
      UICommon.showError("Internal error retrieving Vendor Details.\n\nTry again later. If the problem persists  contact administrator.", "Internal Error", MainWindow.instance);
      return;
    }
    if (this._mVendorList != null) {
      this._mVendorList.clear();
    }
    this._mVendorList = localArrayList;
    if ((localArrayList == null) || (localArrayList.size() == 0)) {
      return;
    }
    Iterator localIterator = localArrayList.iterator();
    while (localIterator.hasNext())
    {
      DBRow localDBRow = (DBRow)localIterator.next();
      this._mVendor.addItem(localDBRow);
    }
  }
  
  private VendorRow findVendorForId(int paramInt)
  {
    if ((this._mVendorList == null) || (this._mVendorList.size() == 0)) {
      return null;
    }
    Iterator localIterator = this._mVendorList.iterator();
    while (localIterator.hasNext())
    {
      DBRow localDBRow = (DBRow)localIterator.next();
      VendorRow localVendorRow = (VendorRow)localDBRow;
      if (localVendorRow.getVendorId() == paramInt) {
        return localVendorRow;
      }
    }
    return null;
  }
  
  private void searchByProductId()
  {
    String str = this._mProductId.getText().trim();
    Object localObject1;
    Object localObject2;
    if (str.length() == 0)
    {
      localObject1 = new ProductSearchPanel(MainWindow.instance);
      ((ProductSearchPanel)localObject1).setDefaultFocus();
      ((ProductSearchPanel)localObject1).setSingleSelectionMode(true);
      ((ProductSearchPanel)localObject1).setVisible(true);
      if (((ProductSearchPanel)localObject1).isCancelled())
      {
        this._mProductId.requestFocusInWindow();
        return;
      }
      localObject2 = ((ProductSearchPanel)localObject1).getSelectedProducts();
      if ((localObject2 == null) || (localObject2.length == 0))
      {
        this._mProductId.requestFocusInWindow();
        return;
      }
      try
      {
        ArrayList localArrayList = StockAndProductTableDef.getInstance().getAllValuesWithWhereClause("PROD_INDEX=" + localObject2[0].getProdIndex());
        if ((localArrayList == null) || (localArrayList.size() == 0))
        {
          UICommon.showError("Internal Error retrieving current stock details.\n\nTry again later. If the problem persists contact administrator.", "Internal Error", MainWindow.instance);
          this._mProductId.requestFocusInWindow();
          return;
        }
        StockAndProductRow localStockAndProductRow = (StockAndProductRow)localArrayList.get(0);
        setProduct(localStockAndProductRow);
      }
      catch (DBException localDBException2)
      {
        localDBException2.printStackTrace();
        UICommon.showError("Internal Error retrieving current stock details.\n\nTry again later. If the problem persists contact administrator.", "Internal Error", MainWindow.instance);
        this._mProductId.requestFocusInWindow();
        return;
      }
      this._mQuantity.requestFocusInWindow();
      return;
    }
    try
    {
      localObject1 = StockAndProductTableDef.getInstance().getAllValuesWithWhereClause("PRODUCT_CODE=" + str);
      if ((localObject1 == null) || (((ArrayList)localObject1).size() == 0))
      {
        UICommon.showError("No product found.", "No record found", MainWindow.instance);
        this._mProductId.requestFocusInWindow();
        return;
      }
      localObject2 = (StockAndProductRow)((ArrayList)localObject1).get(0);
      setProduct((StockAndProductRow)localObject2);
    }
    catch (DBException localDBException1)
    {
      localDBException1.printStackTrace();
      UICommon.showError("Internal Error retrieving current stock details.\n\nTry again later. If the problem persists contact administrator.", "Internal Error", MainWindow.instance);
      return;
    }
    this._mQuantity.requestFocusInWindow();
  }
  
  private void searchByProductName()
  {
    String str = this._mProductName.getText().trim();
    ProductRow localProductRow = null;
    Object localObject1;
    Object localObject2;
    if (str.length() == 0)
    {
      localObject1 = new ProductSearchPanel(MainWindow.instance);
      ((ProductSearchPanel)localObject1).setDefaultFocus();
      ((ProductSearchPanel)localObject1).setSingleSelectionMode(true);
      ((ProductSearchPanel)localObject1).setVisible(true);
      if (((ProductSearchPanel)localObject1).isCancelled())
      {
        this._mProductName.requestFocusInWindow();
        return;
      }
      localObject2 = ((ProductSearchPanel)localObject1).getSelectedProducts();
      if ((localObject2 == null) || (localObject2.length == 0))
      {
        this._mProductName.requestFocusInWindow();
        return;
      }
      localProductRow = localObject2[0];
    }
    else
    {
      localObject1 = new ProductSearchPanel(MainWindow.instance);
      ((ProductSearchPanel)localObject1).setSingleSelectionMode(true);
      if (!str.endsWith("%")) {
        str = str + "%";
      }
      localProductRow = ((ProductSearchPanel)localObject1).searchProduct(str, "", "", MainWindow.instance);
      if (localProductRow == null)
      {
        this._mProductName.requestFocusInWindow();
        return;
      }
    }
    try
    {
      localObject1 = StockAndProductTableDef.getInstance().getAllValuesWithWhereClause("PROD_INDEX=" + localProductRow.getProdIndex());
      if ((localObject1 == null) || (((ArrayList)localObject1).size() == 0))
      {
        UICommon.showError("Internal Error retrieving current stock details.\n\nTry again later. If the problem persists contact administrator.", "Internal Error", MainWindow.instance);
        this._mProductName.requestFocusInWindow();
        return;
      }
      localObject2 = (StockAndProductRow)((ArrayList)localObject1).get(0);
      setProduct((StockAndProductRow)localObject2);
    }
    catch (DBException localDBException)
    {
      localDBException.printStackTrace();
      UICommon.showError("Internal Error retrieving current stock details.\n\nTry again later. If the problem persists contact administrator.", "Internal Error", MainWindow.instance);
      this._mProductName.requestFocusInWindow();
      return;
    }
    this._mQuantity.requestFocusInWindow();
  }
  
  private void setProduct(StockAndProductRow paramStockAndProductRow)
  {
    this._mSelectedProduct = paramStockAndProductRow;
    if (paramStockAndProductRow == null)
    {
      this._mProductId.setText("");
      this._mProductName.setText("");
      this._mQuantity.setText("");
      if (this._mVendor.getItemCount() > 0) {
        this._mVendor.setSelectedIndex(0);
      }
      this._mProductId.requestFocusInWindow();
      this._mQtyUnitLbl.setText("");
      this._mOfferPrice.setText("");
      this._mPriceLabel.setText(CommonConfig.getInstance().country.currency);
      return;
    }
    this._mProductId.setText(paramStockAndProductRow.getProductCode());
    this._mProductName.setText(paramStockAndProductRow.getProdName());
    this._mQtyUnitLbl.setText("  " + InternalQuantity.quantityUnits[paramStockAndProductRow.getProdUnit()]);
    int i = paramStockAndProductRow.getVendorId();
    VendorRow localVendorRow = findVendorForId(i);
    InternalAmount localInternalAmount = new InternalAmount(paramStockAndProductRow.getPurchasePrice());
    this._mOfferPrice.setText(localInternalAmount.toString());
    this._mPriceLabel.setText(CommonConfig.getInstance().country.currency + " per " + InternalQuantity.quantityUnits[paramStockAndProductRow.getProdUnit()]);
    if (localVendorRow == null) {
      return;
    }
    this._mVendor.setSelectedItem(localVendorRow);
  }
  
  private void poRowSelected(POTableRow paramPOTableRow)
  {
    if (paramPOTableRow == null)
    {
      this._mProductId.setEditable(true);
      this._mProductName.setEditable(true);
      this._mQuantity.setEditable(true);
      this._mOfferPrice.setEditable(true);
      return;
    }
    setProduct(paramPOTableRow.product);
    if (paramPOTableRow.product == null)
    {
      this._mProductId.setEditable(false);
      this._mProductName.setEditable(false);
      this._mQuantity.setEditable(false);
      this._mOfferPrice.setEditable(false);
      return;
    }
    this._mProductId.setEditable(true);
    this._mProductName.setEditable(true);
    this._mQuantity.setEditable(true);
    this._mOfferPrice.setEditable(true);
    InternalQuantity localInternalQuantity = new InternalQuantity(paramPOTableRow.quantity, paramPOTableRow.product.getProdUnit(), false);
    this._mQuantity.setText(localInternalQuantity.toString());
    InternalAmount localInternalAmount = new InternalAmount(paramPOTableRow.offerPrice);
    this._mOfferPrice.setText(localInternalAmount.toString());
  }
  
  private void addClicked()
  {
    if (this._mSelectedProduct == null)
    {
      UICommon.showError("No product selected.\nEnter a product code and click the button to load product, or search the product using Product Name.", "Error", MainWindow.instance);
      this._mProductId.requestFocusInWindow();
      return;
    }
    String str1 = this._mQuantity.getText().trim();
    if (str1.length() == 0)
    {
      UICommon.showError("Quantity cannot be empty.", "Error", MainWindow.instance);
      this._mQuantity.requestFocusInWindow();
      return;
    }
    if (!Validation.isValidFloat(str1, 8, false))
    {
      UICommon.showError("Invalid quantity.", "Error", MainWindow.instance);
      this._mQuantity.requestFocusInWindow();
      return;
    }
    if ((str1.contains(".")) && (!InternalQuantity.isUnitFractionAllowed(this._mSelectedProduct.getProdUnit())))
    {
      UICommon.showError("Fraction quantity is not alowed for this product.", "Error", MainWindow.instance);
      this._mQuantity.requestFocusInWindow();
      return;
    }
    String str2 = this._mOfferPrice.getText().trim();
    if (str2.length() == 0)
    {
      UICommon.showError("Offer price cannot be empty.", "Error", MainWindow.instance);
      this._mOfferPrice.requestFocusInWindow();
      return;
    }
    if (!Validation.isValidAmount(str2))
    {
      UICommon.showError("Offer price is invalid.", "Error", MainWindow.instance);
      this._mOfferPrice.requestFocusInWindow();
      return;
    }
    double d = Double.valueOf(str2).doubleValue();
    POTableRow localPOTableRow = new POTableRow(this._mSelectedProduct, Double.valueOf(str1).doubleValue(), d);
    if (this._mTable.getSelectedRowCount() > 0)
    {
      this._mModel.updateRow(localPOTableRow);
      return;
    }
    this._mModel.addPoRow(localPOTableRow);
    this._mTable.updateUI();
    setProduct(null);
  }
  
  private void deleteClicked()
  {
    int i = this._mTable.getSelectedRow();
    i = this._mTable.convertRowIndexToModel(i);
    this._mModel.removeRow(i);
  }
  
  private void updateClicked()
  {
    if (this._mTable.getRowCount() == 0)
    {
      UICommon.showError("No products were selected for Purchase Order.", "Error", MainWindow.instance);
      return;
    }
    PoInfoRow localPoInfoRow = this._mSelectedPO;
    DBConnection localDBConnection = Db.getConnection();
    java.sql.Date localDate = new java.sql.Date(new java.util.Date().getTime());
    Time localTime = new Time(new java.util.Date().getTime());
    VendorRow localVendorRow = (VendorRow)this._mVendor.getSelectedItem();
    UserInfoRow localUserInfoRow = UserInfoTableDef.getCurrentUser();
    this._mAddressWindow.selectNone();
    this._mAddressWindow.setBillToAddress(localPoInfoRow.getBillTo());
    this._mAddressWindow.setShipToAddress(localPoInfoRow.getShipTo());
    this._mAddressWindow.setVisible(true);
    if (this._mAddressWindow.isCancelled()) {
      return;
    }
    try
    {
      localDBConnection.openTrans();
      int i = this._mTable.getRowCount();
      String str1 = this._mAddressWindow.getBillToAddress();
      String str2 = this._mAddressWindow.getShipToAddress();
      localPoInfoRow.setValues(localDate, localTime, i, "N", localVendorRow.getVendorId(), localUserInfoRow.getUserIndex(), this._mExpectedDate.getDate() == null ? null : new java.sql.Date(this._mExpectedDate.getDate().getTime()), str1, str2);
      for (int j = 0; j < i; j++)
      {
        POTableRow localPOTableRow = this._mModel.getPoRow(j);
        PoEntryRow localPoEntryRow = localPOTableRow.getPoEntryRow(j);
        localPoInfoRow.addOrUpdateEntryRow(localPoEntryRow);
      }
      localPoInfoRow.update(true);
      localDBConnection.commit();
    }
    catch (DBException localDBException)
    {
      localDBConnection.rollbackNoExp();
      localDBException.printStackTrace();
      UICommon.showError("Internal Error updating Purchase Order.\n\nTry again later. If the problem persists contact administrator.", "Internal Error", MainWindow.instance);
      return;
    }
    UICommon.showMessage("Purchase Order " + this._mSelectedPO.getPoIndex() + " updated successfully.", "Success", MainWindow.instance);
  }
  
  private void saveClicked()
  {
    if (this._mTable.getRowCount() == 0)
    {
      UICommon.showError("No products were selected for Purchase Order.", "Error", MainWindow.instance);
      return;
    }
    if (this._mInEditMode)
    {
      int i = UICommon.showQuestionWithCancel("The Purchase Order is in edit mode. Do you want to create a new one with the values on the screen.?", "Confirm Duplication", MainWindow.instance);
      if (i != 1) {
        return;
      }
    }
    this._mAddressWindow.resetFields();
    this._mAddressWindow.setVisible(true);
    if (this._mAddressWindow.isCancelled()) {
      return;
    }
    String str1 = this._mAddressWindow.getBillToAddress();
    String str2 = this._mAddressWindow.getShipToAddress();
    PoInfoRow localPoInfoRow = PoInfoTableDef.getInstance().getNewRow();
    DBConnection localDBConnection = Db.getConnection();
    java.sql.Date localDate = new java.sql.Date(new java.util.Date().getTime());
    Time localTime = new Time(new java.util.Date().getTime());
    VendorRow localVendorRow = (VendorRow)this._mVendor.getSelectedItem();
    UserInfoRow localUserInfoRow = UserInfoTableDef.getCurrentUser();
    try
    {
      localDBConnection.openTrans();
      int j = this._mTable.getRowCount();
      localPoInfoRow.setValues(localDate, localTime, j, "N", localVendorRow.getVendorId(), localUserInfoRow.getUserIndex(), this._mExpectedDate.getDate() == null ? null : new java.sql.Date(this._mExpectedDate.getDate().getTime()), str1, str2);
      for (int m = 0; m < j; m++)
      {
        POTableRow localPOTableRow = this._mModel.getPoRow(m);
        PoEntryRow localPoEntryRow = localPOTableRow.getPoEntryRow(m);
        localPoInfoRow.addOrUpdateEntryRow(localPoEntryRow);
      }
      localPoInfoRow.create();
      localDBConnection.commit();
    }
    catch (DBException localDBException)
    {
      localDBConnection.rollbackNoExp();
      localDBException.printStackTrace();
      UICommon.showError("Internal Error creating Purchase Order.\n\nTry again later. If the problem persists contact administrator.", "Internal Error", MainWindow.instance);
      return;
    }
    resetPoClicked();
    int k = UICommon.showQuestion("Purchase Order " + localPoInfoRow.getPoIndex() + " created successfully.\n\nDo you want to print the Purchase Order ?", "Success", MainWindow.instance);
    if (k != 1) {
      return;
    }
    try
    {
      Print.getInstance().printPO(localPoInfoRow, localVendorRow);
    }
    catch (JePrinterException localJePrinterException)
    {
      localJePrinterException.printStackTrace();
      UICommon.showError("Error printing the bill.\nDetails:\n" + localJePrinterException.toString(), "Printing Error", MainWindow.instance);
      return;
    }
  }
  
  private void printClicked()
  {
    VendorRow localVendorRow = (VendorRow)this._mVendor.getSelectedItem();
    try
    {
      Print.getInstance().printPO(this._mSelectedPO, localVendorRow);
    }
    catch (JePrinterException localJePrinterException)
    {
      localJePrinterException.printStackTrace();
      UICommon.showError("Error printing the bill.\nDetails:\n" + localJePrinterException.toString(), "Printing Error", MainWindow.instance);
      return;
    }
  }
  
  private void resetPoClicked()
  {
    this._mInEditMode = false;
    setProduct(null);
    this._mModel.removeAllRows();
    this._mUpdateButton.setEnabled(false);
    this._mDeletePOButton.setEnabled(false);
    this._mPrintButton.setEnabled(false);
    this._mPONumber.requestFocusInWindow();
    this._mPONumber.setText("");
    this._mExpectedDate.setDate(null);
    this._mSelectedPO = null;
  }
  
  private void poLoadClicked()
  {
    String str = this._mPONumber.getText().trim();
    if (str.length() == 0)
    {
      UICommon.showError("Purchase Order number cannot be empty.", "Error", MainWindow.instance);
      this._mPONumber.requestFocusInWindow();
      return;
    }
    if (!Validation.isValidInt(str, false))
    {
      UICommon.showError("Invalid Purchase Order number.", "Error", MainWindow.instance);
      this._mPONumber.requestFocusInWindow();
      return;
    }
    int i = Integer.valueOf(str).intValue();
    PoInfoRow localPoInfoRow = null;
    try
    {
      localPoInfoRow = PoInfoTableDef.getInstance().findRowByIndex(i);
      if (localPoInfoRow == null)
      {
        UICommon.showError("Purchase Order not found.", "Error", MainWindow.instance);
        this._mPONumber.requestFocusInWindow();
        return;
      }
      this._mModel.removeAllRows();
      java.util.Date localDate = null;
      if (localPoInfoRow.getExpectedDate() != null) {
        localDate = new java.util.Date(localPoInfoRow.getExpectedDate().getTime());
      }
      this._mExpectedDate.setDate(localDate);
      ArrayList localArrayList = localPoInfoRow.getEntries();
      this._mSelectedPO = localPoInfoRow;
      Object localObject;
      if (localArrayList != null)
      {
        Iterator localIterator = localArrayList.iterator();
        while (localIterator.hasNext())
        {
          PoEntryRow localPoEntryRow = (PoEntryRow)localIterator.next();
          StockAndProductRow localStockAndProductRow = (StockAndProductRow)StockAndProductTableDef.getInstance().findRowByIndex(localPoEntryRow.getProductId());
          localPoEntryRow.setProduct(localStockAndProductRow);
          localObject = new POTableRow(localStockAndProductRow, localPoEntryRow.getQuantity(), localPoEntryRow.getPriceExpected());
          this._mModel.addPoRow((POTableRow)localObject);
        }
        this._mSelectedPO.prepareForUpdate();
      }
      else
      {
        UICommon.showWarning("No record found in the Purchase Order.", "Warning", MainWindow.instance);
      }
      this._mInEditMode = true;
      this._mUpdateButton.setEnabled(true);
      this._mDeletePOButton.setEnabled(true);
      this._mPrintButton.setEnabled(true);
      int j = this._mVendor.getItemCount();
      this._mVendor.setSelectedItem(null);
      int k = this._mSelectedPO.getVendorId();
      for (int m = 0; m < j; m++)
      {
        localObject = (VendorRow)this._mVendor.getItemAt(m);
        if (((VendorRow)localObject).getVendorId() == k)
        {
          this._mVendor.setSelectedItem(localObject);
          break;
        }
      }
    }
    catch (DBException localDBException)
    {
      localDBException.printStackTrace();
      UICommon.showError("Internal Error searching for Purchase Order.\n\nTry again later. If the problem persists, contact administrator.", "Internal Error", MainWindow.instance);
      this._mPONumber.requestFocusInWindow();
      return;
    }
  }
  
  private void deletePoClicked()
  {
    int i = UICommon.showQuestion("Are you sure you want to delete this Purchase Order record ? ", "Confirm Deletion", MainWindow.instance);
    if (i != 1) {
      return;
    }
    DBConnection localDBConnection = Db.getConnection();
    try
    {
      localDBConnection.openTrans();
      this._mSelectedPO.delete();
      localDBConnection.endTrans();
    }
    catch (DBException localDBException)
    {
      localDBConnection.rollbackNoExp();
      localDBException.printStackTrace();
      UICommon.showError("Internal Error deleting the Purchase Order records.\n\nTry again later. If the problem persists contact administrator.", "Internal Error", MainWindow.instance);
      return;
    }
    resetPoClicked();
    UICommon.showMessage("Purchase Order deleted successfully.", "Success", MainWindow.instance);
  }
  
  private void detailsButtonClicked()
  {
    String str = this._mProductId.getText().trim();
    if (str.length() == 0)
    {
      UICommon.showError("Product Code cannot be empty.", "Error", MainWindow.instance);
      this._mProductId.requestFocusInWindow();
      return;
    }
    try
    {
      ProductDetailsWindow.showProductDetailsForProductCode(str);
    }
    catch (DBException localDBException)
    {
      localDBException.printStackTrace();
      UICommon.showError("Internal Error retrieving product details.", "Internal Error", MainWindow.instance);
    }
    this._mProductId.requestFocusInWindow();
  }
  
  private class POAddressWindow
    extends JDialog
  {
    private JRadioButton _mShipCompany = null;
    private JRadioButton _mShipWh = null;
    private JRadioButton _mShipStore = null;
    private JRadioButton _mBillCompany = null;
    private JRadioButton _mBillWh = null;
    private JRadioButton _mBillStore = null;
    private JComboBox _mBillToCombo = null;
    private JComboBox _mShipToCombo = null;
    private JTextArea _mBillToAddress = null;
    private JTextArea _mShipToAddress = null;
    private JButton _mOkButton = null;
    private boolean _mIsCancelled = true;
    
    public POAddressWindow()
    {
      super("Billing and Shipping address", true);
      initAddressWindow();
      pack();
      setLocationRelativeTo(MainWindow.instance);
      addWindowListener(new WindowAdapter()
      {
        public void windowOpened(WindowEvent paramAnonymousWindowEvent)
        {
          PurchaseOrderUIPanel.POAddressWindow.this.getRootPane().setDefaultButton(PurchaseOrderUIPanel.POAddressWindow.this._mOkButton);
        }
      });
      addKeyListener(new KeyAdapter()
      {
        public void keyPressed(KeyEvent paramAnonymousKeyEvent)
        {
          if (paramAnonymousKeyEvent.getKeyCode() == 27)
          {
            PurchaseOrderUIPanel.POAddressWindow.this._mIsCancelled = true;
            PurchaseOrderUIPanel.POAddressWindow.this.setVisible(false);
          }
        }
      });
      InputMap localInputMap = ((JPanel)getContentPane()).getInputMap(2);
      String str = "ESCAction";
      localInputMap.put(KeyStroke.getKeyStroke("ESCAPE"), str);
      AbstractAction local3 = new AbstractAction()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          PurchaseOrderUIPanel.POAddressWindow.this._mIsCancelled = true;
          PurchaseOrderUIPanel.POAddressWindow.this.setVisible(false);
        }
      };
      ((JPanel)getContentPane()).getActionMap().put(str, local3);
    }
    
    void resetFields()
    {
      this._mBillCompany.setSelected(true);
      this._mShipCompany.setSelected(true);
      this._mBillToCombo.setEnabled(false);
      this._mShipToCombo.setEnabled(false);
      try
      {
        CompanyInfoRow localCompanyInfoRow = CompanyInfoTableDef.getInstance().getCompany();
        this._mBillToAddress.setText(localCompanyInfoRow.getAddress() + "\nPhone : " + localCompanyInfoRow.getPhone1());
        this._mShipToAddress.setText(localCompanyInfoRow.getAddress() + "\nPhone : " + localCompanyInfoRow.getPhone1());
      }
      catch (DBException localDBException)
      {
        localDBException.printStackTrace();
      }
    }
    
    private void initAddressWindow()
    {
      JPanel localJPanel = (JPanel)getContentPane();
      FormLayout localFormLayout = new FormLayout("10px,90px,10px,90px,90px,90px,20px,90px,10px, 90px,90px,90px,10px", "10px,25px,5px,25px,5px,200px,20px,30px,10px");
      localJPanel.setLayout(localFormLayout);
      CellConstraints localCellConstraints = new CellConstraints();
      localCellConstraints.xywh(2, 2, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
      localJPanel.add(new JLabel("Bill To : "), localCellConstraints);
      localCellConstraints.xywh(4, 2, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
      this._mBillCompany = new JRadioButton("Company");
      localJPanel.add(this._mBillCompany, localCellConstraints);
      this._mBillCompany.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          if (PurchaseOrderUIPanel.POAddressWindow.this._mBillCompany.isSelected())
          {
            PurchaseOrderUIPanel.POAddressWindow.this._mBillToCombo.setEnabled(false);
            try
            {
              CompanyInfoRow localCompanyInfoRow = CompanyInfoTableDef.getInstance().getCompany();
              PurchaseOrderUIPanel.POAddressWindow.this._mBillToAddress.setText(localCompanyInfoRow.getAddress() + "\nPhone : " + localCompanyInfoRow.getPhone1());
            }
            catch (DBException localDBException)
            {
              localDBException.printStackTrace();
            }
          }
        }
      });
      localCellConstraints.xywh(5, 2, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
      this._mBillWh = new JRadioButton("Warehouse");
      localJPanel.add(this._mBillWh, localCellConstraints);
      this._mBillWh.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          PurchaseOrderUIPanel.POAddressWindow.this.billWhClicked();
        }
      });
      localCellConstraints.xywh(6, 2, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
      this._mBillStore = new JRadioButton("Store");
      localJPanel.add(this._mBillStore, localCellConstraints);
      this._mBillStore.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          PurchaseOrderUIPanel.POAddressWindow.this.billStoreClicked();
        }
      });
      ButtonGroup localButtonGroup = new ButtonGroup();
      localButtonGroup.add(this._mBillCompany);
      localButtonGroup.add(this._mBillWh);
      localButtonGroup.add(this._mBillStore);
      localCellConstraints.xywh(8, 2, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
      localJPanel.add(new JLabel("Ship To : "), localCellConstraints);
      localCellConstraints.xywh(10, 2, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
      this._mShipCompany = new JRadioButton("Company");
      localJPanel.add(this._mShipCompany, localCellConstraints);
      this._mShipCompany.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          if (PurchaseOrderUIPanel.POAddressWindow.this._mShipCompany.isSelected())
          {
            PurchaseOrderUIPanel.POAddressWindow.this._mShipToCombo.setEnabled(false);
            try
            {
              CompanyInfoRow localCompanyInfoRow = CompanyInfoTableDef.getInstance().getCompany();
              PurchaseOrderUIPanel.POAddressWindow.this._mShipToAddress.setText(localCompanyInfoRow.getAddress() + "\nPhone : " + localCompanyInfoRow.getPhone1());
            }
            catch (DBException localDBException)
            {
              localDBException.printStackTrace();
            }
          }
        }
      });
      localCellConstraints.xywh(11, 2, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
      this._mShipWh = new JRadioButton("Warehouse");
      localJPanel.add(this._mShipWh, localCellConstraints);
      this._mShipWh.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          PurchaseOrderUIPanel.POAddressWindow.this.shipWhClicked();
        }
      });
      localCellConstraints.xywh(12, 2, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
      this._mShipStore = new JRadioButton("Store");
      localJPanel.add(this._mShipStore, localCellConstraints);
      this._mShipStore.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          PurchaseOrderUIPanel.POAddressWindow.this.shipStoreClicked();
        }
      });
      localButtonGroup = new ButtonGroup();
      localButtonGroup.add(this._mShipCompany);
      localButtonGroup.add(this._mShipWh);
      localButtonGroup.add(this._mShipStore);
      localCellConstraints.xywh(4, 4, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
      this._mBillToCombo = new JComboBox();
      localJPanel.add(this._mBillToCombo, localCellConstraints);
      localCellConstraints.xywh(10, 4, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
      this._mShipToCombo = new JComboBox();
      localJPanel.add(this._mShipToCombo, localCellConstraints);
      localCellConstraints.xywh(4, 6, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
      this._mBillToAddress = new JTextArea();
      this._mBillToAddress.setBackground(UICommon.MANDATORY_COLOR);
      localJPanel.add(new JScrollPane(this._mBillToAddress), localCellConstraints);
      localCellConstraints.xywh(10, 6, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
      this._mShipToAddress = new JTextArea();
      localJPanel.add(new JScrollPane(this._mShipToAddress), localCellConstraints);
      this._mShipToAddress.setBackground(UICommon.MANDATORY_COLOR);
      localCellConstraints.xywh(1, 7, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.CENTER);
      localJPanel.add(new JSeparator(), localCellConstraints);
      localCellConstraints.xywh(1, 8, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.FILL);
      localJPanel.add(getButtonPanel(), localCellConstraints);
    }
    
    private JPanel getButtonPanel()
    {
      JPanel localJPanel = new JPanel();
      localJPanel.setLayout(new FormLayout("10px,100px,pref:grow,100px,pref:grow,100px,10px", "30px"));
      CellConstraints localCellConstraints = new CellConstraints();
      Object localObject = new JXButton("Select");
      localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
      localJPanel.add((Component)localObject, localCellConstraints);
      this._mOkButton = ((JButton)localObject);
      ((JXButton)localObject).addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          PurchaseOrderUIPanel.POAddressWindow.this.selectClicked();
        }
      });
      localObject = new JXButton("Close");
      localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
      localJPanel.add((Component)localObject, localCellConstraints);
      ((JXButton)localObject).addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          PurchaseOrderUIPanel.POAddressWindow.this._mIsCancelled = true;
          PurchaseOrderUIPanel.POAddressWindow.this.setVisible(false);
        }
      });
      localObject = new HelpButton("ISP_SHIP_BILLADDRESS");
      ((JXButton)localObject).setMnemonic(72);
      localCellConstraints.xywh(6, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
      localJPanel.add((Component)localObject, localCellConstraints);
      return localJPanel;
    }
    
    public boolean isCancelled()
    {
      return this._mIsCancelled;
    }
    
    private void billWhClicked()
    {
      try
      {
        ArrayList localArrayList = WearehouseInfoTableDef.getInstance().getWarehouseList();
        this._mBillToCombo.removeAllItems();
        this._mBillToCombo.setEnabled(true);
        if ((localArrayList == null) || (localArrayList.size() == 0))
        {
          this._mBillToAddress.setText("");
          return;
        }
        Iterator localIterator = localArrayList.iterator();
        while (localIterator.hasNext())
        {
          WearehouseInfoRow localWearehouseInfoRow = (WearehouseInfoRow)localIterator.next();
          this._mBillToCombo.addItem(localWearehouseInfoRow);
        }
        this._mBillToCombo.setSelectedIndex(0);
        this._mBillToAddress.setText(((WearehouseInfoRow)localArrayList.get(0)).getWearehouseAddress());
      }
      catch (DBException localDBException)
      {
        localDBException.printStackTrace();
      }
    }
    
    private void shipWhClicked()
    {
      try
      {
        ArrayList localArrayList = WearehouseInfoTableDef.getInstance().getWarehouseList();
        this._mShipToCombo.removeAllItems();
        this._mShipToCombo.setEnabled(true);
        if ((localArrayList == null) || (localArrayList.size() == 0))
        {
          this._mShipToAddress.setText("");
          return;
        }
        Iterator localIterator = localArrayList.iterator();
        while (localIterator.hasNext())
        {
          WearehouseInfoRow localWearehouseInfoRow = (WearehouseInfoRow)localIterator.next();
          this._mShipToCombo.addItem(localWearehouseInfoRow);
        }
        this._mShipToCombo.setSelectedIndex(0);
        this._mShipToAddress.setText(((WearehouseInfoRow)localArrayList.get(0)).getWearehouseAddress());
      }
      catch (DBException localDBException)
      {
        localDBException.printStackTrace();
      }
    }
    
    private void billStoreClicked()
    {
      try
      {
        ArrayList localArrayList = StoreInfoTableDef.getInstance().getStoreList();
        this._mBillToCombo.setEnabled(true);
        this._mBillToCombo.removeAllItems();
        if ((localArrayList == null) || (localArrayList.size() == 0))
        {
          this._mBillToAddress.setText("");
          return;
        }
        Iterator localIterator = localArrayList.iterator();
        while (localIterator.hasNext())
        {
          StoreInfoRow localStoreInfoRow = (StoreInfoRow)localIterator.next();
          this._mBillToCombo.addItem(localStoreInfoRow);
        }
        this._mBillToCombo.setSelectedIndex(0);
        this._mBillToAddress.setText(((StoreInfoRow)localArrayList.get(0)).getAddress());
      }
      catch (DBException localDBException)
      {
        localDBException.printStackTrace();
      }
    }
    
    private void shipStoreClicked()
    {
      try
      {
        ArrayList localArrayList = StoreInfoTableDef.getInstance().getStoreList();
        this._mShipToCombo.setEnabled(true);
        this._mShipToCombo.removeAllItems();
        if ((localArrayList == null) || (localArrayList.size() == 0))
        {
          this._mShipToAddress.setText("");
          return;
        }
        Iterator localIterator = localArrayList.iterator();
        while (localIterator.hasNext())
        {
          StoreInfoRow localStoreInfoRow = (StoreInfoRow)localIterator.next();
          this._mShipToCombo.addItem(localStoreInfoRow);
        }
        this._mShipToCombo.setSelectedIndex(0);
        this._mShipToAddress.setText(((StoreInfoRow)localArrayList.get(0)).getAddress());
      }
      catch (DBException localDBException)
      {
        localDBException.printStackTrace();
      }
    }
    
    private void selectClicked()
    {
      String str1 = this._mBillToAddress.getText();
      if (str1.length() == 0)
      {
        UICommon.showError("Bill To address cannot be empty.", "Error", MainWindow.instance);
        this._mBillToAddress.requestFocusInWindow();
        return;
      }
      String str2 = this._mShipToAddress.getText();
      if (str2.length() == 0)
      {
        UICommon.showError("Ship To address cannot be empty.", "Error", MainWindow.instance);
        this._mShipToAddress.requestFocusInWindow();
        return;
      }
      this._mIsCancelled = false;
      PurchaseOrderUIPanel.this._mBillAddress = str1;
      PurchaseOrderUIPanel.this._mShipAddress = str2;
      setVisible(false);
    }
    
    String getBillToAddress()
    {
      return PurchaseOrderUIPanel.this._mBillAddress;
    }
    
    String getShipToAddress()
    {
      return PurchaseOrderUIPanel.this._mShipAddress;
    }
    
    void selectNone()
    {
      this._mShipCompany.setSelected(false);
      this._mShipWh.setSelected(false);
      this._mShipStore.setSelected(false);
      this._mBillCompany.setSelected(false);
      this._mBillWh.setSelected(false);
      this._mBillStore.setSelected(false);
    }
    
    void setBillToAddress(String paramString)
    {
      this._mBillToAddress.setText(paramString);
    }
    
    void setShipToAddress(String paramString)
    {
      this._mShipToAddress.setText(paramString);
    }
  }
  
  class TestRenderer
    extends DefaultTableCellRenderer
  {
    TestRenderer() {}
    
    public Component getTableCellRendererComponent(JTable paramJTable, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2)
    {
      super.getTableCellRendererComponent(paramJTable, paramObject, paramBoolean1, paramBoolean2, paramInt1, paramInt2);
      if ((paramInt2 == 1) || (paramInt2 == 2)) {
        setHorizontalAlignment(2);
      } else {
        setHorizontalAlignment(4);
      }
      return this;
    }
  }
  
  private class POTableModel
    extends DefaultTableModel
  {
    private ArrayList<PurchaseOrderUIPanel.POTableRow> _mPORows = new ArrayList();
    private String[] colNames = { "Sl No.", "Product Code", "Product Name", "Current Stock", "Order Stock", "Offer Price (" + CommonConfig.getInstance().country.currency + " )" };
    
    private POTableModel() {}
    
    public int getColumnCount()
    {
      return this.colNames.length;
    }
    
    public String getColumnName(int paramInt)
    {
      return this.colNames[paramInt];
    }
    
    public void updateRow(PurchaseOrderUIPanel.POTableRow paramPOTableRow)
    {
      int i = 0;
      Iterator localIterator = this._mPORows.iterator();
      while (localIterator.hasNext())
      {
        PurchaseOrderUIPanel.POTableRow localPOTableRow = (PurchaseOrderUIPanel.POTableRow)localIterator.next();
        if (localPOTableRow.product.getProdIndex() == paramPOTableRow.product.getProdIndex())
        {
          localPOTableRow.quantity = paramPOTableRow.quantity;
          InternalQuantity localInternalQuantity = new InternalQuantity(localPOTableRow.quantity, localPOTableRow.product.getProdUnit(), true);
          setValueAt(localInternalQuantity, i, 4);
          return;
        }
        i++;
      }
    }
    
    public void addPoRow(PurchaseOrderUIPanel.POTableRow paramPOTableRow)
    {
      int i = 0;
      if (paramPOTableRow.product != null)
      {
        localObject1 = this._mPORows.iterator();
        while (((Iterator)localObject1).hasNext())
        {
          localObject2 = (PurchaseOrderUIPanel.POTableRow)((Iterator)localObject1).next();
          if (((PurchaseOrderUIPanel.POTableRow)localObject2).product.getProdIndex() == paramPOTableRow.product.getProdIndex())
          {
            localObject2.quantity += paramPOTableRow.quantity;
            localObject3 = new InternalQuantity(((PurchaseOrderUIPanel.POTableRow)localObject2).quantity, ((PurchaseOrderUIPanel.POTableRow)localObject2).product.getProdUnit(), true);
            setValueAt(localObject3, i, 4);
            return;
          }
          i++;
        }
      }
      Object localObject1 = null;
      Object localObject2 = null;
      if (paramPOTableRow.product == null)
      {
        localObject1 = new InternalQuantity(paramPOTableRow.quantity, 0, true);
        localObject2 = "-";
      }
      else
      {
        localObject1 = new InternalQuantity(paramPOTableRow.quantity, paramPOTableRow.product.getProdUnit(), true);
        localObject2 = new InternalQuantity(paramPOTableRow.product.getStock(), paramPOTableRow.product.getProdUnit(), true);
      }
      Object localObject3 = new InternalAmount(paramPOTableRow.offerPrice);
      Object[] arrayOfObject = { getRowCount() + 1 + "  ", "  " + (paramPOTableRow.product == null ? "-" : paramPOTableRow.product.getProductCode()), paramPOTableRow, localObject2, localObject1, localObject3 };
      addRow(arrayOfObject);
      this._mPORows.add(paramPOTableRow);
    }
    
    public PurchaseOrderUIPanel.POTableRow getPoRow(int paramInt)
    {
      return (PurchaseOrderUIPanel.POTableRow)this._mPORows.get(paramInt);
    }
    
    public void removeAllRows()
    {
      int i = this._mPORows.size();
      for (int j = 0; j < i; j++) {
        super.removeRow(0);
      }
      this._mPORows.clear();
    }
    
    public void removeRow(int paramInt)
    {
      this._mPORows.remove(paramInt);
      super.removeRow(paramInt);
      int i = this._mPORows.size();
      for (int j = 0; j < i; j++) {
        setValueAt(j + 1 + " ", j, 0);
      }
    }
  }
  
  private class POTableRow
  {
    StockAndProductRow product;
    double quantity;
    double offerPrice;
    
    public POTableRow(StockAndProductRow paramStockAndProductRow, double paramDouble1, double paramDouble2)
    {
      this.product = paramStockAndProductRow;
      this.quantity = paramDouble1;
      this.offerPrice = paramDouble2;
    }
    
    public String toString()
    {
      if (this.product == null) {
        return "-";
      }
      return this.product.getProdName();
    }
    
    public PoEntryRow getPoEntryRow(int paramInt)
    {
      PoEntryTableDef localPoEntryTableDef = PoEntryTableDef.getInstance();
      PoEntryRow localPoEntryRow = localPoEntryTableDef.getNewRow();
      localPoEntryRow.setProduct(this.product);
      localPoEntryRow.setValues(-1, paramInt, this.product.getProdIndex(), this.quantity, this.offerPrice, -1.0D, 0.0D);
      return localPoEntryRow;
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.inv.PurchaseOrderUIPanel
 * JD-Core Version:    0.7.0.1
 */