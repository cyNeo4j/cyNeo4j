package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal;

import java.util.Observable;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.controls.ServiceActionCreator;

import org.cytoscape.application.CyApplicationManager;
import org.osgi.framework.BundleContext;

public class Plugin extends Observable{
	private CyApplicationManager cyApplicationManager;
	private BundleContext context;
	
	private Neo4jConnectionHandler connHandler;
	private ServiceActionCreator serviceActionCreator;
	
	public Plugin(CyApplicationManager cyApplicationManager, BundleContext context, CyActivator cyActivator) {
		super();
		this.cyApplicationManager = cyApplicationManager;
		this.context = context;
		
		getNeo4jConnectionHandler().addObserver(getServiceActionCreator());
	}
	
	public Neo4jConnectionHandler getNeo4jConnectionHandler() {
		if(connHandler == null)
			connHandler = new Neo4jConnectionHandler();
		
		return connHandler;
	}
	
	public ServiceActionCreator getServiceActionCreator() {
		if(serviceActionCreator == null)
			serviceActionCreator = new ServiceActionCreator(getContext(),getCyApplicationManager());
		
		return serviceActionCreator;
	}

	public BundleContext getContext() {
		return context;
	}

	public CyApplicationManager getCyApplicationManager() {
		return cyApplicationManager;
	}


}
