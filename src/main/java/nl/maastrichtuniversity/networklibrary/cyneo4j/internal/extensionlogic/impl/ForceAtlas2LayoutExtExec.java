package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JFrame;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ContinuiousExtensionExecutor;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.Extension;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionCall;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jCall;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.utils.CyUtils;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;

public class ForceAtlas2LayoutExtExec implements ContinuiousExtensionExecutor {

	public static final int IterationStepSize = 10;
	
	private Plugin plugin;
	private Extension extension;
	private CyNetwork currNet;
	
	private boolean runIt = false;
	private int numRuns = 0;
	
	
	private Map<String,Object> params;

	public ForceAtlas2LayoutExtExec() {
		params = new HashMap<String, Object>();
		
		// default parameters
		params.put("dissuadeHubs", false);
		params.put("linLogMode", false);
		params.put("preventOverlap", false);
		params.put("edgeWeightInfluence", 1.0);
		
		params.put("scaling", 10.0);
		params.put("strongGravityMode", false);
		params.put("gravity", 1.0);
		
		params.put("tolerance", 0.1);
		params.put("approxRepulsion", false);
		params.put("approx", 1.2);
		
		params.put("saveInGraph", true);
		params.put("numIterations", 1000);
	}

	@Override
	public boolean collectParameters() {	
		currNet = getPlugin().getCyApplicationManager().getCurrentNetwork();

		JDialog dialog = new JDialog(plugin.getCySwingApplication().getJFrame());
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		ForceAtlas2LayoutControlPanel controls = new ForceAtlas2LayoutControlPanel(dialog,params);
		controls.setOpaque(true);
		dialog.setModal(true);
		dialog.setContentPane(controls);
		dialog.setResizable(false);

		dialog.pack();
		dialog.setVisible(true);

		runIt = controls.runIt();
		
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

		if(runIt){
			
			String urlFragment = extension.getEndpoint();

			ObjectMapper mapper = new ObjectMapper();
			String payload;
			try {		
				int numIterations = (Integer)params.get("numIterations");
				
				while(numIterations > 0){
				
					int itersToDo = IterationStepSize;
					
					if(numIterations < IterationStepSize)
						itersToDo = numIterations;
					
					numIterations -= IterationStepSize;
					
					Map<String,Object> callParams = new HashMap<String,Object>(params);

					callParams.put("numIterations", itersToDo);
					callParams.put("pickup",numRuns>0);
				
					payload = mapper.writeValueAsString(callParams);
					calls.add(new Neo4jCall(urlFragment,payload,false));
					++numRuns;
				}
			} catch (JsonGenerationException e) {
				System.out.println("payload generation failed" + e);
			} catch (JsonMappingException e) {
				System.out.println("payload generation failed" + e);
			} catch (IOException e) {
				System.out.println("payload generation failed" + e);
			}
		}

		return calls;
	}
	

	@Override
	public boolean doContinue() {
		return runIt;
	}
}
