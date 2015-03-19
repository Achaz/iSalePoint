package dm.tools.ui;

import com.jgoodies.looks.BorderStyle;
import com.jgoodies.looks.FontSizeHints;
import com.jgoodies.looks.HeaderStyle;
import com.jgoodies.looks.plastic.PlasticTheme;
import com.jgoodies.looks.plastic.theme.DesertBluer;
import com.jgoodies.looks.windows.WindowsLookAndFeel;
import javax.swing.LookAndFeel;

public final class UISettings
{
  private LookAndFeel selectedLookAndFeel;
  private PlasticTheme selectedTheme;
  private Boolean useSystemFonts;
  private FontSizeHints fontSizeHints;
  private boolean useNarrowButtons;
  private boolean tabIconsEnabled;
  private Boolean popupDropShadowEnabled;
  private String plasticTabStyle;
  private boolean plasticHighContrastFocusEnabled;
  private HeaderStyle menuBarHeaderStyle;
  private BorderStyle menuBarPlasticBorderStyle;
  private BorderStyle menuBarWindowsBorderStyle;
  private Boolean menuBar3DHint;
  private HeaderStyle toolBarHeaderStyle;
  private BorderStyle toolBarPlasticBorderStyle;
  private BorderStyle toolBarWindowsBorderStyle;
  private Boolean toolBar3DHint;
  private String clearLookPolicyName;
  
  public static UISettings createDefault()
  {
    UISettings localUISettings = new UISettings();
    localUISettings.setSelectedLookAndFeel(new WindowsLookAndFeel());
    localUISettings.setSelectedTheme(new DesertBluer());
    localUISettings.setUseSystemFonts(Boolean.TRUE);
    localUISettings.setFontSizeHints(FontSizeHints.MIXED);
    localUISettings.setUseNarrowButtons(false);
    localUISettings.setTabIconsEnabled(true);
    localUISettings.setPlasticTabStyle("default");
    localUISettings.setPlasticHighContrastFocusEnabled(false);
    localUISettings.setMenuBarHeaderStyle(null);
    localUISettings.setMenuBarPlasticBorderStyle(null);
    localUISettings.setMenuBarWindowsBorderStyle(null);
    localUISettings.setMenuBar3DHint(null);
    localUISettings.setToolBarHeaderStyle(null);
    localUISettings.setToolBarPlasticBorderStyle(null);
    localUISettings.setToolBarWindowsBorderStyle(null);
    localUISettings.setToolBar3DHint(null);
    return localUISettings;
  }
  
  public String getClearLookPolicyName()
  {
    return this.clearLookPolicyName;
  }
  
  public void setClearLookPolicyName(String paramString)
  {
    this.clearLookPolicyName = paramString;
  }
  
  public FontSizeHints getFontSizeHints()
  {
    return this.fontSizeHints;
  }
  
  public void setFontSizeHints(FontSizeHints paramFontSizeHints)
  {
    this.fontSizeHints = paramFontSizeHints;
  }
  
  public Boolean getMenuBar3DHint()
  {
    return this.menuBar3DHint;
  }
  
  public void setMenuBar3DHint(Boolean paramBoolean)
  {
    this.menuBar3DHint = paramBoolean;
  }
  
  public HeaderStyle getMenuBarHeaderStyle()
  {
    return this.menuBarHeaderStyle;
  }
  
  public void setMenuBarHeaderStyle(HeaderStyle paramHeaderStyle)
  {
    this.menuBarHeaderStyle = paramHeaderStyle;
  }
  
  public BorderStyle getMenuBarPlasticBorderStyle()
  {
    return this.menuBarPlasticBorderStyle;
  }
  
  public void setMenuBarPlasticBorderStyle(BorderStyle paramBorderStyle)
  {
    this.menuBarPlasticBorderStyle = paramBorderStyle;
  }
  
  public BorderStyle getMenuBarWindowsBorderStyle()
  {
    return this.menuBarWindowsBorderStyle;
  }
  
