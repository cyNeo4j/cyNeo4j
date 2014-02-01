package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyTableFactory;

public class Plugin implements Neo4jInteractor{
	private CyApplicationManager cyApplicationManager = null;
	
	private SimpleNeo4jConnectionHandler connHandler = null;
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

	public SimpleNeo4jConnectionHandler getNeo4jConnectionHandler() {
		if(connHandler == null)
			connHandler = new SimpleNeo4jConnectionHandler(this);
		
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
		return getNeo4jConnectionHandler().connectToInstance(instanceLocation);
	}

	public boolean isConnected() {
		return getNeo4jConnectionHandler().isConnected();
	}

	public void syncUp(boolean wipeRemote) {
		getNeo4jConnectionHandler().syncUp(wipeRemote);		
	}

	public void syncDown(boolean mergeInCurrent) {
		
		getNeo4jConnectionHandler().syncDown(mergeInCurrent);
		
	}

	public String getInstanceLocation() {
		return getNeo4jConnectionHandler().getInstanceLocation();
	}

	public void addNetwork(Long SUID) {
		myNetworks.add(SUID);
	}

}
