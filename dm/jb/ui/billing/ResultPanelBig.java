package dm.jb.ui.billing;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.ui.common.HelpButton;
import dm.jb.ui.common.JBShadowPanel;
import dm.jb.ui.common.JBTransparentWindow;
import dm.tools.db.DBRow;
import dm.tools.ui.NonEditableJXTable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXPanel;

public class ResultPanelBig
  extends JBTransparentWindow
{
  private String[] _mAttribs = null;
  private NonEditableJXTable _mResultTable = null;
  private boolean _mCancelled = true;
  private JButton _mSearchBtn = null;
  private DBRow _mSelectedRow = null;
  private JBShadowPanel _mShadowPanel = null;
  private String _mHelpKey = null;
  
  public ResultPanelBig(String[] paramArrayOfString, JFrame paramJFrame, String paramString)
  {
    super(paramJFrame, true);
    setUndecorated(true);
    this._mHelpKey = paramString;
    this._mAttribs = paramArrayOfString;
    initUI();
    pack();
    addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 27) {
          ResultPanelBig.this.setVisible(false);
        }
      }
    });
    InputMap localInputMap = ((JPanel)getContentPane()).getInputMap(0);
    String str = "ESCAction";
    localInputMap.put(KeyStroke.getKeyStroke("ESCAPE"), str);
    AbstractAction local2 = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ResultPanelBig.this.setVisible(false);
      }
    };
    ((JPanel)getContentPane()).getActionMap().put(str, local2);
    addWindowListener(new WindowAdapter()
    {
      public void windowOpened(WindowEvent paramAnonymousWindowEvent)
      {
        ResultPanelBig.this._mResultTable.requestFocusInWindow();
      }
    });
  }
  
  public void setDefaultFocus() {}
  
  public boolean isCancelled()
  {
    return this._mCancelled;
  }
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("30px,800px,30px", "30px,300px:grow,20px,30px,30px");
    JPanel localJPanel = (JPanel)getContentPane();
    this._mShadowPanel = new JBShadowPanel(new Color(217, 197, 166));
    JBShadowPanel localJBShadowPanel = this._mShadowPanel;
    localJPanel.setLayout(new BorderLayout());
    localJPanel.add(localJBShadowPanel, "Center");
    localJBShadowPanel.setLayout(localFormLayout);
    this._mResultTable = new NonEditableJXTable(new ResultTableModel());
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(2, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJBShadowPanel.add(new JScrollPane(this._mResultTable), localCellConstraints);
    this._mResultTable.addKeyListener(new KeyAdapter()
    {
      public void keyTyped(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyChar() == '\n')
        {
          paramAnonymousKeyEvent.consume();
          ResultPanelBig.this.selectButtonClicked();
        }
        else if (paramAnonymousKeyEvent.getKeyChar() == '\033')
        {
          paramAnonymousKeyEvent.consume();
          ResultPanelBig.this.setVisible(false);
        }
      }
    });
    this._mResultTable.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
      {
        if (paramAnonymousMouseEvent.getClickCount() == 2) {
          ResultPanelBig.this.selectButtonClicked();
        }
      }
    });
    this._mResultTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent paramAnonymousListSelectionEvent)
      {
        if (paramAnonymousListSelectionEvent.getValueIsAdjusting()) {
          return;
        }
        int[] arrayOfInt = ResultPanelBig.this._mResultTable.getSelectedRows();
        ResultPanelBig.this._mSelectedRow = null;
        if (arrayOfInt.length == 0)
        {
          ResultPanelBig.this._mSearchBtn.setEnabled(false);
          return;
        }
        ResultPanelBig.this._mSearchBtn.setEnabled(true);
      }
    });
    localCellConstraints.xywh(2, 3, localFormLayout.getColumnCount() - 2, 1, CellConstraints.FILL, CellConstraints.CENTER);
    localJBShadowPanel.add(new JSeparator(), localCellConstraints);
    localCellConstraints.xywh(2, 4, localFormLayout.getColumnCount() - 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJBShadowPanel.add(getButtonPanel(), localCellConstraints);
    setTitleFonts();
  }
  
  private void setTitleFonts()
  {
    TableColumnModel localTableColumnModel = this._mResultTable.getColumnModel();
    final JTableHeader localJTableHeader = this._mResultTable.getTableHeader();
    Font localFont1 = localJTableHeader.getFont();
    final Font localFont2 = new Font(localFont1.getName(), 1, 14);
    for (int i = 0; i < localTableColumnModel.getColumnCount(); i++)
    {
      localTableColumnModel.getColumn(i).setPreferredWidth(250);
      localTableColumnModel.getColumn(i).setCellRenderer(new TestRenderer());
      localTableColumnModel.getColumn(i).setHeaderRenderer(new TableCellRenderer()
      {
        public Component getTableCellRendererComponent(JTable paramAnonymousJTable, Object paramAnonymousObject, boolean paramAnonymousBoolean1, boolean paramAnonymousBoolean2, int paramAnonymousInt1, int paramAnonymousInt2)
        {
          TableCellRenderer localTableCellRenderer = localJTableHeader.getDefaultRenderer();
          Component localComponent = localTableCellRenderer.getTableCellRendererComponent(paramAnonymousJTable, paramAnonymousObject, paramAnonymousBoolean1, paramAnonymousBoolean2, paramAnonymousInt1, paramAnonymousInt2);
          localComponent.setFont(localFont2);
          return localComponent;
        }
      });
    }
    localTableColumnModel.getColumn(0).setPreferredWidth(140);
  }
  
  private JPanel getButtonPanel()
  {
    JXPanel localJXPanel = new JXPanel();
    FormLayout localFormLayout = new FormLayout("100px,10px,100px,pref:grow,100px", "30px");
    localJXPanel.setLayout(localFormLayout);
    localJXPanel.setOpaque(false);
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    Object localObject = new JXButton("Select");
    ((JXButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ResultPanelBig.this.selectButtonClicked();
      }
    });
    this._mSearchBtn = ((JButton)localObject);
    localJXPanel.add((Component)localObject, localCellConstraints);
    ((JXButton)localObject).setBackground(this._mShadowPanel.getBackground());
    localCellConstraints.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localObject = new JXButton("Close");
    localJXPanel.add((Component)localObject, localCellConstraints);
    ((JXButton)localObject).setBackground(this._mShadowPanel.getBackground());
    ((JXButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ResultPanelBig.this.setVisible(false);
      }
    });
    localCellConstraints.xywh(5, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localObject = new HelpButton(this._mHelpKey, true);
    ((JXButton)localObject).setBackground(this._mShadowPanel.getBackground());
    localJXPanel.add((Component)localObject, localCellConstraints);
    return localJXPanel;
  }
  
  public void setData(ArrayList<DBRow> paramArrayList)
  {
    ResultTableModel localResultTableModel = (ResultTableModel)this._mResultTable.getModel();
    this._mSelectedRow = null;
    for (int i = 0; i < localResultTableModel.getRowCount(); i++) {
      localResultTableModel.removeRow(0);
    }
    for (i = 0; i < paramArrayList.size(); i++) {
      localResultTableModel.addRow((DBRow)paramArrayList.get(i));
    }
    this._mSearchBtn.setEnabled(false);
  }
  
  private void selectButtonClicked()
  {
    int i = this._mResultTable.getSelectedRow();
    i = this._mResultTable.convertRowIndexToModel(i);
    ResultTableModel localResultTableModel = (ResultTableModel)this._mResultTable.getModel();
    DBRow localDBRow = localResultTableModel.getRow(i);
    this._mSelectedRow = localDBRow;
    this._mCancelled = false;
    setVisible(false);
  }
  
  public DBRow getSelectedRow()
  {
    return this._mSelectedRow;
  }
  
  class TestRenderer
    extends DefaultTableCellRenderer
  {
    TestRenderer() {}
    
    public Component getTableCellRendererComponent(JTable paramJTable, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2)
    {
      super.getTableCellRendererComponent(paramJTable, paramObject, paramBoolean1, paramBoolean2, paramInt1, paramInt2);
      Font localFont1 = getFont();
      Font localFont2 = new Font(localFont1.getName(), localFont1.getStyle(), 14);
      setFont(localFont2);
      return this;
    }
  }
  
  public class ResultTableModel
    extends DefaultTableModel
  {
    ArrayList<DBRow> _mData = new ArrayList();
    
    public ResultTableModel() {}
    
    public int getColumnCount()
    {
      return ResultPanelBig.this._mAttribs.length / 2;
    }
    
    public void removeRow(int paramInt)
    {
      this._mData.remove(paramInt);
      super.removeRow(paramInt);
    }
    
    public void addRow(DBRow paramDBRow)
    {
      Object[] arrayOfObject = new Object[ResultPanelBig.this._mAttribs.length / 2];
      for (int i = 0; i < ResultPanelBig.this._mAttribs.length / 2; i++) {
        arrayOfObject[i] = paramDBRow.getValue(ResultPanelBig.this._mAttribs[(i * 2)]);
      }
      super.addRow(arrayOfObject);
      this._mData.add(paramDBRow);
    }
    
    public DBRow getRow(int paramInt)
    {
      return (DBRow)this._mData.get(paramInt);
    }
    
    public String getColumnName(int paramInt)
    {
      return ResultPanelBig.this._mAttribs[(paramInt * 2 + 1)];
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.billing.ResultPanelBig
 * JD-Core Version:    0.7.0.1
 */