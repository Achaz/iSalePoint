package dm.jb.ui.inv;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.db.objects.ProductRow;
import dm.jb.db.objects.SelectedWhtostoreRow;
import dm.jb.db.objects.SelectedWhtostoreTableDef;
import dm.jb.db.objects.StoreStockRow;
import dm.jb.db.objects.StoreStockTableDef;
import dm.jb.db.objects.WearehouseInfoRow;
import dm.jb.db.objects.WearehouseInfoTableDef;
import dm.jb.printing.JePrinterException;
import dm.jb.printing.Print;
import dm.jb.ui.MainWindow;
import dm.jb.ui.common.HelpButton;
import dm.tools.db.DBConnection;
import dm.tools.db.DBException;
import dm.tools.db.Db;
import dm.tools.types.InternalQuantity;
import dm.tools.ui.AbstractMainPanel;
import dm.tools.ui.ProgressWindow;
import dm.tools.ui.ProgressWindowAction;
import dm.tools.ui.UICommon;
import dm.tools.utils.CommonConfig;
import dm.tools.utils.SafeMath;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class WHToStoreRecordView
  extends AbstractMainPanel
{
  public static WHToStoreRecordView INSTANCE = new WHToStoreRecordView();
  private JTextField _mTxnNo = null;
  private JTextField _mDate = null;
  private JTable _mTable = null;
  private StoreListTableModel _mModel = null;
  private int[] _mProductId = null;
  private double[] _mQty = null;
  private ProductRow[] _mProducts = null;
  private boolean[] _mUpdated = null;
  private boolean[] _mUpdatedInDb = null;
  private Date _mCreateDate = null;
  private int _mTxnNoId = 0;
  private boolean _mFoundUpdated = false;
  private ArrayList<SelectedWhtostoreRow> _mSelectedInDBRows = null;
  
  private WHToStoreRecordView()
  {
    initUI();
  }
  
  public void setDefaultFocus() {}
  
  public JPanel getPanel()
  {
    return this;
  }
  
  public void windowDisplayed() {}
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("10px,602px,10px", "10px,pref,20px,pref:grow,20px,30px,10px");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(1, 2, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getDetailsPanel(), localCellConstraints);
    this._mModel = new StoreListTableModel(null);
    this._mTable = new JTable(this._mModel);
    localCellConstraints.xywh(2, 4, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(new JScrollPane(this._mTable), localCellConstraints);
    this._mTable.setAutoResizeMode(0);
    this._mTable.getColumnModel().getColumn(0).setPreferredWidth(50);
    this._mTable.getColumnModel().getColumn(1).setPreferredWidth(50);
    this._mTable.getColumnModel().getColumn(2).setPreferredWidth(100);
    this._mTable.getColumnModel().getColumn(3).setPreferredWidth(150);
    this._mTable.getColumnModel().getColumn(4).setPreferredWidth(100);
    this._mTable.getColumnModel().getColumn(5).setPreferredWidth(150);
    localCellConstraints.xywh(1, 5, 3, 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(new JSeparator(), localCellConstraints);
    localCellConstraints.xywh(1, 6, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getButtonPanel(), localCellConstraints);
  }
  
  private JPanel getDetailsPanel()
  {
    JPanel localJPanel = new JPanel();
    localJPanel.setLayout(new FormLayout("10px,90px,10px,150px,10px", "25px,10px,25px"));
    CellConstraints localCellConstraints = new CellConstraints();
    JLabel localJLabel = new JLabel("Transaction No : ");
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mTxnNo = new JTextField();
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mTxnNo, localCellConstraints);
    this._mTxnNo.setEditable(false);
    localJLabel = new JLabel("Date : ");
    localCellConstraints.xywh(2, 3, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mDate = new JTextField();
    localCellConstraints.xywh(4, 3, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mDate, localCellConstraints);
    this._mDate.setEditable(false);
    return localJPanel;
  }
  
  private JPanel getButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    localJPanel.setLayout(new FormLayout("10px,100px,10px,100px,10px,100px,pref:grow,100px,10px", "30px"));
    CellConstraints localCellConstraints = new CellConstraints();
    Object localObject = new JButton("Update");
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        WHToStoreRecordView.this.updateClicked();
      }
    });
    localObject = new JButton("Print");
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        WHToStoreRecordView.this.printClicked();
      }
    });
    localObject = new JButton("Close");
    localCellConstraints.xywh(6, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        WHToStoreRecordView.this.closeWindow();
      }
    });
    localObject = new HelpButton("ISP_VIEW_WH_TO_STORE");
    localCellConstraints.xywh(8, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    return localJPanel;
  }
  
  public void setData(int paramInt, Date paramDate, int[] paramArrayOfInt1, double[] paramArrayOfDouble, ProductRow[] paramArrayOfProductRow, int[] paramArrayOfInt2)
    throws DBException
  {
    ArrayList localArrayList = SelectedWhtostoreTableDef.getInstance().getAllValuesWithWhereClause("TXN_NO=" + paramInt);
    if ((localArrayList != null) && (localArrayList.size() < paramArrayOfInt1.length) && (localArrayList.size() != 0)) {
      throw new DBException("Less number of values for the selected whtostore transaction details, than the count passed.", "May be internal Error", "try again", null, null);
    }
    this._mUpdatedInDb = new boolean[paramArrayOfInt1.length];
    if (this._mSelectedInDBRows == null) {
      this._mSelectedInDBRows = new ArrayList();
    } else {
      this._mSelectedInDBRows.clear();
    }
    for (int i = 0; i < this._mUpdatedInDb.length; i++) {
      this._mUpdatedInDb[i] = false;
    }
    if ((localArrayList != null) && (localArrayList.size() > 0))
    {
      this._mFoundUpdated = true;
      for (i = 0; i < this._mUpdatedInDb.length; i++)
      {
        SelectedWhtostoreRow localSelectedWhtostoreRow = (SelectedWhtostoreRow)localArrayList.get(i);
        this._mUpdatedInDb[localSelectedWhtostoreRow.getSlNo()] = localSelectedWhtostoreRow.getSelected().equalsIgnoreCase("Y");
        this._mSelectedInDBRows.add(localSelectedWhtostoreRow);
      }
      localArrayList.clear();
      localArrayList = null;
    }
    else
    {
      this._mFoundUpdated = false;
    }
    this._mProductId = paramArrayOfInt1;
    this._mTxnNoId = paramInt;
    this._mQty = paramArrayOfDouble;
    this._mTable.clearSelection();
    this._mModel.removeAllRows();
    this._mTxnNo.setText(paramInt + "");
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(CommonConfig.getInstance().dateFormat);
    this._mDate.setText(localSimpleDateFormat.format(paramDate));
    this._mProducts = paramArrayOfProductRow;
    for (int j = 0; j < paramArrayOfDouble.length; j++)
    {
      WearehouseInfoRow localWearehouseInfoRow = WearehouseInfoTableDef.getInstance().getWarehouseForIndex(paramArrayOfInt2[j]);
      this._mModel.addDataRow(j + 1, paramArrayOfDouble[j], paramArrayOfProductRow[j], localWearehouseInfoRow, this._mUpdatedInDb[j]);
    }
    this._mCreateDate = paramDate;
  }
  
  private void updateClicked()
  {
    ProgressWindowAction local4 = new ProgressWindowAction()
    {
      public void startAction()
      {
        DBConnection localDBConnection = Db.getConnection();
        try
        {
          localDBConnection.openTrans();
          for (int i = 0; i < WHToStoreRecordView.this._mProductId.length; i++)
          {
            Object localObject1;
            if (WHToStoreRecordView.this._mModel.isSelected(i))
            {
              localObject1 = StoreStockTableDef.getInstance().getStockForProductInCurrentStore(WHToStoreRecordView.this._mProductId[i]);
              double d = SafeMath.safeAdd(((StoreStockRow)localObject1).getStock(), WHToStoreRecordView.this._mQty[i]);
              ((StoreStockRow)localObject1).setStock(d);
              ((StoreStockRow)localObject1).update(true);
              Object localObject2;
              if (WHToStoreRecordView.this._mFoundUpdated)
              {
                localObject2 = WHToStoreRecordView.this._mSelectedInDBRows.iterator();
                while (((Iterator)localObject2).hasNext())
                {
                  SelectedWhtostoreRow localSelectedWhtostoreRow = (SelectedWhtostoreRow)((Iterator)localObject2).next();
                  if (localSelectedWhtostoreRow.getSlNo() == i)
                  {
                    localSelectedWhtostoreRow.setSelected("Y");
                    localSelectedWhtostoreRow.update(true);
                    WHToStoreRecordView.this._mUpdatedInDb[localSelectedWhtostoreRow.getSlNo()] = 1;
                    WHToStoreRecordView.this._mModel.setSelected(i, false);
                    break;
                  }
                }
              }
              else
              {
                localObject2 = SelectedWhtostoreTableDef.getInstance().getNewRow();
                ((SelectedWhtostoreRow)localObject2).setValues(WHToStoreRecordView.this._mTxnNoId, i, "Y");
                ((SelectedWhtostoreRow)localObject2).create();
                WHToStoreRecordView.this._mUpdatedInDb[localObject2.getSlNo()] = 1;
                WHToStoreRecordView.this._mModel.setSelected(i, false);
              }
            }
            else if (!WHToStoreRecordView.this._mFoundUpdated)
            {
              localObject1 = SelectedWhtostoreTableDef.getInstance().getNewRow();
              ((SelectedWhtostoreRow)localObject1).setValues(WHToStoreRecordView.this._mTxnNoId, i, "N");
              ((SelectedWhtostoreRow)localObject1).create();
            }
          }
          localDBConnection.endTrans();
          UICommon.showDelayedMessage("Stock updated successfully", "Success", MainWindow.instance);
        }
        catch (DBException localDBException)
        {
          localDBConnection.rollbackNoExp();
          localDBException.printStackTrace();
          UICommon.showDelayedErrorMessage("Internal error updating stock.\n\nTry again later. If the problem persists contact administrator.", "Internal Error", MainWindow.instance);
          return;
        }
      }
      
      public String getActionName()
      {
        return "Update store stock";
      }
    };
    ProgressWindow localProgressWindow = new ProgressWindow(MainWindow.instance, local4);
    localProgressWindow.startProgress();
  }
  
  private void printClicked()
  {
    this._mUpdated = new boolean[this._mProducts.length];
    for (int i = 0; i < this._mProducts.length; i++) {
      this._mUpdated[i] = this._mModel.isSelected(i);
    }
    try
    {
      Print.getInstance().printWhToStoreView(this._mProducts, this._mQty, this._mCreateDate, this._mTxnNoId, this._mUpdated);
    }
    catch (JePrinterException localJePrinterException)
    {
      localJePrinterException.printStackTrace();
      UICommon.showError("Error printing.\nDetails:\n" + localJePrinterException.toString(), "Printing Error", MainWindow.instance);
      return;
    }
  }
  
  private class StoreListTableModel
    extends DefaultTableModel
  {
    private String[] columnNames = { "Sl. No.", "Select", "Product Code", "Product Name", "Quantity", "Warehouse" };
    
    private StoreListTableModel() {}
    
    public int getColumnCount()
    {
      return this.columnNames.length;
    }
    
    public String getColumnName(int paramInt)
    {
      return this.columnNames[paramInt];
    }
    
    public Class getColumnClass(int paramInt)
    {
      if (paramInt == 1) {
        return Boolean.class;
      }
      if ((paramInt == 1) || (paramInt == 4)) {
        return Integer.class;
      }
      return String.class;
    }
    
    public boolean isCellEditable(int paramInt1, int paramInt2)
    {
      return (paramInt2 == 1) && (WHToStoreRecordView.this._mUpdatedInDb[paramInt1] == 0);
    }
    
    private void removeAllRows()
    {
      int i = getRowCount();
      for (int j = 0; j < i; j++) {
        removeRow(0);
      }
    }
    
    private void addDataRow(int paramInt, double paramDouble, ProductRow paramProductRow, WearehouseInfoRow paramWearehouseInfoRow, boolean paramBoolean)
    {
      Object[] arrayOfObject = { Integer.valueOf(paramInt), Boolean.valueOf(!paramBoolean ? 1 : false), paramProductRow.getProductCode(), paramProductRow.getProdName(), InternalQuantity.toString(paramDouble, (short)paramProductRow.getProdUnit(), true), paramWearehouseInfoRow };
      addRow(arrayOfObject);
    }
    
    private boolean isSelected(int paramInt)
    {
      return ((Boolean)getValueAt(paramInt, 1)).booleanValue();
    }
    
    private void setSelected(int paramInt, boolean paramBoolean)
    {
      setValueAt(Boolean.valueOf(paramBoolean), paramInt, 1);
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.inv.WHToStoreRecordView
 * JD-Core Version:    0.7.0.1
 */