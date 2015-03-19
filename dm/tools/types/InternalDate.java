package dm.tools.types;

import java.text.SimpleDateFormat;

public class InternalDate
  extends java.util.Date
{
  private java.util.Date _mDate = null;
  private String _mPrefix = "";
  private String _mSufix = "";
  private boolean _mExpNA = false;
  private String _mDateFormat = null;
  
  public InternalDate(java.util.Date paramDate, String paramString1, String paramString2, String paramString3)
  {
    this._mPrefix = paramString1;
    this._mSufix = paramString2;
    this._mDate = paramDate;
    this._mDateFormat = paramString3;
  }
  
  public InternalDate(java.util.Date paramDate, boolean paramBoolean, String paramString1, String paramString2, String paramString3)
  {
    this._mPrefix = paramString1;
    this._mSufix = paramString2;
    this._mDate = paramDate;
    this._mExpNA = paramBoolean;
    this._mDateFormat = paramString3;
  }
  
  public InternalDate(java.util.Date paramDate, boolean paramBoolean, String paramString)
  {
    this._mDate = paramDate;
    this._mExpNA = paramBoolean;
    this._mDateFormat = paramString;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    if (this._mExpNA)
    {
      localStringBuffer.append(" NA ");
    }
    else
    {
      localStringBuffer.append(this._mPrefix);
      if (this._mDateFormat == null)
      {
        localStringBuffer.append(this._mDate.toString());
      }
      else
      {
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(this._mDateFormat);
        localStringBuffer.append(localSimpleDateFormat.format(this._mDate));
      }
      localStringBuffer.append(this._mSufix);
    }
    return localStringBuffer.toString();
  }
  
  public static java.sql.Date getSqlDate(java.util.Date paramDate)
  {
    if (paramDate == null) {
      return null;
    }
    return new java.sql.Date(paramDate.getTime());
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.types.InternalDate
 * JD-Core Version:    0.7.0.1
 */