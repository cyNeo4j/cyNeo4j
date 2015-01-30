package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.Extension;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionCall;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionExecutor;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jCall;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.utils.CyUtils;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.utils.NeoUtils;

import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.CyTableUtil;
import org.cytoscape.view.model.CyNetworkView;

public class ShortestPathExtExec implements ExtensionExecutor {
	
	private Plugin plugin;
	private Extension extension;
	private CyNode from;
	private CyNode to;
	private CyNetwork net;
	private Integer depth = 10000;
	
	public ShortestPathExtExec(){
	}
	
	@Override
	public boolean collectParameters() {
		CyNetwork currNet = getPlugin().getCyApplicationManager().getCurrentNetwork();
		List<CyNode> selectedNodes = CyTableUtil.getNodesInState(currNet, "selected", true);
		
		if(selectedNodes.size() == 2){
			setFrom(selectedNodes.get(0));
			setTo(selectedNodes.get(1));
			setNet(currNet);
		} else {
			JOptionPane.showMessageDialog(plugin.getCySwingApplication().getJFrame(), "Please select only two nodes!");
		}
		
		return from != null && to != null && depth != null && net != null;
	}

	@Override
	public List<ExtensionCall> buildExtensionCalls() {
		String instanceLocation = getExtension().getEndpoint();
//		curl -X POST http://localhost:7474/db/data/ext/ShortestPath/node/1311/shortestPath -H "Content-Type: application/json" -d '{"target":"http://localhost:7474/db/data/node/1315","depth":5}'
		String urlFragment = "ShortestPath/node/" +CyUtils.getNeoID(getNet(), from)+ "/shortestPath";
		String payload = "{\"target\":\""+instanceLocation+"node/"+CyUtils.getNeoID(getNet(), to)+"\",\"depth\":"+getDepth()+"}";
		
		List<ExtensionCall> calls = new ArrayList<ExtensionCall>();
		calls.add(new Neo4jCall(urlFragment, payload));
		
		urlFragment = "ShortestPath/node/" +CyUtils.getNeoID(getNet(), to)+ "/shortestPath";
		payload = "{\"target\":\""+instanceLocation+"node/"+CyUtils.getNeoID(getNet(), from)+"\",\"depth\":"+getDepth()+"}";
		calls.add(new Neo4jCall(urlFragment, payload));
		
		return calls;
	}

	@Override
	public void processCallResponse(ExtensionCall call, Object callRetValue) {
		List<Map<String,Object>> paths = (List<Map<String,Object>>)callRetValue;

		CyTable edgeTab = getNet().getDefaultEdgeTable();
		
		Set<CyNode> nodesToSelect = new HashSet<CyNode>();
		Set<CyEdge> edgesToSelect = new HashSet<CyEdge>();
		
		nodesToSelect.add(getFrom());
		nodesToSelect.add(getTo());
		
		for(Map<String,Object> p : paths){
			List<String> rels = (List<String>)p.get("relationships");
			for(String rel : rels){
				Long relID = NeoUtils.extractID(rel);
				Set<CyEdge> es = CyUtils.getEdgeWithValue(getNet(), edgeTab, "neoid", relID);
				if(es.size() != 1){
//					error!!
				}
				
				CyEdge e = es.iterator().next();
				nodesToSelect.add(e.getSource());
				nodesToSelect.add(e.getTarget());
				edgesToSelect.add(e);
				
			}
		}
		
		System.out.println("have to highlight: " + nodesToSelect.size() + " nodes and " + edgesToSelect.size() + " edges!");
		
		CyTable nodeTab = getNet().getDefaultNodeTable();
		for(CyNode n : nodesToSelect){
			nodeTab.getRow(n.getSUID()).set(CyNetwork.SELECTED, true);
		}
		for(CyEdge e : edgesToSelect){
			edgeTab.getRow(e.getSUID()).set(CyNetwork.SELECTED, true);
		}
		
		Collection<CyNetworkView> views = getPlugin().getCyNetViewMgr().getNetworkViews(getNet());
		System.out.println("views available: " + views.size());
		for(CyNetworkView view : views){
			view.updateView();
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

	@Override
	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}
	
	public Plugin getPlugin() {
		return plugin;
	}

	@Override
	public String toString() {
		return "ShortestPathExtExec [extension=" + extension + ", from=" + from
				+ ", to=" + to + ", depth=" + depth + "]";
	}

	protected CyNetwork getNet() {
		return net;
	}

	protected void setNet(CyNetwork net) {
		this.net = net;
	}
}
