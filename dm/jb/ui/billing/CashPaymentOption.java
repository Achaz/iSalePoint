package dm.jb.ui.billing;

import dm.jb.db.objects.CashTxnRow;
import dm.jb.db.objects.CashTxnTableDef;
import dm.tools.db.BindObject;
import dm.tools.db.DBException;
import dm.tools.db.DBRow;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JPanel;

public class CashPaymentOption
  implements PaymentOption
{
  private CashPaymentOptionPanel _mPanel = null;
  
  public JPanel getOptionPanel()
  {
    return this._mPanel;
  }
  
  public String getCode()
  {
    return "CS";
  }
  
  public String toString()
  {
    return "Cash";
  }
  
  public PaymentModeObject getNewPaymentObjectForBillPay(int paramInt)
  {
    java.util.Date localDate = new java.util.Date();
    CashPaymentObject localCashPaymentObject = new CashPaymentObject(new java.sql.Date(localDate.getTime()), new Time(localDate.getTime()), this, "BP", paramInt);
    localCashPaymentObject._mAmount = this._mPanel.getAmount();
    return localCashPaymentObject;
  }
  
  public PaymentModeObject getNewPaymentObjectForBillRefund(int paramInt)
  {
    java.util.Date localDate = new java.util.Date();
    CashPaymentObject localCashPaymentObject = new CashPaymentObject(new java.sql.Date(localDate.getTime()), new Time(localDate.getTime()), this, "RF", paramInt);
    localCashPaymentObject._mAmount = this._mPanel.getAmount();
    return localCashPaymentObject;
  }
  
  public PaymentModeObject getNewPaymentObjectForBillAddl(int paramInt)
  {
    java.util.Date localDate = new java.util.Date();
    CashPaymentObject localCashPaymentObject = new CashPaymentObject(new java.sql.Date(localDate.getTime()), new Time(localDate.getTime()), this, "AP", paramInt);
    localCashPaymentObject._mAmount = this._mPanel.getAmount();
    return localCashPaymentObject;
  }
  
  public ArrayList<PaymentModeObject> getPayments(long paramLong, int paramInt)
    throws DBException
  {
    CashTxnTableDef localCashTxnTableDef = CashTxnTableDef.getInstance();
    ArrayList localArrayList1 = localCashTxnTableDef.getAllValuesWithWhereClause("BILL_NO =" + paramLong + " AND STORE_ID=" + paramInt);
    if ((localArrayList1 == null) || (localArrayList1.size() == 0)) {
      return null;
    }
    ArrayList localArrayList2 = new ArrayList();
    Iterator localIterator = localArrayList1.iterator();
    while (localIterator.hasNext())
    {
      DBRow localDBRow = (DBRow)localIterator.next();
      CashTxnRow localCashTxnRow = (CashTxnRow)localDBRow;
      CashPaymentObject localCashPaymentObject = new CashPaymentObject(localCashTxnRow, this);
      localCashPaymentObject._mAmount = localCashTxnRow.getAmount();
      localArrayList2.add(localCashPaymentObject);
    }
    return localArrayList2;
  }
  
  public ArrayList<PaymentModeObject> getPaymentsOnDate(java.sql.Date paramDate)
    throws DBException
  {
    BindObject localBindObject = new BindObject(1, (short)2, paramDate);
    BindObject[] arrayOfBindObject = { localBindObject };
    ArrayList localArrayList1 = CashTxnTableDef.getInstance().getAllValuesWithWhereClauseWithBind("TXN_DATE=?", arrayOfBindObject);
    if ((localArrayList1 == null) || (localArrayList1.size() == 0)) {
      return null;
    }
    ArrayList localArrayList2 = new ArrayList();
    Iterator localIterator = localArrayList1.iterator();
    while (localIterator.hasNext())
    {
      DBRow localDBRow = (DBRow)localIterator.next();
      CashTxnRow localCashTxnRow = (CashTxnRow)localDBRow;
      CashPaymentObject localCashPaymentObject = new CashPaymentObject(localCashTxnRow, this);
      localArrayList2.add(localCashPaymentObject);
    }
    return localArrayList2;
  }
  
  public ArrayList<PaymentModeObject> getPaymentsOnDateRange(java.sql.Date paramDate1, java.sql.Date paramDate2)
    throws DBException
  {
    BindObject[] arrayOfBindObject = { new BindObject(1, 2, paramDate1), new BindObject(2, 2, paramDate1), new BindObject(3, 2, paramDate2), new BindObject(4, 2, paramDate2) };
    ArrayList localArrayList1 = CashTxnTableDef.getInstance().getAllValuesWithWhereClauseWithBind("(TXN_DATE > ? OR TXN_DATE = ?) AND (TXN_DATE < ? OR TXN_DATE =?) ", arrayOfBindObject);
    if ((localArrayList1 == null) || (localArrayList1.size() == 0)) {
      return null;
    }
    ArrayList localArrayList2 = new ArrayList();
    Iterator localIterator = localArrayList1.iterator();
    while (localIterator.hasNext())
    {
      DBRow localDBRow = (DBRow)localIterator.next();
      CashTxnRow localCashTxnRow = (CashTxnRow)localDBRow;
      CashPaymentObject localCashPaymentObject = new CashPaymentObject(localCashTxnRow, this);
      localArrayList2.add(localCashPaymentObject);
    }
    return localArrayList2;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.billing.CashPaymentOption
 * JD-Core Version:    0.7.0.1
 */