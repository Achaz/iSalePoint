package dm.jb.ui.billing;

import dm.jb.db.objects.CcTxnRow;
import dm.jb.db.objects.CcTxnTableDef;
import dm.tools.db.BindObject;
import dm.tools.db.DBException;
import dm.tools.db.DBRow;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JPanel;

public class CCPaymentOption
  implements PaymentOption
{
  private CCPaymentPanel _mPanel = new CCPaymentPanel();
  public static CardType[] cardTypes = { new CardType("VS", "VISA"), new CardType("MC", "MASTERCARD"), new CardType("AM", "AMEX") };
  
  public JPanel getOptionPanel()
  {
    return this._mPanel;
  }
  
  public String getCode()
  {
    return "CC";
  }
  
  public String toString()
  {
    return "Credit Card";
  }
  
  public PaymentModeObject getNewPaymentObjectForBillPay(int paramInt)
  {
    java.util.Date localDate = new java.util.Date();
    String str1 = this._mPanel.getCCNo();
    String str2 = this._mPanel.getRefNo();
    double d = this._mPanel.getAmount();
    CardType localCardType = this._mPanel.getCardType();
    CCPaymentObject localCCPaymentObject = new CCPaymentObject(this, localCardType, str1, str2, d, "BP", new java.sql.Date(localDate.getTime()), new Time(localDate.getTime()));
    return localCCPaymentObject;
  }
  
  public PaymentModeObject getNewPaymentObjectForBillRefund(int paramInt)
  {
    java.util.Date localDate = new java.util.Date();
    String str1 = this._mPanel.getCCNo();
    String str2 = this._mPanel.getRefNo();
    double d = this._mPanel.getAmount();
    CardType localCardType = this._mPanel.getCardType();
    CCPaymentObject localCCPaymentObject = new CCPaymentObject(this, localCardType, str1, str2, d, "RF", new java.sql.Date(localDate.getTime()), new Time(localDate.getTime()));
    return localCCPaymentObject;
  }
  
  public PaymentModeObject getNewPaymentObjectForBillAddl(int paramInt)
  {
    java.util.Date localDate = new java.util.Date();
    String str1 = this._mPanel.getCCNo();
    String str2 = this._mPanel.getRefNo();
    double d = this._mPanel.getAmount();
    CardType localCardType = this._mPanel.getCardType();
    CCPaymentObject localCCPaymentObject = new CCPaymentObject(this, localCardType, str1, str2, d, "AP", new java.sql.Date(localDate.getTime()), new Time(localDate.getTime()));
    return localCCPaymentObject;
  }
  
  public ArrayList<PaymentModeObject> getPayments(long paramLong, int paramInt)
    throws DBException
  {
    CcTxnTableDef localCcTxnTableDef = CcTxnTableDef.getInstance();
    ArrayList localArrayList1 = localCcTxnTableDef.getAllValuesWithWhereClause("BILL_NO=" + paramLong + " AND STORE_ID=" + paramInt);
    if ((localArrayList1 == null) || (localArrayList1.size() == 0)) {
      return null;
    }
    ArrayList localArrayList2 = new ArrayList();
    Iterator localIterator = localArrayList1.iterator();
    while (localIterator.hasNext())
    {
      DBRow localDBRow = (DBRow)localIterator.next();
      CcTxnRow localCcTxnRow = (CcTxnRow)localDBRow;
      CCPaymentObject localCCPaymentObject = new CCPaymentObject(localCcTxnRow, this);
      localArrayList2.add(localCCPaymentObject);
    }
    return localArrayList2;
  }
  
  public ArrayList<PaymentModeObject> getPaymentsOnDate(java.sql.Date paramDate)
    throws DBException
  {
    BindObject localBindObject = new BindObject(1, (short)2, paramDate);
    BindObject[] arrayOfBindObject = { localBindObject };
    ArrayList localArrayList1 = CcTxnTableDef.getInstance().getAllValuesWithWhereClauseWithBind("TXN_DATE=?", arrayOfBindObject);
    if ((localArrayList1 == null) || (localArrayList1.size() == 0)) {
      return null;
    }
    ArrayList localArrayList2 = new ArrayList();
    Iterator localIterator = localArrayList1.iterator();
    while (localIterator.hasNext())
    {
      DBRow localDBRow = (DBRow)localIterator.next();
      CcTxnRow localCcTxnRow = (CcTxnRow)localDBRow;
      CCPaymentObject localCCPaymentObject = new CCPaymentObject(localCcTxnRow, this);
      localArrayList2.add(localCCPaymentObject);
    }
    return localArrayList2;
  }
  
  public ArrayList<PaymentModeObject> getPaymentsOnDateRange(java.sql.Date paramDate1, java.sql.Date paramDate2)
    throws DBException
  {
    BindObject[] arrayOfBindObject = { new BindObject(1, 2, paramDate1), new BindObject(2, 2, paramDate1), new BindObject(3, 2, paramDate2), new BindObject(4, 2, paramDate2) };
    ArrayList localArrayList1 = CcTxnTableDef.getInstance().getAllValuesWithWhereClauseWithBind("(TXN_DATE > ? OR TXN_DATE = ?) AND (TXN_DATE < ? OR TXN_DATE =?) ", arrayOfBindObject);
    if ((localArrayList1 == null) || (localArrayList1.size() == 0)) {
      return null;
    }
    ArrayList localArrayList2 = new ArrayList();
    Iterator localIterator = localArrayList1.iterator();
    while (localIterator.hasNext())
    {
      DBRow localDBRow = (DBRow)localIterator.next();
      CcTxnRow localCcTxnRow = (CcTxnRow)localDBRow;
      CCPaymentObject localCCPaymentObject = new CCPaymentObject(localCcTxnRow, this);
      localArrayList2.add(localCCPaymentObject);
    }
    return localArrayList2;
  }
  
  public CardType getCardTypeByString(String paramString)
  {
    for (CardType localCardType : cardTypes) {
      if (localCardType._mType.equals(paramString)) {
        return localCardType;
      }
    }
    return null;
  }
  
  public static class CardType
  {
    String _mType = null;
    String _mName = null;
    
    public CardType(String paramString1, String paramString2)
    {
      this._mType = paramString1;
      this._mName = paramString2;
    }
    
    public String toString()
    {
      return this._mName;
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.billing.CCPaymentOption
 * JD-Core Version:    0.7.0.1
 */