package dm.jb.op.report;

public class ReportTemplate
{
  private String _mName = null;
  private String _mDescription = null;
  private Object _mObject = null;
  public static final String REP_TEMPLATE_DAILY_SALES_PRODUCT_STR = "Product";
  public static final String REP_TEMPLATE_DAILY_SALES_DEPT_STR = "Department";
  public static final String REP_TEMPLATE_PERIOD_SALES_PRODUCT_STR = "Product";
  public static final String REP_TEMPLATE_PERIOD_SALES_DEPT_STR = "Department";
  
  ReportTemplate(String paramString1, String paramString2)
  {
    this._mName = paramString1;
    this._mDescription = paramString2;
  }
  
  public String getDescription()
  {
    return this._mDescription;
  }
  
  public String toString()
  {
    return this._mName;
  }
  
  public String getName()
  {
    return this._mName;
  }
  
  public void setObject(Object paramObject)
  {
    this._mObject = paramObject;
  }
  
  public Object getObject()
  {
    return this._mObject;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.op.report.ReportTemplate
 * JD-Core Version:    0.7.0.1
 */