package dm.jb.op.report;

import java.util.ArrayList;
import java.util.Iterator;

public class ReportCategory
{
  private String _mName = null;
  private static ArrayList<ReportCategory> _mReportCatList = new ArrayList();
  private ArrayList<ReportTemplate> _mTemplateList = null;
  public static final String REP_CAT_DAILY_SALES_STR = "Daily sales";
  public static final String REP_CAT_DATE_RANGE_SALES_STR = "Sales over a period";
  
  private ReportCategory(String paramString)
  {
    this._mName = paramString;
  }
  
  public String toString()
  {
    return this._mName;
  }
  
  public static void setupReportCatList()
  {
    _mReportCatList.clear();
    ReportCategory localReportCategory = new ReportCategory("Daily sales");
    localReportCategory._mTemplateList = getDailySalesTemplateList();
    _mReportCatList.add(localReportCategory);
    localReportCategory = new ReportCategory("Sales over a period");
    localReportCategory._mTemplateList = getPeriodSalesTemplateList();
    _mReportCatList.add(localReportCategory);
  }
  
  public static ArrayList<ReportCategory> getRepCatList()
  {
    return _mReportCatList;
  }
  
  public ArrayList<ReportTemplate> getTemplateList()
  {
    return this._mTemplateList;
  }
  
  private static ArrayList<ReportTemplate> getDailySalesTemplateList()
  {
    ArrayList localArrayList = new ArrayList();
    ReportTemplate localReportTemplate = new ReportTemplate("Product", "Daily product sales report. Multiple product selection allowed");
    localArrayList.add(localReportTemplate);
    localReportTemplate = new ReportTemplate("Department", "Departmentwise sales report. Mutliple department selection allowed");
    localArrayList.add(localReportTemplate);
    return localArrayList;
  }
  
  private static ArrayList<ReportTemplate> getPeriodSalesTemplateList()
  {
    ArrayList localArrayList = new ArrayList();
    ReportTemplate localReportTemplate = new ReportTemplate("Product", "Product sales report for a period. Multiple product selection allowed");
    localArrayList.add(localReportTemplate);
    localReportTemplate = new ReportTemplate("Department", "Departmentwise sales report for a period. Mutliple department selection allowed");
    localArrayList.add(localReportTemplate);
    return localArrayList;
  }
  
  public static void setObjectForTemplate(String paramString1, String paramString2, Object paramObject)
  {
    Iterator localIterator = _mReportCatList.iterator();
    ReportCategory localReportCategory = null;
    while (localIterator.hasNext())
    {
      localReportCategory = (ReportCategory)localIterator.next();
      if (localReportCategory._mName.equals(paramString1)) {
        break;
      }
    }
    if (localReportCategory != null) {
      localReportCategory.addObjectToCategory(paramString2, paramObject);
    }
  }
  
  private void addObjectToCategory(String paramString, Object paramObject)
  {
    Iterator localIterator = this._mTemplateList.iterator();
    while (localIterator.hasNext())
    {
      ReportTemplate localReportTemplate = (ReportTemplate)localIterator.next();
      if (localReportTemplate.getName().equals(paramString))
      {
        localReportTemplate.setObject(paramObject);
        return;
      }
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.op.report.ReportCategory
 * JD-Core Version:    0.7.0.1
 */