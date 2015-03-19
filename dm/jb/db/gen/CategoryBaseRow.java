package dm.jb.db.gen;

import dm.tools.db.DBRow;

public class CategoryBaseRow
  extends DBRow
{
  protected CategoryBaseRow(int paramInt, CategoryBaseTableDef paramCategoryBaseTableDef)
  {
    super(paramInt, paramCategoryBaseTableDef);
  }
  
  public void setValues(int paramInt1, String paramString1, String paramString2, double paramDouble1, double paramDouble2, int paramInt2)
  {
    setDeptIndex(paramInt1);
    setCatName(paramString1);
    setCatDetails(paramString2);
    setDiscount(paramDouble1);
    setTax(paramDouble2);
    setTaxUnit(paramInt2);
  }
  
  public void setCatIndex(int paramInt)
  {
    setValue("CAT_INDEX", Integer.valueOf(paramInt));
  }
  
  public int getCatIndex()
  {
    return ((Integer)getValue("CAT_INDEX")).intValue();
  }
  
  public void setDeptIndex(int paramInt)
  {
    setValue("DEPT_INDEX", Integer.valueOf(paramInt));
  }
  
  public int getDeptIndex()
  {
    return ((Integer)getValue("DEPT_INDEX")).intValue();
  }
  
  public void setCatName(String paramString)
  {
    setValue("CAT_NAME", paramString);
  }
  
  public String getCatName()
  {
    return (String)getValue("CAT_NAME");
  }
  
  public void setCatDetails(String paramString)
  {
    setValue("CAT_DETAILS", paramString);
  }
  
  public String getCatDetails()
  {
    return (String)getValue("CAT_DETAILS");
  }
  
  public void setDiscount(double paramDouble)
  {
    setValue("DISCOUNT", Double.valueOf(paramDouble));
  }
  
  public double getDiscount()
  {
    return ((Double)getValue("DISCOUNT")).doubleValue();
  }
  
  public void setTax(double paramDouble)
  {
    setValue("TAX", Double.valueOf(paramDouble));
  }
  
  public double getTax()
  {
    return ((Double)getValue("TAX")).doubleValue();
  }
  
  public void setTaxUnit(int paramInt)
  {
    setValue("TAX_UNIT", Integer.valueOf(paramInt));
  }
  
  public int getTaxUnit()
  {
    return ((Integer)getValue("TAX_UNIT")).intValue();
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.gen.CategoryBaseRow
 * JD-Core Version:    0.7.0.1
 */