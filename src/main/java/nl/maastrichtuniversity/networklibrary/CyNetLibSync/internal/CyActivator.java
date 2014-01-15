package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal;

import java.util.Properties;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.ui.ConnectInstanceMenuAction;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.ui.ServiceMenuAction;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.ui.SynchronizeMenuAction;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.service.util.AbstractCyActivator;
import org.osgi.framework.BundleContext;

public class CyActivator extends AbstractCyActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		
		CyApplicationManager cyApplicationManager = getService(context, CyApplicationManager.class);
		CySwingApplication cySwingApplication = getService(context, CySwingApplication.class);
		
		Plugin plugin = new Plugin(cyApplicationManager,cySwingApplication,context,this);
		
		ConnectInstanceMenuAction connectAction = new ConnectInstanceMenuAction(cyApplicationManager,plugin.getNeo4jConnectionHandler());
		SynchronizeMenuAction synchAction = new SynchronizeMenuAction(cyApplicationManager,plugin);
		ServiceMenuAction serviceAction = new ServiceMenuAction(cyApplicationManager, plugin);
			
		registerAllServices(context, connectAction, new Properties());
		registerAllServices(context, synchAction, new Properties());
		registerAllServices(context, serviceAction, new Properties());
	}
}
