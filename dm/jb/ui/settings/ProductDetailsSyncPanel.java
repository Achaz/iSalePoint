package dm.jb.ui.settings;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.db.objects.CategoryRow;
import dm.jb.db.objects.CurrentStockRow;
import dm.jb.db.objects.CurrentStockTableDef;
import dm.jb.db.objects.DeptRow;
import dm.jb.db.objects.DeptTableDef;
import dm.jb.db.objects.ProductRow;
import dm.jb.db.objects.StoreInfoRow;
import dm.jb.db.objects.StoreInfoTableDef;
import dm.jb.db.objects.StoreStockRow;
import dm.jb.db.objects.StoreStockTableDef;
import dm.jb.op.sync.FileSync;
import dm.jb.op.sync.NetworkSync;
import dm.jb.op.sync.SyncException;
import dm.jb.op.sync.SyncMode;
import dm.jb.op.sync.SyncWriter;
import dm.jb.ui.MainWindow;
import dm.tools.db.DBException;
import dm.tools.ui.ProgressWindow;
import dm.tools.ui.ProgressWindowAction;
import dm.tools.ui.ShuttlePane;
import dm.tools.ui.ShuttlePaneActionListener;
import dm.tools.ui.UICommon;
import dm.tools.ui.WizardBase;
import dm.tools.ui.WizardPanelBase;
import dm.tools.ui.components.JTextSeparator;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

