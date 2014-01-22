package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.ResponseHandlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.codehaus.jackson.map.ObjectMapper;

public class GetExtensionHandler implements ResponseHandler<List<String>>{

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