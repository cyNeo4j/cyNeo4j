package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider.sync;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.utils.CyUtils;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.layout.CyLayoutAlgorithm;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.model.View;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

public class SyncDownTask extends AbstractTask{

	private boolean mergeInCurrent;
	private String cypherURL;
	private String instanceLocation;
	private CyNetworkFactory cyNetworkFactory;
	private CyNetworkManager cyNetworkMgr;
	private CyNetworkViewManager cyNetworkViewMgr;
	private CyNetworkViewFactory cyNetworkViewFactory;
	private CyLayoutAlgorithmManager cyLayoutAlgorithmMgr;
	private VisualMappingManager visualMappingMgr;

	public SyncDownTask(boolean mergeInCurrent, String cypherURL,
			String instanceLocation, CyNetworkFactory cyNetworkFactory,
			CyNetworkManager cyNetworkMgr,
			CyNetworkViewManager cyNetworkViewMgr,
			CyNetworkViewFactory cyNetworkViewFactory,
			CyLayoutAlgorithmManager cyLayoutAlgorithmMgr,
			VisualMappingManager visualMappingMgr) {
		super();
		this.mergeInCurrent = mergeInCurrent;
		this.cypherURL = cypherURL;
		this.instanceLocation = instanceLocation;
		this.cyNetworkFactory = cyNetworkFactory;
		this.cyNetworkMgr = cyNetworkMgr;
		this.cyNetworkViewMgr = cyNetworkViewMgr;
		this.cyNetworkViewFactory = cyNetworkViewFactory;
		this.cyLayoutAlgorithmMgr = cyLayoutAlgorithmMgr;
		this.visualMappingMgr = visualMappingMgr;
	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		if(mergeInCurrent){

		} else {
			try {
				String query = "{ \"query\" : \"MATCH (n) RETURN n\",\"params\" : {}}";

				taskMonitor.setTitle("Synchronizing the remote network DOWN");
				
				taskMonitor.setStatusMessage("Downloading nodes");
				SyncDownNodeResponseHandler nodeSyncHandler = new SyncDownNodeResponseHandler(instanceLocation, cyNetworkFactory,cyNetworkMgr);
				CyNetwork network = Request.Post(cypherURL).bodyString(query, ContentType.APPLICATION_JSON).execute().handleResponse(nodeSyncHandler);
				
				// In a dialog?
				if(network == null){
					System.out.println("an error occured while downloading the nodes:");
					System.out.println(nodeSyncHandler.getErrors());
				}
				taskMonitor.setProgress(0.3);
				
				query = "{ \"query\" : \"MATCH (n)-[r]->(m) RETURN r\",\"params\" : {}}";
				taskMonitor.setStatusMessage("Downloading edges");
				SyncDownEdgeResponseHandler edgeSyncHandler = new SyncDownEdgeResponseHandler(network);
				Long edgeRes = Request.Post(cypherURL).bodyString(query, ContentType.APPLICATION_JSON).execute().handleResponse(edgeSyncHandler);
				
				if(edgeRes == null){
					System.out.println("an error occured while downloading the edges:");
					System.out.println(edgeSyncHandler.getErrors());
				}
				
				taskMonitor.setProgress(0.9);
				
				taskMonitor.setStatusMessage("Creating View");
				
				Collection<CyNetworkView> views = cyNetworkViewMgr.getNetworkViews(network);
				CyNetworkView view;
				if(!views.isEmpty()) {
					view = views.iterator().next();
				} else {
					view = cyNetworkViewFactory.createNetworkView(network);
					cyNetworkViewMgr.addNetworkView(view);
				}
				taskMonitor.setProgress(0.8);
				taskMonitor.setStatusMessage("Applying Layout");
				
				Set<View<CyNode>> nodes = new HashSet<View<CyNode>>();
				CyLayoutAlgorithm layout = cyLayoutAlgorithmMgr.getLayout("force-directed");
				insertTasksAfterCurrentTask(layout.createTaskIterator(view, layout.createLayoutContext(), nodes, null));
				
				CyUtils.updateVisualStyle(visualMappingMgr, view, network);
				
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
