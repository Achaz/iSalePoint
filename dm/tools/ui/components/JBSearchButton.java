package dm.tools.ui.components;

import java.net.URL;
import javax.swing.ImageIcon;
import org.jdesktop.swingx.JXButton;

public class JBSearchButton
  extends JXButton
{
  public JBSearchButton(boolean paramBoolean)
  {
    URL localURL = null;
    if (paramBoolean) {
      localURL = getClass().getResource("/images/searchBig.gif");
    } else {
      localURL = getClass().getResource("/images/search.gif");
    }
    if (localURL != null) {
      setIcon(new ImageIcon(localURL, "Search"));
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.ui.components.JBSearchButton
 * JD-Core Version:    0.7.0.1
 */