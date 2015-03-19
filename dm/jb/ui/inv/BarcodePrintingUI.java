package dm.jb.ui.inv;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.db.objects.ProductRow;
import dm.jb.db.objects.ProductTableDef;
import dm.jb.printing.JePrinterException;
import dm.jb.ui.MainWindow;
import dm.jb.ui.common.HelpButton;
import dm.jb.ui.common.ShortKeyCommon;
import dm.jb.ui.inv.barcode.ProductBarCodeIf;
import dm.jb.ui.inv.barcode.ProductTagCode;
import dm.jb.ui.inv.barcode.SimpleProductBarCode;
import dm.tools.db.DBException;
import dm.tools.ui.AbstractMainPanel;
import dm.tools.ui.UICommon;
import dm.tools.ui.components.JBIntegerTextField;
import dm.tools.ui.components.JBSearchButton;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jdesktop.swingx.JXButton;

public class BarcodePrintingUI
  extends AbstractMainPanel
{
  public static BarcodePrintingUI _mInstance = new BarcodePrintingUI();
  private JBIntegerTextField _mProductId = null;
  private JTextField _mProductName = null;
  private JBIntegerTextField _mTagCount = null;
  private JList _mSelectedList = null;
  private DefaultListModel _mSelectedListModel = null;
  private JComboBox _mFormatType = null;
  private JPanel _mSamplePanel = null;
  private JButton _mDeleteButton = null;
  private JSpinner _mScaleX = null;
  private JSpinner _mScaleY = null;
  private ArrayList<ProductTagCode> _mSelectedCodes = new ArrayList();
  
  private BarcodePrintingUI()
  {
    initUI();
    registerFormats();
  }
  
  public void setDefaultFocus()
  {
    this._mFormatType.setSelectedIndex(0);
    formatTypeChanged();
    this._mProductName.requestFocusInWindow();
  }
  
  public JPanel getPanel()
  {
    return this;
  }
  
  public void windowDisplayed()
  {
    this._mFormatType.setSelectedIndex(0);
    formatTypeChanged();
    this._mScaleX.setValue(new Double(1.0D));
    this._mScaleY.setValue(new Double(1.0D));
  }
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("10px,100px,10px,120px,4px,40px,50px,4px,40px,20px,270px,10px", "10px,25px, 10px,25px,10px,25px,10px,25px,10px,25px,10px,30px,10px,200px,pref:grow,20px,30px,10px");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    int i = 2;
    JLabel localJLabel = new JLabel("Format : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mFormatType = new JComboBox();
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mFormatType, localCellConstraints);
    this._mFormatType.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        BarcodePrintingUI.this.formatTypeChanged();
      }
    });
    ShortKeyCommon.shortKeyForLabels(localJLabel, 84, this._mFormatType);
    i += 2;
    localJLabel = new JLabel("Product Code : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mProductId = new JBIntegerTextField();
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mProductId, localCellConstraints);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 67, this._mProductId);
    JBSearchButton localJBSearchButton = new JBSearchButton(false);
    localCellConstraints.xywh(6, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJBSearchButton, localCellConstraints);
    localJBSearchButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        BarcodePrintingUI.this.searchByProductCode();
      }
    });
    i += 2;
    localJLabel = new JLabel("Product Name : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mProductName = new JBIntegerTextField();
    localCellConstraints.xywh(4, i, 4, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mProductName, localCellConstraints);
    this._mProductName.setBackground(UICommon.MANDATORY_COLOR);
    this._mProductId.setBackground(UICommon.MANDATORY_COLOR);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 78, this._mProductName);
    localJBSearchButton = new JBSearchButton(false);
    localCellConstraints.xywh(9, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJBSearchButton, localCellConstraints);
    localJBSearchButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ProductSearchPanel localProductSearchPanel = new ProductSearchPanel(MainWindow.instance);
        String str1 = BarcodePrintingUI.this._mProductName.getText().trim();
        if (str1.length() == 0) {
          str1 = "%";
        }
        String str2 = "";
        String str3 = "";
        ProductRow localProductRow = localProductSearchPanel.searchProduct(str1, str3, str2, MainWindow.instance);
        if (localProductRow != null)
        {
          BarcodePrintingUI.this._mProductId.setText(localProductRow.getProductCode());
          BarcodePrintingUI.this.setProduct(localProductRow);
          BarcodePrintingUI.this._mTagCount.requestFocusInWindow();
        }
      }
    });
    i += 2;
    localJLabel = new JLabel("Count : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mTagCount = new JBIntegerTextField(true, true, 2147483647, 0);
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    this._mTagCount.setHorizontalAlignment(4);
    add(this._mTagCount, localCellConstraints);
    this._mTagCount.setBackground(UICommon.MANDATORY_COLOR);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 85, this._mTagCount);
    i += 2;
    localCellConstraints.xywh(2, i, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getScalingPanel(), localCellConstraints);
    i += 2;
    localCellConstraints.xywh(4, i, 7, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getAddDeleteButtonPanel(), localCellConstraints);
    i += 2;
    localJLabel = new JLabel("Selected : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.TOP);
    add(localJLabel, localCellConstraints);
    this._mSelectedListModel = new DefaultListModel();
    this._mSelectedList = new JList(this._mSelectedListModel);
    localCellConstraints.xywh(4, i, 4, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(new JScrollPane(this._mSelectedList), localCellConstraints);
    this._mSelectedList.addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent paramAnonymousListSelectionEvent)
      {
        int i = BarcodePrintingUI.this._mSelectedList.getSelectedIndex();
        if (paramAnonymousListSelectionEvent.getValueIsAdjusting()) {
          return;
        }
        if (i >= 0) {
          BarcodePrintingUI.this._mDeleteButton.setEnabled(true);
        } else {
          BarcodePrintingUI.this._mDeleteButton.setEnabled(false);
        }
      }
    });
    ShortKeyCommon.shortKeyForLabels(localJLabel, 76, this._mSelectedList);
    this._mSamplePanel = new JPanel()
    {
      public void paintComponent(Graphics paramAnonymousGraphics)
      {
        super.paintComponent(paramAnonymousGraphics);
        SwingUtilities.invokeLater(new Runnable()
        {
          public void run()
          {
            BarcodePrintingUI.this.drawCode();
          }
        });
      }
      
      public void repaint()
      {
        super.repaint();
        BarcodePrintingUI.this.drawCode();
      }
    };
    this._mSamplePanel.setOpaque(true);
    localCellConstraints.xywh(11, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(new JLabel("Sample"), localCellConstraints);
    localCellConstraints.xywh(11, 4, 1, i - 4 + 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mSamplePanel, localCellConstraints);
    i += 2;
    localCellConstraints.xywh(1, i, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(new JSeparator(), localCellConstraints);
    i++;
    localCellConstraints.xywh(2, i, localFormLayout.getColumnCount() - 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getButtonPanel(), localCellConstraints);
  }
  
  private JPanel getScalingPanel()
  {
    JPanel localJPanel = new JPanel();
    localJPanel.setLayout(new FormLayout("100px,10px,pref:grow,10px,pref:grow", "25px"));
    CellConstraints localCellConstraints = new CellConstraints();
    JLabel localJLabel = new JLabel("Scale : ");
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.RIGHT, CellConstraints.CENTER);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mScaleX = new JSpinner(new SpinnerNumberModel(1.0D, 0.1D, 10.0D, 0.05D));
    ShortKeyCommon.shortKeyForLabels(localJLabel, 69, this._mScaleX);
    localCellConstraints.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mScaleX, localCellConstraints);
    this._mScaleX.addChangeListener(new ChangeListener()
    {
      public void stateChanged(ChangeEvent paramAnonymousChangeEvent)
      {
        BarcodePrintingUI.this.drawCode();
      }
    });
    localJLabel = new JLabel("-");
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.CENTER, CellConstraints.CENTER);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mScaleY = new JSpinner(new SpinnerNumberModel(1.0D, 0.1D, 10.0D, 0.05D));
    localCellConstraints.xywh(5, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mScaleY, localCellConstraints);
    this._mScaleY.addChangeListener(new ChangeListener()
    {
      public void stateChanged(ChangeEvent paramAnonymousChangeEvent)
      {
        BarcodePrintingUI.this.drawCode();
      }
    });
    return localJPanel;
  }
  
  private void registerFormats()
  {
    SimpleProductBarCode localSimpleProductBarCode = new SimpleProductBarCode();
    this._mFormatType.addItem(localSimpleProductBarCode);
  }
  
  private void formatTypeChanged()
  {
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        BarcodePrintingUI.this.drawCode();
      }
    });
  }
  
  private JPanel getButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("100px,10px,100px,pref:grow,100px", "30px");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    JXButton localJXButton = new JXButton("Print");
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        BarcodePrintingUI.this.printBarCode();
      }
    });
    localCellConstraints.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJXButton = new JXButton("Close");
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        BarcodePrintingUI.this.closeWindow();
      }
    });
    localCellConstraints.xywh(5, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    HelpButton localHelpButton = new HelpButton("ISP_BC_PRINT");
    localJPanel.add(localHelpButton, localCellConstraints);
    return localJPanel;
  }
  
  private JPanel getAddDeleteButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("80px,10px,80px", "30px");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    JXButton localJXButton = new JXButton("Add +");
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.setMnemonic(521);
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        BarcodePrintingUI.this.addClicked();
      }
    });
    localJXButton = new JXButton("Remove -");
    localCellConstraints.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    this._mDeleteButton = localJXButton;
    localJXButton.setMnemonic(45);
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        BarcodePrintingUI.this.deleteClicked();
      }
    });
    localJXButton.setEnabled(false);
    return localJPanel;
  }
  
  private void drawCode()
  {
    ProductBarCodeIf localProductBarCodeIf = (ProductBarCodeIf)this._mFormatType.getSelectedItem();
    if (localProductBarCodeIf == null) {
      return;
    }
    double d1 = ((Double)this._mScaleX.getValue()).doubleValue();
    double d2 = ((Double)this._mScaleY.getValue()).doubleValue();
    localProductBarCodeIf.printSample((Graphics2D)this._mSamplePanel.getGraphics(), d1, d2);
  }
  
  private ProductRow searchByProductCode()
  {
    String str = this._mProductId.getText();
    if (str.length() == 0)
    {
      UICommon.showError("Product code cannot be empty.", "Error", MainWindow.instance);
      this._mProductId.requestFocusInWindow();
      return null;
    }
    try
    {
      ProductRow localProductRow = ProductTableDef.getInstance().getProductByCode(str);
      if (localProductRow == null)
      {
        UICommon.showError("No product found.", "No record", MainWindow.instance);
        return null;
      }
      setProduct(localProductRow);
      this._mTagCount.requestFocusInWindow();
      return localProductRow;
    }
    catch (DBException localDBException)
    {
      UICommon.showError("Internal error searching for product details.\n\nTry again later. If the problem persists contact administrator.", "Internal Error", MainWindow.instance);
    }
    return null;
  }
  
  private void setProduct(ProductRow paramProductRow)
  {
    this._mProductName.setText(paramProductRow.getProdName());
  }
  
  private void addClicked()
  {
    ProductRow localProductRow = searchByProductCode();
    if (localProductRow == null) {
      return;
    }
    int i = this._mSelectedCodes.size();
    for (int j = 0; j < i; j++)
    {
      ProductTagCode localProductTagCode1 = (ProductTagCode)this._mSelectedCodes.get(j);
      if (localProductTagCode1.product.getProdIndex() == localProductRow.getProdIndex())
      {
        UICommon.showError("The product already added.\nDelete the existing entry to add it again.", "Error", MainWindow.instance);
        this._mSelectedList.setSelectedIndex(j);
        return;
      }
    }
    String str = this._mTagCount.getText().trim();
    if (str.length() == 0)
    {
      UICommon.showError("Tag count cannot be empty.", "Error", MainWindow.instance);
      this._mTagCount.requestFocusInWindow();
      return;
    }
    if (!this._mTagCount.isValidValue())
    {
      UICommon.showError("Invalid value for tag count", "Error", MainWindow.instance);
      this._mTagCount.requestFocusInWindow();
      return;
    }
    int k = Integer.valueOf(str).intValue();
    ProductTagCode localProductTagCode2 = new ProductTagCode(localProductRow, k);
    this._mSelectedListModel.addElement(localProductTagCode2);
    this._mSelectedCodes.add(localProductTagCode2);
    this._mSelectedList.updateUI();
    this._mProductId.setText("");
    this._mProductName.setText("");
    this._mTagCount.setText("");
    this._mProductName.requestFocusInWindow();
  }
  
  private void deleteClicked()
  {
    int i = UICommon.showQuestion("Are you sure you want to delete the selected entries.", "Confirm Deletion", MainWindow.instance);
    if (i != 1) {
      return;
    }
    Object[] arrayOfObject = this._mSelectedList.getSelectedValues();
    for (int j = 0; j < arrayOfObject.length; j++)
    {
      this._mSelectedListModel.removeElement(arrayOfObject[j]);
      this._mSelectedCodes.remove(arrayOfObject[j]);
    }
  }
  
  private void printBarCode()
  {
    ProductBarCodeIf localProductBarCodeIf = (ProductBarCodeIf)this._mFormatType.getSelectedItem();
    if (localProductBarCodeIf == null) {
      return;
    }
    try
    {
      double d1 = ((Double)this._mScaleX.getValue()).doubleValue();
      double d2 = ((Double)this._mScaleY.getValue()).doubleValue();
      localProductBarCodeIf.printBarCode(this._mSelectedCodes, d1, d2);
    }
    catch (JePrinterException localJePrinterException)
    {
      localJePrinterException.printStackTrace();
      UICommon.showError("Error printing the barcode.", "Error", MainWindow.instance);
      return;
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.inv.BarcodePrintingUI
 * JD-Core Version:    0.7.0.1
 */