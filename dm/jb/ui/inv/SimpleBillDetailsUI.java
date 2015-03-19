package dm.jb.ui.inv;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.db.objects.Payment;
import dm.jb.db.objects.SiteInfoRow;
import dm.jb.db.objects.StockAndProductRow;
import dm.jb.db.objects.StoreInfoRow;
import dm.jb.op.bill.Bill;
import dm.jb.op.bill.BillProduct;
import dm.jb.ui.billing.PaymentModeObject;
import dm.jb.ui.common.HelpButton;
import dm.jb.ui.settings.CountryInfo;
import dm.tools.types.InternalAmount;
import dm.tools.types.InternalQuantity;
import dm.tools.ui.AbstractMainPanel;
import dm.tools.ui.components.JTextSeparator;
import dm.tools.utils.CommonConfig;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class SimpleBillDetailsUI
  extends AbstractMainPanel
{
  public static SimpleBillDetailsUI INSTANCE = new SimpleBillDetailsUI();
  private JTextField _mTxnNo = null;
  private JTextField _mTxnDate = null;
  private JTextField _mStore = null;
  private JTextField _mSite = null;
  private SimpleBillTableModel _mBillModel = null;
  private JTable _mBillTable = null;
  private PaymentTableModel _mPaymentModel = null;
  private JTable _mPaymentTable = null;
  private JTextField _mDiscount = null;
  private JTextField _mTax = null;
  private JTextField _mTotal = null;
  
  private SimpleBillDetailsUI()
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
    FormLayout localFormLayout = new FormLayout("10px,90px,10px,120px,10px, 60px,10px,70px, 90px,10px,83px,37px,3px,60px,10px", "10px,25px,10px,25px,10px,25px,10px,200px:grow,10px,30px,25px,10px,25px,10px,25px,20px,30px,10px");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    int i = 2;
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.CENTER);
    add(new JLabel("Transaction No.: "), localCellConstraints);
    this._mTxnNo = new JTextField();
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mTxnNo, localCellConstraints);
    this._mTxnNo.setEditable(false);
    this._mTxnNo.setToolTipText("Transaction number");
    i += 2;
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.CENTER);
    add(new JLabel("Date : "), localCellConstraints);
    this._mTxnDate = new JTextField();
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mTxnDate, localCellConstraints);
    this._mTxnDate.setEditable(false);
    this._mTxnDate.setToolTipText("Transaction date");
    i += 2;
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.CENTER);
    add(new JLabel("Store : "), localCellConstraints);
    this._mStore = new JTextField();
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mStore, localCellConstraints);
    this._mStore.setEditable(false);
    this._mStore.setToolTipText("Store");
    localCellConstraints.xywh(6, i, 1, 1, CellConstraints.RIGHT, CellConstraints.CENTER);
    add(new JLabel("Terminal : "), localCellConstraints);
    this._mSite = new JTextField();
    localCellConstraints.xywh(8, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mSite, localCellConstraints);
    this._mSite.setToolTipText("Terminal which created this transaction");
    this._mSite.setEditable(false);
    i += 2;
    this._mBillModel = new SimpleBillTableModel(null);
    this._mBillTable = new JTable(this._mBillModel);
    localCellConstraints.xywh(2, i, localFormLayout.getColumnCount() - 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(new JScrollPane(this._mBillTable), localCellConstraints);
    this._mBillTable.setAutoResizeMode(0);
    this._mBillTable.setToolTipText("Transaction items");
    this._mBillTable.getColumnModel().getColumn(0).setPreferredWidth(50);
    this._mBillTable.getColumnModel().getColumn(1).setPreferredWidth(80);
    this._mBillTable.getColumnModel().getColumn(2).setPreferredWidth(150);
    this._mBillTable.getColumnModel().getColumn(5).setPreferredWidth(90);
    this._mBillTable.getColumnModel().getColumn(6).setPreferredWidth(90);
    this._mBillTable.getColumnModel().getColumn(7).setPreferredWidth(90);
    i += 2;
    localCellConstraints.xywh(2, i, 6, 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(new JTextSeparator("Payment Details "), localCellConstraints);
    this._mPaymentModel = new PaymentTableModel(null);
    this._mPaymentTable = new JTable(this._mPaymentModel);
    this._mPaymentTable.setToolTipText("Payment details");
    i++;
    localCellConstraints.xywh(2, i, 6, 5, CellConstraints.FILL, CellConstraints.FILL);
    add(new JScrollPane(this._mPaymentTable), localCellConstraints);
    localCellConstraints.xywh(9, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(new JLabel("Addl Discount : "), localCellConstraints);
    this._mDiscount = new JTextField();
    this._mDiscount.setToolTipText("Additional discount");
    localCellConstraints.xywh(11, i, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mDiscount, localCellConstraints);
    this._mDiscount.setEditable(false);
    localCellConstraints.xywh(14, i, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    add(new JLabel(CommonConfig.getInstance().country.currency), localCellConstraints);
    this._mDiscount.setHorizontalAlignment(4);
    i += 2;
    localCellConstraints.xywh(9, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(new JLabel("Addl Tax : "), localCellConstraints);
    this._mTax = new JTextField();
    this._mTax.setToolTipText("Additional Tax");
    localCellConstraints.xywh(11, i, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mTax, localCellConstraints);
    this._mTax.setEditable(false);
    localCellConstraints.xywh(14, i, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    add(new JLabel(CommonConfig.getInstance().country.currency), localCellConstraints);
    this._mTax.setHorizontalAlignment(4);
    i += 2;
    localCellConstraints.xywh(9, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(new JLabel("Total : "), localCellConstraints);
    this._mTotal = new JTextField();
    this._mTotal.setToolTipText("Total amount");
    localCellConstraints.xywh(11, i, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mTotal, localCellConstraints);
    this._mTotal.setEditable(false);
    localCellConstraints.xywh(14, i, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    add(new JLabel(CommonConfig.getInstance().country.currency), localCellConstraints);
    this._mTotal.setHorizontalAlignment(4);
    i++;
    localCellConstraints.xywh(1, i, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(new JSeparator(), localCellConstraints);
    i++;
    localCellConstraints.xywh(2, i, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    Object localObject = new JButton("Close");
    ((JButton)localObject).setToolTipText("Close the window");
    add((Component)localObject, localCellConstraints);
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        SimpleBillDetailsUI.this.closeWindow();
      }
    });
    localCellConstraints.xywh(12, i, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
    localObject = new HelpButton("ISP_VIEW_BILL_ADMIN");
    add((Component)localObject, localCellConstraints);
    ((JButton)localObject).setToolTipText("Help");
  }
  
  public void setBill(Bill paramBill)
  {
    this._mTxnNo.setText(paramBill.getBillNo() + "");
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(CommonConfig.getInstance().dateFormat);
    this._mTxnDate.setText(localSimpleDateFormat.format(paramBill.getBillDate()));
    String str1 = "NA";
    if (paramBill.getStore() != null) {
      str1 = paramBill.getStore().toString();
    }
    String str2 = "NA";
    if (paramBill.getSite() != null) {
      str2 = paramBill.getSite().getSiteId();
    }
    this._mStore.setText(str1);
    this._mSite.setText(str2);
    ArrayList localArrayList = paramBill.getBillEntries();
    Object localObject1 = localArrayList.iterator();
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = (BillProduct)((Iterator)localObject1).next();
      this._mBillModel.addBillEntry((BillProduct)localObject2);
    }
    localObject1 = paramBill.getPayment();
    Object localObject2 = ((Payment)localObject1).getPaymentObjects();
    this._mPaymentModel.removeAllPaymentRows();
    Iterator localIterator = ((ArrayList)localObject2).iterator();
    while (localIterator.hasNext())
    {
      PaymentModeObject localPaymentModeObject = (PaymentModeObject)localIterator.next();
      this._mPaymentModel.addPaymentRow(localPaymentModeObject);
    }
    this._mDiscount.setText(InternalAmount.toString(paramBill.getFinalDiscount()));
    this._mTax.setText(InternalAmount.toString(paramBill.getFinalTax()));
    this._mTotal.setText(InternalAmount.toString(paramBill.getFinalAmount()));
  }
  
  private class PaymentTableModel
    extends DefaultTableModel
  {
    String[] columnNames = { "Payment Mode", "Amount (" + CommonConfig.getInstance().country.currency + ")" };
    
    private PaymentTableModel() {}
    
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
      return paramInt == 0 ? String.class : Integer.class;
    }
    
    public void addPaymentRow(PaymentModeObject paramPaymentModeObject)
    {
      Object[] arrayOfObject = { paramPaymentModeObject.getPaymentOption().toString(), InternalAmount.toString(paramPaymentModeObject.getAmount()) };
      addRow(arrayOfObject);
    }
    
    public void removeAllPaymentRows()
    {
      int i = getRowCount();
      for (int j = 0; j < i; j++) {
        removeRow(0);
      }
    }
  }
  
  private class SimpleBillTableModel
    extends DefaultTableModel
  {
    private String[] columnNames = { "Sl No.", "Product Code", "Product Name", "Quantity", "Price (" + CommonConfig.getInstance().country.currency + ")", "Discount (" + CommonConfig.getInstance().country.currency + ")", "Tax (" + CommonConfig.getInstance().country.currency + ")", "Amount (" + CommonConfig.getInstance().country.currency + ")" };
    
    private SimpleBillTableModel() {}
    
    public String getColumnName(int paramInt)
    {
      return this.columnNames[paramInt];
    }
    
    public Class getColumnClass(int paramInt)
    {
      if ((paramInt == 0) || (paramInt == 3) || (paramInt == 4) || (paramInt == 5) || (paramInt == 6) || (paramInt == 7)) {
        return Integer.class;
      }
      return Object.class;
    }
    
    public int getColumnCount()
    {
      return this.columnNames.length;
    }
    
    public boolean isCellEditable(int paramInt1, int paramInt2)
    {
      return false;
    }
    
    void addBillEntry(BillProduct paramBillProduct)
    {
      int i = getRowCount();
      Object[] arrayOfObject = { Integer.valueOf(i + 1), "  " + (paramBillProduct.getStockAndProduct() == null ? "NA" : paramBillProduct.getStockAndProduct().getProductCode()), paramBillProduct.getStockAndProduct() == null ? "NA" : paramBillProduct.getStockAndProduct().getProdName(), InternalQuantity.toString(paramBillProduct.getQuantity(), paramBillProduct.getStockAndProduct() == null ? -1 : (short)paramBillProduct.getStockAndProduct().getProdUnit(), true), InternalAmount.toString(paramBillProduct.getAmount()), InternalAmount.toString(paramBillProduct.getDiscount()), InternalAmount.toString(paramBillProduct.getTax()), InternalAmount.toString(paramBillProduct.getTotalAmount()) };
      addRow(arrayOfObject);
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.inv.SimpleBillDetailsUI
 * JD-Core Version:    0.7.0.1
 */