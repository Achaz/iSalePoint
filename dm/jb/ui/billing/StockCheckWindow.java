package dm.jb.ui.billing;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.db.objects.ProductRow;
import dm.jb.db.objects.ProductTableDef;
import dm.jb.db.objects.StoreInfoRow;
import dm.jb.db.objects.StoreInfoTableDef;
import dm.jb.db.objects.StoreStockRow;
import dm.jb.db.objects.StoreStockTableDef;
import dm.jb.messages.JbMessageLoader;
import dm.jb.ui.common.JBShadowPanel;
import dm.jb.ui.common.JBTransparentWindow;
import dm.tools.db.DBException;
import dm.tools.messages.ButtonLabels_base;
import dm.tools.types.InternalQuantity;
import dm.tools.ui.UICommon;
import dm.tools.ui.components.JBSearchButton;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import org.jdesktop.swingx.JXButton;

public class StockCheckWindow
  extends JBTransparentWindow
{
  public static StockCheckWindow INSTANCE = new StockCheckWindow();
  private JBShadowPanel _mShadowPanel = null;
  private JTextField _mProductCode = null;
  private JTextField _mProductName = null;
  private JTextField _mStock = null;
  private JLabel _mStockLbl = null;
  
  private StockCheckWindow()
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
        StockCheckWindow.this.closeClicked();
      }
    };
    ((JPanel)getContentPane()).getActionMap().put(str, local1);
  }
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("30px,120px,10px,200px,4px,60px,100px,4px, 60px,30px", "30px,32px,10px,32px,10px,32px,20px,35px,30px");
    JPanel localJPanel = (JPanel)getContentPane();
    localJPanel.setLayout(new BorderLayout());
    this._mShadowPanel = new JBShadowPanel(new Color(255, 187, 119));
    JBShadowPanel localJBShadowPanel = this._mShadowPanel;
    localJPanel.add(localJBShadowPanel, "Center");
    localJBShadowPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    JLabel localJLabel = new JLabel("Code :");
    Font localFont1 = localJLabel.getFont();
    Font localFont2 = new Font(localFont1.getName(), 1, 14);
    localJLabel.setFont(localFont2);
    localCellConstraints.xywh(2, 2, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJBShadowPanel.add(localJLabel, localCellConstraints);
    this._mProductCode = new JTextField();
    localCellConstraints.xywh(4, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJBShadowPanel.add(this._mProductCode, localCellConstraints);
    Font localFont3 = this._mProductCode.getFont();
    Font localFont4 = new Font(localFont3.getName(), 1, 14);
    this._mProductCode.setFont(localFont4);
    this._mProductCode.addKeyListener(new KeyListener()
    {
      public void keyTyped(KeyEvent paramAnonymousKeyEvent) {}
      
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 114) {
          StockCheckWindow.this.searchByProductIcClicked();
        }
      }
      
      public void keyReleased(KeyEvent paramAnonymousKeyEvent) {}
    });
    JBSearchButton localJBSearchButton = new JBSearchButton(true);
    localCellConstraints.xywh(6, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJBShadowPanel.add(localJBSearchButton, localCellConstraints);
    localJBSearchButton.setBackground(this._mShadowPanel.getBackground());
    localJBSearchButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        StockCheckWindow.this.searchByProductIcClicked();
      }
    });
    localJLabel = new JLabel("Product  :");
    localJLabel.setFont(localFont2);
    localCellConstraints.xywh(2, 4, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJBShadowPanel.add(localJLabel, localCellConstraints);
    localJBShadowPanel.setBackground(this._mShadowPanel.getBackground());
    this._mProductName = new JTextField();
    localCellConstraints.xywh(4, 4, 4, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJBShadowPanel.add(this._mProductName, localCellConstraints);
    this._mProductName.setFont(localFont4);
    this._mProductName.addKeyListener(new KeyListener()
    {
      public void keyTyped(KeyEvent paramAnonymousKeyEvent) {}
      
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 114) {
          StockCheckWindow.this.searchByProductName();
        }
      }
      
      public void keyReleased(KeyEvent paramAnonymousKeyEvent) {}
    });
    localJBSearchButton = new JBSearchButton(true);
    localCellConstraints.xywh(9, 4, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJBShadowPanel.add(localJBSearchButton, localCellConstraints);
    localJBSearchButton.setBackground(this._mShadowPanel.getBackground());
    localJBSearchButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        StockCheckWindow.this.searchByProductName();
      }
    });
    localJLabel = new JLabel("Stock  :");
    localJLabel.setFont(localFont2);
    localCellConstraints.xywh(2, 6, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJBShadowPanel.add(localJLabel, localCellConstraints);
    localJBShadowPanel.setBackground(this._mShadowPanel.getBackground());
    this._mStock = new JTextField();
    localCellConstraints.xywh(4, 6, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJBShadowPanel.add(this._mStock, localCellConstraints);
    this._mStock.setFont(localFont4);
    this._mStock.setEditable(false);
    localCellConstraints.xywh(6, 6, 3, 1, CellConstraints.LEFT, CellConstraints.CENTER);
    this._mStockLbl = new JLabel();
    this._mStockLbl.setFont(localFont2);
    localJBShadowPanel.add(this._mStockLbl, localCellConstraints);
    localCellConstraints.xywh(2, 7, localFormLayout.getColumnCount() - 2, 1, CellConstraints.FILL, CellConstraints.CENTER);
    localJBShadowPanel.add(new JSeparator(), localCellConstraints);
    localCellConstraints.xywh(1, 8, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.FILL);
    localJBShadowPanel.add(getButtonPanel(), localCellConstraints);
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
    JXButton localJXButton = new JXButton(localButtonLabels_base.getMessage(135172));
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        StockCheckWindow.this.closeClicked();
      }
    });
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.setBackground(this._mShadowPanel.getBackground());
    Font localFont = localJXButton.getFont();
    localFont = new Font(localFont.getName(), localFont.getStyle() | 0x1, 14);
    localJXButton.setFont(localFont);
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJXButton = new JXButton("Reset");
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.setFont(localFont);
    localJXButton.setBackground(this._mShadowPanel.getBackground());
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        StockCheckWindow.this.resetFields();
      }
    });
    localCellConstraints.xywh(6, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJXButton = new JXButton(localButtonLabels_base.getMessage(135171));
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.setFont(localFont);
    localJXButton.setBackground(this._mShadowPanel.getBackground());
    return localJPanel;
  }
  
  void resetFields()
  {
    this._mProductCode.setText("");
    this._mProductName.setText("");
    this._mStock.setText("");
    this._mStockLbl.setText("");
    this._mProductCode.requestFocusInWindow();
  }
  
  private void closeClicked()
  {
    setVisible(false);
  }
  
  private void searchByProductIcClicked()
  {
    String str = this._mProductCode.getText().trim();
    if (str.length() == 0)
    {
      UICommon.showError("Product Code cannot be empty.\nIf you want are looking for search, enter the appropriate wild card(eg., '*'), and press enter.", "Error", BillingLauncher.getInstance());
      this._mProductCode.requestFocusInWindow();
      return;
    }
    try
    {
      ProductRow localProductRow = ProductTableDef.getInstance().getProductByCode(str);
      if (localProductRow == null)
      {
        UICommon.showError("Invalid Product Code or no stock available.", "Error", BillingLauncher.getInstance());
        this._mProductCode.requestFocusInWindow();
        return;
      }
      this._mProductName.setText(localProductRow.getProdName());
      StoreStockRow localStoreStockRow = StoreStockTableDef.getInstance().getStockForProductInCurrentStore(localProductRow.getProdIndex());
      if (localStoreStockRow == null)
      {
        this._mStock.setText("0");
        this._mStock.setText("0");
      }
      else
      {
        this._mStock.setText(InternalQuantity.toString(localStoreStockRow.getStock(), (short)localProductRow.getProdUnit(), false));
      }
      this._mStockLbl.setText("  " + InternalQuantity.quantityUnits[localProductRow.getProdUnit()]);
    }
    catch (DBException localDBException)
    {
      UICommon.showError("Internal error reading product details.\nCheck the input values. If the problem persis contact administrator", "Internal error", BillingLauncher.getInstance());
      return;
    }
  }
  
  private void searchByProductName()
  {
    ProductSearchWithoutStockDialog localProductSearchWithoutStockDialog = ProductSearchWithoutStockDialog.getInstance();
    localProductSearchWithoutStockDialog.setProdName(this._mProductName.getText().trim() + "%");
    int i = StoreInfoTableDef.getCurrentStore().getStoreId();
    ProductRow localProductRow = localProductSearchWithoutStockDialog.searchAndShow(this._mProductName.getText().trim() + "%", i);
    if (localProductRow == null) {
      return;
    }
    this._mProductCode.setText("" + localProductRow.getProductCode());
    this._mProductName.setText("" + localProductRow.getProdName());
    try
    {
      StoreStockRow localStoreStockRow = StoreStockTableDef.getInstance().getStockForProductInCurrentStore(localProductRow.getProdIndex());
      if (localStoreStockRow == null) {
        this._mStock.setText("0");
      } else {
        this._mStock.setText(InternalQuantity.toString(localStoreStockRow.getStock(), (short)localProductRow.getProdUnit(), false));
      }
      this._mStockLbl.setText("  " + InternalQuantity.quantityUnits[localProductRow.getProdUnit()]);
    }
    catch (DBException localDBException)
    {
      UICommon.showError("Internal error reading product details.\nCheck the input values. If the problem persis contact administrator", "Internal error", BillingLauncher.getInstance());
      return;
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.billing.StockCheckWindow
 * JD-Core Version:    0.7.0.1
 */