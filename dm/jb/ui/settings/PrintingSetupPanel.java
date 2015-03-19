package dm.jb.ui.settings;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.ui.MainWindow;
import dm.jb.ui.common.HelpButton;
import dm.jb.ui.common.ShortKeyCommon;
import dm.tools.printing.PrinterType;
import dm.tools.printing.PrintingUIClassIf;
import dm.tools.ui.AbstractMainPanel;
import dm.tools.ui.ActionPanel;
import dm.tools.ui.UICommon;
import dm.tools.utils.CommonConfig;
import dm.tools.utils.Config;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

public class PrintingSetupPanel
  extends AbstractMainPanel
{
  private static PrintingSetupPanel _mPrintingSetupPanel = null;
  private JComboBox _mPrinterType = null;
  private PrintingUIClassIf _mCurrentPanel = null;
  private HelpButton _mHelpButton = null;
  
  public static void printSettingsPanel()
  {
    if (_mPrintingSetupPanel == null) {
      _mPrintingSetupPanel = new PrintingSetupPanel();
    }
    ActionPanel localActionPanel = MainWindow.getActionPanel();
    localActionPanel.setVisible(true);
    PrintingSetupPanel localPrintingSetupPanel = _mPrintingSetupPanel;
    localActionPanel.cleanPush(localPrintingSetupPanel);
    localPrintingSetupPanel.setActionPanel(localActionPanel);
    localActionPanel.setTitle("Printer Configuration");
    MainWindow.instance.setVisible(true);
    localPrintingSetupPanel.setDefaultFocus();
  }
  
  public PrintingSetupPanel()
  {
    initUI();
  }
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("10px, 90px, 10px, 100px, 0px:grow,10px", "10px, 23px,20px,pref,20px,30px,20px");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    this._mPrinterType = new JComboBox();
    JLabel localJLabel = new JLabel("Printer Type : ");
    localCellConstraints.xywh(2, 2, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    localCellConstraints.xywh(4, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mPrinterType, localCellConstraints);
    ShortKeyCommon.shortKeyForLabels(localJLabel, 84, this._mPrinterType);
    Iterator localIterator = CommonConfig.getInstance().printerTypes.iterator();
    while (localIterator.hasNext())
    {
      PrinterType localPrinterType = (PrinterType)localIterator.next();
      this._mPrinterType.addItem(localPrinterType);
    }
    this._mPrinterType.setSelectedIndex(1);
    localCellConstraints.xywh(1, 3, 6, 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(new JSeparator(), localCellConstraints);
    this._mPrinterType.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PrintingSetupPanel.this.printerTypeChanged();
      }
    });
    localCellConstraints.xywh(1, 6, 6, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getButtonPanel(), localCellConstraints);
    localCellConstraints.xywh(1, 5, 6, 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(new JSeparator(), localCellConstraints);
  }
  
  public void windowDisplayed()
  {
    String str = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.PRINTER_NAME.VALUE");
    PrinterType localPrinterType = CommonConfig.getInstance().getPrinerTypeByName(str);
    this._mPrinterType.setSelectedItem(localPrinterType);
    this._mHelpButton.setKey(localPrinterType.helpCode);
  }
  
  public void setDefaultFocus() {}
  
  public JPanel getPanel()
  {
    return this;
  }
  
  private void printerTypeChanged()
  {
    CellConstraints localCellConstraints = new CellConstraints();
    if (this._mCurrentPanel != null)
    {
      this._mCurrentPanel.setVisible(false);
      remove(this._mCurrentPanel);
    }
    this._mCurrentPanel = null;
    PrinterType localPrinterType = (PrinterType)this._mPrinterType.getSelectedItem();
    this._mHelpButton.setKey(localPrinterType.helpCode);
    PrintingUIClassIf localPrintingUIClassIf = localPrinterType.getPrintingUIClassInstance();
    localCellConstraints.xywh(1, 4, 5, 1, CellConstraints.FILL, CellConstraints.TOP);
    this._mCurrentPanel = localPrintingUIClassIf;
    if (this._mCurrentPanel != null)
    {
      localCellConstraints.xywh(1, 4, 5, 1, CellConstraints.FILL, CellConstraints.TOP);
      add(this._mCurrentPanel, localCellConstraints);
      this._mCurrentPanel.setVisible(true);
      this._mCurrentPanel.readConfig();
    }
  }
  
  private JPanel getButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("10px, 100px, 30px:grow, 100px, 30px:grow, 100px, 10px", "30px");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    Object localObject = new JButton(" Update ");
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PrintingSetupPanel.this.okClicked();
      }
    });
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JButton)localObject).setMnemonic(85);
    localObject = new JButton(" Close ");
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PrintingSetupPanel.this.closeWindow();
      }
    });
    this._mHelpButton = new HelpButton("ISP_PRINTER_SETUP");
    localObject = this._mHelpButton;
    localCellConstraints.xywh(6, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add((Component)localObject, localCellConstraints);
    String str = "F1Action";
    final HelpButton localHelpButton = (HelpButton)localObject;
    InputMap localInputMap = getInputMap(2);
    localInputMap.put(KeyStroke.getKeyStroke("F1"), str);
    AbstractAction local4 = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        localHelpButton.actionPerformed(paramAnonymousActionEvent);
      }
    };
    getActionMap().put(str, local4);
    return localJPanel;
  }
  
  private void okClicked()
  {
    PrinterType localPrinterType = (PrinterType)this._mPrinterType.getSelectedItem();
    if (localPrinterType.getName().contains(" ")) {
      Config.INSTANCE.setAttrib("JB_CONFIG.PRINTER.PRINTER_NAME.VALUE", localPrinterType.getName());
    } else {
      Config.INSTANCE.setAttrib("JB_CONFIG.PRINTER.PRINTER_NAME.VALUE", localPrinterType.getName());
    }
    if (!this._mCurrentPanel.isPrintingUIValid()) {
      return;
    }
    if (!this._mCurrentPanel.writeConfig())
    {
      UICommon.showError("Error saving the configuration.", "Internal Error", MainWindow.instance);
      return;
    }
    Config.INSTANCE.printXML();
    UICommon.showMessage("Configuration stored successfully", "Error", MainWindow.instance);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.settings.PrintingSetupPanel
 * JD-Core Version:    0.7.0.1
 */