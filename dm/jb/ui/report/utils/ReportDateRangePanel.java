package dm.jb.ui.report.utils;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dm.tools.ui.CustomDateChooser;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class ReportDateRangePanel
  extends JPanel
{
  private JRadioButton _mRangeRadio = null;
  private JRadioButton _mPastRadio = null;
  private CustomDateChooser _mFromDate = null;
  private CustomDateChooser _mToDate = null;
  private JTextField _mPeriod = null;
  private JComboBox _mPeriodUnit = null;
  private JCheckBox _mIncludeToday = null;
  
  public ReportDateRangePanel()
  {
    initUI();
    radioSelected();
  }
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("90px,10px,100px,3px, 7px, 50px, 10px, 40px, 60px,10px", "25px,10px,25px");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    ButtonGroup localButtonGroup = new ButtonGroup();
    this._mRangeRadio = new JRadioButton("Date range");
    localButtonGroup.add(this._mRangeRadio);
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(this._mRangeRadio, localCellConstraints);
    this._mRangeRadio.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ReportDateRangePanel.this.radioSelected();
      }
    });
    this._mFromDate = new CustomDateChooser(new Date());
    localCellConstraints.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mFromDate, localCellConstraints);
    localCellConstraints.xywh(4, 1, 2, 1, CellConstraints.CENTER, CellConstraints.FILL);
    add(new JLabel("-"), localCellConstraints);
    this._mToDate = new CustomDateChooser(new Date());
    localCellConstraints.xywh(6, 1, 3, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mToDate, localCellConstraints);
    this._mPastRadio = new JRadioButton("Duration");
    localButtonGroup.add(this._mPastRadio);
    localCellConstraints.xywh(1, 3, 1, 1, CellConstraints.RIGHT, CellConstraints.FILL);
    add(this._mPastRadio, localCellConstraints);
    this._mPastRadio.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ReportDateRangePanel.this.radioSelected();
      }
    });
    this._mRangeRadio.setSelected(true);
    this._mPeriod = new JTextField();
    localCellConstraints.xywh(3, 3, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mPeriod, localCellConstraints);
    this._mPeriodUnit = new JComboBox();
    localCellConstraints.xywh(5, 3, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mPeriodUnit, localCellConstraints);
    this._mIncludeToday = new JCheckBox("Today inclusive");
    localCellConstraints.xywh(8, 3, 2, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mIncludeToday, localCellConstraints);
  }
  
  public void setBackground(Color paramColor)
  {
    super.setBackground(paramColor);
    if (this._mRangeRadio != null)
    {
      this._mRangeRadio.setBackground(paramColor);
      this._mPastRadio.setBackground(paramColor);
      this._mFromDate.setBackground(paramColor);
      this._mToDate.setBackground(paramColor);
      this._mPeriod.setBackground(paramColor);
      this._mPeriodUnit.setBackground(paramColor);
      this._mIncludeToday.setBackground(paramColor);
    }
  }
  
  private void radioSelected()
  {
    if (this._mRangeRadio.isSelected())
    {
      this._mFromDate.setEnabled(true);
      this._mToDate.setEnabled(true);
      this._mPeriod.setEnabled(false);
      this._mPeriodUnit.setEnabled(false);
      this._mIncludeToday.setEnabled(false);
    }
    else
    {
      this._mFromDate.setEnabled(false);
      this._mToDate.setEnabled(false);
      this._mPeriod.setEnabled(true);
      this._mPeriodUnit.setEnabled(true);
      this._mIncludeToday.setEnabled(true);
    }
  }
  
  public void resetAll()
  {
    this._mRangeRadio.setSelected(true);
    radioSelected();
    this._mRangeRadio.setEnabled(true);
    this._mPastRadio.setEnabled(true);
  }
  
  public void setTodayDate()
  {
    this._mToDate.setEnabled(false);
    this._mFromDate.setEnabled(false);
    this._mRangeRadio.setEnabled(false);
    this._mPastRadio.setEnabled(false);
  }
  
  public void setDateRange(Date paramDate1, Date paramDate2)
  {
    if (paramDate1 != null) {
      this._mFromDate.setDate(paramDate1);
    }
    if (this._mToDate != null) {
      this._mFromDate.setDate(paramDate2);
    }
  }
  
  public Date getFromDate()
  {
    return this._mFromDate.getDate();
  }
  
  public Date getToDate()
  {
    return this._mToDate.getDate();
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.report.utils.ReportDateRangePanel
 * JD-Core Version:    0.7.0.1
 */