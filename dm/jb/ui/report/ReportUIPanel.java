package dm.jb.ui.report;

import dm.jb.ui.MainWindow;
import dm.tools.ui.AbstractMainPanel;
import dm.tools.ui.ActionPanel;
import dm.tools.ui.QuickTask;
import dm.tools.ui.QuickTaskBox;
import dm.tools.ui.QuickTaskPane;
import dm.tools.ui.QuickTaskPane.TaskGroup;

public class ReportUIPanel
{
  private static QuickTaskPane _mTaskPane = null;
  
  public static void getReportTemplatePanel()
  {
    QuickTaskBox localQuickTaskBox = QuickTaskBox.getInstance();
    if (!localQuickTaskBox.getName().equals("Reports"))
    {
      setupActionList();
      localQuickTaskBox.setTaskPane(_mTaskPane);
      localQuickTaskBox.setName("Reports");
    }
    ReportManagePanel localReportManagePanel = ReportManagePanel._mInstance;
    ActionPanel localActionPanel = MainWindow.getActionPanel();
    localActionPanel.setVisible(true);
    AbstractMainPanel localAbstractMainPanel = (AbstractMainPanel)localReportManagePanel;
    localActionPanel.cleanPush(localAbstractMainPanel);
    localAbstractMainPanel.setActionPanel(localActionPanel);
    localActionPanel.setTitle("Reports. Select a template");
    MainWindow.instance.setVisible(true);
    localAbstractMainPanel.setDefaultFocus();
  }
  
  private static void setupActionList()
  {
    if (_mTaskPane == null)
    {
      _mTaskPane = new QuickTaskPane();
      QuickTaskPane.TaskGroup localTaskGroup = _mTaskPane.createTaskGroup("Reports", "/dm/jb/images/report.png");
      localTaskGroup.addAction("Report from template", "Report from template", "/dm/jb/images/report.gif", new QuickTask()
      {
        public void actionPerformed() {}
      });
      localTaskGroup.addAction("Favourite Reports", "Favourite Reports", "/dm/jb/images/product_add.gif", new QuickTask()
      {
        public void actionPerformed() {}
      });
      _mTaskPane.addTaskGroup(localTaskGroup);
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.report.ReportUIPanel
 * JD-Core Version:    0.7.0.1
 */