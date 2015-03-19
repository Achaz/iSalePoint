package dm.jb.ui.common;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Robot;
import java.awt.Window;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JWindow;

public class NumericVirtualKeyPad
  extends JWindow
{
  private static NumericVirtualKeyPad INSTANCE = null;
  static Image BUTTON_BG = null;
  static Image BUTTON_BG_P = null;
  static Font FONT = null;
  private JComponent _mTarget = null;
  
  public NumericVirtualKeyPad(Window paramWindow)
  {
    super(paramWindow);
    setAlwaysOnTop(true);
    try
    {
      BUTTON_BG = ImageIO.read(getClass().getResourceAsStream("/dm/jb/images/btnbg.png"));
      BUTTON_BG_P = ImageIO.read(getClass().getResourceAsStream("/dm/jb/images/btnbg_p.png"));
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    initUI();
    pack();
    INSTANCE = this;
  }
  
  public NumericVirtualKeyPad(Frame paramFrame)
  {
    super(paramFrame);
    setAlwaysOnTop(true);
    try
    {
      BUTTON_BG = ImageIO.read(getClass().getResourceAsStream("/dm/jb/images/btnbg.png"));
      BUTTON_BG_P = ImageIO.read(getClass().getResourceAsStream("/dm/jb/images/btnbg_p.png"));
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    initUI();
    pack();
    INSTANCE = this;
  }
  
  public void setParentWindow(JWindow paramJWindow) {}
  
  public void setParentWindow(JDialog paramJDialog) {}
  
  public static NumericVirtualKeyPad getInstance()
  {
    if (INSTANCE == null) {
      INSTANCE = new NumericVirtualKeyPad((Window)null);
    }
    return INSTANCE;
  }
  
  private void initUI()
  {
    JPanel localJPanel = (JPanel)getContentPane();
    localJPanel.setBackground(new Color(162, 195, 255));
    FormLayout localFormLayout = new FormLayout("10px,60px,5px,60px,5px,60px,10px", "10px,60px,5px,60px,5px,60px,5px,60px,10px,0px,0px");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(2, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(new KeyButton("7", 55), localCellConstraints);
    localCellConstraints.xywh(4, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(new KeyButton("8", 56), localCellConstraints);
    localCellConstraints.xywh(6, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(new KeyButton("9", 57), localCellConstraints);
    localCellConstraints.xywh(2, 4, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(new KeyButton("4", 52), localCellConstraints);
    localCellConstraints.xywh(4, 4, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(new KeyButton("5", 53), localCellConstraints);
    localCellConstraints.xywh(6, 4, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(new KeyButton("6", 54), localCellConstraints);
    localCellConstraints.xywh(2, 6, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(new KeyButton("1", 49), localCellConstraints);
    localCellConstraints.xywh(4, 6, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(new KeyButton("2", 50), localCellConstraints);
    localCellConstraints.xywh(6, 6, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(new KeyButton("3", 51), localCellConstraints);
    localCellConstraints.xywh(2, 8, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(new KeyButton("0", 48), localCellConstraints);
    localCellConstraints.xywh(4, 8, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(new KeyButton("del", 8), localCellConstraints);
    localCellConstraints.xywh(6, 8, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(new KeyButton("ent", 10), localCellConstraints);
  }
  
  void sendKeyEvent(int paramInt)
  {
    this._mTarget.requestFocusInWindow();
    Robot localRobot = null;
    try
    {
      localRobot = new Robot();
    }
    catch (AWTException localAWTException)
    {
      localAWTException.printStackTrace();
      return;
    }
    this._mTarget.requestFocusInWindow();
    if (paramInt != 0) {
      localRobot.keyPress(paramInt);
    } else {
      localRobot.keyPress(paramInt);
    }
  }
  
  public void paint(Graphics paramGraphics)
  {
    FONT = paramGraphics.getFont();
    FONT = new Font(FONT.getName(), 1, 20);
    super.paint(paramGraphics);
    paramGraphics.setColor(new Color(50, 106, 208));
    paramGraphics.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    paramGraphics.drawRect(1, 1, getWidth() - 3, getHeight() - 3);
    paramGraphics.drawRect(2, 2, getWidth() - 5, getHeight() - 5);
    paramGraphics.setColor(Color.WHITE);
    paramGraphics.drawRect(3, 3, getWidth() - 7, getHeight() - 7);
  }
  
  public void setTarget(JComponent paramJComponent)
  {
    this._mTarget = paramJComponent;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.common.NumericVirtualKeyPad
 * JD-Core Version:    0.7.0.1
 */