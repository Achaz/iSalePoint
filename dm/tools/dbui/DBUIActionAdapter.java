package dm.tools.dbui;

public class DBUIActionAdapter
  implements DBUIActionListener
{
  public boolean beforeAction(ActionObject paramActionObject)
  {
    return true;
  }
  
  public void afterAction(ActionObject paramActionObject) {}
  
  public void actionFailed(Object paramObject) {}
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.dbui.DBUIActionAdapter
 * JD-Core Version:    0.7.0.1
 */