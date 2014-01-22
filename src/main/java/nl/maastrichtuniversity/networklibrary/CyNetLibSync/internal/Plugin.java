package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyTableFactory;

public class Plugin{
	private CyApplicationManager cyApplicationManager = null;
	
	private Neo4jConnectionHandler connHandler = null;
	private CySwingApplication cySwingApplication = null;
	private CyNetworkFactory cyNetworkFactory = null;
	private CyTableFactory cyTableFactory = null;
	private CyNetworkManager cyNetMgr = null;
	
	private Set<Long> myNetworks = new HashSet<Long>();

	public Plugin(CyApplicationManager cyApplicationManager, 
			CySwingApplication cySwingApplication,
			CyNetworkFactory cyNetworkFactory, CyTableFactory cyTableFactory, CyNetworkManager cyNetMgr
			) {
		super();
		this.cyApplicationManager = cyApplicationManager;
		this.cySwingApplication = cySwingApplication;
		this.cyNetworkFactory = cyNetworkFactory;
		this.cyTableFactory = cyTableFactory;
		this.cyNetMgr = cyNetMgr;
	}
	
	public CyNetworkFactory getCyNetworkFactory() {
		return cyNetworkFactory;
	}
	
	public CyTableFactory getCyTableFactory() {
		return cyTableFactory;
	}
	
	public CyNetworkManager getCyNetworkManager() {
		return cyNetMgr;
	}

	public Neo4jConnectionHandler getNeo4jConnectionHandler() {
		if(connHandler == null)
			connHandler = new Neo4jConnectionHandler(this);
		
		return connHandler;
	}

	public CyApplicationManager getCyApplicationManager() {
		return cyApplicationManager;
	}

	public CySwingApplication getCySwingApplication() {
		return cySwingApplication;
	}

	public List<String> getAvailableExtensions() {
		
		return getNeo4jConnectionHandler().getExtensions();
	}

	public boolean connectToInstance(String instanceLocation) {
		return getNeo4jConnectionHandler().connect(instanceLocation);
	}

	public boolean isConnected() {
		return getNeo4jConnectionHandler().isConnected();
	}

	public void syncUp() {
		// TODO Auto-generated method stub
		
	}

	public void syncDown() {
		
		getNeo4jConnectionHandler().syncDown();
		
	}

	public String getInstanceLocation() {
		return getNeo4jConnectionHandler().getInstanceLocation();
	}

	public void addNetwork(Long SUID) {
		myNetworks.add(SUID);
	}

}
