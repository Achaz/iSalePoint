package dm.jb.ui.report;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.ui.MainWindow;
import dm.tools.ui.AbstractMainPanel;
import dm.tools.ui.ActionPanel;
import dm.tools.ui.UICommon;
import dm.tools.utils.JBJarClassLoader;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import org.jdesktop.swingx.JXButton;
import xeus.jcl.JarClassLoader;
import xeus.jcl.exception.JclException;

public class ReportManagePanel
  extends AbstractMainPanel
{
  public static ReportManagePanel _mInstance = new ReportManagePanel();
  private JTextField _mReportFile = null;
  private JTree _mReportTree = null;
  private JXButton _mExecuteButton = null;
  
  public ReportManagePanel()
  {
    initUI();
    populateTree();
  }
  
  public void setDefaultFocus()
  {
    this._mReportTree.expandRow(0);
    this._mReportTree.setRootVisible(false);
  }
  
  public JPanel getPanel()
  {
    return null;
  }
  
  public void windowDisplayed()
  {
    DefaultMutableTreeNode localDefaultMutableTreeNode = (DefaultMutableTreeNode)this._mReportTree.getLastSelectedPathComponent();
    if ((localDefaultMutableTreeNode == null) || (!localDefaultMutableTreeNode.isLeaf()))
    {
      this._mExecuteButton.setEnabled(false);
      return;
    }
    this._mExecuteButton.setEnabled(true);
  }
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("10px,100px,10px,300px,3px,40px,10px,pref:grow,10px", "10px,25px,5px,30px,5px,200px,60px,200px,20px,30px,10px");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    JLabel localJLabel = new JLabel("Report File : ");
    localCellConstraints.xywh(2, 2, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mReportFile = new JTextField();
    localCellConstraints.xywh(4, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mReportFile, localCellConstraints);
    ImageIcon localImageIcon1 = new ImageIcon(getClass().getResource("/dm/jb/images/file_folder.gif"));
    JXButton localJXButton = new JXButton(localImageIcon1);
    localCellConstraints.xywh(6, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJXButton, localCellConstraints);
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ReportManagePanel.this.reportBrowseClicked();
      }
    });
    localCellConstraints.xywh(4, 4, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getActionButtonPanel(), localCellConstraints);
    localJLabel = new JLabel("Reports : ");
    localCellConstraints.xywh(2, 6, 1, 1, CellConstraints.RIGHT, CellConstraints.TOP);
    add(localJLabel, localCellConstraints);
    this._mReportTree = new JTree(new DefaultTreeModel(null));
    this._mReportTree.setShowsRootHandles(true);
    localCellConstraints.xywh(4, 6, 1, 3, CellConstraints.FILL, CellConstraints.FILL);
    add(new JScrollPane(this._mReportTree), localCellConstraints);
    TreeSelectionModel localTreeSelectionModel = this._mReportTree.getSelectionModel();
    ImageIcon localImageIcon2 = new ImageIcon(getClass().getResource("/dm/jb/images/report.gif"));
    this._mReportTree.setRowHeight(24);
    if (localImageIcon2 != null)
    {
      DefaultTreeCellRenderer localDefaultTreeCellRenderer = new DefaultTreeCellRenderer();
      localDefaultTreeCellRenderer.setLeafIcon(localImageIcon2);
      this._mReportTree.setCellRenderer(localDefaultTreeCellRenderer);
    }
    localTreeSelectionModel.addTreeSelectionListener(new TreeSelectionListener()
    {
      public void valueChanged(TreeSelectionEvent paramAnonymousTreeSelectionEvent)
      {
        DefaultMutableTreeNode localDefaultMutableTreeNode = (DefaultMutableTreeNode)ReportManagePanel.this._mReportTree.getLastSelectedPathComponent();
        if ((localDefaultMutableTreeNode == null) || (!localDefaultMutableTreeNode.isLeaf()))
        {
          ReportManagePanel.this._mExecuteButton.setEnabled(false);
          return;
        }
        ReportManagePanel.this._mExecuteButton.setEnabled(true);
      }
    });
    localImageIcon1 = new ImageIcon(getClass().getResource("/dm/jb/images/execute.gif"));
    localJXButton = new JXButton(localImageIcon1);
    localCellConstraints.xywh(6, 7, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJXButton, localCellConstraints);
    this._mExecuteButton = localJXButton;
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ReportManagePanel.this.runReport();
      }
    });
    localCellConstraints.xywh(1, 9, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(new JSeparator(), localCellConstraints);
    localCellConstraints.xywh(2, 10, localFormLayout.getColumnCount() - 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getButtonPanel(), localCellConstraints);
  }
  
  private JPanel getActionButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("100px,pref:grow,100px", "pref:grow");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    JXButton localJXButton = new JXButton("Add");
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ReportManagePanel.this.addReportClicked();
      }
    });
    localJXButton = new JXButton("Delete");
    localCellConstraints.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    return localJPanel;
  }
  
  private JPanel getButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("100px,pref:grow,100px", "pref:grow");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    JXButton localJXButton = new JXButton("Close");
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ReportManagePanel.this.closeWindow();
      }
    });
    localJXButton = new JXButton("Help");
    localCellConstraints.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJXButton, localCellConstraints);
    return localJPanel;
  }
  
  private void populateTree()
  {
    DefaultTreeModel localDefaultTreeModel = (DefaultTreeModel)this._mReportTree.getModel();
    ReportTemplate localReportTemplate1 = ReportTemplateLoader._mInstance._mRootReport;
    DefaultMutableTreeNode localDefaultMutableTreeNode1 = new DefaultMutableTreeNode(localReportTemplate1);
    localDefaultTreeModel.setRoot(localDefaultMutableTreeNode1);
    for (ReportTemplate localReportTemplate2 = localReportTemplate1._mChild; localReportTemplate2 != null; localReportTemplate2 = localReportTemplate2._mNext)
    {
      DefaultMutableTreeNode localDefaultMutableTreeNode2 = new DefaultMutableTreeNode(localReportTemplate2);
      localDefaultMutableTreeNode1.add(localDefaultMutableTreeNode2);
      for (ReportTemplate localReportTemplate3 = localReportTemplate2._mChild; localReportTemplate3 != null; localReportTemplate3 = localReportTemplate3._mNext)
      {
        DefaultMutableTreeNode localDefaultMutableTreeNode3 = new DefaultMutableTreeNode(localReportTemplate3);
        localDefaultMutableTreeNode2.add(localDefaultMutableTreeNode3);
      }
    }
  }
  
  private void reportBrowseClicked()
  {
    JFileChooser localJFileChooser = new JFileChooser();
    localJFileChooser.showOpenDialog(MainWindow.instance);
    File localFile = localJFileChooser.getSelectedFile();
    if (localFile == null) {
      return;
    }
    this._mReportFile.setText(localFile.getAbsolutePath());
  }
  
  private void addReportClicked()
  {
    String str1 = this._mReportFile.getText().trim();
    if (str1.length() == 0)
    {
      UICommon.showError("Report file cannot be empty.", "Error", MainWindow.instance);
      this._mReportFile.requestFocusInWindow();
      return;
    }
    try
    {
      String[] arrayOfString = { str1 };
      JarClassLoader localJarClassLoader = new JarClassLoader(arrayOfString);
      if (localJarClassLoader == null)
      {
        UICommon.showError("Error opening the report information file.", "Error", MainWindow.instance);
        return;
      }
      JarFile localJarFile = new JarFile(str1);
      if (localJarFile == null)
      {
        UICommon.showError("Error opening the report information file.", "Error", MainWindow.instance);
        return;
      }
      ZipEntry localZipEntry = localJarFile.getEntry("report.inf");
      if (localZipEntry == null)
      {
        UICommon.showError("Error opening the report information file.", "Error", MainWindow.instance);
        return;
      }
      InputStream localInputStream = localJarFile.getInputStream(localZipEntry);
      if (localInputStream == null)
      {
        UICommon.showError("Error opening the report information file.", "Error", MainWindow.instance);
        return;
      }
      BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localInputStream));
      String str2 = null;
      while ((str2 = localBufferedReader.readLine()) != null) {
        if (str2.trim().length() != 0)
        {
          String str3 = str2;
          String str4 = localBufferedReader.readLine();
          String str5 = localBufferedReader.readLine();
          String str6 = str3.substring("REPORTCLASS=".length());
          String str7 = str4.substring("REPORTNAME=".length());
          String str8 = str5.substring("REPORTGROUP=".length());
          createAndAddTemplate(localJarClassLoader, str1, str6, str8, str7);
        }
      }
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      UICommon.showError("Error opening the file.", "Error", MainWindow.instance);
    }
    catch (IOException localIOException)
    {
      UICommon.showError("Error opening the file.", "Error", MainWindow.instance);
      return;
    }
    catch (JclException localJclException)
    {
      UICommon.showError("Error opening the file.", "Error", MainWindow.instance);
      return;
    }
  }
  
  private boolean createAndAddTemplate(JarClassLoader paramJarClassLoader, String paramString1, String paramString2, String paramString3, String paramString4)
    throws IOException
  {
    try
    {
      Class localClass = paramJarClassLoader.loadClass(paramString2);
      if (localClass == null)
      {
        UICommon.showError("Error in report file format.", "Error", MainWindow.instance);
        return false;
      }
      try
      {
        localObject1 = localClass.newInstance();
        if (localObject1 == null)
        {
          UICommon.showError("Error in report file format.", "Error", MainWindow.instance);
          return false;
        }
        if (!(localObject1 instanceof JBReport))
        {
          UICommon.showError("Error in report file format.", "Error", MainWindow.instance);
          return false;
        }
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        UICommon.showError("Error in report file format.", "Error", MainWindow.instance);
        return false;
      }
      catch (InstantiationException localInstantiationException)
      {
        UICommon.showError("Error in report file format.", "Error", MainWindow.instance);
        return false;
      }
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      UICommon.showError("Error in report file format.", "Error", MainWindow.instance);
      return false;
    }
    StringBuffer localStringBuffer = new StringBuffer("REPORT:name=");
    localStringBuffer.append(paramString4);
    localStringBuffer.append("|group=");
    localStringBuffer.append(paramString3);
    localStringBuffer.append("|class=");
    localStringBuffer.append(paramString2);
    localStringBuffer.append("|jar=");
    Object localObject1 = new File(paramString1);
    localStringBuffer.append(((File)localObject1).getAbsoluteFile().getName());
    String str = "reports/" + ((File)localObject1).getAbsoluteFile().getName();
    File localFile = new File(str);
    FileInputStream localFileInputStream = null;
    FileOutputStream localFileOutputStream = null;
    try
    {
      localFileInputStream = new FileInputStream((File)localObject1);
      localFileOutputStream = new FileOutputStream(localFile);
      byte[] arrayOfByte = new byte[1024];
      int i = 0;
      while ((i = localFileInputStream.read(arrayOfByte)) != -1) {
        localFileOutputStream.write(arrayOfByte, 0, i);
      }
    }
    catch (IOException localIOException)
    {
      UICommon.showError("Error copying the report to internal folder.", "Error", MainWindow.instance);
      throw localIOException;
    }
    finally
    {
      if (localFileInputStream != null) {
        localFileInputStream.close();
      }
      if (localFileOutputStream != null) {
        localFileOutputStream.close();
      }
    }
    try
    {
      ReportTemplate localReportTemplate = ReportTemplateLoader._mInstance.addReportTemplate(localStringBuffer.toString());
      ReportTemplateLoader._mInstance.writeTemplateToFile();
      addTemplateToTree(localReportTemplate);
    }
    catch (JBReportException localJBReportException)
    {
      UICommon.showError("Error registering the report.", "Error", MainWindow.instance);
      return false;
    }
    return true;
  }
  
  private void addTemplateToTree(ReportTemplate paramReportTemplate)
  {
    DefaultTreeModel localDefaultTreeModel = (DefaultTreeModel)this._mReportTree.getModel();
    DefaultMutableTreeNode localDefaultMutableTreeNode1 = (DefaultMutableTreeNode)localDefaultTreeModel.getRoot();
    if (paramReportTemplate._mParent == localDefaultMutableTreeNode1.getUserObject())
    {
      DefaultMutableTreeNode localDefaultMutableTreeNode2 = new DefaultMutableTreeNode(paramReportTemplate);
      localDefaultMutableTreeNode1.add(localDefaultMutableTreeNode2);
      this._mReportTree.updateUI();
      return;
    }
    int i = localDefaultMutableTreeNode1.getChildCount();
    for (int j = 0; j < i; j++)
    {
      localDefaultMutableTreeNode4 = (DefaultMutableTreeNode)localDefaultMutableTreeNode1.getChildAt(j);
      if (localDefaultMutableTreeNode4.getUserObject() == paramReportTemplate._mParent)
      {
        DefaultMutableTreeNode localDefaultMutableTreeNode5 = new DefaultMutableTreeNode(paramReportTemplate);
        localDefaultMutableTreeNode4.add(localDefaultMutableTreeNode5);
        return;
      }
    }
    DefaultMutableTreeNode localDefaultMutableTreeNode3 = new DefaultMutableTreeNode(paramReportTemplate._mParent);
    localDefaultMutableTreeNode1.add(localDefaultMutableTreeNode3);
    DefaultMutableTreeNode localDefaultMutableTreeNode4 = new DefaultMutableTreeNode(paramReportTemplate);
    localDefaultMutableTreeNode3.add(localDefaultMutableTreeNode4);
    this._mReportTree.updateUI();
  }
  
  private void runReport()
  {
    DefaultMutableTreeNode localDefaultMutableTreeNode = (DefaultMutableTreeNode)this._mReportTree.getLastSelectedPathComponent();
    ReportTemplate localReportTemplate = (ReportTemplate)localDefaultMutableTreeNode.getUserObject();
    if (localReportTemplate._mReportInstance == null) {
      try
      {
        String[] arrayOfString = { "reports/" + localReportTemplate.jarFile };
        localObject1 = new JBJarClassLoader(arrayOfString);
        if (localObject1 == null)
        {
          UICommon.showError("Error opening the report information file.", "Error", MainWindow.instance);
          return;
        }
        Class localClass = ((JBJarClassLoader)localObject1).loadClass(localReportTemplate._mClassName);
        if (localClass == null)
        {
          UICommon.showError("Error in report file format.", "Error", MainWindow.instance);
          return;
        }
        Object localObject2 = localClass.newInstance();
        if (localObject2 == null)
        {
          UICommon.showError("Error in report file format.", "Error", MainWindow.instance);
          return;
        }
        localReportTemplate._mReportInstance = ((JBReport)localObject2);
      }
      catch (IOException localIOException)
      {
        localIOException.printStackTrace();
        UICommon.showError("Internal 'I/O' Error running the report.", "Error", MainWindow.instance);
        return;
      }
      catch (JclException localJclException)
      {
        UICommon.showError("Internal 'Java Class Loader' Error running the report.", "Error", MainWindow.instance);
        return;
      }
      catch (ClassNotFoundException localClassNotFoundException)
      {
        UICommon.showError("Internal 'Java Missing Class' Error running the report.", "Error", MainWindow.instance);
        return;
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        UICommon.showError("Internal 'Access' Error running the report.", "Error", MainWindow.instance);
        return;
      }
      catch (InstantiationException localInstantiationException)
      {
        UICommon.showError("Internal Error running the report.", "Error", MainWindow.instance);
        return;
      }
    }
    Object localObject1 = localReportTemplate._mReportInstance.getUI();
    ActionPanel localActionPanel = MainWindow.getActionPanel();
    localActionPanel.setVisible(true);
    ((AbstractMainPanel)localObject1).clearAllFields();
    localActionPanel.pushObject((JComponent)localObject1);
    ((AbstractMainPanel)localObject1).setActionPanel(localActionPanel);
    localActionPanel.setTitle(localReportTemplate._mName);
    MainWindow.instance.setVisible(true);
    ((AbstractMainPanel)localObject1).setDefaultFocus();
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.report.ReportManagePanel
 * JD-Core Version:    0.7.0.1
 */