package dm.tools.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import xeus.jcl.JarClassLoader;
import xeus.jcl.exception.JclException;

public class JBJarClassLoader
  extends JarClassLoader
{
  JarFile jarFile = null;
  
  public JBJarClassLoader(String[] paramArrayOfString)
    throws IOException, JclException
  {
    super((String[])paramArrayOfString);
  }
  
  public InputStream getResourceAsStream(String paramString)
  {
    ZipEntry localZipEntry = this.jarFile.getEntry(paramString);
    if (localZipEntry == null) {
      return null;
    }
    InputStream localInputStream = null;
    try
    {
      localInputStream = this.jarFile.getInputStream(localZipEntry);
    }
    catch (IOException localIOException)
    {
      return null;
    }
    return localInputStream;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.utils.JBJarClassLoader
 * JD-Core Version:    0.7.0.1
 */