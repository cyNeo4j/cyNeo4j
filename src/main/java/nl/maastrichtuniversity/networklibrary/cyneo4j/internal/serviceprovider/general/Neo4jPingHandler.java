package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.general;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.codehaus.jackson.map.ObjectMapper;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.Neo4jServer.ServerMessage;

public class Neo4jPingHandler implements ResponseHandler<ServerMessage> {
	
	@Override
	public ServerMessage handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {

		int responseCode = response.getStatusLine().getStatusCode();
		System.out.println(responseCode);
		
		if(responseCode == 401){
			ObjectMapper mapper = new ObjectMapper();
			Map<String,String> instanceResp = mapper.readValue(response.getEntity().getContent(),Map.class);
			
			return ServerMessage.AUTH_FAILURE;
		}
		
		if(responseCode >= 200 && responseCode < 300){
			ObjectMapper mapper = new ObjectMapper();
			Map<String,String> instanceResp = mapper.readValue(response.getEntity().getContent(),Map.class);
			
//			if(instanceResp.containsKey("node"))
//				return true;
			return ServerMessage.CONNECT_SUCCESS;
		}
		
		return ServerMessage.CONNECT_FAILED;
	}
}