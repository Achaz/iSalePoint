package dm.jb.ui.settings.initial;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class InitialSetupWizard
  extends JFrame
{
  public InitialSetupWizard()
  {
    super("iSalePoint Initial Setup");
    initUI();
    pack();
    setLocationRelativeTo(null);
    setDefaultCloseOperation(3);
  }
  
  public static void main(String[] paramArrayOfString)
  {
    InitialSetupWizard localInitialSetupWizard = new InitialSetupWizard();
    localInitialSetupWizard.setVisible(true);
  }
  
  private void initUI()
  {
    GridBagLayout localGridBagLayout = new GridBagLayout();
    JPanel localJPanel = (JPanel)getContentPane();
    localJPanel.setLayout(localGridBagLayout);
    GridBagConstraints localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 3;
    localJPanel.add(getButtonPanel(), localGridBagConstraints);
  }
  
  private JPanel getButtonPanel()
  {
    JPanel localJPanel = new JPanel();
    GridBagLayout localGridBagLayout = new GridBagLayout();
    localJPanel.setLayout(localGridBagLayout);
    GridBagConstraints localGridBagConstraints = new GridBagConstraints();
    Insets localInsets = new Insets(0, 0, 0, 10);
    localGridBagConstraints.insets = localInsets;
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 0;
    JButton localJButton = new JButton("<< Previous");
    localJButton.setPreferredSize(new Dimension(120, 30));
    localJPanel.add(localJButton, localGridBagConstraints);
    localGridBagConstraints.gridx = 1;
    localJButton = new JButton("Next >>");
    localJButton.setPreferredSize(new Dimension(120, 30));
    localJPanel.add(localJButton, localGridBagConstraints);
    localJButton = new JButton("Finish");
    localGridBagConstraints.gridx = 2;
    localJButton.setPreferredSize(new Dimension(120, 30));
    localJPanel.add(localJButton, localGridBagConstraints);
    localJButton = new JButton("Close");
    localGridBagConstraints.gridx = 3;
    localJButton.setPreferredSize(new Dimension(120, 30));
    localJPanel.add(localJButton, localGridBagConstraints);
    return localJPanel;
  }
  
  private class WelcomeScreen
    extends JPanel
    implements SetupWizard
  {
    private WelcomeScreen() {}
  }
}


/* Location:           D:\pos\iSalePoint\iSalePoint.jar
 * Qualified Name:     dm.jb.ui.settings.initial.InitialSetupWizard
 * JD-Core Version:    0.7.0.1
 */