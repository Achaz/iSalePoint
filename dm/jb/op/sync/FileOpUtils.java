package dm.jb.op.sync;

import dm.jb.Version;
import dm.tools.db.DBException;
import dm.tools.ui.components.ProgressWindowWithStatus;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class FileOpUtils
{
  public static final short ISP_FILE_TYPE_WH_TO_STORE = 1;
  public static final short ISP_FILE_TYPE_FILE_SYNC = 2;
  private static byte[] ISP_HEADER = "ISPF".getBytes();
  public static FileOpUtils INSTANCE = new FileOpUtils();
  ProgressWindowWithStatus progressWindow = null;
  private static final short TOTAL_HEADER_LENGTH = 18;
  
  public static void writeHeaderInfo(FileOutputStream paramFileOutputStream, short paramShort)
    throws IOException
  {
    paramFileOutputStream.write(ISP_HEADER);
    paramFileOutputStream.write(intToBytes(Integer.valueOf(Version.getVerisonAsInt()).intValue()));
    paramFileOutputStream.write(shortToBytes(paramShort));
    long l = new Date().getTime();
    paramFileOutputStream.write(longToBytes(l));
  }
  
  public FileOutputStream createFileStream(String paramString, short paramShort)
    throws IOException
  {
    FileOutputStream localFileOutputStream = new FileOutputStream(paramString);
    writeHeaderInfo(localFileOutputStream, paramShort);
    return localFileOutputStream;
  }
  
  public String getDataFileName(String paramString)
  {
    String str = System.getProperty("user.home") + File.separator + paramString + ".isp";
    return str;
  }
  
  public boolean processDataFile(String paramString)
    throws IOException, InvalidFileException, DBException
  {
    FileInputStream localFileInputStream = null;
    try
    {
      localFileInputStream = new FileInputStream(paramString);
      if (localFileInputStream == null) {
        return false;
      }
      byte[] arrayOfByte1 = new byte[4];
      localFileInputStream.read(arrayOfByte1);
      for (int i = 0; i < arrayOfByte1.length; i++) {
        if (arrayOfByte1[i] != ISP_HEADER[i]) {
          throw new InvalidFileException("Missing header");
        }
      }
      localFileInputStream.read(arrayOfByte1);
      i = bytesToInt(arrayOfByte1);
      if (!Version.isDataVersionCompatible(i)) {
        throw new InvalidFileException("Version error");
      }
      byte[] arrayOfByte2 = new byte[2];
      localFileInputStream.read(arrayOfByte2);
      int j = bytesToShort(arrayOfByte2);
      byte[] arrayOfByte3 = new byte[8];
      localFileInputStream.read(arrayOfByte3);
      long l = bytesToLong(arrayOfByte3);
      switch (j)
      {
      case 1: 
        WhToStoreFile.INSTANCE.readData(localFileInputStream, new Date(l));
        localFileInputStream.close();
        break;
      case 2: 
        startFileSync(localFileInputStream, new Date(l), paramString);
        localFileInputStream.close();
        break;
      default: 
        localFileInputStream.close();
        throw new InvalidFileException("Invalid type " + j);
      }
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      try
      {
        if (localFileInputStream != null) {
          localFileInputStream.close();
        }
      }
      catch (Exception localException1)
      {
        localException1.printStackTrace();
      }
      return false;
    }
    catch (IOException localIOException)
    {
      try
      {
        if (localFileInputStream != null) {
          localFileInputStream.close();
        }
      }
      catch (Exception localException2)
      {
        localException2.printStackTrace();
      }
      throw localIOException;
    }
    catch (InvalidFileException localInvalidFileException)
    {
      try
      {
        if (localFileInputStream != null) {
          localFileInputStream.close();
        }
      }
      catch (Exception localException3)
      {
        localException3.printStackTrace();
      }
      throw localInvalidFileException;
    }
    catch (DBException localDBException)
    {
      try
      {
        if (localFileInputStream != null) {
          localFileInputStream.close();
        }
      }
      catch (Exception localException4)
      {
        localException4.printStackTrace();
      }
      throw localDBException;
    }
    return true;
  }
  
  public static void skipFileOpHeader(FileInputStream paramFileInputStream)
    throws IOException
  {
    paramFileInputStream.skip(18L);
  }
  
  private void startFileSync(FileInputStream paramFileInputStream, Date paramDate, String paramString)
    throws IOException, InvalidFileException, DBException
  {
    FileSync.readData(paramFileInputStream, paramDate, this.progressWindow, 18, paramString);
  }
  
  public static int readInt(FileInputStream paramFileInputStream)
    throws IOException
  {
    byte[] arrayOfByte = new byte[4];
    int i = paramFileInputStream.read(arrayOfByte);
    if (i < 4) {
      throw new IOException("Unexpected End of File reached.");
    }
    return bytesToInt(arrayOfByte);
  }
  
  public static long readLong(FileInputStream paramFileInputStream)
    throws IOException
  {
    byte[] arrayOfByte = new byte[8];
    int i = paramFileInputStream.read(arrayOfByte);
    if (i < 8) {
      throw new IOException("Unexpected End of File reached.");
    }
    return bytesToLong(arrayOfByte);
  }
  
  public static short readShort(FileInputStream paramFileInputStream)
    throws IOException
  {
    byte[] arrayOfByte = new byte[2];
    int i = paramFileInputStream.read(arrayOfByte);
    if (i < 2) {
      throw new IOException("Unexpected End of File reached.");
    }
    return bytesToShort(arrayOfByte);
  }
  
  public static double readDouble(FileInputStream paramFileInputStream)
    throws IOException
  {
    byte[] arrayOfByte = new byte[8];
    int i = paramFileInputStream.read(arrayOfByte);
    if (i < 8) {
      throw new IOException("Unexpected End of File reached.");
    }
    return bytesToDouble(arrayOfByte);
  }
  
  public static Date readDate(FileInputStream paramFileInputStream)
    throws IOException
  {
    byte[] arrayOfByte = new byte[8];
    int i = paramFileInputStream.read(arrayOfByte);
    if (i < 8) {
      throw new IOException("Unexpected End of File reached.");
    }
    long l = bytesToLong(arrayOfByte);
    if (l == -1L) {
      return null;
    }
    return new Date(l);
  }
  
  public static void writeString(FileOutputStream paramFileOutputStream, String paramString)
    throws IOException
  {
    byte[] arrayOfByte = paramString.getBytes();
    short s = (short)arrayOfByte.length;
    paramFileOutputStream.write(shortToBytes(s));
    paramFileOutputStream.write(arrayOfByte);
  }
  
  public static String readString(FileInputStream paramFileInputStream)
    throws IOException
  {
    int i = readShort(paramFileInputStream);
    byte[] arrayOfByte = new byte[i];
    int j = paramFileInputStream.read(arrayOfByte);
    if (j < i) {
      throw new IOException("Unexpected End of File reached.");
    }
    String str = new String(arrayOfByte);
    return str;
  }
  
  public static byte[] intToBytes(int paramInt)
  {
    byte[] arrayOfByte = new byte[4];
    for (int i = 0; i < 4; i++) {
      arrayOfByte[(3 - i)] = ((byte)(paramInt >>> i * 8));
    }
    return arrayOfByte;
  }
  
  public static int bytesToInt(byte[] paramArrayOfByte)
  {
    int i = 0;
    for (int j = 0; j < 4; j++)
    {
      i <<= 8;
      i = (int)(i ^ paramArrayOfByte[j] & 0xFF);
    }
    return i;
  }
  
  public static byte[] longToBytes(long paramLong)
  {
    byte[] arrayOfByte = new byte[8];
    for (int i = 0; i < 8; i++) {
      arrayOfByte[(7 - i)] = ((byte)(int)(paramLong >>> i * 8));
    }
    return arrayOfByte;
  }
  
  public static long bytesToLong(byte[] paramArrayOfByte)
  {
    long l = 0L;
    for (int i = 0; i < 8; i++)
    {
      l <<= 8;
      l ^= paramArrayOfByte[i] & 0xFF;
    }
    return l;
  }
  
  public static byte[] shortToBytes(short paramShort)
  {
    byte[] arrayOfByte = new byte[2];
    arrayOfByte[0] = ((byte)(paramShort >> 8 & 0xFF));
    arrayOfByte[1] = ((byte)(paramShort & 0xFF));
    return arrayOfByte;
  }
  
  public static short bytesToShort(byte[] paramArrayOfByte)
  {
    int i = 0;
    i = (short)(i | paramArrayOfByte[0] << 8);
    i = (short)(i | paramArrayOfByte[1]);
    return i;
  }
  
  public static byte[] doubleToBytes(double paramDouble)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(8);
    DataOutputStream localDataOutputStream = new DataOutputStream(localByteArrayOutputStream);
    try
    {
      localDataOutputStream.writeDouble(paramDouble);
      return localByteArrayOutputStream.toByteArray();
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    return null;
  }
  
  public static double bytesToDouble(byte[] paramArrayOfByte)
  {
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
    DataInputStream localDataInputStream = new DataInputStream(localByteArrayInputStream);
    try
    {
      double d = localDataInputStream.readDouble();
      return d;
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    return (0.0D / 0.0D);
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.op.sync.FileOpUtils
 * JD-Core Version:    0.7.0.1
 */