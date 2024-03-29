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
package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.layout.CyLayoutAlgorithm;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.model.View;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.dsmn.DsmnResultParser;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.utils.CyUtils;

public class SyncNewTask extends AbstractTask {

	private boolean mergeInCurrent;
	private String cypherURL;
	private String instanceLocation;
	private String auth;
	private CyNetworkFactory cyNetworkFactory;
	private CyNetworkManager cyNetworkMgr;
	private CyNetworkViewManager cyNetworkViewMgr;
	private CyNetworkViewFactory cyNetworkViewFactory;
	private CyLayoutAlgorithmManager cyLayoutAlgorithmMgr;
	private VisualMappingManager visualMappingMgr;
	private Set<String> queryList;

	int chunkSize = 500;

	public SyncNewTask(boolean mergeInCurrent, String cypherURL, String instanceLocation, String auth,
			CyNetworkFactory cyNetworkFactory, CyNetworkManager cyNetworkMgr, CyNetworkViewManager cyNetworkViewMgr,
			CyNetworkViewFactory cyNetworkViewFactory, CyLayoutAlgorithmManager cyLayoutAlgorithmMgr,
			VisualMappingManager visualMappingMgr, Set<String> queryList) {
		super();
		this.mergeInCurrent = mergeInCurrent;
		this.cypherURL = cypherURL;
		this.instanceLocation = instanceLocation;
		this.auth = auth;
		this.cyNetworkFactory = cyNetworkFactory;
		this.cyNetworkMgr = cyNetworkMgr;
		this.cyNetworkViewMgr = cyNetworkViewMgr;
		this.cyNetworkViewFactory = cyNetworkViewFactory;
		this.cyLayoutAlgorithmMgr = cyLayoutAlgorithmMgr;
		this.visualMappingMgr = visualMappingMgr;
		this.queryList = queryList;
	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		if (mergeInCurrent) {

		} else {
			String queryArray = "[";
			boolean first = true;
			for (String s : queryList) {
				if (first) {
					queryArray = queryArray + "'" + s + "'";
					first = false;
				} else
					queryArray = queryArray + "," + "'" + s + "'";
			}
			queryArray = queryArray + "]";

			String neo4jQuery = "match (a),(b), p=allShortestPaths((a)-[:interaction*]->(b)) where  a <> b and a.id in "
					+ queryArray + " and b.id in " + queryArray + " return p";
			String payload = "{ \"query\" : \"" + neo4jQuery + "\",\"params\" : {}}";

			MetaboliteHandler passHandler = new MetaboliteHandler();
			Object responseObj = Request.Post(cypherURL).addHeader("Authorization:", auth)
					.bodyString(payload, ContentType.APPLICATION_JSON).execute().handleResponse(passHandler);

			CyNetwork network = cyNetworkFactory.createNetwork();
			network.getRow(network).set(CyNetwork.NAME, instanceLocation);

			DsmnResultParser cypherParser = new DsmnResultParser(network, auth, taskMonitor);
			cypherParser.parseRetVal(responseObj);

			cyNetworkMgr.addNetwork(network);

			taskMonitor.setStatusMessage("Creating View");
			taskMonitor.setProgress(0.8);

			Collection<CyNetworkView> views = cyNetworkViewMgr.getNetworkViews(network);
			CyNetworkView view;
			if (!views.isEmpty()) {
				view = views.iterator().next();
			} else {
				view = cyNetworkViewFactory.createNetworkView(network);
				cyNetworkViewMgr.addNetworkView(view);
			}

			taskMonitor.setStatusMessage("Applying Layout");
			taskMonitor.setProgress(0.9);

			Set<View<CyNode>> nodes = new HashSet<View<CyNode>>();
			CyLayoutAlgorithm layout = cyLayoutAlgorithmMgr.getLayout("force-directed");

			insertTasksAfterCurrentTask(layout.createTaskIterator(view, layout.createLayoutContext(), nodes, null));

			CyUtils.updateDirectedVisualStyle(visualMappingMgr, view, network);
		}
	}
}