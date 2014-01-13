package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.controls;

import java.awt.event.ActionEvent;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.Neo4jConnectionHandler;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;

public class ServiceMenuAction extends AbstractCyAction {

	private static final String MENU_LOC = "Apps.CyNetLibSync.Services";
	private String name;
	
	public ServiceMenuAction(CyApplicationManager cyApplicationManager, String name) {
		super(name, cyApplicationManager,null,null);
		this.name = name;
		setPreferredMenu(MENU_LOC);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		// TODO who do you call?

	}

}
