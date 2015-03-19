package dm.jb.ui.inv.barcode;

import dm.jb.db.objects.ProductRow;
import dm.jb.printing.JePrinterException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeFactory;

public class SimpleProductBarCode
  implements ProductBarCodeIf, Printable
{
  private ArrayList<ProductTagCode> _mTagsToBePrinted = null;
  private int _mNextTagIndex = 0;
  private int _mNextCodeIndex = 0;
  private int _mCodeHeight = 100;
  private int _mCodeWidth = 200;
  private boolean _mPOFirstPageDone = false;
  private int lastPagePrinted = -1;
  private int _mLeftBorder = 100;
  private double _mTheXScale = 0.5D;
  private double _mTheYScale = 0.5D;
  
  public String toString()
  {
    return "Simple Format";
  }
  
  public void printSample(Graphics2D paramGraphics2D, double paramDouble1, double paramDouble2)
  {
    if (paramGraphics2D == null) {
      return;
    }
    try
    {
      paramGraphics2D.scale(paramDouble1, paramDouble2);
      Barcode localBarcode = BarcodeFactory.createCode128C("0008970001");
      localBarcode.setBarHeight(40.0D);
      if (paramGraphics2D == null) {
        return;
      }
      localBarcode.paint(paramGraphics2D);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  public void printBarCode(ArrayList<ProductTagCode> paramArrayList, double paramDouble1, double paramDouble2)
    throws JePrinterException
  {
    if ((paramArrayList == null) || (paramArrayList.size() == 0)) {
      return;
    }
    this._mTagsToBePrinted = paramArrayList;
    this.lastPagePrinted = -1;
    this._mNextTagIndex = 0;
    this._mNextCodeIndex = 0;
    this._mPOFirstPageDone = false;
    this._mTheXScale = paramDouble1;
    this._mTheYScale = paramDouble2;
    PrinterJob localPrinterJob = PrinterJob.getPrinterJob();
    localPrinterJob.setPrintable(this);
    if (!localPrinterJob.printDialog()) {
      return;
    }
    try
    {
      localPrinterJob.print();
    }
    catch (PrinterException localPrinterException)
    {
      throw new JePrinterException("Internal Error printing.", "Internal Error", localPrinterException.getMessage());
    }
  }
  
  public int print(Graphics paramGraphics, PageFormat paramPageFormat, int paramInt)
  {
    Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
    localGraphics2D.translate(paramPageFormat.getImageableX(), paramPageFormat.getImageableY());
    int i = this._mTagsToBePrinted.size();
    localGraphics2D.scale(this._mTheXScale, this._mTheYScale);
    if (this._mNextTagIndex == i) {
      return 1;
    }
    if (paramInt != this.lastPagePrinted)
    {
      this._mPOFirstPageDone = true;
      this.lastPagePrinted = paramInt;
      return 0;
    }
    int j = this._mLeftBorder;
    int k = 40;
    int m = (int)(paramPageFormat.getImageableWidth() / this._mTheXScale);
    int n = (int)(paramPageFormat.getImageableHeight() / this._mTheYScale);
    localGraphics2D.translate(j, k);
    try
    {
      while ((k + this._mCodeHeight < n) && (this._mNextTagIndex < i))
      {
        ProductTagCode localProductTagCode = getNextCodeToPrint();
        Barcode localBarcode = BarcodeFactory.createCode128C(localProductTagCode.product.getProductCode());
        localBarcode.setBarHeight(40.0D);
        if (localGraphics2D == null) {
          return 1;
        }
        localBarcode.setBarHeight(40.0D);
        localBarcode.paint(localGraphics2D);
        j += this._mCodeWidth;
        if (j + this._mCodeWidth > m)
        {
          localGraphics2D.translate(this._mCodeWidth - j + this._mLeftBorder, this._mCodeHeight);
          k += this._mCodeHeight;
          j = this._mLeftBorder;
        }
        else
        {
          localGraphics2D.translate(this._mCodeWidth, 0);
        }
      }
    }
    catch (BarcodeException localBarcodeException)
    {
      return 1;
    }
    return 0;
  }
  
  private ProductTagCode getNextCodeToPrint()
  {
    ProductTagCode localProductTagCode = (ProductTagCode)this._mTagsToBePrinted.get(this._mNextTagIndex);
    this._mNextCodeIndex += 1;
    if (this._mNextCodeIndex == localProductTagCode.tagCount)
    {
      this._mNextTagIndex += 1;
      this._mNextCodeIndex = 0;
    }
    return localProductTagCode;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.inv.barcode.SimpleProductBarCode
 * JD-Core Version:    0.7.0.1
 */