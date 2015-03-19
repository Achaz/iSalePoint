package dm.tools.ui;

import com.toedter.calendar.JDateChooser;

public class InternalDateChooser
  extends JDateChooser
{
  private boolean _mEmptyAllowed = false;
  
  public InternalDateChooser(boolean paramBoolean)
  {
    this._mEmptyAllowed = paramBoolean;
  }
  
  public boolean emptyAllowed()
  {
    return this._mEmptyAllowed;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.ui.InternalDateChooser
 * JD-Core Version:    0.7.0.1
 */