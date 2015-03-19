package dm.jb.db.objects;

import dm.jb.ui.billing.PaymentModeObject;
import dm.jb.ui.billing.PaymentOption;
import dm.tools.db.DBException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Payment
{
  ArrayList<PaymentModeObject> _mPayments = null;
  private static HashMap<String, PaymentOption> _mPaymentModes = new HashMap();
  
  public void addPaymentMode(PaymentModeObject paramPaymentModeObject)
  {
    if (this._mPayments == null) {
      this._mPayments = new ArrayList();
    }
    this._mPayments.add(paramPaymentModeObject);
  }
  
  public void removeAllPayments()
  {
    if (this._mPayments != null) {
      this._mPayments.clear();
    }
  }
  
  public static Payment getPayment()
    throws DBException
  {
    return null;
  }
  
  public void create(int paramInt1, int paramInt2)
    throws DBException
  {
    Iterator localIterator = this._mPayments.iterator();
    while (localIterator.hasNext())
    {
      PaymentModeObject localPaymentModeObject = (PaymentModeObject)localIterator.next();
      localPaymentModeObject.createInDB(paramInt1, paramInt2);
    }
  }
  
  public ArrayList<PaymentModeObject> getPaymentObjects()
  {
    return this._mPayments;
  }
  
  public void delete()
    throws DBException
  {
    Iterator localIterator = this._mPayments.iterator();
    while (localIterator.hasNext())
    {
      PaymentModeObject localPaymentModeObject = (PaymentModeObject)localIterator.next();
      localPaymentModeObject.deleteFromDB();
    }
  }
  
  public void update(int paramInt)
    throws DBException
  {
    Iterator localIterator = this._mPayments.iterator();
    while (localIterator.hasNext())
    {
      PaymentModeObject localPaymentModeObject = (PaymentModeObject)localIterator.next();
      localPaymentModeObject.updateInDB(paramInt);
    }
  }
  
  public static Payment getPayments(int paramInt1, int paramInt2)
    throws DBException
  {
    Payment localPayment = new Payment();
    Iterator localIterator = _mPaymentModes.keySet().iterator();
    ArrayList localArrayList1 = new ArrayList();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      PaymentOption localPaymentOption = (PaymentOption)_mPaymentModes.get(str);
      ArrayList localArrayList2 = localPaymentOption.getPayments(paramInt1, paramInt2);
      if (localArrayList2 != null) {
        localArrayList1.addAll(localArrayList2);
      }
    }
    localPayment._mPayments = localArrayList1;
    return localPayment;
  }
  
  public static void registerOption(PaymentOption paramPaymentOption)
  {
    _mPaymentModes.put(paramPaymentOption.getCode(), paramPaymentOption);
  }
  
  public static ArrayList<PaymentOption> getOptionsList()
  {
    return new ArrayList(_mPaymentModes.values());
  }
  
  public static PaymentOption getCashPaymentOption()
  {
    return (PaymentOption)_mPaymentModes.get("CS");
  }
  
  public double getTotalAmount()
  {
    double d1 = 0.0D;
    Iterator localIterator = this._mPayments.iterator();
    while (localIterator.hasNext())
    {
      PaymentModeObject localPaymentModeObject = (PaymentModeObject)localIterator.next();
      double d2 = localPaymentModeObject.getAmount();
      if (localPaymentModeObject.getTxnType().equals("RF")) {
        d1 -= d2;
      } else {
        d1 += d2;
      }
    }
    return d1;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.Payment
 * JD-Core Version:    0.7.0.1
 */