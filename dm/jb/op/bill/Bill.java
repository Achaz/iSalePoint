package dm.jb.op.bill;

import dm.jb.db.objects.BillEntryModRow;
import dm.jb.db.objects.BillEntryModTableDef;
import dm.jb.db.objects.BillEntryRow;
import dm.jb.db.objects.BillEntryTableDef;
import dm.jb.db.objects.BillTableRow;
import dm.jb.db.objects.BillTableTableDef;
import dm.jb.db.objects.CustomerRow;
import dm.jb.db.objects.DefectiveProductRow;
import dm.jb.db.objects.DefectiveProductTableDef;
import dm.jb.db.objects.DeletedBillsRow;
import dm.jb.db.objects.DeletedBillsTableDef;
import dm.jb.db.objects.Payment;
import dm.jb.db.objects.ProductRow;
import dm.jb.db.objects.SiteInfoRow;
import dm.jb.db.objects.SiteInfoTableDef;
import dm.jb.db.objects.StockAndProductRow;
import dm.jb.db.objects.StockAndProductTableDef;
import dm.jb.db.objects.StoreInfoRow;
import dm.jb.db.objects.StoreInfoTableDef;
import dm.jb.db.objects.StoreStockRow;
import dm.jb.db.objects.StoreStockTableDef;
import dm.jb.db.objects.UserInfoRow;
import dm.jb.db.objects.UserInfoTableDef;
import dm.tools.db.DBException;
import dm.tools.db.DBRow;
import dm.tools.utils.CommonConfig;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Bill
{
  private int _mBillNo = -1;
  private double _mTotalAmount = 0.0D;
  private double _mTotalTax = 0.0D;
  private double _mTotalDiscount = 0.0D;
  private double _mFinalDiscount = 0.0D;
  private double _mFinalTax = 0.0D;
  private double _mFinalAmount = 0.0D;
  HashMap<StockAndProductRow, BillProduct> _mBillEntries = new HashMap();
  ArrayList<BillProduct> _mRowsForDelete = null;
  private CustomerRow _mCustomer = null;
  private CustomerRow _mOldCustomer = null;
  private Payment _mPayment = null;
  private boolean _mBillCleaned = false;
  private java.util.Date _mDate = null;
  private double _mFinalTaxPercentage = 0.0D;
  private BillTableRow _mBillInDb = null;
  private static ArrayList<Bill> _mSavedBills = new ArrayList();
  private int _mLoyaltyRedeemed = 0;
  private StoreInfoRow _mStore = null;
  private SiteInfoRow _mSite = null;
  private boolean _mMissingProducts = false;
  
  public Bill()
  {
    if (CommonConfig.getInstance().finalTax) {
      this._mFinalTaxPercentage = CommonConfig.getInstance().finalTaxAmount;
    }
  }
  
  public static void saveBill(Bill paramBill)
  {
    _mSavedBills.add(paramBill);
  }
  
  public static Bill getSavedBill(int paramInt)
  {
    if (paramInt >= _mSavedBills.size()) {
      return null;
    }
    return (Bill)_mSavedBills.get(paramInt);
  }
  
  public static int getSizeOfSavedBills()
  {
    return _mSavedBills.size();
  }
  
  public static ArrayList<Bill> getSavedBills()
  {
    return _mSavedBills;
  }
  
  public void removeBillProduct(BillProduct paramBillProduct)
  {
    this._mBillEntries.remove(paramBillProduct._mProduct);
    this._mTotalAmount -= paramBillProduct._mFinalAmount;
    this._mTotalTax -= paramBillProduct._mTax;
    this._mFinalAmount -= paramBillProduct._mFinalAmount;
    this._mTotalDiscount -= paramBillProduct._mDiscount;
  }
  
  public void addBillProduct(BillProduct paramBillProduct)
  {
    this._mBillEntries.put(paramBillProduct._mProduct, paramBillProduct);
    this._mTotalAmount += paramBillProduct._mFinalAmount;
    this._mTotalDiscount += paramBillProduct._mDiscount;
    this._mTotalTax += paramBillProduct._mTax;
  }
  
  public boolean addBillProduct(int paramInt, double paramDouble, StockAndProductRow paramStockAndProductRow, boolean paramBoolean)
  {
    boolean bool = false;
    BillProduct localBillProduct = null;
    StockAndProductRow localStockAndProductRow = getProductById(paramInt);
    if (localStockAndProductRow != null) {
      localBillProduct = (BillProduct)this._mBillEntries.get(localStockAndProductRow);
    }
    if (localBillProduct != null) {
      bool = true;
    }
    if ((localBillProduct != null) && (paramBoolean))
    {
      this._mTotalAmount -= localBillProduct._mFinalAmount;
      this._mTotalTax -= localBillProduct._mTax;
      this._mTotalDiscount -= localBillProduct._mDiscount;
      this._mFinalAmount -= localBillProduct._mFinalAmount;
      localBillProduct._mQuantity += paramDouble;
      localBillProduct.recalcInternal();
      this._mTotalAmount += localBillProduct._mFinalAmount;
      this._mTotalTax += localBillProduct._mTax;
      this._mTotalDiscount += localBillProduct._mDiscount;
      this._mBillEntries.remove(localStockAndProductRow);
      this._mBillEntries.put(paramStockAndProductRow, localBillProduct);
    }
    else if (localBillProduct == null)
    {
      localBillProduct = new BillProduct(this._mBillEntries.size() + 1, paramDouble, paramStockAndProductRow);
      this._mTotalAmount += localBillProduct._mFinalAmount;
      this._mTotalTax += localBillProduct._mTax;
      this._mTotalDiscount += localBillProduct._mDiscount;
      this._mFinalAmount += localBillProduct._mFinalAmount;
      this._mBillEntries.put(paramStockAndProductRow, localBillProduct);
      bool = false;
    }
    return bool;
  }
  
  public void setPointsRedeemed(int paramInt)
  {
    this._mLoyaltyRedeemed = paramInt;
  }
  
  public BillProduct getBillProductForProduct(ProductRow paramProductRow)
  {
    return (BillProduct)this._mBillEntries.get(paramProductRow);
  }
  
  public java.util.Date getBillDate()
  {
    return this._mDate;
  }
  
  public Payment getPayment()
  {
    return this._mPayment;
  }
  
  public BillProduct getBillProductByProductId(int paramInt)
  {
    StockAndProductRow localStockAndProductRow = getProductById(paramInt);
    return localStockAndProductRow != null ? (BillProduct)this._mBillEntries.get(localStockAndProductRow) : null;
  }
  
  public StockAndProductRow getProductById(int paramInt)
  {
    Iterator localIterator = this._mBillEntries.keySet().iterator();
    while (localIterator.hasNext())
    {
      StockAndProductRow localStockAndProductRow = (StockAndProductRow)localIterator.next();
      if ((localStockAndProductRow != null) && (localStockAndProductRow.getProdIndex() == paramInt)) {
        return localStockAndProductRow;
      }
    }
    return null;
  }
  
  public void updateBillProduct(BillProduct paramBillProduct, double paramDouble)
  {
    this._mTotalAmount -= paramBillProduct._mFinalAmount;
    this._mTotalTax -= paramBillProduct._mTax;
    this._mFinalAmount -= paramBillProduct._mFinalAmount;
    this._mTotalDiscount -= paramBillProduct._mDiscount;
    paramBillProduct._mQuantity = paramDouble;
    paramBillProduct.recalcInternal();
    this._mTotalDiscount += paramBillProduct._mDiscount;
    this._mTotalAmount += paramBillProduct._mFinalAmount;
    this._mTotalTax += paramBillProduct._mTax;
  }
  
  public void clear()
  {
    this._mBillEntries.clear();
    this._mFinalAmount = 0.0D;
    this._mTotalDiscount = 0.0D;
    this._mTotalTax = 0.0D;
    this._mTotalAmount = 0.0D;
    this._mFinalAmount = 0.0D;
    this._mFinalTax = 0.0D;
    this._mFinalDiscount = 0.0D;
    this._mPayment = null;
  }
  
  public void setPayment(Payment paramPayment)
  {
    this._mPayment = paramPayment;
  }
  
  public int getEntriesCount()
  {
    return this._mBillEntries.size();
  }
  
  public void createBillinDB()
    throws DBException
  {
    BillTableRow localBillTableRow = BillTableTableDef.getInstance().getNewRow();
    this._mBillInDb = localBillTableRow;
    java.util.Date localDate = new java.util.Date();
    Time localTime = new Time(localDate.getTime());
    this._mDate = localDate;
    int i = StoreInfoTableDef.getCurrentStore().getStoreId();
    int j = SiteInfoTableDef.getCurrentSite().getSiteIndex();
    int k = UserInfoTableDef.getCurrentUser().getUserIndex();
    localBillTableRow.setValues("N", new java.sql.Date(localDate.getTime()), localTime, this._mFinalAmount, this._mFinalDiscount, this._mFinalTax, this._mBillEntries.size(), this._mCustomer != null ? this._mCustomer.getCustIndex() : -1, i, j, k, this._mLoyaltyRedeemed, getTotalLoyaltyPoints());
    localBillTableRow.create();
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = this._mBillEntries.values().iterator();
    int m = localBillTableRow.getBillNo();
    this._mBillNo = m;
    while (localIterator.hasNext())
    {
      localObject1 = (BillProduct)localIterator.next();
      localObject2 = BillEntryTableDef.getInstance().getNewRow();
      ((BillEntryRow)localObject2).setValues(m, ((BillProduct)localObject1)._mProduct.getProdIndex(), ((BillProduct)localObject1).getIndex(), ((BillProduct)localObject1)._mQuantity, ((BillProduct)localObject1)._mAmount, ((BillProduct)localObject1)._mDiscount, ((BillProduct)localObject1)._mProduct.getPurchasePrice(), localBillTableRow.getStoreId(), ((BillProduct)localObject1)._mTax);
      localArrayList.add(localObject2);
    }
    Object localObject1 = localArrayList.iterator();
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = (BillEntryRow)((Iterator)localObject1).next();
      ((BillEntryRow)localObject2).create();
    }
    Object localObject2 = this._mBillEntries.keySet().iterator();
    while (((Iterator)localObject2).hasNext())
    {
      StockAndProductRow localStockAndProductRow = (StockAndProductRow)((Iterator)localObject2).next();
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[1] = Integer.valueOf(localStockAndProductRow.getProdIndex());
      arrayOfObject[0] = Integer.valueOf(StoreInfoTableDef.getCurrentStore().getStoreId());
      StoreStockRow localStoreStockRow = (StoreStockRow)StoreStockTableDef.getInstance().findRowByIndexForUpdate(arrayOfObject);
      BillProduct localBillProduct = (BillProduct)this._mBillEntries.get(localStockAndProductRow);
      localStoreStockRow.setStock(localStoreStockRow.getStock() - localBillProduct._mQuantity);
      localStoreStockRow.update(true);
    }
    if (this._mPayment != null) {
      this._mPayment.create(m, localBillTableRow.getStoreId());
    }
    if (this._mCustomer != null) {
      this._mCustomer.update(true);
    }
  }
  
  public void deleteFromDB(boolean paramBoolean, int paramInt)
    throws DBException
  {
    double d = getFinalAmount();
    int i = getBillEntries().size();
    if (this._mBillInDb != null) {
      this._mBillInDb.delete();
    }
    BillEntryTableDef.getInstance().deleteEntriesForBillNo(this._mBillNo);
    this._mPayment.delete();
    if (this._mCustomer != null) {
      this._mCustomer.delete();
    }
    if (paramBoolean)
    {
      localObject = this._mBillEntries.keySet().iterator();
      while (((Iterator)localObject).hasNext())
      {
        StockAndProductRow localStockAndProductRow = (StockAndProductRow)((Iterator)localObject).next();
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[1] = Integer.valueOf(localStockAndProductRow.getProdIndex());
        arrayOfObject[0] = Integer.valueOf(StoreInfoTableDef.getCurrentStore().getStoreId());
        StoreStockRow localStoreStockRow = (StoreStockRow)StoreStockTableDef.getInstance().findRowByIndexForUpdate(arrayOfObject);
        BillProduct localBillProduct = (BillProduct)this._mBillEntries.get(localStockAndProductRow);
        localStoreStockRow.setStock(localStoreStockRow.getStock() + localBillProduct._mQuantity);
        localStoreStockRow.update(true);
      }
    }
    Object localObject = DeletedBillsTableDef.getInstance().getNewRow();
    ((DeletedBillsRow)localObject).setValues(this._mBillNo, paramInt, new java.sql.Date(new java.util.Date().getTime()), new Time(new java.util.Date().getTime()), UserInfoTableDef.getCurrentUser().getUserId(), d, i, this._mBillInDb.getStoreId());
    ((DeletedBillsRow)localObject).create();
  }
  
  public void setCustomerInfo(CustomerRow paramCustomerRow)
  {
    this._mOldCustomer = this._mCustomer;
    this._mCustomer = paramCustomerRow;
  }
  
  public void updateInDb()
    throws DBException
  {
    int i = getBillNo();
    String str = UserInfoTableDef.getCurrentUser().getUserId();
    java.sql.Date localDate = new java.sql.Date(new java.util.Date().getTime());
    Time localTime = new Time(new java.util.Date().getTime());
    BillProduct localBillProduct;
    BillEntryRow localBillEntryRow;
    Object localObject1;
    Object localObject2;
    Object localObject3;
    if (this._mRowsForDelete != null)
    {
      localIterator = this._mRowsForDelete.iterator();
      while (localIterator.hasNext())
      {
        localBillProduct = (BillProduct)localIterator.next();
        localBillEntryRow = BillEntryTableDef.getInstance().getNewRow();
        localBillEntryRow.setValues(i, localBillProduct._mProduct.getProdIndex(), localBillProduct.getIndex(), localBillProduct._mQuantity, localBillProduct._mAmount, localBillProduct._mDiscount, localBillProduct._mProduct.getPurchasePrice(), this._mBillInDb.getStoreId(), localBillProduct._mTax);
        localObject1 = BillEntryModTableDef.getInstance().getNewRow();
        ((BillEntryModRow)localObject1).setValuesFromRow(localBillEntryRow, localBillProduct.getUpdateType(), localDate, localTime, str, -1.0D * localBillProduct.oldQty, localBillProduct._mRefund, localBillEntryRow.getDiscount(), this._mBillInDb.getStoreId());
        ((BillEntryModRow)localObject1).create();
        if (localBillProduct.isUpdateStoreStock())
        {
          localObject2 = new Object[2];
          localObject2[1] = Integer.valueOf(localBillProduct._mProduct.getProdIndex());
          localObject2[0] = Integer.valueOf(StoreInfoTableDef.getCurrentStore().getStoreId());
          localObject3 = (StoreStockRow)StoreStockTableDef.getInstance().findRowByIndexForUpdate((Object[])localObject2);
          ((StoreStockRow)localObject3).setStock(((StoreStockRow)localObject3).getStock() + localBillProduct.oldQty);
          ((StoreStockRow)localObject3).update(true);
        }
        else if (localBillProduct.isUpdateDefectives())
        {
          localObject2 = DefectiveProductTableDef.getInstance().getNewRow();
          ((DefectiveProductRow)localObject2).setValues(localBillProduct._mProduct.getProdIndex(), StoreInfoTableDef.getCurrentStore().getStoreId(), localBillProduct.oldQty, localDate, 0.0D);
          ((DefectiveProductRow)localObject2).create();
        }
      }
    }
    Iterator localIterator = this._mBillEntries.values().iterator();
    while (localIterator.hasNext())
    {
      localBillProduct = (BillProduct)localIterator.next();
      localBillEntryRow = BillEntryTableDef.getInstance().getNewRow();
      localBillEntryRow.setValues(i, localBillProduct._mProduct.getProdIndex(), localBillProduct.getIndex(), localBillProduct._mQuantity, localBillProduct._mAmount, localBillProduct._mDiscount, localBillProduct._mProduct.getPurchasePrice(), this._mBillInDb.getStoreId(), localBillProduct._mTax);
      if (localBillProduct.isNew())
      {
        localObject1 = new Object[2];
        localObject1[1] = Integer.valueOf(localBillProduct._mProduct.getProdIndex());
        localObject1[0] = Integer.valueOf(StoreInfoTableDef.getCurrentStore().getStoreId());
        localObject2 = (StoreStockRow)StoreStockTableDef.getInstance().findRowByIndexForUpdate((Object[])localObject1);
        localObject3 = (BillProduct)this._mBillEntries.get(localBillProduct._mProduct);
        double d2 = 0.0D - ((BillProduct)localObject3)._mQuantity;
        ((StoreStockRow)localObject2).setStock(((StoreStockRow)localObject2).getStock() - ((BillProduct)localObject3)._mQuantity);
        ((StoreStockRow)localObject2).update(true);
        BillEntryModRow localBillEntryModRow = BillEntryModTableDef.getInstance().getNewRow();
        localBillEntryModRow.setValuesFromRow(localBillEntryRow, localBillProduct.getUpdateType(), localDate, localTime, str, d2, localBillEntryRow.getAmount(), localBillEntryRow.getDiscount(), this._mBillInDb.getStoreId());
        localBillEntryModRow.create();
      }
      else
      {
        double d1;
        Object localObject4;
        StoreStockRow localStoreStockRow;
        if (localBillProduct.isUpdate())
        {
          localObject1 = (BillProduct)this._mBillEntries.get(localBillProduct._mProduct);
          d1 = ((BillProduct)localObject1)._mQuantity - ((BillProduct)localObject1).oldQty;
          if (localBillProduct.isUpdateStoreStock())
          {
            localObject4 = new Object[2];
            localObject4[1] = Integer.valueOf(localBillProduct._mProduct.getProdIndex());
            localObject4[0] = Integer.valueOf(StoreInfoTableDef.getCurrentStore().getStoreId());
            localStoreStockRow = (StoreStockRow)StoreStockTableDef.getInstance().findRowByIndexForUpdate((Object[])localObject4);
            localStoreStockRow.setStock(localStoreStockRow.getStock() + ((BillProduct)localObject1).oldQty - ((BillProduct)localObject1)._mQuantity);
            localStoreStockRow.update(true);
          }
          else if (localBillProduct.isUpdateDefectives())
          {
            localObject4 = DefectiveProductTableDef.getInstance().getNewRow();
            ((DefectiveProductRow)localObject4).setValues(localBillProduct._mProduct.getProdIndex(), StoreInfoTableDef.getCurrentStore().getStoreId(), ((BillProduct)localObject1).oldQty - ((BillProduct)localObject1)._mQuantity, localDate, 0.0D);
            ((DefectiveProductRow)localObject4).create();
          }
          localObject4 = BillEntryModTableDef.getInstance().getNewRow();
          ((BillEntryModRow)localObject4).setValuesFromRow(localBillEntryRow, localBillProduct.getUpdateType(), localDate, localTime, str, d1, localBillProduct._mRefund, ((BillProduct)localObject1)._mDiscount - ((BillProduct)localObject1).oldDiscount, this._mBillInDb.getStoreId());
          ((BillEntryModRow)localObject4).create();
        }
        else if (localBillProduct.isDelete())
        {
          localObject1 = (BillProduct)this._mBillEntries.get(localBillProduct._mProduct);
          d1 = ((BillProduct)localObject1).oldQty;
          if (localBillProduct.isUpdateStoreStock())
          {
            localObject4 = new Object[2];
            localObject4[1] = Integer.valueOf(localBillProduct._mProduct.getProdIndex());
            localObject4[0] = Integer.valueOf(StoreInfoTableDef.getCurrentStore().getStoreId());
            localStoreStockRow = (StoreStockRow)StoreStockTableDef.getInstance().findRowByIndexForUpdate((Object[])localObject4);
            localStoreStockRow.setStock(localStoreStockRow.getStock() + ((BillProduct)localObject1).oldQty);
            localStoreStockRow.update(true);
          }
          else if (localBillProduct.isUpdateStoreStock())
          {
            localObject4 = DefectiveProductTableDef.getInstance().getNewRow();
            ((DefectiveProductRow)localObject4).setValues(localBillProduct._mProduct.getProdIndex(), StoreInfoTableDef.getCurrentStore().getStoreId(), ((BillProduct)localObject1).oldQty, localDate, 0.0D);
            ((DefectiveProductRow)localObject4).create();
          }
          localObject4 = BillEntryModTableDef.getInstance().getNewRow();
          ((BillEntryModRow)localObject4).setValuesFromRow(localBillEntryRow, localBillProduct.getUpdateType(), localDate, localTime, str, -1.0D * d1, localBillProduct._mRefund, -1.0D * ((BillProduct)localObject1).oldDiscount, this._mBillInDb.getStoreId());
          ((BillEntryModRow)localObject4).create();
        }
      }
    }
    this._mBillInDb.setTax(getFinalTax());
    this._mBillInDb.update(true);
    this._mPayment.update(i);
    if (this._mCustomer != null)
    {
      this._mCustomer.update(true);
      if ((this._mOldCustomer != null) && (this._mOldCustomer.getCustIndex() != this._mCustomer.getCustIndex())) {
        this._mOldCustomer.update(true);
      }
    }
  }
  
  public CustomerRow getCustomer()
  {
    return this._mCustomer;
  }
  
  public boolean getBillCleanedUp()
  {
    return this._mBillCleaned;
  }
  
  public ArrayList<BillProduct> getBillEntries()
  {
    ArrayList localArrayList = new ArrayList();
    Object[] arrayOfObject = this._mBillEntries.values().toArray();
    for (int i = 1; i < arrayOfObject.length; i++)
    {
      BillProduct localBillProduct1 = (BillProduct)arrayOfObject[i];
      int j = 0;
      for (j = i - 1; j >= 0; j--)
      {
        BillProduct localBillProduct2 = (BillProduct)arrayOfObject[j];
        if (localBillProduct2._mIndex <= localBillProduct1._mIndex) {
          break;
        }
        arrayOfObject[(j + 1)] = arrayOfObject[j];
      }
      arrayOfObject[(j + 1)] = localBillProduct1;
    }
    for (i = 0; i < arrayOfObject.length; i++) {
      localArrayList.add((BillProduct)arrayOfObject[i]);
    }
    return localArrayList;
  }
  
  public int getTotalLoyaltyPoints()
  {
    ArrayList localArrayList = getBillEntries();
    int i = 0;
    if (localArrayList.size() == 0) {
      return 0;
    }
    Object localObject;
    if (CommonConfig.getInstance().redemptionOption == 0)
    {
      BigDecimal localBigDecimal = new BigDecimal(getTotalAmount());
      localObject = new BigDecimal(CommonConfig.getInstance().loyaltyAmount);
      i = localBigDecimal.divide((BigDecimal)localObject, 0, RoundingMode.DOWN).intValue();
    }
    else if (CommonConfig.getInstance().redemptionOption == 1)
    {
      for (int j = 0; j < localArrayList.size(); j++)
      {
        localObject = (BillProduct)localArrayList.get(j);
        i = (int)(i + ((BillProduct)localObject).getStockAndProduct().getLoyaltyPoints() * ((BillProduct)localObject)._mQuantity);
      }
    }
    return i;
  }
  
  public int getRedeemedPoints()
  {
    return this._mLoyaltyRedeemed;
  }
  
  public int getRedeemableLoyaltyPoints()
  {
    Object localObject;
    int j;
    if (CommonConfig.getInstance().redemptionOption == 0)
    {
      BigDecimal localBigDecimal = new BigDecimal(getTotalAmount());
      localObject = new BigDecimal(CommonConfig.getInstance().redemptionAmount);
      j = localBigDecimal.divide((BigDecimal)localObject, 0, RoundingMode.DOWN).intValue();
      return j;
    }
    if (CommonConfig.getInstance().redemptionOption == 1)
    {
      int i = 0;
      localObject = getBillEntries();
      if (((ArrayList)localObject).size() == 0) {
        return 0;
      }
      for (j = 0; j < ((ArrayList)localObject).size(); j++)
      {
        BillProduct localBillProduct = (BillProduct)((ArrayList)localObject).get(j);
        i = (int)(i + localBillProduct.getStockAndProduct().getRedeemablePoints() * localBillProduct._mQuantity);
      }
      return i;
    }
    return 0;
  }
  
  public static Bill getBillForBillNoWithUpdate(int paramInt)
    throws DBException
  {
    int i = StoreInfoTableDef.getCurrentStore().getStoreId();
    Bill localBill = getBillForBillNo(paramInt, i);
    if (localBill == null) {
      return null;
    }
    localBill.loadBillModifications();
    return localBill;
  }
  
  public static Bill getBillForBillNo(int paramInt)
    throws DBException
  {
    int i = StoreInfoTableDef.getCurrentStore().getStoreId();
    return getBillForBillNo(paramInt, i);
  }
  
  public static Bill getBillForBillNo(int paramInt1, int paramInt2)
    throws DBException
  {
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = Integer.valueOf(paramInt1);
    arrayOfObject[1] = Integer.valueOf(paramInt2);
    BillTableRow localBillTableRow = (BillTableRow)BillTableTableDef.getInstance().findRowByKey(arrayOfObject);
    if (localBillTableRow == null) {
      return null;
    }
    Bill localBill = new Bill();
    localBill._mBillInDb = localBillTableRow;
    localBill._mBillNo = localBillTableRow.getBillNo();
    localBill._mFinalDiscount = localBillTableRow.getAddlDiscount();
    localBill._mFinalTax = localBillTableRow.getTax();
    ArrayList localArrayList = BillEntryTableDef.getInstance().getBillEntriesForBillNo(localBill._mBillNo);
    if (localArrayList == null) {
      return null;
    }
    if (localArrayList.size() != localBillTableRow.getTotalEntries()) {
      throw new DBException("Number of bill entries does not match the count in bill.", "Internal error.", "Try again later or contact administrator.", null, null);
    }
    BillEntryRow[] arrayOfBillEntryRow = new BillEntryRow[localArrayList.size()];
    arrayOfBillEntryRow = (BillEntryRow[])localArrayList.toArray(arrayOfBillEntryRow);
    StockAndProductTableDef localStockAndProductTableDef = StockAndProductTableDef.getInstance();
    for (int i = 0; i < arrayOfBillEntryRow.length; i++)
    {
      BillEntryRow localBillEntryRow = arrayOfBillEntryRow[i];
      StockAndProductRow localStockAndProductRow = localStockAndProductTableDef.getRowForProductId(localBillEntryRow.getProductIndex());
      if (localStockAndProductRow == null) {
        localBill._mMissingProducts = true;
      }
      BillProduct localBillProduct = new BillProduct(localBillEntryRow.getEntryIndex(), localBillEntryRow.getQuantity(), localStockAndProductRow, localBillEntryRow.getAmount(), localBillEntryRow.getDiscount(), localBillEntryRow.getTax());
      localBill.addBillProduct(localBillProduct);
    }
    Payment localPayment = Payment.getPayments(localBill._mBillNo, localBill.getStoreId());
    localBill._mPayment = localPayment;
    localBill._mDate = localBillTableRow.getBillDate();
    localBill._mBillCleaned = localBillTableRow.getBillCleanedUp().equalsIgnoreCase("Y");
    localBill._mLoyaltyRedeemed = localBillTableRow.getPointsRedeemed();
    localBill._mCustomer = localBillTableRow.getCustomerRow();
    if (paramInt2 != -1) {
      localBill._mStore = StoreInfoTableDef.getInstance().getStoreForIndex(paramInt2);
    }
    localBill._mSite = localBill._mStore.getSiteForId(localBillTableRow.getSiteId());
    return localBill;
  }
  
  private void loadBillModifications()
    throws DBException
  {
    ArrayList localArrayList1 = BillEntryModTableDef.getInstance().getAllValuesWithWhereClause("BILL_NO=" + this._mBillNo);
    if ((localArrayList1 == null) || (localArrayList1.size() == 0)) {
      return;
    }
    Iterator localIterator = localArrayList1.iterator();
    while (localIterator.hasNext())
    {
      DBRow localDBRow = (DBRow)localIterator.next();
      BillEntryModRow localBillEntryModRow = (BillEntryModRow)localDBRow;
      ArrayList localArrayList2 = getBillEntries();
      Object localObject1 = null;
      Object localObject2 = localArrayList2.iterator();
      while (((Iterator)localObject2).hasNext())
      {
        BillProduct localBillProduct = (BillProduct)((Iterator)localObject2).next();
        if ((localBillProduct._mProduct != null) && (localBillProduct._mProduct.getProdIndex() == localBillEntryModRow.getProductIndex()))
        {
          localObject1 = localBillProduct;
          break;
        }
      }
      if (localObject1 != null)
      {
        if (localObject1._mQuantity - localBillEntryModRow.getQuantity() == 0.0D) {
          removeBillProduct(localObject1);
        } else {
          localObject1._mQuantity += localBillEntryModRow.getQuantity();
        }
        localObject1.oldQty = localObject1._mQuantity;
        updateBillProduct(localObject1, localObject1._mQuantity);
      }
      else
      {
        localObject2 = StockAndProductTableDef.getInstance().getRowForProductId(localBillEntryModRow.getProductIndex());
        addBillProduct(localBillEntryModRow.getProductIndex(), 0.0D - localBillEntryModRow.getQuantity(), (StockAndProductRow)localObject2, false);
      }
    }
  }
  
  public double getTotalAmount()
  {
    return this._mTotalAmount;
  }
  
  public double getTotalTax()
  {
    return this._mTotalTax;
  }
  
  public double getTotalDiscount()
  {
    return this._mTotalDiscount;
  }
  
  public void setFinalDiscount(double paramDouble)
  {
    this._mFinalDiscount = paramDouble;
  }
  
  public void setFinalTax(double paramDouble)
  {
    this._mFinalTax = paramDouble;
  }
  
  public double getFinalTax()
  {
    this._mFinalAmount = (this._mTotalAmount - this._mFinalDiscount);
    this._mFinalTax = (this._mFinalAmount * (this._mFinalTaxPercentage / 100.0D));
    return this._mFinalTax;
  }
  
  public double getFinalAmount()
  {
    this._mFinalAmount = (this._mTotalAmount - this._mFinalDiscount);
    this._mFinalTax = (this._mFinalAmount * (this._mFinalTaxPercentage / 100.0D));
    this._mFinalAmount += this._mFinalTax;
    return this._mFinalAmount;
  }
  
  public double getFinalDiscount()
  {
    return this._mFinalDiscount;
  }
  
  public int getBillNo()
  {
    return this._mBillNo;
  }
  
  public int getStoreId()
  {
    return this._mBillInDb.getStoreId();
  }
  
  public StoreInfoRow getStore()
  {
    return this._mStore;
  }
  
  public SiteInfoRow getSite()
  {
    return this._mSite;
  }
  
  public double getStoredFinalAmount()
  {
    return this._mBillInDb.getAmount();
  }
  
  public void setRoundedFinalAmount(double paramDouble)
  {
    this._mFinalAmount = paramDouble;
  }
  
  public void setFinalAmountInDb(double paramDouble)
  {
    this._mBillInDb.setAmount(paramDouble);
  }
  
  public int getLoyaltyPointsInDb()
  {
    return this._mBillInDb.getPointsRedeemed();
  }
  
  public int getLoyaltyPointsAwardedInDb()
  {
    return this._mBillInDb.getPointsAwarded();
  }
  
  public void addRowsForDelete(BillProduct paramBillProduct)
  {
    if (this._mRowsForDelete == null) {
      this._mRowsForDelete = new ArrayList();
    }
    this._mRowsForDelete.add(paramBillProduct);
  }
  
  public class BillSaleReturnRow
  {
    private double _mQuantity = 0.0D;
    private double _mAmount = 0.0D;
    private StockAndProductRow _mProduct = null;
    private int _mMode = 0;
    
    public BillSaleReturnRow(double paramDouble1, double paramDouble2, StockAndProductRow paramStockAndProductRow, int paramInt)
    {
      this._mQuantity = paramDouble1;
      this._mAmount = paramDouble2;
      this._mProduct = paramStockAndProductRow;
      this._mMode = paramInt;
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.op.bill.Bill
 * JD-Core Version:    0.7.0.1
 */