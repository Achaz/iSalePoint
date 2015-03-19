package dm.jb.ui.inv;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.IDateEditor;
import com.toedter.calendar.JDateChooser;
import dm.jb.db.objects.CustomerAdvancedRow;
import dm.jb.db.objects.CustomerAdvancedRow.Hobby;
import dm.jb.db.objects.CustomerAdvancedTableDef;
import dm.jb.db.objects.CustomerRow;
import dm.jb.ui.MainWindow;
import dm.jb.ui.common.HelpButton;
import dm.tools.types.InternalDate;
import dm.tools.ui.AbstractMainPanel;
import dm.tools.ui.ShuttlePane;
import dm.tools.ui.UICommon;
import dm.tools.utils.CommonConfig;
import dm.tools.utils.Validation;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

public class CustomerAdvancedPanel
  extends AbstractMainPanel
{
  public static CustomerAdvancedPanel INSTANCE = new CustomerAdvancedPanel();
  private Profession[] professions = { new Profession("Not Specified", 0), new Profession("Doctor", 1), new Profession("Executive", 2), new Profession("IT", 3), new Profession("Mechanical", 4), new Profession("Political", 5), new Profession("Sales", 6), new Profession("Sports", 7) };
  private JDateChooser _mDob = null;
  private JDateChooser _mAnniversary = null;
  private JTextField _mMobile = null;
  private JComboBox _mProfession = null;
  private JComboBox _mDomesticTravel = null;
  private JComboBox _mInTravel = null;
  private JTextField _mEmail = null;
  private ShuttlePane<CustomerAdvancedRow.Hobby> _mHobbies = null;
  private CustomerAdvancedRow _mData = null;
  private CustomerRow _mCustomer = null;
  
  private CustomerAdvancedPanel()
  {
    initUI();
  }
  
  public void setDefaultFocus() {}
  
  public JPanel getPanel()
  {
    return this;
  }
  
  public void windowDisplayed() {}
  
  public void clearAllFields()
  {
    this._mDob.setDate(null);
    this._mAnniversary.setDate(null);
    this._mMobile.setText("");
    this._mProfession.setSelectedIndex(0);
    this._mDomesticTravel.setSelectedIndex(0);
    this._mInTravel.setSelectedIndex(0);
    this._mHobbies.setToList(null);
  }
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("10px,90px,10px,100px,50px,50px,150px,10px:grow,10px", "10px,25px,10px,25px,10px,25px, 10px,25px,10px,25px,10px,25px,10px,25px,10px,150px,20px,30px,10px");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    int i = 2;
    JLabel localJLabel = new JLabel("Date of Birth : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mDob = new JDateChooser();
    this._mDob.setDateFormatString(CommonConfig.getInstance().dateFormat);
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mDob, localCellConstraints);
    this._mDob.getDateEditor().getUiComponent().setToolTipText("Customer date of birth");
    i += 2;
    localJLabel = new JLabel("Anniversary : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mAnniversary = new JDateChooser();
    this._mAnniversary.getDateEditor().getUiComponent().setToolTipText("Customer anniversary date");
    localCellConstraints.xywh(4, i, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mAnniversary, localCellConstraints);
    i += 2;
    localJLabel = new JLabel("Mobile : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mMobile = new JTextField();
    this._mMobile.setToolTipText("Customer mobile phone number");
    localCellConstraints.xywh(4, i, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mMobile, localCellConstraints);
    i += 2;
    localJLabel = new JLabel("E-Mail : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mEmail = new JTextField();
    this._mEmail.setToolTipText("Customer e-mail id");
    localCellConstraints.xywh(4, i, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mEmail, localCellConstraints);
    i += 2;
    localJLabel = new JLabel("Profession : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mProfession = new JComboBox(this.professions);
    this._mProfession.setToolTipText("Profession");
    localCellConstraints.xywh(4, i, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mProfession, localCellConstraints);
    i += 2;
    localJLabel = new JLabel("Domestic Travel : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mDomesticTravel = new JComboBox();
    this._mDomesticTravel.setToolTipText("Frequency of domestic travel");
    localCellConstraints.xywh(4, i, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mDomesticTravel, localCellConstraints);
    this._mDomesticTravel.addItem("Never");
    this._mDomesticTravel.addItem("Every year");
    this._mDomesticTravel.addItem("Every six months");
    this._mDomesticTravel.addItem("Every three months");
    this._mDomesticTravel.addItem("Monthly");
    this._mDomesticTravel.addItem("Weekly");
    i += 2;
    localJLabel = new JLabel("Travel abroad : ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(localJLabel, localCellConstraints);
    this._mInTravel = new JComboBox();
    this._mInTravel.setToolTipText("Frequency of international travel");
    localCellConstraints.xywh(4, i, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mInTravel, localCellConstraints);
    this._mInTravel.addItem("Never");
    this._mInTravel.addItem("Every year");
    this._mInTravel.addItem("Every six months");
    this._mInTravel.addItem("Every three months");
    this._mInTravel.addItem("Monthly");
    this._mInTravel.addItem("Weekly");
    i += 2;
    localJLabel = new JLabel("Hobbies: ");
    localCellConstraints.xywh(2, i, 1, 1, CellConstraints.RIGHT, CellConstraints.TOP);
    add(localJLabel, localCellConstraints);
    this._mHobbies = new ShuttlePane(false);
    localCellConstraints.xywh(4, i, 4, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mHobbies, localCellConstraints);
    this._mHobbies.setToolTipText("Hobbies");
    this._mHobbies.setFromList(CustomerAdvancedRow.Hobbies);
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
    localJPanel.setLayout(new FormLayout("10px,100px,pref:grow,100px,10px, 100px,10px", "30px"));
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(2, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    Object localObject = new JButton("Save");
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        CustomerAdvancedPanel.this.saveClicked();
      }
    });
    localCellConstraints.xywh(4, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localObject = new JButton("Close");
    localJPanel.add((Component)localObject, localCellConstraints);
    ((JButton)localObject).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        CustomerAdvancedPanel.this.closeWindow();
      }
    });
    localCellConstraints.xywh(6, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    localObject = new HelpButton("ISP_MANAGE_CUSTOMER");
    localJPanel.add((Component)localObject, localCellConstraints);
    return localJPanel;
  }
  
  void setCustomerAdvancedRow(CustomerRow paramCustomerRow, CustomerAdvancedRow paramCustomerAdvancedRow)
  {
    this._mData = paramCustomerAdvancedRow;
    this._mCustomer = paramCustomerRow;
    clearAllFields();
    if (paramCustomerAdvancedRow == null) {
      return;
    }
    this._mDob.setDate(paramCustomerAdvancedRow.getDob());
    this._mAnniversary.setDate(paramCustomerAdvancedRow.getAnniversary());
    this._mMobile.setText(paramCustomerAdvancedRow.getMobile());
    this._mProfession.setSelectedItem(getProfessionForCode(paramCustomerAdvancedRow.getProfession()));
    this._mDomesticTravel.setSelectedIndex(paramCustomerAdvancedRow.getDomesticTravel());
    this._mInTravel.setSelectedIndex(paramCustomerAdvancedRow.getIntTravel());
    ArrayList localArrayList = CustomerAdvancedRow.getHobbiesForCode(paramCustomerAdvancedRow.getHobby());
    this._mHobbies.setToList(localArrayList);
    this._mHobbies.setFromList(CustomerAdvancedRow.Hobbies);
  }
  
  public void saveClicked()
  {
    String str1 = this._mMobile.getText().trim();
    if ((str1.length() > 0) && (!Validation.isValidPhone(str1)))
    {
      UICommon.showError("Invalid mobile phone number.", "Error", MainWindow.instance);
      this._mMobile.requestFocusInWindow();
      return;
    }
    String str2 = this._mEmail.getText().trim();
    if ((str2.length() > 0) && (!Validation.isValidEMail(str2)))
    {
      UICommon.showError("Invalid email address.", "Error", MainWindow.instance);
      this._mEmail.requestFocusInWindow();
      return;
    }
    saveAdvancedRow();
    closeWindow();
  }
  
  public void saveAdvancedRow()
  {
    if (this._mData == null)
    {
      this._mData = CustomerAdvancedTableDef.getInstance().getNewRow();
      this._mData.setCreated(true);
    }
    this._mData.setDob(InternalDate.getSqlDate(this._mDob.getDate()));
    this._mData.setAnniversary(InternalDate.getSqlDate(this._mAnniversary.getDate()));
    String str1 = this._mMobile.getText().trim();
    if (str1.length() == 0) {
      this._mData.setMobile(null);
    } else {
      this._mData.setMobile(str1);
    }
    String str2 = this._mEmail.getText().trim();
    if (str2.length() == 0) {
      this._mData.setEmail(null);
    } else {
      this._mData.setEmail(str2);
    }
    Profession localProfession = (Profession)this._mProfession.getSelectedItem();
    this._mData.setProfession(localProfession.code);
    this._mData.setDomesticTravel(this._mDomesticTravel.getSelectedIndex());
    this._mData.setIntTravel(this._mInTravel.getSelectedIndex());
    ArrayList localArrayList = this._mHobbies.getSelectedObjects();
    String str3 = CustomerAdvancedRow.getHobbyStringFromList(localArrayList);
    this._mData.setHobby(str3);
    if (this._mCustomer != null) {
      this._mCustomer.setAdvancedRow(this._mData);
    }
  }
  
  public CustomerAdvancedRow getAdvancedRow()
  {
    return this._mData;
  }
  
  private Profession getProfessionForCode(int paramInt)
  {
    for (int i = 0; i < this.professions.length; i++) {
      if (this.professions[i].code == paramInt) {
        return this.professions[i];
      }
    }
    return null;
  }
  
  public class Profession
  {
    String name;
    int code;
    
    public Profession(String paramString, int paramInt)
    {
      this.name = paramString;
      this.code = paramInt;
    }
    
    public String toString()
    {
      return this.name;
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.inv.CustomerAdvancedPanel
 * JD-Core Version:    0.7.0.1
 */