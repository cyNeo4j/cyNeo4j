package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync;

import java.awt.Point;
import java.awt.event.ActionEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.generallogic.ConnectPanel;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;

@SuppressWarnings("serial")
public class SyncNewMenuAction extends AbstractCyAction {

	public final static String MENU_TITLE = "Directed Small Molecules Network 1";
	public final static String MENU_LOC = "Apps.cyNeo4j";

	private Plugin plugin;

	public SyncNewMenuAction(CyApplicationManager cyApplicationManager, Plugin plugin){
		super(MENU_TITLE, cyApplicationManager, null, null);
		setPreferredMenu(MENU_LOC);
		setEnabled(false);
		setMenuGravity(0.1f);
		this.plugin = plugin;
		
	}

	protected Plugin getPlugin() {
		return plugin;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
//		JDialog dialog = new JDialog(plugin.getCySwingApplication().getJFrame());
		
		JDialog dialog = new JDialog(plugin.getCySwingApplication().getJFrame());
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		IdsInputPanel p = new IdsInputPanel(dialog,plugin);
		p.setOpaque(true);
		locate(dialog);
		dialog.setModal(true);
		dialog.setContentPane(p);
		dialog.setResizable(false);
		
		dialog.pack();
		dialog.setVisible(true);
		
		
		
		getPlugin().getInteractor().syncNew(false);
	}
	
	private void locate(JDialog dialog) {
		 
        Point cyLocation = plugin.getCySwingApplication().getJFrame().getLocation();
        int cyHeight = plugin.getCySwingApplication().getJFrame().getHeight();
        int cyWidth = plugin.getCySwingApplication().getJFrame().getWidth();

        Point middle = new Point(cyLocation.x + (cyWidth / 4), cyLocation.y + (cyHeight / 4));

        dialog.setLocation(middle);
	 }


}
