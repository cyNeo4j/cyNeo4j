package nl.maastrichtuniversity.networklibrary.cyneo4j.internal;

import java.util.Properties;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.generallogic.ConnectInstanceMenuAction;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync.SyncDownMenuAction;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync.SyncUpMenuAction;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyTableFactory;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.work.swing.DialogTaskManager;
import org.osgi.framework.BundleContext;

public class CyActivator extends AbstractCyActivator {
	
	protected Plugin plugin;

	@Override
	public void start(BundleContext context) throws Exception {
		
		CyApplicationManager cyApplicationManager = getService(context, CyApplicationManager.class);
		CySwingApplication cySwingApplication = getService(context, CySwingApplication.class);
		CyNetworkFactory cyNetworkFactory = getService(context, CyNetworkFactory.class);
		CyNetworkManager cyNetMgr = getService(context,CyNetworkManager.class);
		CyTableFactory tableFactory = getService(context, CyTableFactory.class);
		CyNetworkViewManager cyNetViewMgr = getService(context, CyNetworkViewManager.class);
		DialogTaskManager diagTaskManager = getService(context, DialogTaskManager.class);
		CyNetworkViewFactory cyNetworkViewFactory = getService(context, CyNetworkViewFactory.class);
		CyLayoutAlgorithmManager cyLayoutAlgorithmMgr = getService(context,CyLayoutAlgorithmManager.class);
		VisualMappingManager visualMappingMgr = getService(context,VisualMappingManager.class);
		
		plugin = new Plugin(cyApplicationManager,cySwingApplication,cyNetworkFactory,tableFactory,cyNetMgr,cyNetViewMgr,diagTaskManager,cyNetworkViewFactory,cyLayoutAlgorithmMgr,visualMappingMgr);
		
		// core actions
		ConnectInstanceMenuAction connectAction = new ConnectInstanceMenuAction(cyApplicationManager,plugin);	
		SyncUpMenuAction syncUpAction = new SyncUpMenuAction(cyApplicationManager, plugin);
		SyncDownMenuAction syncDownAction = new SyncDownMenuAction(cyApplicationManager, plugin);
		
		registerAllServices(context, connectAction, new Properties());
		registerAllServices(context, syncUpAction, new Properties());
		registerAllServices(context, syncDownAction, new Properties());
		
	}
	
	@Override
	public void shutDown(){
		plugin.cleanUp();
	}
	

}
