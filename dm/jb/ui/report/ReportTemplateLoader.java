package dm.jb.ui.report;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ReportTemplateLoader
{
  public static ReportTemplateLoader _mInstance = new ReportTemplateLoader();
  public ReportTemplate _mRootReport = null;
  
  private ReportTemplateLoader()
  {
    try
    {
      loadTemplateFiles();
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    catch (JBReportException localJBReportException)
    {
      localJBReportException.printStackTrace();
    }
  }
  
  private void loadTemplateFiles()
    throws IOException, JBReportException
  {
    BufferedReader localBufferedReader = new BufferedReader(new FileReader("reports/reports.inf"));
    String str = null;
    while ((str = localBufferedReader.readLine()) != null) {
      addReportTemplate(str);
    }
    localBufferedReader.close();
  }
  
  private ReportTemplate getReportTemplateFromLine(String paramString)
    throws JBReportException
  {
    if (!paramString.startsWith("REPORT:")) {
      throw new JBReportException("Report defintion file format error", "Internal Error", null);
    }
    paramString = paramString.substring("REPORT:".length());
    if (!paramString.startsWith("name=")) {
      throw new JBReportException("Report defintion file format error\nName not found.", "Internal Error", null);
    }
    paramString = paramString.substring("name=".length());
    int i = paramString.indexOf('|');
    if (i <= 0) {
      throw new JBReportException("Report defintion file format error\nName not found.", "Internal Error", null);
    }
    String str1 = paramString.substring(0, i);
    paramString = paramString.substring(i + 1);
    if (!paramString.startsWith("group=")) {
      throw new JBReportException("Report defintion file format error\nGroup not found.", "Internal Error", null);
    }
    paramString = paramString.substring("group=".length());
    i = paramString.indexOf('|');
    if (i <= 0) {
      throw new JBReportException("Report defintion file format error\nGroup not found.", "Internal Error", null);
    }
    String str2 = paramString.substring(0, i);
    paramString = paramString.substring(i + 1);
    if (!paramString.startsWith("class=")) {
      throw new JBReportException("Report defintion file format error\nClass not found.", "Internal Error", null);
    }
    paramString = paramString.substring("class=".length());
    i = paramString.indexOf('|');
    if (i <= 0) {
      throw new JBReportException("Report defintion file format error\nGroup not found.", "Internal Error", null);
    }
    String str3 = paramString.substring(0, i);
    paramString = paramString.substring(i + 1);
    if (!paramString.startsWith("jar=")) {
      throw new JBReportException("Report defintion file format error\nClass not found.", "Internal Error", null);
    }
    paramString = paramString.substring("jar=".length());
    ReportTemplate localReportTemplate = new ReportTemplate(str3, str1, str2, paramString);
    return localReportTemplate;
  }
  
  private ReportTemplate findGroup(String paramString)
  {
    if ((paramString.equals(this._mRootReport._mName)) || (paramString.equals("Root"))) {
      return this._mRootReport;
    }
    for (ReportTemplate localReportTemplate = this._mRootReport._mChild; localReportTemplate != null; localReportTemplate = localReportTemplate._mNext) {
      if (localReportTemplate._mName.equals(paramString)) {
        return localReportTemplate;
      }
    }
    return null;
  }
  
  public ReportTemplate addReportTemplate(String paramString)
    throws JBReportException
  {
    ReportTemplate localReportTemplate1 = getReportTemplateFromLine(paramString);
    ReportTemplate localReportTemplate2 = findGroup(localReportTemplate1._mGroupName);
    if (localReportTemplate2 == null)
    {
      localReportTemplate2 = new ReportTemplate(null, localReportTemplate1._mGroupName, null, null);
      localReportTemplate3 = this._mRootReport._mChild;
      this._mRootReport._mChild = localReportTemplate2;
      localReportTemplate2._mNext = localReportTemplate3;
      localReportTemplate2._mParent = this._mRootReport;
    }
    ReportTemplate localReportTemplate3 = localReportTemplate2._mChild;
    localReportTemplate2._mChild = localReportTemplate1;
    localReportTemplate1._mNext = localReportTemplate3;
    localReportTemplate2._mChild._mParent = localReportTemplate2;
    return localReportTemplate1;
  }
  
  public void writeTemplateToFile()
    throws JBReportException
  {
    BufferedWriter localBufferedWriter = null;
    try
    {
      localBufferedWriter = new BufferedWriter(new FileWriter("reports/reports.inf"));
      for (ReportTemplate localReportTemplate = this._mRootReport._mChild; localReportTemplate != null; localReportTemplate = localReportTemplate._mNext)
      {
        Object localObject1;
        if (localReportTemplate._mChild == null)
        {
          localObject1 = localReportTemplate.getOpLine();
          localBufferedWriter.write((String)localObject1 + "\n");
        }
        else
        {
          for (localObject1 = localReportTemplate._mChild; localObject1 != null; localObject1 = ((ReportTemplate)localObject1)._mNext)
          {
            String str = ((ReportTemplate)localObject1).getOpLine();
            localBufferedWriter.write(str + "\n");
          }
        }
      }
      return;
    }
    catch (IOException localIOException2)
    {
      throw new JBReportException("Error writing the registration file.", "Internal Error", localIOException2);
    }
    finally
    {
      try
      {
        localBufferedWriter.close();
      }
      catch (IOException localIOException3)
      {
        throw new JBReportException("Report defintion file format error\nClass not found.", "Internal Error", null);
      }
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.report.ReportTemplateLoader
 * JD-Core Version:    0.7.0.1
 */