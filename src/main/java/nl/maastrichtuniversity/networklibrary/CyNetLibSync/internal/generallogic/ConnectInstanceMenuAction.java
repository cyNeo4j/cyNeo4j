package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.generallogic;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.Plugin;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;

public class ConnectInstanceMenuAction extends AbstractCyAction {
	
	public final static String MENU_TITLE = "Connect to Instance";
	public final static String MENU_LOC = "Apps.CyNetLibSync";
	
	private Plugin plugin;
	
	public ConnectInstanceMenuAction(CyApplicationManager cyApplicationManager, Plugin plugin){
		super(MENU_TITLE, cyApplicationManager, null, null);
		setPreferredMenu(MENU_LOC);
		this.plugin = plugin;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		String instanceLocation = null;
		
		// TODO add URL validation to menu
		while(instanceLocation == null || instanceLocation.isEmpty())
			instanceLocation = JOptionPane.showInputDialog("Provide the URL of the Neo4j instance:","http://localhost:7474");
		
		boolean status = plugin.getInteractor().connect(instanceLocation);
		
		if(status)
			JOptionPane.showMessageDialog(null, "Connected to Instance at "+ instanceLocation);
		else
			JOptionPane.showMessageDialog(null, "Failed to connect to Instance at " + instanceLocation);
	}

}
