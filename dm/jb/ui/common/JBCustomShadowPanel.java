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

public class JBCustomShadowPanel
  extends JXPanel
{
  private Color _mFillColor = new Color(87, 147, 191, 220);
  private BufferedImage shadow = null;
  private boolean _mShowShadow = true;
  private int _mShadowWidth = 30;
  
  public JBCustomShadowPanel(Color paramColor, int paramInt)
  {
    setOpaque(false);
    if (paramColor != null) {
      this._mFillColor = paramColor;
    }
    this._mShadowWidth = paramInt;
  }
  
  protected void paintComponent(Graphics paramGraphics)
  {
    int i = this._mShadowWidth / 2 - 1;
    int j = this._mShadowWidth / 2 - 1;
    int k = getWidth() - (this._mShadowWidth - 2);
    int m = getHeight() - (this._mShadowWidth - 2);
    int n = this._mShadowWidth;
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
    int i = getWidth() - (this._mShadowWidth - 2);
    int j = getHeight() - (this._mShadowWidth - 2);
    int k = this._mShadowWidth;
    int m = this._mShadowWidth / 2 - 1;
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
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.common.JBCustomShadowPanel
 * JD-Core Version:    0.7.0.1
 */