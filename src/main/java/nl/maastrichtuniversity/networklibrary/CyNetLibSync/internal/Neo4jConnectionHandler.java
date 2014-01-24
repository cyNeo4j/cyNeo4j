package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.ResponseHandlers.GetExtensionHandler;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.ResponseHandlers.Neo4jPingHandler;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.ResponseHandlers.SyncDownEdgeResponseHandler;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.ResponseHandlers.SyncDownNodeResponseHandler;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
public class Neo4jConnectionHandler {

	private String instanceLocation = null;
	private Plugin plugin = null;

	private static final String DATA_URL = "/db/data/";
	private static final String EXT_URL = DATA_URL + "ext/";
	private static final String CYPHER_URL = DATA_URL + "cypher";

	public Neo4jConnectionHandler(Plugin plugin) {
		super();
		this.plugin = plugin;
	}

	protected Plugin getPlugin() {
		return plugin;
	}

	public boolean connect(String location) {
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

	public List<String> getExtensions(){

		List<String> res = null;

		try {
			res = Request.Get(getInstanceLocation() + EXT_URL).execute().handleResponse(new GetExtensionHandler());
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return res;
	}

	// TODO change properties into something more suitable?
	public Properties getExtensionParameters(String extension){
		return null;
	}

	// TODO identify proper parameters necessary for the call
	public void invokeExtension(Properties parameters){

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

	public void syncDown() {
		try {
			String query = "{ \"query\" : \"MATCH (n) RETURN n\",\"params\" : {}}";
			System.out.println("loc: " + getInstanceLocation() + CYPHER_URL + " query: " + query);
			
			Long resNetSUID = Request.Post(getInstanceLocation() + CYPHER_URL).bodyString(query, ContentType.APPLICATION_JSON).execute().handleResponse(new SyncDownNodeResponseHandler(getPlugin()));
			
			query = "{ \"query\" : \"MATCH (n)-[r]->(m) RETURN r\",\"params\" : {}}";
			System.out.println("loc: " + getInstanceLocation() + CYPHER_URL + " query: " + query);
			Request.Post(getInstanceLocation() + CYPHER_URL).bodyString(query, ContentType.APPLICATION_JSON).execute().handleResponse(new SyncDownEdgeResponseHandler(getPlugin(),resNetSUID));
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void syncUp() {
		// case 1: create new network -> requires purge of existing if anything is there
		// case 2: make updates. (merge or create unique)
		
		
	}

	
}
