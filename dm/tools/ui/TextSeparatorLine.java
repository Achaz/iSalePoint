package dm.tools.ui;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.Container;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

public class TextSeparatorLine
  extends JPanel
{
  public TextSeparatorLine(String paramString)
  {
    initUI(paramString);
  }
  
  private void initUI(String paramString)
  {
    CellConstraints localCellConstraints = new CellConstraints();
    FormLayout localFormLayout = new FormLayout("pref, 10px, pref:grow", "pref");
    setLayout(localFormLayout);
    JLabel localJLabel = new JLabel(paramString);
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    localCellConstraints.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(new JSeparator(), localCellConstraints);
  }
  
  public void setVisible(boolean paramBoolean)
  {
    super.setVisible(paramBoolean);
    setBackground(getParent().getBackground());
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.ui.TextSeparatorLine
 * JD-Core Version:    0.7.0.1
 */