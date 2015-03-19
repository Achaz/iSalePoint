package dm.jb.printing;

import dm.jb.db.objects.BillTableRow;
import dm.jb.db.objects.PoInfoRow;
import dm.jb.db.objects.ProductRow;
import dm.jb.db.objects.StockReturnRow;
import dm.jb.db.objects.StoreInfoRow;
import dm.jb.db.objects.VendorRow;
import dm.jb.db.objects.WhToStoreRow;
import dm.jb.op.bill.Bill;
import dm.jb.ui.inv.AllotStockPanel.SelectedEntry;
import dm.jb.ui.inv.ReturnStock;
import dm.tools.printing.PrinterType;
import dm.tools.utils.CommonConfig;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class Print
{
  private static Print _mInstance = new Print();
  private PrinterType _mPrinterType = null;
  private JBPrinter _mPrinterInstance = null;
  
  public static Print getInstance()
  {
    return _mInstance;
  }
  
  public void setPrinterInstance(String paramString)
  {
    if ((paramString == null) || (paramString.length() == 0))
    {
      this._mPrinterInstance = null;
      return;
    }
    Iterator localIterator = CommonConfig.getInstance().printerTypes.iterator();
    while (localIterator.hasNext())
    {
      PrinterType localPrinterType = (PrinterType)localIterator.next();
      if (localPrinterType.getName().equalsIgnoreCase(paramString))
      {
        this._mPrinterType = localPrinterType;
        break;
      }
    }
    this._mPrinterInstance = this._mPrinterType.getPrintingClassInstance();
  }
  
  public void printBill(Bill paramBill)
    throws JePrinterException
  {
    this._mPrinterInstance.reset();
    this._mPrinterInstance.printBill(paramBill);
  }
  
  public void printPO(PoInfoRow paramPoInfoRow, VendorRow paramVendorRow)
    throws JePrinterException
  {
    this._mPrinterInstance.reset();
    this._mPrinterInstance.printPO(paramPoInfoRow, paramVendorRow);
  }
  
  public void printStockReturn(StockReturnRow paramStockReturnRow, ArrayList<ReturnStock> paramArrayList, VendorRow paramVendorRow)
    throws JePrinterException
  {
    this._mPrinterInstance.reset();
    this._mPrinterInstance.printStockReturn(paramStockReturnRow, paramArrayList, paramVendorRow);
  }
  
  public void printWhToStore(WhToStoreRow paramWhToStoreRow, ArrayList<AllotStockPanel.SelectedEntry> paramArrayList, StoreInfoRow paramStoreInfoRow)
    throws JePrinterException
  {
    this._mPrinterInstance.reset();
    this._mPrinterInstance.printWhToStore(paramWhToStoreRow, paramArrayList, paramStoreInfoRow);
  }
  
  public void printWhToStoreView(ProductRow[] paramArrayOfProductRow, double[] paramArrayOfDouble, Date paramDate, int paramInt, boolean[] paramArrayOfBoolean)
    throws JePrinterException
  {
    this._mPrinterInstance.reset();
    this._mPrinterInstance.printWhToStoreView(paramArrayOfProductRow, paramArrayOfDouble, paramDate, paramInt, paramArrayOfBoolean);
  }
  
  public void printPurchaseHistory(double paramDouble, int paramInt, String paramString, Date paramDate1, Date paramDate2, ArrayList<BillTableRow> paramArrayList)
    throws JePrinterException
  {
    this._mPrinterInstance.reset();
    this._mPrinterInstance.printPurchaseHistory(paramDouble, paramInt, paramString, paramDate1, paramDate2, paramArrayList);
  }
  
  public JBPrinter getPrinterInstance()
  {
    return this._mPrinterInstance;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.printing.Print
 * JD-Core Version:    0.7.0.1
 */