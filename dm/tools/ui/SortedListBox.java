package dm.tools.ui;

import javax.swing.DefaultListModel;
import javax.swing.JList;

public class SortedListBox
  extends JList
{
  private DefaultListModel _mModel = null;
  public static final int SORT_MODE_NONE = 0;
  public static final int SORT_MODE_ASC = 2;
  public static final int SORT_MODE_DESC = 3;
  private int _mSortMode = 0;
  
  public SortedListBox(int paramInt)
  {
    setModel(this._mModel);
    initUI();
    this._mSortMode = paramInt;
  }
  
  private void initUI() {}
  
  public int addElement(Object paramObject)
  {
    if (this._mSortMode == 2) {
      return addToAscSortedList(paramObject);
    }
    if (this._mSortMode == 3) {
      return addToDescSortedList(paramObject);
    }
    this._mModel.addElement(paramObject);
    return this._mModel.size() - 1;
  }
  
  private int addToAscSortedList(Object paramObject)
  {
    int i = this._mModel.getSize();
    for (int j = 0; j < i; j++)
    {
      Object localObject = this._mModel.get(j);
      if (localObject.toString().compareTo(paramObject.toString()) > 0)
      {
        this._mModel.insertElementAt(paramObject, j);
        return j;
      }
    }
    this._mModel.addElement(paramObject);
    return this._mModel.size() - 1;
  }
  
  private int addToDescSortedList(Object paramObject)
  {
    int i = this._mModel.getSize();
    for (int j = 0; j < i; j++)
    {
      Object localObject = this._mModel.get(j);
      if (localObject.toString().compareTo(paramObject.toString()) < 0)
      {
        this._mModel.insertElementAt(paramObject, j);
        return j;
      }
    }
    this._mModel.addElement(paramObject);
    return this._mModel.size() - 1;
  }
  
  public void clear()
  {
    this._mModel.clear();
  }
  
  public void remove(Object paramObject)
  {
    this._mModel.removeElement(paramObject);
  }
  
  public Object getElementAt(int paramInt)
  {
    return this._mModel.getElementAt(paramInt);
  }
  
  public void removeElement(Object paramObject)
  {
    this._mModel.removeElement(paramObject);
  }
  
  public int updateItem(Object paramObject)
  {
    this._mModel.removeElement(paramObject);
    return addElement(paramObject);
  }
  
  public int indexOf(Object paramObject)
  {
    return this._mModel.indexOf(paramObject);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.ui.SortedListBox
 * JD-Core Version:    0.7.0.1
 */