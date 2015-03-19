package dm.tools.dbui;

public abstract interface DBUIActionListener
{
  public abstract boolean beforeAction(ActionObject paramActionObject);
  
  public abstract void afterAction(ActionObject paramActionObject);
  
  public abstract void actionFailed(Object paramObject);
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.dbui.DBUIActionListener
 * JD-Core Version:    0.7.0.1
 */