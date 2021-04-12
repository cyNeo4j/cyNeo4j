package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.dsmn;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Paint;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.utils.CyUtils;

import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.codehaus.jackson.map.ObjectMapper;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.CyEdge;
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
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.view.presentation.property.PaintVisualProperty;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualStyle;
import org.cytoscape.view.vizmap.mappings.BoundaryRangeValues;
import org.cytoscape.view.vizmap.mappings.ContinuousMapping;
import org.cytoscape.view.vizmap.mappings.PassthroughMapping;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

public class SyncDsmnTask extends AbstractTask{

	private boolean mergeInCurrent;
	private String cypherURL;
	private String instanceLocation;
	private String auth;
	private CySwingApplication cySwingApp;
	private CyNetworkFactory cyNetworkFactory;
	private CyNetworkManager cyNetworkMgr;
	private CyNetworkViewManager cyNetworkViewMgr;
	private CyNetworkViewFactory cyNetworkViewFactory;
	private CyLayoutAlgorithmManager cyLayoutAlgorithmMgr;
	private VisualMappingManager visualMappingMgr;
	private VisualMappingFunctionFactory vmfFactoryP;
	private VisualMappingFunctionFactory vmfFactoryC;
	private Set<String> queryList;
	private Plugin plugin;

	int chunkSize = 500;
	
	public SyncDsmnTask(
			boolean mergeInCurrent, Plugin plugin,
			String cypherURL, String instanceLocation,
			String auth) {
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
	}
	
