package dm.jb.printing.laser;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

public class TablePrintCell
{
  public static final int DATA_ALIGN_LEFT = 2;
  public static final int DATA_ALIGN_RIGHT = 4;
  public static final int DATA_ALIGN_UNSPECIFIED = 0;
  public String data = "";
  public int alignment = 0;
  public Font font = null;
  private int _mWidth = 0;
  
  public TablePrintCell(String paramString, int paramInt1, Font paramFont, int paramInt2)
  {
    this.data = paramString;
    this.alignment = paramInt1;
    this.font = paramFont;
    this._mWidth = paramInt2;
  }
  
  public Point print(Graphics2D paramGraphics2D, Font paramFont, boolean paramBoolean1, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean2)
  {
    Font localFont = null;
    int i = paramInt1;
    if ((paramBoolean1) && (paramBoolean2)) {
      paramGraphics2D.drawLine(paramInt1, paramInt2, paramInt1, paramInt2 + paramInt3);
    }
    if (this.font == null) {
      localFont = paramFont;
    } else {
      localFont = this.font;
    }
    paramGraphics2D.setFont(localFont);
    FontRenderContext localFontRenderContext = paramGraphics2D.getFontRenderContext();
    if (this.alignment == 2)
    {
      paramGraphics2D.drawString(this.data, paramInt1 + 4, paramInt2 + paramInt3 - 4);
    }
    else
    {
      int j = (int)localFont.getStringBounds(this.data, localFontRenderContext).getWidth();
      paramGraphics2D.drawString(this.data, paramInt1 + (this._mWidth - j) - 4, paramInt2 + paramInt3 - 4);
    }
    if (paramBoolean2) {
      paramGraphics2D.drawLine(paramInt1 + this._mWidth, paramInt2, paramInt1 + this._mWidth, paramInt2 + paramInt3);
    }
    paramInt1 = i + this._mWidth;
    Point localPoint = new Point(paramInt1, paramInt2);
    return localPoint;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.printing.laser.TablePrintCell
 * JD-Core Version:    0.7.0.1
 */