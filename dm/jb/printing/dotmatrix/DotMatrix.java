package dm.jb.printing.dotmatrix;

import dm.jb.db.objects.BillTableRow;
import dm.jb.db.objects.CompanyInfoRow;
import dm.jb.db.objects.CompanyInfoTableDef;
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
import dm.tools.types.InternalDate;
import dm.tools.types.InternalQuantity;
import dm.tools.ui.UICommon;
import dm.tools.utils.CommonConfig;
import dm.tools.utils.Config;
import dm.tools.utils.TextPrinter;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;

public class DotMatrix
  implements JBPrinter
{
  public static int totalColWidth = -1;
  public static boolean printColumnLine = false;
  public static boolean printColumnHeader = false;
  public static boolean printColumnHeaderEachPage = false;
  public static String headerMessage = null;
  public static int maxRows = -1;
  public static int pageEndGap = 0;
  public static boolean multiPageEnabled = false;
  int headerGap = -1;
  private String _mBillPrintCommand = null;
  private String _mPrintFileName = "";
  private int _mTotalColumns = 90;
  
  public DotMatrix()
  {
    String str1 = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.LINES.COLUMNS");
    printColumnLine = (str1 != null) && (str1.equals("TRUE"));
    str1 = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.DOT_MATRIX.GENERAL.PRINT_COLUMN_HEADER");
    printColumnHeader = (str1 != null) && (str1.equals("TRUE"));
    str1 = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.DOT_MATRIX.MULTI_PAGE.PRINT_COLUMN_HEADER_EACH");
    printColumnHeaderEachPage = (str1 != null) && (str1.equals("TRUE"));
    str1 = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.DOT_MATRIX.MULTI_PAGE.ENABLED");
    multiPageEnabled = (str1 != null) && (str1.equals("TRUE"));
    str1 = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.DOT_MATRIX.MULTI_PAGE.MAX_ROWS");
    if ((str1 == null) || (str1.length() == 0)) {
      maxRows = -1;
    } else {
      maxRows = Integer.valueOf(str1).intValue();
    }
    str1 = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.DOT_MATRIX.MULTI_PAGE.PAGE_END_GAP");
    if ((str1 == null) || (str1.length() == 0)) {
      pageEndGap = -1;
    } else {
      pageEndGap = Integer.valueOf(str1).intValue();
    }
    if (headerMessage == null)
    {
      str2 = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.DOT_MATRIX.GENERAL.HEADER_FILE");
      if ((str2 == null) || (str2.length() == 0))
      {
        headerMessage = "";
        return;
      }
      try
      {
        FileInputStream localFileInputStream = new FileInputStream(str2);
        localObject = new BufferedReader(new InputStreamReader(new DataInputStream(localFileInputStream)));
        str1 = ((BufferedReader)localObject).readLine();
        StringBuffer localStringBuffer = new StringBuffer();
        while (str1 != null)
        {
          localStringBuffer.append(str1 + "\n");
          str1 = ((BufferedReader)localObject).readLine();
        }
        headerMessage = localStringBuffer.toString();
        ((BufferedReader)localObject).close();
        localFileInputStream.close();
      }
      catch (FileNotFoundException localFileNotFoundException)
      {
        System.err.println("Header file not found." + localFileNotFoundException);
      }
      catch (Exception localException)
      {
        System.err.println("Exception reading header file." + localException);
      }
      str1 = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.DOT_MATRIX.GENERAL.HEADER_GAP");
      if ((str1 != null) && (str1.length() > 0)) {
        this.headerGap = Integer.valueOf(str1).intValue();
      } else {
        this.headerGap = 0;
      }
    }
    if (!multiPageEnabled) {
      maxRows = -1;
    }
    String str2 = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.DOT_MATRIX.GENERAL.PRINT_COMMAND");
    if ((str2 == null) || (str2.length() == 0)) {
      return;
    }
    String str3 = Config.INSTANCE.getAttrib("JB_CONFIG.OTHERS.TEMP_FOLDER");
    if ((str3 == null) || (str3.length() == 0)) {
      if (File.separator.equals("\\")) {
        str3 = "C:\\Temp";
      } else {
        str3 = "/tmp";
      }
    }
    str3 = str3 + File.separator + "PRINT.txt";
    this._mPrintFileName = str3;
    Object localObject = "";
    for (int i = str2.indexOf("#PRINT_FILE"); i != -1; i = str2.indexOf("#PRINT_FILE"))
    {
      String str4 = str2.substring(0, i);
      localObject = (String)localObject + str4;
      localObject = (String)localObject + str3;
      str2 = str2.substring(i + "#PRINT_FILE".length());
    }
    this._mBillPrintCommand = ((String)localObject);
  }
  
  public void printBill(Bill paramBill)
    throws JePrinterException
  {
    Iterator localIterator1 = null;
    localIterator1 = paramBill.getBillEntries().iterator();
    String str1 = this._mPrintFileName;
    PrintWriter localPrintWriter = null;
    try
    {
      localPrintWriter = new PrintWriter(new FileWriter(str1));
    }
    catch (IOException localIOException)
    {
      System.err.println("ERROR: IO-Exception while DOT-MATRIX printing " + localIOException.getMessage());
      throw new JePrinterException(this, "IO error while opening print device.", localIOException.toString());
    }
    ArrayList localArrayList = BillPrintCommon.getInstance().getPrintColumns();
    Iterator localIterator2 = localArrayList.iterator();
    PrintColumn[] arrayOfPrintColumn = new PrintColumn[localArrayList.size()];
    for (int i = 0; localIterator2.hasNext(); i++) {
      arrayOfPrintColumn[i] = ((PrintColumn)localIterator2.next());
    }
    if ((headerMessage != null) && (headerMessage.length() > 0)) {
      localPrintWriter.print(headerMessage);
    }
    for (int j = 0; j < this.headerGap; j++) {
      localPrintWriter.println();
    }
    if (totalColWidth == -1)
    {
      totalColWidth = 0;
      for (j = 0; j < arrayOfPrintColumn.length; j++) {
        totalColWidth += arrayOfPrintColumn[j].width + 2;
      }
      totalColWidth += arrayOfPrintColumn.length;
    }
    localPrintWriter.println();
    String str2 = new InternalDate(paramBill.getBillDate(), "", "", UICommon.getDateFormat()).toString();
    str2 = "Date : " + str2;
    str2 = getAlignedString(str2, totalColWidth, 4);
    localPrintWriter.println(str2);
    String str3 = Integer.toString(paramBill.getBillNo());
    str3 = "Bill No : " + str3;
    str3 = getAlignedString(str3, totalColWidth, 4);
    localPrintWriter.println(str3);
    if (printColumnHeader) {
      printColumnHeaders(localPrintWriter, arrayOfPrintColumn);
    }
    boolean bool = printNextSet(maxRows, localPrintWriter, localIterator1, arrayOfPrintColumn);
    int k;
    if (printColumnHeader)
    {
      for (k = 0; k <= totalColWidth; k++) {
        localPrintWriter.print("-");
      }
      localPrintWriter.println();
    }
    while (bool)
    {
      for (k = 0; k < pageEndGap; k++) {
        localPrintWriter.println();
      }
      localPrintWriter.println();
      if (printColumnHeaderEachPage) {
        printColumnHeaders(localPrintWriter, arrayOfPrintColumn);
      }
      bool = printNextSet(maxRows, localPrintWriter, localIterator1, arrayOfPrintColumn);
      if (printColumnHeader)
      {
        for (k = 0; k <= totalColWidth; k++) {
          localPrintWriter.print("-");
        }
        localPrintWriter.println();
      }
    }
    localPrintWriter.println();
    double d1 = paramBill.getFinalAmount();
    double d2 = paramBill.getFinalDiscount();
    InternalAmount localInternalAmount = new InternalAmount(d1 + d2, "", "", true);
    String str4 = getAlignedString(localInternalAmount.toString(), 11, 4);
    localPrintWriter.println(getAlignedString("Total Amount     : " + str4, totalColWidth, 4));
    localInternalAmount = new InternalAmount(d2, "", "", true);
    str4 = getAlignedString(localInternalAmount.toString(), 11, 4);
    localPrintWriter.println(getAlignedString("Addl Discount    : " + str4, totalColWidth, 4));
    localInternalAmount = new InternalAmount(d1, "", "", true);
    str4 = getAlignedString(localInternalAmount.toString(), 11, 4);
    localPrintWriter.println(getAlignedString("Amount to be paid : " + str4, totalColWidth, 4));
    localPrintWriter.close();
    executePrintCommand();
  }
  
  private void printColumnHeaders(PrintWriter paramPrintWriter, PrintColumn[] paramArrayOfPrintColumn)
  {
    paramPrintWriter.println();
    for (int i = 0; i <= totalColWidth; i++) {
      paramPrintWriter.print("-");
    }
    paramPrintWriter.println();
    paramPrintWriter.print("| ");
    for (i = 0; i < paramArrayOfPrintColumn.length; i++)
    {
      String str = getAlignedString(paramArrayOfPrintColumn[i].name, paramArrayOfPrintColumn[i].width, 0);
      if (i == paramArrayOfPrintColumn.length - 1) {
        paramPrintWriter.print(str + " |");
      } else {
        paramPrintWriter.print(str + " | ");
      }
    }
    paramPrintWriter.println();
    for (i = 0; i <= totalColWidth; i++) {
      paramPrintWriter.print("-");
    }
    paramPrintWriter.println();
  }
  
  private boolean printNextSet(int paramInt, PrintWriter paramPrintWriter, Iterator<BillProduct> paramIterator, PrintColumn[] paramArrayOfPrintColumn)
  {
    BillProduct localBillProduct1 = 0;
    int i = paramInt;
    if (i == -1) {
      i = 65535;
    }
    BillProduct localBillProduct2;
    while ((paramIterator.hasNext()) && (localBillProduct1 < i))
    {
      if (printColumnLine) {
        paramPrintWriter.print("| ");
      } else {
        paramPrintWriter.print("  ");
      }
      localBillProduct2 = (BillProduct)paramIterator.next();
      printRow(paramPrintWriter, paramArrayOfPrintColumn, localBillProduct2);
      localBillProduct1++;
    }
    if ((paramInt != -1) && (localBillProduct1 < paramInt + 1)) {
      for (localBillProduct2 = localBillProduct1; localBillProduct2 < paramInt; localBillProduct2++)
      {
        paramPrintWriter.print("|");
        for (int j = 0; j < paramArrayOfPrintColumn.length; j++)
        {
          String str = getAlignedString("", paramArrayOfPrintColumn[j].width, 4);
          if (localBillProduct2 == paramArrayOfPrintColumn.length - 1) {
            paramPrintWriter.print(str + " |");
          } else {
            paramPrintWriter.print(str + " | ");
          }
        }
        paramPrintWriter.println();
      }
    }
    return paramIterator.hasNext();
  }
  
  public void reset() {}
  
  private void printRow(PrintWriter paramPrintWriter, PrintColumn[] paramArrayOfPrintColumn, BillProduct paramBillProduct)
  {
    for (int i = 0; i < paramArrayOfPrintColumn.length; i++)
    {
      String str1 = getDataFromBillEntry(paramArrayOfPrintColumn[i], paramBillProduct);
      String str2 = getAlignedString(str1, paramArrayOfPrintColumn[i].width, paramArrayOfPrintColumn[i].alignment);
      paramPrintWriter.print(str2);
      if (printColumnLine)
      {
        if (i == paramArrayOfPrintColumn.length - 1) {
          paramPrintWriter.print(" |");
        } else {
          paramPrintWriter.print(" | ");
        }
      }
      else {
        paramPrintWriter.print("   ");
      }
    }
    paramPrintWriter.println("");
  }
  
  private String getDataFromBillEntry(PrintColumn paramPrintColumn, BillProduct paramBillProduct)
  {
    if (paramPrintColumn.getCode().equalsIgnoreCase("SL")) {
      return Integer.toString(paramBillProduct.getIndex());
    }
    if (paramPrintColumn.getCode().equalsIgnoreCase("PC")) {
      return paramBillProduct.getStockAndProduct().getProductCode();
    }
    if (paramPrintColumn.getCode().equalsIgnoreCase("PN")) {
      return paramBillProduct.getStockAndProduct().getProdName();
    }
    if (paramPrintColumn.getCode().equalsIgnoreCase("EX")) {
      return new InternalDate(paramBillProduct.getStockAndProduct().getExpiry(), paramBillProduct.getStockAndProduct().getExpiry() == null, "", "", UICommon.getDateFormat()).toString();
    }
    if (paramPrintColumn.getCode().equalsIgnoreCase("UP")) {
      return InternalAmount.toString(paramBillProduct.getStockAndProduct().getUnitPrice());
    }
    if (paramPrintColumn.getCode().equalsIgnoreCase("QT")) {
      return InternalQuantity.toString(paramBillProduct.getQuantity(), (short)paramBillProduct.getStockAndProduct().getProdUnit());
    }
    if (paramPrintColumn.getCode().equalsIgnoreCase("AM")) {
      return InternalAmount.toString(paramBillProduct.getTotalAmount());
    }
    if (paramPrintColumn.getCode().equalsIgnoreCase("DC")) {
      return Double.toString(paramBillProduct.getDiscount());
    }
    return null;
  }
  
  private String getAlignedString(String paramString, int paramInt1, int paramInt2)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    int i = paramString.length();
    if (i > paramInt1)
    {
      localStringBuffer.append(paramString.substring(0, paramInt1));
      return localStringBuffer.toString();
    }
    int j;
    int k;
    if (paramInt2 == 2)
    {
      localStringBuffer.append(paramString);
      j = paramInt1 - i;
      for (k = 0; k < j; k++) {
        localStringBuffer.append(' ');
      }
    }
    else if (paramInt2 == 4)
    {
      j = paramInt1 - i;
      for (k = 0; k < j; k++) {
        localStringBuffer.append(' ');
      }
      localStringBuffer.append(paramString);
    }
    else
    {
      j = (paramInt1 - i) / 2;
      if (paramInt1 % 2 == 1) {
        localStringBuffer.append(' ');
      }
      for (k = 0; k < j; k++) {
        localStringBuffer.append(' ');
      }
      localStringBuffer.append(paramString);
      for (k = 0; k < j; k++) {
        localStringBuffer.append(' ');
      }
    }
    return localStringBuffer.toString();
  }
  
  public String getColumnConfigString()
  {
    String str = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.DOT_MATRIX.COLUMN_STRING.VALUE");
    return str;
  }
  
  public void setPrintColumnList(ArrayList<PrintColumn> paramArrayList) {}
  
  public void printStockReturn(StockReturnRow paramStockReturnRow, ArrayList<ReturnStock> paramArrayList, VendorRow paramVendorRow)
    throws JePrinterException
  {
    BufferedOutputStream localBufferedOutputStream = null;
    try
    {
      localBufferedOutputStream = new BufferedOutputStream(new FileOutputStream(this._mPrintFileName));
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      throw new JePrinterException(this, "Cannot open temporary file for printing.", localFileNotFoundException.toString());
    }
    try
    {
      String[] arrayOfString1 = { "Transaction No", "Create Date" };
      SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(CommonConfig.getInstance().dateFormat);
      String[] arrayOfString2 = { paramStockReturnRow.getTxnNo() + "", localSimpleDateFormat.format(paramStockReturnRow.getCreateDate()) };
      String[] arrayOfString3 = TextPrinter.convertToMaxRightAligned(arrayOfString1, arrayOfString2);
      StringTokenizer localStringTokenizer = new StringTokenizer(paramVendorRow.getVendorAddress(), "\n");
      int i = 0;
      while (localStringTokenizer.hasMoreTokens())
      {
        i++;
        localStringTokenizer.nextToken();
      }
      localStringTokenizer = new StringTokenizer(paramVendorRow.getVendorAddress(), "\n");
      String[] arrayOfString4 = new String[i + 1];
      i = 1;
      arrayOfString4[0] = paramVendorRow.getVendorName();
      while (localStringTokenizer.hasMoreTokens())
      {
        arrayOfString4[i] = localStringTokenizer.nextToken();
        i++;
      }
      String[] arrayOfString5 = TextPrinter.getMixedAlignment(arrayOfString4, arrayOfString3, this._mTotalColumns);
      for (int j = 0; j < arrayOfString5.length; j++)
      {
        TextPrinter.printAsIs(localBufferedOutputStream, arrayOfString5[j]);
        TextPrinter.nextLine(localBufferedOutputStream);
      }
      TextPrinter.nextLine(localBufferedOutputStream);
      String[] arrayOfString6 = { "Sl.", "Product Code", "Product Name", "Quantity", "Details" };
      int[] arrayOfInt = { 8, 14, 28, 14, 20 };
      TextPrinter.printDashedLine(localBufferedOutputStream, this._mTotalColumns - 2, 2);
      TextPrinter.nextLine(localBufferedOutputStream);
      TextPrinter.printTableHeader(localBufferedOutputStream, arrayOfString6, true, (byte)2, (byte)3, 0, 1, arrayOfInt);
      TextPrinter.nextLine(localBufferedOutputStream);
      TextPrinter.printDashedLine(localBufferedOutputStream, this._mTotalColumns - 2, 2);
      TextPrinter.nextLine(localBufferedOutputStream);
      int k = paramArrayList.size();
      byte[] arrayOfByte = { 2, 1, 1, 2, 2 };
      for (int m = 0; m < k; m++)
      {
        ReturnStock localReturnStock = (ReturnStock)paramArrayList.get(m);
        InternalQuantity localInternalQuantity = new InternalQuantity(localReturnStock.quantity, localReturnStock.productRow.getProdUnit(), true);
        String[] arrayOfString7 = { m + 1 + "", localReturnStock.productRow.getProductCode(), localReturnStock.productRow.getProdName(), localInternalQuantity.toString(), localReturnStock.getDetailsForPrinting() };
        TextPrinter.printTableRow(localBufferedOutputStream, arrayOfString7, true, true, (byte)3, arrayOfByte, arrayOfInt, 1);
        TextPrinter.nextLine(localBufferedOutputStream);
      }
      TextPrinter.printDashedLine(localBufferedOutputStream, this._mTotalColumns - 2, 2);
      TextPrinter.nextLine(localBufferedOutputStream);
    }
    catch (IOException localIOException)
    {
      throw new JePrinterException(this, "Error printing column lines", localIOException.getMessage());
    }
    executePrintCommand();
  }
  
  public void printPO(PoInfoRow paramPoInfoRow, VendorRow paramVendorRow)
    throws JePrinterException
  {
    BufferedOutputStream localBufferedOutputStream = null;
    try
    {
      localBufferedOutputStream = new BufferedOutputStream(new FileOutputStream(this._mPrintFileName));
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      throw new JePrinterException(this, "Cannot open temporary file for printing.", localFileNotFoundException.toString());
    }
    try
    {
      String[] arrayOfString1 = { "P. O. Number", "Date", "Expected Date" };
      SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(CommonConfig.getInstance().dateFormat);
      String str1 = "NA";
      if (paramPoInfoRow.getExpectedDate() != null) {
        str1 = localSimpleDateFormat.format(paramPoInfoRow.getExpectedDate());
      }
      String[] arrayOfString2 = { paramPoInfoRow.getPoIndex() + "", localSimpleDateFormat.format(paramPoInfoRow.getPoDate()), str1 };
      String[] arrayOfString3 = TextPrinter.convertToMaxRightAligned(arrayOfString1, arrayOfString2);
      StringTokenizer localStringTokenizer1 = new StringTokenizer(paramVendorRow.getVendorAddress(), "\n");
      int i = 0;
      while (localStringTokenizer1.hasMoreTokens())
      {
        i++;
        localStringTokenizer1.nextToken();
      }
      localStringTokenizer1 = new StringTokenizer(paramVendorRow.getVendorAddress(), "\n");
      String[] arrayOfString4 = new String[i + 1];
      i = 1;
      arrayOfString4[0] = paramVendorRow.getVendorName();
      while (localStringTokenizer1.hasMoreTokens())
      {
        arrayOfString4[i] = localStringTokenizer1.nextToken();
        i++;
      }
      String[] arrayOfString5 = TextPrinter.getMixedAlignment(arrayOfString4, arrayOfString3, this._mTotalColumns);
      for (int j = 0; j < arrayOfString5.length; j++)
      {
        TextPrinter.printAsIs(localBufferedOutputStream, arrayOfString5[j]);
        TextPrinter.nextLine(localBufferedOutputStream);
      }
      TextPrinter.nextLine(localBufferedOutputStream);
      String str2 = paramPoInfoRow.getBillTo();
      String str3 = paramPoInfoRow.getBillTo();
      StringTokenizer localStringTokenizer2 = new StringTokenizer(str3, "\n");
      int k = 0;
      while (localStringTokenizer2.hasMoreTokens())
      {
        localObject1 = localStringTokenizer2.nextToken();
        if (((String)localObject1).length() > k) {
          k = ((String)localObject1).length();
        }
      }
      Object localObject1 = new ArrayList();
      StringTokenizer localStringTokenizer3 = new StringTokenizer(str2, "\n");
      StringTokenizer localStringTokenizer4 = new StringTokenizer(str3, "\n");
      StringBuffer localStringBuffer = new StringBuffer("Bill To : ");
      int m = this._mTotalColumns - (localStringBuffer.length() + k);
      for (int n = 0; n < m; n++) {
        localStringBuffer.append(" ");
      }
      localStringBuffer.append("Ship To : ");
      ((ArrayList)localObject1).add(localStringBuffer.toString());
      while ((localStringTokenizer3.hasMoreTokens()) || (localStringTokenizer4.hasMoreTokens()))
      {
        localObject2 = new StringBuffer();
        if (localStringTokenizer3.hasMoreTokens()) {
          ((StringBuffer)localObject2).append(localStringTokenizer3.nextToken());
        }
        if (localStringTokenizer4.hasMoreTokens())
        {
          int i1 = ((StringBuffer)localObject2).length();
          int i2 = this._mTotalColumns - (i1 + k);
          for (i3 = 0; i3 < i2; i3++) {
            ((StringBuffer)localObject2).append(" ");
          }
          ((StringBuffer)localObject2).append(localStringTokenizer4.nextToken());
        }
        ((ArrayList)localObject1).add(((StringBuffer)localObject2).toString());
      }
      Object localObject2 = ((ArrayList)localObject1).iterator();
      while (((Iterator)localObject2).hasNext())
      {
        localObject3 = (String)((Iterator)localObject2).next();
        TextPrinter.printAsIs(localBufferedOutputStream, (String)localObject3);
        TextPrinter.nextLine(localBufferedOutputStream);
      }
      TextPrinter.nextLine(localBufferedOutputStream);
      ((ArrayList)localObject1).clear();
      localObject2 = new String[] { "Sl.", "Product Code", "Product Name", "Quantity", "Price/Unit" };
      Object localObject3 = { 8, 16, 30, 18, 12 };
      TextPrinter.printDashedLine(localBufferedOutputStream, this._mTotalColumns - 2, 2);
      TextPrinter.nextLine(localBufferedOutputStream);
      TextPrinter.printTableHeader(localBufferedOutputStream, (String[])localObject2, true, (byte)2, (byte)3, 0, 1, (int[])localObject3);
      TextPrinter.nextLine(localBufferedOutputStream);
      TextPrinter.printDashedLine(localBufferedOutputStream, this._mTotalColumns - 2, 2);
      TextPrinter.nextLine(localBufferedOutputStream);
      ArrayList localArrayList = paramPoInfoRow.getEntries();
      int i3 = localArrayList.size();
      byte[] arrayOfByte = { 2, 1, 1, 2, 2 };
      for (int i4 = 0; i4 < i3; i4++)
      {
        PoEntryRow localPoEntryRow = (PoEntryRow)localArrayList.get(i4);
        InternalQuantity localInternalQuantity = new InternalQuantity(localPoEntryRow.getQuantity(), localPoEntryRow.getProduct().getProdUnit(), true);
        String[] arrayOfString6 = { i4 + 1 + "", localPoEntryRow.getProduct().getProductCode(), localPoEntryRow.getProduct().getProdName(), localInternalQuantity.toString(), InternalAmount.toString(localPoEntryRow.getPriceExpected()) };
        TextPrinter.printTableRow(localBufferedOutputStream, arrayOfString6, true, true, (byte)3, arrayOfByte, (int[])localObject3, 1);
        TextPrinter.nextLine(localBufferedOutputStream);
      }
      TextPrinter.printDashedLine(localBufferedOutputStream, this._mTotalColumns - 2, 2);
      TextPrinter.nextLine(localBufferedOutputStream);
    }
    catch (IOException localIOException)
    {
      throw new JePrinterException(this, "Error printing column lines", localIOException.getMessage());
    }
    executePrintCommand();
  }
  
  public void printWhToStore(WhToStoreRow paramWhToStoreRow, ArrayList<AllotStockPanel.SelectedEntry> paramArrayList, StoreInfoRow paramStoreInfoRow)
    throws JePrinterException
  {
    BufferedOutputStream localBufferedOutputStream = null;
    try
    {
      localBufferedOutputStream = new BufferedOutputStream(new FileOutputStream(this._mPrintFileName));
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      throw new JePrinterException(this, "Cannot open temporary file for printing.", localFileNotFoundException.toString());
    }
    try
    {
      String[] arrayOfString1 = { "Trasaction No : ", "Date" };
      SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(CommonConfig.getInstance().dateFormat);
      String[] arrayOfString2 = { paramWhToStoreRow.getTxnNo() + "", localSimpleDateFormat.format(paramWhToStoreRow.getDate()) };
      String[] arrayOfString3 = TextPrinter.convertToMaxRightAligned(arrayOfString1, arrayOfString2);
      StringTokenizer localStringTokenizer = new StringTokenizer(paramStoreInfoRow.getAddress(), "\n");
      int i = 0;
      while (localStringTokenizer.hasMoreTokens())
      {
        i++;
        localStringTokenizer.nextToken();
      }
      localStringTokenizer = new StringTokenizer(paramStoreInfoRow.getAddress(), "\n");
      String[] arrayOfString4 = new String[i + 1];
      i = 1;
      arrayOfString4[0] = paramStoreInfoRow.getName();
      while (localStringTokenizer.hasMoreTokens())
      {
        arrayOfString4[i] = localStringTokenizer.nextToken();
        i++;
      }
      String[] arrayOfString5 = TextPrinter.getMixedAlignment(arrayOfString4, arrayOfString3, this._mTotalColumns);
      for (int j = 0; j < arrayOfString5.length; j++)
      {
        TextPrinter.printAsIs(localBufferedOutputStream, arrayOfString5[j]);
        TextPrinter.nextLine(localBufferedOutputStream);
      }
      TextPrinter.nextLine(localBufferedOutputStream);
      String[] arrayOfString6 = { "Sl.", "Product Code", "Product Name", "Quantity", "Warehouse" };
      int[] arrayOfInt = { 8, 16, 30, 18, 12 };
      TextPrinter.printDashedLine(localBufferedOutputStream, this._mTotalColumns - 2, 2);
      TextPrinter.nextLine(localBufferedOutputStream);
      TextPrinter.printTableHeader(localBufferedOutputStream, arrayOfString6, true, (byte)2, (byte)3, 0, 1, arrayOfInt);
      TextPrinter.nextLine(localBufferedOutputStream);
      TextPrinter.printDashedLine(localBufferedOutputStream, this._mTotalColumns - 2, 2);
      TextPrinter.nextLine(localBufferedOutputStream);
      int k = paramArrayList.size();
      byte[] arrayOfByte = { 2, 1, 1, 2, 1 };
      for (int m = 0; m < k; m++)
      {
        AllotStockPanel.SelectedEntry localSelectedEntry = (AllotStockPanel.SelectedEntry)paramArrayList.get(m);
        InternalQuantity localInternalQuantity = new InternalQuantity(localSelectedEntry._mQuantity.doubleValue(), localSelectedEntry._mProduct.getProdUnit(), true);
        WearehouseInfoRow localWearehouseInfoRow = null;
        try
        {
          localWearehouseInfoRow = WearehouseInfoTableDef.getInstance().getWarehouseForIndex(localSelectedEntry._mStockRow.currentStockRow.getWearHouseIndex());
        }
        catch (DBException localDBException)
        {
          localDBException.printStackTrace();
        }
        String[] arrayOfString7 = { m + 1 + "", localSelectedEntry._mProduct.getProductCode(), localSelectedEntry._mProduct.getProdName(), localInternalQuantity.toString(), localWearehouseInfoRow == null ? "NA" : localWearehouseInfoRow.getWearehouseName() };
        TextPrinter.printTableRow(localBufferedOutputStream, arrayOfString7, true, true, (byte)3, arrayOfByte, arrayOfInt, 1);
        TextPrinter.nextLine(localBufferedOutputStream);
      }
      TextPrinter.printDashedLine(localBufferedOutputStream, this._mTotalColumns - 2, 2);
      TextPrinter.nextLine(localBufferedOutputStream);
    }
    catch (IOException localIOException)
    {
      throw new JePrinterException(this, "Error printing column lines", localIOException.getMessage());
    }
    executePrintCommand();
  }
  
  public void printWhToStoreView(ProductRow[] paramArrayOfProductRow, double[] paramArrayOfDouble, Date paramDate, int paramInt, boolean[] paramArrayOfBoolean)
    throws JePrinterException
  {
    BufferedOutputStream localBufferedOutputStream = null;
    try
    {
      localBufferedOutputStream = new BufferedOutputStream(new FileOutputStream(this._mPrintFileName));
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      throw new JePrinterException(this, "Cannot open temporary file for printing.", localFileNotFoundException.toString());
    }
    try
    {
      String[] arrayOfString1 = { "Trasaction No : ", "Date" };
      SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(CommonConfig.getInstance().dateFormat);
      String[] arrayOfString2 = { paramInt + "", localSimpleDateFormat.format(paramDate) };
      String[] arrayOfString3 = TextPrinter.convertToMaxRightAligned(arrayOfString1, arrayOfString2);
      StoreInfoRow localStoreInfoRow = StoreInfoTableDef.getCurrentStore();
      StringTokenizer localStringTokenizer = new StringTokenizer(localStoreInfoRow.getAddress(), "\n");
      int i = 0;
      while (localStringTokenizer.hasMoreTokens())
      {
        i++;
        localStringTokenizer.nextToken();
      }
      localStringTokenizer = new StringTokenizer(localStoreInfoRow.getAddress(), "\n");
      String[] arrayOfString4 = new String[i + 1];
      i = 1;
      arrayOfString4[0] = localStoreInfoRow.getName();
      while (localStringTokenizer.hasMoreTokens())
      {
        arrayOfString4[i] = localStringTokenizer.nextToken();
        i++;
      }
      String[] arrayOfString5 = TextPrinter.getMixedAlignment(arrayOfString4, arrayOfString3, this._mTotalColumns);
      for (int j = 0; j < arrayOfString5.length; j++)
      {
        TextPrinter.printAsIs(localBufferedOutputStream, arrayOfString5[j]);
        TextPrinter.nextLine(localBufferedOutputStream);
      }
      TextPrinter.nextLine(localBufferedOutputStream);
      String[] arrayOfString6 = { "Sl.", "Product Code", "Product Name", "Quantity", "Updated" };
      int[] arrayOfInt = { 8, 16, 30, 18, 12 };
      TextPrinter.printDashedLine(localBufferedOutputStream, this._mTotalColumns - 2, 2);
      TextPrinter.nextLine(localBufferedOutputStream);
      TextPrinter.printTableHeader(localBufferedOutputStream, arrayOfString6, true, (byte)2, (byte)3, 0, 1, arrayOfInt);
      TextPrinter.nextLine(localBufferedOutputStream);
      TextPrinter.printDashedLine(localBufferedOutputStream, this._mTotalColumns - 2, 2);
      TextPrinter.nextLine(localBufferedOutputStream);
      int k = paramArrayOfProductRow.length;
      byte[] arrayOfByte = { 2, 1, 1, 2, 1 };
      for (int m = 0; m < k; m++)
      {
        ProductRow localProductRow = paramArrayOfProductRow[m];
        InternalQuantity localInternalQuantity = new InternalQuantity(paramArrayOfDouble[m], localProductRow.getProdUnit(), true);
        String[] arrayOfString7 = { m + 1 + "", localProductRow.getProductCode(), localProductRow.getProdName(), localInternalQuantity.toString(), paramArrayOfBoolean[m] != 0 ? "YES" : "NO" };
        TextPrinter.printTableRow(localBufferedOutputStream, arrayOfString7, true, true, (byte)3, arrayOfByte, arrayOfInt, 1);
        TextPrinter.nextLine(localBufferedOutputStream);
      }
      TextPrinter.printDashedLine(localBufferedOutputStream, this._mTotalColumns - 2, 2);
      TextPrinter.nextLine(localBufferedOutputStream);
    }
    catch (IOException localIOException)
    {
      throw new JePrinterException(this, "Error printing column lines", localIOException.getMessage());
    }
    executePrintCommand();
  }
  
  public void printPurchaseHistory(double paramDouble, int paramInt, String paramString, Date paramDate1, Date paramDate2, ArrayList<BillTableRow> paramArrayList)
    throws JePrinterException
  {
    BufferedOutputStream localBufferedOutputStream = null;
    try
    {
      localBufferedOutputStream = new BufferedOutputStream(new FileOutputStream(this._mPrintFileName));
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      throw new JePrinterException(this, "Cannot open temporary file for printing.", localFileNotFoundException.toString());
    }
    try
    {
      String str1 = CompanyInfoTableDef.getInstance().getCompany().getName() + "\n" + CompanyInfoTableDef.getInstance().getCompany().getAddress();
      StringTokenizer localStringTokenizer = new StringTokenizer(str1, "\n");
      while (localStringTokenizer.hasMoreTokens())
      {
        localObject1 = TextPrinter.getCenterAlignedText(localStringTokenizer.nextToken(), 101, (byte)3);
        TextPrinter.printAsIs(localBufferedOutputStream, (String)localObject1);
        TextPrinter.nextLine(localBufferedOutputStream);
      }
      Object localObject1 = new StringTokenizer(paramString, "\n");
      while (((StringTokenizer)localObject1).hasMoreTokens())
      {
        localObject2 = ((StringTokenizer)localObject1).nextToken();
        TextPrinter.printAsIs(localBufferedOutputStream, "    " + (String)localObject2);
        TextPrinter.nextLine(localBufferedOutputStream);
      }
      TextPrinter.nextLine(localBufferedOutputStream);
      TextPrinter.printAsIs(localBufferedOutputStream, "Duration : ");
      Object localObject2 = new SimpleDateFormat(CommonConfig.getInstance().dateFormat);
      String str2 = "";
      if (paramDate1 != null) {
        str2 = ((SimpleDateFormat)localObject2).format(paramDate1);
      }
      String str3 = "Current";
      if (paramDate2 != null) {
        str3 = ((SimpleDateFormat)localObject2).format(paramDate2);
      }
      TextPrinter.printAsIs(localBufferedOutputStream, str2 + " To " + str3);
      TextPrinter.nextLine(localBufferedOutputStream);
      TextPrinter.printDashedLine(localBufferedOutputStream, 101, 2);
      TextPrinter.nextLine(localBufferedOutputStream);
      String[] arrayOfString1 = { "Sl. No", "Rcpt. No", "Date", "Amount(" + CommonConfig.getInstance().country.currency + ")", "Pts. Earned", "Pts. Redeemed" };
      int[] arrayOfInt = { 6, 20, 20, 20, 15, 15 };
      TextPrinter.printTableHeader(localBufferedOutputStream, arrayOfString1, true, (byte)3, (byte)3, 40, 0, arrayOfInt);
      TextPrinter.nextLine(localBufferedOutputStream);
      TextPrinter.printDashedLine(localBufferedOutputStream, 101, 2);
      TextPrinter.nextLine(localBufferedOutputStream);
      byte[] arrayOfByte = { 2, 2, 2, 2, 2, 2 };
      int i = 1;
      Object localObject3 = paramArrayList.iterator();
      while (((Iterator)localObject3).hasNext())
      {
        BillTableRow localBillTableRow = (BillTableRow)((Iterator)localObject3).next();
        String str4 = i + "";
        String str5 = localBillTableRow.getBillNo() + "";
        String str6 = ((SimpleDateFormat)localObject2).format(localBillTableRow.getBillDate());
        String str7 = InternalAmount.toString(localBillTableRow.getAmount());
        String str8 = localBillTableRow.getPointsAwarded() + "";
        String str9 = localBillTableRow.getPointsRedeemed() + "";
        String[] arrayOfString2 = { str4, str5, str6, str7, str8, str9 };
        TextPrinter.printTableRow(localBufferedOutputStream, arrayOfString2, true, false, (byte)3, arrayOfByte, arrayOfInt, 0);
        TextPrinter.nextLine(localBufferedOutputStream);
      }
      TextPrinter.printDashedLine(localBufferedOutputStream, 101, 2);
      TextPrinter.nextLine(localBufferedOutputStream);
      localObject3 = "Total Purchase : " + InternalAmount.toString(paramDouble) + " " + CommonConfig.getInstance().country.currency;
      localObject3 = TextPrinter.getRightAlignedText((String)localObject3, 101, (byte)3);
      TextPrinter.printAsIs(localBufferedOutputStream, (String)localObject3);
      TextPrinter.nextLine(localBufferedOutputStream);
      localObject3 = "Points Earned: " + paramInt;
      localObject3 = TextPrinter.getRightAlignedText((String)localObject3, 101, (byte)3);
      TextPrinter.printAsIs(localBufferedOutputStream, (String)localObject3);
      TextPrinter.nextLine(localBufferedOutputStream);
    }
    catch (IOException localIOException)
    {
      throw new JePrinterException(this, "Error printing column lines", localIOException.getMessage());
    }
    catch (DBException localDBException)
    {
      throw new JePrinterException(this, "Error printing column lines", localDBException.getMessage());
    }
    executePrintCommand();
  }
  
  private void executePrintCommand()
    throws JePrinterException
  {
    try
    {
      Runtime.getRuntime().exec(this._mBillPrintCommand);
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
      throw new JePrinterException(this, "Internal error printing to the device", "IOException");
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.printing.dotmatrix.DotMatrix
 * JD-Core Version:    0.7.0.1
 */