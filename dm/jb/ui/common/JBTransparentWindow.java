package dm.jb.ui.common;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.PrintStream;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class JBTransparentWindow
  extends JDialog
{
  BufferedImage capturedImage = null;
  private Robot _mRobot = null;
  
  public JBTransparentWindow(JFrame paramJFrame)
  {
    this(paramJFrame, true);
  }
  
  public JBTransparentWindow(JFrame paramJFrame, boolean paramBoolean)
  {
    super(paramJFrame, paramBoolean);
    setUndecorated(true);
    initUI();
  }
  
  public JBTransparentWindow(JDialog paramJDialog, boolean paramBoolean)
  {
    super(paramJDialog, paramBoolean);
    setUndecorated(true);
    initUI();
  }
  
  private void initUI()
  {
    JPanel local1 = new JPanel()
    {
      public void paintComponent(Graphics paramAnonymousGraphics)
      {
        JBTransparentWindow.this.paintThisPanel((Graphics2D)paramAnonymousGraphics);
      }
    };
    setContentPane(local1);
  }
  
  private void paintThisPanel(Graphics2D paramGraphics2D)
  {
    paramGraphics2D.drawImage(this.capturedImage, 0, 0, null);
  }
  
  public void captureImage()
  {
    Rectangle localRectangle = getBounds();
    try
    {
      if (this._mRobot == null) {
        this._mRobot = new Robot(getGraphicsConfiguration().getDevice());
      }
      this.capturedImage = this._mRobot.createScreenCapture(new Rectangle(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height));
    }
    catch (Exception localException)
    {
      System.err.println(localException);
    }
  }
  
  public void setBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.setBounds(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public void setVisible(boolean paramBoolean)
  {
    captureImage();
    super.setVisible(paramBoolean);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.common.JBTransparentWindow
 * JD-Core Version:    0.7.0.1
 */