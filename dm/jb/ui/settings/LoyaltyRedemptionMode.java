package dm.jb.ui.settings;

import dm.jb.ui.res.ResourceUtils;

public class LoyaltyRedemptionMode
{
  public static final int REDEMPTION_MODE_ENTIRE_BILL = 0;
  public static final int REDEMPTION_MODE_PRODUCT = 1;
  public static final int REDEMPTION_MODE_NONE = 2;
  public static final LoyaltyRedemptionMode[] redemptionModes = { new LoyaltyRedemptionMode(ResourceUtils.getString("LOYALTY_MODE_PER_TXN"), 0), new LoyaltyRedemptionMode(ResourceUtils.getString("LOYALTY_MODE_PER_PRODUCT"), 1), new LoyaltyRedemptionMode(ResourceUtils.getString("LOYALTY_MODE_NONE"), 2) };
  private String _mMode = null;
  private int _mCode = -1;
  
  public LoyaltyRedemptionMode(String paramString, int paramInt)
  {
    this._mMode = paramString;
    this._mCode = paramInt;
  }
  
  public int getCode()
  {
    return this._mCode;
  }
  
  public String toString()
  {
    return this._mMode;
  }
  
  public static LoyaltyRedemptionMode getModeForCode(int paramInt)
  {
    for (int i = 0; i < redemptionModes.length; i++) {
      if (redemptionModes[i]._mCode == paramInt) {
        return redemptionModes[i];
      }
    }
    return null;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.settings.LoyaltyRedemptionMode
 * JD-Core Version:    0.7.0.1
 */