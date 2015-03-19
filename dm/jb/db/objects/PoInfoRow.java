package dm.jb.db.objects;

import dm.jb.db.gen.PoInfoBaseRow;
import dm.tools.db.DBException;
import java.util.ArrayList;
import java.util.Iterator;

public class PoInfoRow
  extends PoInfoBaseRow
{
  private ArrayList<PoEntryRow> _mEntries = null;
  
  PoInfoRow(int paramInt, PoInfoTableDef paramPoInfoTableDef)
  {
    super(paramInt, paramPoInfoTableDef);
  }
  
  public void addOrUpdateEntryRow(PoEntryRow paramPoEntryRow)
  {
    if (this._mEntries == null)
    {
      this._mEntries = new ArrayList();
      paramPoEntryRow.markedForDelete = false;
      this._mEntries.add(paramPoEntryRow);
      return;
    }
    Iterator localIterator = this._mEntries.iterator();
    while (localIterator.hasNext())
    {
      PoEntryRow localPoEntryRow = (PoEntryRow)localIterator.next();
      if (localPoEntryRow.getProductId() == paramPoEntryRow.getProductId())
      {
        localPoEntryRow.setPriceExpected(paramPoEntryRow.getPriceExpected());
        localPoEntryRow.setPricePaid(paramPoEntryRow.getPricePaid());
        localPoEntryRow.setQuantity(paramPoEntryRow.getQuantity());
        localPoEntryRow.setQuantityRecieved(paramPoEntryRow.getQuantityRecieved());
        localPoEntryRow.markedForDelete = false;
        return;
      }
    }
    paramPoEntryRow.markedForDelete = false;
    this._mEntries.add(paramPoEntryRow);
  }
  
  public void create()
    throws DBException
  {
    super.create();
    int i = getPoIndex();
    Iterator localIterator = this._mEntries.iterator();
    while (localIterator.hasNext())
    {
      PoEntryRow localPoEntryRow = (PoEntryRow)localIterator.next();
      localPoEntryRow.setPoId(i);
      localPoEntryRow.create();
    }
  }
  
  public void update(boolean paramBoolean)
    throws DBException
  {
    setPoEntryCount(this._mEntries.size());
    super.update(paramBoolean);
    int i = getPoIndex();
    Iterator localIterator = this._mEntries.iterator();
    PoEntryRow localPoEntryRow;
    while (localIterator.hasNext())
    {
      localPoEntryRow = (PoEntryRow)localIterator.next();
      if (localPoEntryRow.markedForDelete) {
        localPoEntryRow.delete();
      }
    }
    localIterator = this._mEntries.iterator();
    while (localIterator.hasNext())
    {
      localPoEntryRow = (PoEntryRow)localIterator.next();
      if (localPoEntryRow.getPoId() != -1)
      {
        localPoEntryRow.update(paramBoolean);
      }
      else
      {
        localPoEntryRow.setPoId(i);
        localPoEntryRow.create();
      }
    }
  }
  
  public void delete()
    throws DBException
  {
    Iterator localIterator = this._mEntries.iterator();
    while (localIterator.hasNext())
    {
      PoEntryRow localPoEntryRow = (PoEntryRow)localIterator.next();
      localPoEntryRow.delete();
    }
    super.delete();
  }
  
  void setEntries(ArrayList<PoEntryRow> paramArrayList)
  {
    if (this._mEntries != null)
    {
      this._mEntries.clear();
      this._mEntries = null;
    }
    this._mEntries = paramArrayList;
  }
  
  public ArrayList<PoEntryRow> getEntries()
  {
    return this._mEntries;
  }
  
  public void prepareForUpdate()
  {
    Iterator localIterator = this._mEntries.iterator();
    while (localIterator.hasNext())
    {
      PoEntryRow localPoEntryRow = (PoEntryRow)localIterator.next();
      localPoEntryRow.markedForDelete = true;
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.PoInfoRow
 * JD-Core Version:    0.7.0.1
 */