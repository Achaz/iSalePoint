package dm.tools.utils;

import java.util.ArrayList;

public class Stack<E>
{
  private ArrayList<E> _mElements = null;
  private int _mElementCount = 0;
  
  public void push(E paramE)
  {
    this._mElements.add(paramE);
    this._mElementCount += 1;
  }
  
  public E pop()
  {
    Object localObject = this._mElements.get(this._mElementCount - 1);
    this._mElements.remove(localObject);
    this._mElementCount -= 1;
    return localObject;
  }
  
  public boolean isEmpty()
  {
    return this._mElementCount == 0;
  }
  
  public boolean contains(E paramE)
  {
    return this._mElements.indexOf(paramE) != -1;
  }
  
  public void popAll()
  {
    this._mElements.clear();
    this._mElementCount = 0;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.utils.Stack
 * JD-Core Version:    0.7.0.1
 */