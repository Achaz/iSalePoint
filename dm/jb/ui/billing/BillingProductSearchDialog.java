package dm.jb.ui.billing;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.db.objects.CategoryRow;
import dm.jb.db.objects.DeptRow;
import dm.jb.db.objects.StockAndProductRow;
import dm.jb.db.objects.StockAndProductTableDef;
import dm.jb.db.objects.StoreInfoRow;
import dm.jb.db.objects.StoreInfoTableDef;
import dm.jb.ui.MainWindow;
import dm.jb.ui.common.JBShadowPanel;
import dm.jb.ui.common.JBTransparentWindow;
import dm.tools.db.DBException;
import dm.tools.types.InternalAmount;
import dm.tools.types.InternalDate;
import dm.tools.types.InternalQuantity;
import dm.tools.ui.UICommon;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTable;

public class BillingProductSearchDialog
  extends JBTransparentWindow
{
  private static BillingProductSearchDialog _mInstance = null;
  private JTextField _mPIDFrom = null;
  private JTextField _mProdName = null;
  private JTextField _mCategory = null;
  private JTextField _mDepartment = null;
  private JCheckBox _mAppendResult = null;
  private ProductSearchTable _mSearchTable = null;
  private JButton _mOKButton = null;
  private StockAndProductRow _mSelectedRow = null;
  private int currentStore = -1;
  private JBShadowPanel _mShadowPanel = null;
  
  private BillingProductSearchDialog(JFrame paramJFrame)
  {
    super(paramJFrame);
    initUI();
    pack();
    setLocationRelativeTo(null);
    addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 27) {
          BillingProductSearchDialog.this.closeWindowClicked();
        }
      }
    });
    this._mSearchTable.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 27) {
          BillingProductSearchDialog.this.closeWindowClicked();
        }
      }
    });
    InputMap localInputMap = ((JPanel)getContentPane()).getInputMap(2);
    String str = "ESCAction";
    localInputMap.put(KeyStroke.getKeyStroke("ESCAPE"), str);
    AbstractAction local3 = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        BillingProductSearchDialog.this.closeWindowClicked();
      }
    };
    ((JPanel)getContentPane()).getActionMap().put(str, local3);
  }
  
  public static BillingProductSearchDialog getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new BillingProductSearchDialog(BillingLauncher.getInstance());
    }
    return _mInstance;
  }
  
  private void initUI()
  {
    JPanel localJPanel = (JPanel)getContentPane();
    localJPanel.setLayout(new BorderLayout());
    this._mShadowPanel = new JBShadowPanel(new Color(255, 187, 119));
    JBShadowPanel localJBShadowPanel = this._mShadowPanel;
    localJPanel.add(localJBShadowPanel, "Center");
    FormLayout localFormLayout = new FormLayout("30px,pref:grow,30px", "30px,pref,10px,pref:grow,30px,40px,30px");
    localJBShadowPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(2, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJBShadowPanel.add(getSearchPanel(), localCellConstraints);
    localCellConstraints.xywh(2, 4, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJBShadowPanel.add(getSearchTablePanel(), localCellConstraints);
    localCellConstraints.xywh(2, 5, 1, 1, CellConstraints.FILL, CellConstraints.CENTER);
    localJBShadowPanel.add(new JSeparator(), localCellConstraints);
    localCellConstraints.xywh(2, 6, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJBShadowPanel.add(getButtonPanel(), localCellConstraints);
  }
  
  private JPanel getSearchTablePanel()
  {
    JXPanel localJXPanel = new JXPanel();
    FormLayout localFormLayout = new FormLayout("10px,800px,10px", "250px:grow");
    localJXPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    this._mSearchTable = new ProductSearchTable();
    this._mSearchTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent paramAnonymousListSelectionEvent)
      {
        int i = BillingProductSearchDialog.this._mSearchTable.getSelectedRow();
        if (i != -1) {
          BillingProductSearchDialog.this._mOKButton.setEnabled(true);
        } else {
          BillingProductSearchDialog.this._mOKButton.setEnabled(false);
        }
      }
    });
    this._mSearchTable.getSelectionModel().setSelectionMode(0);
    this._mSearchTable.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
      {
        if (paramAnonymousMouseEvent.getClickCount() == 2) {
          BillingProductSearchDialog.this.okClicked();
        }
      }
    });
    this._mSearchTable.addKeyListener(new KeyAdapter()
    {
      public void keyTyped(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyChar() == '\n')
        {
          paramAnonymousKeyEvent.consume();
          BillingProductSearchDialog.this.okClicked();
        }
      }
    });
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJXPanel.add(new JScrollPane(this._mSearchTable), localCellConstraints);
    this._mSearchTable.setHorizontalScrollEnabled(true);
    this._mSearchTable.setColumnWidths();
    localJXPanel.setOpaque(false);
    return localJXPanel;
  }
  
  private JPanel getSearchPanel()
  {
    JXPanel localJXPanel = new JXPanel();
    FormLayout localFormLayout = null;
    localJXPanel.setOpaque(false);
    localFormLayout = new FormLayout("10px,180px,10px,200px,50px,200px,10px, pref,pref:grow", "10px, 30px,10px,30px,10px,30px,10px,30px");
    localJXPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    JLabel localJLabel = new JLabel("Product Code : ");
    localCellConstraints.xywh(2, 2, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJXPanel.add(localJLabel, localCellConstraints);
    Font localFont1 = localJLabel.getFont();
    localFont1 = new Font(localFont1.getName(), 1, 14);
    localJLabel.setFont(localFont1);
    this._mPIDFrom = new JTextField();
    localCellConstraints.xywh(4, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJXPanel.add(this._mPIDFrom, localCellConstraints);
    Font localFont2 = this._mPIDFrom.getFont();
    localFont2 = new Font(localFont1.getName(), 1, 14);
    this._mPIDFrom.setFont(localFont2);
    localJLabel = new JLabel("Product Name : ");
    localCellConstraints.xywh(2, 4, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJXPanel.add(localJLabel, localCellConstraints);
    localJLabel.setFont(localFont1);
    this._mProdName = new JTextField();
    localCellConstraints.xywh(4, 4, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJXPanel.add(this._mProdName, localCellConstraints);
    this._mProdName.setFont(localFont2);
    localJLabel = new JLabel("Category : ");
    localCellConstraints.xywh(2, 6, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJXPanel.add(localJLabel, localCellConstraints);
    this._mCategory = new JTextField();
    localCellConstraints.xywh(4, 6, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJXPanel.add(this._mCategory, localCellConstraints);
    localJLabel.setFont(localFont1);
    this._mCategory.setFont(localFont2);
    localJLabel = new JLabel("Department : ");
    localCellConstraints.xywh(2, 8, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJXPanel.add(localJLabel, localCellConstraints);
    this._mDepartment = new JTextField();
    localCellConstraints.xywh(4, 8, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJXPanel.add(this._mDepartment, localCellConstraints);
    localJLabel.setFont(localFont1);
    this._mDepartment.setFont(localFont2);
    localCellConstraints.xywh(9, 1, 1, 8, CellConstraints.CENTER, CellConstraints.BOTTOM);
    localJXPanel.add(getSearchButtonPanel(), localCellConstraints);
    return localJXPanel;
  }
  
  private JPanel getSearchButtonPanel()
  {
    JXPanel localJXPanel = new JXPanel();
    FormLayout localFormLayout = new FormLayout("10px,100px,10px,pref,10px", "10px,30px,10px,30px,10px,30px,10px");
    localJXPanel.setLayout(localFormLayout);
    localJXPanel.setOpaque(false);
    CellConstraints localCellConstraints = new CellConstraints();
    JXButton localJXButton = new JXButton("Search");
    localCellConstraints.xywh(2, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJXPanel.add(localJXButton, localCellConstraints);
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        BillingProductSearchDialog.this.searchClicked();
      }
    });
    localJXButton.setBackground(this._mShadowPanel.getBackground());
    this._mAppendResult = new JCheckBox("Append");
    localCellConstraints.xywh(4, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJXPanel.add(this._mAppendResult, localCellConstraints);
    this._mAppendResult.setOpaque(false);
    localJXButton = new JXButton("Reset");
    localCellConstraints.xywh(2, 4, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJXPanel.add(localJXButton, localCellConstraints);
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        BillingProductSearchDialog.this.resetClicked();
      }
    });
    localJXButton.setBackground(this._mShadowPanel.getBackground());
    localJXButton = new JXButton("Clear");
    localCellConstraints.xywh(2, 6, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        BillingProductSearchDialog.this.clearClicked();
      }
    });
    localJXPanel.add(localJXButton, localCellConstraints);
    localJXButton.setBackground(this._mShadowPanel.getBackground());
    return localJXPanel;
  }
  
  private JPanel getButtonPanel()
  {
    JXPanel localJXPanel = new JXPanel();
    localJXPanel.setOpaque(false);
    FormLayout localFormLayout = new FormLayout("10px,100px,pref:grow,100px,pref:grow,100px,10px", "30px");
    localJXPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    JXButton localJXButton = new JXButton("OK");
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJXPanel.add(localJXButton, localCellConstraints);
    this._mOKButton = localJXButton;
    this._mOKButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        BillingProductSearchDialog.this.okClicked();
      }
    });
    localJXButton.setBackground(this._mShadowPanel.getBackground());
    localJXButton = new JXButton("Close");
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJXPanel.add(localJXButton, localCellConstraints);
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        BillingProductSearchDialog.this.closeWindowClicked();
      }
    });
    localJXButton.setBackground(this._mShadowPanel.getBackground());
    localJXButton = new JXButton("Help");
    localCellConstraints.xywh(6, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJXPanel.add(localJXButton, localCellConstraints);
    localJXButton.setBackground(this._mShadowPanel.getBackground());
    return localJXPanel;
  }
  
  public void setProdName(String paramString)
  {
    this._mProdName.setText(paramString);
  }
  
  public void clear()
  {
    clearClicked();
  }
  
  public void search()
  {
    searchClicked();
  }
  
  private void resetClicked()
  {
    this._mPIDFrom.setText("");
    this._mProdName.setText("");
    this._mCategory.setText("");
    this._mDepartment.setText("");
  }
  
  private void clearClicked()
  {
    this._mSearchTable.removeAllProducts();
    this._mSearchTable.updateUI();
  }
  
  public StockAndProductRow searchAndShow(String paramString, int paramInt)
  {
    ArrayList localArrayList = null;
    this.currentStore = paramInt;
    try
    {
      localArrayList = getRowsAfterSearchForProductName(paramString, "%", null, null, paramInt);
    }
    catch (DBException localDBException)
    {
      localDBException.printStackTrace();
      UICommon.showError("Internal error searching for products.\n\nCheck search parameters. If the problem persists contact administrator.\n\nHint : Do not use 'very' special characeters.", "Internal Error", MainWindow.instance);
      setVisible(true);
      this.currentStore = -1;
      return getSelectedProduct();
    }
    if ((localArrayList == null) || (localArrayList.size() == 0))
    {
      UICommon.showError("No record found.", "No data", MainWindow.instance);
      setVisible(true);
      this.currentStore = -1;
      return getSelectedProduct();
    }
    if (localArrayList.size() == 1)
    {
      this.currentStore = -1;
      return (StockAndProductRow)localArrayList.get(0);
    }
    addRowsToTable(localArrayList);
    setVisible(true);
    this.currentStore = -1;
    return getSelectedProduct();
  }
  
  private ArrayList<StockAndProductRow> getRowsAfterSearchForProductName(String paramString1, String paramString2, String paramString3, String paramString4, int paramInt)
    throws DBException
  {
    ArrayList localArrayList = null;
    localArrayList = StockAndProductTableDef.getInstance().searchStockAndProductByParams(paramString2, paramString3, paramString4, paramString1, paramInt);
    return localArrayList;
  }
  
  private void searchClicked()
  {
    if (!this._mAppendResult.isSelected())
    {
      this._mSearchTable.removeAllProducts();
      this._mSearchTable.updateUI();
    }
    String str = this._mPIDFrom.getText().trim();
    str = str + "%";
    ArrayList localArrayList = null;
    int i = StoreInfoTableDef.getCurrentStore().getStoreId();
    try
    {
      localArrayList = getRowsAfterSearchForProductName(this._mProdName.getText(), str, this._mDepartment.getText(), this._mCategory.getText(), i);
    }
    catch (DBException localDBException)
    {
      localDBException.printStackTrace();
      UICommon.showError("Internal error searching for products.\n\nCheck search parameters. If the problem persists contact administrator.\n\nHint : Do not use 'very' special characeters.", "Internal Error", MainWindow.instance);
      return;
    }
    if ((localArrayList == null) || (localArrayList.size() == 0))
    {
      UICommon.showWarning("Search did not result in any product.", "Result empty", MainWindow.instance);
      return;
    }
    addRowsToTable(localArrayList);
  }
  
  private void addRowsToTable(ArrayList<StockAndProductRow> paramArrayList)
  {
    this._mSearchTable.clearSelection();
    Iterator localIterator = paramArrayList.iterator();
    while (localIterator.hasNext())
    {
      StockAndProductRow localStockAndProductRow = (StockAndProductRow)localIterator.next();
      this._mSearchTable.addSearchProduct(localStockAndProductRow);
    }
    this._mSearchTable.requestFocusInWindow();
  }
  
  public StockAndProductRow getItemAt(int paramInt)
  {
    return this._mSearchTable.getProductAt(paramInt);
  }
  
  public int getItemsCount()
  {
    return this._mSearchTable.getItemsCount();
  }
  
  public StockAndProductRow getSelectedItem()
  {
    int[] arrayOfInt = this._mSearchTable.getSelectedRows();
    if (arrayOfInt.length == 0) {
      return null;
    }
    int i = this._mSearchTable.convertRowIndexToModel(arrayOfInt[0]);
    return this._mSearchTable.getProductAt(i);
  }
  
  private void okClicked()
  {
    StockAndProductRow localStockAndProductRow = this._mSearchTable.getSelectedProduct();
    if ((localStockAndProductRow != null) && ((localStockAndProductRow.getStock() < 0.0D) || (localStockAndProductRow.getStoreId() != this.currentStore)))
    {
      UICommon.showError("No stock found for the selected product in the store.", "Error", this);
      return;
    }
    this._mSelectedRow = localStockAndProductRow;
    setVisible(false);
  }
  
  private void closeWindowClicked()
  {
    this._mSelectedRow = null;
    closeClicked();
  }
  
  private void closeClicked()
  {
    this._mSelectedRow = null;
    setVisible(false);
  }
  
  private void updateProductSearchComponentFonts(JPanel paramJPanel)
  {
    JLabel localJLabel = new JLabel();
    Font localFont1 = localJLabel.getFont();
    Font localFont2 = new Font(localFont1.getName(), 1, 14);
    JTextField localJTextField = new JTextField();
    localFont1 = localJTextField.getFont();
    Font localFont3 = new Font(localFont1.getName(), 1, 14);
    Font localFont4 = new JXButton().getFont();
    localFont4 = new Font(localFont4.getName(), localFont4.getStyle() | 0x1, 14);
    setFontForChildren(paramJPanel, localFont2, localFont3, localFont4);
  }
  
  public StockAndProductRow getSelectedProduct()
  {
    return this._mSelectedRow;
  }
  
  private void setFontForChildren(JComponent paramJComponent, Font paramFont1, Font paramFont2, Font paramFont3)
  {
    if ((paramJComponent instanceof JButton)) {
      paramJComponent.setFont(paramFont3);
    }
    if ((paramJComponent instanceof JTextField))
    {
      paramJComponent.setFont(paramFont2);
    }
    else if (((paramJComponent instanceof JLabel)) || ((paramJComponent instanceof JCheckBox)))
    {
      paramJComponent.setFont(paramFont1);
    }
    else
    {
      Component[] arrayOfComponent = paramJComponent.getComponents();
      for (int i = 0; i < arrayOfComponent.length; i++) {
        if ((arrayOfComponent[i] instanceof JComponent)) {
          setFontForChildren((JComponent)arrayOfComponent[i], paramFont1, paramFont2, paramFont3);
        }
      }
    }
  }
  
  private class ProductSearchTable
    extends JXTable
  {
    private ProductSearchTableModel _mModel = null;
    
    public ProductSearchTable()
    {
      setModel(this._mModel);
      setColumnControlVisible(true);
      setRowHeight(25);
      initUI();
    }
    
    public void addSearchProduct(StockAndProductRow paramStockAndProductRow)
    {
      this._mModel.addProductRow(paramStockAndProductRow);
    }
    
    public boolean isCellEditable(int paramInt1, int paramInt2)
    {
      return false;
    }
    
    private void initUI()
    {
      ProductSearchTableCellRenderer localProductSearchTableCellRenderer = new ProductSearchTableCellRenderer();
      setDefaultRenderer(Object.class, localProductSearchTableCellRenderer);
    }
    
    public void removeAllProducts()
    {
      this._mModel.removeAllRows();
    }
    
    public StockAndProductRow getSelectedProduct()
    {
      return this._mModel.getProduct(getSelectedRow());
    }
    
    public StockAndProductRow getProductAt(int paramInt)
    {
      return this._mModel.getProduct(paramInt);
    }
    
    public int getItemsCount()
    {
      return this._mModel.getRowCount();
    }
    
    private void setColumnWidths()
    {
      int i = BillingProductSearchDialog.this._mSearchTable.getColumnCount();
      TableColumnModel localTableColumnModel = BillingProductSearchDialog.this._mSearchTable.getColumnModel();
      JTableHeader localJTableHeader = BillingProductSearchDialog.this._mSearchTable.getTableHeader();
      Font localFont1 = localJTableHeader.getFont();
      final Font localFont2 = new Font(localFont1.getName(), 1, 14);
      final TableCellRenderer localTableCellRenderer = localJTableHeader.getDefaultRenderer();
      for (int j = 0; j < i; j++)
      {
        TableColumn localTableColumn = localTableColumnModel.getColumn(j);
        if (j == 0) {
          localTableColumn.setPreferredWidth(80);
        }
        if ((j == 1) || (j == 3) || (j == 9)) {
          localTableColumn.setPreferredWidth(160);
        }
        if ((j == 2) || (j == 6) || (j == 7)) {
          localTableColumn.setPreferredWidth(300);
        }
        if (j == 4) {
          localTableColumn.setPreferredWidth(150);
        }
        if ((j == 5) || (j == 8)) {
          localTableColumn.setPreferredWidth(150);
        }
        localTableColumn.setCellRenderer(new ProductSearchTableCellRenderer());
        localTableColumn.setHeaderRenderer(new TableCellRenderer()
        {
          public Component getTableCellRendererComponent(JTable paramAnonymousJTable, Object paramAnonymousObject, boolean paramAnonymousBoolean1, boolean paramAnonymousBoolean2, int paramAnonymousInt1, int paramAnonymousInt2)
          {
            Component localComponent = localTableCellRenderer.getTableCellRendererComponent(paramAnonymousJTable, paramAnonymousObject, paramAnonymousBoolean1, paramAnonymousBoolean2, paramAnonymousInt1, paramAnonymousInt2);
            localComponent.setFont(localFont2);
            return localComponent;
          }
        });
      }
    }
    
    private class ProductSearchTableModel
      extends DefaultTableModel
    {
      private String[] columnNames = { "Sl. No   ", "Product Code ", "Product Name", "Price", "Current stock", "Expiry", "Category", "Department", "Discount", "Tax" };
      private int _mTotalRows = 0;
      private ArrayList<StockAndProductRow> items = new ArrayList();
      
      public ProductSearchTableModel() {}
      
      public StockAndProductRow getProduct(int paramInt)
      {
        return (StockAndProductRow)this.items.get(paramInt);
      }
      
      public int getRowCount()
      {
        return this._mTotalRows;
      }
      
      public int getColumnCount()
      {
        return this.columnNames.length;
      }
      
      public String getColumnName(int paramInt)
      {
        return this.columnNames[paramInt];
      }
      
      public void removeAllRows()
      {
        this.items.clear();
        this._mTotalRows = 0;
      }
      
      public void addProductRow(StockAndProductRow paramStockAndProductRow)
      {
        this._mTotalRows += 1;
        this.items.add(paramStockAndProductRow);
        StockAndProductRow localStockAndProductRow = paramStockAndProductRow;
        String str1 = paramStockAndProductRow.getCategory() != null ? paramStockAndProductRow.getCategory().getCatName() : "NA";
        String str2 = paramStockAndProductRow.getDepartment() != null ? paramStockAndProductRow.getDepartment().getDeptName() : "NA";
        Object[] arrayOfObject = { new Integer(this._mTotalRows), localStockAndProductRow.getProductCode(), " " + localStockAndProductRow.getProdName(), new InternalAmount(localStockAndProductRow.getUnitPrice(), "", " ", false), new InternalQuantity(paramStockAndProductRow.getStock(), "", " ", localStockAndProductRow.getProdUnit(), false), new InternalDate(paramStockAndProductRow.getExpiry(), paramStockAndProductRow.getExpiry() == null, " ", "", UICommon.getDateFormat()), str1, str2, localStockAndProductRow.getDiscountString(), new Double(localStockAndProductRow.getTax()) };
        insertRow(this._mTotalRows - 1, arrayOfObject);
      }
    }
    
    class ProductSearchTableCellRenderer
      extends DefaultTableCellRenderer
    {
      ProductSearchTableCellRenderer() {}
      
      public Component getTableCellRendererComponent(JTable paramJTable, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2)
      {
        super.getTableCellRendererComponent(paramJTable, paramObject, paramBoolean1, paramBoolean2, paramInt1, paramInt2);
        Font localFont1 = getFont();
        Font localFont2 = new Font(localFont1.getName(), localFont1.getStyle() | 0x1, 16);
        setFont(localFont2);
        if ((paramInt2 == 0) || (paramInt2 == 3) || (paramInt2 == 4) || (paramInt2 == 5) || (paramInt2 == 9) || (paramInt2 == 8) || (paramInt2 == 10)) {
          setHorizontalAlignment(4);
        } else if (paramInt2 == 1) {
          setHorizontalAlignment(0);
        } else {
          setHorizontalAlignment(2);
        }
        return this;
      }
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.billing.BillingProductSearchDialog
 * JD-Core Version:    0.7.0.1
 */