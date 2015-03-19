package dm.jb.op.bill;

import dm.jb.db.objects.StockAndProductRow;
import dm.tools.types.InternalAmount;
import dm.tools.types.InternalDate;
import dm.tools.types.InternalInteger;
import dm.tools.types.InternalQuantity;
import dm.tools.ui.UICommon;
import dm.tools.utils.CommonConfig;

public class BillProduct
{
  public static final byte UPDATE_ACTION_NEW = 1;
  public static final byte UPDATE_ACTION_DELETE = 2;
  public static final byte UPDATE_ACTION_UPDATE = 3;
  StockAndProductRow _mProduct = null;
  double _mQuantity = 0.0D;
  int _mIndex = 0;
  double _mDiscount = 0.0D;
  double _mAmount = 0.0D;
  double _mFinalAmount = 0.0D;
  double _mTax = 0.0D;
  double _mRefund = 0.0D;
  private BillChange billChange = null;
  double oldQty = 0.0D;
  double oldDiscount = 0.0D;
  
  public BillProduct(int paramInt, double paramDouble, StockAndProductRow paramStockAndProductRow)
  {
    this._mIndex = paramInt;
    this._mQuantity = paramDouble;
    this._mProduct = paramStockAndProductRow;
    this.oldQty = paramDouble;
    if (paramStockAndProductRow != null) {
      recalcInternal();
    }
    this.oldDiscount = this._mDiscount;
  }
  
  public BillProduct(int paramInt, double paramDouble1, StockAndProductRow paramStockAndProductRow, double paramDouble2, double paramDouble3, double paramDouble4)
  {
    this._mIndex = paramInt;
    this._mQuantity = paramDouble1;
    this._mProduct = paramStockAndProductRow;
    this.oldQty = paramDouble1;
    if (paramStockAndProductRow != null) {
      recalcInternal();
    }
    this.oldDiscount = this._mDiscount;
    this._mAmount = paramDouble2;
    this._mDiscount = paramDouble3;
    this._mTax = paramDouble4;
  }
  
  public void setRefund(double paramDouble)
  {
    this._mRefund = paramDouble;
  }
  
  public double getOldQuantity()
  {
    return this.oldQty;
  }
  
  public BillProduct(BillProduct paramBillProduct)
  {
    this._mIndex = paramBillProduct._mIndex;
    this._mQuantity = paramBillProduct._mQuantity;
    this._mProduct = paramBillProduct._mProduct;
    this._mDiscount = paramBillProduct._mDiscount;
    this._mAmount = paramBillProduct._mAmount;
    this._mTax = paramBillProduct._mTax;
    recalcInternal();
    this.oldDiscount = this._mDiscount;
  }
  
  public StockAndProductRow getStockAndProduct()
  {
    return this._mProduct;
  }
  
  public void recalcInternal()
  {
    this._mAmount = (this._mQuantity * this._mProduct.getUnitPrice());
    double d = this._mProduct.getDiscount();
    if (d < 0.0D)
    {
      d *= -1.0D;
      this._mDiscount = (this._mQuantity * d);
    }
    else
    {
      this._mDiscount = (d * this._mAmount);
      this._mDiscount /= 100.0D;
    }
    this._mFinalAmount = (this._mAmount - this._mDiscount);
    if (!CommonConfig.getInstance().finalTax)
    {
      this._mTax = (this._mFinalAmount * (this._mProduct.getTax() / 100.0D));
      this._mFinalAmount += this._mTax;
    }
    else
    {
      this._mTax = 0.0D;
    }
  }
  
  public double getFinalAmountForQuantity(double paramDouble)
  {
    double d1 = paramDouble * this._mProduct.getUnitPrice();
    double d2 = this._mProduct.getDiscount();
    double d3 = 0.0D;
    if (d2 < 0.0D)
    {
      d2 *= -1.0D;
      d3 = paramDouble * d2;
    }
    else
    {
      d3 = d2 * d1;
      d3 /= 100.0D;
    }
    double d4 = d1 - d3;
    if (!CommonConfig.getInstance().finalTax)
    {
      double d5 = d4 * (this._mProduct.getTax() / 100.0D);
      d4 += d5;
    }
    return d4;
  }
  
