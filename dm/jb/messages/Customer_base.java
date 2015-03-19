package dm.jb.messages;

import dm.tools.messages.MessageIf;

public class Customer_base
  extends MessageIf
{
  public static final int ERROR_MESSAGE_CUSTOMER_ID_EMPTY = 139281;
  public static final int BUTTON_LABEL_PREVIOUS = 139282;
  
  public void initMessages()
  {
    addMessage("Customer Id cannot be empty.", 139281);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.messages.Customer_base
 * JD-Core Version:    0.7.0.1
 */