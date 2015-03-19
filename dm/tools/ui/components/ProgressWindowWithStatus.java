package dm.tools.ui.components;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.tools.ui.ProgressMessageListener;
import dm.tools.ui.ProgressWindowAction;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class ProgressWindowWithStatus
  extends JDialog
  implements ProgressMessageListener
{
  private ProgressWindowAction _mAction = null;
  private JLabel _mActionMessage = null;
  private JProgressBar _mPg = null;
  
  public ProgressWindowWithStatus(JFrame paramJFrame, ProgressWindowAction paramProgressWindowAction)
  {
    super(paramJFrame, paramProgressWindowAction != null ? paramProgressWindowAction.getActionName() : "Progress", paramJFrame != null);
    initUI();
    pack();
    setLocationRelativeTo(paramJFrame);
    setDefaultCloseOperation(0);
    this._mAction = paramProgressWindowAction;
  }
  
  private void initUI()
  {
    JPanel localJPanel = (JPanel)getContentPane();
    FormLayout localFormLayout = new FormLayout("10px,150px,100px,150px,10px", "10px,25px,10px,25px,20px,30px,10px");
    localJPanel.setLayout(localFormLayout);
    this._mActionMessage = new JLabel();
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(2, 2, 3, 1, CellConstraints.LEFT, CellConstraints.CENTER);
    localJPanel.add(this._mActionMessage, localCellConstraints);
    localCellConstraints.xywh(2, 4, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
    this._mPg = new JProgressBar();
    localJPanel.add(this._mPg, localCellConstraints);
    this._mPg.setIndeterminate(false);
  }
  
  public void startProgress()
  {
    if (this._mAction == null) {
      setVisible(true);
    } else {
      startProgressInternal();
    }
  }
  
  private void startProgressInternal()
  {
    this._mPg.setIndeterminate(false);
    final Thread local1 = new Thread()
    {
      public void run()
      {
        ProgressWindowWithStatus.this._mAction.startAction();
        SwingUtilities.invokeLater(new Runnable()
        {
          public void run()
          {
            ProgressWindowWithStatus.this.stopProgress();
          }
        });
      }
    };
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        local1.start();
      }
    });
    setVisible(true);
  }
  
  public void setMessage(final String paramString)
  {
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        ProgressWindowWithStatus.this._mActionMessage.setText(paramString);
      }
    });
  }
  
  public void setValue(final int paramInt)
  {
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        ProgressWindowWithStatus.this._mPg.setValue(paramInt);
      }
    });
  }
  
  public void stopProgress()
  {
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        ProgressWindowWithStatus.this._mPg.setIndeterminate(true);
        ProgressWindowWithStatus.this.setVisible(false);
      }
    });
  }
  
  public void setMaximum(int paramInt)
  {
    this._mPg.setMaximum(paramInt);
  }
  
  public void setMinimum(int paramInt)
  {
    this._mPg.setMinimum(paramInt);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.ui.components.ProgressWindowWithStatus
 * JD-Core Version:    0.7.0.1
 */