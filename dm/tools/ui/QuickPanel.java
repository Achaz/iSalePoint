package dm.tools.ui;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JPanel;

public class QuickPanel
  extends SimpleInternalFrame
{
  private String _mPanelName = "Quick Access";
  private JPanel mPanel = null;
  private ArrayList<QuickButtonGroup> _mButtonGroupList = null;
  
  public QuickPanel()
  {
    super("Quick Access");
    FormLayout localFormLayout = new FormLayout("pref:grow, pref, pref:grow", "10px, pref, 10px, pref, 10px, pref, 10px, pref, 10px, pref, 10px, pref, 10px:grow");
    this.mPanel.setLayout(localFormLayout);
    this.mPanel.setBackground(new Color(202, 130, 133));
    setContent(this.mPanel);
    this.mPanel.setVisible(true);
    setVisible(true);
  }
  
  public String getPanelName()
  {
    return this._mPanelName;
  }
  
  public void cleanup()
  {
    this.mPanel.removeAll();
    this.mPanel.repaint();
    this._mButtonGroupList = null;
  }
  
  public void setTitle(String paramString)
  {
    this._mPanelName = paramString;
    super.setTitle(paramString);
  }
  
  public QuickButtonGroup createButtonGroup(Color paramColor)
  {
    return new QuickButtonGroup(paramColor);
  }
  
  public void setupGroupList(ArrayList<QuickButtonGroup> paramArrayList)
  {
    this._mButtonGroupList = paramArrayList;
    if (this._mButtonGroupList == null)
    {
      cleanup();
      return;
    }
    CellConstraints localCellConstraints = new CellConstraints();
    Iterator localIterator = this._mButtonGroupList.iterator();
    for (int i = 2; localIterator.hasNext(); i += 2)
    {
      QuickButtonGroup localQuickButtonGroup = (QuickButtonGroup)localIterator.next();
      localCellConstraints.xywh(2, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
      this.mPanel.add(localQuickButtonGroup, localCellConstraints);
      localQuickButtonGroup.setupButtons();
    }
    this.mPanel.setVisible(true);
    setVisible(true);
  }
  
  public class QuickButtonGroup
    extends JPanel
  {
    Color _mColor = null;
    ArrayList<JButton> _mButtonList = null;
    
    public QuickButtonGroup(Color paramColor)
    {
      setBackground(paramColor);
      this._mColor = paramColor;
      this._mButtonList = new ArrayList();
      initUI();
    }
    
    public void initUI() {}
    
    public void addButton(JButton paramJButton)
    {
      this._mButtonList.add(paramJButton);
    }
    
    private void setupButtons()
    {
      CellConstraints localCellConstraints = new CellConstraints();
      Iterator localIterator = this._mButtonList.iterator();
      int i = 2;
      removeAll();
      int j = this._mButtonList.size();
      StringBuffer localStringBuffer = new StringBuffer("");
      for (int k = 0; k < j; k++) {
        if (k == 0) {
          localStringBuffer.append("10px, 25px");
        } else {
          localStringBuffer.append(", 10px, 25px");
        }
      }
      localStringBuffer.append(",10px");
      FormLayout localFormLayout = new FormLayout("5px, 130px, 5px", localStringBuffer.toString());
      setLayout(localFormLayout);
      while (localIterator.hasNext())
      {
        JButton localJButton = (JButton)localIterator.next();
        localCellConstraints.xywh(2, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
        add(localJButton, localCellConstraints);
        i += 2;
      }
      setVisible(true);
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.ui.QuickPanel
 * JD-Core Version:    0.7.0.1
 */