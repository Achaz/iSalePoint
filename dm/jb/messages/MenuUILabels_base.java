package dm.jb.messages;

import dm.tools.messages.MessageIf;

public class MenuUILabels_base
  extends MessageIf
{
  public static final int MENU_LABEL_FILE = 131073;
  public static final int MENU_LABEL_FILE_EXIT = 131074;
  
  public void initMessages()
  {
    addMessage("File", 131073);
    addMessage("Exit", 131074);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.messages.MenuUILabels_base
 * JD-Core Version:    0.7.0.1
 */