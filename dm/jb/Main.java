package dm.jb;

import dm.helpui.HelpException;
import dm.jb.db.DBAccess;
import dm.jb.db.objects.Payment;
import dm.jb.ext.RFIDException;
import dm.jb.ext.RFIDReader;
import dm.jb.op.report.ReportCategory;
import dm.jb.printing.BillPrintCommon;
import dm.jb.printing.JBPrinter;
import dm.jb.printing.Print;
import dm.jb.ui.AboutDialog;
import dm.jb.ui.ISPHelpLauncher;
import dm.jb.ui.LoginDialog;
import dm.jb.ui.MainWindow;
import dm.jb.ui.billing.CCPaymentOption;
import dm.jb.ui.billing.CashPaymentOption;
import dm.jb.ui.billing.ChequePaymentOption;
import dm.jb.ui.res.MessageResourceUtils;
import dm.tools.db.DBConnection;
import dm.tools.db.DBException;
import dm.tools.db.Db;
import dm.tools.printing.PrinterType;
import dm.tools.ui.UICommon;
import dm.tools.ui.UISettings;
import dm.tools.utils.CommonConfig;
import dm.tools.utils.Config;
import dm.tools.utils.Registration;
import ispinstaller.InpInst;
import java.io.File;
import javax.swing.JDialog;
import javax.swing.JFrame;

public class Main
{
  public static void main(String[] paramArrayOfString)
  {
    try
    {
      String str1 = "config" + File.separator + "config.xml";
      if (paramArrayOfString.length > 0) {
        str1 = paramArrayOfString[0];
      }
      boolean bool1 = false;
      if (paramArrayOfString.length > 1) {
        if (paramArrayOfString[1].equals("admin")) {
          bool1 = true;
        } else {
          bool1 = false;
        }
      }
      UISettings localUISettings = UISettings.createDefault();
      UICommon localUICommon = new UICommon(localUISettings);
      UICommon.registerMessageLoaderClass("dm.jb.messages.JbMessageLoader");
      localUICommon.initUI();
      File localFile = new File(str1);
      if (!localFile.exists())
      {
        int i = UICommon.showQuestion(MessageResourceUtils.getString("MAIN_FIRST_TIME_QUESTION"), MessageResourceUtils.getString("MAIN_FIRST_TIME_QUESTION_TITLE"), (JDialog)null);
        if (i != 1) {
          return;
        }
        InpInst.runISP();
        if (InpInst.INSTANCE.isClosed()) {
          return;
        }
      }
      boolean bool2 = Config.INSTANCE.loadConfig(str1);
      if (!bool2)
      {
        UICommon.showError("Error loading configuration file. Application will exit now", "Error", (JDialog)null);
        return;
      }
      Db.initStatic("JB_CONFIG");
      ReportCategory.setupReportCatList();
      String str2 = Config.INSTANCE.getAttrib("JB_CONFIG.REG_CODE.VALUE");
      if ((str2 == null) || (!Registration.getInstance().isAppRegistered(str2)))
      {
        str3 = Config.INSTANCE.getAttrib("JB_CONFIG.SKIP_REG.VALUE");
        if ((str3 != null) && (str3.equalsIgnoreCase("NO")))
        {
          AboutDialog.getInstance().setVisible(true);
          return;
        }
      }
      CommonConfig.getInstance().addPrinterType(new PrinterType("Laser", "dm.jb.printing.laser.LaserPrint", 1, "dm.jb.printing.laser.LaserPrinterConfigPanel", "ISP_PRINTER_LASER"));
      CommonConfig.getInstance().addPrinterType(new PrinterType("Dot Matrix", "dm.jb.printing.dotmatrix.DotMatrix", 2, "dm.jb.printing.dotmatrix.DotMatrixPrintSetupPanel", "ISP_PRINTER_DM"));
      String str3 = Config.INSTANCE.getAttrib("JB_CONFIG.PRINTER.PRINTER_NAME.VALUE");
      Print.getInstance().setPrinterInstance(str3);
      if (Print.getInstance().getPrinterInstance() == null)
      {
        UICommon.showError("Printer configuration is invalid or the defined printer is not found.\n\nPrinting functionality will not be operational.", "Error", (JFrame)null);
      }
      else
      {
        localObject1 = CommonConfig.getInstance().getPrinerTypeByName(str3);
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
      int j = Integer.valueOf(Config.INSTANCE.getAttrib("JB_CONFIG.DB_CONFIG.PORT").trim()).intValue();
      String str4 = Config.INSTANCE.getAttrib("JB_CONFIG.DB_CONFIG.DATABASE");
      String str5 = DBAccess.getDBUserName();
      String str6 = DBAccess.getPassword();
      try
      {
        Db.createConnection((String)localObject1, j, str5, str6, Db.getDBType(), str4);
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
        localDBException2.printStackTrace();
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
      String str7 = Config.INSTANCE.getAttrib("JB_CONFIG.SKIP_LOGIN.VALUE");
      if ((str7 != null) && (str7.equalsIgnoreCase("YES")))
      {
        if (!((LoginDialog)localObject2).createConnectionForTesting("billing", bool1)) {
          System.exit(0);
        }
      }
      else if (!((LoginDialog)localObject2).showAndValidate(true)) {
        System.exit(0);
      }
      Payment.registerOption(new CashPaymentOption());
      Payment.registerOption(new ChequePaymentOption());
      Payment.registerOption(new CCPaymentOption());
      try
      {
        ISPHelpLauncher.INSTANCE.initHelp();
      }
      catch (HelpException localHelpException)
      {
        localHelpException.printStackTrace();
        UICommon.showError("Help System cannot be initalized.", "Error", (JFrame)null);
      }
      MainWindow localMainWindow = new MainWindow();
      localMainWindow.enableMenus();
      localMainWindow.setVisible(true);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.Main
 * JD-Core Version:    0.7.0.1
 */