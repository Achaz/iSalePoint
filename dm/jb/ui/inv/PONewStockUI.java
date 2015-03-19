package dm.jb.ui.inv;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.db.objects.CurrentStockRow;
import dm.jb.db.objects.CurrentStockTableDef;
import dm.jb.db.objects.PoEntryRow;
import dm.jb.db.objects.PoEntryTableDef;
import dm.jb.db.objects.PoInfoRow;
import dm.jb.db.objects.PoInfoTableDef;
import dm.jb.db.objects.StockAndProductRow;
import dm.jb.db.objects.StockAndProductTableDef;
import dm.jb.db.objects.StoreInfoRow;
import dm.jb.db.objects.StoreInfoTableDef;
import dm.jb.db.objects.StoreStockRow;
import dm.jb.db.objects.StoreStockTableDef;
import dm.jb.db.objects.UserInfoRow;
import dm.jb.db.objects.UserInfoTableDef;
import dm.jb.db.objects.WearehouseInfoRow;
import dm.jb.db.objects.WearehouseInfoTableDef;
import dm.jb.ui.MainWindow;
import dm.jb.ui.common.HelpButton;
import dm.jb.ui.common.ShortKeyCommon;
import dm.jb.ui.settings.CountryInfo;
import dm.tools.db.DBConnection;
import dm.tools.db.DBException;
import dm.tools.db.Db;
import dm.tools.types.InternalAmount;
import dm.tools.types.InternalQuantity;
import dm.tools.ui.AbstractMainPanel;
import dm.tools.ui.NonEditableJXTable;
import dm.tools.ui.UICommon;
import dm.tools.ui.components.JBSearchButton;
import dm.tools.ui.components.JTextSeparator;
import dm.tools.utils.CommonConfig;
import dm.tools.utils.Validation;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ButtonGroup;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXLabel;

