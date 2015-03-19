package dm.jb.db.gen;

import dm.tools.db.DBRow;

public class SelectedWhtostoreBaseRow
  extends DBRow
{
  protected SelectedWhtostoreBaseRow(int paramInt, SelectedWhtostoreBaseTableDef paramSelectedWhtostoreBaseTableDef)
  {
    super(paramInt, paramSelectedWhtostoreBaseTableDef);
  }
  
  public void setValues(int paramInt1, int paramInt2, String paramString)
  {
    setTxnNo(paramInt1);
    setSlNo(paramInt2);
    setSelected(paramString);
  }
  
  public void setTxnNo(int paramInt)
  {
    setValue("TXN_NO", Integer.valueOf(paramInt));
  }
  
  public int getTxnNo()
  {
    return ((Integer)getValue("TXN_NO")).intValue();
  }
  
  public void setSlNo(int paramInt)
  {
    setValue("SL_NO", Integer.valueOf(paramInt));
  }
  
  public int getSlNo()
  {
    return ((Integer)getValue("SL_NO")).intValue();
  }
  
  public void setSelected(String paramString)
  {
    setValue("SELECTED", paramString);
  }
  
  public String getSelected()
  {
    return (String)getValue("SELECTED");
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.SelectedWhtostoreBaseRow
 * JD-Core Version:    0.7.0.1
 */