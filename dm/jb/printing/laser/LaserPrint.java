package dm.jb.printing.laser;

import dm.jb.JeException;
import dm.jb.db.objects.BillTableRow;
import dm.jb.db.objects.CurrentStockRow;
import dm.jb.db.objects.PoEntryRow;
import dm.jb.db.objects.PoInfoRow;
import dm.jb.db.objects.ProductRow;
import dm.jb.db.objects.StockAndProductRow;
import dm.jb.db.objects.StockReturnRow;
import dm.jb.db.objects.StoreInfoRow;
import dm.jb.db.objects.StoreInfoTableDef;
import dm.jb.db.objects.VendorRow;
import dm.jb.db.objects.WearehouseInfoRow;
import dm.jb.db.objects.WearehouseInfoTableDef;
import dm.jb.db.objects.WhToStoreRow;
import dm.jb.op.bill.Bill;
import dm.jb.op.bill.BillProduct;
import dm.jb.printing.BillPrintCommon;
import dm.jb.printing.JBPrinter;
import dm.jb.printing.JePrinterException;
import dm.jb.printing.PrintColumn;
import dm.jb.ui.inv.AllotStockPanel.SelectedEntry;
import dm.jb.ui.inv.ReturnStock;
import dm.jb.ui.inv.StockInfoAndCurrentStock;
import dm.jb.ui.settings.CountryInfo;
import dm.tools.db.DBException;
import dm.tools.types.InternalAmount;
import dm.tools.types.InternalQuantity;
import dm.tools.utils.CommonConfig;
import dm.tools.utils.Config;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeFactory;

