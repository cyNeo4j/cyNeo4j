package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.Plugin;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.Extension;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.ExtensionExecutor;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider.Neo4jCall;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.utils.CyUtils;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;

public class GridLayoutExtExec implements ExtensionExecutor {

	private Plugin plugin;
	private Extension extension;
	private CyNetwork currNet;
	
	public GridLayoutExtExec() {
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
	public void processCallResponse(Neo4jCall call, Object callRetValue) {
		
		List<Integer> values = (List<Integer>)callRetValue;
		
		CyTable defNodeTab = currNet.getDefaultNodeTable();
		CyNetworkView networkView = getPlugin().getCyNetViewMgr().getNetworkViews(currNet).iterator().next();
		
		for(int i = 0; i < (values.size() / 3); ++i){
			Long neoid = values.get(i*3).longValue();
			Long x = values.get(i*3+1).longValue();
			Long y = values.get(i*3+2).longValue();
			
			
			Set<CyNode> nodeSet = CyUtils.getNodesWithValue(currNet, defNodeTab, "neoid", neoid);
			CyNode n = nodeSet.iterator().next();
			
			View<CyNode> nodeView = networkView.getNodeView(n);
			nodeView.setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, x.doubleValue());
			nodeView.setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, y.doubleValue());
			
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
	public List<Neo4jCall> buildNeo4jCalls() {
		List<Neo4jCall> calls = new ArrayList<Neo4jCall>();
		
		String urlFragment = extension.getEndpoint();
		String payload = "{}";
		
		calls.add(new Neo4jCall(urlFragment,payload));
		
		return calls;
	}

}
