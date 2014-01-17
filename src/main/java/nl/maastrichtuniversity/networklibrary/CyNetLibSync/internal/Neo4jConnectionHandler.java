package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.codehaus.jackson.map.ObjectMapper;


public class Neo4jConnectionHandler {

	private String instanceLocation = null;
	
	private static final String DATA_URL = "/db/data/";
	private static final String EXT_URL = DATA_URL + "ext/";
	private static final String CYPHER_URL = DATA_URL + "cypher";
	
	
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
			Request.Post(getInstanceLocation()).bodyString("", ContentType.APPLICATION_JSON).execute().handleResponse(new SyncDownResponseHandler());
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	class GetExtensionHandler implements ResponseHandler<List<String>>{

		@Override
		public List<String> handleResponse(HttpResponse response)
				throws ClientProtocolException, IOException {
			List<String> res = null;
			int responseCode = response.getStatusLine().getStatusCode();
			if(responseCode >= 200 && responseCode < 300){
				ObjectMapper mapper = new ObjectMapper();
				Map<String,String> instanceResp = mapper.readValue(response.getEntity().getContent(),Map.class);
				
				res = new ArrayList<String>(instanceResp.keySet());
			}

			return res;
		}
		
	}

	class SyncDownResponseHandler implements ResponseHandler<Boolean> {

		@Override
		public Boolean handleResponse(HttpResponse arg0)
				throws ClientProtocolException, IOException {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	class Neo4jPingHandler implements ResponseHandler<Boolean> {

		@Override
		public Boolean handleResponse(HttpResponse response)
				throws ClientProtocolException, IOException {
			
			int responseCode = response.getStatusLine().getStatusCode();
			if(responseCode >= 200 && responseCode < 300){
				ObjectMapper mapper = new ObjectMapper();
				Map<String,String> instanceResp = mapper.readValue(response.getEntity().getContent(),Map.class);
				if(instanceResp.containsKey("data"))
					return true;
			}
			
			return false;
		}
		
	}
}
