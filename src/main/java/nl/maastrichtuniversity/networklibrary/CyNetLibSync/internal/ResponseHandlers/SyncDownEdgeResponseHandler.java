package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.ResponseHandlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.Plugin;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.utils.CyUtils;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.utils.NeoUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.codehaus.jackson.map.ObjectMapper;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;

public class SyncDownEdgeResponseHandler implements ResponseHandler<Long> {

	private Plugin plugin = null;
	private Long netSUID = null;

	public SyncDownEdgeResponseHandler(Plugin plugin, Long netSUID){
		this.plugin = plugin;
		this.netSUID = netSUID;
	}

	@Override
	public Long handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		int responseCode = response.getStatusLine().getStatusCode();

		Long resNet = null;
		System.out.println("responseCode: " + responseCode);
		if(responseCode >= 200 && responseCode < 300){
			ObjectMapper mapper = new ObjectMapper();
			Map<String,Object> nodes = mapper.readValue(response.getEntity().getContent(), Map.class);

			List<Object> data = (ArrayList<Object>)nodes.get("data");

			if(data.size() > 0){
				
				CyNetwork myNet = getPlugin().getCyNetworkManager().getNetwork(getNetSUID());
				resNet = myNet.getSUID();

				Set<String> attributeCols = new HashSet<String>();
				attributeCols.add("name");
				CyTable defEdgeTab = myNet.getDefaultEdgeTable();
				defEdgeTab.createColumn("neoid", Long.class, false);

				for(Object nodeObj : data){
					
					Map<Object, Object> edge = (Map<Object, Object>)((ArrayList<Object>)nodeObj).get(0);

					String selfURL = (String)edge.get("self");
					Long self = NeoUtils.extractID(selfURL);
					
					String startUrl = (String)edge.get("start");
					Long start = NeoUtils.extractID(startUrl);
					
					String endUrl = (String)edge.get("end");
					Long end = NeoUtils.extractID(endUrl);
					
					String type = (String)edge.get("type");

					Set<CyNode> res = CyUtils.getNodesWithValue(myNet, myNet.getDefaultNodeTable(), "neoid", start);
					if(res.size() > 1){
						throw new IllegalArgumentException("more than one start node found! " + res.toString());
					}
					CyNode startNode = res.iterator().next();
					
					res = CyUtils.getNodesWithValue(myNet, myNet.getDefaultNodeTable(), "neoid", end);
					if(res.size() > 1){
						throw new IllegalArgumentException("more than one end node found! " + res.toString());
					}
					
					CyNode endNode = res.iterator().next();
					
					CyEdge cyEdge = myNet.addEdge(startNode, endNode, true);
					
					myNet.getRow(cyEdge).set("neoid", self);
					myNet.getRow(cyEdge).set(CyEdge.INTERACTION, type);

					Map<String,Object> nodeProps = (Map<String,Object>) edge.get("data");

					for(Entry<String,Object> obj : nodeProps.entrySet()){
						
						if(!attributeCols.contains(obj.getKey())){
							if(obj.getValue().getClass() == ArrayList.class){
								defEdgeTab.createListColumn(obj.getKey(), String.class, true);
							} else {
								defEdgeTab.createColumn(obj.getKey(), obj.getValue().getClass(), true);
							}
							
							attributeCols.add(obj.getKey());
						}
						defEdgeTab.getRow(cyEdge.getSUID()).set(obj.getKey(), obj.getValue());

					}
				}
			}

		} else {
			System.out.println("ERROR " + responseCode);
			ObjectMapper mapper = new ObjectMapper();

			Map<String,String> error = mapper.readValue(response.getEntity().getContent(),Map.class);
			System.out.println(error);

		}

		return resNet;
	}

	protected Plugin getPlugin() {
		return plugin;
	}

	protected Long getNetSUID() {
		return netSUID;
	}

}