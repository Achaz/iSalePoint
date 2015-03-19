package dm.tools.dbui;

import dm.tools.db.DBException;
import dm.tools.db.DBRow;
import dm.tools.ui.components.Validator;

public abstract interface DBUIObject
  extends Validator
{
  public abstract void setInstance(DBRow paramDBRow);
  
  public abstract void setValueToInstance(DBRow paramDBRow);
  
  public abstract void initSelf()
    throws DBException;
  
  public abstract void setMandatory(boolean paramBoolean);
  
  public abstract void resetValue();
  
  public abstract boolean isResetAllowed();
  
  public abstract void setResetAllowed(boolean paramBoolean);
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.dbui.DBUIObject
 * JD-Core Version:    0.7.0.1
 */