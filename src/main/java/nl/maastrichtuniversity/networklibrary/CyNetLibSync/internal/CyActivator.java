package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal;

import java.util.Properties;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.impl.CircularLayoutExtMenuAction;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.impl.CypherMenuAction;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.impl.ForceAtlas2LayoutExtMenuAction;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.impl.GridLayoutExtMenuAction;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.impl.NeoNetworkAnalyzerAction;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.impl.ShortestPathExtMenuAction;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.generallogic.ConnectInstanceMenuAction;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.synclogic.SyncDownMenuAction;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.synclogic.SyncUpMenuAction;

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
			
		SyncUpMenuAction syncUpAction = new SyncUpMenuAction(cyApplicationManager, plugin);
		SyncDownMenuAction syncDownAction = new SyncDownMenuAction(cyApplicationManager, plugin);
		
		registerAllServices(context, connectAction, new Properties());
		registerAllServices(context, syncUpAction, new Properties());
		registerAllServices(context, syncDownAction, new Properties());
		
		// automate me!
//		ShortestPathExtMenuAction spMenuAction = new ShortestPathExtMenuAction(cyApplicationManager, plugin);
//		registerAllServices(context, spMenuAction, new Properties());
		
		CypherMenuAction cypherMenuAction = new CypherMenuAction(cyApplicationManager, plugin);
		registerAllServices(context, cypherMenuAction, new Properties());
		
		GridLayoutExtMenuAction gridlayoutMenuAction = new GridLayoutExtMenuAction(cyApplicationManager, plugin);
		registerAllServices(context,gridlayoutMenuAction,new Properties());
		
		CircularLayoutExtMenuAction circlayoutMenuAction = new CircularLayoutExtMenuAction(cyApplicationManager, plugin);
//		cySwingApplication.addAction(circlayoutMenuAction);
		registerAllServices(context,circlayoutMenuAction,new Properties());
		
		ForceAtlas2LayoutExtMenuAction forceAtlasMenuAction = new ForceAtlas2LayoutExtMenuAction(cyApplicationManager, plugin);
		registerAllServices(context, forceAtlasMenuAction, new Properties());
//		cySwingApplication.addAction(forceAtlasMenuAction);
		
		
		NeoNetworkAnalyzerAction neoNetworkAnalyzerMenuAction = new NeoNetworkAnalyzerAction(cyApplicationManager, plugin);
		registerAllServices(context,neoNetworkAnalyzerMenuAction,new Properties());
//		cySwingApplication.addAction(neoNetworkAnalyzerMenuAction);
		
	}
	
	
}
