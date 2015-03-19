package dm.tools.ui.components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import javax.swing.JComponent;

public class JMultilineLabel
  extends JComponent
{
  private String text;
  private Insets margin = new Insets(5, 5, 5, 5);
  private int maxWidth = 2147483647;
  private boolean justify;
  private final FontRenderContext frc = new FontRenderContext(null, false, false);
  
  private void morph()
  {
    revalidate();
    repaint();
  }
  
  public String getText()
  {
    return this.text;
  }
  
  public void setText(String paramString)
  {
    String str = this.text;
    this.text = paramString;
    firePropertyChange("text", str, this.text);
    if (str == null ? paramString != null : !str.equals(paramString)) {
      morph();
    }
  }
  
  public int getMaxWidth()
  {
    return this.maxWidth;
  }
  
  public void setMaxWidth(int paramInt)
  {
    if (paramInt <= 0) {
      throw new IllegalArgumentException();
    }
    int i = this.maxWidth;
    this.maxWidth = paramInt;
    firePropertyChange("maxWidth", i, this.maxWidth);
    if (i != this.maxWidth) {
      morph();
    }
  }
  
  public boolean isJustified()
  {
    return this.justify;
  }
  
  public void setJustified(boolean paramBoolean)
  {
    boolean bool = this.justify;
    this.justify = paramBoolean;
    firePropertyChange("justified", bool, this.justify);
    if (bool != this.justify) {
      repaint();
    }
  }
  
  public Dimension getPreferredSize()
  {
    return paintOrGetSize(null, getMaxWidth());
  }
  
  public Dimension getMinimumSize()
  {
    return getPreferredSize();
  }
  
  protected void paintComponent(Graphics paramGraphics)
  {
    super.paintComponent(paramGraphics);
    paintOrGetSize((Graphics2D)paramGraphics, getWidth());
  }
  
  private Dimension paintOrGetSize(Graphics2D paramGraphics2D, int paramInt)
  {
    Insets localInsets = getInsets();
    paramInt -= localInsets.left + localInsets.right + this.margin.left + this.margin.right;
    float f1 = localInsets.left + localInsets.right + this.margin.left + this.margin.right;
    float f2 = localInsets.left + this.margin.left;
    float f3 = localInsets.top + this.margin.top;
    if ((paramInt > 0) && (this.text != null) && (this.text.length() > 0))
    {
      AttributedString localAttributedString = new AttributedString(getText());
      localAttributedString.addAttribute(TextAttribute.FONT, getFont());
      AttributedCharacterIterator localAttributedCharacterIterator = localAttributedString.getIterator();
      LineBreakMeasurer localLineBreakMeasurer = new LineBreakMeasurer(localAttributedCharacterIterator, this.frc);
      TextLayout localTextLayout;
      for (float f4 = 0.0F; localLineBreakMeasurer.getPosition() < localAttributedCharacterIterator.getEndIndex(); f4 = Math.max(f4, localTextLayout.getVisibleAdvance()))
      {
        localTextLayout = localLineBreakMeasurer.nextLayout(paramInt);
        if ((paramGraphics2D != null) && (isJustified()) && (localTextLayout.getVisibleAdvance() > 0.8D * paramInt)) {
          localTextLayout = localTextLayout.getJustifiedLayout(paramInt);
        }
        if (paramGraphics2D != null) {
          localTextLayout.draw(paramGraphics2D, f2, f3 + localTextLayout.getAscent());
        }
        f3 += localTextLayout.getDescent() + localTextLayout.getLeading() + localTextLayout.getAscent();
      }
      f1 += f4;
    }
    return new Dimension((int)Math.ceil(f1), (int)Math.ceil(f3) + localInsets.bottom + this.margin.bottom);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.ui.components.JMultilineLabel
 * JD-Core Version:    0.7.0.1
 */