package dm.tools.printing;

import javax.swing.JPanel;

public abstract class PrintingUIClassIf
  extends JPanel
{
  public abstract JPanel getConfigpanel();
  
  public abstract boolean isPrintingUIValid();
  
  public abstract boolean writeConfig();
  
  public abstract void readConfig();
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.printing.PrintingUIClassIf
 * JD-Core Version:    0.7.0.1
 */