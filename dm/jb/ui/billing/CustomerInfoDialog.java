package dm.jb.ui.billing;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.db.objects.CustomerRow;
import dm.jb.db.objects.CustomerTableDef;
import dm.jb.ext.RFIDReadListener;
import dm.jb.ext.RFIDReader;
import dm.jb.messages.Customer_base;
import dm.jb.messages.GeneralUIMessages_base;
import dm.jb.messages.JbMessageLoader;
import dm.jb.ui.MainWindow;
import dm.jb.ui.common.JBCheckBox;
import dm.jb.ui.common.JBLabel;
import dm.jb.ui.common.JBNumTextFieldWithKeyPad;
import dm.jb.ui.common.JBShadowPanel;
import dm.jb.ui.common.JBTextField;
import dm.jb.ui.common.JBTransparentWindow;
import dm.tools.db.DBConnection;
import dm.tools.db.DBException;
import dm.tools.db.DBRow;
import dm.tools.db.Db;
import dm.tools.messages.ButtonLabels_base;
import dm.tools.ui.UICommon;
import dm.tools.ui.components.JBIntegerTextField;
import dm.tools.ui.components.JBSearchButton;
import dm.tools.utils.CommonConfig;
import dm.tools.utils.Validation;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import org.jdesktop.swingx.JXButton;

