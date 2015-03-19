package dm.tools.ui;

import javax.swing.JPanel;

public abstract class WizardPanelBase
  extends JPanel
{
  private WizardBase _mParent = null;
  
  public WizardPanelBase(WizardBase paramWizardBase)
  {
    this._mParent = paramWizardBase;
    initWizardPanel();
  }
  
  private void initWizardPanel() {}
  
  public WizardBase getParentWizard()
  {
    return this._mParent;
  }
  
  public abstract boolean isPageValid();
  
  public String getTitle()
  {
    return "<Title not defined>";
  }
  
  public void pageDisplayed() {}
  
  public void setFailedCompFocus() {}
  
  public boolean leaveForNext()
  {
    return true;
  }
  
  public boolean leaveForPrev()
  {
    return true;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.ui.WizardPanelBase
 * JD-Core Version:    0.7.0.1
 */