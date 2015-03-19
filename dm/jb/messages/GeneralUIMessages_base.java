package dm.jb.messages;

import dm.tools.messages.MessageIf;

public class GeneralUIMessages_base
  extends MessageIf
{
  public static final int GENERAL_LABEL_WELCOME = 36865;
  public static final int RS = 36866;
  public static final int NUM = 36867;
  public static final int ADDRESS = 36868;
  public static final int NAME = 36869;
  public static final int CUSTOMER_ID = 36870;
  public static final int PHONE = 36871;
  
  public void initMessages()
  {
    addMessage("Welcome", 36865);
    addMessage("Rs", 36866);
    addMessage("No.", 36867);
    addMessage("Address", 36868);
    addMessage("Phone", 36871);
    addMessage("Name", 36869);
    addMessage("Customer ID", 36870);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.messages.GeneralUIMessages_base
 * JD-Core Version:    0.7.0.1
 */