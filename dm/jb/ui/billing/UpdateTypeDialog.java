package dm.jb.ui.billing;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.ui.common.JBShadowPanel;
import dm.jb.ui.common.JBTransparentWindow;
import dm.jb.ui.common.ShortKeyCommon;
import dm.tools.types.InternalAmount;
import dm.tools.ui.UICommon;
import dm.tools.utils.Validation;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import org.jdesktop.swingx.JXButton;

public class UpdateTypeDialog
  extends JBTransparentWindow
{
  private static StockReturnOption[] _mReturnOptions = { new StockReturnOption("To Store", 1), new StockReturnOption("None", 3), new StockReturnOption("To Vendor", 2) };
  private static UpdateTypeDialog _mInstance = null;
  private JComboBox _mReturnMode = null;
  private JComboBox _mRestockOption = null;
  private JXButton _mOKButton = null;
  private JBShadowPanel _mShadowPanel = null;
  private JTextField _mRefund = null;
  private boolean _mIsCancelled = true;
  private int _mPreviousMode = 0;
  private boolean _mAddMode = false;
  
  private UpdateTypeDialog()
  {
    super(BillingLauncher.getInstance());
    initUI();
    pack();
    setLocationRelativeTo(BillingLauncher.getInstance());
    InputMap localInputMap = ((JPanel)getContentPane()).getInputMap(2);
    String str = "ESCAction";
    localInputMap.put(KeyStroke.getKeyStroke("ESCAPE"), str);
    AbstractAction local1 = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        UpdateTypeDialog.this.setVisible(false);
      }
    };
    ((JPanel)getContentPane()).getActionMap().put(str, local1);
    addWindowListener(new WindowAdapter()
    {
      public void windowOpened(WindowEvent paramAnonymousWindowEvent)
      {
        UpdateTypeDialog.this.getRootPane().setDefaultButton(UpdateTypeDialog.this._mOKButton);
        UpdateTypeDialog.this._mRefund.requestFocusInWindow();
        UpdateTypeDialog.this.returnModeChanged();
      }
    });
  }
  
  public void initialize()
  {
    this._mRestockOption.setSelectedIndex(0);
    this._mReturnMode.requestFocusInWindow();
    this._mIsCancelled = true;
    this._mReturnMode.setEnabled(true);
    this._mRefund.setText("0.00");
    this._mAddMode = false;
  }
  
  public void setPreviousMode(int paramInt)
  {
    int i = this._mReturnMode.getItemCount();
    for (int j = 0; j < i; j++)
    {
      BillUpdateMode localBillUpdateMode = (BillUpdateMode)this._mReturnMode.getItemAt(j);
      if (localBillUpdateMode.code == paramInt) {
        this._mReturnMode.setSelectedIndex(j);
      }
    }
    this._mPreviousMode = paramInt;
  }
  
  public static UpdateTypeDialog getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new UpdateTypeDialog();
    }
    return _mInstance;
  }
  
  public int getUpdateMode()
  {
    BillUpdateMode localBillUpdateMode = (BillUpdateMode)this._mReturnMode.getSelectedItem();
    return localBillUpdateMode.code;
  }
  
  public byte getRestockMode()
  {
    StockReturnOption localStockReturnOption = (StockReturnOption)this._mRestockOption.getSelectedItem();
    return localStockReturnOption.code;
  }
  
  public void setNoRefundMode(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this._mRefund.setEditable(false);
      this._mRefund.setText("0.00");
    }
    else
    {
      this._mRefund.setEditable(true);
      this._mRefund.setText("0.00");
    }
  }
  
  public void setRefundAmount(double paramDouble)
  {
    this._mRefund.setText(InternalAmount.valueOf(paramDouble).toString());
  }
  
  public void setAddMode(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this._mReturnMode.setSelectedItem(BillUpdateMode.billUpdateModes[3]);
      this._mAddMode = true;
    }
    else
    {
      this._mReturnMode.setSelectedIndex(0);
      this._mAddMode = false;
    }
  }
  
  private void initUI()
  {
    JPanel localJPanel = (JPanel)getContentPane();
    localJPanel.setLayout(new BorderLayout());
    localJPanel.add(new ProductReturnDetails(), "Center");
  }
  
  private JPanel getButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    localJPanel.setOpaque(false);
    CellConstraints localCellConstraints = new CellConstraints();
    FormLayout localFormLayout = new FormLayout("120px,10px:grow,120px,10px:grow,120px", "35px");
    localJPanel.setLayout(localFormLayout);
    this._mOKButton = new JXButton("OK");
    this._mOKButton.setBackground(this._mShadowPanel.getBackground());
    this._mOKButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        UpdateTypeDialog.this.okClicked();
      }
    });
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mOKButton, localCellConstraints);
    JXButton localJXButton = new JXButton("Close");
    localCellConstraints.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.setBackground(this._mShadowPanel.getBackground());
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        UpdateTypeDialog._mInstance.setVisible(false);
      }
    });
    localJXButton = new JXButton("Help");
    localCellConstraints.xywh(5, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.setBackground(this._mShadowPanel.getBackground());
    return localJPanel;
  }
  
  private void okClicked()
  {
    String str = this._mRefund.getText();
    if (this._mAddMode)
    {
      BillUpdateMode localBillUpdateMode = (BillUpdateMode)this._mReturnMode.getSelectedItem();
      if ((localBillUpdateMode.code != 4) && (localBillUpdateMode.code != 2))
      {
        UICommon.showError("For additional quantity only 'Billing Error' or 'Addition' is allowed.", "Error", this);
        return;
      }
    }
    if (str.length() == 0)
    {
      UICommon.showError("Refund amount is not valid.", "Error", BillingLauncher.getInstance());
      this._mRefund.requestFocusInWindow();
      return;
    }
    if (!Validation.isValidAmount(str))
    {
      UICommon.showError("Refund amount is not valid.", "Error", BillingLauncher.getInstance());
      this._mRefund.requestFocusInWindow();
      return;
    }
    this._mIsCancelled = false;
    _mInstance.setVisible(false);
  }
  
  public boolean isCancelled()
  {
    return this._mIsCancelled;
  }
  
  public double getRefund()
  {
    String str = this._mRefund.getText();
    return Double.valueOf(str).doubleValue();
  }
  
  private void returnModeChanged()
  {
    BillUpdateMode localBillUpdateMode = (BillUpdateMode)this._mReturnMode.getSelectedItem();
    if (localBillUpdateMode.code == 1) {
      for (int i = 0; i < _mReturnOptions.length; i++) {
        if (_mReturnOptions[i].code == 2)
        {
          this._mRestockOption.setSelectedItem(_mReturnOptions[i]);
          this._mRestockOption.setEnabled(false);
          return;
        }
      }
    }
    this._mRestockOption.setSelectedIndex(0);
    this._mRestockOption.setEnabled(true);
  }
  
  private class ProductReturnDetails
    extends JBShadowPanel
  {
    public ProductReturnDetails()
    {
      super();
      UpdateTypeDialog.this._mShadowPanel = this;
      initUI();
    }
    
    private void initUI()
    {
      FormLayout localFormLayout = new FormLayout("30px, 100px, 10px, 150px,10px:grow, 30px", "30px, 28px, 10px, 28px,10px,28px,20px,40px,30px");
      setLayout(localFormLayout);
      CellConstraints localCellConstraints = new CellConstraints();
      JLabel localJLabel = new JLabel("Mode : ");
      Font localFont = localJLabel.getFont();
      localFont = new Font(localFont.getName(), 1, 14);
      localJLabel.setFont(localFont);
      localCellConstraints.xywh(2, 2, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
      add(localJLabel, localCellConstraints);
      UpdateTypeDialog.this._mReturnMode = new JComboBox(BillUpdateMode.billUpdateModes);
      ShortKeyCommon.shortKeyForLabels(localJLabel, 77, UpdateTypeDialog.this._mReturnMode);
      localCellConstraints.xywh(4, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
      add(UpdateTypeDialog.this._mReturnMode, localCellConstraints);
      UpdateTypeDialog.this._mReturnMode.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          UpdateTypeDialog.this.returnModeChanged();
        }
      });
      UpdateTypeDialog.this._mReturnMode.setFont(localFont);
      localJLabel = new JLabel("Refund : ");
      localJLabel.setFont(localFont);
      localCellConstraints.xywh(2, 4, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
      add(localJLabel, localCellConstraints);
      UpdateTypeDialog.this._mRefund = new JTextField();
      localCellConstraints.xywh(4, 4, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
      add(UpdateTypeDialog.this._mRefund, localCellConstraints);
      UpdateTypeDialog.this._mRefund.setFont(localFont);
      ShortKeyCommon.shortKeyForLabels(localJLabel, 70, UpdateTypeDialog.this._mRefund);
      localJLabel = new JLabel("Restock to : ");
      localJLabel.setFont(localFont);
      localCellConstraints.xywh(2, 6, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
      add(localJLabel, localCellConstraints);
      UpdateTypeDialog.this._mRestockOption = new JComboBox(UpdateTypeDialog._mReturnOptions);
      localCellConstraints.xywh(4, 6, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
      add(UpdateTypeDialog.this._mRestockOption, localCellConstraints);
      UpdateTypeDialog.this._mRestockOption.setFont(localFont);
      UpdateTypeDialog.this._mRestockOption.setSelectedIndex(0);
      UpdateTypeDialog.this._mReturnMode.addKeyListener(new KeyAdapter()
      {
        public void keyPressed(KeyEvent paramAnonymousKeyEvent)
        {
          if (paramAnonymousKeyEvent.getKeyCode() == 10) {
            UpdateTypeDialog.this._mRestockOption.requestFocusInWindow();
          }
        }
      });
      ShortKeyCommon.shortKeyForLabels(localJLabel, 84, UpdateTypeDialog.this._mRestockOption);
      localCellConstraints.xywh(2, 8, localFormLayout.getColumnCount() - 2, 1, CellConstraints.CENTER, CellConstraints.FILL);
      add(UpdateTypeDialog.this.getButtonPanel(), localCellConstraints);
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.billing.UpdateTypeDialog
 * JD-Core Version:    0.7.0.1
 */