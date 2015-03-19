package dm.tools.dbui;

import dm.tools.db.DBRow;

public abstract interface DBUIContainer
{
  public abstract DBRow getCurrentInstance();
  
  public abstract void setCurrentInstance(DBRow paramDBRow);
  
  public abstract boolean validateValues(int paramInt);
  
  public abstract void setValueToInstance(DBRow paramDBRow);
  
  public abstract DBRow createRow();
  
  public abstract void resetAttributes();
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.dbui.DBUIContainer
 * JD-Core Version:    0.7.0.1
 */