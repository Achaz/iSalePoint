package dm.jb.messages;

import dm.tools.messages.MessageIf;

public class ProductMessages_base
  extends MessageIf
{
  public static final int PRODUCT = 131073;
  public static final int PRODUCT_NAME = 131074;
  public static final int VENDOR = 131075;
  public static final int QUANTITY = 131076;
  public static final int PRODUCT_ID = 131077;
  public static final int STOCK_INDEX = 131078;
  public static final int STOCK_DATE = 131079;
  public static final int EXPIRY = 131080;
  public static final int PURCHASE_PRICE = 131081;
  public static final int SEL_CAT_FOR_CUST_STOCK_REPORT = 131082;
  public static final int SEL_DEPT_FOR_CUST_STOCK_REPORT = 131083;
  public static final int PRICE = 131084;
  
  public void initMessages()
  {
    addMessage("Product", 131073);
    addMessage("Product Name", 131074);
    addMessage("Vendor", 131075);
    addMessage("Quantity", 131076);
    addMessage("Product Id", 131077);
    addMessage("Stock Id", 131078);
    addMessage("Stock Date", 131079);
    addMessage("Expiry", 131080);
    addMessage("Purchase Price", 131081);
    addMessage("Select category for the report", 131082);
    addMessage("Select departments for the report", 131083);
    addMessage("Price", 131084);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.messages.ProductMessages_base
 * JD-Core Version:    0.7.0.1
 */