package dm.jb.db.gen;

import dm.tools.db.DBRow;

public class DeptBaseRow
  extends DBRow
{
  protected DeptBaseRow(int paramInt, DeptBaseTableDef paramDeptBaseTableDef)
  {
    super(paramInt, paramDeptBaseTableDef);
  }
  
  public void setValues(String paramString1, String paramString2)
  {
    setDeptName(paramString1);
    setDeptDetails(paramString2);
  }
  
  public void setDeptIndex(int paramInt)
  {
    setValue("DEPT_INDEX", Integer.valueOf(paramInt));
  }
  
  public int getDeptIndex()
  {
    return ((Integer)getValue("DEPT_INDEX")).intValue();
  }
  
  public void setDeptName(String paramString)
  {
    setValue("DEPT_NAME", paramString);
  }
  
  public String getDeptName()
  {
    return (String)getValue("DEPT_NAME");
  }
  
  public void setDeptDetails(String paramString)
  {
    setValue("DEPT_DETAILS", paramString);
  }
  
  public String getDeptDetails()
  {
    return (String)getValue("DEPT_DETAILS");
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.DeptBaseRow
 * JD-Core Version:    0.7.0.1
 */