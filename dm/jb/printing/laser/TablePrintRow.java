package dm.jb.printing.laser;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

public class TablePrintRow
{
  public ArrayList<TablePrintCell> _mCells = new ArrayList();
  public int tableRowHeight = 0;
  
  public TablePrintRow(int paramInt)
  {
    this.tableRowHeight = paramInt;
  }
  
  public void addCell(TablePrintCell paramTablePrintCell)
  {
    this._mCells.add(paramTablePrintCell);
  }
  
  public Point printRow(Graphics2D paramGraphics2D, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    Iterator localIterator = this._mCells.iterator();
    int i = 0;
    Point localPoint = new Point(paramInt1, paramInt2);
    while (localIterator.hasNext())
    {
      TablePrintCell localTablePrintCell = (TablePrintCell)localIterator.next();
      localPoint = localTablePrintCell.print(paramGraphics2D, null, i == 0, localPoint.x, paramInt2, this.tableRowHeight, paramBoolean);
      i++;
    }
    return new Point(0, localPoint.y + this.tableRowHeight);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.printing.laser.TablePrintRow
 * JD-Core Version:    0.7.0.1
 */