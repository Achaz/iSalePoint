package dm.jb.ui.common;

import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.JWindow;

public class JBNumTextFieldWithKeyPad
  extends JTextField
  implements FocusListener
{
  private JWindow _mParentWindowW = null;
  private JDialog _mParentWindowD = null;
  
  public JBNumTextFieldWithKeyPad(JWindow paramJWindow)
  {
    initUI();
    this._mParentWindowW = paramJWindow;
  }
  
  public JBNumTextFieldWithKeyPad(JDialog paramJDialog)
  {
    initUI();
    this._mParentWindowD = paramJDialog;
  }
  
  public JBNumTextFieldWithKeyPad()
  {
    this((JDialog)null);
  }
  
  private void initUI()
  {
    addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
      {
        if (paramAnonymousMouseEvent.getClickCount() != 2) {
          return;
        }
        JBNumTextFieldWithKeyPad.this.displayKeypad();
      }
    });
    addFocusListener(this);
  }
  
  private void displayKeypad()
  {
    Point localPoint = getLocationOnScreen();
    int i = localPoint.x + 35;
    int j = localPoint.y + getHeight();
    NumericVirtualKeyPad.getInstance().setVisible(false);
    NumericVirtualKeyPad localNumericVirtualKeyPad = null;
    if (this._mParentWindowW == null) {
      localNumericVirtualKeyPad = new NumericVirtualKeyPad(this._mParentWindowD);
    } else {
      localNumericVirtualKeyPad = new NumericVirtualKeyPad(this._mParentWindowW);
    }
    localNumericVirtualKeyPad.setTarget(this);
    localNumericVirtualKeyPad.setLocation(i, j);
    localNumericVirtualKeyPad.setAlwaysOnTop(true);
    localNumericVirtualKeyPad.setVisible(true);
  }
  
  public void focusGained(FocusEvent paramFocusEvent)
  {
    if (NumericVirtualKeyPad.getInstance().isVisible()) {
      NumericVirtualKeyPad.getInstance().setTarget(this);
    }
    super.requestFocusInWindow();
  }
  
  public void focusLost(FocusEvent paramFocusEvent)
  {
    if (NumericVirtualKeyPad.getInstance().isVisible()) {
      NumericVirtualKeyPad.getInstance().setVisible(false);
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.common.JBNumTextFieldWithKeyPad
 * JD-Core Version:    0.7.0.1
 */