package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal;

import java.util.Properties;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.controls.ConnectInstanceMenuAction;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.controls.SynchronizeMenuAction;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.service.util.AbstractCyActivator;
import org.osgi.framework.BundleContext;

public class CyActivator extends AbstractCyActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		
		CyApplicationManager cyApplicationManager = getService(context, CyApplicationManager.class);
		
		Plugin plugin = new Plugin(cyApplicationManager,context,this);
		
		ConnectInstanceMenuAction connectAction = new ConnectInstanceMenuAction(cyApplicationManager,plugin.getNeo4jConnectionHandler());
		SynchronizeMenuAction synchAction = new SynchronizeMenuAction(cyApplicationManager,plugin);
			
		registerAllServices(context, connectAction, new Properties());
		registerAllServices(context, synchAction, new Properties());
	}
}
