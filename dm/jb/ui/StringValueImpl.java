package dm.jb.ui;

import org.jdesktop.swingx.renderer.StringValue;

public class StringValueImpl
  implements StringValue
{
  public String getString(Object paramObject)
  {
    return paramObject.toString();
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.StringValueImpl
 * JD-Core Version:    0.7.0.1
 */