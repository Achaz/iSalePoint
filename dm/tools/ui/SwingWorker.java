package dm.tools.ui;

import javax.swing.SwingUtilities;

public abstract class SwingWorker
{
  private Object value;
  private ThreadVar threadVar;
  
  protected synchronized Object getValue()
  {
    return this.value;
  }
  
  private synchronized void setValue(Object paramObject)
  {
    this.value = paramObject;
  }
  
  public abstract Object construct();
  
  public void finished() {}
  
  public void interrupt()
  {
    Thread localThread = this.threadVar.get();
    if (localThread != null) {
      localThread.interrupt();
    }
    this.threadVar.clear();
  }
  
  public Object get()
  {
    for (;;)
    {
      Thread localThread = this.threadVar.get();
      if (localThread == null) {
        return getValue();
      }
      try
      {
        localThread.join();
      }
      catch (InterruptedException localInterruptedException)
      {
        Thread.currentThread().interrupt();
        return null;
      }
    }
  }
  
  public SwingWorker()
  {
    final Runnable local1 = new Runnable()
    {
      public void run()
      {
        SwingWorker.this.finished();
      }
    };
    Runnable local2 = new Runnable()
    {
      public void run()
      {
        try
        {
          SwingWorker.this.setValue(SwingWorker.this.construct());
        }
        finally
        {
          SwingWorker.this.threadVar.clear();
        }
        SwingUtilities.invokeLater(local1);
      }
    };
    Thread localThread = new Thread(local2);
    this.threadVar = new ThreadVar(localThread);
  }
  
  public void start()
  {
    Thread localThread = this.threadVar.get();
    if (localThread != null) {
      localThread.start();
    }
  }
  
  private static class ThreadVar
  {
    private Thread thread;
    
    ThreadVar(Thread paramThread)
    {
      this.thread = paramThread;
    }
    
    synchronized Thread get()
    {
      return this.thread;
    }
    
    synchronized void clear()
    {
      this.thread = null;
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.ui.SwingWorker
 * JD-Core Version:    0.7.0.1
 */