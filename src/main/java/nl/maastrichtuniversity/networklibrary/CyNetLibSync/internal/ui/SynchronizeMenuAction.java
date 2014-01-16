package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.ui;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.Plugin;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;

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

	@Override
	public void actionPerformed(ActionEvent e) {

		Object[] directions = {"Up","Down"};
		int n = JOptionPane.showOptionDialog(plugin.getCySwingApplication().getJFrame(),
				null,
				"Synchronization direction",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				directions,
				null);

		if(n > 0){
			// TODO: 1. make Cytoscape TASK for Extension Invocation
			// TODO: 2. start invocation and exit here
		}
	}

}
