package dm.jb.ui.inv;

import dm.jb.db.objects.ProductRow;
import java.util.ArrayList;

public abstract interface ProductListListener
{
  public abstract void productListSelectClicked(ArrayList<ProductRow> paramArrayList);
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.inv.ProductListListener
 * JD-Core Version:    0.7.0.1
 */