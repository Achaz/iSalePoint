package dm.tools.ui.components;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

public class JTextSeparator
  extends JPanel
{
  private JLabel _mLabel = null;
  
  public JTextSeparator(String paramString)
  {
    initUI(paramString);
  }
  
  public JTextSeparator(String paramString, boolean paramBoolean1, boolean paramBoolean2)
  {
    setOpaque(paramBoolean1);
    initUI(paramString);
    Font localFont = this._mLabel.getFont();
    this._mLabel.setFont(new Font(localFont.getName(), 0, 20));
  }
  
  public void setText(String paramString)
  {
    this._mLabel.setText(paramString);
  }
  
  private void initUI(String paramString)
  {
    FormLayout localFormLayout;
    CellConstraints localCellConstraints;
    if (paramString == null)
    {
      localFormLayout = new FormLayout("90px,10px,pref:grow", "pref");
      setLayout(localFormLayout);
      localCellConstraints = new CellConstraints();
      localCellConstraints.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.CENTER);
      add(new JSeparator(), localCellConstraints);
    }
    else
    {
      localFormLayout = new FormLayout("pref,3px,pref:grow", "pref");
      setLayout(localFormLayout);
      localCellConstraints = new CellConstraints();
      JLabel localJLabel = new JLabel(paramString);
      localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.LEFT, CellConstraints.FILL);
      add(localJLabel, localCellConstraints);
      this._mLabel = localJLabel;
      localCellConstraints.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.CENTER);
      add(new JSeparator(), localCellConstraints);
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.ui.components.JTextSeparator
 * JD-Core Version:    0.7.0.1
 */