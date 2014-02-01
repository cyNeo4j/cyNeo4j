package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.ResponseHandlers;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.codehaus.jackson.map.ObjectMapper;

public class Neo4jPingHandler implements ResponseHandler<Boolean> {

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