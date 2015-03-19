package dm.jb.ui.common;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import javax.swing.JLabel;

public class KeyButton
  extends JLabel
{
  public static final byte SIZE_NORMAL = 0;
  private String _mKey = "";
  private boolean _mPressed = false;
  int _mKC = 0;
  
  public KeyButton(String paramString, int paramInt)
  {
    this._mKC = paramInt;
    this._mKey = paramString;
    addMouseListener(new MouseListener()
    {
      public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
      {
        NumericVirtualKeyPad.getInstance().sendKeyEvent(KeyButton.this._mKC);
      }
      
      public void mousePressed(MouseEvent paramAnonymousMouseEvent)
      {
        KeyButton.this._mPressed = true;
        KeyButton.this.repaint();
      }
      
      public void mouseReleased(MouseEvent paramAnonymousMouseEvent)
      {
        KeyButton.this._mPressed = false;
        KeyButton.this.repaint();
      }
      
      public void mouseEntered(MouseEvent paramAnonymousMouseEvent) {}
      
      public void mouseExited(MouseEvent paramAnonymousMouseEvent) {}
    });
  }
  
  public void paint(Graphics paramGraphics)
  {
    Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
    localGraphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    localGraphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    paramGraphics.setFont(NumericVirtualKeyPad.FONT);
    localGraphics2D.getFontRenderContext();
    int i = getWidth();
    int j = getHeight();
    Image localImage = null;
    if (this._mPressed) {
      localImage = NumericVirtualKeyPad.BUTTON_BG_P;
    } else {
      localImage = NumericVirtualKeyPad.BUTTON_BG;
    }
    paramGraphics.drawImage(localImage, 0, 0, null);
    if (i > 60)
    {
      for (int k = 50; k < i - 20; k += 20) {
        paramGraphics.drawImage(localImage, k, 0, k + 20, j, 10, 0, 30, 60, null);
      }
      paramGraphics.drawImage(localImage, i - 20, 0, i, j, 40, 0, 60, 60, null);
    }
    if (this._mPressed) {
      paramGraphics.setColor(new Color(200, 200, 200));
    } else {
      paramGraphics.setColor(Color.WHITE);
    }
    FontRenderContext localFontRenderContext = localGraphics2D.getFontRenderContext();
    Rectangle2D localRectangle2D = NumericVirtualKeyPad.FONT.getStringBounds(this._mKey, localFontRenderContext);
    int m = (i - (int)localRectangle2D.getWidth()) / 2;
    int n = 38;
    paramGraphics.drawString(this._mKey, m, n);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.common.KeyButton
 * JD-Core Version:    0.7.0.1
 */