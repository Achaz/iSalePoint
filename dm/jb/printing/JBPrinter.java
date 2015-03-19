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
import java.util.ArrayList;
import java.util.Date;

public abstract interface JBPrinter
{
  public abstract void printBill(Bill paramBill)
    throws JePrinterException;
  
  public abstract void printPO(PoInfoRow paramPoInfoRow, VendorRow paramVendorRow)
    throws JePrinterException;
  
  public abstract void reset();
  
  public abstract String getColumnConfigString();
  
  public abstract void setPrintColumnList(ArrayList<PrintColumn> paramArrayList);
  
  public abstract void printStockReturn(StockReturnRow paramStockReturnRow, ArrayList<ReturnStock> paramArrayList, VendorRow paramVendorRow)
    throws JePrinterException;
  
  public abstract void printWhToStore(WhToStoreRow paramWhToStoreRow, ArrayList<AllotStockPanel.SelectedEntry> paramArrayList, StoreInfoRow paramStoreInfoRow)
    throws JePrinterException;
  
  public abstract void printWhToStoreView(ProductRow[] paramArrayOfProductRow, double[] paramArrayOfDouble, Date paramDate, int paramInt, boolean[] paramArrayOfBoolean)
    throws JePrinterException;
  
  public abstract void printPurchaseHistory(double paramDouble, int paramInt, String paramString, Date paramDate1, Date paramDate2, ArrayList<BillTableRow> paramArrayList)
    throws JePrinterException;
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.printing.JBPrinter
 * JD-Core Version:    0.7.0.1
 */