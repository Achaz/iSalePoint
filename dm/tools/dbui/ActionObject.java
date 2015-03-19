package dm.tools.dbui;

import dm.tools.db.DBRow;

public abstract interface ActionObject
{
  public abstract void setInstance(DBRow paramDBRow);
  
  public abstract void setContainer(DBUIContainer paramDBUIContainer);
  
  public abstract void addDBUIActionListener(DBUIActionListener paramDBUIActionListener);
  
  public abstract byte getActionCode();
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.dbui.ActionObject
 * JD-Core Version:    0.7.0.1
 */