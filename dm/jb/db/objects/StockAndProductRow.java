package dm.jb.db.objects;

import dm.jb.db.gen.StockAndProductBaseRow;
import dm.tools.db.DBException;
import dm.tools.db.DBRow;
import dm.tools.types.InternalAmount;
import dm.tools.utils.CommonConfig;
import java.util.ArrayList;
import java.util.Iterator;

public class StockAndProductRow
  extends StockAndProductBaseRow
{
  private CategoryRow _mCategory = null;
  private DeptRow _mDept = null;
  private String _mStoreName = null;
  
  public StockAndProductRow(int paramInt, StockAndProductTableDef paramStockAndProductTableDef)
  {
    super(paramInt, paramStockAndProductTableDef);
  }
  
  public double getPriceWithTax()
  {
    if (!CommonConfig.getInstance().finalTax) {
      return getUnitPrice() + getUnitPrice() * (getTax() / 100.0D);
    }
    return getUnitPrice();
  }
  
  public void readDeptAndCategory()
    throws DBException
  {
    this._mDept = DeptTableDef.getInstance().findDeptByIndex(getDeptIndex());
    this._mCategory = CategoryTableDef.getInstance().findCatgeoryByIndex(getCatIndex());
  }
  
  public DeptRow getDepartment()
  {
    return this._mDept;
  }
  
  public CategoryRow getCategory()
  {
    return this._mCategory;
  }
  
  public String getDiscountString()
  {
    double d1 = getDiscount();
    if (d1 < 0.0D)
    {
      double d2 = d1 * -1.0D;
      InternalAmount localInternalAmount = new InternalAmount(d2, "", "", true);
      return localInternalAmount.toString();
    }
    return Double.toString(d1) + " %";
  }
  
  public ArrayList<CurrentStockRow> getCurrentStocks()
    throws DBException
  {
    ArrayList localArrayList1 = CurrentStockTableDef.getInstance().getAllValuesWithWhereClause("PROD_ID=" + getProdIndex());
    ArrayList localArrayList2 = new ArrayList();
    Iterator localIterator = localArrayList1.iterator();
    while (localIterator.hasNext())
    {
      DBRow localDBRow = (DBRow)localIterator.next();
      localArrayList2.add((CurrentStockRow)localDBRow);
    }
    return localArrayList2;
  }
  
  public String getProductCode()
  {
    if ((super.getProductCode() == null) || (super.getProductCode().length() == 0)) {
      return "" + getProdIndex();
    }
    return super.getProductCode();
  }
  
  public String getStoreName()
  {
    if (this._mStoreName != null) {
      return this._mStoreName;
    }
    try
    {
      StoreInfoRow localStoreInfoRow = (StoreInfoRow)StoreInfoTableDef.getInstance().findRowByIndex(getStoreId());
      if (getStoreId() == 0) {
        return null;
      }
      this._mStoreName = localStoreInfoRow.getName();
    }
    catch (DBException localDBException)
    {
      localDBException.printStackTrace();
    }
    return this._mStoreName;
  }
  
  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof StockAndProductRow))
    {
      StockAndProductRow localStockAndProductRow = (StockAndProductRow)paramObject;
      return (localStockAndProductRow.getProdIndex() == getProdIndex()) || (localStockAndProductRow.getStoreId() == getStoreId());
    }
    return super.equals(paramObject);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.StockAndProductRow
 * JD-Core Version:    0.7.0.1
 */