package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensions.specialized;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensions.Extension;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.utils.CyUtils;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.utils.Neo4jCall;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.utils.NeoUtils;

import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;

public class ShortestPathExtExec implements ExtensionExecutor {

	private Extension extension;
	private CyNode from;
	private CyNode to;
	private Integer depth;
	
	@Override
	public void collectParameters() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Neo4jCall buildNeo4jCall(String instanceLocation) {
//		curl -X POST http://localhost:7474/db/data/ext/ShortestPath/node/1311/shortestPath -H "Content-Type: application/json" -d '{"target":"http://localhost:7474/db/data/node/1315","depth":5}'
		String urlFragment = "ext/ShortestPath/node/" +CyUtils.getNeoID(from)+ "/shortestPath";
		String payload = "{\"target\":\""+instanceLocation+"node/1315\",\"depth\":"+getDepth()+"}";
		return new Neo4jCall(urlFragment, payload);
	}

	@Override
	public void processRetValue(Object callRetValue) {
		List<Map<String,Object>> paths = (List<Map<String,Object>>)callRetValue;
		CyNetwork net = from.getNetworkPointer();
		CyTable edgeTab = net.getDefaultEdgeTable();
		
		Set<CyNode> nodesToSelect = new HashSet<CyNode>();
		Set<CyEdge> edgesToSelect = new HashSet<CyEdge>();
		
		nodesToSelect.add(getFrom());
		nodesToSelect.add(getTo());
		
		for(Map<String,Object> p : paths){
			List<String> rels = (List<String>)p.get("relationships");
			for(String rel : rels){
				Long relID = NeoUtils.extractID(rel);
				Set<CyEdge> es = CyUtils.getEdgeWithValue(net, edgeTab, "neoid", relID);
				if(es.size() != 1){
//					error!!
				}
				
				CyEdge e = es.iterator().next();
				nodesToSelect.add(e.getSource());
				nodesToSelect.add(e.getTarget());
				edgesToSelect.add(e);
				
			}
		}
		
		CyTable nodeTab = net.getDefaultNodeTable();
		for(CyNode n : nodesToSelect){
			nodeTab.getRow(n.getSUID()).set(CyNetwork.SELECTED, true);
		}
		for(CyEdge e : edgesToSelect){
			edgeTab.getRow(e.getSUID()).set(CyNetwork.SELECTED, true);
		}
	}

	protected Extension getExtension() {
		return extension;
	}

	public void setExtension(Extension extension) {
		this.extension = extension;
	}

	protected CyNode getFrom() {
		return from;
	}

	protected void setFrom(CyNode from) {
		this.from = from;
	}

	protected CyNode getTo() {
		return to;
	}

	protected void setTo(CyNode to) {
		this.to = to;
	}

	protected Integer getDepth() {
		return depth;
	}

	protected void setDepth(Integer depth) {
		this.depth = depth;
	}
	
	
}
