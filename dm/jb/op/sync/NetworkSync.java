package dm.jb.op.sync;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.io.IOException;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class NetworkSync
  implements SyncMode
{
  private JPanel _mConfigPanel = null;
  private JTextField _mHost = null;
  private JTextField _mPort = null;
  private JTextField _mUserName = null;
  private JPasswordField _mPassword = null;
  
  public String toString()
  {
    return "Network";
  }
  
  public JPanel getOptionPanel()
  {
    if (this._mConfigPanel == null)
    {
      this._mConfigPanel = new JPanel();
      FormLayout localFormLayout = new FormLayout("90px,10px,100px,10px", "25px,10px,25px,10px,25px,10px,25px");
      this._mConfigPanel.setLayout(localFormLayout);
      CellConstraints localCellConstraints = new CellConstraints();
      JLabel localJLabel = new JLabel("Host : ");
      localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
      this._mConfigPanel.add(localJLabel, localCellConstraints);
      this._mHost = new JTextField();
      localCellConstraints.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
      this._mConfigPanel.add(this._mHost, localCellConstraints);
      localJLabel = new JLabel("Port : ");
      localCellConstraints.xywh(1, 3, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
      this._mConfigPanel.add(localJLabel, localCellConstraints);
      this._mPort = new JTextField();
      localCellConstraints.xywh(3, 3, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
      this._mConfigPanel.add(this._mPort, localCellConstraints);
      localJLabel = new JLabel("User : ");
      localCellConstraints.xywh(1, 5, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
      this._mConfigPanel.add(localJLabel, localCellConstraints);
      this._mUserName = new JTextField();
      localCellConstraints.xywh(3, 5, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
      this._mConfigPanel.add(this._mUserName, localCellConstraints);
      localJLabel = new JLabel("Password : ");
      localCellConstraints.xywh(1, 7, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
      this._mConfigPanel.add(localJLabel, localCellConstraints);
      this._mPassword = new JPasswordField();
      localCellConstraints.xywh(3, 7, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
      this._mConfigPanel.add(this._mPassword, localCellConstraints);
    }
    return this._mConfigPanel;
  }
  
  public boolean isPanelValid()
  {
    return false;
  }
  
  public SyncWriter getWriterInstance()
  {
    return null;
  }
  
  public void writeModeSpecificDataToWrite(SyncWriter paramSyncWriter)
    throws IOException
  {}
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.op.sync.NetworkSync
 * JD-Core Version:    0.7.0.1
 */