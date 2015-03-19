package dm.tools.ui.components;

import dm.tools.db.DBConnection;
import dm.tools.db.DBException;
import dm.tools.db.DBRow;
import dm.tools.db.Db;
import dm.tools.dbui.ActionObject;
import dm.tools.dbui.DBUIActionListener;
import dm.tools.dbui.DBUIContainer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import org.jdesktop.swingx.JXButton;

public class JBActionButton
  extends JXButton
  implements ActionObject, ActionListener
{
  public static final byte JBACTION_UPDATE = 1;
  public static final byte JBACTION_CREATE = 2;
  public static final byte JBACTION_DELETE = 3;
  public static final byte JBACTION_UPDATE_CREATE = 4;
  private DBRow _mObjectInstance = null;
  private byte _mAction = 1;
  private DBUIContainer _mContainer = null;
  private ArrayList<DBUIActionListener> _mActionListeners = new ArrayList();
  
  public JBActionButton(String paramString, byte paramByte, DBRow paramDBRow)
  {
    super(paramString);
    this._mAction = paramByte;
    this._mObjectInstance = paramDBRow;
    addActionListener(this);
  }
  
  public void setInstance(DBRow paramDBRow)
  {
    if ((paramDBRow == null) && ((this._mAction == 1) || (this._mAction == 3))) {
      setEnabled(false);
    } else {
      setEnabled(true);
    }
    this._mObjectInstance = paramDBRow;
  }
  
  public void actionPerformed(ActionEvent paramActionEvent)
  {
    int i = 0;
    if (this._mContainer == null) {
      return;
    }
    if (this._mAction == 2) {
      this._mObjectInstance = this._mContainer.createRow();
    }
    if ((this._mAction == 4) && (this._mObjectInstance == null))
    {
      i = 1;
      this._mObjectInstance = this._mContainer.createRow();
    }
    if (this._mObjectInstance == null) {
      this._mObjectInstance = this._mContainer.getCurrentInstance();
    }
    if (this._mObjectInstance == null) {
      return;
    }
    if ((this._mAction != 3) && (!this._mContainer.validateValues(this._mAction))) {
      return;
    }
    this._mContainer.setValueToInstance(this._mObjectInstance);
    if (this._mActionListeners.size() > 0)
    {
      int j = this._mActionListeners.size();
      for (int k = 0; k < j; k++) {
        if (!((DBUIActionListener)this._mActionListeners.get(k)).beforeAction(this)) {
          return;
        }
      }
    }
    DBConnection localDBConnection = null;
    int n;
    try
    {
      localDBConnection = Db.getConnection();
      localDBConnection.openTrans();
      switch (this._mAction)
      {
      case 1: 
        this._mObjectInstance.update(true);
        break;
      case 2: 
        this._mObjectInstance.create();
        setInstance(this._mObjectInstance);
        break;
      case 3: 
        this._mObjectInstance.delete();
        break;
      case 4: 
        if (i != 0)
        {
          this._mObjectInstance.create();
          setInstance(this._mObjectInstance);
        }
        else
        {
          this._mObjectInstance.update(true);
        }
        break;
      }
      localDBConnection.endTrans();
    }
    catch (DBException localDBException)
    {
      localDBConnection.rollbackNoExp();
      if (this._mActionListeners.size() > 0)
      {
        n = this._mActionListeners.size();
        for (int i1 = 0; i1 < n; i1++) {
          ((DBUIActionListener)this._mActionListeners.get(i1)).actionFailed(localDBException);
        }
      }
      return;
    }
    if (this._mActionListeners.size() > 0)
    {
      int m = this._mActionListeners.size();
      for (n = 0; n < m; n++) {
        ((DBUIActionListener)this._mActionListeners.get(n)).afterAction(this);
      }
    }
  }
  
  public void setContainer(DBUIContainer paramDBUIContainer)
  {
    this._mContainer = paramDBUIContainer;
  }
  
  public byte getActionCode()
  {
    return this._mAction;
  }
  
  public void addDBUIActionListener(DBUIActionListener paramDBUIActionListener)
  {
    this._mActionListeners.add(paramDBUIActionListener);
  }
  
  public void removeDBUIActionListener(DBUIActionListener paramDBUIActionListener)
  {
    this._mActionListeners.remove(paramDBUIActionListener);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.ui.components.JBActionButton
 * JD-Core Version:    0.7.0.1
 */