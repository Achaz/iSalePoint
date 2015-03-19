package dm.jb.ui.billing;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.ui.MainWindow;
import dm.jb.ui.settings.CountryInfo;
import dm.tools.types.InternalAmount;
import dm.tools.ui.UICommon;
import dm.tools.utils.CommonConfig;
import dm.tools.utils.Validation;
import java.awt.Font;
import java.io.PrintStream;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CashPaymentOptionPanel
  extends JPanel
  implements PaymentOptionPanel
{
  private JTextField _mAmount = null;
  
  public CashPaymentOptionPanel()
  {
    initUI();
    setOpaque(false);
  }
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("140px,10px,150px,3px,50px", "30px,10px");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    JLabel localJLabel = new JLabel("Amount : ");
    Font localFont = localJLabel.getFont();
    localJLabel.setFont(new Font(localFont.getName(), 0, 20));
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mAmount = new JTextField();
    localFont = this._mAmount.getFont();
    this._mAmount.setFont(new Font(localFont.getName(), 1, 20));
    localCellConstraints.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mAmount, localCellConstraints);
    localJLabel = new JLabel(CommonConfig.getInstance().country.currency);
    localCellConstraints.xywh(5, 1, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    localFont = localJLabel.getFont();
    localJLabel.setFont(new Font(localFont.getName(), 0, 20));
    add(localJLabel, localCellConstraints);
    this._mAmount.setHorizontalAlignment(4);
    this._mAmount.setBackground(UICommon.MANDATORY_COLOR);
  }
  
  public void setEditable(boolean paramBoolean)
  {
    this._mAmount.setEditable(paramBoolean);
    if (paramBoolean) {}
  }
  
  public void windowDisplayed()
  {
    this._mAmount.requestFocusInWindow();
    this._mAmount.setCaretPosition(0);
  }
  
  public boolean isPageValid()
  {
    String str = this._mAmount.getText();
    if (str.length() == 0)
    {
      UICommon.showError("Invalid amount specified.", "Error", MainWindow.instance);
      this._mAmount.requestFocusInWindow();
      return false;
    }
    if (!Validation.isValidAmount(str))
    {
      UICommon.showError("Invalid amount specified.", "Error", MainWindow.instance);
      this._mAmount.requestFocusInWindow();
      return false;
    }
    return true;
  }
  
  public void setAmount(double paramDouble)
  {
    this._mAmount.setText(new InternalAmount(paramDouble, "", "", false).toString());
  }
  
  double getAmount()
  {
    return Double.valueOf(this._mAmount.getText()).doubleValue();
  }
  
  public void setObject(PaymentModeObject paramPaymentModeObject)
  {
    if ((paramPaymentModeObject instanceof CashPaymentObject))
    {
      CashPaymentObject localCashPaymentObject = (CashPaymentObject)paramPaymentModeObject;
      setAmount(localCashPaymentObject.getAmount());
    }
  }
  
  public void getValuesInObject(PaymentModeObject paramPaymentModeObject)
  {
    if ((paramPaymentModeObject instanceof CashPaymentObject))
    {
      CashPaymentObject localCashPaymentObject = (CashPaymentObject)paramPaymentModeObject;
      localCashPaymentObject._mAmount = getAmount();
    }
    else
    {
      System.err.println("Invalid Object passed to CashPaymentPanel.");
    }
  }
  
  public void clearFields()
  {
    this._mAmount.setText("");
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.billing.CashPaymentOptionPanel
 * JD-Core Version:    0.7.0.1
 */