package dm.tools.printing;

import dm.jb.printing.JBPrinter;

public class PrinterType
{
  private String _mName = null;
  private String _mClassName = null;
  private JBPrinter _mPrintingClassInstance = null;
  private String _mPriterUIClassName = null;
  private PrintingUIClassIf _mPrinterConfigUI = null;
  private int _mCode = -1;
  public String helpCode = null;
  
  public PrinterType(String paramString1, String paramString2, int paramInt, String paramString3, String paramString4)
  {
    this._mName = paramString1;
    this._mClassName = paramString2;
    this._mCode = paramInt;
    this._mPriterUIClassName = paramString3;
    this.helpCode = paramString4;
  }
  
  public String toString()
  {
    return this._mName;
  }
  
  public int getCode()
  {
    return this._mCode;
  }
  
  public String getName()
  {
    return this._mName;
  }
  
  public JBPrinter getPrintingClassInstance()
  {
    try
    {
      this._mPrintingClassInstance = ((JBPrinter)Class.forName(this._mClassName).newInstance());
      return this._mPrintingClassInstance;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      return null;
    }
    catch (InstantiationException localInstantiationException)
    {
      return null;
    }
    catch (IllegalAccessException localIllegalAccessException) {}
    return null;
  }
  
  public PrintingUIClassIf getPrintingUIClassInstance()
  {
    if (this._mPrinterConfigUI != null) {
      return this._mPrinterConfigUI;
    }
    try
    {
      Object localObject = Class.forName(this._mPriterUIClassName).newInstance();
      if ((localObject instanceof PrintingUIClassIf))
      {
        this._mPrinterConfigUI = ((PrintingUIClassIf)localObject);
        return this._mPrinterConfigUI;
      }
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      return null;
    }
    catch (InstantiationException localInstantiationException)
    {
      return null;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      return null;
    }
    return null;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.printing.PrinterType
 * JD-Core Version:    0.7.0.1
 */