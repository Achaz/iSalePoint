package dm.jb.ui.billing;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import dm.jb.op.bill.Bill;
import dm.jb.ui.MainWindow;
import dm.jb.ui.common.JBCustomShadowPanel;
import dm.jb.ui.common.JBTransparentWindow;
import dm.jb.ui.common.NumericVirtualKeyPad;
import dm.jb.ui.res.MessageResourceUtils;
import dm.jb.ui.res.ResourceUtils;
import dm.tools.ui.UICommon;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.graphics.GraphicsUtilities;
import org.jdesktop.swingx.graphics.ShadowRenderer;

public class BillingButtonPanel
  extends JXPanel
{
  private static BillingButtonPanel _mInstance = null;
  BufferedImage shadow = null;
  private JButton _mRestorebutton = null;
  private Color _mPanelBackgroundColor = new Color(87, 147, 191, 220);
  private SavedBillButtonWindow _mSavedBillWindowInstance = null;
  
  private BillingButtonPanel()
  {
    initUI();
    InputMap localInputMap = getInputMap(2);
    String str = "F7Action";
    localInputMap.put(KeyStroke.getKeyStroke("F7"), str);
    Object localObject = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        BillingButtonPanel.this.openNewBillWindow();
      }
    };
    getActionMap().put(str, (Action)localObject);
    str = "F8Action";
    localInputMap.put(KeyStroke.getKeyStroke("F8"), str);
    localObject = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        BillingButtonPanel.this.viewBillClicked();
      }
    };
    getActionMap().put(str, (Action)localObject);
    str = "F9Action";
    localInputMap.put(KeyStroke.getKeyStroke("F9"), str);
    localObject = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        BillingButtonPanel.this.holdCurrentBill();
      }
    };
    getActionMap().put(str, (Action)localObject);
    str = "F10Action";
    localInputMap.put(KeyStroke.getKeyStroke("F10"), str);
    localObject = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        BillingButtonPanel.this.restoreSavedBill();
      }
    };
    getActionMap().put(str, (Action)localObject);
    str = "F2Action";
    localInputMap.put(KeyStroke.getKeyStroke("F2"), str);
    localObject = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        BillingButtonPanel.this.checkStock();
      }
    };
    getActionMap().put(str, (Action)localObject);
    setOpaque(false);
    addMouseListener(new MouseListener()
    {
      public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
      {
        NumericVirtualKeyPad.getInstance().setVisible(false);
      }
      
      public void mousePressed(MouseEvent paramAnonymousMouseEvent) {}
      
      public void mouseReleased(MouseEvent paramAnonymousMouseEvent) {}
      
      public void mouseEntered(MouseEvent paramAnonymousMouseEvent) {}
      
      public void mouseExited(MouseEvent paramAnonymousMouseEvent) {}
    });
  }
  
  public static BillingButtonPanel getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new BillingButtonPanel();
    }
    return _mInstance;
  }
  
  protected void paintComponent(Graphics paramGraphics)
  {
    int i = 14;
    int j = 14;
    int k = getWidth() - 28;
    int m = getHeight() - 28;
    int n = 30;
    Graphics2D localGraphics2D = (Graphics2D)paramGraphics.create();
    localGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    if (this.shadow != null)
    {
      int i1 = (this.shadow.getWidth() - k) / 2;
      int i2 = (this.shadow.getHeight() - m) / 2;
      localGraphics2D.drawImage(this.shadow, i - i1, j - i2, null);
    }
    localGraphics2D.setColor(this._mPanelBackgroundColor);
    localGraphics2D.fillRoundRect(i, j, k, m, n, n);
    localGraphics2D.setStroke(new BasicStroke(3.0F));
    localGraphics2D.setColor(Color.WHITE);
    localGraphics2D.drawRoundRect(i, j, k, m, n, n);
    localGraphics2D.dispose();
  }
  
  public void setBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.setBounds(paramInt1, paramInt2, paramInt3, paramInt4);
    int i = getWidth() - 28;
    int j = getHeight() - 28;
    int k = 30;
    int m = 14;
    this.shadow = GraphicsUtilities.createCompatibleTranslucentImage(i, j);
    Graphics2D localGraphics2D = this.shadow.createGraphics();
    localGraphics2D.setColor(Color.WHITE);
    localGraphics2D.fillRoundRect(0, 0, i, j, k, k);
    localGraphics2D.dispose();
    ShadowRenderer localShadowRenderer = new ShadowRenderer(m, 0.5F, Color.BLACK);
    this.shadow = localShadowRenderer.createShadow(this.shadow);
    localGraphics2D = this.shadow.createGraphics();
    localGraphics2D.setColor(Color.RED);
    localGraphics2D.setComposite(AlphaComposite.Clear);
    localGraphics2D.fillRoundRect(m, m, i, j, k, k);
    localGraphics2D.dispose();
  }
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("30px,pref:grow,30px", "30px,40px,10px,40px,10px,40px,10px,40px,10px,40px");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(2, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    JXButton localJXButton = new JXButton(ResourceUtils.getString("POS_CHECK_STOCK_BTN"));
    add(localJXButton, localCellConstraints);
    localJXButton.setBackground(this._mPanelBackgroundColor);
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        BillingButtonPanel.this.checkStock();
      }
    });
    localJXButton = new JXButton(ResourceUtils.getString("POS_NEW_SALE_BTN"));
    localCellConstraints.xywh(2, 4, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJXButton, localCellConstraints);
    localJXButton.setBackground(this._mPanelBackgroundColor);
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        BillingButtonPanel.this.openNewBillWindow();
      }
    });
    localJXButton = new JXButton(ResourceUtils.getString("POS_VIEW_SALE_BTN"));
    localCellConstraints.xywh(2, 6, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJXButton, localCellConstraints);
    localJXButton.setBackground(this._mPanelBackgroundColor);
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        BillingButtonPanel.this.viewBillClicked();
      }
    });
    localJXButton = new JXButton(ResourceUtils.getString("POS_HOLD_SALE_BTN"));
    localCellConstraints.xywh(2, 8, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJXButton, localCellConstraints);
    localJXButton.setBackground(this._mPanelBackgroundColor);
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        BillingButtonPanel.this.holdCurrentBill();
      }
    });
    localJXButton = new JXButton(ResourceUtils.getString("POS_RESTORE_SALE_BTN"));
    localCellConstraints.xywh(2, 10, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJXButton, localCellConstraints);
    localJXButton.setBackground(this._mPanelBackgroundColor);
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        BillingButtonPanel.this.restoreSavedBill();
      }
    });
    this._mRestorebutton = localJXButton;
  }
  
  private void openNewBillWindow()
  {
    BillingPanel localBillingPanel = BillingPanel.getBillingPanel();
    localBillingPanel.prepareForNewBill();
    localBillingPanel.setToUpdateMode(false);
    localBillingPanel.setDefaultFocus();
  }
  
  public void viewBillClicked()
  {
    ViewBillNoDialog.getInstance().setVisible(true);
  }
  
  public void holdCurrentBill()
  {
    BillingPanel.getBillingPanel().saveCurrentBill();
    openNewBillWindow();
  }
  
  public void restoreSavedBill()
  {
    if (Bill.getSavedBills().size() == 0)
    {
      UICommon.showError(MessageResourceUtils.getString("POS_NO_HOLD"), MessageResourceUtils.getString("ERROR_TITLE"), BillingLauncher.getInstance());
      return;
    }
    if (this._mSavedBillWindowInstance == null) {
      this._mSavedBillWindowInstance = new SavedBillButtonWindow();
    }
    this._mSavedBillWindowInstance.initButtons();
    this._mSavedBillWindowInstance.pack();
    Point localPoint = this._mRestorebutton.getLocationOnScreen();
    localPoint.x += this._mRestorebutton.getWidth();
    this._mSavedBillWindowInstance.setLocation(localPoint);
    this._mSavedBillWindowInstance.setVisible(true);
  }
  
  private void checkStock()
  {
    StockCheckWindow.INSTANCE.resetFields();
    StockCheckWindow.INSTANCE.setVisible(true);
  }
  
  public class SavedBillButtonWindow
    extends JBTransparentWindow
    implements WindowListener
  {
    private JXButton[] _mRestoreButtons = new JXButton[10];
    private JBCustomShadowPanel _mButtonPanel = null;
    private int[] buttonMnemonics = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57 };
    
    public SavedBillButtonWindow()
    {
      super(false);
      initThisUI();
      addWindowListener(this);
      InputMap localInputMap = ((JPanel)getContentPane()).getInputMap(2);
      String str = "ESCAction";
      localInputMap.put(KeyStroke.getKeyStroke("ESCAPE"), str);
      AbstractAction local1 = new AbstractAction()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          BillingButtonPanel.SavedBillButtonWindow.this.setVisible(false);
        }
      };
      ((JPanel)getContentPane()).getActionMap().put(str, local1);
    }
    
    private void initThisUI()
    {
      JPanel localJPanel = (JPanel)getContentPane();
      JBCustomShadowPanel localJBCustomShadowPanel = new JBCustomShadowPanel(null, 10);
      this._mButtonPanel = ((JBCustomShadowPanel)localJBCustomShadowPanel);
      localJPanel.setLayout(new BorderLayout());
      localJPanel.add(localJBCustomShadowPanel, "Center");
      FormLayout localFormLayout = new FormLayout("8px,120px,8px", "8px");
      localJBCustomShadowPanel.setLayout(localFormLayout);
      InputMap localInputMap = localJPanel.getInputMap(2);
      for (int i = 0; i < 10; i++)
      {
        String str = "" + i + "Action";
        localInputMap.put(KeyStroke.getKeyStroke("" + i), str);
        final int j = i;
        AbstractAction local2 = new AbstractAction()
        {
          public void actionPerformed(ActionEvent paramAnonymousActionEvent)
          {
            BillingButtonPanel.SavedBillButtonWindow.this.restoreClicked(j);
          }
        };
        localJPanel.getActionMap().put(str, local2);
      }
    }
    
    private void initButtons()
    {
      int i = Bill.getSizeOfSavedBills();
      FormLayout localFormLayout = (FormLayout)this._mButtonPanel.getLayout();
      int j = localFormLayout.getRowCount();
      for (int k = 2; k <= j; k++)
      {
        if (k % 2 == 0) {
          this._mButtonPanel.remove(this._mRestoreButtons[(k / 2 - 1)]);
        }
        localFormLayout.removeRow(2);
      }
      for (k = 0; k < i; k++) {
        if (this._mRestoreButtons[k] == null)
        {
          this._mRestoreButtons[k] = new JXButton("" + k);
          this._mRestoreButtons[k].setMnemonic(this.buttonMnemonics[k]);
          final int m = k;
          this._mRestoreButtons[k].addActionListener(new ActionListener()
          {
            public void actionPerformed(ActionEvent paramAnonymousActionEvent)
            {
              BillingButtonPanel.SavedBillButtonWindow.this.restoreClicked(m);
            }
          });
        }
      }
      for (k = 0; k < i; k++)
      {
        localFormLayout.appendRow(new RowSpec("30px"));
        localFormLayout.appendRow(new RowSpec("8px"));
      }
      ArrayList localArrayList = Bill.getSavedBills();
      CellConstraints localCellConstraints = new CellConstraints();
      for (int n = 1; n <= i; n++)
      {
        int i1 = n - 1;
        localCellConstraints.xywh(2, n * 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
        this._mRestoreButtons[i1].setText("" + i1 + " ( " + ((Bill)localArrayList.get(i1)).getEntriesCount() + " )");
        this._mButtonPanel.add(this._mRestoreButtons[i1], localCellConstraints);
      }
    }
    
    public void windowOpened(WindowEvent paramWindowEvent) {}
    
    public void windowClosing(WindowEvent paramWindowEvent) {}
    
    public void windowClosed(WindowEvent paramWindowEvent) {}
    
    public void windowIconified(WindowEvent paramWindowEvent) {}
    
    public void windowDeiconified(WindowEvent paramWindowEvent) {}
    
    public void windowActivated(WindowEvent paramWindowEvent) {}
    
    public void windowDeactivated(WindowEvent paramWindowEvent)
    {
      setVisible(false);
    }
    
    private void restoreClicked(int paramInt)
    {
      Bill localBill = Bill.getSavedBill(paramInt);
      Bill.getSavedBills().remove(localBill);
      BillingPanel.getBillingPanel().prepareForNewBill();
      BillingPanel.getBillingPanel().loadSavedBill(localBill);
      setVisible(false);
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.billing.BillingButtonPanel
 * JD-Core Version:    0.7.0.1
 */