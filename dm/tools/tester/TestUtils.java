package dm.tools.tester;

import java.awt.Component;
import java.awt.Container;
import java.io.PrintStream;
import javax.swing.JButton;
import javax.swing.JComponent;

public class TestUtils
{
  public static JComponent findComponentByName(String paramString, Container paramContainer, int paramInt)
  {
    JComponent localJComponent = null;
    if (paramInt == -1) {
      localJComponent = findComponentByNameWithoutLevel(paramString, paramContainer);
    } else {
      localJComponent = findComponentByNameWithLevel(paramString, paramContainer, paramInt, 0);
    }
    return localJComponent;
  }
  
  private static JComponent findComponentByNameWithLevel(String paramString, Container paramContainer, int paramInt1, int paramInt2)
  {
    JComponent localJComponent = null;
    if (paramInt1 == paramInt2) {
      return null;
    }
    if (!(paramContainer instanceof Container)) {
      return null;
    }
    if ((paramString == null) || (paramString.length() == 0)) {
      return null;
    }
    Component[] arrayOfComponent = paramContainer.getComponents();
    Object localObject;
    for (int i = 0; i < arrayOfComponent.length; i++) {
      if ((arrayOfComponent[i] instanceof JComponent))
      {
        localObject = arrayOfComponent[i].getName();
        if ((localObject != null) && (((String)localObject).equals(paramString))) {
          return (JComponent)arrayOfComponent[i];
        }
      }
    }
    for (i = 0; i < arrayOfComponent.length; i++) {
      if ((arrayOfComponent[i] instanceof Container))
      {
        localObject = findComponentByNameWithLevel(paramString, (Container)arrayOfComponent[i], paramInt1, paramInt2 + 1);
        if (localObject != null) {
          return localObject;
        }
      }
    }
    return localJComponent;
  }
  
  private static JComponent findComponentByNameWithoutLevel(String paramString, Container paramContainer)
  {
    JComponent localJComponent = null;
    if (!(paramContainer instanceof Container)) {
      return null;
    }
    if ((paramString == null) || (paramString.length() == 0)) {
      return null;
    }
    Component[] arrayOfComponent = paramContainer.getComponents();
    Object localObject;
    for (int i = 0; i < arrayOfComponent.length; i++) {
      if ((arrayOfComponent[i] instanceof JComponent))
      {
        localObject = arrayOfComponent[i].getName();
        if ((localObject != null) && (((String)localObject).equals(paramString))) {
          return (JComponent)arrayOfComponent[i];
        }
      }
    }
    for (i = 0; i < arrayOfComponent.length; i++) {
      if ((arrayOfComponent[i] instanceof Container))
      {
        localObject = findComponentByNameWithoutLevel(paramString, (Container)arrayOfComponent[i]);
        if (localObject != null) {
          return localObject;
        }
      }
    }
    return localJComponent;
  }
  
  public static void threadJoinExp(Thread paramThread)
  {
    try
    {
      paramThread.join();
    }
    catch (Exception localException)
    {
      System.err.println("Ecxception " + localException);
    }
  }
  
  public static Thread doButtonClickInThread(JButton paramJButton)
  {
    Thread localThread = new Thread(new Runnable()
    {
      public void run()
      {
        try
        {
          this.val$btnf.doClick();
        }
        catch (Exception localException) {}
      }
    }, "ModalThread");
    localThread.start();
    return localThread;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.tester.TestUtils
 * JD-Core Version:    0.7.0.1
 */