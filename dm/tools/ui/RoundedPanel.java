package dm.tools.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import org.jdesktop.swingx.JXPanel;

public class RoundedPanel
  extends JXPanel
{
  private Color _mFillColor = new Color(241, 184, 133, 255);
  
  protected void paintComponent(Graphics paramGraphics)
  {
    int i = 14;
    int j = 14;
    int k = getWidth();
    int m = getHeight();
    int n = 30;
    Graphics2D localGraphics2D = (Graphics2D)paramGraphics.create();
    localGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    localGraphics2D.setColor(this._mFillColor);
    localGraphics2D.fillRoundRect(0, 0, k, m, n, n);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.ui.RoundedPanel
 * JD-Core Version:    0.7.0.1
 */