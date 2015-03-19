package dm.jb.ui.settings;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.jb.ui.MainWindow;
import dm.jb.ui.common.HelpButton;
import dm.jb.ui.common.ShortKeyCommon;
import dm.tools.db.DBConnection;
import dm.tools.db.DBException;
import dm.tools.db.DBRow;
import dm.tools.db.Db;
import dm.tools.dbui.ActionObject;
import dm.tools.dbui.DBUIActionAdapter;
import dm.tools.dbui.DBUIContainerImpl;
import dm.tools.dbui.ValidationException;
import dm.tools.ui.AbstractMainPanel;
import dm.tools.ui.UICommon;
import dm.tools.ui.components.JBPhoneField;
import dm.tools.ui.components.JBStringTextField;
import dm.tools.ui.components.JBTextArea;
import dm.tools.ui.components.Validator;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import org.jdesktop.swingx.JXButton;

public class CompanyInfoPanel
  extends AbstractMainPanel
  implements Validator
{
  private static CompanyInfoPanel _mInstance = null;
  private DBUIContainerImpl _mDBUIContainer = null;
  private JComponent nameComp = null;
  private JLabel label = null;
  private JTextField _mCompanyLogo = null;
  private JTextField _mReportImage = null;
  private JTextField _mBackgroundImage = null;
  private boolean _mCompanyExists = false;
  
  public static CompanyInfoPanel getInstance()
  {
    if (_mInstance == null) {
      _mInstance = new CompanyInfoPanel();
    }
    return _mInstance;
  }
  
  private CompanyInfoPanel()
  {
    this._mDBUIContainer.addValidator(this);
    initUI();
  }
  
  public void setCompanyInfo(DBRow paramDBRow)
  {
    if (paramDBRow != null) {
      this._mCompanyExists = true;
    } else {
      this._mCompanyExists = false;
    }
    this._mDBUIContainer.setCurrentInstance(paramDBRow);
  }
  
  public void setDefaultFocus()
  {
    this.nameComp.requestFocusInWindow();
  }
  
  public JPanel getPanel()
  {
    return this;
  }
  
  public void windowDisplayed() {}
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("10px,90px,10px,250px,3px,30px,10px", "10px,25px,10px,100px,10px,25px,10px,25px,10px,25px,10px,25px,20px,30px,10px");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    int i = 2;
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.CENTER);
    this.label = new JLabel("Name : ");
    add(this.label, localCellConstraints);
    this.nameComp = ((JComponent)this._mDBUIContainer.createComponentForAttribute("NAME", "Company Name"));
    this.nameComp.setToolTipText("Company name");
    ShortKeyCommon.shortKeyForLabels(this.label, 78, this.nameComp);
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this.nameComp, localCellConstraints);
    if ((this.nameComp instanceof JBStringTextField))
    {
      localObject1 = (JBStringTextField)this.nameComp;
      ((JBStringTextField)localObject1).setMaxLength(128);
      ((JBStringTextField)localObject1).setMinLength(1);
      ((JBStringTextField)localObject1).setRunTimeValidation(false);
    }
    i += 2;
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.TOP);
    this.label = new JLabel("Address : ");
    add(this.label, localCellConstraints);
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    Object localObject1 = (JComponent)this._mDBUIContainer.createComponentForAttribute("ADDRESS", "Address", (short)1, false);
    add(new JScrollPane((Component)localObject1), localCellConstraints);
    ((JComponent)localObject1).setToolTipText("Company address");
    ShortKeyCommon.shortKeyForLabels(this.label, 83, (JComponent)localObject1);
    if ((localObject1 instanceof JBTextArea))
    {
      localObject2 = (JBTextArea)localObject1;
      ((JBTextArea)localObject2).setMaxLength(512);
      ((JBTextArea)localObject2).setMinLength(0);
      ((JBTextArea)localObject2).setRunTimeValidation(false);
      ((JBTextArea)localObject2).setMandatory(true);
    }
    i += 2;
    localCellConstraints.xywh(2, 6, 1, 1, CellConstraints.RIGHT, CellConstraints.CENTER);
    this.label = new JLabel("Phone : ");
    add(this.label, localCellConstraints);
    Object localObject2 = (JComponent)this._mDBUIContainer.createComponentForAttribute("PHONE1", "Phone", (short)2, false);
    ((JComponent)localObject2).setToolTipText("Compnay main contact number");
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add((Component)localObject2, localCellConstraints);
    ShortKeyCommon.shortKeyForLabels(this.label, 80, (JComponent)localObject2);
    if ((localObject2 instanceof JBPhoneField))
    {
      localObject3 = (JBPhoneField)localObject2;
      ((JBPhoneField)localObject3).setRunTimeValidation(false);
      ((JBPhoneField)localObject3).setMandatory(true);
    }
    i += 2;
    this.label = new JLabel("Company Logo : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(this.label, localCellConstraints);
    this._mCompanyLogo = new JTextField();
    this._mCompanyLogo.setToolTipText("Logo of the company");
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mCompanyLogo, localCellConstraints);
    Object localObject3 = new ImageIcon(getClass().getResource("/dm/jb/images/file_folder.gif"));
    JXButton localJXButton = new JXButton((Icon)localObject3);
    localJXButton.setToolTipText("Browse for company logo");
    localCellConstraints.xywh(6, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJXButton, localCellConstraints);
    ShortKeyCommon.shortKeyForLabels(this.label, 76, this._mCompanyLogo);
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JFileChooser localJFileChooser = new JFileChooser();
        localJFileChooser.addChoosableFileFilter(new CompanyInfoPanel.ImageFilter(CompanyInfoPanel.this));
        int i = localJFileChooser.showOpenDialog(MainWindow.instance);
        if (i == 0)
        {
          String str = localJFileChooser.getSelectedFile().getAbsolutePath();
          CompanyInfoPanel.this._mCompanyLogo.setText(str);
        }
      }
    });
    i += 2;
    this.label = new JLabel("Report Image : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(this.label, localCellConstraints);
    this._mReportImage = new JTextField();
    this._mReportImage.setToolTipText("Image to be displayed in the report");
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mReportImage, localCellConstraints);
    localJXButton = new JXButton((Icon)localObject3);
    localJXButton.setToolTipText("Browse for report image");
    localCellConstraints.xywh(6, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJXButton, localCellConstraints);
    ShortKeyCommon.shortKeyForLabels(this.label, 73, this._mReportImage);
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JFileChooser localJFileChooser = new JFileChooser();
        localJFileChooser.addChoosableFileFilter(new CompanyInfoPanel.ImageFilter(CompanyInfoPanel.this));
        int i = localJFileChooser.showOpenDialog(MainWindow.instance);
        if (i == 0)
        {
          String str = localJFileChooser.getSelectedFile().getAbsolutePath();
          CompanyInfoPanel.this._mReportImage.setText(str);
        }
      }
    });
    i += 2;
    this.label = new JLabel("Background : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(this.label, localCellConstraints);
    this._mBackgroundImage = new JTextField();
    this._mBackgroundImage.setToolTipText("Background image");
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mBackgroundImage, localCellConstraints);
    localJXButton = new JXButton((Icon)localObject3);
    localJXButton.setToolTipText("Browse for background image");
    localCellConstraints.xywh(6, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(localJXButton, localCellConstraints);
    ShortKeyCommon.shortKeyForLabels(this.label, 66, this._mBackgroundImage);
    localJXButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JFileChooser localJFileChooser = new JFileChooser();
        localJFileChooser.addChoosableFileFilter(new CompanyInfoPanel.ImageFilter(CompanyInfoPanel.this));
        int i = localJFileChooser.showOpenDialog(MainWindow.instance);
        if (i == 0)
        {
          String str = localJFileChooser.getSelectedFile().getAbsolutePath();
          CompanyInfoPanel.this._mBackgroundImage.setText(str);
        }
      }
    });
    i++;
    localCellConstraints.xywh(1, i, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.CENTER);
    add(new JSeparator(), localCellConstraints);
    i++;
    localCellConstraints.xywh(1, i, localFormLayout.getColumnCount(), 1, CellConstraints.FILL, CellConstraints.FILL);
    add(getButtonPanel(), localCellConstraints);
  }
  
  private JPanel getButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    FormLayout localFormLayout = new FormLayout("10px,100px,pref:grow,100px,pref:grow,100px,10px", "pref:grow");
    localJPanel.setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    JComponent localJComponent = (JComponent)this._mDBUIContainer.createActionObject("Update", "Update_CREATE", null);
    JButton localJButton1 = (JButton)localJComponent;
    localJButton1.setToolTipText("Update the compnay information");
    localJButton1.setMnemonic(85);
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJComponent, localCellConstraints);
    ((ActionObject)localJComponent).addDBUIActionListener(new DBUIActionAdapter()
    {
      public void actionFailed(Object paramAnonymousObject)
      {
        UICommon.showMessage("Error updating company information.", "Error", MainWindow.instance);
        CompanyInfoPanel.this.nameComp.requestFocusInWindow();
      }
      
      public void afterAction(ActionObject paramAnonymousActionObject)
      {
        String str = "";
        if (!CompanyInfoPanel.this._mCompanyExists) {
          str = "You may now restart the application to see all the menus.";
        }
        try
        {
          CompanyInfoPanel.this.setImages();
        }
        catch (DBException localDBException)
        {
          localDBException.printStackTrace();
          UICommon.showError("Company information updated successfully. But images werenot updated.\n" + str, "Success", MainWindow.instance);
          CompanyInfoPanel.this._mCompanyExists = true;
          return;
        }
        CompanyInfoPanel.this._mCompanyExists = true;
        UICommon.showMessage("Company information updated successfully.\n" + str, "Success", MainWindow.instance);
      }
    });
    JButton localJButton2 = new JButton("Close");
    localJButton2.setToolTipText("Close this window");
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localJButton2, localCellConstraints);
    localJButton2.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        CompanyInfoPanel.this.closeWindow();
      }
    });
    HelpButton localHelpButton1 = new HelpButton("ISP_COMPANY");
    localHelpButton1.setToolTipText("help");
    localHelpButton1.setMnemonic(72);
    localCellConstraints.xywh(6, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localJPanel.add(localHelpButton1, localCellConstraints);
    String str = "F1Action";
    final HelpButton localHelpButton2 = (HelpButton)localHelpButton1;
    InputMap localInputMap = getInputMap(2);
    localInputMap.put(KeyStroke.getKeyStroke("F1"), str);
    AbstractAction local6 = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        localHelpButton2.actionPerformed(paramAnonymousActionEvent);
      }
    };
    getActionMap().put(str, local6);
    return localJPanel;
  }
  
  private void setImages()
    throws DBException
  {
    String str1 = this._mCompanyLogo.getText().trim();
    if (str1.length() > 0) {
      setImageInternal("LOGO", str1);
    }
    String str2 = this._mBackgroundImage.getText().trim();
    if (str2.length() > 0) {
      setImageInternal("BACKGROUND", str2);
    }
    String str3 = this._mReportImage.getText().trim();
    if (str3.length() > 0) {
      setImageInternal("REPORT_IMAGE", str3);
    }
  }
  
  private void setImageInternal(String paramString1, String paramString2)
    throws DBException
  {
    String str = "UPDATE COMPANY_INFO SET " + paramString1 + "=? WHERE NAME=NAME";
    try
    {
      PreparedStatement localPreparedStatement = Db.getConnection().getJDBCConnection().prepareStatement(str);
      FileInputStream localFileInputStream = new FileInputStream(paramString2);
      localPreparedStatement.setBinaryStream(1, localFileInputStream, localFileInputStream.available());
      localPreparedStatement.executeUpdate();
      Db.getConnection().commit();
      localFileInputStream.close();
    }
    catch (SQLException localSQLException)
    {
      localSQLException = localSQLException;
      throw new DBException("Error in setting company images", "SQL Exception", "Try again", null, localSQLException);
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      localFileNotFoundException = localFileNotFoundException;
      throw new DBException("Error in setting company images", "FileNotFoundException", "Try again", null, null);
    }
    catch (IOException localIOException)
    {
      localIOException = localIOException;
      throw new DBException("Error in setting company images", "IO Exception", "Try again", null, null);
    }
    finally {}
  }
  
  public void validateValue()
    throws ValidationException
  {
    String str1 = this._mCompanyLogo.getText().trim();
    if (str1.length() > 0) {
      try
      {
        FileInputStream localFileInputStream1 = new FileInputStream(str1);
        localFileInputStream1.close();
      }
      catch (FileNotFoundException localFileNotFoundException1)
      {
        SwingUtilities.invokeLater(new Runnable()
        {
          public void run()
          {
            CompanyInfoPanel.this._mCompanyLogo.requestFocusInWindow();
          }
        });
        throw new ValidationException("Logo file not found.", "Cannot open logo file.", "Specify a valid logo file.");
      }
      catch (IOException localIOException1)
      {
        SwingUtilities.invokeLater(new Runnable()
        {
          public void run()
          {
            CompanyInfoPanel.this._mCompanyLogo.requestFocusInWindow();
          }
        });
        throw new ValidationException("Logo file not found.", "Cannot open logo file.", "Specify a valid logo file.");
      }
    }
    String str2 = this._mBackgroundImage.getText().trim();
    if (str2.length() > 0) {
      try
      {
        FileInputStream localFileInputStream2 = new FileInputStream(str2);
        localFileInputStream2.close();
      }
      catch (FileNotFoundException localFileNotFoundException2)
      {
        SwingUtilities.invokeLater(new Runnable()
        {
          public void run()
          {
            CompanyInfoPanel.this._mBackgroundImage.requestFocusInWindow();
          }
        });
        throw new ValidationException("Background image file not found.", "Cannot open background image file.", "Specify a valid background image file.");
      }
      catch (IOException localIOException2)
      {
        SwingUtilities.invokeLater(new Runnable()
        {
          public void run()
          {
            CompanyInfoPanel.this._mBackgroundImage.requestFocusInWindow();
          }
        });
        throw new ValidationException("Background image file not found.", "Cannot open background image file.", "Specify a valid background image file.");
      }
    }
    String str3 = this._mReportImage.getText().trim();
    if (str3.length() > 0) {
      try
      {
        FileInputStream localFileInputStream3 = new FileInputStream(str2);
        localFileInputStream3.close();
      }
      catch (FileNotFoundException localFileNotFoundException3)
      {
        SwingUtilities.invokeLater(new Runnable()
        {
          public void run()
          {
            CompanyInfoPanel.this._mReportImage.requestFocusInWindow();
          }
        });
        throw new ValidationException("Report image file not found.", "Cannot open report image file.", "Specify a valid report image file.");
      }
      catch (IOException localIOException3)
      {
        SwingUtilities.invokeLater(new Runnable()
        {
          public void run()
          {
            CompanyInfoPanel.this._mReportImage.requestFocusInWindow();
          }
        });
        throw new ValidationException("Report image file not found.", "Cannot open report image file.", "Specify a valid report image file.");
      }
    }
  }
  
  public class ImageFilter
    extends FileFilter
  {
    public ImageFilter() {}
    
    public boolean accept(File paramFile)
    {
      if (paramFile.isDirectory()) {
        return true;
      }
      String str1 = paramFile.getName();
      int i = str1.lastIndexOf(".");
      if (i < 0) {
        return false;
      }
      String str2 = str1.substring(i + 1);
      if (str2 != null) {
        return (str2.equalsIgnoreCase("gif")) || (str2.equalsIgnoreCase("jpg")) || (str2.equalsIgnoreCase("png"));
      }
      return false;
    }
    
    public String getDescription()
    {
      return "Images (*.gif, *.jpg, *.png)";
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.settings.CompanyInfoPanel
 * JD-Core Version:    0.7.0.1
 */