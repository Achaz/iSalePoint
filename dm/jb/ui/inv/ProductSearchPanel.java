package dm.jb.ui.inv;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.db.objects.CategoryRow;
import dm.jb.db.objects.CategoryTableDef;
import dm.jb.db.objects.DeptRow;
import dm.jb.db.objects.DeptTableDef;
import dm.jb.db.objects.ProductRow;
import dm.jb.db.objects.ProductTableDef;
import dm.jb.ui.MainWindow;
import dm.jb.ui.common.ShortKeyCommon;
import dm.tools.db.DBException;
import dm.tools.db.DBRow;
import dm.tools.db.Db;
import dm.tools.types.InternalAmount;
import dm.tools.ui.NonEditableJXTable;
import dm.tools.ui.UICommon;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXTable;

public class ProductSearchPanel
  extends JDialog
{
  private JTextField _mProductId = null;
  private JTextField _mProductName = null;
  private JTextField _mDeptName = null;
  private JTextField _mCatName = null;
  private SearchResultTableModel _mModel = null;
  private JXTable _mTable = null;
  private boolean _mIsCancelled = true;
  private ProductRow _mSelectedProduct = null;
  public static Color BACKGROUND_COLOR = new Color(136, 189, 193);
  
  public ProductSearchPanel(JFrame paramJFrame)
  {
    super(paramJFrame, "Product Search", true);
    initUI();
    pack();
    setLocationRelativeTo(null);
    addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 27) {
          ProductSearchPanel.this.setVisible(false);
        }
      }
    });
    InputMap localInputMap = ((JPanel)getContentPane()).getInputMap(1);
    String str = "ESCAction";
    localInputMap.put(KeyStroke.getKeyStroke("ESCAPE"), str);
    Object localObject = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ProductSearchPanel.this.setVisible(false);
      }
    };
    ((JPanel)getContentPane()).getActionMap().put(str, (Action)localObject);
    str = "F3Action";
    localInputMap.put(KeyStroke.getKeyStroke("F3"), str);
    localObject = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ProductSearchPanel.this.searchClicked();
      }
    };
    ((JPanel)getContentPane()).getActionMap().put(str, (Action)localObject);
    this._mTable.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 114) {
          ProductSearchPanel.this.searchClicked();
        }
      }
    });
    str = "F5Action";
    localInputMap.put(KeyStroke.getKeyStroke("F5"), str);
    localObject = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ProductSearchPanel.this.resetClicked();
        ProductSearchPanel.this._mProductId.requestFocusInWindow();
      }
    };
    ((JPanel)getContentPane()).getActionMap().put(str, (Action)localObject);
    this._mTable.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 116)
        {
          ProductSearchPanel.this.resetClicked();
          ProductSearchPanel.this._mProductId.requestFocusInWindow();
        }
      }
    });
    addWindowListener(new WindowAdapter()
    {
      public void windowActivated(WindowEvent paramAnonymousWindowEvent)
      {
        if (ProductSearchPanel.this._mTable.getRowCount() > 0) {
          ProductSearchPanel.this._mTable.requestFocusInWindow();
        } else {
          ProductSearchPanel.this._mProductId.requestFocusInWindow();
        }
        ProductSearchPanel.this._mIsCancelled = true;
      }
    });
    this._mTable.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 27) {
          ProductSearchPanel.this.setVisible(false);
        }
      }
    });
  }
  
  public void setDefaultFocus()
  {
    this._mProductId.requestFocusInWindow();
  }
  
  public void setSingleSelectionMode(boolean paramBoolean)
  {
    if (paramBoolean) {
      this._mTable.setSelectionMode(0);
    } else {
      this._mTable.setSelectionMode(2);
    }
  }
  
  private void initUI()
  {
    JPanel localJPanel = (JPanel)getContentPane();
    FormLayout localFormLayout = new FormLayout("10px,pref:grow,20px,pref,150px,10px", "10px,pref,20px,200px:grow,20px,30px,10px");
    localJPanel.setLayout(localFormLayout);
    localJPanel.setBorder(BorderFactory.createEtchedBorder());
    localJPanel.setBackground(BACKGROUND_COLOR);
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(2, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(getAttrPane(), localCellConstraints);
    localCellConstraints.xywh(4, 2, 1, 1, CellConstraints.LEFT, CellConstraints.TOP);
    localJPanel.add(getSearchButtonPanel(), localCellConstraints);
    this._mModel = new SearchResultTableModel(null);
    this._mTable = new NonEditableJXTable(this._mModel);
    JScrollPane localJScrollPane = new JScrollPane(this._mTable);
    localCellConstraints.xywh(2, 4, 4, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJScrollPane, localCellConstraints);
    this._mTable.getColumn(1).setPreferredWidth(180);
    this._mTable.getColumn(0).setPreferredWidth(100);
    this._mTable.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
      {
        if (paramAnonymousMouseEvent.getClickCount() == 2)
        {
          ProductSearchPanel.this._mSelectedProduct = ProductSearchPanel.this._mModel.getProductAtRow(ProductSearchPanel.this._mTable.getSelectedRow());
          ProductSearchPanel.this._mIsCancelled = false;
          ProductSearchPanel.this.setVisible(false);
        }
      }
    });
    this._mTable.addKeyListener(new KeyAdapter()
    {
      public void keyTyped(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyChar() == '\n')
        {
          paramAnonymousKeyEvent.consume();
          ProductSearchPanel.this._mSelectedProduct = ProductSearchPanel.this._mModel.getProductAtRow(ProductSearchPanel.this._mTable.getSelectedRow());
          ProductSearchPanel.this.setVisible(false);
        }
      }
    });
    localCellConstraints.xywh(1, 5, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.CENTER);
    localJPanel.add(new JSeparator(), localCellConstraints);
    localCellConstraints.xywh(2, 6, localFormLayout.getColumnCount() - 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(getButtonPanel(), localCellConstraints);
  }
  
  private JPanel getAttrPane()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("100px,10px,200px", "23px,10px,23px,10px,23px,10px,23px");
    localJPanel.setLayout(localFormLayout);
    localJPanel.setBackground(BACKGROUND_COLOR);
    CellConstraints localCellConstraints = new CellConstraints();
    JLabel localJLabel = new JLabel("Product Code : ");
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mProductId = new JTextField();
    localCellConstraints.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mProductId, localCellConstraints);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 80, this._mProductId);
    localJLabel = new JLabel("Product Name : ");
    localCellConstraints.xywh(1, 3, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mProductName = new JTextField();
    localCellConstraints.xywh(3, 3, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mProductName, localCellConstraints);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 78, this._mProductName);
    localJLabel = new JLabel("Category : ");
    localCellConstraints.xywh(1, 5, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mCatName = new JTextField();
    localCellConstraints.xywh(3, 5, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mCatName, localCellConstraints);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 67, this._mCatName);
    localJLabel = new JLabel("Department : ");
    localCellConstraints.xywh(1, 7, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mDeptName = new JTextField();
    localCellConstraints.xywh(3, 7, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mDeptName, localCellConstraints);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 68, this._mDeptName);
    return localJPanel;
  }
  
  public JPanel getButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    localJPanel.setLayout(new FormLayout("100px,pref:grow,100px", "30px"));
    JXButton localJXButton = new JXButton("Select");
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    final ProductSearchPanel localProductSearchPanel = this;
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (ProductSearchPanel.this._mTable.getSelectedRow() == -1)
        {
          UICommon.showError("Please select a product.", "Error", localProductSearchPanel);
          return;
        }
        ProductSearchPanel.this._mIsCancelled = false;
        ProductSearchPanel.this._mSelectedProduct = ProductSearchPanel.this._mModel.getProductAtRow(ProductSearchPanel.this._mTable.getSelectedRow());
        ProductSearchPanel.this.setVisible(false);
      }
    });
    localJPanel.setBackground(BACKGROUND_COLOR);
    localJXButton.setBackground(BACKGROUND_COLOR);
    localJXButton = new JXButton("Close");
    localCellConstraints.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.setBackground(BACKGROUND_COLOR);
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ProductSearchPanel.this._mSelectedProduct = null;
        ProductSearchPanel.this._mIsCancelled = true;
        ProductSearchPanel.this.setVisible(false);
      }
    });
    return localJPanel;
  }
  
  private JPanel getSearchButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    localJPanel.setLayout(new FormLayout("100px", "30px,30px,30px"));
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    JXButton localJXButton = new JXButton("Search [F3]");
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.setBackground(BACKGROUND_COLOR);
    localJPanel.setBackground(BACKGROUND_COLOR);
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ProductSearchPanel.this.searchClicked();
      }
    });
    localCellConstraints.xywh(1, 3, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJXButton = new JXButton("Reset [F5]");
    localJXButton.setBackground(BACKGROUND_COLOR);
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ProductSearchPanel.this.resetClicked();
        ProductSearchPanel.this._mProductId.requestFocusInWindow();
      }
    });
    return localJPanel;
  }
  
  public ProductRow searchProduct(String paramString1, String paramString2, String paramString3, Object paramObject)
  {
    paramString1 = Db.getSearchFormattedString(paramString1);
    paramString2 = Db.getSearchFormattedString(paramString2);
    paramString3 = Db.getSearchFormattedString(paramString3);
    this._mProductName.setText(paramString1);
    this._mCatName.setText(paramString2);
    this._mDeptName.setText(paramString3);
    ProductRow localProductRow = searchProductInternal(paramString1, paramString2, paramString3, paramObject);
    if (localProductRow != null) {
      return localProductRow;
    }
    setVisible(true);
    return this._mSelectedProduct;
  }
  
  public ProductRow searchProductInternal(String paramString1, String paramString2, String paramString3, Object paramObject)
  {
    this._mModel.removeAllRows();
    if ((paramString2.length() == 0) && (paramString3.length() == 0)) {
      try
      {
        ArrayList localArrayList1 = ProductTableDef.getInstance().getAllValuesWithWhereClause("PROD_NAME LIKE '" + paramString1 + "'");
        if ((localArrayList1 == null) || (localArrayList1.size() == 0))
        {
          showWarning("No records found.", "No record found", paramObject);
          return null;
        }
        return addRowsToModel(localArrayList1);
      }
      catch (DBException localDBException1)
      {
        localDBException1.printStackTrace();
        showError("Internal error searching for product.", "Internal Error", paramObject);
        return null;
      }
    }
    ArrayList localArrayList5;
    int i;
    Object localObject1;
    Object localObject2;
    if (paramString3.length() == 0) {
      try
      {
        ArrayList localArrayList2 = CategoryTableDef.getInstance().getAllValuesWithWhereClause("CAT_NAME LIKE '" + paramString2 + "'");
        if ((localArrayList2 == null) || (localArrayList2.size() == 0))
        {
          showWarning("No record found.", "No record", paramObject);
          return null;
        }
        localArrayList5 = new ArrayList();
        for (i = 0; i < localArrayList2.size(); i++)
        {
          localObject1 = (CategoryRow)localArrayList2.get(i);
          localObject2 = ProductTableDef.getInstance().getAllValuesWithWhereClause("PROD_NAME LIKE '" + paramString1 + "' AND CAT_INDEX=" + ((CategoryRow)localObject1).getCatIndex());
          localArrayList5.addAll((Collection)localObject2);
        }
        if ((localArrayList5 == null) || (localArrayList5.size() == 0))
        {
          showWarning("No record found.", "No record", paramObject);
          return null;
        }
        return addRowsToModel(localArrayList5);
      }
      catch (DBException localDBException2)
      {
        showError("Internal error search product.", "Internal Error", paramObject);
        return null;
      }
    }
    if (paramString2.length() == 0) {
      try
      {
        ArrayList localArrayList3 = DeptTableDef.getInstance().getAllValuesWithWhereClause("DEPT_NAME LIKE '" + paramString3 + "'");
        if ((localArrayList3 == null) || (localArrayList3.size() == 0))
        {
          showWarning("No records found.", "No records", paramObject);
          return null;
        }
        localArrayList5 = new ArrayList();
        for (i = 0; i < localArrayList3.size(); i++)
        {
          localObject1 = (DeptRow)localArrayList3.get(i);
          localObject2 = ProductTableDef.getInstance().getAllValuesWithWhereClause("PROD_NAME LIKE '" + paramString1 + "' AND DEPT_INDEX=" + ((DeptRow)localObject1).getDeptIndex());
          localArrayList5.addAll((Collection)localObject2);
        }
        if ((localArrayList5 == null) || (localArrayList5.size() == 0))
        {
          showWarning("No record found.", "No record", paramObject);
          return null;
        }
        return addRowsToModel(localArrayList5);
      }
      catch (DBException localDBException3)
      {
        showError("Internal error searching product.", "Internal Error", paramObject);
        return null;
      }
    }
    try
    {
      ArrayList localArrayList4 = DeptTableDef.getInstance().getAllValuesWithWhereClause("DEPT_NAME LIKE '" + paramString3 + "'");
      if ((localArrayList4 == null) || (localArrayList4.size() == 0))
      {
        showWarning("No records found.", "No records", paramObject);
        return null;
      }
      localArrayList5 = CategoryTableDef.getInstance().getAllValuesWithWhereClause("CAT_NAME LIKE '" + paramString2 + "'");
      if ((localArrayList5 == null) || (localArrayList5.size() == 0))
      {
        showWarning("No record found.", "No record", paramObject);
        return null;
      }
      ArrayList localArrayList6 = new ArrayList();
      Object localObject3;
      for (int j = 0; j < localArrayList5.size(); j++)
      {
        localObject2 = (CategoryRow)localArrayList5.get(j);
        for (int m = 0; m < localArrayList4.size(); m++)
        {
          localObject3 = (DeptRow)localArrayList4.get(m);
          if (((DeptRow)localObject3).getDeptIndex() == ((CategoryRow)localObject2).getDeptIndex())
          {
            localArrayList6.add(localObject2);
            break;
          }
        }
      }
      if ((localArrayList6 == null) || (localArrayList6.size() == 0))
      {
        showWarning("No record found.", "No record", paramObject);
        return null;
      }
      ArrayList localArrayList7 = new ArrayList();
      for (int k = 0; k < localArrayList6.size(); k++)
      {
        CategoryRow localCategoryRow = (CategoryRow)localArrayList6.get(k);
        localObject3 = ProductTableDef.getInstance().getAllValuesWithWhereClause("PROD_NAME LIKE '" + paramString1 + "' AND CAT_INDEX=" + localCategoryRow.getCatIndex());
        localArrayList7.addAll((Collection)localObject3);
      }
      if ((localArrayList7 == null) || (localArrayList7.size() == 0))
      {
        showWarning("No record found.", "No record", paramObject);
        return null;
      }
      return addRowsToModel(localArrayList7);
    }
    catch (DBException localDBException4)
    {
      showError("Internal error searching products.", "Internal Error", paramObject);
    }
    return null;
  }
  
  private void searchClicked()
  {
    String str1 = this._mProductId.getText().trim();
    if (str1.length() != 0) {
      try
      {
        String str2 = "PRODUCT_CODE='" + str1 + "' ";
        localObject1 = ProductTableDef.getInstance().getAllValuesWithWhereClause(str2);
        if ((localObject1 == null) || (((ArrayList)localObject1).size() == 0))
        {
          UICommon.showError("Product not found.", "Error", MainWindow.instance);
          this._mProductId.requestFocusInWindow();
          return;
        }
        localObject2 = (ProductRow)((ArrayList)localObject1).get(0);
        this._mModel.removeAllRows();
        this._mModel.addProductRow((ProductRow)localObject2, str1);
      }
      catch (DBException localDBException)
      {
        localDBException.printStackTrace();
        UICommon.showError("Internal Error", "Internal Error", this);
        return;
      }
    }
    String str3 = this._mCatName.getText().trim();
    Object localObject1 = this._mDeptName.getText().trim();
    Object localObject2 = this._mProductName.getText().trim();
    localObject2 = Db.getSearchFormattedString((String)localObject2);
    str3 = Db.getSearchFormattedString(str3);
    localObject1 = Db.getSearchFormattedString((String)localObject1);
    searchProductInternal((String)localObject2, str3, (String)localObject1, this);
  }
  
  private ProductRow addRowsToModel(ArrayList<DBRow> paramArrayList)
    throws DBException
  {
    for (int i = 0; i < paramArrayList.size(); i++)
    {
      ProductRow localProductRow2 = (ProductRow)paramArrayList.get(i);
      String str = localProductRow2.getProductCode();
      this._mModel.addProductRow(localProductRow2, str);
    }
    if (paramArrayList.size() == 1)
    {
      ProductRow localProductRow1 = (ProductRow)paramArrayList.get(0);
      return localProductRow1;
    }
    this._mTable.requestFocusInWindow();
    this._mTable.getSelectionModel().setSelectionInterval(0, 0);
    paramArrayList.clear();
    paramArrayList = null;
    return null;
  }
  
  private void resetClicked()
  {
    this._mProductId.setText("");
    this._mProductName.setText("%");
    this._mDeptName.setText("%");
    this._mCatName.setText("%");
    this._mModel.removeAllRows();
    this._mIsCancelled = true;
  }
  
  private void showInfo(String paramString1, String paramString2, Object paramObject)
  {
    if ((paramObject instanceof JDialog)) {
      UICommon.showMessage(paramString1, paramString2, (JDialog)paramObject);
    } else {
      UICommon.showMessage(paramString1, paramString2, (JFrame)paramObject);
    }
  }
  
  private void showError(String paramString1, String paramString2, Object paramObject)
  {
    if ((paramObject instanceof JDialog)) {
      UICommon.showError(paramString1, paramString2, (JDialog)paramObject);
    } else {
      UICommon.showError(paramString1, paramString2, (JFrame)paramObject);
    }
  }
  
  private void showWarning(String paramString1, String paramString2, Object paramObject)
  {
    if ((paramObject instanceof JDialog)) {
      UICommon.showWarning(paramString1, paramString2, (JDialog)paramObject);
    } else {
      UICommon.showWarning(paramString1, paramString2, (JFrame)paramObject);
    }
  }
  
  public ProductRow[] getSelectedProducts()
  {
    int i = this._mTable.getSelectedRowCount();
    if (i == 0) {
      return null;
    }
    ProductRow[] arrayOfProductRow = new ProductRow[i];
    int[] arrayOfInt = this._mTable.getSelectedRows();
    for (int j = 0; j < arrayOfInt.length; j++)
    {
      int k = arrayOfInt[j];
      k = this._mTable.convertRowIndexToModel(k);
      arrayOfProductRow[j] = this._mModel.getProductAtRow(k);
    }
    return arrayOfProductRow;
  }
  
  public boolean isCancelled()
  {
    return this._mIsCancelled;
  }
  
  private class SearchResultTableModel
    extends DefaultTableModel
  {
    String[] colNames = { "Product Code", "Product Name", "Department", "Category", "Price" };
    
    private SearchResultTableModel() {}
    
    public String getColumnName(int paramInt)
    {
      return this.colNames[paramInt];
    }
    
    public Class getColumnClass(int paramInt)
    {
      if (paramInt == 4) {
        return Integer.class;
      }
      return Object.class;
    }
    
    public boolean isCellEditable(int paramInt1, int paramInt2)
    {
      return false;
    }
    
    public int getColumnCount()
    {
      return this.colNames.length;
    }
    
    public void addProductRow(ProductRow paramProductRow, String paramString)
    {
      Object[] arrayOfObject = new Object[this.colNames.length];
      arrayOfObject[0] = paramString;
      arrayOfObject[1] = paramProductRow;
      arrayOfObject[2] = paramProductRow.getDept();
      arrayOfObject[3] = paramProductRow.getCategory();
      arrayOfObject[4] = InternalAmount.toString(paramProductRow.getUnitPrice());
      addRow(arrayOfObject);
    }
    
    public void removeAllRows()
    {
      int i = getRowCount();
      for (int j = 0; j < i; j++) {
        removeRow(0);
      }
    }
    
    public ProductRow getProductAtRow(int paramInt)
    {
      return (ProductRow)getValueAt(paramInt, 1);
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.inv.ProductSearchPanel
 * JD-Core Version:    0.7.0.1
 */