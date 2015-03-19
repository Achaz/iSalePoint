package dm.jb.ui.settings;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.JeException;
import dm.jb.db.DBAccess;
import dm.jb.db.objects.SiteInfoRow;
import dm.jb.db.objects.SiteInfoTableDef;
import dm.jb.db.objects.StoreInfoRow;
import dm.jb.db.objects.StoreInfoTableDef;
import dm.jb.printing.BillPrintCommon;
import dm.jb.printing.PrintColumn;
import dm.jb.ui.MainWindow;
import dm.jb.ui.billing.BillingColumn;
import dm.jb.ui.billing.BillingUI;
import dm.jb.ui.common.HelpButton;
import dm.jb.ui.common.ShortKeyCommon;
import dm.jb.ui.res.MessageResourceUtils;
import dm.jb.ui.res.ResourceUtils;
import dm.tools.db.DBException;
import dm.tools.types.InternalAmount;
import dm.tools.ui.AbstractMainPanel;
import dm.tools.ui.MainPanelIf;
import dm.tools.ui.ShuttlePane;
import dm.tools.ui.ShuttlePaneActionListener;
import dm.tools.ui.UICommon;
import dm.tools.ui.components.JBDBUIAmountTextField;
import dm.tools.ui.components.JBIntegerTextField;
import dm.tools.utils.CommonConfig;
import dm.tools.utils.Config;
import dm.tools.utils.MyFormatter;
import dm.tools.utils.Validation;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import org.jdesktop.swingx.JXButton;

