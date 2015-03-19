package dm.tools.ui;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class JTextAndComboBox
  extends JPanel
{
  private JTextField _mTextBox = null;
  private JComboBox _mComboBox = null;
  private boolean _mComboDisplayed = true;
  Object[] _mItems = null;
  
  public JTextAndComboBox(Object[] paramArrayOfObject)
  {
    this._mItems = paramArrayOfObject;
    initUI();
  }
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("pref:grow", "pref:grow");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    if (this._mItems == null) {
      this._mComboBox = new JComboBox();
    } else {
      this._mComboBox = new JComboBox(this._mItems);
    }
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mComboBox, localCellConstraints);
    this._mComboDisplayed = true;
  }
  
  public void addItem(Object paramObject)
  {
    this._mComboBox.addItem(paramObject);
  }
  
  public Object getSelectedItem()
  {
    return this._mComboBox.getSelectedItem();
  }
  
  public void setTextField()
  {
    if (this._mTextBox == null)
    {
      this._mTextBox = new JTextField();
      this._mTextBox.setEditable(false);
    }
    if (this._mComboDisplayed)
    {
      this._mComboBox.setVisible(false);
      remove(this._mComboBox);
    }
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mTextBox, localCellConstraints);
    if (this._mComboBox != null) {
      this._mTextBox.setText(this._mComboBox.getSelectedItem().toString());
    }
    this._mComboDisplayed = false;
    this._mTextBox.setVisible(true);
  }
  
  public void setComboField()
  {
    if ((!this._mComboDisplayed) && (this._mTextBox != null))
    {
      this._mTextBox.setVisible(false);
      remove(this._mTextBox);
    }
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mComboBox, localCellConstraints);
    this._mComboBox.setVisible(true);
    this._mComboDisplayed = true;
  }
  
  public void addActionListener(ActionListener paramActionListener)
  {
    this._mComboBox.addActionListener(paramActionListener);
  }
  
  public int getSelectedIndex()
  {
    return this._mComboBox.getSelectedIndex();
  }
  
  public void setSelectedIndex(int paramInt)
  {
    this._mComboBox.setSelectedIndex(paramInt);
  }
  
  public void setSelectedItem(Object paramObject)
  {
    this._mComboBox.setSelectedItem(paramObject);
  }
  
  public void setTextFieldText(String paramString)
  {
    if (!this._mComboDisplayed) {
      this._mTextBox.setText(paramString);
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.ui.JTextAndComboBox
 * JD-Core Version:    0.7.0.1
 */