package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.codehaus.jackson.map.ObjectMapper;

public class IdListHandler implements ResponseHandler<List<Long>> {

	@Override
	public List<Long> handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		
		int responseCode = response.getStatusLine().getStatusCode();
		
		List<Long> ids = null;

		System.out.println("responseCode: " + responseCode);
		if(responseCode >= 200 && responseCode < 300){

			ObjectMapper mapper = new ObjectMapper();
			Map<String,Object> wrapper = mapper.readValue(response.getEntity().getContent(), Map.class);
			
			List<List<Integer>> queryRes = (List<List<Integer>>)wrapper.get("data");
			ids = new ArrayList<Long>();
			
			for(List<Integer> id : queryRes){
				ids.add(id.get(0).longValue());
			}
		} else {
			System.out.println("ERROR " + responseCode);
			ObjectMapper mapper = new ObjectMapper();

			Map<String,String> error = mapper.readValue(response.getEntity().getContent(),Map.class);
			System.out.println(error);
		}

		return ids;
	}

}
