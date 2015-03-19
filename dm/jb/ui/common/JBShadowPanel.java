package dm.jb.ui.common;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.graphics.GraphicsUtilities;
import org.jdesktop.swingx.graphics.ShadowRenderer;

public class JBShadowPanel
  extends JXPanel
{
  private BufferedImage shadow = null;
  private boolean _mShowShadow = true;
  private Color _mFillColor = new Color(87, 147, 191, 220);
  
  public JBShadowPanel()
  {
    this(true);
  }
  
  public JBShadowPanel(Color paramColor)
  {
    this(true);
    this._mFillColor = paramColor;
  }
  
  public JBShadowPanel(boolean paramBoolean)
  {
    setOpaque(false);
    this._mShowShadow = paramBoolean;
  }
  
  protected void paintComponent(Graphics paramGraphics)
  {
    int i = 14;
    int j = 14;
    int k = getWidth() - 28;
    int m = getHeight() - 28;
    int n = 30;
    Graphics2D localGraphics2D = (Graphics2D)paramGraphics.create();
    localGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    if ((this._mShowShadow) && (this.shadow != null))
    {
      int i1 = (this.shadow.getWidth() - k) / 2;
      int i2 = (this.shadow.getHeight() - m) / 2;
      localGraphics2D.drawImage(this.shadow, i - i1, j - i2, null);
    }
    localGraphics2D.setColor(this._mFillColor);
    localGraphics2D.fillRoundRect(i, j, k, m, n, n);
    if (this._mShowShadow)
    {
      localGraphics2D.setStroke(new BasicStroke(3.0F));
      localGraphics2D.setColor(Color.WHITE);
      localGraphics2D.drawRoundRect(i, j, k, m, n, n);
    }
    localGraphics2D.dispose();
  }
  
  public void setBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.setBounds(paramInt1, paramInt2, paramInt3, paramInt4);
    int i = getWidth() - 28;
    int j = getHeight() - 28;
    int k = 30;
    int m = 14;
    this.shadow = GraphicsUtilities.createCompatibleTranslucentImage(i, j);
    Graphics2D localGraphics2D = this.shadow.createGraphics();
    localGraphics2D.setColor(Color.WHITE);
    localGraphics2D.fillRoundRect(0, 0, i, j, k, k);
    localGraphics2D.dispose();
    ShadowRenderer localShadowRenderer = new ShadowRenderer(m, 0.5F, Color.BLACK);
    this.shadow = localShadowRenderer.createShadow(this.shadow);
    localGraphics2D = this.shadow.createGraphics();
    localGraphics2D.fillRoundRect(m, m, i, j, k, k);
    localGraphics2D.dispose();
  }
  
  public Color getBackground()
  {
    return this._mFillColor;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.common.JBShadowPanel
 * JD-Core Version:    0.7.0.1
 */