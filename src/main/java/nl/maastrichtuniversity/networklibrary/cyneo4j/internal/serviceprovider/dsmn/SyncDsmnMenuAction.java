package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.dsmn;

import java.awt.Point;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;

public class SyncDsmnMenuAction extends AbstractCyAction {

	public final static String MENU_TITLE = "Directed Small Molecules Network";
	public final static String MENU_LOC = "Apps.cyNeo4j";
  

	private Plugin plugin;
	//private ImageIcon icon = null;

	public SyncDsmnMenuAction(CyApplicationManager cyApplicationManager, Plugin plugin){
		super(MENU_TITLE, cyApplicationManager, null, null);
		ImageIcon icon = new ImageIcon(getClass().getResource("/images/shortestPath30.png"));
		//putValue(LARGE_ICON_KEY, icon); //Adds icon directly to menu bar under toolbar, iso as a logo to App menu...
		setPreferredMenu(MENU_LOC);
		putValue(SMALL_ICON, icon); //Adds icon to submenu
		setEnabled(false);
		setMenuGravity(0.1f);
		this.plugin = plugin;
		
	}
	
	public boolean isInToolBar() {
	        return true;
    }

	@Override
	public void actionPerformed(ActionEvent e) {

		if (plugin.getInteractor().getInstanceLocation()==null){
			JOptionPane.showMessageDialog(plugin.getCySwingApplication().getJFrame(),
					"Please connect to a Neo4j instance first!");
		}
		else{
			JDialog dialog = new JDialog(plugin.getCySwingApplication().getJFrame());
			dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			DsmnInputPanel p = new DsmnInputPanel(dialog,plugin);
			p.setOpaque(true);
			locate(dialog);
			dialog.setModal(true);
			dialog.setContentPane(p);
			dialog.setResizable(false);
			dialog.pack();
			dialog.setVisible(true);
		}
	}
	
	private void locate(JDialog dialog) {
		 
        Point cyLocation = plugin.getCySwingApplication().getJFrame().getLocation();
        int cyHeight = plugin.getCySwingApplication().getJFrame().getHeight();
        int cyWidth = plugin.getCySwingApplication().getJFrame().getWidth();

        Point middle = new Point(cyLocation.x + (cyWidth / 4), cyLocation.y + (cyHeight / 4));

        dialog.setLocation(middle);
	 }


	protected Plugin getPlugin() {
		return plugin;
	}
}