public class LaserPrint
  implements JBPrinter, Printable
{
  private static final short PRINT_BILL = 1;
  private static final short PRINT_PO = 2;
  private static final short PRINT_RETURN = 3;
  private static final short PRINT_WHTOSTORE = 4;
  private static final short PRINT_WHVIEW = 5;
  private static final short PRINT_PURCHASE_HISTORY = 6;
  private static final byte ALIGN_LEFT = 0;
  private static final byte ALIGN_RIGHT = 1;
  private ArrayList<PrintColumn> _mPrintColList = null;
  private boolean totalPrinted = false;
  private Font fnb = null;
  private Font fn = null;
  private Font fixedFont = null;
  private int _mCurrentPageIndex = -1;
  private boolean _mTablePrintCompleted = false;
  private Bill _mBillTable = null;
  private TablePrint _mTablePrint = null;
  private int lastPagePrinted = -1;
  private short _mPrintType = 1;
  private int _mBillLeftMargin = 10;
  private int _mBillHeaderGap = 10;
  private int _mBillTopMargin = 50;
  private int _mBillBottomMargin = 50;
  private boolean _mBillPrintColumnHeader = true;
  private boolean _mBillPrintColumnLines = false;
  private boolean _mBillPrintRowLines = false;
  private boolean _mBillPrintBarCode = false;
  private int _mTopMargin = 50;
  private int _mLeftMargin = 70;
  private int _mRightMargin = 60;
  private int _mBottomMargin = 60;
  private int _mTotalPageWidth = 0;
  private boolean _mPrintColumnHeader = true;
  private boolean _mPrintColumnLines = false;
  private boolean _mPrintRowLines = false;
  private boolean _mAllDataPrinted = false;
  private boolean _mFirstPageDone = false;
  private PoInfoRow _mPO = null;
  private VendorRow _mPOVendor = null;
  private int _mPOEntryIndex = 0;
  private ArrayList<ReturnStock> _mReturnEntries = null;
  private VendorRow _mReturnVendorRow = null;
  private StockReturnRow _mStockReturnRow = null;
  private int _mReturnEntryIndex = 0;
  private WhToStoreRow _mWhToStore = null;
  private ArrayList<AllotStockPanel.SelectedEntry> _mWhStoreEntries = null;
  private StoreInfoRow _mWhToStoreStore = null;
  private int _mWhToStoreIndex = 0;
  private ProductRow[] _mWhViewProds = null;
  private double[] _mWhViewQty = null;
  private int _mWhViewIndex = 0;
  private int _mWhViewTxnNo = 0;
  private Date _mWhViewCreateDate = null;
  private boolean[] _mWhViewUpdated = null;
  private double _mTotalHistoryAmount = 0.0D;
  private int _mTotalHistoryPoints = 0;
  private Date _mHistFromDate = null;
  private Date _mHistToDate = null;
  private String _mHistCustomer = null;
  private ArrayList<BillTableRow> _mHistoryBillRows = null;
  private int _mCurrentHistoryIndex = 0;
  private boolean _mHistoryTotalPrinted = false;
  
  public LaserPrint()
  {
    BillPrintCommon localBillPrintCommon = BillPrintCommon.getInstance();
    this._mPrintColList = localBillPrintCommon.getPrintColumns();
  }
  
  public void reset()
  {
    this.totalPrinted = false;
    this._mCurrentPageIndex = -1;
    this._mTablePrintCompleted = false;
    this._mBillTable = null;
    this._mTablePrint = null;
    String str = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.LASER.GENERAL.TOP_MARGIN.VALUE");
    if (str != null) {
      this._mTopMargin = Integer.valueOf(str).intValue();
    }
    str = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.LASER.GENERAL.BOTTOM_MARGIN.VALUE");
    if (str != null) {
      this._mBottomMargin = Integer.valueOf(str).intValue();
    }
    str = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.LASER.GENERAL.RIGHT_MARGIN.VALUE");
    if (str != null) {
      this._mRightMargin = Integer.valueOf(str).intValue();
    }
    str = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.LASER.GENERAL.LEFT_MARGIN.VALUE");
    if (str != null) {
      this._mLeftMargin = Integer.valueOf(str).intValue();
    }
    str = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.LASER.GENERAL.PRINT_COLUMN_HEADER.VALUE");
    if (str != null) {
      this._mPrintColumnHeader = Boolean.valueOf(str).booleanValue();
    }
    str = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.LASER.GENERAL.COLUMNS.VALUE");
    if (str != null) {
      this._mPrintColumnLines = Boolean.valueOf(str).booleanValue();
    }
    str = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.LASER.GENERAL.ROWS.VALUE");
    if (str != null) {
      this._mPrintRowLines = Boolean.valueOf(str).booleanValue();
    }
    str = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.LASER.BILL.LEFT_MARGIN.VALUE");
    if (str != null) {
      this._mBillLeftMargin = Integer.valueOf(str).intValue();
    }
    str = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.LASER.BILL.TOP_MARGIN.VALUE");
    if (str != null) {
      this._mBillTopMargin = Integer.valueOf(str).intValue();
    }
    str = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.LASER.BILL.BOTTOM_MARGIN.VALUE");
    if (str != null) {
      this._mBillBottomMargin = Integer.valueOf(str).intValue();
    }
    str = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.LASER.BILL.HEADER_GAP.VALUE");
    if (str != null) {
      this._mBillHeaderGap = Integer.valueOf(str).intValue();
    }
    str = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.LASER.BILL.PRINT_COLUMN_HEADER.VALUE");
    if (str != null) {
      this._mBillPrintColumnHeader = Boolean.valueOf(str).booleanValue();
    }
    str = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.LASER.BILL.COLUMNS.VALUE");
    if (str != null) {
      this._mBillPrintColumnLines = Boolean.valueOf(str).booleanValue();
    }
    str = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.LASER.BILL.ROWS.VALUE");
    if (str != null) {
      this._mBillPrintRowLines = Boolean.valueOf(str).booleanValue();
    }
    str = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.LASER.BILL.BARCODE.VALUE");
    if (str != null) {
      this._mBillPrintBarCode = Boolean.valueOf(str).booleanValue();
    }
  }
  
  public void printBill(Bill paramBill)
    throws JePrinterException
  {
    this._mBillTable = paramBill;
    this._mPrintType = 1;
    PrinterJob localPrinterJob = PrinterJob.getPrinterJob();
    localPrinterJob.setPrintable(this);
    if (localPrinterJob.printDialog()) {
      try
      {
        localPrinterJob.print();
      }
      catch (PrinterException localPrinterException)
      {
        throw new JePrinterException("Internal Error printing.", "Internal Error", localPrinterException.getMessage());
      }
    }
  }
  
  public void printPO(PoInfoRow paramPoInfoRow, VendorRow paramVendorRow)
    throws JePrinterException
  {
    this._mPrintType = 2;
    this._mPO = paramPoInfoRow;
    this._mPOVendor = paramVendorRow;
    this._mAllDataPrinted = false;
    this.lastPagePrinted = -1;
    this._mFirstPageDone = false;
    this._mPOEntryIndex = 0;
    PrinterJob localPrinterJob = PrinterJob.getPrinterJob();
    localPrinterJob.setPrintable(this);
    if (localPrinterJob.printDialog()) {
      try
      {
        localPrinterJob.print();
      }
      catch (PrinterException localPrinterException)
      {
        throw new JePrinterException("Internal Error printing.", "Internal Error", localPrinterException.getMessage());
      }
    }
  }
  
  public void printStockReturn(StockReturnRow paramStockReturnRow, ArrayList<ReturnStock> paramArrayList, VendorRow paramVendorRow)
    throws JePrinterException
  {
    this._mPrintType = 3;
    this._mReturnEntries = paramArrayList;
    this._mReturnVendorRow = paramVendorRow;
    this._mStockReturnRow = paramStockReturnRow;
    PrinterJob localPrinterJob = PrinterJob.getPrinterJob();
    this._mReturnEntryIndex = 0;
    localPrinterJob.setPrintable(this);
    if (localPrinterJob.printDialog()) {
      try
      {
        localPrinterJob.print();
      }
      catch (PrinterException localPrinterException)
      {
        throw new JePrinterException("Internal Error printing.", "Internal Error", localPrinterException.getMessage());
      }
    }
  }
  
  public void printWhToStore(WhToStoreRow paramWhToStoreRow, ArrayList<AllotStockPanel.SelectedEntry> paramArrayList, StoreInfoRow paramStoreInfoRow)
    throws JePrinterException
  {
    this._mWhToStore = paramWhToStoreRow;
    this._mWhToStoreStore = paramStoreInfoRow;
    this._mWhStoreEntries = paramArrayList;
    this._mWhToStoreIndex = 0;
    this._mPrintType = 4;
    PrinterJob localPrinterJob = PrinterJob.getPrinterJob();
    localPrinterJob.setPrintable(this);
    if (localPrinterJob.printDialog()) {
      try
      {
        localPrinterJob.print();
      }
      catch (PrinterException localPrinterException)
      {
        throw new JePrinterException("Internal Error printing.", "Internal Error", localPrinterException.getMessage());
      }
    }
  }
  
  public void printWhToStoreView(ProductRow[] paramArrayOfProductRow, double[] paramArrayOfDouble, Date paramDate, int paramInt, boolean[] paramArrayOfBoolean)
    throws JePrinterException
  {
    this._mWhViewCreateDate = paramDate;
    this._mWhViewTxnNo = paramInt;
    this._mWhViewUpdated = paramArrayOfBoolean;
    this._mWhViewProds = paramArrayOfProductRow;
    this._mWhViewQty = paramArrayOfDouble;
    this._mWhViewIndex = 0;
    this._mPrintType = 5;
    PrinterJob localPrinterJob = PrinterJob.getPrinterJob();
    localPrinterJob.setPrintable(this);
    if (localPrinterJob.printDialog()) {
      try
      {
        localPrinterJob.print();
      }
      catch (PrinterException localPrinterException)
      {
        throw new JePrinterException("Internal Error printing.", "Internal Error", localPrinterException.getMessage());
      }
    }
  }
  
  public void printPurchaseHistory(double paramDouble, int paramInt, String paramString, Date paramDate1, Date paramDate2, ArrayList<BillTableRow> paramArrayList)
    throws JePrinterException
  {
    this._mTotalHistoryAmount = paramDouble;
    this._mTotalHistoryPoints = paramInt;
    this._mHistToDate = paramDate2;
    this._mHistFromDate = paramDate1;
    this._mHistoryBillRows = paramArrayList;
    this._mHistCustomer = paramString;
    this._mPrintType = 6;
    PrinterJob localPrinterJob = PrinterJob.getPrinterJob();
    this._mCurrentHistoryIndex = 0;
    localPrinterJob.setPrintable(this);
    if (localPrinterJob.printDialog()) {
      try
      {
        localPrinterJob.print();
      }
      catch (PrinterException localPrinterException)
      {
        throw new JePrinterException("Internal Error printing.", "Internal Error", localPrinterException.getMessage());
      }
    }
  }
  
  public int print(Graphics paramGraphics, PageFormat paramPageFormat, int paramInt)
  {
    switch (this._mPrintType)
    {
    case 1: 
      return printBillInternal(paramGraphics, paramPageFormat, paramInt);
    case 2: 
      return printPOInternal(paramGraphics, paramPageFormat, paramInt);
    case 3: 
      return printReturnInternal(paramGraphics, paramPageFormat, paramInt);
    case 4: 
      return printWhToStoreInternal(paramGraphics, paramPageFormat, paramInt);
    case 5: 
      return printWhViewInternal(paramGraphics, paramPageFormat, paramInt);
    case 6: 
      return printPurchaseHistoryInternal(paramGraphics, paramPageFormat, paramInt);
    }
    return 1;
  }
  
  private int printPurchaseHistoryInternal(Graphics paramGraphics, PageFormat paramPageFormat, int paramInt)
  {
    Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
    localGraphics2D.translate(paramPageFormat.getImageableX() + this._mLeftMargin, paramPageFormat.getImageableY() + this._mTopMargin);
    if ((this._mAllDataPrinted) && (this._mHistoryTotalPrinted)) {
      return 1;
    }
    if ((this._mAllDataPrinted) && (!this._mHistoryTotalPrinted))
    {
      this._mHistToDate = null;
      this._mHistFromDate = null;
      this._mHistoryBillRows.clear();
      this._mHistoryBillRows = null;
      this._mHistCustomer = null;
      this._mCurrentHistoryIndex = 0;
    }
    if (paramInt != this.lastPagePrinted)
    {
      this._mFirstPageDone = true;
      this.lastPagePrinted = paramInt;
      return 0;
    }
    int i = 0;
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(CommonConfig.getInstance().dateFormat);
    if (paramInt == 0)
    {
      this._mTotalPageWidth = ((int)(paramPageFormat.getImageableWidth() - (this._mLeftMargin + this._mRightMargin)));
      localObject1 = new Font("Courier New", 0, 10);
      localGraphics2D.setFont((Font)localObject1);
      localObject2 = new Font("Courier New", 1, 20);
      localGraphics2D.setFont((Font)localObject2);
      FontRenderContext localFontRenderContext = localGraphics2D.getFontRenderContext();
      localObject3 = ((Font)localObject2).getStringBounds("Customer Purchase History", localFontRenderContext);
      int k = (int)((Rectangle2D)localObject3).getWidth();
      n = (int)((Rectangle2D)localObject3).getHeight();
      i1 = (this._mTotalPageWidth - k) / 2;
      localGraphics2D.drawString("Customer Purchase History", i1, n);
      localGraphics2D.drawLine(i1, n + 2, i1 + k, n + 2);
      i = 40;
      localObject4 = new Font(((Font)localObject1).getName(), 1, ((Font)localObject1).getSize());
      localGraphics2D.setFont((Font)localObject4);
      int i2 = (int)((Font)localObject1).getStringBounds("Wy", localFontRenderContext).getHeight();
      localGraphics2D.drawString("Customer : ", 0, i);
      i += i2;
      i += 2;
      localGraphics2D.setFont((Font)localObject1);
      i2 = (int)((Font)localObject1).getStringBounds("Wy", localFontRenderContext).getHeight();
      localObject5 = new StringTokenizer(this._mHistCustomer, "\n");
      while (((StringTokenizer)localObject5).hasMoreTokens())
      {
        localGraphics2D.drawString(((StringTokenizer)localObject5).nextToken(), 20, i);
        i += i2;
        i += 2;
      }
      String str1 = "";
      if (this._mHistFromDate != null) {
        str1 = localSimpleDateFormat.format(this._mHistFromDate);
      }
      str2 = "Current";
      if (this._mHistToDate != null) {
        str2 = localSimpleDateFormat.format(this._mHistToDate);
      }
      String str3 = "Duration : " + str1 + " To " + str2;
      localGraphics2D.drawString(str3, 20, i);
      i += i2;
      i += 2;
    }
    else
    {
      i += 4;
    }
    Object localObject1 = null;
    Object localObject2 = { 40, 80, 75, 80, 80, 80 };
    int j = 0;
    if (this._mPrintColumnHeader)
    {
      localObject3 = new Font("Courier New", 1, 10);
      localGraphics2D.setFont((Font)localObject3);
      String[] arrayOfString1 = { "Sl. No", "Rcpt. No", "Date", "Amount(" + CommonConfig.getInstance().country.currency + ")", "Pts. Earned", "Pts. Redeemed" };
      localObject1 = localGraphics2D.getFontRenderContext();
      j = (int)((Font)localObject3).getStringBounds("W", (FontRenderContext)localObject1).getHeight();
      printTableHeaders(localGraphics2D, 20, i, arrayOfString1, (int[])localObject2, j, this._mPrintColumnLines, this._mPrintRowLines);
      i += j + 6;
    }
    Object localObject3 = new Font("Courier New", 0, 10);
    localGraphics2D.setFont((Font)localObject3);
    localObject1 = localGraphics2D.getFontRenderContext();
    j = (int)((Font)localObject3).getStringBounds("W", (FontRenderContext)localObject1).getHeight();
    int m = 0;
    int n = this._mHistoryBillRows.size();
    int i1 = (int)paramPageFormat.getImageableHeight() - this._mBottomMargin;
    while ((m == 0) && (this._mCurrentHistoryIndex < n))
    {
      if (i + j + 4 >= i1) {
        return 0;
      }
      localObject4 = (BillTableRow)this._mHistoryBillRows.get(this._mCurrentHistoryIndex);
      this._mCurrentHistoryIndex += 1;
      String[] arrayOfString2 = { this._mCurrentHistoryIndex + "  ", ((BillTableRow)localObject4).getBillNo() + " ", localSimpleDateFormat.format(((BillTableRow)localObject4).getBillDate()), InternalAmount.toString(((BillTableRow)localObject4).getAmount()), ((BillTableRow)localObject4).getPointsAwarded() + " ", ((BillTableRow)localObject4).getPointsRedeemed() + " " };
      localObject5 = new byte[] { 1, 1, 1, 1, 1, 1 };
      int i4 = getTableHorizontalLineTotalWidth((int[])localObject2);
      printTableRowInternal(localGraphics2D, arrayOfString2, (int[])localObject2, (byte[])localObject5, 20, i, this._mPrintColumnLines, this._mPrintRowLines, i4, j);
      i += j + 5;
    }
    if (i + j + 4 >= i1) {
      return 0;
    }
    i += 16;
    Object localObject4 = new Font(paramGraphics.getFont().getName(), 1, 12);
    localObject1 = localGraphics2D.getFontRenderContext();
    int i3 = (int)((Font)localObject4).getStringBounds("Total Amount :  ", (FontRenderContext)localObject1).getWidth();
    Object localObject5 = new InternalAmount(this._mTotalHistoryAmount);
    Font localFont = new Font(paramGraphics.getFont().getName(), 0, 12);
    String str2 = ((InternalAmount)localObject5).toString() + " " + CommonConfig.getInstance().country.currency;
    int i5 = (int)localFont.getStringBounds(str2, (FontRenderContext)localObject1).getWidth();
    String str4 = this._mTotalHistoryPoints + " ";
    int i6 = (int)localFont.getStringBounds(str4, (FontRenderContext)localObject1).getWidth();
    int i7 = i5 > i6 ? i5 : i6;
    localGraphics2D.setFont((Font)localObject4);
    localGraphics2D.drawString("Total Amount :  ", this._mTotalPageWidth - (i3 + i7), i);
    localGraphics2D.setFont(localFont);
    localGraphics2D.drawString(str2, this._mTotalPageWidth - i7, i);
    i += 16;
    i3 = (int)((Font)localObject4).getStringBounds("Total Points :  ", (FontRenderContext)localObject1).getWidth();
    localGraphics2D.setFont((Font)localObject4);
    localGraphics2D.drawString("Total Points :  ", this._mTotalPageWidth - (i3 + i7), i);
    localGraphics2D.setFont(localFont);
    localGraphics2D.drawString(str4, this._mTotalPageWidth - i7, i);
    this._mHistoryTotalPrinted = true;
    this._mTotalHistoryAmount = 0.0D;
    this._mTotalHistoryPoints = 0;
    this._mAllDataPrinted = true;
    return 0;
  }
  
  private int printWhViewInternal(Graphics paramGraphics, PageFormat paramPageFormat, int paramInt)
  {
    Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
    localGraphics2D.translate(paramPageFormat.getImageableX() + this._mLeftMargin, paramPageFormat.getImageableY() + this._mTopMargin);
    if (this._mAllDataPrinted)
    {
      this._mWhViewCreateDate = null;
      this._mWhViewUpdated = null;
      this._mWhViewProds = null;
      this._mWhViewQty = null;
      return 1;
    }
    if (paramInt != this.lastPagePrinted)
    {
      this._mFirstPageDone = true;
      this.lastPagePrinted = paramInt;
      return 0;
    }
    int i = 0;
    Object localObject4;
    Object localObject5;
    Object localObject6;
    if (paramInt == 0)
    {
      this._mTotalPageWidth = ((int)(paramPageFormat.getImageableWidth() - (this._mLeftMargin + this._mRightMargin)));
      localObject1 = new Font("Courier New", 0, 10);
      localGraphics2D.setFont((Font)localObject1);
      localObject2 = new String[] { "Transaction. No.", "Date" };
      SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(CommonConfig.getInstance().dateFormat);
      localObject3 = new String[] { this._mWhViewTxnNo + "", localSimpleDateFormat.format(this._mWhViewCreateDate) };
      printRightAlignedHeaderAndText(localGraphics2D, (String[])localObject2, (String[])localObject3, (Font)localObject1, 30, this._mTotalPageWidth);
      i = 30;
      localObject4 = new Font(((Font)localObject1).getName(), 1, ((Font)localObject1).getSize());
      localGraphics2D.setFont((Font)localObject4);
      FontRenderContext localFontRenderContext = localGraphics2D.getFontRenderContext();
      n = (int)((Font)localObject1).getStringBounds("Wy", localFontRenderContext).getHeight();
      localGraphics2D.drawString("Store : ", 0, i);
      i += n;
      i += 2;
      localGraphics2D.setFont((Font)localObject1);
      n = (int)((Font)localObject1).getStringBounds("Wy", localFontRenderContext).getHeight();
      localObject5 = StoreInfoTableDef.getCurrentStore();
      if (localObject5 != null)
      {
        localGraphics2D.drawString(((StoreInfoRow)localObject5).getName(), 10, i);
        i += n;
        i += 2;
        localObject6 = new StringTokenizer(((StoreInfoRow)localObject5).getAddress());
        while (((StringTokenizer)localObject6).hasMoreTokens())
        {
          localGraphics2D.drawString(((StringTokenizer)localObject6).nextToken(), 10, i);
          i += n;
          i += 2;
        }
      }
    }
    else
    {
      i += 4;
    }
    Object localObject1 = null;
    Object localObject2 = { 40, 80, 140, 80, 80 };
    int j = 0;
    if (this._mPrintColumnHeader)
    {
      localObject3 = new Font("Courier New", 1, 10);
      localGraphics2D.setFont((Font)localObject3);
      localObject4 = new String[] { "Sl.", "Product Code", "Product Name", "Quantity", "Updated " };
      localObject1 = localGraphics2D.getFontRenderContext();
      j = (int)((Font)localObject3).getStringBounds("W", (FontRenderContext)localObject1).getHeight();
      printTableHeaders(localGraphics2D, 20, i, (String[])localObject4, (int[])localObject2, j, this._mPrintColumnLines, this._mPrintRowLines);
      i += j + 6;
    }
    Object localObject3 = new Font("Courier New", 0, 10);
    localGraphics2D.setFont((Font)localObject3);
    localObject1 = localGraphics2D.getFontRenderContext();
    j = (int)((Font)localObject3).getStringBounds("W", (FontRenderContext)localObject1).getHeight();
    int k = 0;
    int m = this._mWhViewProds.length;
    int n = (int)paramPageFormat.getImageableHeight() - this._mBottomMargin;
    while ((k == 0) && (this._mWhViewIndex < m))
    {
      if (i + j + 4 >= n) {
        return 0;
      }
      localObject5 = this._mWhViewProds[this._mWhViewIndex];
      localObject6 = new InternalQuantity(this._mWhViewQty[this._mWhViewIndex], ((ProductRow)localObject5).getProdUnit(), true);
      int i1 = this._mWhViewUpdated[this._mWhViewIndex];
      this._mWhViewIndex += 1;
      String[] arrayOfString = { this._mWhViewIndex + "  ", ((ProductRow)localObject5).getProductCode(), ((ProductRow)localObject5).getProdName(), ((InternalQuantity)localObject6).toString(), i1 != 0 ? "YES" : "NO" };
      byte[] arrayOfByte = { 1, 0, 0, 1, 0 };
      int i2 = getTableHorizontalLineTotalWidth((int[])localObject2);
      printTableRowInternal(localGraphics2D, arrayOfString, (int[])localObject2, arrayOfByte, 20, i, this._mPrintColumnLines, this._mPrintRowLines, i2, j);
      i += j + 5;
    }
    this._mAllDataPrinted = true;
    return 0;
  }
  
  private int printWhToStoreInternal(Graphics paramGraphics, PageFormat paramPageFormat, int paramInt)
  {
    Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
    localGraphics2D.translate(paramPageFormat.getImageableX() + this._mLeftMargin, paramPageFormat.getImageableY() + this._mTopMargin);
    if (this._mAllDataPrinted)
    {
      this._mWhStoreEntries.clear();
      this._mWhStoreEntries = null;
      return 1;
    }
    if (paramInt != this.lastPagePrinted)
    {
      this._mFirstPageDone = true;
      this.lastPagePrinted = paramInt;
      return 0;
    }
    int i = 0;
    Object localObject4;
    Object localObject5;
    if (paramInt == 0)
    {
      this._mTotalPageWidth = ((int)(paramPageFormat.getImageableWidth() - (this._mLeftMargin + this._mRightMargin)));
      localObject1 = new Font("Courier New", 0, 10);
      localGraphics2D.setFont((Font)localObject1);
      localObject2 = new String[] { "Transaction. No.", "Date" };
      SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(CommonConfig.getInstance().dateFormat);
      localObject3 = new String[] { this._mWhToStore.getTxnNo() + "", localSimpleDateFormat.format(this._mWhToStore.getDate()) };
      printRightAlignedHeaderAndText(localGraphics2D, (String[])localObject2, (String[])localObject3, (Font)localObject1, 30, this._mTotalPageWidth);
      i = 30;
      localObject4 = new Font(((Font)localObject1).getName(), 1, ((Font)localObject1).getSize());
      localGraphics2D.setFont((Font)localObject4);
      FontRenderContext localFontRenderContext = localGraphics2D.getFontRenderContext();
      n = (int)((Font)localObject1).getStringBounds("Wy", localFontRenderContext).getHeight();
      localGraphics2D.drawString("Store : ", 0, i);
      i += n;
      i += 2;
      localGraphics2D.setFont((Font)localObject1);
      n = (int)((Font)localObject1).getStringBounds("Wy", localFontRenderContext).getHeight();
      if (this._mWhToStoreStore != null)
      {
        localGraphics2D.drawString(this._mWhToStoreStore.getName(), 10, i);
        i += n;
        i += 2;
        localObject5 = new StringTokenizer(this._mWhToStoreStore.getAddress());
        while (((StringTokenizer)localObject5).hasMoreTokens())
        {
          localGraphics2D.drawString(((StringTokenizer)localObject5).nextToken(), 10, i);
          i += n;
          i += 2;
        }
      }
    }
    else
    {
      i += 4;
    }
    Object localObject1 = null;
    Object localObject2 = { 40, 80, 140, 80, 100 };
    int j = 0;
    if (this._mPrintColumnHeader)
    {
      localObject3 = new Font("Courier New", 1, 10);
      localGraphics2D.setFont((Font)localObject3);
      localObject4 = new String[] { "Sl.", "Product Code", "Product Name", "Quantity", "Warehouse" };
      localObject1 = localGraphics2D.getFontRenderContext();
      j = (int)((Font)localObject3).getStringBounds("W", (FontRenderContext)localObject1).getHeight();
      printTableHeaders(localGraphics2D, 0, i, (String[])localObject4, (int[])localObject2, j, this._mPrintColumnLines, this._mPrintRowLines);
      i += j + 6;
    }
    Object localObject3 = new Font("Courier New", 0, 10);
    localGraphics2D.setFont((Font)localObject3);
    localObject1 = localGraphics2D.getFontRenderContext();
    j = (int)((Font)localObject3).getStringBounds("W", (FontRenderContext)localObject1).getHeight();
    int k = 0;
    int m = this._mWhStoreEntries.size();
    int n = (int)paramPageFormat.getImageableHeight() - this._mBottomMargin;
    while ((k == 0) && (this._mWhToStoreIndex < m))
    {
      if (i + j + 4 >= n) {
        return 0;
      }
      localObject5 = (AllotStockPanel.SelectedEntry)this._mWhStoreEntries.get(this._mWhToStoreIndex);
      InternalQuantity localInternalQuantity = new InternalQuantity(((AllotStockPanel.SelectedEntry)localObject5)._mQuantity.doubleValue(), ((AllotStockPanel.SelectedEntry)localObject5)._mProduct.getProdUnit(), true);
      WearehouseInfoRow localWearehouseInfoRow = null;
      this._mWhToStoreIndex += 1;
      try
      {
        localWearehouseInfoRow = WearehouseInfoTableDef.getInstance().getWarehouseForIndex(((AllotStockPanel.SelectedEntry)localObject5)._mStockRow.currentStockRow.getWearHouseIndex());
      }
      catch (DBException localDBException)
      {
        localDBException.printStackTrace();
      }
      String[] arrayOfString = { this._mWhToStoreIndex + "  ", ((AllotStockPanel.SelectedEntry)localObject5)._mProduct.getProductCode(), ((AllotStockPanel.SelectedEntry)localObject5)._mProduct.getProdName(), localInternalQuantity.toString(), localWearehouseInfoRow == null ? "NA" : localWearehouseInfoRow.getWearehouseName() };
      byte[] arrayOfByte = { 1, 0, 0, 1, 0 };
      int i1 = getTableHorizontalLineTotalWidth((int[])localObject2);
      printTableRowInternal(localGraphics2D, arrayOfString, (int[])localObject2, arrayOfByte, 0, i, this._mPrintColumnLines, this._mPrintRowLines, i1, j);
      i += j + 5;
    }
    this._mAllDataPrinted = true;
    return 0;
  }
  
  private int printReturnInternal(Graphics paramGraphics, PageFormat paramPageFormat, int paramInt)
  {
    Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
    localGraphics2D.translate(paramPageFormat.getImageableX() + this._mLeftMargin, paramPageFormat.getImageableY() + this._mTopMargin);
    if (this._mAllDataPrinted) {
      return 1;
    }
    if (paramInt != this.lastPagePrinted)
    {
      this._mFirstPageDone = true;
      this.lastPagePrinted = paramInt;
      return 0;
    }
    int i = 0;
    Object localObject3;
    Object localObject4;
    String[] arrayOfString2;
    Object localObject5;
    int i3;
    if (paramInt == 0)
    {
      this._mTotalPageWidth = ((int)(paramPageFormat.getImageableWidth() - (this._mLeftMargin + this._mRightMargin)));
      localObject1 = new Font("Courier New", 1, 32);
      localGraphics2D.setFont((Font)localObject1);
      localObject2 = localGraphics2D.getFontRenderContext();
      Rectangle2D localRectangle2D = ((Font)localObject1).getStringBounds("Goods Return", (FontRenderContext)localObject2);
      int k = (int)localRectangle2D.getWidth();
      int m = (int)localRectangle2D.getHeight();
      i1 = (this._mTotalPageWidth - k) / 2;
      localGraphics2D.drawString("Goods Return", i1, m);
      localGraphics2D.drawLine(i1, m + 2, i1 + k, m + 2);
      Font localFont2 = new Font("Courier New", 0, 10);
      localGraphics2D.setFont(localFont2);
      localObject3 = new String[] { "Transaction. No.", "Date" };
      localObject4 = new SimpleDateFormat(CommonConfig.getInstance().dateFormat);
      arrayOfString2 = new String[] { this._mStockReturnRow.getTxnNo() + "", ((SimpleDateFormat)localObject4).format(this._mStockReturnRow.getCreateDate()) };
      printRightAlignedHeaderAndText(localGraphics2D, (String[])localObject3, arrayOfString2, localFont2, m + 30, this._mTotalPageWidth);
      i = m + 30;
      localObject5 = new Font(localFont2.getName(), 1, localFont2.getSize());
      localGraphics2D.setFont((Font)localObject5);
      localObject2 = localGraphics2D.getFontRenderContext();
      i3 = (int)localFont2.getStringBounds("Wy", (FontRenderContext)localObject2).getHeight();
      localGraphics2D.drawString("Vendor : ", 0, i);
      i += i3;
      i += 2;
      localGraphics2D.setFont(localFont2);
      i3 = (int)localFont2.getStringBounds("Wy", (FontRenderContext)localObject2).getHeight();
      if (this._mReturnVendorRow != null)
      {
        localGraphics2D.drawString(this._mReturnVendorRow.getVendorName(), 10, i);
        i += i3;
        i += 2;
        StringTokenizer localStringTokenizer = new StringTokenizer(this._mReturnVendorRow.getVendorAddress());
        while (localStringTokenizer.hasMoreTokens())
        {
          localGraphics2D.drawString(localStringTokenizer.nextToken(), 10, i);
          i += i3;
          i += 2;
        }
      }
    }
    else
    {
      i += 4;
    }
    Object localObject1 = null;
    Object localObject2 = { 40, 100, 140, 80, 120 };
    int j = 0;
    if (this._mPrintColumnHeader)
    {
      localFont1 = new Font("Courier New", 1, 10);
      localGraphics2D.setFont(localFont1);
      String[] arrayOfString1 = { "Sl.", "Product Code", "Product Name", "Quantity", "Details" };
      localObject1 = localGraphics2D.getFontRenderContext();
      j = (int)localFont1.getStringBounds("W", (FontRenderContext)localObject1).getHeight();
      printTableHeaders(localGraphics2D, 0, i, arrayOfString1, (int[])localObject2, j, this._mPrintColumnLines, this._mPrintRowLines);
      i += j + 6;
    }
    Font localFont1 = new Font("Courier New", 0, 10);
    localGraphics2D.setFont(localFont1);
    localObject1 = localGraphics2D.getFontRenderContext();
    j = (int)localFont1.getStringBounds("W", (FontRenderContext)localObject1).getHeight();
    int n = 0;
    int i1 = this._mReturnEntries.size();
    int i2 = (int)paramPageFormat.getImageableHeight() - this._mBottomMargin;
    while ((n == 0) && (this._mReturnEntryIndex < i1))
    {
      if (i + j + 4 >= i2) {
        return 0;
      }
      localObject3 = (ReturnStock)this._mReturnEntries.get(this._mReturnEntryIndex);
      this._mReturnEntryIndex += 1;
      localObject4 = new InternalQuantity(((ReturnStock)localObject3).quantity, ((ReturnStock)localObject3).productRow.getProdUnit(), true);
      arrayOfString2 = new String[] { this._mReturnEntryIndex + "  ", ((ReturnStock)localObject3).productRow.getProductCode(), ((ReturnStock)localObject3).productRow.getProdName(), ((InternalQuantity)localObject4).toString(), ((ReturnStock)localObject3).getDetailsForPrinting() };
      localObject5 = new byte[] { 1, 0, 0, 1, 0 };
      i3 = getTableHorizontalLineTotalWidth((int[])localObject2);
      printTableRowInternal(localGraphics2D, arrayOfString2, (int[])localObject2, (byte[])localObject5, 0, i, this._mPrintColumnLines, this._mPrintRowLines, i3, j);
      i += j + 5;
    }
    this._mAllDataPrinted = true;
    return 0;
  }
  
  private int printPOInternal(Graphics paramGraphics, PageFormat paramPageFormat, int paramInt)
  {
    Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
    localGraphics2D.translate(paramPageFormat.getImageableX() + this._mLeftMargin, paramPageFormat.getImageableY() + this._mTopMargin);
    if (this._mAllDataPrinted) {
      return 1;
    }
    if (paramInt != this.lastPagePrinted)
    {
      this._mFirstPageDone = true;
      this.lastPagePrinted = paramInt;
      return 0;
    }
    double d1 = 0;
    Object localObject3;
    Object localObject4;
    Object localObject5;
    Object localObject6;
    if (paramInt == 0)
    {
      this._mTotalPageWidth = ((int)(paramPageFormat.getImageableWidth() - (this._mLeftMargin + this._mRightMargin)));
      localObject1 = new Font("Courier New", 1, 32);
      localGraphics2D.setFont((Font)localObject1);
      localObject2 = localGraphics2D.getFontRenderContext();
      Rectangle2D localRectangle2D = ((Font)localObject1).getStringBounds("PURCHASE ORDER", (FontRenderContext)localObject2);
      int j = (int)localRectangle2D.getWidth();
      int k = (int)localRectangle2D.getHeight();
      n = (this._mTotalPageWidth - j) / 2;
      localGraphics2D.drawString("PURCHASE ORDER", n, k);
      localGraphics2D.drawLine(n, k + 2, n + j, k + 2);
      Font localFont2 = new Font("Courier New", 0, 10);
      localGraphics2D.setFont(localFont2);
      localObject3 = new String[] { "P. O. No.", "Date", "Expected Date" };
      localObject4 = new SimpleDateFormat(CommonConfig.getInstance().dateFormat);
      localObject5 = "NA";
      if (this._mPO.getExpectedDate() != null) {
        localObject5 = ((SimpleDateFormat)localObject4).format(this._mPO.getExpectedDate());
      }
      localObject6 = new String[] { this._mPO.getPoIndex() + "", ((SimpleDateFormat)localObject4).format(this._mPO.getPoDate()), localObject5 };
      printRightAlignedHeaderAndText(localGraphics2D, (String[])localObject3, (String[])localObject6, localFont2, k + 30, this._mTotalPageWidth);
      d1 = k + 30;
      Font localFont3 = new Font(localFont2.getName(), 1, localFont2.getSize());
      localGraphics2D.setFont(localFont3);
      localObject2 = localGraphics2D.getFontRenderContext();
      int i3 = (int)localFont2.getStringBounds("Wy", (FontRenderContext)localObject2).getHeight();
      localGraphics2D.drawString("Vendor : ", 0, d1);
      d1 += i3;
      d1 += 2;
      localGraphics2D.setFont(localFont2);
      i3 = (int)localFont2.getStringBounds("Wy", (FontRenderContext)localObject2).getHeight();
      if (this._mPOVendor != null)
      {
        localGraphics2D.drawString(this._mPOVendor.getVendorName(), 10, d1);
        d1 += i3;
        d1 += 2;
        localObject7 = new StringTokenizer(this._mPOVendor.getVendorAddress());
        while (((StringTokenizer)localObject7).hasMoreTokens())
        {
          localGraphics2D.drawString(((StringTokenizer)localObject7).nextToken(), 10, d1);
          d1 += i3;
          d1 += 2;
        }
      }
      Object localObject7 = this._mPO.getBillTo();
      String str = this._mPO.getShipTo();
      localObject2 = localGraphics2D.getFontRenderContext();
      double d2 = 0.0D;
      StringTokenizer localStringTokenizer1 = new StringTokenizer(str, "\n");
      while (localStringTokenizer1.hasMoreTokens())
      {
        d3 = localFont2.getStringBounds(localStringTokenizer1.nextToken(), (FontRenderContext)localObject2).getWidth();
        if (d3 > d2) {
          d2 = d3;
        }
      }
      d1 += 5;
      double d3 = d1;
      double d4 = localFont2.getStringBounds("Wy", (FontRenderContext)localObject2).getHeight();
      d4 += 2.0D;
      localGraphics2D.setFont(localFont3);
      localGraphics2D.drawString("Bill To : ", 0, d1);
      d1 = (int)(d1 + localFont3.getStringBounds("Wy", (FontRenderContext)localObject2).getHeight());
      d1 += 2;
      StringTokenizer localStringTokenizer2 = new StringTokenizer((String)localObject7, "\n");
      localGraphics2D.setFont(localFont2);
      while (localStringTokenizer2.hasMoreTokens())
      {
        localGraphics2D.drawString(localStringTokenizer2.nextToken(), 10, d1);
        d1 = (int)(d1 + d4);
      }
      double d5 = d1;
      d1 = d3;
      int i4 = (int)(this._mTotalPageWidth - (d2 + 10.0D));
      localGraphics2D.setFont(localFont3);
      localGraphics2D.drawString("Ship To : ", i4, d1);
      d1 = (int)(d1 + localFont3.getStringBounds("Wy", (FontRenderContext)localObject2).getHeight());
      d1 += 2;
      localStringTokenizer1 = new StringTokenizer(str, "\n");
      localGraphics2D.setFont(localFont2);
      while (localStringTokenizer1.hasMoreTokens())
      {
        localGraphics2D.drawString(localStringTokenizer1.nextToken(), i4 + 10, d1);
        d1 = (int)(d1 + d4);
      }
      if (d1 < d5) {
        d1 = d5;
      }
      localGraphics2D.setFont(localFont2);
    }
    else
    {
      d1 += 4;
    }
    Object localObject1 = null;
    Object localObject2 = { 40, 120, 160, 80, 80 };
    int i = 0;
    if (this._mPrintColumnHeader)
    {
      localFont1 = new Font("Courier New", 1, 10);
      localGraphics2D.setFont(localFont1);
      String[] arrayOfString = { "Sl.", "Product Code", "Product Name", "Quantity", "Price/Unit" };
      localObject1 = localGraphics2D.getFontRenderContext();
      i = (int)localFont1.getStringBounds("W", (FontRenderContext)localObject1).getHeight();
      printTableHeaders(localGraphics2D, 0, d1, arrayOfString, (int[])localObject2, i, this._mPrintColumnLines, this._mPrintRowLines);
      d1 += i + 6;
    }
    Font localFont1 = new Font("Courier New", 0, 10);
    localGraphics2D.setFont(localFont1);
    localObject1 = localGraphics2D.getFontRenderContext();
    i = (int)localFont1.getStringBounds("W", (FontRenderContext)localObject1).getHeight();
    int m = 0;
    int n = this._mPO.getEntries().size();
    int i1 = (int)paramPageFormat.getImageableHeight() - this._mBottomMargin;
    while ((m == 0) && (this._mPOEntryIndex < n))
    {
      if (d1 + i + 4 >= i1) {
        return 0;
      }
      localObject3 = (PoEntryRow)this._mPO.getEntries().get(this._mPOEntryIndex);
      this._mPOEntryIndex += 1;
      localObject4 = new InternalQuantity(((PoEntryRow)localObject3).getQuantity(), ((PoEntryRow)localObject3).getProduct().getProdUnit(), true);
      localObject5 = new String[] { this._mPOEntryIndex + "", ((PoEntryRow)localObject3).getProduct().getProductCode(), ((PoEntryRow)localObject3).getProduct().getProdName(), ((InternalQuantity)localObject4).toString(), InternalAmount.toString(((PoEntryRow)localObject3).getPriceExpected()) };
      localObject6 = new byte[] { 1, 0, 0, 1, 1 };
      int i2 = getTableHorizontalLineTotalWidth((int[])localObject2);
      printTableRowInternal(localGraphics2D, (String[])localObject5, (int[])localObject2, (byte[])localObject6, 0, d1, this._mPrintColumnLines, this._mPrintRowLines, i2, i);
      d1 += i + 5;
    }
    this._mAllDataPrinted = true;
    return 0;
  }
  
  private int printBillInternal(Graphics paramGraphics, PageFormat paramPageFormat, int paramInt)
  {
    Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
    localGraphics2D.translate(paramPageFormat.getImageableX() + this._mBillLeftMargin, paramPageFormat.getImageableY() + this._mBillTopMargin);
    if (this.totalPrinted) {
      return 1;
    }
    if (this.fn == null)
    {
      this.fnb = new Font(paramGraphics.getFont().getName(), 1, 7);
      this.fn = new Font(paramGraphics.getFont().getName(), 0, 7);
      this.fixedFont = new Font("Courier New", 0, 8);
    }
    if (this._mCurrentPageIndex == paramInt)
    {
      if (this._mTablePrintCompleted)
      {
        printTotal(this._mBillTable.getFinalAmount(), 0, this._mTablePrint.getWidth(), this.fnb, this.fn, localGraphics2D);
        this.totalPrinted = true;
        return 0;
      }
      if (paramInt != 0)
      {
        try
        {
          this._mTablePrintCompleted = this._mTablePrint.printNextPage(paramGraphics, 0, this._mBillPrintRowLines, this._mBillPrintColumnLines, this._mBillPrintColumnHeader);
          if (this._mTablePrintCompleted)
          {
            int i = 40;
            k = this._mTablePrint.getCurrentYPos();
            int m = (int)paramPageFormat.getImageableHeight() - k;
            if (m < i) {
              return 0;
            }
            printTotal(this._mBillTable.getFinalAmount(), k + 2, this._mTablePrint.getWidth(), this.fnb, this.fn, localGraphics2D);
            this.totalPrinted = true;
          }
        }
        catch (JeException localJeException1)
        {
          localJeException1.printStackTrace();
        }
        return 0;
      }
      int j = this._mBillHeaderGap;
      int k = (int)paramPageFormat.getImageableHeight() - (this._mBillTopMargin + this._mBillBottomMargin);
      TablePrint localTablePrint = new TablePrint(10, 10, TablePrint.PRINT_LATER, j, k, "Invoice No. : " + Integer.toString(this._mBillTable.getBillNo()));
      this._mTablePrint = localTablePrint;
      Iterator localIterator = this._mPrintColList.iterator();
      while (localIterator.hasNext())
      {
        localObject1 = (PrintColumn)localIterator.next();
        localTablePrint.addColumn(new TablePrintColumn(((PrintColumn)localObject1).toString(), this.fnb, null, ((PrintColumn)localObject1).width, ((PrintColumn)localObject1).alignment, 15));
      }
      Object localObject1 = null;
      localObject1 = this._mBillTable.getBillEntries().iterator();
      int n = 1;
      SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(CommonConfig.getInstance().dateFormat);
      int i1 = 1;
      Object localObject4;
      while (((Iterator)localObject1).hasNext())
      {
        BillProduct localBillProduct = (BillProduct)((Iterator)localObject1).next();
        localObject2 = new TablePrintRow(15);
        localTablePrint.addRow((TablePrintRow)localObject2);
        localIterator = this._mPrintColList.iterator();
        while (localIterator.hasNext())
        {
          localObject3 = (PrintColumn)localIterator.next();
          localObject4 = getTablePrintCell(i1, (PrintColumn)localObject3, localBillProduct);
          ((TablePrintRow)localObject2).addCell((TablePrintCell)localObject4);
        }
        i1++;
        n++;
      }
      int i2 = localTablePrint.getWidth();
      Object localObject2 = localGraphics2D.getFontRenderContext();
      localGraphics2D.setFont(this.fn);
      Object localObject3 = ": " + Integer.toString(this._mBillTable.getBillNo());
      if (this._mBillPrintBarCode)
      {
        try
        {
          localObject4 = BarcodeFactory.createCode128(getNumberInDigits(this._mBillTable.getBillNo(), 12));
          ((Barcode)localObject4).setFont(this.fn);
          ((Barcode)localObject4).setBarWidth(0.25D);
          ((Barcode)localObject4).draw(localGraphics2D, 0, j);
        }
        catch (Exception localException)
        {
          localException.printStackTrace();
        }
        j += 10;
      }
      int i3 = (int)this.fnb.getStringBounds("Invoice No. ", (FontRenderContext)localObject2).getWidth();
      int i4 = (int)this.fnb.getStringBounds("Invoice No. ", (FontRenderContext)localObject2).getHeight();
      int i5 = (int)this.fnb.getStringBounds("Invoice Date ", (FontRenderContext)localObject2).getHeight();
      int i6 = (int)this.fnb.getStringBounds("Invoice Date ", (FontRenderContext)localObject2).getWidth();
      int i7 = i6 > i3 ? i6 : i3;
      String str = ": " + localSimpleDateFormat.format(this._mBillTable.getBillDate());
      int i8 = (int)this.fnb.getStringBounds((String)localObject3, (FontRenderContext)localObject2).getWidth();
      int i9 = (int)this.fnb.getStringBounds(str, (FontRenderContext)localObject2).getWidth();
      int i10 = i8 > i9 ? i8 : i9;
      localGraphics2D.setFont(this.fnb);
      localGraphics2D.drawString("Invoice No. ", i2 - (i7 + i10), j + 10);
      localGraphics2D.setFont(this.fn);
      localGraphics2D.drawString((String)localObject3, i2 - i10, j + 10);
      j += i4 + 5;
      localGraphics2D.setFont(this.fnb);
      localGraphics2D.drawString("Invoice Date ", i2 - (i7 + i10), j + 10);
      localGraphics2D.setFont(this.fn);
      localGraphics2D.drawString(str, i2 - i10, j + 10);
      j += i5 + 15;
      try
      {
        this._mTablePrintCompleted = localTablePrint.printNextPage(paramGraphics, j, this._mBillPrintRowLines, this._mBillPrintColumnLines, this._mBillPrintColumnHeader);
      }
      catch (JeException localJeException2)
      {
        localJeException2.printStackTrace();
      }
      if (this._mTablePrintCompleted)
      {
        int i11 = 40;
        int i12 = localTablePrint.getCurrentYPos();
        int i13 = (int)paramPageFormat.getImageableHeight() - i12;
        if (i13 < i11) {
          return 0;
        }
        printTotal(this._mBillTable.getFinalAmount(), i12 + 2, this._mTablePrint.getWidth(), this.fnb, this.fn, localGraphics2D);
        this.totalPrinted = true;
      }
      return 0;
    }
    this._mCurrentPageIndex = paramInt;
    return 0;
  }
  
  void printTotal(double paramDouble, int paramInt1, int paramInt2, Font paramFont1, Font paramFont2, Graphics2D paramGraphics2D)
  {
    FontRenderContext localFontRenderContext = paramGraphics2D.getFontRenderContext();
    int i = (int)this.fnb.getStringBounds("Total :  ", localFontRenderContext).getWidth();
    InternalAmount localInternalAmount = new InternalAmount(paramDouble);
    String str = localInternalAmount.toString() + " " + CommonConfig.getInstance().country.currency;
    int j = (int)this.fn.getStringBounds(str, localFontRenderContext).getWidth();
    paramGraphics2D.setFont(paramFont1);
    paramGraphics2D.drawString("Total :  ", paramInt2 - (i + j) - 5, paramInt1 + 12);
    paramGraphics2D.setFont(paramFont2);
    paramGraphics2D.drawString(str, paramInt2 - j - 5, paramInt1 + 12);
  }
  
  private TablePrintCell getTablePrintCell(int paramInt, PrintColumn paramPrintColumn, BillProduct paramBillProduct)
  {
    Object localObject;
    if (paramPrintColumn.getCode().equalsIgnoreCase("SL"))
    {
      localObject = new TablePrintCell(Integer.toString(paramInt), paramPrintColumn.alignment, this.fixedFont, paramPrintColumn.width);
      return localObject;
    }
    if (paramPrintColumn.getCode().equalsIgnoreCase("PC"))
    {
      localObject = new TablePrintCell(Integer.toString(paramBillProduct.getStockAndProduct().getProdIndex()), paramPrintColumn.alignment, this.fixedFont, paramPrintColumn.width);
      return localObject;
    }
    if (paramPrintColumn.getCode().equalsIgnoreCase("PN"))
    {
      localObject = new TablePrintCell(paramBillProduct.getStockAndProduct().getProdName(), paramPrintColumn.alignment, this.fixedFont, paramPrintColumn.width);
      return localObject;
    }
    if (paramPrintColumn.getCode().equalsIgnoreCase("EX"))
    {
      localObject = new TablePrintCell(paramBillProduct.getStockAndProduct().toString(), paramPrintColumn.alignment, this.fixedFont, paramPrintColumn.width);
      return localObject;
    }
    TablePrintCell localTablePrintCell;
    if (paramPrintColumn.getCode().equalsIgnoreCase("UP"))
    {
      localObject = new InternalAmount(paramBillProduct.getStockAndProduct().getUnitPrice());
      localTablePrintCell = new TablePrintCell(((InternalAmount)localObject).toString(), paramPrintColumn.alignment, this.fixedFont, paramPrintColumn.width);
      return localTablePrintCell;
    }
    if (paramPrintColumn.getCode().equalsIgnoreCase("QT"))
    {
      localObject = new InternalQuantity(paramBillProduct.getQuantity(), paramBillProduct.getStockAndProduct().getProdUnit(), false);
      localTablePrintCell = new TablePrintCell(((InternalQuantity)localObject).toString(), paramPrintColumn.alignment, this.fixedFont, paramPrintColumn.width);
      return localTablePrintCell;
    }
    if (paramPrintColumn.getCode().equalsIgnoreCase("AM"))
    {
      localObject = new InternalAmount(paramBillProduct.getTotalAmount());
      localTablePrintCell = new TablePrintCell(((InternalAmount)localObject).toString(), paramPrintColumn.alignment, this.fixedFont, paramPrintColumn.width);
      return localTablePrintCell;
    }
    if (paramPrintColumn.getCode().equalsIgnoreCase("DC"))
    {
      localObject = new TablePrintCell(Double.toString(paramBillProduct.getDiscount()), paramPrintColumn.alignment, this.fixedFont, paramPrintColumn.width);
      return localObject;
    }
    return null;
  }
  
  public String getColumnConfigString()
  {
    String str = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.LASER.BILL.COLUMN_STRING.VALUE");
    return str;
  }
  
  public void setPrintColumnList(ArrayList<PrintColumn> paramArrayList)
  {
    this._mPrintColList = paramArrayList;
  }
  
  public void printRightAlignedHeaderAndText(Graphics2D paramGraphics2D, String[] paramArrayOfString1, String[] paramArrayOfString2, Font paramFont, int paramInt1, int paramInt2)
  {
    FontRenderContext localFontRenderContext = paramGraphics2D.getFontRenderContext();
    double d1 = 0.0D;
    for (int i = 0; i < paramArrayOfString2.length; i++)
    {
      double d2 = paramFont.getStringBounds(paramArrayOfString2[i], localFontRenderContext).getWidth();
      if (d2 > d1) {
        d1 = d2;
      }
    }
    i = (int)d1;
    int j = (int)paramFont.getStringBounds(paramArrayOfString1[0], localFontRenderContext).getHeight();
    for (int k = 0; k < paramArrayOfString2.length; k++)
    {
      int m = (int)paramFont.getStringBounds(paramArrayOfString1[k] + " : ", localFontRenderContext).getWidth();
      int n = paramInt2 - (m + i);
      paramGraphics2D.drawString(paramArrayOfString1[k] + " : ", n, paramInt1);
      n += m;
      paramGraphics2D.drawString(paramArrayOfString2[k], n, paramInt1);
      paramInt1 += j + 2;
    }
  }
  
  private void printTableHeaders(Graphics2D paramGraphics2D, int paramInt1, int paramInt2, String[] paramArrayOfString, int[] paramArrayOfInt, int paramInt3, boolean paramBoolean1, boolean paramBoolean2)
  {
    Font localFont = paramGraphics2D.getFont();
    FontRenderContext localFontRenderContext = paramGraphics2D.getFontRenderContext();
    int i = getTableHorizontalLineTotalWidth(paramArrayOfInt);
    if (paramBoolean2) {
      paramGraphics2D.drawLine(paramInt1, paramInt2, paramInt1 + i, paramInt2);
    }
    paramInt2 += 1;
    int j = paramInt2;
    paramInt2 += 2;
    paramInt2 += paramInt3;
    int k = paramInt1;
    if (paramBoolean1) {
      paramGraphics2D.drawLine(paramInt1, j, paramInt1, paramInt2 + 2);
    }
    paramInt1++;
    for (int m = 0; m < paramArrayOfString.length; m++)
    {
      paramInt1 += 2;
      int n = (int)localFont.getStringBounds(paramArrayOfString[m], localFontRenderContext).getWidth();
      int i1 = paramArrayOfInt[m] - n;
      i1 /= 2;
      paramGraphics2D.drawString(paramArrayOfString[m], paramInt1 + i1, paramInt2);
      paramInt1 += paramArrayOfInt[m];
      paramInt1 += 2;
      paramInt1++;
      if (paramBoolean1) {
        paramGraphics2D.drawLine(paramInt1, j, paramInt1, paramInt2 + 3);
      }
    }
    paramInt2 += 3;
    if (paramBoolean2) {
      paramGraphics2D.drawLine(k, paramInt2, k + i, paramInt2);
    }
  }
  
  private void printTableRowInternal(Graphics2D paramGraphics2D, String[] paramArrayOfString, int[] paramArrayOfInt, byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2, int paramInt3, int paramInt4)
  {
    if (paramBoolean1) {
      paramGraphics2D.drawLine(paramInt1, paramInt2, paramInt1, paramInt2 + 5 + paramInt4);
    }
    int i = paramInt1;
    paramInt1++;
    paramInt2++;
    int j = paramInt2;
    paramInt2 += 2;
    paramInt2 += paramInt4;
    for (int k = 0; k < paramArrayOfString.length; k++)
    {
      paramInt1 += 2;
      if (paramArrayOfByte[k] == 1) {
        drawRightAligned(paramGraphics2D, paramArrayOfString[k], paramArrayOfInt[k] - 1, paramInt1, paramInt2);
      } else {
        paramGraphics2D.drawString(paramArrayOfString[k], paramInt1, paramInt2);
      }
      paramInt1 += paramArrayOfInt[k];
      paramInt1 += 2;
      paramInt1++;
      if (paramBoolean1) {
        paramGraphics2D.drawLine(paramInt1, j, paramInt1, paramInt2 + 3);
      }
    }
    if (paramBoolean2) {
      paramGraphics2D.drawLine(i, paramInt2 + 3, i + paramInt3, paramInt2 + 3);
    }
  }
  
  private void drawRightAligned(Graphics2D paramGraphics2D, String paramString, int paramInt1, int paramInt2, int paramInt3)
  {
    Font localFont = paramGraphics2D.getFont();
    FontRenderContext localFontRenderContext = paramGraphics2D.getFontRenderContext();
    int i = (int)localFont.getStringBounds(paramString, localFontRenderContext).getWidth();
    paramInt2 += paramInt1 - i;
    paramGraphics2D.drawString(paramString, paramInt2, paramInt3);
  }
  
  private int getTableHorizontalLineTotalWidth(int[] paramArrayOfInt)
  {
    int i = 1;
    for (int j = 0; j < paramArrayOfInt.length; j++)
    {
      i++;
      i += paramArrayOfInt[j];
      i += 4;
    }
    return i;
  }
  
  private String getNumberInDigits(long paramLong, int paramInt)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    while (paramLong > 0L)
    {
      long l = paramLong % 10L;
      localStringBuffer.insert(0, (int)l);
      System.out.println("IVALUE NOW IS " + localStringBuffer);
      paramLong /= 10L;
    }
    int i = localStringBuffer.length();
    for (int j = i; j < paramInt; j++)
    {
      System.out.println("VALUE NOW IS " + localStringBuffer);
      localStringBuffer.insert(0, '0');
    }
    return localStringBuffer.toString();
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.printing.laser.LaserPrint
 * JD-Core Version:    0.7.0.1
 */