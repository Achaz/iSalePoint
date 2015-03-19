package dm.tools.ui;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.JXTable;

public class NonEditableJXTable
  extends JXTable
{
  public NonEditableJXTable()
  {
    setColumnControlVisible(true);
  }
  
  public NonEditableJXTable(DefaultTableModel paramDefaultTableModel)
  {
    super(paramDefaultTableModel);
    setColumnControlVisible(true);
    getInputMap(1).put(KeyStroke.getKeyStroke(10, 0), "SOME_ACTION");
    getInputMap(0).put(KeyStroke.getKeyStroke(10, 0), "SOME_ACTION");
    AbstractAction local1 = new AbstractAction()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent) {}
    };
    getActionMap().put("SOME_ACTION", local1);
  }
  
  public boolean isCellEditable(int paramInt1, int paramInt2)
  {
    return false;
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.tools.ui.NonEditableJXTable
 * JD-Core Version:    0.7.0.1
 */