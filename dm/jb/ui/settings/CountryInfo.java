package dm.jb.ui.settings;

import java.util.Locale;

public class CountryInfo
{
  public String name = null;
  public String currency = null;
  public Locale locale = null;
  
  public CountryInfo()
  {
    this.name = "";
    this.currency = "";
    this.locale = Locale.UK;
  }
  
  public CountryInfo(String paramString1, String paramString2, Locale paramLocale)
  {
    this.name = paramString1;
    this.currency = paramString2;
    this.locale = paramLocale;
  }
  
  public String toString()
  {
    return this.name;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.settings.CountryInfo
 * JD-Core Version:    0.7.0.1
 */