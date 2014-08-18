package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider.sync;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.Plugin;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;

@Deprecated
public class SynchronizeMenuAction extends AbstractCyAction {

	public final static String MENU_TITLE = "Synchronize";
	public final static String MENU_LOC = "Apps.CyNetLibSync";

	private Plugin plugin;

	public SynchronizeMenuAction(CyApplicationManager cyApplicationManager, Plugin plugin){
		super(MENU_TITLE, cyApplicationManager, null, null);
		setPreferredMenu(MENU_LOC);
		setEnabled(false);
		this.plugin = plugin;
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
		
		Object[] directions = {"Up","Down"};
		int n = JOptionPane.showOptionDialog(plugin.getCySwingApplication().getJFrame(),
				null,
				"Synchronization direction",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				directions,
				null);
	
		switch(n){
		case 0:
			getPlugin().getInteractor().syncUp(true,getPlugin().getCyApplicationManager().getCurrentNetwork());
			
			break;
		case 1:
			getPlugin().getInteractor().syncDown(false);
			// take care of the view and layout! not the interactors job
			break;
		default:
			break;
		}

	}

}
