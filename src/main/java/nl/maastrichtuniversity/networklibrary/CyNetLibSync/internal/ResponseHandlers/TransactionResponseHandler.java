package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.ResponseHandlers;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.codehaus.jackson.map.ObjectMapper;

public class TransactionResponseHandler implements ResponseHandler<String> {

	@Override
	public String handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {

//		{
//			  "commit": "http:\/\/localhost:7474\/db\/data\/transaction\/13\/commit",
//			  "results": [
//			    
//			  ],
//			  "transaction": {
//			    "expires": "Tue, 28 Jan 2014 14:02:29 +0000"
//			  },
//			  "errors": [
//			    
//			  ]
//			}
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String,Object> transaction = mapper.readValue(response.getEntity().getContent(), Map.class);
		
		for(Entry<String, Object> e : transaction.entrySet()){
			System.out.println(e.getKey() + " -> " + e.getValue().toString());
		}
		
		return null;
	}

}
