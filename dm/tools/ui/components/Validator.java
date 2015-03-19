package dm.tools.ui.components;

import dm.tools.dbui.ValidationException;

public abstract interface Validator
{
  public abstract void validateValue()
    throws ValidationException;
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.ui.components.Validator
 * JD-Core Version:    0.7.0.1
 */