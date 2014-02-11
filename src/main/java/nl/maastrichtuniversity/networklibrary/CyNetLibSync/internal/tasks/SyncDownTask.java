package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.tasks;

import java.io.IOException;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.ResponseHandlers.SyncDownEdgeResponseHandler;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.ResponseHandlers.SyncDownNodeResponseHandler;

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

	public SyncDownTask(boolean mergeInCurrent, String cypherURL,
			String instanceLocation, CyNetworkFactory cyNetworkFactory,
			CyNetworkManager cyNetworkMgr) {
		super();
		this.mergeInCurrent = mergeInCurrent;
		this.cypherURL = cypherURL;
		this.instanceLocation = instanceLocation;
		this.cyNetworkFactory = cyNetworkFactory;
		this.cyNetworkMgr = cyNetworkMgr;
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
				taskMonitor.setProgress(0.5);
				
				query = "{ \"query\" : \"MATCH (n)-[r]->(m) RETURN r\",\"params\" : {}}";
//				System.out.println("loc: " + getInstanceLocation() + CYPHER_URL + " query: " + query);
				taskMonitor.setStatusMessage("Downloading edges");
				Request.Post(cypherURL).bodyString(query, ContentType.APPLICATION_JSON).execute().handleResponse(new SyncDownEdgeResponseHandler(network));
				taskMonitor.setProgress(1.0);
				
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
