package dm.jb.ui.billing;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.db.objects.CustomerRow;
import dm.jb.ui.common.JBShadowPanel;
import dm.jb.ui.common.JBTransparentWindow;
import dm.tools.ui.NonEditableJXTable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class CustomerListWindow
  extends JBTransparentWindow
{
  private NonEditableJXTable _mTable = null;
  private TableModel _mModel = null;
  private CustomerRow _mSelectedRow = null;
  JDialog _mParent = null;
  private JBShadowPanel _mShadowPanel = null;
  
  public CustomerListWindow(JDialog paramJDialog)
  {
    super(paramJDialog, true);
    this._mParent = paramJDialog;
    initUI();
    pack();
    addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 27) {
          CustomerListWindow.this.setVisible(false);
        }
      }
    });
    this._mTable.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 27) {
          CustomerListWindow.this.setVisible(false);
        }
      }
    });
    this._mTable.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 10)
        {
          int[] arrayOfInt = CustomerListWindow.this._mTable.getSelectedRows();
          if (arrayOfInt.length == 0)
          {
            CustomerListWindow.this._mSelectedRow = null;
            return;
          }
          int i = CustomerListWindow.this._mTable.convertRowIndexToModel(arrayOfInt[0]);
          CustomerRow localCustomerRow = (CustomerRow)CustomerListWindow.this._mModel.getValueAt(i, 0);
          CustomerListWindow.this._mSelectedRow = localCustomerRow;
          if ((CustomerListWindow.this._mParent != null) && ((CustomerListWindow.this._mParent instanceof CustomerListAction))) {
            ((CustomerListAction)CustomerListWindow.this._mParent).selected(localCustomerRow);
          }
          CustomerListWindow.this.setVisible(false);
        }
      }
    });
  }
  
  public CustomerRow getSelectedRow()
  {
    return this._mSelectedRow;
  }
  
  public void initUI()
  {
    JPanel localJPanel = (JPanel)getContentPane();
    this._mShadowPanel = new JBShadowPanel(new Color(255, 187, 119));
    JBShadowPanel localJBShadowPanel = this._mShadowPanel;
    localJPanel.setLayout(new BorderLayout());
    localJPanel.add(localJBShadowPanel, "Center");
    localJBShadowPanel.setBackground(new Color(100, 200, 100));
    FormLayout localFormLayout = new FormLayout("500px", "200px");
    localJBShadowPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    this._mTable = new NonEditableJXTable();
    this._mTable.setColumnControlVisible(false);
    this._mModel = new TableModel();
    this._mTable.setModel(this._mModel);
    this._mTable.setMinimumSize(new Dimension(300, 100));
    JScrollPane localJScrollPane = new JScrollPane(this._mTable);
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJBShadowPanel.add(localJScrollPane, localCellConstraints);
    CustomerNameRenderer localCustomerNameRenderer = new CustomerNameRenderer();
    this._mTable.setDefaultRenderer(Object.class, localCustomerNameRenderer);
    this._mTable.setRowHeight(25);
    setColumnWidths();
  }
  
  public void clearItems()
  {
    this._mModel.clearAllIterms();
    this._mSelectedRow = null;
  }
  
  public void showValues(ArrayList<CustomerRow> paramArrayList)
  {
    this._mModel.populate(paramArrayList);
  }
  
  private void setColumnWidths()
  {
    TableColumnModel localTableColumnModel = this._mTable.getColumnModel();
    localTableColumnModel.getColumn(0).setPreferredWidth(60);
    localTableColumnModel.getColumn(0).setHeaderRenderer(new TableCellRenderer()
    {
      public Component getTableCellRendererComponent(JTable paramAnonymousJTable, Object paramAnonymousObject, boolean paramAnonymousBoolean1, boolean paramAnonymousBoolean2, int paramAnonymousInt1, int paramAnonymousInt2)
      {
        JTableHeader localJTableHeader = CustomerListWindow.this._mTable.getTableHeader();
        Font localFont1 = localJTableHeader.getFont();
        Font localFont2 = new Font(localFont1.getName(), 1, 14);
        TableCellRenderer localTableCellRenderer = localJTableHeader.getDefaultRenderer();
        Component localComponent = localTableCellRenderer.getTableCellRendererComponent(paramAnonymousJTable, paramAnonymousObject, paramAnonymousBoolean1, paramAnonymousBoolean2, paramAnonymousInt1, paramAnonymousInt2);
        localComponent.setFont(localFont2);
        return localComponent;
      }
    });
    localTableColumnModel.getColumn(1).setPreferredWidth(130);
    localTableColumnModel.getColumn(1).setHeaderRenderer(new TableCellRenderer()
    {
      public Component getTableCellRendererComponent(JTable paramAnonymousJTable, Object paramAnonymousObject, boolean paramAnonymousBoolean1, boolean paramAnonymousBoolean2, int paramAnonymousInt1, int paramAnonymousInt2)
      {
        JTableHeader localJTableHeader = CustomerListWindow.this._mTable.getTableHeader();
        Font localFont1 = localJTableHeader.getFont();
        Font localFont2 = new Font(localFont1.getName(), 1, 14);
        TableCellRenderer localTableCellRenderer = localJTableHeader.getDefaultRenderer();
        Component localComponent = localTableCellRenderer.getTableCellRendererComponent(paramAnonymousJTable, paramAnonymousObject, paramAnonymousBoolean1, paramAnonymousBoolean2, paramAnonymousInt1, paramAnonymousInt2);
        localComponent.setFont(localFont2);
        return localComponent;
      }
    });
    localTableColumnModel.getColumn(2).setPreferredWidth(80);
    localTableColumnModel.getColumn(2).setHeaderRenderer(new TableCellRenderer()
    {
      public Component getTableCellRendererComponent(JTable paramAnonymousJTable, Object paramAnonymousObject, boolean paramAnonymousBoolean1, boolean paramAnonymousBoolean2, int paramAnonymousInt1, int paramAnonymousInt2)
      {
        JTableHeader localJTableHeader = CustomerListWindow.this._mTable.getTableHeader();
        Font localFont1 = localJTableHeader.getFont();
        Font localFont2 = new Font(localFont1.getName(), 1, 14);
        TableCellRenderer localTableCellRenderer = localJTableHeader.getDefaultRenderer();
        Component localComponent = localTableCellRenderer.getTableCellRendererComponent(paramAnonymousJTable, paramAnonymousObject, paramAnonymousBoolean1, paramAnonymousBoolean2, paramAnonymousInt1, paramAnonymousInt2);
        localComponent.setFont(localFont2);
        return localComponent;
      }
    });
  }
  
  class CustomerNameRenderer
    extends DefaultTableCellRenderer
  {
    CustomerNameRenderer() {}
    
    public Component getTableCellRendererComponent(JTable paramJTable, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2)
    {
      super.getTableCellRendererComponent(paramJTable, paramObject, paramBoolean1, paramBoolean2, paramInt1, paramInt2);
      Font localFont1 = getFont();
      Font localFont2 = new Font(localFont1.getName(), localFont1.getStyle() | 0x1, 16);
      setFont(localFont2);
      if (paramInt2 == 0)
      {
        setHorizontalAlignment(4);
        if (!paramBoolean1) {
          setBackground(new Color(39, 152, 79));
        } else {
          setBackground(Color.blue);
        }
      }
      else
      {
        setHorizontalAlignment(2);
        if (!paramBoolean1) {
          setBackground(new Color(39, 152, 79));
        } else {
          setBackground(Color.blue);
        }
      }
      return this;
    }
  }
  
  private class TableModel
    extends DefaultTableModel
  {
    int _mRowCount = 0;
    
    public TableModel() {}
    
    public int getColumnCount()
    {
      return 3;
    }
    
    public void populate(ArrayList<CustomerRow> paramArrayList)
    {
      clearAllIterms();
      CustomerListWindow.this._mSelectedRow = null;
      if (paramArrayList == null) {
        this._mRowCount = 0;
      }
      Iterator localIterator = paramArrayList.iterator();
      while (localIterator.hasNext())
      {
        Object[] arrayOfObject = new Object[3];
        CustomerRow localCustomerRow = (CustomerRow)localIterator.next();
        arrayOfObject[0] = localCustomerRow;
        arrayOfObject[1] = localCustomerRow.getCustName();
        arrayOfObject[2] = localCustomerRow.getCustPhone();
        this._mRowCount += 1;
        insertRow(this._mRowCount - 1, arrayOfObject);
      }
    }
    
    public int getRowCount()
    {
      return this._mRowCount;
    }
    
    public void clearAllIterms()
    {
      for (int i = 0; i < this._mRowCount; i++) {
        removeRow(0);
      }
      this._mRowCount = 0;
    }
    
    public String getColumnName(int paramInt)
    {
      if (paramInt == 0) {
        return "Customer ID ";
      }
      if (paramInt == 1) {
        return "Customer Name ";
      }
      if (paramInt == 2) {
        return "Phone";
      }
      return "NO VALUE- REPORT THIS";
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.billing.CustomerListWindow
 * JD-Core Version:    0.7.0.1
 */