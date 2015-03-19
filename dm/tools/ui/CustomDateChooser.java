package dm.tools.ui;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Color;
import java.util.Date;
import javax.swing.JComponent;

public class CustomDateChooser
  extends JDateChooser
{
  private String name = "";
  private boolean created = false;
  
  public CustomDateChooser()
  {
    this.created = true;
  }
  
  public CustomDateChooser(Date paramDate)
  {
    super(paramDate);
  }
  
  public CustomDateChooser(Date paramDate, String paramString)
  {
    setDate(paramDate);
    setDateFormatString(paramString);
  }
  
  public void setName(String paramString)
  {
    this.name = paramString;
  }
  
  public void setBackground(Color paramColor)
  {
    if (!this.created)
    {
      super.setBackground(paramColor);
    }
    else
    {
      JTextFieldDateEditor localJTextFieldDateEditor = (JTextFieldDateEditor)this.dateEditor;
      localJTextFieldDateEditor.getUiComponent().setBackground(paramColor);
    }
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public boolean requestFocusInWindow()
  {
    JTextFieldDateEditor localJTextFieldDateEditor = (JTextFieldDateEditor)this.dateEditor;
    localJTextFieldDateEditor.requestFocusInWindow();
    return true;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.ui.CustomDateChooser
 * JD-Core Version:    0.7.0.1
 */