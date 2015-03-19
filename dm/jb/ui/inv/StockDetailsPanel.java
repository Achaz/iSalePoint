package dm.jb.ui.inv;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.db.objects.StockAndProductRow;
import dm.jb.ui.MainWindow;
import dm.tools.db.DBException;
import dm.tools.types.InternalQuantity;
import dm.tools.ui.AbstractMainPanel;
import dm.tools.ui.ActionPanel;
import dm.tools.ui.UICommon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class StockDetailsPanel
  extends AbstractMainPanel
{
  private static StockDetailsPanel _mInstance = null;
  private CurrentStockTable _mStockTable = null;
  private JTextField _mProductId = null;
  private JTextField _mProductName = null;
  private JTextField _mCurrentStock = null;
  private StockAndProductRow _mCurrentProduct = null;
  private JButton _mDetailsButton = null;
  private ArrayList<StockInfoAndCurrentStock> _mCurrentStocks = null;
  
  public static StockDetailsPanel getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new StockDetailsPanel();
    }
    return _mInstance;
  }
  
  private StockDetailsPanel()
  {
    initUI();
  }
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("10px, 500px,20px,pref,10px", "10px,pref,20px, 350px,20px,30px,10px");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(2, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getProductDetailsPanel(), localCellConstraints);
    this._mStockTable = new CurrentStockTable();
    JScrollPane localJScrollPane = new JScrollPane(this._mStockTable);
    localCellConstraints.xywh(2, 4, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJScrollPane, localCellConstraints);
    this._mStockTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent paramAnonymousListSelectionEvent)
      {
        int[] arrayOfInt = StockDetailsPanel.this._mStockTable.getSelectedRows();
        if (arrayOfInt.length == 1) {
          StockDetailsPanel.this._mDetailsButton.setEnabled(true);
        } else {
          StockDetailsPanel.this._mDetailsButton.setEnabled(false);
        }
      }
    });
    this._mStockTable.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
      {
        if (paramAnonymousMouseEvent.getClickCount() == 2) {
          StockDetailsPanel.this.detailsClicked();
        }
      }
    });
    localCellConstraints.xywh(1, 5, 5, 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(new JSeparator(), localCellConstraints);
    localCellConstraints.xywh(1, 6, 5, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getButtonPanel(), localCellConstraints);
    localCellConstraints.xywh(4, 4, 1, 1, CellConstraints.FILL, CellConstraints.TOP);
    add(getActionButtonPanel(), localCellConstraints);
  }
  
  private JPanel getActionButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    localJPanel.setBackground(getBackground());
    FormLayout localFormLayout = new FormLayout("100px", "30px, 40px,30px,20px, 30px");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    this._mDetailsButton = new JButton("Details");
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mDetailsButton, localCellConstraints);
    this._mDetailsButton.setEnabled(false);
    this._mDetailsButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        StockDetailsPanel.this.detailsClicked();
      }
    });
    JButton localJButton = new JButton("Assign Stock");
    localCellConstraints.xywh(1, 3, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJButton, localCellConstraints);
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        StockDetailsPanel.this.assignClicked();
      }
    });
    localJButton = new JButton("Edit Stock");
    localCellConstraints.xywh(1, 5, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJButton, localCellConstraints);
    return localJPanel;
  }
  
  public JPanel getPanel()
  {
    return this;
  }
  
  public void setDefaultFocus() {}
  
  public void windowDisplayed() {}
  
  private JPanel getProductDetailsPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("90px, 10px,120px,100px", "23px,10px,23px,10px,23px");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(new JLabel("Product Code : "), localCellConstraints);
    this._mProductId = new JTextField();
    localCellConstraints.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mProductId, localCellConstraints);
    this._mProductId.setEditable(false);
    localCellConstraints.xywh(1, 3, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(new JLabel("Product Name : "), localCellConstraints);
    this._mProductName = new JTextField();
    localCellConstraints.xywh(3, 3, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mProductName, localCellConstraints);
    this._mProductName.setEditable(false);
    localCellConstraints.xywh(1, 5, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(new JLabel("Sales Stock : "), localCellConstraints);
    this._mCurrentStock = new JTextField();
    localCellConstraints.xywh(3, 5, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mCurrentStock, localCellConstraints);
    this._mCurrentStock.setEditable(false);
    return localJPanel;
  }
  
  public void setStockRows(StockAndProductRow paramStockAndProductRow)
  {
    ArrayList localArrayList1 = null;
    try
    {
      localArrayList1 = paramStockAndProductRow.getCurrentStocks();
    }
    catch (DBException localDBException1)
    {
      UICommon.showError("Internal error reading warehouse stock information. Try again later.\n\nIf the problem persists contact administrator.", "Internal Error", MainWindow.instance);
      return;
    }
    this._mStockTable.clearSelection();
    this._mStockTable.removeAllRows();
    StockAndProductRow localStockAndProductRow = paramStockAndProductRow;
    this._mCurrentProduct = localStockAndProductRow;
    this._mProductId.setText(localStockAndProductRow.getProductCode());
    this._mProductName.setText(localStockAndProductRow.getProdName());
    InternalQuantity localInternalQuantity = new InternalQuantity(localStockAndProductRow.getStock(), localStockAndProductRow.getProdUnit(), true);
    this._mCurrentStock.setText(localInternalQuantity.toString());
    if (localArrayList1 == null) {
      return;
    }
    ArrayList localArrayList2 = null;
    try
    {
      localArrayList2 = StockInfoAndCurrentStock.getStockInfoAndCurrentStocksForCurrentStocks(localArrayList1);
      this._mCurrentStocks = localArrayList2;
    }
    catch (DBException localDBException2)
    {
      UICommon.showError("Internal error reading stock information.", "Internal Error", MainWindow.instance);
      return;
    }
    Iterator localIterator = localArrayList2.iterator();
    while (localIterator.hasNext()) {
      this._mStockTable.addStock((StockInfoAndCurrentStock)localIterator.next());
    }
  }
  
  private JPanel getButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("10px,100px,pref:grow,100px, pref:grow, 100px, 10px", "30px");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    JButton localJButton = new JButton("Close");
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJButton, localCellConstraints);
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        StockDetailsPanel.this.closeWindow();
      }
    });
    localJButton = new JButton("Help");
    localCellConstraints.xywh(6, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJButton, localCellConstraints);
    localJPanel.setBackground(getBackground());
    return localJPanel;
  }
  
  public String getTitle()
  {
    return "Stock details for selected product";
  }
  
  private void assignClicked()
  {
    AllotStockPanel localAllotStockPanel = AllotStockPanel.getAssignStockPanel();
    ActionPanel localActionPanel = MainWindow.getActionPanel();
    localActionPanel.setVisible(true);
    localActionPanel.pushObject(localAllotStockPanel);
    localAllotStockPanel.setActionPanel(localActionPanel);
    localActionPanel.setTitle("Allot Stock");
    MainWindow.instance.setVisible(true);
    localAllotStockPanel.setStocks(this._mCurrentStocks);
    localAllotStockPanel.windowDisplayed();
    localAllotStockPanel.setDefaultFocus();
  }
  
  private void detailsClicked()
  {
    SingleStockDetailPanel localSingleStockDetailPanel = SingleStockDetailPanel.getInstance();
    ActionPanel localActionPanel = MainWindow.getActionPanel();
    localActionPanel.setVisible(true);
    localActionPanel.pushObject(localSingleStockDetailPanel);
    localSingleStockDetailPanel.setActionPanel(localActionPanel);
    localActionPanel.setTitle("Stock Details");
    MainWindow.instance.setVisible(true);
    localSingleStockDetailPanel.windowDisplayed();
    localSingleStockDetailPanel.setDefaultFocus();
    int[] arrayOfInt = this._mStockTable.getSelectedRows();
    int i = this._mStockTable.convertRowIndexToModel(arrayOfInt[0]);
    StockInfoAndCurrentStock localStockInfoAndCurrentStock = (StockInfoAndCurrentStock)this._mCurrentStocks.get(i);
    localSingleStockDetailPanel.setInfo(this._mCurrentProduct, localStockInfoAndCurrentStock);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.inv.StockDetailsPanel
 * JD-Core Version:    0.7.0.1
 */