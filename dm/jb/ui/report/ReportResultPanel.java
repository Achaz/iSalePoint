package dm.jb.ui.report;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JPanel;

public class ReportResultPanel
  extends JPanel
{
  public void setValues(Comparable[] paramArrayOfComparable, double[] paramArrayOfDouble, boolean paramBoolean, String paramString) {}
  
  public void setValuesInHashMap(HashMap<Comparable, Double> paramHashMap, boolean paramBoolean, String paramString)
  {
    Comparable[] arrayOfComparable = new Comparable[paramHashMap.size()];
    double[] arrayOfDouble = new double[paramHashMap.size()];
    Iterator localIterator = paramHashMap.keySet().iterator();
    for (int i = 0; localIterator.hasNext(); i++)
    {
      Comparable localComparable = (Comparable)localIterator.next();
      arrayOfComparable[i] = localComparable;
      arrayOfDouble[i] = ((Double)paramHashMap.get(localComparable)).doubleValue();
      if (arrayOfDouble[i] > 0.0D) {
        paramBoolean |= true;
      }
    }
    setValues(arrayOfComparable, arrayOfDouble, paramBoolean, paramString);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.report.ReportResultPanel
 * JD-Core Version:    0.7.0.1
 */