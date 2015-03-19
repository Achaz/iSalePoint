package dm.jb.db.objects;

import dm.jb.db.gen.CustomerAdvancedBaseRow;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class CustomerAdvancedRow
  extends CustomerAdvancedBaseRow
{
  public static Hobby[] Hobbies = { new Hobby("Computer Games", "CG"), new Hobby("Dance", "DN"), new Hobby("Fashion", "FS"), new Hobby("Movies", "MV"), new Hobby("Music-Classical", "MC"), new Hobby("Music-Others", "MO"), new Hobby("Sports", "SP"), new Hobby("Travel", "TR"), new Hobby("Trecking", "TC") };
  public boolean _mCreated = false;
  
  public CustomerAdvancedRow(int paramInt, CustomerAdvancedTableDef paramCustomerAdvancedTableDef)
  {
    super(paramInt, paramCustomerAdvancedTableDef);
  }
  
  public static ArrayList<Hobby> getHobbiesForCode(String paramString)
  {
    if ((paramString == null) || (paramString.length() == 0)) {
      return null;
    }
    ArrayList localArrayList = new ArrayList();
    StringTokenizer localStringTokenizer = new StringTokenizer(paramString, ":");
    while (localStringTokenizer.hasMoreElements())
    {
      String str = localStringTokenizer.nextToken();
      for (Hobby localHobby : Hobbies) {
        if (localHobby.code.equals(str))
        {
          localArrayList.add(localHobby);
          break;
        }
      }
    }
    return localArrayList;
  }
  
  public static String getHobbyStringFromList(ArrayList<Hobby> paramArrayList)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    if (paramArrayList == null) {
      return null;
    }
    int i = paramArrayList.size();
    if (i == 0) {
      return null;
    }
    for (int j = 0; j < i - 1; j++)
    {
      localStringBuffer.append(((Hobby)paramArrayList.get(j)).code);
      localStringBuffer.append(":");
    }
    localStringBuffer.append(((Hobby)paramArrayList.get(i - 1)).code);
    return localStringBuffer.toString();
  }
  
  public void setCreated(boolean paramBoolean)
  {
    this._mCreated = paramBoolean;
  }
  
  public boolean isCreated()
  {
    return this._mCreated;
  }
  
  public static class Hobby
  {
    String name;
    String code;
    
    public Hobby(String paramString1, String paramString2)
    {
      this.name = paramString1;
      this.code = paramString2;
    }
    
    public String toString()
    {
      return this.name;
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.db.objects.CustomerAdvancedRow
 * JD-Core Version:    0.7.0.1
 */