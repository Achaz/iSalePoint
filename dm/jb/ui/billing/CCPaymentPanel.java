package dm.jb.ui.billing;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.ui.common.JBNumTextFieldWithKeyPad;
import dm.tools.types.InternalAmount;
import dm.tools.ui.UICommon;
import dm.tools.utils.Validation;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.PrintStream;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CCPaymentPanel
  extends JPanel
  implements PaymentOptionPanel
{
  private JBNumTextFieldWithKeyPad _mCC1 = null;
  private JBNumTextFieldWithKeyPad _mCC2 = null;
  private JBNumTextFieldWithKeyPad _mCC3 = null;
  private JBNumTextFieldWithKeyPad _mCC4 = null;
  private JBNumTextFieldWithKeyPad _mRefNo = null;
  private JBNumTextFieldWithKeyPad _mAmount = null;
  private JComboBox _mCardType = null;
  
  public CCPaymentPanel()
  {
    initUI();
    setOpaque(false);
  }
  
  public void windowDisplayed()
  {
    this._mAmount.requestFocusInWindow();
    this._mAmount.setCaretPosition(0);
  }
  
  public boolean isPageValid()
  {
    String str1 = this._mAmount.getText();
    if (str1.length() == 0)
    {
      UICommon.showError("Invalid amount specified.", "Error", BillingLauncher.getInstance());
      this._mAmount.requestFocusInWindow();
      return false;
    }
    if (!Validation.isValidAmount(str1))
    {
      UICommon.showError("Invalid amount specified.", "Error", BillingLauncher.INSTANCE);
      this._mAmount.requestFocusInWindow();
      return false;
    }
    String str2 = this._mCC1.getText();
    String str3 = this._mCC2.getText();
    String str4 = this._mCC3.getText();
    String str5 = this._mCC4.getText();
    if ((str2.length() != 4) || (!Validation.isValidInt(str2, false)))
    {
      UICommon.showError("Invalid card number.", "Error", BillingLauncher.INSTANCE);
      this._mCC1.requestFocusInWindow();
      return false;
    }
    if ((str3.length() != 4) || (!Validation.isValidInt(str3, false)))
    {
      UICommon.showError("Invalid card number.", "Error", BillingLauncher.INSTANCE);
      this._mCC2.requestFocusInWindow();
      return false;
    }
    if ((str4.length() != 4) || (!Validation.isValidInt(str4, false)))
    {
      UICommon.showError("Invalid card number.", "Error", BillingLauncher.INSTANCE);
      this._mCC3.requestFocusInWindow();
      return false;
    }
    if ((str5.length() != 4) || (!Validation.isValidInt(str5, false)))
    {
      UICommon.showError("Invalid card number.", "Error", BillingLauncher.INSTANCE);
      this._mCC4.requestFocusInWindow();
      return false;
    }
    String str6 = this._mRefNo.getText().trim();
    if (str6.length() == 0)
    {
      UICommon.showError("Transaction number cannot be empty.", "Error", BillingLauncher.INSTANCE);
      this._mRefNo.requestFocusInWindow();
      return false;
    }
    if (str6.length() > 10)
    {
      UICommon.showError("Invalid transaction number.", "Error", BillingLauncher.INSTANCE);
      this._mRefNo.requestFocusInWindow();
      return false;
    }
    return true;
  }
  
  public void setAmount(double paramDouble)
  {
    this._mAmount.setText(new InternalAmount(paramDouble, "", "", false).toString());
    this._mAmount.setCaretPosition(0);
  }
  
  public void setObject(PaymentModeObject paramPaymentModeObject)
  {
    CCPaymentObject localCCPaymentObject = (CCPaymentObject)paramPaymentModeObject;
    setAmount(localCCPaymentObject._mAmount);
    if ((localCCPaymentObject._mCCNo != null) && (localCCPaymentObject._mCCNo.length() != 0))
    {
      String str1 = localCCPaymentObject._mCCNo.substring(0, 4);
      String str2 = localCCPaymentObject._mCCNo.substring(4, 8);
      String str3 = localCCPaymentObject._mCCNo.substring(8, 12);
      String str4 = localCCPaymentObject._mCCNo.substring(12, 16);
      this._mCC1.setText(str1);
      this._mCC2.setText(str2);
      this._mCC3.setText(str3);
      this._mCC4.setText(str4);
    }
    this._mRefNo.setText(localCCPaymentObject._mRefNo == null ? "" : localCCPaymentObject._mRefNo);
    this._mCardType.setSelectedItem(localCCPaymentObject._mCCType);
  }
  
  public void clearFields()
  {
    this._mCC1.setText("");
    this._mCC2.setText("");
    this._mCC3.setText("");
    this._mCC4.setText("");
    this._mAmount.setText("");
    this._mRefNo.setText("");
  }
  
  public void getValuesInObject(PaymentModeObject paramPaymentModeObject)
  {
    CCPaymentObject localCCPaymentObject = (CCPaymentObject)paramPaymentModeObject;
    localCCPaymentObject._mCCNo = getCCNo();
    localCCPaymentObject._mAmount = getAmount();
    localCCPaymentObject._mCCType = ((CCPaymentOption.CardType)this._mCardType.getSelectedItem());
    localCCPaymentObject._mRefNo = getRefNo();
  }
  
  public void setEditable(boolean paramBoolean)
  {
    this._mCC1.setEditable(paramBoolean);
    this._mCC2.setEditable(paramBoolean);
    this._mCC3.setEditable(paramBoolean);
    this._mCC4.setEditable(paramBoolean);
    this._mRefNo.setEditable(paramBoolean);
    this._mAmount.setEditable(paramBoolean);
    this._mCardType.setEnabled(paramBoolean);
  }
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("140px,10px, 80px,5px,80px,5px,80px,5px,80px", "30px,10px,30px,10px, 30px,10px,30px");
    setLayout(localFormLayout);
    System.out.println();
    this._mCC1 = new JBNumTextFieldWithKeyPad(PaymentDialog.getDialogInstance());
    this._mCC1.setColumns(4);
    this._mCC2 = new JBNumTextFieldWithKeyPad(PaymentDialog.getDialogInstance());
    this._mCC2.setColumns(4);
    this._mCC3 = new JBNumTextFieldWithKeyPad(PaymentDialog.getDialogInstance());
    this._mCC3.setColumns(4);
    this._mCC4 = new JBNumTextFieldWithKeyPad(PaymentDialog.getDialogInstance());
    this._mCC4.setColumns(4);
    Font localFont1 = this._mCC1.getFont();
    Font localFont2 = new Font(localFont1.getName(), 1, 20);
    JLabel localJLabel = new JLabel("Amount : ");
    localFont1 = localJLabel.getFont();
    Font localFont3 = new Font(localFont1.getName(), 0, 20);
    localJLabel.setFont(localFont3);
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mAmount = new JBNumTextFieldWithKeyPad();
    localFont1 = this._mAmount.getFont();
    localFont2 = new Font(localFont1.getName(), 1, 20);
    this._mAmount.setFont(localFont2);
    localCellConstraints.xywh(3, 1, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mAmount, localCellConstraints);
    this._mAmount.setHorizontalAlignment(4);
    this._mAmount.setBackground(UICommon.MANDATORY_COLOR);
    localJLabel = new JLabel("Card Number : ");
    localCellConstraints.xywh(1, 3, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    localJLabel.setFont(localFont3);
    localFont2 = new Font(localFont1.getName(), localFont1.getStyle(), 20);
    this._mCC1.setFont(localFont2);
    this._mCC2.setFont(localFont2);
    this._mCC3.setFont(localFont2);
    this._mCC4.setFont(localFont2);
    localCellConstraints.xywh(3, 3, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mCC1, localCellConstraints);
    localCellConstraints.xywh(5, 3, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mCC2, localCellConstraints);
    localCellConstraints.xywh(7, 3, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mCC3, localCellConstraints);
    localCellConstraints.xywh(9, 3, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mCC4, localCellConstraints);
    this._mCC1.addKeyListener(new KeyAdapter()
    {
      public void keyReleased(KeyEvent paramAnonymousKeyEvent)
      {
        if (CCPaymentPanel.this._mCC1.getText().trim().length() == 4) {
          CCPaymentPanel.this._mCC2.requestFocusInWindow();
        }
      }
    });
    this._mCC2.addKeyListener(new KeyAdapter()
    {
      public void keyReleased(KeyEvent paramAnonymousKeyEvent)
      {
        if (CCPaymentPanel.this._mCC2.getText().trim().length() == 4) {
          CCPaymentPanel.this._mCC3.requestFocusInWindow();
        }
      }
    });
    this._mCC3.addKeyListener(new KeyAdapter()
    {
      public void keyReleased(KeyEvent paramAnonymousKeyEvent)
      {
        if (CCPaymentPanel.this._mCC3.getText().trim().length() == 4) {
          CCPaymentPanel.this._mCC4.requestFocusInWindow();
        }
      }
    });
    this._mCC4.addKeyListener(new KeyAdapter()
    {
      public void keyReleased(KeyEvent paramAnonymousKeyEvent)
      {
        if (CCPaymentPanel.this._mCC4.getText().trim().length() == 4) {
          CCPaymentPanel.this._mRefNo.requestFocusInWindow();
        }
      }
    });
    localJLabel = new JLabel("Transaction : ");
    localCellConstraints.xywh(1, 5, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    localJLabel.setFont(localFont3);
    this._mRefNo = new JBNumTextFieldWithKeyPad();
    localCellConstraints.xywh(3, 5, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mRefNo, localCellConstraints);
    this._mRefNo.setFont(localFont2);
    localJLabel = new JLabel("Card Type : ");
    localCellConstraints.xywh(1, 7, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    localJLabel.setFont(localFont3);
    this._mCardType = new JComboBox(CCPaymentOption.cardTypes);
    localCellConstraints.xywh(3, 7, 5, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mCardType, localCellConstraints);
    this._mCardType.setFont(localFont2);
  }
  
  public String getCCNo()
  {
    return this._mCC1.getText().trim() + this._mCC2.getText().trim() + this._mCC3.getText().trim() + this._mCC4.getText().trim();
  }
  
  public String getRefNo()
  {
    return this._mRefNo.getText().trim();
  }
  
  public double getAmount()
  {
    String str = this._mAmount.getText().trim();
    return Double.valueOf(str).doubleValue();
  }
  
  public CCPaymentOption.CardType getCardType()
  {
    return (CCPaymentOption.CardType)this._mCardType.getSelectedItem();
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.billing.CCPaymentPanel
 * JD-Core Version:    0.7.0.1
 */