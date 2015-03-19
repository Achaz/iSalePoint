package dm.jb.ui.billing;

import dm.jb.db.objects.CashTxnRow;
import dm.jb.db.objects.CashTxnTableDef;
import dm.tools.db.DBException;
import java.sql.Date;
import java.sql.Time;

public class CashPaymentObject
  implements PaymentModeObject
{
  double _mAmount = 0.0D;
  CashPaymentOption _mPaymentOption = null;
  Date _mDate = null;
  Time _mTime = null;
  private String _mTxnType = null;
  private CashTxnRow _mRow = null;
  
  public CashPaymentObject(Date paramDate, Time paramTime, CashPaymentOption paramCashPaymentOption, String paramString, int paramInt)
  {
    this._mPaymentOption = paramCashPaymentOption;
    this._mDate = paramDate;
    this._mTxnType = paramString;
    this._mTime = paramTime;
    this._mRow = CashTxnTableDef.getInstance().getNewRow();
    this._mRow.setStoreId(paramInt);
  }
  
  public CashPaymentObject(CashTxnRow paramCashTxnRow, CashPaymentOption paramCashPaymentOption)
  {
    this._mRow = paramCashTxnRow;
    this._mDate = paramCashTxnRow.getTxnDate();
    this._mTime = paramCashTxnRow.getTxnTime();
    this._mTxnType = paramCashTxnRow.getTxnType();
    this._mPaymentOption = paramCashPaymentOption;
    this._mAmount = paramCashTxnRow.getAmount();
  }
  
  public void setAmount(double paramDouble)
  {
    this._mAmount = paramDouble;
    this._mRow.setAmount(paramDouble);
  }
  
  public String getRefNo()
  {
    return "";
  }
  
  public double getAmount()
  {
    return this._mAmount;
  }
  
  public String getComments()
  {
    return "";
  }
  
  public String getOptionString()
  {
    return "Cash";
  }
  
  public String toString()
  {
    if (getAmountForModel() < 0.0D) {
      return getOptionString() + " (Refund) ";
    }
    return getOptionString();
  }
  
  public PaymentOption getPaymentOption()
  {
    return this._mPaymentOption;
  }
  
  public Date getDate()
  {
    return this._mDate;
  }
  
  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof CashPaymentObject)) {
      return true;
    }
    return super.equals(paramObject);
  }
  
  public void createInDB(long paramLong, int paramInt)
    throws DBException
  {
    this._mRow.setValues(paramLong, this._mDate, this._mTime, this._mAmount, this._mTxnType, paramInt);
    this._mRow.create();
  }
  
  public void deleteFromDB()
    throws DBException
  {
    this._mRow.delete();
  }
  
  public void updateInDB(long paramLong)
    throws DBException
  {
    long l = this._mRow.getBillNo();
    this._mRow.setValues(paramLong, this._mDate, this._mTime, this._mAmount, this._mTxnType, this._mRow.getStoreId());
    if (l == -1L) {
      this._mRow.create();
    } else {
      this._mRow.update(true);
    }
  }
  
  public String getTxnType()
  {
    return this._mTxnType;
  }
  
  public double getAmountForModel()
  {
    if ((this._mTxnType.equals("RF")) && (this._mAmount > 0.0D)) {
      return this._mAmount * -1.0D;
    }
    return this._mAmount;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.billing.CashPaymentObject
 * JD-Core Version:    0.7.0.1
 */