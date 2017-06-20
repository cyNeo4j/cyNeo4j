package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;

@SuppressWarnings("serial")
public class SyncDownMenuAction extends AbstractCyAction {

	public final static String MENU_TITLE = "Sync Down";
	public final static String MENU_LOC = "Apps.cyNeo4j";

	private Plugin plugin;

	public SyncDownMenuAction(CyApplicationManager cyApplicationManager, Plugin plugin){
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
//		if(!plugin.getInteractor().isConnected()){
//			JOptionPane.showMessageDialog(null, "Not connected to any remote instance");
//			return;
//		}
		getPlugin().getInteractor().syncDown(false);
	}

}
