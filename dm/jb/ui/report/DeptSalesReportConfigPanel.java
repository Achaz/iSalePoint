package dm.jb.ui.report;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.db.objects.BillEntryRow;
import dm.jb.db.objects.BillTableTableDef;
import dm.jb.db.objects.DeptRow;
import dm.jb.db.objects.DeptTableDef;
import dm.jb.db.objects.ProductRow;
import dm.jb.ui.MainWindow;
import dm.jb.ui.report.utils.ReportDateRangePanel;
import dm.tools.db.DBException;
import dm.tools.ui.ActionPanel;
import dm.tools.ui.ShuttlePane;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JPanel;

public class DeptSalesReportConfigPanel
  extends AbstractReportConfigPanel
{
  private ShuttlePane<DeptRow> _mDeptSelect = null;
  private ReportDateRangePanel _mDateRangePanel = null;
  private boolean _mDateToday = false;
  
  public DeptSalesReportConfigPanel(boolean paramBoolean)
  {
    initUI();
    this._mDateToday = paramBoolean;
  }
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("10px,pref:grow,10px", "10px,pref, 10px, pref,20px,pref:grow,20px,pref:grow,10px");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    this._mDateRangePanel = new ReportDateRangePanel();
    localCellConstraints.xywh(2, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mDateRangePanel, localCellConstraints);
    localCellConstraints.xywh(2, 4, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getDeptPanel(), localCellConstraints);
  }
  
  private JPanel getDeptPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("200px,50px,200px", "250px");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    ArrayList localArrayList = null;
    try
    {
      localArrayList = DeptTableDef.getInstance().getDeptList();
    }
    catch (DBException localDBException)
    {
      System.err.println(localDBException);
    }
    this._mDeptSelect = new ShuttlePane(false);
    this._mDeptSelect.setFromList(localArrayList);
    localCellConstraints.xywh(1, 1, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mDeptSelect, localCellConstraints);
    this._mDeptSelect.setBackground(getBackground());
    return localJPanel;
  }
  
  public String getTitle()
  {
    return "Department sales report configuration";
  }
  
  public HashMap<Comparable, Double> getSalesReportForTodayForSelectedDepts()
  {
    HashMap localHashMap = new HashMap();
    ArrayList localArrayList = this._mDeptSelect.getSelectedObjects();
    Iterator localIterator = localArrayList.iterator();
    if (localArrayList.size() == 0) {
      return localHashMap;
    }
    Date localDate1 = this._mDateRangePanel.getFromDate();
    Date localDate2 = this._mDateRangePanel.getToDate();
    if (localDate1 == null) {
      localDate1 = new Date();
    }
    if (localDate2 == null) {
      localDate2 = new Date();
    }
    try
    {
      BillEntryRow[] arrayOfBillEntryRow = BillTableTableDef.getInstance().getBillEntriesForDateDurationForDepts(localDate1, localDate2, localArrayList);
      while (localIterator.hasNext())
      {
        DeptRow localDeptRow = (DeptRow)localIterator.next();
        int i = localDeptRow.getDeptIndex();
        double d = 0.0D;
        for (int j = 0; j < arrayOfBillEntryRow.length; j++)
        {
          int k = arrayOfBillEntryRow[j].getProduct().getDeptIndex();
          if (k == i) {
            d += arrayOfBillEntryRow[j].getAmount();
          }
        }
        localHashMap.put(localDeptRow, new Double(d));
      }
    }
    catch (DBException localDBException)
    {
      System.err.println("EXP " + localDBException);
    }
    return localHashMap;
  }
  
  public boolean isConfigValid()
  {
    return true;
  }
  
  public void configApplied(ReportTypeConfigPanel paramReportTypeConfigPanel)
  {
    ReportOutputPanel localReportOutputPanel = ReportOutputPanel.getInstance();
    paramReportTypeConfigPanel.showReport(getSalesReportForTodayForSelectedDepts(), false, "Daily department sales report", localReportOutputPanel);
    localReportOutputPanel.clearAllFields();
    ActionPanel localActionPanel = MainWindow.getActionPanel();
    localActionPanel.setVisible(true);
    localActionPanel.pushObject(localReportOutputPanel);
    localReportOutputPanel.setActionPanel(localActionPanel);
    localActionPanel.setTitle("Report result");
    MainWindow.instance.setVisible(true);
    localReportOutputPanel.setDefaultFocus();
  }
  
  public void windowDisplayed()
  {
    this._mDateRangePanel.resetAll();
    if (this._mDateToday)
    {
      this._mDateRangePanel.setDateRange(new Date(), null);
      this._mDateRangePanel.setTodayDate();
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.report.DeptSalesReportConfigPanel
 * JD-Core Version:    0.7.0.1
 */