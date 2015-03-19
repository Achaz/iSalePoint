package dm.jb.printing.laser;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.printing.BillPrintCommon;
import dm.jb.printing.PrintColumn;
import dm.jb.ui.common.ShortKeyCommon;
import dm.jb.ui.res.ResourceUtils;
import dm.tools.printing.PrintingUIClassIf;
import dm.tools.utils.Config;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class LaserPrinterConfigPanel
  extends PrintingUIClassIf
{
  private MyTableModel _mModel = null;
  private JTable _mWidthsTable = null;
  private JCheckBox _mBillDrawColumnLines = null;
  private JCheckBox _mBillDrawRowLines = null;
  private JCheckBox _mBillColumnHeader = null;
  private JSpinner _mBillHeaderGap = null;
  private JCheckBox _mBillBarCode = null;
  private JCheckBox _mMiscDrawColumnLines = null;
  private JCheckBox _mMiscDrawRowLines = null;
  private JCheckBox _mMiscColumnHeader = null;
  private JSpinner _mMiscLeftMargin = null;
  private JSpinner _mMiscRightMargin = null;
  private JSpinner _mMiscTopMargin = null;
  private JSpinner _mMiscBottomMargin = null;
  private JSpinner _mBillLeftMargin = null;
  private JSpinner _mBillTopMargin = null;
  private JSpinner _mBillBottomMargin = null;
  public static PrintColumn[] PrintAvailableColumns = { new PrintColumn(ResourceUtils.getString("LASTER_PRINT_COL_SLNO"), "SL", 5, 4), new PrintColumn(ResourceUtils.getString("LASTER_PRINT_COL_CODE"), "PC", 6, 4), new PrintColumn(ResourceUtils.getString("LASTER_PRINT_COL_PRODUCT_NAME"), "PN", 25, 2), new PrintColumn(ResourceUtils.getString("LASTER_PRINT_COL_EXPIRY"), "EX", 12, 2), new PrintColumn(ResourceUtils.getString("LASTER_PRINT_COL_UNIT_PRICE"), "UP", 12, 4), new PrintColumn(ResourceUtils.getString("LASTER_PRINT_COL_QTY"), "QT", 12, 4), new PrintColumn(ResourceUtils.getString("LASTER_PRINT_COL_AMT"), "AM", 7, 4), new PrintColumn(ResourceUtils.getString("LASTER_PRINT_COL_DISCOUNT"), "DC", 7, 4) };
  
  public LaserPrinterConfigPanel()
  {
    initUI();
  }
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("pref:grow", "pref:grow");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    JTabbedPane localJTabbedPane = new JTabbedPane();
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJTabbedPane, localCellConstraints);
    localJTabbedPane.add(ResourceUtils.getString("LASTER_PRINT_RECPT_PRINT"), getBillPrintConfigPanel());
    localJTabbedPane.add(ResourceUtils.getString("LASTER_PRINT_OTHERS"), getPrintMiscConfigPanel());
  }
  
  private JPanel getBillPrintConfigPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("10px, pref, 10px, 275px,10px", "10px, 180px,pref:grow");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(2, 2, 1, 2, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(getBillPrintMiscConfigPanel(), localCellConstraints);
    this._mModel = new MyTableModel(null);
    this._mWidthsTable = new JTable(this._mModel);
    localCellConstraints.xywh(4, 2, 1, 1, CellConstraints.FILL, CellConstraints.TOP);
    JScrollPane localJScrollPane = new JScrollPane(this._mWidthsTable);
    localJPanel.add(localJScrollPane, localCellConstraints);
    this._mWidthsTable.setAutoResizeMode(0);
    this._mWidthsTable.getColumnModel().getColumn(0).setPreferredWidth(150);
    this._mWidthsTable.getColumnModel().getColumn(1).setPreferredWidth(60);
    this._mWidthsTable.getColumnModel().getColumn(2).setPreferredWidth(60);
    return localJPanel;
  }
  
  private JPanel getBillPrintMiscConfigPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("90px, 10px, 60px, 140px,2px,30px", "25px,10px,25px,10px,25px,10px,25px,10px,25px,10px, 25px,10px,25px,10px,25px,10px,25px,10px,pref");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    int i = 1;
    JLabel localJLabel = new JLabel();
    ResourceUtils.setLabelString("LASTER_PRINT_HEADER_GAP", localJLabel);
    localCellConstraints.xywh(1, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mBillHeaderGap = new JSpinner(new SpinnerNumberModel(0.0D, 0.0D, 20.0D, 0.01D));
    localCellConstraints.xywh(3, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mBillHeaderGap, localCellConstraints);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 71, this._mBillHeaderGap);
    i += 2;
    localJLabel = new JLabel();
    ResourceUtils.setLabelString("LASTER_PRINT_LEFT_MARGIN", localJLabel);
    localCellConstraints.xywh(1, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mBillLeftMargin = new JSpinner(new SpinnerNumberModel(0.0D, 0.0D, 10.0D, 0.01D));
    localCellConstraints.xywh(3, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mBillLeftMargin, localCellConstraints);
    i += 2;
    localJLabel = new JLabel();
    ResourceUtils.setLabelString("LASTER_PRINT_TOP_MARGIN", localJLabel);
    localCellConstraints.xywh(1, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mBillTopMargin = new JSpinner(new SpinnerNumberModel(0.0D, 0.0D, 10.0D, 0.01D));
    localCellConstraints.xywh(3, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mBillTopMargin, localCellConstraints);
    i += 2;
    localJLabel = new JLabel();
    ResourceUtils.setLabelString("LASTER_PRINT_BOTTOM_MARGIN", localJLabel);
    localCellConstraints.xywh(1, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mBillBottomMargin = new JSpinner(new SpinnerNumberModel(0.0D, 0.0D, 10.0D, 0.01D));
    localCellConstraints.xywh(3, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mBillBottomMargin, localCellConstraints);
    i += 2;
    this._mBillDrawColumnLines = new JCheckBox();
    ResourceUtils.setCheckBoxString("LASTER_PRINT_DRAW_COL_BORDER", this._mBillDrawColumnLines);
    localCellConstraints.xywh(3, i, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mBillDrawColumnLines, localCellConstraints);
    i += 2;
    this._mBillDrawRowLines = new JCheckBox();
    ResourceUtils.setCheckBoxString("LASTER_PRINT_DRAW_ROW_BORDER", this._mBillDrawRowLines);
    localCellConstraints.xywh(3, i, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mBillDrawRowLines, localCellConstraints);
    i += 2;
    this._mBillColumnHeader = new JCheckBox();
    ResourceUtils.setCheckBoxString("LASTER_PRINT_DRAW_PRINT_COL_HEADER", this._mBillColumnHeader);
    localCellConstraints.xywh(3, i, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mBillColumnHeader, localCellConstraints);
    i += 2;
    this._mBillBarCode = new JCheckBox();
    ResourceUtils.setCheckBoxString("LASTER_PRINT_DRAW_PRINT_BAR_CODE", this._mBillBarCode);
    localCellConstraints.xywh(3, i, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mBillBarCode, localCellConstraints);
    return localJPanel;
  }
  
  private JPanel getPrintMiscConfigPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("10px, 90px, 10px, 60px, 140px,2px,30px", "10px,25px,10px,25px,10px,25px,10px,25px,10px, 25px,10px,25px,10px,25px,10px,25px,10px,pref");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    int i = 2;
    JLabel localJLabel = new JLabel();
    ResourceUtils.setLabelString("LASTER_PRINT_MISC_LEFT_MARGIN", localJLabel);
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mMiscLeftMargin = new JSpinner(new SpinnerNumberModel(0.0D, 0.0D, 10.0D, 0.01D));
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mMiscLeftMargin, localCellConstraints);
    i += 2;
    localJLabel = new JLabel();
    ResourceUtils.setLabelString("LASTER_PRINT_MISC_RIGHT_MARGIN", localJLabel);
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mMiscRightMargin = new JSpinner(new SpinnerNumberModel(0.0D, 0.0D, 10.0D, 0.01D));
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mMiscRightMargin, localCellConstraints);
    i += 2;
    localJLabel = new JLabel();
    ResourceUtils.setLabelString("LASTER_PRINT_MISC_TOP_MARGIN", localJLabel);
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mMiscTopMargin = new JSpinner(new SpinnerNumberModel(0.0D, 0.0D, 10.0D, 0.01D));
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mMiscTopMargin, localCellConstraints);
    i += 2;
    localJLabel = new JLabel();
    ResourceUtils.setLabelString("LASTER_PRINT_MISC_BOTTOM_MARGIN", localJLabel);
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mMiscBottomMargin = new JSpinner(new SpinnerNumberModel(0.0D, 0.0D, 10.0D, 0.01D));
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mMiscBottomMargin, localCellConstraints);
    i += 2;
    this._mMiscDrawColumnLines = new JCheckBox("Draw column border");
    localCellConstraints.xywh(4, i, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mMiscDrawColumnLines, localCellConstraints);
    i += 2;
    this._mMiscDrawRowLines = new JCheckBox();
    ResourceUtils.setCheckBoxString("LASTER_PRINT_MISC_DRAW_COL_BORDER", this._mMiscDrawRowLines);
    localCellConstraints.xywh(4, i, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mMiscDrawRowLines, localCellConstraints);
    i += 2;
    this._mMiscColumnHeader = new JCheckBox();
    ResourceUtils.setCheckBoxString("LASTER_PRINT_MISC_DRAW_ROW_BORDER", this._mMiscColumnHeader);
    localCellConstraints.xywh(4, i, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mMiscColumnHeader, localCellConstraints);
    return localJPanel;
  }
  
  public JPanel getConfigpanel()
  {
    return null;
  }
  
  public boolean isPrintingUIValid()
  {
    return true;
  }
  
  public boolean writeConfig()
  {
    if (!writeBillConfig()) {
      return false;
    }
    return writeMiscConfig();
  }
  
  private boolean writeBillConfig()
  {
    Config.INSTANCE.setAttrib("JB_CONFIG.PRINTER.LASER.BILL.COLUMNS.VALUE", this._mBillDrawColumnLines.isSelected() ? "TRUE" : "FALSE");
    Config.INSTANCE.setAttrib("JB_CONFIG.PRINTER.LASER.BILL.BARCODE.VALUE", this._mBillBarCode.isSelected() ? "TRUE" : "FALSE");
    Config.INSTANCE.setAttrib("JB_CONFIG.PRINTER.LASER.BILL.ROWS.VALUE", this._mBillDrawRowLines.isSelected() ? "TRUE" : "FALSE");
    Config.INSTANCE.setAttrib("JB_CONFIG.PRINTER.LASER.BILL.PRINT_COLUMN_HEADER.VALUE", this._mBillColumnHeader.isSelected() ? "TRUE" : "FALSE");
    String str = getColumnString();
    Config.INSTANCE.setAttrib("JB_CONFIG.PRINTER.LASER.BILL.COLUMN_STRING.VALUE", str);
    int i = convertInchToPix(Double.valueOf(this._mBillHeaderGap.getValue().toString()).doubleValue());
    Config.INSTANCE.setAttrib("JB_CONFIG.PRINTER.LASER.BILL.HEADER_GAP.VALUE", Integer.valueOf(i).toString());
    i = convertInchToPix(Double.valueOf(this._mBillLeftMargin.getValue().toString()).doubleValue());
    Config.INSTANCE.setAttrib("JB_CONFIG.PRINTER.LASER.BILL.LEFT_MARGIN.VALUE", Integer.valueOf(i).toString());
    i = convertInchToPix(Double.valueOf(this._mBillTopMargin.getValue().toString()).doubleValue());
    Config.INSTANCE.setAttrib("JB_CONFIG.PRINTER.LASER.BILL.TOP_MARGIN.VALUE", Integer.valueOf(i).toString());
    i = convertInchToPix(Double.valueOf(this._mBillBottomMargin.getValue().toString()).doubleValue());
    Config.INSTANCE.setAttrib("JB_CONFIG.PRINTER.LASER.BILL.BOTTOM_MARGIN.VALUE", Integer.valueOf(i).toString());
    return true;
  }
  
  private boolean writeMiscConfig()
  {
    Config.INSTANCE.setAttrib("JB_CONFIG.PRINTER.LASER.GENERAL.COLUMNS.VALUE", this._mMiscDrawColumnLines.isSelected() ? "TRUE" : "FALSE");
    Config.INSTANCE.setAttrib("JB_CONFIG.PRINTER.LASER.GENERAL.ROWS.VALUE", this._mMiscDrawRowLines.isSelected() ? "TRUE" : "FALSE");
    Config.INSTANCE.setAttrib("JB_CONFIG.PRINTER.LASER.GENERAL.PRINT_COLUMN_HEADER.VALUE", this._mMiscColumnHeader.isSelected() ? "TRUE" : "FALSE");
    int i = convertInchToPix(Double.valueOf(this._mMiscLeftMargin.getValue().toString()).doubleValue());
    Config.INSTANCE.setAttrib("JB_CONFIG.PRINTER.LASER.GENERAL.LEFT_MARGIN.VALUE", Integer.valueOf(i).toString());
    i = convertInchToPix(Double.valueOf(this._mMiscRightMargin.getValue().toString()).doubleValue());
    Config.INSTANCE.setAttrib("JB_CONFIG.PRINTER.LASER.GENERAL.RIGHT_MARGIN.VALUE", Integer.valueOf(i).toString());
    i = convertInchToPix(Double.valueOf(this._mMiscTopMargin.getValue().toString()).doubleValue());
    Config.INSTANCE.setAttrib("JB_CONFIG.PRINTER.LASER.GENERAL.TOP_MARGIN.VALUE", Integer.valueOf(i).toString());
    i = convertInchToPix(Double.valueOf(this._mMiscBottomMargin.getValue().toString()).doubleValue());
    Config.INSTANCE.setAttrib("JB_CONFIG.PRINTER.LASER.GENERAL.BOTTOM_MARGIN.VALUE", Integer.valueOf(i).toString());
    return true;
  }
  
  public void readConfig()
  {
    readBillConfig();
    readMiscConfig();
  }
  
  public void readBillConfig()
  {
    String str1 = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.LASER.BILL.COLUMNS.VALUE");
    this._mBillDrawColumnLines.setSelected((str1 != null) && (str1.equals("TRUE")));
    str1 = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.LASER.BILL.ROWS.VALUE");
    this._mBillDrawRowLines.setSelected((str1 != null) && (str1.equals("TRUE")));
    str1 = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.LASER.BILL.BARCODE.VALUE");
    this._mBillBarCode.setSelected((str1 != null) && (str1.equals("TRUE")));
    str1 = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.LASER.BILL.PRINT_COLUMN_HEADER.VALUE");
    this._mBillColumnHeader.setSelected((str1 != null) && (str1.equals("TRUE")));
    str1 = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.LASER.BILL.HEADER_GAP.VALUE");
    double d;
    if ((str1 != null) && (str1.length() > 0))
    {
      d = convertPixToInch(Integer.valueOf(str1).intValue());
      this._mBillHeaderGap.setValue(Double.valueOf(d));
    }
    else
    {
      this._mBillHeaderGap.setValue(Double.valueOf(0.0D));
    }
    str1 = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.LASER.BILL.LEFT_MARGIN.VALUE");
    if ((str1 != null) && (str1.length() > 0))
    {
      d = convertPixToInch(Integer.valueOf(str1).intValue());
      this._mBillLeftMargin.setValue(Double.valueOf(d));
    }
    else
    {
      this._mBillLeftMargin.setValue(Double.valueOf(0.0D));
    }
    str1 = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.LASER.BILL.TOP_MARGIN.VALUE");
    if ((str1 != null) && (str1.length() > 0))
    {
      d = convertPixToInch(Integer.valueOf(str1).intValue());
      this._mBillTopMargin.setValue(Double.valueOf(d));
    }
    else
    {
      this._mBillTopMargin.setValue(Double.valueOf(0.0D));
    }
    str1 = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.LASER.BILL.BOTTOM_MARGIN.VALUE");
    if ((str1 != null) && (str1.length() > 0))
    {
      d = convertPixToInch(Integer.valueOf(str1).intValue());
      this._mBillBottomMargin.setValue(Double.valueOf(d));
    }
    else
    {
      this._mBillBottomMargin.setValue(Double.valueOf(0.0D));
    }
    ArrayList localArrayList = BillPrintCommon.getInstance().getPrintColumns();
    boolean[] arrayOfBoolean = new boolean[PrintAvailableColumns.length];
    int[] arrayOfInt = new int[PrintAvailableColumns.length];
    String str2 = str1 = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.LASER.BILL.COLUMN_STRING.VALUE");
    Object localObject1;
    Object localObject2;
    if ((str2 != null) && (str2.length() > 0))
    {
      StringTokenizer localStringTokenizer = new StringTokenizer(str2, ",");
      for (localObject1 = localStringTokenizer.nextToken(); localObject1 != null; localObject1 = localStringTokenizer.hasMoreTokens() ? localStringTokenizer.nextToken() : null)
      {
        localObject2 = ((String)localObject1).substring(0, 2);
        int j = Integer.valueOf(((String)localObject1).substring(3)).intValue();
        for (int m = 0; m < PrintAvailableColumns.length; m++) {
          if (((String)localObject2).equals(PrintAvailableColumns[m].code))
          {
            arrayOfBoolean[m] = true;
            arrayOfInt[m] = j;
          }
        }
      }
    }
    this._mModel.setRowCount(0);
    for (int i = 0; i < PrintAvailableColumns.length; i++)
    {
      localObject1 = localArrayList.iterator();
      localObject2 = null;
      while (((Iterator)localObject1).hasNext())
      {
        PrintColumn localPrintColumn = (PrintColumn)((Iterator)localObject1).next();
        if (localPrintColumn.toString().equals(PrintAvailableColumns[i].toString()))
        {
          localObject2 = localPrintColumn;
          break;
        }
      }
      int k = arrayOfBoolean[i] != 0 ? arrayOfInt[i] : PrintAvailableColumns[i].width;
      Object[] arrayOfObject;
      if (localObject2 == null)
      {
        arrayOfObject = new Object[] { PrintAvailableColumns[i], Boolean.valueOf(arrayOfBoolean[i]), Integer.valueOf(k) };
        this._mModel.addRow(arrayOfObject);
      }
      else
      {
        arrayOfObject = new Object[] { localObject2, Boolean.valueOf(arrayOfBoolean[i]), Integer.valueOf(k) };
        this._mModel.addRow(arrayOfObject);
      }
    }
    this._mWidthsTable.updateUI();
  }
  
  public void readMiscConfig()
  {
    String str = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.LASER.GENERAL.COLUMNS.VALUE");
    this._mMiscDrawColumnLines.setSelected((str != null) && (str.equals("TRUE")));
    str = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.LASER.GENERAL.ROWS.VALUE");
    this._mMiscDrawRowLines.setSelected((str != null) && (str.equals("TRUE")));
    str = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.LASER.GENERAL.PRINT_COLUMN_HEADER.VALUE");
    this._mMiscColumnHeader.setSelected((str != null) && (str.equals("TRUE")));
    str = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.LASER.GENERAL.LEFT_MARGIN.VALUE");
    double d;
    if ((str != null) && (str.length() > 0))
    {
      d = convertPixToInch(Integer.valueOf(str).intValue());
      this._mMiscLeftMargin.setValue(Double.valueOf(d));
    }
    else
    {
      this._mMiscLeftMargin.setValue(Double.valueOf(0.0D));
    }
    str = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.LASER.GENERAL.RIGHT_MARGIN.VALUE");
    if ((str != null) && (str.length() > 0))
    {
      d = convertPixToInch(Integer.valueOf(str).intValue());
      this._mMiscRightMargin.setValue(Double.valueOf(d));
    }
    else
    {
      this._mMiscRightMargin.setValue(Double.valueOf(0.0D));
    }
    str = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.LASER.GENERAL.TOP_MARGIN.VALUE");
    if ((str != null) && (str.length() > 0))
    {
      d = convertPixToInch(Integer.valueOf(str).intValue());
      this._mMiscTopMargin.setValue(Double.valueOf(d));
    }
    else
    {
      this._mMiscTopMargin.setValue(Double.valueOf(0.0D));
    }
    str = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.LASER.GENERAL.BOTTOM_MARGIN.VALUE");
    if ((str != null) && (str.length() > 0))
    {
      d = convertPixToInch(Integer.valueOf(str).intValue());
      this._mMiscBottomMargin.setValue(Double.valueOf(d));
    }
    else
    {
      this._mMiscBottomMargin.setValue(Double.valueOf(0.0D));
    }
  }
  
  private String getColumnString()
  {
    int i = this._mModel.getRowCount();
    StringBuffer localStringBuffer = new StringBuffer();
    int j = 0;
    for (int k = 0; k < i; k++)
    {
      Boolean localBoolean = (Boolean)this._mModel.getValueAt(k, 1);
      boolean bool = localBoolean.booleanValue();
      if (bool)
      {
        int m = ((Integer)this._mModel.getValueAt(k, 2)).intValue();
        String str = PrintAvailableColumns[k].code;
        if (j != 0) {
          localStringBuffer.append(",");
        }
        localStringBuffer.append(str + ":");
        localStringBuffer.append(m);
        j = 1;
      }
    }
    return localStringBuffer.toString();
  }
  
  private double convertPixToInch(int paramInt)
  {
    return paramInt / 76.0D;
  }
  
  private int convertInchToPix(double paramDouble)
  {
    return (int)(paramDouble * 76.0D);
  }
  
  private class MyTableModel
    extends DefaultTableModel
  {
    String[] columnNames = { "Column Name", "Print", "Width" };
    
    private MyTableModel() {}
    
    public String getColumnName(int paramInt)
    {
      return this.columnNames[paramInt];
    }
    
    public boolean isCellEditable(int paramInt1, int paramInt2)
    {
      return (paramInt2 == 2) || (paramInt2 == 1);
    }
    
    public int getColumnCount()
    {
      return this.columnNames.length;
    }
    
    public Class getColumnClass(int paramInt)
    {
      switch (paramInt)
      {
      case 0: 
        return PrintColumn.class;
      case 1: 
        return Boolean.class;
      case 2: 
        return Integer.class;
      }
      return null;
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.printing.laser.LaserPrinterConfigPanel
 * JD-Core Version:    0.7.0.1
 */