  public int getUpdateType()
  {
    return this.billChange != null ? this.billChange.updateaction : 0;
  }
  
  public double getTotalAmount()
  {
    return this._mFinalAmount;
  }
  
  public Object getQuantityString()
  {
    return new InternalQuantity(this._mQuantity, "  ", "  ", this._mProduct == null ? -1 : this._mProduct.getProdUnit(), true);
  }
  
  public Object getQuantityStringWithoutSpace()
  {
    return new InternalQuantity(this._mQuantity, this._mProduct.getProdUnit(), false);
  }
  
  public double getQuantity()
  {
    return this._mQuantity;
  }
  
  private Object getTotalAmountString()
  {
    return new InternalAmount(this._mFinalAmount, "  ", "  ", false);
  }
  
  private Object getDiscString()
  {
    return new InternalAmount(this._mDiscount, "  ", "  ", false);
  }
  
  public Object getDataForColCode(String paramString)
  {
    if (paramString.equalsIgnoreCase("SL")) {
      return new InternalInteger(this._mIndex, "  ", "  ");
    }
    if (paramString.equalsIgnoreCase("PC"))
    {
      if (this._mProduct == null) {
        return "NA";
      }
      return this._mProduct.getProductCode();
    }
    if (paramString.equalsIgnoreCase("PN"))
    {
      if (this._mProduct == null) {
        return "NA";
      }
      return this._mProduct.getProdName();
    }
    if (paramString.equalsIgnoreCase("EX")) {
      return new InternalDate(new java.util.Date(this._mProduct.getExpiry().getTime()), this._mProduct.getExpiry() == null, "  ", "  ", UICommon.getDateFormat());
    }
    if (paramString.equalsIgnoreCase("UP"))
    {
      if (this._mProduct == null) {
        return new InternalAmount(this._mAmount - this._mDiscount / this._mQuantity, "  ", "  ", false);
      }
      return new InternalAmount(this._mProduct.getPriceWithTax(), "  ", "  ", false);
    }
    if (paramString.equalsIgnoreCase("QT")) {
      return getQuantityString();
    }
    if (paramString.equalsIgnoreCase("AM")) {
      return getTotalAmountString();
    }
    if (paramString.equalsIgnoreCase("DC")) {
      return getDiscString();
    }
    return new String("");
  }
  
  public int getIndex()
  {
    return this._mIndex;
  }
  
  public double getDiscount()
  {
    return this._mDiscount;
  }
  
  public double getAmount()
  {
    return this._mAmount;
  }
  
  public double getTax()
  {
    return this._mTax;
  }
  
  public boolean isNew()
  {
    return (this.billChange != null) && (this.billChange.updateaction == 1);
  }
  
  public boolean isUpdate()
  {
    return (this.billChange != null) && (this.billChange.updateaction == 3);
  }
  
  public boolean isUpdateStoreStock()
  {
    return this.billChange.updateStock == 1;
  }
  
  public boolean isUpdateDefectives()
  {
    return this.billChange.updateStock == 2;
  }
  
  public boolean isDelete()
  {
    return (this.billChange != null) && (this.billChange.updateaction == 2);
  }
  
  public void setBillChange(int paramInt, byte paramByte1, byte paramByte2)
  {
    this.billChange = new BillChange(paramInt, paramByte1, paramByte2);
  }
  
  public void setBillChange(BillChange paramBillChange)
  {
    this.billChange = paramBillChange;
  }
  
  public class BillChange
  {
    int type;
    byte updateStock;
    byte updateaction = -1;
    
    public BillChange(int paramInt, byte paramByte1, byte paramByte2)
    {
      this.type = paramInt;
      this.updateStock = paramByte1;
      this.updateaction = paramByte2;
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.op.bill.BillProduct
 * JD-Core Version:    0.7.0.1
 */