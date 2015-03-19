package dm.tools.ui;

import dm.tools.utils.Stack;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class ActionPanel
  extends SimpleInternalFrame
{
  private JComponent _mCurrentComponent = null;
  private Stack<JComponent> _mCompStack = new Stack();
  private Stack<String> _mTitles = new Stack();
  private String _mCurrentTitle = "";
  
  public ActionPanel()
  {
    super("Action Panel");
  }
  
  public void setTitle(String paramString)
  {
    this._mCurrentTitle = paramString;
    super.setTitle(paramString);
  }
  
  public void cleanPush(JComponent paramJComponent)
  {
    popAll();
    pushObject(paramJComponent);
  }
  
  public void pushObject(JComponent paramJComponent)
  {
    if (this._mCompStack.contains(paramJComponent)) {
      return;
    }
    if (this._mCurrentComponent != null)
    {
      this._mCompStack.push(this._mCurrentComponent);
      this._mTitles.push(this._mCurrentTitle);
      this._mCurrentComponent.setVisible(false);
      super.remove(this._mCurrentComponent);
    }
    String str = "";
    if ((paramJComponent instanceof AbstractMainPanel)) {
      str = ((AbstractMainPanel)paramJComponent).getTitle();
    }
    setObject(paramJComponent, str);
  }
  
  public JComponent popObject()
  {
    setVisible(false);
    if (this._mCurrentComponent != null)
    {
      this._mCurrentComponent.setVisible(false);
      super.remove(this._mCurrentComponent);
      if ((this._mCurrentComponent instanceof AbstractMainPanel)) {
        ((AbstractMainPanel)this._mCurrentComponent).windowClosed();
      }
      this._mCurrentComponent = null;
    }
    if (this._mCompStack.isEmpty())
    {
      setVisible(false);
      return null;
    }
    JComponent localJComponent = (JComponent)this._mCompStack.pop();
    String str = (String)this._mTitles.pop();
    setObject(localJComponent, str);
    return localJComponent;
  }
  
  private void setObject(JComponent paramJComponent, String paramString)
  {
    super.setContent(paramJComponent);
    this._mCurrentComponent = paramJComponent;
    this._mCurrentComponent.setVisible(true);
    setVisible(true);
    this._mCurrentTitle = paramString;
    setTitle(paramString);
    if ((this._mCurrentComponent instanceof MainPanelIf))
    {
      ((MainPanelIf)this._mCurrentComponent).windowDisplayed();
      ((MainPanelIf)this._mCurrentComponent).setDefaultFocus();
    }
  }
  
  public JPanel getCurrentPanel()
  {
    return (JPanel)this._mCurrentComponent;
  }
  
  public void popAll()
  {
    while (!this._mCompStack.isEmpty()) {
      this._mCurrentComponent = popObject();
    }
    this._mCurrentComponent = null;
    this._mCompStack.popAll();
    this._mTitles.popAll();
    this._mCurrentTitle = "";
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.ui.ActionPanel
 * JD-Core Version:    0.7.0.1
 */