package dm.tools.ui.components;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class TreeShuttle
  extends JPanel
{
  private JTree _mFromTree = null;
  private JTree _mToTree = null;
  
  public TreeShuttle()
  {
    initUI();
  }
  
  private void initUI()
  {
    FormLayout localFormLayout = new FormLayout("150px:grow,10px,30px,10px,150px:grow", "pref:grow");
    setLayout(localFormLayout);
    CellConstraints localCellConstraints = new CellConstraints();
    localCellConstraints.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    this._mFromTree = new JTree(new ShuttleTreeNode("Available"));
    JScrollPane localJScrollPane = new JScrollPane(this._mFromTree);
    add(localJScrollPane, localCellConstraints);
    localCellConstraints.xywh(5, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL);
    this._mToTree = new JTree(new ShuttleTreeNode("Tezt"));
    localJScrollPane = new JScrollPane(this._mToTree);
    add(localJScrollPane, localCellConstraints);
  }
  
  private class ShuttleTreeNode
    extends DefaultMutableTreeNode
  {
    public ShuttleTreeNode(String paramString)
    {
      super();
    }
  }
  
  private class ShuttleNodeWithPath
  {
    private Object _mNode = null;
    private TreePath _mPath = null;
    
    public ShuttleNodeWithPath(Object paramObject, TreePath paramTreePath)
    {
      this._mNode = paramObject;
      this._mPath = paramTreePath;
    }
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.ui.components.TreeShuttle
 * JD-Core Version:    0.7.0.1
 */