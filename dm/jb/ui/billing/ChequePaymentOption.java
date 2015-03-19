package dm.jb.ui.billing;

import dm.jb.db.objects.DdTxnRow;
import dm.jb.db.objects.DdTxnTableDef;
import dm.tools.db.BindObject;
import dm.tools.db.DBException;
import dm.tools.db.DBRow;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JPanel;

public class ChequePaymentOption
  implements PaymentOption
{
  private ChequePaymentOptionPanel _mPanel = null;
  
  public JPanel getOptionPanel()
  {
    return this._mPanel;
  }
  
  public String getCode()
  {
    return "CQ";
  }
  
  public String toString()
  {
    return "Cheque";
  }
  
  public PaymentModeObject getNewPaymentObjectForBillPay(int paramInt)
  {
    java.util.Date localDate = new java.util.Date();
    ChequePaymentObject localChequePaymentObject = new ChequePaymentObject(this._mPanel.getAmount(), this._mPanel.getChequeNo(), this._mPanel.getBank(), new java.sql.Date(this._mPanel.getDate().getTime()), this, "BP", new java.sql.Date(localDate.getTime()), new Time(localDate.getTime()), paramInt);
    return localChequePaymentObject;
  }
  
  public PaymentModeObject getNewPaymentObjectForBillRefund(int paramInt)
  {
    java.util.Date localDate = new java.util.Date();
    ChequePaymentObject localChequePaymentObject = new ChequePaymentObject(this._mPanel.getAmount(), this._mPanel.getChequeNo(), this._mPanel.getBank(), new java.sql.Date(this._mPanel.getDate().getTime()), this, "RF", new java.sql.Date(localDate.getTime()), new Time(localDate.getTime()), paramInt);
    return localChequePaymentObject;
  }
  
  public PaymentModeObject getNewPaymentObjectForBillAddl(int paramInt)
  {
    java.util.Date localDate = new java.util.Date();
    ChequePaymentObject localChequePaymentObject = new ChequePaymentObject(this._mPanel.getAmount(), this._mPanel.getChequeNo(), this._mPanel.getBank(), new java.sql.Date(this._mPanel.getDate().getTime()), this, "AP", new java.sql.Date(localDate.getTime()), new Time(localDate.getTime()), paramInt);
    return localChequePaymentObject;
  }
  
  public ArrayList<PaymentModeObject> getPayments(long paramLong, int paramInt)
    throws DBException
  {
    DdTxnTableDef localDdTxnTableDef = DdTxnTableDef.getInstance();
    ArrayList localArrayList1 = localDdTxnTableDef.getAllValuesWithWhereClause("BILL_NO =" + paramLong + " AND STORE_ID=" + paramInt);
    if ((localArrayList1 == null) || (localArrayList1.size() == 0)) {
      return null;
    }
    ArrayList localArrayList2 = new ArrayList();
    Iterator localIterator = localArrayList1.iterator();
    while (localIterator.hasNext())
    {
      DBRow localDBRow = (DBRow)localIterator.next();
      DdTxnRow localDdTxnRow = (DdTxnRow)localDBRow;
      ChequePaymentObject localChequePaymentObject = new ChequePaymentObject(localDdTxnRow, this);
      localChequePaymentObject._mAmount = localDdTxnRow.getAmount();
      localArrayList2.add(localChequePaymentObject);
    }
    return localArrayList2;
  }
  
  public void delete(long paramLong)
    throws DBException
  {
    DdTxnTableDef.getInstance().deleteWithWhere("BILL_NO=" + paramLong);
  }
  
  public ArrayList<PaymentModeObject> getPaymentsOnDate(java.sql.Date paramDate)
    throws DBException
  {
    BindObject localBindObject = new BindObject(1, (short)2, paramDate);
    BindObject[] arrayOfBindObject = { localBindObject };
    ArrayList localArrayList1 = DdTxnTableDef.getInstance().getAllValuesWithWhereClauseWithBind("TXN_DATE=?", arrayOfBindObject);
    if ((localArrayList1 == null) || (localArrayList1.size() == 0)) {
      return null;
    }
    ArrayList localArrayList2 = new ArrayList();
    Iterator localIterator = localArrayList1.iterator();
    while (localIterator.hasNext())
    {
      DBRow localDBRow = (DBRow)localIterator.next();
      DdTxnRow localDdTxnRow = (DdTxnRow)localDBRow;
      ChequePaymentObject localChequePaymentObject = new ChequePaymentObject(localDdTxnRow, this);
      localArrayList2.add(localChequePaymentObject);
    }
    return localArrayList2;
  }
  
  public ArrayList<PaymentModeObject> getPaymentsOnDateRange(java.sql.Date paramDate1, java.sql.Date paramDate2)
    throws DBException
  {
    BindObject[] arrayOfBindObject = { new BindObject(1, 2, paramDate1), new BindObject(2, 2, paramDate1), new BindObject(3, 2, paramDate2), new BindObject(4, 2, paramDate2) };
    ArrayList localArrayList1 = DdTxnTableDef.getInstance().getAllValuesWithWhereClauseWithBind("(TXN_DATE > ? OR TXN_DATE = ?) AND (TXN_DATE < ? OR TXN_DATE =?) ", arrayOfBindObject);
    if ((localArrayList1 == null) || (localArrayList1.size() == 0)) {
      return null;
    }
    ArrayList localArrayList2 = new ArrayList();
    Iterator localIterator = localArrayList1.iterator();
    while (localIterator.hasNext())
    {
      DBRow localDBRow = (DBRow)localIterator.next();
      DdTxnRow localDdTxnRow = (DdTxnRow)localDBRow;
      ChequePaymentObject localChequePaymentObject = new ChequePaymentObject(localDdTxnRow, this);
      localArrayList2.add(localChequePaymentObject);
    }
    return localArrayList2;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.billing.ChequePaymentOption
 * JD-Core Version:    0.7.0.1
 */