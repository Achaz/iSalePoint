package dm.jb.printing.laser;

import dm.jb.JeException;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;

public class TablePrint
{
  public static int PRINT_IMMEDIATE = 1;
  public static int PRINT_LATER = 2;
  private int cols = 0;
  private int rows = -1;
  private int _mPrintMode = PRINT_IMMEDIATE;
  private ArrayList<TablePrintColumn> _mColumns = new ArrayList();
  private ArrayList<TablePrintRow> _mRows = null;
  private int currentPage = -1;
  private boolean _mColumnPrinted = false;
  private int pageHeight = 0;
  private int curStartYPos = 0;
  private int _mMaxColumnHeight = -1;
  private int _mCurrentRowIndex = 0;
  private int _mRowCount = 0;
  private String _mMultiPageTitle = "";
  private int _mCurrentYPos = 0;
  
  public TablePrint(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, String paramString)
  {
    this.cols = paramInt1;
    this.rows = paramInt2;
    this._mPrintMode = paramInt3;
    if (paramInt3 == PRINT_LATER) {
      this._mRows = new ArrayList();
    }
    this.curStartYPos = paramInt4;
    this.pageHeight = paramInt5;
    this._mMultiPageTitle = paramString;
  }
  
  public int getTotalColumns()
  {
    return this.cols;
  }
  
  public int getTotalRows()
  {
    return this.rows;
  }
  
  public void setStartPos(int paramInt)
  {
    this.curStartYPos = paramInt;
  }
  
  public void addColumn(TablePrintColumn paramTablePrintColumn)
  {
    this._mColumns.add(paramTablePrintColumn);
    if (this._mMaxColumnHeight < paramTablePrintColumn.titleHeight) {
      this._mMaxColumnHeight = paramTablePrintColumn.titleHeight;
    }
  }
  
  public void addRow(TablePrintRow paramTablePrintRow)
  {
    if (this._mPrintMode == PRINT_LATER)
    {
      this._mRows.add(paramTablePrintRow);
      this._mRowCount += 1;
      return;
    }
  }
  
  public boolean printNextPage(Graphics paramGraphics, int paramInt, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
    throws JeException
  {
    int i = 0;
    int j = 0;
    Iterator localIterator = this._mColumns.iterator();
    Point localPoint1 = new Point(0, paramInt);
    this.currentPage += 1;
    paramGraphics.setFont(new Font(paramGraphics.getFont().getName(), 1, 8));
    int k;
    if (this.currentPage > 0)
    {
      k = this.currentPage + 1;
      ((Graphics2D)paramGraphics).drawString("Page : " + k, 0, paramInt + 15);
      int m = getWidth();
      FontRenderContext localFontRenderContext = ((Graphics2D)paramGraphics).getFontRenderContext();
      int n = (int)paramGraphics.getFont().getStringBounds(this._mMultiPageTitle, localFontRenderContext).getWidth();
      ((Graphics2D)paramGraphics).drawString(this._mMultiPageTitle, m - n, paramInt + 15);
      paramInt += 20;
    }
    Object localObject;
    if (!this._mColumnPrinted)
    {
      k = this.pageHeight - this.curStartYPos;
      if (k < 50 + this._mMaxColumnHeight) {
        return false;
      }
      if (paramBoolean3) {
        while (localIterator.hasNext())
        {
          localObject = (TablePrintColumn)localIterator.next();
          localPoint1 = ((TablePrintColumn)localObject).printTitle((Graphics2D)paramGraphics, null, -1, i == 0, localPoint1.x, paramInt, paramBoolean1, paramBoolean2);
          i++;
        }
      }
      this._mColumnPrinted = false;
      j = localPoint1.y;
    }
    localPoint1.y = j;
    Point localPoint2 = new Point(0, localPoint1.y);
    while (this._mCurrentRowIndex < this._mRowCount)
    {
      localObject = (TablePrintRow)this._mRows.get(this._mCurrentRowIndex);
      localPoint2 = ((TablePrintRow)localObject).printRow((Graphics2D)paramGraphics, 0, localPoint2.y, paramBoolean2);
      j = localPoint2.y;
      this._mCurrentYPos = j;
      this._mCurrentRowIndex += 1;
      if (this.pageHeight - j < ((TablePrintRow)localObject).tableRowHeight + 2) {
        break;
      }
    }
    if (paramBoolean1) {
      paramGraphics.drawLine(0, j, getWidth(), j);
    }
    boolean bool = this._mCurrentRowIndex >= this._mRowCount;
    return bool;
  }
  
  public int getWidth()
  {
    int i = 0;
    Iterator localIterator = this._mColumns.iterator();
    while (localIterator.hasNext())
    {
      TablePrintColumn localTablePrintColumn = (TablePrintColumn)localIterator.next();
      i += localTablePrintColumn.width;
    }
    return i;
  }
  
  public int getCurrentYPos()
  {
    return this._mCurrentYPos;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.printing.laser.TablePrint
 * JD-Core Version:    0.7.0.1
 */