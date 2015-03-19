package dm.tools.ui;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.tools.messages.ButtonLabels_base;
import dm.tools.messages.MessageLoader;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;

public abstract class WizardBase
  extends AbstractMainPanel
{
  public static final int VALIDATE_ON_FINISH = 1;
  public static final int VALIDATE_ON_NEXT = 2;
  private ArrayList<WizardPanelBase> _mWizardPanelList = new ArrayList();
  private WizardPanelBase _mCurrentPage = null;
  private int _mCurrentPageIndex = -1;
  private JButton _mNextButton = null;
  private JButton _mPrevButton = null;
  private JButton _mFinishButton = null;
  private int _mValidationMode = 1;
  private String _mPrefixTitle = "";
  private String _mFinishButtonText = "Finish";
  private JPanel _mButtonPanel = null;
  private WizardPanelBase _mWelcomePage = null;
  
  public WizardBase(int paramInt)
  {
    this._mValidationMode = paramInt;
    initWizardBase(680, 350);
    initPanels();
  }
  
  public void setBackground(Color paramColor)
  {
    super.setBackground(paramColor);
    if (this._mButtonPanel != null) {
      this._mButtonPanel.setBackground(paramColor);
    }
  }
  
  public void setWelcomePage(WizardPanelBase paramWizardPanelBase)
  {
    this._mWelcomePage = paramWizardPanelBase;
  }
  
  protected void initPanels() {}
  
  public WizardBase(int paramInt1, int paramInt2, int paramInt3)
  {
    this._mValidationMode = paramInt1;
    initWizardBase(paramInt2, paramInt3);
  }
  
  private void initWizardBase(int paramInt1, int paramInt2)
  {
    String str1 = new String(paramInt1 + "px");
    String str2 = new String(paramInt2 + "px");
    FormLayout localFormLayout = new FormLayout(str1 + ",10px", str2 + ",20px,30px,10px");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(1, 2, 2, 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(new JSeparator(), localCellConstraints);
    this._mButtonPanel = getButtonPanel();
    localCellConstraints.xywh(1, 3, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mButtonPanel, localCellConstraints);
    this._mPrevButton.setEnabled(false);
    this._mNextButton.setEnabled(false);
  }
  
  private JPanel getButtonPanel()
  {
    ButtonLabels_base localButtonLabels_base = UICommon.getMessageLoader().getButtonLabelsMessages();
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("10px,100px,30px:grow,100px,40px:grow,100px,10px,100px,10px,100px,10px", "30px");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    JButton localJButton = new JButton(localButtonLabels_base.getMessage(135172));
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJButton, localCellConstraints);
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        WizardBase.this.closeWindow();
      }
    });
    localJButton = new JButton(localButtonLabels_base.getMessage(135171));
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJButton, localCellConstraints);
    localJButton = new JButton(localButtonLabels_base.getMessage(135170));
    this._mPrevButton = localJButton;
    localCellConstraints.xywh(6, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJButton, localCellConstraints);
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        WizardBase.this.prevClicked();
      }
    });
    localJButton = new JButton(localButtonLabels_base.getMessage(135169));
    this._mNextButton = localJButton;
    localCellConstraints.xywh(8, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJButton, localCellConstraints);
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        WizardBase.this.nextClicked();
      }
    });
    localJButton.setName("dm.tools.ui.Wizardbase.NextButton");
    localJButton = new JButton(localButtonLabels_base.getMessage(135173));
    this._mFinishButton = localJButton;
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        WizardBase.this.finishClicked();
      }
    });
    localCellConstraints.xywh(10, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJButton, localCellConstraints);
    localJButton.setName("dm.tools.ui.Wizardbase.FinishButton");
    localJPanel.setBackground(getBackground());
    return localJPanel;
  }
  
  public JPanel getPanel()
  {
    return this;
  }
  
  public void setDefaultFocus() {}
  
  public void windowDisplayed() {}
  
  public void addPanel(WizardPanelBase paramWizardPanelBase)
  {
    paramWizardPanelBase.setBackground(getBackground());
    this._mWizardPanelList.add(paramWizardPanelBase);
    this._mNextButton.setEnabled(true);
  }
  
  public void addPanel(WizardPanelBase paramWizardPanelBase, int paramInt)
  {
    paramWizardPanelBase.setBackground(getBackground());
    this._mWizardPanelList.add(paramInt, paramWizardPanelBase);
    this._mNextButton.setEnabled(true);
  }
  
  public void addPanel(WizardPanelBase paramWizardPanelBase1, WizardPanelBase paramWizardPanelBase2)
  {
    paramWizardPanelBase1.setBackground(getBackground());
    int i = this._mWizardPanelList.indexOf(paramWizardPanelBase2);
    if (i == this._mWizardPanelList.size() - 1) {
      this._mWizardPanelList.add(paramWizardPanelBase1);
    } else {
      this._mWizardPanelList.add(i + 1, paramWizardPanelBase1);
    }
  }
  
  public void removePanel(WizardPanelBase paramWizardPanelBase)
  {
    this._mWizardPanelList.remove(paramWizardPanelBase);
  }
  
  public void setPage(int paramInt)
  {
    if ((this._mCurrentPage != null) && (this._mCurrentPage == this._mWizardPanelList.get(paramInt))) {
      return;
    }
    if (this._mCurrentPage != null)
    {
      this._mCurrentPage.setVisible(false);
      remove(this._mCurrentPage);
    }
    CellConstraints localCellConstraints = new CellConstraints();
    WizardPanelBase localWizardPanelBase = (WizardPanelBase)this._mWizardPanelList.get(paramInt);
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localWizardPanelBase, localCellConstraints);
    this._mCurrentPage = localWizardPanelBase;
    setTitle(this._mPrefixTitle + " - " + this._mCurrentPage.getTitle());
    this._mCurrentPage.setVisible(true);
    this._mCurrentPageIndex = paramInt;
    if (this._mCurrentPageIndex == 0) {
      this._mPrevButton.setEnabled(false);
    } else {
      this._mPrevButton.setEnabled(true);
    }
    if (this._mCurrentPageIndex == this._mWizardPanelList.size() - 1) {
      this._mNextButton.setEnabled(false);
    } else {
      this._mNextButton.setEnabled(true);
    }
    this._mCurrentPage.pageDisplayed();
    if (this._mCurrentPage == this._mWelcomePage) {
      this._mFinishButton.setEnabled(false);
    } else {
      this._mFinishButton.setEnabled(true);
    }
  }
  
  private void nextClicked()
  {
    if ((this._mValidationMode == 2) && (!this._mCurrentPage.isPageValid())) {
      return;
    }
    WizardPanelBase localWizardPanelBase = this._mCurrentPage;
    if (!localWizardPanelBase.leaveForNext()) {
      return;
    }
    this._mCurrentPageIndex += 1;
    setPage(this._mCurrentPageIndex);
  }
  
  private void prevClicked()
  {
    WizardPanelBase localWizardPanelBase = this._mCurrentPage;
    if (!localWizardPanelBase.leaveForPrev()) {
      return;
    }
    this._mCurrentPageIndex -= 1;
    setPage(this._mCurrentPageIndex);
  }
  
  public void setPrefixTitle(String paramString)
  {
    this._mPrefixTitle = paramString;
    if (this._mCurrentPage != null) {
      setTitle(this._mPrefixTitle + " - " + this._mCurrentPage.getTitle());
    }
  }
  
  public String getTitle()
  {
    if (this._mCurrentPage != null) {
      return this._mPrefixTitle + " - " + this._mCurrentPage.getTitle();
    }
    return this._mPrefixTitle;
  }
  
  public void setFinishButtonText(String paramString)
  {
    ButtonLabels_base localButtonLabels_base = UICommon.getMessageLoader().getButtonLabelsMessages();
    if (this._mFinishButtonText.equals("Finish")) {
      paramString = localButtonLabels_base.getMessage(135173);
    }
    this._mFinishButtonText = paramString;
    this._mFinishButton.setText(this._mFinishButtonText);
  }
  
  private void finishClicked()
  {
    Iterator localIterator = this._mWizardPanelList.iterator();
    for (int i = 0; localIterator.hasNext(); i++)
    {
      WizardPanelBase localWizardPanelBase = (WizardPanelBase)localIterator.next();
      if (!localWizardPanelBase.isPageValid())
      {
        setPage(i);
        localWizardPanelBase.setFailedCompFocus();
        return;
      }
    }
    boolean bool = finish();
    if (bool)
    {
      closeWindow();
      return;
    }
  }
  
  public abstract boolean finish();
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.ui.WizardBase
 * JD-Core Version:    0.7.0.1
 */