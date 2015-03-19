package dm.jb.ui.billing;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.db.objects.Payment;
import dm.jb.db.objects.StoreInfoRow;
import dm.jb.db.objects.StoreInfoTableDef;
import dm.jb.ui.common.JBShadowPanel;
import dm.jb.ui.settings.CountryInfo;
import dm.tools.types.InternalAmount;
import dm.tools.ui.UICommon;
import dm.tools.ui.components.JTextSeparator;
import dm.tools.utils.CommonConfig;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import org.jdesktop.swingx.JXButton;

public class PaymentPanel
  extends JBShadowPanel
{
  private static PaymentPanel _mInstance = null;
  boolean _mIsCancelled = true;
  private double _mAmountRequired = 0.0D;
  private JComboBox _mOptions = null;
  private JTable _mTable = null;
  private PaymentTableModel _mModel = null;
  private JLabel _mTotalAmount = null;
  private JLabel _mTotalBillAmount = null;
  private JXButton _mOKBtn = null;
  private JXButton _mApplyBtn = null;
  private boolean _mEditable = true;
  private JXButton _mDelButton = null;
  private JPanel _mCurrentOptionPanel = null;
  private int _mSelectedRow = -1;
  private ArrayList<PaymentOption> _mPaymentModes = new ArrayList();
  private PaymentModeObject _mCurrentSelectedObject = null;
  private JPanel _mDataPanel = null;
  private Payment _mCurrentPayment = null;
  private boolean _mEditing = false;
  private JLabel _mOldAmount = null;
  private JLabel _mOldAmountLbl = null;
  private JLabel _mRefundAmount = null;
  private JLabel _mRefundAmountLbl = null;
  private double _mOldAmountFloat = 0.0D;
  private JCheckBox _mAdjustMode = null;
  private JPanel _mAdjustPanel = null;
  private JPanel _mOldAmountPanel = null;
  private JTextSeparator _mAdjSeparator = null;
  private Payment _mCurrentPaymentForEdit = null;
  private double _mRefund = 0.0D;
  private double _mBillAmount = 0.0D;
  private boolean _mRefundMode = false;
  private Payment _mCurrentBillPayment = null;
  private Payment _mCurrentRefund = null;
  private Font _mCellFont = null;
  
  private PaymentPanel()
  {
    initUI();
    setColumnWidths();
    InputMap localInputMap = getInputMap(2);
    String str = "ESCAction";
    localInputMap.put(KeyStroke.getKeyStroke("ESCAPE"), str);
    Object localObject = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PaymentPanel.this.closeClicked();
      }
    };
    getActionMap().put(str, (Action)localObject);
    str = "F11Action";
    localInputMap.put(KeyStroke.getKeyStroke("F11"), str);
    localObject = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (!PaymentPanel.this._mAdjustMode.isVisible()) {
          return;
        }
        if (PaymentPanel.this._mAdjustMode.isSelected())
        {
          PaymentPanel.this._mAdjustMode.setSelected(false);
          PaymentPanel.this.setInBillRefundMode();
        }
        else
        {
          PaymentPanel.this._mAdjustMode.setSelected(true);
          PaymentPanel.this.setInBillAdjustMode();
        }
      }
    };
    getActionMap().put(str, (Action)localObject);
    str = "F7Action";
    localInputMap.put(KeyStroke.getKeyStroke("F7"), str);
    localObject = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PaymentPanel.this._mTable.requestFocusInWindow();
      }
    };
    getActionMap().put(str, (Action)localObject);
  }
  
  public static PaymentPanel getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new PaymentPanel();
    }
    return _mInstance;
  }
  
  private void initUI()
  {
    PaymentPanel localPaymentPanel = this;
    FormLayout localFormLayout = new FormLayout("30px,pref:grow,30px", "30px,pref,pref:grow,20px,pref,30px");
    localPaymentPanel.setLayout(localFormLayout);
    JPanel localJPanel = getPaymentModePanel();
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(2, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localPaymentPanel.add(getAdjPanel(), localCellConstraints);
    localCellConstraints.xywh(2, 3, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localPaymentPanel.add(localJPanel, localCellConstraints);
    localCellConstraints.xywh(2, 5, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localPaymentPanel.add(getButtonsPanel(), localCellConstraints);
  }
  
  private JPanel getAdjPanel()
  {
    JPanel localJPanel = new JPanel();
    this._mAdjustPanel = localJPanel;
    FormLayout localFormLayout = new FormLayout("pref:grow", "30px,30px,20px");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    this._mAdjustMode = new JCheckBox("Adjust Payment     [F11]");
    this._mAdjustMode.setSelected(true);
    this._mAdjustMode.setOpaque(false);
    this._mAdjustMode.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (PaymentPanel.this._mAdjustMode.isSelected()) {
          PaymentPanel.this.setInBillAdjustMode();
        } else {
          PaymentPanel.this.setInBillRefundMode();
        }
      }
    });
    Font localFont = this._mAdjustMode.getFont();
    this._mAdjustMode.setFont(new Font(localFont.getName(), 0, 20));
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.CENTER, CellConstraints.FILL);
    localJPanel.add(this._mAdjustMode, localCellConstraints);
    localCellConstraints.xywh(1, 2, 1, 1, CellConstraints.FILL, CellConstraints.CENTER);
    this._mAdjSeparator = new JTextSeparator("Old Payment Details  ", false, true);
    localJPanel.add(this._mAdjSeparator, localCellConstraints);
    localJPanel.setOpaque(false);
    return localJPanel;
  }
  
  private JPanel getPaymentModePanel()
  {
    JPanel localJPanel = new JPanel();
    localJPanel.setOpaque(false);
    this._mDataPanel = localJPanel;
    FormLayout localFormLayout = new FormLayout("200px, 10px, 50px, 10px,50px,300px:grow,10px,250px,10px", "35px,0px,30px,0px 200px,10px, 40px, 20px,pref");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    this._mOptions = new JComboBox();
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mOptions, localCellConstraints);
    Font localFont = this._mOptions.getFont();
    this._mOptions.setFont(new Font(localFont.getName(), 0, 20));
    URL localURL = getClass().getResource("/dm/jb/images/delete_20_20.png");
    JXButton localJXButton1 = new JXButton(new ImageIcon(localURL));
    localJXButton1.setBackground(getBackground());
    localCellConstraints.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton1, localCellConstraints);
    this._mDelButton = localJXButton1;
    this._mDelButton.setEnabled(false);
    Object localObject = new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PaymentPanel.this.deleteClicked();
      }
    };
    localJXButton1.addActionListener((ActionListener)localObject);
    localURL = getClass().getResource("/dm/jb/images/add_payment.gif");
    JXButton localJXButton2 = new JXButton(new ImageIcon(localURL));
    localCellConstraints.xywh(5, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton2, localCellConstraints);
    localObject = new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PaymentPanel.this._mTable.clearSelection();
      }
    };
    localJXButton2.addActionListener((ActionListener)localObject);
    localJXButton2.setBackground(getBackground());
    localCellConstraints.xywh(1, 3, 6, 1, CellConstraints.FILL, CellConstraints.CENTER);
    this._mModel = new PaymentTableModel();
    this._mTable = new JTable(this._mModel)
    {
      public boolean isCellEditable(int paramAnonymousInt1, int paramAnonymousInt2)
      {
        return false;
      }
    };
    PaymentTableCellRenderer localPaymentTableCellRenderer = new PaymentTableCellRenderer();
    this._mTable.setDefaultRenderer(Object.class, localPaymentTableCellRenderer);
    localCellConstraints.xywh(8, 1, 1, 6, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(new JScrollPane(this._mTable), localCellConstraints);
    this._mTable.getSelectionModel().setSelectionMode(0);
    this._mTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent paramAnonymousListSelectionEvent)
      {
        if (paramAnonymousListSelectionEvent.getValueIsAdjusting()) {
          return;
        }
        PaymentPanel.this.tableSelected();
      }
    });
    this._mTable.setRowHeight(25);
    this._mTable.addKeyListener(new KeyAdapter()
    {
      public void keyTyped(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyChar() == '\033')
        {
          PaymentPanel.this.closeClicked();
          return;
        }
        if (paramAnonymousKeyEvent.getKeyChar() == '\n')
        {
          PaymentPanel.this.okClicked();
          return;
        }
        if (((paramAnonymousKeyEvent.getKeyChar() == '&') || (paramAnonymousKeyEvent.getKeyChar() == '(')) && (PaymentPanel.this._mModel.getRowCount() > 0))
        {
          PaymentPanel.this._mTable.setRowSelectionInterval(0, 0);
          PaymentPanel.this.tableSelected();
        }
      }
    });
    localCellConstraints.xywh(1, 9, 9, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(getTotalAmountPanel(), localCellConstraints);
    JSeparator localJSeparator = new JSeparator();
    localCellConstraints.xywh(1, 8, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.CENTER);
    localJPanel.add(localJSeparator, localCellConstraints);
    localJSeparator.setBackground(localJPanel.getBackground());
    JXButton localJXButton3 = new JXButton("Apply/Add (F12)");
    localCellConstraints.xywh(1, 7, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton3, localCellConstraints);
    this._mApplyBtn = localJXButton3;
    localJXButton3.setBackground(getBackground());
    localFont = this._mApplyBtn.getFont();
    localFont = new Font(localFont.getName(), localFont.getStyle() | 0x1, 14);
    this._mApplyBtn.setFont(localFont);
    localJXButton3.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PaymentPanel.this._mIsCancelled = true;
        PaymentPanel.this.applyClicked();
      }
    });
    this._mOptions.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PaymentPanel.this.optionChanged();
      }
    });
    this._mTotalAmount.setText("0.00");
    fillOptions();
    this._mTotalAmount.setBackground(UICommon.MANDATORY_COLOR);
    this._mDataPanel = localJPanel;
    return localJPanel;
  }
  
  public void setStartIndex()
  {
    fillOptions();
    this._mOptions.setSelectedIndex(0);
  }
  
  private JPanel getTotalAmountPanel()
  {
    JPanel localJPanel = new JPanel();
    localJPanel.setOpaque(false);
    FormLayout localFormLayout = new FormLayout("160px,10px,140px,3px,40px,140px,140px,10px,140px,3px,pref", "35px,10px,pref");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    JLabel localJLabel = new JLabel("Sale Amount : ");
    Font localFont1 = localJLabel.getFont();
    Font localFont2 = new Font(localFont1.getName(), localFont1.getStyle(), 24);
    localJLabel.setFont(localFont2);
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mTotalBillAmount = new JLabel();
    this._mTotalBillAmount.setForeground(new Color(255, 255, 255));
    localFont1 = localJLabel.getFont();
    Font localFont3 = new Font(localFont1.getName(), 1, 24);
    localCellConstraints.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mTotalBillAmount, localCellConstraints);
    this._mTotalBillAmount.setHorizontalAlignment(4);
    this._mTotalBillAmount.setFont(localFont3);
    localJLabel = new JLabel("Total : ");
    localJLabel.setFont(localFont2);
    localCellConstraints.xywh(7, 1, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mTotalAmount = new JLabel();
    localCellConstraints.xywh(9, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mTotalAmount, localCellConstraints);
    this._mTotalAmount.setHorizontalAlignment(4);
    this._mTotalAmount.setFont(localFont3);
    localCellConstraints.xywh(1, 3, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(getOldAmountPanel(), localCellConstraints);
    return localJPanel;
  }
  
  private JPanel getOldAmountPanel()
  {
    JPanel localJPanel = new JPanel();
    this._mOldAmountPanel = localJPanel;
    localJPanel.setOpaque(false);
    FormLayout localFormLayout = new FormLayout("160px,10px,140px,3px,40px,140px,140px,10px,140px,3px,pref", "35px");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    JLabel localJLabel = new JLabel("Old Amount : ");
    Font localFont1 = localJLabel.getFont();
    Font localFont2 = new Font(localFont1.getName(), localFont1.getStyle(), 24);
    localJLabel.setFont(localFont2);
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mOldAmountLbl = localJLabel;
    this._mOldAmount = new JLabel();
    this._mOldAmount.setForeground(new Color(255, 255, 255));
    localCellConstraints.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mOldAmount, localCellConstraints);
    this._mOldAmount.setHorizontalAlignment(4);
    localFont1 = localJLabel.getFont();
    Font localFont3 = new Font(localFont1.getName(), 1, 24);
    this._mOldAmount.setFont(localFont3);
    localJLabel = new JLabel("Payable : ");
    localJLabel.setFont(localFont2);
    localCellConstraints.xywh(7, 1, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    localJPanel.add(localJLabel, localCellConstraints);
    this._mRefundAmountLbl = localJLabel;
    this._mRefundAmount = new JLabel();
    this._mRefundAmount.setForeground(new Color(255, 255, 255));
    localCellConstraints.xywh(9, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mRefundAmount, localCellConstraints);
    this._mRefundAmount.setHorizontalAlignment(4);
    this._mRefundAmount.setFont(localFont3);
    return localJPanel;
  }
  
  private JPanel getButtonsPanel()
  {
    JPanel localJPanel = new JPanel();
    localJPanel.setOpaque(false);
    FormLayout localFormLayout = new FormLayout("10px,120px,pref:grow, 120px,pref:grow, 120px, 10px", "40px");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    JXButton localJXButton = new JXButton("O K");
    Font localFont = localJXButton.getFont();
    localJXButton.setBackground(getBackground());
    localFont = new Font(localFont.getName(), localFont.getStyle() | 0x1, 14);
    localJXButton.setFont(localFont);
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PaymentPanel.this.okClicked();
      }
    });
    this._mOKBtn = localJXButton;
    localJXButton = new JXButton("Cancel");
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PaymentPanel.this._mIsCancelled = true;
        PaymentPanel.this.closeClicked();
      }
    });
    localJXButton.setFont(localFont);
    localJXButton.setBackground(getBackground());
    localJXButton = new JXButton("Help");
    localCellConstraints.xywh(6, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.setBackground(getBackground());
    localJPanel.setBackground(this._mDataPanel.getBackground());
    InputMap localInputMap = localJPanel.getInputMap(2);
    String str = "F3Action";
    localInputMap.put(KeyStroke.getKeyStroke("F3"), str);
    Object localObject = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (!PaymentPanel.this._mEditable)
        {
          ((PaymentOptionPanel)PaymentPanel.this._mCurrentOptionPanel).windowDisplayed();
          return;
        }
        ((PaymentOptionPanel)PaymentPanel.this._mCurrentOptionPanel).windowDisplayed();
      }
    };
    localJPanel.getActionMap().put(str, (Action)localObject);
    str = "F12Action";
    localInputMap.put(KeyStroke.getKeyStroke("F12"), str);
    localObject = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (!PaymentPanel.this._mEditable) {
          return;
        }
        PaymentPanel.this.applyClicked();
      }
    };
    localJPanel.getActionMap().put(str, (Action)localObject);
    str = "F4Action";
    localInputMap.put(KeyStroke.getKeyStroke("F4"), str);
    localObject = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (PaymentPanel.this._mTable.hasFocus()) {
          PaymentPanel.this._mTable.requestFocusInWindow();
        }
      }
    };
    localJPanel.getActionMap().put(str, (Action)localObject);
    str = "F5Action";
    localInputMap.put(KeyStroke.getKeyStroke("F5"), str);
    localObject = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (!PaymentPanel.this._mEditable) {
          return;
        }
        if (PaymentPanel.this._mTable.hasFocus()) {
          PaymentPanel.this._mTable.clearSelection();
        }
        PaymentPanel.this.focusTypesField();
      }
    };
    localJXButton.setFont(localFont);
    localJPanel.getActionMap().put(str, (Action)localObject);
    return localJPanel;
  }
  
  public void setEditable(boolean paramBoolean)
  {
    this._mEditable = paramBoolean;
    this._mOKBtn.setEnabled(paramBoolean);
    this._mApplyBtn.setEnabled(paramBoolean);
    this._mOptions.setEnabled(paramBoolean);
  }
  
  public void setPayment(Payment paramPayment)
  {
    this._mModel.clear();
    if (paramPayment != null)
    {
      this._mAdjustPanel.setVisible(true);
      ArrayList localArrayList = paramPayment.getPaymentObjects();
      if (localArrayList != null)
      {
        int i = localArrayList.size();
        PaymentModeObject[] arrayOfPaymentModeObject = new PaymentModeObject[i];
        for (int j = 0; j < i; j++)
        {
          PaymentModeObject localPaymentModeObject = (PaymentModeObject)localArrayList.get(j);
          arrayOfPaymentModeObject[j] = localPaymentModeObject;
        }
        for (j = 0; j < i; j++) {
          this._mModel.addPaymentObjectInstance(arrayOfPaymentModeObject[j]);
        }
      }
    }
    this._mTable.updateUI();
    updateTotalForSetPayment();
    this._mTable.requestFocusInWindow();
    if (this._mModel.getRowCount() > 0) {
      this._mTable.setRowSelectionInterval(0, 0);
    }
    this._mCurrentPaymentForEdit = paramPayment;
  }
  
  public void setOldAmount(double paramDouble)
  {
    this._mOldAmountFloat = paramDouble;
  }
  
  private void optionChanged()
  {
    if (this._mCurrentOptionPanel != null)
    {
      this._mCurrentOptionPanel.setVisible(false);
      this._mDataPanel.remove(this._mCurrentOptionPanel);
    }
    PaymentOption localPaymentOption = (PaymentOption)this._mOptions.getSelectedItem();
    if (localPaymentOption == null) {
      return;
    }
    this._mCurrentOptionPanel = localPaymentOption.getOptionPanel();
    if (this._mCurrentOptionPanel != null)
    {
      CellConstraints localCellConstraints = new CellConstraints();
      localCellConstraints.xywh(1, 5, 6, 1, CellConstraints.FILL, CellConstraints.FILL);
      this._mDataPanel.add(this._mCurrentOptionPanel, localCellConstraints);
      this._mCurrentOptionPanel.setVisible(true);
      double d = this._mAmountRequired - this._mModel.getTotalAmount();
      d = d > 0.0D ? d : 0.0D;
      ((PaymentOptionPanel)this._mCurrentOptionPanel).clearFields();
      ((PaymentOptionPanel)this._mCurrentOptionPanel).setAmount(d);
      if (!this._mEditing)
      {
        ((PaymentOptionPanel)this._mCurrentOptionPanel).setEditable(this._mEditable);
      }
      else
      {
        ((PaymentOptionPanel)this._mCurrentOptionPanel).setEditable(false);
        this._mDelButton.setEnabled(false);
      }
    }
  }
  
  public void setDefaultButton()
  {
    getRootPane().setDefaultButton(this._mOKBtn);
  }
  
  private void deleteClicked()
  {
    int[] arrayOfInt = this._mTable.getSelectedRows();
    if (arrayOfInt.length == 0) {
      return;
    }
    int i = this._mTable.convertRowIndexToModel(arrayOfInt[0]);
    this._mModel.deleteRow(i);
    double d = this._mModel.getTotalAmount();
    this._mTotalAmount.setText(new InternalAmount(d, "", "", false).toString());
    if (d > this._mAmountRequired) {
      this._mTotalAmount.setBackground(Color.RED);
    } else {
      this._mTotalAmount.setBackground(Color.WHITE);
    }
  }
  
  private void tableSelected()
  {
    int[] arrayOfInt = this._mTable.getSelectedRows();
    if (arrayOfInt.length == 0)
    {
      this._mSelectedRow = -1;
      this._mCurrentSelectedObject = null;
      optionChanged();
      if (!this._mEditing)
      {
        this._mDelButton.setEnabled(false);
        this._mOptions.setEnabled(true);
      }
      return;
    }
    this._mDelButton.setEnabled(true);
    this._mSelectedRow = this._mTable.convertRowIndexToModel(arrayOfInt[0]);
    this._mCurrentSelectedObject = ((PaymentModeObject)this._mModel.getValueAt(this._mSelectedRow, 0));
    this._mOptions.setEnabled(false);
    this._mOptions.setSelectedItem(this._mCurrentSelectedObject.getPaymentOption());
    if (this._mCurrentOptionPanel != null)
    {
      this._mCurrentOptionPanel.setVisible(false);
      this._mDataPanel.remove(this._mCurrentOptionPanel);
    }
    PaymentOption localPaymentOption = this._mCurrentSelectedObject.getPaymentOption();
    PaymentOptionPanel localPaymentOptionPanel = (PaymentOptionPanel)localPaymentOption.getOptionPanel();
    this._mCurrentOptionPanel = ((JPanel)localPaymentOptionPanel);
    if (this._mCurrentOptionPanel != null)
    {
      CellConstraints localCellConstraints = new CellConstraints();
      localCellConstraints.xywh(1, 5, 6, 1, CellConstraints.FILL, CellConstraints.FILL);
      this._mDataPanel.add(this._mCurrentOptionPanel, localCellConstraints);
      this._mCurrentOptionPanel.setVisible(true);
      localPaymentOptionPanel.setObject(this._mCurrentSelectedObject);
      ((PaymentOptionPanel)this._mCurrentOptionPanel).setEditable(!this._mEditing);
    }
    if (!this._mEditing) {
      localPaymentOptionPanel.setEditable(this._mEditable);
    }
  }
  
  private void updateTotalForSetPayment()
  {
    double d = this._mModel.getTotalAmount();
    this._mTotalAmount.setText(new InternalAmount(d, "", "", false).toString());
    if (d > this._mAmountRequired) {
      this._mTotalAmount.setBackground(Color.RED);
    } else {
      this._mTotalAmount.setBackground(Color.WHITE);
    }
    this._mOldAmount.setText(new InternalAmount(this._mOldAmountFloat, "", "", false).toString());
  }
  
  private void updateTotal()
  {
    double d = this._mModel.getTotalAmount();
    this._mTotalAmount.setText(new InternalAmount(d, "", "", false).toString());
    if (d > this._mAmountRequired) {
      this._mTotalAmount.setBackground(Color.RED);
    } else {
      this._mTotalAmount.setBackground(Color.WHITE);
    }
  }
  
  public void focusTypesField()
  {
    this._mOptions.requestFocusInWindow();
  }
  
  public void clearAllFields()
  {
    this._mIsCancelled = true;
    this._mCurrentPayment = null;
    this._mAdjustPanel.setVisible(false);
    this._mRefundMode = false;
  }
  
  private void applyClicked()
  {
    if (!this._mEditable) {
      return;
    }
    int[] arrayOfInt = this._mTable.getSelectedRows();
    if ((arrayOfInt.length == 0) && (this._mTable.getRowCount() >= 10))
    {
      UICommon.showError("Only ten items are allowed.", "Error", BillingLauncher.getInstance());
      return;
    }
    if (!((PaymentOptionPanel)this._mCurrentOptionPanel).isPageValid()) {
      return;
    }
    if (arrayOfInt.length == 1)
    {
      ((PaymentOptionPanel)this._mCurrentOptionPanel).getValuesInObject(this._mCurrentSelectedObject);
      int i = this._mTable.convertRowIndexToModel(arrayOfInt[0]);
      this._mModel.updateRow(i, this._mCurrentSelectedObject);
    }
    else
    {
      PaymentOption localPaymentOption = (PaymentOption)this._mOptions.getSelectedItem();
      PaymentModeObject localPaymentModeObject = null;
      if (!this._mRefundMode) {
        localPaymentModeObject = localPaymentOption.getNewPaymentObjectForBillPay(StoreInfoTableDef.getCurrentStore().getStoreId());
      } else if (this._mRefund < 0.0D) {
        localPaymentModeObject = localPaymentOption.getNewPaymentObjectForBillRefund(StoreInfoTableDef.getCurrentStore().getStoreId());
      } else {
        localPaymentModeObject = localPaymentOption.getNewPaymentObjectForBillAddl(StoreInfoTableDef.getCurrentStore().getStoreId());
      }
      this._mModel.addPaymentObjectInstance(localPaymentModeObject);
      ((PaymentOptionPanel)this._mCurrentOptionPanel).clearFields();
      optionChanged();
    }
    updateTotal();
    this._mOKBtn.requestFocusInWindow();
  }
  
  private void okClicked()
  {
    double d = this._mModel.getTotalAmount();
    String str = this._mRefundMode ? "refund/payment" : "bill";
    if (d > this._mAmountRequired)
    {
      UICommon.showError("The amount enetered (" + d + " " + CommonConfig.getInstance().country.currency + " ) is more than the " + str + " amount (" + this._mAmountRequired + " " + CommonConfig.getInstance().country.currency + " ).", "Error", BillingLauncher.getInstance());
      return;
    }
    if (d < this._mAmountRequired)
    {
      UICommon.showError("The amount entered (" + d + " " + CommonConfig.getInstance().country.currency + " ) is less than the " + str + " amount (" + this._mAmountRequired + " " + CommonConfig.getInstance().country.currency + " ).", "Error", BillingLauncher.getInstance());
      return;
    }
    ArrayList localArrayList = this._mModel.getStoredData();
    Iterator localIterator = localArrayList.iterator();
    Payment localPayment = null;
    if (this._mCurrentPaymentForEdit == null) {
      localPayment = new Payment();
    } else {
      localPayment = this._mCurrentPaymentForEdit;
    }
    localPayment.removeAllPayments();
    while (localIterator.hasNext())
    {
      PaymentModeObject localPaymentModeObject = (PaymentModeObject)localIterator.next();
      localPayment.addPaymentMode(localPaymentModeObject);
    }
    this._mCurrentPayment = localPayment;
    this._mIsCancelled = false;
    PaymentDialog.getInstance().setVisible(false);
  }
  
  void fillOptions()
  {
    this._mPaymentModes = Payment.getOptionsList();
    updateComboBox();
  }
  
  private void setColumnWidths()
  {
    TableColumnModel localTableColumnModel = this._mTable.getColumnModel();
    localTableColumnModel.getColumn(0).setPreferredWidth(80);
    localTableColumnModel.getColumn(0).setHeaderRenderer(new TableCellRenderer()
    {
      public Component getTableCellRendererComponent(JTable paramAnonymousJTable, Object paramAnonymousObject, boolean paramAnonymousBoolean1, boolean paramAnonymousBoolean2, int paramAnonymousInt1, int paramAnonymousInt2)
      {
        JTableHeader localJTableHeader = PaymentPanel.this._mTable.getTableHeader();
        Font localFont1 = localJTableHeader.getFont();
        Font localFont2 = new Font(localFont1.getName(), 1, 14);
        TableCellRenderer localTableCellRenderer = localJTableHeader.getDefaultRenderer();
        Component localComponent = localTableCellRenderer.getTableCellRendererComponent(paramAnonymousJTable, paramAnonymousObject, paramAnonymousBoolean1, paramAnonymousBoolean2, paramAnonymousInt1, paramAnonymousInt2);
        localComponent.setFont(localFont2);
        return localComponent;
      }
    });
    localTableColumnModel.getColumn(1).setPreferredWidth(60);
    localTableColumnModel.getColumn(1).setHeaderRenderer(new TableCellRenderer()
    {
      public Component getTableCellRendererComponent(JTable paramAnonymousJTable, Object paramAnonymousObject, boolean paramAnonymousBoolean1, boolean paramAnonymousBoolean2, int paramAnonymousInt1, int paramAnonymousInt2)
      {
        JTableHeader localJTableHeader = PaymentPanel.this._mTable.getTableHeader();
        Font localFont1 = localJTableHeader.getFont();
        Font localFont2 = new Font(localFont1.getName(), 1, 14);
        TableCellRenderer localTableCellRenderer = localJTableHeader.getDefaultRenderer();
        Component localComponent = localTableCellRenderer.getTableCellRendererComponent(paramAnonymousJTable, paramAnonymousObject, paramAnonymousBoolean1, paramAnonymousBoolean2, paramAnonymousInt1, paramAnonymousInt2);
        localComponent.setFont(localFont2);
        return localComponent;
      }
    });
  }
  
  private void updateComboBox()
  {
    Iterator localIterator = this._mPaymentModes.iterator();
    while (localIterator.hasNext())
    {
      PaymentOption localPaymentOption = (PaymentOption)localIterator.next();
      this._mOptions.insertItemAt(localPaymentOption, 0);
    }
  }
  
  public void setTotalAmount(double paramDouble1, double paramDouble2)
  {
    this._mTotalBillAmount.setText(new InternalAmount(paramDouble1, "", "", false).toString());
    this._mAmountRequired = paramDouble1;
    this._mBillAmount = paramDouble1;
    double d = this._mModel.getTotalAmount();
    if (d < paramDouble1)
    {
      this._mRefund = (paramDouble1 - d);
      this._mRefundAmount.setText(new InternalAmount(paramDouble1 - d, "", "", false).toString());
    }
    else
    {
      this._mRefund = (-1.0D * paramDouble2);
      this._mRefundAmount.setText(new InternalAmount(paramDouble2, "", "", false).toString());
    }
  }
  
  public boolean isCancelled()
  {
    return this._mIsCancelled;
  }
  
  public void resetDetails()
  {
    this._mOptions.setSelectedItem(Payment.getCashPaymentOption());
    this._mEditable = true;
    this._mTable.clearSelection();
    this._mModel.clear();
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        PaymentPanel.this.optionChanged();
        ((PaymentOptionPanel)PaymentPanel.this._mCurrentOptionPanel).windowDisplayed();
      }
    });
    getRootPane().setDefaultButton(this._mOKBtn);
    updateTotal();
    this._mIsCancelled = true;
  }
  
  public void setBillUpdateMode(boolean paramBoolean)
  {
    this._mOldAmountPanel.setVisible(paramBoolean);
    this._mAdjustMode.setSelected(true);
  }
  
  public Payment getPayment()
  {
    return this._mCurrentPayment;
  }
  
  private void closeClicked()
  {
    this._mIsCancelled = true;
    PaymentDialog.getInstance().setVisible(false);
    this._mRefundMode = false;
  }
  
  private void setInBillAdjustMode()
  {
    this._mCurrentRefund = this._mCurrentPaymentForEdit;
    this._mCurrentPaymentForEdit = this._mCurrentBillPayment;
    this._mAdjSeparator.setText("Old Payment Details  ");
    setPayment(this._mCurrentPaymentForEdit);
    this._mAmountRequired = this._mBillAmount;
    this._mRefundMode = false;
  }
  
  private void setInBillRefundMode()
  {
    this._mCurrentBillPayment = this._mCurrentPaymentForEdit;
    if (this._mCurrentRefund == null) {
      this._mCurrentRefund = new Payment();
    }
    this._mCurrentPaymentForEdit = this._mCurrentRefund;
    this._mRefundMode = true;
    if (this._mRefund > 0.0D)
    {
      this._mAdjSeparator.setText("Refund Details  ");
      this._mRefundAmountLbl.setText("Refund : ");
      this._mAmountRequired = this._mRefund;
    }
    else
    {
      this._mAdjSeparator.setText("Payment Details  ");
      this._mRefundAmountLbl.setText("Payable : ");
      this._mAmountRequired = (-1.0D * this._mRefund);
    }
    setPayment(this._mCurrentPaymentForEdit);
  }
  
  public Payment getRefundPayment()
  {
    return null;
  }
  
  class PaymentTableCellRenderer
    extends DefaultTableCellRenderer
  {
    PaymentTableCellRenderer() {}
    
    public Component getTableCellRendererComponent(JTable paramJTable, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2)
    {
      super.getTableCellRendererComponent(paramJTable, paramObject, paramBoolean1, paramBoolean2, paramInt1, paramInt2);
      super.setOpaque(true);
      if (paramInt2 == 1) {
        setHorizontalAlignment(4);
      } else {
        setHorizontalAlignment(2);
      }
      if (PaymentPanel.this._mCellFont == null)
      {
        Font localFont = getFont();
        PaymentPanel.this._mCellFont = new Font(localFont.getName(), localFont.getStyle() | 0x1, 16);
      }
      setFont(PaymentPanel.this._mCellFont);
      return this;
    }
    
    public boolean isEditable()
    {
      return false;
    }
  }
  
  private class PaymentTableModel
    extends DefaultTableModel
  {
    private int _mTotalRows = 0;
    private ArrayList<String> _mColumns = null;
    private boolean _mCashAdded = false;
    private int _mCashIndex = -1;
    private double _mTotalModelAmount = 0.0D;
    private ArrayList<PaymentModeObject> _mObjects = new ArrayList();
    
    public PaymentTableModel()
    {
      initModel();
    }
    
    private void initModel()
    {
      this._mColumns = new ArrayList();
      this._mColumns.add("Option");
      this._mColumns.add("Amount");
    }
    
    public ArrayList<PaymentModeObject> getStoredData()
    {
      return this._mObjects;
    }
    
    public int getColumnCount()
    {
      return this._mColumns.size();
    }
    
    public String getColumnName(int paramInt)
    {
      return (String)this._mColumns.get(paramInt);
    }
    
    public void deleteRow(int paramInt)
    {
      double d = Double.valueOf(getValueAt(paramInt, 0).toString()).doubleValue();
      this._mTotalModelAmount -= d;
      this._mObjects.remove((PaymentModeObject)getValueAt(paramInt, 0));
      removeRow(paramInt);
      this._mTotalRows -= 1;
      for (int i = paramInt; i < this._mTotalRows; i++) {
        setValueAt(Integer.valueOf(i + 1), i, 0);
      }
    }
    
    public void updateRow(int paramInt, PaymentModeObject paramPaymentModeObject)
    {
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = paramPaymentModeObject;
      double d1 = Double.valueOf(getValueAt(paramInt, 1).toString()).doubleValue();
      double d2 = paramPaymentModeObject.getAmountForModel();
      this._mTotalModelAmount -= d1;
      this._mTotalModelAmount += d2;
      arrayOfObject[1] = new InternalAmount(paramPaymentModeObject.getAmount(), "", "  ", false);
      setValueAt(arrayOfObject[0], paramInt, 0);
      setValueAt(arrayOfObject[1], paramInt, 1);
    }
    
    public void addPaymentObjectInstance(PaymentModeObject paramPaymentModeObject)
    {
      Object[] arrayOfObject = new Object[2];
      this._mTotalRows += 1;
      arrayOfObject[0] = paramPaymentModeObject;
      double d = paramPaymentModeObject.getAmount();
      this._mTotalModelAmount += d;
      arrayOfObject[1] = new InternalAmount(paramPaymentModeObject.getAmount(), "", "  ", false);
      this._mObjects.add(paramPaymentModeObject);
      insertRow(this._mTotalRows - 1, arrayOfObject);
    }
    
    public double getTotalAmount()
    {
      return this._mTotalModelAmount;
    }
    
    public void clear()
    {
      while (this._mTotalRows > 0)
      {
        removeRow(0);
        this._mTotalRows -= 1;
      }
      this._mTotalRows = 0;
      this._mCashAdded = false;
      this._mCashIndex = -1;
      this._mTotalModelAmount = 0.0D;
      this._mObjects.clear();
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.billing.PaymentPanel
 * JD-Core Version:    0.7.0.1
 */