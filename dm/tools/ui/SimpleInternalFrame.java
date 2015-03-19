package dm.tools.ui;

import com.jgoodies.looks.LookUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Paint;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;

public class SimpleInternalFrame
  extends JPanel
{
  private JLabel titleLabel;
  private GradientPanel gradientPanel;
  private JPanel headerPanel;
  private boolean isSelected = false;
  
  public SimpleInternalFrame(String paramString)
  {
    this(null, paramString, null, null);
  }
  
  public SimpleInternalFrame(Icon paramIcon, String paramString)
  {
    this(paramIcon, paramString, null, null);
  }
  
  public SimpleInternalFrame(String paramString, JToolBar paramJToolBar, JComponent paramJComponent)
  {
    this(null, paramString, paramJToolBar, paramJComponent);
  }
  
  public SimpleInternalFrame(Icon paramIcon, String paramString, JToolBar paramJToolBar, JComponent paramJComponent)
  {
    super(new BorderLayout());
    this.titleLabel = new JLabel(paramString, paramIcon, 10);
    JPanel localJPanel = buildHeader(this.titleLabel, paramJToolBar);
    add(localJPanel, "North");
    if (paramJComponent != null) {
      setContent(paramJComponent);
    }
    setBorder(new ShadowBorder(null));
    setSelected(true);
    updateHeader();
  }
  
  public Icon getFrameIcon()
  {
    return this.titleLabel.getIcon();
  }
  
  public void setFrameIcon(Icon paramIcon)
  {
    Icon localIcon = getFrameIcon();
    this.titleLabel.setIcon(paramIcon);
    firePropertyChange("frameIcon", localIcon, paramIcon);
  }
  
  public String getTitle()
  {
    return this.titleLabel.getText();
  }
  
  public void setTitle(String paramString)
  {
    String str = getTitle();
    this.titleLabel.setText(paramString);
    firePropertyChange("title", str, paramString);
  }
  
  public JToolBar getToolBar()
  {
    return this.headerPanel.getComponentCount() > 1 ? (JToolBar)this.headerPanel.getComponent(1) : null;
  }
  
  public void setToolBar(JToolBar paramJToolBar)
  {
    JToolBar localJToolBar = getToolBar();
    if (localJToolBar == paramJToolBar) {
      return;
    }
    if (localJToolBar != null) {
      this.headerPanel.remove(localJToolBar);
    }
    if (paramJToolBar != null)
    {
      paramJToolBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      this.headerPanel.add(paramJToolBar, "East");
    }
    updateHeader();
    firePropertyChange("toolBar", localJToolBar, paramJToolBar);
  }
  
  public Component getContent()
  {
    return hasContent() ? getComponent(1) : null;
  }
  
  public void setContent(Component paramComponent)
  {
    Component localComponent = getContent();
    if (hasContent()) {
      remove(localComponent);
    }
    add(paramComponent, "Center");
    firePropertyChange("content", localComponent, paramComponent);
  }
  
  public boolean isSelected()
  {
    return this.isSelected;
  }
  
  public void setSelected(boolean paramBoolean)
  {
    boolean bool = isSelected();
    this.isSelected = paramBoolean;
    updateHeader();
    firePropertyChange("selected", bool, paramBoolean);
  }
  
  public void paintComponent(Graphics paramGraphics)
  {
    super.paintComponent(paramGraphics);
    if (!isOpaque()) {
      return;
    }
    Color localColor = UIManager.getColor("control");
    int i = getWidth();
    int j = getHeight();
    Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
    Paint localPaint = localGraphics2D.getPaint();
    localGraphics2D.setPaint(new GradientPaint(0.0F, 0.0F, Color.white, i, j, localColor));
    localGraphics2D.fillRect(0, 0, i, j);
    localGraphics2D.setPaint(localPaint);
  }
  
  private JPanel buildHeader(JLabel paramJLabel, JToolBar paramJToolBar)
  {
    this.gradientPanel = new GradientPanel(new BorderLayout(), getHeaderBackground(), null);
    paramJLabel.setOpaque(false);
    this.gradientPanel.add(paramJLabel, "West");
    this.gradientPanel.setBorder(BorderFactory.createEmptyBorder(3, 4, 3, 1));
    this.headerPanel = new JPanel(new BorderLayout());
    this.headerPanel.add(this.gradientPanel, "Center");
    setToolBar(paramJToolBar);
    this.headerPanel.setBorder(new RaisedHeaderBorder(null));
    this.headerPanel.setOpaque(false);
    return this.headerPanel;
  }
  
  private void updateHeader()
  {
    this.gradientPanel.setBackground(getHeaderBackground());
    this.gradientPanel.setOpaque(isSelected());
    this.titleLabel.setForeground(getTextForeground(isSelected()));
    this.headerPanel.repaint();
  }
  
  public void updateUI()
  {
    super.updateUI();
    if (this.titleLabel != null) {
      updateHeader();
    }
  }
  
  private boolean hasContent()
  {
    return getComponentCount() > 1;
  }
  
  protected Color getTextForeground(boolean paramBoolean)
  {
    Color localColor = UIManager.getColor(paramBoolean ? "SimpleInternalFrame.activeTitleForeground" : "SimpleInternalFrame.inactiveTitleForeground");
    if (localColor != null) {
      return localColor;
    }
    return UIManager.getColor(paramBoolean ? "InternalFrame.activeTitleForeground" : "Label.foreground");
  }
  
  protected Color getHeaderBackground()
  {
    Color localColor = UIManager.getColor("SimpleInternalFrame.activeTitleBackground");
    if (localColor != null) {
      return localColor;
    }
    if (LookUtils.IS_LAF_WINDOWS_XP_ENABLED) {
      localColor = UIManager.getColor("InternalFrame.activeTitleGradient");
    }
    return localColor != null ? localColor : UIManager.getColor("InternalFrame.activeTitleBackground");
  }
  
  private static class GradientPanel
    extends JPanel
  {
    private GradientPanel(LayoutManager paramLayoutManager, Color paramColor)
    {
      super();
      setBackground(paramColor);
    }
    
    public void paintComponent(Graphics paramGraphics)
    {
      super.paintComponent(paramGraphics);
      if (!isOpaque()) {
        return;
      }
      Color localColor = UIManager.getColor("control");
      int i = getWidth();
      int j = getHeight();
      Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
      Paint localPaint = localGraphics2D.getPaint();
      localGraphics2D.setPaint(new GradientPaint(0.0F, 0.0F, getBackground(), i, 0.0F, localColor));
      localGraphics2D.fillRect(0, 0, i, j);
      localGraphics2D.setPaint(localPaint);
    }
  }
  
  private static class ShadowBorder
    extends AbstractBorder
  {
    private static final Insets INSETS = new Insets(1, 1, 3, 3);
    
    public Insets getBorderInsets(Component paramComponent)
    {
      return INSETS;
    }
    
    public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      Color localColor1 = UIManager.getColor("controlShadow");
      if (localColor1 == null) {
        localColor1 = Color.GRAY;
      }
      Color localColor2 = new Color(localColor1.getRed(), localColor1.getGreen(), localColor1.getBlue(), 170);
      Color localColor3 = new Color(localColor1.getRed(), localColor1.getGreen(), localColor1.getBlue(), 70);
      paramGraphics.translate(paramInt1, paramInt2);
      paramGraphics.setColor(localColor1);
      paramGraphics.fillRect(0, 0, paramInt3 - 3, 1);
      paramGraphics.fillRect(0, 0, 1, paramInt4 - 3);
      paramGraphics.fillRect(paramInt3 - 3, 1, 1, paramInt4 - 3);
      paramGraphics.fillRect(1, paramInt4 - 3, paramInt3 - 3, 1);
      paramGraphics.setColor(localColor2);
      paramGraphics.fillRect(paramInt3 - 3, 0, 1, 1);
      paramGraphics.fillRect(0, paramInt4 - 3, 1, 1);
      paramGraphics.fillRect(paramInt3 - 2, 1, 1, paramInt4 - 3);
      paramGraphics.fillRect(1, paramInt4 - 2, paramInt3 - 3, 1);
      paramGraphics.setColor(localColor3);
      paramGraphics.fillRect(paramInt3 - 2, 0, 1, 1);
      paramGraphics.fillRect(0, paramInt4 - 2, 1, 1);
      paramGraphics.fillRect(paramInt3 - 2, paramInt4 - 2, 1, 1);
      paramGraphics.fillRect(paramInt3 - 1, 1, 1, paramInt4 - 2);
      paramGraphics.fillRect(1, paramInt4 - 1, paramInt3 - 2, 1);
      paramGraphics.translate(-paramInt1, -paramInt2);
    }
  }
  
  private static class RaisedHeaderBorder
    extends AbstractBorder
  {
    private static final Insets INSETS = new Insets(1, 1, 1, 0);
    
    public Insets getBorderInsets(Component paramComponent)
    {
      return INSETS;
    }
    
    public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      paramGraphics.translate(paramInt1, paramInt2);
      paramGraphics.setColor(UIManager.getColor("controlLtHighlight"));
      paramGraphics.fillRect(0, 0, paramInt3, 1);
      paramGraphics.fillRect(0, 1, 1, paramInt4 - 1);
      paramGraphics.setColor(UIManager.getColor("controlShadow"));
      paramGraphics.fillRect(0, paramInt4 - 1, paramInt3, 1);
      paramGraphics.translate(-paramInt1, -paramInt2);
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.ui.SimpleInternalFrame
 * JD-Core Version:    0.7.0.1
 */