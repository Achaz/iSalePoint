package dm.jb.ui.common;

import dm.jb.ui.ISPHelpLauncher;
import dm.jb.ui.MainWindow;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jdesktop.swingx.JXButton;

public class HelpButton
  extends JXButton
  implements ActionListener
{
  private String _mKey = null;
  private boolean _mBig = false;
  
  public HelpButton(String paramString)
  {
    super("Help");
    this._mKey = paramString;
    addActionListener(this);
  }
  
  public HelpButton(String paramString, boolean paramBoolean)
  {
    super("Help");
    this._mKey = paramString;
    addActionListener(this);
    this._mBig = paramBoolean;
  }
  
  public void actionPerformed(ActionEvent paramActionEvent)
  {
    ISPHelpLauncher.INSTANCE.showHelp(this._mKey, MainWindow.instance);
  }
  
  public void setKey(String paramString)
  {
    this._mKey = paramString;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.common.HelpButton
 * JD-Core Version:    0.7.0.1
 */