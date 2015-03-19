package dm.jb.db.objects;

import dm.jb.db.gen.VendorBaseTableDef;
import dm.tools.db.DBException;
import dm.tools.db.DBRow;
import java.util.ArrayList;
import java.util.Iterator;

public class VendorTableDef
  extends VendorBaseTableDef
{
  private static VendorTableDef _mInsatnce = null;
  private ArrayList<VendorRow> _mVendors = null;
  
  public static VendorTableDef getInstance()
  {
    if (_mInsatnce == null) {
      _mInsatnce = new VendorTableDef();
    }
    return _mInsatnce;
  }
  
  public VendorRow getNewRow()
  {
    return new VendorRow(getAttrList().size(), this);
  }
  
  public ArrayList<VendorRow> getVendorRows()
    throws DBException
  {
    if (this._mVendors == null)
    {
      ArrayList localArrayList = getAllValues();
      if ((localArrayList == null) || (localArrayList.size() == 0))
      {
        this._mVendors = new ArrayList();
        return this._mVendors;
      }
      this._mVendors = new ArrayList();
      Iterator localIterator = localArrayList.iterator();
      while (localIterator.hasNext())
      {
        DBRow localDBRow = (DBRow)localIterator.next();
        this._mVendors.add((VendorRow)localDBRow);
      }
    }
    return this._mVendors;
  }
  
  public void addVendorRow(VendorRow paramVendorRow)
    throws DBException
  {
    if (getVendorRows() == null) {
      return;
    }
    this._mVendors.add(paramVendorRow);
  }
  
  public void deleteVendor(VendorRow paramVendorRow)
    throws DBException
  {
    if (getVendorRows() == null) {
      return;
    }
    for (int i = 0; i < this._mVendors.size(); i++)
    {
      VendorRow localVendorRow = (VendorRow)this._mVendors.get(i);
      if (localVendorRow.getVendorId() == paramVendorRow.getVendorId())
      {
        this._mVendors.remove(localVendorRow);
        return;
      }
    }
  }
  
  public VendorRow getVendorById(int paramInt)
    throws DBException
  {
    if (getVendorRows() == null) {
      return null;
    }
    for (int i = 0; i < this._mVendors.size(); i++)
    {
      VendorRow localVendorRow = (VendorRow)this._mVendors.get(i);
      if (localVendorRow.getVendorId() == paramInt) {
        return localVendorRow;
      }
    }
    return null;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.VendorTableDef
 * JD-Core Version:    0.7.0.1
 */