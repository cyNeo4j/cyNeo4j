package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.Extension;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionCall;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionExecutor;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jCall;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.utils.CyUtils;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;

public class SimpleLayoutExtExec implements ExtensionExecutor {

	private Plugin plugin;
	private Extension extension;
	private CyNetwork currNet;
	
	public SimpleLayoutExtExec() {
	}

	@Override
	public boolean collectParameters() {
		currNet = getPlugin().getCyApplicationManager().getCurrentNetwork();
		return true;
	}

	private Plugin getPlugin() {
		return plugin;
	}

	@Override
	public void processCallResponse(ExtensionCall call, Object callRetValue) {
		
		List<Double> values = (List<Double>)callRetValue;
		
		CyTable defNodeTab = currNet.getDefaultNodeTable();
		CyNetworkView networkView = getPlugin().getCyNetViewMgr().getNetworkViews(currNet).iterator().next();
		
		for(int i = 0; i < (values.size() / 3); ++i){
			Long neoid = values.get(i*3).longValue();
			Double x = values.get(i*3+1);
			Double y = values.get(i*3+2);
			
			
			Set<CyNode> nodeSet = CyUtils.getNodesWithValue(currNet, defNodeTab, "neoid", neoid);
			CyNode n = nodeSet.iterator().next();
			
			View<CyNode> nodeView = networkView.getNodeView(n);
			nodeView.setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, x);
			nodeView.setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, y);
			
			CyUtils.updateVisualStyle(getPlugin().getVisualMappingManager(), networkView, currNet);
		}
		

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
	public List<ExtensionCall> buildExtensionCalls() {
		List<ExtensionCall> calls = new ArrayList<ExtensionCall>();
		
		String urlFragment = extension.getEndpoint();
		String payload = "{\"saveInGraph\":false}";
		
		calls.add(new Neo4jCall(urlFragment,payload));
		
		return calls;
	}
}
