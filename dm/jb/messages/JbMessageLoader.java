package dm.jb.messages;

import dm.tools.messages.MessageLoader;
import java.io.PrintStream;

public class JbMessageLoader
  extends MessageLoader
{
  private MenuUILabels_base _mInstMenuUILabels = null;
  private GeneralUIMessages_base _mInstGeneralUILabels = null;
  private ProductMessages_base _mInstProductLabels = null;
  private Customer_base _mCustomerLabelInstance = null;
  
  public GeneralUIMessages_base getGeneralUILabelsMessages()
  {
    if (this._mInstGeneralUILabels == null)
    {
      String str = "dm.jb.messages.GeneralUIMessages_" + this.langCode;
      try
      {
        this._mInstGeneralUILabels = ((GeneralUIMessages_base)Class.forName(str).newInstance());
      }
      catch (Exception localException)
      {
        System.err.println(localException);
      }
    }
    return this._mInstGeneralUILabels;
  }
  
  public ProductMessages_base getProductLabelsMessages()
  {
    if (this._mInstGeneralUILabels == null)
    {
      String str = "dm.jb.messages.ProductMessages_" + this.langCode;
      try
      {
        this._mInstProductLabels = ((ProductMessages_base)Class.forName(str).newInstance());
      }
      catch (Exception localException)
      {
        System.err.println(localException);
      }
    }
    return this._mInstProductLabels;
  }
  
  public MenuUILabels_base getMenuUILabelsMessages()
  {
    if (this._mInstMenuUILabels == null)
    {
      String str = "dm.jb.messages.MenuUILabels_" + this.langCode;
      try
      {
        this._mInstMenuUILabels = ((MenuUILabels_base)Class.forName(str).newInstance());
      }
      catch (Exception localException)
      {
        System.err.println(localException);
      }
    }
    return this._mInstMenuUILabels;
  }
  
  public Customer_base getCustomerMessages()
  {
    if (this._mCustomerLabelInstance == null)
    {
      String str = "dm.jb.messages.Customer_" + this.langCode;
      try
      {
        this._mCustomerLabelInstance = ((Customer_base)Class.forName(str).newInstance());
      }
      catch (Exception localException)
      {
        System.err.println(localException);
      }
    }
    return this._mCustomerLabelInstance;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.messages.JbMessageLoader
 * JD-Core Version:    0.7.0.1
 */