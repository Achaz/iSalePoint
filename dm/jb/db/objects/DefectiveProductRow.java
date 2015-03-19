package dm.jb.db.objects;

import dm.jb.db.gen.DefectiveProductBaseRow;

public class DefectiveProductRow
  extends DefectiveProductBaseRow
{
  private ProductRow _mProduct = null;
  
  public DefectiveProductRow(int paramInt, DefectiveProductTableDef paramDefectiveProductTableDef)
  {
    super(paramInt, paramDefectiveProductTableDef);
  }
  
  public void setProductRow(ProductRow paramProductRow)
  {
    this._mProduct = paramProductRow;
  }
  
  public ProductRow getProductRow()
  {
    return this._mProduct;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.DefectiveProductRow
 * JD-Core Version:    0.7.0.1
 */