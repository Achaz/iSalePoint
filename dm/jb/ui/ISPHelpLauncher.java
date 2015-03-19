package dm.jb.ui;

import dm.helpui.HelpException;
import dm.helpui.HelpLauncher;
import javax.swing.JFrame;

public class ISPHelpLauncher
{
  public static final ISPHelpLauncher INSTANCE = new ISPHelpLauncher();
  
  public void initHelp()
    throws HelpException
  {
    HelpLauncher.initHelp("iSalePoint.jhlp");
  }
  
  public void showHelp(String paramString, JFrame paramJFrame)
  {
    HelpLauncher.launchHelp(paramString);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.ISPHelpLauncher
 * JD-Core Version:    0.7.0.1
 */