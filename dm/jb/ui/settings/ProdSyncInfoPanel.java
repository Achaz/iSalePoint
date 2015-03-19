package dm.jb.ui.settings;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.op.sync.FileSync.FileSyncReader;
import dm.jb.op.sync.InvalidFileException;
import dm.jb.ui.MainWindow;
import dm.jb.ui.common.HelpButton;
import dm.tools.db.DBException;
import dm.tools.ui.AbstractMainPanel;
import dm.tools.ui.UICommon;
import dm.tools.ui.components.ProgressWindowWithStatus;
import dm.tools.utils.CommonConfig;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ProdSyncInfoPanel
  extends AbstractMainPanel
{
  public static ProdSyncInfoPanel INSTANCE = new ProdSyncInfoPanel();
  private static final String SYNC_TYPE_CLEAN_AND_CREATE = "Clean and Create";
  private static final String SYNC_TYPE_UPDATE_AND_CREATE = "Update and Create";
  private static final String SYNC_TYPE_UPDATE_ONLY = "Update Only";
  private static final String SYNC_TYPE_CREATE_ONLY = "Create Only";
  private JLabel _mTypeLabel = null;
  private JTextField _mCreateDate = null;
  private JTextField _mExpiryDate = null;
  private JComboBox _mSyncType = null;
  private Date _mExpiry = null;
  private FileSync.FileSyncReader _mFileSync = null;
  ProgressWindowWithStatus progressWindow = null;
  
  private ProdSyncInfoPanel()
  {
    initUI();
  }
  
  public void setDefaultFocus() {}
  
  public JPanel getPanel()
  {
    return this;
  }
  
  public void windowDisplayed() {}
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("10px,90px,10px,100px,100px,100px,10px", "10px,25px,20px,25px,10px,25px,10px,25px,20px,30px,10px");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    this._mTypeLabel = new JLabel("");
    localCellConstraints.xywh(2, 2, 5, 1, CellConstraints.CENTER, CellConstraints.CENTER);
    add(this._mTypeLabel, localCellConstraints);
    JLabel localJLabel = new JLabel("Create Date : ");
    localCellConstraints.xywh(2, 4, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mCreateDate = new JTextField();
    localCellConstraints.xywh(4, 4, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mCreateDate, localCellConstraints);
    localJLabel = new JLabel("Expiry : ");
    localCellConstraints.xywh(2, 6, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mExpiryDate = new JTextField();
    localCellConstraints.xywh(4, 6, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mExpiryDate, localCellConstraints);
    this._mExpiryDate.setEditable(false);
    this._mCreateDate.setEditable(false);
    localJLabel = new JLabel("Synch. method : ");
    localCellConstraints.xywh(2, 8, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mSyncType = new JComboBox();
    localCellConstraints.xywh(4, 8, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mSyncType, localCellConstraints);
    this._mSyncType.addItem("Clean and Create");
    this._mSyncType.addItem("Update and Create");
    this._mSyncType.addItem("Update Only");
    this._mSyncType.addItem("Create Only");
    localCellConstraints.xywh(1, 9, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(new JSeparator(), localCellConstraints);
    localCellConstraints.xywh(1, 10, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getButtonPanel(), localCellConstraints);
  }
  
  public JPanel getButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("10px,100px,10px,100px,pref:grow,100px,10px", "30px");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    Object localObject = new JButton("Synchronize");
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ProdSyncInfoPanel.this.syncClicked();
      }
    });
    localObject = new JButton("Close");
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ProdSyncInfoPanel.this.closeWindow();
      }
    });
    localObject = new HelpButton("ISP_SYNC_HELP");
    localCellConstraints.xywh(6, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    return localJPanel;
  }
  
  public void setMessageText(String paramString)
  {
    this._mTypeLabel.setText("<HTML><B>" + paramString + "</B></HTML>");
  }
  
  public void setExpiryAndCreateDate(Date paramDate1, Date paramDate2)
  {
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(CommonConfig.getInstance().dateFormat);
    if (paramDate1 == null) {
      this._mExpiryDate.setText("NA");
    } else {
      this._mExpiryDate.setText(localSimpleDateFormat.format(paramDate1));
    }
    this._mExpiry = paramDate1;
    this._mCreateDate.setText(localSimpleDateFormat.format(paramDate2));
  }
  
  private void syncClicked()
  {
    if ((this._mExpiry != null) && (new Date().getTime() > this._mExpiry.getTime()))
    {
      UICommon.showError("The synchronization data is very old.\n\nPlease check the expiry date.", "Error", MainWindow.instance);
      return;
    }
    String str = (String)this._mSyncType.getSelectedItem();
    byte b1 = 0;
    if (str.equals("Clean and Create")) {
      b1 = 1;
    } else if (str.equals("Update and Create")) {
      b1 = 2;
    } else if (str.equals("Update Only")) {
      b1 = 3;
    } else if (str.equals("Create Only")) {
      b1 = 4;
    }
    if (this.progressWindow == null) {
      this.progressWindow = new ProgressWindowWithStatus(MainWindow.instance, null);
    }
    this.progressWindow.setValue(0);
    final byte b2 = b1;
    Thread local3 = new Thread()
    {
      public void run()
      {
        int i = 0;
        try
        {
          ProdSyncInfoPanel.this._mFileSync.setProgressMessageListener(ProdSyncInfoPanel.this.progressWindow);
          ProdSyncInfoPanel.this._mFileSync.syncProductDetails(b2);
        }
        catch (DBException localDBException)
        {
          localDBException.printStackTrace();
          ProdSyncInfoPanel.this.progressWindow.stopProgress();
          i = 1;
          UICommon.showDelayedErrorMessage("Internal Database error during synchrionization.", "Internal Error", MainWindow.instance);
        }
        catch (IOException localIOException)
        {
          localIOException.printStackTrace();
          ProdSyncInfoPanel.this.progressWindow.stopProgress();
          i = 1;
          UICommon.showDelayedErrorMessage("Internal error during synchrionization.", "Internal Error", MainWindow.instance);
        }
        catch (InvalidFileException localInvalidFileException)
        {
          localInvalidFileException.printStackTrace();
          ProdSyncInfoPanel.this.progressWindow.stopProgress();
          i = 1;
          UICommon.showDelayedErrorMessage("Internal error during synchrionization.", "Internal Error", MainWindow.instance);
        }
        ProdSyncInfoPanel.this.progressWindow.stopProgress();
        if (i == 0)
        {
          UICommon.showDelayedMessage("Synchrnization successful.", "Success", MainWindow.instance);
          return;
        }
      }
    };
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        ProdSyncInfoPanel.this.progressWindow.startProgress();
      }
    });
    local3.start();
  }
  
  public void setFileSyncObject(FileSync.FileSyncReader paramFileSyncReader)
  {
    this._mFileSync = paramFileSyncReader;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.settings.ProdSyncInfoPanel
 * JD-Core Version:    0.7.0.1
 */