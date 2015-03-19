package dm.jb.printing;

import dm.jb.JeException;
import java.util.ArrayList;

public class BillPrintCommon
{
  private ArrayList<PrintColumn> _mPrintColumns = new ArrayList();
  public static PrintColumn[] PrintAvailableColumns = { new PrintColumn("Sl. No", "SL", 25, 4), new PrintColumn("Code", "PC", 100, 4), new PrintColumn("Product Name", "PN", 200, 2), new PrintColumn("Expiry", "EX", 100, 2), new PrintColumn("Unit price", "UP", 100, 4), new PrintColumn("Quantity", "QT", 100, 4), new PrintColumn("Amount", "AM", 44, 4), new PrintColumn("Discount", "DC", 44, 4) };
  private static BillPrintCommon _mInstance = new BillPrintCommon();
  
  public static BillPrintCommon getInstance()
  {
    return _mInstance;
  }
  
  public void createBillingColumnList(String paramString)
    throws JeException
  {
    String str1 = paramString;
    if (str1.length() == 0) {
      return;
    }
    if (str1 == null) {
      throw new JeException("Cannot find the column configuration int the config file.", "Config file might have corrupted.", "Contact administrator.", null);
    }
    int i = 0;
    while (i == 0)
    {
      int j = str1.indexOf(',');
      String str2 = null;
      if (j == -1)
      {
        str2 = str1;
        i = 1;
      }
      else
      {
        str2 = str1.substring(0, j);
      }
      String str3 = str2.substring(0, 2);
      int k = new Integer(str2.substring(3)).intValue();
      for (int m = 0; m < PrintAvailableColumns.length; m++) {
        if (str3.equalsIgnoreCase(PrintAvailableColumns[m].getCode()))
        {
          PrintAvailableColumns[m].width = k;
          this._mPrintColumns.add(PrintAvailableColumns[m]);
        }
      }
      str1 = str1.substring(j + 1);
    }
  }
  
  public ArrayList<PrintColumn> getPrintColumns()
  {
    return this._mPrintColumns;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.printing.BillPrintCommon
 * JD-Core Version:    0.7.0.1
 */