public class ConfigurationPanel
  extends AbstractMainPanel
{
  private ShuttlePane<BillingColumn> _mColumnViewShuttle = null;
  private JTextField _mBillViewWidth = null;
  private JLabel _mBillViewWidthLabel = null;
  private JButton _mBillViewDataSetBtn = null;
  private JPanel _mBillViewSizeEditPanel = null;
  private JComboBox _mCountry = null;
  private JComboBox _mDateFormat = null;
  private JTextField _mCurrency = null;
  private JTextField _mFinalTaxAmount = null;
  private JCheckBox _mFinalTax = null;
  private JComboBox _mCustomerOption = null;
  private JCheckBox _mEnableRFID = null;
  private JComboBox _mLoyaltyRedeemMode = null;
  private JComboBox _mLoyaltyMode = null;
  private JComboBox _mRoundingOption = null;
  private JTextField _mLoyaltyRedeemAmount = null;
  private JTextField _mLoyaltyAmount = null;
  private JLabel _mLoyaltyRedeemAmountLbl = null;
  private JLabel _mLoyaltyAmountLbl = null;
  private JLabel _mRedeemAmountUnitLbl = null;
  private JLabel _mLoyaltyAmountUnitLbl = null;
  private JTextField _mTempFolder = null;
  private JTextField _mRFIDPort = null;
  private JLabel _mRFIDLbl = null;
  private JTextField _mDbUser = null;
  private JTextField _mDbHost = null;
  private JTextField _mDbPort = null;
  private JPasswordField _mDbPassword = null;
  private static ConfigurationPanel _mConfigPanel = null;
  
  public static MainPanelIf getConfigPanel()
  {
    if (_mConfigPanel == null) {
      _mConfigPanel = new ConfigurationPanel();
    } else {
      _mConfigPanel.clearAllFields();
    }
    return _mConfigPanel;
  }
  
  public ConfigurationPanel()
  {
    initUI();
  }
  
  public void windowDisplayed() {}
  
  private void initUI()
  {
    FormLayout localFormLayout1 = new FormLayout("10px, 650px:grow, 10px", "10px, pref:grow, 20px, 30px, 10px");
    setLayout(localFormLayout1);
    CellConstraints localCellConstraints = new CellConstraints();
    JTabbedPane localJTabbedPane = new JTabbedPane();
    localCellConstraints.xywh(2, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJTabbedPane, localCellConstraints);
    localJTabbedPane.addTab(ResourceUtils.getString("CONFIG_GENERAL_TAB"), getGeneralPanel());
    localJTabbedPane.addTab(ResourceUtils.getString("CONFIG_GENERAL_TRANSACTION"), getTransactionPanel());
    JPanel localJPanel1 = getBillViewPanel();
    if (localJPanel1 == null) {
      return;
    }
    StoreInfoRow localStoreInfoRow = StoreInfoTableDef.getCurrentStore();
    SiteInfoRow localSiteInfoRow = SiteInfoTableDef.getCurrentSite();
    if ((localSiteInfoRow != null) && (localStoreInfoRow != null)) {
      localJTabbedPane.addTab(ResourceUtils.getString("CONFIG_GENERAL_BILLING_VIEW"), localJPanel1);
    }
    JSeparator localJSeparator = new JSeparator();
    localCellConstraints.xywh(1, 3, 3, 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(localJSeparator, localCellConstraints);
    JPanel localJPanel2 = new JPanel();
    FormLayout localFormLayout2 = new FormLayout("10px, 100px, pref:grow, 100px, pref:grow, 100px, 10px", "30px");
    localJPanel2.setLayout(localFormLayout2);
    localCellConstraints.xywh(1, 4, 3, 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(localJPanel2, localCellConstraints);
    Object localObject = new JButton(ResourceUtils.getString("UPDATE_BTN"));
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ConfigurationPanel.this.applyClicked();
      }
    });
    localJPanel2.add((Component)localObject, localCellConstraints);
    ((JButton)localObject).setMnemonic(85);
    localObject = new JButton(ResourceUtils.getString("CLOSE_BTN"));
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel2.add((Component)localObject, localCellConstraints);
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ConfigurationPanel.this.closeWindow();
      }
    });
    localObject = new HelpButton("ISP_CONF");
    localCellConstraints.xywh(6, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel2.add((Component)localObject, localCellConstraints);
    ((JButton)localObject).setMnemonic(72);
  }
  
  private JPanel getBillViewPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("10px, 1px, 10px, 450px, 10px, 90px, 10px, pref:grow, 10px", "10px, 23px, 2px, 200px, 10px");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    JLabel localJLabel = new JLabel(ResourceUtils.getString("CONFIG_BV_TAB_AVAILABLE"));
    localCellConstraints.xywh(4, 2, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    ArrayList localArrayList1 = null;
    try
    {
      localArrayList1 = BillingUI.getViewColumns();
    }
    catch (JeException localJeException)
    {
      UICommon.showError(MessageResourceUtils.getString("CONFIG_PANEL_BILLING_CLUMN_LOAD_ERROR"), MessageResourceUtils.getString("INTERNAL_ERROR"), MainWindow.instance);
      return null;
    }
    ArrayList localArrayList2 = getBillViewColumnFromArray(localArrayList1);
    this._mColumnViewShuttle = new ShuttlePane(true);
    this._mColumnViewShuttle.setFromList(localArrayList2);
    this._mColumnViewShuttle.setToList(localArrayList1);
    localCellConstraints.xywh(4, 4, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mColumnViewShuttle, localCellConstraints);
    this._mColumnViewShuttle.addSelectionListener(new ShuttlePaneActionListener()
    {
      public void objectSelected(Object[] paramAnonymousArrayOfObject)
      {
        ConfigurationPanel.this.billViewObjectSelected(paramAnonymousArrayOfObject);
      }
      
      public boolean addToFromList(Object paramAnonymousObject)
      {
        return true;
      }
      
      public boolean addToToList(Object paramAnonymousObject)
      {
        return true;
      }
    });
    this._mBillViewSizeEditPanel = new JPanel();
    localFormLayout = new FormLayout("60px,10px, 60px, 10px", "10px, 23px, 10px, 30px, 10px");
    this._mBillViewSizeEditPanel.setLayout(localFormLayout);
    localCellConstraints.xywh(6, 4, 3, 1, CellConstraints.FILL, CellConstraints.TOP);
    localJPanel.add(this._mBillViewSizeEditPanel, localCellConstraints);
    localJLabel = new JLabel(ResourceUtils.getString("CONFIG_BV_TAB_WIDTH"));
    localCellConstraints.xywh(1, 2, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    this._mBillViewSizeEditPanel.add(localJLabel, localCellConstraints);
    this._mBillViewWidthLabel = localJLabel;
    this._mBillViewWidth = new JTextField();
    localCellConstraints.xywh(3, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    this._mBillViewSizeEditPanel.add(this._mBillViewWidth, localCellConstraints);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 87, this._mBillViewWidth);
    this._mBillViewWidth.setHorizontalAlignment(4);
    this._mBillViewSizeEditPanel.setBorder(BorderFactory.createEtchedBorder());
    this._mBillViewWidth.setEnabled(false);
    this._mBillViewWidthLabel.setEnabled(false);
    JButton localJButton = new JButton(ResourceUtils.getString("CONFIG_BV_TAB_SET"));
    localJButton.setMnemonic(84);
    localCellConstraints.xywh(1, 4, 4, 1, CellConstraints.CENTER, CellConstraints.FILL);
    this._mBillViewSizeEditPanel.add(localJButton, localCellConstraints);
    localJButton.setEnabled(false);
    this._mBillViewDataSetBtn = localJButton;
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ConfigurationPanel.this.billViewColumnAttribSetClicked();
      }
    });
    return localJPanel;
  }
  
  private void billViewObjectSelected(Object[] paramArrayOfObject)
  {
    if (paramArrayOfObject.length != 1)
    {
      this._mBillViewWidth.setEnabled(false);
      this._mBillViewWidthLabel.setEnabled(false);
      this._mBillViewWidth.setText("");
      this._mBillViewDataSetBtn.setEnabled(false);
    }
    else
    {
      this._mBillViewWidth.setEnabled(true);
      this._mBillViewWidthLabel.setEnabled(true);
      this._mBillViewDataSetBtn.setEnabled(true);
      this._mBillViewWidth.setText(new Integer(((BillingColumn)paramArrayOfObject[0])._mSize).toString());
    }
  }
  
  private ArrayList<BillingColumn> getBillViewColumnFromArray(ArrayList<BillingColumn> paramArrayList)
  {
    ArrayList localArrayList = new ArrayList();
    for (int i = 0; i < BillingUI.BillViewColumnList.length; i++) {
      if (paramArrayList.indexOf(BillingUI.BillViewColumnList[i]) == -1) {
        localArrayList.add(BillingUI.BillViewColumnList[i]);
      }
    }
    return localArrayList;
  }
  
  private ArrayList<PrintColumn> getBillPrintColumnFromArray(ArrayList<PrintColumn> paramArrayList)
  {
    ArrayList localArrayList = new ArrayList();
    for (int i = 0; i < BillPrintCommon.PrintAvailableColumns.length; i++) {
      if (paramArrayList.indexOf(BillPrintCommon.PrintAvailableColumns[i]) == -1) {
        localArrayList.add(BillPrintCommon.PrintAvailableColumns[i]);
      }
    }
    return localArrayList;
  }
  
  private void billViewColumnAttribSetClicked()
  {
    String str = this._mBillViewWidth.getText();
    if (str.length() == 0)
    {
      UICommon.showError("Invalid width specified.", "Error", MainWindow.instance);
      this._mBillViewWidth.requestFocusInWindow();
      return;
    }
    if (!Validation.isValidInt(str, false))
    {
      UICommon.showError("Invalid width specified.", "Error", MainWindow.instance);
      this._mBillViewWidth.requestFocusInWindow();
      return;
    }
    int i = new Integer(str).intValue();
    BillingColumn localBillingColumn = (BillingColumn)this._mColumnViewShuttle.getListSelectedObjects()[0];
    localBillingColumn._mSize = i;
  }
  
  private JPanel getGeneralPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("10px, 90px, 10px, 80px, 90px, 3px,40px,50px,10px,60px,10px", "10px,23px,10px,23px,10px,23px, 10px,23px,10px,23px,10px,23px,10px, 23px, 10px, 23px, 10px,23px, 10px, 23px, 10px,23px,10px");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    JLabel localJLabel = new JLabel("Country :");
    int i = 2;
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mCountry = new JComboBox();
    localCellConstraints.xywh(4, i, 7, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mCountry, localCellConstraints);
    fillCountryCombo();
    this._mCountry.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ConfigurationPanel.this.countryChanged();
      }
    });
    ShortKeyCommon.shortKeyForLabels(localJLabel, 78, this._mCountry);
    i += 2;
    localJLabel = new JLabel("Currency :");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mCurrency = new JTextField();
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mCurrency, localCellConstraints);
    this._mCurrency.setBackground(UICommon.MANDATORY_COLOR);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 89, this._mCurrency);
    i += 2;
    localJLabel = new JLabel("Date format : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mDateFormat = new JComboBox(DateFormat.dateFormats);
    localCellConstraints.xywh(4, i, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mDateFormat, localCellConstraints);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 68, this._mDateFormat);
    i += 2;
    this._mEnableRFID = new JCheckBox("Enable RFID");
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    localJPanel.add(this._mEnableRFID, localCellConstraints);
    this._mEnableRFID.setMnemonic(73);
    this._mEnableRFID.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (ConfigurationPanel.this._mEnableRFID.isSelected())
        {
          ConfigurationPanel.this._mRFIDLbl.setEnabled(true);
          ConfigurationPanel.this._mRFIDPort.setEnabled(true);
          ConfigurationPanel.this._mRFIDPort.requestFocusInWindow();
        }
        else
        {
          ConfigurationPanel.this._mRFIDLbl.setEnabled(false);
          ConfigurationPanel.this._mRFIDPort.setEnabled(false);
        }
      }
    });
    this._mRFIDLbl = new JLabel("RFID Port : ");
    localCellConstraints.xywh(5, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(this._mRFIDLbl, localCellConstraints);
    this._mRFIDPort = new JTextField();
    localCellConstraints.xywh(7, i, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mRFIDPort, localCellConstraints);
    i += 2;
    localJLabel = new JLabel("Temp. Folder : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mTempFolder = new JTextField();
    localCellConstraints.xywh(4, i, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mTempFolder, localCellConstraints);
    ImageIcon localImageIcon = new ImageIcon(getClass().getResource("/dm/jb/images/file_folder.gif"));
    JXButton localJXButton = new JXButton(localImageIcon);
    localCellConstraints.xywh(7, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JFileChooser localJFileChooser = new JFileChooser();
        localJFileChooser.setFileSelectionMode(1);
        int i = localJFileChooser.showOpenDialog(MainWindow.instance);
        if (i == 0)
        {
          String str = localJFileChooser.getSelectedFile().getAbsolutePath();
          ConfigurationPanel.this._mTempFolder.setText(str);
        }
      }
    });
    i += 2;
    localJLabel = new JLabel("DB User : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mDbUser = new JTextField();
    localCellConstraints.xywh(4, i, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mDbUser, localCellConstraints);
    this._mDbUser.setBackground(UICommon.MANDATORY_COLOR);
    i += 2;
    localJLabel = new JLabel("DB Password : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mDbPassword = new JPasswordField();
    localCellConstraints.xywh(4, i, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mDbPassword, localCellConstraints);
    this._mDbPassword.setBackground(UICommon.MANDATORY_COLOR);
    i += 2;
    localJLabel = new JLabel("DB Host : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mDbHost = new JTextField();
    localCellConstraints.xywh(4, i, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mDbHost, localCellConstraints);
    this._mDbHost.setBackground(UICommon.MANDATORY_COLOR);
    i += 2;
    localJLabel = new JLabel("DB Port : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mDbPort = new JTextField();
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mDbPort, localCellConstraints);
    this._mDbPort.setBackground(UICommon.MANDATORY_COLOR);
    this._mCountry.setSelectedIndex(0);
    setGeneralValues();
    return localJPanel;
  }
  
  private JPanel getTransactionPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("10px, 120px, 10px, 160px,3px,100px,50px,30px,5px,150px", "10px,23px,10px,23px,10px,23px,10px,23px,10px,23px,10px,23px");
    int i = 2;
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    JLabel localJLabel = new JLabel("Rounding :");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mRoundingOption = new JComboBox(MyFormatter.roundingOptions);
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mRoundingOption, localCellConstraints);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 79, this._mRoundingOption);
    i += 2;
    localJLabel = new JLabel("Loyalty Mode : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mLoyaltyMode = new JComboBox(LoyaltyRedemptionMode.redemptionModes);
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mLoyaltyMode, localCellConstraints);
    this._mLoyaltyMode.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        LoyaltyRedemptionMode localLoyaltyRedemptionMode = (LoyaltyRedemptionMode)ConfigurationPanel.this._mLoyaltyMode.getSelectedItem();
        if (localLoyaltyRedemptionMode == null) {
          return;
        }
        if (localLoyaltyRedemptionMode.getCode() == 2)
        {
          ConfigurationPanel.this._mLoyaltyAmount.setEnabled(false);
          ConfigurationPanel.this._mLoyaltyAmountUnitLbl.setEnabled(false);
          ConfigurationPanel.this._mLoyaltyAmountLbl.setEnabled(false);
        }
        else if (localLoyaltyRedemptionMode.getCode() == 0)
        {
          ConfigurationPanel.this._mLoyaltyAmount.setEnabled(true);
          ConfigurationPanel.this._mLoyaltyAmountUnitLbl.setEnabled(true);
          ConfigurationPanel.this._mLoyaltyAmountLbl.setEnabled(true);
        }
        else
        {
          ConfigurationPanel.this._mLoyaltyAmount.setEnabled(false);
          ConfigurationPanel.this._mLoyaltyAmountUnitLbl.setEnabled(false);
          ConfigurationPanel.this._mLoyaltyAmountLbl.setEnabled(false);
        }
      }
    });
    localJLabel = new JLabel("One point per ");
    localCellConstraints.xywh(6, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mLoyaltyAmountUnitLbl = localJLabel;
    this._mLoyaltyAmount = new JBDBUIAmountTextField("Loyalty Amount", null);
    localCellConstraints.xywh(7, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mLoyaltyAmount, localCellConstraints);
    this._mLoyaltyAmount.setHorizontalAlignment(4);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 80, this._mLoyaltyRedeemAmount);
    localJLabel = new JLabel("  " + this._mCurrency.getText());
    localCellConstraints.xywh(8, i, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mLoyaltyAmountLbl = localJLabel;
    i += 2;
    localJLabel = new JLabel("Loyalty Red. Mode : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mLoyaltyRedeemMode = new JComboBox(LoyaltyRedemptionMode.redemptionModes);
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mLoyaltyRedeemMode, localCellConstraints);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 89, this._mLoyaltyRedeemMode);
    this._mLoyaltyRedeemAmount = new JBIntegerTextField();
    localCellConstraints.xywh(7, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mLoyaltyRedeemAmount, localCellConstraints);
    this._mLoyaltyRedeemAmount.setHorizontalAlignment(4);
    localJLabel = new JLabel("One point per ");
    localCellConstraints.xywh(6, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mRedeemAmountUnitLbl = localJLabel;
    localJLabel = new JLabel("  " + this._mCurrency.getText());
    localCellConstraints.xywh(8, i, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mLoyaltyRedeemAmountLbl = localJLabel;
    this._mLoyaltyRedeemMode.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        LoyaltyRedemptionMode localLoyaltyRedemptionMode = (LoyaltyRedemptionMode)ConfigurationPanel.this._mLoyaltyRedeemMode.getSelectedItem();
        if (localLoyaltyRedemptionMode == null) {
          return;
        }
        if (localLoyaltyRedemptionMode.getCode() == 2)
        {
          ConfigurationPanel.this._mLoyaltyRedeemAmount.setEnabled(false);
          ConfigurationPanel.this._mRedeemAmountUnitLbl.setEnabled(false);
          ConfigurationPanel.this._mLoyaltyRedeemAmountLbl.setEnabled(false);
        }
        else if (localLoyaltyRedemptionMode.getCode() == 0)
        {
          ConfigurationPanel.this._mLoyaltyRedeemAmount.setEnabled(true);
          ConfigurationPanel.this._mRedeemAmountUnitLbl.setEnabled(true);
          ConfigurationPanel.this._mLoyaltyRedeemAmountLbl.setEnabled(true);
        }
        else
        {
          ConfigurationPanel.this._mLoyaltyRedeemAmount.setEnabled(false);
          ConfigurationPanel.this._mRedeemAmountUnitLbl.setEnabled(false);
          ConfigurationPanel.this._mLoyaltyRedeemAmountLbl.setEnabled(false);
        }
      }
    });
    i += 2;
    this._mFinalTax = new JCheckBox("Final Tax ");
    this._mFinalTax.setMnemonic(88);
    localCellConstraints.xywh(4, i, 2, 1, CellConstraints.LEFT, CellConstraints.FILL);
    localJPanel.add(this._mFinalTax, localCellConstraints);
    this._mFinalTax.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (ConfigurationPanel.this._mFinalTax.isSelected()) {
          ConfigurationPanel.this._mFinalTaxAmount.setBackground(UICommon.MANDATORY_COLOR);
        } else {
          ConfigurationPanel.this._mFinalTaxAmount.setBackground(Color.WHITE);
        }
      }
    });
    ShortKeyCommon.shortKeyForLabels(localJLabel, 88, this._mFinalTax);
    i += 2;
    localJLabel = new JLabel("Tax : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mFinalTaxAmount = new JTextField();
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mFinalTaxAmount, localCellConstraints);
    this._mFinalTaxAmount.setHorizontalAlignment(4);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 65, this._mFinalTaxAmount);
    localCellConstraints.xywh(5, i, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(new JLabel(" %"), localCellConstraints);
    i += 2;
    localJLabel = new JLabel("Customer info : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    String[] arrayOfString = { "Optional", "Mandatory", "None" };
    this._mCustomerOption = new JComboBox(arrayOfString);
    localCellConstraints.xywh(4, i, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mCustomerOption, localCellConstraints);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 79, this._mCustomerOption);
    setTransactionValues();
    return localJPanel;
  }
  
  private void countryChanged()
  {
    CountryInfo localCountryInfo = (CountryInfo)this._mCountry.getSelectedItem();
    if (localCountryInfo == null)
    {
      clearAllCountryFields();
      return;
    }
    this._mCurrency.setText(localCountryInfo.currency);
  }
  
  private void clearAllCountryFields()
  {
    this._mCurrency.setText("");
  }
  
  public void closeWindow()
  {
    BillingUI.reloadBillingPanel();
    super.closeWindow();
  }
  
  private void fillCountryCombo()
  {
    int i = CountrySettings.CountryInfos.length;
    for (int j = 0; j < i; j++) {
      this._mCountry.addItem(CountrySettings.CountryInfos[j]);
    }
  }
  
  public void setDefaultFocus() {}
  
  public JPanel getPanel()
  {
    return this;
  }
  
  private void applyClicked()
  {
    if (!validateAndSaveGeneralValues()) {
      return;
    }
    if (!validateAndSaveTransactionValues()) {
      return;
    }
    if (!validateAndSaveViewBillParams()) {
      return;
    }
    try
    {
      CommonConfig.getInstance().save();
    }
    catch (DBException localDBException)
    {
      localDBException.printStackTrace();
      UICommon.showError("Internal error saving the data.\nCheck then input values. If the problem persists contact administrator", "Internal error", MainWindow.instance);
      return;
    }
    Config.INSTANCE.printXML();
    UICommon.showMessage("Configuration updated successfully.", "Success", MainWindow.instance);
    BillingUI.reloadBillingPanel();
    Config.INSTANCE.printXML();
  }
  
  private boolean validateAndSaveViewBillParams()
  {
    BillingUI.setViewColumnList(this._mColumnViewShuttle.getSelectedObjects());
    return true;
  }
  
  private boolean validateAndSaveTransactionValues()
  {
    CommonConfig.getInstance().roundingOption = this._mRoundingOption.getSelectedIndex();
    CommonConfig.getInstance().redemptionOption = ((LoyaltyRedemptionMode)this._mLoyaltyRedeemMode.getSelectedItem()).getCode();
    LoyaltyRedemptionMode localLoyaltyRedemptionMode = (LoyaltyRedemptionMode)this._mLoyaltyRedeemMode.getSelectedItem();
    String str;
    if (localLoyaltyRedemptionMode.getCode() == 0)
    {
      str = this._mLoyaltyRedeemAmount.getText().trim();
      if (str == null)
      {
        str = "0.00";
      }
      else if (!Validation.isValidAmount(str))
      {
        this._mLoyaltyRedeemAmount.requestFocusInWindow();
        UICommon.showError("Invalid value for amount per point.", "Error", MainWindow.instance);
        return false;
      }
      str = this._mLoyaltyRedeemAmount.getText().trim();
      if (str == null)
      {
        str = "0.00";
      }
      else if (!Validation.isValidAmount(str))
      {
        this._mLoyaltyRedeemAmount.requestFocusInWindow();
        UICommon.showError("Invalid value redeem amount.", "Error", MainWindow.instance);
        this._mLoyaltyRedeemAmount.requestFocusInWindow();
        return false;
      }
      CommonConfig.getInstance().redemptionAmount = Double.valueOf(this._mLoyaltyRedeemAmount.getText().trim()).doubleValue();
    }
    CommonConfig.getInstance().loyaltyOption = ((LoyaltyRedemptionMode)this._mLoyaltyMode.getSelectedItem()).getCode();
    localLoyaltyRedemptionMode = (LoyaltyRedemptionMode)this._mLoyaltyMode.getSelectedItem();
    if (localLoyaltyRedemptionMode.getCode() == 0)
    {
      str = this._mLoyaltyAmount.getText().trim();
      if (str == null)
      {
        str = "0.00";
      }
      else if (!Validation.isValidAmount(str))
      {
        this._mLoyaltyAmount.requestFocusInWindow();
        UICommon.showError("Invalid value for amount per point.", "Error", MainWindow.instance);
        return false;
      }
      str = this._mLoyaltyAmount.getText().trim();
      if (str == null)
      {
        str = "0.00";
      }
      else if (!Validation.isValidAmount(str))
      {
        this._mLoyaltyAmount.requestFocusInWindow();
        UICommon.showError("Invalid value redeem amount.", "Error", MainWindow.instance);
        return false;
      }
      CommonConfig.getInstance().loyaltyAmount = Double.valueOf(this._mLoyaltyAmount.getText().trim()).doubleValue();
      return true;
    }
    return true;
  }
  
  private boolean validateAndSaveGeneralValues()
  {
    String str1 = this._mCurrency.getText();
    if (str1.length() == 0)
    {
      UICommon.showError("Currency should be specified", "Error", MainWindow.instance);
      this._mCurrency.requestFocusInWindow();
      return false;
    }
    if (str1.length() == 32)
    {
      UICommon.showError("Currency length should not exceed 32 characters.", "Error", MainWindow.instance);
      this._mCurrency.requestFocusInWindow();
      return false;
    }
    String str2 = this._mFinalTaxAmount.getText();
    double d = 0.0D;
    if ((this._mFinalTax.isSelected()) && (str2.length() == 0) && (str1.length() == 0))
    {
      UICommon.showError("Final tax percentage should be specified.", "Error", MainWindow.instance);
      this._mFinalTaxAmount.requestFocusInWindow();
      return false;
    }
    if (str2.length() > 0)
    {
      if (!Validation.isValidFloat(str2, 3, false))
      {
        UICommon.showError("Invalid Final tax percentage is specified.", "Error", MainWindow.instance);
        this._mFinalTaxAmount.requestFocusInWindow();
        return false;
      }
      d = Double.valueOf(str2).doubleValue();
      if (d > 100.0D)
      {
        UICommon.showError("Invalid Final tax percentage is specified.", "Error", MainWindow.instance);
        this._mFinalTaxAmount.requestFocusInWindow();
        return false;
      }
    }
    String str3 = this._mDbUser.getText().trim();
    if (str3.length() == 0)
    {
      UICommon.showError("Db user cannot be empty.", "Error", MainWindow.instance);
      this._mDbUser.requestFocusInWindow();
      return false;
    }
    String str4 = this._mDbUser.getText().trim();
    if (str4.length() == 0)
    {
      UICommon.showError("Db password cannot be empty.", "Error", MainWindow.instance);
      this._mDbPassword.requestFocusInWindow();
      return false;
    }
    String str5 = this._mDbHost.getText().trim();
    if (str5.length() == 0)
    {
      UICommon.showError("Db host cannot be empty.", "Error", MainWindow.instance);
      this._mDbHost.requestFocusInWindow();
      return false;
    }
    String str6 = this._mDbPort.getText().trim();
    if (str6.length() == 0)
    {
      UICommon.showError("Db host cannot be empty.", "Error", MainWindow.instance);
      this._mDbPort.requestFocusInWindow();
      return false;
    }
    if (!Validation.isValidInt(str6, false))
    {
      UICommon.showError("Invalid DB port.", "Error", MainWindow.instance);
      this._mDbPort.requestFocusInWindow();
      return false;
    }
    CommonConfig.getInstance().finalTax = this._mFinalTax.isSelected();
    CommonConfig.getInstance().finalTaxAmount = d;
    CommonConfig.getInstance().customerOption = this._mCustomerOption.getSelectedIndex();
    CommonConfig.getInstance().country.currency = str1;
    CommonConfig.getInstance().dateFormat = ((DateFormat)this._mDateFormat.getSelectedItem()).format;
    CommonConfig.getInstance().country.name = this._mCountry.getSelectedItem().toString();
    CommonConfig.getInstance().enableRFID = this._mEnableRFID.isSelected();
    Config.INSTANCE.setAttrib("JB_CONFIG.OTHERS.TEMP_FOLDER", this._mTempFolder.getText().trim());
    Config.INSTANCE.setAttrib("JB_CONFIG.DB_USER_NAME.VALUE", this._mDbUser.getText().trim());
    Config.INSTANCE.setAttrib("JB_CONFIG.DB_CONFIG.HOST", this._mDbHost.getText().trim());
    Config.INSTANCE.setAttrib("JB_CONFIG.DB_CONFIG.PORT", this._mDbPort.getText().trim());
    DBAccess.setPassword(new String(this._mDbPassword.getPassword()));
    if (CommonConfig.getInstance().enableRFID) {
      Config.INSTANCE.setAttrib("JB_CONFIG.OTHERS.RFID_PORT", this._mRFIDPort.getText().trim());
    }
    return true;
  }
  
  private void setTransactionValues()
  {
    this._mRoundingOption.setSelectedIndex(CommonConfig.getInstance().roundingOption);
    LoyaltyRedemptionMode localLoyaltyRedemptionMode = LoyaltyRedemptionMode.getModeForCode(CommonConfig.getInstance().redemptionOption);
    this._mLoyaltyRedeemMode.setSelectedItem(localLoyaltyRedemptionMode);
    InternalAmount localInternalAmount;
    if (localLoyaltyRedemptionMode.getCode() == 2)
    {
      this._mLoyaltyRedeemAmount.setEnabled(false);
      this._mLoyaltyRedeemAmount.setText("");
    }
    else if (localLoyaltyRedemptionMode.getCode() == 0)
    {
      this._mLoyaltyRedeemAmount.setEnabled(true);
      localInternalAmount = new InternalAmount(CommonConfig.getInstance().redemptionAmount);
      this._mLoyaltyRedeemAmount.setText(localInternalAmount.toString());
    }
    else
    {
      this._mLoyaltyRedeemAmount.setEnabled(false);
      this._mLoyaltyRedeemAmount.setText("");
    }
    localLoyaltyRedemptionMode = LoyaltyRedemptionMode.getModeForCode(CommonConfig.getInstance().loyaltyOption);
    this._mLoyaltyMode.setSelectedItem(localLoyaltyRedemptionMode);
    if (localLoyaltyRedemptionMode.getCode() == 2)
    {
      this._mLoyaltyAmount.setEnabled(false);
      this._mLoyaltyAmount.setText("");
    }
    else if (localLoyaltyRedemptionMode.getCode() == 0)
    {
      this._mLoyaltyAmount.setEnabled(true);
      localInternalAmount = new InternalAmount(CommonConfig.getInstance().loyaltyAmount);
      this._mLoyaltyAmount.setText(localInternalAmount.toString());
    }
    else
    {
      this._mLoyaltyAmount.setEnabled(false);
      this._mLoyaltyAmount.setText("");
    }
    this._mFinalTax.setSelected(CommonConfig.getInstance().finalTax);
    this._mFinalTaxAmount.setText(Double.valueOf(CommonConfig.getInstance().finalTaxAmount).toString());
    if (this._mFinalTax.isSelected()) {
      this._mFinalTaxAmount.setBackground(UICommon.MANDATORY_COLOR);
    } else {
      this._mFinalTaxAmount.setBackground(Color.WHITE);
    }
    this._mCustomerOption.setSelectedIndex(CommonConfig.getInstance().customerOption);
  }
  
  private void setGeneralValues()
  {
    int i = 0;
    int j = CountrySettings.CountryInfos.length;
    for (int k = 0; k < j; k++) {
      if (CountrySettings.CountryInfos[k].name.equalsIgnoreCase(CommonConfig.getInstance().country.name)) {
        i = k;
      }
    }
    this._mCountry.setSelectedIndex(i);
    this._mCurrency.setText(CommonConfig.getInstance().country.currency);
    this._mDateFormat.setSelectedItem(DateFormat.getDateFormat(CommonConfig.getInstance().dateFormat));
    this._mEnableRFID.setSelected(CommonConfig.getInstance().enableRFID);
    this._mRFIDLbl.setEnabled(CommonConfig.getInstance().enableRFID);
    this._mRFIDPort.setEnabled(CommonConfig.getInstance().enableRFID);
    String str1 = Config.INSTANCE.getAttrib("JB_CONFIG.OTHERS.TEMP_FOLDER");
    if ((str1 == null) || (str1.length() == 0))
    {
      str1 = "C:\\Temp";
      Config.INSTANCE.setAttrib("JB_CONFIG.OTHERS.TEMP_FOLDER", str1);
    }
    this._mTempFolder.setText(str1);
    String str2 = Config.INSTANCE.getAttrib("JB_CONFIG.DB_USER_NAME.VALUE");
    String str3 = DBAccess.getPassword();
    String str4 = Config.INSTANCE.getAttrib("JB_CONFIG.DB_CONFIG.HOST");
    String str5 = Config.INSTANCE.getAttrib("JB_CONFIG.DB_CONFIG.PORT");
    this._mDbUser.setText(str2);
    this._mDbHost.setText(str4);
    this._mDbPort.setText(str5);
    this._mDbPassword.setText(str3);
    String str6 = Config.INSTANCE.getAttrib("JB_CONFIG.OTHERS.RFID_PORT");
    if (str6 == null) {
      str6 = "";
    }
    this._mRFIDPort.setText(str6);
  }
  
  private static class DateFormat
  {
    static DateFormat[] dateFormats = { new DateFormat("yyyy-MMM-dd", "2008-Oct-01"), new DateFormat("yyyy-MM-dd", "2008-01-31"), new DateFormat("dd-MMM-yyyy", "01-Oct-2008"), new DateFormat("dd-MMMM-yyyy", "01-October-2008"), new DateFormat("dd-MM-yyyy", "23-11-2008") };
    String format;
    String sample;
    
    DateFormat(String paramString1, String paramString2)
    {
      this.format = paramString1;
      this.sample = paramString2;
    }
    
    public String toString()
    {
      return this.format + " (" + this.sample + ")";
    }
    
    public static DateFormat getDateFormat(String paramString)
    {
      for (DateFormat localDateFormat : dateFormats) {
        if (localDateFormat.format.equals(paramString)) {
          return localDateFormat;
        }
      }
      return null;
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.settings.ConfigurationPanel
 * JD-Core Version:    0.7.0.1
 */