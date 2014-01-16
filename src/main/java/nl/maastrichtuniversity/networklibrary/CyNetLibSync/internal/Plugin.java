package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal;

import java.util.List;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.osgi.framework.BundleContext;

public class Plugin{
	private CyApplicationManager cyApplicationManager;
	private BundleContext context;
	
	private Neo4jConnectionHandler connHandler;
	private CySwingApplication cySwingApplication;

	public Plugin(CyApplicationManager cyApplicationManager, CySwingApplication cySwingApplication, BundleContext context, CyActivator cyActivator) {
		super();
		this.cyApplicationManager = cyApplicationManager;
		this.cySwingApplication = cySwingApplication;
		this.context = context;
		
	}
	
	public Neo4jConnectionHandler getNeo4jConnectionHandler() {
		if(connHandler == null)
			connHandler = new Neo4jConnectionHandler();
		
		return connHandler;
	}
	
	
	public BundleContext getContext() {
		return context;
	}

	public CyApplicationManager getCyApplicationManager() {
		return cyApplicationManager;
	}

	public CySwingApplication getCySwingApplication() {
		return cySwingApplication;
	}

	public List<String> getAvailableExtensions() {
		
		return null;
	}

	public boolean connectToInstance(String instanceLocation) {
		return false;
	}

	public boolean isConnected() {
		
		return false;
	}


}
