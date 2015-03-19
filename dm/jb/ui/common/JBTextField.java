package dm.jb.ui.common;

import java.awt.Font;
import javax.swing.JTextField;

public class JBTextField
  extends JTextField
{
  public static Font fnt = null;
  
  public JBTextField()
  {
    if (fnt == null)
    {
      Font localFont = getFont();
      fnt = new Font(localFont.getName(), 0, 20);
    }
    setFont(fnt);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.common.JBTextField
 * JD-Core Version:    0.7.0.1
 */