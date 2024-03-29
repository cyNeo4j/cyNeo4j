//	cyNeo4j - Cytoscape app connecting to Neo4j
//
//	Copyright 2014-2022  
//
//	Licensed under the Apache License, Version 2.0 (the "License");
//	you may not use this file except in compliance with the License.
//	You may obtain a copy of the License at
//
//		http://www.apache.org/licenses/LICENSE-2.0
//
//	Unless required by applicable law or agreed to in writing, software
//	distributed under the License is distributed on an "AS IS" BASIS,
//	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//	See the License for the specific language governing permissions and
//	limitations under the License.

package nl.maastrichtuniversity.networklibrary.cyneo4j.internal;

import java.util.Properties;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyTableFactory;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.work.swing.DialogTaskManager;
import org.osgi.framework.BundleContext;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.generallogic.ConnectInstanceMenuAction;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.dsmn.DsmnResultPanel;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.dsmn.SyncDsmnMenuAction;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync.SyncDownMenuAction;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync.SyncUpMenuAction;

public class CyActivator extends AbstractCyActivator {

	protected Plugin plugin;

	@Override
	public void start(BundleContext context) throws Exception {

		CyApplicationManager cyApplicationManager = getService(context, CyApplicationManager.class);
		CySwingApplication cySwingApplication = getService(context, CySwingApplication.class);
		CyNetworkFactory cyNetworkFactory = getService(context, CyNetworkFactory.class);
		CyNetworkManager cyNetMgr = getService(context, CyNetworkManager.class);
		CyTableFactory tableFactory = getService(context, CyTableFactory.class);
		CyNetworkViewManager cyNetViewMgr = getService(context, CyNetworkViewManager.class);
		DialogTaskManager diagTaskManager = getService(context, DialogTaskManager.class);
		CyNetworkViewFactory cyNetworkViewFactory = getService(context, CyNetworkViewFactory.class);
		CyLayoutAlgorithmManager cyLayoutAlgorithmMgr = getService(context, CyLayoutAlgorithmManager.class);
		VisualMappingManager visualMappingMgr = getService(context, VisualMappingManager.class);
		VisualMappingFunctionFactory vmfFactoryP = getService(context, VisualMappingFunctionFactory.class,
				"(mapping.type=passthrough)");
		VisualMappingFunctionFactory vmfFactoryC = getService(context, VisualMappingFunctionFactory.class,
				"(mapping.type=continuous)");

		plugin = new Plugin(cyApplicationManager, cySwingApplication, cyNetworkFactory, tableFactory, cyNetMgr,
				cyNetViewMgr, diagTaskManager, cyNetworkViewFactory, cyLayoutAlgorithmMgr, visualMappingMgr,
				vmfFactoryP, vmfFactoryC);

		ConnectInstanceMenuAction connectAction = new ConnectInstanceMenuAction(cyApplicationManager, plugin);
		SyncUpMenuAction syncUpAction = new SyncUpMenuAction(plugin);
		SyncDownMenuAction syncDownAction = new SyncDownMenuAction(cyApplicationManager, plugin);
		SyncDsmnMenuAction syncDsmnAction = new SyncDsmnMenuAction(cyApplicationManager, plugin);

		DsmnResultPanel myPanel = new DsmnResultPanel();
		plugin.setResultPanel(myPanel);
		registerService(context, myPanel, CytoPanelComponent.class, new Properties());

		registerAllServices(context, connectAction, new Properties());
		registerAllServices(context, syncUpAction, new Properties());
		registerAllServices(context, syncDownAction, new Properties());
		registerAllServices(context, syncDsmnAction, new Properties());
	}

	@Override
	public void shutDown() {
		plugin.cleanUp();
	}
}
