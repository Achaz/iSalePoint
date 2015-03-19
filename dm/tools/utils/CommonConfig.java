package dm.tools.utils;

import dm.jb.ui.settings.CountryInfo;
import dm.tools.db.DBConnection;
import dm.tools.db.DBException;
import dm.tools.db.Db;
import dm.tools.printing.PrinterType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class CommonConfig
{
  private static CommonConfig _mInstance = new CommonConfig();
  private ArrayList<CommonConfigChangeListener> _mListenerList = null;
  public long timeZoneOffSet = 19800000L;
  public String dateFormat = "dd-MMM-yyyy";
  public ArrayList<PrinterType> printerTypes = null;
  public CountryInfo country = new CountryInfo();
  public boolean finalTax = false;
  public double finalTaxAmount = 0.0D;
  public int customerOption = 0;
  public boolean enableRFID = false;
  public int roundingOption = 3;
  public int redemptionOption = 0;
  public double redemptionAmount = 100.0D;
  public int loyaltyOption = 0;
  public double loyaltyAmount = 1.0D;
  
  private CommonConfig()
  {
    initPrinterList();
  }
  
  private void initPrinterList()
  {
    this.printerTypes = new ArrayList();
  }
  
  public void addPrinterType(PrinterType paramPrinterType)
  {
    this.printerTypes.add(paramPrinterType);
  }
  
  public PrinterType getPrinerTypeByName(String paramString)
  {
    Iterator localIterator = this.printerTypes.iterator();
    while (localIterator.hasNext())
    {
      PrinterType localPrinterType = (PrinterType)localIterator.next();
      if (localPrinterType.getName().equalsIgnoreCase(paramString)) {
        return localPrinterType;
      }
    }
    return null;
  }
  
  public static CommonConfig getInstance()
  {
    return _mInstance;
  }
  
  public void save()
    throws DBException
  {
    PreparedStatement localPreparedStatement = Db.getConnection().createPreparedStatement("UPDATE CONFIG_INFO SET VALUE=? WHERE NAME=?");
    try
    {
      Db.getConnection().openTrans();
      setCurrencyInDB(localPreparedStatement);
      setDateFormatInDB(localPreparedStatement);
      setCountryInDB(localPreparedStatement);
      setRFIDOPtionInDB(localPreparedStatement);
      setFinalTaxInDB(localPreparedStatement);
      setFinalTaxAmountInDB(localPreparedStatement);
      setCustomerRequired(localPreparedStatement);
      setRoundingOptionInDB(localPreparedStatement);
      setRedemptionOptionInDB(localPreparedStatement);
      setLoyaltyOptionInDB(localPreparedStatement);
      Db.getConnection().endTrans();
      Iterator localIterator = this._mListenerList.iterator();
      while (localIterator.hasNext())
      {
        CommonConfigChangeListener localCommonConfigChangeListener = (CommonConfigChangeListener)localIterator.next();
        localCommonConfigChangeListener.setConfigValues();
      }
    }
    catch (SQLException localSQLException)
    {
      Db.getConnection().rollbackNoExp();
      throw new DBException("SQLException saving config information", "SQLException", "Contact administrator", null, localSQLException);
    }
  }
  
  public void load()
    throws DBException
  {
    try
    {
      PreparedStatement localPreparedStatement = Db.getConnection().createPreparedStatement("SELECT NAME, VALUE FROM CONFIG_INFO");
      ResultSet localResultSet = localPreparedStatement.executeQuery();
      boolean bool = localResultSet.next();
      Object localObject2;
      while (bool)
      {
        localObject1 = localResultSet.getString(1);
        localObject2 = localResultSet.getString(2);
        bool = localResultSet.next();
        setConfigValue((String)localObject1, (String)localObject2);
      }
      Object localObject1 = this._mListenerList.iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject2 = (CommonConfigChangeListener)((Iterator)localObject1).next();
        ((CommonConfigChangeListener)localObject2).setConfigValues();
      }
    }
    catch (SQLException localSQLException)
    {
      throw new DBException("SQLException reading config information.", "SQLException", "Contact administrator", null, localSQLException);
    }
  }
  
  private void setConfigValue(String paramString1, String paramString2)
  {
    if (paramString1.equalsIgnoreCase("country.name"))
    {
      this.country.name = paramString2;
      CountryInfo localCountryInfo = findCountryWithName(paramString2);
      this.country.locale = localCountryInfo.locale;
      MyFormatter.init(this.country);
    }
    else if (paramString1.equalsIgnoreCase("country.currency"))
    {
      this.country.currency = paramString2;
    }
    else if (paramString1.equalsIgnoreCase("dateformat"))
    {
      this.dateFormat = paramString2;
    }
    else if (paramString1.equalsIgnoreCase("FinalTax"))
    {
      this.finalTax = paramString2.equals("1");
    }
    else if (paramString1.equalsIgnoreCase("FinalTaxAmount"))
    {
      this.finalTaxAmount = Double.valueOf(paramString2).doubleValue();
    }
    else if (paramString1.equalsIgnoreCase("CustomerRequired"))
    {
      this.customerOption = Integer.valueOf(paramString2).intValue();
    }
    else if (paramString1.equalsIgnoreCase("RoundingOption"))
    {
      this.roundingOption = Integer.valueOf(paramString2).intValue();
    }
    else if (paramString1.equalsIgnoreCase("RedemptionOption"))
    {
      this.redemptionOption = Integer.valueOf(paramString2).intValue();
    }
    else if (paramString1.equalsIgnoreCase("RedemptionAmount"))
    {
      this.redemptionAmount = Double.valueOf(paramString2).doubleValue();
    }
    else if (paramString1.equalsIgnoreCase("LoyaltyOption"))
    {
      this.loyaltyOption = Integer.valueOf(paramString2).intValue();
    }
    else if (paramString1.equalsIgnoreCase("LoyaltyAmount"))
    {
      this.loyaltyAmount = Double.valueOf(paramString2).doubleValue();
    }
    else if (paramString1.equalsIgnoreCase("EnableRFID"))
    {
      this.enableRFID = paramString2.equalsIgnoreCase("Y");
    }
  }
  
  private void setCurrencyInDB(PreparedStatement paramPreparedStatement)
    throws SQLException
  {
    paramPreparedStatement.setObject(1, this.country.currency);
    paramPreparedStatement.setObject(2, "country.currency");
    paramPreparedStatement.executeUpdate();
  }
  
  private void setDateFormatInDB(PreparedStatement paramPreparedStatement)
    throws SQLException
  {
    paramPreparedStatement.setObject(1, this.dateFormat);
    paramPreparedStatement.setObject(2, "dateformat");
    paramPreparedStatement.executeUpdate();
  }
  
  private void setCountryInDB(PreparedStatement paramPreparedStatement)
    throws SQLException
  {
    paramPreparedStatement.setObject(1, this.country.name);
    paramPreparedStatement.setObject(2, "country.name");
    paramPreparedStatement.executeUpdate();
  }
  
  private void setFinalTaxInDB(PreparedStatement paramPreparedStatement)
    throws SQLException
  {
    paramPreparedStatement.setObject(1, this.finalTax ? "1" : "0");
    paramPreparedStatement.setObject(2, "FinalTax");
    paramPreparedStatement.executeUpdate();
  }
  
  private void setFinalTaxAmountInDB(PreparedStatement paramPreparedStatement)
    throws SQLException
  {
    paramPreparedStatement.setObject(1, Double.valueOf(this.finalTaxAmount));
    paramPreparedStatement.setObject(2, "FinalTaxAmount");
    paramPreparedStatement.executeUpdate();
  }
  
  private void setRoundingOptionInDB(PreparedStatement paramPreparedStatement)
    throws SQLException
  {
    paramPreparedStatement.setObject(1, "" + this.roundingOption + "");
    paramPreparedStatement.setObject(2, "RoundingOption");
    paramPreparedStatement.executeUpdate();
  }
  
  private void setRFIDOPtionInDB(PreparedStatement paramPreparedStatement)
    throws SQLException
  {
    paramPreparedStatement.setObject(1, this.enableRFID ? "Y" : "N");
    paramPreparedStatement.setObject(2, "EnableRFID");
    paramPreparedStatement.executeUpdate();
  }
  
  private void setLoyaltyOptionInDB(PreparedStatement paramPreparedStatement)
    throws SQLException
  {
    paramPreparedStatement.setObject(1, "" + this.loyaltyOption + "");
    paramPreparedStatement.setObject(2, "LoyaltyOption");
    paramPreparedStatement.executeUpdate();
    paramPreparedStatement.setObject(1, "" + this.loyaltyAmount + "");
    paramPreparedStatement.setObject(2, "LoyaltyAmount");
    paramPreparedStatement.executeUpdate();
  }
  
  private void setRedemptionOptionInDB(PreparedStatement paramPreparedStatement)
    throws SQLException
  {
    paramPreparedStatement.setObject(1, "" + this.redemptionOption + "");
    paramPreparedStatement.setObject(2, "RedemptionOption");
    paramPreparedStatement.executeUpdate();
    paramPreparedStatement.setObject(1, "" + this.redemptionAmount + "");
    paramPreparedStatement.setObject(2, "RedemptionAmount");
    paramPreparedStatement.executeUpdate();
  }
  
  private void setCustomerRequired(PreparedStatement paramPreparedStatement)
    throws SQLException
  {
    paramPreparedStatement.setObject(1, Integer.valueOf(this.customerOption));
    paramPreparedStatement.setObject(2, "CustomerRequired");
    paramPreparedStatement.executeUpdate();
  }
  
  public void addChangeListener(CommonConfigChangeListener paramCommonConfigChangeListener)
  {
    this._mListenerList.add(paramCommonConfigChangeListener);
  }
  
  private CountryInfo findCountryWithName(String paramString)
  {
    for (int i = 0; i < CountrySettings.CountryInfos.length; i++) {
      if (CountrySettings.CountryInfos[i].name.equalsIgnoreCase(paramString)) {
        return CountrySettings.CountryInfos[i];
      }
    }
    return new CountryInfo();
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.utils.CommonConfig
 * JD-Core Version:    0.7.0.1
 */