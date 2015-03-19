package dm.jb.ui.report;

public class ReportTemplate
{
  String _mClassName = null;
  String _mName = null;
  String _mGroupName = null;
  ReportTemplate _mNext = null;
  ReportTemplate _mChild = null;
  ReportTemplate _mParent = null;
  String jarFile = null;
  JBReport _mReportInstance = null;
  
  public ReportTemplate(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    this._mClassName = paramString1;
    this._mName = paramString2;
    this._mGroupName = paramString3;
    this.jarFile = paramString4;
  }
  
  public String toString()
  {
    return this._mName;
  }
  
  public String getOpLine()
  {
    StringBuffer localStringBuffer = new StringBuffer("REPORT:name=");
    localStringBuffer.append(this._mName);
    localStringBuffer.append("|group=");
    localStringBuffer.append(this._mGroupName);
    localStringBuffer.append("|class=");
    localStringBuffer.append(this._mClassName);
    localStringBuffer.append("|jar=");
    localStringBuffer.append(this.jarFile);
    return localStringBuffer.toString();
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.report.ReportTemplate
 * JD-Core Version:    0.7.0.1
 */