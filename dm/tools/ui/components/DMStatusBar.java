package dm.tools.ui.components;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import javax.swing.JPanel;

public class DMStatusBar
  extends JPanel
{
  public DMStatusBar()
  {
    initUI();
  }
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("0px,pref:grow,0px", "pref:grow");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    DMStatusBarComponent localDMStatusBarComponent = new DMStatusBarComponent();
    add(localDMStatusBarComponent, localCellConstraints);
  }
  
  public void addStatusbarComponent(DMStatusBarComponent paramDMStatusBarComponent)
  {
    FormLayout localFormLayout = (FormLayout)getLayout();
    int i = localFormLayout.getColumnCount();
    localFormLayout.insertColumn(i - 2, new ColumnSpec("2px"));
    localFormLayout.insertColumn(i - 2, new ColumnSpec("pref"));
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(i - 2, 1, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
    add(paramDMStatusBarComponent, localCellConstraints);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.ui.components.DMStatusBar
 * JD-Core Version:    0.7.0.1
 */