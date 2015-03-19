package dm.jb.ui.billing;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.messages.JbMessageLoader;
import dm.jb.op.bill.Bill;
import dm.jb.ui.common.JBShadowPanel;
import dm.jb.ui.common.JBTransparentWindow;
import dm.tools.db.DBException;
import dm.tools.messages.ButtonLabels_base;
import dm.tools.ui.UICommon;
import dm.tools.utils.Validation;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import org.jdesktop.swingx.JXButton;

public class ViewBillNoDialog
  extends JBTransparentWindow
{
  private static ViewBillNoDialog _mInstance = null;
  private JTextField _mBillNo = null;
  private JXButton _mOkBtn = null;
  private JBShadowPanel _mShadowPanel = null;
  
  private ViewBillNoDialog()
  {
    super(BillingLauncher.getInstance());
    initUI();
    pack();
    setLocationRelativeTo(BillingLauncher.getInstance());
    addWindowListener(new WindowAdapter()
    {
      public void windowActivated(WindowEvent paramAnonymousWindowEvent)
      {
        ViewBillNoDialog.this._mBillNo.requestFocusInWindow();
      }
    });
    InputMap localInputMap = ((JPanel)getContentPane()).getInputMap(2);
    String str = "ESCAction";
    localInputMap.put(KeyStroke.getKeyStroke("ESCAPE"), str);
    AbstractAction local2 = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ViewBillNoDialog.this.closeClicked();
      }
    };
    ((JPanel)getContentPane()).getActionMap().put(str, local2);
  }
  
  public void setVisible(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      getRootPane().setDefaultButton(this._mOkBtn);
      this._mBillNo.setText("");
    }
    super.setVisible(paramBoolean);
  }
  
  public static ViewBillNoDialog getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new ViewBillNoDialog();
    }
    return _mInstance;
  }
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("30px,120px,10px,250px,pref:grow,30px", "30px,28px,20px,35px,30px");
    JPanel localJPanel1 = (JPanel)getContentPane();
    localJPanel1.setLayout(new BorderLayout());
    this._mShadowPanel = new JBShadowPanel(true);
    JBShadowPanel localJBShadowPanel = this._mShadowPanel;
    localJPanel1.add(localJBShadowPanel, "Center");
    localJBShadowPanel.setLayout(localFormLayout);
    this._mBillNo = new JTextField();
    CellConstraints localCellConstraints = new CellConstraints();
    JLabel localJLabel = new JLabel("Transaction No.:");
    Font localFont1 = localJLabel.getFont();
    Font localFont2 = new Font(localFont1.getName(), 1, 14);
    localJLabel.setFont(localFont2);
    localCellConstraints.xywh(2, 2, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJBShadowPanel.add(localJLabel, localCellConstraints);
    localCellConstraints.xywh(4, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJBShadowPanel.add(this._mBillNo, localCellConstraints);
    Font localFont3 = this._mBillNo.getFont();
    Font localFont4 = new Font(localFont3.getName(), 1, 14);
    this._mBillNo.setFont(localFont4);
    JPanel localJPanel2 = getButtonPanel();
    localCellConstraints.xywh(1, 4, 6, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJBShadowPanel.add(localJPanel2, localCellConstraints);
  }
  
  private JPanel getButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("30px,120px,10px:grow,120px,10px:grow,120px,30px", "35px");
    localJPanel.setLayout(localFormLayout);
    localJPanel.setOpaque(false);
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    JbMessageLoader localJbMessageLoader = (JbMessageLoader)UICommon.getMessageLoader();
    ButtonLabels_base localButtonLabels_base = localJbMessageLoader.getButtonLabelsMessages();
    JXButton localJXButton = new JXButton(localButtonLabels_base.getMessage(135175));
    this._mOkBtn = localJXButton;
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ViewBillNoDialog.this.okClicked();
      }
    });
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.setBackground(this._mShadowPanel.getBackground());
    Font localFont = localJXButton.getFont();
    localFont = new Font(localFont.getName(), localFont.getStyle() | 0x1, 14);
    localJXButton.setFont(localFont);
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJXButton = new JXButton(localButtonLabels_base.getMessage(135172));
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.setFont(localFont);
    localJXButton.setBackground(this._mShadowPanel.getBackground());
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ViewBillNoDialog.this.closeClicked();
      }
    });
    localCellConstraints.xywh(6, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJXButton = new JXButton(localButtonLabels_base.getMessage(135171));
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.setFont(localFont);
    localJXButton.setBackground(this._mShadowPanel.getBackground());
    return localJPanel;
  }
  
  private void okClicked()
  {
    String str = this._mBillNo.getText();
    if (str.length() == 0)
    {
      UICommon.showError("Bill number cannot be empty", "Error", BillingLauncher.getInstance());
      this._mBillNo.requestFocusInWindow();
      return;
    }
    if (!Validation.isValidInt(str, false))
    {
      UICommon.showError("Invalid bill number", "Error", BillingLauncher.getInstance());
      this._mBillNo.requestFocusInWindow();
      return;
    }
    int i = Integer.valueOf(str).intValue();
    Bill localBill = null;
    try
    {
      localBill = Bill.getBillForBillNoWithUpdate(i);
    }
    catch (DBException localDBException)
    {
      localDBException.printStackTrace();
      UICommon.showError("Internal error reading bill details.\n\ntry again later. If the problem persists contact administrator", "Internal Error", BillingLauncher.getInstance());
      this._mBillNo.requestFocusInWindow();
      return;
    }
    if (localBill == null)
    {
      UICommon.showError("Bill not found or not created in this store.", "Error", BillingLauncher.getInstance());
      SwingUtilities.invokeLater(new Runnable()
      {
        public void run()
        {
          ViewBillNoDialog.this._mBillNo.requestFocusInWindow();
        }
      });
      return;
    }
    closeClicked();
    BillingPanel.getBillingPanel().prepareForNewBill();
    BillingPanel.getBillingPanel().loadBill(localBill);
    BillingPanel.getBillingPanel().setToUpdateMode(true);
  }
  
  public void setDefaultFocus()
  {
    this._mBillNo.requestFocusInWindow();
  }
  
  private void closeClicked()
  {
    setVisible(false);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.billing.ViewBillNoDialog
 * JD-Core Version:    0.7.0.1
 */