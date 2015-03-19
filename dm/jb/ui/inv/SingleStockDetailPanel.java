package dm.jb.ui.inv;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.IDateEditor;
import dm.jb.db.objects.CurrentStockRow;
import dm.jb.db.objects.StockAndProductRow;
import dm.jb.db.objects.StockInfoRow;
import dm.jb.db.objects.VendorRow;
import dm.jb.db.objects.VendorTableDef;
import dm.jb.messages.GeneralUIMessages_base;
import dm.jb.messages.JbMessageLoader;
import dm.jb.messages.ProductMessages_base;
import dm.jb.ui.MainWindow;
import dm.jb.ui.common.HelpButton;
import dm.jb.ui.common.ShortKeyCommon;
import dm.tools.db.DBConnection;
import dm.tools.db.DBException;
import dm.tools.db.Db;
import dm.tools.messages.ButtonLabels_base;
import dm.tools.types.InternalAmount;
import dm.tools.ui.AbstractMainPanel;
import dm.tools.ui.InternalDateChooser;
import dm.tools.ui.UICommon;
import dm.tools.utils.Validation;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

public class SingleStockDetailPanel
  extends AbstractMainPanel
{
  private static SingleStockDetailPanel _mInstance = null;
  private JTextField _mProductId = null;
  private JTextField _mProductName = null;
  private JTextField _mStockIndex = null;
  private JTextField _mQuantity = null;
  private JLabel _mQtyUnitLbl = null;
  private InternalDateChooser _mStockDate = null;
  private JComboBox _mVendor = null;
  private JTextField _mPurchasePrice = null;
  private InternalDateChooser _mExpiry = null;
  private StockAndProductRow _mProduct = null;
  private StockInfoAndCurrentStock _mStockInfo = null;
  
  private SingleStockDetailPanel()
  {
    initUI();
  }
  
  public static SingleStockDetailPanel getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new SingleStockDetailPanel();
    }
    return _mInstance;
  }
  
  public void windowDisplayed()
  {
    try
    {
      ArrayList localArrayList = VendorTableDef.getInstance().getVendorRows();
      this._mVendor.removeAllItems();
      Iterator localIterator = localArrayList.iterator();
      while (localIterator.hasNext())
      {
        VendorRow localVendorRow = (VendorRow)localIterator.next();
        this._mVendor.addItem(localVendorRow);
      }
    }
    catch (DBException localDBException)
    {
      UICommon.showInternalError("Internal error populating vendor details. The vendor list may not be complete.");
      return;
    }
  }
  
  public void setDefaultFocus() {}
  
  public JPanel getPanel()
  {
    return this;
  }
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("10px,90px,10px,80px,10px, 40px, 40px, 40px, 100px", "10px,25px,10px,25px,10px,25px,10px,25px,10px,25px, 0px,0px, 10px,25px,10px,23px, 20px,30px,10px");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    JbMessageLoader localJbMessageLoader = (JbMessageLoader)UICommon.getMessageLoader();
    ProductMessages_base localProductMessages_base = localJbMessageLoader.getProductLabelsMessages();
    GeneralUIMessages_base localGeneralUIMessages_base = localJbMessageLoader.getGeneralUILabelsMessages();
    localCellConstraints.xywh(2, 2, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(new JLabel(localProductMessages_base.getMessage(131073) + " : "), localCellConstraints);
    this._mProductId = new JTextField();
    localCellConstraints.xywh(4, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mProductId, localCellConstraints);
    this._mProductId.setEditable(false);
    localCellConstraints.xywh(2, 4, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(new JLabel(localProductMessages_base.getMessage(131074) + " : "), localCellConstraints);
    this._mProductName = new JTextField();
    localCellConstraints.xywh(4, 4, 5, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mProductName, localCellConstraints);
    this._mProductName.setEditable(false);
    localCellConstraints.xywh(2, 6, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(new JLabel(localProductMessages_base.getMessage(131078) + " : "), localCellConstraints);
    this._mStockIndex = new JTextField();
    localCellConstraints.xywh(4, 6, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mStockIndex, localCellConstraints);
    this._mStockIndex.setEditable(false);
    JLabel localJLabel = new JLabel(localProductMessages_base.getMessage(131076) + " : ");
    localCellConstraints.xywh(2, 8, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mQuantity = new JTextField();
    localCellConstraints.xywh(4, 8, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mQuantity, localCellConstraints);
    this._mQuantity.setBackground(UICommon.MANDATORY_COLOR);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 81, this._mQuantity);
    this._mQtyUnitLbl = new JLabel("");
    localCellConstraints.xywh(5, 8, 3, 1, CellConstraints.LEFT, CellConstraints.FILL);
    add(this._mQtyUnitLbl, localCellConstraints);
    this._mStockDate = new InternalDateChooser(false);
    localCellConstraints.xywh(2, 10, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJLabel = new JLabel(localProductMessages_base.getMessage(131079) + " : ");
    add(localJLabel, localCellConstraints);
    localCellConstraints.xywh(4, 10, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mStockDate, localCellConstraints);
    this._mStockDate.setBackground(UICommon.MANDATORY_COLOR);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 68, this._mStockDate.getDateEditor().getUiComponent());
    this._mExpiry = new InternalDateChooser(false);
    localCellConstraints.xywh(2, 14, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJLabel = new JLabel(localProductMessages_base.getMessage(131081) + " : ");
    add(localJLabel, localCellConstraints);
    this._mPurchasePrice = new JTextField();
    localCellConstraints.xywh(4, 14, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mPurchasePrice, localCellConstraints);
    this._mPurchasePrice.setBackground(UICommon.MANDATORY_COLOR);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 80, this._mPurchasePrice);
    localCellConstraints.xywh(6, 14, 1, 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(new JLabel(" " + localGeneralUIMessages_base.getMessage(36866)), localCellConstraints);
    this._mVendor = new JComboBox();
    localCellConstraints.xywh(2, 16, 1, 1, CellConstraints.RIGHT, CellConstraints.TOP);
    localJLabel = new JLabel(localProductMessages_base.getMessage(131075) + " : ");
    add(localJLabel, localCellConstraints);
    localCellConstraints.xywh(4, 16, 5, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mVendor, localCellConstraints);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 86, this._mVendor);
    localCellConstraints.xywh(1, 17, 9, 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(new JSeparator(), localCellConstraints);
    localCellConstraints.xywh(1, 18, 9, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getButtonPanel(), localCellConstraints);
  }
  
  private JPanel getButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    JbMessageLoader localJbMessageLoader = (JbMessageLoader)UICommon.getMessageLoader();
    ButtonLabels_base localButtonLabels_base = localJbMessageLoader.getButtonLabelsMessages();
    FormLayout localFormLayout = new FormLayout("10px,100px,pref:grow, 100px,pref:grow,100px, 10px", "30px");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    Object localObject = new JButton(localButtonLabels_base.getMessage(135174));
    ((JButton)localObject).setMnemonic(65);
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        SingleStockDetailPanel.this.applyClicked();
      }
    });
    localObject = new JButton(localButtonLabels_base.getMessage(135172));
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        SingleStockDetailPanel.this.closeWindow();
      }
    });
    localObject = new HelpButton("ISP_STOCK_DETAILS");
    ((JButton)localObject).setMnemonic(72);
    localCellConstraints.xywh(6, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    localJPanel.setBackground(getBackground());
    return localJPanel;
  }
  
  public void setInfo(StockAndProductRow paramStockAndProductRow, StockInfoAndCurrentStock paramStockInfoAndCurrentStock)
  {
    this._mProduct = paramStockAndProductRow;
    this._mStockInfo = paramStockInfoAndCurrentStock;
    this._mProductId.setText(Integer.toString(paramStockAndProductRow.getProdIndex()));
    this._mProductName.setText(paramStockAndProductRow.getProdName());
    this._mStockIndex.setText(Integer.toString(paramStockInfoAndCurrentStock.stockInfoRow.getStockIndex()));
    this._mQtyUnitLbl.setText("  " + dm.tools.types.InternalQuantity.quantityUnits[paramStockAndProductRow.getProdUnit()]);
    if (paramStockAndProductRow.getProdUnit() == 2)
    {
      int i = (int)paramStockInfoAndCurrentStock.currentStockRow.getQuantity();
      this._mQuantity.setText(Integer.toString(i));
    }
    else
    {
      this._mQuantity.setText(Double.toString(paramStockInfoAndCurrentStock.currentStockRow.getQuantity()));
    }
    this._mStockDate.setDate(paramStockInfoAndCurrentStock.stockInfoRow.getStockDate());
    try
    {
      VendorRow localVendorRow = VendorTableDef.getInstance().getVendorById(paramStockInfoAndCurrentStock.stockInfoRow.getVendor());
      if (localVendorRow != null) {
        this._mVendor.setSelectedItem(localVendorRow);
      }
    }
    catch (DBException localDBException)
    {
      UICommon.showError("Internal Error reading Vendor details.\n\nTry again later. If the problem persists contact administartir.", "Internal Error", MainWindow.instance);
    }
    if (paramStockInfoAndCurrentStock.currentStockRow.getExpiryNa().equalsIgnoreCase("Y")) {
      this._mExpiry.setDate(null);
    } else {
      this._mExpiry.setDate(paramStockInfoAndCurrentStock.currentStockRow.getExpiry());
    }
    this._mPurchasePrice.setText(InternalAmount.toString(paramStockInfoAndCurrentStock.stockInfoRow.getPurchasePrice()));
  }
  
  private void applyClicked()
  {
    String str = this._mQuantity.getText();
    if (str.length() == 0)
    {
      UICommon.showError("Quantity cannot be empty.", "Error", MainWindow.instance);
      this._mQuantity.requestFocusInWindow();
      return;
    }
    if (this._mProduct.getProdUnit() == 2)
    {
      if (!Validation.isValidInt(str, false))
      {
        UICommon.showError("Invalid quantity value.", "Error", MainWindow.instance);
        this._mQuantity.requestFocusInWindow();
      }
    }
    else if (!Validation.isValidFloat(str, 10, false))
    {
      UICommon.showError("Invalid quantity value.", "Error", MainWindow.instance);
      this._mQuantity.requestFocusInWindow();
      return;
    }
    if (this._mStockDate.getDate() == null)
    {
      UICommon.showError("Stock date cannot be empty.", "Error", MainWindow.instance);
      this._mStockDate.requestFocusInWindow();
      return;
    }
    str = this._mPurchasePrice.getText();
    if (str.length() == 0)
    {
      UICommon.showError("Purchase price cannot be empty.", "Error", MainWindow.instance);
      this._mPurchasePrice.requestFocusInWindow();
      return;
    }
    if (!Validation.isValidAmount(str))
    {
      UICommon.showError("Purchase price is invalid.", "Error", MainWindow.instance);
      this._mPurchasePrice.requestFocusInWindow();
      return;
    }
    if (str.length() > 255)
    {
      UICommon.showError("Vendor details cannot excceed 255 characters.", "Error", MainWindow.instance);
      this._mVendor.requestFocusInWindow();
      return;
    }
    CurrentStockRow localCurrentStockRow = this._mStockInfo.currentStockRow;
    if (this._mExpiry.getDate() == null)
    {
      localCurrentStockRow.setExpiryNa("Y");
      localCurrentStockRow.setExpiry(null);
    }
    else
    {
      localCurrentStockRow.setExpiryNa("N");
      localCurrentStockRow.setExpiry(new java.sql.Date(this._mExpiry.getDate().getTime()));
    }
    double d = Double.valueOf(this._mQuantity.getText()).doubleValue();
    localCurrentStockRow.setQuantity(d);
    d = Double.valueOf(this._mPurchasePrice.getText()).doubleValue();
    StockInfoRow localStockInfoRow = this._mStockInfo.stockInfoRow;
    localStockInfoRow.setPurchasePrice(d);
    localStockInfoRow.setStockDate(new java.sql.Date(this._mStockDate.getDate().getTime()));
    VendorRow localVendorRow = (VendorRow)this._mVendor.getSelectedItem();
    if (localVendorRow == null) {
      localStockInfoRow.setVendor(localVendorRow.getVendorId());
    } else {
      localStockInfoRow.setVendor(-1);
    }
    DBConnection localDBConnection = Db.getConnection();
    try
    {
      localDBConnection.openTrans();
      localCurrentStockRow.update(false);
      localStockInfoRow.update(false);
      localDBConnection.commit();
    }
    catch (DBException localDBException)
    {
      UICommon.showError("Internal error updating stock information.\n\nRetry after sometime. If the problem persists contact administrator.", "Internal Error", MainWindow.instance);
      return;
    }
    UICommon.showMessage("Stock information updated successfully.", "Success", MainWindow.instance);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.inv.SingleStockDetailPanel
 * JD-Core Version:    0.7.0.1
 */