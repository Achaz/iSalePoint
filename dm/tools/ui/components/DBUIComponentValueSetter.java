package dm.tools.ui.components;

import dm.tools.db.DBRow;

public abstract interface DBUIComponentValueSetter
{
  public abstract void setValueToInstance(DBRow paramDBRow);
  
  public abstract void setInstance(DBRow paramDBRow);
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.ui.components.DBUIComponentValueSetter
 * JD-Core Version:    0.7.0.1
 */