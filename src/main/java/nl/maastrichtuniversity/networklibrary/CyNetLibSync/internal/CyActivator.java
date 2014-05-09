package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal;

import java.util.Properties;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.ServiceMenuAction;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.generallogic.ConnectInstanceMenuAction;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.synclogic.SyncDownMenuAction;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.synclogic.SyncUpMenuAction;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.synclogic.SynchronizeMenuAction;

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
		
		
		Plugin plugin = new Plugin(cyApplicationManager,cySwingApplication,cyNetworkFactory,tableFactory,cyNetMgr,cyNetViewMgr,diagTaskManager,cyNetworkViewFactory,cyLayoutAlgorithmMgr,visualMappingMgr);
		
		
		ConnectInstanceMenuAction connectAction = new ConnectInstanceMenuAction(cyApplicationManager,plugin);
//		SynchronizeMenuAction synchAction = new SynchronizeMenuAction(cyApplicationManager,plugin);
		
		SyncUpMenuAction syncUpAction = new SyncUpMenuAction(cyApplicationManager, plugin);
		SyncDownMenuAction syncDownAction = new SyncDownMenuAction(cyApplicationManager, plugin);
		
		ServiceMenuAction serviceAction = new ServiceMenuAction(cyApplicationManager, plugin);

		registerAllServices(context, connectAction, new Properties());
//		registerAllServices(context, synchAction, new Properties());
		registerAllServices(context, syncUpAction, new Properties());
		registerAllServices(context, syncDownAction, new Properties());
		registerAllServices(context, serviceAction, new Properties());
	}
}
