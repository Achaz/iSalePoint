package dm.jb.ext;

import dm.tools.utils.Config;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.SwingUtilities;

public class RFIDReader
{
  public static RFIDReader _mInstance = new RFIDReader();
  private int _mStartCode;
  private int _mEndCode;
  private int _mMaxCodeLen;
  private String _mPortName;
  private byte[] dataBuffer;
  private InputStream _mInStream = null;
  private ArrayList<RFIDReadListener> _mListenerList = null;
  
  private RFIDReader()
  {
    initParams();
    this._mListenerList = new ArrayList();
  }
  
  private void initParams()
  {
    this._mStartCode = 2;
    this._mEndCode = 13;
    this._mMaxCodeLen = 10;
    this._mPortName = "COM5";
    this.dataBuffer = new byte[this._mMaxCodeLen + 1];
  }
  
  public void initDevice()
    throws RFIDException
  {
    String str = Config.INSTANCE.getAttrib("JB_CONFIG.OTHERS.RFID_PORT");
    if ((str == null) || (str.trim().length() == 0)) {
      str = "COM7";
    }
    try
    {
      CommPortIdentifier localCommPortIdentifier = CommPortIdentifier.getPortIdentifier(str);
      if (localCommPortIdentifier.isCurrentlyOwned()) {
        throw new RFIDException("Port already in use by another application.", "Open the port", null);
      }
      CommPort localCommPort = localCommPortIdentifier.open(localCommPortIdentifier.getClass().getName(), 2000);
      if (!(localCommPort instanceof SerialPort)) {
        throw new RFIDException("Not a valid serial port.", "Open the port", null);
      }
      SerialPort localSerialPort = (SerialPort)localCommPort;
      localSerialPort.setSerialPortParams(9600, 8, 1, 0);
      localSerialPort.setDTR(true);
      localSerialPort.setRTS(true);
      this._mInStream = localSerialPort.getInputStream();
      if (this._mInStream == null) {
        throw new RFIDException("I/O error.", "Open the port", null);
      }
    }
    catch (NoSuchPortException localNoSuchPortException)
    {
      throw new RFIDException("Port cannot be opened.", "Open the port", localNoSuchPortException);
    }
    catch (PortInUseException localPortInUseException)
    {
      throw new RFIDException("Port already in use by another application.", "Open the port", localPortInUseException);
    }
    catch (UnsupportedCommOperationException localUnsupportedCommOperationException)
    {
      throw new RFIDException("Port cannot be opened.", "Open the port", localUnsupportedCommOperationException);
    }
    catch (IOException localIOException)
    {
      throw new RFIDException("I/O error.", "Open the port", localIOException);
    }
  }
  
  public void startThread()
  {
    Thread local1 = new Thread()
    {
      public void run()
      {
        try
        {
          RFIDReader.this.startProcess();
        }
        catch (RFIDException localRFIDException)
        {
          localRFIDException.printStackTrace();
        }
      }
    };
    local1.start();
  }
  
  private void startProcess()
    throws RFIDException
  {
    int i = 0;
    byte[] arrayOfByte = new byte[1024];
    int j = 0;
    int k = 0;
    try
    {
      while ((i = this._mInStream.read(arrayOfByte)) > -1) {
        if (i > 0)
        {
          int m;
          if (j == 0)
          {
            k = 0;
            if (arrayOfByte[0] == this._mStartCode)
            {
              j = 1;
              for (m = 1; m < i; m++)
              {
                this.dataBuffer[k] = arrayOfByte[m];
                k++;
              }
            }
          }
          else
          {
            for (m = 0; (m < i) && (arrayOfByte[m] != this._mEndCode); m++)
            {
              this.dataBuffer[k] = arrayOfByte[m];
              k++;
            }
            if (arrayOfByte[m] == this._mEndCode)
            {
              this.dataBuffer[k] = 0;
              j = 0;
              final String str = new String(this.dataBuffer, 0, k);
              SwingUtilities.invokeLater(new Runnable()
              {
                public void run()
                {
                  RFIDReader.this.notifyListeners(str);
                }
              });
            }
          }
        }
      }
    }
    catch (IOException localIOException)
    {
      throw new RFIDException("I/O error.", "Open the port", localIOException);
    }
  }
  
  public void addReadListener(RFIDReadListener paramRFIDReadListener)
  {
    this._mListenerList.add(paramRFIDReadListener);
  }
  
  public void removeReadListener(RFIDReadListener paramRFIDReadListener)
  {
    this._mListenerList.remove(paramRFIDReadListener);
  }
  
  private void notifyListeners(String paramString)
  {
    Iterator localIterator = this._mListenerList.iterator();
    while (localIterator.hasNext())
    {
      RFIDReadListener localRFIDReadListener = (RFIDReadListener)localIterator.next();
      localRFIDReadListener.dataRead(paramString);
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ext.RFIDReader
 * JD-Core Version:    0.7.0.1
 */