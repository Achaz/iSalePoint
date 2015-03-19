package dm.jb.ui.billing;

import dm.jb.db.objects.DdTxnRow;
import dm.jb.db.objects.DdTxnTableDef;
import dm.tools.db.DBException;
import java.sql.Date;
import java.sql.Time;

public class ChequePaymentObject
  implements PaymentModeObject
{
  double _mAmount = 0.0D;
  String _mChequeNo = "";
  String _mBank = null;
  Date _mPayDate = null;
  Date _mDate = null;
  Time _mTime = null;
  ChequePaymentOption _mPaymentOption = null;
  private String _mTxnType = null;
  private DdTxnRow _mRow = null;
  
  public ChequePaymentObject(double paramDouble, String paramString1, String paramString2, Date paramDate1, ChequePaymentOption paramChequePaymentOption, String paramString3, Date paramDate2, Time paramTime, int paramInt)
  {
    this._mAmount = paramDouble;
    this._mChequeNo = paramString1;
    this._mPaymentOption = paramChequePaymentOption;
    this._mBank = paramString2;
    this._mDate = paramDate2;
    this._mTime = paramTime;
    this._mPayDate = paramDate1;
    this._mTxnType = paramString3;
    this._mRow = DdTxnTableDef.getInstance().getNewRow();
    this._mRow.setStoreId(paramInt);
  }
  
  public ChequePaymentObject(DdTxnRow paramDdTxnRow, ChequePaymentOption paramChequePaymentOption)
  {
    this._mAmount = paramDdTxnRow.getAmount();
    this._mChequeNo = paramDdTxnRow.getDdNo();
    this._mPaymentOption = paramChequePaymentOption;
    this._mBank = paramDdTxnRow.getBank();
    this._mDate = paramDdTxnRow.getTxnDate();
    this._mTime = paramDdTxnRow.getTxnTime();
    this._mTxnType = paramDdTxnRow.getTxnType();
    this._mPayDate = paramDdTxnRow.getPayDate();
  }
  
  public void setAmount(double paramDouble)
  {
    this._mAmount = paramDouble;
    this._mRow.setAmount(paramDouble);
  }
  
  public String getRefNo()
  {
    return this._mChequeNo;
  }
  
  public double getAmount()
  {
    return this._mAmount;
  }
  
  public String getComments()
  {
    return "Bank : " + this._mBank;
  }
  
  public String getOptionString()
  {
    return "Cheque";
  }
  
  public String toString()
  {
    return getOptionString();
  }
  
  public PaymentOption getPaymentOption()
  {
    return this._mPaymentOption;
  }
  
  public String getBank()
  {
    return this._mBank;
  }
  
  public Date getDate()
  {
    return this._mDate;
  }
  
  public void createInDB(long paramLong, int paramInt)
    throws DBException
  {
    this._mRow.setValues(paramLong, this._mChequeNo, this._mPayDate, this._mDate, this._mTime, this._mBank, this._mTxnType, this._mAmount, paramInt);
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
    this._mRow.setValues(paramLong, this._mChequeNo, this._mPayDate, this._mDate, this._mTime, this._mBank, this._mTxnType, this._mAmount, this._mRow.getStoreId());
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
 * Qualified Name:     dm.jb.ui.billing.ChequePaymentObject
 * JD-Core Version:    0.7.0.1
 */