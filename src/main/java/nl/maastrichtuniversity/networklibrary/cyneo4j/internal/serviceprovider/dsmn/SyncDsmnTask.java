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

/**
 * Definition of DSMN Cypher queries and handling ID input 
 * @author jmelius
 * @author dslenter
 *
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Paint;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.CyColumn;
import org.cytoscape.model.CyRow;
import org.cytoscape.view.layout.CyLayoutAlgorithm;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.model.View;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.view.presentation.property.NodeShapeVisualProperty; //Lib. used for shapes, e.g. diamond, hexagon, etc.
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualStyle;
import org.cytoscape.view.vizmap.mappings.BoundaryRangeValues;
import org.cytoscape.view.vizmap.mappings.ContinuousMapping;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

import com.fasterxml.jackson.databind.ObjectMapper;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.utils.CyUtils;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.utils.NeoUtils;

//Import Viz Style Class + Cytoscape Registrar service
//import org.cytoscape.service.util.CyServiceRegistrar;
//import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.dsmn.DsmnVizStyle;

public class SyncDsmnTask extends AbstractTask {

	private boolean mergeInCurrent;
	private String cypherURL;
//	private String instanceLocation;
	private String auth;
	private CySwingApplication cySwingApp;
	private CyNetworkFactory cyNetworkFactory;
	private CyNetworkManager cyNetworkMgr;
	private CyNetworkViewManager cyNetworkViewMgr;
	private CyNetworkViewFactory cyNetworkViewFactory;
	private CyLayoutAlgorithmManager cyLayoutAlgorithmMgr;
	private VisualMappingManager visualMappingMgr;
//	private VisualMappingFunctionFactory vmfFactoryP;
//	private VisualMappingFunctionFactory vmfFactoryC;
	private Set<String> queryList;
	private Plugin plugin;
	
	//Variable to add mapping values for ChEBI or HMDB queries:
	 private CyTable table;
	 boolean verbose = true; //set to true when debugging, to false otherwise.

	int chunkSize = 500;
	//private Object continuousMappingFactoryServiceRef;

	public SyncDsmnTask(boolean mergeInCurrent, Plugin plugin, String cypherURL, String instanceLocation, String auth) { //, CyServiceRegistrar vizStyle
		super();
		this.cyNetworkMgr = plugin.getCyNetworkManager();
		this.mergeInCurrent = mergeInCurrent;
		this.cyNetworkFactory = plugin.getCyNetworkFactory();
//		this.instanceLocation = instanceLocation;
		this.cypherURL = cypherURL;
		this.auth = auth;
		this.cyNetworkViewMgr = plugin.getCyNetViewMgr();
		this.cyNetworkViewFactory = plugin.getCyNetworkViewFactory();
		this.cyLayoutAlgorithmMgr = plugin.getCyLayoutAlgorithmManager();
		this.visualMappingMgr = plugin.getVisualMappingManager();
//		this.vmfFactoryP = plugin.getVmfFactoryP();
//		this.vmfFactoryC = plugin.getVmfFactoryC();
		this.queryList = plugin.getQueryList();
		this.cySwingApp = plugin.getCySwingApplication();
		this.plugin = plugin;
		//this.vizStyle =  new DsmnVizStyle(registrar);
	}

	public SyncDsmnTask(boolean mergeInCurrent, String cypherURL, String instanceLocation, String auth,
			CyNetworkFactory cyNetworkFactory, CyNetworkManager cyNetworkMgr, CyNetworkViewManager cyNetworkViewMgr,
			CyNetworkViewFactory cyNetworkViewFactory, CyLayoutAlgorithmManager cyLayoutAlgorithmMgr,
			VisualMappingManager visualMappingMgr, Set<String> queryList, VisualMappingFunctionFactory vmfFactoryP,
			VisualMappingFunctionFactory vmfFactoryC) {
		super();
		this.mergeInCurrent = mergeInCurrent;
		this.cypherURL = cypherURL;
//		this.instanceLocation = instanceLocation;
		this.auth = auth;
		this.cyNetworkFactory = cyNetworkFactory;
		this.cyNetworkMgr = cyNetworkMgr;
		this.cyNetworkViewMgr = cyNetworkViewMgr;
		this.cyNetworkViewFactory = cyNetworkViewFactory;
		this.cyLayoutAlgorithmMgr = cyLayoutAlgorithmMgr;
		this.visualMappingMgr = visualMappingMgr;
//		this.vmfFactoryP = vmfFactoryP;
//		this.vmfFactoryC = vmfFactoryC;
		this.queryList = queryList;

	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		if (mergeInCurrent) {

		} else {
//			double progress = 0.1;
			taskMonitor.setTitle("Directed Small Molecules Network query");
			taskMonitor.setProgress(0.0); // progressBar is later overwritten when data is read in (DsmnResultsParser class) with 0.2.
			taskMonitor.setStatusMessage("Building query");

			// Create list from input, compatible with Cypher.
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

			// Since Wikidata IDs always start with a Q, execute shortest path algorithm without
			// mapping approach:
			if (new String(queryArray).contains("Q")) {

				String neo4jQuery = "MATCH (n:Metabolite) where n.wdID IN  " + queryArray
						+ "  WITH collect(n) as nodes  " + " UNWIND nodes as n " + " UNWIND nodes as m "
						+ " WITH * WHERE n <> m "
						+ " MATCH p = allShortestPaths( (n:Metabolite)-[:AllInteractions*]->(m:Metabolite) ) "
						+ " return p";
				String payload = "{ \"query\" : \"" + neo4jQuery + "\",\"params\" : {}}";

				taskMonitor.setStatusMessage("Downloading nodes and edges");

				// Send query to Neo4j
				DsmnResponseHandler passHandler = new DsmnResponseHandler();
				Object responseObj = Request.Post(cypherURL).addHeader("Authorization:", auth)
						.bodyString(payload, ContentType.APPLICATION_JSON).execute().handleResponse(passHandler);

				// Create a new (empty) network in Cytoscape
				CyNetwork network = cyNetworkFactory.createNetwork();
				// Set name for network as provided in DsmnInputPanel as network title in
				// Cytoscape.
				network.getRow(network).set(CyNetwork.NAME, plugin.getNetworkName());

				taskMonitor.setStatusMessage("Building network (this might take some time)");
				DsmnResultParser cypherParser = new DsmnResultParser(network, auth, taskMonitor);
				cypherParser.parseRetVal(responseObj);

				// Add data obtained from query to Cytoscape
				cyNetworkMgr.addNetwork(network);
				taskMonitor.setProgress(0.5);

				taskMonitor.setStatusMessage("Creating View");
				taskMonitor.setProgress(0.6);

				// Obtain view of network in Cytoscape
				Collection<CyNetworkView> views = cyNetworkViewMgr.getNetworkViews(network);
				CyNetworkView view = null; // empty view option, so "view" object is empty again.

				// Create basic view
				if (!views.isEmpty()) {
					view = views.iterator().next();
				} else {
					view = cyNetworkViewFactory.createNetworkView(network);
					cyNetworkViewMgr.addNetworkView(view);

				}

				// //Bypass certain view elements with data from Neo4j:
					// Use count property for data visualization on edges
				    int max = 15;
					double min = 1.0;
					for (View<CyEdge> v : view.getEdgeViews()) {
						v.setLockedValue(BasicVisualLexicon.EDGE_LABEL, ""); // Makes sure no label is visualized on
																				// interaction itself.
						
						Integer occ = (Integer) network.getRow(v.getModel()).getAllValues().get("count"); 
						//In case count is 0, or not available (both should not be the case when only querying metabolic reactions!).
						if (occ == 0 | occ == null ) {
						v.setLockedValue(BasicVisualLexicon.EDGE_WIDTH, min); // Set default value of edge width to 1.0, if not present or not available/null
						}
						
						//setLockedValue EDGE_WIDTH needs to be defined as a double, however count column is an integer.						
						if (occ > max)
							occ = max; // Update value for max, to not get to thick edges
						double setting = occ * 2;
						v.setLockedValue(BasicVisualLexicon.EDGE_WIDTH, setting); //replace EDGE_WIDTH with bypass value.
						
					}
					
				// Use wdID property for coloring queried IDs red, and keep track of which IDs
				// are not part of shortest path.
				Set<String> notInResultNames = new HashSet<String>();
				Set<String> notInDataseNames = new HashSet<String>();
				Set<String> presentNames = new HashSet<String>();
				for (View<CyNode> v : view.getNodeViews()) {
					String name = (String)network.getRow(v.getModel()).getAllValues().get("wdID");
					//System.out.print(name);
					if (queryList.contains(name)) {
						v.setLockedValue(BasicVisualLexicon.NODE_FILL_COLOR, Color.ORANGE); // color all queried markers red

						double num = 100;
						v.setLockedValue(BasicVisualLexicon.NODE_SIZE, num); // Make queried nodes larger
						presentNames.add(name);

					} else {
						notInResultNames.add(name); // Not in shortest path result
					}

				}
				
				
				 //Add visualization for reaction nodes 
				for (View<CyNode> v : view.getNodeViews()) {
					String reaction = (String)network.getRow(v.getModel()).getAllValues().get("rwID");
					if (reaction != null) { //skip all entries which are null (Metabolites & Proteins do not have an rwID.
						double num=15;
						v.setLockedValue(BasicVisualLexicon.NODE_SIZE, num); //Make reaction nodes small 
						v.setLockedValue(BasicVisualLexicon.NODE_SHAPE, NodeShapeVisualProperty.RECTANGLE); //Set shape to rectangle
						v.setLockedValue(BasicVisualLexicon.NODE_LABEL, ""); // Makes sure no label is visualized on
						// reaction Nodes itself.
					}
					
				}
				 
				queryList.removeAll(presentNames);
				// Query only wdIDs from querylist who are not in results (aka presentNames) to
				// check if they're even present in Neo4j database
				for (String name : queryList) {
					String query = "MATCH (n:Metabolite) where n.wdID = '" + name + "' RETURN n";
					payload = "{ \"query\" : \"" + query + "\",\"params\" : {}}";

					Response response = Request.Post(cypherURL).addHeader("Authorization:", auth)
							.bodyString(payload, ContentType.APPLICATION_JSON).execute();

					ObjectMapper mapper = new ObjectMapper();
					Map<String, Object> retVal = (Map<String, Object>) mapper
							.readValue(response.returnResponse().getEntity().getContent(), Map.class);

					List list = (List<List<Object>>) retVal.get("data");

					if (list.isEmpty())
						notInDataseNames.add(name);
				}
				queryList.removeAll(notInDataseNames);

				taskMonitor.setStatusMessage("Update Dsmn Result Panel");
				taskMonitor.setProgress(0.8);

				plugin.setIds(new DsmnResultsIds(notInDataseNames, queryList, presentNames));
				createDsmnResultPanel();

				taskMonitor.setStatusMessage("Applying Layout");
				taskMonitor.setProgress(0.9);

				Set<View<CyNode>> nodes = new HashSet<View<CyNode>>();
				CyLayoutAlgorithm layout = cyLayoutAlgorithmMgr.getLayout("force-directed");

				insertTasksAfterCurrentTask(layout.createTaskIterator(view, layout.createLayoutContext(), nodes, null));

				CyUtils.updateDirectedVisualStyle(visualMappingMgr, view, network);

				// Obtain the current network style
				VisualStyle currentVisualStyle = visualMappingMgr.getCurrentVisualStyle();

				// Apply the previously described visualization styles (view) to the current
				// network style.
				currentVisualStyle.apply(view);
				view.updateView();
			}

			// IDs which do not start with a Q (ChEBI and HMDB), execute shortest path
			// algorithm WITH mapping approach:
			else {

				String neo4jQuery = " WITH " + queryArray + " AS coll " + " UNWIND coll AS y " + " MATCH (a:Mapping) "
						+ " WHERE single(x IN a.mappingIDs WHERE x = y) " + " WITH DISTINCT a, y " + " MATCH (a) "
						+ " WITH [(a)-[:MappingInteractions*..1]->(b) WHERE b:Metabolite | b.wdID] AS MappedTo "
						+ " UNWIND MappedTo as c " + " WITH collect(c) as List "
						+ " MATCH (n:Metabolite) where n.wdID IN List WITH collect(n) as nodes " + " UNWIND nodes as n "
						+ " UNWIND nodes as m " + " WITH * WHERE n <> m "
						+ " MATCH p = allShortestPaths( (n)-[:AllInteractions|:AllCatalysis*]->(m) ) " + " return p";
				String payload = "{ \"query\" : \"" + neo4jQuery + "\",\"params\" : {}}";

				taskMonitor.setStatusMessage("Downloading nodes");

				DsmnResponseHandler passHandler = new DsmnResponseHandler();
				Object responseObj = Request.Post(cypherURL).addHeader("Authorization:", auth)
						.bodyString(payload, ContentType.APPLICATION_JSON).execute().handleResponse(passHandler);

				CyNetwork network = cyNetworkFactory.createNetwork();
				network.getRow(network).set(CyNetwork.NAME, plugin.getNetworkName());

				taskMonitor.setStatusMessage("Building network (this might take some time)");
				DsmnResultParser cypherParser = new DsmnResultParser(network, auth, taskMonitor);
				cypherParser.parseRetVal(responseObj);

				cyNetworkMgr.addNetwork(network);
				taskMonitor.setProgress(0.5);

				taskMonitor.setProgress(0.6);
				taskMonitor.setStatusMessage("Creating View");

				Collection<CyNetworkView> views = cyNetworkViewMgr.getNetworkViews(network);
				CyNetworkView view = null;

				if (!views.isEmpty()) {
					view = views.iterator().next();
				} else {
					view = cyNetworkViewFactory.createNetworkView(network);
					cyNetworkViewMgr.addNetworkView(view);

				}
				// Use count property for data visualization on edges
			    int max = 15;
				double min = 1.0;
				for (View<CyEdge> v : view.getEdgeViews()) {
					v.setLockedValue(BasicVisualLexicon.EDGE_LABEL, ""); // Makes sure no label is visualized on
																						// interaction itself.
					//v.setLockedValue(BasicVisualLexicon.EDGE_LABEL_FONT_SIZE, 1);	//To surpass large labels, if byPass is not registered...		
					Integer occ = (Integer) network.getRow(v.getModel()).getAllValues().get("count"); 
					//In case count is 0, or not available (both should not be the case when only querying metabolic reactions!).
					if (occ == 0 | occ == null ) {
						v.setLockedValue(BasicVisualLexicon.EDGE_WIDTH, min); // Set default value of edge width to 1.0, if not present or not available/null
						}
								
					//setLockedValue EDGE_WIDTH needs to be defined as a double, however count column is an integer.						
					if (occ > max)
						occ = max; // Update value for max, to not get to thick edges
					double setting = occ * 2;
					v.setLockedValue(BasicVisualLexicon.EDGE_WIDTH, setting); //replace EDGE_WIDTH with bypass value.
								
					}
								
				 //Add visualization for reaction nodes 
				for (View<CyNode> v : view.getNodeViews()) {
					String reaction = (String)network.getRow(v.getModel()).getAllValues().get("rwID");
					if (reaction != null) { //skip all entries which are null (Metabolites & Proteins do not have an rwID.
						double num=15;
						v.setLockedValue(BasicVisualLexicon.NODE_SIZE, num); //Make reaction nodes small 
						v.setLockedValue(BasicVisualLexicon.NODE_SHAPE, NodeShapeVisualProperty.RECTANGLE); //Set shape to rectangle
						v.setLockedValue(BasicVisualLexicon.NODE_LABEL, ""); // Makes sure no label is visualized on
						// reaction Nodes itself.
					}
					
				}
				
				//Query ChEBI-Wikidata mappings for coloring and results panel
				// Use wdID property for coloring queried IDs red, and keep track of which IDs
				// are not part of shortest path.
				// Match WD ID, add ChEBI ID in column "mappedID"
				Set<String> notInResultNames = new HashSet<String>();
				Set<String> notInDataseNames = new HashSet<String>();
				Set<String> presentNames = new HashSet<String>();
				List list = new ArrayList<Object>();
				List<String> combinedList = new ArrayList<String>();
				Map<String, String> result = new HashMap<String, String>();
				
				// Query only wdIDs from querylist who are not in results (aka presentNames) to
				// check if they're even present in Neo4j database
				for (String name : queryList) {
					String query = "MATCH (a:Mapping) where a.mappingIDs = '" + name + "' WITH [(a)-[:MappingInteractions*..1]->(b) WHERE b:Metabolite | b.wdID] AS MappedTo, a UNWIND MappedTo as c RETURN a.mappingIDs, c";
					payload = "{ \"query\" : \"" + query + "\",\"params\" : {}}";

					Response response = Request.Post(cypherURL).addHeader("Authorization:", auth)
							.bodyString(payload, ContentType.APPLICATION_JSON).execute();

					ObjectMapper mapperMissing = new ObjectMapper();
					Map<String, Object> retVal = (Map<String, Object>) mapperMissing.readValue(response.returnResponse().getEntity().getContent(), Map.class);
											
					//Store mapped values only
					list = (List<Object>) retVal.get("data");
					if (list.isEmpty()){notInDataseNames.add(name);}
					else{presentNames.add(name);}

					for(int i = 0; i < list.size(); i++)
					{
						combinedList.add(list.get(i).toString());
						for (View<CyNode> v : view.getNodeViews()) {
							String id = (String) network.getRow(v.getModel()).getAllValues().get("wdID");
							if (list.get(i) != null && id != null && list.get(i).toString().contains(id)) {
								v.setLockedValue(BasicVisualLexicon.NODE_FILL_COLOR, Color.ORANGE); // color all queried markers red
								double num = 100;
								v.setLockedValue(BasicVisualLexicon.NODE_SIZE, num); // Make queried nodes larger
							} 
						}
					}
				
				}
			
				//For each value in the combined list, add the first entry (everything before the comma, without whitespaces) as a key, add the second entry (everything after the comma, without whitespaces) as a value.
				for (int i = 0; i < combinedList.size(); ++i) { //loop over each value in the combined list
					String last_entry = combinedList.set(i, combinedList.get(i)).substring(combinedList.set(i, combinedList.get(i)).lastIndexOf(',') + 1).replaceAll("\\]+", "").trim(); //Wikidata ID
					String first_entry = combinedList.get(i).substring(0, combinedList.get(i).indexOf(",")).replaceAll("\\[+", "").trim(); //ChEBI ID
					result.put(first_entry, last_entry); //Combine Wikidata and ChEBI IDs in hashMap, to add to new column in Cytoscape later on.
		        }
				//System.out.print(result);
				for (String key: result.keySet()){
		            if ((result.get(key)).equals(" Q4545703")){
		            	System.out.println(key  +" :: "+ result.get(key));
		            }
				}
				
				//Add a column to store the mapped IDs, to be able to add the mapped data later:
				CyTable defNodeTab = network.getDefaultNodeTable();
				if (defNodeTab.getColumn("mappedID") == null) {
					defNodeTab.createColumn("mappedID", Long.class, false);
				}

				//	CyRow row = table.getRow("Q2629377"); //->TODO nullPointerException
			//		if (verbose) System.out.println("Testing matching against wdID: Q2629377, found in row: " + row);		
				
	
			//	if (verbose) System.out.println( "Adding Wikidata-ChEBI IDs: " + result);
			//	if (result.size() > 0)
			//	{
					//for(View<CyNode> v : view.getNodeViews()) //->TODO works, but cannot update Table
				//	for (CyRow row : table.getAllRows()) //->TODO nullPointerException
				//	{
						
						//String id = (String) network.getRow(v.getModel()).getAllValues().get("wdID"); //->TODO works, but cannot update Table
						/*
						 * String id = row.get("name", String.class); //get("wdID"); if (id == null) //->TODO nullPointerException
						 * continue; for (String key: result.keySet()){ if
						 * ((result.get(key)).equals(id)){ //row.set("mappedID", key);
						 * System.out.println(key +" :: "+ result.get(key)); } }
						 */
				//	}
			//	}
				
				queryList.removeAll(notInDataseNames);
				queryList.removeAll(presentNames);

				taskMonitor.setStatusMessage("Update Dsmn Result Panel");
				taskMonitor.setProgress(0.8);

				plugin.setIds(new DsmnResultsIds(notInDataseNames, queryList, presentNames));
				createDsmnResultPanel();

				taskMonitor.setStatusMessage("Applying Layout");
				taskMonitor.setProgress(0.9);

				Set<View<CyNode>> nodes = new HashSet<View<CyNode>>();
				CyLayoutAlgorithm layout = cyLayoutAlgorithmMgr.getLayout("force-directed");

				insertTasksAfterCurrentTask(layout.createTaskIterator(view, layout.createLayoutContext(), nodes, null));

				CyUtils.updateDirectedVisualStyle(visualMappingMgr, view, network);

				VisualStyle currentVisualStyle = visualMappingMgr.getCurrentVisualStyle();

				currentVisualStyle.apply(view);
				view.updateView();
			}

		}
	}

	
	  private void createDsmnResultPanel() { DsmnResultPanel resultPanel =
	  plugin.getResultPanel(); JTabbedPane tabPanel = resultPanel.getTabbedPane();
	  
	  JPanel panel = new JPanel();
	  
	  BorderLayout layout = new BorderLayout(); DsmnResultsIds ids =
	  plugin.getIds();
	  
	  JTextArea textArea = new JTextArea(); textArea.setColumns(20);
	  textArea.setLineWrap(true); textArea.setRows(5);
	  textArea.setWrapStyleWord(true); textArea.setEditable(false);
	  textArea.setSize(600, 800); textArea.setText("Dmsn result analysis:\n\n" +
	  "IDs present in the query: " + ids.getPresentNames() + "\n\n" +
	  "IDs not connected through shortest path: " + ids.getNotInDatabase() + "\n\n" +
	  "IDs not present in the graph database: " + ids.getNotInResult());

	  layout.addLayoutComponent(textArea, BorderLayout.CENTER);
	  
	  panel.add(textArea); panel.setLayout(layout); panel.setOpaque(true);
	  
	  panel.setVisible(true); tabPanel.addTab(plugin.getNetworkName(), panel); }
	 
}