	public SyncDsmnTask(boolean mergeInCurrent, String cypherURL,
			String instanceLocation, 
			String auth,
			CyNetworkFactory cyNetworkFactory,
			CyNetworkManager cyNetworkMgr,
			CyNetworkViewManager cyNetworkViewMgr,
			CyNetworkViewFactory cyNetworkViewFactory,
			CyLayoutAlgorithmManager cyLayoutAlgorithmMgr,
			VisualMappingManager visualMappingMgr,
			Set<String> queryList,
			VisualMappingFunctionFactory vmfFactoryP,
			VisualMappingFunctionFactory vmfFactoryC) {
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
		this.vmfFactoryP = vmfFactoryP;
		this.vmfFactoryC = vmfFactoryC;
		this.queryList = queryList;
		
	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		if(mergeInCurrent){

		} 
		else {
//			double progress = 0.1;
			taskMonitor.setTitle("Directed Small Molecules Network query");
			taskMonitor.setProgress(0.0); //progressBar is later overwritten when data is read in (DsmnResultsParser class, L70) with 0.2.
			taskMonitor.setStatusMessage("Building query"); 
			
			
			//Create list from input, compatible with Cypher.
			String queryArray = "[";
			boolean first = true;
			for(String s : queryList) {
				if(first) {
					queryArray = queryArray + "'" + s + "'"; first = false;
				} else queryArray = queryArray + "," + "'" + s + "'";
			}
			queryArray = queryArray + "]";
			
			//Since Wikidata IDs start with a Q, execute shortest path algorithm without mapping approach:
			if(new String(queryArray).contains("Q")) { 
			
			String neo4jQuery = "MATCH (n:Metabolite) where n.wdID IN  " + queryArray 
					+ "  WITH collect(n) as nodes  " 
					+ " UNWIND nodes as n " 
					+ " UNWIND nodes as m " 
					+ " WITH * WHERE n <> m " 
					+ " MATCH p = allShortestPaths( (n:Metabolite)-[:AllInteractions*]->(m:Metabolite) ) "  
					+ " return p";
			String payload = "{ \"query\" : \""+neo4jQuery+"\",\"params\" : {}}";
			
			taskMonitor.setStatusMessage("Downloading nodes");

			DsmnResponseHandler passHandler = new DsmnResponseHandler();			
			Object responseObj = Request.Post(cypherURL).
										addHeader("Authorization:", auth).
										bodyString(payload, ContentType.APPLICATION_JSON).
										execute().handleResponse(passHandler);
			
			CyNetwork network = cyNetworkFactory.createNetwork();
			network.getRow(network).set(CyNetwork.NAME,plugin.getNetworkName());
					
			taskMonitor.setStatusMessage("Building network (this might take some time)");
			DsmnResultParser cypherParser = new DsmnResultParser(network,auth,taskMonitor);
			cypherParser.parseRetVal(responseObj);
			
			cyNetworkMgr.addNetwork(network);
			taskMonitor.setProgress(0.5);
			
			taskMonitor.setStatusMessage("Creating View");
			taskMonitor.setProgress(0.6);
			
			
			Collection<CyNetworkView> views = cyNetworkViewMgr.getNetworkViews(network);
			CyNetworkView view = null;
			
			if(!views.isEmpty()) {
				view = views.iterator().next();
			} else {
				view = cyNetworkViewFactory.createNetworkView(network);
				cyNetworkViewMgr.addNetworkView(view);
				
			}	
			//Use count property for data visualisation on edges
			int max = 0;
			for ( View<CyEdge> v : view.getEdgeViews()){
				int occ = (Integer) network.getRow(v.getModel()).getAllValues().get("count");
				if (occ>max)max=occ;
				v.setLockedValue(BasicVisualLexicon.EDGE_LABEL, "");		
			}
			//Use wdID property for colouring queried IDs red, and keep track of which IDs are not part of shortest path.
			Set<String> notInResultNames = new HashSet<String>();
			//TODO: add side metabolite set; also add in results panel.
			Set<String> notInDataseNames = new HashSet<String>();
			Set<String> presentNames = new HashSet<String>();
			for ( View<CyNode> v : view.getNodeViews()){
				String name = (String) network.getRow(v.getModel()).getAllValues().get("wdID");
				if (queryList.contains(name)){
					v.setLockedValue(BasicVisualLexicon.NODE_FILL_COLOR, Color.red);
					presentNames.add(name);
				}
				else{
					notInResultNames.add(name);			//Not in shortest path result	
				}
			}
			
			queryList.removeAll(presentNames);
			//Query only wdIDs from querylist who are not in results (aka presentNames) to check if they're even present in Neo4j database
			for (String name : queryList){
				String query = "MATCH (n:Metabolite) where n.id = '"+ name +"' RETURN n";
				payload = "{ \"query\" : \""+query+"\",\"params\" : {}}";
				
				 Response response = Request.Post(cypherURL).
						addHeader("Authorization:", auth).
						bodyString(payload,ContentType.APPLICATION_JSON).
						execute();
				 
				 ObjectMapper mapper = new ObjectMapper();
				 Map<String,Object> retVal = 
						 (Map<String,Object>)mapper.readValue(response.returnResponse().
								 getEntity().getContent(), Map.class);
				 
				 List list = (List<List<Object>>)retVal.get("data");
				 
				 if (list.isEmpty())
					 notInDataseNames.add(name);
			}
			queryList.removeAll(notInDataseNames);
			
			
			taskMonitor.setStatusMessage("Update Dsmn Result Panel");
			taskMonitor.setProgress(0.8);
			
			plugin.setIds(new DsmnResultsIds(notInDataseNames, queryList , presentNames));
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
			
			//IDs which do not start with a Q (ChEBI and HMDB), execute shortest path algorithm WITH mapping approach:
			else { 
					
					String neo4jQuery = " WITH " + queryArray + " AS coll " 
							+ " UNWIND coll AS y " 
							+ " MATCH (a:Mapping) " 
							+ " WHERE single(x IN a.mappingIDs WHERE x = y) " 
							+ " WITH DISTINCT a, y " 
							+ " MATCH (a) " 
							+ " WITH [(a)-[:MappingInteractions*..1]->(b) WHERE b:Metabolite | b.wdID] AS MappedTo " 
							+ " UNWIND MappedTo as c " 
							+ " WITH collect(c) as List " 
							+ " MATCH (n:Metabolite) where n.wdID IN List WITH collect(n) as nodes " 
							+ " UNWIND nodes as n " 
							+ " UNWIND nodes as m " 
							+ " WITH * WHERE n <> m " 
							+ " MATCH p = allShortestPaths( (n)-[:AllInteractions|:AllCatalysis*]->(m) ) "   
							+ " return p";
					String payload = "{ \"query\" : \""+neo4jQuery+"\",\"params\" : {}}";
					
					taskMonitor.setStatusMessage("Downloading nodes");

					DsmnResponseHandler passHandler = new DsmnResponseHandler();			
					Object responseObj = Request.Post(cypherURL).
												addHeader("Authorization:", auth).
												bodyString(payload, ContentType.APPLICATION_JSON).
												execute().handleResponse(passHandler);
					
					CyNetwork network = cyNetworkFactory.createNetwork();
					network.getRow(network).set(CyNetwork.NAME,plugin.getNetworkName());
	
					taskMonitor.setStatusMessage("Building network (this might take some time)");
					DsmnResultParser cypherParser = new DsmnResultParser(network,auth,taskMonitor);
					cypherParser.parseRetVal(responseObj);
					
					cyNetworkMgr.addNetwork(network);
					taskMonitor.setProgress(0.5);
					
					taskMonitor.setProgress(0.6);
					taskMonitor.setStatusMessage("Creating View");
					
					
					Collection<CyNetworkView> views = cyNetworkViewMgr.getNetworkViews(network);
					CyNetworkView view = null;
					
					if(!views.isEmpty()) {
						view = views.iterator().next();
					} else {
						view = cyNetworkViewFactory.createNetworkView(network);
						cyNetworkViewMgr.addNetworkView(view);
						
					}	
					//Use count property for data visualisation on edges
					int max = 0;
					for ( View<CyEdge> v : view.getEdgeViews()){
						int occ = (Integer) network.getRow(v.getModel()).getAllValues().get("count");
						if (occ>max)max=occ;
						v.setLockedValue(BasicVisualLexicon.EDGE_LABEL, "");		
					}
					//Use wdID property for colouring queried IDs red, and keep track of which IDs are not part of shortest path.
					Set<String> notInResultNames = new HashSet<String>();
					//TODO: add side metabolite set; also add in results panel.
					Set<String> notInDataseNames = new HashSet<String>();
					Set<String> presentNames = new HashSet<String>();
					for ( View<CyNode> v : view.getNodeViews()){
						String name = (String) network.getRow(v.getModel()).getAllValues().get("wdID");
						if (queryList.contains(name)){
							v.setLockedValue(BasicVisualLexicon.NODE_FILL_COLOR, Color.red);
							presentNames.add(name);
						}
						else{
							notInResultNames.add(name);			//Not in shortest path result	
						}
					}
					
					queryList.removeAll(presentNames);
					//Query only wdIDs from querylist who are not in results (aka presentNames) to check if they're even present in Neo4j database
					for (String name : queryList){
						String query = "MATCH (n:Metabolite) where n.id = '"+ name +"' RETURN n";
						payload = "{ \"query\" : \""+query+"\",\"params\" : {}}";
						
						 Response response = Request.Post(cypherURL).
								addHeader("Authorization:", auth).
								bodyString(payload,ContentType.APPLICATION_JSON).
								execute();
						 
						 ObjectMapper mapper = new ObjectMapper();
						 Map<String,Object> retVal = 
								 (Map<String,Object>)mapper.readValue(response.returnResponse().
										 getEntity().getContent(), Map.class);
						 
						 List list = (List<List<Object>>)retVal.get("data");
						 
						 if (list.isEmpty())
							 notInDataseNames.add(name);
					}
					queryList.removeAll(notInDataseNames);
					
					
					taskMonitor.setStatusMessage("Update Dsmn Result Panel");
					taskMonitor.setProgress(0.8);
					
					plugin.setIds(new DsmnResultsIds(notInDataseNames, queryList , presentNames));
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
	
	private void createDsmnResultPanel(){
		DsmnResultPanel resultPanel = plugin.getResultPanel();
		JTabbedPane tabPanel = resultPanel.getTabbedPane();

		JPanel panel = new JPanel();
		
		BorderLayout layout = new BorderLayout();		
		DsmnResultsIds ids = plugin.getIds();		
		
		JTextArea textArea = new JTextArea();
        textArea.setColumns(20);
        textArea.setLineWrap(true);
        textArea.setRows(5);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false); 
        textArea.setSize(600, 800);
        textArea.setText("Dmsn result analysis:\n\n"
        		+ "IDs present in the query: "+ ids.getPresentNames()+ "\n\n"
				+ "Not connected through shortest path: "+ ids.getNotInResult()+ "\n\n"
				+ "IDs not in the database: "+ ids.getNotInDatabase()
				); 
        
        layout.addLayoutComponent(textArea, BorderLayout.CENTER);
        
        panel.add(textArea);
        panel.setLayout(layout);
        panel.setOpaque(true);

		panel.setVisible(true);
		tabPanel.addTab(plugin.getNetworkName(), panel);
	}
}