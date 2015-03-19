package dm.jb.db.objects;

import dm.jb.db.gen.ProductBaseRow;
import dm.jb.op.sync.FileOpUtils;
import dm.jb.op.sync.SyncWriter;
import dm.tools.db.DBException;
import dm.tools.types.InternalAmount;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;

public class ProductRow
  extends ProductBaseRow
  implements Comparable
{
  private DeptRow _mDept = null;
  private CategoryRow _mCat = null;
  
  ProductRow(int paramInt, ProductTableDef paramProductTableDef)
  {
    super(paramInt, paramProductTableDef);
  }
  
  public double getPriceWithTax()
  {
    return getUnitPrice() + getUnitPrice() * (getTax() / 100.0D);
  }
  
  void getDeptForThis()
    throws DBException
  {
    this._mDept = DeptTableDef.getInstance().findDeptByIndex(getDeptIndex());
  }
  
  void getCatForThis()
    throws DBException
  {
    if (this._mDept == null) {
      getDeptForThis();
    }
    this._mCat = this._mDept.findCategoryByIndex(getCatIndex());
  }
  
  public CategoryRow getCategory()
  {
    if (this._mCat == null) {
      try
      {
        if (this._mDept == null)
        {
          getDeptForThis();
          if (this._mDept == null) {
            return null;
          }
        }
        this._mCat = this._mDept.findCategoryByIndex(getCatIndex());
      }
      catch (DBException localDBException)
      {
        System.err.println("Error reading category or dept information for product. Avoid call to this method");
      }
    }
    return this._mCat;
  }
  
  public DeptRow getDept()
  {
    if (this._mDept == null) {
      try
      {
        getDeptForThis();
      }
      catch (DBException localDBException)
      {
        System.err.println("Error reading category or dept information for product. Avoid call to this method");
      }
    }
    return this._mDept;
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
  
  public int compareTo(Object paramObject)
  {
    if ((paramObject instanceof ProductRow))
    {
      ProductRow localProductRow = (ProductRow)paramObject;
      if (localProductRow.getProdIndex() == getProdIndex()) {
        return 0;
      }
    }
    return 1;
  }
  
  public String toString()
  {
    return getProdName();
  }
  
  public int syncFromStream(FileInputStream paramFileInputStream)
    throws IOException
  {
    int i = FileOpUtils.readInt(paramFileInputStream);
    int j = FileOpUtils.readInt(paramFileInputStream);
    int k = FileOpUtils.readInt(paramFileInputStream);
    String str1 = FileOpUtils.readString(paramFileInputStream);
    int m = FileOpUtils.readInt(paramFileInputStream);
    double d1 = FileOpUtils.readDouble(paramFileInputStream);
    double d2 = FileOpUtils.readDouble(paramFileInputStream);
    double d3 = FileOpUtils.readDouble(paramFileInputStream);
    int n = FileOpUtils.readShort(paramFileInputStream);
    double d4 = FileOpUtils.readDouble(paramFileInputStream);
    int i1 = FileOpUtils.readInt(paramFileInputStream);
    int i2 = FileOpUtils.readInt(paramFileInputStream);
    String str2 = FileOpUtils.readString(paramFileInputStream);
    int i3 = FileOpUtils.readInt(paramFileInputStream);
    String str3 = FileOpUtils.readString(paramFileInputStream);
    setValues(j, k, str1, m, d1, d2, d3, n, d4, i1, i2, str2, i3, str3);
    setProdIndex(i);
    return 12 + (str1.getBytes().length + 2) + 4 + 8 + 8 + 8 + 2 + 8 + 4 + 4 + (str2.getBytes().length + 2) + 4;
  }
  
  public void sync(SyncWriter paramSyncWriter)
    throws IOException
  {
    paramSyncWriter.writeInt(getProdIndex());
    paramSyncWriter.writeInt(getCatIndex());
    paramSyncWriter.writeInt(getDeptIndex());
    paramSyncWriter.writeString(getProdName());
    paramSyncWriter.writeInt(getProdUnit());
    paramSyncWriter.writeDouble(getUnitPrice());
    paramSyncWriter.writeDouble(getDiscount());
    paramSyncWriter.writeDouble(getTax());
    paramSyncWriter.writeShort((short)getTaxUnit());
    paramSyncWriter.writeDouble(getLowStock());
    paramSyncWriter.writeInt(getLoyaltyPoints());
    paramSyncWriter.writeInt(getRedeemablePoints());
    paramSyncWriter.writeString(getProductCode());
    paramSyncWriter.writeInt(getVendorId());
    paramSyncWriter.writeString(getRfid());
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.ProductRow
 * JD-Core Version:    0.7.0.1
 */