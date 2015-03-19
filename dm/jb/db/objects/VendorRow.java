package dm.jb.db.objects;

import dm.jb.db.gen.VendorBaseRow;
import dm.tools.db.DBException;

public class VendorRow
  extends VendorBaseRow
{
  public VendorRow(int paramInt, VendorTableDef paramVendorTableDef)
  {
    super(paramInt, paramVendorTableDef);
  }
  
  public String toString()
  {
    return getVendorName();
  }
  
  public void create()
    throws DBException
  {
    super.create();
    VendorTableDef.getInstance().addVendorRow(this);
  }
  
  public void delete()
    throws DBException
  {
    super.delete();
    VendorTableDef.getInstance().deleteVendor(this);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.VendorRow
 * JD-Core Version:    0.7.0.1
 */