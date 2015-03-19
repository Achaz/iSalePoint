package dm.jb.ui.billing;

import dm.jb.db.objects.CcTxnRow;
import dm.jb.db.objects.CcTxnTableDef;
import dm.tools.db.DBException;
import java.sql.Date;
import java.sql.Time;

public class CCPaymentObject
  implements PaymentModeObject
{
  String _mCCNo = null;
  String _mRefNo = null;
  double _mAmount = 0.0D;
  CCPaymentOption _mPaymentOption = null;
  String _mTxnType = null;
  Date _mDate = null;
  Time _mTime = null;
  CCPaymentOption.CardType _mCCType = null;
  CcTxnRow _mRow = null;
  
  public CCPaymentObject(CCPaymentOption paramCCPaymentOption, CCPaymentOption.CardType paramCardType, String paramString1, String paramString2, double paramDouble, String paramString3, Date paramDate, Time paramTime)
  {
    this._mCCNo = paramString1;
    this._mRefNo = paramString2;
    this._mAmount = paramDouble;
    this._mPaymentOption = paramCCPaymentOption;
    this._mTxnType = paramString3;
    this._mDate = paramDate;
    this._mTime = paramTime;
    this._mCCType = paramCardType;
    this._mRow = CcTxnTableDef.getInstance().getNewRow();
  }
  
  public CCPaymentObject(CcTxnRow paramCcTxnRow, CCPaymentOption paramCCPaymentOption)
  {
    this._mRow = paramCcTxnRow;
    this._mPaymentOption = paramCCPaymentOption;
    this._mCCNo = paramCcTxnRow.getCcNo();
    this._mCCType = paramCCPaymentOption.getCardTypeByString(paramCcTxnRow.getCardType());
    this._mRefNo = paramCcTxnRow.getRefNo();
    this._mAmount = paramCcTxnRow.getAmount();
    this._mTxnType = paramCcTxnRow.getTxnType();
    this._mDate = paramCcTxnRow.getTxnDate();
    this._mTime = paramCcTxnRow.getTxnTime();
  }
  
  public String getRefNo()
  {
    return this._mRefNo;
  }
  
  public double getAmount()
  {
    return this._mAmount;
  }
  
  public double getAmountForModel()
  {
    if ((this._mTxnType.equals("RF")) && (this._mAmount > 0.0D)) {
      return this._mAmount * -1.0D;
    }
    return this._mAmount;
  }
  
  public String getComments()
  {
    return "Credit Card";
  }
  
  public String getOptionString()
  {
    return "Credit Card";
  }
  
  public PaymentOption getPaymentOption()
  {
    return this._mPaymentOption;
  }
  
  public Date getDate()
  {
    return this._mDate;
  }
  
  public String getTxnType()
  {
    return this._mTxnType;
  }
  
  public void createInDB(long paramLong, int paramInt)
    throws DBException
  {
    this._mRow.setValues(paramLong, this._mCCNo, this._mCCType._mType, this._mRefNo, this._mAmount, this._mTxnType, this._mDate, this._mTime, paramInt);
    this._mRow.create();
  }
  
  public void deleteFromDB()
    throws DBException
  {
    this._mRow.delete();
    this._mRow = null;
  }
  
  public void updateInDB(long paramLong)
    throws DBException
  {
    long l = this._mRow.getBillNo();
    this._mRow.setValues(paramLong, this._mCCNo, this._mCCType._mType, this._mRefNo, this._mAmount, this._mTxnType, this._mDate, this._mTime, this._mRow.getStoreId());
    if (l == -1L) {
      this._mRow.create();
    } else {
      this._mRow.update(true);
    }
  }
  
  public String toString()
  {
    return "Credit Card";
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.billing.CCPaymentObject
 * JD-Core Version:    0.7.0.1
 */