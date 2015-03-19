package dm.jb.ui.jasper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;

public class JBJasperCompiler
{
  private static JBJasperCompiler _mInstance = new JBJasperCompiler();
  
  public static JBJasperCompiler getInstance()
  {
    return _mInstance;
  }
  
  public boolean compile(String paramString1, String paramString2)
    throws JRException
  {
    JasperCompileManager.compileReportToFile(paramString1, paramString2);
    return true;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.jasper.JBJasperCompiler
 * JD-Core Version:    0.7.0.1
 */