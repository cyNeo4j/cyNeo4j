package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JFrame;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.Extension;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionCall;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionExecutor;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jCall;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.utils.CyUtils;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.cytoscape.model.CyColumn;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;

public class NeoNetworkAnalyzerExec implements ExtensionExecutor {

	private Plugin plugin;
	private Extension extension;
	private CyNetwork currNet;

	private boolean run;
	private boolean saveInGraph;
	private boolean doAsync;
	private boolean undirected;

	// to calculate
	private boolean eccentricity;
	private boolean betweenness;
	private boolean stress;
	private boolean avgSp;
	private boolean topoCoeff;
	private boolean radiality;
	private boolean neighbourhood;
	private boolean multiEdgePairs;
	private boolean closeness;
	private boolean clustCoeff;

	public NeoNetworkAnalyzerExec() {

	}

	@Override
	public boolean collectParameters() {
		currNet = getPlugin().getCyApplicationManager().getCurrentNetwork();

		JDialog dialog = new JDialog(plugin.getCySwingApplication().getJFrame());
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		NeoNetworkAnalyzerControlPanel p = new NeoNetworkAnalyzerControlPanel(dialog);
		p.setOpaque(true);
		dialog.setModal(true);
		dialog.setContentPane(p);
		dialog.setResizable(false);

		dialog.pack();
		dialog.setVisible(true);

		run = p.runIt();
		saveInGraph = p.isSaveInGraph();
		doAsync = p.isDoAsync();
		undirected = p.isUndirected();

		eccentricity = p.isEccentricity();
		stress = p.isStress();
		betweenness = p.isBetweenness();
		avgSp = p.isAvgSP();
		radiality = p.isRadiality();
		topoCoeff = p.isTopoCoeff();
		neighbourhood = p.isNeighbourhoodConn();
		multiEdgePairs = p.isMultiEdgePairs();
		closeness = p.isCloseness();
		clustCoeff = p.isClustCoeff();

		return true;
	}

	@Override
	public void processCallResponse(ExtensionCall call, Object callRetValue) {

		if(currNet != null){

			List<Object> allStats = (List<Object>)callRetValue;

			ObjectMapper mapper = new ObjectMapper();
			CyTable defNodeTab = currNet.getDefaultNodeTable();

			try {
				for(Object obj : allStats){
					Map<String, Object> stats = mapper.readValue((String)obj, Map.class);
					Long neoid = ((Integer)stats.get("nodeid")).longValue();

					Set<CyNode> nodeSet = CyUtils.getNodesWithValue(currNet, defNodeTab, "neoid", neoid);

					if(nodeSet != null && nodeSet.size() > 0){
						CyNode n = nodeSet.iterator().next();

						if(n != null){

							for(Entry<String,Object> e : stats.entrySet()){

								if(e.getKey().equals("nodeid")){
									continue;
								}

								addValue(n,defNodeTab,e.getKey(),e.getValue());

							}
						}
					}
				}

			} catch (JsonParseException e1) {
				e1.printStackTrace();
			} catch (JsonMappingException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void addValue(CyNode n, CyTable defNodeTab, String key, Object value) {
		if(defNodeTab.getColumn(key) == null){
			defNodeTab.createColumn(key, value.getClass(), false);
		}
		Object val2 = value;
		
		CyColumn col = defNodeTab.getColumn(key);
		if(!value.getClass().equals(col.getType())){
			val2 = col.getType().cast(value);
		}

		defNodeTab.getRow(n.getSUID()).set(key, val2);
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

		if(run){
			String urlFragment = extension.getEndpoint();

			Map<String,Object> params = new HashMap<String,Object>();
			params.put("saveInGraph",saveInGraph);
			params.put("eccentricity",eccentricity);
			params.put("betweenness",betweenness);
			params.put("stress",stress);
			params.put("avgSP",avgSp);
			params.put("radiality",radiality);
			params.put("topoCoeff",topoCoeff);
			params.put("neighbourhood",neighbourhood);
			params.put("multiEdgePairs",multiEdgePairs);
			params.put("closeness",closeness);
			params.put("clustCoeff",clustCoeff);

			ObjectMapper mapper = new ObjectMapper();
			String payload;
			try {
				payload = mapper.writeValueAsString(params);
				calls.add(new Neo4jCall(urlFragment,payload,doAsync));
			} catch (JsonGenerationException e) {
				System.out.println("payload generation failed");
				e.printStackTrace();
			} catch (JsonMappingException e) {
				System.out.println("payload generation failed");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("payload generation failed");
				e.printStackTrace();
			}

		}

		return calls;
	}

	protected Plugin getPlugin() {
		return plugin;
	}
}
