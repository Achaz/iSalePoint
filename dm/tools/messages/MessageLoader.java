package dm.tools.messages;

import java.awt.Font;
import java.io.PrintStream;
import javax.swing.UIManager;

public class MessageLoader
{
  public String langCode = "_base";
  private ButtonLabels_base _mButtonLabelsInstance = null;
  
  public MessageLoader()
  {
    if ((this.langCode != null) && (this.langCode.equals("kn")))
    {
      UIManager.put("Label.font", new Font("Sampige", 0, 18));
      UIManager.put("Menu.font", new Font("Sampige", 0, 18));
      UIManager.put("MenuItem.font", new Font("Sampige", 0, 18));
      UIManager.put("ComboBox.font", new Font("Sampige", 0, 14));
      UIManager.put("Button.font", new Font("Kedage", 0, 14));
      UIManager.put("OptionPane.font", new Font("Sampige", 0, 18));
      UIManager.put("OptionPane.messageFont", new Font("Sampige", 0, 20));
    }
    else
    {
      this.langCode = "base";
    }
  }
  
  public ButtonLabels_base getButtonLabelsMessages()
  {
    if (this._mButtonLabelsInstance == null)
    {
      String str = "dm.tools.messages.ButtonLabels_" + this.langCode;
      try
      {
        this._mButtonLabelsInstance = ((ButtonLabels_base)Class.forName(str).newInstance());
      }
      catch (Exception localException)
      {
        System.err.println(localException);
      }
    }
    return this._mButtonLabelsInstance;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.messages.MessageLoader
 * JD-Core Version:    0.7.0.1
 */