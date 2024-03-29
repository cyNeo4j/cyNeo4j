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
package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.dsmn;

import java.util.Set;

import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;

//import org.cytoscape.service.util.CyServiceRegistrar;
//import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.dsmn.DsmnVizStyle;


public class SyncDsmnTaskFactory extends AbstractTaskFactory {

	private CyNetworkManager cyNetworkMgr;
	private boolean mergeInCurrent;
	private CyNetworkFactory cyNetworkFactory;
	private String instanceLocation;
	private String cypherURL;
	private String auth;
	private CySwingApplication cySwingApp;
	private CyNetworkViewManager cyNetworkViewMgr;
	private CyNetworkViewFactory cyNetworkViewFactory;
	private CyLayoutAlgorithmManager cyLayoutAlgorithmMgr;
	private VisualMappingManager visualMappingMgr;
	private VisualMappingFunctionFactory vmfFactoryP;
	private VisualMappingFunctionFactory vmfFactoryC;
	private Set<String> queryList;
	private Plugin plugin;
	
//	CyServiceRegistrar registrar;
//	private DsmnVizStyle vizStyle;

	public SyncDsmnTaskFactory(boolean mergeInCurrent, Plugin plugin, String instanceLocation, String cypherURL,
			String auth) { //, DsmnVizStyle vizStyle --> also affects Neo4jRESTServer.java class
		super();
		this.cyNetworkMgr = plugin.getCyNetworkManager();
		this.mergeInCurrent = mergeInCurrent;
		this.cyNetworkFactory = plugin.getCyNetworkFactory();
		this.instanceLocation = instanceLocation;
		this.cypherURL = cypherURL;
		this.auth = auth;
		this.cyNetworkViewMgr = plugin.getCyNetViewMgr();
		this.cyNetworkViewFactory = plugin.getCyNetworkViewFactory();
		this.cyLayoutAlgorithmMgr = plugin.getCyLayoutAlgorithmManager();
		this.visualMappingMgr = plugin.getVisualMappingManager();
		this.vmfFactoryP = plugin.getVmfFactoryP();
		this.vmfFactoryC = plugin.getVmfFactoryC();
		this.queryList = plugin.getQueryList();
		this.cySwingApp = plugin.getCySwingApplication();
		this.plugin = plugin;
		//this.vizStyle =  new DsmnVizStyle(registrar);
	}

	public SyncDsmnTaskFactory(CyNetworkManager cyNetworkMgr, boolean mergeInCurrent, CyNetworkFactory cyNetworkFactory,
			String instanceLocation, String cypherURL, String auth, CyNetworkViewManager cyNetworkViewMgr,
			CyNetworkViewFactory cyNetworkViewFactory, CyLayoutAlgorithmManager cyLayoutAlgorithmMgr,
			VisualMappingManager visualMappingMgr, Set<String> queryList, VisualMappingFunctionFactory vmfFactoryP,
			VisualMappingFunctionFactory vmfFactoryC) { //, CyServiceRegistrar vizStyle
		super();
		this.cyNetworkMgr = cyNetworkMgr;
		this.mergeInCurrent = mergeInCurrent;
		this.cyNetworkFactory = cyNetworkFactory;
		this.instanceLocation = instanceLocation;
		this.cypherURL = cypherURL;
		this.auth = auth;
		this.cyNetworkViewMgr = cyNetworkViewMgr;
		this.cyNetworkViewFactory = cyNetworkViewFactory;
		this.cyLayoutAlgorithmMgr = cyLayoutAlgorithmMgr;
		this.visualMappingMgr = visualMappingMgr;
		this.vmfFactoryP = vmfFactoryP;
		this.vmfFactoryC = vmfFactoryC;
		this.queryList = queryList;
		//this.vizStyle =  new DsmnVizStyle(registrar);
	}

	@Override
	public TaskIterator createTaskIterator() {
		return new TaskIterator(new SyncDsmnTask(mergeInCurrent, plugin, cypherURL, instanceLocation, auth)); //, vizStyle //TODO: type of VizStyle is unclear, check!
	}
}