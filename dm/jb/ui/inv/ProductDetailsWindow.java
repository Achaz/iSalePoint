package dm.jb.ui.inv;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.db.objects.CategoryRow;
import dm.jb.db.objects.CurrentStockRow;
import dm.jb.db.objects.CurrentStockTableDef;
import dm.jb.db.objects.DeptRow;
import dm.jb.db.objects.StockAndProductRow;
import dm.jb.db.objects.StockAndProductTableDef;
import dm.jb.db.objects.StoreInfoRow;
import dm.jb.db.objects.StoreInfoTableDef;
import dm.jb.ui.MainWindow;
import dm.tools.db.DBException;
import dm.tools.db.DBRow;
import dm.tools.types.InternalQuantity;
import dm.tools.ui.UICommon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXLabel;

public class ProductDetailsWindow
  extends JDialog
{
  public static ProductDetailsWindow INSTANCE = new ProductDetailsWindow();
  private JTextField _mProductCode = null;
  private JTextField _mProductName = null;
  private JTextField _mDeptName = null;
  private JTextField _mCatName = null;
  private JTextField _mStockInCurrentStore = null;
  private JTextField _mStockInAllStores = null;
  private JTextField _mStockInAllWareHouse = null;
  
  private ProductDetailsWindow()
  {
    super(MainWindow.instance, "Product Details", true);
    initUI();
    pack();
    setLocationRelativeTo(MainWindow.instance);
    InputMap localInputMap = ((JPanel)getContentPane()).getInputMap(2);
    String str = "ESCAction";
    localInputMap.put(KeyStroke.getKeyStroke("ESCAPE"), str);
    AbstractAction local1 = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ProductDetailsWindow.this.setVisible(false);
      }
    };
    ((JPanel)getContentPane()).getActionMap().put(str, local1);
  }
  
  private void initUI()
  {
    JPanel localJPanel = (JPanel)getContentPane();
    FormLayout localFormLayout = new FormLayout("10px,110px,10px,100px,100px,10px", "10px,25px,10px,25px,10px,25px,10px,25px,10px,25px,10px,25px,10px,25px,20px,30px,10px");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    JXLabel localJXLabel = new JXLabel("Product Code : ");
    int i = 2;
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.CENTER);
    localJPanel.add(localJXLabel, localCellConstraints);
    this._mProductCode = new JTextField();
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mProductCode, localCellConstraints);
    i += 2;
    localJXLabel = new JXLabel("Product Name : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.CENTER);
    localJPanel.add(localJXLabel, localCellConstraints);
    this._mProductName = new JTextField();
    localCellConstraints.xywh(4, i, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mProductName, localCellConstraints);
    i += 2;
    localJXLabel = new JXLabel("Department : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.CENTER);
    localJPanel.add(localJXLabel, localCellConstraints);
    this._mDeptName = new JTextField();
    localCellConstraints.xywh(4, i, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mDeptName, localCellConstraints);
    i += 2;
    localJXLabel = new JXLabel("Category : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.CENTER);
    localJPanel.add(localJXLabel, localCellConstraints);
    this._mCatName = new JTextField();
    localCellConstraints.xywh(4, i, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mCatName, localCellConstraints);
    i += 2;
    localJXLabel = new JXLabel("Stock in store : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.CENTER);
    localJPanel.add(localJXLabel, localCellConstraints);
    this._mStockInCurrentStore = new JTextField();
    localCellConstraints.xywh(4, i, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mStockInCurrentStore, localCellConstraints);
    this._mStockInCurrentStore.setHorizontalAlignment(4);
    i += 2;
    localJXLabel = new JXLabel("Stock in all stores : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.CENTER);
    localJPanel.add(localJXLabel, localCellConstraints);
    this._mStockInAllStores = new JTextField();
    this._mStockInAllStores.setHorizontalAlignment(4);
    localCellConstraints.xywh(4, i, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mStockInAllStores, localCellConstraints);
    i += 2;
    localJXLabel = new JXLabel("Stock in warehouses : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.CENTER);
    localJPanel.add(localJXLabel, localCellConstraints);
    this._mStockInAllWareHouse = new JTextField();
    localCellConstraints.xywh(4, i, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mStockInAllWareHouse, localCellConstraints);
    this._mStockInAllWareHouse.setHorizontalAlignment(4);
    i++;
    localCellConstraints.xywh(1, i, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.CENTER);
    localJPanel.add(new JSeparator(), localCellConstraints);
    i++;
    localCellConstraints.xywh(1, i, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(getButtonPanel(), localCellConstraints);
    this._mProductCode.setEditable(false);
    this._mProductName.setEditable(false);
    this._mDeptName.setEditable(false);
    this._mCatName.setEditable(false);
    this._mStockInCurrentStore.setEditable(false);
    this._mStockInAllStores.setEditable(false);
    this._mStockInAllWareHouse.setEditable(false);
  }
  
  private JPanel getButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("10px:grow,100px,10px:grow", "30px");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    JXButton localJXButton = new JXButton("Close");
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ProductDetailsWindow.this.setVisible(false);
      }
    });
    return localJPanel;
  }
  
  private void clearAll()
  {
    this._mProductCode.setText("");
    this._mProductName.setText("");
    this._mDeptName.setText("");
    this._mCatName.setText("");
    this._mStockInCurrentStore.setText("");
    this._mStockInAllStores.setText("");
    this._mStockInAllWareHouse.setText("");
  }
  
  public static void showProductDetailsForProductCode(String paramString)
    throws DBException
  {
    ArrayList localArrayList = StockAndProductTableDef.getInstance().getAllValuesWithWhereClause("PRODUCT_CODE='" + paramString + "'");
    if ((localArrayList == null) || (localArrayList.size() == 0))
    {
      UICommon.showError("No product found for the code.", "No data", MainWindow.instance);
      return;
    }
    INSTANCE._mProductCode.setText(paramString);
    StockAndProductRow localStockAndProductRow = (StockAndProductRow)localArrayList.get(0);
    localStockAndProductRow.readDeptAndCategory();
    INSTANCE._mProductName.setText(localStockAndProductRow.getProdName());
    INSTANCE._mDeptName.setText(localStockAndProductRow.getDepartment() == null ? "NA" : localStockAndProductRow.getDepartment().getDeptName());
    INSTANCE._mCatName.setText(localStockAndProductRow.getCategory() == null ? "NA" : localStockAndProductRow.getCategory().getCatName());
    double d1 = 0.0D;
    StoreInfoRow localStoreInfoRow = StoreInfoTableDef.getCurrentStore();
    Object localObject1;
    Object localObject2;
    if (localStoreInfoRow == null)
    {
      INSTANCE._mStockInCurrentStore.setText("NA");
    }
    else
    {
      int i = localStoreInfoRow.getStoreId();
      Iterator localIterator = localArrayList.iterator();
      while (localIterator.hasNext())
      {
        DBRow localDBRow = (DBRow)localIterator.next();
        localObject1 = (StockAndProductRow)localDBRow;
        d1 += ((StockAndProductRow)localObject1).getStock();
        if (((StockAndProductRow)localObject1).getStoreId() == i)
        {
          localObject2 = new InternalQuantity(((StockAndProductRow)localObject1).getStock(), ((StockAndProductRow)localObject1).getProdUnit(), true);
          INSTANCE._mStockInCurrentStore.setText(((InternalQuantity)localObject2).toString());
        }
      }
    }
    InternalQuantity localInternalQuantity = new InternalQuantity(d1, localStockAndProductRow.getProdUnit(), true);
    INSTANCE._mStockInAllStores.setText(localInternalQuantity.toString());
    localArrayList = CurrentStockTableDef.getInstance().getAllValuesWithWhereClause("PROD_ID=" + localStockAndProductRow.getProdIndex());
    double d2 = 0.0D;
    if ((localArrayList != null) && (localArrayList.size() > 0))
    {
      localObject1 = localArrayList.iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject2 = (DBRow)((Iterator)localObject1).next();
        CurrentStockRow localCurrentStockRow = (CurrentStockRow)localObject2;
        d2 += localCurrentStockRow.getQuantity();
      }
    }
    localInternalQuantity = new InternalQuantity(d2, localStockAndProductRow.getProdUnit(), true);
    INSTANCE._mStockInAllWareHouse.setText(localInternalQuantity.toString());
    INSTANCE.setVisible(true);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.inv.ProductDetailsWindow
 * JD-Core Version:    0.7.0.1
 */