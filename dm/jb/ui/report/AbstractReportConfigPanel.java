package dm.jb.ui.report;

import javax.swing.JPanel;

public abstract class AbstractReportConfigPanel
  extends JPanel
{
  public abstract boolean isConfigValid();
  
  public abstract void configApplied(ReportTypeConfigPanel paramReportTypeConfigPanel);
  
  public abstract void windowDisplayed();
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.report.AbstractReportConfigPanel
 * JD-Core Version:    0.7.0.1
 */