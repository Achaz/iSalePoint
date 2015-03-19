package dm.jb.op.sync;

import dm.jb.db.objects.CurrentStockRow;
import dm.jb.db.objects.ProductRow;
import dm.jb.db.objects.ProductTableDef;
import dm.jb.db.objects.StoreInfoRow;
import dm.jb.db.objects.StoreInfoTableDef;
import dm.jb.db.objects.WhToStoreRow;
import dm.jb.ui.MainWindow;
import dm.jb.ui.inv.AllotStockPanel.SelectedEntry;
import dm.jb.ui.inv.StockInfoAndCurrentStock;
import dm.jb.ui.inv.WHToStoreRecordView;
import dm.tools.db.DBException;
import dm.tools.ui.AbstractMainPanel;
import dm.tools.ui.ActionPanel;
import dm.tools.ui.UICommon;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import javax.swing.JComponent;

public class WhToStoreFile
{
  public static WhToStoreFile INSTANCE = new WhToStoreFile();
  int[] productId = null;
  double[] qty = null;
  int[] whIndex = null;
  int prodCount = 0;
  int storeId = 0;
  
  public String writeData(WhToStoreRow paramWhToStoreRow, ArrayList<AllotStockPanel.SelectedEntry> paramArrayList)
    throws IOException
  {
    String str = FileOpUtils.INSTANCE.getDataFileName("ISP_WhToStore");
    FileOutputStream localFileOutputStream = FileOpUtils.INSTANCE.createFileStream(str, (short)1);
    localFileOutputStream.write(FileOpUtils.intToBytes(paramWhToStoreRow.getTxnNo()));
    localFileOutputStream.write(FileOpUtils.intToBytes(paramArrayList.size()));
    localFileOutputStream.write(FileOpUtils.intToBytes(paramWhToStoreRow.getStoreId()));
    Iterator localIterator = paramArrayList.iterator();
    while (localIterator.hasNext())
    {
      AllotStockPanel.SelectedEntry localSelectedEntry = (AllotStockPanel.SelectedEntry)localIterator.next();
      localFileOutputStream.write(FileOpUtils.intToBytes(localSelectedEntry._mProduct.getProdIndex()));
      localFileOutputStream.write(FileOpUtils.doubleToBytes(localSelectedEntry._mQuantity.doubleValue()));
      FileOpUtils.writeString(localFileOutputStream, localSelectedEntry._mProduct.getProductCode());
      localFileOutputStream.write(FileOpUtils.intToBytes(localSelectedEntry._mStockRow.currentStockRow.getWearHouseIndex()));
    }
    localFileOutputStream.close();
    return str;
  }
  
  public void readData(FileInputStream paramFileInputStream, Date paramDate)
    throws IOException, DBException, InvalidFileException
  {
    int i = FileOpUtils.readInt(paramFileInputStream);
    this.prodCount = FileOpUtils.readInt(paramFileInputStream);
    this.storeId = FileOpUtils.readInt(paramFileInputStream);
    this.productId = new int[this.prodCount];
    this.qty = new double[this.prodCount];
    String[] arrayOfString = new String[this.prodCount];
    ProductRow[] arrayOfProductRow = new ProductRow[this.prodCount];
    this.whIndex = new int[this.prodCount];
    if (StoreInfoTableDef.getCurrentStore() == null)
    {
      UICommon.showError("Updating store stock records require a store. You did not set any store.\n\nPlease login and select a store.", "Error", MainWindow.instance);
      return;
    }
    if (StoreInfoTableDef.getCurrentStore().getStoreId() != this.storeId)
    {
      UICommon.showError("The stock information is not meant for the current store.", "Error", MainWindow.instance);
      return;
    }
    for (int j = 0; j < this.prodCount; j++)
    {
      this.productId[j] = FileOpUtils.readInt(paramFileInputStream);
      this.qty[j] = FileOpUtils.readDouble(paramFileInputStream);
      arrayOfString[j] = FileOpUtils.readString(paramFileInputStream);
      this.whIndex[j] = FileOpUtils.readInt(paramFileInputStream);
    }
    paramFileInputStream.close();
    j = 0;
    for (int k = 0; k < this.prodCount; k++)
    {
      localObject = ProductTableDef.getInstance().findRowByIndex(this.productId[k]);
      arrayOfProductRow[k] = localObject;
      if (!((ProductRow)localObject).getProductCode().equals(arrayOfString[k]))
      {
        j = 1;
        break;
      }
    }
    if (j != 0)
    {
      UICommon.showError("Some products in the store do not match with the ones recieved.", "Error", MainWindow.instance);
      return;
    }
    ActionPanel localActionPanel = MainWindow.getActionPanel();
    Object localObject = WHToStoreRecordView.INSTANCE;
    WHToStoreRecordView.INSTANCE.setData(i, paramDate, this.productId, this.qty, arrayOfProductRow, this.whIndex);
    localActionPanel.cleanPush((JComponent)localObject);
    ((AbstractMainPanel)localObject).setActionPanel(localActionPanel);
    localActionPanel.setTitle("Warehouse to store record window");
    MainWindow.instance.repaint();
    ((AbstractMainPanel)localObject).setDefaultFocus();
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.op.sync.WhToStoreFile
 * JD-Core Version:    0.7.0.1
 */