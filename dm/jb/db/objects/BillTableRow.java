package dm.jb.db.objects;

import dm.jb.db.gen.BillTableBaseRow;
import dm.tools.db.DBException;
import java.util.ArrayList;
import java.util.Iterator;

public class BillTableRow
  extends BillTableBaseRow
{
  private ArrayList<BillEntryRow> billEntries = null;
  private Payment _mPayment = null;
  private CustomerRow custRow = null;
  
  BillTableRow(int paramInt, BillTableTableDef paramBillTableTableDef)
  {
    super(paramInt, paramBillTableTableDef);
  }
  
  public void create()
    throws DBException
  {
    if ((this.custRow != null) && (this.custRow.getCustIndex() != -1))
    {
      setCustIndex(this.custRow.getCustIndex());
    }
    else if (this.custRow != null)
    {
      this.custRow.create();
      setCustIndex(this.custRow.getCustIndex());
    }
    super.create();
    if (this.billEntries == null) {
      return;
    }
    Iterator localIterator = this.billEntries.iterator();
    int i = getBillNo();
    while (localIterator.hasNext())
    {
      BillEntryRow localBillEntryRow = (BillEntryRow)localIterator.next();
      localBillEntryRow.setBillNo(i);
      localBillEntryRow.create();
    }
  }
  
  public void update(boolean paramBoolean)
    throws DBException
  {
    if ((this.custRow != null) && (this.custRow.getCustIndex() != -1))
    {
      setCustIndex(this.custRow.getCustIndex());
      this.custRow.update(paramBoolean);
    }
    else if (this.custRow != null)
    {
      this.custRow.create();
      setCustIndex(this.custRow.getCustIndex());
    }
    super.update(paramBoolean);
    if (this.billEntries == null) {
      return;
    }
    Iterator localIterator = this.billEntries.iterator();
    int i = getBillNo();
    while (localIterator.hasNext())
    {
      BillEntryRow localBillEntryRow = (BillEntryRow)localIterator.next();
      localBillEntryRow.setBillNo(i);
      localBillEntryRow.update(paramBoolean);
    }
  }
  
  public Payment getPayment()
    throws DBException
  {
    if (this._mPayment != null) {
      return this._mPayment;
    }
    this._mPayment = Payment.getPayment();
    return this._mPayment;
  }
  
  public CustomerRow getCustomerRow()
    throws DBException
  {
    int i = getCustIndex();
    if (i == -1) {
      return null;
    }
    CustomerRow localCustomerRow = CustomerTableDef.getInstance().findRowByIndex(i);
    return localCustomerRow;
  }
  
  public ArrayList<BillEntryRow> getBillEntries()
    throws DBException
  {
    if (this.billEntries == null) {
      this.billEntries = BillEntryTableDef.getInstance().getBillEntriesForBillNo(getBillNo());
    }
    return this.billEntries;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.BillTableRow
 * JD-Core Version:    0.7.0.1
 */