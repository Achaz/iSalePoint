package dm.jb.ui.billing;

import dm.jb.JeException;
import dm.tools.ui.MainPanelIf;
import dm.tools.utils.Config;
import java.util.ArrayList;
import java.util.Iterator;

public class BillingUI
{
  private static MainPanelIf _mNewBillPanel = null;
  public static final BillingColumn[] BillViewColumnList = { new BillingColumn("Sl. No", "SL", 64, 4), new BillingColumn("Product code", "PC", 100, 0), new BillingColumn("Product Name", "PN", 200, 2), new BillingColumn("Unit price", "UP", 100, 4), new BillingColumn("Quantity", "QT", 100, 4), new BillingColumn("Amount", "AM", 44, 4), new BillingColumn("Discount", "DC", 44, 4) };
  public static ArrayList<BillingColumn> _mSelectedViewColumn = null;
  
  public static void reloadBillingPanel()
  {
    if (_mNewBillPanel != null) {
      ((BillingPanel)_mNewBillPanel).reloadWindow();
    }
  }
  
  public static void getNewBillPanel() {}
  
  public static void getViewBillPanel() {}
  
  public static ArrayList<BillingColumn> getViewColumns()
    throws JeException
  {
    if (_mSelectedViewColumn == null) {
      _mSelectedViewColumn = createSelectedViewBillingList();
    }
    return _mSelectedViewColumn;
  }
  
  public static ArrayList<BillingColumn> createSelectedViewBillingList()
    throws JeException
  {
    ArrayList localArrayList = new ArrayList();
    String str1 = Config.INSTANCE.getAttrib("JB_CONFIG.BILLING.VIEW.COLUMNS");
    if (str1 == null) {
      str1 = "SL:20,PC:80,PN:92,UP:32,QT:32,DC:32,AM:32";
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
      for (int m = 0; m < BillViewColumnList.length; m++) {
        if (str3.equalsIgnoreCase(BillViewColumnList[m]._mCode))
        {
          BillViewColumnList[m]._mSize = k;
          localArrayList.add(BillViewColumnList[m]);
        }
      }
      str1 = str1.substring(j + 1);
    }
    return localArrayList;
  }
  
  public static void setViewColumnList(ArrayList<BillingColumn> paramArrayList)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    Iterator localIterator = paramArrayList.iterator();
    boolean bool = localIterator.hasNext();
    while (bool)
    {
      BillingColumn localBillingColumn = (BillingColumn)localIterator.next();
      bool = localIterator.hasNext();
      localStringBuffer.append(localBillingColumn._mCode + ":" + localBillingColumn._mSize);
      if (bool) {
        localStringBuffer.append(",");
      }
    }
    Config.INSTANCE.setAttrib("JB_CONFIG.BILLING.VIEW.COLUMNS", localStringBuffer.toString());
  }
  
  public static void loadSelectedBillingColumns() {}
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.billing.BillingUI
 * JD-Core Version:    0.7.0.1
 */