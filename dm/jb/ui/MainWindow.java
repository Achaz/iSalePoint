package dm.jb.ui;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.db.objects.CompanyInfoRow;
import dm.jb.db.objects.CompanyInfoTableDef;
import dm.jb.db.objects.UserInfoRow;
import dm.jb.db.objects.UserInfoTableDef;
import dm.jb.ui.settings.Configuration;
import dm.tools.db.DBConnection;
import dm.tools.db.DBException;
import dm.tools.db.Db;
import dm.tools.ui.ActionPanel;
import dm.tools.ui.QuickPanel;
import dm.tools.ui.QuickTaskBox;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

public class MainWindow
  extends JFrame
  implements WindowListener
{
  private MainMenu _mMainMenu = null;
  private static QuickPanel _mQuickPanel = null;
  private static ActionPanel _mActionPanel = null;
  public static MainWindow instance = null;
  
  public MainWindow()
  {
    super("iSalePoint");
    setDefaultCloseOperation(3);
    instance = this;
    initUI();
    pack();
    InputMap localInputMap = getRootPane().getInputMap(1);
    String str = "ESCAction";
    localInputMap.put(KeyStroke.getKeyStroke("ESCAPE"), str);
    AbstractAction local1 = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        MainWindow._mActionPanel.popObject();
      }
    };
    getRootPane().getActionMap().put(str, local1);
    CompanyInfoRow localCompanyInfoRow = null;
    try
    {
      localCompanyInfoRow = CompanyInfoTableDef.getInstance().getCompany();
    }
    catch (DBException localDBException)
    {
      localDBException.printStackTrace();
    }
    Configuration.companyInfoPanel();
    addWindowListener(new WindowAdapter()
    {
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
    setExtendedState(getExtendedState() | 0x6);
  }
  
  public void showRegWindow() {}
  
  public void windowOpened(WindowEvent paramWindowEvent)
  {
    JPanel localJPanel = (JPanel)getContentPane();
    localJPanel.setBackground(new Color(102, 140, 217));
    pack();
  }
  
  public void windowActivated(WindowEvent paramWindowEvent) {}
  
  public void windowDeactivated(WindowEvent paramWindowEvent) {}
  
  public void windowClosed(WindowEvent paramWindowEvent) {}
  
  public void windowIconified(WindowEvent paramWindowEvent) {}
  
  public void windowDeiconified(WindowEvent paramWindowEvent) {}
  
  public void windowClosing(WindowEvent paramWindowEvent) {}
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("5px,220px, 10px, 870px:grow, 5px", "25px, 5px, 630px:grow, 5px, 25px");
    JPanel localJPanel = (JPanel)getContentPane();
    localJPanel.setBackground(new Color(102, 140, 217));
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    this._mMainMenu = new MainMenu();
    localCellConstraints.xywh(1, 1, 5, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(this._mMainMenu, localCellConstraints);
    QuickTaskBox localQuickTaskBox = QuickTaskBox.getInstance();
    localCellConstraints.xywh(2, 3, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localQuickTaskBox, localCellConstraints);
    _mActionPanel = new ActionPanel();
    localCellConstraints.xywh(4, 3, 1, 1, CellConstraints.CENTER, CellConstraints.CENTER);
    localJPanel.add(_mActionPanel, localCellConstraints);
  }
  
  public static QuickPanel getQuickPanel()
  {
    return _mQuickPanel;
  }
  
  public static ActionPanel getActionPanel()
  {
    return _mActionPanel;
  }
  
  public Color getBackground()
  {
    JPanel localJPanel = (JPanel)getContentPane();
    return localJPanel.getBackground();
  }
  
  public void enableMenus()
  {
    this._mMainMenu.enableMenus();
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.MainWindow
 * JD-Core Version:    0.7.0.1
 */