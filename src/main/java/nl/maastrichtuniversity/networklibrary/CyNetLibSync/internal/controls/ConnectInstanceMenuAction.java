package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.controls;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.Neo4jConnectionHandler;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;

public class ConnectInstanceMenuAction extends AbstractCyAction {
	
	public final static String MENU_TITLE = "Connect to Instance";
	public final static String MENU_LOC = "Apps.CyNetLibSync";
	
	private Neo4jConnectionHandler connHandler;
	
	public ConnectInstanceMenuAction(CyApplicationManager cyApplicationManager, Neo4jConnectionHandler neo4jConnectionHandler){
		super(MENU_TITLE, cyApplicationManager, null, null);
		setPreferredMenu(MENU_LOC);
		connHandler = neo4jConnectionHandler;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO 1: make connection dialog to get a url
		String location = new String("http://localhost:7474");
		
		connHandler.connect(location);
		
		String msg = new String();
		
		if(connHandler.isConnected())
			msg = "connection established";
		else
			msg = "connection failed";
		
		JOptionPane.showMessageDialog(null, msg);
	}

}
