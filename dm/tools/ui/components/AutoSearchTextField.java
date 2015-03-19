package dm.tools.ui.components;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JWindow;

public class AutoSearchTextField
  extends JTextField
{
  private JWindow dataWindow = null;
  private JList _mDataList = null;
  private DefaultListModel _mModel = null;
  private boolean _mAutoEnabled = false;
  private String _mOldText = "";
  private Object[] _mData = null;
  
  public void setAutoSearchEnabled(boolean paramBoolean)
  {
    this._mAutoEnabled = paramBoolean;
    if (!paramBoolean) {
      return;
    }
    if (this.dataWindow == null)
    {
      this.dataWindow = new JWindow();
      this.dataWindow.setSize(400, 400);
      JPanel localJPanel = (JPanel)this.dataWindow.getContentPane();
      localJPanel.setLayout(new BorderLayout());
      this._mModel = new DefaultListModel();
      this._mDataList = new JList(this._mModel);
      JScrollPane localJScrollPane = new JScrollPane(this._mDataList);
      localJPanel.add(localJScrollPane, "Center");
      addKeyListener(new KeyAdapter()
      {
        public void keyReleased(KeyEvent paramAnonymousKeyEvent)
        {
          String str1 = AutoSearchTextField.this._mOldText;
          if (paramAnonymousKeyEvent.getKeyCode() == 8)
          {
            if (str1.length() > 0) {
              AutoSearchTextField.this.setText(str1.substring(0, str1.length() - 1));
            }
          }
          else
          {
            String str2;
            if (paramAnonymousKeyEvent.getKeyCode() == 39)
            {
              str2 = AutoSearchTextField.this.getText();
              if (str1.length() < str2.length()) {
                AutoSearchTextField.this.setText(str2.substring(0, str1.length() + 1));
              }
            }
            else if (paramAnonymousKeyEvent.getKeyCode() == 37)
            {
              str2 = AutoSearchTextField.this.getText();
              if ((str1.length() < str2.length()) && (str1.length() > 0)) {
                AutoSearchTextField.this.setText(str2.substring(0, str1.length() - 1));
              }
            }
            else if (paramAnonymousKeyEvent.getKeyCode() == 10)
            {
              str2 = AutoSearchTextField.this.getText();
              AutoSearchTextField.this.setSelectionStart(str2.length());
              AutoSearchTextField.this.setSelectionEnd(str2.length());
              AutoSearchTextField.this.dataWindow.setVisible(false);
              return;
            }
          }
          if (!AutoSearchTextField.this._mOldText.equals(AutoSearchTextField.this.getText())) {
            AutoSearchTextField.this.dataChanged();
          }
        }
      });
      addFocusListener(new FocusListener()
      {
        public void focusGained(FocusEvent paramAnonymousFocusEvent) {}
        
        public void focusLost(FocusEvent paramAnonymousFocusEvent)
        {
          if (AutoSearchTextField.this.dataWindow != null) {
            AutoSearchTextField.this.dataWindow.setVisible(false);
          }
        }
      });
    }
  }
  
  public void setData(ArrayList<?> paramArrayList)
  {
    this._mModel.removeAllElements();
    this._mData = null;
    this._mData = new Object[paramArrayList.size()];
    int i = 0;
    Iterator localIterator = paramArrayList.iterator();
    while (localIterator.hasNext())
    {
      Object localObject = localIterator.next();
      this._mModel.addElement(localObject);
      this._mData[i] = localObject;
      i++;
    }
  }
  
  private void dataChanged()
  {
    if (!this._mAutoEnabled) {
      return;
    }
    String str1 = getText();
    Object localObject;
    if (!this.dataWindow.isVisible())
    {
      localObject = getLocationOnScreen();
      ((Point)localObject).setLocation(((Point)localObject).getX(), ((Point)localObject).getY() + getHeight());
      this.dataWindow.setLocation((Point)localObject);
      this.dataWindow.setVisible(true);
    }
    if (str1.length() > this._mOldText.length())
    {
      runRemoveFilter(str1);
    }
    else if (str1.length() < this._mOldText.length())
    {
      runAddFilter(str1);
    }
    else
    {
      runAddFilter(str1);
      runRemoveFilter(str1);
    }
    if (this._mModel.getSize() > 0)
    {
      this._mDataList.setSelectedIndex(0);
      localObject = this._mModel.getElementAt(0);
      String str2 = localObject.toString();
      int i = getCaretPosition();
      setText(str2);
      setCaretPosition(str2.length());
      moveCaretPosition(i);
    }
    this._mOldText = str1;
    this._mDataList.updateUI();
  }
  
  private void runRemoveFilter(String paramString)
  {
    int i = this._mModel.getSize();
    int j = 0;
    for (int k = 0; k < i; k++)
    {
      Object localObject = this._mModel.getElementAt(j);
      if (localObject.toString().startsWith(paramString)) {
        j++;
      } else {
        this._mModel.removeElementAt(j);
      }
    }
  }
  
  private void runAddFilter(String paramString)
  {
    int i = 0;
    int j = this._mModel.getSize();
    int k = 0;
    while (i < this._mData.length)
    {
      Object localObject1 = this._mData[i];
      if (!localObject1.toString().startsWith(paramString))
      {
        i++;
      }
      else
      {
        String str = localObject1.toString();
        int m = 0;
        for (m = k; m < j; m++)
        {
          Object localObject2 = this._mModel.getElementAt(m);
          if (localObject2.toString().compareTo(str) > 0)
          {
            this._mModel.insertElementAt(localObject1, m);
            k = m;
            i++;
            break;
          }
          if (localObject1 == localObject2)
          {
            i++;
            break;
          }
        }
        if (m == j)
        {
          this._mModel.addElement(localObject1);
          k = j;
          j = k + 1;
          i++;
        }
      }
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.ui.components.AutoSearchTextField
 * JD-Core Version:    0.7.0.1
 */