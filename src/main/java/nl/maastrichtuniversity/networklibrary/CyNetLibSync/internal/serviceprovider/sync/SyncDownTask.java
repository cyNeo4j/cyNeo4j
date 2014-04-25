package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider.sync;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

public class SyncDownTask extends AbstractTask{

	private boolean mergeInCurrent;
	private String cypherURL;
	private String instanceLocation;
	private CyNetworkFactory cyNetworkFactory;
	private CyNetworkManager cyNetworkMgr;
//	private CyNetworkViewManager cyNetworkViewMgr;
//	private CyNetworkViewFactory cyNetworkViewFactory;
//	private CyLayoutAlgorithmManager cyLayoutAlgorithmMgr;
//	private VisualMappingManager visualMappingMgr;
//	
	public SyncDownTask(boolean mergeInCurrent, String cypherURL,
			String instanceLocation, CyNetworkFactory cyNetworkFactory,
			CyNetworkManager cyNetworkMgr/*,
			CyNetworkViewManager cyNetworkViewMgr,
			CyNetworkViewFactory cyNetworkViewFactory,
			CyLayoutAlgorithmManager cyLayoutAlgorithmMgr,
			VisualMappingManager visualMappingMgr*/) {
		super();
		this.mergeInCurrent = mergeInCurrent;
		this.cypherURL = cypherURL;
		this.instanceLocation = instanceLocation;
		this.cyNetworkFactory = cyNetworkFactory;
		this.cyNetworkMgr = cyNetworkMgr;
//		this.cyNetworkViewMgr = cyNetworkViewMgr;
//		this.cyNetworkViewFactory = cyNetworkViewFactory;
//		this.cyLayoutAlgorithmMgr = cyLayoutAlgorithmMgr;
//		this.visualMappingMgr = visualMappingMgr;
	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		if(mergeInCurrent){

		} else {
			try {
				String query = "{ \"query\" : \"MATCH (n) RETURN n\",\"params\" : {}}";
//				System.out.println("loc: " + getInstanceLocation() + CYPHER_URL + " query: " + query);

				taskMonitor.setTitle("Synchronizing the remote network DOWN");
				
				taskMonitor.setStatusMessage("Downloading nodes");
				CyNetwork network = Request.Post(cypherURL).bodyString(query, ContentType.APPLICATION_JSON).execute().handleResponse(new SyncDownNodeResponseHandler(instanceLocation, cyNetworkFactory,cyNetworkMgr));
				taskMonitor.setProgress(0.3);
				
				query = "{ \"query\" : \"MATCH (n)-[r]->(m) RETURN r\",\"params\" : {}}";
//				System.out.println("loc: " + getInstanceLocation() + CYPHER_URL + " query: " + query);
				taskMonitor.setStatusMessage("Downloading edges");
				Request.Post(cypherURL).bodyString(query, ContentType.APPLICATION_JSON).execute().handleResponse(new SyncDownEdgeResponseHandler(network));
				taskMonitor.setProgress(1.0);
				
//				taskMonitor.setStatusMessage("Creating View");
//				
//				Collection<CyNetworkView> views = cyNetworkViewMgr.getNetworkViews(network);
//				CyNetworkView view;
//				if(!views.isEmpty()) {
//					view = views.iterator().next();
//				} else {
//					view = cyNetworkViewFactory.createNetworkView(network);
//					cyNetworkViewMgr.addNetworkView(view);
//				}
//				taskMonitor.setProgress(0.8);
//				taskMonitor.setStatusMessage("Applying Layout");
//				
//				Set<View<CyNode>> nodes = new HashSet<View<CyNode>>();
//				CyLayoutAlgorithm layout = cyLayoutAlgorithmMgr.getLayout("force-directed");
//				insertTasksAfterCurrentTask(layout.createTaskIterator(view, layout.createLayoutContext(), nodes, null));
//				
//				CyUtils.updateVisualStyle(visualMappingMgr, view, network);
				
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
