package dm.tools.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Config
  extends DefaultHandler
{
  public static Config INSTANCE = new Config();
  private static String _mFile = null;
  private ConfigNode _mCurrentNode = null;
  private ConfigNode _mRootNode = null;
  
  public boolean loadConfig(String paramString)
  {
    _mFile = paramString;
    return loadConfigFile(paramString);
  }
  
  public String getAttrib(String paramString)
  {
    int i = 0;
    Object localObject = this._mRootNode;
    int j = paramString.indexOf('.');
    String str;
    if (j == -1)
    {
      if (((ConfigNode)localObject).name.equals(paramString)) {
        return null;
      }
    }
    else
    {
      str = paramString.substring(0, j);
      paramString = paramString.substring(j + 1);
      j = paramString.indexOf('.');
      if (j == -1) {
        return ((ConfigNode)localObject).getAttribValue(str);
      }
    }
    while (i == 0)
    {
      j = paramString.indexOf('.');
      if (j == -1) {
        break;
      }
      str = paramString.substring(0, j);
      ConfigNode localConfigNode = ((ConfigNode)localObject).getChild(str);
      if (localConfigNode == null) {
        return null;
      }
      localObject = localConfigNode;
      paramString = paramString.substring(j + 1);
    }
    return ((ConfigNode)localObject).getAttribValue(paramString);
  }
  
  public void printXML()
  {
    DocumentBuilderFactory localDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
    PrintStream localPrintStream = null;
    try
    {
      DocumentBuilder localDocumentBuilder = localDocumentBuilderFactory.newDocumentBuilder();
      Document localDocument = localDocumentBuilder.newDocument();
      localDocument.appendChild(this._mRootNode.printXML(localDocument));
      localPrintStream = new PrintStream(new FileOutputStream(new File(_mFile)));
      OutputFormat localOutputFormat = new OutputFormat(localDocument);
      localOutputFormat.setIndenting(true);
      XMLSerializer localXMLSerializer = new XMLSerializer(localPrintStream, localOutputFormat);
      localXMLSerializer.serialize(localDocument);
    }
    catch (ParserConfigurationException localParserConfigurationException)
    {
      localParserConfigurationException.printStackTrace();
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    finally
    {
      if (localPrintStream != null) {
        localPrintStream.close();
      }
    }
  }
  
  public void setAttrib(String paramString1, String paramString2)
  {
    int i = 0;
    Object localObject = this._mRootNode;
    int j = paramString1.indexOf('.');
    String str;
    if (j == -1)
    {
      if (!((ConfigNode)localObject).name.equals(paramString1)) {}
    }
    else
    {
      str = paramString1.substring(0, j);
      paramString1 = paramString1.substring(j + 1);
      j = paramString1.indexOf('.');
      if (j == -1)
      {
        ((ConfigNode)localObject).addAttrib(str, paramString2);
        return;
      }
    }
    while (i == 0)
    {
      j = paramString1.indexOf('.');
      if (j == -1) {
        break;
      }
      str = paramString1.substring(0, j);
      ConfigNode localConfigNode = ((ConfigNode)localObject).getChild(str);
      if (localConfigNode == null)
      {
        addValueToNode((ConfigNode)localObject, paramString1, paramString2);
        return;
      }
      paramString1 = paramString1.substring(j + 1);
      localObject = localConfigNode;
    }
    ((ConfigNode)localObject).addAttrib(paramString1, paramString2);
  }
  
  private void addValueToNode(ConfigNode paramConfigNode, String paramString1, String paramString2)
  {
    for (int i = paramString1.indexOf('.'); i >= 0; i = paramString1.indexOf('.'))
    {
      String str = paramString1.substring(0, i);
      ConfigNode localConfigNode = new ConfigNode(str, null);
      paramConfigNode.addChild(localConfigNode);
      localConfigNode.parent = paramConfigNode;
      paramConfigNode = localConfigNode;
      paramString1 = paramString1.substring(i + 1);
    }
    paramConfigNode.addAttrib(paramString1, paramString2);
  }
  
  private boolean loadConfigFile(String paramString)
  {
    SAXParserFactory localSAXParserFactory = SAXParserFactory.newInstance();
    try
    {
      SAXParser localSAXParser = localSAXParserFactory.newSAXParser();
      localSAXParser.parse(new File(paramString), this);
    }
    catch (SAXException localSAXException)
    {
      localSAXException.printStackTrace();
      return false;
    }
    catch (ParserConfigurationException localParserConfigurationException)
    {
      localParserConfigurationException.printStackTrace();
      return false;
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
      return false;
    }
    return true;
  }
  
  public void startElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
    throws SAXException
  {
    ConfigNode localConfigNode = new ConfigNode(paramString3, null);
    localConfigNode.parent = this._mCurrentNode;
    if (this._mRootNode == null) {
      this._mRootNode = localConfigNode;
    } else {
      this._mCurrentNode.addChild(localConfigNode);
    }
    if ((paramAttributes == null) || (paramAttributes.getLength() == 0))
    {
      this._mCurrentNode = localConfigNode;
      return;
    }
    int i = paramAttributes.getLength();
    for (int j = 0; j < i; j++)
    {
      String str1 = paramAttributes.getQName(j);
      String str2 = paramAttributes.getValue(j);
      localConfigNode.addAttrib(str1, str2);
    }
    this._mCurrentNode = localConfigNode;
  }
  
  public void endElement(String paramString1, String paramString2, String paramString3)
    throws SAXException
  {
    this._mCurrentNode = this._mCurrentNode.parent;
  }
  
  public class ConfigNode
  {
    private String name = null;
    private String value = null;
    private ArrayList<ConfigNode> children = null;
    private HashMap<String, String> _mAttribs = null;
    private ConfigNode parent = null;
    
    public ConfigNode(String paramString1, String paramString2)
    {
      this.name = paramString1;
      this.value = paramString2;
    }
    
    public String toString()
    {
      return this.name;
    }
    
    public String getValue()
    {
      return this.value;
    }
    
    public void addChild(ConfigNode paramConfigNode)
    {
      if (this.children == null) {
        this.children = new ArrayList();
      }
      this.children.add(paramConfigNode);
    }
    
    public void addAttrib(String paramString1, String paramString2)
    {
      if (this._mAttribs == null) {
        this._mAttribs = new HashMap();
      }
      this._mAttribs.put(paramString1, paramString2);
    }
    
    public String getAttribValue(String paramString)
    {
      if (this._mAttribs == null) {
        return null;
      }
      String str = (String)this._mAttribs.get(paramString);
      return str;
    }
    
    public ConfigNode getChild(String paramString)
    {
      if (this.children == null) {
        return null;
      }
      Iterator localIterator = this.children.iterator();
      while (localIterator.hasNext())
      {
        ConfigNode localConfigNode = (ConfigNode)localIterator.next();
        if (localConfigNode.name.equals(paramString)) {
          return localConfigNode;
        }
      }
      return null;
    }
    
    Element printXML(Document paramDocument)
      throws IOException
    {
      Element localElement = paramDocument.createElement(this.name);
      Iterator localIterator;
      Object localObject1;
      Object localObject2;
      if ((this._mAttribs != null) && (this._mAttribs.size() > 0))
      {
        localIterator = this._mAttribs.keySet().iterator();
        while (localIterator.hasNext())
        {
          localObject1 = (String)localIterator.next();
          localObject2 = (String)this._mAttribs.get(localObject1);
          localElement.setAttribute((String)localObject1, (String)localObject2);
        }
      }
      if ((this.children != null) && (this.children.size() > 0))
      {
        localIterator = this.children.iterator();
        while (localIterator.hasNext())
        {
          localObject1 = (ConfigNode)localIterator.next();
          localObject2 = ((ConfigNode)localObject1).printXML(paramDocument);
          localElement.appendChild((Node)localObject2);
        }
      }
      return localElement;
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.utils.Config
 * JD-Core Version:    0.7.0.1
 */