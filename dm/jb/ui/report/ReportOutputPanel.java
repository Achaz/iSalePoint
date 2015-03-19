package dm.jb.ui.report;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.tools.ui.AbstractMainPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

public class ReportOutputPanel
  extends AbstractMainPanel
{
  private static ReportOutputPanel _mInstance = null;
  private ReportResultPanel _mResultPanel = null;
  private JScrollPane _msc = null;
  
  private ReportOutputPanel()
  {
    initUI();
  }
  
  public static ReportOutputPanel getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new ReportOutputPanel();
    }
    return _mInstance;
  }
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("10px,600px:grow,10px", "10px,400px:grow,20px,30px,10px");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    int i = localFormLayout.getColumnCount();
    int j = localFormLayout.getRowCount();
    localCellConstraints.xywh(1, j - 2, i, 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(new JSeparator(), localCellConstraints);
    localCellConstraints.xywh(1, j - 1, i, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getButtonPanel(), localCellConstraints);
  }
  
  private JPanel getButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("10px,100px,20px:grow,100px, 20px:grow, 100px, 10px", "30px");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    JButton localJButton = new JButton("Next");
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJButton, localCellConstraints);
    localJButton = new JButton("Close");
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJButton, localCellConstraints);
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ReportOutputPanel.this.closeWindow();
      }
    });
    localJButton = new JButton("Help");
    localCellConstraints.xywh(6, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJButton, localCellConstraints);
    localJPanel.setBackground(getBackground());
    return localJPanel;
  }
  
  public void windowDisplayed() {}
  
  public void setDefaultFocus() {}
  
  public JPanel getPanel()
  {
    return this;
  }
  
  public void setReportResultPanel(ReportResultPanel paramReportResultPanel)
  {
    if (this._mResultPanel != null)
    {
      this._mResultPanel.setVisible(false);
      remove(this._msc);
      this._msc.removeAll();
      this._mResultPanel = null;
      this._msc = null;
    }
    this._mResultPanel = paramReportResultPanel;
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(2, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    if (this._msc == null)
    {
      this._msc = new JScrollPane(paramReportResultPanel);
      add(this._msc, localCellConstraints);
    }
    paramReportResultPanel.setVisible(true);
    this._msc.setVisible(true);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.report.ReportOutputPanel
 * JD-Core Version:    0.7.0.1
 */