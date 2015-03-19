package dm.jb.printing.dotmatrix;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.printing.BillPrintCommon;
import dm.jb.printing.PrintColumn;
import dm.jb.ui.MainWindow;
import dm.jb.ui.common.ShortKeyCommon;
import dm.tools.printing.PrintingUIClassIf;
import dm.tools.ui.UICommon;
import dm.tools.utils.Config;
import dm.tools.utils.Validation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.jdesktop.swingx.JXButton;

public class DotMatrixPrintSetupPanel
  extends PrintingUIClassIf
{
  private JTextField _mPrintCommand = null;
  private JTable _mWidthsTable = null;
  private MyTableModel _mModel = null;
  private JTextField _mHeaderFile = null;
  private JCheckBox _mDrawColumnLines = null;
  private JCheckBox _mColumnHeader = null;
  private JTextField _mTotalBillRowsPerPage = null;
  private JTextField _mHeaderGap = null;
  private JCheckBox _mMultiPage = null;
  private JLabel _mMaxBillLbl = null;
  private JCheckBox _mPrintHeaderEachPage = null;
  private JCheckBox _mPrintColHeaderEachPage = null;
  private JTextField _mPerPageHeaderGap = null;
  private JLabel _mPerPageHeaderGaplbl = null;
  private JTextField _mPageEndGap = null;
  private JLabel _mPageEndGaplbl = null;
  public static PrintColumn[] PrintAvailableColumns = { new PrintColumn("Sl. No", "SL", 5, 4), new PrintColumn("Code", "PC", 6, 4), new PrintColumn("Product Name", "PN", 25, 2), new PrintColumn("Expiry", "EX", 12, 2), new PrintColumn("Unit price", "UP", 12, 4), new PrintColumn("Quantity", "QT", 12, 4), new PrintColumn("Amount", "AM", 7, 4), new PrintColumn("Discount", "DC", 7, 4) };
  
  public DotMatrixPrintSetupPanel()
  {
    initUI();
  }
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("10px, pref, 10px, 275px,10px", "180px,pref:grow");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(2, 1, 1, 2, CellConstraints.FILL, CellConstraints.FILL);
    add(getPrintConfigPanel(), localCellConstraints);
    this._mModel = new MyTableModel(null);
    this._mWidthsTable = new JTable(this._mModel);
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.TOP);
    JScrollPane localJScrollPane = new JScrollPane(this._mWidthsTable);
    add(localJScrollPane, localCellConstraints);
    this._mWidthsTable.setAutoResizeMode(0);
    this._mWidthsTable.getColumnModel().getColumn(0).setPreferredWidth(150);
    this._mWidthsTable.getColumnModel().getColumn(1).setPreferredWidth(60);
    this._mWidthsTable.getColumnModel().getColumn(2).setPreferredWidth(60);
  }
  
  private JPanel getPrintConfigPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("90px, 10px, 60px, 140px,2px,30px", "25px,10px,25px,10px,25px,10px,25px,10px,25px,10px,pref");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    JLabel localJLabel = new JLabel("Print Command : ");
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mPrintCommand = new JTextField();
    localCellConstraints.xywh(3, 1, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mPrintCommand, localCellConstraints);
    this._mPrintCommand.setBackground(UICommon.MANDATORY_COLOR);
    localJLabel = new JLabel("Header File : ");
    localCellConstraints.xywh(1, 3, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mHeaderFile = new JTextField();
    localCellConstraints.xywh(3, 3, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mHeaderFile, localCellConstraints);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 65, this._mHeaderFile);
    ImageIcon localImageIcon = new ImageIcon(getClass().getResource("/dm/jb/images/file_folder.gif"));
    JXButton localJXButton = new JXButton(localImageIcon);
    localCellConstraints.xywh(6, 3, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    localJLabel = new JLabel("Header Gap : ");
    localCellConstraints.xywh(1, 5, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mHeaderGap = new JTextField();
    localCellConstraints.xywh(3, 5, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mHeaderGap, localCellConstraints);
    this._mDrawColumnLines = new JCheckBox("Draw column border");
    localCellConstraints.xywh(3, 7, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mDrawColumnLines, localCellConstraints);
    this._mColumnHeader = new JCheckBox("Print column headers");
    localCellConstraints.xywh(3, 9, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mColumnHeader, localCellConstraints);
    localCellConstraints.xywh(1, 11, 6, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(getMultiPagePanel(), localCellConstraints);
    return localJPanel;
  }
  
  public void readConfig()
  {
    String str1 = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.DOT_MATRIX.GENERAL.PRINT_COMMAND");
    if (str1 != null) {
      this._mPrintCommand.setText(str1);
    }
    str1 = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.DOT_MATRIX.GENERAL.HEADER_FILE");
    if (str1 != null) {
      this._mHeaderFile.setText(str1);
    }
    str1 = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.LINES.COLUMNS");
    this._mDrawColumnLines.setSelected((str1 != null) && (str1.equals("TRUE")));
    str1 = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.DOT_MATRIX.GENERAL.PRINT_COLUMN_HEADER");
    this._mColumnHeader.setSelected((str1 != null) && (str1.equals("TRUE")));
    str1 = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.DOT_MATRIX.MULTI_PAGE.MAX_ROWS");
    if ((str1 != null) && (str1.length() > 0) && (!str1.equals("-1"))) {
      this._mTotalBillRowsPerPage.setText(str1);
    }
    str1 = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.DOT_MATRIX.GENERAL.HEADER_GAP");
    if ((str1 != null) && (str1.length() > 0)) {
      this._mHeaderGap.setText(str1);
    }
    str1 = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.DOT_MATRIX.MULTI_PAGE.ENABLED");
    this._mMultiPage.setSelected((str1 != null) && (str1.equals("TRUE")));
    str1 = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.DOT_MATRIX.MULTI_PAGE.PRINT_HEADER_EACH");
    this._mPrintHeaderEachPage.setSelected((str1 != null) && (str1.equals("TRUE")));
    str1 = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.DOT_MATRIX.MULTI_PAGE.PRINT_COLUMN_HEADER_EACH");
    this._mPrintColHeaderEachPage.setSelected((str1 != null) && (str1.equals("TRUE")));
    str1 = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.DOT_MATRIX.MULTI_PAGE.PER_PAGE_HEADER_GAP");
    this._mPerPageHeaderGap.setText(str1);
    str1 = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.DOT_MATRIX.MULTI_PAGE.PAGE_END_GAP");
    this._mPageEndGap.setText(str1);
    ArrayList localArrayList = BillPrintCommon.getInstance().getPrintColumns();
    boolean[] arrayOfBoolean = new boolean[PrintAvailableColumns.length];
    int[] arrayOfInt = new int[PrintAvailableColumns.length];
    String str2 = str1 = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.DOT_MATRIX.COLUMN_STRING.VALUE");
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
    if (this._mMultiPage.isSelected())
    {
      this._mPrintColHeaderEachPage.setEnabled(true);
      this._mTotalBillRowsPerPage.setEnabled(true);
      this._mTotalBillRowsPerPage.setEditable(true);
      this._mMaxBillLbl.setEnabled(true);
      this._mPrintHeaderEachPage.setEnabled(true);
      if (this._mPrintHeaderEachPage.isSelected())
      {
        this._mPerPageHeaderGaplbl.setEnabled(false);
        this._mPerPageHeaderGap.setEditable(false);
        this._mPerPageHeaderGap.setEnabled(false);
      }
      else
      {
        this._mPerPageHeaderGaplbl.setEnabled(true);
        this._mPerPageHeaderGap.setEditable(true);
        this._mPerPageHeaderGap.setEnabled(true);
      }
      this._mPageEndGaplbl.setEnabled(true);
      this._mPageEndGap.setEditable(true);
      this._mPageEndGap.setEnabled(true);
    }
    else
    {
      this._mPerPageHeaderGaplbl.setEnabled(false);
      this._mPerPageHeaderGap.setEditable(false);
      this._mPrintColHeaderEachPage.setEnabled(false);
      this._mTotalBillRowsPerPage.setEnabled(false);
      this._mTotalBillRowsPerPage.setEditable(false);
      this._mMaxBillLbl.setEnabled(false);
      this._mPrintHeaderEachPage.setEnabled(false);
      this._mPerPageHeaderGap.setEnabled(false);
      this._mPageEndGaplbl.setEnabled(false);
      this._mPageEndGap.setEditable(false);
      this._mPageEndGap.setEnabled(false);
    }
  }
  
  private JPanel getMultiPagePanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("90px, 10px, 60px, 140px,2px,30px", "25px,10px,25px,10px,25px,10px,25px,10px,25px,10px,25px");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    this._mMultiPage = new JCheckBox("Multi-page printing");
    localCellConstraints.xywh(3, 1, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mMultiPage, localCellConstraints);
    this._mMultiPage.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (DotMatrixPrintSetupPanel.this._mMultiPage.isSelected())
        {
          DotMatrixPrintSetupPanel.this._mTotalBillRowsPerPage.setEnabled(true);
          DotMatrixPrintSetupPanel.this._mTotalBillRowsPerPage.setEditable(true);
          DotMatrixPrintSetupPanel.this._mMaxBillLbl.setEnabled(true);
          DotMatrixPrintSetupPanel.this._mPrintHeaderEachPage.setEnabled(true);
          DotMatrixPrintSetupPanel.this._mPrintColHeaderEachPage.setEnabled(true);
          DotMatrixPrintSetupPanel.this._mPerPageHeaderGaplbl.setEnabled(true);
          DotMatrixPrintSetupPanel.this._mPageEndGap.setEditable(true);
          DotMatrixPrintSetupPanel.this._mPageEndGap.setEnabled(true);
          DotMatrixPrintSetupPanel.this._mPageEndGaplbl.setEnabled(true);
          if (DotMatrixPrintSetupPanel.this._mPrintHeaderEachPage.isSelected())
          {
            DotMatrixPrintSetupPanel.this._mPerPageHeaderGaplbl.setEnabled(false);
            DotMatrixPrintSetupPanel.this._mPerPageHeaderGap.setEditable(false);
            DotMatrixPrintSetupPanel.this._mPerPageHeaderGap.setEnabled(false);
          }
          else
          {
            DotMatrixPrintSetupPanel.this._mPerPageHeaderGaplbl.setEnabled(true);
            DotMatrixPrintSetupPanel.this._mPerPageHeaderGap.setEditable(true);
            DotMatrixPrintSetupPanel.this._mPerPageHeaderGap.setEnabled(true);
          }
        }
        else
        {
          DotMatrixPrintSetupPanel.this._mTotalBillRowsPerPage.setEnabled(false);
          DotMatrixPrintSetupPanel.this._mTotalBillRowsPerPage.setEditable(false);
          DotMatrixPrintSetupPanel.this._mMaxBillLbl.setEnabled(false);
          DotMatrixPrintSetupPanel.this._mPrintHeaderEachPage.setEnabled(false);
          DotMatrixPrintSetupPanel.this._mPrintColHeaderEachPage.setEnabled(false);
          DotMatrixPrintSetupPanel.this._mPerPageHeaderGaplbl.setEnabled(false);
          DotMatrixPrintSetupPanel.this._mPerPageHeaderGap.setEditable(false);
          DotMatrixPrintSetupPanel.this._mPerPageHeaderGap.setEnabled(false);
          DotMatrixPrintSetupPanel.this._mPageEndGap.setEditable(false);
          DotMatrixPrintSetupPanel.this._mPageEndGap.setEnabled(false);
          DotMatrixPrintSetupPanel.this._mPageEndGaplbl.setEnabled(false);
        }
      }
    });
    this._mMaxBillLbl = new JLabel("Max. Bill Rows : ");
    localCellConstraints.xywh(1, 3, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(this._mMaxBillLbl, localCellConstraints);
    this._mTotalBillRowsPerPage = new JTextField();
    localCellConstraints.xywh(3, 3, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mTotalBillRowsPerPage, localCellConstraints);
    this._mTotalBillRowsPerPage.setBackground(UICommon.MANDATORY_COLOR);
    this._mPerPageHeaderGaplbl = new JLabel("Page hdr size : ");
    localCellConstraints.xywh(1, 5, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(this._mPerPageHeaderGaplbl, localCellConstraints);
    this._mPerPageHeaderGap = new JTextField();
    localCellConstraints.xywh(3, 5, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mPerPageHeaderGap, localCellConstraints);
    this._mPageEndGaplbl = new JLabel("Page end gap : ");
    localCellConstraints.xywh(1, 7, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(this._mPageEndGaplbl, localCellConstraints);
    this._mPageEndGap = new JTextField();
    localCellConstraints.xywh(3, 7, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mPageEndGap, localCellConstraints);
    this._mPrintHeaderEachPage = new JCheckBox("Print page header on each page");
    localCellConstraints.xywh(3, 9, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mPrintHeaderEachPage, localCellConstraints);
    this._mPrintHeaderEachPage.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (DotMatrixPrintSetupPanel.this._mPrintHeaderEachPage.isSelected())
        {
          DotMatrixPrintSetupPanel.this._mPerPageHeaderGaplbl.setEnabled(false);
          DotMatrixPrintSetupPanel.this._mPerPageHeaderGap.setEditable(false);
          DotMatrixPrintSetupPanel.this._mPerPageHeaderGap.setEnabled(false);
        }
        else
        {
          DotMatrixPrintSetupPanel.this._mPerPageHeaderGaplbl.setEnabled(true);
          DotMatrixPrintSetupPanel.this._mPerPageHeaderGap.setEditable(true);
          DotMatrixPrintSetupPanel.this._mPerPageHeaderGap.setEnabled(true);
        }
      }
    });
    this._mPrintColHeaderEachPage = new JCheckBox("Print column header on each page");
    localCellConstraints.xywh(3, 11, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mPrintColHeaderEachPage, localCellConstraints);
    return localJPanel;
  }
  
  public boolean isPrintingUIValid()
  {
    String str = this._mPrintCommand.getText().trim();
    if (str.length() == 0)
    {
      UICommon.showError("Print command cannot be empty.", "Error", MainWindow.instance);
      this._mPrintCommand.requestFocusInWindow();
      return false;
    }
    if (str.length() > 32)
    {
      UICommon.showError("Print command cannot exceed 256 characters.", "Error", MainWindow.instance);
      this._mPrintCommand.requestFocusInWindow();
      return false;
    }
    if (!str.contains("#PRINT_FILE"))
    {
      UICommon.showError("The print command show have #PRINT_FILE in it.", "Error", MainWindow.instance);
      this._mPrintCommand.requestFocusInWindow();
      return false;
    }
    str = this._mHeaderFile.getText();
    if ((str.length() > 0) && (!isHeaderTextValid(str)))
    {
      UICommon.showError("Header text file contains invalid characters or file not found.", "Error", MainWindow.instance);
      this._mHeaderFile.requestFocusInWindow();
      return false;
    }
    str = this._mHeaderGap.getText();
    if ((str.length() > 0) && (!Validation.isValidInt(str, false)))
    {
      UICommon.showError("Header gap is invalid.", "Error", MainWindow.instance);
      this._mHeaderGap.requestFocusInWindow();
      return false;
    }
    return (!this._mMultiPage.isSelected()) || (isMultiPageAttribValid());
  }
  
  private boolean isMultiPageAttribValid()
  {
    String str = this._mTotalBillRowsPerPage.getText();
    if (str.length() == 0)
    {
      UICommon.showError("If multi-page printing is enabled, maximum number of bill rows should not be empty.", "Error", MainWindow.instance);
      this._mTotalBillRowsPerPage.requestFocusInWindow();
      return false;
    }
    if (!Validation.isValidInt(str, false))
    {
      UICommon.showError("Maximum number of bill rows is invalid.", "Error", MainWindow.instance);
      this._mTotalBillRowsPerPage.requestFocusInWindow();
      return false;
    }
    str = this._mPerPageHeaderGap.getText();
    if ((str.length() > 0) && (!Validation.isValidInt(str, false)))
    {
      UICommon.showError("Per page header gap is invalid.", "Error", MainWindow.instance);
      this._mPerPageHeaderGap.requestFocusInWindow();
      return false;
    }
    str = this._mPageEndGap.getText();
    if ((str.length() > 0) && (!Validation.isValidInt(str, false)))
    {
      UICommon.showError("Page end gap is invalid.", "Error", MainWindow.instance);
      this._mPageEndGap.requestFocusInWindow();
      return false;
    }
    return true;
  }
  
  public boolean writeConfig()
  {
    Config.INSTANCE.setAttrib("JB_CONFIG.PRINTER.DOT_MATRIX.GENERAL.PRINT_COMMAND", this._mPrintCommand.getText());
    Config.INSTANCE.setAttrib("JB_CONFIG.PRINTER.DOT_MATRIX.GENERAL.HEADER_FILE", this._mHeaderFile.getText());
    Config.INSTANCE.setAttrib("JB_CONFIG.PRINTER.DOT_MATRIX.LINES.COLUMNS", this._mDrawColumnLines.isSelected() ? "TRUE" : "FALSE");
    Config.INSTANCE.setAttrib("JB_CONFIG.PRINTER.DOT_MATRIX.GENERAL.PRINT_COLUMN_HEADER", this._mColumnHeader.isSelected() ? "TRUE" : "FALSE");
    Config.INSTANCE.setAttrib("JB_CONFIG.PRINTER.DOT_MATRIX.MULTI_PAGE.ENABLED", this._mMultiPage.isSelected() ? "TRUE" : "FALSE");
    String str1 = this._mTotalBillRowsPerPage.getText().trim();
    if ((this._mColumnHeader.isSelected()) && (str1.length() > 0)) {
      Config.INSTANCE.setAttrib("JB_CONFIG.PRINTER.DOT_MATRIX.MULTI_PAGE.MAX_ROWS", str1);
    } else {
      Config.INSTANCE.setAttrib("JB_CONFIG.PRINTER.DOT_MATRIX.MULTI_PAGE.MAX_ROWS", "-1");
    }
    str1 = this._mHeaderGap.getText().trim();
    if (str1.length() > 0) {
      Config.INSTANCE.setAttrib("JB_CONFIG.PRINTER.DOT_MATRIX.GENERAL.HEADER_GAP", str1);
    } else {
      Config.INSTANCE.setAttrib("JB_CONFIG.PRINTER.DOT_MATRIX.GENERAL.HEADER_GAP", "0");
    }
    Config.INSTANCE.setAttrib("JB_CONFIG.PRINTER.DOT_MATRIX.MULTI_PAGE.PRINT_HEADER_EACH", this._mPrintHeaderEachPage.isSelected() ? "TRUE" : "FALSE");
    if (this._mPrintHeaderEachPage.isSelected())
    {
      str1 = this._mPerPageHeaderGap.getText().trim();
      Config.INSTANCE.setAttrib("JB_CONFIG.PRINTER.DOT_MATRIX.MULTI_PAGE.PER_PAGE_HEADER_GAP", str1);
    }
    else
    {
      Config.INSTANCE.setAttrib("JB_CONFIG.PRINTER.DOT_MATRIX.MULTI_PAGE.PER_PAGE_HEADER_GAP", "0");
    }
    Config.INSTANCE.setAttrib("JB_CONFIG.PRINTER.DOT_MATRIX.MULTI_PAGE.PRINT_COLUMN_HEADER_EACH", this._mPrintColHeaderEachPage.isSelected() ? "TRUE" : "FALSE");
    str1 = this._mPageEndGap.getText().trim();
    if (str1.length() > 0) {
      Config.INSTANCE.setAttrib("JB_CONFIG.PRINTER.DOT_MATRIX.MULTI_PAGE.PAGE_END_GAP", str1);
    } else {
      Config.INSTANCE.setAttrib("JB_CONFIG.PRINTER.DOT_MATRIX.MULTI_PAGE.PAGE_END_GAP", "0");
    }
    String str2 = getColumnString();
    Config.INSTANCE.setAttrib("JB_CONFIG.PRINTER.DOT_MATRIX.COLUMN_STRING.VALUE", str2);
    return true;
  }
  
  ArrayList<PrintColumn> getCurrentPrintColumns()
  {
    int i = this._mModel.getRowCount();
    ArrayList localArrayList = new ArrayList();
    for (int j = 0; j < i; j++)
    {
      PrintColumn localPrintColumn1 = (PrintColumn)this._mModel.getValueAt(j, 0);
      boolean bool = ((Boolean)this._mModel.getValueAt(j, 1)).booleanValue();
      int k = ((Integer)this._mModel.getValueAt(j, 2)).intValue();
      if (bool)
      {
        PrintColumn localPrintColumn2 = new PrintColumn(localPrintColumn1.name, localPrintColumn1.code, k, localPrintColumn1.alignment);
        localArrayList.add(localPrintColumn2);
      }
    }
    return localArrayList;
  }
  
  private boolean isHeaderTextValid(String paramString)
  {
    try
    {
      BufferedReader localBufferedReader = new BufferedReader(new FileReader(paramString));
      String str;
      while ((str = localBufferedReader.readLine()) != null) {
        if (lineContainsInValidChars(str)) {
          return false;
        }
      }
      localBufferedReader.close();
    }
    catch (IOException localIOException)
    {
      return false;
    }
    return true;
  }
  
  private boolean lineContainsInValidChars(String paramString)
  {
    int i = paramString.length();
    for (int j = 0; j < i; j++)
    {
      char c = paramString.charAt(j);
      if (((c < 'A') || (c > 'Z')) && ((c < 'a') || (c > 'z')) && ((c < 'a') || (c > 'z')) && ((c < '0') || (c > '9')) && (c != '_') && (c != '-') && (c != '|') && (c != '?') && (c != ':') && (c != '.') && (c != ',') && (c != ';') && (c != '(') && (c != ')') && (c != '[') && (c != ']') && (c != '\n') && (c != '\r') && (c != '\t') && (c != ' '))
      {
        System.err.println("INVALID CHAR IN HEADER FILE '" + c + "'.");
        return true;
      }
    }
    return false;
  }
  
  public JPanel getConfigpanel()
  {
    return null;
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
 * Qualified Name:     dm.jb.printing.dotmatrix.DotMatrixPrintSetupPanel
 * JD-Core Version:    0.7.0.1
 */