public class CustomerInfoDialog
  extends JBTransparentWindow
{
  private static CustomerInfoDialog _mInstance = null;
  private JBTextField _mCustomerName = null;
  private JTextArea _mCustomerAddress = null;
  private JBTextField _mPhone = null;
  private JBLabel _mLoyaltyPoints = null;
  private JBNumTextFieldWithKeyPad _mCustomerId = null;
  private JXButton _mOkButton = null;
  private JBTextField _mBarCode = null;
  private JBTextField _mRFID = null;
  private CustomerRow _mCustomer = null;
  private boolean _mCancelled = true;
  private boolean _mMandatory = false;
  private int _mBillLoyaltyPoints = 0;
  private JBCheckBox _mRedeemLoyalty = null;
  private JBIntegerTextField _mRedeemPoints = null;
  private boolean _mCanRedeem = false;
  private int _mPointsToRedeem = 0;
  private int _mRedeemablePoints = 0;
  private JBLabel _mOldLoyaltyPoints = null;
  private JBLabel _mOldLoyaltyPointsLbl = null;
  private JButton _mIdSearchButton = null;
  private JButton _mNameSearchButton = null;
  private JButton _mPhoneSearchButton = null;
  private JBLabel _mReturnLoyaltyLbl = null;
  private JBIntegerTextField _mReturnLoyalty = null;
  private boolean _mUpdateMode = false;
  private JBShadowPanel _mShadowPanel = null;
  private CustomerRow _mOldCustomer = null;
  private int _mOldLoyaltyRedeemed = 0;
  private int _mOldPointsAwarded = 0;
  
  private CustomerInfoDialog()
  {
    super(BillingLauncher.getInstance());
    initUI();
    pack();
    setLocationRelativeTo(BillingLauncher.getInstance());
    addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 27) {
          CustomerInfoDialog.this.closeClicked();
        }
      }
    });
    InputMap localInputMap = ((JPanel)getContentPane()).getInputMap(2);
    String str = "ESCAction";
    localInputMap.put(KeyStroke.getKeyStroke("ESCAPE"), str);
    AbstractAction local2 = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        CustomerInfoDialog.this.closeClicked();
      }
    };
    ((JPanel)getContentPane()).getActionMap().put(str, local2);
    addWindowListener(new WindowAdapter()
    {
      public void windowOpened(WindowEvent paramAnonymousWindowEvent)
      {
        CustomerInfoDialog.this.getRootPane().setDefaultButton(CustomerInfoDialog.this._mOkButton);
        CustomerInfoDialog.this._mCustomerId.requestFocusInWindow();
      }
    });
    RFIDReader localRFIDReader = RFIDReader._mInstance;
    localRFIDReader.addReadListener(new RFIDReadListener()
    {
      public void dataRead(String paramAnonymousString)
      {
        if (!CustomerInfoDialog.this.isVisible()) {
          return;
        }
        CustomerInfoDialog.this._mRFID.setText(paramAnonymousString);
        CustomerInfoDialog.this.searchByRFID();
      }
    });
  }
  
  public void setRedeemablePoints(int paramInt)
  {
    this._mRedeemablePoints = paramInt;
  }
  
  public static CustomerInfoDialog getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new CustomerInfoDialog();
    }
    return _mInstance;
  }
  
  public void setMandatory(boolean paramBoolean)
  {
    this._mMandatory = paramBoolean;
  }
  
  public void clearAllFields()
  {
    this._mCustomerName.setText("");
    this._mCustomerId.setText("");
    this._mCustomerAddress.setText("");
    this._mPhone.setText("");
    this._mMandatory = false;
    this._mRedeemLoyalty.setSelected(false);
    this._mRedeemPoints.setText("");
    this._mRedeemPoints.setVisible(false);
    this._mOldLoyaltyPointsLbl.setVisible(false);
    this._mOldLoyaltyPoints.setVisible(false);
    setCustomer(null, false);
    this._mReturnLoyaltyLbl.setVisible(false);
    this._mReturnLoyalty.setVisible(false);
    this._mRFID.setText("");
    this._mBarCode.setText("");
    this._mCustomerId.requestFocusInWindow();
  }
  
  public void setPointsAwarded(int paramInt)
  {
    this._mOldPointsAwarded = paramInt;
  }
  
  public void setOldLoyaltyPoints(int paramInt)
  {
    this._mOldLoyaltyRedeemed = paramInt;
    this._mOldLoyaltyPointsLbl.setVisible(true);
    this._mOldLoyaltyPoints.setVisible(true);
    this._mOldLoyaltyPoints.setText("" + paramInt);
    this._mReturnLoyaltyLbl.setVisible(true);
    this._mReturnLoyalty.setVisible(true);
    int i = paramInt - this._mRedeemablePoints;
    if (i > 0)
    {
      this._mReturnLoyalty.setText("" + i);
      this._mRedeemPoints.setText("0");
    }
    else
    {
      this._mReturnLoyalty.setText("0");
      i *= -1;
      this._mRedeemPoints.setText("" + i);
      this._mRedeemablePoints = i;
    }
  }
  
  private void closeClicked()
  {
    this._mCancelled = true;
    setVisible(false);
  }
  
  private void initUI()
  {
    JPanel localJPanel = (JPanel)getContentPane();
    this._mShadowPanel = new JBShadowPanel(new Color(255, 187, 119));
    JBShadowPanel localJBShadowPanel = this._mShadowPanel;
    localJPanel.setLayout(new BorderLayout());
    localJPanel.add(localJBShadowPanel, "Center");
    FormLayout localFormLayout = new FormLayout("30px,pref:grow,30px", "30px,pref:grow,20px,40px,10px,30px");
    localJBShadowPanel.setLayout(localFormLayout);
    localJBShadowPanel.setOpaque(false);
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(2, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJBShadowPanel.add(getDataPanel(), localCellConstraints);
    localCellConstraints.xywh(2, 3, 1, 1, CellConstraints.FILL, CellConstraints.CENTER);
    localJBShadowPanel.add(new JSeparator(), localCellConstraints);
    localCellConstraints.xywh(2, 4, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJBShadowPanel.add(getButtonsPanel(), localCellConstraints);
  }
  
  private JPanel getDataPanel()
  {
    JPanel localJPanel = new JPanel();
    int i = 2;
    FormLayout localFormLayout = new FormLayout("10px,180px, 10px, 150px,10px,100px, 3px, 60px,2px,40px,10px", "10px,30px,10px,30px, 10px,30px,10px, 30px,10px, 150px,10px,30px,10px,30px,10px,30px, 10px,30px");
    localJPanel.setLayout(localFormLayout);
    localJPanel.setOpaque(false);
    CellConstraints localCellConstraints = new CellConstraints();
    JbMessageLoader localJbMessageLoader = (JbMessageLoader)UICommon.getMessageLoader();
    GeneralUIMessages_base localGeneralUIMessages_base = localJbMessageLoader.getGeneralUILabelsMessages();
    JBLabel localJBLabel = new JBLabel(localGeneralUIMessages_base.getMessage(36870) + " : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJBLabel, localCellConstraints);
    this._mCustomerId = new JBNumTextFieldWithKeyPad(this);
    localCellConstraints.xywh(4, i, 5, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mCustomerId, localCellConstraints);
    this._mCustomerId.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if ((paramAnonymousKeyEvent.getKeyCode() == 10) || (paramAnonymousKeyEvent.getKeyCode() == 114)) {
          CustomerInfoDialog.this.searchForCustomerId();
        }
      }
    });
    JBSearchButton localJBSearchButton = new JBSearchButton(true);
    this._mIdSearchButton = localJBSearchButton;
    localCellConstraints.xywh(10, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJBSearchButton, localCellConstraints);
    localJBSearchButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        CustomerInfoDialog.this.searchForCustomerId();
      }
    });
    localJBSearchButton.setBackground(this._mShadowPanel.getBackground());
    i += 2;
    localJBLabel = new JBLabel("Barcode : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJBLabel, localCellConstraints);
    this._mBarCode = new JBTextField();
    localCellConstraints.xywh(4, i, 5, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mBarCode, localCellConstraints);
    this._mBarCode.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if ((paramAnonymousKeyEvent.getKeyCode() == 114) || (paramAnonymousKeyEvent.getKeyCode() == 10))
        {
          CustomerInfoDialog.this.searchByBarCode();
          if (paramAnonymousKeyEvent.getKeyCode() == 10) {
            paramAnonymousKeyEvent.consume();
          }
        }
      }
    });
    localJBSearchButton = new JBSearchButton(true);
    localCellConstraints.xywh(10, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJBSearchButton, localCellConstraints);
    localJBSearchButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        CustomerInfoDialog.this.searchByBarCode();
      }
    });
    localJBSearchButton.setBackground(this._mShadowPanel.getBackground());
    i += 2;
    localJBLabel = new JBLabel("RFID : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJBLabel, localCellConstraints);
    this._mRFID = new JBTextField();
    localCellConstraints.xywh(4, i, 5, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mRFID, localCellConstraints);
    this._mRFID.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 114) {
          CustomerInfoDialog.this.searchByRFID();
        }
      }
    });
    localJBSearchButton = new JBSearchButton(true);
    localCellConstraints.xywh(10, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJBSearchButton, localCellConstraints);
    localJBSearchButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        CustomerInfoDialog.this.searchByRFID();
      }
    });
    localJBSearchButton.setBackground(this._mShadowPanel.getBackground());
    i += 2;
    localJBLabel = new JBLabel(localGeneralUIMessages_base.getMessage(36869) + " : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJBLabel, localCellConstraints);
    this._mCustomerName = new JBTextField();
    localCellConstraints.xywh(4, i, 5, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mCustomerName, localCellConstraints);
    localJBSearchButton = new JBSearchButton(true);
    this._mNameSearchButton = localJBSearchButton;
    localCellConstraints.xywh(10, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJBSearchButton, localCellConstraints);
    localJBSearchButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        CustomerInfoDialog.this.searchByCusomerName();
      }
    });
    localJBSearchButton.setBackground(this._mShadowPanel.getBackground());
    this._mCustomerName.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 114) {
          CustomerInfoDialog.this.searchByCusomerName();
        }
      }
    });
    i += 2;
    localJBLabel = new JBLabel(localGeneralUIMessages_base.getMessage(36868) + " : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.TOP);
    localJPanel.add(localJBLabel, localCellConstraints);
    this._mCustomerAddress = new JTextArea();
    JScrollPane localJScrollPane = new JScrollPane(this._mCustomerAddress);
    localCellConstraints.xywh(4, i, 6, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJScrollPane, localCellConstraints);
    this._mCustomerAddress.setFont(this._mCustomerName.getFont());
    i += 2;
    localJBLabel = new JBLabel(localGeneralUIMessages_base.getMessage(36871) + "  : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJBLabel, localCellConstraints);
    this._mPhone = new JBTextField();
    localCellConstraints.xywh(4, i, 5, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mPhone, localCellConstraints);
    localJBSearchButton = new JBSearchButton(true);
    this._mPhoneSearchButton = localJBSearchButton;
    localCellConstraints.xywh(10, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJBSearchButton, localCellConstraints);
    localJBSearchButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        CustomerInfoDialog.this.searchByCusomerPhone();
      }
    });
    localJBSearchButton.setBackground(this._mShadowPanel.getBackground());
    this._mPhone.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 114) {
          CustomerInfoDialog.this.searchByCusomerPhone();
        }
      }
    });
    i += 2;
    localJBLabel = new JBLabel("Loyalty Points  : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJBLabel, localCellConstraints);
    this._mLoyaltyPoints = new JBLabel("");
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mLoyaltyPoints, localCellConstraints);
    this._mRedeemLoyalty = new JBCheckBox("Redeem");
    this._mRedeemLoyalty.setMnemonic('R');
    localCellConstraints.xywh(6, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mRedeemLoyalty, localCellConstraints);
    this._mRedeemLoyalty.setOpaque(false);
    this._mRedeemLoyalty.setBackground(getBackground());
    this._mRedeemPoints = new JBIntegerTextField(false, true, 2147483647, 0);
    localCellConstraints.xywh(8, i, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mRedeemPoints, localCellConstraints);
    this._mRedeemPoints.setFont(this._mPhone.getFont());
    if (CommonConfig.getInstance().redemptionOption == 2) {
      this._mRedeemLoyalty.setEnabled(false);
    }
    this._mRedeemLoyalty.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (CustomerInfoDialog.this._mRedeemLoyalty.isSelected())
        {
          CustomerInfoDialog.this._mRedeemPoints.setVisible(true);
          CustomerInfoDialog.this._mRedeemPoints.requestFocusInWindow();
          CustomerInfoDialog.this._mRedeemPoints.setText("" + CustomerInfoDialog.this._mRedeemablePoints);
        }
        else
        {
          CustomerInfoDialog.this._mRedeemPoints.setVisible(false);
        }
      }
    });
    i += 2;
    this._mOldLoyaltyPointsLbl = new JBLabel("Redeemed Points : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(this._mOldLoyaltyPointsLbl, localCellConstraints);
    this._mOldLoyaltyPoints = new JBLabel("");
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mOldLoyaltyPoints, localCellConstraints);
    this._mReturnLoyaltyLbl = new JBLabel("Return LP: ");
    localCellConstraints.xywh(6, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mReturnLoyaltyLbl, localCellConstraints);
    this._mReturnLoyalty = new JBIntegerTextField(false, true, 2147483647, 0);
    localCellConstraints.xywh(8, i, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mReturnLoyalty, localCellConstraints);
    this._mReturnLoyalty.setFont(this._mPhone.getFont());
    this._mOldLoyaltyPointsLbl.setVisible(false);
    this._mOldLoyaltyPoints.setVisible(false);
    this._mReturnLoyaltyLbl.setVisible(false);
    this._mReturnLoyalty.setVisible(false);
    return localJPanel;
  }
  
  private JPanel getButtonsPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("10px,100px,30px,100px,20px,100px,pref:grow,100px,10px", "30px");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    localJPanel.setOpaque(false);
    JbMessageLoader localJbMessageLoader = (JbMessageLoader)UICommon.getMessageLoader();
    ButtonLabels_base localButtonLabels_base = localJbMessageLoader.getButtonLabelsMessages();
    JXButton localJXButton = new JXButton(localButtonLabels_base.getMessage(135175));
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    this._mOkButton = localJXButton;
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        CustomerInfoDialog.this.okClicked();
      }
    });
    localJXButton.setBackground(this._mShadowPanel.getBackground());
    localJXButton = new JXButton("Reset");
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.setBackground(this._mShadowPanel.getBackground());
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        CustomerInfoDialog.this.resetClicked();
      }
    });
    localJXButton = new JXButton(localButtonLabels_base.getMessage(135172));
    localCellConstraints.xywh(6, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.setBackground(this._mShadowPanel.getBackground());
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        CustomerInfoDialog.this.closeClicked();
      }
    });
    localJXButton = new JXButton(localButtonLabels_base.getMessage(135171));
    localCellConstraints.xywh(8, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.setBackground(this._mShadowPanel.getBackground());
    return localJPanel;
  }
  
  private void okClicked()
  {
    String str1 = this._mCustomerName.getText();
    String str2 = this._mCustomerAddress.getText();
    String str3 = this._mPhone.getText();
    if ((CommonConfig.getInstance().customerOption == 0) && (!this._mMandatory) && (str1.length() == 0) && (str2.length() == 0) && (str3.length() == 0))
    {
      setVisible(false);
      this._mCancelled = false;
      return;
    }
    if (str1.length() == 0)
    {
      UICommon.showError("Customer Name cannot be empty.", "Error", BillingLauncher.getInstance());
      this._mCustomerName.requestFocusInWindow();
      return;
    }
    if (str1.length() > 31)
    {
      UICommon.showError("Customer Name cannot exceed 31 characeters.", "Error", BillingLauncher.getInstance());
      this._mCustomerName.requestFocusInWindow();
      return;
    }
    if (str2.length() == 0)
    {
      UICommon.showError("Address cannot be empty.", "Error", BillingLauncher.getInstance());
      this._mCustomerAddress.requestFocusInWindow();
      return;
    }
    if (str2.length() > 255)
    {
      UICommon.showError("Address cannot exceed 255 characeters.", "Error", BillingLauncher.getInstance());
      this._mCustomerAddress.requestFocusInWindow();
      return;
    }
    if (!Validation.isValidPhone(str3))
    {
      UICommon.showError("Invalid Phone number.", "Error", BillingLauncher.getInstance());
      this._mPhone.requestFocusInWindow();
      return;
    }
    String str4 = this._mCustomerId.getText();
    int i = 0;
    if (str4.length() == 0)
    {
      int j = UICommon.showQuestion("Customer Id is empty. A new custmer entry will be created.\n\nDo you want to proceed", "Confirm customer creation", BillingLauncher.getInstance());
      if (j != 1) {
        return;
      }
      if (j == 1)
      {
        i = 1;
        this._mCustomer = CustomerTableDef.getInstance().getNewRow();
        DBConnection localDBConnection = Db.getConnection();
        try
        {
          localDBConnection.openTrans();
          this._mCustomer.create();
          localDBConnection.endTrans();
        }
        catch (DBException localDBException)
        {
          localDBConnection.rollbackNoExp();
          UICommon.showError("Internal error creating customer entry. Please try again later.\n\nIf the problem persists contact administrator", "Internal Error", BillingLauncher.getInstance());
          this._mCancelled = false;
          return;
        }
      }
    }
    this._mCanRedeem = false;
    this._mPointsToRedeem = 0;
    if (this._mRedeemLoyalty.isSelected())
    {
      this._mCanRedeem = true;
      String str5 = this._mRedeemPoints.getText().trim();
      if (str5.length() != 0)
      {
        if (!Validation.isValidInt(str5, false))
        {
          this._mRedeemPoints.requestFocusInWindow();
          UICommon.showError("Invalid value for redeemable points.", "Error", MainWindow.instance);
          return;
        }
        int m = Integer.valueOf(str5).intValue();
        if (m > this._mCustomer.getLoyalty())
        {
          this._mRedeemPoints.requestFocusInWindow();
          UICommon.showError("Not enough loyalty points to redeem.", "Error", MainWindow.instance);
          return;
        }
        if (this._mRedeemablePoints < m)
        {
          this._mRedeemPoints.requestFocusInWindow();
          this._mRedeemPoints.setText("" + this._mRedeemablePoints);
          UICommon.showError("Only " + this._mRedeemablePoints + " points can be redeemed.", "Error", MainWindow.instance);
          return;
        }
        this._mPointsToRedeem = m;
      }
    }
    int k = 0;
    if (i == 0) {
      if (this._mUpdateMode)
      {
        str4 = this._mReturnLoyalty.getText().trim();
        if (str4.length() > 0)
        {
          if (!Validation.isValidInt(str4, false))
          {
            UICommon.showError("Invalid value for loyalty points to be returned.", "Error", MainWindow.instance);
            this._mReturnLoyalty.requestFocusInWindow();
          }
          k = Integer.valueOf(str4).intValue();
          this._mCustomer.setLoyalty(this._mCustomer.getLoyalty() - k);
        }
      }
      else
      {
        this._mCustomer.setLoyalty(this._mCustomer.getLoyalty() + this._mBillLoyaltyPoints);
      }
    }
    this._mCancelled = false;
    setVisible(false);
  }
  
  public boolean isRedeemOptionSelected()
  {
    return this._mCanRedeem;
  }
  
  public int getRedeemedPoints()
  {
    return this._mPointsToRedeem;
  }
  
  private void searchForCustomerId()
  {
    String str1 = this._mCustomerId.getText();
    JbMessageLoader localJbMessageLoader = (JbMessageLoader)UICommon.getMessageLoader();
    Customer_base localCustomer_base = localJbMessageLoader.getCustomerMessages();
    if (str1.length() == 0)
    {
      String str2 = localCustomer_base.getMessage(139281);
      UICommon.showError(str2, "Error", BillingLauncher.getInstance());
      this._mCustomerId.requestFocusInWindow();
      return;
    }
    if (!Validation.isValidInt(str1, false))
    {
      UICommon.showError("Invalid Customer ID.", "Error", BillingLauncher.getInstance());
      this._mCustomerId.requestFocusInWindow();
      return;
    }
    int i = Integer.valueOf(str1).intValue();
    CustomerRow localCustomerRow = null;
    try
    {
      localCustomerRow = CustomerTableDef.getInstance().findRowByIndex(i);
    }
    catch (DBException localDBException)
    {
      UICommon.showError("Internal error reading customer details", "Internal Error", BillingLauncher.getInstance());
      this._mCustomerId.requestFocusInWindow();
      return;
    }
    if (localCustomerRow == null)
    {
      UICommon.showError("No customer found matching the ID.", "Error", BillingLauncher.getInstance());
      this._mCustomerId.requestFocusInWindow();
      return;
    }
    setCustomer(localCustomerRow, false);
  }
  
  private void searchByBarCode()
  {
    String[] arrayOfString = { "CUST_INDEX", "Customer Id", "CUST_NAME", "Customer Name", "BARCODE", "Barcode", "RFID", "RFID", "CUST_PHONE", "Customer Phone" };
    ArrayList localArrayList = null;
    try
    {
      String str = this._mBarCode.getText().trim();
      localObject = "BARCODE LIKE '" + str + "%'";
      if (str.length() == 0) {
        localObject = (String)localObject + " OR BARCODE IS NULL";
      }
      localArrayList = CustomerTableDef.getInstance().getAllValuesWithWhereClause((String)localObject);
      if ((localArrayList == null) || (localArrayList.size() == 0))
      {
        UICommon.showError("No customer found matching the record.", "No data", MainWindow.instance);
        this._mBarCode.requestFocusInWindow();
        setCustomer((CustomerRow)localArrayList.get(0), false);
        return;
      }
      if (localArrayList.size() == 1)
      {
        setCustomer((CustomerRow)localArrayList.get(0), false);
        return;
      }
    }
    catch (DBException localDBException)
    {
      UICommon.showError("Internal error searching customer details.", "Internal Error", MainWindow.instance);
      return;
    }
    ResultPanelBig localResultPanelBig = new ResultPanelBig(arrayOfString, MainWindow.instance, "ISP_BILL_CUSTOMER_SEARCH");
    localResultPanelBig.setData(localArrayList);
    localResultPanelBig.setLocationRelativeTo(MainWindow.instance);
    localResultPanelBig.setVisible(true);
    this._mBarCode.requestFocusInWindow();
    if (localResultPanelBig.isCancelled()) {
      return;
    }
    Object localObject = localResultPanelBig.getSelectedRow();
    setCustomer((CustomerRow)localObject, false);
  }
  
  private void searchByRFID()
  {
    String[] arrayOfString = { "CUST_INDEX", "Customer Id", "CUST_NAME", "Customer Name", "BARCODE", "Barcode", "RFID", "RFID", "CUST_PHONE", "Customer Phone" };
    ArrayList localArrayList = null;
    try
    {
      String str = this._mRFID.getText().trim();
      localObject = "RFID LIKE '" + str + "%' ";
      if (str.length() == 0) {
        localObject = (String)localObject + " OR RFID IS NULL ";
      }
      localArrayList = CustomerTableDef.getInstance().getAllValuesWithWhereClause((String)localObject);
      if ((localArrayList == null) || (localArrayList.size() == 0))
      {
        UICommon.showError("No customer found matching the record.", "No data", MainWindow.instance);
        this._mRFID.requestFocusInWindow();
        return;
      }
      if (localArrayList.size() == 1)
      {
        setCustomer((CustomerRow)localArrayList.get(0), false);
        return;
      }
    }
    catch (DBException localDBException)
    {
      UICommon.showError("Internal error searching customer details.", "Internal Error", MainWindow.instance);
      return;
    }
    ResultPanelBig localResultPanelBig = new ResultPanelBig(arrayOfString, MainWindow.instance, "ISP_BILL_CUSTOMER_SEARCH");
    localResultPanelBig.setData(localArrayList);
    localResultPanelBig.setLocationRelativeTo(MainWindow.instance);
    localResultPanelBig.setVisible(true);
    this._mRFID.requestFocusInWindow();
    if (localResultPanelBig.isCancelled()) {
      return;
    }
    Object localObject = localResultPanelBig.getSelectedRow();
    setCustomer((CustomerRow)localObject, false);
  }
  
  private void searchByCusomerName()
  {
    String[] arrayOfString = { "CUST_INDEX", "Customer Id", "CUST_NAME", "Customer Name", "BARCODE", "Barcode", "RFID", "RFID", "CUST_PHONE", "Customer Phone" };
    ArrayList localArrayList = null;
    try
    {
      String str = this._mCustomerName.getText().trim();
      localArrayList = CustomerTableDef.getInstance().getAllValuesWithWhereClause("CUST_NAME LIKE '" + str + "%'");
      if (localArrayList == null)
      {
        UICommon.showError("No Data found.", "No Data", MainWindow.instance);
        return;
      }
    }
    catch (DBException localDBException)
    {
      UICommon.showError("Internal error searching customer details.", "Internal Error", MainWindow.instance);
      return;
    }
    ResultPanelBig localResultPanelBig = new ResultPanelBig(arrayOfString, MainWindow.instance, "ISP_BILL_CUSTOMER_SEARCH");
    localResultPanelBig.setData(localArrayList);
    localResultPanelBig.setLocationRelativeTo(MainWindow.instance);
    localResultPanelBig.setVisible(true);
    this._mCustomerName.requestFocusInWindow();
    if (localResultPanelBig.isCancelled()) {
      return;
    }
    DBRow localDBRow = localResultPanelBig.getSelectedRow();
    setCustomer((CustomerRow)localDBRow, false);
  }
  
  public void setUpdate(boolean paramBoolean)
  {
    this._mUpdateMode = paramBoolean;
  }
  
  public void setCustomer(CustomerRow paramCustomerRow, boolean paramBoolean)
  {
    if ((paramBoolean) && (paramCustomerRow != null)) {
      this._mOldCustomer = paramCustomerRow;
    }
    this._mCustomerName.setEnabled(true);
    this._mCustomerAddress.setEnabled(true);
    this._mPhone.setEnabled(true);
    this._mCustomerId.setEnabled(true);
    this._mIdSearchButton.setEnabled(true);
    this._mNameSearchButton.setEnabled(true);
    this._mPhoneSearchButton.setEnabled(true);
    this._mRFID.setEnabled(true);
    this._mBarCode.setEnabled(true);
    if (paramCustomerRow == null)
    {
      this._mLoyaltyPoints.setText("" + this._mBillLoyaltyPoints);
      return;
    }
    this._mCustomerId.setText(new Integer(paramCustomerRow.getCustIndex()).toString());
    this._mCustomerName.setText(paramCustomerRow.getCustName());
    this._mCustomerAddress.setText(paramCustomerRow.getCustAddress());
    this._mPhone.setText(paramCustomerRow.getCustPhone());
    if ((this._mOldCustomer != null) && (this._mOldCustomer.getCustIndex() == paramCustomerRow.getCustIndex()))
    {
      int i = this._mOldPointsAwarded - this._mBillLoyaltyPoints;
      if (i < 0) {
        this._mLoyaltyPoints.setText("" + paramCustomerRow.getLoyalty() + " + 0");
      } else {
        this._mLoyaltyPoints.setText("" + paramCustomerRow.getLoyalty() + " + " + i);
      }
      int j = this._mOldLoyaltyRedeemed - this._mRedeemablePoints;
      if (j > 0)
      {
        this._mReturnLoyalty.setText("" + j);
        this._mRedeemPoints.setText("0");
      }
      else
      {
        this._mReturnLoyalty.setText("0");
        j *= -1;
        this._mRedeemPoints.setText("" + j);
        this._mRedeemablePoints = j;
      }
    }
    else
    {
      this._mReturnLoyalty.setText("0");
      this._mLoyaltyPoints.setText("" + paramCustomerRow.getLoyalty() + " + " + this._mBillLoyaltyPoints);
    }
    if (paramCustomerRow.getBarcode() == null) {
      this._mBarCode.setText("");
    } else {
      this._mBarCode.setText(paramCustomerRow.getBarcode());
    }
    if (paramCustomerRow.getRfid() == null) {
      this._mRFID.setText("");
    } else {
      this._mRFID.setText(paramCustomerRow.getRfid());
    }
    this._mCustomer = paramCustomerRow;
  }
  
  private void searchByCusomerPhone()
  {
    String[] arrayOfString = { "CUST_INDEX", "Customer Id", "CUST_NAME", "Customer Name", "CUST_PHONE", "Customer Phone" };
    ArrayList localArrayList = null;
    try
    {
      String str = this._mPhone.getText().trim();
      localArrayList = CustomerTableDef.getInstance().getAllValuesWithWhereClause("CUST_PHONE LIKE '" + str + "%'");
      if (localArrayList == null)
      {
        UICommon.showError("No Data found.", "No Data", MainWindow.instance);
        return;
      }
    }
    catch (DBException localDBException)
    {
      UICommon.showError("Internal error searching customer details.", "Internal Error", MainWindow.instance);
      return;
    }
    ResultPanelBig localResultPanelBig = new ResultPanelBig(arrayOfString, MainWindow.instance, "ISP_BILL_CUSTOMER_SEARCH");
    localResultPanelBig.setData(localArrayList);
    localResultPanelBig.setLocationRelativeTo(MainWindow.instance);
    localResultPanelBig.setVisible(true);
    this._mPhone.requestFocusInWindow();
    if (localResultPanelBig.isCancelled()) {
      return;
    }
    DBRow localDBRow = localResultPanelBig.getSelectedRow();
    setCustomer((CustomerRow)localDBRow, false);
  }
  
  void setLoyaltyPoints(int paramInt, boolean paramBoolean)
  {
    this._mBillLoyaltyPoints = paramInt;
    if (this._mCustomer == null) {
      this._mLoyaltyPoints.setText("" + paramInt);
    } else if (paramBoolean) {
      this._mLoyaltyPoints.setText("" + this._mCustomer.getLoyalty());
    } else {
      this._mLoyaltyPoints.setText(this._mCustomer.getLoyalty() + " + " + paramInt);
    }
  }
  
  public CustomerRow getSelectedRow()
  {
    return this._mCustomer;
  }
  
  public boolean isCancelled()
  {
    return this._mCancelled;
  }
  
  private void resetClicked()
  {
    clearAllFields();
    this._mCustomer = null;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.billing.CustomerInfoDialog
 * JD-Core Version:    0.7.0.1
 */