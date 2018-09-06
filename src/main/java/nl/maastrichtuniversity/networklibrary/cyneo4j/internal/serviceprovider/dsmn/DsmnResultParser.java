package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.dsmn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.utils.CyUtils;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.utils.NeoUtils;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.codehaus.jackson.map.ObjectMapper;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.work.TaskMonitor;

public class DsmnResultParser {

	private static final String NODE_KEY = "outgoing_relationships";
	private static final String EDGE_KEY = "type";

	protected List<String> cols;
	protected Map<String,ResType> colType = new HashMap<String,ResType>();
	protected CyNetwork currNet;

	protected long numNodes;
	protected long numEdges;
	TaskMonitor taskMonitor;
	
	
	String auth = new String(Base64.encodeBase64(("neo4j" + ":" + "dsmn").getBytes()));
	
	public DsmnResultParser(CyNetwork network, String auth, TaskMonitor taskMonitor){
		this.currNet = network;
		this.auth = auth;
		this.taskMonitor = taskMonitor;

		numNodes = 0;
		numEdges = 0;
	}

	public void parseRetVal(Object callRetValue) throws ClientProtocolException, IOException{
		Map<String,Object> retVal = (Map<String,Object>)callRetValue;
		readColumns((List<String>)retVal.get("columns"));
		readResultTable((List<List<Object>>)retVal.get("data"));
	}

	protected void readColumns(List<String> columns){
		cols = columns;
		for(String col : cols){
			colType.put(col,ResType.Unknown);
		}
	}

	protected void readResultTable(List<List<Object>> rows) throws ClientProtocolException, IOException{		
		for(int r = 0; r < rows.size(); ++r){
			List<Object> row = rows.get(r);

			for(int i = 0; i < row.size(); ++i){
				Object item = row.get(i);
				LinkedHashMap<Object,Object> map = (LinkedHashMap<Object,Object>)item;
				ArrayList<String> nodes = (ArrayList<String>) map.get("relationships");
				taskMonitor.setProgress( i );

				for ( String s : nodes){					
					Response responseObj = Request.Get(s).addHeader("Authorization:", auth).execute();
					
					ObjectMapper mapper = new ObjectMapper();
					Map<String,Object> retVal = mapper.readValue(responseObj.returnResponse().getEntity().getContent(), Map.class);
					
					String col = cols.get(i);				
					
					responseObj = Request.Get(retVal.get("start").toString()).addHeader("Authorization:", auth).execute();					
					mapper = new ObjectMapper();
					Map<String,Object> retVal2 = mapper.readValue(responseObj.returnResponse().getEntity().getContent(), Map.class);			
					String name = parseNode(retVal2,col);
			
					responseObj = Request.Get(retVal.get("end").toString()).addHeader("Authorization:", auth).execute();			
					mapper = new ObjectMapper();
					Map<String,Object> retVal3 = mapper.readValue(responseObj.returnResponse().getEntity().getContent(), Map.class);
					parseNode(retVal3,col);
					
					parseEdge(retVal,col,name);
				}
			}
		}
	}

	public String parseNode(Object nodeObj, String column){
		String name = "";
		CyTable defNodeTab = currNet.getDefaultNodeTable();
		if(defNodeTab.getColumn("neoid") == null){
			defNodeTab.createColumn("neoid", Long.class, false);
		}

		Map<String,Object> node = (Map<String,Object>)nodeObj;

		String selfURL = (String)node.get("self");
		Long self = Long.valueOf(NeoUtils.extractID((String)node.get("self")));

		CyNode cyNode = CyUtils.getNodeByNeoId(currNet, self);

		if(cyNode == null){
			cyNode = currNet.addNode();
			currNet.getRow(cyNode).set("neoid", self);
			Map<String,String>  data = (Map<String, String>) node.get("data");
			name = data.get("id");
			currNet.getRow(cyNode).set("name", name);			
			++numNodes;
		}
		Map<String,Object> nodeProps = (Map<String,Object>) node.get("data");
		
		CyUtils.addProperties(cyNode.getSUID(), defNodeTab, nodeProps);	
		Map<String, Object> metadata = (Map<String,Object>) node.get("metadata");
		
		CyUtils.addProperties(cyNode.getSUID(), defNodeTab, metadata);
		return name;
	}

	public void parseEdge(Object edgeObj, String column, String name){

		CyTable defEdgeTab = currNet.getDefaultEdgeTable();
		if(defEdgeTab.getColumn("neoid") == null){
			defEdgeTab.createColumn("neoid", Long.class, false);
		}

		CyTable defNodeTab = currNet.getDefaultNodeTable();
		if(defNodeTab.getColumn("neoid") == null){
			defNodeTab.createColumn("neoid", Long.class, false);
		}

		Map<Object, Object> edge = (Map<Object, Object>)edgeObj;

		String selfURL = (String)edge.get("self");
		Long self = NeoUtils.extractID(selfURL);

		CyEdge cyEdge = CyUtils.getEdgeByNeoId(currNet, self);

		if(cyEdge == null){

			String startUrl = (String)edge.get("start");
			Long start = NeoUtils.extractID(startUrl);

			String endUrl = (String)edge.get("end");
			Long end = NeoUtils.extractID(endUrl);

			String type = (String)edge.get("type");

			CyNode startNode = CyUtils.getNodeByNeoId(currNet, start);
			CyNode endNode = CyUtils.getNodeByNeoId(currNet, end);

			if(startNode == null){
				startNode = currNet.addNode();
				currNet.getRow(startNode).set("neoid", start);
				
			}

			if(endNode == null){
				endNode = currNet.addNode();
				currNet.getRow(endNode).set("neoid", end);
			}
			name = (String) currNet.getRow(startNode).getAllValues().get("name");
			cyEdge = currNet.addEdge(startNode, endNode, true);
			++numEdges;

			currNet.getRow(cyEdge).set("neoid", self);
			currNet.getRow(cyEdge).set("name", name);	
			currNet.getRow(cyEdge).set(CyEdge.INTERACTION, type);

			Map<String,Object> edgeProps = (Map<String,Object>) edge.get("data");

			CyUtils.addProperties(cyEdge.getSUID(), defEdgeTab, edgeProps);
		}
	}

	public long nodesAdded(){
		return numNodes;
	}

	public long edgesAdded() {
		return numEdges;
	}

	protected enum ResType {
		Node,
		Edge,
		Ignore,
		Unknown
	}


}
