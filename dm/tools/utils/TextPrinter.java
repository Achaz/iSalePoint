package dm.tools.utils;

import java.io.IOException;
import java.io.OutputStream;

public class TextPrinter
{
  public static final byte TRIM_END = 1;
  public static final byte TRIM_START = 2;
  public static final byte TRIM_NONE = 3;
  public static final byte TABLE_HEADER_EQUAL_LENGTH = 1;
  public static final byte TABLE_HEADER_TEXT_LENGTH = 2;
  public static final byte TABLE_HEADER_CUSTOM_LENGTH = 3;
  public static final byte ALIGN_LEFT = 1;
  public static final byte ALIGN_RIGHT = 2;
  public static final byte ALIGN_CENTER = 3;
  public static int TextPrinterColumnWidth = 80;
  
  public static void printColumnNumbers(OutputStream paramOutputStream, int paramInt)
    throws IOException
  {
    char[] arrayOfChar = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };
    for (int i = 0; i < paramInt; i++) {
      paramOutputStream.write(arrayOfChar[(i % 10)]);
    }
    paramOutputStream.write(13);
    paramOutputStream.write(10);
    paramOutputStream.flush();
  }
  
  public static void printAsIs(OutputStream paramOutputStream, String paramString)
    throws IOException
  {
    char[] arrayOfChar = paramString.toCharArray();
    for (int i = 0; i < arrayOfChar.length; i++) {
      paramOutputStream.write(arrayOfChar[i]);
    }
    paramOutputStream.flush();
  }
  
  public static void printRightAlignedData(OutputStream paramOutputStream, String paramString, int paramInt, byte paramByte)
    throws IOException
  {
    String str = getRightAlignedText(paramString, paramInt, paramByte);
    char[] arrayOfChar = str.toCharArray();
    for (int i = 0; i < arrayOfChar.length; i++) {
      paramOutputStream.write(arrayOfChar[i]);
    }
    paramOutputStream.flush();
  }
  
  public static void nextLine(OutputStream paramOutputStream)
    throws IOException
  {
    paramOutputStream.write(13);
    paramOutputStream.write(10);
    paramOutputStream.flush();
  }
  
  public static void printDashedLine(OutputStream paramOutputStream, int paramInt1, int paramInt2)
    throws IOException
  {
    for (int i = 1; i < paramInt2; i++) {
      paramOutputStream.write(32);
    }
    for (i = 0; i < paramInt1; i++) {
      paramOutputStream.write(45);
    }
    paramOutputStream.flush();
  }
  
  public static void printTableHeader(OutputStream paramOutputStream, String[] paramArrayOfString, boolean paramBoolean, byte paramByte1, byte paramByte2, int paramInt1, int paramInt2, int[] paramArrayOfInt)
    throws IOException
  {
    for (int i = 1; i < paramInt2; i++) {
      paramOutputStream.write(32);
    }
    if (paramBoolean) {
      paramOutputStream.write(124);
    }
    String str2;
    Object localObject;
    int m;
    if (paramArrayOfInt != null)
    {
      String str1 = "";
      for (k = 0; k < paramArrayOfString.length; k++)
      {
        str2 = paramArrayOfString[k];
        switch (paramByte2)
        {
        case 2: 
          str1 = getRightAlignedText(str2, paramArrayOfInt[k], (byte)3);
          break;
        case 1: 
          str1 = getLeftAlignedText(str2, paramArrayOfInt[k], (byte)3);
          break;
        case 3: 
          str1 = getCenterAlignedText(str2, paramArrayOfInt[k], (byte)3);
        }
        localObject = str1.toCharArray();
        for (m = 0; m < localObject.length; m++) {
          paramOutputStream.write(localObject[m]);
        }
        if (paramBoolean) {
          paramOutputStream.write(124);
        }
      }
      return;
    }
    int j = 0;
    if (paramByte1 == 1)
    {
      for (k = 0; k < paramArrayOfString.length; k++) {
        if (j < paramArrayOfString[k].length()) {
          j = paramArrayOfString[k].length();
        }
      }
      paramInt1 = j;
    }
    for (int k = 0; k < paramArrayOfString.length; k++)
    {
      str2 = paramArrayOfString[k];
      if (paramByte1 == 2)
      {
        localObject = str2.toCharArray();
        for (m = 0; m < localObject.length; m++) {
          paramOutputStream.write(localObject[m]);
        }
      }
      else
      {
        localObject = null;
        switch (paramByte2)
        {
        case 2: 
          localObject = getRightAlignedText(str2, paramInt1, (byte)3);
          break;
        case 1: 
          localObject = getLeftAlignedText(str2, paramInt1, (byte)3);
          break;
        case 3: 
          localObject = getCenterAlignedText(str2, paramInt1, (byte)3);
        }
        char[] arrayOfChar = ((String)localObject).toCharArray();
        for (int n = 0; n < arrayOfChar.length; n++) {
          paramOutputStream.write(arrayOfChar[n]);
        }
      }
      if (paramBoolean) {
        paramOutputStream.write(124);
      }
    }
    paramOutputStream.flush();
  }
  
  public static void printTableRow(OutputStream paramOutputStream, String[] paramArrayOfString, boolean paramBoolean1, boolean paramBoolean2, byte paramByte, byte[] paramArrayOfByte, int[] paramArrayOfInt, int paramInt)
    throws IOException
  {
    for (int i = 1; i < paramInt; i++) {
      paramOutputStream.write(32);
    }
    if (paramBoolean1) {
      if (paramBoolean2) {
        paramOutputStream.write(32);
      } else {
        paramOutputStream.write(124);
      }
    }
    for (i = 0; i < paramArrayOfString.length; i++)
    {
      String str = paramArrayOfString[i];
      Object localObject;
      if (paramByte == 2)
      {
        localObject = str.toCharArray();
        for (int j = 0; j < localObject.length; j++) {
          paramOutputStream.write(localObject[j]);
        }
      }
      else
      {
        localObject = null;
        switch (paramArrayOfByte[i])
        {
        case 2: 
          localObject = getRightAlignedText(str, paramArrayOfInt[i], (byte)3);
          break;
        case 1: 
          localObject = getLeftAlignedText(str, paramArrayOfInt[i], (byte)3);
          break;
        case 3: 
          localObject = getCenterAlignedText(str, paramArrayOfInt[i], (byte)3);
        }
        char[] arrayOfChar = ((String)localObject).toCharArray();
        for (int k = 0; k < arrayOfChar.length; k++) {
          paramOutputStream.write(arrayOfChar[k]);
        }
      }
      if (paramBoolean1) {
        if (paramBoolean2) {
          paramOutputStream.write(32);
        } else {
          paramOutputStream.write(124);
        }
      }
    }
    paramOutputStream.flush();
  }
  
  public static void printTable(OutputStream paramOutputStream, String[] paramArrayOfString, int paramInt1, boolean paramBoolean, byte paramByte1, byte paramByte2, int paramInt2)
    throws IOException
  {
    for (int i = 1; i < paramInt1; i++) {
      paramOutputStream.write(32);
    }
    paramOutputStream.write(32);
    i = getTotalTableLength(paramArrayOfString, paramBoolean, paramByte1, paramByte2, paramInt2);
    i -= 2;
    printDashedLine(paramOutputStream, i, 0);
  }
  
  public static String[] getMixedAlignment(String[] paramArrayOfString1, String[] paramArrayOfString2, int paramInt)
  {
    int i = paramArrayOfString1.length;
    if (i < paramArrayOfString2.length) {
      i = paramArrayOfString2.length;
    }
    String[] arrayOfString = new String[i];
    for (int j = 0; j < i; j++)
    {
      String str1 = "";
      if (j < paramArrayOfString1.length) {
        str1 = paramArrayOfString1[j];
      }
      String str2 = "";
      if (j < paramArrayOfString2.length) {
        str2 = paramArrayOfString2[j];
      }
      StringBuffer localStringBuffer = new StringBuffer(str1);
      int k = paramInt - (str1.length() + str2.length());
      for (int m = 0; m < k; m++) {
        localStringBuffer.append(" ");
      }
      localStringBuffer.append(str2);
      arrayOfString[j] = localStringBuffer.toString();
    }
    return arrayOfString;
  }
  
  public static String[] convertToMaxRightAligned(String[] paramArrayOfString1, String[] paramArrayOfString2)
  {
    String[] arrayOfString = new String[paramArrayOfString2.length];
    int i = 0;
    for (int j = 0; j < paramArrayOfString2.length; j++) {
      if (paramArrayOfString2[j].length() > i) {
        i = paramArrayOfString2[j].length();
      }
    }
    for (j = 0; j < paramArrayOfString2.length; j++)
    {
      arrayOfString[j] = getLeftAlignedText(paramArrayOfString2[j], i, 3);
      arrayOfString[j] = (paramArrayOfString1[j] + " : " + arrayOfString[j]);
    }
    return arrayOfString;
  }
  
  private static int getTotalTableLength(String[] paramArrayOfString, boolean paramBoolean, byte paramByte1, byte paramByte2, int paramInt)
  {
    int i = 0;
    if (paramBoolean) {
      i++;
    }
    int j = 0;
    if (paramByte1 == 1)
    {
      for (k = 0; k < paramArrayOfString.length; k++) {
        if (j < paramArrayOfString[k].length()) {
          j = paramArrayOfString[k].length();
        }
      }
      paramInt = j;
    }
    for (int k = 0; k < paramArrayOfString.length; k++)
    {
      String str1 = paramArrayOfString[k];
      if (paramByte1 == 2)
      {
        i += str1.length();
      }
      else
      {
        String str2 = null;
        switch (paramByte2)
        {
        case 2: 
          str2 = getRightAlignedText(str1, paramInt, (byte)3);
          break;
        case 1: 
          str2 = getLeftAlignedText(str1, paramInt, (byte)3);
          break;
        case 3: 
          str2 = getCenterAlignedText(str1, paramInt, (byte)3);
        }
        i += str2.length();
      }
      if (paramBoolean) {
        i++;
      }
    }
    return i;
  }
  
  public static String getRightAlignedText(String paramString, int paramInt, byte paramByte)
  {
    int i = paramInt - paramString.length();
    if (i < 0)
    {
      if (paramByte == 3) {
        return paramString;
      }
      if (paramByte == 2)
      {
        i *= -1;
        return paramString.substring(i);
      }
      return paramString.substring(0, paramString.length() + i);
    }
    StringBuffer localStringBuffer = new StringBuffer();
    for (int j = 0; j < i; j++) {
      localStringBuffer.append(" ");
    }
    localStringBuffer.append(paramString);
    return localStringBuffer.toString();
  }
  
  public static String getLeftAlignedText(String paramString, int paramInt, byte paramByte)
  {
    int i = paramInt - paramString.length();
    if (i < 0)
    {
      if (paramByte == 3) {
        return paramString;
      }
      if (paramByte == 2)
      {
        i *= -1;
        return paramString.substring(i);
      }
      return paramString.substring(0, paramString.length() + i);
    }
    StringBuffer localStringBuffer = new StringBuffer(paramString);
    for (int j = 0; j < i; j++) {
      localStringBuffer.append(" ");
    }
    return localStringBuffer.toString();
  }
  
  public static String getCenterAlignedText(String paramString, int paramInt, byte paramByte)
  {
    int i = paramInt - paramString.length();
    if (i < 0)
    {
      if (paramByte == 3) {
        return paramString;
      }
      if (paramByte == 2)
      {
        i *= -1;
        return paramString.substring(i);
      }
      return paramString.substring(0, paramString.length() + i);
    }
    StringBuffer localStringBuffer = new StringBuffer();
    int j = i / 2;
    for (int k = 0; k < j; k++) {
      localStringBuffer.append(" ");
    }
    localStringBuffer.append(paramString);
    j = i - j;
    for (k = 0; k < j; k++) {
      localStringBuffer.append(" ");
    }
    return localStringBuffer.toString();
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.utils.TextPrinter
 * JD-Core Version:    0.7.0.1
 */