  public void setMenuBarWindowsBorderStyle(BorderStyle paramBorderStyle)
  {
    this.menuBarWindowsBorderStyle = paramBorderStyle;
  }
  
  public Boolean isPopupDropShadowEnabled()
  {
    return this.popupDropShadowEnabled;
  }
  
  public void setPopupDropShadowEnabled(Boolean paramBoolean)
  {
    this.popupDropShadowEnabled = paramBoolean;
  }
  
  public boolean isPlasticHighContrastFocusEnabled()
  {
    return this.plasticHighContrastFocusEnabled;
  }
  
  public void setPlasticHighContrastFocusEnabled(boolean paramBoolean)
  {
    this.plasticHighContrastFocusEnabled = paramBoolean;
  }
  
  public String getPlasticTabStyle()
  {
    return this.plasticTabStyle;
  }
  
  public void setPlasticTabStyle(String paramString)
  {
    this.plasticTabStyle = paramString;
  }
  
  public LookAndFeel getSelectedLookAndFeel()
  {
    return this.selectedLookAndFeel;
  }
  
  public void setSelectedLookAndFeel(LookAndFeel paramLookAndFeel)
  {
    this.selectedLookAndFeel = paramLookAndFeel;
  }
  
  public void setSelectedLookAndFeel(Class paramClass)
  {
    try
    {
      this.selectedLookAndFeel = ((LookAndFeel)paramClass.newInstance());
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    setSelectedLookAndFeel(this.selectedLookAndFeel);
  }
  
  public void setSelectedLookAndFeel(String paramString)
  {
    Class localClass = null;
    try
    {
      localClass = Class.forName(paramString);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    setSelectedLookAndFeel(localClass);
  }
  
  public PlasticTheme getSelectedTheme()
  {
    return this.selectedTheme;
  }
  
  public void setSelectedTheme(PlasticTheme paramPlasticTheme)
  {
    this.selectedTheme = paramPlasticTheme;
  }
  
  public boolean isTabIconsEnabled()
  {
    return this.tabIconsEnabled;
  }
  
  public void setTabIconsEnabled(boolean paramBoolean)
  {
    this.tabIconsEnabled = paramBoolean;
  }
  
  public Boolean getToolBar3DHint()
  {
    return this.toolBar3DHint;
  }
  
  public void setToolBar3DHint(Boolean paramBoolean)
  {
    this.toolBar3DHint = paramBoolean;
  }
  
  public HeaderStyle getToolBarHeaderStyle()
  {
    return this.toolBarHeaderStyle;
  }
  
  public void setToolBarHeaderStyle(HeaderStyle paramHeaderStyle)
  {
    this.toolBarHeaderStyle = paramHeaderStyle;
  }
  
  public BorderStyle getToolBarPlasticBorderStyle()
  {
    return this.toolBarPlasticBorderStyle;
  }
  
  public void setToolBarPlasticBorderStyle(BorderStyle paramBorderStyle)
  {
    this.toolBarPlasticBorderStyle = paramBorderStyle;
  }
  
  public BorderStyle getToolBarWindowsBorderStyle()
  {
    return this.toolBarWindowsBorderStyle;
  }
  
  public void setToolBarWindowsBorderStyle(BorderStyle paramBorderStyle)
  {
    this.toolBarWindowsBorderStyle = paramBorderStyle;
  }
  
  public boolean isUseNarrowButtons()
  {
    return this.useNarrowButtons;
  }
  
  public void setUseNarrowButtons(boolean paramBoolean)
  {
    this.useNarrowButtons = paramBoolean;
  }
  
  public Boolean isUseSystemFonts()
  {
    return this.useSystemFonts;
  }
  
  public void setUseSystemFonts(Boolean paramBoolean)
  {
    this.useSystemFonts = paramBoolean;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.ui.UISettings
 * JD-Core Version:    0.7.0.1
 */