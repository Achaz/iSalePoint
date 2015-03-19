package dm.jb.ui.inv;

import dm.jb.db.objects.CurrentStockRow;
import dm.jb.db.objects.ProductRow;
import dm.jb.db.objects.StoreInfoRow;

public class ReturnStock
{
  public ProductRow productRow = null;
  public double quantity = 0.0D;
  public Object fromObject = null;
  public Object internalObject = null;
  
  public ReturnStock(ProductRow paramProductRow, double paramDouble, Object paramObject)
  {
    this.productRow = paramProductRow;
    this.quantity = paramDouble;
    this.fromObject = paramObject;
  }
  
  public ReturnStock(ProductRow paramProductRow, double paramDouble, Object paramObject1, Object paramObject2)
  {
    this.productRow = paramProductRow;
    this.quantity = paramDouble;
    this.fromObject = paramObject1;
    this.internalObject = paramObject2;
  }
  
  public String getSmallDetailsForPrinting()
  {
    String str = "";
    if ((this.fromObject instanceof StoreInfoRow)) {
      str = "Store ";
    } else if ((this.fromObject instanceof CurrentStockRow)) {
      str = "Warehouse ";
    } else {
      str = "Defective";
    }
    return str;
  }
  
  public String getMediumDetailsForPrinting()
  {
    String str = "";
    if ((this.fromObject instanceof StoreInfoRow)) {
      str = this.fromObject.toString();
    } else if ((this.fromObject instanceof CurrentStockRow)) {
      str = this.internalObject.toString();
    } else {
      str = "Def: " + this.internalObject.toString();
    }
    return str;
  }
  
  public String getDetailsForPrinting()
  {
    String str = "";
    if ((this.fromObject instanceof StoreInfoRow)) {
      str = "Store : " + this.fromObject.toString();
    } else if ((this.fromObject instanceof CurrentStockRow)) {
      str = "Warehouse : " + this.internalObject.toString();
    } else {
      str = "Def : " + this.internalObject.toString();
    }
    return str;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.inv.ReturnStock
 * JD-Core Version:    0.7.0.1
 */