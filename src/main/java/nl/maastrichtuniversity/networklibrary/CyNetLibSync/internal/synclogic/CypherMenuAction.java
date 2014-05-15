package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.synclogic;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.Plugin;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;

public class CypherMenuAction extends AbstractCyAction {

	public final static String MENU_TITLE = "Sync Down";
	public final static String MENU_LOC = "Apps.CyNetLibSync";

	private Plugin plugin;

	public CypherMenuAction(CyApplicationManager cyApplicationManager, Plugin plugin){
		super(MENU_TITLE, cyApplicationManager, null, null);
		setPreferredMenu(MENU_LOC);
		setEnabled(false);
		this.plugin = plugin;
		
//		ImageIcon icon = new ImageIcon(getClass().getResource("/resources/red_down.png"));
//		putValue(LARGE_ICON_KEY, icon);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		// make a niceish UI
		String cypherQuery = JOptionPane.showInputDialog(plugin.getCySwingApplication().getJFrame(),"Cypher query:");
		
		getPlugin().getInteractor().query(cypherQuery);

	}

	protected Plugin getPlugin() {
		return plugin;
	}

	
}
