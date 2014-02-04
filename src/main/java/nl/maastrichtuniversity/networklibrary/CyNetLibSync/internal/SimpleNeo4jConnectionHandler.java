package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.ResponseHandlers.ExtensionLocationsHandler;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.ResponseHandlers.ExtensionParametersResponseHandler;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.ResponseHandlers.Neo4jPingHandler;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.ResponseHandlers.PassThroughResponseHandler;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.ResponseHandlers.ReturnCodeResponseHandler;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.ResponseHandlers.SyncDownEdgeResponseHandler;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.ResponseHandlers.SyncDownNodeResponseHandler;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensions.Extension;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.utils.CyUtils;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.utils.Neo4jCall;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;

public class SimpleNeo4jConnectionHandler implements Neo4jInteractor {

	private String instanceLocation = null;
	private Plugin plugin = null;

	private static final String DATA_URL = "/db/data/";
	private static final String EXT_URL = DATA_URL + "ext/";
	private static final String CYPHER_URL = DATA_URL + "cypher";
	private static final String TRANSACTION_URL = DATA_URL + "transaction";

	public SimpleNeo4jConnectionHandler(Plugin plugin) {
		super();
		this.plugin = plugin;
	}

	protected Plugin getPlugin() {
		return plugin;
	}

	public boolean connectToInstance(String location) {
		setInstanceLocation(location);

		return pingServer(getInstanceLocation());
	}
	
	public boolean isConnected() {
		return pingServer(getInstanceLocation());
	}

	private boolean pingServer(String loc){	
		try {
			return sanityCheckUrl(loc) && Request.Get(loc).execute().handleResponse(new Neo4jPingHandler());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		};
		return false;
	}

	public List<Extension> getExtensions(){

		List<Extension> res = new ArrayList<Extension>();

		try {
			Set<String> extNames = Request.Get(getInstanceLocation() + EXT_URL).execute().handleResponse(new ExtensionLocationsHandler());
			
			for(String extName : extNames){
				res.addAll(Request.Get(getInstanceLocation() + EXT_URL + extName).execute().handleResponse(new ExtensionParametersResponseHandler()));
			}
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return res;
	}

	private boolean sanityCheckUrl(String url){
		// TODO implement me
		return true;
	}

	public String getInstanceLocation() {
		return instanceLocation;
	}

	public void setInstanceLocation(String instanceLocation) {
		this.instanceLocation = instanceLocation;
	}
	
	public String getInstanceDataLocation() {
		return instanceLocation + DATA_URL;
	}

	public void syncDown(boolean mergeInCurrent) {
		if(mergeInCurrent){

		} else {
			try {
				String query = "{ \"query\" : \"MATCH (n) RETURN n\",\"params\" : {}}";
				System.out.println("loc: " + getInstanceLocation() + CYPHER_URL + " query: " + query);

				Long resNetSUID = Request.Post(getInstanceLocation() + CYPHER_URL).bodyString(query, ContentType.APPLICATION_JSON).execute().handleResponse(new SyncDownNodeResponseHandler(getPlugin()));

				query = "{ \"query\" : \"MATCH (n)-[r]->(m) RETURN r\",\"params\" : {}}";
				System.out.println("loc: " + getInstanceLocation() + CYPHER_URL + " query: " + query);
				Request.Post(getInstanceLocation() + CYPHER_URL).bodyString(query, ContentType.APPLICATION_JSON).execute().handleResponse(new SyncDownEdgeResponseHandler(getPlugin(),resNetSUID));

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void syncUp(boolean wipeRemote) {
		// case 1: create new network -> requires purge of existing if anything is there
		// case 2: make updates. (merge or create unique)
		boolean wiped = false;
		if(wipeRemote){
			String wipeQuery = "{ \"query\" : \"MATCH (n) OPTIONAL MATCH (n)-[r]-() DELETE n,r\",\"params\" : {}}";
			System.out.println(wipeQuery);
			try {
				wiped = Request.Post(getInstanceLocation() + CYPHER_URL).bodyString(wipeQuery, ContentType.APPLICATION_JSON).execute().handleResponse(new ReturnCodeResponseHandler());
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		System.out.println("remote network wipe done");
		
		if(wiped == wipeRemote){
			CyNetwork currNet = getPlugin().getCyApplicationManager().getCurrentNetwork();
			
			if(currNet == null){
				System.out.println("no network selected!");
				
			} else {
					
				System.out.println("got a network");
				//TODO get all tables; check if node is in table; upload everything!
				CyTable defNodeTab = currNet.getDefaultNodeTable();
				
				for(CyNode node : currNet.getNodeList()){
					
					String params = CyUtils.convertCyAttributesToJson(node, defNodeTab);
					String cypher = "{ \"query\" : \"CREATE (n { props })\", \"params\" : {   \"props\" : [ "+ params +" ] } }";
//					System.out.println(cypher);
					try {
						Request.Post(getInstanceLocation() + CYPHER_URL).bodyString(cypher, ContentType.APPLICATION_JSON).execute().handleResponse(new ReturnCodeResponseHandler());
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// */
				
				System.out.println("uploaded nodes");
				
				CyTable defEdgeTab = currNet.getDefaultEdgeTable();
				
				for(CyEdge edge : currNet.getEdgeList()){
					String from = defNodeTab.getRow(edge.getSource().getSUID()).get(CyNetwork.NAME, String.class);
					String to = defNodeTab.getRow(edge.getTarget().getSUID()).get(CyNetwork.NAME, String.class);
					
					String rparams = CyUtils.convertCyAttributesToJson(edge, defEdgeTab);
					
					String rtype = defEdgeTab.getRow(edge.getSUID()).get(CyEdge.INTERACTION, String.class);
					
					String cypher = "{\"query\" : \"MATCH (from { name: {fname}}),(to { name: {tname}}) CREATE (from)-[r:"+rtype+" { rprops } ]->(to) return r\", \"params\" : { \"fname\" : \""+from+"\", \"tname\" : \""+to+"\", \"rprops\" : "+ rparams +" }}";
//					System.out.println(cypher);
					try {
						Request.Post(getInstanceLocation() + CYPHER_URL).bodyString(cypher, ContentType.APPLICATION_JSON).execute().handleResponse(new ReturnCodeResponseHandler());
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				// */
				
				System.out.println("uploaded edges");
			}
			
		} else {
			System.out.println("could not wipe the instance! aborting syncUp");
		}
		// */

	}

	@Override
	public Object executeExtensionCall(Neo4jCall call) {
		Object retVal = null;
		try {
			retVal = Request.Post(getInstanceLocation() + call.getUrlFragment()).bodyString(call.getPayload(), ContentType.APPLICATION_JSON).execute().handleResponse(new PassThroughResponseHandler());
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return retVal;
	}

}
