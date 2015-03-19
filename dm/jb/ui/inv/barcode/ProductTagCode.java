package dm.jb.ui.inv.barcode;

import dm.jb.db.objects.ProductRow;

public class ProductTagCode
{
  public ProductRow product = null;
  public int tagCount = 0;
  
  public ProductTagCode(ProductRow paramProductRow, int paramInt)
  {
    this.product = paramProductRow;
    this.tagCount = paramInt;
  }
  
  public String toString()
  {
    return this.product.getProductCode() + ":" + this.product.getProdName() + " ( " + this.tagCount + " ) ";
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.inv.barcode.ProductTagCode
 * JD-Core Version:    0.7.0.1
 */