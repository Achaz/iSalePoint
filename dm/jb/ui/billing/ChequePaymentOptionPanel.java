package dm.jb.ui.billing;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.ui.common.JBNumTextFieldWithKeyPad;
import dm.jb.ui.settings.CountryInfo;
import dm.tools.types.InternalAmount;
import dm.tools.ui.CustomDateChooser;
import dm.tools.ui.UICommon;
import dm.tools.utils.CommonConfig;
import dm.tools.utils.Validation;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ChequePaymentOptionPanel
  extends JPanel
  implements PaymentOptionPanel
{
  private JTextField _mAmount = null;
  private JBNumTextFieldWithKeyPad _mChequeNo = null;
  private JTextField _mBank = null;
  private CustomDateChooser _mDate = null;
  
  public ChequePaymentOptionPanel()
  {
    initUI();
    setOpaque(false);
  }
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("140px,10px,150px,3px,50px", "30px,10px, 30px,10px,30px,10px,30px,10px");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    JLabel localJLabel = new JLabel("Amount : ");
    Font localFont1 = localJLabel.getFont();
    Font localFont2 = new Font(localFont1.getName(), 0, 20);
    localJLabel.setFont(localFont2);
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mAmount = new JTextField();
    localFont1 = this._mAmount.getFont();
    Font localFont3 = new Font(localFont1.getName(), 1, 20);
    this._mAmount.setFont(localFont3);
    localCellConstraints.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mAmount, localCellConstraints);
    this._mAmount.setBackground(UICommon.MANDATORY_COLOR);
    localCellConstraints.xywh(5, 1, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    localJLabel = new JLabel(CommonConfig.getInstance().country.currency);
    localJLabel.setFont(localFont2);
    add(localJLabel, localCellConstraints);
    this._mAmount.setHorizontalAlignment(4);
    localFont3 = new Font(localFont1.getName(), localFont1.getStyle(), 20);
    localJLabel = new JLabel("Cheque No : ");
    localJLabel.setFont(localFont2);
    localCellConstraints.xywh(1, 3, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mChequeNo = new JBNumTextFieldWithKeyPad(PaymentDialog.getDialogInstance());
    this._mChequeNo.setFont(localFont3);
    localCellConstraints.xywh(3, 3, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mChequeNo, localCellConstraints);
    this._mChequeNo.setBackground(UICommon.MANDATORY_COLOR);
    localJLabel = new JLabel("Bank : ");
    localJLabel.setFont(localFont2);
    localCellConstraints.xywh(1, 5, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mBank = new JTextField();
    this._mBank.setFont(localFont3);
    localCellConstraints.xywh(3, 5, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mBank, localCellConstraints);
    this._mBank.setBackground(UICommon.MANDATORY_COLOR);
    localJLabel = new JLabel("Date : ");
    localJLabel.setFont(localFont2);
    localCellConstraints.xywh(1, 7, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mDate = new CustomDateChooser(new java.util.Date(), "dd-MM-yyyy");
    this._mDate.setFont(localFont3);
    this._mDate.setPreferredSize(new Dimension(150, 35));
    localCellConstraints.xywh(3, 7, 3, 1, CellConstraints.LEFT, CellConstraints.FILL);
    add(this._mDate, localCellConstraints);
    this._mDate.setBackground(UICommon.MANDATORY_COLOR);
  }
  
  public void windowDisplayed()
  {
    this._mAmount.requestFocusInWindow();
    this._mAmount.setCaretPosition(0);
  }
  
  public void setEditable(boolean paramBoolean)
  {
    this._mAmount.setEditable(paramBoolean);
    this._mChequeNo.setEditable(paramBoolean);
    this._mBank.setEditable(paramBoolean);
    this._mDate.setEnabled(paramBoolean);
  }
  
  public boolean isPageValid()
  {
    String str = this._mAmount.getText();
    if (str.length() == 0)
    {
      UICommon.showError("Invalid amount specified.", "Error", BillingLauncher.INSTANCE);
      this._mAmount.requestFocusInWindow();
      return false;
    }
    if (!Validation.isValidAmount(str))
    {
      UICommon.showError("Invalid amount specified.", "Error", BillingLauncher.INSTANCE);
      this._mAmount.requestFocusInWindow();
      return false;
    }
    str = this._mChequeNo.getText();
    if (str.length() == 0)
    {
      UICommon.showError("Invalid cheque no. specified.", "Error", BillingLauncher.INSTANCE);
      this._mChequeNo.requestFocusInWindow();
      return false;
    }
    if (!Validation.isValidInt(str, false))
    {
      UICommon.showError("Invalid cheque no. specified.", "Error", BillingLauncher.INSTANCE);
      this._mChequeNo.requestFocusInWindow();
      return false;
    }
    str = this._mBank.getText();
    if (str.length() == 0)
    {
      UICommon.showError("Bank name cannot be empty.", "Error", BillingLauncher.INSTANCE);
      this._mBank.requestFocusInWindow();
      return false;
    }
    if (str.length() > 32)
    {
      UICommon.showError("Bank name cannot have more than 32 characters.", "Error", BillingLauncher.INSTANCE);
      this._mBank.requestFocusInWindow();
      return false;
    }
    return true;
  }
  
  public void setAmount(double paramDouble)
  {
    this._mAmount.setText(new InternalAmount(paramDouble, "", "", false).toString());
  }
  
  public double getAmount()
  {
    return Double.valueOf(this._mAmount.getText()).doubleValue();
  }
  
  public String getChequeNo()
  {
    return this._mChequeNo.getText().trim();
  }
  
  public String getBank()
  {
    return this._mBank.getText().trim();
  }
  
  public java.util.Date getDate()
  {
    return this._mDate.getDate();
  }
  
  public void setObject(PaymentModeObject paramPaymentModeObject)
  {
    ChequePaymentObject localChequePaymentObject = (ChequePaymentObject)paramPaymentModeObject;
    this._mAmount.setText(new InternalAmount(localChequePaymentObject.getAmount()).toString());
    this._mChequeNo.setText(localChequePaymentObject.getRefNo());
    this._mBank.setText(localChequePaymentObject.getBank());
    this._mDate.setDate(localChequePaymentObject.getDate());
  }
  
  public void getValuesInObject(PaymentModeObject paramPaymentModeObject)
  {
    ChequePaymentObject localChequePaymentObject = (ChequePaymentObject)paramPaymentModeObject;
    localChequePaymentObject._mAmount = getAmount();
    localChequePaymentObject._mChequeNo = this._mChequeNo.getText();
    localChequePaymentObject._mBank = this._mBank.getText();
    localChequePaymentObject._mDate = new java.sql.Date(this._mDate.getDate().getTime());
  }
  
  public void clearFields()
  {
    this._mAmount.setText("");
    this._mChequeNo.setText("");
    this._mBank.setText("");
    this._mDate.setDate(new java.util.Date());
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.billing.ChequePaymentOptionPanel
 * JD-Core Version:    0.7.0.1
 */