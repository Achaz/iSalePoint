package dm.tools.dbui;

import dm.tools.db.DBAttribute;
import dm.tools.db.DBObjectDef;
import dm.tools.db.DBRow;
import dm.tools.ui.UICommon;
import dm.tools.ui.components.Validator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JDialog;
import javax.swing.JFrame;

public class DBUIContainerImpl
  implements DBUIContainer
{
  private ComponentFactory _mCompFactory = null;
  private DBObjectDef _mTableDef = null;
  private ArrayList<ActionObject> _mActionObjects = new ArrayList();
  private ArrayList<DBUIObject> _mUIObjects = new ArrayList();
  private ArrayList<DBUIObject> _mReadOnlyUIObjects = new ArrayList();
  private ArrayList<Validator> _mValidators = null;
  private HashMap<String, StaticListComponent> _mStaticListItems = new HashMap();
  private DBRow _mCurrentInstance = null;
  private Object _mParentInstance = null;
  private DBRow _mNewRowCreated = null;
  private int _mCurrentAction = -1;
  
  public DBUIContainerImpl(DBObjectDef paramDBObjectDef, Object paramObject)
  {
    this._mTableDef = paramDBObjectDef;
    this._mParentInstance = paramObject;
    this._mCompFactory = ComponentFactory.getInstance();
  }
  
  public Object createComponentForAttribute(String paramString1, String paramString2)
  {
    return createComponentForAttribute(paramString1, paramString2, false);
  }
  
  public Object createComponentForAttribute(String paramString1, String paramString2, boolean paramBoolean)
  {
    return createComponentForAttribute(paramString1, paramString2, (short)-1, paramBoolean);
  }
  
  public DBUIObject createComponentForAttribute(String paramString1, String paramString2, short paramShort, boolean paramBoolean)
  {
    DBAttribute localDBAttribute = this._mTableDef.getAttributeDefByName(paramString1);
    if (localDBAttribute == null) {
      return null;
    }
    DBUIObject localDBUIObject = this._mCompFactory.getComponent(localDBAttribute, paramString2, paramShort);
    if (paramBoolean) {
      this._mReadOnlyUIObjects.add(localDBUIObject);
    } else {
      this._mUIObjects.add(localDBUIObject);
    }
    return localDBUIObject;
  }
  
  public DBUIObject createComponentForTable(DBObjectDef paramDBObjectDef, String paramString1, short paramShort, String paramString2, String paramString3, boolean paramBoolean)
  {
    DBUIObject localDBUIObject = this._mCompFactory.getComponent(paramDBObjectDef, paramString1, paramShort, paramString2, paramString3);
    if (paramBoolean) {
      this._mReadOnlyUIObjects.add(localDBUIObject);
    } else {
      this._mUIObjects.add(localDBUIObject);
    }
    return localDBUIObject;
  }
  
  public ActionObject createActionObject(String paramString1, String paramString2, DBRow paramDBRow)
  {
    ActionObject localActionObject = this._mCompFactory.getActionComponent(paramDBRow, paramString2, paramString1);
    this._mActionObjects.add(localActionObject);
    localActionObject.setContainer(this);
    return localActionObject;
  }
  
  public void addStaticListComponent(StaticListComponent paramStaticListComponent, String paramString)
  {
    this._mStaticListItems.put(paramString, paramStaticListComponent);
  }
  
  public void addValidator(Validator paramValidator)
  {
    if (this._mValidators == null) {
      this._mValidators = new ArrayList();
    }
    this._mValidators.add(paramValidator);
  }
  
  public DBRow getCurrentInstance()
  {
    if (this._mCurrentAction == 2) {
      return this._mNewRowCreated;
    }
    return this._mCurrentInstance;
  }
  
  public void setCurrentInstance(DBRow paramDBRow)
  {
    setCurrentInstance(paramDBRow, true);
  }
  
  public void setCurrentInstance(DBRow paramDBRow, boolean paramBoolean)
  {
    this._mCurrentInstance = paramDBRow;
    if (!paramBoolean) {
      return;
    }
    Iterator localIterator1 = this._mUIObjects.iterator();
    while (localIterator1.hasNext()) {
      ((DBUIObject)localIterator1.next()).setInstance(this._mCurrentInstance);
    }
    localIterator1 = this._mReadOnlyUIObjects.iterator();
    while (localIterator1.hasNext()) {
      ((DBUIObject)localIterator1.next()).setInstance(this._mCurrentInstance);
    }
    Iterator localIterator2 = this._mActionObjects.iterator();
    while (localIterator2.hasNext()) {
      ((ActionObject)localIterator2.next()).setInstance(paramDBRow);
    }
    Iterator localIterator3 = this._mStaticListItems.values().iterator();
    while (localIterator3.hasNext()) {
      ((StaticListComponent)localIterator3.next()).setValueFromRow(paramDBRow);
    }
  }
  
  public boolean validateValues(int paramInt)
  {
    try
    {
      this._mCurrentAction = paramInt;
      validateInternal();
    }
    catch (ValidationException localValidationException)
    {
      processValidationException(localValidationException);
      return false;
    }
    return true;
  }
  
  private void validateInternal()
    throws ValidationException
  {
    int i = this._mUIObjects.size();
    Object localObject;
    for (int j = 0; j < i; j++)
    {
      localObject = (DBUIObject)this._mUIObjects.get(j);
      ((DBUIObject)localObject).validateValue();
    }
    if (this._mValidators != null)
    {
      localIterator = this._mValidators.iterator();
      while (localIterator.hasNext()) {
        ((Validator)localIterator.next()).validateValue();
      }
    }
    Iterator localIterator = this._mStaticListItems.values().iterator();
    while (localIterator.hasNext())
    {
      localObject = (StaticListComponent)localIterator.next();
      ((StaticListComponent)localObject).validateValue();
    }
  }
  
  public void setValueToInstance(DBRow paramDBRow)
  {
    Iterator localIterator1 = this._mUIObjects.iterator();
    while (localIterator1.hasNext()) {
      ((DBUIObject)localIterator1.next()).setValueToInstance(paramDBRow);
    }
    Iterator localIterator2 = this._mStaticListItems.values().iterator();
    while (localIterator2.hasNext()) {
      ((StaticListComponent)localIterator2.next()).setValueToRow(paramDBRow);
    }
  }
  
  public DBRow createRow()
  {
    this._mNewRowCreated = this._mTableDef.getNewRow();
    return this._mNewRowCreated;
  }
  
  public void resetAttributes()
  {
    resetAttributes(false);
  }
  
  public void resetAttributes(boolean paramBoolean)
  {
    setCurrentInstance(null, false);
    Iterator localIterator = this._mUIObjects.iterator();
    Object localObject;
    while (localIterator.hasNext())
    {
      localObject = (DBUIObject)localIterator.next();
      if ((((DBUIObject)localObject).isResetAllowed()) || (paramBoolean)) {
        ((DBUIObject)localObject).resetValue();
      }
    }
    localIterator = this._mReadOnlyUIObjects.iterator();
    while (localIterator.hasNext())
    {
      localObject = (DBUIObject)localIterator.next();
      if ((((DBUIObject)localObject).isResetAllowed()) || (paramBoolean)) {
        ((DBUIObject)localObject).resetValue();
      }
    }
    localIterator = this._mActionObjects.iterator();
    while (localIterator.hasNext())
    {
      localObject = (ActionObject)localIterator.next();
      ((ActionObject)localObject).setInstance(null);
    }
  }
  
  public void setInstanceForActions(DBRow paramDBRow)
  {
    Iterator localIterator = this._mActionObjects.iterator();
    while (localIterator.hasNext()) {
      ((ActionObject)localIterator.next()).setInstance(paramDBRow);
    }
  }
  
  public void processValidationException(ValidationException paramValidationException)
  {
    if (this._mParentInstance == null) {
      UICommon.showError(paramValidationException.getMessage(), "Error", (JDialog)null);
    }
    if ((this._mParentInstance instanceof JDialog)) {
      UICommon.showError(paramValidationException.getMessage(), "Error", (JDialog)this._mParentInstance);
    } else if ((this._mParentInstance instanceof JFrame)) {
      UICommon.showError(paramValidationException.getMessage(), "Error", (JFrame)this._mParentInstance);
    } else {
      UICommon.showError(paramValidationException.getMessage(), "Error", (JDialog)null);
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.dbui.DBUIContainerImpl
 * JD-Core Version:    0.7.0.1
 */