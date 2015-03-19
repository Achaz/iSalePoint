package dm.tools.ui;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ShuttlePane<E>
  extends JPanel
{
  private JList _mFromList = null;
  private JList _mToList = null;
  private DefaultListModel _mFromModel = null;
  private DefaultListModel _mToModel = null;
  private boolean _mPosition = false;
  private JButton _mRightButton = null;
  private JButton _mLeftButton = null;
  private JButton _mRightAllButton = null;
  private JButton _mLeftAllButton = null;
  private JButton _mUpBtn = null;
  private JButton _mDownBtn = null;
  private JButton _mUpAllBtn = null;
  private JButton _mDownAllBtn = null;
  ArrayList<E> _mFromArrayList = null;
  ArrayList<E> _mToArrayList = null;
  private ArrayList<ShuttlePaneActionListener> _mSelectListenerList = new ArrayList();
  
  public ShuttlePane(boolean paramBoolean)
  {
    this._mPosition = paramBoolean;
    initUI();
  }
  
  public void setFromList(E[] paramArrayOfE)
  {
    ArrayList localArrayList = new ArrayList();
    for (int i = 0; i < paramArrayOfE.length; i++) {
      localArrayList.add(paramArrayOfE[i]);
    }
    setFromList(localArrayList);
  }
  
  public void setFromList(ArrayList<E> paramArrayList)
  {
    if (this._mFromArrayList == null) {
      this._mFromArrayList = new ArrayList();
    }
    this._mFromArrayList.clear();
    this._mFromModel.removeAllElements();
    if (paramArrayList == null) {
      return;
    }
    Iterator localIterator = paramArrayList.iterator();
    while (localIterator.hasNext())
    {
      Object localObject = localIterator.next();
      if ((this._mToModel == null) || (!this._mToModel.contains(localObject))) {
        this._mFromArrayList.add(localObject);
      }
    }
    if (paramArrayList != null)
    {
      fillFromList();
    }
    else
    {
      this._mRightAllButton.setEnabled(false);
      this._mRightButton.setEnabled(false);
      this._mToModel.removeAllElements();
    }
  }
  
  public void setToList(ArrayList<E> paramArrayList)
  {
    if (this._mToArrayList == null) {
      this._mToArrayList = new ArrayList();
    }
    this._mToArrayList.clear();
    this._mToModel.removeAllElements();
    if (paramArrayList == null) {
      return;
    }
    Iterator localIterator = paramArrayList.iterator();
    while (localIterator.hasNext())
    {
      Object localObject = localIterator.next();
      this._mToArrayList.add(localObject);
    }
    if (paramArrayList != null)
    {
      fillToList();
    }
    else
    {
      this._mLeftAllButton.setEnabled(false);
      this._mLeftButton.setEnabled(false);
      this._mToModel.removeAllElements();
    }
  }
  
  private void initUI()
  {
    FormLayout localFormLayout = null;
    CellConstraints localCellConstraints = new CellConstraints();
    if (this._mPosition) {
      localFormLayout = new FormLayout("100px:grow,2dlu, pref, 2dlu, 100px:grow, 2dlu, pref", "pref:grow, pref, 4dlu, pref, 4dlu,pref, 4dlu, pref, pref:grow");
    } else {
      localFormLayout = new FormLayout("100px:grow,2dlu, pref, 2dlu, 100px:grow", "pref:grow, pref, 4dlu, pref, 4dlu,pref, 4dlu, pref, pref:grow");
    }
    setLayout(localFormLayout);
    this._mRightButton = getIconButton("/dm/tools/images/right_arrow.gif", "Add selected to right");
    localCellConstraints.xywh(3, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mRightButton, localCellConstraints);
    this._mRightButton.setMnemonic(49);
    this._mRightButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ShuttlePane.this.rightClicked();
        ShuttlePane.this.updateUpDwonBasedOnSelection(ShuttlePane.this._mToList.getSelectedValues());
      }
    });
    this._mRightAllButton = getIconButton("/dm/tools/images/right_all.gif", "Add all to left");
    localCellConstraints.xywh(3, 4, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mRightAllButton, localCellConstraints);
    this._mRightAllButton.setMnemonic(50);
    this._mRightAllButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ShuttlePane.this.rightAllClicked();
        ShuttlePane.this.updateUpDwonBasedOnSelection(ShuttlePane.this._mToList.getSelectedValues());
      }
    });
    this._mLeftButton = getIconButton("/dm/tools/images/left_arrow.gif", "Add selected to right");
    localCellConstraints.xywh(3, 6, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mLeftButton, localCellConstraints);
    this._mLeftButton.setMnemonic(51);
    this._mLeftButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ShuttlePane.this.leftClicked();
        ShuttlePane.this.updateUpDwonBasedOnSelection(ShuttlePane.this._mToList.getSelectedValues());
      }
    });
    this._mLeftAllButton = getIconButton("/dm/tools/images/left_all.gif", "Add all to right");
    localCellConstraints.xywh(3, 8, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    add(this._mLeftAllButton, localCellConstraints);
    this._mLeftAllButton.setMnemonic(52);
    this._mLeftAllButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        ShuttlePane.this.leftAllClicked();
        ShuttlePane.this.updateUpDwonBasedOnSelection(ShuttlePane.this._mToList.getSelectedValues());
      }
    });
    this._mToModel = new DefaultListModel();
    this._mToList = new JList(this._mToModel);
    JScrollPane localJScrollPane = new JScrollPane(this._mToList);
    localCellConstraints.xywh(5, 1, 1, 9, CellConstraints.FILL, CellConstraints.FILL);
    add(localJScrollPane, localCellConstraints);
    this._mToList.addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent paramAnonymousListSelectionEvent)
      {
        if (!paramAnonymousListSelectionEvent.getValueIsAdjusting())
        {
          Object[] arrayOfObject = ShuttlePane.this._mToList.getSelectedValues();
          Iterator localIterator;
          ShuttlePaneActionListener localShuttlePaneActionListener;
          if (arrayOfObject.length == 0)
          {
            ShuttlePane.this._mLeftButton.setEnabled(false);
            if (ShuttlePane.this._mPosition)
            {
              ShuttlePane.this._mUpBtn.setEnabled(false);
              ShuttlePane.this._mDownBtn.setEnabled(false);
              ShuttlePane.this._mUpAllBtn.setEnabled(false);
              ShuttlePane.this._mDownAllBtn.setEnabled(false);
              localIterator = ShuttlePane.this._mSelectListenerList.iterator();
              while (localIterator.hasNext())
              {
                localShuttlePaneActionListener = (ShuttlePaneActionListener)localIterator.next();
                localShuttlePaneActionListener.objectSelected(arrayOfObject);
              }
            }
          }
          else
          {
            ShuttlePane.this._mLeftButton.setEnabled(true);
            if (ShuttlePane.this._mPosition) {
              ShuttlePane.this.updateUpDwonBasedOnSelection(arrayOfObject);
            }
            localIterator = ShuttlePane.this._mSelectListenerList.iterator();
            while (localIterator.hasNext())
            {
              localShuttlePaneActionListener = (ShuttlePaneActionListener)localIterator.next();
              localShuttlePaneActionListener.objectSelected(arrayOfObject);
            }
          }
        }
      }
    });
    this._mFromModel = new DefaultListModel();
    this._mFromList = new JList(this._mFromModel);
    localJScrollPane = new JScrollPane(this._mFromList);
    localCellConstraints.xywh(1, 1, 1, 9, CellConstraints.FILL, CellConstraints.FILL);
    add(localJScrollPane, localCellConstraints);
    this._mFromList.addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent paramAnonymousListSelectionEvent)
      {
        if (!paramAnonymousListSelectionEvent.getValueIsAdjusting())
        {
          Object[] arrayOfObject = ShuttlePane.this._mFromList.getSelectedValues();
          if (arrayOfObject.length == 0) {
            ShuttlePane.this._mRightButton.setEnabled(false);
          } else {
            ShuttlePane.this._mRightButton.setEnabled(true);
          }
        }
      }
    });
    if (this._mPosition)
    {
      this._mUpAllBtn = getIconButton("/dm/tools/images/up_all.gif", "Move to top");
      localCellConstraints.xywh(7, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
      add(this._mUpAllBtn, localCellConstraints);
      this._mUpAllBtn.setMnemonic(37);
      this._mUpAllBtn.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          ShuttlePane.this.upAllClicked();
        }
      });
      this._mUpBtn = getIconButton("/dm/tools/images/up.gif", "Move up");
      localCellConstraints.xywh(7, 4, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
      add(this._mUpBtn, localCellConstraints);
      this._mUpBtn.setMnemonic(38);
      this._mUpBtn.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          ShuttlePane.this.upClicked();
        }
      });
      this._mDownBtn = getIconButton("/dm/tools/images/down.gif", "Move down");
      localCellConstraints.xywh(7, 6, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
      add(this._mDownBtn, localCellConstraints);
      this._mDownBtn.setMnemonic(40);
      this._mDownBtn.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          ShuttlePane.this.downClicked();
        }
      });
      this._mDownAllBtn = getIconButton("/dm/tools/images/down_all.gif", "Move to bottom");
      localCellConstraints.xywh(7, 8, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
      add(this._mDownAllBtn, localCellConstraints);
      this._mDownAllBtn.setMnemonic(39);
      this._mDownAllBtn.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          ShuttlePane.this.downAllClicked();
        }
      });
      this._mUpBtn.setEnabled(false);
      this._mUpAllBtn.setEnabled(false);
      this._mDownBtn.setEnabled(false);
      this._mDownAllBtn.setEnabled(false);
    }
    this._mRightAllButton.setEnabled(false);
    this._mLeftAllButton.setEnabled(false);
    this._mRightButton.setEnabled(false);
    this._mLeftButton.setEnabled(false);
  }
  
  private void fillFromList()
  {
    Iterator localIterator = this._mFromArrayList.iterator();
    this._mFromModel.removeAllElements();
    if (localIterator.hasNext()) {
      this._mRightAllButton.setEnabled(true);
    } else {
      this._mRightAllButton.setEnabled(false);
    }
    while (localIterator.hasNext())
    {
      Object localObject = localIterator.next();
      this._mFromModel.addElement(localObject);
    }
  }
  
  private void fillToList()
  {
    Iterator localIterator = this._mToArrayList.iterator();
    this._mToModel.removeAllElements();
    if (localIterator.hasNext()) {
      this._mLeftAllButton.setEnabled(true);
    } else {
      this._mLeftAllButton.setEnabled(false);
    }
    while (localIterator.hasNext())
    {
      Object localObject = localIterator.next();
      this._mToModel.addElement(localObject);
    }
  }
  
  private void updateUpDwonBasedOnSelection(Object[] paramArrayOfObject)
  {
    if (!this._mPosition) {
      return;
    }
    if (paramArrayOfObject.length == 1)
    {
      Object localObject = paramArrayOfObject[0];
      int i = this._mToModel.indexOf(localObject);
      this._mUpBtn.setEnabled(true);
      this._mDownBtn.setEnabled(true);
      this._mUpAllBtn.setEnabled(true);
      this._mDownAllBtn.setEnabled(true);
      if (i == this._mToModel.size() - 1)
      {
        this._mDownAllBtn.setEnabled(false);
        this._mDownBtn.setEnabled(false);
      }
      if (i == 0)
      {
        this._mUpAllBtn.setEnabled(false);
        this._mUpBtn.setEnabled(false);
      }
      if ((this._mToModel.size() == 1) && (this._mUpBtn != null))
      {
        this._mUpBtn.setEnabled(false);
        this._mDownBtn.setEnabled(false);
        this._mUpAllBtn.setEnabled(false);
        this._mDownAllBtn.setEnabled(false);
      }
    }
    else
    {
      this._mUpBtn.setEnabled(false);
      this._mDownBtn.setEnabled(false);
      this._mUpAllBtn.setEnabled(false);
      this._mDownAllBtn.setEnabled(false);
    }
  }
  
  private JButton getIconButton(String paramString1, String paramString2)
  {
    JButton localJButton = null;
    URL localURL = getClass().getResource(paramString1);
    ImageIcon localImageIcon = new ImageIcon(localURL);
    localJButton = new JButton(localImageIcon);
    localJButton.setToolTipText(paramString2);
    return localJButton;
  }
  
  private void rightClicked()
  {
    Object[] arrayOfObject = this._mFromList.getSelectedValues();
    for (int i = 0; i < arrayOfObject.length; i++)
    {
      this._mFromModel.removeElement(arrayOfObject[i]);
      int j = 0;
      Iterator localIterator = this._mSelectListenerList.iterator();
      while (localIterator.hasNext())
      {
        ShuttlePaneActionListener localShuttlePaneActionListener = (ShuttlePaneActionListener)localIterator.next();
        if (!localShuttlePaneActionListener.addToToList(arrayOfObject[i]))
        {
          j = 1;
          break;
        }
      }
      if (j == 0) {
        this._mToModel.addElement(arrayOfObject[i]);
      }
    }
    if (this._mFromModel.size() == 0) {
      this._mRightAllButton.setEnabled(false);
    }
    this._mLeftAllButton.setEnabled(true);
  }
  
  private void leftClicked()
  {
    Object[] arrayOfObject = this._mToList.getSelectedValues();
    for (int i = 0; i < arrayOfObject.length; i++)
    {
      this._mToModel.removeElement(arrayOfObject[i]);
      int j = 0;
      Iterator localIterator = this._mSelectListenerList.iterator();
      while (localIterator.hasNext())
      {
        ShuttlePaneActionListener localShuttlePaneActionListener = (ShuttlePaneActionListener)localIterator.next();
        if (!localShuttlePaneActionListener.addToFromList(arrayOfObject[i]))
        {
          j = 1;
          break;
        }
      }
      if (j == 0) {
        this._mFromModel.addElement(arrayOfObject[i]);
      }
    }
    if (this._mToModel.size() == 0) {
      this._mLeftAllButton.setEnabled(false);
    }
    this._mRightAllButton.setEnabled(true);
  }
  
  private void leftAllClicked()
  {
    int i = this._mToModel.size();
    for (int j = 0; j < i; j++)
    {
      Object localObject = this._mToModel.getElementAt(0);
      this._mToModel.removeElement(localObject);
      int k = 0;
      Iterator localIterator = this._mSelectListenerList.iterator();
      while (localIterator.hasNext())
      {
        ShuttlePaneActionListener localShuttlePaneActionListener = (ShuttlePaneActionListener)localIterator.next();
        if (!localShuttlePaneActionListener.addToFromList(localObject))
        {
          k = 1;
          break;
        }
      }
      if (k == 0) {
        this._mFromModel.addElement(localObject);
      }
    }
    this._mLeftAllButton.setEnabled(false);
    this._mRightAllButton.setEnabled(true);
  }
  
  private void rightAllClicked()
  {
    int i = this._mFromModel.size();
    for (int j = 0; j < i; j++)
    {
      Object localObject = this._mFromModel.getElementAt(0);
      this._mFromModel.removeElement(localObject);
      int k = 0;
      Iterator localIterator = this._mSelectListenerList.iterator();
      while (localIterator.hasNext())
      {
        ShuttlePaneActionListener localShuttlePaneActionListener = (ShuttlePaneActionListener)localIterator.next();
        if (!localShuttlePaneActionListener.addToToList(localObject))
        {
          k = 1;
          break;
        }
      }
      if (k == 0) {
        this._mToModel.addElement(localObject);
      }
    }
    this._mRightAllButton.setEnabled(false);
    this._mLeftAllButton.setEnabled(true);
  }
  
  public ArrayList<E> getSelectedObjects()
  {
    ArrayList localArrayList = new ArrayList();
    int i = this._mToModel.size();
    for (int j = 0; j < i; j++)
    {
      Object localObject = this._mToModel.getElementAt(j);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public ArrayList<E> getUnSelectedObjects()
  {
    ArrayList localArrayList = new ArrayList();
    int i = this._mFromModel.size();
    for (int j = 0; j < i; j++)
    {
      Object localObject = this._mFromModel.getElementAt(j);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public Object[] getListSelectedObjects()
  {
    return this._mToList.getSelectedValues();
  }
  
  private void upClicked()
  {
    Object[] arrayOfObject = this._mToList.getSelectedValues();
    int i = this._mToModel.indexOf(arrayOfObject[0]);
    this._mToModel.removeElementAt(i);
    this._mToModel.insertElementAt(arrayOfObject[0], i - 1);
    this._mToList.setSelectedIndex(i - 1);
  }
  
  private void downClicked()
  {
    Object[] arrayOfObject = this._mToList.getSelectedValues();
    int i = this._mToModel.indexOf(arrayOfObject[0]);
    this._mToModel.removeElementAt(i);
    this._mToModel.insertElementAt(arrayOfObject[0], i + 1);
    this._mToList.setSelectedIndex(i + 1);
  }
  
  private void upAllClicked()
  {
    Object[] arrayOfObject = this._mToList.getSelectedValues();
    int i = this._mToModel.indexOf(arrayOfObject[0]);
    this._mToModel.removeElementAt(i);
    this._mToModel.insertElementAt(arrayOfObject[0], 0);
    this._mToList.setSelectedIndex(0);
  }
  
  private void downAllClicked()
  {
    Object[] arrayOfObject = this._mToList.getSelectedValues();
    int i = this._mToModel.size();
    int j = this._mToModel.indexOf(arrayOfObject[0]);
    this._mToModel.removeElementAt(j);
    this._mToModel.insertElementAt(arrayOfObject[0], i - 1);
    this._mToList.setSelectedIndex(i - 1);
  }
  
  public void addSelectionListener(ShuttlePaneActionListener paramShuttlePaneActionListener)
  {
    this._mSelectListenerList.add(paramShuttlePaneActionListener);
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    this._mFromList.setEnabled(paramBoolean);
    this._mToList.setEnabled(paramBoolean);
    if (!paramBoolean)
    {
      if (this._mPosition)
      {
        this._mUpBtn.setEnabled(paramBoolean);
        this._mUpAllBtn.setEnabled(paramBoolean);
        this._mDownBtn.setEnabled(paramBoolean);
        this._mDownAllBtn.setEnabled(paramBoolean);
      }
      this._mRightAllButton.setEnabled(false);
      this._mRightButton.setEnabled(false);
      this._mLeftAllButton.setEnabled(false);
      this._mLeftButton.setEnabled(false);
    }
    else
    {
      if (this._mPosition) {
        updateUpDwonBasedOnSelection(this._mToList.getSelectedValues());
      }
      if (this._mFromModel.getSize() > 0) {
        this._mRightAllButton.setEnabled(true);
      } else {
        this._mRightAllButton.setEnabled(false);
      }
      if (this._mFromList.getSelectedValues().length > 0) {
        this._mRightButton.setEnabled(true);
      } else {
        this._mRightButton.setEnabled(false);
      }
      if (this._mToModel.getSize() > 0) {
        this._mLeftAllButton.setEnabled(true);
      } else {
        this._mLeftAllButton.setEnabled(false);
      }
      if (this._mToList.getSelectedValues().length > 0) {
        this._mLeftButton.setEnabled(true);
      } else {
        this._mLeftButton.setEnabled(false);
      }
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.ui.ShuttlePane
 * JD-Core Version:    0.7.0.1
 */