package dm.tools.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public abstract class AbstractMainPanel
  extends JPanel
  implements MainPanelIf, KeyListener
{
  private ActionPanel _mActionPanel = null;
  public static Color backGroundColor = new Color(195, 217, 255);
  
  public AbstractMainPanel()
  {
    InputMap localInputMap = getInputMap(2);
    String str = "ESCAction";
    localInputMap.put(KeyStroke.getKeyStroke("ESCAPE"), str);
    AbstractAction local1 = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        AbstractMainPanel.this.closeWindow();
      }
    };
    getActionMap().put(str, local1);
  }
  
  public void setActionPanel(ActionPanel paramActionPanel)
  {
    this._mActionPanel = paramActionPanel;
  }
  
  public void closeWindow()
  {
    this._mActionPanel.popObject();
  }
  
  public void setTitle(String paramString)
  {
    if (this._mActionPanel != null) {
      this._mActionPanel.setTitle(paramString);
    }
  }
  
  public String getTitle()
  {
    return "<TITLE NOT ASSIGNED>";
  }
  
  public void windowClosed() {}
  
  public void keyTyped(KeyEvent paramKeyEvent) {}
  
  public void keyPressed(KeyEvent paramKeyEvent) {}
  
  public void keyReleased(KeyEvent paramKeyEvent) {}
  
  public void clearAllFields() {}
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.ui.AbstractMainPanel
 * JD-Core Version:    0.7.0.1
 */