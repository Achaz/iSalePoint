package dm.jb.ui.billing;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.helpui.HelpException;
import dm.jb.JeException;
import dm.jb.db.DBAccess;
import dm.jb.db.objects.Payment;
import dm.jb.db.objects.SiteInfoTableDef;
import dm.jb.db.objects.StoreInfoTableDef;
import dm.jb.db.objects.UserInfoRow;
import dm.jb.db.objects.UserInfoTableDef;
import dm.jb.ext.RFIDException;
import dm.jb.ext.RFIDReader;
import dm.jb.printing.BillPrintCommon;
import dm.jb.printing.JBPrinter;
import dm.jb.printing.Print;
import dm.jb.ui.AboutDialog;
import dm.jb.ui.ISPHelpLauncher;
import dm.jb.ui.LoginDialog;
import dm.jb.ui.MainWindow;
import dm.tools.db.DBConnection;
import dm.tools.db.DBException;
import dm.tools.db.Db;
import dm.tools.printing.PrinterType;
import dm.tools.ui.UICommon;
import dm.tools.ui.UISettings;
import dm.tools.ui.components.DMStatusBar;
import dm.tools.ui.components.DMStatusBarComponent;
import dm.tools.utils.CommonConfig;
import dm.tools.utils.Config;
import dm.tools.utils.Registration;
import ispinstaller.InpInst;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class BillingLauncher
  extends JFrame
{
  public static BillingLauncher INSTANCE = null;
  private DMStatusBar _mStatusBar = null;
  private DMStatusBarComponent _mConnectionStatus = null;
  
  private BillingLauncher()
  {
    super("Billing");
    initUI();
    pack();
    setLocationRelativeTo(null);
    setDefaultCloseOperation(3);
    setExtendedState(getExtendedState() | 0x6);
    addWindowListener(new WindowAdapter()
    {
      public void windowActivated(WindowEvent paramAnonymousWindowEvent) {}
      
      public void windowClosing(WindowEvent paramAnonymousWindowEvent)
      {
        try
        {
          Db.getConnection().openTrans();
          UserInfoTableDef.getCurrentUser().setLocked(false);
          UserInfoTableDef.getCurrentUser().update(true);
          Db.getConnection().commit();
        }
        catch (DBException localDBException)
        {
          localDBException.printStackTrace();
        }
        Db.getConnection().close();
      }
    });
  }
  
  public static BillingLauncher getInstance()
  {
    if (INSTANCE == null) {
      INSTANCE = new BillingLauncher();
    }
    return INSTANCE;
  }
  
  public static void main(String[] paramArrayOfString)
  {
    UISettings localUISettings = UISettings.createDefault();
    File localFile = new File("config" + File.separator + "config.xml");
    UICommon localUICommon = new UICommon(localUISettings);
    UICommon.registerMessageLoaderClass("dm.jb.messages.JbMessageLoader");
    localUICommon.initUI();
    if (!localFile.exists())
    {
      int i = UICommon.showQuestion("Is this the first time you are launching iSalePoint. If so, you need to configure iSalePoint. Application cannot continue without configuration.\n\n\tClick 'Yes' to start the configuration.", "iSalePoint Startup", (JDialog)null);
      if (i != 1) {
        return;
      }
      try
      {
        InpInst.runISP();
      }
      catch (NoClassDefFoundError localNoClassDefFoundError)
      {
        localNoClassDefFoundError.getCause().printStackTrace();
      }
      if (InpInst.INSTANCE.isClosed()) {
        return;
      }
    }
    boolean bool = Config.INSTANCE.loadConfig("config/config.xml");
    if (!bool)
    {
      UICommon.showError("Error loading configuration file. Application will exit now", "Error", (JDialog)null);
      return;
    }
    Db.initStatic("JB_CONFIG");
    String str1 = Config.INSTANCE.getAttrib("JB_CONFIG.REG_CODE.VALUE");
    if ((str1 == null) || (!Registration.getInstance().isAppRegistered(str1)))
    {
      str2 = Config.INSTANCE.getAttrib("JB_CONFIG.SKIP_REG.VALUE");
      if ((str2 != null) && (str2.equalsIgnoreCase("NO")))
      {
        AboutDialog.getInstance().setVisible(true);
        return;
      }
    }
    CommonConfig.getInstance().addPrinterType(new PrinterType("Laser", "dm.jb.printing.laser.LaserPrint", 1, "dm.jb.printing.laser.LaserPrinterConfigPanel", "SP_PRINTER_LASER"));
    CommonConfig.getInstance().addPrinterType(new PrinterType("Dot Matrix", "dm.jb.printing.dotmatrix.DotMatrix", 2, "dm.jb.printing.dotmatrix.DotMatrixPrintSetupPanel", "ISP_PRINTER_DM"));
    String str2 = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.PRINTER_NAME.VALUE");
    Print.getInstance().setPrinterInstance(str2);
    if (Print.getInstance().getPrinterInstance() == null)
    {
      UICommon.showError("Printer configuration is invalid or the defined printer is not found.\n\nPrinting functionality will not be operational.", "Error", (JFrame)null);
    }
    else
    {
      localObject1 = CommonConfig.getInstance().getPrinerTypeByName(str2);
      try
      {
        BillPrintCommon.getInstance().createBillingColumnList(((PrinterType)localObject1).getPrintingClassInstance().getColumnConfigString());
        ((PrinterType)localObject1).getPrintingClassInstance().setPrintColumnList(BillPrintCommon.getInstance().getPrintColumns());
      }
      catch (JeException localJeException)
      {
        UICommon.showError("Printer configuration error.\n\n" + localJeException.getMessage(), "Error", (JFrame)null);
        return;
      }
    }
    Object localObject1 = Config.INSTANCE.getAttrib("JB_CONFIG.DB_CONFIG.HOST");
    int j = Integer.valueOf(Config.INSTANCE.getAttrib("JB_CONFIG.DB_CONFIG.PORT")).intValue();
    String str3 = Config.INSTANCE.getAttrib("JB_CONFIG.DB_CONFIG.DATABASE");
    String str4 = DBAccess.getDBUserName();
    String str5 = DBAccess.getPassword();
    try
    {
      Db.createConnection((String)localObject1, j, str4, str5, Db.getDBType(), str3);
    }
    catch (DBException localDBException1)
    {
      localDBException1.printStackTrace();
      UICommon.showError("Error connecting to database.\n\nCheck if database is running or not.", "Error", MainWindow.instance);
      return;
    }
    DBConnection localDBConnection = Db.getConnection();
    Db.setConnection(localDBConnection);
    try
    {
      CommonConfig.getInstance().load();
    }
    catch (DBException localDBException2)
    {
      UICommon.showError("Internal error loading configuration details.\nTry again later. If the problem persists contact administrator.", "Internal Error", (JFrame)null);
      return;
    }
    if (CommonConfig.getInstance().enableRFID)
    {
      localObject2 = RFIDReader._mInstance;
      try
      {
        ((RFIDReader)localObject2).initDevice();
        ((RFIDReader)localObject2).startThread();
      }
      catch (RFIDException localRFIDException)
      {
        UICommon.showError("Error initializing RFID reader.", "Error", (JFrame)null);
        localRFIDException.printStackTrace();
      }
    }
    Object localObject2 = new LoginDialog(null, "Login", true);
    String str6 = Config.INSTANCE.getAttrib("JB_CONFIG.SKIP_LOGIN.VALUE");
    if ((str6 != null) && (str6.equalsIgnoreCase("YES")))
    {
      if (!((LoginDialog)localObject2).createConnectionForTesting("billing")) {
        System.exit(0);
      }
    }
    else
    {
      if (!((LoginDialog)localObject2).showAndValidate(false)) {
        System.exit(0);
      }
      if ((StoreInfoTableDef.getCurrentStore() == null) || (SiteInfoTableDef.getCurrentSite() == null)) {
        UICommon.showError("Stor and/or site information is not selected.", "User cannot login", MainWindow.instance);
      }
    }
    PaymentDialog.getInstance();
    Payment.registerOption(new CashPaymentOption());
    Payment.registerOption(new CCPaymentOption());
    Payment.registerOption(new ChequePaymentOption());
    PaymentDialog.getInstance().setStartIndex();
    getInstance();
    if (CommonConfig.getInstance().customerOption != 2) {
      CustomerInfoDialog.getInstance();
    }
    try
    {
      ISPHelpLauncher.INSTANCE.initHelp();
    }
    catch (HelpException localHelpException)
    {
      localHelpException.printStackTrace();
      UICommon.showError("Help System cannot be initalized.", "Error", (JFrame)null);
    }
    BillingLauncher localBillingLauncher = getInstance();
    localBillingLauncher.runIt();
  }
  
  public void runIt()
  {
    setVisible(true);
  }
  
  private void initUI()
  {
    JPanel localJPanel = (JPanel)getContentPane();
    FormLayout localFormLayout = new FormLayout("10px,250px,10px,pref:grow,10px", "23px,10px,500px,pref:grow,30px,10px,30px, 1px");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(1, 3, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(BillingButtonPanel.getInstance(), localCellConstraints);
    BillingPanel localBillingPanel = BillingPanel.getBillingPanel();
    localCellConstraints.xywh(4, 3, 1, 2, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localBillingPanel, localCellConstraints);
    this._mStatusBar = new DMStatusBar();
    localCellConstraints.xywh(1, 7, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mStatusBar, localCellConstraints);
    this._mConnectionStatus = new DMStatusBarComponent();
    this._mStatusBar.addStatusbarComponent(this._mConnectionStatus);
    this._mConnectionStatus.setText("Online");
    this._mConnectionStatus = new DMStatusBarComponent();
    this._mStatusBar.addStatusbarComponent(this._mConnectionStatus);
    this._mConnectionStatus.setText("Online");
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        BillingPanel.getBillingPanel().setDefaultFocus();
      }
    });
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.billing.BillingLauncher
 * JD-Core Version:    0.7.0.1
 */