package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.synclogic;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.Plugin;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;

public class SyncDownMenuAction extends AbstractCyAction {

	public final static String MENU_TITLE = "Sync Down";
	public final static String MENU_LOC = "Apps.CyNetLibSync";

	private Plugin plugin;

	public SyncDownMenuAction(CyApplicationManager cyApplicationManager, Plugin plugin){
		super(MENU_TITLE, cyApplicationManager, null, null);
		setPreferredMenu(MENU_LOC);
		setEnabled(false);
		this.plugin = plugin;
		
//		ImageIcon icon = new ImageIcon(getClass().getResource("/resources/red_down.png"));
//		putValue(LARGE_ICON_KEY, icon);
	}

	protected Plugin getPlugin() {
		return plugin;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(!plugin.getInteractor().isConnected()){
			JOptionPane.showMessageDialog(null, "Not connected to any remote instance");
			return;
		}
		getPlugin().getInteractor().syncDown(false);
	}

//	public boolean isInToolBar() {
//		return true;
//	}
}
