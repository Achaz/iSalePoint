package dm.jb.ui.inv.barcode;

import dm.jb.printing.JePrinterException;
import java.awt.Graphics2D;
import java.util.ArrayList;

public abstract interface ProductBarCodeIf
{
  public abstract void printSample(Graphics2D paramGraphics2D, double paramDouble1, double paramDouble2);
  
  public abstract void printBarCode(ArrayList<ProductTagCode> paramArrayList, double paramDouble1, double paramDouble2)
    throws JePrinterException;
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.inv.barcode.ProductBarCodeIf
 * JD-Core Version:    0.7.0.1
 */