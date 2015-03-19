package dm.jb.printing.laser;

import dm.jb.JeException;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

public class TablePrintColumn
{
  public int alignment;
  public String title = "";
  public Font titleFont = null;
  public Font dataFont = null;
  public int width = 0;
  public int titleHeight = -1;
  
  public TablePrintColumn(String paramString, Font paramFont1, Font paramFont2, int paramInt1, int paramInt2, int paramInt3)
  {
    this.title = paramString;
    this.alignment = paramInt2;
    this.titleFont = paramFont1;
    this.dataFont = paramFont2;
    this.width = paramInt1;
    this.titleHeight = paramInt3;
  }
  
  public Point printTitle(Graphics2D paramGraphics2D, Font paramFont, int paramInt1, boolean paramBoolean1, int paramInt2, int paramInt3, boolean paramBoolean2, boolean paramBoolean3)
    throws JeException
  {
    Font localFont = null;
    if (this.titleFont != null) {
      localFont = this.titleFont;
    } else {
      localFont = paramFont;
    }
    paramGraphics2D.setFont(localFont);
    FontRenderContext localFontRenderContext = paramGraphics2D.getFontRenderContext();
    Rectangle2D localRectangle2D = localFont.getStringBounds(this.title, localFontRenderContext);
    int i = this.titleHeight != -1 ? this.titleHeight : paramInt1;
    int j = paramInt3 + i - 4;
    int k = this.width != -1 ? this.width : (int)localRectangle2D.getWidth();
    if ((paramBoolean1) && (paramBoolean3)) {
      paramGraphics2D.drawLine(paramInt2, paramInt3, paramInt2, paramInt3 + i);
    }
    paramGraphics2D.drawString(this.title, paramInt2 + 4, j);
    if (paramBoolean2)
    {
      paramGraphics2D.drawLine(paramInt2, paramInt3, paramInt2 + k, paramInt3);
      paramGraphics2D.drawLine(paramInt2, paramInt3 + i, paramInt2 + k, paramInt3 + i);
    }
    paramInt2 += k;
    if (paramBoolean3) {
      paramGraphics2D.drawLine(paramInt2, paramInt3, paramInt2, paramInt3 + i);
    }
    paramInt3 += i;
    Point localPoint = new Point(paramInt2, paramInt3);
    return localPoint;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.printing.laser.TablePrintColumn
 * JD-Core Version:    0.7.0.1
 */