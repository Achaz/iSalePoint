package dm.jb.ui.common;

import java.awt.Font;
import javax.swing.JLabel;

public class JBLabel
  extends JLabel
{
  public static Font fnt = null;
  
  public JBLabel(String paramString)
  {
    super(paramString);
    if (fnt == null)
    {
      Font localFont = getFont();
      fnt = new Font(localFont.getName(), 0, 20);
    }
    setFont(fnt);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.common.JBLabel
 * JD-Core Version:    0.7.0.1
 */