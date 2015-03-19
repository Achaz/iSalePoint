package dm.tools.dbui;

import dm.tools.db.DBRow;
import dm.tools.ui.components.Validator;

public abstract interface StaticListComponent
  extends Validator
{
  public abstract void setValueToRow(DBRow paramDBRow);
  
  public abstract void setValueFromRow(DBRow paramDBRow);
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.dbui.StaticListComponent
 * JD-Core Version:    0.7.0.1
 */