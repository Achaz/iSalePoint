package dm.tools.ui;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class ProgressWindow
  extends JDialog
{
  private JProgressBar _mPg = null;
  private ProgressWindowAction _mAction = null;
  
  public ProgressWindow(JFrame paramJFrame, ProgressWindowAction paramProgressWindowAction)
  {
    super(paramJFrame, paramProgressWindowAction != null ? paramProgressWindowAction.getActionName() : "Progress", paramJFrame != null);
    initUI();
    pack();
    setLocationRelativeTo(paramJFrame);
    this._mAction = paramProgressWindowAction;
  }
  
  public ProgressWindow(JDialog paramJDialog, ProgressWindowAction paramProgressWindowAction)
  {
    super(paramJDialog, paramProgressWindowAction != null ? paramProgressWindowAction.getActionName() : "Progress", paramJDialog != null);
    initUI();
    pack();
    setLocationRelativeTo(paramJDialog);
    this._mAction = paramProgressWindowAction;
  }
  
  private void initUI()
  {
    JPanel localJPanel = (JPanel)getContentPane();
    FormLayout localFormLayout = new FormLayout("10px,150px:grow,70px,150px:grow,10px", "10px,23px,20px,30px,10px");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(2, 2, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
    this._mPg = new JProgressBar();
    localJPanel.add(this._mPg, localCellConstraints);
    JButton localJButton = new JButton("Close");
    localCellConstraints.xywh(3, 4, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJButton, localCellConstraints);
    ActionListener local1 = new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ProgressWindow.this.setVisible(false);
      }
    };
    localJButton.addActionListener(local1);
  }
  
  public void startProgress()
  {
    this._mPg.setIndeterminate(true);
    final Thread local2 = new Thread()
    {
      public void run()
      {
        ProgressWindow.this._mAction.startAction();
        SwingUtilities.invokeLater(new Runnable()
        {
          public void run()
          {
            ProgressWindow.this.stopProgress();
          }
        });
      }
    };
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        local2.start();
      }
    });
    setVisible(true);
  }
  
  public void stopProgress()
  {
    this._mPg.setIndeterminate(true);
    setVisible(false);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.ui.ProgressWindow
 * JD-Core Version:    0.7.0.1
 */