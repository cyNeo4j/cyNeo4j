package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JFrame;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.Plugin;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.ContinuiousExtensionExecutor;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.Extension;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider.Neo4jCall;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.utils.CyUtils;

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
	
	private boolean runIt;
	private int numRuns = 0;
	
	private JDialog dialog = null;
	private ForceAtlas2LayoutControlPanel controls = null;

	public ForceAtlas2LayoutExtExec() {
	}

	@Override
	public boolean collectParameters() {
		currNet = getPlugin().getCyApplicationManager().getCurrentNetwork();

		if(dialog == null)
			setupDialog();

		dialog.setVisible(true);

		controls.getParameters();
		controls.getNumIterations();
		runIt = controls.runIt();

		return true;
	}

	private void setupDialog(){
		dialog = new JDialog(plugin.getCySwingApplication().getJFrame());
		dialog.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		controls = new ForceAtlas2LayoutControlPanel(dialog);
		controls.setOpaque(true);
		dialog.setModal(true);
		dialog.setContentPane(controls);
		dialog.setResizable(false);

		dialog.pack();
	}

	private Plugin getPlugin() {
		return plugin;
	}

	@Override
	public void processCallResponse(Neo4jCall call, Object callRetValue) {

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
	public List<Neo4jCall> buildNeo4jCalls() {
		List<Neo4jCall> calls = new ArrayList<Neo4jCall>();

		if(runIt){
			
			String urlFragment = extension.getEndpoint();
			
//			String payload = "{\"numIterations\":\"10\",\"saveInGraph\":false,\"pickup\":false}";
			ObjectMapper mapper = new ObjectMapper();
			String payload;
			try {
				
				
				
				int numIterations = controls.getNumIterations();
				
				while(numIterations > 0){
				
					int itersToDo = IterationStepSize;
					
					if(numIterations < IterationStepSize)
						itersToDo = numIterations;
					
					numIterations -= IterationStepSize;
					
					Map<String,Object> params = new HashMap<String,Object>(controls.getParameters());
					params.put("saveInGraph", true);
					params.put("numIterations", itersToDo);
					params.put("pickup",numRuns>0);
				
					payload = mapper.writeValueAsString(params);
					calls.add(new Neo4jCall(urlFragment,payload));
					++numRuns;
				}
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
	

	@Override
	public boolean doContinue() {
		return runIt;
	}
}