public class ProductDetailsSyncPanel
  extends WizardBase
{
  public static ProductDetailsSyncPanel INSTANCE = new ProductDetailsSyncPanel();
  private SyncDeptSelection _mDeptSelection = null;
  private SyncCatSelection _mCatSelection = null;
  private OutputOptions _mOPPanel = null;
  
  private ProductDetailsSyncPanel()
  {
    super(2, 620, 300);
    addPages();
  }
  
  public void windowDisplayed()
  {
    setPage(0);
    super.windowDisplayed();
    setPrefixTitle("Product Details Synchronization");
    try
    {
      ArrayList localArrayList = DeptTableDef.getInstance().getDeptList();
      this._mDeptSelection.fillDeptList(localArrayList);
      this._mCatSelection.fillDeptList(localArrayList);
      Iterator localIterator = localArrayList.iterator();
      while (localIterator.hasNext())
      {
        DeptRow localDeptRow = (DeptRow)localIterator.next();
        localDeptRow.getCategoryList();
      }
    }
    catch (DBException localDBException)
    {
      localDBException.printStackTrace();
      UICommon.showInternalError("Internal search searching for departments.");
      return;
    }
  }
  
  public boolean finish()
  {
    this._mOPPanel.syncData();
    return false;
  }
  
  private void addPages()
  {
    WelcomePage localWelcomePage = new WelcomePage(this);
    addPanel(localWelcomePage);
    setWelcomePage(localWelcomePage);
    this._mOPPanel = new OutputOptions(this);
    addPanel(this._mOPPanel);
    this._mDeptSelection = new SyncDeptSelection(this);
    addPanel(this._mDeptSelection);
    this._mCatSelection = new SyncCatSelection(this);
    addPanel(this._mCatSelection);
    addPanel(new OtherDetailsSelection(this));
  }
  
  private class OtherObjects
  {
    private String _mName = null;
    private int _mCode = 0;
    
    public OtherObjects(String paramString, int paramInt)
    {
      this._mName = paramString;
      this._mCode = paramInt;
    }
    
    public String toString()
    {
      return this._mName;
    }
  }
  
  private class OutputOptions
    extends WizardPanelBase
    implements ProgressWindowAction
  {
    private JComboBox _mOutputType = null;
    private JPanel _mCurrentPanel = null;
    
    public OutputOptions(WizardBase paramWizardBase)
    {
      super();
      initUI();
    }
    
    public boolean isPageValid()
    {
      SyncMode localSyncMode = (SyncMode)this._mOutputType.getSelectedItem();
      return localSyncMode.isPanelValid();
    }
    
    public String getTitle()
    {
      return "Output";
    }
    
    private void initUI()
    {
      FormLayout localFormLayout = new FormLayout("10px,pref,10px,90px,10px,100px,250px:grow,10px", "10px,25px,50px, pref:grow,10px");
      setLayout(localFormLayout);
      URL localURL = getClass().getResource("/dm/jb/images/syncprodwiz.png");
      ImageIcon localImageIcon = new ImageIcon(localURL);
      JLabel localJLabel = new JLabel(localImageIcon);
      CellConstraints localCellConstraints = new CellConstraints();
      localCellConstraints.xywh(2, 2, 1, localFormLayout.getRowCount() - 2, CellConstraints.FILL, CellConstraints.TOP);
      add(localJLabel, localCellConstraints);
      this._mOutputType = new JComboBox();
      localJLabel = new JLabel("Output to : ");
      localCellConstraints.xywh(4, 2, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
      add(localJLabel, localCellConstraints);
      localCellConstraints.xywh(6, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
      add(this._mOutputType, localCellConstraints);
      this._mOutputType.addItem(new FileSync());
      this._mOutputType.addItem(new NetworkSync());
      this._mOutputType.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          ProductDetailsSyncPanel.OutputOptions.this.typeChanged();
        }
      });
      localCellConstraints.xywh(4, 3, 4, 1, CellConstraints.FILL, CellConstraints.CENTER);
      add(new JTextSeparator("Output Setup"), localCellConstraints);
      typeChanged();
    }
    
    private void typeChanged()
    {
      SyncMode localSyncMode = (SyncMode)this._mOutputType.getSelectedItem();
      CellConstraints localCellConstraints = new CellConstraints();
      if (this._mCurrentPanel != null)
      {
        this._mCurrentPanel.setVisible(false);
        remove(this._mCurrentPanel);
      }
      this._mCurrentPanel = localSyncMode.getOptionPanel();
      localCellConstraints.xywh(4, 4, 4, 1, CellConstraints.FILL, CellConstraints.TOP);
      add(this._mCurrentPanel, localCellConstraints);
      this._mCurrentPanel.setVisible(true);
    }
    
    private void syncData()
    {
      ProgressWindow localProgressWindow = new ProgressWindow(MainWindow.instance, this);
      localProgressWindow.startProgress();
    }
    
    public void startAction()
    {
      SyncMode localSyncMode = (SyncMode)this._mOutputType.getSelectedItem();
      SyncWriter localSyncWriter = localSyncMode.getWriterInstance();
      if (localSyncWriter == null)
      {
        UICommon.showDelayedErrorMessage("Internal error running synchronization.", "Internal Error", MainWindow.instance);
        return;
      }
      try
      {
        localSyncWriter.open((short)1);
        localSyncWriter.init();
        localSyncMode.writeModeSpecificDataToWrite(localSyncWriter);
        ArrayList localArrayList1 = new ArrayList();
        ArrayList localArrayList2 = new ArrayList();
        ArrayList localArrayList3 = new ArrayList();
        ArrayList localArrayList4 = ProductDetailsSyncPanel.this._mDeptSelection.getSelectedDepts();
        localSyncWriter.writeShort((short)1);
        localSyncWriter.writeInt(localArrayList4.size());
        Object localObject2;
        Object localObject3;
        if ((localArrayList4 != null) && (localArrayList4.size() > 0))
        {
          localObject1 = localArrayList4.iterator();
          while (((Iterator)localObject1).hasNext())
          {
            localObject2 = (DeptRow)((Iterator)localObject1).next();
            ((DeptRow)localObject2).sync(localSyncWriter);
            if (ProductDetailsSyncPanel.SyncDeptSelection.access$400(ProductDetailsSyncPanel.this._mDeptSelection).isSelected())
            {
              localObject3 = ((DeptRow)localObject2).getAllProducts();
              localArrayList1.addAll((Collection)localObject3);
              if (ProductDetailsSyncPanel.SyncDeptSelection.access$500(ProductDetailsSyncPanel.this._mDeptSelection).isSelected()) {
                localArrayList2.addAll(getStoreStockForProducts((ArrayList)localObject3, false, null));
              }
              if (ProductDetailsSyncPanel.SyncDeptSelection.access$600(ProductDetailsSyncPanel.this._mDeptSelection).isSelected()) {
                localArrayList3.addAll(CurrentStockTableDef.getInstance().getStocksForProducts((ArrayList)localObject3, null));
              }
            }
            else if (ProductDetailsSyncPanel.SyncDeptSelection.access$500(ProductDetailsSyncPanel.this._mDeptSelection).isSelected())
            {
              localObject3 = ((DeptRow)localObject2).getAllProducts();
              localArrayList2.addAll(getStoreStockForProducts((ArrayList)localObject3, false, null));
              if (ProductDetailsSyncPanel.SyncDeptSelection.access$600(ProductDetailsSyncPanel.this._mDeptSelection).isSelected()) {
                localArrayList3.addAll(CurrentStockTableDef.getInstance().getStocksForProducts((ArrayList)localObject3, null));
              }
            }
            else if (ProductDetailsSyncPanel.SyncDeptSelection.access$600(ProductDetailsSyncPanel.this._mDeptSelection).isSelected())
            {
              localObject3 = ((DeptRow)localObject2).getAllProducts();
              localArrayList3.addAll(CurrentStockTableDef.getInstance().getStocksForProducts((ArrayList)localObject3, null));
            }
          }
        }
        Object localObject1 = ProductDetailsSyncPanel.this._mCatSelection.getSelectedCategories();
        localSyncWriter.writeShort((short)2);
        localSyncWriter.writeInt(((ArrayList)localObject1).size());
        if ((localObject1 != null) && (((ArrayList)localObject1).size() > 0))
        {
          localObject2 = ((ArrayList)localObject1).iterator();
          while (((Iterator)localObject2).hasNext())
          {
            localObject3 = (CategoryRow)((Iterator)localObject2).next();
            ((CategoryRow)localObject3).sync(localSyncWriter);
            ArrayList localArrayList5;
            if (ProductDetailsSyncPanel.SyncCatSelection.access$800(ProductDetailsSyncPanel.this._mCatSelection).isSelected())
            {
              localArrayList5 = ((CategoryRow)localObject3).getProductsForThis();
              addProductWithoutDuplicate(localArrayList1, localArrayList5);
              if (ProductDetailsSyncPanel.SyncCatSelection.access$900(ProductDetailsSyncPanel.this._mCatSelection).isSelected()) {
                localArrayList2.addAll(getStoreStockForProducts(localArrayList5, true, localArrayList2));
              }
              if (ProductDetailsSyncPanel.SyncCatSelection.access$1000(ProductDetailsSyncPanel.this._mCatSelection).isSelected()) {
                localArrayList3.addAll(CurrentStockTableDef.getInstance().getStocksForProducts(localArrayList5, null));
              }
            }
            else if (ProductDetailsSyncPanel.SyncCatSelection.access$900(ProductDetailsSyncPanel.this._mCatSelection).isSelected())
            {
              localArrayList5 = ((CategoryRow)localObject3).getProductsForThis();
              localArrayList2.addAll(getStoreStockForProducts(localArrayList5, true, localArrayList2));
              if (ProductDetailsSyncPanel.SyncCatSelection.access$1000(ProductDetailsSyncPanel.this._mCatSelection).isSelected()) {
                localArrayList3.addAll(CurrentStockTableDef.getInstance().getStocksForProducts(localArrayList5, null));
              }
            }
            else if (ProductDetailsSyncPanel.SyncCatSelection.access$1000(ProductDetailsSyncPanel.this._mCatSelection).isSelected())
            {
              localArrayList5 = ((CategoryRow)localObject3).getProductsForThis();
              localArrayList3.addAll(CurrentStockTableDef.getInstance().getStocksForProducts(localArrayList5, null));
            }
          }
        }
        if (localArrayList1.size() > 0)
        {
          localSyncWriter.writeShort((short)3);
          localSyncWriter.writeInt(localArrayList1.size());
          localObject2 = localArrayList1.iterator();
          while (((Iterator)localObject2).hasNext())
          {
            localObject3 = (ProductRow)((Iterator)localObject2).next();
            ((ProductRow)localObject3).sync(localSyncWriter);
          }
        }
        if (localArrayList2.size() > 0)
        {
          localSyncWriter.writeShort((short)4);
          localSyncWriter.writeInt(localArrayList2.size());
          localObject2 = localArrayList2.iterator();
          while (((Iterator)localObject2).hasNext())
          {
            localObject3 = (StoreStockRow)((Iterator)localObject2).next();
            ((StoreStockRow)localObject3).sync(localSyncWriter);
          }
        }
        if (localArrayList3.size() > 0)
        {
          localSyncWriter.writeShort((short)5);
          localSyncWriter.writeInt(localArrayList3.size());
          localObject2 = localArrayList3.iterator();
          while (((Iterator)localObject2).hasNext())
          {
            localObject3 = (CurrentStockRow)((Iterator)localObject2).next();
            ((CurrentStockRow)localObject3).sync(localSyncWriter);
          }
        }
      }
      catch (IOException localIOException)
      {
        try
        {
          localSyncWriter.close();
          localSyncWriter.destroy();
        }
        catch (SyncException localSyncException2)
        {
          localIOException.printStackTrace();
        }
        UICommon.showDelayedErrorMessage("Internal error running synchronization.", "Internal Error", MainWindow.instance);
        return;
      }
      catch (DBException localDBException)
      {
        try
        {
          localSyncWriter.close();
          localSyncWriter.destroy();
        }
        catch (SyncException localSyncException3)
        {
          localDBException.printStackTrace();
        }
        UICommon.showDelayedErrorMessage("Internal error running synchronization.", "Internal Error", MainWindow.instance);
        return;
      }
      catch (SyncException localSyncException1)
      {
        try
        {
          localSyncWriter.close();
          localSyncWriter.destroy();
        }
        catch (SyncException localSyncException4)
        {
          localSyncException1.printStackTrace();
        }
        UICommon.showDelayedErrorMessage("Internal error running synchronization.", "Internal Error", MainWindow.instance);
        return;
      }
      UICommon.showDelayedMessage("Synchronization process from this terminal completed succesfully.", "Success", MainWindow.instance);
    }
    
    public String getActionName()
    {
      return "Synhronization";
    }
    
    private ArrayList<StoreStockRow> getStoreStockForProducts(ArrayList<ProductRow> paramArrayList, boolean paramBoolean, ArrayList<StoreStockRow> paramArrayList1)
      throws DBException
    {
      ArrayList localArrayList = new ArrayList();
      int i = -1;
      if (StoreInfoTableDef.getCurrentStore() != null) {
        i = StoreInfoTableDef.getCurrentStore().getStoreId();
      }
      Iterator localIterator1 = paramArrayList.iterator();
      while (localIterator1.hasNext())
      {
        ProductRow localProductRow = (ProductRow)localIterator1.next();
        Object localObject;
        if (i == -1)
        {
          localObject = StoreStockTableDef.getInstance().getStockForProductInStore(localProductRow.getProdIndex(), i);
          if ((localObject != null) && ((!paramBoolean) || (!isDuplicateStock(paramArrayList1, (StoreStockRow)localObject)))) {
            localArrayList.add(localObject);
          }
        }
        else
        {
          localObject = StoreStockTableDef.getInstance().getStocksForProductInAllStores(localProductRow.getProdIndex());
          if (localObject != null) {
            if (!paramBoolean)
            {
              localArrayList.addAll((Collection)localObject);
            }
            else
            {
              Iterator localIterator2 = ((ArrayList)localObject).iterator();
              while (localIterator2.hasNext())
              {
                StoreStockRow localStoreStockRow = (StoreStockRow)localIterator2.next();
                if (!isDuplicateStock(paramArrayList1, localStoreStockRow)) {
                  localArrayList.add(localStoreStockRow);
                }
              }
            }
          }
        }
      }
      return localArrayList;
    }
    
    private boolean isDuplicateStock(ArrayList<StoreStockRow> paramArrayList, StoreStockRow paramStoreStockRow)
    {
      Iterator localIterator = paramArrayList.iterator();
      while (localIterator.hasNext())
      {
        StoreStockRow localStoreStockRow = (StoreStockRow)localIterator.next();
        if ((localStoreStockRow.getStoreId() == paramStoreStockRow.getStoreId()) && (localStoreStockRow.getProductId() == paramStoreStockRow.getProductId())) {
          return true;
        }
      }
      return false;
    }
    
    private void addProductWithoutDuplicate(ArrayList<ProductRow> paramArrayList1, ArrayList<ProductRow> paramArrayList2)
    {
      Iterator localIterator1 = paramArrayList2.iterator();
      while (localIterator1.hasNext())
      {
        ProductRow localProductRow1 = (ProductRow)localIterator1.next();
        int i = 0;
        Iterator localIterator2 = paramArrayList1.iterator();
        while (localIterator2.hasNext())
        {
          ProductRow localProductRow2 = (ProductRow)localIterator2.next();
          if (localProductRow2.getProdIndex() == localProductRow1.getProdIndex())
          {
            i = 1;
            break;
          }
        }
        if (i == 0) {
          paramArrayList1.add(localProductRow1);
        }
      }
    }
  }
  
  private class OtherDetailsSelection
    extends WizardPanelBase
  {
    public static final short OTHER_TYPE_SITE_INFO = 1;
    public static final short OTHER_TYPE_STORE_INFO = 2;
    public static final short OTHER_TYPE_WH_INFO = 3;
    public static final short OTHER_TYPE_USER_INFO = 3;
    private ShuttlePane<ProductDetailsSyncPanel.OtherObjects> _mOthersSelect = null;
    
    public OtherDetailsSelection(WizardBase paramWizardBase)
    {
      super();
      initUI();
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(new ProductDetailsSyncPanel.OtherObjects(ProductDetailsSyncPanel.this, "Site Information", 1));
      localArrayList.add(new ProductDetailsSyncPanel.OtherObjects(ProductDetailsSyncPanel.this, "Store Information", 2));
      localArrayList.add(new ProductDetailsSyncPanel.OtherObjects(ProductDetailsSyncPanel.this, "Warehouse Information", 3));
      localArrayList.add(new ProductDetailsSyncPanel.OtherObjects(ProductDetailsSyncPanel.this, "User Information", 3));
      this._mOthersSelect.setFromList(localArrayList);
    }
    
    public boolean isPageValid()
    {
      return true;
    }
    
    public String getTitle()
    {
      return "Other items";
    }
    
    private void initUI()
    {
      setLayout(new FormLayout("10px,pref,10px,300px:grow,10px", "10px,pref:grow,200px,pref:grow,10px"));
      this._mOthersSelect = new ShuttlePane(false);
      URL localURL = getClass().getResource("/dm/jb/images/syncprodwiz.png");
      ImageIcon localImageIcon = new ImageIcon(localURL);
      JLabel localJLabel = new JLabel(localImageIcon);
      CellConstraints localCellConstraints = new CellConstraints();
      localCellConstraints.xywh(2, 2, 1, 3, CellConstraints.FILL, CellConstraints.TOP);
      add(localJLabel, localCellConstraints);
      localCellConstraints.xywh(4, 3, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
      add(this._mOthersSelect, localCellConstraints);
    }
  }
  
  private class SyncCatSelection
    extends WizardPanelBase
    implements ShuttlePaneActionListener
  {
    private JComboBox _mDeptSelect = null;
    private ShuttlePane<CategoryRow> _mCatSelect = null;
    private JCheckBox _mSelectProductAlso = null;
    private JCheckBox _mSelectStoreStockAlso = null;
    private JCheckBox _mSelectWarehouseStockAlso = null;
    
    public SyncCatSelection(WizardBase paramWizardBase)
    {
      super();
      initUI();
    }
    
    public String getTitle()
    {
      return "Category Selection";
    }
    
    public boolean isPageValid()
    {
      return true;
    }
    
    private void initUI()
    {
      setLayout(new FormLayout("10px,pref,10px,90px,10px,200px,200px,10px", "10px,25px,10px,pref:grow,10px,25px,10px,25px,10px,25px,10px"));
      URL localURL = getClass().getResource("/dm/jb/images/syncprodwiz.png");
      ImageIcon localImageIcon = new ImageIcon(localURL);
      JLabel localJLabel = new JLabel(localImageIcon);
      CellConstraints localCellConstraints = new CellConstraints();
      localCellConstraints.xywh(2, 2, 1, 9, CellConstraints.FILL, CellConstraints.TOP);
      add(localJLabel, localCellConstraints);
      localJLabel = new JLabel("Department : ");
      localCellConstraints.xywh(4, 2, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
      add(localJLabel, localCellConstraints);
      this._mDeptSelect = new JComboBox();
      localCellConstraints.xywh(6, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
      add(this._mDeptSelect, localCellConstraints);
      this._mDeptSelect.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          ProductDetailsSyncPanel.SyncCatSelection.this.deptChanged();
        }
      });
      this._mCatSelect = new ShuttlePane(false);
      localCellConstraints.xywh(4, 4, 4, 1, CellConstraints.FILL, CellConstraints.FILL);
      add(this._mCatSelect, localCellConstraints);
      this._mCatSelect.addSelectionListener(this);
      this._mSelectProductAlso = new JCheckBox("Select products belonging to these categories.");
      localCellConstraints.xywh(4, 6, 4, 1, CellConstraints.LEFT, CellConstraints.FILL);
      add(this._mSelectProductAlso, localCellConstraints);
      String str = "";
      if (StoreInfoTableDef.getCurrentStore() != null) {
        str = "Select stock details in store - " + StoreInfoTableDef.getCurrentStore().getName();
      } else {
        str = "Select stock details in all stors.";
      }
      this._mSelectStoreStockAlso = new JCheckBox(str);
      localCellConstraints.xywh(4, 8, 4, 1, CellConstraints.LEFT, CellConstraints.FILL);
      add(this._mSelectStoreStockAlso, localCellConstraints);
      this._mSelectWarehouseStockAlso = new JCheckBox("Select Stock details in warehouse");
      localCellConstraints.xywh(4, 10, 4, 1, CellConstraints.LEFT, CellConstraints.FILL);
      add(this._mSelectWarehouseStockAlso, localCellConstraints);
    }
    
    void fillDeptList(ArrayList<DeptRow> paramArrayList)
    {
      Iterator localIterator = paramArrayList.iterator();
      while (localIterator.hasNext())
      {
        DeptRow localDeptRow = (DeptRow)localIterator.next();
        this._mDeptSelect.addItem(localDeptRow);
      }
      deptChanged();
    }
    
    void deptChanged()
    {
      DeptRow localDeptRow = (DeptRow)this._mDeptSelect.getSelectedItem();
      if (localDeptRow == null) {
        return;
      }
      try
      {
        this._mCatSelect.setFromList(localDeptRow.getCategoryList());
      }
      catch (DBException localDBException)
      {
        localDBException.printStackTrace();
      }
    }
    
    public void objectSelected(Object[] paramArrayOfObject) {}
    
    public boolean addToFromList(Object paramObject)
    {
      CategoryRow localCategoryRow = (CategoryRow)paramObject;
      DeptRow localDeptRow = (DeptRow)this._mDeptSelect.getSelectedItem();
      return localDeptRow.getDeptIndex() == localCategoryRow.getDeptIndex();
    }
    
    public boolean addToToList(Object paramObject)
    {
      return true;
    }
    
    public ArrayList<CategoryRow> getSelectedCategories()
    {
      return this._mCatSelect.getSelectedObjects();
    }
  }
  
  private class SyncDeptSelection
    extends WizardPanelBase
  {
    private ShuttlePane<DeptRow> _mDeptSelect = null;
    private JCheckBox _mSelectProductAlso = null;
    private JCheckBox _mSelectStoreStockAlso = null;
    private JCheckBox _mSelectWarehouseStockAlso = null;
    
    public SyncDeptSelection(WizardBase paramWizardBase)
    {
      super();
      initUI();
    }
    
    public boolean isPageValid()
    {
      return true;
    }
    
    public String getTitle()
    {
      return "Department Selection";
    }
    
    private void initUI()
    {
      setLayout(new FormLayout("10px,pref,10px, 500px,10px", "10px,pref:grow,10px,25px,10px,25px,10px,25px,10px"));
      this._mDeptSelect = new ShuttlePane(false);
      URL localURL = getClass().getResource("/dm/jb/images/syncprodwiz.png");
      ImageIcon localImageIcon = new ImageIcon(localURL);
      JLabel localJLabel = new JLabel(localImageIcon);
      CellConstraints localCellConstraints = new CellConstraints();
      localCellConstraints.xywh(2, 2, 1, 8, CellConstraints.FILL, CellConstraints.TOP);
      add(localJLabel, localCellConstraints);
      localCellConstraints.xywh(4, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
      add(this._mDeptSelect, localCellConstraints);
      this._mSelectProductAlso = new JCheckBox("Select products belonging to these departments.");
      localCellConstraints.xywh(4, 4, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
      add(this._mSelectProductAlso, localCellConstraints);
      String str = "";
      if (StoreInfoTableDef.getCurrentStore() != null) {
        str = "Select stock details in store - " + StoreInfoTableDef.getCurrentStore().getName();
      } else {
        str = "Select stock details in all stors.";
      }
      this._mSelectStoreStockAlso = new JCheckBox(str);
      localCellConstraints.xywh(4, 6, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
      add(this._mSelectStoreStockAlso, localCellConstraints);
      this._mSelectWarehouseStockAlso = new JCheckBox("Select Stock details in warehouse");
      localCellConstraints.xywh(4, 8, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
      add(this._mSelectWarehouseStockAlso, localCellConstraints);
    }
    
    private void fillDeptList(ArrayList<DeptRow> paramArrayList)
    {
      this._mDeptSelect.setFromList(paramArrayList);
    }
    
    ArrayList<DeptRow> getSelectedDepts()
    {
      return this._mDeptSelect.getSelectedObjects();
    }
  }
  
  public class WelcomePage
    extends WizardPanelBase
  {
    public WelcomePage(WizardBase paramWizardBase)
    {
      super();
      initUI();
    }
    
    public boolean isPageValid()
    {
      return true;
    }
    
    private void initUI()
    {
      setLayout(new FormLayout("10px,pref,10px,pref:grow,10px", "10px,pref:grow,10px"));
      URL localURL = getClass().getResource("/dm/jb/images/syncprodwiz.png");
      ImageIcon localImageIcon = new ImageIcon(localURL);
      JLabel localJLabel = new JLabel(localImageIcon);
      CellConstraints localCellConstraints = new CellConstraints();
      localCellConstraints.xywh(2, 2, 1, 1, CellConstraints.FILL, CellConstraints.TOP);
      add(localJLabel, localCellConstraints);
      JTextPane localJTextPane = new JTextPane();
      localJTextPane.setText("    Syncrhoniztation wizard will ensure that the data various sites of iSalePoint is same are are in perfectly sync. The wizard provides various modes of syncrhonization. \n\n   This window provides step by step guidance to sync the iSalePoint data at various sites.\n\nClick next to continue.");
      localJTextPane.setEditable(false);
      localCellConstraints.xywh(4, 2, 1, 1, CellConstraints.FILL, CellConstraints.CENTER);
      add(localJTextPane, localCellConstraints);
      Font localFont = localJTextPane.getFont();
      localFont = new Font(localFont.getName(), localFont.getStyle() | 0x1, 13);
      localJTextPane.setFont(localFont);
      localJTextPane.setPreferredSize(new Dimension(300, 200));
      localJTextPane.setOpaque(false);
    }
    
    public String getTitle()
    {
      return "Welcome";
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.settings.ProductDetailsSyncPanel
 * JD-Core Version:    0.7.0.1
 */