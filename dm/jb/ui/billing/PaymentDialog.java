package dm.jb.ui.billing;

import dm.jb.ui.common.JBTransparentWindow;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class PaymentDialog
  extends JBTransparentWindow
{
  private static PaymentDialog _mInstance = null;
  
  private PaymentDialog()
  {
    super(BillingLauncher.getInstance());
    _mInstance = this;
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
        PaymentDialog.this.closeClicked();
      }
    };
    ((JPanel)getContentPane()).getActionMap().put(str, local1);
  }
  
  public static PaymentDialog getDialogInstance()
  {
    return _mInstance;
  }
  
  public static PaymentDialog getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new PaymentDialog();
    }
    return _mInstance;
  }
  
  private void initUI()
  {
    JPanel localJPanel = (JPanel)getContentPane();
    localJPanel.setLayout(new BorderLayout());
    localJPanel.add(PaymentPanel.getInstance(), "Center");
  }
  
  public void setStartIndex()
  {
    PaymentPanel.getInstance().setStartIndex();
  }
  
  private void closeClicked()
  {
    PaymentPanel.getInstance()._mIsCancelled = true;
    setVisible(false);
  }
  
  void clearAllFields()
  {
    PaymentPanel.getInstance().clearAllFields();
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.billing.PaymentDialog
 * JD-Core Version:    0.7.0.1
 */