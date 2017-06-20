package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.codehaus.jackson.map.ObjectMapper;

public class CreateIdReturnResponseHandler implements ResponseHandler<Long> {

	@Override
	public Long handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		int responseCode = response.getStatusLine().getStatusCode();

		Long id = null;

		System.out.println("responseCode: " + responseCode);
		if(responseCode >= 200 && responseCode < 300){

			ObjectMapper mapper = new ObjectMapper();
			Map<String,Object> wrapper = mapper.readValue(response.getEntity().getContent(), Map.class);
			
			System.out.println(wrapper);
			List<List<Integer>> queryRes = (List<List<Integer>>)wrapper.get("data");
			
			id = new Long(queryRes.get(0).get(0).longValue());

		} else {
			System.out.println("ERROR " + responseCode);
			ObjectMapper mapper = new ObjectMapper();

			Map<String,String> error = mapper.readValue(response.getEntity().getContent(),Map.class);
			System.out.println(error);
		}

		return id;
	}

}
