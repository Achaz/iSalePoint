package dm.tools.ui;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.Color;
import javax.swing.JPanel;
import org.jdesktop.swingx.JXTaskPaneContainer;

public class QuickTaskBox
  extends JPanel
{
  private String _mName = "";
  private static QuickTaskBox _mInstance = null;
  private JXTaskPaneContainer _mCurrentTaskContainer = null;
  
  private QuickTaskBox()
  {
    FormLayout localFormLayout = new FormLayout(" pref:grow", "10px, pref:grow, 10px");
    setLayout(localFormLayout);
    setBackground(new Color(102, 140, 217));
  }
  
  public static QuickTaskBox getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new QuickTaskBox();
    }
    return _mInstance;
  }
  
  public void setTaskPane(QuickTaskPane paramQuickTaskPane)
  {
    CellConstraints localCellConstraints = new CellConstraints();
    if (this._mCurrentTaskContainer != null) {
      this._mCurrentTaskContainer.setVisible(false);
    }
    removeAll();
    localCellConstraints.xywh(1, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(paramQuickTaskPane._mTaskContainer, localCellConstraints);
    this._mCurrentTaskContainer = paramQuickTaskPane._mTaskContainer;
    this._mCurrentTaskContainer.setVisible(true);
  }
  
  public void setName(String paramString)
  {
    this._mName = paramString;
  }
  
  public String getName()
  {
    return this._mName;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.ui.QuickTaskBox
 * JD-Core Version:    0.7.0.1
 */