package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.Plugin;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.Extension;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.ExtensionExecutor;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider.Neo4jCall;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.utils.CyUtils;

public class NeoNetworkAnalyzerExec implements ExtensionExecutor {

	private Plugin plugin;
	private Extension extension;
	private CyNetwork currNet;

	public NeoNetworkAnalyzerExec() {

	}

	@Override
	public boolean collectParameters() {
		currNet = getPlugin().getCyApplicationManager().getCurrentNetwork();
		return true;
	}

	@Override
	public void processCallResponse(Neo4jCall call, Object callRetValue) {

		List<Object> allStats = (List<Object>)callRetValue;

		ObjectMapper mapper = new ObjectMapper();
		CyTable defNodeTab = currNet.getDefaultNodeTable();
		
		try {
			for(Object obj : allStats){
				Map<String, Object> stats = mapper.readValue((String)obj, Map.class);
				Long neoid = ((Integer)stats.get("nodeid")).longValue();
				
				System.out.println(stats);
				System.out.println("working on node: " +neoid);
				
				Set<CyNode> nodeSet = CyUtils.getNodesWithValue(currNet, defNodeTab, "neoid", neoid);
				CyNode n = nodeSet.iterator().next();
				
				for(Entry<String,Object> e : stats.entrySet()){

					if(e.getKey().equals("neo_name") || e.getKey().equals("nodeid")){
						continue;
					}

					addValue(n,defNodeTab,e.getKey(),e.getValue());
					
				}
				System.out.println("\n");
			}

		} catch (JsonParseException e1) {
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void addValue(CyNode n, CyTable defNodeTab, String key, Object value) {
		if(defNodeTab.getColumn(key) == null){
			defNodeTab.createColumn(key, value.getClass(), false);
		}
		
		defNodeTab.getRow(n.getSUID()).set(key, value);
	}

	@Override
	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void setExtension(Extension extension) {
		this.extension = extension;
	}

	@Override
	public List<Neo4jCall> buildNeo4jCalls() {
		List<Neo4jCall> calls = new ArrayList<Neo4jCall>();

		String urlFragment = extension.getEndpoint();
		String payload = "{}";

		calls.add(new Neo4jCall(urlFragment,payload));

		return calls;
	}

	protected Plugin getPlugin() {
		return plugin;
	}
}