public class PONewStockUI
  extends AbstractMainPanel
{
  public static PONewStockUI INSTANCE = new PONewStockUI();
  private JTextField _mPONumber = null;
  private JTextField _mProductNumber = null;
  private JTextField _mProductName = null;
  private JTextField _mQunatityOrdered = null;
  private JTextField _mPriceExpected = null;
  private JTextField _mQunaitytRecivedTillNow = null;
  private JTextField _mQuantityRecievedNow = null;
  private JTextField _mCostPerUnit = null;
  private NonEditableJXTable _mTable = null;
  private JXButton _mApplyButton = null;
  private JXButton _mSaveButton = null;
  private PONewStockTableModel _mModel = null;
  private JTextField _mCreatedBy = null;
  private JLabel _mOfferPriceLbl = null;
  private JLabel _mCostLbl = null;
  private JLabel _mRecivedNowUnit = null;
  private JLabel _mQtyOrderedUnit = null;
  private JLabel _mRecivedLastUnit = null;
  private JRadioButton _mStockToWearhouse = null;
  private JRadioButton _mStockToStore = null;
  private JComboBox _mWeareHouseCombo = null;
  private JComboBox _mStoreCombo = null;
  private PONewStockRow _mCurrentNewRow = null;
  private PoInfoRow _mCurrentPoRow = null;
  
  private PONewStockUI()
  {
    initUI();
    InputMap localInputMap = getInputMap(2);
    String str = "F7Action";
    localInputMap.put(KeyStroke.getKeyStroke("F7"), str);
    AbstractAction local1 = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PONewStockUI.this._mTable.requestFocusInWindow();
      }
    };
    getActionMap().put(str, local1);
  }
  
  public void setDefaultFocus()
  {
    this._mPONumber.requestFocusInWindow();
  }
  
  public JPanel getPanel()
  {
    return this;
  }
  
  public void windowDisplayed()
  {
    this._mApplyButton.setEnabled(false);
    this._mWeareHouseCombo.setEnabled(false);
    this._mStoreCombo.setEnabled(true);
    this._mStockToStore.setSelected(true);
    fillStoreAndWearhouseList();
    this._mSaveButton.setEnabled(false);
    this._mPONumber.setText("");
    poRowSelected(null);
    this._mModel.removeAllRows();
    this._mPONumber.requestFocusInWindow();
    this._mStoreCombo.setSelectedItem(StoreInfoTableDef.getCurrentStore());
    getRootPane().setDefaultButton(this._mSaveButton);
    this._mCreatedBy.setText("");
  }
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("10px,100px,10px,100px,3px,40px,30px,90px,10px,150px,pref:grow,10px", "10px,25px,30px,pref,10px,250px:grow,20px,30px,10px");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    JXLabel localJXLabel = new JXLabel("P.O. Number : ");
    localCellConstraints.xywh(2, 2, 1, 1, CellConstraints.RIGHT, CellConstraints.CENTER);
    add(localJXLabel, localCellConstraints);
    this._mPONumber = new JTextField();
    this._mPONumber.setToolTipText("Purchase Order number");
    this._mPONumber.setName("PO_NUMBER");
    ShortKeyCommon.shortKeyForLabels(localJXLabel, 79, this._mPONumber);
    localCellConstraints.xywh(4, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mPONumber, localCellConstraints);
    JBSearchButton localJBSearchButton = new JBSearchButton(false);
    localCellConstraints.xywh(6, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJBSearchButton, localCellConstraints);
    localJBSearchButton.setName("PO_NUMBER_BTN");
    this._mPONumber.setBackground(UICommon.MANDATORY_COLOR);
    localJBSearchButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PONewStockUI.this.purchaseOrderSearchClicked();
      }
    });
    localJBSearchButton.setToolTipText("Search purchase order");
    this._mPONumber.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 114) {
          PONewStockUI.this.purchaseOrderSearchClicked();
        }
      }
    });
    localCellConstraints.xywh(8, 2, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(new JLabel("Created By : "), localCellConstraints);
    this._mCreatedBy = new JTextField();
    this._mCreatedBy.setToolTipText("User created the PO");
    localCellConstraints.xywh(10, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mCreatedBy, localCellConstraints);
    this._mCreatedBy.setEditable(false);
    localCellConstraints.xywh(2, 3, 10, 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(new JTextSeparator("Product Details"), localCellConstraints);
    localCellConstraints.xywh(2, 4, 10, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getProductInfoPanel(), localCellConstraints);
    this._mModel = new PONewStockTableModel(null);
    this._mTable = new NonEditableJXTable(this._mModel);
    JScrollPane localJScrollPane = new JScrollPane(this._mTable);
    this._mTable.setSelectionMode(0);
    localCellConstraints.xywh(2, 6, 10, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJScrollPane, localCellConstraints);
    this._mTable.setDefaultRenderer(Object.class, new TestRenderer());
    this._mTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent paramAnonymousListSelectionEvent)
      {
        if (paramAnonymousListSelectionEvent.getValueIsAdjusting()) {
          return;
        }
        int i = PONewStockUI.this._mTable.getSelectedRow();
        if (i != -1)
        {
          i = PONewStockUI.this._mTable.convertRowIndexToModel(i);
          PONewStockUI.PONewStockRow localPONewStockRow = (PONewStockUI.PONewStockRow)PONewStockUI.this._mModel.getValueAt(i, 1);
          PONewStockUI.this._mQuantityRecievedNow.requestFocusInWindow();
          PONewStockUI.this.poRowSelected(localPONewStockRow);
        }
        else
        {
          PONewStockUI.this.poRowSelected(null);
        }
        if (PONewStockUI.this._mTable.getSelectedRowCount() == 0) {
          PONewStockUI.this.poRowSelected(null);
        }
      }
    });
    localCellConstraints.xywh(1, 7, 12, 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(new JSeparator(), localCellConstraints);
    localCellConstraints.xywh(2, 8, 10, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getActionButtonPanel(), localCellConstraints);
  }
  
  private JPanel getProductInfoPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("100px,10px,100px,3px,60px,10px,90px,10px,100px,3px,60px,20px,100px", "25px,10px,25px,10px,25px,10px,25px,10px,25px,10px,pref");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    int i = 1;
    JXLabel localJXLabel = new JXLabel("Product Code : ");
    localCellConstraints.xywh(1, i, 1, 1, CellConstraints.RIGHT, CellConstraints.CENTER);
    localJPanel.add(localJXLabel, localCellConstraints);
    this._mProductNumber = new JTextField();
    this._mProductNumber.setToolTipText("Product Code");
    localCellConstraints.xywh(3, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mProductNumber, localCellConstraints);
    this._mProductNumber.setEditable(false);
    ShortKeyCommon.shortKeyForLabels(localJXLabel, 80, this._mProductNumber);
    i += 2;
    localJXLabel = new JXLabel("Product Name : ");
    localCellConstraints.xywh(1, i, 1, 1, CellConstraints.RIGHT, CellConstraints.CENTER);
    localJPanel.add(localJXLabel, localCellConstraints);
    this._mProductName = new JTextField();
    this._mProductName.setToolTipText("Product name");
    this._mProductName.setEditable(false);
    localCellConstraints.xywh(3, i, 5, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mProductName, localCellConstraints);
    ShortKeyCommon.shortKeyForLabels(localJXLabel, 78, this._mProductName);
    i += 2;
    localJXLabel = new JXLabel("Price Offered : ");
    localCellConstraints.xywh(1, i, 1, 1, CellConstraints.RIGHT, CellConstraints.CENTER);
    localJPanel.add(localJXLabel, localCellConstraints);
    this._mPriceExpected = new JTextField();
    this._mPriceExpected.setToolTipText("Price offered");
    this._mPriceExpected.setEditable(false);
    localCellConstraints.xywh(3, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mPriceExpected, localCellConstraints);
    ShortKeyCommon.shortKeyForLabels(localJXLabel, 69, this._mPriceExpected);
    this._mPriceExpected.setHorizontalAlignment(4);
    this._mOfferPriceLbl = new JXLabel(CommonConfig.getInstance().country.currency);
    localCellConstraints.xywh(5, i, 2, 1, CellConstraints.LEFT, CellConstraints.FILL);
    localJPanel.add(this._mOfferPriceLbl, localCellConstraints);
    this._mOfferPriceLbl.setText("  " + CommonConfig.getInstance().country.currency + " per unit");
    i += 2;
    localJXLabel = new JXLabel("Quantity Ordered : ");
    localCellConstraints.xywh(1, i, 1, 1, CellConstraints.RIGHT, CellConstraints.CENTER);
    localJPanel.add(localJXLabel, localCellConstraints);
    this._mQunatityOrdered = new JTextField();
    this._mQunatityOrdered.setToolTipText("Quantity ordered");
    this._mQunatityOrdered.setEditable(false);
    localCellConstraints.xywh(3, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mQunatityOrdered, localCellConstraints);
    ShortKeyCommon.shortKeyForLabels(localJXLabel, 81, this._mQunatityOrdered);
    this._mQunatityOrdered.setHorizontalAlignment(4);
    this._mQtyOrderedUnit = new JXLabel("");
    localCellConstraints.xywh(5, i, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    localJPanel.add(this._mQtyOrderedUnit, localCellConstraints);
    localJXLabel = new JXLabel("Recieved last : ");
    localCellConstraints.xywh(7, i, 1, 1, CellConstraints.RIGHT, CellConstraints.CENTER);
    localJPanel.add(localJXLabel, localCellConstraints);
    this._mQunaitytRecivedTillNow = new JTextField();
    this._mQunaitytRecivedTillNow.setToolTipText("Quantity recieved till now");
    this._mQunaitytRecivedTillNow.setEditable(false);
    localCellConstraints.xywh(9, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mQunaitytRecivedTillNow, localCellConstraints);
    ShortKeyCommon.shortKeyForLabels(localJXLabel, 76, this._mQunaitytRecivedTillNow);
    this._mQunaitytRecivedTillNow.setHorizontalAlignment(4);
    this._mRecivedLastUnit = new JXLabel("");
    localCellConstraints.xywh(11, i, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    localJPanel.add(this._mRecivedLastUnit, localCellConstraints);
    i += 2;
    localJXLabel = new JXLabel("Recieved now : ");
    localCellConstraints.xywh(1, i, 1, 1, CellConstraints.RIGHT, CellConstraints.CENTER);
    localJPanel.add(localJXLabel, localCellConstraints);
    this._mQuantityRecievedNow = new JTextField();
    this._mQuantityRecievedNow.setToolTipText("Quantity recieved now");
    localCellConstraints.xywh(3, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mQuantityRecievedNow, localCellConstraints);
    this._mQuantityRecievedNow.setBackground(UICommon.MANDATORY_COLOR);
    ShortKeyCommon.shortKeyForLabels(localJXLabel, 78, this._mQuantityRecievedNow);
    this._mQuantityRecievedNow.setHorizontalAlignment(4);
    this._mRecivedNowUnit = new JXLabel("");
    localCellConstraints.xywh(5, i, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    localJPanel.add(this._mRecivedNowUnit, localCellConstraints);
    this._mCostLbl = new JXLabel("Cost per unit : ");
    localCellConstraints.xywh(7, i, 1, 1, CellConstraints.RIGHT, CellConstraints.CENTER);
    localJPanel.add(this._mCostLbl, localCellConstraints);
    this._mCostPerUnit = new JTextField();
    this._mCostPerUnit.setToolTipText("Cost per unit");
    localCellConstraints.xywh(9, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mCostPerUnit, localCellConstraints);
    this._mCostPerUnit.setBackground(UICommon.MANDATORY_COLOR);
    ShortKeyCommon.shortKeyForLabels(localJXLabel, 83, this._mCostPerUnit);
    this._mCostPerUnit.setHorizontalAlignment(4);
    localJXLabel = new JXLabel(CommonConfig.getInstance().country.currency);
    localCellConstraints.xywh(11, i, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    localJPanel.add(localJXLabel, localCellConstraints);
    localJXLabel.setText("  " + CommonConfig.getInstance().country.currency);
    JXButton localJXButton = new JXButton("Apply [F4]");
    this._mApplyButton = localJXButton;
    localJXButton.setToolTipText("Apply changes");
    localCellConstraints.xywh(13, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PONewStockUI.this.applyClicked();
      }
    });
    i += 2;
    localJXLabel = new JXLabel("Stock to : ");
    localCellConstraints.xywh(1, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJXLabel, localCellConstraints);
    localCellConstraints.xywh(3, i, 6, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(getStockToPanel(), localCellConstraints);
    return localJPanel;
  }
  
  private JPanel getStockToPanel()
  {
    JPanel localJPanel = new JPanel();
    localJPanel.setLayout(new FormLayout("100px,10px,pref:grow", "25px,10px,25px"));
    CellConstraints localCellConstraints = new CellConstraints();
    this._mStockToWearhouse = new JRadioButton("Warehouse");
    this._mStockToWearhouse.setToolTipText("Send stock to warehouse");
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    localJPanel.add(this._mStockToWearhouse, localCellConstraints);
    this._mStockToWearhouse.setMnemonic(87);
    this._mWeareHouseCombo = new JComboBox();
    localCellConstraints.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mWeareHouseCombo, localCellConstraints);
    this._mWeareHouseCombo.setToolTipText("Select warehouse");
    this._mStockToStore = new JRadioButton("Store");
    this._mStockToStore.setToolTipText("Send stock to store");
    localCellConstraints.xywh(1, 3, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    localJPanel.add(this._mStockToStore, localCellConstraints);
    this._mStockToStore.setMnemonic(84);
    this._mStoreCombo = new JComboBox();
    localCellConstraints.xywh(3, 3, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mStoreCombo, localCellConstraints);
    this._mStoreCombo.setToolTipText("Select store");
    ButtonGroup localButtonGroup = new ButtonGroup();
    localButtonGroup.add(this._mStockToWearhouse);
    localButtonGroup.add(this._mStockToStore);
    this._mStockToStore.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (PONewStockUI.this._mStockToStore.isSelected())
        {
          PONewStockUI.this._mWeareHouseCombo.setEnabled(false);
          PONewStockUI.this._mStoreCombo.setEnabled(true);
        }
        else
        {
          PONewStockUI.this._mWeareHouseCombo.setEnabled(true);
          PONewStockUI.this._mStoreCombo.setEnabled(false);
        }
      }
    });
    this._mStockToWearhouse.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (PONewStockUI.this._mStockToStore.isSelected())
        {
          PONewStockUI.this._mWeareHouseCombo.setEnabled(false);
          PONewStockUI.this._mStoreCombo.setEnabled(true);
        }
        else
        {
          PONewStockUI.this._mWeareHouseCombo.setEnabled(true);
          PONewStockUI.this._mStoreCombo.setEnabled(false);
        }
      }
    });
    return localJPanel;
  }
  
  private JPanel getActionButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("100px,10px,100px,pref:grow,100px", "30px");
    CellConstraints localCellConstraints = new CellConstraints();
    localJPanel.setLayout(localFormLayout);
    Object localObject = new JXButton("Save [F12]");
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JXButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PONewStockUI.this.saveClicked();
      }
    });
    ((JXButton)localObject).setToolTipText("Save the changes to purchase order");
    this._mSaveButton = ((JXButton)localObject);
    localObject = new JXButton("Close");
    localCellConstraints.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JXButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PONewStockUI.this.closeWindow();
      }
    });
    ((JXButton)localObject).setToolTipText("Close the window");
    localObject = new HelpButton("ISP_NEW_PO_STOCK");
    localCellConstraints.xywh(5, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JXButton)localObject).setToolTipText("Help");
    String str = "F1Action";
    final HelpButton localHelpButton = (HelpButton)localObject;
    InputMap localInputMap = getInputMap(2);
    localInputMap.put(KeyStroke.getKeyStroke("F1"), str);
    AbstractAction local10 = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        localHelpButton.actionPerformed(paramAnonymousActionEvent);
      }
    };
    getActionMap().put(str, local10);
    return localJPanel;
  }
  
  public void fillStoreAndWearhouseList()
  {
    try
    {
      ArrayList localArrayList1 = StoreInfoTableDef.getInstance().getAllValues();
      this._mStoreCombo.removeAllItems();
      if ((localArrayList1 != null) && (localArrayList1.size() > 0)) {
        for (int i = 0; i < localArrayList1.size(); i++) {
          this._mStoreCombo.addItem(localArrayList1.get(i));
        }
      }
      ArrayList localArrayList2 = WearehouseInfoTableDef.getInstance().getAllValues();
      this._mWeareHouseCombo.removeAllItems();
      if ((localArrayList2 == null) || (localArrayList2.size() == 0)) {
        return;
      }
      for (int j = 0; j < localArrayList2.size(); j++) {
        this._mWeareHouseCombo.addItem(localArrayList2.get(j));
      }
    }
    catch (DBException localDBException)
    {
      UICommon.showError("Internal Error populating store list. Try again later.\\n\\nIf the problem persists contact administrator.\", ", "Internal Error", MainWindow.instance);
      return;
    }
  }
  
  private void poRowSelected(PONewStockRow paramPONewStockRow)
  {
    this._mCurrentNewRow = paramPONewStockRow;
    if (paramPONewStockRow == null)
    {
      this._mProductNumber.setText("");
      this._mProductName.setText("");
      this._mQunatityOrdered.setText("");
      this._mPriceExpected.setText("");
      this._mQunaitytRecivedTillNow.setText("");
      this._mQuantityRecievedNow.setText("");
      this._mCostPerUnit.setText("");
      this._mApplyButton.setEnabled(false);
      this._mWeareHouseCombo.setEnabled(false);
      this._mStoreCombo.setEnabled(true);
      this._mProductNumber.setText("");
      this._mProductName.setText("");
      this._mOfferPriceLbl.setText("  " + CommonConfig.getInstance().country.currency + " per unit");
      this._mCostLbl.setText(" Cost per unit : ");
      this._mRecivedNowUnit.setText(" units");
      this._mQtyOrderedUnit.setText(" units");
      this._mRecivedLastUnit.setText(" units");
      if (this._mWeareHouseCombo.getItemCount() > 0) {
        this._mWeareHouseCombo.setSelectedIndex(0);
      }
      if (this._mStoreCombo.getItemCount() > 0) {
        this._mStoreCombo.setSelectedIndex(0);
      }
      return;
    }
    double d = paramPONewStockRow.poEntry.getQuantity() - paramPONewStockRow.poEntry.getQuantityRecieved();
    if (paramPONewStockRow.newQty != 0.0D) {
      d = paramPONewStockRow.newQty;
    }
    if (paramPONewStockRow.product == null)
    {
      this._mProductNumber.setText("");
      this._mProductName.setText("");
      this._mQunatityOrdered.setText(new InternalQuantity(paramPONewStockRow.totalQuantity, -1, false).toString());
      this._mQuantityRecievedNow.setEditable(false);
      this._mApplyButton.setEnabled(false);
      this._mCostPerUnit.setEditable(false);
      this._mOfferPriceLbl.setText("  " + CommonConfig.getInstance().country.currency + " per unit");
      this._mCostLbl.setText(" Cost per unit : ");
      this._mRecivedNowUnit.setText(" units");
      this._mQtyOrderedUnit.setText(" units");
      this._mRecivedLastUnit.setText(" units");
      this._mQunaitytRecivedTillNow.setText(new InternalQuantity(paramPONewStockRow.poEntry.getQuantityRecieved(), -1, false).toString());
      this._mQuantityRecievedNow.setText(new InternalQuantity(d, -1, false).toString());
    }
    else
    {
      this._mQuantityRecievedNow.setEditable(true);
      this._mCostPerUnit.setEditable(true);
      this._mApplyButton.setEnabled(true);
      this._mProductNumber.setText(paramPONewStockRow.product.getProductCode());
      this._mProductName.setText(paramPONewStockRow.product.getProdName());
      this._mQunatityOrdered.setText(new InternalQuantity(paramPONewStockRow.totalQuantity, paramPONewStockRow.product.getProdUnit(), false).toString());
      this._mOfferPriceLbl.setText("  " + CommonConfig.getInstance().country.currency + " per " + InternalQuantity.quantityUnits[paramPONewStockRow.product.getProdUnit()]);
      this._mCostLbl.setText(" Cost per " + InternalQuantity.quantityUnits[paramPONewStockRow.product.getProdUnit()] + " : ");
      this._mRecivedNowUnit.setText(" " + InternalQuantity.quantityUnits[paramPONewStockRow.product.getProdUnit()]);
      this._mQtyOrderedUnit.setText(" " + InternalQuantity.quantityUnits[paramPONewStockRow.product.getProdUnit()]);
      this._mRecivedLastUnit.setText(" " + InternalQuantity.quantityUnits[paramPONewStockRow.product.getProdUnit()]);
      this._mQunaitytRecivedTillNow.setText(new InternalQuantity(paramPONewStockRow.poEntry.getQuantityRecieved(), paramPONewStockRow.product.getProdUnit(), false).toString());
      this._mQuantityRecievedNow.setText(new InternalQuantity(d, paramPONewStockRow.product.getProdUnit(), false).toString());
    }
    this._mPriceExpected.setText(InternalAmount.valueOf(paramPONewStockRow.offerPrice).toString());
    this._mCostPerUnit.setText(InternalAmount.valueOf(paramPONewStockRow.newPrice != 0.0D ? paramPONewStockRow.newPrice : paramPONewStockRow.offerPrice).toString());
    if (paramPONewStockRow.storeOrWarehouse != null)
    {
      Object localObject;
      if ((paramPONewStockRow.storeOrWarehouse instanceof StoreInfoRow))
      {
        this._mStockToStore.setSelected(true);
        localObject = (StoreInfoRow)paramPONewStockRow.storeOrWarehouse;
        this._mStoreCombo.setSelectedItem(localObject);
        this._mWeareHouseCombo.setEnabled(false);
        this._mStoreCombo.setEnabled(true);
      }
      else
      {
        this._mStockToWearhouse.setSelected(true);
        localObject = (WearehouseInfoRow)paramPONewStockRow.storeOrWarehouse;
        this._mWeareHouseCombo.setSelectedItem(localObject);
        this._mWeareHouseCombo.setEnabled(true);
        this._mStoreCombo.setEnabled(false);
      }
    }
  }
  
  private void purchaseOrderSearchClicked()
  {
    String str = this._mPONumber.getText().trim();
    if (str.length() == 0)
    {
      UICommon.showError("Purchase Order Number cannot be empty.", "Error", MainWindow.instance);
      this._mPONumber.requestFocusInWindow();
      return;
    }
    if (!Validation.isValidInt(str, false))
    {
      UICommon.showError("Invalid Purchase Order Number.", "Error", MainWindow.instance);
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
      this._mSaveButton.setEnabled(true);
      this._mCurrentPoRow = localPoInfoRow;
      ArrayList localArrayList = localPoInfoRow.getEntries();
      this._mModel.removeAllRows();
      Iterator localIterator = localArrayList.iterator();
      while (localIterator.hasNext())
      {
        localObject = (PoEntryRow)localIterator.next();
        StockAndProductRow localStockAndProductRow = (StockAndProductRow)StockAndProductTableDef.getInstance().findRowByIndex(((PoEntryRow)localObject).getProductId());
        PONewStockRow localPONewStockRow = new PONewStockRow(localStockAndProductRow, (PoEntryRow)localObject, ((PoEntryRow)localObject).getQuantity(), ((PoEntryRow)localObject).getPriceExpected());
        this._mModel.addPoRow(localPONewStockRow);
      }
      int j = this._mCurrentPoRow.getCreatedBy();
      Object localObject = (UserInfoRow)UserInfoTableDef.getInstance().findRowByIndex(j);
      if (localObject == null) {
        this._mCreatedBy.setText("");
      } else {
        this._mCreatedBy.setText(((UserInfoRow)localObject).getUserName());
      }
    }
    catch (DBException localDBException)
    {
      UICommon.showError("Internal Error searching for Purchase Order.\n\nTry again later. If the problem persists contact administrator.", "Internal Error", MainWindow.instance);
      return;
    }
  }
  
  private void applyClicked()
  {
    String str1 = this._mQuantityRecievedNow.getText().trim();
    if (str1.length() == 0)
    {
      UICommon.showError("Quantity Recieved cannot be empty.", "Error", MainWindow.instance);
      this._mQuantityRecievedNow.requestFocusInWindow();
      return;
    }
    if (!Validation.isValidFloat(str1, 5, false))
    {
      UICommon.showError("Invalid value for Quantity Recieved.", "Error", MainWindow.instance);
      this._mQuantityRecievedNow.requestFocusInWindow();
      return;
    }
    if ((str1.contains(".")) && (!InternalQuantity.isUnitFractionAllowed(this._mCurrentNewRow.product.getProdUnit())))
    {
      UICommon.showError("Fraction quantity is not alowed for this product.", "Error", MainWindow.instance);
      this._mQuantityRecievedNow.requestFocusInWindow();
      return;
    }
    double d1 = Double.valueOf(str1).doubleValue();
    double d2 = this._mCurrentNewRow.poEntry.getQuantity() - this._mCurrentNewRow.poEntry.getQuantityRecieved();
    if (d1 > d2)
    {
      String str2 = new InternalQuantity(d2, this._mCurrentNewRow.product.getProdUnit(), true).toString();
      UICommon.showError("Quantity recieved cannot be more that the quantity expected ( " + str2 + " ).", "Error", MainWindow.instance);
      this._mQuantityRecievedNow.requestFocusInWindow();
      return;
    }
    if (this._mStockToWearhouse.isSelected())
    {
      if (this._mWeareHouseCombo.getSelectedItem() == null) {
        UICommon.showError("Select a warehouse to accept this stock.", "Error", MainWindow.instance);
      }
    }
    else if (this._mStoreCombo.getSelectedItem() == null)
    {
      UICommon.showError("Select a store to accept this stock.", "Error", MainWindow.instance);
      return;
    }
    str1 = this._mCostPerUnit.getText().trim();
    if (str1.length() == 0)
    {
      UICommon.showError("Cost per unit cannot be empty.", "Error", MainWindow.instance);
      this._mCostPerUnit.requestFocusInWindow();
      return;
    }
    if (!Validation.isValidAmount(str1))
    {
      UICommon.showError("Invalid value for Cost per unit.", "Error", MainWindow.instance);
      this._mCostPerUnit.requestFocusInWindow();
      return;
    }
    double d3 = Double.valueOf(str1).doubleValue();
    this._mCurrentNewRow.newQty = d1;
    this._mCurrentNewRow.newPrice = d3;
    int i = this._mTable.getSelectedRow();
    i = this._mTable.convertRowIndexToModel(i);
    if (this._mStockToWearhouse.isSelected()) {
      this._mCurrentNewRow.storeOrWarehouse = this._mWeareHouseCombo.getSelectedItem();
    } else {
      this._mCurrentNewRow.storeOrWarehouse = this._mStoreCombo.getSelectedItem();
    }
    this._mModel.setNewUpdates(i, this._mCurrentNewRow);
  }
  
  private void saveClicked()
  {
    PONewStockRow[] arrayOfPONewStockRow = this._mModel.getAllEntriesAsArray();
    DBConnection localDBConnection = Db.getConnection();
    try
    {
      localDBConnection.openTrans();
      for (int i = 0; i < arrayOfPONewStockRow.length; i++)
      {
        PONewStockRow localPONewStockRow = arrayOfPONewStockRow[i];
        if (localPONewStockRow.newQty > 0.0D)
        {
          PoEntryRow localPoEntryRow = localPONewStockRow.poEntry;
          localPoEntryRow.setQuantityRecieved(localPoEntryRow.getQuantityRecieved() + localPONewStockRow.newQty);
          localPoEntryRow.update(true);
          Object localObject;
          int k;
          if ((localPONewStockRow.storeOrWarehouse instanceof StoreInfoRow))
          {
            localObject = (StoreInfoRow)localPONewStockRow.storeOrWarehouse;
            int j = ((StoreInfoRow)localObject).getStoreId();
            k = localPONewStockRow.product.getProdIndex();
            ArrayList localArrayList = StoreStockTableDef.getInstance().getAllValuesWithWhereClauseForUpdate("STORE_ID = " + j + " AND PRODUCT_ID = " + k);
            StoreStockRow localStoreStockRow;
            if ((localArrayList != null) && (localArrayList.size() > 0))
            {
              localStoreStockRow = (StoreStockRow)localArrayList.get(0);
              double d1 = localStoreStockRow.getPurchasePrice();
              localStoreStockRow.setPurchasePrice((localPONewStockRow.newPrice + d1) / 2.0D);
              double d2 = localStoreStockRow.getStock();
              d2 += localPONewStockRow.newQty;
              localStoreStockRow.setStock(d2);
              localStoreStockRow.update(true);
            }
            else
            {
              localStoreStockRow = StoreStockTableDef.getInstance().getNewRow();
              localStoreStockRow.setValues(j, k, localPONewStockRow.newQty, localPONewStockRow.newPrice, null, -1);
              localStoreStockRow.create();
            }
          }
          else
          {
            localObject = CurrentStockTableDef.getInstance().getNewRow();
            WearehouseInfoRow localWearehouseInfoRow = (WearehouseInfoRow)localPONewStockRow.storeOrWarehouse;
            k = localWearehouseInfoRow.getWearehouseId();
            ((CurrentStockRow)localObject).setValues(-1, localPONewStockRow.product.getProdIndex(), null, "Y", localPONewStockRow.newQty, localPONewStockRow.product.getProdUnit(), k);
            ((CurrentStockRow)localObject).create();
          }
        }
      }
      localDBConnection.commit();
    }
    catch (DBException localDBException)
    {
      localDBConnection.rollbackNoExp();
      localDBException.printStackTrace();
      UICommon.showError("Internal Error updating Purchase Order.\n\nTry again later. If the problem persists contact administrator", "Internal Error", MainWindow.instance);
      return;
    }
    UICommon.showMessage("The purchase order and the stock details are updated successfully.", "Success", MainWindow.instance);
    windowDisplayed();
  }
  
  class TestRenderer
    extends DefaultTableCellRenderer
  {
    TestRenderer() {}
    
    public Component getTableCellRendererComponent(JTable paramJTable, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2)
    {
      super.getTableCellRendererComponent(paramJTable, paramObject, paramBoolean1, paramBoolean2, paramInt1, paramInt2);
      if ((paramInt2 == 1) || (paramInt2 == 4)) {
        setHorizontalAlignment(2);
      } else {
        setHorizontalAlignment(4);
      }
      return this;
    }
  }
  
  private class PONewStockTableModel
    extends DefaultTableModel
  {
    String[] columns = { "Sl. No", "Product Name              ", "Quantity Ordered", "Qunaitity Recived Now", "Store/Warehouse     " };
    
    private PONewStockTableModel() {}
    
    public int getColumnCount()
    {
      return this.columns.length;
    }
    
    public String getColumnName(int paramInt)
    {
      return this.columns[paramInt];
    }
    
    public void removeAllRows()
    {
      int i = PONewStockUI.this._mTable.getRowCount();
      for (int j = 0; j < i; j++) {
        super.removeRow(0);
      }
    }
    
    public void addPoRow(PONewStockUI.PONewStockRow paramPONewStockRow)
    {
      int i = -1;
      if (paramPONewStockRow.product != null) {
        i = (short)paramPONewStockRow.product.getProdUnit();
      }
      InternalQuantity localInternalQuantity1 = new InternalQuantity(paramPONewStockRow.totalQuantity, i, true);
      InternalQuantity localInternalQuantity2 = new InternalQuantity(paramPONewStockRow.poEntry.getQuantityRecieved(), i, true);
      Object[] arrayOfObject = { getRowCount() + 1 + "  ", paramPONewStockRow, localInternalQuantity1, localInternalQuantity2.toString(), "" };
      super.addRow(arrayOfObject);
    }
    
    public void setNewUpdates(int paramInt, PONewStockUI.PONewStockRow paramPONewStockRow)
    {
      InternalQuantity localInternalQuantity = new InternalQuantity(paramPONewStockRow.newQty, paramPONewStockRow.product.getProdUnit(), true);
      setValueAt(localInternalQuantity, paramInt, 3);
      if (paramPONewStockRow.storeOrWarehouse != null)
      {
        String str = "";
        Object localObject;
        if ((paramPONewStockRow.storeOrWarehouse instanceof StoreInfoRow))
        {
          localObject = (StoreInfoRow)paramPONewStockRow.storeOrWarehouse;
          str = "Store : " + ((StoreInfoRow)localObject).getName();
        }
        else
        {
          localObject = (WearehouseInfoRow)paramPONewStockRow.storeOrWarehouse;
          str = "Warehouse : " + ((WearehouseInfoRow)localObject).getWearehouseName();
        }
        setValueAt(str, paramInt, 4);
      }
    }
    
    private PONewStockUI.PONewStockRow[] getAllEntriesAsArray()
    {
      PONewStockUI.PONewStockRow[] arrayOfPONewStockRow = new PONewStockUI.PONewStockRow[getRowCount()];
      for (int i = 0; i < arrayOfPONewStockRow.length; i++) {
        arrayOfPONewStockRow[i] = ((PONewStockUI.PONewStockRow)getValueAt(i, 1));
      }
      return arrayOfPONewStockRow;
    }
  }
  
  private class PONewStockRow
  {
    StockAndProductRow product;
    double offerPrice;
    PoEntryRow poEntry;
    double newQty = 0.0D;
    double newPrice = 0.0D;
    double totalQuantity = 0.0D;
    Object storeOrWarehouse = null;
    
    public PONewStockRow(StockAndProductRow paramStockAndProductRow, PoEntryRow paramPoEntryRow, double paramDouble1, double paramDouble2)
    {
      this.product = paramStockAndProductRow;
      this.offerPrice = paramDouble2;
      this.poEntry = paramPoEntryRow;
      this.totalQuantity = paramDouble1;
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
      localPoEntryRow.setValues(-1, paramInt, this.product.getProdIndex(), this.totalQuantity, this.offerPrice, -1.0D, 0.0D);
      return localPoEntryRow;
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.inv.PONewStockUI
 * JD-Core Version:    0.7.0.1
 */