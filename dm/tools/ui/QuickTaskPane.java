package dm.tools.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

public class QuickTaskPane
{
  JXTaskPaneContainer _mTaskContainer = null;
  
  public QuickTaskPane()
  {
    this._mTaskContainer.setBackground(new Color(102, 140, 217));
    this._mTaskContainer.setForeground(new Color(102, 140, 217));
  }
  
  JXTaskPaneContainer getTaskContainer()
  {
    return this._mTaskContainer;
  }
  
  public TaskGroup createTaskGroup(String paramString1, String paramString2)
  {
    return new TaskGroup(paramString1, paramString2);
  }
  
  public void addTaskGroup(TaskGroup paramTaskGroup)
  {
    this._mTaskContainer.add(paramTaskGroup._mTaskPane);
  }
  
  public void setVisible(boolean paramBoolean)
  {
    this._mTaskContainer.setVisible(paramBoolean);
  }
  
  public class TaskGroup
  {
    private String _mName = null;
    private JXTaskPane _mTaskPane = null;
    
    TaskGroup(String paramString1, String paramString2)
    {
      this._mName = paramString1;
      this._mTaskPane = new JXTaskPane();
      this._mTaskPane.setTitle(this._mName);
      URL localURL = getClass().getResource(paramString2);
      this._mTaskPane.setIcon(new ImageIcon(localURL));
    }
    
    public void addAction(String paramString1, String paramString2, String paramString3, QuickTask paramQuickTask)
    {
      final QuickTask localQuickTask = paramQuickTask;
      AbstractAction local1 = new AbstractAction(paramString1)
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          localQuickTask.actionPerformed();
        }
      };
      URL localURL = getClass().getResource(paramString3);
      local1.putValue("SmallIcon", new ImageIcon(localURL));
      local1.putValue("ShortDescription", paramString2);
      this._mTaskPane.add(local1);
    }
    
    public void expand()
    {
      this._mTaskPane.setCollapsed(false);
    }
    
    public void collapse()
    {
      this._mTaskPane.setCollapsed(true);
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.ui.QuickTaskPane
 * JD-Core Version:    0.7.0.1
 */