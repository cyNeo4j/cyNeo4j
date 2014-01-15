package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.ui;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.Plugin;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;

public class SynchronizeMenuAction extends AbstractCyAction {

	public final static String MENU_TITLE = "Synchronize";
	public final static String MENU_LOC = "Apps.CyNetLibSync";
	
	public SynchronizeMenuAction(CyApplicationManager cyApplicationManager, Plugin plugin){
		super(MENU_TITLE, cyApplicationManager, null, null);
		setPreferredMenu(MENU_LOC);
		setEnabled(false);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(null, "Synchronize Action");
	}

}
