package dm.tools.messages;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class MessageIf
{
  ArrayList<MessageAndKey> messages = new ArrayList();
  
  public MessageIf()
  {
    initMessages();
  }
  
  public abstract void initMessages();
  
  public String getMessage(int paramInt)
  {
    Iterator localIterator = this.messages.iterator();
    while (localIterator.hasNext())
    {
      MessageAndKey localMessageAndKey = (MessageAndKey)localIterator.next();
      if (localMessageAndKey.key == paramInt) {
        return localMessageAndKey.message;
      }
    }
    return null;
  }
  
  public void addMessage(String paramString, int paramInt)
  {
    MessageAndKey localMessageAndKey = new MessageAndKey();
    localMessageAndKey.key = paramInt;
    localMessageAndKey.message = paramString;
    this.messages.add(localMessageAndKey);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.messages.MessageIf
 * JD-Core Version:    0.7.0.1
 */