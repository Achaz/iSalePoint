package dm.jb.op.sync;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.IDateEditor;
import com.toedter.calendar.JDateChooser;
import dm.jb.db.objects.CategoryRow;
import dm.jb.db.objects.CategoryTableDef;
import dm.jb.db.objects.CurrentStockRow;
import dm.jb.db.objects.CurrentStockTableDef;
import dm.jb.db.objects.DeptRow;
import dm.jb.db.objects.DeptTableDef;
import dm.jb.db.objects.ProductRow;
import dm.jb.db.objects.ProductTableDef;
import dm.jb.db.objects.StoreStockRow;
import dm.jb.db.objects.StoreStockTableDef;
import dm.jb.ui.MainWindow;
import dm.jb.ui.settings.ProdSyncInfoPanel;
import dm.tools.db.DBConnection;
import dm.tools.db.DBException;
import dm.tools.db.Db;
import dm.tools.ui.AbstractMainPanel;
import dm.tools.ui.ActionPanel;
import dm.tools.ui.ProgressMessageListener;
import dm.tools.ui.UICommon;
import dm.tools.utils.CommonConfig;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jdesktop.swingx.JXButton;

public class FileSync
  implements SyncMode
{
  private JPanel _mConfigPanel = null;
  private JTextField _mFileName = null;
  private JDateChooser _mExpiryDate = null;
  private JCheckBox _mExpiryNotRequired = null;
  
  public String toString()
  {
    return "File";
  }
  
  public JPanel getOptionPanel()
  {
    if (this._mConfigPanel == null)
    {
      this._mConfigPanel = new JPanel();
      FormLayout localFormLayout = new FormLayout("90px,10px,100px,10px,100px,3px,40px", "25px,10px,25px");
      this._mConfigPanel.setLayout(localFormLayout);
      CellConstraints localCellConstraints = new CellConstraints();
      JLabel localJLabel = new JLabel("File : ");
      localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.CENTER, CellConstraints.FILL);
      this._mConfigPanel.add(localJLabel, localCellConstraints);
      this._mFileName = new JTextField();
      localCellConstraints.xywh(3, 1, 4, 1, CellConstraints.FILL, CellConstraints.FILL);
      this._mConfigPanel.add(this._mFileName, localCellConstraints);
      this._mFileName.setBackground(UICommon.MANDATORY_COLOR);
      this._mFileName.setText("C:\\Documents and Settings\\Deekshith\\My Documents");
      ImageIcon localImageIcon = new ImageIcon(getClass().getResource("/dm/jb/images/file_folder.gif"));
      JXButton localJXButton = new JXButton(localImageIcon);
      localCellConstraints.xywh(7, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
      this._mConfigPanel.add(localJXButton, localCellConstraints);
      localJXButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          JFileChooser localJFileChooser = new JFileChooser();
          int i = localJFileChooser.showOpenDialog(MainWindow.instance);
          if (i == 0)
          {
            String str = localJFileChooser.getSelectedFile().getAbsolutePath();
            FileSync.this._mFileName.setText(str);
          }
        }
      });
      localJLabel = new JLabel("Expiry Date : ");
      localCellConstraints.xywh(1, 3, 1, 1, CellConstraints.CENTER, CellConstraints.FILL);
      this._mConfigPanel.add(localJLabel, localCellConstraints);
      this._mExpiryDate = new JDateChooser();
      this._mExpiryDate.setDateFormatString(CommonConfig.getInstance().dateFormat);
      localCellConstraints.xywh(3, 3, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
      this._mConfigPanel.add(this._mExpiryDate, localCellConstraints);
      this._mExpiryNotRequired = new JCheckBox("Not Applicable");
      localCellConstraints.xywh(5, 3, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
      this._mConfigPanel.add(this._mExpiryNotRequired, localCellConstraints);
      this._mExpiryDate.getDateEditor().getUiComponent().setBackground(UICommon.MANDATORY_COLOR);
      Date localDate = new Date();
      localDate = new Date(localDate.getTime() + 86400000L);
      this._mExpiryDate.setDate(localDate);
      this._mExpiryNotRequired.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          if (FileSync.this._mExpiryNotRequired.isSelected())
          {
            FileSync.this._mExpiryDate.setEnabled(false);
          }
          else
          {
            FileSync.this._mExpiryDate.setEnabled(true);
            FileSync.this._mExpiryDate.getDateEditor().getUiComponent().setBackground(UICommon.MANDATORY_COLOR);
          }
        }
      });
    }
    return this._mConfigPanel;
  }
  
  public boolean isPanelValid()
  {
    String str = this._mFileName.getText().trim();
    if (str.length() == 0)
    {
      UICommon.showError("File name cannot be empty.", "Error", MainWindow.instance);
      this._mFileName.requestFocusInWindow();
      return false;
    }
    if ((!this._mExpiryNotRequired.isSelected()) && (this._mExpiryDate.getDate() == null))
    {
      UICommon.showError("Expiry date cannot be empty.\n\nIf the expiry date is nt applicabel, selection the 'Not Applicable' option.", "Error", MainWindow.instance);
      this._mExpiryDate.getDateEditor().getUiComponent().requestFocusInWindow();
      return false;
    }
    return true;
  }
  
  public SyncWriter getWriterInstance()
  {
    String str = this._mFileName.getText().trim();
    File localFile = new File(str);
    if (localFile.isDirectory())
    {
      if (!str.endsWith(File.separator)) {
        str = str + File.separator;
      }
      str = str + "ProductDetailsSyncData.isp";
    }
    return new FileSyncWriter(str);
  }
  
  public void writeModeSpecificDataToWrite(SyncWriter paramSyncWriter)
    throws IOException
  {
    Date localDate = this._mExpiryDate.getDate();
    paramSyncWriter.writeDate(localDate);
  }
  
  public static void readData(FileInputStream paramFileInputStream, Date paramDate, ProgressMessageListener paramProgressMessageListener, int paramInt, String paramString)
    throws IOException, DBException
  {
    FileSyncReader localFileSyncReader = new FileSyncReader(paramFileInputStream, paramDate, paramProgressMessageListener, paramString);
    try
    {
      localFileSyncReader.process(paramInt);
    }
    catch (InvalidFileException localInvalidFileException)
    {
      Db.getConnection().rollbackNoExp();
      UICommon.showDelayedErrorMessage("Invalid file.\n\nIt looks like a corrupted synchronization file.", "Error", MainWindow.instance);
    }
    finally
    {
      Db.getConnection().rollbackNoExp();
    }
  }
  
  public FileSync getFileSyncObject()
  {
    return this;
  }
  
  public static class FileSyncReader
  {
    FileInputStream fip = null;
    Date fielCreateDate = null;
    ProgressMessageListener messageListener = null;
    int currentByteCount = 0;
    private String _mFileName = null;
    
    public FileSyncReader(FileInputStream paramFileInputStream, Date paramDate, ProgressMessageListener paramProgressMessageListener, String paramString)
    {
      this.fip = paramFileInputStream;
      this.fielCreateDate = paramDate;
      this.messageListener = paramProgressMessageListener;
      this._mFileName = paramString;
    }
    
    public void process(int paramInt)
      throws IOException, InvalidFileException, DBException
    {
      this.currentByteCount = paramInt;
      int i = FileOpUtils.readShort(this.fip);
      switch (i)
      {
      case 1: 
        Date localDate = FileOpUtils.readDate(this.fip);
        this.currentByteCount += 4;
        ActionPanel localActionPanel = MainWindow.getActionPanel();
        ProdSyncInfoPanel localProdSyncInfoPanel = ProdSyncInfoPanel.INSTANCE;
        localActionPanel.cleanPush(localProdSyncInfoPanel);
        localProdSyncInfoPanel.setActionPanel(localActionPanel);
        localActionPanel.setTitle("Syncrhonization");
        MainWindow.instance.repaint();
        ProdSyncInfoPanel.INSTANCE.setExpiryAndCreateDate(localDate, this.fielCreateDate);
        ProdSyncInfoPanel.INSTANCE.setMessageText("Synchronize Product details");
        ProdSyncInfoPanel.INSTANCE.setFileSyncObject(this);
        break;
      default: 
        UICommon.showDelayedErrorMessage("Invalid file.\n\nIt looks like a corrupted synchronization file.", "Error", MainWindow.instance);
      }
    }
    
    public void setProgressMessageListener(ProgressMessageListener paramProgressMessageListener)
    {
      this.messageListener = paramProgressMessageListener;
    }
    
    public void syncProductDetails(byte paramByte)
      throws IOException, InvalidFileException, DBException
    {
      this.fip = new FileInputStream(this._mFileName);
      Db.getConnection().openTrans();
      FileOpUtils.skipFileOpHeader(this.fip);
      this.fip.skip(2L);
      this.fip.skip(8L);
      while (this.fip.available() > 0)
      {
        int i = FileOpUtils.readShort(this.fip);
        this.currentByteCount += 2;
        int j;
        int k;
        Object localObject2;
        int n;
        Object localObject4;
        Object localObject1;
        int m;
        Object localObject3;
        int i1;
        switch (i)
        {
        case 1: 
          j = FileOpUtils.readInt(this.fip);
          this.currentByteCount += 4;
          this.messageListener.setValue(this.currentByteCount);
          if (paramByte == 1)
          {
            DeptTableDef.getInstance().deleteAllRows();
            for (k = 0; k < j; k++)
            {
              localObject2 = DeptTableDef.getInstance().getNewRow();
              this.currentByteCount += ((DeptRow)localObject2).syncFromStream(this.fip);
              ((DeptRow)localObject2).create(true);
              this.messageListener.setValue(this.currentByteCount);
            }
          }
          else if (paramByte == 2)
          {
            for (k = 0; k < j; k++)
            {
              localObject2 = DeptTableDef.getInstance().getNewRow();
              this.currentByteCount += ((DeptRow)localObject2).syncFromStream(this.fip);
              ((DeptRow)localObject2).delete();
              ((DeptRow)localObject2).create(true);
              this.messageListener.setValue(this.currentByteCount);
            }
          }
          else if (paramByte == 3)
          {
            for (k = 0; k < j; k++)
            {
              localObject2 = DeptTableDef.getInstance().getNewRow();
              this.currentByteCount += ((DeptRow)localObject2).syncFromStream(this.fip);
              n = ((DeptRow)localObject2).getDeptIndex();
              localObject4 = DeptTableDef.getInstance().findDeptByIndex(n);
              if (localObject4 != null)
              {
                ((DeptRow)localObject2).delete();
                ((DeptRow)localObject2).create(true);
                this.messageListener.setValue(this.currentByteCount);
              }
            }
          }
          else if (paramByte == 4)
          {
            for (k = 0; k < j; k++)
            {
              localObject2 = DeptTableDef.getInstance().getNewRow();
              this.currentByteCount += ((DeptRow)localObject2).syncFromStream(this.fip);
              n = ((DeptRow)localObject2).getDeptIndex();
              localObject4 = DeptTableDef.getInstance().findDeptByIndex(n);
              if (localObject4 == null)
              {
                ((DeptRow)localObject2).create(true);
                this.messageListener.setValue(this.currentByteCount);
              }
            }
          }
          break;
        case 2: 
          j = FileOpUtils.readInt(this.fip);
          this.currentByteCount += 4;
          this.messageListener.setValue(this.currentByteCount);
          if (paramByte == 1)
          {
            CategoryTableDef.getInstance().deleteAllRows();
            for (k = 0; k < j; k++)
            {
              localObject2 = CategoryTableDef.getInstance().getNewRow();
              this.currentByteCount += ((CategoryRow)localObject2).syncFromStream(this.fip);
              ((CategoryRow)localObject2).create(true);
              this.messageListener.setValue(this.currentByteCount);
            }
          }
          else if (paramByte == 2)
          {
            for (k = 0; k < j; k++)
            {
              localObject2 = CategoryTableDef.getInstance().getNewRow();
              this.currentByteCount += ((CategoryRow)localObject2).syncFromStream(this.fip);
              ((CategoryRow)localObject2).delete();
              ((CategoryRow)localObject2).create(true);
              this.messageListener.setValue(this.currentByteCount);
            }
          }
          else if (paramByte == 3)
          {
            for (k = 0; k < j; k++)
            {
              localObject2 = CategoryTableDef.getInstance().getNewRow();
              this.currentByteCount += ((CategoryRow)localObject2).syncFromStream(this.fip);
              n = ((CategoryRow)localObject2).getDeptIndex();
              localObject4 = CategoryTableDef.getInstance().findCatgeoryByIndex(n);
              if (localObject4 != null)
              {
                ((CategoryRow)localObject2).delete();
                ((CategoryRow)localObject2).create(true);
                this.messageListener.setValue(this.currentByteCount);
              }
            }
          }
          else if (paramByte == 4)
          {
            for (k = 0; k < j; k++)
            {
              localObject2 = CategoryTableDef.getInstance().getNewRow();
              this.currentByteCount += ((CategoryRow)localObject2).syncFromStream(this.fip);
              n = ((CategoryRow)localObject2).getCatIndex();
              localObject4 = CategoryTableDef.getInstance().findCatgeoryByIndex(n);
              if (localObject4 == null)
              {
                ((CategoryRow)localObject2).create(true);
                this.messageListener.setValue(this.currentByteCount);
              }
            }
          }
          break;
        case 3: 
          j = FileOpUtils.readInt(this.fip);
          this.currentByteCount += 4;
          this.messageListener.setValue(this.currentByteCount);
          if (paramByte == 1)
          {
            ProductTableDef.getInstance().deleteAllRows();
            for (k = 0; k < j; k++)
            {
              localObject2 = ProductTableDef.getInstance().getNewRow();
              this.currentByteCount += ((ProductRow)localObject2).syncFromStream(this.fip);
              ((ProductRow)localObject2).create(true);
              this.messageListener.setValue(this.currentByteCount);
            }
          }
          else if (paramByte == 2)
          {
            for (k = 0; k < j; k++)
            {
              localObject2 = ProductTableDef.getInstance().getNewRow();
              this.currentByteCount += ((ProductRow)localObject2).syncFromStream(this.fip);
              ((ProductRow)localObject2).delete();
              ((ProductRow)localObject2).create(true);
              this.messageListener.setValue(this.currentByteCount);
            }
          }
          else if (paramByte == 3)
          {
            for (k = 0; k < j; k++)
            {
              localObject2 = ProductTableDef.getInstance().getNewRow();
              this.currentByteCount += ((ProductRow)localObject2).syncFromStream(this.fip);
              n = ((ProductRow)localObject2).getProdIndex();
              localObject4 = ProductTableDef.getInstance().findRowByIndex(n);
              if (localObject4 != null)
              {
                ((ProductRow)localObject2).delete();
                ((ProductRow)localObject2).create(true);
                this.messageListener.setValue(this.currentByteCount);
              }
            }
          }
          else if (paramByte == 4)
          {
            for (k = 0; k < j; k++)
            {
              localObject2 = ProductTableDef.getInstance().getNewRow();
              this.currentByteCount += ((ProductRow)localObject2).syncFromStream(this.fip);
              n = ((ProductRow)localObject2).getProdIndex();
              localObject4 = ProductTableDef.getInstance().findRowByIndex(n);
              if (localObject4 == null)
              {
                ((ProductRow)localObject2).create(true);
                this.messageListener.setValue(this.currentByteCount);
              }
            }
          }
          break;
        case 4: 
          j = FileOpUtils.readInt(this.fip);
          this.currentByteCount += 4;
          this.messageListener.setValue(this.currentByteCount);
          if (paramByte == 1)
          {
            StoreStockTableDef.getInstance().deleteAllRows();
            for (k = 0; k < j; k++)
            {
              localObject2 = StoreStockTableDef.getInstance().getNewRow();
              this.currentByteCount += ((StoreStockRow)localObject2).syncFromStream(this.fip);
              ((StoreStockRow)localObject2).create(true);
              this.messageListener.setValue(this.currentByteCount);
            }
          }
          else if (paramByte == 2)
          {
            localObject1 = StoreStockTableDef.getInstance();
            for (m = 0; m < j; m++)
            {
              localObject3 = ((StoreStockTableDef)localObject1).getNewRow();
              this.currentByteCount += ((StoreStockRow)localObject3).syncFromStream(this.fip);
              ((StoreStockRow)localObject3).delete();
              ((StoreStockRow)localObject3).create(true);
              this.messageListener.setValue(this.currentByteCount);
            }
          }
          else
          {
            int i2;
            StoreStockRow localStoreStockRow;
            if (paramByte == 3)
            {
              localObject1 = StoreStockTableDef.getInstance();
              for (m = 0; m < j; m++)
              {
                localObject3 = ((StoreStockTableDef)localObject1).getNewRow();
                this.currentByteCount += ((StoreStockRow)localObject3).syncFromStream(this.fip);
                i1 = ((StoreStockRow)localObject3).getProductId();
                i2 = ((StoreStockRow)localObject3).getStoreId();
                localStoreStockRow = ((StoreStockTableDef)localObject1).getStockForProductInStore(i1, i2);
                if (localStoreStockRow != null)
                {
                  ((StoreStockRow)localObject3).delete();
                  ((StoreStockRow)localObject3).create(true);
                  this.messageListener.setValue(this.currentByteCount);
                }
              }
            }
            else if (paramByte == 4)
            {
              localObject1 = StoreStockTableDef.getInstance();
              for (m = 0; m < j; m++)
              {
                localObject3 = ((StoreStockTableDef)localObject1).getNewRow();
                this.currentByteCount += ((StoreStockRow)localObject3).syncFromStream(this.fip);
                i1 = ((StoreStockRow)localObject3).getProductId();
                i2 = ((StoreStockRow)localObject3).getStoreId();
                localStoreStockRow = ((StoreStockTableDef)localObject1).getStockForProductInStore(i1, i2);
                if (localStoreStockRow == null)
                {
                  ((StoreStockRow)localObject3).create(true);
                  this.messageListener.setValue(this.currentByteCount);
                }
              }
            }
          }
          break;
        case 5: 
          j = FileOpUtils.readInt(this.fip);
          this.currentByteCount += 4;
          this.messageListener.setValue(this.currentByteCount);
          if (paramByte == 1)
          {
            localObject1 = CurrentStockTableDef.getInstance();
            ((CurrentStockTableDef)localObject1).deleteAllRows();
            for (m = 0; m < j; m++)
            {
              localObject3 = ((CurrentStockTableDef)localObject1).getNewRow();
              this.currentByteCount += ((CurrentStockRow)localObject3).syncFromStream(this.fip);
              ((CurrentStockRow)localObject3).create(true);
              this.messageListener.setValue(this.currentByteCount);
            }
          }
          else if (paramByte == 2)
          {
            localObject1 = CurrentStockTableDef.getInstance();
            for (m = 0; m < j; m++)
            {
              localObject3 = ((CurrentStockTableDef)localObject1).getNewRow();
              this.currentByteCount += ((CurrentStockRow)localObject3).syncFromStream(this.fip);
              ((CurrentStockRow)localObject3).delete();
              ((CurrentStockRow)localObject3).create(true);
              this.messageListener.setValue(this.currentByteCount);
            }
          }
          else
          {
            CurrentStockRow localCurrentStockRow;
            if (paramByte == 3)
            {
              localObject1 = CurrentStockTableDef.getInstance();
              for (m = 0; m < j; m++)
              {
                localObject3 = ((CurrentStockTableDef)localObject1).getNewRow();
                this.currentByteCount += ((CurrentStockRow)localObject3).syncFromStream(this.fip);
                i1 = ((CurrentStockRow)localObject3).getCurStockIndex();
                localCurrentStockRow = (CurrentStockRow)((CurrentStockTableDef)localObject1).findRowByIndex(i1);
                if (localCurrentStockRow != null)
                {
                  ((CurrentStockRow)localObject3).delete();
                  ((CurrentStockRow)localObject3).create(true);
                  this.messageListener.setValue(this.currentByteCount);
                }
              }
            }
            else if (paramByte == 4)
            {
              localObject1 = CurrentStockTableDef.getInstance();
              for (m = 0; m < j; m++)
              {
                localObject3 = ((CurrentStockTableDef)localObject1).getNewRow();
                this.currentByteCount += ((CurrentStockRow)localObject3).syncFromStream(this.fip);
                i1 = ((CurrentStockRow)localObject3).getCurStockIndex();
                localCurrentStockRow = (CurrentStockRow)((CurrentStockTableDef)localObject1).findRowByIndex(i1);
                if (localCurrentStockRow == null)
                {
                  ((CurrentStockRow)localObject3).create(true);
                  this.messageListener.setValue(this.currentByteCount);
                }
              }
            }
          }
          break;
        default: 
          this.fip.close();
          throw new InvalidFileException("Invalid header code " + i);
        }
      }
      Db.getConnection().commit();
      this.fip.close();
    }
  }
  
  public class FileSyncWriter
    implements SyncWriter
  {
    String _mFileName = null;
    private FileOutputStream _mFile = null;
    private short _mType = 0;
    
    public FileSyncWriter(String paramString)
    {
      this._mFileName = paramString;
    }
    
    private void writeData(byte[] paramArrayOfByte)
      throws IOException
    {
      this._mFile.write(paramArrayOfByte);
    }
    
    public void open(short paramShort)
      throws SyncException
    {
      try
      {
        this._mFile = new FileOutputStream(new File(this._mFileName));
      }
      catch (FileNotFoundException localFileNotFoundException)
      {
        throw new SyncException("File " + this._mFileName + " not found.");
      }
      this._mType = paramShort;
    }
    
    public void init()
      throws SyncException
    {
      try
      {
        FileOpUtils.writeHeaderInfo(this._mFile, (short)2);
        this._mFile.write(FileOpUtils.shortToBytes(this._mType));
      }
      catch (IOException localIOException)
      {
        new SyncException("IOException writing the header", localIOException);
      }
    }
    
    public void close()
      throws SyncException
    {}
    
    public void destroy()
    {
      try
      {
        this._mFile.close();
        new File(this._mFileName).delete();
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
    }
    
    public void writeShort(short paramShort)
      throws IOException
    {
      writeData(FileOpUtils.shortToBytes(paramShort));
    }
    
    public void writeInt(int paramInt)
      throws IOException
    {
      writeData(FileOpUtils.intToBytes(paramInt));
    }
    
    public void writeLong(long paramLong)
      throws IOException
    {
      writeData(FileOpUtils.longToBytes(paramLong));
    }
    
    public void writeString(String paramString)
      throws IOException
    {
      FileOpUtils.writeString(this._mFile, paramString);
    }
    
    public void writeDouble(double paramDouble)
      throws IOException
    {
      writeData(FileOpUtils.doubleToBytes(paramDouble));
    }
    
    public void writeDate(Date paramDate)
      throws IOException
    {
      if (paramDate == null) {
        writeData(FileOpUtils.longToBytes(-1L));
      } else {
        writeData(FileOpUtils.longToBytes(paramDate.getTime()));
      }
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.op.sync.FileSync
 * JD-Core Version:    0.7.0.1
 */